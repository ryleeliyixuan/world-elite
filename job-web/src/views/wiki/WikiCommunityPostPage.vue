<template>
    <div class="community-post-container" v-if="postPage">
        <div class="d-flex align-items-center">
            <el-link :underline="false" style="color: #4895ef">闲聊区</el-link>
            <span class="mr-2 ml-2" style="color: #999999"> / </span>
            <el-link :underline="false" @click="goToScore" style="color: #999999"
            >评分区
            </el-link
            >
        </div>
        <div
            class="community-post-display"
            v-if="postPage.list && postPage.list.length > 0"
        >
            <div class="community-post-switch mb-4 d-flex justify-content-end">
                <!-- <el-button type="text" @click="listQuery.sort = `-id`">最新</el-button>
                <el-button type="text" @click="listQuery.sort = `-hots`"
                  >最热</el-button
                > -->
                <el-tabs @tab-click="handleClick" v-model="activeSortName">
                    <el-tab-pane label="最新" name="1"></el-tab-pane>
                    <el-tab-pane label="最热" name="2"></el-tab-pane>
                    <el-tab-pane label="精品区" name="3"></el-tab-pane>
                </el-tabs>
            </div>
            <el-row>
                <el-col :key="post.id" :span="24" v-for="post in postPage.list">
                    <el-card
                        :body-style="{ padding: '0px' }"
                        :id="post.id"
                        class="mb-2"
                        shadow="hover"
                    >
                        <div class="community-post-item">
                            <div class="community-post-item-title" v-on:click="handleOpenPostDetail(post.id)">
                                <div class="community-post-item-title-text">
                                    <span class="mr-2"> {{ post.title }} </span>
                                    <el-tag
                                        size="medium"
                                        type="warning"
                                        v-if="post.recommend === 1"
                                    ><i class="el-icon-magic-stick"></i> 精品
                                    </el-tag>
                                </div>
                                <div class="community-post-item-title-stats">
                                    <el-tag size="medium" type="danger"
                                    ><i class="el-icon-s-opportunity"></i> 热度:
                                        {{ post.hots }}
                                    </el-tag>
                                    <el-tag size="medium" type="success">
                                        <i class="el-icon-time"></i> {{ post.updateTime }}
                                    </el-tag>
                                    <el-button
                                        @click="handleOpenPostDetail(post.id)"
                                        size="mini"
                                        type="text"
                                    >
                                        查看详情
                                    </el-button>
                                </div>
                            </div>
                        </div>
                    </el-card>
                </el-col>
            </el-row>
            <div class="load-more mb-4">
                <div
                    class="community-load-more-post mt-2 d-flex justify-content-center"
                    v-if="hasMorePost == true"
                >
                    <el-button @click="loadMorePost" round> 加 载 更 多</el-button>
                </div>
                <div class="mt-2 d-flex justify-content-center" v-else>
                    <el-button disabled round> 没 有 更 多 了</el-button>
                </div>
            </div>
        </div>
        <div class="noInfoMsgBox" v-else>
            <svg-icon
                icon-class="post-missing"
                style="height: 265px; width: 344px; margin-top: 50px"
            />
            <div>来做第一个发帖子的人吧！</div>
        </div>
        <div class="community-post-comment" id="comment" style="margin-bottom: 90px">
            <div class="d-flex mb-2">
                <h5 class="mr-3">发布帖子</h5>
                <div style="color: grey; font-size: 14px">
                    文明上网理性发言，请遵守用户服务协议
                </div>
            </div>
            <el-form
                :model="postForm"
                :rules="postFormRules"
                class="mt-4"
                hide-required-asterisk
                label-position="left"
                label-width="90px"
                ref="postForm"
            >
                <el-form-item label="标题" prop="title">
                    <el-input
                        :rows="1"
                        maxlength="50"
                        placeholder="请输入标题"
                        show-word-limit
                        style="margin-bottom: 12px"
                        type="textarea"
                        v-model="postForm.title"
                    >
                    </el-input>
                </el-form-item>
                <el-form-item class="post-add-tag" label="标签">
                    <el-tag
                        :disable-transitions="false"
                        :key="tag"
                        @close="handleClose(tag)"
                        closable
                        v-for="tag in dynamicTags"
                    >
                        {{ tag }}
                    </el-tag>
                    <el-input
                        @blur="handleInputConfirm"
                        @keyup.enter.native="handleInputConfirm()"
                        class="input-new-tag"
                        ref="saveTagInput"
                        size="small"
                        v-if="inputVisible"
                        v-model="inputValue"
                    >
                    </el-input>
                    <el-button
                        @click="showInput"
                        class="button-new-tag"
                        size="small"
                        v-else
                    >
                        <svg-icon icon-class="add-tag" style="height: 23px; width: 23px"/>
                        <span style="font-size: 16px;font-weight: 500;line-height: 22px;padding-left: 3px">添加新标签</span>
                    </el-button>
                </el-form-item>
            </el-form>
            <tinymce
                :width="'100%'"
                style="padding-left: 84px"
                v-if="token"
                v-loading="loading"
                v-model="postForm.content"
            ></tinymce>
            <div
                class="log-in-alert p-2"
                style="font-size: 14px; color: #cccccc"
                v-else
            >
                您还没有登陆哦~赶快
                <el-link
                    @click="onLoginClick"
                    style="color: #568ed0; text-decoration: underline"
                >登录
                </el-link
                >
            </div>
            <div class="post-button mt-2 d-flex justify-content-end">
                <el-button
                    @click="savePost"
                    type="mini"
                    v-if="token"
                >发布帖子
                </el-button
                >
                <div class="disable" v-else>
                    <el-button
                        :loading="saveLoading"
                        @click="savePost"
                        disabled
                        type="mini"
                    >发布帖子
                    </el-button
                    >
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import Pagination from "@/components/Pagination";
    import {curRelativePath, formatListQuery, parseListQuery} from "@/utils/common";
    import {library} from "@fortawesome/fontawesome-svg-core";
    import {faThumbsUp} from "@fortawesome/free-solid-svg-icons";
    import {mapGetters} from "vuex";
    import Toast from "@/utils/toast";
    import tinymce from "@/components/Tinymce";
    //api
    import {getPostList, savePost,} from "@/api/community_api";

    library.add(faThumbsUp);

    export default {
        name: "WikiCommunityPostPage",
        components: {Pagination, tinymce},
        data() {
            return {
                activeSortName: "1",
                //tag
                dynamicTags: [],
                inputVisible: false,
                inputValue: "",
                //save post
                loading: false,
                //COMMUNITY ATTRIBUTES
                activeCommName: "1",
                companyId: undefined,
                saveLoading: false,
                //COMMUNITY POST ATTRIBUTES
                hasMorePost: false,
                postPage: {
                    list: [],
                },
                listQuery: {
                    page: 1,
                    limit: 5,
                    sort: "-create_time",
                    id: undefined,
                    companyId: undefined,
                    cliqueId: undefined,
                    title: "",
                },
                postForm: {
                    id: undefined, //帖子ID
                    companyId: undefined, //企业ID
                    cliqueId: undefined, //圈子ID
                    title: "", //帖子标题
                    content: "", //帖子内容
                    tags: [], //标签列表
                },
                postFormRules: {
                    title: [
                        {required: true, message: "请填写您的帖子标题", trigger: "blur"},
                    ],
                    content: [
                        {required: true, message: "请填写您的帖子内容", trigger: "blur"},
                    ],
                },
            };
        },
        created() {
            this.initData();
            // this.$emit("complete");
        },
        computed: {
            ...mapGetters(["token", "userId"]),
        },
        watch: {
            $route() {
                this.$emit("complete");
                // this.getPostList();
            },
        },
        mounted() {
            this.postForm.title = "";
            this.postForm.content = "";
            this.dynamicTags = [];
        },
        methods: {
            initData() {
                const {id} = this.$route.params;
                this.companyId = id;
                this.postForm.companyId = id;
                this.listQuery.companyId = id;
                // console.log("post - companyid", id);
                // console.log("initData -> this.$route", this.listQuery);
                // debugger;
                parseListQuery(this.$route.query, this.listQuery);
                this.getPostList();
                this.$emit("complete");
            },
            handleClick(tab, event) {
                const status = parseInt(tab.name);
                if (status == 1) {
                    this.listQuery.sort = "-create_time";
                    this.listQuery.recommend = undefined;
                } else if (status == 2) {
                    this.listQuery.sort = "-hots";
                    this.listQuery.recommend = undefined;
                } else {
                    let j = this.postPage.list.length;
                    for (let i = 0; i < j; i++) {
                        if (this.postPage.list[i].recommend == 1) {
                            this.listQuery.sort = "-create_time";
                            this.listQuery.recommend = 1;
                            break;
                        } else {
                            const h = this.$createElement;
                            this.$message({
                                message: h('p', {style: 'color: teal'}, [
                                    h('span', {style: 'color: teal'}, '暂无精品贴，按最新发布顺序为您展示'),
                                    h('i', {style: 'color: teal'}, '')
                                ])
                            });
                            this.listQuery.sort = "-create_time";
                            this.listQuery.recommend = undefined;
                            break;
                        }
                    }
                }
                this.getPostList();
            },
            //score
            goToScore() {
                this.$router.push(`/company/${this.companyId}/score`);
            },
            //tags
            handleClose(tag) {
                this.dynamicTags.splice(this.dynamicTags.indexOf(tag), 1);
            },
            showInput() {
                this.inputVisible = true;
                this.$nextTick((_) => {
                    this.$refs.saveTagInput.$refs.input.focus();
                });
            },

            handleInputConfirm() {
                let inputValue = this.inputValue;
                if (inputValue) {
                    this.dynamicTags.push(inputValue);
                }
                this.inputVisible = false;
                this.inputValue = "";
            },
            //POST METHODS
            handleRouteList() {
                this.$router.push({
                    path: this.$route.path,
                    query: formatListQuery(this.listQuery),
                });
            },
            getPostList() {
                getPostList(this.listQuery).then((response) => {
                    this.postPage = response.data;
                    this.hasMorePost = response.data.hasMore;
                    this.handleRouteList();
                });
            },
            savePost() {
                this.postForm.tags = this.dynamicTags;
                this.$refs["postForm"].validate((valid) => {
                    if (valid) {
                        this.saveLoading = true;
                        savePost(this.postForm)
                            .then(() => {
                                Toast.success("成功发布帖子");
                                this.postForm.title = "";
                                this.postForm.content = "";
                                this.dynamicTags = [];
                                this.getPostList();
                            }).finally(() => {
                            this.saveLoading = false;
                        });
                    } else {
                        document.getElementById("comment").scrollIntoView();
                    }
                });
            },
            handleOpenPostDetail(id) {
                const urlRootPath = `/company/${this.companyId}/postdetail`;
                this.$router.push({
                    path: urlRootPath,
                    query: {postId: id},
                });
            },
            loadMorePost() {
                this.listQuery.limit += 5;
                this.getPostList();
            },
            //POST METHODS END
            onLoginClick() {
                this.$router.push({path: "/login", query: {redirect: curRelativePath()}})
            },
        },
    };
