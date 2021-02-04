<template>
    <div class="nav-container">
        <div class="nav-left-container">
            <div class="logo-container">
                <router-link class="logo-image" to="/"></router-link>
            </div>
            <el-menu :router="true"
                     mode="horizontal"
                     :default-active="activeIndex"
                     class="menu-container">
                <el-menu-item class="nav-item" index="/">首页</el-menu-item>
                <el-menu-item class="nav-item" index="/manage-job">职位管理</el-menu-item>
                <el-menu-item class="nav-item" index="/manage-resume">简历管理</el-menu-item>
                <el-menu-item class="nav-item" index="/manage-search">简历搜索</el-menu-item>
                <el-menu-item class="nav-item" index="/activity/publish">活动管理</el-menu-item>
            </el-menu>
        </div>

        <div class="nav-right-container">
            <!-- 未登录 -->
            <div class="user-container" v-if="!token">
                <div class="register-button">
                    <el-button type="primary" @click="routeTo('/register')" size="small">注册</el-button>
                </div>
                <div class="login-button">
                    <el-button type="primary" @click="routeTo('/login')" size="small">登录</el-button>
                </div>
            </div>
            <!-- 已登录 -->
            <div class="user-container" v-else>
                <svg-icon @click="routeTo('/chat')"
                          icon-class="home-message"
                          class="chat"
                          clickable/>

                <!-- 系统通知 -->
                <el-popover placement="bottom-end"
                            width="300"
                            trigger="hover"
                            @show="getMessageList"
                            title="系统通知">
                    <!-- 内容 -->
                    <div v-if="messageList.length !== 0">
                        <div class="message-item" v-for="message in messageList" :key="message.id">
                            <el-badge is-dot v-if="message.readFlag === 0"/>
                            {{message.content}}
                            <el-link v-if="message.url" :href="message.url" :underline="false"> 查看</el-link>
                        </div>
                    </div>
                    <div v-else class="message-text">暂无新消息</div>

                    <!-- 按钮 -->
                    <div class="message-text">
                        <el-link type="primary" :underline="false" @click="routeTo('messages')">查看全部</el-link>
                    </div>

                    <!-- 图标 -->
                    <el-link :underline="false" class="nav-message" slot="reference" @click="routeTo('messages')">
                        <el-badge v-if="messageList.length !== 0" is-dot>
                            <svg-icon icon-class="home-notification" style="width: 25px; height: 28px; margin-bottom: 5px" clickable/>
                        </el-badge>
                        <svg-icon v-else icon-class="home-notification" style="width: 25px; height: 28px; margin-bottom: 5px" clickable/>
                    </el-link>
                </el-popover>

                <!-- 用户头像 -->
                <el-dropdown>
                    <el-avatar :size="35" icon="el-icon-user-solid" :src="avatar" style="cursor: pointer"></el-avatar>
                    <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item>
                            <el-link class="no-decoration" :underline="false" href="/verify" v-if="status === 0"> 审核资料</el-link>
                        </el-dropdown-item>
                        <el-dropdown-item>
                            <el-link class="no-decoration" :underline="false" href="/edit-company" v-if="status !== 0">公司主页</el-link>
                        </el-dropdown-item>
                        <el-dropdown-item>
                            <el-link class="no-decoration" :underline="false" href="/modify-pwd">修改密码</el-link>
                        </el-dropdown-item>
                        <el-dropdown-item @click.native="handleLogout" class="text-danger">退出登录</el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
            </div>
        </div>
    </div>
</template>

