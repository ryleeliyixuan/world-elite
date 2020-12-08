<template>
  <div class="community-score-container">
    <el-menu
      class="mt-2 mb-4"
      :default-active="tabIndex"
      mode="horizontal"
      @select="handleSelectTab"
    >
      <el-menu-item index="score">评价</el-menu-item>
      <el-menu-item index="reply">回复</el-menu-item>
    </el-menu>
    <router-view></router-view>
  </div>
</template>

<script>
import waves from "@/directive/waves"; // waves directive

export default {
  name: "CommunityScoreIndexPage",
  directives: { waves },
  data() {
    return {
      tabIndex: "score",
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
      if (getName) {
        this.tabIndex = getName;
      }
    },
    handleSelectTab(tabIndex) {
      const urlRootPath = `/wiki/community-score/`;
      this.$router.push({
        path: urlRootPath + tabIndex,
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.community-score-container {
  width: 100%;
}
</style>
