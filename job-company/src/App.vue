<template>
  <div id="app">
    <MainNavBar/>
    <router-view />
  </div>
</template>

<script>
import MainNavBar from "@/components/MainNavBar";
export default {
  components: { MainNavBar },
  name: "app"
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
