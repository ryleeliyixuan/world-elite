<template>
  <div class="nav-container">
    <div class="nav-left-container">
      <router-link class="logo-image" to="/"></router-link>
      <el-menu
        :router="true"
        mode="horizontal"
        :default-active="activeIndex"
        @select="handleSelect"
        class="menu-container"
      >
        <el-menu-item class="nav-item" index="/job-list">职位</el-menu-item>
        <el-menu-item class="nav-item" index="/wiki-card">百科</el-menu-item>
        <el-menu-item class="nav-item" index="/activity-list"
          >活动</el-menu-item
        >
        <el-menu-item class="nav-item" index="/mock/interview"
          >模拟面试</el-menu-item
        >
      </el-menu>
    </div>

    <div class="nav-right-container">
      <el-autocomplete
        v-model="keyword"
        class="input-search"
        :placeholder="searchPlaceHolder"
        @keyup.enter.native="handleSearch"
        :fetch-suggestions="querySearch"
        @select="handleSearch"
        :trigger-on-focus="false"
      >
        <i
          slot="suffix"
          class="el-input__icon el-icon-search"
          @click="handleSearch"
        />
      </el-autocomplete>
      <!-- 未登录 -->
      <div class="user-container" v-if="!token">
        <el-link :underline="false" class="join" @click="routeTo('register')">
          <b>立即加入</b>
        </el-link>
        <el-button type="primary" @click="routeTo('/login')" size="small"
          >登录</el-button
        >
      </div>
      <!-- 已登录 -->
      <div class="user-container" v-else>
        <svg-icon @click="routeTo('/chat')" icon-class="chat2" class="chat" />
        <svg-icon
          @click="routeTo('/edit-resume')"
          icon-class="resume"
          class="chat"
        />
        <!-- 系统通知 -->
        <el-popover
          placement="bottom-end"
          width="300"
          trigger="hover"
          @show="getMessageList"
          title="系统通知"
        >
          <!-- 内容 -->
          <div v-if="messageList.length !== 0">
            <div
              class="message-item"
              v-for="message in messageList"
              :key="message.id"
            >
              <el-badge is-dot v-if="message.readFlag === 0" />
              {{ message.content }}
              <el-link
                v-if="message.url"
                :href="message.url"
                :underline="false"
              >
                查看
              </el-link>
            </div>
          </div>
          <div class="message-text" v-else>暂无新消息</div>

          <!-- 按钮 -->
          <div class="message-text">
            <el-link
              type="primary"
              :underline="false"
              @click="routeTo('messages')"
              >查看全部</el-link
            >
          </div>

          <!-- 图标 -->
          <el-link
            :underline="false"
            class="nav-message"
            slot="reference"
            @click="routeTo('messages')"
          >
            <el-badge v-if="messageList.length !== 0" is-dot>
              <i class="el-icon-message-solid"></i>
            </el-badge>
            <i v-else class="el-icon-message-solid"></i>
          </el-link>
        </el-popover>

        <!-- 用户头像 -->
        <el-dropdown>
          <el-avatar
            :size="35"
            icon="el-icon-user-solid"
            :src="avatar"
          ></el-avatar>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item>
              <el-link :underline="false" @click="routeTo('/edit-resume')"
                >我的简历</el-link
              >
            </el-dropdown-item>
            <el-dropdown-item>
              <el-link :underline="false" @click="routeTo('/apply-jobs')"
                >我的投递</el-link
              >
            </el-dropdown-item>
            <el-dropdown-item>
              <el-link :underline="false" @click="routeTo('/favorites')"
                >我的收藏</el-link
              >
            </el-dropdown-item>
            <el-dropdown-item>
              <el-link :underline="false" @click="routeTo('/my-activities')"
                >我的活动</el-link
              >
            </el-dropdown-item>
            <el-dropdown-item>
              <el-link :underline="false" @click="routeTo('/mock-mine')"
                >我的面试</el-link
              >
            </el-dropdown-item>
            <el-dropdown-item>
              <el-link :underline="false" @click="routeTo('/modify-pwd')"
                >修改密码</el-link
              >
            </el-dropdown-item>
            <el-dropdown-item @click.native="handleLogout" class="text-danger">
              退出登录
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>
    <!-- <div class="message-text" v-else>暂无新消息</div> -->

    <!-- 按钮 -->
    <!-- <div class="message-text">
      <el-link type="primary" :underline="false" @click="routeTo('messages')"
        >查看全部</el-link
      >
    </div> -->

    <!-- 图标 -->
    <!-- <el-link
      :underline="false"
      class="nav-message m-2"
      slot="reference"
      @click="routeTo('messages')"
    >
      <el-badge v-if="messageList.length !== 0" is-dot>
        <i class="el-icon-message-solid"></i>
      </el-badge>
      <i v-else class="el-icon-message-solid"></i>
    </el-link> -->

    <!-- 用户头像 -->
    <!-- <el-dropdown>
      <el-avatar :size="35" icon="el-icon-user-solid" :src="avatar"></el-avatar>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item>
          <el-link :underline="false" @click="routeTo('edit-resume')"
            >我的简历</el-link
          >
        </el-dropdown-item>
        <el-dropdown-item>
          <el-link :underline="false" @click="routeTo('apply-jobs')"
            >我的投递</el-link
          >
        </el-dropdown-item>
        <el-dropdown-item>
          <el-link :underline="false" @click="routeTo('favorites')"
            >我的收藏</el-link
          >
        </el-dropdown-item>
        <el-dropdown-item>
          <el-link :underline="false" @click="routeTo('my-activities')"
            >我的活动</el-link
          >
        </el-dropdown-item>
        <el-dropdown-item>
          <el-link :underline="false" @click="routeTo('mock-mine')"
            >我的面试</el-link
          >
        </el-dropdown-item>
        <el-dropdown-item>
          <el-link :underline="false" @click="routeTo('modify-pwd')"
            >修改密码</el-link
          >
        </el-dropdown-item>
        <el-dropdown-item @click.native="handleLogout" class="text-danger">
          退出登录
        </el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown> -->
  </div>
