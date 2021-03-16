<template>
  <loading :loading="loading">
    <div class="app-container">
      <el-menu
        :default-active="listQuery.status"
        mode="horizontal"
        @select="handleSelectMenu"
      >
        <el-menu-item index="2">在线</el-menu-item>
        <el-menu-item index="3">已下线</el-menu-item>
      </el-menu>
      <div class="d-flex mt-4">
        <div class="p-2 flex-grow-1">
          <el-input
            placeholder="搜索职位名称"
            v-model="listQuery.name"
            class="w-50 mr-2"
            @change.native="handleFilter"
            size="mini"
          >
            <i slot="prefix" class="el-input__icon el-icon-search"></i>
          </el-input>
          <el-select
            v-model="listQuery.companyId"
            placeholder="公司"
            clearable
            filterable
            class="w-45"
            size="small"
            @change="handleFilter"
          >
            <el-option
              v-for="item in companyOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            >
            </el-option>
          </el-select>
        </div>
        <div class="p-2">
          <el-button
            type="primary"
            icon="el-icon-edit"
            @click="handleNewJobClick"
            >发布新职位</el-button
          >
        </div>
      </div>

      <div></div>
      <div class="job-list" v-if="total > 0">
        <el-card
          class="job-item mt-4 link-pointer"
          shadow="hover"
          v-for="job in pageResult.list"
          :key="job.id"
          @click.native="handleJobClick(job)"
        >
          <el-row :gutter="8" align="middle" type="flex">
            <el-col :span="12" class="mr-auto">
              <h5 v-if="job.name && !isOP">{{ job.name }}</h5>
              <h5 v-if="job.name && isOP">
                {{ job.company.name }} - {{ job.name }}
              </h5>
              <div class="text-gray pt-2 pb-2">
                <!-- 薪资 -->
                <!-- 0-5k -> 5k一下 -->
                <span
                  v-if="job.minSalary == 0 && job.maxSalary == 5"
                  style="font-size: 16px"
                >
                  {{ "5K以下" }}
                </span>
                <!-- 0-0k -> 不限 -->
                <span
                  v-else-if="job.minSalary == 0 && job.maxSalary == 0"
                  style="font-size: 16px"
                >
                  {{ "薪酬不限" }}
                </span>
                <!-- 其他数值正常显示 -->
                <!-- 全职为“min-max * number” -->
                <span v-else-if="job.jobType.id == 107">
                  {{
                    job.minSalary +
                    "K-" +
                    job.maxSalary +
                    "K" +
                    " * " +
                    job.salaryMonths
                  }}
                </span>
                <!-- 实习/兼职为“min-max 元/天” -->
                <span v-else>
                  {{ job.minSalary + "-" + job.maxSalary + "元/天" }}
                </span>
                /
                <!-- 经验 -->
                <span v-if="job.experience && job.experience.name"
                  >{{
                    job.experience.name == "不限"
                      ? "经验不限"
                      : job.experience.name
                  }}
                  /
                </span>
                <!-- 工作类型 -->
                <span v-if="job.jobType && job.jobType.name"
                  >{{ job.jobType.name }} /
                </span>
                <!-- 工作地点 -->
                <span v-if="job.city && job.city.name"
                  >{{ job.city.name }} /
                </span>
                <!-- 学历要求 -->
                <span>{{
                  job.minDegree && job.minDegree.name
                    ? job.minDegree.name == "不限"
                      ? "学历不限"
                      : job.minDegree.name
                    : "学历不限"
                }}</span>
              </div>
              <div class="text-small">发布时间：{{ job.time }}</div>
            </el-col>
            <el-col :span="3" class="text-center hover-blue">
              <el-link
                type="primary"
                :underline="false"
                @click.stop="handleResumeListClick(job, '1,2')"
              >
                <div class="text-center text-large">
                  {{ job.newResumeCount }}
                </div>
                <div class="text-center">新简历</div>
              </el-link>
            </el-col>
            <el-col :span="3" class="text-center hover-blue">
              <el-link
                type="primary"
                :underline="false"
                @click.stop="handleResumeListClick(job, '3')"
              >
                <div class="text-center text-large">
                  {{ job.candidateResumeCount }}
                </div>
                <div class="text-center">初筛</div>
              </el-link>
            </el-col>
            <el-col :span="3" class="text-center hover-blue">
              <el-link
                type="primary"
                :underline="false"
                @click.stop="handleResumeListClick(job, '4')"
              >
                <div class="text-center text-large">
                  {{ job.interviewResumeCount }}
                </div>
                <div class="text-center">面试</div>
              </el-link>
            </el-col>
            <el-col :span="10" class="text-center">
              <el-button
                type="primary"
                size="mini"
                class="p-2"
                @click.stop="handleOpenJobDetail(job.id)"
                icon="el-icon-view"
                >查看详情
              </el-button>
              <el-button
                type="primary"
                size="mini"
                class="p-2"
                v-if="job.status == 2"
                @click.stop="handleEditJob(job.id)"
                icon="el-icon-edit"
                >编辑
              </el-button>
              <el-button
                size="mini"
                type="danger"
                class="p-2"
                v-if="job.status == 2"
                @click.stop="handleTakeOffJob(job.id)"
                icon="el-icon-close"
                >关闭
              </el-button>
              <el-button
                type="success"
                size="mini"
                class="p-2"
                v-if="job.status == 3"
                icon="el-icon-refresh-right"
                @click.stop="handleReopenJob(job.id)"
                >重新开放
              </el-button>
              <el-button
                size="mini"
                class="p-2"
                icon="el-icon-close"
                v-if="job.status == 3"
                onfirmButtonText="删除"
                @click.stop="handleDeleteJob(job.id)"
                type="danger"
                >删除
              </el-button>
            </el-col>
          </el-row>
        </el-card>
      </div>
      <div v-else class="text-center" style="line-height: 200px">暂无职位</div>

      <pagination
        v-show="total"
        :total="total"
        :page.sync="listQuery.page"
        :limit.sync="listQuery.limit"
        @pagination="handleRouteList"
      />
      <el-backtop></el-backtop>
    </div>
  </loading>
