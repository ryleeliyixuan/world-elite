<template>
    <div class="nav_container">
        <div class="nav_left_container">

            <router-link class="logo-image" to="/">
                <img style="width: 100%;" src="../assets/logo.jpg"/>
            </router-link>
            <!-- <el-link class="logo-image" type="primary" href="/" :underline="false" >-->
            <!--    <img style="width: 100%;" src="../assets/logo.jpg"/>   -->
            <!-- </el-link>-->
            <!-- <el-link class="logo" type="primary" href="/" :underline="false" >{{$t('app_name')}}</el-link>-->
            <el-menu :router="true"
                     mode="horizontal"
                     :default-active="activeIndex"
                     @select="handleSelect"
                     class="menu-container">
                <el-menu-item class="nav-item" index="/job-list">职位</el-menu-item>
                <el-menu-item class="nav-item" index="/wiki-card">百科</el-menu-item>
                <el-menu-item class="nav-item" index="/activity-list">活动</el-menu-item>
            </el-menu>
        </div>

        <div class="nav_right_container">
            <el-input v-model="keyword"
                      class="input-search"
                      :placeholder="searchPlaceHolder"
                      @keyup.enter.native="handleSearch">
                <i slot="suffix" class="el-input__icon el-icon-search" @click="handleSearch"></i>
            </el-input>

            <!-- 未登录 -->
            <div class="user_container" v-if="token === undefined || token === ''">
                <el-link :underline="false" class="join" @click="$router.push('/register')">
                    <b>立即加入</b>
                </el-link>
                <el-button type="primary" @click="$router.push('/login')" size="small">登录</el-button>
                <!--                <el-link :underline="false" class="icon-company" :href="companyHomeUrl" target="_blank">-->
                <!--                    <svg-icon icon-class="company"/>-->
                <!--                </el-link>-->
            </div>
            <!-- 已登录 -->
            <div class="user_container" v-else>
                <svg-icon @click="handlerChat" icon-class="chat2" class="chat"/>
                <!-- 系统通知 -->
                <el-popover placement="bottom-end"
                            width="300"
                            trigger="hover"
                            @show="getMessageList"
                            title="系统通知">
                    <div class="message-list" v-if="newMessageList && newMessageList.length !== 0">
                        <div class="message-item" v-for="message in newMessageList" :key="message.id">
                            <el-badge is-dot v-if="message.readFlag === 0"/>
                            {{message.content}}
                            <el-link v-if="message.url && message.url !== ''"
                                     :href="message.url"
                                     :underline="false">查看
                            </el-link>
                        </div>
                    </div>
                    <div class="message-text" v-else>暂无新消息</div>
                    <div class="message-text">
                        <el-link type="primary" :underline="false" @click="goMessageList">查看全部</el-link>
                    </div>
                    <el-link :underline="false"
                             class="nav-message"
                             slot="reference"
                             @click="goMessageList">
                        <el-badge is-dot v-if="messageCount !== 0">
                            <i class="el-icon-message-solid"></i>
                        </el-badge>
                        <i class="el-icon-message-solid" v-else></i>
                    </el-link>
                </el-popover>
                <!-- 用户头像 -->
                <el-dropdown>
                    <el-avatar :size="35" icon="el-icon-user-solid" :src="avatar"></el-avatar>
                    <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item>
                            <el-link :underline="false" href="/edit-resume">我的简历</el-link>
                        </el-dropdown-item>
                        <el-dropdown-item>
                            <el-link :underline="false" href="/apply-jobs">我的投递</el-link>
                        </el-dropdown-item>
                        <el-dropdown-item>
                            <el-link :underline="false" href="/favorites">我的收藏</el-link>
                        </el-dropdown-item>
                        <el-dropdown-item>
                            <el-link :underline="false" href="/my-activities">我的活动</el-link>
                        </el-dropdown-item>
                        <el-dropdown-item>
                            <el-link :underline="false" href="/modify-pwd">修改密码</el-link>
                        </el-dropdown-item>
                        <el-dropdown-item @click.native="handleLogout" class="text-danger">退出登录</el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
            </div>
        </div>
    </div>
</template>

