<template>
    <div id="app" @click="showConversation=false;">
        <div class="fake-container">
            <MainNavBar/>
            <svg-icon @click.stop="handlerChat" icon-class="chat3" class="chat"/>
            <el-scrollbar class="friend-container" wrap-style="overflow: hidden auto; padding-right: 40px;" v-if="showConversation">
                <div :class="['friend-item',{'friend-item-selected':item.selected}]" v-for="item in conversationList"
                     @click.stop="onConversationClick(item)">
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
            <div class="red-dot" v-if="showRedDot"/>
        </div>

        <transition name="fade-transform" mode="out-in">
            <keep-alive include="ChatBox">
                <router-view @complete="onComplete" @receiveMessage="onReceiveMessage" @imInitComplete="imInitComplete=true;"/>
            </keep-alive>
        </transition>
        <transition name="fade">
            <FooterView v-if="show"/>
        </transition>
        <el-backtop></el-backtop>
    </div>
</template>

<script>
    import MainNavBar from "@/components/MainNavBar";
    import FooterView from '@/components/FooterView';
    import im from "@/utils/im";

    export default {
        data() {
            return {
                show: false,
                showRedDot: false,
                imInitComplete: false,
                conversationList: [],
                showConversation: false,
            }
        },
        components: {MainNavBar, FooterView},
        name: "app",
        watch: {
            $route(newRoute, oldRouter) {
                this.show = newRoute.path === oldRouter.path;
                if (this.$route.path === '/chat') {
                    this.showRedDot = false;
                }
            }
        },
        methods: {
            onComplete() {
                this.show = true;
            },

            handlerChat() {
                if (this.imInitComplete) {
                    if (this.$route.path !== '/chat') {
                        this.showConversation = true;
                        im.getConversationList().then(data => {
                            this.conversationList = data;
                        })
                    }
                } else if (this.$route.path !== '/chat') {
                    this.$router.push({path: "/chat"})
                }
            },

            onReceiveMessage() {
                if (this.$route.path !== '/chat') {
                    this.showRedDot = true;
                    im.getConversationList().then(data => {
                        this.conversationList = data;
                    })
                }
            },

            // 点击会话
            onConversationClick(item) {
                this.showConversation = false;
                this.$router.push({path: '/chat', query: {toUser: item.friendVo.friendUserId, jobId: item.jobId}});
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
        }
    };
</script>

<style lang="scss" scoped>
    #app {
        font-family: "Avenir", Helvetica, Arial, sans-serif;
        -webkit-font-smoothing: antialiased;
        -moz-osx-font-smoothing: grayscale;
        color: #2c3e50;

        .fake-container {
            width: 1200px;
            position: relative;
            margin: 0 auto;
        }

        .chat {
            position: absolute;
            top: 20px;
            right: 176px;
            width: 60px;
            height: 60px;
            padding: 10px;

            &:hover {
                cursor: pointer;
                color: #409eff;
            }
        }

        .friend-container {
            flex: 1;
            margin-top: 20px;
            overflow-y: auto;
            position: absolute;
            top: 48px;
            right: 285px;
            width: 290px;
            height: 240px;
            padding: 5px 0 5px 5px;
            background: #cccccccc;
            z-index: 99;

            .friend-item {
                width: 300px;
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
                            text-overflow: ellipsis;
                            white-space: nowrap;
                            overflow: hidden;
                            width: 80px;
                        }

                        .friend-title {
                            color: #909399;
                            font-size: 12px;
                            flex: 1;
                            padding: 0 5px;
                            text-overflow: ellipsis;
                            white-space: nowrap;
                            overflow: hidden;
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
                            flex: 1;
                            padding: 0 5px;
                            text-overflow: ellipsis;
                            white-space: nowrap;
                            overflow: hidden;
                        }

                        .friend-state {
                            color: #606266;
                            font-size: 12px;
                        }
                    }
                }
            }

            .friend-item-selected {
                background-color: #e3e3e3;
            }
        }

        .red-dot {
            position: absolute;
            top: 28px;
            right: 239px;
            background: red;
            width: 10px;
            height: 10px;
            border-radius: 50%;
        }

        .avatar {
            min-width: 40px;
            width: 40px;
            height: 40px;
            display: flex;
            align-items: center;
            justify-content: center;
        }
    }
</style>
