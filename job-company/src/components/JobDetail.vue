<template>
  <div class="wrapper">
    <div class="app-container">
      <el-card :body-style="{ padding: '0px' }" shadow="never">
        <div class="session1-container" v-if="job">
          <div class="session1-left">
            <div
              class="text1 mb-2"
              v-if="job && job.company && job.recruitType != 172"
            >
              {{ job.company.name }}招聘
            </div>
            <div
              class="text1 mb-2"
              v-if="job && job.company && job.recruitType == 172"
            >
              WE内推岗位
            </div>
            <div class="session1-job-name mb-2">
              {{ job.name }}
              <!-- 薪资 -->
              <!-- 职位薪资范围 -->
              <!-- 0-5k -> 5k一下 -->
              <span
                v-if="job.minSalary == 0 && job.maxSalary == 5"
                class="salary-text ml-4"
                style="font-size: 16px"
              >
                {{ "5K以下" }}
              </span>
              <!-- 0-0k -> 不限 -->
              <span
                v-else-if="job.minSalary == 0 && job.maxSalary == 0"
                class="salary-text ml-4"
                style="font-size: 16px"
              >
                {{ "薪酬不限" }}
              </span>
              <!-- 其他数值正常显示 -->
              <!-- 全职为“min-max * number” -->
              <span v-else-if="job.jobType.id == 107" class="salary-text ml-4">
                {{
                  job.minSalary +
                  "K-" +
                  job.maxSalary +
                  "K" +
                  " * " +
                  job.salaryMonths
                }}
              </span>
              <!-- 实习为“min-max 元/天” -->
              <span v-else class="salary-text ml-4">
                {{ job.minSalary + "-" + job.maxSalary + " 元/天" }}
              </span>
            </div>
            <div class="text3 mb-2">
              {{ job.city ? job.city.name : "" }} |
              {{
                job.minDegree && job.minDegree.name
                  ? job.minDegree.name == "不限"
                    ? "学历不限"
                    : job.minDegree.name
                  : ""
              }}
              | {{ job.jobType ? job.jobType.name : "" }} |
              {{
                job.experience.name
                  ? job.experience.name == "不限"
                    ? "工作经验不限"
                    : job.experience.name
                  : ""
              }}
              <span v-for="item in job.additions" :key="item.id">
                | {{ item.name }}
              </span>
            </div>
            <div class="text4">{{ job.time }}</div>
          </div>
        </div>
        <div class="session2-container" v-if="job">
          <div class="session2-container-left">
            <div v-html="job.description"></div>
            <!-- <div v-if="job.address" class="session2-job-address">
              <h5>工作地址</h5>
              <span>
                {{ job.address }}
              </span>
            </div> -->
          </div>
          <div class="divider"></div>
          <div class="session2-container-right">
            <div class="mb-4" v-if="job && job.recruitType == 172">
              <svg-icon
                icon-class="job-fast-track"
                style="height: 70px; width: 180px; margin-left: -25px"
              />
              <div>
                "WE内推"是World
                Elite最具竞争力的产品之一，致力于推出一个让每个学生都放心的渠道获取内推资源，凭借自己的努力走出自己的职业道路。
              </div>
            </div>
            <div class="session2-container-right-1" v-if="job && job.company">
              <div class="basic-info mb-2">公司基本信息:</div>
              <el-link :href="`/company/${job.company.id}`" :underline="false">
                <div class="session2-logo-container">
                  <el-image
                    :src="job.company.logo"
                    class="session2-logo"
                  ></el-image>
                </div>
              </el-link>
              <div v-if="job.company.industry" class="info-text mt-2">
                <svg-icon
                  icon-class="companycategory"
                  style="height: 19px; width: 19px; margin-right: 9px"
                />
                {{ job.company.industry.name }}
              </div>
              <div
                v-if="job.company.stage && job.company.property"
                class="info-text mt-2"
              >
                <svg-icon
                  icon-class="companytype"
                  style="height: 19px; width: 19px; margin-right: 9px"
                />
                {{ job.company.property.name }} |
                {{ job.company.stage.name }}
              </div>
              <div v-if="job.company.scale" class="info-text mt-2">
                <svg-icon
                  icon-class="employeedata"
                  style="height: 19px; width: 21px; margin-right: 9px"
                />
                {{ job.company.scale.name }}
              </div>
              <div v-if="job.company.homepage" class="info-text mt-2">
                <svg-icon
                  icon-class="website"
                  style="height: 17px; width: 21px; margin-right: 12px"
                />
                <el-link
                  :href="companyLink"
                  target="_blank"
                  :underline="false"
                  class="website-text"
                  >{{ companyLinkName }}
                </el-link>
              </div>
            </div>
            <div
              class="session2-container-right-2"
              v-if="job && job.address && job.latitude && job.longitude"
            >
              <div class="basic-info mb-2">工作地址:</div>
              <div class="map-wrapper">
                <div class="map-box mb-4">
                  <el-amap
                    :vid="'amap'"
                    :zoom="mapZoom"
                    :center="addr.mapWindow.position"
                  >
                    <el-amap-marker :position="addr.mapWindow.position">
                    </el-amap-marker>
                    <el-amap-info-window
                      :position="addr.mapWindow.position"
                      :content="addr.mapWindow.content"
                      :close-when-click-map="true"
                    ></el-amap-info-window>
                  </el-amap>
                </div>
              </div>
              <div class="address-label">
                <div class="address-label-icon-wrapper">
                  <svg-icon
                    icon-class="jobaddress"
                    style="
                      height: 19px;
                      width: 15px;
                      margin-right: 7px;
                      top: 3px;
                      position: absolute;
                    "
                  />
                </div>
                <div class="info-text">{{ job.address }}</div>
              </div>
            </div>
            <div class="session2-container-right-3">
              <div class="basic-info mb-2">分享该职位:</div>
              <share :config="shareConfig"></share>
            </div>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
