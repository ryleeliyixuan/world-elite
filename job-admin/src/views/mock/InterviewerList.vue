<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :inline="true" :model="listQuery">
        <el-form-item>
          <el-input v-model="listQuery.id" placeholder="用户ID" @keyup.enter.native="handleFilter" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-input v-model="listQuery.nickName" placeholder="用户名" @keyup.enter.native="handleFilter"
                    clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-input v-model="listQuery.email" placeholder="Email" @keyup.enter.native="handleFilter"
                    clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleFilter">查询</el-button>
        </el-form-item>
      </el-form>

      <el-button type="success" @click="addInterviewer()" icon="el-icon-plus">添加面试官</el-button>
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
      <el-table-column label="用户ID" prop="id" width="180">
        <template slot-scope="{row}">
          <el-button type="text" @click="showUserVerification(row)">{{ row.id }}</el-button>
        </template>
      </el-table-column>
      <el-table-column label="用户名" prop="nickName"></el-table-column>
      <el-table-column label="状态" prop="status">
        <template slot-scope="{row}">
          <el-tag>{{userStatus[row.status]}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="从事行业" prop="industry.name"></el-table-column>
      <el-table-column label="所在公司">
        <template slot-scope="{row}">
          {{row.company?row.company.name:row.companyName}}
        </template>
      </el-table-column>
      <el-table-column label="提供面试类型" prop="interviewDirectionType" :show-overflow-tooltip='true'></el-table-column>
      <el-table-column label="经验年限" prop="experienceTime.name"></el-table-column>
      <el-table-column label="已面试时长" v-if="listQuery.status === '1'">
        <template slot-scope="{row}">
          {{row.totalInterviewTime}}小时
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" fixed="right" width="300">
        <template slot-scope="{row}">
          <span v-if="row.status !== '0'">
            <el-button
              type="primary"
              size="mini"
              icon="el-icon-document"
              @click="interviewDetails(row.id)"
              v-if="row.status === '1'"
            >查看</el-button>

            <el-button
              type="warning"
              size="mini"
              icon="el-icon-edit"
              @click="editInterviewer(row.id)"
              v-if="row.status === '1'"
            >编辑</el-button>

            <el-button
              type="danger"
              size="mini"
              icon="el-icon-delete"
              @click="offShelfInterviewer(row.id)"
              v-if="row.status === '1'"
            >下架</el-button>
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
        v-if="verifyUser && verifyUser.status === '0'"
      >
        <el-button type="success" icon="el-icon-check" @click="handleVerifyUser(verifyUser, 2)">通过</el-button>
        <el-button type="warning" icon="el-icon-close" @click="handleVerifyUser(verifyUser, 3)">拒绝</el-button>
      </div>
      <VerificationView :userId="verifyUser?verifyUser.id : undefined"></VerificationView>
    </el-drawer>
  </div>
</template>

<script>
  import waves from "@/directive/waves"; // waves directive
  import Pagination from "@/components/Pagination"; // secondary package based on el-pagination
  import VerificationView from "./VerificationView";
  import {getInterviewerList,offShelfInterviewer} from "@/api/mock_api";
  import {verifyInterviewer} from "@/api/verify_api";

  export default {
    name: "InterviewerList",
    components: {Pagination, VerificationView},
    directives: {waves},
    data() {
      return {
        tableKey: 0,
        list: [],
        total: 0,
        listLoading: true,
        listQuery: {
          id: undefined,
          nickName: undefined,
          email: undefined,
          status: "0",
          page: 1,
          limit: 20,
        },
        statusForm: {
          id: undefined,
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
        verifyDrawerVisible: false,
        verifyUser: undefined
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
        getInterviewerList(this.listQuery).then(response => {
          const {total, list} = response.data;
          this.list = list;
          this.total = total;
          this.listLoading = false;
        });
      },
      handleSelectMenu(index) {
        this.listQuery.status = index;
        this.$router.push({path: this.$route.path, query: {status: index}});
      },
      handleRouteList() {
        this.$router.push({path: this.$route.path, query: this.listQuery});
      },
      handleFilter() {
        this.listQuery.page = 1;
        this.handleRouteList();
      },
      handleVerifyUser(user, status) {
        this.verifyDrawerVisible = false;
        this.statusForm.id = user.id;
        this.statusForm.status = status;
        if (status === 2) {
          this.$confirm("此操作将通过审核, 是否继续?", "提示", {
            confirmButtonText: "继续",
            cancelButtonText: "取消",
            type: "warning"
          }).then(() => {
            verifyInterviewer(this.statusForm).then(response => {
              this.getList();
              this.$message("操作成功");
            });
          });
        } else {
          this.$prompt("请填写拒绝通过的理由", "提示", {
            confirmButtonText: "提交",
            cancelButtonText: "取消"
          }).then(({value}) => {
            this.statusForm.reason = value;
            verifyInterviewer(this.statusForm).then(response => {
              this.getList();
              this.$message("操作成功");
            });
          });
        }
      },
      showUserVerification(user) {
        this.verifyDrawerVisible = true;
        this.verifyUser = user;
        console.log(this.verifyUser);
      },
      addInterviewer() {
        this.$router.push("/mock/registerInterviewer");
      },
      interviewDetails(id){
        this.$router.push({path:'/mock/interviewDetails',query: {interviewerId: id}});
      },

      editInterviewer(id) {
        this.$router.push({path:'/mock/editInterviewer',query: {interviewerId: id}});
      },

      offShelfInterviewer(id){
        this.$confirm("此操作将下架面试官, 是否继续?", "提示", {
          confirmButtonText: "继续",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          offShelfInterviewer(id).then(response => {
            if(response.data){
              this.getList();
              this.$message.success("下架面试官成功");
            }
            else
              this.$message.error("下架面试官失败");
          });
        });
      },
    }
  };
</script>
