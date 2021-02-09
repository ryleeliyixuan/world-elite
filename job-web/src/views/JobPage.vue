<template>
  <div class="wrapper">
    <div class="app-container">
      <el-card :body-style="{ padding: '0px' }" shadow="never">
        <div class="session1-container" v-if="job">
          <div class="session1-left">
            <div
              class="text1 mb-2"
              v-if="job.companyUser && job.companyUser.company"
            >
              {{ job.companyUser.company.name }}招聘
            </div>
            <div class="session1-job-name mb-2">
              {{ job.name }}
              <span class="salary-text ml-4">
               {{ job.minSalary+'K-'+job.maxSalary+'K' }}
              </span>
            </div>
            <div class="text3 mb-2">
              {{ job.city ? job.city.name : "" }} |
              {{ job.minDegree ? job.minDegree.name : "" }} |
              {{ job.jobType ? job.jobType.name : "" }}
            </div>
            <div class="text4">{{ job.time }}</div>
          </div>
          <div class="session1-right">
            <!--  :loading="favoriteLoading" -->
            <el-button
              style="margin-right: 29px"
              circle
              @click="handleFavorite"
              class="flag"
              :style="
                job.favoriteFlag === 1
                  ? `background: #ff3d00`
                  : `background: #FFFFFF;`
              "
              ><svg-icon
                :icon-class="job.favoriteFlag === 1 ? 'jobflag' : 'jobunflag'"
                style="height: 15px; width: 19px"
            /></el-button>
            <el-button style="margin-right: 29px" circle @click="handleChat"
              ><svg-icon icon-class="jobchat" style="height: 18px; width: 19px"
            /></el-button>
            <div class="apply">
              <el-button
                type="primary"
                :loading="applyLoading"
                @click="(applyDialog = true), getResumeInfo()"
                v-bind:disabled="job.applyFlag === 1"
              >
                {{ job.applyFlag === 1 ? "已申请" : "申请岗位" }}
              </el-button>
            </div>
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
            <div
              class="session2-container-right-1"
              v-if="job.companyUser && job.companyUser.company"
            >
              <div class="basic-info mb-2">公司基本信息:</div>
              <el-link
                :href="`/company/${job.companyUser.company.id}`"
                :underline="false"
              >
                <div class="session2-logo-container">
                  <el-image
                    :src="job.companyUser.company.logo"
                    class="session2-logo"
                  ></el-image>
                </div>
              </el-link>
              <div
                v-if="job.companyUser.company.industry"
                class="info-text mt-2"
              >
                <svg-icon
                  icon-class="companycategory"
                  style="height: 19px; width: 19px; margin-right: 9px"
                />
                {{ job.companyUser.company.industry.name }}
              </div>
              <div
                v-if="
                  job.companyUser.company.stage &&
                  job.companyUser.company.property
                "
                class="info-text mt-2"
              >
                <svg-icon
                  icon-class="companytype"
                  style="height: 19px; width: 19px; margin-right: 9px"
                />
                {{ job.companyUser.company.property.name }} |
                {{ job.companyUser.company.stage.name }}
              </div>
              <div v-if="job.companyUser.company.scale" class="info-text mt-2">
                <svg-icon
                  icon-class="employeedata"
                  style="height: 19px; width: 21px; margin-right: 9px"
                />
                {{ job.companyUser.company.scale.name }}
              </div>
              <div
                v-if="job.companyUser.company.homepage"
                class="info-text mt-2"
              >
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
              v-if="
                job.companyUser &&
                job.companyUser.company &&
                job.companyUser.company.addressList &&
                job.companyUser.company.addressList.length > 0
              "
            >
              <div class="basic-info mb-2">公司地址:</div>
              <div
                v-for="(addr, index) in job.companyUser.company.addressList"
                :key="addr.id"
              >
                <div class="map-wrapper">
                  <div class="map-box mb-4">
                    <el-amap
                      :vid="'amap' + index"
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
                  <div class="info-text">{{ addr.address }}</div>
                </div>
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
    <!-- 预览简历 -->
    <div class="resume">
      <el-dialog :visible.sync="previewDialog" width="854px">
        <div class="resume-preview">
          <!-- <el-button type="text"
            ><svg-icon
              icon-class="resumedownload"
              style="height: 23px; width: 29px"
          /></el-button> -->
          <el-button @click="previewDialog = false" type="text"
            ><svg-icon
              icon-class="resumereviewclose"
              style="height: 17px; width: 17px"
          /></el-button>
        </div>
        <ResumeView
          :resumeDetail="resumeDetail"
          class="resume-preview-content"
        ></ResumeView
      ></el-dialog>
    </div>
    <!--  选择投递简历  -->
    <el-dialog title="选择投递简历" :visible.sync="applyDialog">
      <!-- resume alert start -->
      <el-dialog
        class="progress-alert"
        :visible.sync="progressAlertDialog"
        append-to-body
      >
        <div style="display: flex; justify-content: center">
          <svg-icon
            icon-class="jobresumeprogressalert"
            style="height: 82px; width: 94px; margin-bottom: 21px"
          />
        </div>
        <div class="progress-alert-text">
          您所选的简历完整度较低（建议提升至80%以上），是否确认投递？
        </div>
        <span slot="footer" class="dialog-footer">
          <el-button @click="progressAlertDialog = false">去修改</el-button>
          <el-button
            type="primary"
            @click="submitResume(), (progressAlertDialog = false)"
            >确 定</el-button
          >
        </span>
      </el-dialog>
      <!-- resume alert end -->
      <div class="dialog-header">
        <span class="dialog-header-title">在线简历</span>
        <span class="dialog-header-button"
          ><el-button type="text" @click="getResumeInfo"
            >刷新<svg-icon
              icon-class="jobrefresh"
              style="height: 13px; width: 13px; margin-left: 7px" /></el-button
        ></span>
      </div>
      <el-table
        ref="multipleTable"
        class="dialog-table"
        :data="resume"
        style="width: 100%"
        :show-header="false"
        v-loading="resumeListLoading"
      >
        <el-table-column width="30">
          <template slot-scope="scope">
            <el-radio
              @change="
                handleCurrentChange(
                  scope.row.id,
                  scope.row.resumeCompleteProgress
                )
              "
              v-model="checked"
              :label="scope.row.id"
            ></el-radio>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="简历名称" width="180">
          <template slot-scope="scope">
            <span>{{
              scope.row.title && scope.row.title != ""
                ? scope.row.title
                : "未命名简历"
            }}</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="resumeCompleteProgress"
          label="完成度"
          width="120"
        >
          <template slot-scope="scope">
            <span
              :style="
                scope.row.resumeCompleteProgress == 100
                  ? `color: #455A64`
                  : scope.row.resumeCompleteProgress >= 80
                  ? `color: #607D8B;`
                  : `color: #90A4AE;`
              "
              >(完整度: {{ scope.row.resumeCompleteProgress }}%)</span
            >
          </template>
        </el-table-column>
        <el-table-column class="button" label="操作" width="180">
          <template slot-scope="scope">
            <el-button
              type="primary"
              @click="handlePreview(scope.row.id), (previewDialog = true)"
              >预览</el-button
            >
            <el-button @click="handleEditResume(scope.row.id)">修改</el-button>
          </template>
        </el-table-column>
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button @click="applyDialog = false">取 消</el-button>
        <el-button type="primary" @click="handleApplyJob()">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import Vue from "vue";
