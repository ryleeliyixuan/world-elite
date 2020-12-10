<template>
    <div class="bg">
        <div class="app-container">

            <div class="main-container">
                <div class="video-container" :style="{height:videoHeight+'px'}">
                    <video :class="[{'major':remoteVideo && !waiting},{'minor':!remoteVideo || waiting}]" autoplay playsinline ref="remote"
                           @play="calculateHeight"
                           @click="changeVideo"/>
                    <video :class="[{'major':!remoteVideo || waiting},{'minor':remoteVideo && !waiting}]" autoplay playsinline ref="preview"
                           @play="calculateHeight"
                           @click="changeVideo"/>
                    <div v-if="waiting" style="text-align: center; z-index: 100; line-height: 600px; background: #cccccc;">{{tips}}
                    </div>
                </div>

                <div class="chat-container" :style="{height:videoHeight+'px'}">
                    <div class="header" v-if="conversationItem">
                        <el-avatar :size="35" :src="interviewer.avatar" style="border: 1px solid #3D6FF4;"></el-avatar>
                        <div>
                            <div class="name">{{interviewer.nickName}}</div>
                            <div class="tags" v-if="interviewer">{{interviewer.position}}</div>
                        </div>
                    </div>
                    <el-scrollbar ref="receive" class="content-container" wrap-style="overflow: hidden auto; padding-right: 10px;">
                        <div v-for="item in messageList">
                            <div class="self-container" v-if="item.fromUser===userId">
                                <div class="self-text-content" v-if="item.payload.contentType===1">
                                    {{item.payload.content}}
                                </div>
                                <div class="self-image-content" v-else-if="item.payload.contentType===3">
                                    <el-image :src="item.payload.remoteMediaUrl" @load="scrollBottom"/>
                                </div>
                                <div class="self-text-content" v-else-if="item.payload.contentType===5">
                                    <el-link type="primary" :href="item.payload.remoteMediaUrl" target="_blank">{{item.payload.content}}</el-link>
                                </div>
                                <el-image :src="$store.state.user.avatar" class="avatar">
                                    <div slot="error">
                                        <i class="el-icon-picture-outline"></i>
                                    </div>
                                </el-image>
                            </div>
                            <div class="others-container" v-else>
                                <el-image :src="conversationItem.friendVo.avatar" class="avatar">
                                    <div slot="error">
                                        <i class="el-icon-picture-outline"></i>
                                    </div>
                                </el-image>
                                <div class="others-text-content" v-if="item.payload.contentType===1">
                                    {{item.payload.content}}
                                </div>
                                <div class="others-image-content" v-else-if="item.payload.contentType===3">
                                    <el-image :src="item.payload.remoteMediaUrl" @load="scrollBottom"/>
                                </div>
                                <div class="others-text-content" v-else-if="item.payload.contentType===5">
                                    <el-link type="primary" :href="item.payload.remoteMediaUrl" target="_blank">{{item.payload.content}}</el-link>
                                </div>
                            </div>
                        </div>
                    </el-scrollbar>
                    <div class="footer">
                        <el-input type="text"
                                  placeholder="请输入内容"
                                  v-model="content"
                                  maxlength="150"
                                  @keyup.enter.native="onSend"
                                  :show-word-limit="false"/>
                        <el-button class="send-button" type="primary" icon="el-icon-message" size="small" circle @click="onSend"></el-button>
                    </div>
                </div>
            </div>
            <div class="button-container">
                <div class="finish-button" @click="leaveRoom">结束面试</div>
                <div class="service-button" @click="onService">人工客服</div>
            </div>
        </div>

        <el-dialog :visible.sync="dialogVisible" center top="35vh" :show-close="false">
            <span class="text" v-html="dialogText"/>
            <div class="button" @click="dialogVisible=false">确认</div>
        </el-dialog>
    </div>
</template>

