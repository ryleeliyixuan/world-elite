<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :inline="true" :model="listQuery">
        <el-form-item>
          <el-input v-model="listQuery.jobId" placeholder="职位ID" @keyup.enter.native="handleFilter"></el-input>
        </el-form-item>
        <el-form-item>
          <el-select
            v-model="listQuery.companyId"
            filterable
            clearable
            remote
            placeholder="所属公司"
            :remote-method="getCompanyOptions"
            :loading="loadingCompanyOptions"
            @change="handleFilter"
          >
            <el-option
              v-for="item in companyOptions"
              :key="item.id"
              :label="item.fullName"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleFilter">查询</el-button>
          <el-button type="success" @click="exportAsExcel" :loading="exporting">导出Excel</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-menu :default-active="listQuery.status" mode="horizontal" @select="handleSelectMenu">
      <el-menu-item index="2">正常</el-menu-item>
      <el-menu-item index="3">下架</el-menu-item>
    </el-menu>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.limit"
      @pagination="handleRouteList"
    />

    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      highlight-current-row
      style="margin-top: 10px"
    >
      <el-table-column label="ID" prop="id" width="180">
          <template slot-scope="{row}">
              <el-link @click.stop="goJobDetail(row.id)" type="primary">{{row.id}}</el-link>
          </template>
      </el-table-column>
      <el-table-column label="职位名称" prop="category.name"></el-table-column>
      <el-table-column label="职位分类" prop="name"></el-table-column>
      <el-table-column label="类型" prop="jobType.name"></el-table-column>
      <el-table-column label="发布者" prop="companyUser.name"></el-table-column>
      <el-table-column label="公司" prop="companyUser.company.name"></el-table-column>
      <el-table-column label="薪资待遇">
        <template slot-scope="{row}">
          {{row.minSalary}} - {{row.maxSalary}}
          <span v-if="row.salaryMonths">×{{row.salaryMonths}}</span>
        </template>
      </el-table-column>
      <el-table-column label="学历要求" prop="minDegree.name"></el-table-column>
      <el-table-column label="城市" prop="city.name"></el-table-column>
      <el-table-column label="发布时间" prop="time"></el-table-column>
      <el-table-column label="状态" prop="status">
        <template slot-scope="{row}">{{statusMap[row.status]}}</template>
      </el-table-column>
      <el-table-column label="投递数" align="center">
        <template slot-scope="{row}">
           <el-link type="primary" @click="showResumeApplyJobs(row.id)">{{row.totalResumeCount}}</el-link>
        </template>
      </el-table-column>
      <el-table-column label="待处理" align="center">
        <template slot-scope="{row}">
           <el-link type="primary" @click="showResumeApplyJobs(row.id, [1,2])">{{row.newResumeCount}}</el-link>
        </template>
      </el-table-column>
      <el-table-column label="候选简历" align="center">
        <template slot-scope="{row}">
           <el-link type="primary" @click="showResumeApplyJobs(row.id, [3])">{{row.candidateResumeCount}}</el-link>
        </template>
      </el-table-column>
      <el-table-column label="进入面试" align="center">
        <template slot-scope="{row}">
           <el-link type="primary" @click="showResumeApplyJobs(row.id, [4])">{{row.interviewResumeCount}}</el-link>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150">
        <template slot-scope="{row}">
          <el-button size="mini" type="danger" @click="handleTakeOffJob" v-if="row.status == 2">下架</el-button>
          <el-button size="mini" type="primary" @click="goJobDetail(row.id)" v-if="row.status == 3">查看</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.limit"
      @pagination="handleRouteList"
    />

    <el-drawer title="投递简历" :visible.sync="applyResumeDrawerVisible" direction="rtl" size="50%">
      <div style="margin: 0 12px">
        <pagination
          v-show="applyResumePage.total>0"
          :total="applyResumePage.total"
          :page.sync="applyResumeForm.page"
          :limit.sync="applyResumeForm.limit"
          @pagination="getJobApplyResumeList"
        />
        <el-table key="applyResumeTable" :data="applyResumePage.list" style="margin-top: 10px">
          <el-table-column label="简历ID" prop="resume.id"></el-table-column>
          <el-table-column label="用户名" prop="resume.name"></el-table-column>
          <el-table-column label="投递时间" prop="time"></el-table-column>
        </el-table>
        <pagination
          v-show="applyResumePage.total>0"
          :total="applyResumePage.total"
          :page.sync="applyResumeForm.page"
          :limit.sync="applyResumeForm.limit"
          @pagination="getJobApplyResumeList"
        />
      </div>
    </el-drawer>
  </div>
