<template>
  <div class="community-post-container" v-if="postDetail">
    <el-row :gutter="20">
      <el-col :span="18">
        <div class="community-post-item mr-2">
          <el-card
            class="mb-4 p-3"
            :body-style="{ padding: '0px' }"
            shadow="never"
          >
            <div class="community-post-item community-post-item-title">
              <div class="community-post-item-title-user">
                <el-avatar
                  class="mr-2"
                  :src="postDetail.fromUser.avatar"
                  size="small"
                ></el-avatar>
                <span class="mr-2">
                  {{ postDetail.fromUser.name }} (楼主)
                </span>
                <el-tag
                  v-if="postDetail.recommend === 1"
                  size="medium"
                  type="warning"
                  ><i class="el-icon-magic-stick"></i> 精品
                </el-tag>
              </div>
              <h4 class="community-post-item-title-text">
                {{ postDetail.title }}
              </h4>
              <div class="community-post-item-title-stats text-secondary">
                <small class="mr-4"> 点赞数 {{ postDetail.likes }} </small>
                <small class="mr-4"> 评论数 {{ postDetail.comments }} </small>
                <!-- 缺接口 -->
                <!-- <small class="mr-4"> 浏览量 {{ postDetail.comments }} </small> -->
                <el-button
                  class="text-secondary"
                  type="text"
                  size="mini"
                  @click="reportDialogVisible = true"
                  icon="el-icon-s-flag"
                  >举报</el-button
                >
                <!-- <small class="mr-4"> {{ dayDiff }} </small> -->
                <el-button
                  icon="el-icon-delete-solid"
                  class="text-secondary"
                  type="text"
                  size="mini"
                  @click="deletePost(postDetail.id)"
                  v-if="postDetail.fromUser.userId === userId"
                  >删除我的发帖</el-button
                >
              </div>
              <!-- 举报框 -->
              <el-dialog
                title="举报理由"
                :visible.sync="reportDialogVisible"
                width="30%"
              >
                <span>这是一段信息</span>
                <span slot="footer" class="dialog-footer">
                  <el-button @click="reportDialogVisible = false"
                    >取 消</el-button
                  >
                  <el-button
                    type="primary"
                    @click="
                      (reportDialogVisible = false), reportPost(postDetail.id)
                    "
                    >确 定</el-button
                  >
                </span>
              </el-dialog>
            </div>
            <el-divider></el-divider>
            <div
              class="mb-4 mt-4 post-content"
              v-html="postDetail.content"
            ></div>
            <div
              class="post-tags mb-4"
              v-if="postDetail.tags && postDetail.tags.length > 0"
            >
              <el-tag type="info" v-for="tag in postDetail.tags" :key="tag">{{
                tag
              }}</el-tag>
            </div>
            <div class="detail-card-footer">
              <!-- <div class="share-button">
                <el-avatar
                  shape="circle"
                  size="medium"
                  icon="el-icon-share"
                ></el-avatar>
                <small class="text-secondary"> 快给朋友分享吧！</small>
              </div> -->
              <el-button
                class="main-action main-button"
                type="primary"
                size="medium"
                round
                @click="likePost(postDetail.id)"
              >
                <font-awesome-icon
                  style="color: #ffffff"
                  :icon="['fa', 'thumbs-up']"
                  size="sm"
                />
                点 赞
                {{ postDetail.likes === 0 ? "" : postDetail.likes }}
              </el-button>
              <small class="text-secondary detail-card-footer-time">
                发布于 {{ postDetail.createTime }}</small
              >
            </div>
            <div
              class="community-post-detail-action mb-4 d-flex justify-content-end"
              v-if="postDetail.fromUser"
            ></div>
          </el-card>
          <div v-if="token" class="community-post-detail-commentbox mb-4">
            <div class="d-flex mb-2">
              <strong class="mr-3">发布评论</strong>
              <div style="color: grey; font-size: 14px">
                文明上网理性发言，请遵守用户服务协议
              </div>
            </div>
            <el-form
              ref="commentForm"
              :rules="commentFormRules"
              :model="commentForm"
              label-width="80px"
              label-position="top"
            >
              <el-form-item label="" prop="content">
                <el-input
                  type="textarea"
                  :rows="3"
                  placeholder="登录后可以发表评论"
                  v-model="commentForm.content"
                  style="margin-bottom: 12px"
                  resize="none"
                >
                </el-input>
              </el-form-item>
            </el-form>
            <div class="d-flex justify-content-end">
              <el-button type="primary" @click="commentPost(postDetail.id)"
                >发布</el-button
              >
            </div>
          </div>
          <div v-else class="noInfoMsgBox">
            <el-button
              type="primary"
              icon="el-icon-user-solid"
              @click="onLoginClick"
            >
              登录后可发表评分，点此登录
            </el-button>
          </div>
          <div class="comment-header d-flex align-items-center">
            <strong>全部评论 {{ postDetail.comments }} </strong>
            <div class="d-flex justify-content-end">
              <el-tabs
                v-model="activeCommentTabName"
                @tab-click="handleCommentClick"
              >
                <el-tab-pane label="最新" name="-id"></el-tab-pane>
                <el-tab-pane label="最热" name="-hots"></el-tab-pane>
              </el-tabs>
            </div>
          </div>
          <div
            class="community-post-detail-comment"
            v-if="commentPage && commentPage.list.length > 0"
          >
            <el-row>
              <el-col
                :span="24"
                v-for="comment in commentPage.list"
                :key="comment.id"
              >
                <el-card :body-style="{ padding: '12px' }" shadow="never">
                  <div class="mb-3 d-flex align-items-center">
                    <el-avatar
                      v-if="
                        comment.fromUser.avatar &&
                        comment.fromUser.avatar.length > 0
                      "
                      style="margin-right: 12px"
                      :src="comment.fromUser.avatar"
                      size="small"
                    ></el-avatar>
                    <el-avatar
                      v-else
                      style="margin-right: 12px"
                      src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png"
                      size="small"
                    >
                    </el-avatar>
                    <div class="mr-2 font-weight-bold">
                      {{
                        comment.fromUser.name.length > 0
                          ? comment.fromUser.name
                          : "新注册用户"
                      }}
                    </div>
                    <small class="text-secondary">{{
                      comment.createTime
                    }}</small>
                  </div>
                  <div>{{ comment.content }}</div>
                  <div
                    class="community-post-item-comment-action d-flex justify-content-end"
                  >
                    <div v-if="comment.like">
                      <el-button
                        type="text"
                        size="mini"
                        class="main-action"
                        @click="likeComment(comment.id)"
                      >
                        <font-awesome-icon
                          style="color: #409eff"
                          :icon="['fa', 'thumbs-up']"
                          size="sm"
                        />
                        点赞
                        {{ comment.likes === 0 ? "" : comment.likes }}
                      </el-button>
                    </div>
                    <div v-else>
                      <el-button
                        type="text"
                        size="mini"
                        class="main-action text-secondary"
                        @click="likeComment(comment.id)"
                      >
                        <font-awesome-icon
                          style="color: grey"
                          :icon="['fa', 'thumbs-up']"
                          size="sm"
                        />
                        点赞
                        {{ comment.likes === 0 ? "" : comment.likes }}
                      </el-button>
                    </div>
                    <el-button
                      type="text"
                      size="small"
                      icon="el-icon-s-comment"
                      @click="
                        openReplybox(comment.id), getReplyList(comment.id)
                      "
                      >回复 {{ comment.comments }}
                    </el-button>
                    <el-button
                      type="text"
                      size="small"
                      @click="reportComment(comment.id)"
                      icon="el-icon-s-flag"
                      >举报</el-button
                    >
                    <el-button
                      icon="el-icon-delete-solid"
                      type="text"
                      size="small"
                      @click="deleteComment(comment.id, 1)"
                      v-if="comment.fromUser.userId === userId"
                      >删除</el-button
                    >
                  </div>
                  <div
                    class="community-post-detail-comment-reply"
                    v-if="
                      replyList &&
                      replyList.length > 0 &&
                      CommentBoxOpen &&
                      comment.id == commentId
                    "
                  >
                    <el-row>
                      <el-col
                        :span="24"
                        v-for="reply in replyList"
                        :key="reply.id"
                      >
                        <div v-if="reply.ownerId == comment.id">
                          <el-card
                            :body-style="{ padding: '12px' }"
                            shadow="never"
                          >
                            <div
                              class="mb-2 d-flex align-items-center"
                              style="font-size: 10px; font-weight: bold"
                            >
                              <el-avatar
                                v-if="
                                  reply.fromUser.avatar &&
                                  reply.fromUser.avatar.length > 0
                                "
                                style="margin-right: 8px"
                                :src="reply.fromUser.avatar"
                                :size="20"
                              ></el-avatar>
                              <el-avatar
                                v-else
                                style="margin-right: 12px"
                                src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png"
                                size="small"
                              >
                              </el-avatar>
                              {{
                                reply.fromUser.name &&
                                reply.fromUser.name.length > 0
                                  ? reply.fromUser.name
                                  : "新注册用户"
                              }}
                              <i class="el-icon-caret-right"></i>
                              {{
                                reply.toUser.name &&
                                reply.toUser.name.length > 0
                                  ? reply.toUser.name
                                  : "新注册用户"
                              }}
                            </div>
                            {{ reply.content }}
                            <div
                              class="community-post-item-reply-action d-flex justify-content-end"
                            >
                              <div v-if="reply.like">
                                <el-button
                                  type="text"
                                  size="mini"
                                  class="main-action"
                                  @click="likeComment(reply.id)"
                                >
                                  <font-awesome-icon
                                    style="color: #409eff"
                                    :icon="['fa', 'thumbs-up']"
                                    size="sm"
                                  />
                                  点赞
                                  {{ reply.likes === 0 ? "" : reply.likes }}
                                </el-button>
                              </div>
                              <div v-else>
                                <el-button
                                  type="text"
                                  size="mini"
                                  class="main-action text-secondary"
                                  @click="likeComment(reply.id)"
                                >
                                  <font-awesome-icon
                                    style="color: grey"
                                    :icon="['fa', 'thumbs-up']"
                                    size="sm"
                                  />
                                  点赞
                                  {{ reply.likes === 0 ? "" : reply.likes }}
                                </el-button>
                              </div>
                              <el-button
                                type="text"
                                size="small"
                                icon="el-icon-s-comment"
                                @click="openSecondaryReplybox(reply.id)"
                                >回复
                              </el-button>
                              <el-button
                                type="text"
                                size="small"
                                @click="reportComment(comment.id)"
                                icon="el-icon-s-flag"
                                >举报</el-button
                              >
                              <el-button
                                icon="el-icon-delete-solid"
                                type="text"
                                size="mini"
                                @click="deleteComment(reply.id, 1)"
                                v-if="reply.fromUser.userId === userId"
                                >删除</el-button
                              >
                            </div>
                            <div
                              v-if="
                                secondaryRepyOpen && reply.id === replyCommentId
                              "
                              class="reply-box mt-2"
                            >
                              <el-form
                                ref="replyForm"
                                :rules="replyFormRules"
                                :model="replyForm"
                                label-width="80px"
                                label-position="top"
                              >
                                <el-form-item label="" prop="content">
                                  <el-input
                                    type="textarea"
                                    :rows="3"
                                    placeholder="发表回复"
                                    resize="none"
                                    v-model="replyForm.content"
                                    style="margin-bottom: 12px"
                                  >
                                  </el-input>
                                </el-form-item>
                              </el-form>
                              <div class="d-flex justify-content-end">
                                <el-button
                                  type="primary"
                                  size="mini"
                                  @click="replyComment(comment.id)"
                                  >回复</el-button
                                >
                              </div>
                            </div>
                          </el-card>
                        </div>
                      </el-col>
                    </el-row>
                  </div>
                  <div
                    v-if="replyOpen && comment.id == commentId"
                    class="reply-box mt-2"
                  >
                    <el-form
                      ref="replyForm"
                      :rules="replyFormRules"
                      :model="replyForm"
                      label-width="80px"
                      label-position="top"
                    >
                      <el-form-item label="" prop="content">
                        <el-input
                          type="textarea"
                          :rows="3"
                          placeholder="发表回复"
                          resize="none"
                          v-model="replyForm.content"
                          style="margin-bottom: 12px"
                        >
                        </el-input>
                      </el-form-item>
                    </el-form>
                    <div class="d-flex justify-content-end">
                      <el-button
                        type="primary"
                        size="mini"
                        @click="replyComment(comment.id)"
                        >回复</el-button
                      >
                    </div>
                  </div>
                </el-card>
              </el-col>
            </el-row>
            <div class="load-more">
              <div
                class="community-load-more-post mt-2 d-flex justify-content-center"
                v-if="hasMoreComment == true"
              >
                <el-button round @click="loadMoreComment">
                  加 载 更 多
                </el-button>
              </div>
              <div v-else class="mt-2 d-flex justify-content-center">
                <el-button round disabled> 没 有 更 多 了 </el-button>
              </div>
            </div>
          </div>
          <div v-else class="noInfoMsgBox">暂无评论</div>
        </div>
      </el-col>
      <el-col :span="6">
        <el-card :body-style="{ padding: '0px' }" shadow="never">
          <div slot="header" class="hotest-post-header align-items-center">
            <strong>热门帖子</strong>
            <el-button
              type="text"
              class="text-secondary"
              @click="handleGetMoreHottest"
              >换一批</el-button
            >
          </div>
          <el-row>
            <el-col :span="24" v-for="post in postPage.list" :key="post.id">
              <el-card
                class="hotest-post"
                :body-style="{ padding: '8px' }"
                shadow="never"
              >
                <el-row :gutter="8">
                  <el-col :span="10">
                    <img
                      v-if="post.image && post.image.length > 0"
                      class="hotest-post-thumbnail"
                      :src="post.image"
                    />
                    <img
                      v-else
                      class="hotest-post-thumbnail"
                      :src="post.company.logo"
                    />
                  </el-col>
                  <el-col :span="14">
                    <div class="d-flex flex-column">
                      <strong>{{ post.title }}</strong>
                      <small class="text-secondary">{{
                        post.createTime
                      }}</small>
                    </div>
                  </el-col>
                </el-row>
              </el-card>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import Vue from "vue";
