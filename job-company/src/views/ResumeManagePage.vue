<template>
  <div class="app-container">
    <el-menu :default-active="activeIndex" mode="horizontal" @select="handleSelectMenu">
      <el-menu-item index="1,2">新简历</el-menu-item>
      <el-menu-item index="3">初筛</el-menu-item>
      <el-menu-item index="4">面试</el-menu-item>
      <el-menu-item index="5">录用</el-menu-item>
    </el-menu>
    <div class="mt-3">
      <el-row :gutter="10">
        <el-col :span="6">
          <el-input
            v-model="listQuery.name"
            @change.native="handleFilter"
            class="w-100"
            placeholder="应聘者名称"
            size="small"
          ></el-input>
        </el-col>
        <el-col :span="6">
          <el-select
            v-model="listQuery.jobIds"
            placeholder="职位"
            multiple
            clearable
            filterable
            size="small"
            class="w-100"
            @change="handleFilter"
          >
            <el-option
              v-for="item in jobOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            >
              <span style="float: left">{{ item.name }}</span>
              <span style="float: right; color: #8492a6; font-size: 13px">{{ item.city.name }}</span>
            </el-option>
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-select
            v-model="listQuery.degreeIds"
            placeholder="学历"
            multiple
            clearable
            filterable
            size="small"
            class="w-100"
            @change="handleFilter"
          >
            <el-option
              v-for="item in degreeOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-col>
      </el-row>
    </div>
    <div class="resume-list mt-3">
      <el-card class="box-card mb-3" v-for="applyResume in pageResult.list" :key="applyResume.id">
        <div slot="header" class="clearfix text-small" style="line-height: 40px;">
          <span>应聘岗位：{{applyResume.job.name}}</span>
          <span class="ml-4">应聘时间：{{applyResume.time}}</span>
          <div class="float-right">
            <el-button
              type="primary"
              size="small"
              @click="handleApplyResume(3, applyResume.id)"
              v-if="applyResume.applyStatus == 1 || applyResume.applyStatus == 2"
            >通过初筛</el-button>
            <el-button
              type="primary"
              size="small"
              @click="handleApplyResume(4, applyResume.id)"
              v-if="applyResume.applyStatus == 3"
            >进入面试</el-button>
            <el-button
              type="primary"
              size="small"
              @click="handleApplyResume(5, applyResume.id)"
              v-if="applyResume.applyStatus == 4"
            >录用</el-button>
            <el-button
              type="danger"
              plain
              size="small"
              @click="handleApplyResume(6, applyResume.id)"
              v-if="applyResume.applyStatus != 5"
            >不合适</el-button>
          </div>
        </div>
        <b-media @click="handleShowResume(applyResume)" style="cursor: pointer;">
          <template v-slot:aside>
            <el-badge is-dot class="item" :hidden="applyResume.applyStatus !== 1">
              <el-avatar :src="applyResume.resume.avatar"></el-avatar>
            </el-badge>
          </template>
          <b-media-body>
            <h6>
              {{applyResume.resume.name}}
              <span class="ml-4 text-muted text-small">
                <span
                  v-if="applyResume.resume.maxResumeEdu"
                >{{applyResume.resume.maxResumeEdu.degree.name}} /</span>
                {{applyResume.resume.age}}岁 / {{applyResume.resume.curPlace}}
              </span>
            </h6>
            <div
              class="tag-group"
              v-if="applyResume.resume.resumeSkillList && applyResume.resume.resumeSkillList.length !== 0"
            >
              <el-tag
                size="small"
                effect="plain"
                type="info"
                class="mr-1"
                v-for="skill in applyResume.resume.resumeSkillList"
                :key="skill.id"
              >{{skill.name}}</el-tag>
            </div>
            <el-row :gutter="12" class="mt-4">
              <el-col
                :span="12"
                v-if="applyResume.resume.resumeEduList && applyResume.resume.resumeEduList.length !== 0"
              >
                <h6>教育经历</h6>
                <div
                  class="text-small mt-2"
                  v-for="edu in applyResume.resume.resumeEduList"
                  :key="edu.id"
                >
                  <span>{{ edu.schoolName }} / {{ edu.majorName }}.{{edu.degree.name}}</span>
                  <span v-if="edu.gpa">/ GPA {{edu.gpa}}</span>
                </div>
              </el-col>
              <el-col
                :span="12"
                v-if="applyResume.resume.resumeExpList && applyResume.resume.resumeExpList.length !== 0"
              >
                <h6>工作经历</h6>
                <div
                  :gutter="12"
                  class="text-small mt-2"
                  v-for="exp in applyResume.resume.resumeExpList"
                  :key="exp.id"
                >
                  <span>{{exp.company}} / {{exp.post}} / {{exp.startTime}} - {{exp.finishTime}}</span>
                </div>
              </el-col>
            </el-row>
          </b-media-body>
        </b-media>
      </el-card>
    </div>
    <pagination
      v-show="total"
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.limit"
      @pagination="handleListPageRoute"
    />

    <div class="resume-drawer" v-if="reviewDrawerVisible && activeApplyResume">
      <div class="resume-drawer-header">
        <el-row type="flex" class="row-bg" justify="space-between">
          <el-col :span="12" class="mt-4 ml-4">
            <el-button
              type="primary"
              @click="handleApplyResume(3, activeApplyResume.id)"
              v-if="activeApplyResume.applyStatus == 1 || activeApplyResume.applyStatus == 2"
            >通过初筛</el-button>
            <el-button
              type="primary"
              @click="handleApplyResume(4, activeApplyResume.id)"
              v-if="activeApplyResume.applyStatus == 3"
            >进入面试</el-button>
            <el-button
              type="primary"
              @click="handleApplyResume(5, activeApplyResume.id)"
              v-if="activeApplyResume.applyStatus == 4"
            >录用</el-button>
            <el-button
              type="danger"
              plain
              @click="handleApplyResume(6, activeApplyResume.id)"
              v-if="activeApplyResume.applyStatus != 5"
            >不合适</el-button>
            <el-button
              type="primary"
              :loading="resumeExporting"
              icon="el-icon-download"
              @click="onDownloadResumeClick(activeApplyResume)"
            >下载简历</el-button>
          </el-col>
          <el-col :span="6" class="text-right">
            <el-button
              type="text"
              class="el-icon-close text-muted close-text p-4"
              @click="reviewDrawerVisible=false"
            ></el-button>
          </el-col>
        </el-row>
      </div>
      <div class="resume-drawer-body pl-4 pr-4 pb-4">
        <ResumeView :resumeId="activeApplyResume.resume.id" class="mt-3"></ResumeView>
      </div>
    </div>
  </div>