import VueAMap from "vue-amap";
import ResumeView from "@/components/ResumeView";
import { getResumeInfo, getResumeDetail } from "@/api/resume_api";
import { getJobInfo, applyJob } from "@/api/job_api";
import { doFavorite } from "@/api/favorite_api";
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
  name: "JobPage",
  components: { ResumeView },
  data() {
    return {
      //resume
      selectedId: undefined,
      resume: undefined,
      previewDialog: false,
      // previewResumeId: undefined,
      resumeDetail: undefined,
      resumeListLoading: true,
      progressAlertDialog: false,
      checked: null,

      //map
      activeAddress: 0,
      mapZoom: 13,

      //applydialog
      applyDialog: false,

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
      return this.job.companyUser.company.homepage.startsWith("http")
        ? this.job.companyUser.company.homepage
        : "http://" + this.job.companyUser.company.homepage;
    },
    companyLinkName() {
      return this.job.companyUser.company.homepage.replace(/http(s)?:\/\//, "");
    },
  },
  methods: {
    initData() {
      const jobId = this.$route.params.id;
      getJobInfo(jobId).then((response) => {
        this.job = response.data;
        const title = `${this.job.name} - ${
          this.job.companyUser.company ? this.job.companyUser.company.name : ""
        }`;
        if (
          this.job.companyUser &&
          this.job.companyUser.company &&
          this.job.companyUser.company.addressList &&
          this.job.companyUser.company.addressList.length > 0
        ) {
          for (const addr of this.job.companyUser.company.addressList) {
            addr.mapWindow = {
              position: [addr.longitude, addr.latitude],
              content: addr.address,
            };
          }
        }
        setPageTitle(title);
        this.shareConfig.title = title;
        this.shareConfig.description = this.job.description;
        this.favoriteForm.objectId = this.job.id;
        this.favoriteForm.favorite = this.job.favoriteFlag == 1;
        this.$emit("complete");
      });
    },
    handleCurrentChange(id, progess) {
      this.selectedId = id;
      this.selectedProgress = progess;
      console.log(this.selectedProgress);
    },
    handleEditResume(id) {
      let page = this.$router.resolve({
        path: "/edit-resume",
        query: { resumeId: id },
      });
      window.open(page.href, "_blank");
    },
    handleFavorite() {
      this.favoriteLoading = true;
      this.favoriteForm.favorite = !this.favoriteForm.favorite;
      doFavorite(this.favoriteForm)
        .then(() => {
          this.job.favoriteFlag = this.favoriteForm.favorite ? 1 : 0;
          this.$message("操作成功");
        })
        .finally(() => {
          this.favoriteLoading = false;
        });
    },
    handleApplyJob() {
      if (this.selectedProgress < 80) {
        this.progressAlertDialog = true;
        return;
      }
      this.submitResume();
    },
    submitResume() {
      this.applyLoading = true;
      let data = {
        id: this.job.id,
        resumeId: this.selectedId,
      };
      applyJob(data)
        .then(() => {
          this.job.applyFlag = 1;
          this.$message("申请成功");
        })
        .finally(() => {
          this.applyLoading = false;
        });
      this.applyDialog = false;
    },
    handleChat() {
      this.$router.push({
        path: "/chat",
        query: { toUser: this.job.creatorId, jobId: this.job.id },
      });
    },
    getResumeInfo() {
      this.resumeListLoading = true;
      getResumeInfo()
        .then((response) => (this.resume = response.data))
        .finally(() => {
          this.resumeListLoading = false;
        });
    },
    handlePreview(id) {
      getResumeDetail(id).then((response) => {
        this.resumeDetail = response.data;
        // console.log(this.resumeDetail);
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