import Vue from "vue";
import VueAMap from "vue-amap";
import { getResumeInfo, getResumeDetail } from "@/api/resume_api";
import { getJobInfo, applyJob } from "@/api/job_api";
import { setPageTitle } from "@/utils/setting";
import Share from "vue-social-share";
import "vue-social-share/dist/client.css";

Vue.use(Share);
Vue.use(VueAMap);

VueAMap.initAMapApiLoader({
  key: process.env.VUE_APP_AMAP_KEY,
  v: "1.4.4",
});

export default {
  name: "JobDetail",
  props: {
    jobId: {
      type: Number,
    },
  },
  data() {
    return {
      //resume
      resume: undefined,
      // previewResumeId: undefined,
      resumeDetail: undefined,
      resumeListLoading: true,
      progressAlertDialog: false,
      checked: null,

      //map
      activeAddress: 0,
      mapZoom: 13,
      addr: {
        mapWindow: {},
      },

      job: undefined,
      favoriteForm: {
        objectId: undefined,
        type: 1,
        favorite: false,
      },
      mapDialogVisible: false,
      favoriteLoading: false,
      applyLoading: false,
      shareConfig: {
        url: window.location.href,
        source: "",
        title: "",
        description: "",
        image: "",
        sites: ["wechat", "qq", "weibo", "google", "facebook", "twitter"],
      },
    };
  },
  created() {
    this.initData();
  },
  watch: {
    $route() {
      this.initData();
    },
  },
  computed: {
    companyLink() {
      return this.job.company.homepage.startsWith("http")
        ? this.job.company.homepage
        : "http://" + this.job.company.homepage;
    },
    companyLinkName() {
      return this.job.company.homepage.replace(/http(s)?:\/\//, "");
    },
  },
  methods: {
    initData() {
      getJobInfo(this.jobId).then((response) => {
        this.job = response.data;
        const title = `${this.job.name} - ${
          this.job.company ? this.job.company.name : ""
        }`;
        // 工作地址
        if (
          this.job &&
          this.job.address &&
          this.job.latitude &&
          this.job.longitude
        ) {
          this.addr.mapWindow = {
            position: [this.job.longitude, this.job.latitude],
            content: this.job.address,
          };
        }
        setPageTitle(title);
        this.shareConfig.title = title;
        this.shareConfig.description = this.job.description;
        this.favoriteForm.objectId = this.job.id;
        this.favoriteForm.favorite = this.job.favoriteFlag == 1;
        this.$emit("complete");
      });
    },
  },
};
</script>

<style scoped lang="scss">
.wrapper {
  width: 100%;
  background: #f6f9fc;
}

.resume {
  /deep/ .el-dialog__header {
    display: none;
  }
  /deep/ .el-dialog {
    border-radius: 0px;
  }

  /deep/ .el-dialog__body {
    padding: 10px 9px 43px 62px;
  }

  .resume-preview {
    display: flex;
    justify-content: flex-end;
  }

  .resume-preview-content {
    padding: 25px 74px 0px 0px;
  }
}

.progress-alert {
  /deep/ .el-dialog__body {
    padding: 26px 62px 21px 63px;
  }
  .dialog-footer {
    padding-bottom: 22px;
  }
  .progress-alert-text {
    font-size: 18px;
    font-family: PingFangSC-Medium, PingFang SC;
    font-weight: 500;
    color: #333333;
    line-height: 25px;
  }
}

/deep/ .el-dialog {
  width: 675px;
  background: #ffffff;
  border-radius: 28px;
  border: 0px;
}

/deep/ .el-dialog__headerbtn {
  display: none;
}

/deep/ .el-dialog__header {
  display: flex;
  justify-content: center;

  .el-dialog__title {
    font-size: 24px;
    font-family: PingFangSC-Medium, PingFang SC;
    font-weight: 500;
    color: #333333;
    line-height: 33px;
  }
}

/deep/ .el-dialog__body {
  padding: 31px 73px 64px 75px;
}

/deep/.el-table td,
.el-table th {
  border-bottom: 0px !important;
}

/deep/ .el-table::before {
  display: none;
}

/deep/ .el-table td,
.el-table th {
  padding: 9px 0;
}

.dialog-table {
  /deep/ .el-radio {
    margin: 0;
  }

  /deep/ .el-radio__label {
    display: none;
  }

  /deep/ .cell {
    display: flex;
    padding: 0;
  }

  /deep/ .el-table__body {
    margin-right: 0px;
  }

  /deep/ .el-table__row td:last-child {
    display: flex;
    justify-content: flex-end;
  }

  /deep/ .el-checkbox {
    margin-bottom: 0;
  }

  /deep/ .el-checkbox__inner {
    border-color: #4895ef;
  }
  /deep/ .el-button--primary {
    width: 65px;
    height: 22px;
    background: #4895ef;
    border-radius: 11px;
    font-size: 14px;
    font-family: PingFangSC-Medium, PingFang SC;
    font-weight: 500;
    color: #ffffff;
    line-height: 20px;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  /deep/ .el-button--default {
    width: 65px;
    height: 22px;
    background: #ffffff;
    border-radius: 11px;
    border: 1px solid #4895ef;
    font-size: 14px;
    font-family: PingFangSC-Medium, PingFang SC;
    font-weight: 500;
    color: #4895ef;
    line-height: 20px;
    display: flex;
    align-items: center;
    justify-content: center;
  }
}

.dialog-header {
  display: flex;
  justify-content: space-between;
  .dialog-header-title {
    font-size: 21px;
    font-family: PingFangSC-Medium, PingFang SC;
    font-weight: 500;
    color: #333333;
    line-height: 29px;
  }
  .dialog-header-button {
    font-size: 14px;
    font-family: PingFangSC-Medium, PingFang SC;
    font-weight: 500;
    color: #4895ef;
    line-height: 20px;
    /deep/ .el-button {
      span {
        display: flex;
      }
    }
  }
}

.dialog-footer {
  display: flex;
  justify-content: center;
  padding-bottom: 39px;

  /deep/ .el-button--primary {
    width: 133px;
    height: 35px;
    background: #4895ef;
    border-radius: 18px;
    font-size: 16px;
    font-family: PingFangSC-Medium, PingFang SC;
    font-weight: 500;
    color: #ffffff;
    line-height: 22px;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  /deep/ .el-button--default {
    width: 134px;
    height: 35px;
    background: #ffffff;
    border-radius: 18px;
    border: 1px solid #4895ef;
    font-size: 16px;
    font-family: PingFangSC-Medium, PingFang SC;
    font-weight: 500;
    color: #4895ef;
    line-height: 22px;
    display: flex;
    align-items: center;
    justify-content: center;
  }
}

.app-container {
  max-width: 1140px;
  margin: 0 auto;
  padding: 0 20px;
  min-height: calc(100vh - 477px);
  padding-top: 31px;
  padding-bottom: 194px;

  /deep/.el-card {
    background: #ffffff;
    box-shadow: 0px 4px 16px 3px rgba(191, 199, 215, 0.31);
  }

  .session1-container {
    height: 198px;
    display: flex;
    align-items: center;
    background: url("../assets/job/job-background.svg") no-repeat;
    background-size: 1200px 198px;

    .session1-left {
      padding: 28px 50px;
      display: flex;
      flex-direction: column;
      flex: 2;

      .text1 {
        font-size: 16px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #e0e0e0;
        line-height: 22px;
      }

      .session1-job-name {
        font-size: 24px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #fafafa;
        line-height: 33px;

        .salary-text {
          font-size: 24px;
          font-family: PingFangSC-Medium, PingFang SC;
          font-weight: 500;
          color: #eeff41;
          line-height: 33px;
        }
      }

      .text3 {
        font-size: 18px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #fafafa;
        line-height: 25px;
      }

      .text4 {
        font-size: 16px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #e0e0e0;
        line-height: 22px;
      }
    }

    .session1-right {
      display: flex;
      flex: 1;
      margin-left: 30px;
      /deep/.el-button {
        box-shadow: 0px 5px 13px 0px #2868c0;
        border: 0px;
        height: 45px;
        width: 45px;
      }

      /deep/.el-button + .el-button {
        margin-left: 0px;
      }

      .apply {
        /deep/ .el-button {
          background: #ffffff;
          box-shadow: 0px 5px 13px 0px #2868c0;
          border-radius: 20px;
          min-width: 125px;

          span {
            font-size: 16px;
            font-family: PingFangSC-Medium, PingFang SC;
            font-weight: 500;
            color: #333333;
            line-height: 22px;
          }
        }
      }
    }
  }

  .session2-container {
    display: flex;
    padding: 31px 68px 50px 50px;

    .session2-container-left {
      display: flex;
      flex-direction: column;
      flex: 3;
      margin-right: 50px;

      .session2-job-address {
        margin-top: 10px;

        span {
          line-height: 50px;
          margin-top: 10px;
        }
      }
    }

    .session2-container-bottom {
      display: none;
    }

    .divider {
      width: 1px;
      background: #cccccc;
      border-radius: 1px;
    }

    .session2-container-right {
      flex: 1;
      display: flex;
      flex-direction: column;
      margin-left: 50px;
      min-width: 259px;

      .session2-container-right-2 {
        margin-top: 60px;
        .address-label {
          display: flex;
          flex-direction: row;
          justify-content: flex-start;
          .address-label-icon-wrapper {
            position: relative;
            width: 40px;
            height: 19px;
          }
        }
        /deep/ .amap-logo {
          display: none !important;
        }

        /deep/ .amap-info {
          display: none !important;
        }
        /deep/ .el-vue-amap {
          background-color: white !important;
        }
        /deep/ .amap-maps {
          border-radius: 13px;
        }
      }

      .session2-container-right-3 {
        margin-top: 60px;
      }

      .basic-info {
        font-size: 18px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #333333;
        line-height: 25px;
      }

      .el-link {
        text-align: left;
        display: inline;

        .session2-logo-container {
          display: flex;
          align-items: flex-end;
          justify-content: flex-start;

          .session2-logo {
            width: 130px;
          }

          .company-text {
            font-size: 18px;
            padding-left: 12px;
          }
        }
      }

      .info-text {
        font-size: 16px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #333333;
        line-height: 22px;
      }

      .website-text {
        font-size: 16px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #4895ef;
        line-height: 22px;
      }
    }
  }
}

@media screen and (max-width: 850px) {
  .app-container {
  }
}

@media screen and (max-width: 410px) {
  .app-container {
    .session1-container {
      .session1-right {
        display: none;
      }
    }

    .session2-container {
      flex-direction: column-reverse;

      .session2-container-bottom {
        display: flex;
        justify-content: center;
        margin: 30px;
      }

      .session2-container-right {
        margin-left: 0;
        margin-bottom: 30px;

        .el-link {
          .session2-logo-container {
            display: flex;
            align-items: flex-end;
            justify-content: center;

            .session2-logo {
              width: 100px;
            }

            .company-text {
              font-size: 18px;
              padding-left: 12px;
            }
          }
        }
      }
    }
  }
}

.map-box {
  height: 175px;
}
</style>