</template>

<script>
import Pagination from "@/components/Pagination";
import { applyResumeList, handleApplyResume } from "@/api/resume_api";
import { listByType } from "@/api/dict_api";
import { getUserJobOptions } from "@/api/job_api";
import ResumeView from "@/components/ResumeView";
import { exportResumeToPdf } from "@/api/export_api";
import { downloadFile, formatListQuery, parseListQuery } from "@/utils/common";

export default {
  name: "ResumeManagePage",
  components: { Pagination, ResumeView },
  data() {
    return {
      activeIndex: "1,2",
      listQuery: {
        name: undefined,
        jobIds: [],
        degreeIds: [],
        statuses: [1, 2],
        page: 1,
        limit: 20
      },
      total: 0,
      activeApplyResume: undefined,
      reviewDrawerVisible: false,
      resumeExporting: false,
      pageResult: {},
      degreeOptions: [],
      jobOptions: [],
      statusMessageMap: {
        3: "此操作将把该简历标识为初筛，并通知应聘者，是否继续？",
        4: "此操作将把该简历标识为面试，并通知应聘者，是否继续？",
        5: "此操作将把该简历标识为录用，并通知应聘者，是否继续？",
        6: "此操作将把该简历标识为不合适，并通知应聘者，是否继续？"
      }
    };
  },
  watch: {
    $route: "getList"
  },
  created() {
    this.initData();
  },
  methods: {
    initData() {
      listByType(1).then(response => (this.degreeOptions = response.data.list));
      getUserJobOptions().then(response => (this.jobOptions = response.data));
      this.getList();
    },
    handleSelectMenu(index) {
      this.activeIndex = index;
      this.$nextTick(() => {
        this.$router.push({
          path: "/manage-resume",
          query: { statuses: index }
        });
      });
    },
    getList() {
      if (this.$route.query.statuses) {
        this.activeIndex = this.$route.query.statuses;
      }
      parseListQuery(this.$route.query, this.listQuery);
      if(this.$route.query.jobIds){
        // long 类型当做string处理，浏览器会丢失精度
        this.listQuery.jobIds = this.$route.query.jobIds.split(',');
      }
      applyResumeList(this.listQuery).then(response => {
        this.pageResult = response.data;
        this.total = this.pageResult.total;
      });
    },
    handleListPageRoute() {
      this.$router.push({
        path: this.$route.path,
        query: formatListQuery(this.listQuery)
      });
    },
    handleFilter() {
      this.listQuery.page = 1;
      this.handleListPageRoute();
    },
    handleApplyResume(status, id) {
      const message = this.statusMessageMap[status];
      this.$confirm(message, "提示", {
        confirmButtonText: "继续",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        handleApplyResume({ id: id, status: status }).then(() => {
          this.$message("操作成功");
          this.getList();
        });
      });
    },
    handleShowResume(applyResume) {
      this.reviewDrawerVisible = true;
      this.activeApplyResume = applyResume;
      if (applyResume.applyStatus === 1) {
        handleApplyResume({ id: applyResume.id, status: 2 }).then(() => {
          applyResume.applyStatus = 2;
        });
      }
    },
    onDownloadResumeClick(applyResume) {
      this.resumeExporting = true;
      let fileName = `${applyResume.job.name}_${applyResume.resume.name}`;
      if (applyResume.resume.maxResumeEdu) {
        fileName += `_${applyResume.resume.maxResumeEdu.schoolName}_${applyResume.resume.maxResumeEdu.majorName}`;
      }
      exportResumeToPdf(applyResume.resume.id)
        .then(response => {
          downloadFile({
            fileKey: response.data,
            fileName: `${fileName}.pdf`,
            success: () => {
              this.resumeExporting = false;
            }
          });
        })
        .catch(() => {
          this.resumeExporting = false;
        });
    }
  }
};
</script>

<style lang="scss" scoped>
.app-container {
  margin: 0 auto;
  width: 1000px;
}
.text-small {
  font-size: 14px;
}
.close-text {
  font-size: 30px;
}
.resume-drawer {
  right: 0px;
  height: 100%;
  width: 50%;
  top: 0px;
  bottom: 0px;
  position: fixed;
  box-sizing: border-box;
  background-color: rgb(255, 255, 255);
  display: flex;
  flex-direction: column;
  box-shadow: rgba(0, 0, 0, 0.2) 0px 8px 10px -5px,
    rgba(0, 0, 0, 0.14) 0px 16px 24px 2px, rgba(0, 0, 0, 0.12) 0px 6px 30px 5px;
}
.resume-drawer-header {
  position: absolute;
  width: 100%;
  z-index: 1;
  background: #fff;
  height: 80px;
  border-bottom: 1px solid #eee;
}
.resume-drawer-body {
  position: absolute;
  top: 0;
  bottom: 0;
  padding-top: 80px;
  overflow: auto;
}
</style>