</template>

<script>
import {
  getManageJobList,
  takeOffJob,
  reopenJob,
  deleteJob,
  getUserJobOptions,
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
        sort: "-id",
      },
      total: 0,
      pageResult: {},
      loading: true,
      isOP: false, // 是否为超级账户
      companyOptions: [], // 公司选项
    };
  },
  watch: {
    $route: "getList",
  },
  created() {
    this.getList();
    this.initData();
  },
  methods: {
    initData() {
      this.$axios
        .request({
          url: "/usercorporate/check-op",
          method: "get",
        })
        .then((data) => {
          this.isOP = data.data;
        });

      getUserJobOptions().then((response) => {
        this.jobOptions = response.data;

        if (this.jobOptions !== null) {
          let companyIdSet = new Set();
          for (let i = 0; i < this.jobOptions.length; i++) {
            let company = this.jobOptions[i].company;
            if (company === null) continue;

            if (!companyIdSet.has(company.id)) {
              companyIdSet.add(company.id);

              let companyOption = {};
              companyOption.id = company.id;
              companyOption.name = company.name;
              this.companyOptions.push(companyOption);
            }
          }
        }
      });
    },
    handleNewJobClick() {
      this.$router.push("/edit-job");
    },
    handleSelectMenu(index) {
      this.$router.push({ path: this.$route.path, query: { status: index } });
    },
    getList() {
      parseListQuery(this.$route.query, this.listQuery);
      getManageJobList(this.listQuery).then((response) => {
        this.pageResult = response.data;
        this.total = response.data.total;
        this.loading = false;
        this.$emit("complete");
      });
    },
    handleTakeOffJob(id) {
      this.$confirm("此操作将关闭该职位, 是否继续?", "提示", {
        confirmButtonText: "继续",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        takeOffJob(id).then(() => {
          this.$message({
            type: "success",
            message: "操作成功!",
          });
          this.getList();
        });
      });
    },
    handleDeleteJob(id) {
      this.$confirm("此操作将删除该职位, 是否继续?", "提示", {
        confirmButtonText: "删除",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        deleteJob({ id: id }).then(() => {
          this.$message({
            type: "success",
            message: "操作成功!",
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
        query: formatListQuery(this.listQuery),
      });
    },
    handleEditJob(id) {
      this.$router.push({ path: "/edit-job", query: { id: id } });
    },
    // 跳转去该职位的简历管理
    handleJobClick(job) {
      window.open(`/manage-resume?jobIds=${job.id}`);
    },
    handleOpenJobDetail(id) {
      window.open(`/job/${id}`);
    },
    handleResumeListClick(job, statuses) {
      this.$router.push({
        path: "/manage-resume",
        query: {
          jobIds: job.id,
          statuses: statuses,
        },
      });
    },
    handleReopenJob(id) {
      this.$confirm("此操作将重新开放该职位, 是否继续?", "提示", {
        confirmButtonText: "继续",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        reopenJob(id).then(() => {
          this.$message({
            type: "success",
            message: "操作成功!",
          });
          this.getList();
        });
      });
    },
  },
};
</script>

<style lang="scss" scoped>
@import "bootstrap/scss/bootstrap.scss";

.app-container {
  margin: 0 auto;
  width: 1000px;
  min-height: calc(100vh - 430px);
}

.hover-blue {
  :hover {
    color: #409eff;
  }
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
