<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :inline="true" :model="listQuery" size="small">
        <el-form-item>
          <el-input
            v-model="listQuery.userId"
            placeholder="用户ID"
            @keyup.enter.native="handleFilter"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-input v-model="listQuery.name" placeholder="名字" @keyup.enter.native="handleFilter"></el-input>
        </el-form-item>
        <el-form-item>
          <el-select v-model="listQuery.gender" clearable placeholder="性别" @change="handleFilter">
            <el-option
              v-for="item in genderOptions"
              :key="item.value"
              :label="item.name"
              :value="item.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-select
            v-model="listQuery.degreeIds"
            clearable
            multiple
            placeholder="学历"
            @change="handleFilter"
          >
            <el-option
              v-for="item in degreeOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-select
            v-model="listQuery.schoolIds"
            multiple
            filterable
            remote
            reserve-keyword
            :loading="loadingSchoolOptions"
            :remote-method="seachSchoolOptions"
            placeholder="学校"
            @change="handleFilter"
          >
            <el-option
              v-for="item in schoolOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-cascader
            placeholder="意向职位"
            :show-all-levels="false"
            :options="jobCategoryOptions"
            :props="jobCatetoryProps"
            filterable
            clearable
            v-model="listQuery.categoryIds"
            @change="handleFilter"
          ></el-cascader>
        </el-form-item>
        <el-form-item>
          <el-select
            v-model="listQuery.cityIds"
            multiple
            filterable
            placeholder="意向城市"
            @change="handleFilter"
          >
            <el-option
              v-for="item in cityOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-select
            v-model="listQuery.salaryRangeId"
            filterable
            clearable
            placeholder="薪资范围"
            @change="handleFilter"
          >
            <el-option
              v-for="item in salaryRangeOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-select
            v-model="listQuery.gpaRangeId"
            filterable
            clearable
            placeholder="GPA范围"
            @change="handleFilter"
          >
            <el-option
              v-for="item in gpaRangeOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleFilter">查询</el-button>
          <el-button type="success" @click="exportAsExcel">导出excel</el-button>
        </el-form-item>
      </el-form>
    </div>

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
      size="mini"
      highlight-current-row
      style="margin-top: 10px"
    >
      <el-table-column label="ID" prop="id" width="180" fixed>
        <template slot-scope="{row}">
          <el-link @click.stop="handleShowResume(row)" type="primary">{{row.id}}</el-link>
        </template>
      </el-table-column>
      <el-table-column label="名字" prop="name"></el-table-column>
      <el-table-column label="性别" prop="gender">
        <template slot-scope="{row}">{{userGender[row.gender]}}</template>
      </el-table-column>
      <el-table-column label="年龄" prop="age"></el-table-column>
      <el-table-column label="学校" prop="maxResumeEdu.schoolName"></el-table-column>
      <el-table-column label="专业" prop="maxResumeEdu.majorName"></el-table-column>
      <el-table-column label="学历" prop="maxResumeEdu.degree.name"></el-table-column>
      <el-table-column label="GPA" prop="maxResumeEdu.gpa"></el-table-column>
      <el-table-column label="回国时间" prop="returnTime"></el-table-column>
      <el-table-column label="毕业时间" prop="graduateTime"></el-table-column>
      <el-table-column label="现居城市" prop="curPlace"></el-table-column>
      <el-table-column label="意向岗位">
        <template slot-scope="{row}">
          <div v-if="row.userExpectJob && row.userExpectJob.categoryList">
            <span
              v-for="(category, index) in row.userExpectJob.categoryList"
              :key="category.id"
            >{{category.name}} {{(index + 1) !== row.userExpectJob.categoryList.length?' / ':''}}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="意向城市">
        <template slot-scope="{row}">
          <div v-if="row.userExpectJob && row.userExpectJob.cityList">
            <span
              v-for="(city, index) in row.userExpectJob.cityList"
              :key="city.id"
            >{{city.name}} {{(index + 1) !== row.userExpectJob.cityList.length?' / ':''}}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="期望薪资">
        <template slot-scope="{row}">
          <div
            v-if="row.userExpectJob && row.userExpectJob.minSalary"
          >{{`${row.userExpectJob.minSalary}K-${row.userExpectJob.maxSalary}K`}}</div>
        </template>
      </el-table-column>
      <el-table-column label="邮箱" prop="email"></el-table-column>
      <el-table-column label="投递总数" prop="applyTotalCount" align="center">
        <template slot-scope="{row}">
          <el-link type="primary" @click="showResumeApplyJobs(row.id)">{{row.applyTotalCount}}</el-link>
        </template>
      </el-table-column>
      <el-table-column label="投递中" prop="applyingCount" align="center">
        <template slot-scope="{row}">
          <el-link type="primary" @click="showResumeApplyJobs(row.id, 1)">{{row.applyingCount}}</el-link>
        </template>
      </el-table-column>
      <el-table-column label="进入初筛" prop="applyCandidateCount" align="center">
        <template slot-scope="{row}">
          <el-link
            type="primary"
            @click="showResumeApplyJobs(row.id, 3)"
          >{{row.applyCandidateCount}}</el-link>
        </template>
      </el-table-column>
      <el-table-column label="进入面试" prop="applyInterviewCount" align="center">
        <template slot-scope="{row}">
          <el-link
            type="primary"
            @click="showResumeApplyJobs(row.id, 4)"
          >{{row.applyInterviewCount}}</el-link>
        </template>
      </el-table-column>
      <el-table-column label="已录用" prop="applyOfferCount" align="center">
        <template slot-scope="{row}">
          <el-link type="primary" @click="showResumeApplyJobs(row.id, 5)">{{row.applyOfferCount}}</el-link>
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

    <el-drawer title="简历详情" :visible.sync="resumeDrawerVisible" direction="rtl" size="50%">
      <div class="pl-4 pr-4">
        <el-button
              type="primary"
              size="small"
              :loading="resumeExporting"
              icon="el-icon-download"
              @click="onDownloadResumeClick(selectResume)"
       >下载简历</el-button>
      <ResumeView :resumeId="selectResume.id" v-if="selectResume" class="pb-4 mt-2"></ResumeView>
      </div>
    </el-drawer>

    <el-drawer title="投递职位" :visible.sync="applyJobDrawerVisible" direction="rtl" size="50%">
      <div style="margin: 0 12px">
        <pagination
          v-show="applyJobsPage.total>0"
          :total="applyJobsPage.total"
          :page.sync="applyJobsForm.page"
          :limit.sync="applyJobsForm.limit"
          @pagination="getResumeApplyJobList"
        />
        <el-table key="applyJobTable" :data="applyJobsPage.list" style="margin-top: 10px">
          <el-table-column label="公司" prop="company.name"></el-table-column>
          <el-table-column label="职位">
            <template slot-scope="{row}">
              <el-link
                type="primary"
                :href="getJobDetailUrl(row.id)"
                target="_blank"
              >{{row.name}}</el-link>
            </template>
          </el-table-column>
          <el-table-column label="投递时间" prop="applyTime"></el-table-column>
        </el-table>
        <pagination
          v-show="applyJobsPage.total>0"
          :total="applyJobsPage.total"
          :page.sync="applyJobsForm.page"
          :limit.sync="applyJobsForm.limit"
          @pagination="getResumeApplyJobList"
        />
      </div>
    </el-drawer>
  </div>
