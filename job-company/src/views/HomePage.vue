<template>
  <div class="app-container">
    <div class="intro-box">
      <b-img :src="homeConfig.picUrl" class="app-bg-img"></b-img>
      <div class="slogon-box">
        <h1 class="slogon">{{homeConfig.mainTitle}}</h1>
        <p class="slogon-description mt-2">{{homeConfig.subTitle}}</p>
        <el-button type="primary" round class="button-join" @click="handleJoin" v-if="token === undefined || token == ''">马上加入</el-button>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { getHomeConfig } from '@/api/config_api'

export default {
  name: "HomePage",
  computed: {
    ...mapGetters(["token"])
  },
  data() {
    return {
       homeConfig: {},
    };
  },
  created(){
    this.initData();
  },
  methods:{
    initData(){
      getHomeConfig(9).then(response=>{
         this.homeConfig = response.data
      })
    },
    handleJoin(){
      this.$router.push(this.token?'/edit-job':'/register');
    }
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

.intro-box {
  position: relative;
  width: 1200px;
  margin: 0 auto;
  height: calc(100vh - 100px);
}

.intro-box .app-bg-img {
  position: absolute;
  width: 500px;
  height: 500px;
  top: calc(50vh - 350px);
  right: 100px;
  z-index: -1;
}

.intro-box .slogon-box {
  padding-top: calc(50vh - 180px);
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
