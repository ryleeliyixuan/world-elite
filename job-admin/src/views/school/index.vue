<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :inline="true" :model="listQuery" @submit.native.prevent>
        <el-form-item>
          <el-button type="success" @click="onNewSchoolClick" icon="el-icon-plus">添加学校</el-button>
        </el-form-item>
        <el-form-item>
          <el-input v-model="listQuery.name" placeholder="学校名称" @keyup.enter.native="handleFilter"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleFilter">查询</el-button>
        </el-form-item>
      </el-form>
    </div>
        
    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%; margin-top:10px"
    >
      <el-table-column label="ID" prop="id"></el-table-column>
      <el-table-column label="名称" prop="name" align="center"></el-table-column>
      <el-table-column label="主页链接" prop="siteLink" align="center">
        <template slot-scope="{row}">
          <el-link :href="siteLink(row.siteLink)" target="_blank">{{siteLinkName(row.siteLink)}}</el-link>
        </template>
      </el-table-column>
      <el-table-column label="所在国家" prop="country.chineseName" align="center"></el-table-column>
      <el-table-column label="操作">
        <template slot-scope="{row}">
          <el-button type="primary" size="mini" @click="onEditSchoolClick(row)">修改</el-button>
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

    <el-dialog :title="dialogTitle" :visible.sync="dialogEditVisible">
      <el-form
        ref="schoolForm"
        :model="schoolForm"
        label-position="left"
        label-width="100px"
        style="width: 300px; margin-left:50px;"
      >
        <el-form-item label="所在国家" prop="countryId">
          <el-select
            v-model="schoolForm.countryId"
            class="filter-item"
            placeholder="所在国家"
            style="width: 300px"
            filterable
          >
            <el-option
              v-for="country in countryOptions"
              :key="country.id"
              :label="country.chineseName"
              :value="country.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="学校名称" prop="name">
          <el-input v-model="schoolForm.name" placeholder="请填写学校名" style="width: 300px"></el-input>
        </el-form-item>
        <el-form-item label="学校主页" prop="siteLink">
          <el-input v-model="schoolForm.siteLink" placeholder="请填写学校主页链接" style="width: 300px"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogEditVisible = false">取消</el-button>
        <el-button type="primary" @click="onSaveSchoolClick">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getSchoolList, saveSchool } from "@/api/school_api";
import { getAllCountries } from "@/api/country_api";
import Pagination from "@/components/Pagination"; // secondary package based on el-pagination
import { formatListQuery, parseListQuery } from "@/utils/common";
import Toast from '@/utils/toast'

export default {
  name: "SchoolList",
  components: { Pagination },
  data() {
    return {
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      dialogEditVisible: false,
      dialogTitle: "",
      listQuery: {
        page: 1,
        limit: 20,
        name: undefined,
        sort: "-id"
      },
      schoolForm: {
        id: undefined,
        countryId: undefined,
        name: undefined,
        siteLink: undefined
      },
      countryOptions: []
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
      getAllCountries().then(response => {
        this.countryOptions = response.data;
      });
      this.getList();
    },
    getList() {
      this.listLoading = true;
      parseListQuery(this.$route.query, this.listQuery);
      getSchoolList(this.listQuery).then(response => {
        this.list = response.data.list;
        this.total = response.data.total;
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
    onNewSchoolClick() {
      this.dialogEditVisible = true;
      this.dialogTitle = "添加学校";
      this.schoolForm.id = undefined;
      this.schoolForm.countryId = undefined;
      this.schoolForm.name = undefined;
      this.schoolForm.siteLink = undefined;
    },
    onEditSchoolClick(school) {
      this.dialogEditVisible = true;
      this.dialogTitle = "编辑学校";
      this.schoolForm.id = school.id;
      this.schoolForm.countryId = school.country
        ? school.country.id
        : undefined;
      this.schoolForm.name = school.name;
      this.schoolForm.siteLink = school.siteLink;
    },
    onSaveSchoolClick() {
      this.dialogEditVisible = false;
      saveSchool(this.schoolForm).then(() => {
        Toast.success("保存成功");
        this.getList();
      });
    },
    siteLink(siteLink) {
      return siteLink.startsWith("http")
        ? siteLink
        : "http://" + siteLink;
    },
    siteLinkName(siteLink) {
      return siteLink.replace(/http(s)?:\/\//,'');
    }
  }
};
</script>
