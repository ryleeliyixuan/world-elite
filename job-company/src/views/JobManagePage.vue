<template>
  <div class="app-container">
    <el-menu :default-active="listQuery.status" mode="horizontal" @select="handleSelectMenu">
      <el-menu-item index="2">在线</el-menu-item>
      <el-menu-item index="3">已下线</el-menu-item>
    </el-menu>
    <div class="d-flex mt-4">
      <div class="p-2 flex-grow-1">
        <el-input
          placeholder="搜索职位名称"
          v-model="listQuery.name"
          class="w-50"
          @change.native="handleFilter"
        >
          <i slot="prefix" class="el-input__icon el-icon-search"></i>
        </el-input>
      </div>
      <div class="p-2">
        <el-button type="primary" icon="el-icon-edit" @click="handleNewJobClick">发布新职位</el-button>
      </div>
    </div>
    <div class="job-list">
      <el-card
        class="job-item mt-4 link-pointer"
        shadow="hover"
        v-for="job in pageResult.list"
        :key="job.id"
        @click.native="handleJobClick(job)"
      >
        <el-row :gutter="10" align="middle" type="flex">
          <el-col class="mr-auto">
            <h5>{{job.name}}</h5>
            <div
              class="text-gray pt-2 pb-2"
            >{{job.city?job.city.name:''}} / {{job.minDegree?job.minDegree.name:'不限学历'}} / {{job.minSalary}}-{{job.maxSalary}}K</div>
            <div class="text-small">发布时间：{{job.time}}</div>
          </el-col>
          <el-col :span="3" class="text-center">
            <el-link
              type="primary"
              :underline="false"
              @click.stop="handleResumeListClick(job, '1,2')"
            >
              <div class="text-center text-large">{{job.newResumeCount}}</div>
              <div class="text-center">新简历</div>
            </el-link>
          </el-col>
          <el-col :span="3" class="text-center">
            <el-link
              type="primary"
              :underline="false"
              @click.stop="handleResumeListClick(job, '3')"
            >
              <div class="text-center text-large">{{job.candidateResumeCount}}</div>
              <div class="text-center">初筛</div>
            </el-link>
          </el-col>
          <el-col :span="3" class="text-center">
            <el-link
              type="primary"
              :underline="false"
              @click.stop="handleResumeListClick(job, '4')"
            >
              <div class="text-center text-large">{{job.interviewResumeCount}}</div>
              <div class="text-center">面试</div>
            </el-link>
          </el-col>
          <el-col :span="8" class="text-center">
            <el-button
              type="primary"
              size="mini"
              class="p-2"
              v-if="job.status == 2"
              @click.stop="handleEditJob(job.id)"
              icon="el-icon-edit"
            >编辑</el-button>
            <el-button
              size="mini"
              type="danger"
              class="p-2"
              v-if="job.status == 2"
              @click.stop="handleTakeOffJob(job.id)"
              icon="el-icon-close"
            >关闭</el-button>
            <el-button
              type="success"
              size="mini"
              class="p-2"
              v-if="job.status == 3"
              icon="el-icon-refresh-right"
              @click.stop="handleReopenJob(job.id)"
            >重新开放</el-button>
            <el-button
              size="mini"
              class="p-2"
              icon="el-icon-close"
              v-if="job.status == 3"
              onfirmButtonText="删除"
              @click.stop="handleReopenJob(job.id)"
              type="danger"
            >删除</el-button>
          </el-col>
        </el-row>
      </el-card>
    </div>
    <pagination
      v-show="total"
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.limit"
      @pagination="handleRouteList"
    />
    <el-backtop></el-backtop>
  </div>
</template>

<script>
import {
  getManageJobList,
  takeOffJob,
  reopenJob,
  deleteJob
} from "@/api/job_api";
import Pagination from "@/components/Pagination";
import { formatListQuery, parseListQuery } from "@/utils/common";

export default {
  components: { Pagination },
  data() {
    return {
      listQuery: {
        name: "",
        status: "2",
        page: 1,
        limit: 20,
        sort: "-id"
      },
      total: 0,
      pageResult: {}
    };
  },
  watch: {
    $route: "getList"
  },
  created() {
    this.getList();
  },
  methods: {
    handleNewJobClick() {
      this.$router.push("/edit-job");
    },
    handleSelectMenu(index) {
      this.$router.push({ path: this.$route.path, query: { status: index } });
    },
    getList() {
      parseListQuery(this.$route.query, this.listQuery);
      getManageJobList(this.listQuery).then(response => {
        this.pageResult = response.data;
        this.total = response.data.total;
      });
    },
    handleTakeOffJob(id) {
      this.$confirm("此操作将关闭该职位, 是否继续?", "提示", {
        confirmButtonText: "继续",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        takeOffJob(id).then(() => {
          this.$message({
            type: "success",
            message: "操作成功!"
          });
          this.getList();
        });
      });
    },
    handleDeleteJob(job) {
      this.$confirm("此操作将删除该职位, 是否继续?", "提示", {
        confirmButtonText: "删除",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        deleteJob({ id: job.id }).then(() => {
          this.$message({
            type: "success",
            message: "操作成功!"
          });
          this.getList();
        });
      });
    },
    handleFilter() {
      this.listQuery.page = 1;
      this.handleRouteList();
    },
    handleRouteList() {
      this.$router.push({
        path: this.$route.path,
        query: formatListQuery(this.listQuery)
      });
    },
    handleEditJob(id) {
      this.$router.push({ path: "/edit-job", query: { id: id } });
    },
    handleJobClick(job) {
      window.open(`${process.env.VUE_APP_WEB_HOST}/job/${job.id}`);
    },
    handleResumeListClick(job, statuses) {
      this.$router.push({
        path: "/manage-resume",
        query: {
          jobIds: job.id,
          statuses: statuses
        }
      });
    },
    handleReopenJob(id) {
      this.$confirm("此操作将重新开放该职位, 是否继续?", "提示", {
        confirmButtonText: "继续",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        reopenJob(id).then(() => {
          this.$message({
            type: "success",
            message: "操作成功!"
          });
          this.getList();
        });
      });
    }
  }
};
</script>

<style lang="scss">
@import "bootstrap/scss/bootstrap.scss";

.app-container {
  margin: 0 auto;
  width: 1000px;
}
.text-gray {
  color: $gray-600;
  font-size: 15px;
}
.text-small {
  font-size: 14px;
}
.text-large {
  font-size: 18px;
}
</style>
