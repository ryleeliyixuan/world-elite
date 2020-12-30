<template>
  <div class="background">
    <div class="app-container container">
      <!-- 百科头页 -->
      <div v-if="company" class="company-wiki-header-container mt-4">
        <div class="company-info-container">
          <div class="company-info">
            <div class="company-info-banner-wrapper">
              <img
                class="company-info-banner"
                v-if="banner && banner.length > 0"
                :src="banner"
                :alt="company.fullName"
              />
              <img
                v-else
                class="company-info-banner"
                src="@/assets/defaultbanner.png"
                alt=""
              />
            </div>
            <div class="company-info-description">
              <img
                class="company-info-logo"
                :src="company.logo"
                :alt="company.fullName"
                fit="fill"
              />
              <h5 class="mb-2" v-if="company.name">{{ company.name }}</h5>
              <h5 class="mt-2" v-else>{{ company.fullName }}</h5>
              <p class="mb-2">
                <span v-if="company.stage">{{ company.stage.name }} |</span>
                <span v-if="company.property">
                  {{ company.property.name }} |</span
                >
                <span v-if="company.industry">
                  {{ company.industry.name }} |</span
                >
                <span v-if="company.scale"> {{ company.scale.name }}</span>
              </p>
              <div class="company-info-rating mb-2 d-flex">
                <el-tooltip
                  class="item"
                  effect="dark"
                  content="星级呈现该企业在本网站的综评分"
                  placement="left"
                >
                  <el-rate
                    v-model="score"
                    disabled
                    show-score
                    text-color="#ff9900"
                    score-template="{value}"
                  >
                  </el-rate>
                </el-tooltip>
                <el-button
                  v-if="tabIndex != 'community'"
                  type="text"
                  @click="goToSaveScore"
                  size="mini"
                  style="padding: 0; margin-left: 10px"
                  >评分</el-button
                >
              </div>
              <el-button
                type="primary"
                size="small"
                :loading="favoriteLoading"
                @click="handleFavorite"
                round
                style="width: 120px"
              >
                {{ company.favoriteFlag === 1 ? "已订阅百科" : "订阅百科" }}
              </el-button>
            </div>
            <div class="company-info-stat">
              <el-row
                type="flex"
                class="row-bg"
                justify="space-between"
                size="mini"
              >
                <!-- 缺接口 -->
                <!-- <el-col v-if="company.listed && company.listed.length >= 50">
                <el-tag type="info" effect="dark"
                  >已进驻： <span>{{ company.listed || "0" }}</span></el-tag
                >
              </el-col> -->
                <el-col :span="12" v-if="favoriteCount && favoriteCount >= 50">
                  <el-tag type="info" effect="dark"
                    >已订阅：
                    <span>{{ favoriteCount || "0" }}</span>
                  </el-tag>
                </el-col>
              </el-row>
            </div>
          </div>
        </div>
      </div>
      <!-- 百科内容 -->
      <div v-if="company" class="company-wiki-content-container">
        <el-menu
          class="mt-2 mb-4"
          :default-active="tabIndex"
          mode="horizontal"
          @select="handleSelectTab"
        >
          <el-menu-item index="intro">首页</el-menu-item>
          <el-menu-item index="job">岗位</el-menu-item>
          <el-menu-item :index="'activity/'+company.name" index="activity"
            >活动</el-menu-item
          >
          <el-menu-item index="post">圈子</el-menu-item>
        </el-menu>
        <router-view> </router-view>
      </div>
    </div>
  </div>
</template>

<script>
import Vue from "vue";
import VueAMap from "vue-amap";
import Pagination from "@/components/Pagination";
import { formatListQuery, parseListQuery } from "@/utils/common";
import { getCompanyInfo, getCompanyWiki } from "@/api/company_api";
import { setPageTitle } from "@/utils/setting";
import { doFavorite } from "@/api/favorite_api";
import { mapGetters } from "vuex";
import Toast from "@/utils/toast";

Vue.use(VueAMap);

VueAMap.initAMapApiLoader({
  key: process.env.VUE_APP_AMAP_KEY,
  v: "1.4.4",
});

