<template>
  <div class="community-container">
    <el-tabs v-model="activeTabName" type="card" @tab-click="handleTabClick">
      <el-tab-pane label="闲聊区" name="post"> </el-tab-pane>
      <el-tab-pane label="评分区" name="score"> </el-tab-pane>
    </el-tabs>
    <router-view></router-view>
  </div>
</template>

<script>
import Vue from "vue";
import VueAMap from "vue-amap";
import Pagination from "@/components/Pagination";
import { formatListQuery, parseListQuery } from "@/utils/common";

import { WikiCommunityPostPage } from "./WikiCommunityPostPage";
import { WikiCommunityScorePage } from "./WikiCommunityScorePage";

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
  name: "WikiCommunityPage",
  components: { Pagination },
  data() {
    return {
      //COMMUNITY ATTRIBUTES
      activeTabName: "post",
      companyId: undefined,
    };
  },
  created() {
    this.initData();
    this.initTabActive();
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
    initTabActive() {
      const name = this.$route.name;
      const lastIndex = name.lastIndexOf("-");
      const getName =
        lastIndex !== -1 ? name.substring(lastIndex + 1) : this.activeTabName;
      console.log("getName", getName);
      if (getName) {
        this.activeTabName = getName;
      }
    },
    initData() {
      const { id } = this.$route.params;
      this.companyId = id;
    },
    handleTabClick(tab, event) {
      const name = tab.name;
      const urlRootPath = `/company/${this.companyId}/community/`;
      this.$router.push({
        path: urlRootPath + name,
      });
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

.community-container {
  width: 100%;
}
</style>