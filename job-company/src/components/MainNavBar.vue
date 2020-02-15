<template>
  <div>
    <b-navbar toggleable="lg" type="dark" variant="info" class="main-nav">
      <b-navbar-brand href="/">World Edlite</b-navbar-brand>

      <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

      <b-collapse id="nav-collapse" is-nav>
        <b-navbar-nav>
          <b-nav-item to="/manage-job">职位</b-nav-item>
          <b-nav-item href="#">活动</b-nav-item>
        </b-navbar-nav>

        <!-- Right aligned nav items -->
        <b-navbar-nav class="ml-auto">
          
          <b-navbar-nav v-if="token === undefined || token === ''">
            <b-nav-item to="/register">注册</b-nav-item>
            <b-nav-item to="/login">登录</b-nav-item>
          </b-navbar-nav>

          <b-nav-item-dropdown right v-else>
            <!-- Using 'button-content' slot -->
            <template v-slot:button-content>
              <em>{{ name == undefined ? '':  name}}</em>
            </template>
            <b-dropdown-item @click="handleLogout">退出登录</b-dropdown-item>
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
      'name',
      'avatar'
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
  .main-nav{
    padding: 10px 80px;
  }
</style>
