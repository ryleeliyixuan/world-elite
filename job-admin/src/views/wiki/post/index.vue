<template>
  <div class="community-post-container">
    <el-menu
      class="mt-2 mb-4"
      :default-active="tabIndex"
      mode="horizontal"
      @select="handleSelectTab"
    >
      <el-menu-item index="post">帖子</el-menu-item>
      <el-menu-item index="comment">评论</el-menu-item>
      <el-menu-item index="report">举报</el-menu-item>
    </el-menu>
    <router-view></router-view>
  </div>
</template>

<script>
import waves from "@/directive/waves"; // waves directive

export default {
  name: "CommunityPostIndexPage",
  directives: { waves },
  data() {
    return {
      tabIndex: "post",
    };
  },
  created() {
    this.initTabActive();
  },
  methods: {
    initTabActive() {
      const name = this.$route.path;
      const lastIndex = name.lastIndexOf("/");
      const getName =
        lastIndex !== -1 ? name.substring(lastIndex + 1) : this.tabIndex;
      // console.log(getName, "----name----");
      if (getName) {
        this.tabIndex = getName;
      }
    },
    handleSelectTab(tabIndex) {
      const urlRootPath = `/wiki/community-post/`;
      this.$router.push({
        path: urlRootPath + tabIndex,
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.community-post-container {
  width: 100%;
}
</style>
