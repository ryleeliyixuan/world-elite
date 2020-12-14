<template>
  <div class="community-post-container" v-if="postDetail">
    <div class="community-post-wrapper">
      <div class="d-flex align-items-center mb-4">
        <el-link :underline="false" style="color: #4895ef" @click="goToPost"
          >闲聊区</el-link
        >
        <span class="mr-2 ml-2" style="color: #999999"> / </span>
        <el-link :underline="false" style="color: #999999" @click="goToScore"
          >评分区</el-link
        >
      </div>
      <el-card
        style="
          margin-bottom: 30px;
          border: 1px solid #b4c4d0;
          border-radius: 7px;
        "
        :body-style="{ padding: '0px' }"
        shadow="never"
      >
        <div class="detail-card-header">
          <div class="community-post-item community-post-item-title">
            <div class="community-post-item-title-user">
              <div class="detail-card-header-author">
                <el-avatar
                  class="mr-2"
                  :src="postDetail.fromUser.avatar"
                  :size="50"
                ></el-avatar>
                <span class="mr-2">
                  {{ postDetail.fromUser.name }} (楼主)
                </span>
              </div>
              <el-tag
                v-if="postDetail.recommend === 1"
                size="medium"
                type="warning"
                ><i class="el-icon-magic-stick"></i> 精品
              </el-tag>
              <div class="detail-card-header-time">
                {{ postDetail.createTime }}
              </div>
            </div>
            <h4 class="community-post-item-title-text">
              {{ postDetail.title }}
            </h4>
          </div>
          <div class="mb-4 mt-4 post-content" v-html="postDetail.content"></div>
          <div
            class="post-tags"
            v-if="postDetail.tags && postDetail.tags.length > 0"
          >
            <el-tag type="info" v-for="tag in postDetail.tags" :key="tag">{{
              tag
            }}</el-tag>
          </div>
        </div>
        <div class="detail-card-footer">
          <div class="community-post-item-title-stats text-secondary">
            <!-- 点赞 -->
            <span v-if="postDetail.like">
              <el-button
                type="text"
                size="mini"
                class="like-button"
                @click="likePost(postDetail.id)"
              >
                <font-awesome-icon
                  style="color: #568ed0"
                  :icon="['fa', 'thumbs-up']"
                  size="sm"
                />
                <span style="color: #a1aebf; margin-left: 4px">
                  {{ postDetail.likes }}
                </span>
              </el-button>
            </span>
            <span v-else>
              <el-button
                type="text"
                size="mini"
                class="like-button text-secondary"
                @click="likePost(postDetail.id)"
              >
                <font-awesome-icon
                  style="color: grey"
                  :icon="['fa', 'thumbs-up']"
                  size="sm"
                />
                <span style="color: #a1aebf; margin-left: 4px">
                  {{ postDetail.likes }}
                </span>
              </el-button>
            </span>
            <el-button
              style="color: #568ed0"
              type="text"
              size="small"
              icon="el-icon-s-comment"
              ><span style="color: #a1aebf">{{ postDetail.comments }}</span>
            </el-button>
            <el-button
              style="color: #568ed0"
              type="text"
              size="mini"
              @click="reportPost(postDetail.id)"
              icon="el-icon-s-flag"
              ><span style="color: #a1aebf">举报</span></el-button
            >
            <el-button
              icon="el-icon-delete-solid"
              style="color: #568ed0"
              type="text"
              size="mini"
              @click="deletePost(postDetail.id)"
              v-if="postDetail.fromUser.userId === userId"
              ><span style="color: #a1aebf">删除我的发帖</span></el-button
            >
          </div>
        </div>
      </el-card>
      <div class="community-post-detail-commentbox mb-4">
        <div class="d-flex mb-2">
          <strong class="mr-3">发布评论</strong>
          <div style="color: grey; font-size: 14px">
            文明上网理性发言，请遵守用户服务协议
          </div>
        </div>
        <el-form
          v-if="token"
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
              placeholder="请输入评论..."
              v-model="commentForm.content"
              style="margin-bottom: 12px"
              resize="none"
            >
            </el-input>
          </el-form-item>
        </el-form>
        <div class="log-in-alert p-2" v-else style="font-size: 14px; color: #CCCCCC">
          您还没有登陆哦~赶快<el-link
            style="color: #568ed0; text-decoration: underline"
            @click="onLoginClick"
            >登录</el-link
          >
        </div>
        <div class="post-comment d-flex justify-content-end">
          <el-button
            v-if="token"
            type="mini"
            @click="commentPost(postDetail.id)"
            >发布评论</el-button
          >
          <div v-else class="disable">
            <el-button disabled type="mini" @click="commentPost(postDetail.id)"
              >发布评论</el-button
            >
          </div>
        </div>
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
        style="border: 1px solid #b4c4d0; border-radius: 7px"
      >
        <el-row>
          <el-col
            :span="24"
            v-for="comment in commentPage.list"
            :key="comment.id"
          >
            <div>
              <div
                style="padding: 16px 24px 12px 24px"
                class="d-flex align-items-center"
              >
                <el-avatar
                  v-if="
                    comment.fromUser.avatar &&
                    comment.fromUser.avatar.length > 0
                  "
                  style="margin-right: 12px"
                  :src="comment.fromUser.avatar"
                  :size="35"
                ></el-avatar>
                <el-avatar
                  v-else
                  style="margin-right: 12px"
                  src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png"
                  :size="35"
                >
                </el-avatar>
                <div class="comment-detail-header font-weight-bold">
                  {{
                    comment.fromUser.name.length > 0
                      ? comment.fromUser.name
                      : "新注册用户"
                  }}
                  <div class="comment-detail-header-text">
                    {{ comment.createTime }}
                  </div>
                </div>
              </div>
              <div style="padding: 16px 24px">{{ comment.content }}</div>
              <div
                style="padding: 0px 12px 24px 12px"
                class="community-post-item-comment-action d-flex justify-content-start"
              >
                <div v-if="comment.like">
                  <el-button
                    type="text"
                    size="mini"
                    class="like-button"
                    @click="likeComment(comment.id)"
                  >
                    <font-awesome-icon
                      style="color: #568ed0"
                      :icon="['fa', 'thumbs-up']"
                      size="sm"
                    />
                    <span style="color: #a1aebf; margin-left: 4px">{{
                      comment.likes
                    }}</span>
                  </el-button>
                </div>
                <div v-else>
                  <el-button
                    type="text"
                    size="mini"
                    class="like-button text-secondary"
                    @click="likeComment(comment.id)"
                  >
                    <font-awesome-icon
                      style="color: grey"
                      :icon="['fa', 'thumbs-up']"
                      size="sm"
                    />
                    <span style="color: #a1aebf; margin-left: 4px">{{
                      comment.likes
                    }}</span>
                  </el-button>
                </div>
                <el-button
                  type="text"
                  style="color: #568ed0"
                  size="small"
                  icon="el-icon-s-comment"
                  @click="openReplybox(comment.id), getReplyList(comment.id)"
                  ><span style="color: #a1aebf">{{ comment.comments }}</span>
                </el-button>
                <el-button
                  type="text"
                  style="color: #568ed0"
                  size="small"
                  @click="reportComment(comment.id)"
                  icon="el-icon-s-flag"
                  ><span style="color: #a1aebf">举报</span></el-button
                >
                <el-button
                  icon="el-icon-delete-solid"
                  style="color: #568ed0"
                  type="text"
                  size="small"
                  @click="deleteComment(comment.id, 1)"
                  v-if="comment.fromUser.userId === userId"
                  ><span style="color: #a1aebf">删除</span></el-button
                >
              </div>
              <div
                class="community-post-detail-comment-reply"
                style="padding: 0px 24px"
                v-if="
                  replyList &&
                  replyList.length > 0 &&
                  CommentBoxOpen &&
                  comment.id == commentId
                "
              >
                <el-row>
                  <el-col :span="24" v-for="reply in replyList" :key="reply.id">
                    <div v-if="reply.ownerId == comment.id">
                      <el-card :body-style="{ padding: '12px' }" shadow="never">
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
                            reply.toUser.name && reply.toUser.name.length > 0
                              ? reply.toUser.name
                              : "新注册用户"
                          }}
                        </div>
                        {{ reply.content }}
                        <div
                          class="community-post-item-reply-action d-flex justify-content-start"
                        >
                          <div v-if="reply.like">
                            <el-button
                              type="text"
                              size="mini"
                              class="like-button"
                              style="color: #568ed0"
                              @click="likeComment(reply.id)"
                            >
                              <font-awesome-icon
                                style="color: #409eff"
                                :icon="['fa', 'thumbs-up']"
                                size="sm"
                              />
                              <span style="color: #a1aebf; margin-left: 4px">{{
                                reply.likes
                              }}</span>
                            </el-button>
                          </div>
                          <div v-else>
                            <el-button
                              type="text"
                              size="mini"
                              class="like-button text-secondary"
                              @click="likeComment(reply.id)"
                            >
                              <font-awesome-icon
                                style="color: grey"
                                :icon="['fa', 'thumbs-up']"
                                size="sm"
                              />
                              <span style="color: #a1aebf; margin-left: 4px">{{
                                reply.likes
                              }}</span>
                            </el-button>
                          </div>
                          <el-button
                            type="text"
                            size="small"
                            icon="el-icon-s-comment"
                            style="color: #568ed0"
                            @click="openSecondaryReplybox(reply.id)"
                            ><span style="color: #a1aebf">回复</span>
                          </el-button>
                          <el-button
                            type="text"
                            size="small"
                            @click="reportComment(comment.id)"
                            icon="el-icon-s-flag"
                            style="color: #568ed0"
                            ><span style="color: #a1aebf">举报</span></el-button
                          >
                          <el-button
                            icon="el-icon-delete-solid"
                            type="text"
                            size="mini"
                            style="color: #568ed0"
                            @click="deleteComment(reply.id, 1)"
                            v-if="reply.fromUser.userId === userId"
                            ><span style="color: #a1aebf">删除</span></el-button
                          >
                        </div>
                        <div
                          v-if="
                            secondaryRepyOpen && reply.id === replyCommentId
                          "
                          class="reply-box mt-2 mb-2"
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
                class="reply-box mt-2 mb-4"
                style="padding: 0px 24px"
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
            </div>
            <div class="comment-divider"></div>
          </el-col>
        </el-row>
        <div class="load-more">
          <div
            class="community-load-more-post mt-2 d-flex justify-content-center p-2"
            v-if="hasMoreComment == true"
          >
            <el-button round @click="loadMoreComment"> 加 载 更 多 </el-button>
          </div>
          <div v-else class="mt-2 d-flex justify-content-center p-2">
            <el-button round disabled> 没 有 更 多 了 </el-button>
          </div>
        </div>
      </div>
      <div v-else class="noInfoMsgBox">暂无评论</div>
    </div>
    <div class="hottest-post-container">
      <div class="hotest-post-header align-items-center">
        <strong>热门帖子</strong>
        <el-button
          v-show="collapseHottest === false"
          type="text"
          style="color: #333333"
          icon="el-icon-arrow-down"
          @click="collapseHottest = true"
        ></el-button>
        <el-button
          v-show="collapseHottest === true"
          type="text"
          style="color: #333333"
          icon="el-icon-arrow-up"
          @click="collapseHottest = false"
        ></el-button>
      </div>
      <el-row v-show="collapseHottest === false">
        <el-col :span="24" v-for="post in postPage.list" :key="post.id">
          <el-card
            class="hotest-post"
            :body-style="{ padding: '15px' }"
            shadow="never"
          >
            <el-row :gutter="4">
              <el-col :span="10">
                <img
                  v-if="post.image && post.image.length > 0"
                  class="hotest-post-thumbnail"
                  :src="post.image"
                  @click="handleGoToHotPost(post.companyId, post.id)"
                />
                <img
                  v-else
                  class="hotest-post-thumbnail"
                  :src="post.company.logo"
                  @click="handleGoToHotPost(post.companyId, post.id)"
                />
              </el-col>
              <el-col :span="14">
                <div class="d-flex flex-column">
                  <strong class="mb-1" @click="handleGoToHotPost(post.companyId, post.id)">{{ post.title }}</strong>
                  <el-tag class="mb-1"> 热度: {{ post.hots }} </el-tag>
                  <small style="color: #a1aebf">{{ post.createTime }}</small>
                </div>
              </el-col>
            </el-row>
          </el-card>
          <div class="post-divider"></div>
        </el-col>
      </el-row>
      <div
        v-if="collapseHottest === false"
        class="d-flex justify-content-center"
      >
        <el-button
          type="text"
          style="color: #999999"
          @click="handleGetMoreHottest"
          >换一批</el-button
        >
      </div>
    </div>
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

      //hottest
      collapseHottest: false,
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
      this.initData() 
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
    handleGoToHotPost(companyId, id){
      this.$router.push(`/company/${companyId}/postdetail?postId=${id}`);
    },
    onLoginClick() {
      this.$router.push("/login");
    },
    goToScore() {
      this.$router.push(`/company/${this.companyId}/score`);
    },
    goToPost() {
      this.$router.push(`/company/${this.companyId}/post`);
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
        this.$router.push(`/company/${this.companyId}/post`);
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
.log-in-alert {
  height: 110px;
  border-radius: 7px;
  border-bottom-left-radius: 0px;
  border-bottom-right-radius: 0px;
  border: 1px solid #b4c4d0;
}

.comment-detail-header {
  display: flex;
  flex-direction: column;
  .comment-detail-header-text {
    font-size: 12px;
    font-family: PingFangSC-Regular, PingFang SC;
    font-weight: 400;
    color: #a1aebf;
    line-height: 17px;
  }
}

.comment-divider {
  margin: 0px 15px;
  height: 1px;
  background-color: #b4c4d0;
}

//comment box
/deep/ .post-comment .el-button {
  background: #f1f6fd;
  border-radius: 5px;
  border: 0px;
  font-family: PingFangSC-Medium, PingFang SC;
  font-weight: 500;
  color: #568ed0;
  line-height: 22px;
  height: 33px;
  width: 115px;
}

/deep/ .post-comment .disable .el-button {
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

.post-comment {
  border: 1px solid #b4c4d0;
  border-top: 0px;
  border-radius: 7px;
  border-top-left-radius: 0px;
  border-top-right-radius: 0px;
  padding: 5px;
}

/deep/ .community-post-detail-commentbox .el-textarea__inner {
  border-radius: 7px;
  border-bottom-left-radius: 0px;
  border-bottom-right-radius: 0px;
  border: 1px solid #b4c4d0;
}

/deep/ .community-post-detail-commentbox .el-textarea {
  margin-bottom: 0px !important;
}

/deep/ .community-post-detail-commentbox .el-form-item {
  margin-bottom: 0px;
}

//hottest
/deep/ .hottest-post-container .el-card {
  border: 0px;
  border-radius: 0px;
}

/deep/ .hottest-post-container .el-tag {
  width: 74px;
  height: 22px;
  background: #fbe9e7;
  border-radius: 5px;
  border: 0px;
  font-family: PingFangSC-Medium, PingFang SC;
  font-weight: 500;
  color: #ff3d00;
  line-height: 17px;
}

.post-divider {
  width: 200px;
  margin: 0px 15px;
  height: 1px;
  background-color: #b4c4d0;
}
//hottest end

//tag
/deep/ .post-tags .el-tag {
  border: 0px;
  background: #f1f6fd;
  border-radius: 3px;
  font-family: PingFangSC-Medium, PingFang SC;
  font-weight: 500;
  color: #568ed0;
  line-height: 22px;
  padding-top: 5px;
}

.community-post-item-title-user {
  display: flex;
  flex-direction: row;
  align-items: center;
  margin-bottom: 30px;
  position: relative;
}

.detail-card-header {
  padding: 25px 25px 0px 25px;
}

.detail-card-header-time {
  position: absolute;
  right: 0;
  top: 25px;
  font-weight: 500;
  color: #568ed0;
  line-height: 22px;
  font-size: 12px;
}
.detail-card-header-author {
  display: flex;
  align-items: center;
}

.hottest-post-container {
  width: 320px;
  height: auto;
  background: #ffffff;
  box-shadow: 0px 18px 14px 3px rgba(205, 213, 224, 0.3);
}

.community-post-wrapper {
  width: 100%;
  background: #ffffff;
  box-shadow: 0px 18px 14px 3px rgba(205, 213, 224, 0.3);
  padding: 20px 50px 20px 50px;
  margin-right: 50px;
}

.community-post-container {
  width: 100%;
  height: auto;
  display: flex;
  flex-direction: row;
  margin-bottom: 80px;
  align-items: flex-start;
}

.post-content {
  width: 100%;
}

.hotest-post-header {
  height: 50px;
  padding: 0 15px;
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
}

.hotest-post {
  min-height: 80px;
  width: auto;
  .hotest-post-thumbnail {
    height: 70px;
    width: 70px;
    object-fit: fill;
    border-radius: 6px;
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

.like-button {
  padding: 10px;
}

.el-tag + .el-tag {
  margin-left: 12px;
}

.detail-card-footer {
  position: relative;
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  align-items: center;
  padding: 12px;

  .share-button {
    display: flex;
    flex-direction: column;
    align-items: center;
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