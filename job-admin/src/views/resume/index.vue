<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :inline="true" :model="listQuery">
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
          <el-button type="primary" @click="handleFilter">查询</el-button>
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
      highlight-current-row
      style="margin-top: 10px"
    >
      <el-table-column label="ID" prop="id" width="180">
        <template slot-scope="{row}">
          <el-link @click.stop="handleShowResume(row)" type="primary">{{row.id}}</el-link>
        </template>
      </el-table-column>
      <el-table-column label="名字" prop="name"></el-table-column>
      <el-table-column label="性别" prop="gender">
        <template slot-scope="{row}">{{userGender[row.gender]}}</template>
      </el-table-column>
      <el-table-column label="年龄" prop="age"></el-table-column>
      <el-table-column label="最高学历" width="300">
        <template slot-scope="{row}">
          <span v-if="row.maxResumeEdu">
            {{row.maxResumeEdu.schoolName}}/{{row.maxResumeEdu.majorName}}/{{row.maxResumeEdu.degree.name}}
            <span
              v-if="row.maxResumeEdu.gpa"
            >/{{row.maxResumeEdu.gpa}}</span>
          </span>
        </template>
      </el-table-column>
      <el-table-column label="回国时间" prop="returnTime"></el-table-column>
      <el-table-column label="毕业时间" prop="graduateTime"></el-table-column>
      <el-table-column label="现居城市" prop="curPlace"></el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.limit"
      @pagination="handleRouteList"
    />

    <el-drawer title="简历详情" :visible.sync="resumeDrawerVisible" direction="rtl" size="50%">
      <ResumeView :resumeId="selectResume.id" v-if="selectResume" class="pl-4 pr-4 pb-4"></ResumeView>
    </el-drawer>
  </div>
</template>

<script>
import waves from "@/directive/waves"; // waves directive
import Pagination from "@/components/Pagination"; // secondary package based on el-pagination
import ResumeView from '@/components/ResumeView'
import { getResumeList } from "@/api/resume_api";

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
        page: 1,
        limit: 20,
        sort: "-id"
      },
      userGender: {
        0: "未知",
        1: "男",
        2: "女"
      },
      resumeDrawerVisible: false,
      selectResume: undefined
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
      getResumeList(this.listQuery).then(response => {
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
    handleShowResume(resume) {
      this.resumeDrawerVisible = true;
      this.selectResume = resume;
    }
  }
};
</script>
