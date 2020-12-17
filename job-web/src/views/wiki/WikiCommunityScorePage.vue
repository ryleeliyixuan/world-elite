<template>
  <div class="community-score-container">
    <div class="d-flex align-items-center">
      <el-link :underline="false" style="color: #999999" @click="goToPost"
        >闲聊区</el-link
      >
      <span class="mr-2 ml-2" style="color: #999999"> / </span>
      <el-link :underline="false" style="color: #4895ef">评分区</el-link>
    </div>
    <div
      class="community-score-display"
      v-if="scorePage && scorePage.list.length > 0"
    >
      <div class="mb-4 d-flex justify-content-end">
        <el-switch
          v-model="scoreListQuery.sort"
          active-color="#13ce66"
          inactive-color="#ff4949"
          active-text="最新"
          active-value="-id"
          inactive-value="-hots"
          inactive-text="最热"
          @change="getScoreList"
        >
        </el-switch>
      </div>
      <el-row>
        <el-col :span="24" v-for="score in scorePage.list" :key="score.id">
          <el-card
            class="mb-2"
            :body-style="{ padding: '16px' }"
            shadow="hover"
          >
            <div class="community-score-item">
              <div class="community-score-header">
                <el-avatar
                  class="mr-2"
                  :src="score.fromUser.avatar"
                  size="small"
                ></el-avatar>
                <div style="font-weight: bold; font-size: 14px">
                  {{
                    score.fromUser.name && score.fromUser.name.length > 0
                      ? score.fromUser.name
                      : "新注册用户"
                  }}
                </div>
                <div
                  style="
                    color: grey;
                    font-size: 10px;
                    margin-left: 12px;
                    margin-right: 12px;
                  "
                >
                  {{ score.createTime }}
                </div>
                <el-rate
                  v-model="score.score"
                  disabled
                  show-score
                  text-color="#ff9900"
                  score-template="{value}"
                >
                </el-rate>
              </div>
              <div class="mb-4 p-3">
                {{ score.content }}
              </div>
              <div
                class="community-score-action mb-2 d-flex justify-content-end"
              >
                <div v-if="score.like">
                  <el-button
                    type="text"
                    size="mini"
                    class="likeButton"
                    @click="likeScore(score.id)"
                  >
