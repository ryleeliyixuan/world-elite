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
    <div class="section1-container">
      <el-carousel :interval="2500" arrow="always">
        <el-carousel-item v-for="banner in banners" :key="banner.img">
          <el-image
            class="section1-image"
            :src="banner.img"
            :alt="banner.alt"
            v-on:click="select(banner)"
            fit="cover"
          ></el-image>
        </el-carousel-item>
      </el-carousel>
    </div>
    <div class="job-recommend-box">
      <h3 class="main-title">职位精选</h3>
      <div class="recommend-job-container">
        <div
          class="job-item-container"
          v-for="recommendJob in recommendJobList"
          :key="recommendJob.id"
        >
          <el-card
            shadow="hover"
            class="link-pointer job-card"
            v-if="recommendJob.object"
          >
            <div class="company-card-inner">
              <el-link
                :href="`/job/${recommendJob.object.id}`"
                :underline="false"
              >
                <h6 style="display: flex; align-items: center">
                  <span class="job-name">{{ recommendJob.object.name }}</span>
                  <span class="job-salary">{{
                    recommendJob.object.salary.name
                  }}</span>
                </h6>
                <div class="text-gray text-small">
                  {{
                    `${
                      recommendJob.object.city
                        ? recommendJob.object.city.name
                        : ""
                    } /
                                    ${
                                      recommendJob.object.minDegree
                                        ? recommendJob.object.minDegree.name
                                        : ""
                                    }`
                  }}
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
                  <el-image
                    class="company-logo"
                    :src="recommendJob.object.companyUser.company.logo"
                  ></el-image>
                  <div class="company-info">
                    <div class="company-name">
                      {{ recommendJob.object.companyUser.company.name }}
                    </div>
                    <div class="text-gray text-small company-tag">
                      {{
                        recommendJob.object.companyUser.company.industry
                          ? recommendJob.object.companyUser.company.industry
                              .name
                          : ""
                      }}
                      /
                      {{
                        recommendJob.object.companyUser.company.stage
                          ? recommendJob.object.companyUser.company.stage.name
                          : ""
                      }}
                      /
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
          </el-card>
        </div>
      </div>
    </div>
    <!-- 企业百科 starts -->
    <div class="company-recommend-box">
      <div class="header text-center">企业百科</div>
      <div class="subheader text-center">CORPORATE INFO</div>
      <div class="title">大家都在看的企业</div>
      <div class="company-container">
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
                  handleFavorite(
                    company.object.id,
                    company.object.favoriteFlag,
                    company
                  )
                "
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
                  style="height: 18px; width: 14px"
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
                  style="height: 18px; width: 14px"
                />
                {{ company.object.industry.name }}
              </div>
              <div class="text">
                <svg-icon
                  icon-class="company-scale"
                  style="height: 18px; width: 14px"
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
      <div class="title">这些企业最近上新了职位</div>
      <div class="company-container">
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
                  handleFavorite(item.id, item.favoriteFlag, item)
                "
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
                  style="height: 18px; width: 14px"
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
                  style="height: 18px; width: 14px"
                />
                {{ item.industry.name }}
              </div>
              <div class="text">
                <svg-icon
                  icon-class="company-scale"
                  style="height: 18px; width: 14px"
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
    <div class="activity-box">
      <div class="header text-center">职场活动</div>
      <div class="subheader text-center">WORKPLACE EVENTS</div>
      <div class="title">热门招聘活动</div>
      <div class="activity-container">
        <div
          class="activity-card"
          v-for="item in activitylist.slice(0, 3)"
          :key="item.id"
          @click="onActivityDetail(item)"
        >
          <el-image class="thumbnail" :src="item.poster"></el-image>
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
import { getHomeConfig } from "@/api/config_api";
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
      filteredActivityList: [],
      activitylist: [],
      homeConfig: {},
      banners: [
        {
          img: require("../assets/banner/xiaomi1.jpg"),
          alt: "小米未来星项目招聘",
          url: "http://www.myworldelite.com/mi",
        },
        {
          img: require("../assets/banner/vip1.jpg"),
          alt: "唯品会2021校园招聘",
          url:
            "https://recruitment.corp.vipshop.com/wt/VIPS/web/index?brandCode=186838262#/",
        },
        {
          img: require("../assets/banner/qunar1.jpg"),
          alt: "去哪儿2021校园招聘",
          url: "https://app.mokahr.com/campus_apply/qunar/4207#/",
        },
        {
          img: require("../assets/banner/kuaishou1.jpg"),
          alt: "快手2021校园招聘",
          url: "https://campus.kuaishou.cn/recruit/campus/e/#/campus/index/",
        },
      ],
    };
  },
  methods: {
    initData() {
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
        limit: 9,
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
      getHomeConfig(8).then((response) => {
        this.homeConfig = response.data;
      });
    },
    select: function (banner) {
      window.open(banner.url);
    },
    handleFavorite(id, favorite, item) {
      let data = {
        favorite: !favorite,
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
    moreActivity() {
      this.$router.push("/activity-list");
    },
    // 点击活动，查看活动详情
    onActivityDetail(activity) {
      this.$router.push(`/activity/${activity.id}`);
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
    background: linear-gradient(90deg, #4895ef 0%, #2c5fb8 100%);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
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
    height: 50vw;
    width: calc(100vw - 20px);
    margin: 0 auto 30px;

    .el-carousel {
      height: 100%;

      /deep/ .el-carousel__container {
        height: 100%;
      }
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
        }

        .company-section2 {
          display: flex;
          margin-bottom: 13px;
          justify-content: space-between;

          .text {
            font-size: 14px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: #546e7a;
            line-height: 20px;
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
      width: 340px;
      height: 191px;
      margin-bottom: 40px;
      display: flex;
      cursor: pointer;

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

  .job-recommend-box {
    .main-title {
      margin-bottom: 40px;
      color: #333;
      text-align: center;
    }

    .recommend-job-container {
      max-width: 1140px;
      margin: 0 auto;
      display: flex;
      flex-wrap: wrap;
      justify-content: center;

      .job-item-container {
        padding: 10px;

        .job-card {
          border: none;
          text-align: center;

          .company-card-inner {
            width: 317px;
            .job-name {
              display: inline-block;
              max-width: 240px;
              overflow: hidden;
              text-overflow: ellipsis;
              white-space: nowrap;
            }

            .job-salary {
              color: #dc3545;
              margin-left: 4px;
            }

            .job-company-container {
              display: flex;
              align-items: center;

              .company-logo {
                width: 50px;
                height: 50px;
              }

              .company-info {
                margin-left: 5px;
                display: flex;
                flex-direction: column;
                align-items: center;
                flex: 1;

                .company-name {
                  margin-bottom: 4px;
                  overflow: hidden;
                  text-overflow: ellipsis;
                  white-space: nowrap;
                }

                .company-tag {
                  overflow: hidden;
                  text-overflow: ellipsis;
                  white-space: nowrap;
                }
              }
            }
          }
        }
      }
    }
  }
}

@media screen and (max-width: 1140px) {
  .app-container {
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

@media screen and (max-width: 825px) {
  .app-container {
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

@media screen and (max-width: 420px) {
  .app-container {
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
