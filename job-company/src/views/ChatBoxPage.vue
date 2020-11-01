<template>
    <div class="app-container">
        <div class="chat-container">
            <div class="user-list" @click="filter=false">
                <div style="padding-right: 20px">
                    <el-input placeholder="搜索联系人"
                              prefix-icon="el-icon-search"
                              v-model="keywords"
                              clearable
                              @keyup.enter.native="onSearch"/>
                </div>

                <!--                <el-autocomplete-->
                <!--                        style="padding-right: 20px"-->
                <!--                        v-model="keywords"-->
                <!--                        :fetch-suggestions="querySearchAsync"-->
                <!--                        placeholder="搜索联系人"-->
                <!--                        @select="onSearchByJob"-->
                <!--                ></el-autocomplete>-->
                <div class="top-button-container">
                    <div class="filter-main-container">
                        <el-button type="primary" size="mini" @click.stop="filter=!filter;manage=false;">筛选</el-button>
                        <el-card class="filter-container" v-if="filter" @click.stop="filter=true">
                            <div class="filter-title-container">
                                <el-image :src="require('@/assets/close.png')" alt="" class="filter-close" @click="filter=false"/>
                            </div>
                            <div class="check-container" @click.stop>
                                <el-checkbox v-model="watched" @change="onFilter">只看未读</el-checkbox>
                                <el-checkbox v-model="apply" @change="onFilter">只看已投递</el-checkbox>
                            </div>
                        </el-card>
                    </div>
                    <el-button type="primary" size="mini" icon="el-icon-s-fold" style="margin-left: 20px" @click="manage=!manage; filter=false;">
                        {{manage?'完成':'管理'}}
                    </el-button>
                </div>
                <el-scrollbar class="friend-container" wrap-style="overflow: hidden auto; padding-right: 40px;">
                    <div class="friend-item" v-for="item in conversationList" @click="onConversationClick(item)">
                        <el-checkbox v-model="item.checked" class="friends-checked" v-if="manage"></el-checkbox>
                        <div style="position: relative">
                            <el-image :src="item.friendVo.avatar" alt="" class="avatar">
                                <div slot="error" class="image-slot">
                                    <i class="el-icon-picture-outline"></i>
                                </div>
                            </el-image>
                            <span class="circle-number" v-if="item.unReadeCount!==0">{{item.unReadeCount}}</span>
                        </div>
                        <div class="friend-right-container">
                            <div class="top-content">
                                <div class="friend-name">{{item.friendVo.name}}</div>
                                <div class="friend-title">{{item.jobApplyInfoVo && item.jobApplyInfoVo.jobName}}</div>
                                <div class="friend-title">{{timestampToMonthDateHoursMinutes(item.lastMessage && item.lastMessage.timestamp)}}</div>
                            </div>
                            <div class="bottom-content">
                                <div class="friend-title">{{item.lastMessage && item.lastMessage.payload.content}}</div>
                                <div class="friend-state">{{(item.jobApplyInfoVo && item.jobApplyInfoVo.jobStatus)?'已投':'未投'}}</div>
                            </div>
                        </div>
                    </div>
                </el-scrollbar>
                <div class="select-container" v-if="manage">
                    <el-button type="danger" size="mini" @click="onMessageDelete">删除</el-button>
                    <el-button type="primary" size="mini" @click="onMessageRead">已读</el-button>
                    <el-button type="primary" size="mini" @click="onMessageUnread">未读</el-button>
                </div>
            </div>
            <div class="chat-detail" v-if="conversationItem">
                <div class="detail-title">
                    <span>{{conversationItem.friendVo.name}}</span>{{getTime(conversationItem.lastActiveTime)}}
                </div>
                <el-scrollbar ref="receive" class="content-container" wrap-style="overflow: hidden auto; padding-right: 40px;">
                    <div v-for="item in messageList">
                        <div class="self-container" v-if="item.fromUser===userId">
                            <div class="self-content">{{item.payload.content}}
                                <div class="self-arrow"/>
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
                            <div class="others-content">{{item.payload.content}}
                                <div class="others-arrow"/>
                            </div>
                        </div>
                    </div>
                </el-scrollbar>
                <div class="operating-container">
                    <div class="icon-container">
                        <div class="left-container">
                            <div class="icon1" @click="onInvite">邀请面试</div>
                            <div class="icon2" @click="onAnnex">附件</div>
                            <div class="icon3" @click="onWord">常用语</div>
                        </div>
                        <div class="right-container">
                            <div class="icon4" @click="onReport">举报</div>
                            <div class="icon5" @click="onBlack">拉黑</div>
                        </div>
                    </div>
                    <div class="input-container">
                        <el-input type="textarea"
                                  :rows="3"
                                  placeholder="请输入内容"
                                  v-model="content">
                        </el-input>
                        <el-button type="primary" class="send" @click="onSend">发送</el-button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import im from "@/utils/im"

    export default {
        name: "ChatBox",
        data() {
            return {
                // IM信息
                userId: undefined,
                token: undefined,
                conversationList: [], // 会话列表
                messageList: [], // 选中会话的，消息列表
                messageTotal: 0, // 选中会话的消息总数
                conversationItem: undefined, // 选中的会话信息

                // 消息
                content: '', // 输入文本内容
                keywords: '', // 搜索联系人
                manage: false, // 选择
                watched: false, // 只看未读
                apply: false, // 只看已投递
                filter: false, // 过滤框
            };
        },

        watched: {
            manage(newValue) {
                if (!newValue) {
                    this.conversationList.forEach(item => {
                        item.checked = false;
                    })
                }
            }
        },

        mounted() {
            // 初始化webSocket
            im.init(this.receiveMessage).then((data) => {
                this.userId = data.userId;
                this.token = data.token;
                this.getConversationList();
            }).catch(() => {
                this.$router.push({path: "/login", query: {...this.$route.query, redirect: "/chat"}});
            });
        },
        methods: {
            // 获取会话列表
            getConversationList() {
                im.getConversationList().then(data => {
                    this.conversationList = data;

                    if (this.$route.query.toUser) { // 打开与指定联系人的会话
                        this.conversationList.forEach(item => {
                            if (item.friendVo.friendUserId === this.$route.query.toUser) {
                                this.onConversationClick(item);
                            }
                        })
                        // 会话不存在，添加新会话
                        if (!this.conversationItem) {
                            im.addConversation(this.$route.query.toUser, this.$route.query.jobId).then(() => {
                                this.getConversationList();
                            })
                        }
                    } else { // 默认打开第一个会话
                        this.onConversationClick(data[0]);
                    }
                }).catch(() => {
                });
            },

            // 查看会话消息
            onConversationClick(item) {
                this.conversationItem = item;
                im.getHistoryMessage(this.userId, item.friendVo.friendUserId, item.jobId).then(data => {
                    data.list.forEach(item => {
                        this.messageList.unshift(item);
                    })
                    this.messageTotal = data.total;

                    // 标记已读消息
                    if (item.unReadeCount > 0) {
                        let messageIds = [];
                        for (let i = this.messageList.length - 1; i > 0 && messageIds.length < item.unReadeCount; i--) {
                            if (this.messageList[i].toUser === this.userId) {
                                messageIds.push(this.messageList[i].messageId);
                            }
                        }
                        im.msgAsReadMessage(item.friendVo.friendUserId, this.userId, messageIds, item.id).then(() => {
                            item.unReadeCount = 0;
                        });
                    }

                    this.scrollBottom();
                })
            },

            // 搜索职位名称
            querySearchAsync(queryString, cb) {
                console.log(queryString);
                im.jobNameSearchMessage(queryString).then(data => {
                    cb([1, 2, 3]);
                });
            },

            // 按职位搜索联系人
            onSearchByJob() {
                console.log(this.keywords);
                if (this.keywords) {
                    im.conversationSearchByJobMessage(this.keywords).then(data => {

                    })
                }
            },

            // 按名称搜索联系人
            onSearch() {
                console.log(this.keywords);
                if (this.keywords) {
                    im.conversationSearchMessage(this.keywords).then(data => {
                        this.conversationList = data.list;
                    })
                } else {
                    this.getConversationList();
                }
            },

            // 会话过滤
            onFilter() {
                console.log(this.watched);
                console.log(this.apply);
                if (this.watched || this.apply) {
                    im.conversationSearchByJobMessage("", this.watched ? 3 : 0, this.apply ? 1 : 0).then(data => {
                        this.conversationList = data.list;
                    })
                } else {
                    this.getConversationList();
                }
            },

            // 邀请面试
            onInvite() {

            },

            // 附件
            onAnnex() {

            },

            // 常用语
            onWord() {

            },

            // 举报
            onReport() {
                this.$prompt('请输入举报理由', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                }).then(({value}) => {
                    im.reportFriendMessage(this.conversationItem.friendVo.friendUserId, value).then(() => {
                        this.$message({
                            type: 'success',
                            message: '已提交举报信息'
                        });
                    })
                }).catch(() => {
                });
            },

            // 拉黑
            onBlack() {
                this.$confirm('确认拉黑该公司HR?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    im.blockFriendMessage(this.conversationItem.friendVo.friendUserId).then(() => {
                        this.$message({
                            type: 'success',
                            message: '已拉黑!'
                        });
                    })
                }).catch(() => {
                });
            },

            // 发送
            onSend(e) {
                if (this.content !== '') {
                    // 保存消息，并清空发送框
                    let content = this.content;
                    this.content = '';
                    console.log(e);

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
                }
            },

            receiveMessage(value) {
                // 消息来自已经打开的窗口
                if (value.fromUser === this.conversationItem.friendVo.friendUserId) {
                    im.msgAsReadMessage(value.fromUser, value.toUser, [value.messageId], value.conversation.conversationId);

                    // 构建消息对象，插入接收框
                    value.timestamp = Date.now();
                    this.messageList.push(value);
                    this.scrollBottom();

                    // 更新会话列表
                    this.conversationItem.lastMessage = value;
                } else {
                    this.getConversationList();
                }
            },

            // 接收框滚动到底部
            scrollBottom() {
                // 滚动到底部
                this.$nextTick(() => {
                    this.$refs.receive.wrap.scrollTop = this.$refs.receive.wrap.scrollHeight;
                })
            },

            // 删除会话
            onMessageDelete() {
                let conversationList = this.conversationList.filter(item => item.checked);
                conversationList.forEach(item => {
                    im.delConversationMessage(item.friendVo.friendUserId, item.id)
                })
                this.getConversationList();
                this.manage = false;
            },

            // 标记已读
            onMessageRead() {
                let conversationList = this.conversationList.filter(item => item.checked);
                conversationList.forEach(item => {
                    // 获取全部历史消息，并将最后n个未读消息标记为已读
                    console.log(123);
                    im.getHistoryMessage(this.userId, item.friendVo.friendUserId, item.jobId).then(data => {
                        if (item.unReadeCount > 0) {
                            let messageIds = [];
                            for (let i = 0; i < data.list.length && messageIds.length < item.unReadeCount; i++) {
                                if (data.list[i].toUser === this.userId) {
                                    messageIds.push(data.list[i].messageId);
                                }
                            }
                            im.msgAsReadMessage(item.friendVo.friendUserId, this.userId, messageIds, item.id).then(() => {
                                item.unReadeCount = 0;
                            });
                        }
                    })

                    // 将最后一条消息标记为已读
                    // let lastMessage = item.lastMessage;
                    // if (lastMessage && lastMessage.messageId) {
                    //     im.msgAsReadMessage(lastMessage.fromUser, lastMessage.toUser, [lastMessage.messageId], item.id);
                    // }
                })
                this.manage = false;
            },

            // 标记未读
            onMessageUnread() {
                let conversationList = this.conversationList.filter(item => item.checked);
                conversationList.forEach(item => {
                    let lastMessage = item.lastMessage;
                    if (lastMessage && lastMessage.messageId) {
                        im.msgAsUnReadMessage(lastMessage.fromUser, lastMessage.toUser, [lastMessage.messageId], item.id);
                    }
                })
                this.manage = false;
            },

            getHandledValue(num) {
                return num < 10 ? '0' + num : num;
            },

            timestampToMonthDateHoursMinutes (timeStamp) {
                if (timeStamp) {
                    const d = new Date(parseInt(timeStamp));
                    const month = this.getHandledValue(d.getMonth() + 1);
                    const date = this.getHandledValue(d.getDate());
                    const hours = this.getHandledValue(d.getHours());
                    const minutes = this.getHandledValue(d.getMinutes());
                    return month + '/' + date + ' ' + hours + ":" + minutes;
                } else {
                    return '';
                }
            },

            getTime(minute) {
                if (minute) {
                    if (minute < 60) {
                        return minute + "分钟前活跃";
                    } else if (minute < 60 * 24) {
                        return Math.floor(parseInt(minute) / 60) + "小时前活跃";
                    } else {
                        return Math.floor(parseInt(minute) / 60 / 24) + "天前活跃";
                    }
                } else {
                    return "活跃时间未知"
                }
            }
        }
    }
