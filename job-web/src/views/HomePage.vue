<template>
  <div class="app-container">
    <!-- 导航栏 starts -->
    <div class="tool-bar">
      <el-popover placement="left" width="200" trigger="hover">
        <div class="text-center">扫码关注我们的公众号</div>
        <el-image
          class="qr-wechat-img"
          src="https://oss.myworldelite.com/static/worldelite-gongzhonghao.jpg"
        ></el-image>
        <svg-icon
          slot="reference"
          icon-class="home-qrcode"
          style="height: 17px; width: 17px"
        />
      </el-popover>
      <el-tooltip
        class="tool-bar-tooltip"
        effect="dark"
        content="收藏订阅"
        placement="left"
      >
        <svg-icon
          @click="goToFavorite"
          icon-class="home-sub"
          style="height: 19px; width: 18px"
        />
      </el-tooltip>
      <el-tooltip
        class="tool-bar-tooltip"
        effect="dark"
        content="职位"
        placement="left"
      >
        <svg-icon
          @click="goToJob"
          icon-class="home-job"
          style="height: 19px; width: 19px"
        />
      </el-tooltip>
      <el-tooltip
        class="tool-bar-tooltip"
        effect="dark"
        content="百科"
        placement="left"
      >
        <svg-icon
          @click="goToWiki"
          icon-class="home-wiki"
          style="height: 16px; width: 18px"
        />
      </el-tooltip>
      <el-tooltip
        class="tool-bar-tooltip"
        effect="dark"
        content="活动"
        placement="left"
      >
        <svg-icon
          @click="goToActivity"
          icon-class="home-activity"
          style="height: 19px; width: 19px"
        />
      </el-tooltip>
      <el-tooltip
        class="tool-bar-tooltip"
        effect="dark"
        content="即时通讯"
        placement="left"
        ><svg-icon
          @click="goToMessage"
          icon-class="home-message"
          style="height: 19px; width: 19px"
      /></el-tooltip>
      <el-tooltip
        class="tool-bar-tooltip"
        effect="dark"
        content="回到顶部"
        placement="left"
      >
        <svg-icon
          @click="goToTop"
          icon-class="home-backtotop"
          style="height: 19px; width: 19px"
        />
      </el-tooltip>
    </div>
    <!-- 导航栏 ends -->
    <!-- 轮播图 starts -->
    <div style="display: flex">
      <div class="section1-container">
        <el-carousel :interval="2500" arrow="always">
          <el-carousel-item v-for="item in carouselList" :key="item.id">
            <el-image
              class="section1-image"
              :src="item.url"
              :alt="item.name"
              v-on:click="select(item)"
              fit="cover"
            ></el-image>
          </el-carousel-item>
        </el-carousel>
      </div>
      <div>
        <div class="left-company-wiki" @click="moreJob">
          <svg-icon
            icon-class="company-wiki"
            class="company-wiki-img"
          ></svg-icon>
          <!-- <el-image
            class="company-wiki-img"
            :src="recommendConfig.topPicUrl"
          ></el-image> -->
        </div>
        <div class="left-activity" @click="moreActivity">
          <svg-icon icon-class="activity" class="activity-img"></svg-icon>
          <!-- <el-image
            class="activity-img"
            :src="recommendConfig.bottomPicUrl"
          ></el-image> -->
        </div>
      </div>
    </div>
    <!-- 轮播图 ends -->
    <!-- 关于我们 starts -->
    <div class="about-us">
      <svg-icon
        icon-class="about-us"
        class="about-us-img"
        style="height: 334px; width: 100%"
      ></svg-icon>
    </div>
    <!-- 关于我们 ends -->
    <!-- 职位精选 starts -->
    <div class="job-recommend-box">
      <h3 class="header text-center">职位精选</h3>
      <div class="subheader text-center">HOT JOBS</div>
      <div class="title" v-if="recommendJobList && recommendJobList.length > 0">
        <span>“WE内推”全职岗位推荐</span>
      </div>
      <div
        class="recommend-job-container"
        v-if="recommendJobList && recommendJobList.length > 0"
      >
        <div
          class="job-card"
          v-for="recommendJob in recommendJobList.slice(0, 3)"
          :key="recommendJob.id"
        >
          <el-link :href="`/job/${recommendJob.object.id}`" :underline="false">
            <div class="job-section1">
              <span class="job-name">{{ recommendJob.object.name }}</span>
              <span class="job-salary">{{
                recommendJob.object.salary.name
              }}</span>
              <el-button
                circle
                class="flag"
                :style="
                  recommendJob.object.favoriteFlag === 1
                    ? `background: #ff3d00`
                    : `background: #FFFFFF;`
                "
                @click.native.prevent="
                  handleJobFavorite(
                    recommendJob.object.id,
                    recommendJob.object.favoriteFlag
                  )
                "
                @mouseenter.native.prevent="MouseInJobFav(recommendJob)"
                @mouseleave.native.prevent="MouseOutJobFav(recommendJob)"
                ><svg-icon
                  :icon-class="
                    recommendJob.object.favoriteFlag === 1
                      ? 'jobflag'
                      : 'jobunflag'
                  "
                  style="height: 10px; width: 11px"
              /></el-button>
            </div>
            <div class="job-city">
              <span class="job-city-text">
                {{
                  recommendJob.object.city ? recommendJob.object.city.name : ""
                }}
              </span>
              <span class="job-minDegree">
                {{
                  recommendJob.object.minDegree
                    ? recommendJob.object.minDegree.name
                    : ""
                }}
              </span>
              <span>
                {{
                  recommendJob.object.experience
                    ? recommendJob.object.experience.name
                    : ""
                }}
              </span>
            </div>
          </el-link>
          <el-divider></el-divider>
          <el-link
            :href="`/company/${recommendJob.object.companyUser.company.id}`"
            :underline="false"
            v-if="
              recommendJob.object.companyUser &&
              recommendJob.object.companyUser.company
            "
          >
            <div class="job-company-container">
              <div class="company-info">
                <div class="company-name">
                  {{ recommendJob.object.companyUser.company.name }}
                </div>
                <div style="display: flex">
                  <div class="text-gray text-small company-tag">
                    {{
                      recommendJob.object.companyUser.company.property
                        ? recommendJob.object.companyUser.company.property.name
                        : ""
                    }}
                    |
                    {{
                      recommendJob.object.companyUser.company.industry
                        ? recommendJob.object.companyUser.company.industry.name
                        : ""
                    }}
                    |
                    {{
                      recommendJob.object.companyUser.company.scale
                        ? recommendJob.object.companyUser.company.scale.name
                        : ""
                    }}
                  </div>
                </div>
              </div>
            </div>
          </el-link>
        </div>
      </div>
      <div class="title" v-if="recommendJobList && recommendJobList.length > 3">
        <span>“WE内推”每日实习岗位推荐</span>
      </div>
      <div
        class="recommend-job-container"
        v-if="recommendJobList && recommendJobList.length > 3"
      >
        <div
          class="job-card"
          v-for="recommendJob in recommendJobList.slice(3, 6)"
          :key="recommendJob.id"
        >
          <el-link :href="`/job/${recommendJob.object.id}`" :underline="false">
            <div class="job-section1">
              <span class="job-name">{{ recommendJob.object.name }}</span>
              <span class="job-salary">{{
                recommendJob.object.salary.name
              }}</span>
              <el-button
                circle
                class="flag"
                :style="
                  recommendJob.object.favoriteFlag === 1
                    ? `background: #ff3d00`
                    : `background: #FFFFFF;`
                "
                @click.native.prevent="
                  handleJobFavorite(
                    recommendJob.object.id,
                    recommendJob.object.favoriteFlag
                  )
                "
                @mouseenter.native.prevent="MouseInJobFav(recommendJob)"
                @mouseleave.native.prevent="MouseOutJobFav(recommendJob)"
                ><svg-icon
                  :icon-class="
                    recommendJob.object.favoriteFlag === 1
                      ? 'jobflag'
                      : 'jobunflag'
                  "
                  style="height: 10px; width: 11px"
              /></el-button>
            </div>
            <div class="job-city">
              <span class="job-city-text">
                {{
                  recommendJob.object.city ? recommendJob.object.city.name : ""
                }}
              </span>
              <span class="job-minDegree">
                {{
                  recommendJob.object.minDegree
                    ? recommendJob.object.minDegree.name
                    : ""
                }}
              </span>
              <span>
                {{
                  recommendJob.object.experience
                    ? recommendJob.object.experience.name
                    : ""
                }}
              </span>
            </div>
          </el-link>
          <el-divider></el-divider>
          <el-link
            :href="`/company/${recommendJob.object.companyUser.company.id}`"
            :underline="false"
            v-if="
              recommendJob.object.companyUser &&
              recommendJob.object.companyUser.company
            "
          >
            <div class="job-company-container">
              <div class="company-info">
                <div class="company-name">
                  {{ recommendJob.object.companyUser.company.name }}
                </div>
                <div class="text-gray text-small company-tag">
                  {{
                    recommendJob.object.companyUser.company.property
                      ? recommendJob.object.companyUser.company.property.name
                      : ""
                  }}
                  |
                  {{
                    recommendJob.object.companyUser.company.industry
                      ? recommendJob.object.companyUser.company.industry.name
                      : ""
                  }}
                  |
                  {{
                    recommendJob.object.companyUser.company.scale
                      ? recommendJob.object.companyUser.company.scale.name
                      : ""
                  }}
                </div>
              </div>
            </div>
          </el-link>
        </div>
      </div>
      <div class="text-center" style="margin-top: 30px">
        <el-link class="more" type="info" @click="moreJob"
          >查看更多内推职位</el-link
        >
      </div>
    </div>
    <!-- 职位精选 ends -->
    <!-- 企业百科 starts -->
    <div class="company-recommend-box">
      <div class="header text-center">企业百科</div>
      <div class="subheader text-center">CORPORATE INFO</div>
      <div
        class="title"
        v-if="recommendCompanyList && recommendCompanyList.length > 0"
      >
        <span>大家都在看的企业</span>
      </div>
      <div
        class="company-container"
        v-if="recommendCompanyList && recommendCompanyList.length > 0"
      >
        <div
          class="company-card"
          v-for="company in recommendCompanyList"
          :key="company.id"
        >
          <el-link :href="`/company/${company.object.id}`" :underline="false">
            <div class="company-section1">
              <div class="company-name">{{ company.object.name }}</div>
              <!-- <img class="company-logo" :src="company.object.logo" /> -->
              <el-button
                circle
                class="flag"
                :style="
                  company.object.favoriteFlag === 1
                    ? `background: #ff3d00`
                    : `background: #FFFFFF;`
                "
                @click.native.prevent="
                  handleFavorite(company.object.id, company.object.favoriteFlag)
                "
                @mouseenter.native.prevent="MouseInFav(company)"
                @mouseleave.native.prevent="MouseOutFav(company)"
                ><svg-icon
                  :icon-class="
                    company.object.favoriteFlag === 1 ? 'jobflag' : 'jobunflag'
                  "
                  style="height: 10px; width: 11px"
              /></el-button>
            </div>
            <div class="company-section2">
              <div class="text">
                <svg-icon
                  icon-class="company-type"
                  style="height: 18px; width: 14px; margin-right: 2px"
                />
                {{ company.object.stage.name }} |
                {{
                  company.object.property.name === "民营企业"
                    ? "民营"
                    : company.object.property.name
                }}
              </div>
              <div class="text">
                <svg-icon
                  icon-class="company-industry"
                  style="height: 18px; width: 14px; margin-right: 2px"
                />
                {{ company.object.industry.name }}
              </div>
              <div class="text">
                <svg-icon
                  icon-class="company-scale"
                  style="height: 18px; width: 14px; margin-right: 2px"
                />
                {{ company.object.scale.name }}
              </div>
            </div>
            <div class="company-section3">
              {{ company.object.wikiSummary }}
            </div>
          </el-link>
        </div>
      </div>
      <div class="title" v-if="recentJobList && recentJobList.length > 0">
        <span>这些企业最近上新了职位</span>
      </div>
      <div
        class="company-container"
        v-if="recentJobList && recentJobList.length > 0"
      >
        <div class="company-card" v-for="item in recentJobList" :key="item.id">
          <el-link :href="`/company/${item.id}`" :underline="false">
            <div class="company-section1">
              <div class="company-name">{{ item.name }}</div>
              <!-- <img class="company-logo" :src="company.object.logo" /> -->
              <el-button
                circle
                class="flag"
                :style="
                  item.favoriteFlag === 1
                    ? `background: #ff3d00`
                    : `background: #FFFFFF;`
                "
                @click.native.prevent="
                  handleFavorite(item.id, item.favoriteFlag)
                "
                @mouseenter.native.prevent="MouseInRecentFav(item)"
                @mouseleave.native.prevent="MouseOutRecentFav(item)"
                ><svg-icon
                  :icon-class="
                    item.favoriteFlag === 1 ? 'jobflag' : 'jobunflag'
                  "
                  style="height: 10px; width: 11px"
              /></el-button>
            </div>
            <div class="company-section2">
              <div class="text">
                <svg-icon
                  icon-class="company-type"
                  style="height: 18px; width: 14px; margin-right: 2px"
                />
                {{ item.stage.name }} |
                {{
                  item.property.name === "民营企业"
                    ? "民营"
                    : item.property.name
                }}
              </div>
              <div class="text">
                <svg-icon
                  icon-class="company-industry"
                  style="height: 18px; width: 14px; margin-right: 2px"
                />
                {{ item.industry.name }}
              </div>
              <div class="text">
                <svg-icon
                  icon-class="company-scale"
                  style="height: 18px; width: 14px; margin-right: 2px"
                />
                {{ item.scale.name }}
              </div>
            </div>
            <div class="company-section3">
              {{ item.wikiSummary }}
            </div>
          </el-link>
        </div>
      </div>
      <div class="text-center" style="margin-top: 30px">
        <el-link class="more" type="info" @click="moreCompanyWiki"
          >查看更多企业</el-link
        >
      </div>
    </div>
    <!-- 企业百科 ends -->
    <!-- 职场活动 starts -->
    <div class="activity-box" v-if="activitylist && activitylist.length > 0">
      <div class="header text-center">职场活动</div>
      <div class="subheader text-center">WORKPLACE EVENTS</div>
      <div class="title"><span>热门招聘活动</span></div>
      <div class="activity-container">
        <div
          class="activity-card"
          v-for="item in activitylist.slice(0, 3)"
          :key="item.id"
          @click="onActivityDetail(item)"
        >
          <el-image
            style="width: 340px; height: 191px"
            class="thumbnail"
            :src="item.poster"
            fit="fit"
          ></el-image>
          <div class="brief">
            <div class="line1">
              <div class="name">{{ item.title }}</div>
              <div
                v-if="item.city"
                :class="[
                  'city',
                  {
                    online: item.city.id === 999992 || item.city.id === 999993,
                  },
                ]"
              >
                {{ item.city.name }}
              </div>
            </div>
            <div class="line2">
              <div class="time">
                {{ item.activityStartTime | timestampToDateHourMinute }}-{{
                  item.activityFinishTime | timestampToHoursMinutes
                }}
              </div>
              <div class="count">{{ item.follower }}人正在关注</div>
            </div>
          </div>
        </div>
      </div>
      <div class="text-center">
        <el-link class="more" type="info" @click="moreActivity"
          >查看更多活动</el-link
        >
      </div>
    </div>
    <!-- 职场活动 ends -->
  </div>
