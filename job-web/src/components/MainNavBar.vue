<template>
  <div class="app-nav container">
    <el-row align="middle" type="flex">
      <el-col :span="4">
        <div class="logo">
          <el-link class="logo-text" type="primary" href="/" :underline="false">{{$t('app_name')}}</el-link>
        </div>
      </el-col>
      <el-col :span="4">
        <el-menu :router="true" mode="horizontal" :default-active="activeIndex" @select="handleSelect">
          <el-menu-item class="nav-item" index="/job-list">职位</el-menu-item>
          <el-menu-item class="nav-item" index="/activity-list">活动</el-menu-item>
        </el-menu>
      </el-col>
      <el-col :span="10" class="ml-4">
        <el-input
          v-model="keyword"
          class="input-search w-100"
          :placeholder="searchPlaceHolder"
          @keyup.enter.native="handleSearch"
        >
        <i slot="suffix" class="el-input__icon el-icon-search" @click="handleSearch"></i>
        </el-input>
      </el-col>
      <el-col :span="6">
        <div class="text-right" v-if="token === undefined || token === ''">
          <el-link :underline="false" class="p-4 mr-2" @click="$router.push('/register')">
            <b>立即加入</b>
          </el-link>
          <el-button type="primary" @click="$router.push('/login')">登录</el-button>
          <el-link :underline="false" class="ml-4 icon-company" :href="companyHomeUrl"><svg-icon icon-class="company" /></el-link>
        </div>
        <div class="text-right" v-else>
          <el-popover placement="bottom-end" width="300" trigger="hover" @show="getMessageList" title="系统通知">
            <div class="message-list" v-if="newMessageList && newMessageList.length !== 0">
                <div class="message-item" v-for="message in newMessageList" :key="message.id">
                    <el-badge is-dot v-if="message.readFlag === 0"/>
                    {{message.content}} <el-link v-if="message.url && message.url != ''" :href="meesage.url" :underline="false">查看</el-link>
                </div>
            </div>
            <div class="text-center p-2" v-else>暂无新消息</div>
            <div class="text-center p-2"><el-link type="primary" :underline="false" @click="goMessageList">查看全部</el-link></div>
            <el-link :underline="false" class="mr-4 p-2 nav-message" slot="reference" @click="goMessageList">
              <el-badge is-dot v-if="messageCount !== 0">
                <i class="el-icon-message-solid"></i>
              </el-badge>
              <i class="el-icon-message-solid" v-else></i>
            </el-link>
          </el-popover>
          <el-dropdown style="vertical-align: middle;">
            <span class="el-dropdown-link">
              <el-avatar :size="35" icon="el-icon-user-solid" :src="avatar"></el-avatar>
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
import { mapGetters, mapMutations } from "vuex";
import { getUnReadMessageCount, getMessageList } from "@/api/message_api";

export default {
  name: "MainNavBar",
  computed: {
    ...mapGetters(["token", "name", "avatar", "messageCount"]),
    searchPlaceHolder(){
       return this.activeIndex == '/activity-list' ? '搜索活动' : '输入职位、公司'
    }
  },
  data() {
    return {
      activeIndex: "",
      newMessageList: [],
      messageListForm: {
         page: 1,
         limit: 5
      },
      keyword: this.$store.getters.keyword,
      companyHomeUrl: process.env.VUE_APP_COMPANY_URL
    };
  },
  watch: {
    $route() {
      this.activeIndex = this.$route.path;
      this.getUnReadMessageCount();
    }
  },
  created() {
    this.activeIndex = this.$route.path;
    this.getUnReadMessageCount();
  },
  methods: {
    ...mapMutations({
       setMessageCount: 'setting/MESSAGE_COUNT'
    }),
    handleLogout() {
      this.$store.dispatch("user/LOGOUT").then(() => {
        this.$router.push({ path: "/" });
      });
    },
    handleSearch() {
      const cur_path = this.$route.path;
      if(cur_path == '/job-list' || cur_path == '/activity-list'){
         this.$store.commit("setting/SET_KEYWORD", this.keyword);
      } else {
         this.$router.push({ path: "/job-list", query: { keyword: this.keyword } });
      }
    },
    handleSelect(){
       this.keyword = ''
       this.$store.commit("setting/SET_KEYWORD", this.keyword);
    },
    getUnReadMessageCount() {
      if (this.token && this.token !== "") {
        getUnReadMessageCount().then(response => {
           this.setMessageCount(response.data);
        });
      }
    },
    getMessageList() {
      if (this.token && this.token !== "") {
         getMessageList(this.messageListForm).then(response => {
            this.newMessageList = response.data.list;
         });
      }
    },
    goMessageList() {
       this.$router.push('/messages');
    }
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.app-nav {
  padding: 20px 0px;
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

.icon-company{
  font-size: 26px;
}

.message-item{
  padding: 10px 5px;
  border-bottom: 1px solid #EEE;
  line-height: 1.5em;
  font-size: 15px;
  color: #888;
}
</style>