</template>

<script>
import waves from "@/directive/waves"; // waves directive
import Pagination from "@/components/Pagination"; // secondary package based on el-pagination
import { getJobList, takeOffJob } from "@/api/job_api";
import { serachByCompanyName } from "@/api/company_api";
import { getApplyResumeList } from '@/api/resume_api'
import { exportJobList } from '@/api/export_api'
import Toast from '@/utils/toast'

export default {
  name: "JobList",
  components: { Pagination },
  directives: { waves },
  data() {
    return {
      tableKey: 0,
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        companyId: undefined,
        jobId: undefined,
        status: "2",
        page: 1,
        limit: 20,
        sort: "-id"
      },
      userGender: {
        0: "未知",
        1: "男",
        2: "女"
      },
      statusMap: {
        1: "草稿",
        2: "在线",
        3: "下架"
      },
      loadingCompanyOptions: false,
      companyOptions: [],
      resumeDrawerVisible: false,
      selectResume: undefined,
      applyResumeDrawerVisible: false,
      applyResumePage:{
        total: 0,
        list:[]
      },
      applyResumeForm:{
        jobIds: undefined,
        statuses: undefined,
        page: 1,
        limit: 20,
        sort: "-id"
      },
      exporting:false
    };
  },
  created() {
    this.initData();
  },
  watch: {
    $route() {
      this.getList();
    }
  },
  methods: {
    initData() {
      this.getList();
    },
    handleSelectMenu(index) {
      this.$router.push({ path: this.$route.path, query: { status: index } });
    },
    getList() {
      this.listLoading = true;
      const query = this.$route.query;
      Object.keys(query).forEach(key => {
        this.listQuery[key] = query[key];
      });
      if (query.page) {
        this.listQuery.page = parseInt(query.page);
      }
      if (query.limit) {
        this.listQuery.limit = parseInt(query.limit);
      }
      getJobList(this.listQuery).then(response => {
        const { total, list } = response.data;
        this.list = list;
        this.total = total;
        this.listLoading = false;
      });
    },
    handleRouteList() {
      this.$router.push({ path: this.$route.path, query: this.listQuery });
    },
    handleFilter() {
      this.listQuery.page = 1;
      this.handleRouteList();
    },
    getCompanyOptions(query) {
      this.loadingCompanyOptions = true;
      if (query != "") {
        serachByCompanyName(query).then(response => {
          this.companyOptions = response.data.list;
          this.loadingCompanyOptions = false;
        });
      }
    },
    handleTakeOffJob(job, reason) {
      if(reason == ''){
          this.$message('原因不能为空');
          return;
      }  
      this.$prompt("请输入下架原因", "提示", {
        confirmButtonText: "提交",
        cancelButtonText: "取消"
      }).then(({ value }) => {
        takeOffJob({id: job.id, reason: value}).then(response => {
          user.status = status;
          this.$message("操作成功");
        });
      });
    },
    goJobDetail(jobId){
        window.open(`${process.env.VUE_APP_WEB_HOST}/job/${jobId}`)
    },
    showResumeApplyJobs(jobId, statues) {
      this.applyResumeDrawerVisible = true;
      this.applyResumeForm.page = 1;
      this.applyResumeForm.jobIds = [jobId];
      this.applyResumeForm.statuses = statues;
      this.getJobApplyResumeList();
    },
    getJobApplyResumeList(){
      getApplyResumeList(this.applyResumeForm).then(response=>{
        this.applyResumePage.total = response.data.total
        this.applyResumePage.list = response.data.list
      })
    },
    exportAsExcel(){
      this.exporting = true;
      exportJobList(this.listQuery)
        .then(response => {
          Toast.success("已加入下载队列，请稍后到【下载管理】进行下载");
        })
        .finally(() => (this.exporting = false));
    }
  }
};
</script>
