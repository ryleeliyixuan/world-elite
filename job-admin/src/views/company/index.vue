<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :inline="true" :model="listQuery">
        <el-form-item>
          <el-input v-model="listQuery.fullName" placeholder="公司名称" @keyup.enter.native="handleFilter"></el-input>
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
      <el-table-column label="ID" prop="id" width="180"></el-table-column>
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
        class-name="small-padding fixed-width"
        fixed="right"
      >
        <template slot-scope="{row}">
          <el-button type="primary" size="mini" @click="handleModify(row)" icon="el-icon-edit">编辑</el-button>
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
import { getCompanyList } from "@/api/company_api";
import { listByType } from '@/api/dict_api'

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
      cityOptions: []
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
    initData(){
       listByType(7).then(response => this.industryOptions = response.data.list);
       listByType(2).then(response => this.cityOptions = response.data.list);
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
        this.$router.push({path: '/company/edit', query: {id: company.id}})
    }
  }
};
</script>