</template>

<script>
import { mapGetters, mapMutations } from "vuex";
import { storage } from "@/utils/storage";
import { curRelativePath } from "@/utils/common";

export default {
  name: "MainNavBar",
  computed: {
    ...mapGetters(["token", "avatar", "messageCount"]),
  },
  data() {
    return {
      activeIndex: "", // 激活的菜单名称
      searchPlaceHolder: "", // 搜索栏的占位符
      messageList: [], // 消息列表
      keyword: undefined, // 搜索关键词
    };
  },
  watch: {
    activeIndex() {
      if (this.isJob()) {
        this.searchPlaceHolder = "搜索职位";
      } else if (this.isWiki()) {
        this.searchPlaceHolder = "搜索百科";
      } else if (this.isActivity()) {
        this.searchPlaceHolder = "搜索活动";
      } else if (this.isMock()) {
        this.searchPlaceHolder = "搜索面试官";
      }
    },
    $route() {
      if (this.isJob()) {
        this.activeIndex = "/job-list";
      } else if (this.isWiki()) {
        this.activeIndex = "/wiki-card";
      } else if (this.isActivity()) {
        this.activeIndex = "/activity-list";
      } else if (this.isMock()) {
        this.activeIndex = "/mock/interview";
      }
      this.keyword = this.$route.query.searchWord;
      this.getUnReadMessageCount();
    },
  },
  methods: {
    ...mapMutations({
      setMessageCount: "setting/MESSAGE_COUNT",
    }),

    // 获取提示词
    querySearch(queryString, cb) {
      if (queryString) {
        if (this.isJob()) {
          this.$axios
            .get("/job/search-job-name", {
              params: {
                keyWords: this.keyword,
                page: 1,
                size: 99,
              },
            })
            .then((data) => {
              cb(
                data.data.list.map((item) => {
                  return { value: item };
                })
              );
            });
        } else if (this.isWiki()) {
          this.$axios
            .get("/company/search-company-name", {
              params: {
                keyWords: this.keyword,
                page: 1,
                size: 99,
              },
            })
            .then((data) => {
              cb(
                data.data.list.map((item) => {
                  return { value: item };
                })
              );
            });
        } else if (this.isActivity()) {
          this.$axios
            .get("/activity/search-activity-title", {
              params: {
                keyWords: this.keyword,
                page: 1,
                size: 99,
              },
            })
            .then((data) => {
              cb(
                data.data.list.map((item) => {
                  return { value: item };
                })
              );
            });
        } else {
          cb([]);
        }
      } else {
        cb([]);
      }
    },

    // 执行搜索
    handleSearch() {
      let query = { ...this.$route.query };
      if (this.keyword) {
        query.keyword = this.keyword;
      } else {
        delete query.keyword;
      }
      if (this.isJob()) {
        if (this.$route.path === "/job-list") {
          this.$router.replace({ path: "/job-list", query });
        } else {
          this.$router.push({ path: "/job-list", query });
        }
      } else if (this.isWiki()) {
        if (this.$route.path === "/wiki-list") {
          this.$router.replace({ path: "/wiki-list", query });
        } else {
          this.$router.push({ path: "/wiki-list", query });
        }
      } else if (this.isActivity()) {
        if (this.$route.path === "/activity-list") {
          this.$router.replace({ path: "/activity-list", query });
        } else {
          this.$router.push({ path: "/activity-list", query });
        }
      } else {
      }
    },

    // 获取消息
    getMessageList() {
      if (this.token) {
        this.$axios
          .get("/message/list", {
            params: {
              page: 1,
              limit: 5,
            },
          })
          .then((response) => {
            this.messageList = response.data.list;
          });
      }
    },

    // 获取未读消息
    getUnReadMessageCount() {
      if (this.token) {
        this.$axios.get("/message/unread-count").then((response) => {
          this.setMessageCount(response.data);
        });
      }
    },

    // 退出
    handleLogout() {
      this.$store.dispatch("user/LOGOUT").then(() => {
        storage.removeLoginInfo();
        this.$router.push({ path: "/" });
      });
    },

    // 切换菜单时清空关键词
    handleSelect() {
      this.keyword = "";
    },

    // 当前路由是否激活职位菜单
    isJob() {
      return this.$route.path === "/job-list";
    },

    // 当前路由是否激活百科菜单
    isWiki() {
      return (
        this.$route.path === "/wiki-card" ||
        this.$route.path === "/wiki-list" ||
        this.$route.path.indexOf("/company") !== -1
      );
    },

    // 当前路由是否激活活动菜单
    isActivity() {
      return this.$route.path === "/activity-list";
    },

    // 当前路由是否激活模拟面试菜单
    isMock() {
      return this.$route.path === "/mock/interview";
    },

    // 路由跳转
    routeTo(path) {
      if (path === "/login") {
        this.$router.push({ path, query: { redirect: curRelativePath() } });
      } else {
        this.$router.push(path);
      }
    },
  },
};
</script>
<style>
.el-link--inner {
  white-space: nowrap;
}
</style>
<style scoped lang="scss">
.message-text {
  text-align: center;
  padding: 16px 10px 6px;
}

