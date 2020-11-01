<template>
  <div class="community-post-container">
    <div v-if="postPage.list.length > 0" class="community-post-display">
      <div class="community-post-switch mb-4 d-flex justify-content-end">
        <el-switch
          v-if="hasDetailOpen == false"
          v-model="listQuery.sort"
          active-color="#13ce66"
          inactive-color="#ff4949"
          active-text="最新"
          inactive-text="最热"
          active-value="-id"
          inactive-value="-hots"
          @change="getPostList"
        >
        </el-switch>
      </div>
      <el-row>
        <el-col :span="24" v-for="post in postPage.list" :key="post.id">
          <el-card
            class="mb-2"
            :body-style="{ padding: '0px' }"
            shadow="hover"
            :id="post.id"
          >
            <div class="community-post-item">
              <div class="community-post-item-title">
                <div class="community-post-item-title-text">
                  {{ post.title }}
                </div>
                <div class="community-post-item-title-stats">
                  <el-tag size="medium" type="danger"
                    ><i class="el-icon-s-opportunity"></i> 热度: {{ post.hots }}
                  </el-tag>
                  <el-tag size="medium" type="success">
                    <i class="el-icon-time"></i> 更新于 {{ post.updateTime }}
                  </el-tag>
                  <el-button
                    type="text"
                    size="mini"
                    @click="handleCollapse(post.id)"
                  >
                    {{ hasDetailOpen == true ? "返回列表" : "查看详情" }}
                  </el-button>
                </div>
              </div>
              <div v-if="hasDetailOpen == true" class="community-post-detail">
                <el-divider></el-divider>
                <div class="mb-4 d-flex align-items-center">
                  <el-avatar
                    class="mr-2"
                    :src="postDetail.fromUser.avatar"
                    size="small"
                  ></el-avatar>
                  <small
                    >{{ postDetail.fromUser.name }} 发布于
                    {{ postDetail.createTime }}</small
                  >
                </div>
                <div class="mb-4">{{ postDetail.content }}</div>
                <div
                  class="community-post-detail-action mb-4 d-flex justify-content-end"
                >
                  <div v-if="postDetail.like">
                    <el-button
                      class="likeButton"
                      type="text"
                      size="mini"
                      @click="likePost(postDetail.id)"
                    >
                      <font-awesome-icon
                        style="color: #409eff"
                        :icon="['fa', 'thumbs-up']"
                        size="sm"
                      />
                      点赞
                      {{ postDetail.likes === 0 ? "" : postDetail.likes }}
                    </el-button>
                  </div>
                  <div v-else>
                    <el-button
                      class="likeButton text-secondary"
                      type="text"
                      size="mini"
                      @click="likePost(postDetail.id)"
                    >
                      <font-awesome-icon
                        style="color: grey"
                        :icon="['fa', 'thumbs-up']"
                        size="sm"
                      />
                      点赞
                      {{ postDetail.likes === 0 ? "" : postDetail.likes }}
                    </el-button>
                  </div>
                  <el-button type="text" size="mini" icon="el-icon-s-comment"
                    >评论 {{ postDetail.comments }}
                  </el-button>
                  <el-button
                    type="text"
                    size="mini"
                    @click="reportPost(postDetail.id)"
                    icon="el-icon-s-flag"
                    >举报</el-button
                  >
                  <el-button
                    icon="el-icon-delete-solid"
                    type="text"
                    size="mini"
                    @click="deletePost(postDetail.id)"
                    v-if="postDetail.fromUser.userId === userId"
                    >删除我的发帖</el-button
                  >
                </div>
                <div class="community-post-detail-commentbox">
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
                        placeholder="发表评论"
                        v-model="commentForm.content"
                        style="margin-bottom: 12px"
                        resize="none"
                      >
                      </el-input>
                    </el-form-item>
                  </el-form>
                  <div class="d-flex justify-content-end">
                    <el-button
                      type="primary"
                      @click="commentPost(postDetail.id)"
                      >发布</el-button
                    >
                  </div>
                </div>
                <div
                  class="community-post-detail-comment"
                  v-if="commentPage && commentPage.list.length > 0"
                >
                  <el-row>
                    <el-divider></el-divider>
                    <el-col
                      :span="24"
                      v-for="comment in commentPage.list"
                      :key="comment.id"
                    >
                      <el-card
                        class="mb-2"
                        :body-style="{ padding: '12px' }"
                        shadow="hover"
                      >
                        <div class="mb-3 d-flex align-items-center">
                          <el-avatar
                            style="margin-right: 12px"
                            :src="comment.fromUser.avatar"
                            size="small"
                          ></el-avatar>
                          <div class="mr-2 font-weight-bold">
                            {{ comment.fromUser.name }}
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
                              class="likeButton"
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
                              class="likeButton text-secondary"
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
                            @click="openReplybox(comment.id)"
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
                          v-if="replyList && replyList.length > 0"
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
                                      style="margin-right: 8px"
                                      :src="reply.fromUser.avatar"
                                      :size="20"
                                    ></el-avatar>
                                    {{ reply.fromUser.name }}
                                    <i class="el-icon-caret-right"></i>
                                    {{ reply.toUser.name }}
                                  </div>
                                  {{ reply.content }}
                                  <div
                                    class="community-post-item-reply-action d-flex justify-content-end"
                                  >
                                    <div v-if="reply.like">
                                      <el-button
                                        type="text"
                                        size="mini"
                                        class="likeButton"
                                        @click="likeComment(reply.id)"
                                      >
                                        <font-awesome-icon
                                          style="color: #409eff"
                                          :icon="['fa', 'thumbs-up']"
                                          size="sm"
                                        />
                                        点赞
                                        {{
                                          reply.likes === 0 ? "" : reply.likes
                                        }}
                                      </el-button>
                                    </div>
                                    <div v-else>
                                      <el-button
                                        type="text"
                                        size="mini"
                                        class="likeButton text-secondary"
                                        @click="likeComment(reply.id)"
                                      >
                                        <font-awesome-icon
                                          style="color: grey"
                                          :icon="['fa', 'thumbs-up']"
                                          size="sm"
                                        />
                                        点赞
                                        {{
                                          reply.likes === 0 ? "" : reply.likes
                                        }}
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
                                      secondaryRepyOpen &&
                                      reply.id == replyCommentId
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
                          v-if="replyOpen && comment.id == replyCommentId"
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
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <div v-if="hasDetailOpen == false" class="load-more">
        <div
          class="community-load-more-post mt-2 d-flex justify-content-center"
          v-if="hasMorePost == true"
        >
          <el-button round @click="loadMorePost"> 加 载 更 多 </el-button>
        </div>
        <div v-else class="mt-2 d-flex justify-content-center">
          <el-button round disabled> 没 有 更 多 了 </el-button>
        </div>
      </div>
    </div>
    <div v-else class="noInfoMsgBox">暂无帖子，快来发表你的帖子吧！</div>
    <el-divider></el-divider>
    <div class="community-post-comment">
      <div class="d-flex mb-2">
        <h5 class="mr-3">发布留言</h5>
        <div style="color: grey; font-size: 14px">
          文明上网理性发言，请遵守用户服务协议
        </div>
      </div>
      <el-form
        ref="postForm"
        :model="postForm"
        :rules="postFormRules"
        label-width="100px"
        class="mt-4"
        label-position="left"
        hide-required-asterisk
      >
        <el-form-item label="标题" prop="title">
          <el-input
            type="textarea"
            :rows="1"
            placeholder="请输入标题（登录后可以发表留言）"
            v-model="postForm.title"
            style="margin-bottom: 12px"
            maxlength="50"
            show-word-limit
          >
          </el-input>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input
            type="textarea"
            :rows="5"
            placeholder="请输入内容（登录后可以发表留言）"
            v-model="postForm.content"
            style="margin-bottom: 12px"
          >
          </el-input>
        </el-form-item>
      </el-form>
      <div class="d-flex justify-content-end">
        <el-button type="primary" @click="savePost">提交</el-button>
      </div>
    </div>
  </div>
