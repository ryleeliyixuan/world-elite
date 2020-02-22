<template>
  <div class="app-nav">
    <el-row align="middle" type="flex">
      <el-col :span="4">
        <div class="logo">
          <el-link class="logo-text" type="primary" href="/" :underline="false">World Elite</el-link>
        </div>
      </el-col>
      <el-col :span="4">
        <el-menu :router="true" mode="horizontal">
          <el-menu-item class="nav-item" index="/job-list">职位</el-menu-item>
          <el-menu-item class="nav-item" index="#">活动</el-menu-item>
        </el-menu>
      </el-col>
      <el-col :span="10" class="ml-4">
        <el-autocomplete
          v-model="keyword"
          class="input-search w-100"
          placeholder="输入职位、公司"
          suffix-icon="el-icon-search"
          :hide-loading="true"
          :trigger-on-focus="false"
          @change.native="handleSearch"
        ></el-autocomplete>
      </el-col>
      <el-col :span="6">
        <div class="text-right" v-if="token === undefined || token === ''">
          <el-link :underline="false" class="p-4 mr-2" @click="$router.push('/register')"><b>立即加入</b></el-link>
          <el-button type="primary" @click="$router.push('/login')">登录</el-button>
        </div>
        <div class="text-right" v-else>
          <el-link :underline="false" class="mr-4 p-2 nav-message">
            <el-badge :value="200" :max="99" class="item" is-dot>
              <i class="el-icon-message-solid"></i>
            </el-badge>
          </el-link>
          <el-dropdown style="vertical-align: middle;">
            <span class="el-dropdown-link">
              <el-avatar size="35" icon="el-icon-user-solid"></el-avatar>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>
                <el-link class="no-decoration" :underline="false" href="/edit-resume">我的简历</el-link>
              </el-dropdown-item>
              <el-dropdown-item>
                <el-link class="no-decoration" :underline="false" href="/apply-jobs">我的投递</el-link>
              </el-dropdown-item>
              <el-dropdown-item>
                <el-link class="no-decoration" :underline="false" href="/favorites">我的收藏</el-link>
              </el-dropdown-item>
              <el-dropdown-item @click.native="handleLogout" class="text-danger">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { mapGetters } from "vuex";

export default {
  name: "MainNavBar",
  computed: {
    ...mapGetters(["token", "name"])
  },
  data() {
    return {
      activeIndex: "0",
      keyword: this.$store.getters.keyword
    };
  },
  methods: {
    handleLogout() {
      this.$store.dispatch("user/LOGOUT").then(() => {
        this.$router.push({ path: "/" });
      });
    },
    handleSearch(){
      this.$router.push({path: '/job-list', query: {q: this.keyword}});
    }
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.app-nav {
  width: 1200px;
  padding: 20px 0px;
  margin: 0 auto;
}

.logo-text {
  font-size: 30px;
}

.nav-item {
  font-size: 18px;
  font-weight: 600;
}

.nav-message {
  font-size: 22px;
}

a.no-decoration:hover {
  text-decoration: none;
}

a.logo-text:hover,
a.nav-item:hover {
  text-decoration: none;
}
</style>
