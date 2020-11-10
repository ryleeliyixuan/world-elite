<template>
    <div>
        <div>模拟面试</div>
        <el-input v-model="userName" placeholder="请输入用户名"></el-input>
        <el-input v-model="channelId" placeholder="请输入频道id"></el-input>
        <el-button @click="joinRoom">加入房间</el-button>
        <div class="local-video">
            <video autoplay playsinline ref="preview"></video>
        </div>
        <div>流状态：{{streamstate}}</div>
    </div>
</template>

<script>
    import "aliyun-webrtc-sdk"
    import "@/utils/rtcUtils"
    import {sha256} from "@/utils/sha256"

    export default {
        name: "InterviewPage",
        data() {
            return {
                userName: "test",
                channelId: 1,
                aliWebRTC: undefined,
                publisherList: [],
                streamstate:"",

            }
        },
        mounted() {
            this.aliWebRTC = new AliRtcEngine();
            /**
             * AliWebRTC isSupport检测
             */
            this.aliWebRTC.isSupport().then(re => {
                console.log(re);
                this.init();
            }).catch(error => {
                this.$message.error(error.message);
            })

        },
        methods: {
            init() {
                /**
                 * remote用户加入房间 onJoin
                 * 更新在线用户列表
                 */
                this.aliWebRTC.on("onJoin", (publisher) => {
                    if (publisher.userId) {
                        this.updateUserList();
                    }
                    //重置订阅状态
                    //默认订阅远端音频和视频大流，但需要调用subscribe才能生效
                    //这里取消默认订阅，根据需求进行订阅
                    this.aliWebRTC.configRemoteAudio(publisher.userId, false);
                    this.aliWebRTC.configRemoteCameraTrack(publisher.userId, false, false);
                    console.log(publisher.displayName + "加入房间");
                });
                /**
                 * remote流发布事件 onPublish
                 * 将该用户新增到推流列表
                 * 若该用户已存在推流列表，则进行状态更新
                 */
                this.aliWebRTC.on("onPublisher", (publisher) => {
                    console.log("onPublisher", publisher);
                    let index = this.publisherList.getIndexByProprety(publisher.userId, "userId");
                    if (index === -1) {
                        //新增
                        this.publisherList.push(publisher);
                    } else {
                        //流状态更新
                        this.updatePublisherStream(publisher, index);
                    }
                });

                /**
                 * remote流结束发布事件 onUnPublisher
                 * 推流列表删除该用户
                 * 移除用户视图
                 * 初始化订阅状态
                 */
                this.aliWebRTC.on("onUnPublisher", (publisher) => {
                    console.log("onUnPublisher", publisher);
                    this.deletePublisher(publisher.userId);
                    this.removeDom(publisher.userId);
                    this.initialization(publisher.userId);
                });

                /**
                 * 被服务器踢出或者频道关闭时回调 onBye
                 */
                this.aliWebRTC.on("onBye", (message) => {
                    //1:被服务器踢出
                    //2:频道关闭
                    //3:同一个ID在其他端登录,被服务器踢出
                    let msg;
                    switch (message.code) {
                        case 1:
                            msg = "被服务器踢出";
                            break;
                        case 2:
                            msg = "频道关闭";
                            break;
                        case 3:
                            msg = "同一个ID在其他端登录,被服务器踢出";
                            break;
                        default:
                            msg = "onBye";
                    }
                    this.$message.warning(msg);
                });

                /**
                 *  错误信息
                 */
                this.aliWebRTC.on("onError", (error) => {
                    console.log(error)
                    let msg = error && error.message ? error.message : error;
                    if (msg && msg.indexOf("no session") > -1) {
                        msg = "请重新登录：" + msg;
                    }
                    if (error.errorCode === 10011 || error.errorCode === 10012) {
                        msg = error.errorCode === 10011 ? "屏幕共享被禁止" : "屏幕共享已取消";
                        setTimeout(() => {
                            $("#screenPublish").removeAttr("checked");
                            this.getPublishState();
                        }, 2000);
                    }

                    if (error.code == 15) {
                        msg = "没有开启H5兼容";
                    }
                    if (error.type === "publish") {
                        // 提示用户网络状态不佳
                        console.log("推流断开 需要停止推流,然后重新推流");
                        // 先记录当前推流状态
                        let pubAudio = this.aliWebRTC.configLocalAudioPublish;
                        let pubCamera = this.aliWebRTC.configLocalCameraPublish;
                        let pubScreen = this.aliWebRTC.configLocalScreenPublish;
                        // 设置取消推流
                        this.aliWebRTC.configLocalAudioPublish = false;
                        this.aliWebRTC.configLocalCameraPublish = false;
                        this.aliWebRTC.configLocalScreenPublish = false;
                        this.aliWebRTC.publish().then(() => {
                            console.log("推流断开取消推流成功");
                            this.aliWebRTC.configLocalAudioPublish = pubAudio;
                            this.aliWebRTC.configLocalCameraPublish = pubCamera;
                            this.aliWebRTC.configLocalScreenPublish = pubScreen;
                            this.aliWebRTC.publish().then(() => {
                                console.log("推流断开重新推流成功");
                            }).catch(err => {
                                console.log("推流断开重新推流失败");
                            })
                        }).catch(err => {
                            console.log("推流断开取消推流失败");
                        })
                    }
                    if (error.type === "subscribe") {
                        console.log("订阅断开 取消订阅该userId的所有订阅并移除所有该userId的dom");
                        //先记录当前用户的订阅状态
                        let subInfo = this.getSubscribeInfo(error.userId);
                        //取消订阅状态
                        this.initialization(error.userId);
                        this.aliWebRTC.subscribe(error.userId).then(re => {
                            console.log("订阅断开 取消订阅成功");
                            this.aliWebRTC.configRemoteAudio(error.userId, subInfo.isSubAudio);
                            this.aliWebRTC.configRemoteCameraTrack(error.userId, subInfo.isSubLarge, subInfo.isSubCamera);
                            this.aliWebRTC.configRemoteScreenTrack(error.userId, subInfo.isSubScreen);
                            this.aliWebRTC.subscribe(error.userId).then(re => {
                                console.log("订阅断开 重新订阅成功")
                                if ($("#" + error.userId + "_camera")) {
                                    this.aliWebRTC.setDisplayRemoteVideo(error.userId, $("#" + error.userId + "_camera video")[0], 1)
                                }
                                if ($("#" + error.userId + "_screen")) {
                                    this.aliWebRTC.setDisplayRemoteVideo(error.userId, $("#" + error.userId + "_screen video")[0], 2)
                                }
                            }).catch(err => {
                                console.log("订阅断开 重新订阅失败");
                                this.deletePublisher(error.userId);
                                this.removeDom(error.userId);
                            })
                        }).catch(err => {
                            console.log("订阅断开 取消订阅失败", err)
                            this.deletePublisher(error.userId);
                            this.removeDom(error.userId);
                        });
                    }
                    this.$message.warning(msg)
                });

                /**
                 * 检测到用户离开频道
                 * 更新用户列表
                 * 移除用户视图
                 */
                this.aliWebRTC.on("onLeave", (publisher) => {
                    this.initialization(publisher.userId);
                    this.updateUserList();
                    this.removeDom(publisher.userId);
                    this.$message.success(publisher.displayName + "离开房间");
                })
            },

            /**
             * 加入房间
             * 触发：输入房间号、单击加入房间按钮
             * 更新页面信息
             * 默认开启预览
             * 获取鉴权信息
             * 加入房间
             * 本地默认自动推视频流（视频流 + 音频流）
             * 发布本地流
             */
            joinRoom() {
                // 1.预览
                this.aliWebRTC.startPreview(this.$refs.preview).then(() => {
                }).catch((error) => {
                    this.$message.warning("[开启预览失败]" + error.message);
                });
                //2. 获取频道鉴权令牌参数 为了防止被盗用建议该方法在服务端获取
                // this.$axios.post("/webrtc/token", {channelId:this.channelId}).then(data => {
                //     let authInfo = data;

                    // let authInfo = this.GenerateAliRtcAuthInfo(this.channelId);
                    //3. 加入房间 默认推音频视频流
                    this.aliWebRTC.joinChannel(authInfo, this.userName).then(() => {
                        this.$message.success("加入房间成功");
                        // 4. 发布本地流
                        this.aliWebRTC.configLocalAudioPublish = true;
                        this.aliWebRTC.configLocalCameraPublish = true;
                        this.aliWebRTC.publish().then((res) => {
                            setTimeout(() => {
                                console.log("发布流成功");
                                this.streamstate = "视频流"
                            }, 2000)
                        }, (error) => {
                            this.$message.warning("[推流失败]" + error.message);
                        });
                    }).catch((error) => {
                        this.$message.warning("[加入房间失败]" + error.message);
                    })
                // });
            },

            /**
             * 更新在线用户列表
             */
            updateUserList() {
                // $(".user-ul").empty();
                let userList = this.aliWebRTC.getUserList();
                console.log(userList);
                // let frg = document.createDocumentFragment();
                // userList.map((user) => {
                //     let html = $("<li class='user-ul-li'>" + user.displayName + "<ul class='menu'></ul></li>");
                //     $(html).bind("mouseover", user.userId, showUserMenu).bind("mouseleave", hideUserMenu);
                //     frg.append(html[0]);
                // })
                // $(".user-ul").append($(frg));
            },

            /**
             * 获取测试token
             * @param {*} channelId 频道号
             * @return {object} authinfo
             */
            GenerateAliRtcAuthInfo(channelId) {
                var appId = "1cm1kz1b";// 修改为自己的appid 该方案仅为开发测试使用，正式上线需要使用服务端的AppServer
                var appKey = "68d4c920dbb231be0a96aeeae75fb4b9";// 修改为自己的appkey 该方案仅为开发测试使用，正式上线需要使用服务端的AppServer
                let userId = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
                    var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
                    return v.toString(16);
                });// 可以自定义
                var timestamp = parseInt(new Date().getTime() / 1000 + 48 * 60 * 60);
                var nonce = 'AK-' + timestamp;
                var token = sha256(appId + appKey + channelId + userId + nonce + timestamp);
                return {
                    appid: appId,
                    userid: userId,
                    timestamp: timestamp,
                    nonce: nonce,
                    token: token,
                    gslb: ["https://rgslb.rtc.aliyuncs.com"],
                    channel: channelId
                };
            },

            /**
             * 获取当前remote用户的流菜单
             */
            showUserMenu(evt) {
                let userId = evt.data;
                if (!$(event.target).eq(0).hasClass("user-ul-li")) {
                    return
                }
                $(".menu").hide();
                $(event.target).find(".menu").empty().show();
                let userInfo = aliWebRTC.getUserInfo(userId);
                let streamTypeList = userInfo.streamConfigs.filter(item => {
                    return item.state === "active";
                });
                var html = "";
                if (streamTypeList.length == 0) {
                    html = $("<li>该用户未推流</li>");
                    $(event.target).find(".menu").append(html[0]);
                } else {
                    var frg = document.createDocumentFragment()
                    streamTypeList.map(item => {
                        item.userId = userId;
                        var labelName = "";
                        if (item.type === "video") {
                            switch (item.label) {
                                case "sophon_video_camera_large":
                                    labelName = "视频流";
                                    break;
                                case "sophon_video_screen_share":
                                    labelName = "共享流";
                                    break;
                                case "sophon_audio":
                                    labelName = "";
                                    break;
                                default:
                                    labelName = "";
                            }
                        } else {
                            labelName = "";
                        }
                        //将音频流或小流的标签不显示
                        if (labelName !== "") {
                            let subState = item.subscribed === true ? "取消订阅" : "订阅";
                            html = $("<li>" + labelName + "&nbsp;<span>" + subState + "</span></li>");
                            $(html).find("span").off("click").on("click", item, unSub);
                            frg.append(html[0]);
                        }
                    })
                    $(event.target).find(".menu").append($(frg));
                }
            },

            /**
             * 隐藏当前remote用户的流菜单
             */
            hideUserMenu() {
                $(event.currentTarget).find(".menu").hide();
            },

            /**
             * 订阅&取消订阅
             */
            unSub(evt) {
                let v = evt.data;
                if (v.subscribed) {
                    setConfigRemote(v.userId, v.label).then(re => {
                        removeDom(v.userId, v.label);
                        console.log("取消订阅");
                    });
                } else {
                    receivePublishManual(v).then(re => {
                        creatDomAndshowRemoteVideo(v);
                        console.log("订阅成功");
                    });
                }
                $(".menu").hide();
            },


            /**
             * 获取dom标签 设置video
             */
            creatDomAndshowRemoteVideo(v) {
                var dom = getDisplayRemoteVideo(v.userId, v.label);
                if (v.label != "sophon_video_screen_share") {
                    aliWebRTC.setDisplayRemoteVideo(v.userId, dom, 1);
                } else {
                    aliWebRTC.setDisplayRemoteVideo(v.userId, dom, 2);
                }
            },

            /**
             * 创建获取订阅的remote的video标签
             */
            getDisplayRemoteVideo(userId, label) {
                var label = label === "sophon_video_camera_large" ? "camera" : "screen";
                var id = userId + "_" + label;
                var videoWrapper = $("#" + id);
                if (videoWrapper.length == 0) {
                    var userInfo = aliWebRTC.getUserList().filter(item => {
                        return item.userId === userId;
                    })
                    var displayName = userInfo[0].displayName;
                    videoWrapper = $("<div class='remote-subscriber' id=" + id + "> <video autoplay playsinline></video><div class='display-name'></div></div>");
                    $(".video-container").append(videoWrapper);
                }
                videoWrapper.find(".display-name").text(displayName + "—" + label);
                return videoWrapper.find("video")[0];
            },

            /**
             * 移除dom
             */
            removeDom(userId, label) {
                console.log("removeDom   :  " + userId + "   :  " + label);
                // if (label === "sophon_audio") return
                // if (userId) {
                //     if (!label) {
                //         $("#" + userId + "_camera").remove();
                //         $("#" + userId + "_screen").remove();
                //     } else {
                //         label = label === "sophon_video_camera_large" ? "camera" : "screen";
                //         $("#" + userId + "_" + label).remove();
                //     }
                // }
            },

            /**
             * 取消订阅设置
             */
            setConfigRemote(userId, label) {
                return new Promise((resolve, reject) => {
                    //demo中只订阅大流
                    if (label === "sophon_video_camera_large") {
                        this.aliWebRTC.configRemoteCameraTrack(userId, false, false);
                        this.aliWebRTC.configRemoteAudio(userId, false);
                    } else if (label === "sophon_video_screen_share") {
                        this.aliWebRTC.configRemoteScreenTrack(userId, false);
                    }
                    this.aliWebRTC.subscribe(userId).then(re => {
                        resolve();
                    }).catch(error => console.log("取消订阅失败", error))
                });
            },


            /**
             * 订阅设置
             */
            receivePublishManual(v) {
                console.log("receivePublishManual订阅", v);
                return new Promise((resolve, reject) => {
                    if (v.label === "sophon_video_camera_large") {
                        console.log("订阅固定视频流");
                        aliWebRTC.configRemoteCameraTrack(v.userId, true, true);
                        aliWebRTC.configRemoteAudio(v.userId, true);
                    } else if (v.label === "sophon_video_screen_share") {
                        console.log("订阅屏幕共享流");
                        aliWebRTC.configRemoteScreenTrack(v.userId, true);
                    }
                    aliWebRTC.subscribe(v.userId).then(re => {
                        resolve();
                    }).catch((error) => {
                        reject(error);
                        this.$message.warning("[subscribe失败]" + error.message);
                    });
                })
            },

            /**
             * 更新推流状态
             * 当远端流发生变化时，通过onPublisher回调接收到信息
             * 远端流不可用时其state值为inactive
             * 通过对比本地维护的publisherList来进行dom的删除
             * 并且更新本地维护的publisherList
             */
            updatePublisherStream(publisher, index) {
                let oldStreamConfigs = JSON.parse(JSON.stringify(this.publisherList[index].streamConfigs));
                let newStreamConfigs = publisher.streamConfigs;
                let subscribeInfo = this.getSubscribeInfo(publisher.userId);
                oldStreamConfigs.forEach((v, i, a) => {
                    let newStream = newStreamConfigs.getObjByProprety(v.label, "label");
                    // 判断流状态改变了 但不确定我们是否订阅了该流
                    if (v.state != newStream.state) {
                        console.log("流的状态变了" + v.label, v, v.type, ">" + v.state + ">>" + newStream.state + ">", newStream, subscribeInfo);
                        //并且要取消订阅某个流，不然就不能再次订阅了
                        subscribeInfo.subscribeInfoArr.forEach(sv => {
                            if (v.label === sv.label) {
                                console.log("setConfigRemote取消订阅调用[api]:subscribe", publisher.userId, sv.type, sv.label);
                                this.setConfigRemote(publisher.userId, sv.type, sv.label).then(re => {
                                    // 移除dom
                                    this.removeDom(publisher.userId, v.label);
                                }).catch(error => {
                                    console.error("流的状态变了重新订阅出问题", error);
                                });
                            }
                        });
                    }
                });
                this.publisherList.splice(index, 1, publisher);
            },

            /**
             * 用户停止推流时 删除用户列表中该用户
             */
            deletePublisher(userId) {
                let index = this.publisherList.getIndexByProprety(userId, "userId");
                if (index != -1) {
                    this.publisherList.splice(index, 1);
                    this.deletePublisher(userId);
                } else {
                    console.log("未找到之前的推流数据"); //删除推流用户
                }
            },

            /**
             * 正在推流时,热切换进行republish操作
             */
            rePublish() {
                if ($(".publisher .push-stream").text() === "停止推流") {
                    $(".publisher .push-stream").text("处理中...");
                    $(".streamType").hide();
                    this.aliWebRTC.publish().then(re => {
                        setTimeout(() => {
                            this.getPublishState("success");
                        }, 2000);
                    }).catch(error => {
                        setTimeout(() => {
                            this.getPublishState("danger");
                        }, 2000);
                    });
                }
            },

            /**
             * 根据推流状态设置当前推流UI
             */
            getPublishState() {
                var streamstate = $(".streamstate b").text()
                if (aliWebRTC.configLocalAudioPublish || aliWebRTC.configLocalCameraPublish || aliWebRTC.configLocalScreenPublish) {
                    $(".publisher .push-stream").text("停止推流");
                    if (aliWebRTC.configLocalScreenPublish && aliWebRTC.configLocalCameraPublish) {
                        streamstate = "视频流 + 共享流";
                    } else {
                        if (aliWebRTC.configLocalScreenPublish) {
                            streamstate = "共享流";
                        } else if (aliWebRTC.configLocalCameraPublish) {
                            streamstate = "视频流";
                        }
                    }
                } else {
                    $(".publisher .push-stream").text("开始推流");
                    streamstate = "当前未推流";
                }
                this.$message.warning("推流状态：" + streamstate);
                $(".streamstate b").text(streamstate);
                $(".streamType").show();
            },

            /**
             * 初始化订阅状态
             * @param {String} userId
             */
            initialization(userId) {
                if (this.aliWebRTC) {
                    this.aliWebRTC.configRemoteAudio(userId, false);
                    this.aliWebRTC.configRemoteCameraTrack(userId, false, false);
                    this.aliWebRTC.configRemoteScreenTrack(userId, false);
                }
            },

            /**
             * 分析远端用户信息
             * @param {String} userId
             */
            getSubscribeInfo(userId) {
                let userInfo = this.aliWebRTC.getUserInfo(userId);
                let subscribeInfo = [], subscribeInfoArr = [], isSubAudio = false, isSubLarge = false, isSubSmall = false, isSubCamera = false,
                    isSubScreen = false, isSubVideo = false;
                if (userInfo) {
                    userInfo.streamConfigs.forEach(v => {
                        if (v.subscribed) {
                            subscribeInfo.push(v.label);
                            subscribeInfoArr.push(v);
                            v.type == "audio" ? isSubAudio = true : "";
                            v.type == "video" ? isSubVideo = true : "";
                            v.label == "sophon_video_camera_large" ? isSubLarge = true : "";
                            v.label == "sophon_video_camera_small" ? isSubSmall = true : "";
                            v.label == "sophon_video_screen_share" ? isSubScreen = true : "";
                            if (isSubLarge || isSubSmall) {
                                isSubCamera = true;
                            }
                        }
                    });
                }
                return {
                    subscribeInfo: subscribeInfo,
                    subscribeInfoArr: subscribeInfoArr,
                    isSubLarge: isSubLarge,
                    isSubSmall: isSubSmall,
                    isSubCamera: isSubCamera,
                    isSubAudio: isSubAudio,
                    isSubScreen: isSubScreen,
                    isSubVideo: isSubVideo
                };
            },

            /**
             * 取消订阅设置
             * @param {String} userId
             * @param {String} type
             * @param {String} label
             */
            // setConfigRemote(userId, type, label) {
            //     return new Promise((resolve, reject) => {
            //         if (type == "audio") {
            //             this.aliWebRTC.configRemoteAudio(userId, false);
            //         } else {
            //             if (label === "sophon_video_camera_large") {
            //                 this.aliWebRTC.configRemoteCameraTrack(userId, false, false);
            //                 console.warn("取消相机流");
            //             } else if (label === "sophon_video_screen_share") {
            //                 console.warn("取消共享流");
            //                 this.aliWebRTC.configRemoteScreenTrack(userId, false);
            //             }
            //         }
            //         this.aliWebRTC.subscribe(userId).then(re => {
            //             resolve();
            //         }).catch(err => {
            //             console.error("重新订阅失败", err);
            //             alert(err.message);
            //         })
            //     });
            // },
        }
    }
</script>

<style scoped>
    .local-video {
        margin: 0 calc(50 / 1080 * 100vh);
        position: relative;
    }
</style>
