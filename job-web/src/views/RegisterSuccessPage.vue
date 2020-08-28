<template>
  <div class="app-container container">
    <div class="m-4 text-center">
      <i class="el-icon-success success-icon"></i>
    </div>
    <h3 class="m-4 text-center">注册成功</h3>
    <div class="m-4 text-center">
      <el-button type="primary" @click="goEditResume">完善简历</el-button>
    </div>
    <div class="text-center mt-4">
      <p>关注我们公众号</p>
      <el-image src="https://oss.myworldelite.com/static/worldelite-gongzhonghao.jpg" />
    </div>
  </div>
</template>

<script>
export default {
  name: "RegisteSuccessrPage",

  data() {
    return {};
  },
  methods: {
    goEditResume() {
      this.$router.replace("/edit-resume");
    }
  }
};
</script>
<style scoped>
.success-icon {
  font-size: 60px;
  color: #67c23a;
}
</style>