</template>

<script>
import { getRecommendList } from "@/api/recommend_api";
import { getCarouselList, getRcmdConfig } from "@/api/config_api";
import { doFavorite } from "@/api/favorite_api";
import { mapGetters } from "vuex";

export default {
  name: "HomePage",
  computed: {
    filteredActivityList: function () {
      return this.activitylist.slice(0, 2);
    },
  },
  created() {
    this.initData();
  },
  computed: {
    ...mapGetters(["token"]),
  },
  data() {
    return {
      recommendCompanyList: [],
      recommendJobList: [],
      recentJobList: [],
      activitylist: [],
      filteredActivityList: [],
      homeConfig: {},
      carouselList: [],
      recommendConfig: {},
    };
  },
  methods: {
    initData() {
      //获取轮播图接口
      getCarouselList().then((response) => {
        this.carouselList = response.data.list;
        this.$emit("complete");
      });
      //获取推荐板块接口
      getRcmdConfig().then((response) => {
        this.recommendConfig = response.data;
        this.$emit("complete");
      });

      getRecommendList({
        objectType: 2, // 公司
        page: 1,
        limit: 3,
        sort: "+position",
      }).then((response) => {
        this.recommendCompanyList = response.data.list;
        this.$emit("complete");
      });
      getRecommendList({
        objectType: 1, // 职位
        page: 1,
        limit: 6,
        sort: "+position",
      }).then((response) => {
        this.recommendJobList = response.data.list;
      });
      this.$axios
        .get("/activity/list", { params: { sortField: "follower" } })
        .then((response) => {
          this.activitylist = response.data.list;
        });
      this.$axios
        .get("/recommend/recent-job-company", { params: { limit: 3 } })
        .then((response) => {
          this.recentJobList = response.data.list;
        });
    },
    select: function (banner) {
      window.open(banner.url);
    },
    //mouse in fav
    MouseInFav(company) {
      company.object.favoriteFlag === 1
        ? (company.object.favoriteFlag = 0)
        : (company.object.favoriteFlag = 1);
      console.log("-----", company.object.favoriteFlag);
    },
    //mouse out fav
    MouseOutFav() {
      this.initData();
    },
    MouseInRecentFav(company) {
      // console.log("-----");
      company.favoriteFlag === 1
        ? (company.favoriteFlag = 0)
        : (company.favoriteFlag = 1);
    },
    MouseOutRecentFav() {
      // console.log("******");
      this.initData();
    },
    //mouse in fav
    MouseInJobFav(recommendJob) {
      recommendJob.object.favoriteFlag === 1
        ? (recommendJob.object.favoriteFlag = 0)
        : (recommendJob.object.favoriteFlag = 1);
    },
    //mouse out fav
    MouseOutJobFav() {
      this.initData();
    },
    //mouse out fav
    handleJobFavorite(id, favorite) {
      let data = {
        favorite: Boolean(favorite),
        objectId: id,
        type: 1,
      };
      doFavorite(data).then(() => {
        this.initData();
        this.$message("操作成功");
      });
    },
    handleFavorite(id, favorite) {
      let data = {
        favorite: Boolean(favorite),
        objectId: id,
        type: 2,
      };
      doFavorite(data).then(() => {
        this.initData();
        this.$message("操作成功");
      });
    },
    moreCompanyWiki() {
      this.$router.push("/wiki-card");
    },
    moreJob() {
      this.$router.push("/job-list");
    },
    moreActivity() {
      this.$router.push("/activity-list");
    },
    // 点击活动，查看活动详情
    onActivityDetail(activity) {
      this.$router.push(`/activity/${activity.id}`);
    },
    onCompanyDetail(company) {
      this.$router.push(`/company/${company.object.id}`);
    },
    onRecentCompanyDetail(company) {
      this.$router.push(`/company/${company.id}`);
    },
    goToTop() {
      let top = document.documentElement.scrollTop || document.body.scrollTop;
      // 实现滚动效果
      const timeTop = setInterval(() => {
        document.body.scrollTop = document.documentElement.scrollTop = top -= 50;
        if (top <= 0) {
          clearInterval(timeTop);
        }
      }, 10);
    },
    goToActivity() {
      this.$router.push(`/activity-list`);
    },
    goToWiki() {
      this.$router.push(`/wiki-card`);
    },
    goToMessage() {
      this.$router.push(`/chat`);
    },
    goToFavorite() {
      this.$router.push(`/favorites`);
    },
    goToJob() {
      this.$router.push(`/job-list`);
    },
  },
};
</script>