</script>

<style lang="scss" scoped>
    /deep/ .post-button .disable .el-button {
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #999999;
        line-height: 22px;
        width: 117px;
        height: 33px;
        background: #eeeeee;
        border-radius: 5px;
        border: 0px;
    }

    .log-in-alert {
        margin-left: 85px;
        height: 110px;
        border-radius: 7px;
        border: 1px solid #b4c4d0;
    }

    /deep/ .el-form-item__label {
        display: flex;
        justify-content: flex-end;
        font-size: 16px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #333333;
        line-height: 25px;
    }

    //tag
    /deep/ .el-form-item {
        display: flex;
        align-items: center;
    }

    /deep/ .el-form-item__content {
        width: 100%;
        margin-left: 0px !important;
    }

    /deep/ .post-add-tag .el-tag {
        background-color: #f1f6fd;
        border: 0px;
        border-radius: 7px;
        color: #568ed0;
        font-weight: 500;
        position: relative;
        padding: 0 20px;
    }

    /deep/ .post-add-tag .el-tag .el-icon-close {
        position: absolute !important;
        color: #fff;
        background-color: #568ed0;
        top: -3px;
        right: -7px;
    }

    /deep/ .post-add-tag .el-button {
        background: #f1f6fd;
        border-radius: 7px;
        border: 0px;
        font-weight: 500;
        color: #568ed0;
        line-height: 22px;
    }

    .noInfoMsgBox {
        line-height: 80px;
        text-align: center;
        padding-bottom: 20px;
        width: 100%;
    }

    .likeButton {
        width: 60px;
        padding: 10px;
    }

    .post-content {
        width: 100%;
        float: none;
    }

    .button-new-tag {
        height: 32px;
        line-height: 30px;
        padding-top: 0;
        padding-bottom: 0;
    }

    .input-new-tag {
        width: 90px;
        margin-left: 10px;
        vertical-align: bottom;
    }

    .el-tag + .el-tag {
        margin-left: 10px;
    }

    .el-tag + .button-new-tag {
        margin-left: 10px;
    }

    .community-post-container {
        width: 100%;
        box-shadow: 0px 18px 14px 3px rgba(205, 213, 224, 0.3);
        background: #ffffff;
        padding: 20px 50px 20px 50px;
        margin-bottom: 80px;

        .community-post-item-title:hover {
            cursor: pointer;
        }

        .community-post-item {
            padding: 14px;
            display: flex;
            flex-direction: column;
            justify-content: space-between;

            .community-post-item-title {
                height: auto;
                min-height: 75px;
                display: flex;
                flex-direction: row;
                justify-content: space-between;
                align-items: center;

                .community-post-item-title-text {
                    height: 100%;
                    margin: 12px 20px 12px 8px;
                    padding: 8px 0 8px 0;
                }

                .community-post-item-title-stats {
                    min-width: 350px;
                    display: flex;
                    flex-direction: row;
                    justify-content: space-between;
                }
            }
        }
    }

    @media screen and (max-width: 768px) {
        .community-container {
            .community-post-item {
                .community-post-item-title {
                    height: auto;
                    display: flex;
                    flex-direction: column;
                    align-items: flex-start;

                    .community-post-item-title-stats {
                        width: 350px;
                        display: flex;
                        flex-direction: row;
                        justify-content: space-around;
                    }
                }
            }
        }
    }

    @media screen and (max-width: 450px) {
        .community-container {
            .community-post-item {
                .community-post-item-title {
                    height: auto;
                    display: flex;
                    flex-direction: column;
                    align-items: flex-start;

                    .community-post-item-title-text {
                        height: 100%;
                        margin: 8px;
                        padding: 8px 0 8px 0;
                    }

                    .community-post-item-title-stats {
                        width: 280px;
                        min-width: 280px;
                        display: flex;
                        flex-direction: row;
                        justify-content: space-around;
                    }
                }
            }
        }
    }
</style>
<style>
    .el-message--info {
        height: 50px;
        background-color: #d9edf7;
        color: #6d737b;
    }
</style>
