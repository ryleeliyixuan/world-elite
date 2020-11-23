<template>
  <div class="app-container container">
    <!-- 百科头页 -->
    <div v-if="company" class="company-wiki-header-container">
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
            <el-avatar
              class="company-info-logo"
              :src="company.logo"
              :alt="company.fullName"
            ></el-avatar>
            <h5 class="mt-2" v-if="company.name">{{ company.name }}</h5>
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
              <el-col>
                <el-tag type="info" effect="dark"
                  >已进驻： <span>{{ company.listed || "0" }}</span></el-tag
                >
              </el-col>
              <el-col :span="12">
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
        <el-menu-item @click="linkToActivity" index="activity"
          >活动</el-menu-item
        >
        <el-menu-item index="community">圈子</el-menu-item>
      </el-menu>
      <router-view> </router-view>
    </div>
  </div>
</template>

<script>
import Vue from "vue";
import VueAMap from "vue-amap";
import Pagination from "@/components/Pagination";
import { formatListQuery, parseListQuery } from "@/utils/common";

import WikiActivityPage from "./WikiActivityPage";
import WikiIntroPage from "./WikiIntroPage";
import WikiCommunityPage from "./WikiCommunityPage";

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
    WikiActivityPage,
    WikiIntroPage,
    WikiCommunityPage,
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
    linkToActivity() {
      this.$router.push({
        name: "wiki-activity",
        params: {
          id: this.companyId,
          name: this.company.name,
        },
      });
    },
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
        path: urlRootPath + tabIndex,
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
      const urlRootPath = `/company/${this.companyId}/community/score/#save`;
      this.$router.push({
        path: urlRootPath,
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.noInfoMsgBox {
  line-height: 80px;
  text-align: center;
  padding-bottom: 20px;
  width: 100%;
}

.app-container {
  margin: 0 auto;
  min-height: calc(100vh - 477px);

  .company-wiki-header-container {
    // display: flex;
    // align-items: center;
    // justify-content: space-between;
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
        max-height: 335px;
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
        border: 2px solid white;
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
    padding: 0 20px 20px 20px;
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