<style scoped lang="scss">
.app-container {
  min-width: 375px;
  position: relative;

  .header {
    font-size: 21px;
    font-family: PingFangSC-Semibold, PingFang SC;
    font-weight: 600;
    color: #4895ef;
    line-height: 29px;
  }

  .subheader {
    font-size: 12px;
    font-family: PingFangSC-Semibold, PingFang SC;
    font-weight: 600;
    color: #a9e8f1;
    line-height: 17px;
  }

  .title {
    margin-bottom: 18px;
    margin-top: 31px;
    font-size: 18px;
    font-family: PingFangSC-Semibold, PingFang SC;
    font-weight: 600;
    color: #333333;
    line-height: 25px;

    span {
      background: linear-gradient(90deg, #4895ef 0%, #2c5fb8 100%);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
    }
  }

  .more {
    font-size: 14px;
    font-family: PingFangSC-Regular, PingFang SC;
    font-weight: 400;
    color: #999999;
    line-height: 20px;
    text-decoration: underline;
  }

  .section1-container {
    max-width: 1200px;
    max-height: 600px;
    height: 424px;
    width: calc(100vw - 20px);
    margin: 0 auto 30px;
    .section1-image {
      width: 779px;
      height: 424px;
    }
    /deep/.el-carousel__indicators--horizontal {
      bottom: 0;
      left: 50%;
      transform: translateX(-50%);
    }

    .el-carousel {
      height: 100%;
      width: 774px;

      /deep/ .el-carousel__container {
        height: 100%;
      }
    }
  }
  .left-company-wiki {
    cursor: pointer;
    width: 405px;
    height: 202px;
    background: #ffffff;
    .company-wiki-img {
      width: 405px;
      height: 202px;
    }
  }
  .left-activity {
    cursor: pointer;
    width: 405px;
    height: 202px;
    margin-top: 20px;
    background: #ffffff;
    .activity-img {
      width: 405px;
      height: 202px;
    }
  }

  .tool-bar {
    position: fixed;
    z-index: 100;
    right: 20px;
    top: 30vh;
    width: 47px;
    height: 272px;
    background: #ffffff;
    box-shadow: 0px 2px 4px 0px #d9dfe8;
    border-radius: 42px;
    display: flex;
    flex-direction: column;
    justify-content: space-around;
    align-items: center;
    padding-top: 10px;
    padding-bottom: 10px;

    .tooltip {
      cursor: pointer;
    }
  }
  .about-us {
    width: auto;
    height: auto;
    margin-top: -20px;
    margin-bottom: 10px;
    // .about-us-img {
    //   box-shadow: 0px 4px 16px 3px rgba(191, 199, 215, 0.31);
    // }
  }

  .company-recommend-box {
    padding: 25px 55px 47px 55px;
    background: #ffffff;
    box-shadow: 0px 4px 16px 3px rgba(191, 199, 215, 0.31);
    margin-bottom: 31px;
    .company-container {
      display: flex;
      .company-card {
        padding: 12px 13px 23px 16px;
        width: 340px;
        height: 145px;
        background: linear-gradient(135deg, #f9fbfd 0%, #ddecfd 100%);
        cursor: pointer;

        /deep/.el-button {
          box-shadow: 0px 5px 13px 0px #c0cde3;
          border: 0px;
        }

        .company-section1 {
          display: flex;
          justify-content: space-between;
          margin-bottom: 18px;

          .company-name {
            font-size: 18px;
            font-family: PingFangSC-Medium, PingFang SC;
            font-weight: 500;
            color: #333333;
            line-height: 25px;
          }

          .company-logo {
            width: 92px;
            height: 34px;
            background-size: contain;
            margin-right: 24px;
          }

          /deep/.el-button {
            height: 21px;
            width: 21px;
            display: flex;
            justify-content: center;
            align-items: center;

            span {
              display: flex;
            }
          }

          /deep/.el-button:hover {
            background: #ff3d00;
          }
        }

        .company-section2 {
          display: flex;
          margin-bottom: 13px;
          justify-content: space-between;
          align-items: center;

          .text {
            font-size: 14px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: #546e7a;
            line-height: 20px;
            display: flex;
            align-items: center;
          }
        }

        .company-section3 {
          display: -webkit-box;
          overflow: hidden;
          text-overflow: ellipsis;
          word-wrap: break-word;
          white-space: normal !important;
          -webkit-line-clamp: 2;
          -webkit-box-orient: vertical;

          font-size: 12px;
          font-family: PingFangSC-Regular, PingFang SC;
          font-weight: 400;
          color: #78909c;
          line-height: 17px;
        }
      }

      .company-card:hover {
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.2);
      }

      .company-card + .company-card {
        margin-left: 35px;
      }
    }
  }

  .activity-box {
    padding: 25px 55px 47px 55px;
    background: #ffffff;
    box-shadow: 0px 4px 16px 3px rgba(191, 199, 215, 0.31);
    margin-bottom: 93px;

    .activity-container {
      position: relative;
      margin-bottom: 40px;
      display: flex;
      justify-content: space-between;
      cursor: pointer;

      .activity-card:hover {
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.5);
      }

      .activity-card {
        position: relative;
        width: 340px;
        height: 191px;

        .brief {
          position: absolute;
          width: 100%;
          left: 0;
          bottom: 0;
          height: 50px;
          background: rgba(0, 0, 0, 0.6);
          display: flex;
          flex-direction: column;
          padding: 4px 5px 6px 7px;

          .line1 {
            display: flex;
            align-items: center;
            justify-content: space-between;

            .name {
              margin: 0;
              color: #ffffff;
              line-height: 25px;
              overflow: hidden;
              text-overflow: ellipsis;
              white-space: nowrap;
              font-size: 14px;
              font-family: PingFangSC-Medium, PingFang SC;
              font-weight: 500;
              line-height: 20px;
            }

            .city {
              background: #00c853;
              height: 19px;
              line-height: 22px;
              padding: 0 5px;
              border-radius: 4px;
              font-size: 13px;
              flex-shrink: 0;
              margin-left: 7px;
              color: #ffffff;
              font-size: 14px;
              font-family: PingFangSC-Medium, PingFang SC;
              font-weight: 500;
              line-height: 20px;
            }

            .online {
              background: #ffab00;
            }
          }

          .line2 {
            display: flex;
            align-items: center;
            justify-content: space-between;

            .time {
              font-size: 12px;
              font-family: PingFangSC-Regular, PingFang SC;
              font-weight: 400;
              color: #ffffff;
              line-height: 17px;
            }

            .count {
              font-size: 12px;
              font-family: PingFangSC-Regular, PingFang SC;
              font-weight: 400;
              color: #ffffff;
              line-height: 17px;
              margin: auto;
            }
          }
        }
      }
    }
  }

  .job-recommend-box {
    padding: 25px 55px 47px 55px;
    background: #ffffff;
    box-shadow: 0px 4px 16px 3px rgba(191, 199, 215, 0.31);
    margin-bottom: 31px;
    .more {
      font-size: 14px;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      color: #999999;
      line-height: 20px;
      text-decoration: underline;
    }
    .main-title {
      margin-bottom: 40px;
      color: #333;
      text-align: center;
    }

    .recommend-job-container {
      display: flex;

      .job-card:hover {
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.2);
      }

      .job-card {
        padding: 12px 13px 23px 16px;
        width: 340px;
        height: 165px;
        background: linear-gradient(135deg, #f9fbfd 0%, #ddecfd 100%);
        cursor: pointer;
        margin-right: 35px;
        .job-section1 {
          display: flex;
          justify-content: space-between;
          margin-bottom: 18px;
          ::v-deep.el-button.is-circle {
            border-radius: 50%;
            padding: 12px;
            border: none;
          }
          .job-name {
            width: 160px;
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;
            -o-text-overflow: ellipsis;
            font-size: 18px;
            font-family: PingFangSC-Semibold, PingFang SC;
            font-weight: 600;
            color: #333333;
          }
          .job-salary {
            font-size: 16px;
            font-family: PingFangSC-Semibold, PingFang SC;
            font-weight: 600;
            color: #ff5a59;
            margin-left: 55px;
            margin-top: 4px;
            margin-right: 13px;
          }
          /deep/.el-button {
            margin-top: 4px;
            height: 21px;
            width: 21px;
            display: flex;
            justify-content: center;
            align-items: center;

            span {
              display: flex;
            }
          }
        }
        .job-city {
          font-size: 16px;
          font-family: PingFangSC-Regular, PingFang SC;
          font-weight: 400;
          color: #546e7a;
          line-height: 3px;
          .job-city-text {
            padding-right: 20px;
          }
          .job-minDegree {
            padding-right: 30px;
          }
        }

        .company-card-inner {
          width: 317px;

          .job-salary {
            color: #dc3545;
            margin-left: 4px;
          }
        }
        .job-company-container {
          display: flex;
          align-items: center;
          margin-top: -10px;

          .company-logo {
            width: 108px;
            height: 36px;
            margin-top: -9px;
            margin-left: 10px;
          }

          .company-info {
            margin-left: 5px;
            display: block;
            flex-direction: column;
            align-items: center;
            flex: 1;

            .company-name {
              margin-bottom: 4px;
              overflow: hidden;
              text-overflow: ellipsis;
              white-space: nowrap;
              font-size: 14px;
              font-family: PingFangSC-Regular, PingFang SC;
              font-weight: 400;
              color: #546e7a;
              line-height: 20px;
              height: 20px;
            }

            .company-tag {
              overflow: hidden;
              text-overflow: ellipsis;
              white-space: nowrap;
              height: 17px;
              font-size: 12px;
              font-family: PingFangSC-Regular, PingFang SC;
              font-weight: 400;
              color: #999999;
              line-height: 17px;
            }
          }
        }
      }
    }
  }
}