<!--<template>-->
<!--    <div class="app-nav">-->
<!--        <el-row align="middle" type="flex">-->
<!--            <el-col :span="5">-->
<!--                <div class="logo">-->
<!--                    <el-link class="logo-text" type="primary" href="/" :underline="false">{{$t('app_name')}} 企业端</el-link>-->
<!--                </div>-->
<!--            </el-col>-->
<!--            <el-col :span="10">-->
<!--                <el-menu :router="true"-->
<!--                         :default-active="activeIndex"-->
<!--                         mode="horizontal"-->
<!--                         v-if="token && token !== ''">-->
<!--                    <el-menu-item class="nav-item" index="/manage-job">职位管理</el-menu-item>-->
<!--                    <el-menu-item class="nav-item" index="/manage-resume">简历管理</el-menu-item>-->
<!--                    <el-menu-item class="nav-item" index="/manage-search">简历搜索</el-menu-item>-->
<!--                    <el-menu-item class="nav-item" index="/activity/publish">活动管理</el-menu-item>-->
<!--                </el-menu>-->
<!--            </el-col>-->
<!--            <el-col :span="9">-->
<!--                <div class="right-container" v-if="token === undefined || token === ''">-->
<!--                    <el-link :underline="false" class="p-4 mr-2" @click="$router.push('/register')">-->
<!--                        <b>免费加入</b>-->
<!--                    </el-link>-->
<!--                    <el-button type="primary" @click="$router.push('/login')">登录</el-button>-->
<!--                </div>-->
<!--                <div class="right-container" v-else>-->
<!--                    <el-popover placement="bottom-end"-->
<!--                                width="300"-->
<!--                                trigger="hover"-->
<!--                                @show="getMessageList"-->
<!--                                title="系统通知">-->
<!--                        <div class="message-list" v-if="newMessageList && newMessageList.length !== 0">-->
<!--                            <div class="message-item" v-for="message in newMessageList" :key="message.id">-->
<!--                                <el-badge type="success" is-dot v-if="message.readFlag === 0 && message.msgType === 1"/>-->
<!--                                <el-badge is-dot v-if="message.readFlag === 0 && message.msgType === 0"/>-->
<!--                                {{message.content}}-->
<!--                                <el-link v-if="message.url && message.url != ''"-->
<!--                                         :href="message.url"-->
<!--                                         :underline="false">-->
<!--                                    查看-->
<!--                                </el-link>-->
<!--                            </div>-->
<!--                        </div>-->
<!--                        <div class="text-center p-2" v-else>暂无新消息</div>-->
<!--                        <div class="text-center p-2">-->
<!--                            <el-link type="primary" :underline="false" @click="gotoMessageList">查看全部</el-link>-->
<!--                        </div>-->
<!--                        <el-link :underline="false"-->
<!--                                 class="mr-4 p-2 nav-message"-->
<!--                                 slot="reference"-->
<!--                                 @click="gotoMessageList">-->
<!--                            <el-badge is-dot v-if="messageCount !== 0">-->
<!--                                <i class="el-icon-message-solid"></i>-->
<!--                            </el-badge>-->
<!--                            <i class="el-icon-message-solid" v-else></i>-->
<!--                        </el-link>-->
<!--                    </el-popover>-->
<!--                    <el-dropdown style="vertical-align: middle;">-->
<!--                        <span class="el-dropdown-link">-->
<!--                          <el-avatar :size="35" icon="el-icon-user-solid"></el-avatar>-->
<!--                        </span>-->
<!--                        <el-dropdown-menu slot="dropdown">-->
<!--                            <el-dropdown-item>-->
<!--                                <el-link class="no-decoration"-->
<!--                                         :underline="false"-->
<!--                                         href="/verify"-->
<!--                                         v-if="status === 0">-->
<!--                                    审核资料-->
<!--                                </el-link>-->
<!--                            </el-dropdown-item>-->
<!--                            <el-dropdown-item>-->
<!--                                <el-link class="no-decoration" :underline="false" href="/edit-company" v-if="status !== 0">公司主页</el-link>-->
<!--                            </el-dropdown-item>-->
<!--                            <el-dropdown-item>-->
<!--                                <el-link class="no-decoration" :underline="false" href="/modify-pwd">修改密码</el-link>-->
<!--                            </el-dropdown-item>-->
<!--                            <el-dropdown-item @click.native="handleLogout" class="text-danger">退出登录</el-dropdown-item>-->
<!--                        </el-dropdown-menu>-->
<!--                    </el-dropdown>-->
<!--                </div>-->
<!--            </el-col>-->
<!--        </el-row>-->
<!--    </div>-->
<!--</template>-->

