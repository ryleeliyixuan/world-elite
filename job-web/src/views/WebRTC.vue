<template>
    <div>
        <div>模拟面试</div>
        <el-input v-model="userName" placeholder="请输入用户名"></el-input>
        <el-input v-model="channelId" placeholder="请输入频道id"></el-input>
        <div>用户Id：{{userId}}</div>
        <div>用户名：{{userName}}</div>
        <div>远端用户数量:{{userList.length}}</div>
        <div v-if="userList.length>0">
            <div v-for="user in userList">{{user.displayName + " " + user.userId}}</div>
        </div>
        <el-button @click="joinRoom">加入房间</el-button>
        <el-button @click="leaveRoom">离开房间</el-button>
        <div class="local-video">
            <video autoplay playsinline ref="preview"></video>
        </div>
        <div class="remote-video">
            <video autoplay playsinline ref="remote"></video>
        </div>

        <div @click="getPublishState">刷新流状态</div>
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
                userId: undefined,
                userName: "test",
                channelId: 1,
                aliWebRTC: undefined,
                publisherList: [],
                streamstate: "",
                userList: [], // 远程用户列表
            }
        },
        mounted() {
            /**
             * 创建webRTC对象
             */
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

        destroy() {
          this.leaveRoom();
        },

        methods: {
            init() {
                /**
                 * remote用户加入房间 onJoin
                 * 更新在线用户列表
                 */
                this.aliWebRTC.on("onJoin", (publisher) => {
                    console.log("远程用户加入频道：" + publisher.displayName + "加入房间");
                    //重置订阅状态
                    //默认订阅远端音频和视频大流，但需要调用subscribe才能生效
                    //这里取消默认订阅，根据需求进行订阅
                    this.aliWebRTC.configRemoteAudio(publisher.userId, false);
                    this.aliWebRTC.configRemoteCameraTrack(publisher.userId, false, false);
                });

                /**
                 * remote流发布事件 onPublish
                 * 订阅远端视频流
                 */
                this.aliWebRTC.on("onPublisher", (publisher) => {
                    console.log("远程用户开始推送流", publisher);
                    this.receivePublishManual(publisher, "sophon_video_camera_large").then(() => {
                        console.log("订阅成功");
                        this.aliWebRTC.setDisplayRemoteVideo(publisher.userId, this.$refs.remote, 1);
                    });
                });

                /**
                 * remote流结束发布事件 onUnPublisher
                 * 初始化订阅状态
                 */
                this.aliWebRTC.on("onUnPublisher", (publisher) => {
                    console.log("远程用户结束推送流", publisher);
                    this.initialization(publisher.userId);
                });

                /**
                 * 被服务器踢出或者频道关闭时回调 onBye
                 */
                this.aliWebRTC.on("onBye", (message) => {
                    console.log("用户已退出频道");
                    let arr = ["onBye", "被服务器踢出", "频道关闭", "同一个ID在其他端登录,被服务器踢出"];
                    this.$message.warning(arr[message.code]);
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
                            this.getPublishState();
                        }, 2000);
                    }

                    if (error.code === 15) {
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
                        this.aliWebRTC.subscribe(error.userId).then(() => {
                            console.log("订阅断开 取消订阅成功");
                            this.aliWebRTC.configRemoteAudio(error.userId, subInfo.isSubAudio);
                            this.aliWebRTC.configRemoteCameraTrack(error.userId, subInfo.isSubLarge, subInfo.isSubCamera);
                            this.aliWebRTC.configRemoteScreenTrack(error.userId, subInfo.isSubScreen);
                            this.aliWebRTC.subscribe(error.userId).then(re => {
                                console.log("订阅断开 重新订阅成功")
                                this.aliWebRTC.setDisplayRemoteVideo(error.userId, this.$refs.remote, 1);
                            }).catch(err => {
                                console.log("订阅断开 重新订阅失败");
                            })
                        }).catch(err => {
                            console.log("订阅断开 取消订阅失败", err)
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
                    console.log("远程用户已退出频道");
                    this.initialization(publisher.userId);
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
                this.$axios.post("/webrtc/token", {channelId: this.channelId}).then(data => {
                    let authInfo;
                    if (process.env.NODE_ENV === 'development') {
                        authInfo = this.GenerateAliRtcAuthInfo(this.channelId);
                    } else {
                        authInfo = {
                            appid: data.data.appId,
                            userid: data.data.userId,
                            timestamp: data.data.timestamp,
                            nonce: data.data.nonce,
                            token: data.data.token,
                            gslb: ["https://rgslb.rtc.aliyuncs.com"],
                            channel: data.data.channelId
                        };
                    }
                    // 记录用户id
                    this.userId = authInfo.userid;

                    //3. 加入房间 默认推音频视频流
                    this.aliWebRTC.joinChannel(authInfo, this.userName).then(() => {
                        this.$message.success("加入房间成功");
                        // 4. 发布本地流
                        this.aliWebRTC.configLocalAudioPublish = true;
                        this.aliWebRTC.configLocalCameraPublish = true;
                        this.aliWebRTC.publish().then(() => {
                            setTimeout(() => {
                                console.log("发布流成功");
                                this.getPublishState();
                            }, 2000)
                        }, (error) => {
                            this.$message.warning("[推流失败]" + error.message);
                        });
                    }).catch((error) => {
                        this.$message.warning("[加入房间失败]" + error.message);
                    })
                });
            },

            /**
             * 离开房间
             */
            leaveRoom() {
                this.aliWebRTC.leaveChannel().then(() => {
                }, (error) => {
                    console.log(error.message);
                });
            },

            /**
             * 获取测试token
             * @param {*} channelId 频道号
             * @return {object} authinfo
             */
            GenerateAliRtcAuthInfo(channelId) {
                const appId = "rifwol88";// 修改为自己的appid 该方案仅为开发测试使用，正式上线需要使用服务端的AppServer
                const appKey = "9772af2eba07d5c84ce5075f11c6b904";// 修改为自己的appkey 该方案仅为开发测试使用，正式上线需要使用服务端的AppServer
                const userId = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
                    const r = Math.random() * 16 | 0, v = c === 'x' ? r : (r & 0x3 | 0x8);
                    return v.toString(16);
                }); // 可以自定义
                const timestamp = (new Date().getTime() / 1000 + 48 * 60 * 60).toFixed(0);
                const nonce = 'AK-' + timestamp;
                const token = sha256(appId + appKey + channelId + userId + nonce + timestamp);
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
             * 取消订阅设置
             */
            unSubscribe(publisher, label) {
                console.log("取消订阅远程视频", publisher);
                return new Promise((resolve) => {
                    if (label === "sophon_video_camera_large") {
                        this.aliWebRTC.configRemoteCameraTrack(publisher.userId, false, false);
                        this.aliWebRTC.configRemoteAudio(publisher.userId, false);
                    } else if (label === "sophon_video_screen_share") {
                        this.aliWebRTC.configRemoteScreenTrack(publisher.userId, false);
                    }
                    this.aliWebRTC.subscribe(publisher.userId).then(re => {
                        resolve();
                    }).catch(error => console.log("取消订阅失败", error))
                });
            },


            /**
             * 订阅设置
             */
            receivePublishManual(publisher, label) {
                console.log("订阅远程视频", publisher);
                return new Promise((resolve) => {
                    if (label === "sophon_video_camera_large") {
                        console.log("订阅固定视频流");
                        this.aliWebRTC.configRemoteCameraTrack(publisher.userId, true, true);
                        this.aliWebRTC.configRemoteAudio(publisher.userId, true);
                    } else if (label === "sophon_video_screen_share") {
                        console.log("订阅屏幕共享流");
                        this.aliWebRTC.configRemoteScreenTrack(publisher.userId, true);
                    }
                    this.aliWebRTC.subscribe(publisher.userId).then(() => {
                        resolve();
                    }).catch((error) => {
                        this.$message.warning("[subscribe失败]" + error.message);
                    });
                })
            },

            /**
             * 根据推流状态设置当前推流UI
             */
            getPublishState() {
                if (this.aliWebRTC.configLocalAudioPublish || this.aliWebRTC.configLocalCameraPublish || this.aliWebRTC.configLocalScreenPublish) {
                    if (this.aliWebRTC.configLocalScreenPublish && this.aliWebRTC.configLocalCameraPublish) {
                        this.streamstate = "视频流 + 共享流";
                    } else {
                        if (this.aliWebRTC.configLocalScreenPublish) {
                            this.streamstate = "共享流";
                        } else if (this.aliWebRTC.configLocalCameraPublish) {
                            this.streamstate = "视频流";
                        }
                    }
                } else {
                    this.streamstate = "当前未推流";
                }
                this.$message.success("推流状态：" + this.streamstate);
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
                            v.type === "audio" ? isSubAudio = true : "";
                            v.type === "video" ? isSubVideo = true : "";
                            v.label === "sophon_video_camera_large" ? isSubLarge = true : "";
                            v.label === "sophon_video_camera_small" ? isSubSmall = true : "";
                            v.label === "sophon_video_screen_share" ? isSubScreen = true : "";
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
        }
    }
</script>

<style scoped>
    .local-video, .remote-video {
        margin: 0 calc(50 / 1080 * 100vh);
        position: relative;
    }
</style>
