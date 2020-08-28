<template>
  <div id="app">
    <MainNavBar/>
    <router-view />
    <FooterView/>
    <el-backtop></el-backtop>
  </div>
</template>

<script>
import MainNavBar from "@/components/MainNavBar";
import FooterView from '@/components/FooterView'
export default {
  components: { MainNavBar, FooterView },
  name: "App"
};
</script>

<style>
#app {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
}
</style>
