<template>
    <div class="app-container">
        <div class="chat-container" @click="closePopup">
            <div class="user-list">
                <div style="padding-right: 20px">
                    <el-input placeholder="搜索联系人"
                              prefix-icon="el-icon-search"
                              v-model="keywords"
                              clearable
                              @keyup.enter.native="onSearch"/>
                </div>
                <!-- <el-autocomplete-->
                <!--         style="padding-right: 20px"-->
                <!--         v-model="keywords"-->
                <!--         :fetch-suggestions="querySearchAsync"-->
                <!--         placeholder="搜索联系人"-->
                <!--         @select="onSearchByJob"-->
                <!-- ></el-autocomplete>-->
                <div class="top-button-container">
                    <div class="filter-main-container" @click.stop>
                        <el-button type="primary" size="mini" @click="onFilter">筛选</el-button>
                        <el-card class="filter-container" v-if="filter">
                            <div class="filter-title-container">
                                <el-image :src="require('@/assets/close.png')" alt="" class="filter-close" @click="filter=false"/>
                            </div>
                            <div class="check-container">
                                <el-checkbox v-model="watched" @change="onFilterChange">只看未读</el-checkbox>
                                <el-checkbox v-model="apply" @change="onFilterChange">只看已投递</el-checkbox>
                            </div>
                        </el-card>
                    </div>
                    <el-button type="primary" size="mini" icon="el-icon-s-fold" style="margin-left: 20px" @click.stop="onManage">
                        {{manage?'完成':'管理'}}
                    </el-button>
                </div>
                <el-scrollbar class="friend-container" wrap-style="overflow: hidden auto; padding-right: 40px;">
                    <div class="friend-item" v-for="item in conversationList" @click.stop="onConversationClick(item)">
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
                    <div class="job-name" v-if="type==='user'">{{conversationItem.jobApplyInfoVo && conversationItem.jobApplyInfoVo.jobName}}</div>
                    <span>{{conversationItem.friendVo.name}}</span>[{{getTime(conversationItem.lastActiveTime)}}]
                    <el-link class="job-detail-link" v-if="type==='user' && conversationItem.jobApplyInfoVo" type="primary"
                             :href="`/job/${conversationItem.jobApplyInfoVo.jobId}`" :underline="false">查看职位详情
                    </el-link>
                    <el-dropdown class="job-more" v-if="type==='user'" @command="handleCommand">
                        <span>···</span>
                        <el-dropdown-menu slot="dropdown">
                            <el-dropdown-item command="toCompanyHome">查看公司主页</el-dropdown-item>
                        </el-dropdown-menu>
                    </el-dropdown>
                </div>
                <el-scrollbar ref="receive" class="content-container" wrap-style="overflow: hidden auto; padding-right: 40px;">
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
                <div class="operating-container">
                    <div class="icon-container">
                        <div class="left-container">
                            <div class="icon1" @click.stop="onEmoji">表情
                                <el-card class="emoji-card" v-if="showEmoji">
                                    <el-row :gutter="2" v-for="rowIndex in Math.ceil(emojis.length/12)" :key="rowIndex">
                                        <el-col :span="2" v-for="colIndex in 12" class="emoji-item" :key="colIndex">
                                            <div @click.stop="onEmojiSelect(emojis[(rowIndex-1)*12+(colIndex-1)])">
                                                {{emojis[(rowIndex-1)*12+(colIndex-1)]}}
                                            </div>
                                        </el-col>
                                    </el-row>
                                </el-card>
                            </div>
                            <div class="icon1" @click="onInvite" v-if="type==='company'">邀请面试</div>
                            <el-upload
                                    v-loading.fullscreen.lock="fullscreenLoading"
                                    ref="upload"
                                    :action="uploadAnnexOptions.action"
                                    :data="uploadAnnexOptions.params"
                                    :accept="uploadAnnexOptions.acceptFileType"
                                    :show-file-list="false"
                                    :on-success="handleEditorUploadSuccess"
                                    :on-error="handleEditorUploadError"
                                    :before-upload="beforeUpload">
                                <div class="icon2">附件</div>
                            </el-upload>
                            <div class="icon3" @click.stop="onWord">常用语
                                <el-card class="word-card" v-if="showWord">
                                    <div v-for="(word, index) in words" :key="index" class="word-item" @click.stop="onWordSelect(word)">
                                        {{word}}
                                    </div>
                                </el-card>
                            </div>
                        </div>
                        <div class="right-container">
                            <div class="icon4" @click="onReport">举报</div>
                            <div class="icon5" @click="onBlack">拉黑</div>
                        </div>
                    </div>
                    <div class="input-container">
                        <el-input type="textarea"
                                  ref="content"
                                  :rows="3"
                                  placeholder="请输入内容"
                                  v-model="content">
                        </el-input>
                        <el-button type="primary" class="send" @click="onSend">发送</el-button>
                    </div>
                </div>
            </div>
        </div>
        <!--        <el-dialog title="上传附件"-->
        <!--                   :visible.sync="annexDialogVisible"-->
        <!--                   width="400px">-->
        <!--            -->
        <!--            <span slot="footer" class="dialog-footer">-->
        <!--                <el-button @click="annexDialogVisible = false">取 消</el-button>-->
        <!--                <el-button type="primary" @click="annexDialogVisible = false">确 定</el-button>-->
        <!--            </span>-->
        <!--        </el-dialog>-->
    </div>