<script>
    import {mapGetters, mapMutations} from "vuex";
    import {getUnReadMessageCount, getMessageList} from "@/api/message_api";
    import {storage} from "@/utils/storage";

    export default {
        name: "MainNavBar",
        computed: {
            ...mapGetters(["token", "name", "avatar", "messageCount"]),
            searchPlaceHolder() {
                if (this.activeIndex === "/activity-list") {
                    return "搜索活动";
                } else if (this.activeIndex === "/wiki-card") {
                    return "搜索百科";
                } else {
                    return "搜索职位";
                }
            }
        },
        data() {
            return {
                activeIndex: "",
                newMessageList: [],
                messageListForm: {
                    page: 1,
                    limit: 5
                },
                keyword: this.$store.getters.keyword,
                companyHomeUrl: process.env.VUE_APP_COMPANY_URL
            };
        },
        watch: {
            $route() {
                this.activeIndex = this.$route.path === "/wiki-list" ? "/wiki-card" : this.$route.path;
                this.getUnReadMessageCount();
                if (this.isHomeListPage()) {
                    this.keyword = this.$route.query.keyword;
                    this.$store.commit("setting/SET_KEYWORD", this.keyword);
                }
            }
        },
        created() {
            this.activeIndex = this.$route.path;
            this.getUnReadMessageCount();
        },
        methods: {
            ...mapMutations({
                setMessageCount: "setting/MESSAGE_COUNT"
            }),
            handleLogout() {
                this.$store.dispatch("user/LOGOUT").then(() => {
                    storage.removeLoginInfo();
                    this.$router.push({path: "/"});
                });
            },
            handleSearch() {
                if (this.isHomeListPage()) {
                    this.$store.commit("setting/SET_KEYWORD", this.keyword);
                } else {
                    this.$router.push({
                        path: "/job-list",
                        query: {keyword: this.keyword}
                    });
                }
            },
            isHomeListPage() {
                const cur_path = this.$route.path;
                return cur_path === "/job-list" || cur_path === "/activity-list" || cur_path === "/wiki-card" || cur_path === "/wiki-list";
            },
            handleSelect() {
                this.keyword = "";
                this.$store.commit("setting/SET_KEYWORD", this.keyword);
            },
            getUnReadMessageCount() {
                if (this.token && this.token !== "") {
                    getUnReadMessageCount().then(response => {
                        this.setMessageCount(response.data);
                    });
                }
            },
            getMessageList() {
                if (this.token && this.token !== "") {
                    getMessageList(this.messageListForm).then(response => {
                        this.newMessageList = response.data.list;
                    });
                }
            },
            goMessageList() {
                this.$router.push("/messages");
            },

            handlerChat() {
                if (this.$route.path !== '/chat') {
                    this.$router.push({path: "/chat"})
                }
            }
        }
    };
</script>
<style>
    .el-link--inner {
        white-space: nowrap;
    }

</style>
<style scoped lang="scss">
    .message-text {
        text-align: center;
        padding: 16px 10px 6px;
    }

    .nav_container {
        padding: 20px;
        display: flex;
        align-items: center;
        justify-content: center;
        flex-wrap: wrap;
        max-width: 1140px;
        margin: 0 auto;

        .nav_left_container {
            display: flex;
            align-items: center;
            flex: 1;

            .logo {
                font-size: 30px;
            }

            .logo-image {
                width: 28%;
                height: auto;

            }

            .menu-container {
                display: flex;
                align-items: center;
                justify-content: space-around;
                flex: 1;
                margin: 0 6%;


                .nav-item {
                    font-size: 18px;
                }
            }
        }

        .nav_right_container {
            display: flex;
            align-items: center;
            flex: 1;

            .input-search {
                flex: 1;
                min-width: 140px;
            }

            .user_container {
                display: flex;
                align-items: center;

                .chat {
                    width: 30px;
                    height: 30px;
                    margin-left: 10px;

                    &:hover {
                        cursor: pointer;
                        color: #409eff;
                    }
                }

                .join {
                    padding: 0 14px;
                }

                .icon-company {
                    font-size: 26px;
                    margin-left: 14px;
                }

                .nav-message {
                    font-size: 22px;
                    padding: 0 16px;
                }
            }
        }
    }

    @media screen and (max-width: 850px) {
        .nav_container {
            flex-direction: column-reverse;
        }
    }

    @media screen and (max-width: 410px) {
        .nav_container {
            flex-direction: column-reverse;

            .nav_left_container {

                .logo {
                    font-size: 22px;
                }

                .menu-container {
                    .nav-item {
                        font-size: 16px;
                    }
                }
            }
        }
    }

    .message-list {

        .message-item {
            padding: 10px 5px;
            border-bottom: 1px solid #eee;
            line-height: 1.5em;
            font-size: 14px;
            color: #888;
        }
    }


</style>
