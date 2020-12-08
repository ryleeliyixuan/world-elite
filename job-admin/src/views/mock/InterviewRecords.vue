<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :inline="true" :model="listQuery">
        <el-form-item>
          <el-input v-model="listQuery.orderId" placeholder="订单号ID" @keyup.enter.native="handleFilter"
                    clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-date-picker
            v-model="date"
            type="date"
            value-format="yyyy-MM-dd"
            @change="handleFilter"
            placeholder="选择日期" clearable>
          </el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-input v-model="listQuery.nickName" placeholder="面试官昵称" @keyup.enter.native="handleFilter"
                    clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-input v-model="listQuery.name" placeholder="面试者姓名" @keyup.enter.native="handleFilter"
                    clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleFilter">查询</el-button>
        </el-form-item>
      </el-form>

      <el-button type="success" @click="" icon="el-icon-plus">添加</el-button>
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
      <el-table-column label="面试ID" prop="userId" width="80"></el-table-column>
      <el-table-column label="面试时间" prop="name"></el-table-column>
      <el-table-column label="面试官昵称" prop="gender"></el-table-column>
      <el-table-column label="面试者姓名" prop="email"></el-table-column>
      <el-table-column label="面试者评分与反馈" prop="phone"></el-table-column>
      <el-table-column label="状态" prop="status">
        <template slot-scope="{row}">
          <el-tag>{{userStatus[row.status]}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="订单号ID" prop="orderId"></el-table-column>
      <el-table-column label="付款金额" prop="amount"></el-table-column>
      <el-table-column label="操作" align="center" fixed="right" width="400">
        <template slot-scope="{row}">
          <el-button
            type="primary"
            size="mini"
            icon="el-icon-time"
          >修改面试时间</el-button>
          <el-button
            type="warning"
            size="mini"
            icon="el-icon-edit"
          >修改面试评分</el-button>
          <span style="padding-left:10px" v-if="row.status !== '0'">
            <el-button
              type="danger"
              size="mini"
              icon="el-icon-warning-outline"
            >退费</el-button>
          </span>
          <span v-else>
            <el-button
              type="danger"
              size="mini"
              icon="el-icon-circle-check"
              disabled
            >已退费</el-button>
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
  </div>
</template>

<script>
  import waves from "@/directive/waves"; // waves directive
  import Pagination from "@/components/Pagination"; // secondary package based on el-pagination
  import {getInterviewRecordList} from "@/api/mock_api";

  export default {
    name: "InterviewRecords",
    components: {Pagination},
    directives: {waves},
    data() {
      return {
        tableKey: 0,
        list: [],
        total: 0,
        listLoading: true,
        listQuery: {
          orderId: undefined,
          date: undefined,
          name: undefined,
          nickName: undefined,
          page: 1,
          limit: 20
        },
        date: undefined,
        userStatus: {
          0: "未激活",
          1: "正常",
          100: "禁用"
        },
      };
    },
    created() {
      this.getList();
    },
    watch: {
      $route() {
        this.getList();
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
        getInterviewRecordList(this.listQuery).then(response => {
          const {total, list} = response.data;
          this.list = list;
          this.total = total;
          this.listLoading = false;
        });
      },
      handleRouteList() {
        this.$router.push({path: this.$route.path, query: this.listQuery});
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
            modifyApplicantStatus(this.statusForm).then(response => {
              user.status = status;
              this.$message("操作成功");
            });
          });
        } else {
          this.$prompt("请输入禁用的原因", "提示", {
            confirmButtonText: "提交",
            cancelButtonText: "取消"
          }).then(({value}) => {
            this.statusForm.reason = value;
            modifyApplicantStatus(this.statusForm).then(response => {
              user.status = status;
              this.$message("操作成功");
            });
          });
        }
      },
    }
  };
</script>