<script>
    import {mapGetters, mapMutations} from "vuex";
    import {storage} from "@/utils/storage";
    import {curRelativePath} from "@/utils/common";

    export default {
        name: "MainNavBar",
        computed: {
            ...mapGetters(["token", "avatar", "name", "messageCount", "status"])
        },
        data() {
            return {
                activeIndex: "/manage-job",
                messageList: [], // 消息列表
            };
        },
        watch: {
            $route() {
                if (this.isJob()) {
                    this.activeIndex = "/manage-job";
                } else if (this.isResumeManage()) {
                    this.activeIndex = "/manage-resume";
                } else if (this.isResumeSearch()) {
                    this.activeIndex = "/manage-search";
                } else if (this.isActivity()) {
                    this.activeIndex = "/activity/publish";
                } else if (this.isHomePage()) {
                    this.activeIndex = "/";
                }
                this.getUnReadMessageCount();
            },
        },
        created() {
            this.activeIndex = this.$route.path;
            this.getUnReadMessageCount();
        },
        methods: {
            ...mapMutations({
                setMessageCount: "setting/MESSAGE_COUNT"
            }),

            // 获取消息
            getMessageList() {
                if (this.token) {
                    this.$axios.get("/message/list", {
                        params: {page: 1, limit: 5,}
                    }).then((response) => {
                        this.messageList = response.data.list;
                    });
                }
            },

            // 获取未读消息
            getUnReadMessageCount() {
                if (this.token) {
                    this.$axios.get("/message/unread-count").then((response) => {
                        this.setMessageCount(response.data);
                    });
                }
            },

            // 退出
            handleLogout() {
                this.$store.dispatch("user/LOGOUT").then(() => {
                    storage.removeLoginInfo();
                    this.$router.push({path: "/"});
                });
            },

            // 当前路由是否激活homepage
            isHomePage() {
                return this.$route.path === "/";
            },

            // 当前路由是否激活职位管理
            isJob() {
                return this.$route.path === "/manage-job";
            },

            // 当前路由是否激活简历管理
            isResumeManage() {
                return this.$route.path === "/manage-resume";

            },

            // 当前路由是否激活简历搜索
            isResumeSearch() {
                return this.$route.path === "/manage-search";

            },

            // 当前路由是否激活活动管理
            isActivity() {
                return this.$route.path === "/activity/publish";
            },

            // 路由跳转
            routeTo(path) {
                if (path === "/login") {
                    this.$router.push({path, query: {redirect: curRelativePath()}});
                } else {
                    this.$router.push(path);
                }
            },
        }
    };
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
    .register-button {
        margin-left: 41px;
        margin-right: 25px;

        /deep/ .el-button {
            background: #ffffff;
            border-radius: 15px;
            border: 1px solid #4895ef;
            height: 30px;
            font-size: 18px;
            font-weight: 400;
            color: #4895ef;
            line-height: 25px;
            display: flex;
            align-items: center;
            justify-content: center;
        }
    }

    .login-button {
        /deep/ .el-button {
            background: #4895ef;
            height: 30px;
            box-shadow: 0 2px 4px 0 #a7c7f8;
            border-radius: 15px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 18px;
            font-weight: 400;
            color: #ffffff;
            line-height: 25px;
        }
    }

    /deep/ .el-icon-search {
        color: #4895ef;
    }

    /deep/ .el-button.is-circle {
        width: 25px;
        height: 25px;
        background: #ffffff;
        box-shadow: 0 2px 4px 0 #afc1ef;
        display: flex;
        justify-content: center;
        align-items: center;
    }

    /deep/ .el-input-group--append .el-input__inner, .el-input-group__prepend {
        border-radius: 15px;
    }

    /deep/ .el-input-group__append, .el-input-group__prepend {
        border: 0;
        background-color: transparent;
        position: absolute;
        top: 40%;
        right: 0;
        padding: 0 10px;
    }

    /deep/ .el-menu--horizontal > .el-menu-item.is-active {
        border-bottom: 3px solid #4895ef;
        font-size: 18px;
        font-weight: 500;
        color: #4895ef !important;
        line-height: 25px;
        display: flex;
        align-items: center;
    }

    /deep/ .el-menu--horizontal > .el-menu-item {
        font-size: 18px;
        font-weight: 500;
        color: #333333;
        line-height: 25px;
        display: flex;
        align-items: center;
        height: 80px;
    }

    /deep/ .el-menu.el-menu--horizontal {
        border-bottom: 0;
    }

    /deep/ .el-input__inner {
        height: 30px;
        background: rgba(72, 149, 239, 0.11);
        border-radius: 15px;
        border: 0;
    }

    .message-text {
        text-align: center;
        padding: 16px 10px 6px;
    }

    .nav-container {
        padding-top: 10px;
        display: flex;
        align-items: center;
        justify-content: space-between;

        .nav-left-container {
            display: flex;

            .logo {
                font-size: 30px;
            }

            .logo-container {
                .logo-image {
                    display: block;
                    width: 293px;
                    min-width: 293px;
                    height: 80px;
                    background: url("../assets/logo.png") no-repeat;
                    background-size: contain;
                    background-position-y: center;
                }
            }

            .menu-container {
                display: flex;
                align-items: center;
                justify-content: space-around;
                flex: 1;
                margin: 0 30px;

                .nav-item {
                    font-size: 18px;
                }
            }
        }

        .nav-right-container {
            display: flex;
            align-items: center;

            .user-container {
                display: flex;
                align-items: center;

                .chat {
                    width: 30px;
                    height: 30px;
                    margin-left: 10px;
                    cursor: pointer;
                }

                .join {
                    padding: 0 14px;
                }

                .nav-message {
                    font-size: 22px;
                    padding: 0 16px;
                }
            }
        }
    }

    .message-item {
        padding: 10px 5px;
        border-bottom: 1px solid #EEE;
        line-height: 1.5em;
        font-size: 15px;
        color: #888;
    }

    @media screen and (max-width: 850px) {
        .nav-container {
            flex-direction: column-reverse;

            .nav-left-container {
                .logo-image {
                    display: none;
                }
            }
        }
    }

    @media screen and (max-width: 410px) {
        .nav-container {
            flex-direction: column-reverse;

            .nav-left-container {
                .logo {
                    font-size: 22px;
                }

                .menu-container {
                    .nav-item {
                        font-size: 16px;
                    }
                }

                .nav-right-container {
                    padding: 0 15px;
                }
            }

            .nav-right-container {
                padding: 0 15px;
            }
        }
    }

</style>