</template>

<script>
import Vue from "vue";
import VueAMap from "vue-amap";
import Pagination from "@/components/Pagination";
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
  components: { Pagination },
  props: {
    //接受父组件传递来的数据
    // companyId: {
    //   type: String,
    // },
  },
  data() {
    return {
      //COMMUNITY ATTRIBUTES
      activeCommName: "1",
      companyId: undefined,
      //COMMUNITY POST ATTRIBUTES
      hasMorePost: false,
      postPage: {
        list: [],
      },
      listQuery: {
        page: 1,
        limit: 5,
        sort: "-hots",
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
          { required: true, message: "请填写您的帖子标题", trigger: "blur" },
        ],
        content: [
          { required: true, message: "请填写您的帖子内容", trigger: "blur" },
        ],
      },
      hasDetailOpen: false,
      //post detail
      postDetail: {},
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
      const { id } = this.$route.params;
      this.companyId = id;
      this.postForm.companyId = id;
      this.listQuery.companyId = id;
      // console.log("post - companyid", id);
      // console.log("initData -> this.$route", this.listQuery);
      // debugger;
      parseListQuery(this.$route.query, this.listQuery);
      this.getPostList();
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
        if (this.listQuery.id != undefined) {
          this.hasDetailOpen = true;
          this.getPostDetail();
        }
      });
    },
    savePost() {
      this.$refs["postForm"].validate((valid) => {
        if (valid) {
          savePost(this.postForm).then(() => {
            Toast.success("成功发布帖子");
            this.postForm.title = "";
            this.postForm.content = "";
            this.getPostList();
          });
        }
      });
    },
    getPostDetail() {
      getPostDetail(this.listQuery.id).then((response) => {
        this.postDetail = response.data;
        this.getCommentList();
        // console.log("params", this.$route.path);
        // console.log("params", this.$route.params);
        // this.$router.push({
        //   name: this.$route.path,
        //   params: this.listQuery.id,
        // });
      });
    },
    deletePost(id) {
      deletePost(id).then(() => {
        Toast.success("成功删除帖子");
        this.getPostList();
      });
    },
    //open and close post detail
    handleCollapse(id) {
      if (this.hasDetailOpen == true) {
        this.hasDetailOpen = false;
        this.listQuery.id = undefined;
        this.handleRouteList();
      } else {
        this.hasDetailOpen = true;
        this.listQuery.id = id;
        // this.getPostDetail();
      }
      this.getPostList();
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
      this.$refs["commentForm"][0].validate((valid) => {
        if (valid) {
          commentPost(this.commentForm).then(() => {
            Toast.success("评论成功");
            this.commentForm.content = "";
            this.getCommentList();
          });
        }
      });
    },
    loadMorePost() {
      this.listQuery.limit += 5;
      this.getPostList();
    },
    //POST METHODS END

    //COMMENT METHOD
    likeComment(id) {
      likeComment(id).then((response) => {
        this.getCommentList();
      });
    },
    getCommentList() {
      let data = {
        page: 1,
        limit: 10,
        sort: "-id",
        fromId: undefined,
        toId: undefined,
        ownerId: undefined,
      };
      getCommentList(data).then((response) => {
        let temp_list = response.data.list;
        let actual_list = [];
        let reply_list = [];
        let comment_id_list = [];
        let score_comment_list = [];

        for (let i = 0; i < temp_list.length; i++) {
          let comment = temp_list[i];
          comment_id_list.push(comment.id);
          //帖子的评论
          if (comment.type === 1 && comment.ownerId === this.postDetail.id) {
            actual_list.push(comment);
          }
        }

        //评论的评论 二级评论
        for (let i = 0; i < temp_list.length; i++) {
          let comment = temp_list[i];
          if (
            comment.type === 3 &&
            comment_id_list.indexOf(comment.ownerId) != -1
          ) {
            reply_list.push(comment);
          }
        }

        this.commentPage = response.data;
        this.commentPage.list = actual_list;
        this.replyList = reply_list;
      });
      this.$emit("complete");
    },
    getRepyList() {},
    deleteComment(id, type) {
      deleteComment(id).then(() => {
        Toast.success("成功删除评论");
        this.getCommentList();
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
            this.getCommentList();
          });
        }
      });
    },
    openReplybox(id) {
      if (this.replyCommentId === id) {
        //如果是上次点击
        this.replyOpen = !this.replyOpen;
      } else {
        this.replyOpen = true;
      }
      this.replyCommentId = id;
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

.community-container {
  width: 100%;
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
          width: 300px;
          min-width: 300px;
          display: flex;
          flex-direction: row;
          justify-content: space-around;
        }
      }
    }
  }
}
</style>