</template>

<script>
import waves from "@/directive/waves"; // waves directive
import Pagination from "@/components/Pagination"; // secondary package based on el-pagination
import ResumeView from "@/components/ResumeView";
import { getResumeList } from "@/api/resume_api";
import { listByType } from "@/api/dict_api";
import { searchSchool } from "@/api/school_api";
import { getCategoryTree } from "@/api/category_api";
import { getResumeApplyJobs } from "@/api/job_api";
import { downloadFile, formatListQuery, parseListQuery } from "@/utils/common";
import { exportResumeList, exportResumeToPdf } from "@/api/export_api";

import Toast from "@/utils/toast";

export default {
  name: "ResumeList",
  components: { Pagination, ResumeView },
  directives: { waves },
  data() {
    return {
      tableKey: 0,
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        name: undefined,
        userId: undefined,
        gender: undefined,
        degreeIds: undefined,
        schoolIds: undefined,
        categoryIds: undefined,
        cityIds: undefined,
        salaryRangeId: undefined,
        gpaRangeId: undefined,
        page: 1,
        limit: 20,
        sort: "-id"
      },
      userGender: {
        1: "男",
        2: "女"
      },
      genderOptions: [
        { name: "男", value: 1 },
        { name: "女", value: 2 }
      ],
      degreeOptions: [],
      schoolOptions: [],
      jobCategoryOptions: [],
      cityOptions: [],
      salaryRangeOptions: [],
      gpaRangeOptions: [],
      loadingSchoolOptions: false,
      resumeDrawerVisible: false,
      applyJobDrawerVisible: false,
      applyJobsForm: {
        resumeId: undefined,
        status: undefined,
        page: 1,
        limit: 20
      },
      applyJobsPage: {
        list: [],
        total: 0
      },
      selectResume: undefined,
      jobCatetoryProps: {
        multiple: true,
        expandTrigger: "hover",
        value: "id",
        label: "name",
        emitPath: false,
        children: "children"
      },
      exporting: false,
      resumeExporting: false
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
      listByType(1).then(response => (this.degreeOptions = response.data.list));
      listByType(2).then(response => (this.cityOptions = response.data.list));
      listByType(9).then(
        response => (this.salaryRangeOptions = response.data.list)
      );
      listByType(10).then(
        response => (this.gpaRangeOptions = response.data.list)
      );
      getCategoryTree().then(
        response => (this.jobCategoryOptions = response.data)
      );
    },
    getList() {
      this.listLoading = true;
      const query = this.$route.query;
      parseListQuery(query, this.listQuery);
      if (query.gender) {
        this.listQuery.gender = parseInt(query.gender);
      }
      if (query.gpaRangeId) {
        this.listQuery.gpaRangeId = parseInt(query.gpaRangeId);
      }
      if (query.salaryRangeId) {
        this.listQuery.salaryRangeId = parseInt(query.salaryRangeId);
      }
      getResumeList(this.listQuery).then(response => {
        const { total, list } = response.data;
        this.list = list;
        this.total = total;
        this.listLoading = false;
      });
    },
    handleRouteList() {
      this.$router.push({
        path: this.$route.path,
        query: formatListQuery(this.listQuery)
      });
    },
    handleFilter() {
      this.listQuery.page = 1;
      this.handleRouteList();
    },
    handleShowResume(resume) {
      this.resumeDrawerVisible = true;
      this.selectResume = resume;
    },
    seachSchoolOptions(query) {
      if (query != "") {
        this.loadingSchoolOptions = true;
        searchSchool(query)
          .then(response => (this.schoolOptions = response.data))
          .finally(() => (this.loadingSchoolOptions = false));
      } else {
        this.schoolOptions = [];
      }
    },
    exportAsExcel() {
      this.exporting = true;
      exportResumeList(this.listQuery)
        .then(response => {
          Toast.success("已加入下载队列，请稍后到【下载管理】进行下载");
        })
        .finally(() => (this.exporting = false));
    },
    showResumeApplyJobs(resumeId, applyStatus) {
      this.applyJobDrawerVisible = true;
      this.applyJobsForm.page = 1;
      this.applyJobsForm.resumeId = resumeId;
      this.applyJobsForm.status = applyStatus;
      this.getResumeApplyJobList();
    },
    getResumeApplyJobList() {
      getResumeApplyJobs(this.applyJobsForm).then(response => {
        this.applyJobsPage.list = response.data.list;
        this.applyJobsPage.total = response.data.total;
      });
    },
    getJobDetailUrl(jobId){
        return `${process.env.VUE_APP_WEB_HOST}/job/${jobId}`
    },
    onDownloadResumeClick(resume) {
      this.resumeExporting = true;
      let fileName = `${resume.name}`;
      if (resume.maxResumeEdu) {
        fileName += `_${resume.maxResumeEdu.schoolName}_${resume.maxResumeEdu.majorName}`;
      }
      exportResumeToPdf(resume.id)
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