import VueAMap from "vue-amap";
import { formatListQuery, parseListQuery } from "@/utils/common";

import { library, parse } from "@fortawesome/fontawesome-svg-core";
import { faThumbsUp } from "@fortawesome/free-solid-svg-icons";
library.add(faThumbsUp);

import { setPageTitle } from "@/utils/setting";
import { doFavorite } from "@/api/favorite_api";
import { mapGetters } from "vuex";
import Toast from "@/utils/toast";

//api
import {
  //post
  getPostList,
  deletePost,
  savePost,
  getPostDetail,
  likePost,
  reportPost,
  commentPost,
  //comment
  getCommentList,
  deleteComment,
  likeComment,
  reportComment,
  replyComment,
} from "@/api/community_api";

export default {
  name: "WikiCommunityPostPage",
  props: {
    //接受父组件传递来的数据
    // companyId: {
    //   type: String,
    // },
  },
  data() {
    return {
      //COMMUNITY ATTRIBUTES
      activeCommentTabName: "-id",
      activeCommName: "1",
      companyId: undefined,
      dayDiff: undefined,
      postId: undefined,
      reportDialogVisible: false,
      commentId: undefined,
      CommentBoxOpen: false,
      commentQuery: {
        page: 1,
        limit: 5,
        sort: "-id",
        fromId: undefined,
        toId: undefined,
        ownerId: undefined,
      },
      //COMMUNITY POST ATTRIBUTES
      hasMoreComment: false,
      postPage: {
        list: [],
      },
      listQuery: {
        page: 1,
        limit: 10,
        sort: "-hots",
        id: undefined,
        companyId: undefined,
        cliqueId: undefined,
        title: "",
      },
      //post detail
      postDetail: {
        fromUser: undefined,
      },
      postContent: "",
      //post comment
      commentForm: {
        id: undefined,
        ownerId: undefined,
        content: "",
      },
      commentFormRules: {
        content: [
          { required: true, message: "请发表您的评论", trigger: "blur" },
        ],
      },

      //COMMUNITY COMMENT AND REPLY
      commentPage: { list: [] },
      replyList: [],
      replyOpen: false,
      secondaryRepyOpen: false,
      replyCommentId: undefined,
      replyForm: {
        id: undefined,
        ownerId: undefined,
        content: "",
      },
      replyFormRules: {
        content: [
          { required: true, message: "请发表您的回复", trigger: "blur" },
        ],
      },
    };
  },
  created() {
    this.initData();
  },
  computed: {
    ...mapGetters(["token", "userId"]),
  },
  watch: {
    $route() {
      // this.getPostList();
    },
  },
  methods: {
    initData() {
      this.companyId = this.$route.params.id;
      this.postId = this.$route.query.postId;
      this.commentQuery.ownerId = this.postId;
      parseListQuery(this.$route.query, this.listQuery);
      this.getPostDetail();
      this.getPostList();
      this.getCommentList();
    },
    //POST METHODS
    handleRouteList() {
      this.$router.push({
        path: this.$route.path,
        query: formatListQuery(this.listQuery),
      });
    },
    getPostDetail() {
      getPostDetail(this.postId).then((response) => {
        this.postDetail = response.data;
        if (this.postDetail.createTime) {
          this.showtime(this.postDetail.createTime);
        }
      });
    },
    getPostList() {
      getPostList(this.listQuery).then((response) => {
        this.postPage = response.data;
      });
    },
    handleGetMoreHottest() {
      if (this.postPage.hasMore === false) {
        this.listQuery.page = 1;
      } else {
        this.listQuery.page++;
      }
      this.getPostList();
    },
    handleCommentClick() {
      if (this.activeCommentTabName === "-id") {
        this.commentQuery.sort = "-id";
        this.getCommentList();
      } else {
        this.commentQuery.sort = "-hots";
        this.getCommentList();
      }
    },
    loadMoreComment() {
      this.commentQuery.limit += 5;
      this.getCommentList();
    },
    deletePost(id) {
      let data = { postId: id };
      deletePost(data).then(() => {
        Toast.success("成功删除帖子");
        this.$router.push(`/company/${this.companyId}/community/post`);
      });
    },
    likePost(id) {
      likePost(id).then((response) => {
        this.postDetail.like = response.data.like;
        this.postDetail.likes = response.data.likes;
      });
    },
    reportPost(id) {
      let data = {
        ownerId: id,
        optionId: undefined,
        content: "",
      };
      reportPost(data).then(() => {
        Toast.success("举报成功");
      });
      this.$emit("complete");
    },
    commentPost(id) {
      this.commentForm.ownerId = id;
      this.postDetail.comments++;
      this.$refs["commentForm"].validate((valid) => {
        if (valid) {
          commentPost(this.commentForm).then(() => {
            Toast.success("评论成功");
            this.commentForm.content = "";
            this.getCommentList();
          });
        }
      });
    },
    //POST METHODS END

    //COMMENT METHOD
    likeComment(id) {
      likeComment(id).then((response) => {
        this.getCommentList();
        this.getReplyList();
      });
    },
    getCommentList() {
      getCommentList(this.commentQuery).then((response) => {
        this.commentPage = response.data;
        this.commentPage.list = response.data.list;
        this.hasMoreComment = response.data.hasMore;
      });
      this.$emit("complete");
    },
    getReplyList(ownerId) {
      let data = {
        page: 1,
        limit: 10,
        sort: "-id",
        fromId: undefined,
        toId: undefined,
        ownerId: ownerId,
      };
      getCommentList(data).then((response) => {
        this.replyList = response.data.list;
      });
    },
    deleteComment(id, type) {
      let data = { commentId: id };
      deleteComment(data).then(() => {
        Toast.success("成功删除评论");
        this.getCommentList();
        this.getReplyList();
      });
    },
    reportComment(id) {
      let data = {
        ownerId: id,
        optionId: undefined,
        content: "",
      };
      reportComment(data).then(() => {
        Toast.success("举报成功");
      });
    },
    replyComment(id) {
      this.replyForm.ownerId = id;
      this.$refs["replyForm"][0].validate((valid) => {
        if (valid) {
          replyComment(this.replyForm).then(() => {
            Toast.success("回复成功");
            this.replyForm.content = "";
            this.getReplyList();
            this.getCommentList();
          });
        }
      });
    },
    openReplybox(id) {
      if (this.commentId === id) {
        //如果是上次点击
        this.replyOpen = !this.replyOpen;
      } else {
        this.replyOpen = true;
      }
      this.CommentBoxOpen = this.replyOpen;
      this.commentId = id;

      if (this.secondaryRepyOpen == true) {
        this.secondaryRepyOpen = false;
      }
    },
    openSecondaryReplybox(id) {
      this.replyOpen = false;
      if (this.replyCommentId === id) {
        //如果是上次点击
        this.secondaryRepyOpen = !this.secondaryRepyOpen;
      } else {
        this.secondaryRepyOpen = true;
      }
      this.replyCommentId = id;
    },
  },
};
</script>

