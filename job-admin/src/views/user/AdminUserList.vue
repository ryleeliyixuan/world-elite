<template>
  <div class="app-container">
    <el-button type="success" @click="onAddAdminClick" icon="el-icon-plus">添加管理员</el-button>
    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      highlight-current-row
      style="margin-top: 10px"
    >
      <el-table-column label="ID" prop="userId" width="180"></el-table-column>
      <el-table-column label="名字" prop="name"></el-table-column>
      <el-table-column label="email" prop="email"></el-table-column>
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
            @click="handleDelete(row)"
          >删除</el-button>
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

    <el-dialog title="添加管理员" :visible.sync="addAdminDialogVisible">
        <el-form :model="adminForm" status-icon :rules="formRules" ref="adminForm" label-width="100px">
            <el-form-item label="名字" prop="name">
                <el-input v-model="adminForm.name"></el-input>
            </el-form-item>
            <el-form-item label="登录email" prop="email">
                <el-input v-model="adminForm.email"></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="password">
                <el-input type="password" v-model="adminForm.password" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="onSubmitClick">提交</el-button>
            </el-form-item>
        </el-form>
    </el-dialog>
    
  </div>
</template>

<script>
import waves from "@/directive/waves"; // waves directive
import Pagination from "@/components/Pagination"; // secondary package based on el-pagination
import { getAdminUserList, addAdmin, deleteAdmin } from "@/api/user_api";
import { exportUserList } from "@/api/export_api";
import { formatListQuery, parseListQuery } from "@/utils/common";
import Toast from "@/utils/toast";

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
        type: 100,
        page: 1,
        limit: 20,
      },
      adminForm:{
          name: undefined,
          email: undefined,
          password: undefined
      },
      formRules: {
          name: [{ required: true, message: "请输入名称", trigger: "blur" }],
          email: [{ required: true, message: "请输入email", trigger: "blur" }],
          password: [{ required: true, message: "请输入密码", trigger: "blur" }],
      },
      addAdminDialogVisible: false
    };
  },
  created() {
    this.getList();
  },
  watch: {
    $route() {
      this.getList();
    },
  },
  methods: {
    getList() {
      this.listLoading = true;
      const query = this.$route.query;
      parseListQuery(query, this.listQuery);
      getAdminUserList(this.listQuery).then(response => {
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
      this.getList();
    },
    handleDelete(user){
        this.$confirm("此操作将删除该用户, 是否继续?", "提示", {
          confirmButtonText: "继续",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          deleteAdmin(user.userId).then(response => {
              Toast.success('操作成功');
              this.getList();
          })
        });
    },
    onAddAdminClick(){
        this.addAdminDialogVisible = true;
        this.adminForm.name = undefined
        this.adminForm.email = undefined
        this.adminForm.password = undefined
    },
    onSubmitClick(){
        this.$refs["adminForm"].validate(valid => {
        if (valid) {
          addAdmin(this.adminForm)
            .then(() => {
              this.$message("添加成功");
              this.getList();
              this.addAdminDialogVisible = false;            
            })
        }
      });
    }
  }
};
</script>