<!--                    <font-awesome-icon-->
<!--                      style="color: #409eff"-->
<!--                      :icon="['fa', 'thumbs-up']"-->
<!--                      size="sm"-->
<!--                    />-->
                    <el-image
                            :src="require('@/assets/like.png')"
                            style="color: #568ed0;width: 14px;height: 14px"
                    ></el-image>
                    <span style="color: #a1aebf;padding-left: 8px;font-size: 16px;font-weight: 500;">
                      {{ score.likes === 0 ? "" : score.likes }}</span>
                  </el-button>
                </div>
                <div v-else>
                  <el-button
                    type="text"
                    size="mini"
                    class="likeButton text-secondary"
                    @click="likeScore(score.id)"
                  >
                    <el-image
                            :src="require('@/assets/like.png')"
                            style="color: #568ed0;width: 14px;height: 14px"
                    ></el-image>
                    <span style="color: #a1aebf;padding-left: 8px;font-size: 16px;font-weight: 500;">
                    {{ score.likes === 0 ? "" : score.likes }}
                    </span>
                  </el-button>
                </div>
                <el-button
                  type="text"
                  size="mini"
                  @click="openScoreCommentbox(score.id, score.likes, score.comments)
                  "
                  >
                  <el-image
                          :src="require('@/assets/comment.png')"
                          style="width: 13px;height: 13px"
                  ></el-image>
                  <span style="color: #a1aebf;padding-left: 8px;font-size: 16px;font-weight: 500;">
                  评论 {{ score.comments }}
                    </span>
                </el-button>
                <el-button
                  type="text"
                  size="mini"
                  @click="reportScore(score.id)"
                  >
                  <el-image
                          :src="require('@/assets/report1.png')"
                          style="width: 13px;height: 13px"
                  ></el-image>
                  <span style="color: #a1aebf;padding-left: 8px;font-size: 15px;font-weight: 500">举报</span>
                </el-button
                >
                <el-button
                  type="text"
                  size="mini"
                  @click="deleteScore(score.id)"
                  v-if="myScoreId === score.id"
                  ><el-image :src="require('@/assets/delete.png')"
                             style="width: 12px;height: 12px"
                ></el-image>
                  <span style="font-size: 15px;font-weight: 500;color: #A1AEBF;padding-left: 8px">删除</span>

                </el-button
                >
              </div>
              <div
                class="community-score-comment-list"
                v-loading="loading"
                element-loading-text="拼命加载评论中"
                element-loading-spinner="el-icon-loading"
                element-loading-background="rgba(0, 0, 0, 0.8)"
                v-if="
                  scoreCommentboxOpen &&
                  scoreItemOpened.id == score.id &&
                  scoreCommentList &&
                  scoreCommentList.length > 0
                "
              >
                <el-row>
                  <el-divider></el-divider>
                  <el-col
                    :span="24"
                    v-for="(comment, index) in scoreCommentList"
                    :key="comment.id"
                  >
                    <div v-if="scoreCommentList[index].ownerId === score.id">
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
                          <span
                            style="
                              margin-right: 12px;
                              font-weight: bold;
                              font-size: 10px;
                            "
                            >{{
                              comment.fromUser.name &&
                              comment.fromUser.name.length > 0
                                ? comment.fromUser.name
                                : "新注册用户"
                            }}</span
                          >
                          <span style="color: grey; font-size: 8px">{{
                            comment.createTime
                          }}</span>
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
                              <el-image
                                      :src="require('@/assets/like.png')"
                                      style="color: #568ed0;width: 14px;height: 14px"
                              ></el-image>
                              <span style="color: #a1aebf;padding-left: 8px;font-size: 16px;font-weight: 500;">
                              {{ comment.likes === 0 ? "" : comment.likes }}
                              </span>
                            </el-button>
                          </div>
                          <div v-else>
                            <el-button
                              type="text"
                              size="mini"
                              class="likeButton text-secondary"
                              @click="likeComment(comment.id)"
                            >
                              <el-image
                                      :src="require('@/assets/like.png')"
                                      style="color: #568ed0;width: 14px;height: 14px"
                              ></el-image>
                              <span style="color: #a1aebf;padding-left: 8px;font-size: 16px;font-weight: 500;">
                              {{ comment.likes === 0 ? "" : comment.likes }}
                              </span>
                            </el-button>
                          </div>
                          <el-button
                            type="text"
                            size="small"
                            @click="openReplybox(comment.id)"
                            ><el-image
                                  :src="require('@/assets/comment.png')"
                                  style="width: 13px;height: 13px"
                          ></el-image>
                            <span style="color: #a1aebf;padding-left: 8px;font-size: 16px;font-weight: 500;">回复</span> {{ comment.comments }}
                          </el-button>
                          <el-button
                            type="text"
                            size="small"
                            @click="reportComment(comment.id)"
                            >
                            <el-image
                                    :src="require('@/assets/report1.png')"
                                    style="width: 13px;height: 13px"
                            ></el-image>
                            <span style="color: #a1aebf;padding-left: 8px;font-size: 15px;font-weight: 500">举报</span>
                          </el-button
                          >
                          <el-button
                            type="text"
                            size="small"
                            @click="deleteComment(comment.id, 2)"
                            v-if="comment.fromUser.userId === userId"
                            >
                            <el-image :src="require('@/assets/delete.png')"
                                      style="width: 12px;height: 12px"
                            ></el-image>
                            <span style="font-size: 15px;font-weight: 500;color: #A1AEBF;padding-left: 8px">删除</span>
                          </el-button
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
                              <div v-if="reply.ownerId === comment.id">
                                <el-card
                                  :body-style="{ padding: '12px' }"
                                  shadow="never"
                                >
                                  <div
                                    class="mb-2"
                                    style="
                                      display: flex;
                                      align-items: center;
                                      font-size: 10px;
                                      font-weight: bold;
                                    "
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
                                        <el-image
                                                :src="require('@/assets/like.png')"
                                                style="color: #568ed0;width: 14px;height: 14px"
                                        ></el-image>
                                        <span style="color: #a1aebf;padding-left: 8px;font-size: 16px;font-weight: 500;">
                                        {{
                                          reply.likes === 0 ? "" : reply.likes
                                        }}
                                        </span>
                                      </el-button>
                                    </div>
                                    <div v-else>
                                      <el-button
                                        type="text"
                                        size="mini"
                                        class="likeButton text-secondary"
                                        @click="likeComment(reply.id)"
                                      >
                                        <el-image
                                                :src="require('@/assets/like.png')"
                                                style="color: #568ed0;width: 14px;height: 14px"
                                        ></el-image>
                                        <span style="color: #a1aebf;padding-left: 8px;font-size: 16px;font-weight: 500;">
                                        {{
                                          reply.likes === 0 ? "" : reply.likes
                                          }}</span>
                                      </el-button>
                                    </div>
                                    <el-button
                                      type="text"
                                      size="small"
                                      @click="openSecondaryReplybox(reply.id)"
                                      ><el-image
                                            :src="require('@/assets/comment.png')"
                                            style="width: 13px;height: 13px"
                                    ></el-image>
                                      <span style="color: #a1aebf;padding-left: 8px;font-size: 16px;font-weight: 500;">回复</span>
                                    </el-button>
                                    <el-button
                                      type="text"
                                      size="small"
                                      @click="reportComment(comment.id)"
                                      >
                                      <el-image
                                              :src="require('@/assets/report1.png')"
                                              style="width: 13px;height: 13px"
                                      ></el-image>
                                      <span style="color: #a1aebf;padding-left: 8px;font-size: 15px;font-weight: 500">举报</span>
                                    </el-button
                                    >
                                    <el-button
                                      type="text"
                                      size="mini"
                                      @click="deleteComment(reply.id)"
                                      v-if="reply.fromUser.userId === userId"
                                      >
                                      <el-image :src="require('@/assets/delete.png')"
                                                style="width: 12px;height: 12px"
                                      ></el-image>
                                      <span style="font-size: 15px;font-weight: 500;color: #A1AEBF;padding-left: 8px">删除</span>
                                    </el-button
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
                                        ><el-image
                                              :src="require('@/assets/comment.png')"
                                              style="width: 13px;height: 13px"
                                      ></el-image>
                                        <span style="color: #a1aebf;padding-left: 8px;font-size: 16px;font-weight: 500;">回复</span></el-button
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
                              ><el-image
                                    :src="require('@/assets/comment.png')"
                                    style="width: 13px;height: 13px"
                            ></el-image>
                              <span style="color: #a1aebf;padding-left: 8px;font-size: 16px;font-weight: 500;">回复</span></el-button
                            >
                          </div>
                        </div>
                      </el-card>
                    </div>
                  </el-col>
                </el-row>
              </div>
              <div
                class="community-score-commentbox"
                v-if="scoreCommentboxOpen && scoreItemOpened.id == score.id"
              >
                <el-form
                  ref="scoreCommentForm"
                  :rules="scoreCommentFormRules"
                  :model="scoreCommentForm"
                  label-width="80px"
                  label-position="top"
                >
                  <el-form-item label="" prop="content">
                    <el-input
                      class="mt-2"
                      type="textarea"
                      :rows="3"
                      placeholder="发表评论"
                      v-model="scoreCommentForm.content"
                      resize="none"
                    >
                    </el-input>
                  </el-form-item>
                </el-form>
                <div class="d-flex justify-content-end">
                  <el-button type="primary" @click="commentScore(score.id)"
                    >发布</el-button
                  >
                </div>
              </div>
            </div>
            <!-- <div
              v-if="curPostDetailId === score.id"
              class="community-post-dialog"
            >
              <el-divider></el-divider>
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
                  <el-button type="primary" @click="commentPost(postDetail.id)"
                    >发布</el-button
                  >
                </div>
              </div>
            </div> -->
          </el-card>
        </el-col>
      </el-row>
      <div
        class="community-load-more-post mt-2 d-flex justify-content-center"
        v-if="hasMoreScore == true"
      >
        <el-button round @click="loadMoreScore"> 加 载 更 多 </el-button>
      </div>
      <div v-else class="mt-2 d-flex justify-content-center">
        <el-button round disabled> 没 有 更 多 了 </el-button>
      </div>
    </div>
    <div
      v-if="scorePage && scorePage.list.length === 0"
      class="score-missing"
      style="margin-top: 60px"
    >
      <svg-icon
        icon-class="score-missing"
        style="height: 265px; width: 344px; margin-bottom: 17px"
      />
      <div>来做第一个评分的人吧！</div>
    </div>
    <div
      class="community-score-comment-container"
      style="margin-top: 80px"
      id="save"
    >
      <div class="community-score-comment" style="margin-bottom: 90px">
        <div class="mb-4 d-flex">
          <h5 class="mr-3">
            {{ hasMyScore == true ? "修改我的" : "发布" }}评分
          </h5>
          <div style="color: grey; font-size: 14px">
            文明上网理性发言，请遵守用户服务协议
          </div>
        </div>
        <el-form
          ref="scoreForm"
          :model="scoreForm"
          :rules="scoreFormRules"
          label-width="90px"
          class="mt-4"
          label-position="left"
          hide-required-asterisk
        >
          <el-form-item label="评分" prop="score">
            <el-rate v-model="scoreForm.score"></el-rate>
          </el-form-item>
          <el-form-item class="save-score-box" label="评论" prop="content">
            <el-input
              type="textarea"
              :rows="5"
              resize="none"
              placeholder="请输入评论"
              v-model="scoreForm.content"
              style="margin-bottom: 12px"
            >
            </el-input>
          </el-form-item>
        </el-form>
        <div class="post-score d-flex justify-content-end align-items-end">
          <el-checkbox class="mr-4" v-model="scoreForm.anonymous">
            匿名发表
          </el-checkbox>
          <el-button type="mini" @click="saveScore">
            {{ hasMyScore == true ? "修改我的评分" : "提交" }}
          </el-button>
        </div>
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