@media screen and (max-width: 1140px) {
  .app-container {
    .job-recommend-box {
      .recommend-job-container {
        display: flex;
        flex-wrap: wrap;
        justify-content: space-between;
      }

      .job-card {
        margin-right: 0px !important;
      }

      .job-card:last-of-type {
        margin-left: 0px !important;
        margin-top: 12px;
      }
    }

    .company-recommend-box {
      .company-container {
        display: flex;
        flex-wrap: wrap;
        justify-content: space-between;
      }

      .company-card:last-of-type {
        margin-left: 0px !important;
        margin-top: 12px;
      }
    }

    .activity-container {
      display: flex;
      flex-wrap: wrap;
      justify-content: space-between;
      .activity-card:last-of-type {
        margin-left: 0px !important;
        margin-top: 12px;
      }
    }
  }
}

@media screen and (max-width: 826px) {
  .app-container {
    .job-recommend-box {
      .recommend-job-container {
        display: flex;
        flex-wrap: wrap;
        justify-content: center;
      }

      .job-card + .job-card {
        margin-left: 0px !important;
        margin-top: 12px;
      }
    }

    .company-recommend-box {
      .company-container {
        display: flex;
        flex-wrap: wrap;
        justify-content: center;
      }

      .company-card + .company-card {
        margin-left: 0px !important;
        margin-top: 12px;
      }
    }

    .activity-box {
      .activity-container {
        display: flex;
        flex-wrap: wrap;
        justify-content: center;
        .activity-card + .activity-card {
          margin-left: 0px !important;
          margin-top: 12px;
        }
      }
    }
  }
}

@media screen and (max-width: 420px) {
  .app-container {
    .job-recommend-box {
      padding: 15px;
    }
    .company-recommend-box {
      padding: 15px;

      .recommend-company-container {
        .company-item-container {
          width: 100%;
          padding: 0;
        }
      }
    }
  }
}
</style>
