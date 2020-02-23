<template>
  <div class="app-container">
    <el-menu :default-active="activeIndex" mode="horizontal" @select="handleSelectMenu">
      <el-menu-item index="1,2">新简历</el-menu-item>
      <el-menu-item index="3">初筛</el-menu-item>
      <el-menu-item index="4">面试</el-menu-item>
      <el-menu-item index="5">录用</el-menu-item>
    </el-menu>
    <div class="mt-3">
      <el-select
        v-model="listQuery.jobIds"
        placeholder="职位"
        multiple
        clearable
        filterable
        size="small"
        @change="handleListPageRoute"
      >
        <el-option v-for="item in jobOptions" :key="item.id" :label="item.name" :value="item.id">
          <span style="float: left">{{ item.name }}</span>
          <span style="float: right; color: #8492a6; font-size: 13px">{{ item.city.name }}</span>
        </el-option>
      </el-select>
      <el-select
        v-model="listQuery.degreeIds"
        placeholder="学历"
        multiple
        clearable
        filterable
        size="small"
        class="ml-2"
        @change="handleListPageRoute"
      >
        <el-option v-for="item in degreeOptions" :key="item.id" :label="item.name" :value="item.id"></el-option>
      </el-select>
    </div>
    <div class="resume-list mt-3">
      <el-card class="box-card mb-3" v-for="applyResume in pageResult.list" :key="applyResume.id">
        <div slot="header" class="clearfix text-small" style="line-height: 40px;">
          <span>应聘岗位：{{applyResume.job.name}}</span>
          <span class="ml-4">应聘时间：{{applyResume.time}}</span>
          <div class="float-right">
            <el-button type="primary" size="small" @click="handleApplyResume(3, applyResume.id)" v-if="applyResume.applyStatus == 1 || applyResume.applyStatus == 2">通过初筛</el-button>
            <el-button type="primary" size="small" @click="handleApplyResume(4, applyResume.id)" v-if="applyResume.applyStatus == 3">进入面试</el-button>
            <el-button type="primary" size="small" @click="handleApplyResume(5, applyResume.id)" v-if="applyResume.applyStatus == 4">已录用</el-button>
            <el-button type="danger" plain size="small" @click="handleApplyResume(6, applyResume.id)" v-if="applyResume.applyStatus != 5">不合适</el-button>
          </div>
        </div>
        <b-media>
          <template v-slot:aside>
            <el-avatar :src="applyResume.resume.avatar"></el-avatar>
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
  </div>
</template>

<script>
import Pagination from "@/components/Pagination";
import { applyResumeList, handleApplyResume } from "@/api/resume_api";
import { listByType } from "@/api/dict_api";
import { getUserJobOptions } from "@/api/job_api";

export default {
  name: "ResumeManagePage",
  components: { Pagination },
  data() {
    return {
      activeIndex: "1,2",
      listQuery: {
        jobIds: [],
        degreeIds: [],
        statuses: [1, 2],
        page: 1,
        limit: 20
      },
      total: 0,
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
      if (this.$route.query) {
        if (this.$route.query.statuses) {
          this.listQuery.statuses = this.$route.query.statuses.split(",");
          this.activeIndex = this.$route.query.statuses;
        }
        if (this.$route.query.jobIds) {
          this.listQuery.jobIds = this.$route.query.jobIds.split(",");
        }
        if (this.$route.query.degreeIds) {
          this.listQuery.degreeIds = this.$route.query.degreeIds
            .split(",")
            .map(id => {
              return parseInt(id);
            });
        }
        if (this.$route.query.page) {
          this.listQuery.page = this.$route.query.page;
        }
        if (this.$route.query.limit) {
          this.listQuery.limit = this.$route.query.limit;
        }
      }
      applyResumeList(this.listQuery).then(response => {
        this.pageResult = response.data;
        this.total = this.pageResult.total;
      });
    },
    handleListPageRoute() {
      this.$nextTick(() => {
        this.$router.push({
          path: "/manage-resume",
          query: {
            jobIds: this.listQuery.jobIds.join(","),
            degreeIds: this.listQuery.degreeIds.join(","),
            statuses: this.listQuery.statuses.join(","),
            page: this.listQuery.page,
            limit: this.listQuery.limit
          }
        });
      });
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
    }
  }
};
</script>

<style lang="scss">
.app-container {
  margin: 0 auto;
  width: 1000px;
}
.text-small {
  font-size: 14px;
}
.text-large {
  font-size: 18px;
}
</style>
