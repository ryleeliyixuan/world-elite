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
      <el-table-column label="用户ID" prop="userId" width="180"></el-table-column>
      <el-table-column label="用户名" prop="name"></el-table-column>
      <el-table-column label="性别" prop="gender">
        <template slot-scope="{row}">{{userGender[row.gender]}}</template>
      </el-table-column>
      <el-table-column label="email" prop="email"></el-table-column>
      <el-table-column label="号码" prop="phone">
        <template slot-scope="{row}">
          <span v-if="row.phoneCode">({{row.phoneCode}})</span>
          {{row.phone}}
        </template>
      </el-table-column>
      <el-table-column label="状态" prop="status">
        <template slot-scope="{row}">
          <el-tag>{{userStatus[row.status]}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="注册时间" prop="createTime"></el-table-column>
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
        fixed="right"
      >
        <template slot-scope="{row}">
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
            icon="el-icon-circle-check"
            @click="handleModifyStatus(row, 1)"
            v-else
          >解禁</el-button>
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
  </div>
</template>

<script>
import waves from "@/directive/waves"; // waves directive
import Pagination from "@/components/Pagination"; // secondary package based on el-pagination
import { getUserList, modifyUserStatus } from "@/api/user_api";
import { exportUserList } from '@/api/export_api'
import Toast from '@/utils/toast'

export default {
  name: "UserList",
  components: { Pagination },
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
        type: 1,
        status: undefined,
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
      if (query.page) {
        this.listQuery.page = parseInt(query.page);
      }
      if (query.limit) {
        this.listQuery.limit = parseInt(query.limit);
      }
      getUserList(this.listQuery).then(response => {
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
      this.getList();
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
    exportAsExcel(){
      this.exporting = true;
      exportUserList(this.listQuery).then(response=>{
        Toast.success('已加入下载队列，请稍后到【下载管理】进行下载');
      }).finally(()=>this.exporting= false)
    }
  }
};
</script>
