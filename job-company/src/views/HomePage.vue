<template>
  <div class="app-container">
    <b-img :src="picUrl" class="app-bg-img"></b-img>
    <div class="content-box">
      <h1 class="slogon">留学生专属招聘平台</h1>
      <p class="slogon-description mt-2">轻松招聘高素质海外留学人才</p>
      <el-button type="primary" round class="button-join" @click="handleJoin">免费发布职位</el-button>
    </div>
  </div>
</template>

<script>
export default {
  name: "HomePage",
  data() {
    return {
      picUrl:
        "http://worldelite-debug.oss-cn-shanghai.aliyuncs.com/static/home-ad.svg"
    };
  },
  methods:{
    handleJoin(){
      this.$router.push('/register');
    }
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.app-bg-img {
  position: fixed;
  width: 500px;
  height: 500px;
  right: 200px;
  z-index: -1;
}

.content-box {
  width: 1200px;
  padding-top: 180px;
  margin: 0 auto;
}

.slogon {
  font-size: 60px;
  font-weight: 400;
  color: #333;
}

.slogon-description {
  font-size: 20px;
  font-weight: 350;
  color: #888;
}

.button-join{
  width: 200px;
  height: 50px;
  border-radius: 50px;
  font-size: 20px;
}
</style>