</template>

<script>
    import im from "@/utils/im"
    import {getUploadPicToken} from '@/api/upload_api'

    export default {
        name: "ChatBox",
        data() {
            return {
                type: 'user', // user:用户端     company:企业端

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
                filter: false, // 是否显示过滤框
                watched: false, // 只看未读
                apply: false, // 只看已投递
                manage: false, // 是否显示管理框

                // 上传附件
                fullscreenLoading: false,
                uploadAnnexOptions: {
                    action: '',
                    params: {},
                    fileUrl: '',
                    acceptFileType: '.*',
                    filename: '',
                    localUrl: '',
                },

                // 常用语
                showWord: false,
                words: ["你好，看了你的简历，非常感兴趣",
                    "你好，我们该岗位还在招聘",
                    "你好，看了你的简历，非常感兴趣",
                    "你好，我们该岗位还在招聘"],

                // 表情符
                emojis: ["😋", "😘", "😊", "😡", "😋", "😘", "😋", "😘", "😋", "😘", "😋", "😘",
                    "😋", "😘", "😋", "😘", "😋", "😘", "😋", "😘", "😋", "😘", "😋", "😘",
                    "😋", "😘", "😋", "😘", "😋", "😘", "😋", "😘", "😋", "😘", "😋", "😘",
                    "😋", "😘", "😋", "😘", "😋", "😘", "😋", "😘", "😋", "😘", "😋", "😘",
                    "😋", "😘", "😋", "😘", "😋", "😘", "😋", "😘", "😋", "😘", "😋", "😘"],
                showEmoji: false,
                insertPosition: 0,
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
                if(this.$route.path==="/chat") {
                    this.userId = data.userId;
                    this.token = data.token;
                    this.getConversationList();
                } else {
                    this.$emit("receiveMessage");
                }
            }).catch(() => {
                this.$router.push({path: "/login", query: {...this.$route.query, redirect: "/chat"}});
            });
            this.$emit("complete");
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
                this.messageList = [];
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
            onFilterChange() {
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

            // 关闭弹窗
            closePopup() {
                this.showWord = false;
                this.manage = false;
                this.filter = false;
                this.showEmoji = false;
            },
            // 表情
            onEmoji() {
                this.insertPosition = this.$refs.content.$refs.textarea.selectionStart;
                this.showEmoji = true;
                this.manage = false;
                this.showWord = false;
                this.filter = false;
            },

            // 表情选择
            onEmojiSelect(emoji) {
                let prefix = this.content.substring(0, this.insertPosition);
                let suffix = this.content.substring(this.insertPosition);
                this.content = prefix + emoji + suffix;
                this.showEmoji = false;
                this.$refs.content.$refs.textarea.focus();
                this.$nextTick(() => {
                    this.$refs.content.$refs.textarea.selectionStart = this.insertPosition + 2;
                    this.$refs.content.$refs.textarea.selectionEnd = this.insertPosition + 2;
                })
            },

            // 邀请面试
            onInvite() {

            },

            // 过滤按钮
            onFilter() {
                this.filter = !this.filter;
                this.manage = false;
                this.showWord = false;
                this.showEmoji = false;
            },

            // 管理
            onManage() {
                this.manage = !this.manage;
                this.filter = false;
                this.showWord = false;
                this.showEmoji = false;
            },

            // 常用语
            onWord() {
                this.showWord = true;
                this.manage = false;
                this.filter = false;
                this.showEmoji = false;
            },

            // 常用语选择
            onWordSelect(word) {
                this.content = word;
                this.showWord = false;
                this.onSend();
            },

            // 举报
            onReport() {
                this.$prompt('请输入举报理由', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                }).then(({value}) => {
                    if (value === "") {
                        value = "默认举报信息";
                    }
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

            // 接收消息处理
            receiveMessage(value) {
                if(this.$route.path==="/chat") {
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
                    if (lastMessage && lastMessage.messageId && lastMessage.status === 3 && lastMessage.toUser === this.userId) {
                        im.msgAsUnReadMessage(lastMessage.fromUser, lastMessage.toUser, [lastMessage.messageId], item.id);
                        item.unReadeCount++;
                    }
                })
                this.manage = false;
            },

            // 上传资源
            beforeUpload(file) {
                this.fullscreenLoading = true;
                return new Promise((resolve, reject) => {
                    getUploadPicToken(file.name).then(response => {
                        this.fullscreenLoading = false;
                        const {data} = response;
                        this.uploadAnnexOptions.action = data.host;
                        this.uploadAnnexOptions.params = data;
                        this.uploadAnnexOptions.fileUrl = data.host + '/' + data.key;
                        this.uploadAnnexOptions.filename = file.name;
                        const _URL = window.URL || window.webkitURL;
                        this.uploadAnnexOptions.localUrl = _URL.createObjectURL(file);
                        resolve(data)
                    }).catch(error => {
                        this.fullscreenLoading = false;
                        reject(error)
                    })
                })
            },

            // 资源上传成功
            handleEditorUploadSuccess() {
                let contentType;
                let names = this.uploadAnnexOptions.filename.split("\.");
                let imageType = ['jpg', 'jpeg', 'png', 'JPG', 'JPEG', 'PNG'];
                this.$message.success("资源上传成功");
                if (names.length > 1 && imageType.includes(names[names.length - 1])) {
                    contentType = 3;
                } else {
                    contentType = 5;
                }

                // 构建消息对象，插入接收框
                let message = {
                    fromUser: this.userId,
                    messageId: undefined,
                    payload: {
                        content: this.uploadAnnexOptions.filename,
                        contentType: contentType,
                        expireDuration: 0,
                        extra: null,
                        remoteMediaUrl: this.uploadAnnexOptions.localUrl,
                    },
                    status: 1,
                    timestamp: Date.now(),
                    toUser: this.conversationItem.friendVo.friendUserId,
                }
                this.messageList.push(message);

                // 接收框滚动到底部
                this.scrollBottom();

                // 发送消息
                im.chatMessage(this.userId, this.conversationItem.friendVo.friendUserId, this.conversationItem.id, this.uploadAnnexOptions.filename, contentType, this.uploadAnnexOptions.fileUrl).then(data => {
                    message.messageId = data.messageId;
                    this.conversationItem.lastMessage = message;
                });
            },

            // 资源上传失败
            handleEditorUploadError() {
                this.$message.error("资源上传失败")
            },

            // 菜单指令
            handleCommand(command) {
                if (command === 'toCompanyHome') {
                    this.$router.push(`/company/${this.conversationItem.jobApplyInfoVo.companyId}`);
                }
            },

            getHandledValue(num) {
                return num < 10 ? '0' + num : num;
            },

            timestampToMonthDateHoursMinutes(timeStamp) {
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
                if (minute >= 0) {
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
            margin: 0 auto;

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
                    position: relative;

                    .job-name {
                        position: absolute;
                        left: 20px;
                        top: 0;
                        line-height: 50px;
                        font-size: 15px;
                        color: #7f7f7f;
                    }

                    span {
                        font-size: 18px;
                        color: #169BD5;
                        font-weight: bold;
                        margin-right: 5px;
                    }

                    .job-detail-link {
                        position: absolute;
                        right: 45px;
                        top: 0;
                        line-height: 50px;
                        font-size: 15px;
                    }

                    .job-more {
                        position: absolute;
                        right: 0;
                        padding: 0 10px;
                        top: 0;
                        line-height: 50px;
                        font-size: 15px;

                        &:hover {
                            cursor: pointer;
                        }
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
                                    color: #66b1ff;
                                    cursor: pointer;
                                }

                                .emoji-card {
                                    position: absolute;
                                    top: -136px;
                                    left: 0;
                                    width: 400px;

                                    ::v-deep .el-card__body {
                                        padding: 5px;
                                    }

                                    .emoji-item {
                                        text-align: center;
                                        font-size: 16px;
                                        line-height: 26px;

                                        &:hover {
                                            background: #e3e3e3;
                                        }
                                    }
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
                                    color: #66b1ff;
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
                                position: relative;

                                &:hover {
                                    color: #66b1ff;
                                    cursor: pointer;
                                }

                                .word-card {
                                    position: absolute;
                                    top: -150px;
                                    left: -100px;
                                    width: 300px;

                                    .word-item {
                                        font-size: 14px;
                                        line-height: 24px;

                                        &:hover {
                                            color: #66b1ff;
                                        }
                                    }
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
                                    color: #66b1ff;
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
                                    color: #66b1ff;
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
