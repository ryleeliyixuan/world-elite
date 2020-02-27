<template>
  <div class="app-nav">
    <el-row align="middle" type="flex">
      <el-col :span="5">
        <div class="logo">
          <el-link class="logo-text" type="primary" href="/" :underline="false">World Elite 企业端</el-link>
        </div>
      </el-col>
      <el-col :span="6">
        <el-menu
          :router="true"
          :default-active="activeIndex"
          mode="horizontal"
          v-if="token && token !== ''"
        >
          <el-menu-item class="nav-item" index="/manage-job">职位管理</el-menu-item>
          <el-menu-item class="nav-item" index="/manage-resume">简历管理</el-menu-item>
        </el-menu>
      </el-col>
      <el-col :span="13">
        <div class="text-right" v-if="token === undefined || token === ''">
          <el-link :underline="false" class="p-4 mr-2" @click="$router.push('/register')">
            <b>免费加入</b>
          </el-link>
          <el-button type="primary" @click="$router.push('/login')">登录</el-button>
        </div>
        <div class="text-right" v-else>
          <el-popover placement="bottom-end" width="300" trigger="hover" @show="getMessageList" title="系统通知">
            <div class="message-list" v-if="newMessageList && newMessageList.length !== 0">
                <div class="message-item" v-for="message in newMessageList" :key="message.id">
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
              <el-avatar :size="35" icon="el-icon-user-solid"></el-avatar>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>
                <el-link class="no-decoration" :underline="false" href="/edit-company">公司主页</el-link>
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
    ...mapGetters(["token", "name", "messageCount"])
  },
  data() {
    return {
      activeIndex: "/manage-job",
      newMessageList: [],
      unreadMessageCount: 0,
      keyword: this.$store.getters.keyword
    };
  },
  watch: {
    $route() {
      this.activeIndex = this.$route.path;
    }
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
  width: 1200px;
  padding: 20px 0px;
  margin: 0 auto;
}

.logo-text {
  font-size: 25px;
}

.nav-item {
  font-size: 16px;
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
