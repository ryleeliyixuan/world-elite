<template>
  <div class="app-container">
    <el-menu :default-active="activeIndex" mode="horizontal" @select="handleSelectMenu">
      <el-menu-item index="online">在线</el-menu-item>
      <el-menu-item index="offline">已下线</el-menu-item>
    </el-menu>
    <div class="d-flex mt-4">
      <div class="p-2 flex-grow-1">
        <el-input placeholder="搜索职位名称" v-model="listQuery.name" class="w-50">
          <i slot="prefix" class="el-input__icon el-icon-search"></i>
        </el-input>
      </div>
      <div class="p-2">
        <el-button type="primary" icon="el-icon-edit" @click="handleNewJobClick">发布新职位</el-button>
      </div>
    </div>
    <div class="job-list">
      <el-card class="job-item mt-4" shadow="hover" v-for="job in pageResult.list" :key="job.id">
        <el-row :gutter="10" align="middle" type="flex">
          <el-col class="mr-auto">
            <h5>{{job.name}}</h5>
            <div
              class="text-gray pt-2 pb-2"
            >{{job.city.name}} / {{job.minDegree.name}} / {{job.minSalary}}-{{job.maxSalary}}K</div>
            <div class="text-small">发布时间：{{job.time}}</div>
          </el-col>
          <el-col :span="3" class="text-center">
            <el-link type="primary" :underline="false">
              <div class="text-center text-large">100</div>
              <div class="text-center">新简历</div>
            </el-link>
          </el-col>
          <el-col :span="3" class="text-center">
            <el-link type="primary" :underline="false">
              <div class="text-center text-large">100</div>
              <div class="text-center">候选</div>
            </el-link>
          </el-col>
          <el-col :span="4" class="text-center">
            <el-link type="info" :underline="false" class="p-2" @click="handleEditJob(job.id)">编辑</el-link>
            <el-link
              type="danger"
              :underline="false"
              class="p-2"
              @click="handleTakeOffJob(job.id)"
            >关闭</el-link>
          </el-col>
        </el-row>
      </el-card>
    </div>
    <pagination
      v-show="total"
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.limit"
      @pagination="getList"
    />
  </div>
</template>

<script>
import { getManageJobList, takeOffJob } from "@/api/job_api";
import Pagination from "@/components/Pagination";

export default {
  components: { Pagination },
  data() {
    return {
      activeIndex: "online",
      listQuery: {
        name: "",
        status: 2,
        page: 1,
        limit: 20
      },
      total: 0,
      pageResult: {}
    };
  },
  watch: {
    $route: "getList"
  },
  created() {
    if (this.$route.query) {
      if (this.$route.query.name) {
        this.listQuery.name = this.$route.query.name;
      }
      if (this.$route.query.status) {
        this.listQuery.status = this.$route.query.status;
        if(this.listQuery.status == 2){
            this.activeIndex = "online";
        }else{
            this.activeIndex = "offline";
        }
      }
      if (this.$route.query.page) {
        this.listQuery.page = this.$route.query.page;
      }
      if (this.$route.query.limit) {
        this.listQuery.limit = this.$route.query.limit;
      }
    }
    this.getList();
  },
  methods: {
    handleNewJobClick() {
      this.$router.push("/edit-job");
    },
    handleSelectMenu(index) {
      this.activeIndex = index;
      this.listQuery.status = index == "online" ? 2 : 3;
      this.$nextTick(() => {
        this.$router.push({ path: "/manage-job", query: this.listQuery });
      });
    },
    getList() {
      getManageJobList(this.listQuery).then(response => {
        this.pageResult = response.data;
        this.total = response.data.total;
      });
    },
    handleTakeOffJob(id) {
      this.$confirm("此操作将关闭该职位, 是否继续?", "提示", {
        confirmButtonText: "确定",
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
    handleEditJob(id) {
      this.$router.push({ path: "/edit-job", query: { "id": id } });
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
  color: $gray-400;
  font-size: 14px;
}
.text-large {
  font-size: 18px;
}
</style>