<style scoped lang="scss">
.post-content {
  width: 100%;
}
.hotest-post-header {
  height: 18px;
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
}
.hotest-post {
  min-height: 80px;
  width: auto;
  .hotest-post-thumbnail {
    height: 60px;
    width: 90px;
    object-fit: fill;
  }
}

.noInfoMsgBox {
  line-height: 80px;
  text-align: center;
  padding-bottom: 20px;
  width: 100%;
}

.comment-header {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}

.main-action {
  width: 70px;
  padding: 10px;
}

.el-tag + .el-tag {
  margin-left: 12px;
}

.detail-card-footer {
  position: relative;
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  padding: 12px;
  .share-button {
    display: flex;
    flex-direction: column;
    align-items: center;
  }
  .detail-card-footer-time {
    position: absolute;
    right: 0;
    top: 25px;
  }
}

.main-button {
  width: 100px;
}

.community-container {
  width: 100%;
  .community-post-item {
    display: flex;
    flex-direction: column;
    .community-post-item-title {
      height: auto;
      min-height: 75px;
      display: flex;
      flex-direction: column;
      justify-content: flex-start;
      .community-post-item-title-user {
        display: flex;
        flex-direction: row;
        align-items: center;
        padding: 8px;
      }
      .community-post-item-title-text {
        height: 100%;
        margin: 12px 20px 12px 8px;
        padding: 8px 0 8px 0;
      }
      .community-post-item-title-stats {
        width: 75%;
        display: flex;
        flex-direction: row;
        align-items: center;
        justify-content: flex-start;
        padding: 8px;
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
      }
    }
  }
}
</style>