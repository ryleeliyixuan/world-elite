<template>
  <div id="app">
    <MainNavBar />
    <transition name="fade-transform" mode="out-in">
      <router-view @complete="onComplete" @receiveMessage="onReceiveMessage" />
    </transition>
    <transition name="fade">
      <FooterView @complete="onComplete" v-if="show" />
    </transition>
    <el-backtop></el-backtop>
<!--    <svg-icon @click="handlerChat" icon-class="chat3" class="chat" />-->
<!--    <div class="red-dot" v-if="showRedDot" />-->
  </div>
</template>

<script>
import MainNavBar from "@/components/MainNavBar";
import FooterView from "@/components/FooterView";

export default {
  data() {
    return {
      show: false,
      showRedDot: false,
    };
  },
  components: { MainNavBar, FooterView },
  name: "App",
  watch: {
    $route(route) {
      this.show = false;
      if (this.$route.path === "/chat") {
        this.showRedDot = false;
      }
    },
  },
  methods: {
    onComplete() {
      this.show = true;
    },

    handlerChat() {
      if (this.$route.path !== "/chat") {
        this.$router.push({ path: "/chat" });
      }
    },

    onReceiveMessage() {
      if (this.$route.path !== "/chat") {
        this.showRedDot = true;
      }
    },
  },
};
</script>

<style lang="scss" scoped>
#app {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;

  .chat {
    position: fixed;
    top: 650px;
    right: 80px;
    width: 60px;
    height: 60px;
    padding: 10px;

    &:hover {
      cursor: pointer;
      color: #409eff;
    }
  }

  .red-dot {
    position: fixed;
    top: 654px;
    right: 85px;
    background: red;
    width: 14px;
    height: 14px;
    border-radius: 50%;
  }
}
</style>
