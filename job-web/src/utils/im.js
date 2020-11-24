import eventBus from "@/utils/eventBus";
import {Message} from 'element-ui';
import {storage} from "@/utils/storage";

let im = {
    ws: undefined,
    token: undefined,
    userId:undefined,
    init: function (receiveMessage) {
        return new Promise((resolve, reject) => {
            this.token = storage.getData("imInfo").token;
            this.userId = storage.getData("imInfo").userId;

            eventBus.addEvent("-1", (value) => {
                console.log(value);
                Message.error(value.errorMsg);
            });

            eventBus.addEvent("19", (value) => {
                receiveMessage(value);
            });

            if ("WebSocket" in window) {
                console.log("您的浏览器支持 WebSocket!");
                let timer;

                // 打开一个 web socket
                this.ws = new WebSocket(process.env.VUE_APP_WS_API + "/im/ws/");

                this.ws.onopen = () => {
                    // Web Socket 已连接上，使用 send() 方法发送数据
                    timer = setInterval(() => {
                        this.ping();
                    }, 10000)

                    let {email, password} = storage.getLoginInfo();
                    this.login(email, password, 1).then((data) => {
                        resolve(data);
                    }).catch((error) => {
                        console.log(error)
                        reject();
                    })
                };

                this.ws.onmessage = (evt) => {
                    console.log("数据已接收...");
                    console.log(JSON.parse(evt.data));
                    eventBus.emitEvent(JSON.parse(evt.data).messageType.toString(), JSON.parse(evt.data));
                };

                this.ws.onclose = () => {
                    console.log("连接已关闭..."); // 关闭 websocket
                    clearInterval(timer);
                };
            } else {
                // 浏览器不支持 WebSocket
                Message.error("您的浏览器不支持 WebSocket!")
            }
        })
    },

    // 发送消息
    sendMessage: function (message) {
        console.log("数据发送中...");
        console.log(message);
        return new Promise((resolve, reject) => {
            eventBus.addEvent(message.messageType, (value) => {
                resolve(value);
            })
            // setTimeout(() => {
            //     reject("消息已超时");
            // }, 20000);
            this.ws.send(JSON.stringify(message));
        })
    },

    // 发送心跳包
    ping: function () {
        this.sendMessage({messageType: "0"});
    },

    // 检查登录状态
    checkToken() {
        return this.token;
    },

    // 登录获取token
    login: function (email, password, userType) {
        return new Promise((resolve, reject) => {
            this.sendMessage({messageType: "1", email, password, userType}).then(data => {
                if (data.code) {
                    // Message.error(data.errorMsg);
                    reject()
                } else {
                    this.token = data.token;
                    this.userId = data.userId;

                    let imInfo = storage.getData("imInfo");
                    imInfo.token = data.token;
                    imInfo.userId = data.userId;
                    storage.setData("imInfo", imInfo);

                    resolve(data);
                }
            }).catch((error) => {
                Message.error(error);
                reject()
            });
        })
    },

    // Token登录，获取用户id
    tokenLogin: function (token) {
        this.sendMessage({messageType: "13", token}).then(data => {
            this.userId = data.userId;
            console.log('用户Id: ' + this.userId);
        })
    },

    // 退出登录
    loginOut: function (token, userId) {
        this.sendMessage({messageType: "2", token, userId}).then(data => {
            console.log("注销成功");
        })
    },

    // 获取好友列表
    getFriendList() {
        this.sendMessage({messageType: "14"}).then(data => {
            console.log(data);
        })
    },

    // 获取会话列表
    getConversationList() {
        return new Promise((resolve, reject) => {
            this.sendMessage({messageType: "15"}).then(data => {
                if (data.code) {
                    if (data.errorMsg === "没有数据") {
                        resolve([]);
                    } else {
                        Message.error(data.errorMsg);
                        reject(data.errorMsg);
                    }
                } else {
                    resolve(data.conversationVoList);
                }
            }).catch(error => {
                Message.error(error);
            })
        })
    },

    // 添加会话
    addConversation(targetUserId, jobId, reason="添加新会话") {
        return new Promise((resolve) => {
            this.sendMessage({messageType: "18", targetUserId, jobId, reason}).then(data => {
                console.log(data)
                if (data.code) {
                    Message.error(data.errorMsg);
                } else {
                    this.conversationId = data.conversationId;
                    resolve(data);
                }
            })
        })

    },

    /**
     * 发送聊天内容
     * @param fromUser
     * @param toUser
     * @param conversationId
     * @param content
     * @param contentType  0未知消息内容 1文本消息内容 2语音消息内容 3图片消息内容 4位置消息内容 5文件消息内容 6视频消息内容 7动态表情消息内容 8撤回消息内容
     * @param remoteMediaUrl
     */
    chatMessage(fromUser, toUser, conversationId, content, contentType, remoteMediaUrl) {
        return new Promise((resolve) => {
            let conversation = {conversationId, type: 0, target: toUser};
            let payload = {content, contentType, remoteMediaUrl};
            this.sendMessage({messageType: "3", fromUser, toUser, conversation, payload}).then(data => {
                if (data.code) {
                    Message.error(data.errorMsg);
                } else {
                    resolve(data);
                }
            }).catch(error => {
                Message.error(error);
            })
        })
    },

    /**
     * 获取历史消息列表
     * @param fromUser
     * @param toUser
     * @param jobId
     * @param page
     * @param limit
     * @param queryType 查询类型 -1:无   0:按时间查询  1：按id查询
     */
    getHistoryMessage(fromUser, toUser, jobId, page = 1, limit = 100, queryType = -1) {
        return new Promise((resolve, reject) => {
            this.sendMessage({
                messageType: "16", fromUser, toUser, jobId, page, limit, queryType
            }).then(data => {
                if (data.code) {
                    Message.error(data.errorMsg);
                    reject(data.errorMsg);
                } else {
                    resolve(data.historyChatMsg);
                }
            }).catch(error => {
                Message.error(error);
            })
        })
    },

    /**
     * 拉黑好友
     * @param targetUserId 好友Id
     * @param reason 理由
     */
    blockFriendMessage(targetUserId, reason="拉黑") {
        return new Promise((resolve) => {
            this.sendMessage({
                messageType: "4", targetUserId, reason
            }).then(data => {
                if (data.code) {
                    Message.error(data.errorMsg);
                } else {
                    resolve();
                }
            }).catch(error => {
                Message.error(error);
            })
        })
    },

    /**
     * 举报好友
     * @param targetUserId 好友Id
     * @param reason 理由
     */
    reportFriendMessage(targetUserId, reason="举报") {
        return new Promise((resolve) => {
            this.sendMessage({
                messageType: "5", targetUserId, reason
            }).then(data => {
                if (data.code) {
                    Message.error(data.errorMsg);
                } else {
                    resolve();
                }
            }).catch(error => {
                Message.error(error);
            })
        })
    },

    /**
     * 消息已读
     */
    msgAsReadMessage(fromUser, toUser, messageIds, conversationId) {
        return new Promise((resolve) => {
            this.sendMessage({
                messageType: "7", fromUser, toUser, messageIds, conversationId
            }).then(data => {
                if (data.code) {
                    Message.error(data.errorMsg);
                } else {
                    resolve();
                }
            }).catch(error => {
                Message.error(error);
            })
        })
    },

    /**
     * 消息未读
     */
    msgAsUnReadMessage(fromUser, toUser, messageIds, conversationId) {
        return new Promise((resolve) => {
            this.sendMessage({
                messageType: "8", fromUser, toUser, messageIds, conversationId
            }).then(data => {
                if (data.code) {
                    Message.error(data.errorMsg);
                } else {
                    resolve();
                }
            }).catch(error => {
                Message.error(error);
            })
        })
    },

    /**
     * 删除会话
     */
    delConversationMessage(targetUserId, conversationId) {
        return new Promise((resolve) => {
            this.sendMessage({
                messageType: "11", targetUserId, conversationId
            }).then(data => {
                if (data.code) {
                    Message.error(data.errorMsg);
                } else {
                    resolve();
                }
            }).catch(error => {
                Message.error(error);
            })
        })
    },

    /**
     * 搜索联系人会话
     */
    conversationSearchMessage(keyWords) {
        return new Promise((resolve) => {
            this.sendMessage({
                messageType: "6", keyWords
            }).then(data => {
                if (data.code) {
                    Message.error(data.errorMsg);
                } else {
                    resolve(data.voPageResult);
                }
            }).catch(error => {
                Message.error(error);
            })
        })
    },


    /**
     * 搜索联系人会话-按照职位
     * readStatus:3
     * applyStatus:1
     */
    conversationSearchByJobMessage(keyWords, readStatus, applyStatus) {
        return new Promise((resolve) => {
            this.sendMessage({
                messageType: "12", keyWords, readStatus, applyStatus
            }).then(data => {
                if (data.code) {
                    Message.error(data.errorMsg);
                } else {
                    resolve(data.voPageResult);
                }
            }).catch(error => {
                Message.error(error);
            })
        })
    },

    /**
     * 搜索职位名称[职位搜索的辅助接口]
     */
    jobNameSearchMessage(keyWords) {
        return new Promise((resolve) => {
            this.sendMessage({
                messageType: "17", keyWords
            }).then(data => {
                if (data.code) {
                    Message.error(data.errorMsg);
                } else {
                    resolve(data.voPageResult);
                }
            }).catch(error => {
                Message.error(error);
            })
        })
    }
}

export default im;