</script>

<style scoped lang="scss">
    .app-container {
        min-height: calc(100vh - 527px);

        .chat-container {
            width: 1200px;
            height: 678px;
            display: flex;
            margin: 50px auto 0;

            .user-list {
                width: 32%;
                height: 100%;
                background-color: #eceae8;
                padding: 20px 0 0 20px;
                display: flex;
                flex-direction: column;

                .top-button-container {
                    width: 100%;
                    display: flex;
                    margin-top: 20px;

                    .filter-main-container {
                        position: relative;

                        .filter-container {
                            position: absolute;
                            width: 240px;
                            height: 90px;
                            top: 40px;
                            left: 0;
                            z-index: 1;

                            /deep/ .el-card__body {
                                width: 240px;
                                height: auto;
                                align-items: flex-start;
                                flex-wrap: wrap;
                                padding: 10px 15px;
                            }

                            .filter-title-container {
                                width: 100%;
                                height: 20px;
                                text-align: right;

                                .filter-close {
                                    width: 20px;
                                    height: 20px;
                                }
                            }

                            .check-container {
                                width: 100%;
                                height: 30px;
                                display: flex;
                                align-items: center;
                                margin-top: 10px;
                            }
                        }
                    }
                }

                .friend-container {
                    width: 100%;
                    flex: 1;
                    margin-top: 20px;
                    overflow-y: auto;

                    .friend-item {
                        width: calc(100% + 40px);
                        height: auto;
                        display: flex;
                        align-items: center;
                        justify-content: space-between;
                        border-bottom: 1px solid white;
                        padding: 8px 20px;
                        margin: 0 -20px;

                        &:hover {
                            cursor: pointer;
                            background-color: #f3f3f3;
                        }

                        .friends-checked {
                            margin-right: 6px;
                            margin-top: 6px;
                        }

                        .circle-number {
                            width: 12px;
                            height: 12px;
                            border-radius: 50%;
                            background-color: #FF4F4A;
                            position: absolute;
                            right: -4px;
                            top: -4px;
                            color: #ffffff;
                            font-size: 8px;
                            text-align: center;
                            line-height: 12px;
                        }

                        .friend-right-container {
                            flex: 1;
                            margin-left: 6px;

                            .top-content {
                                width: 100%;
                                height: 25px;
                                display: flex;
                                align-items: center;
                                justify-content: space-between;

                                .friend-name {
                                    font-size: 16px;
                                    color: #5C6984;
                                }

                                .friend-title {
                                    color: #909399;
                                    font-size: 12px;
                                }
                            }

                            .bottom-content {
                                width: 100%;
                                height: 20px;
                                display: flex;
                                align-items: center;
                                justify-content: space-between;

                                .friend-title {
                                    color: #909399;
                                    font-size: 12px;
                                }

                                .friend-state {
                                    color: #606266;
                                    font-size: 12px;
                                }
                            }
                        }
                    }
                }

                .select-container {
                    width: 100%;
                    height: 50px;
                    display: flex;
                    align-items: center;
                    justify-content: space-between;
                    padding: 0 30px;
                }
            }

            .chat-detail {
                flex: 1;
                height: 100%;
                background-color: rgba(242, 242, 242, 1);
                padding: 2px;
                display: flex;
                flex-direction: column;

                .detail-title {
                    width: 100%;
                    height: 50px;
                    line-height: 50px;
                    text-align: center;
                    font-size: 10px;
                    color: #909399;

                    span {
                        font-size: 18px;
                        color: #169BD5;
                        font-weight: bold;
                        margin-right: 5px;
                    }
                }


                .content-container {
                    width: 100%;
                    height: 500px;
                    background-color: #f5f5f5;
                    padding: 20px;
                    flex: 1;

                    .self-container {
                        width: 100%;
                        height: auto;
                        display: flex;
                        justify-content: flex-end;
                        margin-top: 10px;

                        .self-content {
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
                    }

                    .others-container {
                        width: 100%;
                        height: auto;
                        display: flex;
                        align-items: flex-start;
                        margin-top: 20px;

                        .others-content {
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
                    }
                }

                .operating-container {
                    width: 100%;
                    height: auto;
                    padding: 0 10px 10px;

                    .icon-container {
                        width: 100%;
                        height: 40px;
                        display: flex;
                        align-items: center;
                        justify-content: space-between;
                        position: relative;

                        .left-container {
                            display: flex;
                            align-items: center;
                            height: 100%;

                            .icon1 {
                                font-size: 14px;
                                color: #606266;
                                background: url("../assets/invite.png") no-repeat;
                                background-size: 20px 20px;
                                padding-left: 20px;
                                margin-right: 12px;
                                line-height: 20px;

                                &:hover {
                                    color: #4545f5;
                                    cursor: pointer;
                                }
                            }

                            .icon2 {
                                font-size: 14px;
                                color: #606266;
                                background: url("../assets/annex.png") no-repeat;
                                background-size: 16px 16px;
                                padding-left: 20px;
                                margin-right: 12px;
                                line-height: 16px;

                                &:hover {
                                    color: #4545f5;
                                    cursor: pointer;
                                }
                            }

                            .icon3 {
                                font-size: 14px;
                                color: #606266;
                                background: url("../assets/word.png") no-repeat;
                                background-size: 15px 16px;
                                padding-left: 18px;
                                margin-right: 12px;
                                line-height: 16px;

                                &:hover {
                                    color: #4545f5;
                                    cursor: pointer;
                                }
                            }
                        }

                        .right-container {
                            display: flex;
                            align-items: center;
                            height: 100%;

                            .icon4 {
                                font-size: 14px;
                                color: #606266;
                                background: url("../assets/report.png") no-repeat;
                                background-size: 15px 16px;
                                padding-left: 18px;
                                margin-right: 12px;
                                line-height: 16px;

                                &:hover {
                                    color: #4545f5;
                                    cursor: pointer;
                                }
                            }

                            .icon5 {
                                font-size: 14px;
                                color: #606266;
                                background: url("../assets/black.png") no-repeat;
                                background-size: 15px 16px;
                                padding-left: 18px;
                                margin-right: 12px;
                                line-height: 16px;

                                &:hover {
                                    color: #4545f5;
                                    cursor: pointer;
                                }
                            }
                        }
                    }

                    .input-container {
                        width: 100%;
                        display: flex;
                        align-items: center;
                    }

                    .send {
                        height: 76px;
                        width: 60px;
                    }
                }
            }
        }
    }

    .friend-container::-webkit-scrollbar {
        display: none;
    }

    ::v-deep .el-scrollbar__wrap::-webkit-scrollbar {
        display: none;
    }

    .avatar {
        min-width: 40px;
        width: 40px;
        height: 40px;
        display: flex;
        align-items: center;
        justify-content: center;
    }

</style>
