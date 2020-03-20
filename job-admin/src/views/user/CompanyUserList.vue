<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :inline="true" :model="listQuery">
        <el-form-item>
          <el-input v-model="listQuery.id" placeholder="用户ID" @keyup.enter.native="handleFilter"></el-input>
        </el-form-item>
        <el-form-item>
          <el-input v-model="listQuery.name" placeholder="用户名" @keyup.enter.native="handleFilter"></el-input>
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="listQuery.email"
            placeholder="email"
            @keyup.enter.native="handleFilter"
          ></el-input>
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
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            value-format="yyyy-MM-dd"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            @change="handleFilter"
          ></el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleFilter">查询</el-button>
          <el-button type="success" @click="exportAsExcel" :loading="exporting">导出Excel</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-menu :default-active="listQuery.status" mode="horizontal" @select="handleSelectMenu">
      <el-menu-item index="0">待审核</el-menu-item>
      <el-menu-item index="1">正常</el-menu-item>
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
      <el-table-column label="用户ID" prop="userId" width="180">
        <template slot-scope="{row}">
          <el-button type="text" @click="showUserVerfication(row)">{{ row.userId }}</el-button>
        </template>
      </el-table-column>
      <el-table-column label="用户名" prop="name"></el-table-column>
      <el-table-column label="所在企业" prop="company.name"></el-table-column>
      <el-table-column label="职位" prop="post"></el-table-column>
      <el-table-column label="性别" prop="gender">
        <template slot-scope="{row}">{{userGender[row.gender?row.gender:0]}}</template>
      </el-table-column>
      <el-table-column label="email" prop="email" width="180"></el-table-column>
      <el-table-column label="状态" prop="status">
        <template slot-scope="{row}">
          <el-tag>{{userStatus[row.status]}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="注册时间" prop="createTime"></el-table-column>
      <el-table-column label="操作" align="center" fixed="right" width="200">
        <template slot-scope="{row}">
          <span v-if="row.status !== 0">
            <el-button
              type="danger"
              size="mini"
              icon="el-icon-error"
              v-if="row.status !== 100"
              @click="handleModifyStatus(row, 100)"
            >禁用</el-button>
            <el-button
              type="success"
              size="mini"
              icon="el-icon-refresh-left"
              @click="handleModifyStatus(row, 1)"
              v-else
            >解禁</el-button>
          </span>
          <span v-else>
            <el-button
              type="success"
              size="mini"
              icon="el-icon-check"
              @click="handleVerifyUser(row, 2)"
            >通过</el-button>
            <el-button
              type="warning"
              size="mini"
              icon="el-icon-close"
              @click="handleVerifyUser(row, 3)"
            >拒绝</el-button>
          </span>
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

    <el-drawer title="用户审核资料" :visible.sync="verifyDrawerVisible" direction="rtl" size="50%">
      <div
        class="drawer__footer"
        style="padding: 12px"
        v-if="verifyUser && verifyUser.status === 0"
      >
        <el-button type="success" icon="el-icon-check" @click="handleVerifyUser(verifyUser, 2)">通过</el-button>
        <el-button type="warning" icon="el-icon-close" @click="handleVerifyUser(verifyUser, 3)">拒绝</el-button>
      </div>
      <VerificationView :userId="verifyUser?verifyUser.userId : undefined"></VerificationView>
    </el-drawer>
  </div>
</template>

<script>
import waves from "@/directive/waves"; // waves directive
import Pagination from "@/components/Pagination"; // secondary package based on el-pagination
import VerificationView from "./VerificationView";
import { getCompanyUserList, modifyUserStatus } from "@/api/user_api";
import { verifyCompanyUser } from "@/api/verify_api";
import { serachByCompanyName } from "@/api/company_api";
import { exportUserList } from '@/api/export_api'
import Toast from '@/utils/toast'

export default {
  name: "UserList",
  components: { Pagination, VerificationView },
  directives: { waves },
  data() {
    return {
      tableKey: 0,
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        id: undefined,
        name: undefined,
        email: undefined,
        companyId: undefined,
        type: 2,
        status: "0",
        page: 1,
        limit: 20,
        beginTime: undefined,
        endTime: undefined
      },
      dateRange: undefined,
      statusForm: {
        userId: undefined,
        status: undefined,
        reason: undefined
      },
      userStatus: {
        0: "未激活",
        1: "正常",
        100: "禁用"
      },
      userGender: {
        0: "未知",
        1: "男",
        2: "女"
      },
      loadingCompanyOptions: false,
      companyOptions: [],
      verifyDrawerVisible: false,
      verifyUser: undefined,
      exporting: false
    };
  },
  created() {
    this.getList();
  },
  watch: {
    $route() {
      this.getList();
    },
    dateRange() {
      if (this.dateRange && this.dateRange.length == 2) {
        this.listQuery.beginTime = this.dateRange[0];
        this.listQuery.endTime = this.dateRange[1];
      } else {
        this.listQuery.beginTime = undefined;
        this.listQuery.endTime = undefined;
      }
    }
  },
  methods: {
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
      getCompanyUserList(this.listQuery).then(response => {
        const { total, list } = response.data;
        this.list = list;
        this.total = total;
        this.listLoading = false;
      });
    },
    handleSelectMenu(index) {
      this.listQuery.status = index;
      this.$router.push({ path: this.$route.path, query: { status: index } });
    },
    handleRouteList() {
      this.$router.push({ path: this.$route.path, query: this.listQuery });
    },
    handleFilter() {
      this.listQuery.page = 1;
      this.handleRouteList();
    },
    handleModifyStatus(user, status) {
      this.statusForm.userId = user.userId;
      this.statusForm.status = status;
      if (status === 1) {
        this.$confirm("此操作将解禁该用户, 是否继续?", "提示", {
          confirmButtonText: "继续",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          modifyUserStatus(this.statusForm).then(response => {
            user.status = status;
            this.$message("操作成功");
          });
        });
      } else {
        this.$prompt("请输入禁用的原因", "提示", {
          confirmButtonText: "提交",
          cancelButtonText: "取消"
        }).then(({ value }) => {
          this.statusForm.reason = value;
          modifyUserStatus(this.statusForm).then(response => {
            user.status = status;
            this.$message("操作成功");
          });
        });
      }
    },
    handleVerifyUser(user, status) {
      this.verifyDrawerVisible = false;
      this.statusForm.userId = user.userId;
      this.statusForm.status = status;
      if (status === 2) {
        this.$confirm("此操作将通过审核, 是否继续?", "提示", {
          confirmButtonText: "继续",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          verifyCompanyUser(this.statusForm).then(response => {
            this.getList();
            this.$message("操作成功");
          });
        });
      } else {
        this.$prompt("请填写拒绝通过的理由", "提示", {
          confirmButtonText: "提交",
          cancelButtonText: "取消"
        }).then(({ value }) => {
          this.statusForm.reason = value;
          verifyCompanyUser(this.statusForm).then(response => {
            this.getList();
            this.$message("操作成功");
          });
        });
      }
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
    showUserVerfication(user) {
      this.verifyDrawerVisible = true;
      this.verifyUser = user;
    },
    exportAsExcel(){
      this.exporting = true;
      exportUserList(this.listQuery).then(response=>{
        Toast.success('已加入下载队列，请稍后到【下载管理】进行下载');
      }).finally(()=>this.exporting= false)
    }
  }
};
</script>
