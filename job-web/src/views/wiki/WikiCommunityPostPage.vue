<template>
  <div class="community-post-container" v-if="postPage">
    <div
      v-if="postPage.list && postPage.list.length > 0"
      class="community-post-display"
    >
      <div class="community-post-switch mb-4 d-flex justify-content-end">
        <el-switch
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
                    <i class="el-icon-time"></i> {{ post.updateTime }}
                  </el-tag>
                  <el-button
                    type="text"
                    size="mini"
                    @click="handleOpenPostDetail(post.id)"
                  >
                    查看详情
                  </el-button>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <div class="load-more">
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
        <h5 class="mr-3">发布帖子</h5>
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
        <el-form-item label="标签">
          <div>
            <el-tag
              :key="tag"
              v-for="tag in dynamicTags"
              closable
              :disable-transitions="false"
              @close="handleClose(tag)"
            >
              {{ tag }}
            </el-tag>
            <el-input
              class="input-new-tag"
              v-if="inputVisible"
              v-model="inputValue"
              ref="saveTagInput"
              size="small"
              @blur="handleInputConfirm"
            >
            </el-input>
            <el-button
              v-else
              class="button-new-tag"
              size="small"
              icon="el-icon-circle-plus"
              @click="showInput"
            >
              添 加 新 标 签
            </el-button>
          </div>
        </el-form-item>
      </el-form>
      <tinymce
        v-loading="loading"
        v-model="postForm.content"
        :width="'100%'"
      ></tinymce>
      <div class="mt-2 d-flex justify-content-end">
        <el-button type="primary" @click="savePost" :loading="saveLoading"
          >发 布 帖 子</el-button
        >
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
import tinymce from "@/components/Tinymce";

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
  components: { Pagination, tinymce },
  data() {
    return {
      //tag
      dynamicTags: [],
      inputVisible: false,
      inputValue: "",
      //save post
      loading: false,
      saveLoading: false,
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
      this.saveLoading = true;
      this.postForm.tags = this.dynamicTags;
      this.$refs["postForm"].validate((valid) => {
        if (valid) {
          savePost(this.postForm)
            .then(() => {
              Toast.success("成功发布帖子");
              this.postForm.title = "";
              this.postForm.content = "";
              this.dynamicTags = [];
              this.getPostList();
            })
            .finally(() => {
              this.saveLoading = false;
            });
        }
      });
    },
    handleOpenPostDetail(id) {
      const urlRootPath = `/company/${this.companyId}/community/postdetail`;
      this.$router.push({
        path: urlRootPath,
        query: { postId: id },
      });
    },
    loadMorePost() {
      this.listQuery.limit += 5;
      this.getPostList();
    },
    //POST METHODS END
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

.post-content {
  width: 100%;
  float: none;
}

.button-new-tag {
  margin-left: 10px;
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