<script>
    import "aliyun-webrtc-sdk"
    import "@/utils/rtcUtils"
    import {sha256} from "@/utils/sha256"
    import im from "@/utils/im"

    export default {
        name: "InterviewPage",
        data() {
            return {
                eventId: undefined, // 预约事件Id
                interviewerId: undefined, // 对方id
                interviewer: undefined, // 对方信息
                userId: undefined, // 用户Id（自己）
                conversationItem: undefined, // 会话
                content: "", // 要发送得消息
                messageList: [], // 消息列表

                aliWebRTC: undefined, // webRTC对象
                streamState: "", // 流状态
                remoteVideo: false, // 默认主显示远程视频
                waiting: true, // 等待远程加入
                videoHeight: 600, // 视频高度

                dialogVisible: true, // 显示对话框
                dialogText: "如果面试官15分钟内未出现请联系人工客服。",
                tips: "等待加入...",
            }
        },

        computed: {
            userName() {
                return this.$store.state.user.name;
            }
        },
        mounted() {
            this.eventId = this.$route.params.id;
            this.interviewerId = this.$route.params.interviewerId;

            // 获取对方信息（主要获取面试官的信息）
            this.getInterviewerInfo(this.interviewerId);

            /**
             * 创建webRTC对象
             */
            this.aliWebRTC = new AliRtcEngine();
            /**
             * AliWebRTC isSupport检测
             */
            this.aliWebRTC.isSupport().then(re => {
                this.init();
                this.joinRoom();
            }).catch(error => {
                this.tips = error.message;
                this.$message.error(error.message);
            })

            /**
             * 初始化webSocket
             */
            im.init(this.receiveMessage).then((data) => {
                this.userId = data.userId;
                this.token = data.token;
                im.addConversation(this.interviewerId, 0).then(() => {
                    im.getConversation(this.interviewerId, 0).then((conversation) => {
                        this.conversationItem = conversation[0];
                    })
                })
            }).catch(() => {
                this.$router.push({path: "/login", query: {...this.$route.query, redirect: "/chat"}});
            });
            this.$emit("complete");
        },

        destroy() {
            im.close();
            this.leaveRoom();
        },

        methods: {
            changeVideo() {
                if (!this.waiting) {
                    this.remoteVideo = !this.remoteVideo;
                }
                this.calculateHeight();
            },
            /**
             * 监听视频播放，获取视频最大高度
             */
            calculateHeight() {
                setTimeout(() => {
                    let videoHeight;
                    if (this.$refs.remote && this.$refs.preview) {
                        videoHeight = Math.max(this.$refs.remote.offsetHeight, this.$refs.preview.offsetHeight);
                    } else if (this.$refs.remote) {
                        videoHeight = this.$refs.remote.offsetHeight;
                    } else if (this.$refs.preview) {
                        videoHeight = this.$refs.preview.offsetHeight;
                    } else {
                        videoHeight = 0;
                    }
                    this.videoHeight = videoHeight;
                }, 100)
            },

            /**
             * webRTC初始化，监听事件
             */
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
                        this.waiting = false;
                        this.remoteVideo = true;
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
                    this.remoteVideo = false;
                    this.waiting = true;
                    this.initialization(publisher.userId);
                    // this.$message.success(publisher.displayName + "离开房间");
                    this.$message.success( "对方已离开房间")
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
                this.$axios.post(`/mock/webrtc/token/${this.eventId}`).then(data => {
                    let authInfo;
                    // if (process.env.NODE_ENV === 'development') {
                    //     authInfo = this.GenerateAliRtcAuthInfo(this.eventId);
                    // } else {
                    authInfo = {
                        appid: data.data.appId,
                        userid: data.data.userId,
                        timestamp: data.data.timestamp,
                        nonce: data.data.nonce,
                        token: data.data.token,
                        gslb: ["https://rgslb.rtc.aliyuncs.com"],
                        channel: data.data.channelId
                    };
                    // }
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
                this.remoteVideo = false;
                // this.calculateHeight();
                this.aliWebRTC.leaveChannel().then(() => {
                    this.$router.go(-1);
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
                        this.streamState = "视频流 + 共享流";
                    } else {
                        if (this.aliWebRTC.configLocalScreenPublish) {
                            this.streamState = "共享流";
                        } else if (this.aliWebRTC.configLocalCameraPublish) {
                            this.streamState = "视频流";
                        }
                    }
                } else {
                    this.streamState = "当前未推流";
                }
                this.$message.success("推流状态：" + this.streamState);
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

            getInterviewerInfo(id) {
                this.$axios.get(`/mock/interviewer/${id}`).then(data => {
                    this.interviewer = data.data;
                })
            },

            // 接收消息处理
            receiveMessage(value) {
                if (value.fromUser === this.conversationItem.friendVo.friendUserId) {
                    im.msgAsReadMessage(value.fromUser, value.toUser, [value.messageId], value.conversation.conversationId);

                    // 构建消息对象，插入接收框
                    value.timestamp = Date.now();
                    this.messageList.push(value);
                    this.scrollBottom();

                    // 更新会话列表
                    this.conversationItem.lastMessage = value;
                }
            },

            // 发送
            onSend() {
                if (this.content !== '') {
                    // 保存消息，并清空发送框
                    let content = this.content;
                    this.content = '';

                    // 构建消息对象，插入接收框
                    let message = {
                        fromUser: this.userId,
                        messageId: undefined,
                        payload: {
                            content: content,
                            contentType: 1,
                            expireDuration: 0,
                            extra: null,
                            remoteMediaUrl: null,
                        },
                        status: 1,
                        timestamp: Date.now(),
                        toUser: this.conversationItem.friendVo.friendUserId,
                    }
                    this.messageList.push(message);

                    // 接收框滚动到底部
                    this.scrollBottom();

                    // 发送消息
                    im.chatMessage(this.userId, this.conversationItem.friendVo.friendUserId, this.conversationItem.id, content, 1).then(data => {
                        message.messageId = data.messageId;
                        this.conversationItem.lastMessage = message;
                    });
                } else {
                    this.$message.warning("消息不能为空");
                }
            },

            // 接收框滚动到底部
            scrollBottom() {
                // 滚动到底部
                this.$nextTick(() => {
                    this.$refs.receive.wrap.scrollTop = this.$refs.receive.wrap.scrollHeight;
                })
            },

            // 人工客服
            onService() {
                this.dialogText = "请联系人工客服：<br/>xiaokefu@we.com";
                this.dialogVisible = true;
            }
        }
    }
</script>

<style scoped lang="scss">
    .bg {
        background: url("../assets/webrtc_bg.png") no-repeat center;
        min-height: calc(100vh - 480px);
        background-size: 1920px 913px;
        padding: 20px 0;

        .app-container {
            max-width: 1140px;
            margin: 0 auto;

            .main-container {
                display: flex;
                justify-content: center;

                .video-container {
                    width: 700px;
                    min-width: 700px;
                    position: relative;
                    display: inline-block;
                    height: 600px;
                    border-radius: 20px;
                    overflow: hidden;
                    background: black;

                    .major {
                        position: absolute;
                        width: 100%;
                        height: 600px;
                    }

                    .minor {
                        position: absolute;
                        width: 20%;
                        right: 0;
                        top: 0;
                        height: auto;
                        z-index: 99;
                    }
                }

                .chat-container {
                    width: 300px;
                    min-width: 300px;
                    display: inline-flex;
                    flex-direction: column;
                    justify-content: space-between;
                    height: 500px;
                    border: 10px solid #D7E6FE;
                    border-radius: 20px;
                    background: white;
                    margin-left: 10px;

                    .header {
                        width: 100%;
                        height: 44px;
                        display: flex;
                        align-items: center;
                        padding-left: 10px;
                        border-bottom: 1px solid #CCCCCC;

                        .name {
                            font-size: 16px;
                            font-weight: 500;
                            color: #333333;
                            line-height: 22px;
                            margin-left: 10px;
                        }

                        .tags {
                            margin-top: 2px;
                            font-size: 7px;
                            transform: scale(0.75) translateX(-10px);
                            font-weight: 500;
                            color: #999999;
                            line-height: 9px;
                            margin-left: 10px;
                        }
                    }

                    .content-container {
                        flex: 1;
                        width: 100%;
                        padding: 10px 0;

                        .self-container {
                            width: 100%;
                            height: auto;
                            display: flex;
                            justify-content: flex-end;
                            margin-top: 10px;
                            padding: 0 15px;

                            .self-text-content {
                                max-width: 70%;
                                height: auto;
                                padding: 8px;
                                font-size: 14px;
                                word-wrap: break-word;
                                word-break: break-all;
                                background-color: #9eea6a;
                                border-radius: 6px;
                                position: relative;
                                margin-right: 16px;


                                &:hover {
                                    background: #98e165;
                                }

                                &::before {
                                    content: '';
                                    position: absolute;
                                    top: 16px;
                                    right: -8px;
                                    border-top: 8px solid transparent;
                                    border-bottom: 8px solid transparent;
                                    border-left: 8px solid #9eea6a;
                                }

                                &:hover::before {
                                    border-left: 8px solid #98e165;
                                }
                            }

                            .self-image-content {
                                max-width: 70%;
                                height: auto;
                                margin-right: 8px;
                            }
                        }

                        .others-container {
                            width: 100%;
                            height: auto;
                            display: flex;
                            align-items: flex-start;
                            margin-top: 20px;
                            padding: 0 15px;

                            .others-text-content {
                                max-width: 70%;
                                height: auto;
                                padding: 8px;
                                font-size: 14px;
                                word-wrap: break-word;
                                word-break: break-all;
                                background-color: #ffffff;
                                border-radius: 6px;
                                position: relative;
                                margin-left: 16px;

                                &:hover {
                                    background: #fbfbfb;
                                }

                                &::before {
                                    content: '';
                                    position: absolute;
                                    top: 16px;
                                    left: -8px;
                                    border-top: 8px solid transparent;
                                    border-bottom: 8px solid transparent;
                                    border-right: 8px solid rgba(255, 255, 255, 0.99);
                                }

                                &:hover::before {
                                    border-right: 8px solid #fbfbfb;
                                }
                            }

                            .others-image-content {
                                max-width: 70%;
                                height: auto;
                                margin-left: 8px;
                            }
                        }
                    }

                    .footer {
                        width: 100%;
                        margin: 10px;
                        height: auto;
                        display: flex;

                        .send-button {
                            min-width: 40px;
                            margin-right: 20px;
                            margin-left: 10px;
                            font-size: 18px;
                        }
                    }
                }
            }


            .button-container {
                width: 300px;
                margin: 0 auto;
                display: flex;
                align-items: center;
                justify-content: center;

                .finish-button {
                    width: 136px;
                    height: 41px;
                    background: #F9E3E1;
                    border-radius: 21px;
                    border: 1px solid #F44336;
                    font-size: 18px;
                    font-family: PingFangSC-Regular, PingFang SC;
                    font-weight: 400;
                    color: #F44336;
                    line-height: 41px;
                    text-align: center;
                    margin: 20px auto;
                    z-index: 120;

                    &:hover {
                        cursor: pointer;
                        color: #db4d42;
                        border-color: #db4d42;
                    }
                }

                .service-button {
                    width: 136px;
                    height: 41px;
                    background: #EDF3FF;
                    border-radius: 21px;
                    border: 1px solid #3D6FF4;
                    font-size: 18px;
                    font-weight: 400;
                    color: #3D6FF4;;
                    line-height: 41px;
                    text-align: center;
                    margin: 20px auto;
                    z-index: 120;

                    &:hover {
                        cursor: pointer;
                        color: #0b51f6;
                        border-color: #0b51f6;
                    }
                }
            }

        }

        .avatar {
            min-width: 40px;
            width: 40px;
            height: 40px;
            display: flex;
            align-items: center;
            justify-content: center;
            border-radius: 50%;
        }

        ::v-deep .el-dialog {
            width: 411px;
            height: 234px;
            background: #FFFFFF;
            border-radius: 17px;

            .el-dialog__body {
                padding: 18px 73px 40px;

                .text {
                    font-size: 21px;
                    color: #333333;
                    line-height: 29px;
                }

                .button {
                    width: 207px;
                    height: 41px;
                    background: #EDF3FF;
                    border-radius: 21px;
                    font-size: 18px;
                    color: #3D6FF4;
                    line-height: 41px;
                    text-align: center;
                    margin: 40px auto 0;
                    cursor: pointer;
                }
            }
        }
    }
</style>
