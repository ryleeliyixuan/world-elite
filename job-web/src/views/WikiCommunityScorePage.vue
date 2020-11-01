<template>
  <div class="community-score-container">
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
                  {{ score.fromUser.name }}
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
                    <font-awesome-icon
                      style="color: #409eff"
                      :icon="['fa', 'thumbs-up']"
                      size="sm"
                    />
                    点赞
                    {{ score.likes === 0 ? "" : score.likes }}
                  </el-button>
                </div>
                <div v-else>
                  <el-button
                    type="text"
                    size="mini"
                    class="likeButton text-secondary"
                    @click="likeScore(score.id)"
                  >
                    <font-awesome-icon
                      style="color: grey"
                      :icon="['fa', 'thumbs-up']"
                      size="sm"
                    />
                    点赞
                    {{ score.likes === 0 ? "" : score.likes }}
                  </el-button>
                </div>
                <el-button
                  type="text"
                  size="mini"
                  icon="el-icon-s-comment"
                  @click="
                    openScoreCommentbox(score.id, score.likes, score.comments)
                  "
                  >评论 {{ score.comments }}
                </el-button>
                <el-button
                  type="text"
                  size="mini"
                  @click="reportScore(score.id)"
                  icon="el-icon-s-flag"
                  >举报</el-button
                >
                <el-button
                  icon="el-icon-delete-solid"
                  type="text"
                  size="mini"
                  @click="deleteScore(score.id)"
                  v-if="myScoreId === score.id"
                  >删除我的评分</el-button
                >
              </div>
              <div
                class="community-score-comment-list"
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
                        style="mb-2"
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
                            >{{ comment.fromUser.name }}</span
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
                            @click="deleteComment(comment.id, 2)"
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
                                      @click="deleteComment(reply.id)"
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
    <div v-if="scorePage && scorePage.list.length === 0" class="noInfoMsgBox">
      暂无评分，快来发表你的评分吧！
    </div>
    <el-divider></el-divider>
    <div class="community-score-comment-container" id="save">
      <!-- 如果登录，显示评分界面 -->
      <div v-if="token" class="community-score-comment">
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
          label-width="100px"
          class="mt-4"
          label-position="left"
          hide-required-asterisk
        >
          <el-form-item label="评分" prop="score">
            <el-rate v-model="scoreForm.score"></el-rate>
          </el-form-item>
          <el-form-item label="评论" prop="content">
            <el-input
              type="textarea"
              :rows="5"
              resize="none"
              placeholder="请输入评论（登录后可以发表评论）"
              v-model="scoreForm.content"
              style="margin-bottom: 12px"
            >
            </el-input>
          </el-form-item>
        </el-form>
        <div class="d-flex justify-content-end align-items-end">
          <el-checkbox class="mr-4" v-model="scoreForm.anonymous">
            匿名发表
          </el-checkbox>
          <el-button type="primary" @click="saveScore">
            {{ hasMyScore == true ? "修改我的评分" : "提交" }}
          </el-button>
        </div>
      </div>
      <!-- 如果未登录，跳转至登录界面 -->
      <div v-else class="noInfoMsgBox">
        <el-button
          type="primary"
          icon="el-icon-user-solid"
          @click="onLoginClick"
        >
          登录后可发表评分，点此登录
        </el-button>
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
        let score_reply_list = [];

        for (let i = 0; i < temp_list.length; i++) {
          let comment = temp_list[i];
          comment_id_list.push(comment.id);
          //评分的评论
          if (
            comment.type === 2 &&
            this.scorePostId.indexOf(comment.ownerId) != -1
          ) {
            score_comment_list.push(comment);
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

        this.replyList = reply_list;
        this.scoreCommentList = score_comment_list;
      });
      this.$emit("complete");
    },
    deleteComment(id, type) {
      deleteComment(id).then(() => {
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
      deleteScore(id).then(() => {
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
      this.getCommentList();
    },
    onLoginClick() {
      this.$router.push("/login");
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
.community-score-header {
  display: flex;
  align-items: center;
}

@media screen and (max-width: 768px) {
  .community-score-header {
    display: flex;
    flex-direction: column;
  }
}
</style>