.nav-container {
  padding-top: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-wrap: wrap;

  .nav-left-container {
    display: flex;
    align-items: center;
    flex: 1;

    .logo {
      font-size: 30px;
    }

    .logo-image {
      display: block;
      width: 289px;
      min-width: 289px;
      height: 78px;
      background: url("../assets/logo.png");
    }

    .menu-container {
      display: flex;
      align-items: center;
      justify-content: space-around;
      flex: 1;
      margin: 0 30px;

      .nav-item {
        font-size: 18px;
      }
    }
  }

  .nav-right-container {
    display: flex;
    align-items: center;
    flex: 1;

    .input-search {
      flex: 1;
      min-width: 140px;
      margin-right: 10px;
    }

    .user-container {
      display: flex;
      align-items: center;

      .chat {
        width: 30px;
        height: 30px;
        margin-left: 10px;
        cursor: pointer;
      }

      .join {
        padding: 0 14px;
      }

      .nav-message {
        font-size: 22px;
        padding: 0 16px;
      }

      ::v-deep .message-item {
        padding: 10px 5px;
        border-bottom: 1px solid #eee;
        line-height: 1.5em;
        font-size: 14px;
        color: #888;
      }
    }
  }
}

@media screen and (max-width: 850px) {
  .nav-container {
    flex-direction: column-reverse;
    .nav-left-container {
      .logo-image {
        display: none;
      }
    }
  }
}

@media screen and (max-width: 410px) {
  .nav-container {
    flex-direction: column-reverse;

    .nav-left-container {
      .logo {
        font-size: 22px;
      }

      .menu-container {
        .nav-item {
          font-size: 16px;
        }
      }
    }

    .nav-right-container {
      padding: 0 15px;
    }
  }
}
</style>