export default {
  name: "CompanyWikiMainPage",
  components: {
    Pagination,
  },
  data() {
    return {
      tabIndex: "intro",
      companyId: undefined,
      company: undefined,
      score: undefined,
      favoriteCount: undefined,
      banner: "",
      //subscribe
      favoriteForm: {
        objectId: undefined,
        type: 2,
        favorite: false,
      },
      favoriteLoading: false,
    };
  },
  created() {
    this.$emit("complete");
    this.initData();
    this.initTabActive();
  },
  computed: {
    companyLink() {
      return this.company.homepage.startsWith("http")
        ? this.company.homepage
        : "http://" + this.company.homepage;
    },
    ...mapGetters(["token", "userId"]),
    noMore() {
      return this.count >= 20;
    },
    disabled() {
      return this.loading || this.noMore;
    },
  },
  watch: {
    $route() {
      this.initData();
    },
  },
  methods: {
    // 重新进入默认选择项
    initTabActive() {
      const name = this.$route.name;
      const lastIndex = name.lastIndexOf("-");
      const getName =
        lastIndex !== -1 ? name.substring(lastIndex + 1) : this.tabIndex;
      if (getName) {
        this.tabIndex = getName;
      }
    },
    // linkToActivity() {
    //   this.$router.push({
    //     name: "wiki-activity",
    //     params: {
    //       id: this.companyId,
    //       name: this.company.name,
    //     },
    //   });
    // },
    initData() {
      let id = this.$route.params.id;
      this.companyId = id;
      this.favoriteForm.objectId = id;
      getCompanyInfo(id).then((response) => {
        this.company = response.data;
        setPageTitle(this.company.name);
      });
      this.getCompanyWiki(id);
    },
    getCompanyWiki(id) {
      getCompanyWiki(id).then((response) => {
        this.score = response.data.score;
        this.favoriteCount = response.data.favoriteCount;
        this.banner = response.data.banner;
      });
    },
    handleSelectTab(tabIndex) {
        const urlRootPath = `/company/${this.companyId}/`;
      this.$router.push({
        path: urlRootPath + (tabIndex==='activity'?`${tabIndex}/${this.company.name}`:tabIndex),
      });
    },
    handleFavorite() {
      this.favoriteLoading = true;
      this.favoriteForm.favorite = !this.favoriteForm.favorite;
      doFavorite(this.favoriteForm)
        .then(() => {
          this.company.favoriteFlag = this.favoriteForm.favorite ? 1 : 0;
          if (this.company.favoriteFlag == 1) {
            this.$message("订阅成功");
          } else {
            this.$message("取消订阅");
          }
        })
        .finally(() => {
          this.favoriteLoading = false;
        });
    },
    onLoginClick() {
      this.$router.push("/login");
    },
    goToSaveScore() {
      let element=document.getElementById("save");
      this.$router.push(`/company/${this.companyId}/score`);
      element.scrollIntoView();
    },
  },
};
</script>

<style scoped lang="scss">
/deep/ .el-menu {
  background-color: #f6f8fb;
}
/deep/ .el-menu--horizontal > .el-menu-item:not(.is-disabled):focus,
.el-menu--horizontal > .el-menu-item:not(.is-disabled):hover,
.el-menu--horizontal > .el-submenu .el-submenu__title:hover {
  background-color: #f6f8fb;
}

/deep/ .el-menu--horizontal > .el-menu-item.is-active {
  color: #4895ef;
  font-weight: 500;
}

/deep/ .el-menu--horizontal > .el-menu-item {
  color: #333333;
  font-weight: 500;
}

/deep/ .el-menu.el-menu--horizontal {
  border-bottom: 0px;
}

.noInfoMsgBox {
  line-height: 80px;
  text-align: center;
  padding-bottom: 20px;
  width: 100%;
}
.background {
  width: 100vw;
  background-color: #f6f8fb;
  padding-top: 20px;
}

.app-container {
  margin: 0 auto;
  width: 100%;
  background-color: #f6f8fb;

  .company-wiki-header-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
  }

  .company-info-container {
    height: calc(91px + 100vw * 0.39);
    width: 100%;

    .company-info {
      position: relative;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;

      .company-info-banner-wrapper {
        max-height: 377px;
        width: 100%;

        .company-info-banner {
          max-height: 335px;
          width: 100%;
          z-index: -1;
          object-fit: fill;
        }
      }

      .company-info-stat {
        width: 230px;
        height: 50px;
        position: absolute;
        right: 7%;
        top: 77%;
      }

      .company-info-logo {
        width: 100px;
        height: 100px;
        border-radius: 50%;
        // border: 2px solid white;
        box-shadow: 0px 4px 12px 0px rgba(0, 0, 0, 0.35);
        margin-bottom: 20px;
      }

      .company-info-description {
        position: absolute;
        top: calc(100% - 50px);
        display: flex;
        flex-direction: column;
        align-items: center;
      }

      .company-rating-dialog-content {
        margin-bottom: 12px;
      }
    }
  }

  .company-wiki-content-container {
    width: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;

    .community-box {
      width: 100%;
    }
  }
}

@media screen and (min-width: 1000px) {
  .app-container {
    .company-info-container {
      height: 525px;
    }
  }
}

@media screen and (max-width: 768px) {
  .app-container {
    .company-info-container {
      height: 350px;

      .company-info {
        max-height: 200px;

        .company-info-banner-wrapper {
          max-height: 200px;
          .company-info-banner {
            max-height: 200px;
          }
        }

        .company-info-stat {
          width: 230px;
          height: 25px;
          position: absolute;
          width: 100%;
          left: 7%;
          top: 50%;
        }
      }
    }
  }
}

@media screen and (max-width: 415px) {
  .app-container {
    .company-info-container {
      height: 300px;
      .company-info {
        max-height: 140px;
        .company-info-banner-wrapper {
          max-height: 140px;

          .company-info-banner {
            max-height: 140px;
          }
        }
      }
    }
  }
}
</style>
