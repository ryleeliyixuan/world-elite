<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :inline="true" :model="listQuery">
        <el-form-item>
          <el-button type="success" @click="handleNewCompany" icon="el-icon-plus">添加企业</el-button>
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="listQuery.fullName"
            placeholder="公司名称"
            @keyup.enter.native="handleFilter"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-select
            v-model="listQuery.industryId"
            filterable
            clearable
            placeholder="所属行业"
            @change="handleFilter"
          >
            <el-option
              v-for="item in industryOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-select
            v-model="listQuery.cityId"
            filterable
            clearable
            placeholder="所在城市"
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
            <el-link @click="goCompanyDetail(row.id)" type="primary">{{row.id}}</el-link>
         </template>  
      </el-table-column>
      <el-table-column label="logo" prop="logo">
        <template slot-scope="{row}">
          <el-image :src="row.logo">
            <div slot="error" class="image-slot">暂无</div>
          </el-image>
        </template>
      </el-table-column>
      <el-table-column label="简称" prop="name"></el-table-column>
      <el-table-column label="全名" prop="fullName"></el-table-column>
      <el-table-column label="行业" prop="industry.name"></el-table-column>
      <el-table-column label="规模" prop="scale.name"></el-table-column>
      <el-table-column label="阶段" prop="stage.name"></el-table-column>
      <el-table-column label="性质" prop="property.name"></el-table-column>

      <el-table-column
        label="操作"
        align="center"
        width="260"
      >
        <template slot-scope="{row}">
          <el-button type="primary" size="mini" @click="handleModify(row)" icon="el-icon-edit">编辑</el-button>
          <el-button type="success" size="mini" @click="handleEditWiki(row)" icon="el-icon-edit">百科</el-button>
          <el-popconfirm title="你确定要删除该企业？" @onConfirm="handleDelete(row)"> 
            <el-button
              type="danger"
              size="mini"
              confirmButtonText="删除"
              icon="el-icon-delete"
              slot="reference"
            >删除</el-button>
          </el-popconfirm>
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

    <el-dialog
      title="添加公司"
      :visible.sync="newDialogVisible"
      width="30%"
    >
      <el-form ref="form" :model="companyForm">
        <el-form-item>
          <el-input v-model="companyForm.fullName" placeholder="公司全称"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="newDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleSaveCompany">下一步</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import waves from "@/directive/waves"; // waves directive
import Pagination from "@/components/Pagination"; // secondary package based on el-pagination
import { getCompanyList, saveCompany, delCompany } from "@/api/company_api";
import { listByType } from "@/api/dict_api";

export default {
  name: "CompanyList",
  components: { Pagination },
  directives: { waves },
  data() {
    return {
      tableKey: 0,
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        fullName: undefined,
        industryId: undefined,
        cityId: undefined,
        page: 1,
        limit: 20,
        sort: "-id"
      },
      industryOptions: [],
      cityOptions: [],
      newDialogVisible: false,
      companyForm: {
        fullName: ""
      }
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
      listByType(7).then(
        response => (this.industryOptions = response.data.list)
      );
      listByType(2).then(response => (this.cityOptions = response.data.list));
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
      getCompanyList(this.listQuery).then(response => {
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
    handleModify(company) {
      this.$router.push({ path: "/company/edit", query: { id: company.id } });
    },
    handleNewCompany() {
      this.newDialogVisible = true;
      this.companyForm.fullName = "";
    },
    handleSaveCompany() {
      this.newDialogVisible = false;
      saveCompany(this.companyForm).then(response => {
        this.$router.push({
          path: "/company/edit",
          query: { id: response.data }
        });
      });
    },
    handleDelete(company) {
        delCompany(company.id).then(()=>{
            this.$message('删除成功');
            this.getList();
        })
    },
    handleEditWiki(company){
      this.$router.push({path: '/company/edit-wiki', query: { id: company.id }});
    },
    goCompanyDetail(companyId){
        window.open(`${process.env.VUE_APP_WEB_HOST}/company/${companyId}`)
    },
  }
};
</script>
