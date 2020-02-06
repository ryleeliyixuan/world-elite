<template>
  <div>
    <b-navbar toggleable="lg" type="dark" variant="info">
      <b-navbar-brand href="/">World Edlite</b-navbar-brand>

      <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

      <b-collapse id="nav-collapse" is-nav>
        <b-navbar-nav>
          <b-nav-item href="#">招聘</b-nav-item>
          <b-nav-item href="#">活动</b-nav-item>
        </b-navbar-nav>

        <b-nav-form class="ml-4">
          <b-form-input size="sm" class="mr-sm-2 nav-search-bar" placeholder="输入求职岗位/公司"></b-form-input>
          <b-button size="sm" class="my-2 my-sm-0" type="submit">搜索</b-button>
        </b-nav-form>

        <!-- Right aligned nav items -->
        <b-navbar-nav class="ml-auto">
          
          <b-navbar-nav v-if="loginUser === undefined">
            <b-nav-item to="/register">注册</b-nav-item>
            <b-nav-item to="/login">登录</b-nav-item>
          </b-navbar-nav>

          <b-nav-item-dropdown right v-else>
            <!-- Using 'button-content' slot -->
            <template v-slot:button-content>
              <em>{{ loginUser.name == '' ? '新用户': loginUser.name }}</em>
            </template>
            <b-dropdown-item to="/edit-resume">我的简历</b-dropdown-item>
            <b-dropdown-item href="#">退出登录</b-dropdown-item>
          </b-nav-item-dropdown>
        </b-navbar-nav>
      </b-collapse>
    </b-navbar>
  </div>
</template>

<script>

import { mapGetters } from 'vuex'

export default {
  name: "MainNavBar",
  computed: {
    ...mapGetters([
      'token',
      'loginUser',
    ])    
  },
  methods: {
     handleLogout(){
       this.$store.dispatch('user/LOGOUT').then(() => {
          this.$router.push({ path: "/" });
       });
     }
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  input.nav-search-bar{
    width: 350px;
  }
</style>