import { getCompanyInfo } from "@/api/company_api";
import { setPageTitle } from "@/utils/setting";
import { doFavorite } from "@/api/favorite_api";
import { mapGetters } from "vuex";
import Toast from "@/utils/toast";

//api
import {
  //comment
  getCommentList,
  deleteComment,
  likeComment,
  reportComment,
  replyComment,
  //score
  saveScore,
  getScoreList,
  commentScore,
  deleteScore,
  reportScore,
  likeScore,
  getMyScore,
} from "@/api/community_api";

export default {
  name: "WikiCommunityScorePage",
  components: { Pagination },
  // props: {
  //   接受父组件传递来的数据
  //   companyId: {
  //     type: String,
  //   },
  // },
  data() {
    return {
      //COMMUNITY ATTRIBUTES
      companyId: undefined,

      loading: true,

      //COMMUNITY COMMENT AND REPLY
      // commentPage: { list: [] },
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

      //COMMUNITY SCORE ATTRIBUTES
      scorePage: { list: [] },
      hasMoreScore: false,
      hasMyScore: false,
      myScoreId: undefined,
      scoreCommentboxOpen: false,
      scoreCommentList: [],
      scorePostId: [],
      scoreListQuery: {
        page: 1,
        limit: 5,
        sort: "-hots",
        fromId: undefined,
        id: undefined,
        companyId: undefined,
      },
      scoreItemOpened: {
        id: undefined,
        likes: undefined,
        comments: undefined,
      },
      scoreForm: {
        id: undefined,
        companyId: undefined,
        score: undefined,
        content: "",
        anonymous: 0,
      },
      scoreFormRules: {
        score: [{ required: true, message: "请发表您的评分", trigger: "blur" }],
        content: [
          { required: true, message: "请发表您的评论", trigger: "blur" },
        ],
      },
      scoreCommentForm: {
        id: undefined,
        ownerId: undefined,
        content: "",
      },
      scoreCommentFormRules: {
        content: [
          { required: true, message: "请发表您的评论", trigger: "blur" },
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
      // this.initData();
    },
  },
  methods: {
    initData() {
      const { id } = this.$route.params;
      this.companyId = id;
      this.scoreForm.companyId = id;
      this.scoreListQuery.companyId = id;
      getScoreList(this.scoreListQuery).then((response) => {
        this.scorePage = response.data;
        for (let i = 0; i < this.scorePage.list.length; i++) {
          this.scorePostId.push(this.scorePage.list[i].id);
        }
        if (this.token) {
          this.getMyScore(this.companyId);
        }
      });
    },
    goToPost() {
      this.$router.push(`/company/${this.companyId}/post`);
    },
    //COMMENT METHOD
    likeComment(id) {
      likeComment(id).then((response) => {
        this.getCommentList();
      });
    },
    getCommentList(id) {
      this.loading = true;
      let data = {
        page: 1,
        limit: 10,
        sort: "-id",
        fromId: undefined,
        toId: undefined,
        ownerId: id,
      };
      getCommentList(data).then((response) => {
        this.scoreCommentList = response.data.list;
        this.getReplyList();
      });
      this.loading = false;
    },
    getReplyList() {
      this.replyList = [];
      for (let i = 0; i < this.scoreCommentList.length; i++) {
        let id = this.scoreCommentList[i].id;
        let data = { ownerId: id };
        getCommentList(data).then((response) => {
          let temp_list = response.data.list;
          for (let i = 0; i < temp_list.length; i++) {
            this.replyList.push(temp_list[i]);
          }
        });
      }
    },
    deleteComment(id, type) {
      let data = {
        commentId: id,
      };
      deleteComment(data).then(() => {
        Toast.success("成功删除评论");
        this.getCommentList();
        if (type === 2) {
          this.getScoreList();
        }
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

    //SCORE METHODS
    loadMoreScore() {
      this.scoreListQuery.limit += 5;
      this.getScoreList();
    },
    getMyScore(id) {
      getMyScore(id).then((response) => {
        if (response.data != undefined) {
          this.hasMyScore = true;
          this.myScoreId = response.data.id;
          this.scoreForm.score = response.data.score;
          this.scoreForm.content = response.data.content;
          this.scoreForm.anonymous = Boolean(response.data.anonymous);
        } else {
          this.hasMyScore = false;
          this.scoreForm.id = undefined;
          this.scoreForm.score = undefined;
          this.scoreForm.content = "";
          this.scoreForm.anonymous = 0;
        }
      });
    },
    saveScore() {
      this.scoreForm.id = this.myScoreId;
      this.scoreForm.anonymous = +this.scoreForm.anonymous;
      this.$refs["scoreForm"].validate((valid) => {
        if (valid) {
          saveScore(this.scoreForm).then(() => {
            Toast.success("评分成功");
            this.getScoreList();
            this.getMyScore(this.companyId);
          });
        }
      });
    },
    deleteScore(id) {
      let data = { scoreId: id };
      deleteScore(data).then(() => {
        Toast.success("成功删除评论");
        this.hasMyScore = false;
        this.myScoreId = undefined;
        this.scoreForm.score = undefined;
        this.scoreForm.content = undefined;
        this.getScoreList();
      });
    },
    likeScore(id) {
      likeScore(id).then((response) => {
        this.getScoreList();
      });
    },
    commentScore(id) {
      this.scoreCommentForm.ownerId = id;
      this.$refs["scoreCommentForm"][0].validate((valid) => {
        if (valid) {
          commentScore(this.scoreCommentForm).then((response) => {
            Toast.success("评论成功");
            this.scoreCommentForm.content = "";
            this.getCommentList();
          });
        }
      });
    },
    reportScore(id) {
      let data = {
        ownerId: id,
        optionId: undefined,
        content: "",
      };
      reportScore(data).then(() => {
        Toast.success("举报成功");
      });
      this.$emit("complete");
    },
    getScoreList() {
      getScoreList(this.scoreListQuery).then((response) => {
        this.scorePage = response.data;
        for (let i = 0; i < this.scorePage.list.length; i++) {
          this.scorePostId.push(this.scorePage.list[i].id);
        }
        this.$emit("complete");
      });
    },
    openScoreCommentbox(id, likes, comments) {
      if (this.scoreItemOpened.id === id) {
        //如果是上次点击
        this.scoreCommentboxOpen = !this.scoreCommentboxOpen;
      } else {
        this.scoreCommentboxOpen = true;
      }
      this.scoreItemOpened.id = id;
      this.scoreItemOpened.likes = likes;
      this.scoreItemOpened.comments = comments;
      this.getCommentList(id);
    },
    onLoginClick() {
      this.$router.push("/login");
    },
  },
};
</script>

<style scoped lang="scss">
.post-score {
  margin-left: 90px;
  padding: 4px 5px 4px 0px;
  border-radius: 3px;
  border: 1px solid #b4c4d0;
  border-top: 0px;
}


/deep/ .save-score-box .el-textarea {
  margin-bottom: 0px !important;
}

/deep/ .save-score-box .el-textarea__inner {
  border-radius: 3px;
  border: 1px solid #b4c4d0;
  margin-bottom: 0px;
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

/deep/ .el-form-item {
  margin-bottom: 0px;
}

.el-form-item + .el-form-item {
  margin-top: 16px;
}

/deep/ .post-score .el-button {
  width: 117px;
  background: #f1f6fd;
  border-radius: 5px;
  font-size: 16px;
  font-family: PingFangSC-Medium, PingFang SC;
  font-weight: 500;
  color: #568ed0;
  line-height: 22px;
  border: 0px;
}

.score-missing {
  display: flex;
  flex-direction: column;
  align-items: center;
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
.community-score-header {
  display: flex;
  align-items: center;
}

.community-score-container {
  width: 100%;
  box-shadow: 0px 18px 14px 3px rgba(205, 213, 224, 0.3);
  background: #ffffff;
  padding: 20px 50px 20px 50px;
  margin-bottom: 80px;
}

@media screen and (max-width: 768px) {
  .community-score-header {
    display: flex;
    flex-direction: column;
  }
}
</style>