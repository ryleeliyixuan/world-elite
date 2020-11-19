<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :inline="true" :model="listQuery" @submit.native.prevent>
        <el-form-item>
          <el-input
            v-model="listQuery.fullName"
            placeholder="公司名称"
            @keyup.enter.native="handleFilter"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleFilter">查询</el-button>
        </el-form-item>
      </el-form>
    </div>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.limit"
      @pagination="handleRouteList"
    />
    <el-table
      :data="companyList"
      border
      style="width: 100%"
      v-loading="listLoading"
    >
      <el-table-column prop="id" label="ID" width="180"> </el-table-column>
      <el-table-column prop="fullName" label="公司名称" width="180">
      </el-table-column>
      <el-table-column label="公司地址">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.addressEnable"
            active-color="#13ce66"
            inactive-color="#ff4949"
            active-value="1"
            inactive-value="0"
            @change="
              changeCityStatus($event, scope.row.wikiModuleId, scope.row.id)
            "
          >
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column label="雇员数量">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.employeeEnable"
            active-color="#13ce66"
            inactive-color="#ff4949"
            active-value="1"
            inactive-value="0"
            @change="
              changeEmployeeStatus($event, scope.row.wikiModuleId, scope.row.id)
            "
          >
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column label="旗下产品">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.productEnable"
            active-color="#13ce66"
            inactive-color="#ff4949"
            active-value="1"
            inactive-value="0"
            @change="
              changeProductStatus($event, scope.row.wikiModuleId, scope.row.id)
            "
          >
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column label="市值情况">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.marketEnable"
            active-color="#13ce66"
            inactive-color="#ff4949"
            active-value="1"
            inactive-value="0"
            @change="
              changeMarketStatus($event, scope.row.wikiModuleId, scope.row.id)
            "
          >
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column label="发展路径">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.historyEnable"
            active-color="#13ce66"
            inactive-color="#ff4949"
            active-value="1"
            inactive-value="0"
            @change="
              changeHistoryStatus($event, scope.row.wikiModuleId, scope.row.id)
            "
          >
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column label="组织架构">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.structureEnable"
            active-color="#13ce66"
            inactive-color="#ff4949"
            active-value="1"
            inactive-value="0"
            @change="
              changeStructureStatus(
                $event,
                scope.row.wikiModuleId,
                scope.row.id
              )
            "
          >
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column label="薪酬待遇">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.salaryEnable"
            active-color="#13ce66"
            inactive-color="#ff4949"
            active-value="1"
            inactive-value="0"
            @change="
              changeSalaryStatus(
                $event,
                scope.row.wikiModuleId,
                scope.row.id
              )
            "
          >
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column label="工作环境">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.environmentEnable"
            active-color="#13ce66"
            inactive-color="#ff4949"
            active-value="1"
            inactive-value="0"
            @change="
              changeEnvironmentStatus(
                $event,
                scope.row.wikiModuleId,
                scope.row.id
              )
            "
          >
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column label="招聘时间线">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.recruitEnable"
            active-color="#13ce66"
            inactive-color="#ff4949"
            active-value="1"
            inactive-value="0"
            @change="
              changeRecruitStatus(
                $event,
                scope.row.wikiModuleId,
                scope.row.id
              )
            "
          >
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column label="企业荣誉">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.honorEnable"
            active-color="#13ce66"
            inactive-color="#ff4949"
            active-value="1"
            inactive-value="0"
            @change="
              changeHonorStatus(
                $event,
                scope.row.wikiModuleId,
                scope.row.id
              )
            "
          >
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column label="公司部门">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.departmentEnable"
            active-color="#13ce66"
            inactive-color="#ff4949"
            active-value="1"
            inactive-value="0"
            @change="
              changeDepartmentStatus(
                $event,
                scope.row.wikiModuleId,
                scope.row.id
              )
            "
          >
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button type="text" @click="viewWiki(scope.row.id)"
            >查看
          </el-button>
          <el-button
            type="text"
            @click="delWiki(scope.row.id, scope.$index, companyList)"
            >删除
          </el-button>
          <el-button type="text" @click="editWiki(scope.row.id)"
            >编辑
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.limit"
      @pagination="handleRouteList"
    />
  </div>
</template>

<script>
import waves from "@/directive/waves"; // waves directive
import Pagination from "@/components/Pagination";
import { listByType } from "@/api/dict_api";
import Toast from "@/utils/toast";
import {
  getCompanyList,
  getCompanyWiki,
  changeModuleEnable,
  delCompany,
} from "@/api/company_api";
import { formatListQuery, parseListQuery } from "@/utils/common";

export default {
  name: "WikiListPage",
  components: { Pagination },
  directives: { waves },
  data() {
    return {
      total: 0,
      listLoading: true,
      companyList: [],
      options: ["开启公司地址"],

      listQuery: {
        fullName: undefined,
        page: 1,
        limit: 20,
        sort: "-id",
      },
    };
  },
  created() {
    this.initData();
  },
  watch: {
    $route() {
      this.initData();
    },
  },
  methods: {
    initData() {
      // parseListQuery(this.$route.query, this.listQuery);
      this.listLoading = true;

      const query = this.$route.query;
      Object.keys(query).forEach((key) => {
        this.listQuery[key] = query[key];
      });
      if (query.page) {
        this.listQuery.page = parseInt(query.page);
      }
      if (query.limit) {
        this.listQuery.limit = parseInt(query.limit);
      }

      getCompanyList(this.listQuery).then((response) => {
        const { total, list } = response.data;
        this.companyList = list;
        for (let i = 0; i < list.length; i++) {
          let company = list[i];
          this.getModuleStatus(company.id, i);
        }
        this.total = total;
        this.listLoading = false;
      });
    },
    getModuleStatus(id, index) {
      getCompanyWiki(id).then((response) => {
        if (
          response.data &&
          response.data.wikiModule &&
          response.data.wikiModule.id
        ) {
          this.companyList[index].wikiModuleId = response.data.wikiModule.id;
        }
        if (
          response.data &&
          response.data.wikiModule &&
          response.data.wikiModule.addressEnable === 1
        ) {
          this.companyList[index].addressEnable = "1";
        } else {
          this.companyList[index].addressEnable = "0";
        }
        if (
          response.data &&
          response.data.wikiModule &&
          response.data.wikiModule.marketEnable === 1
        ) {
          this.companyList[index].marketEnable = "1";
        } else {
          this.companyList[index].marketEnable = "0";
        }
        if (
          response.data &&
          response.data.wikiModule &&
          response.data.wikiModule.historyEnable === 1
        ) {
          this.companyList[index].historyEnable = "1";
        } else {
          this.companyList[index].historyEnable = "0";
        }
        if (
          response.data &&
          response.data.wikiModule &&
          response.data.wikiModule.structureEnable === 1
        ) {
          this.companyList[index].structureEnable = "1";
        } else {
          this.companyList[index].structureEnable = "0";
        }
        if (
          response.data &&
          response.data.wikiModule &&
          response.data.wikiModule.employeeEnable === 1
        ) {
          this.companyList[index].employeeEnable = "1";
        } else {
          this.companyList[index].employeeEnable = "0";
        }
        if (
          response.data &&
          response.data.wikiModule &&
          response.data.wikiModule.productEnable === 1
        ) {
          this.companyList[index].productEnable = "1";
        } else {
          this.companyList[index].productEnable = "0";
        }
        if (
          response.data &&
          response.data.wikiModule &&
          response.data.wikiModule.salaryEnable === 1
        ) {
          this.companyList[index].salaryEnable = "1";
        } else {
          this.companyList[index].salaryEnable = "0";
        }
        if (
          response.data &&
          response.data.wikiModule &&
          response.data.wikiModule.environmentEnable === 1
        ) {
          this.companyList[index].environmentEnable = "1";
        } else {
          this.companyList[index].environmentEnable = "0";
        }
        if (
          response.data &&
          response.data.wikiModule &&
          response.data.wikiModule.recruitEnable === 1
        ) {
          this.companyList[index].recruitEnable = "1";
        } else {
          this.companyList[index].recruitEnable = "0";
        }
        if (
          response.data &&
          response.data.wikiModule &&
          response.data.wikiModule.honorEnable === 1
        ) {
          this.companyList[index].honorEnable = "1";
        } else {
          this.companyList[index].honorEnable = "0";
        }
        if (
          response.data &&
          response.data.wikiModule &&
          response.data.wikiModule.departmentEnable === 1
        ) {
          this.companyList[index].departmentEnable = "1";
        } else {
          this.companyList[index].departmentEnable = "0";
        }
      });
    },
    handleRouteList() {
      this.companyList = [];
      this.$router.push({
        path: this.$route.path,
        query: formatListQuery(this.listQuery),
      });
    },
    handleFilter() {
      this.listQuery.page = 1;
      this.handleRouteList();
    },
    //change status function
    changeEnvironmentStatus(event, wikiModuleId, companyId) {
      let data = {
        environmentEnable: Number(event),
        id: wikiModuleId,
        companyId: companyId,
      };
      changeModuleEnable(data).then((response) => {
        Toast.success("企业环境状态改变成功");
      });
    },
    changeRecruitStatus(event, wikiModuleId, companyId) {
      let data = {
        recruitEnable: Number(event),
        id: wikiModuleId,
        companyId: companyId,
      };
      changeModuleEnable(data).then((response) => {
        Toast.success("招聘时间线状态改变成功");
      });
    },
    changeHonorStatus(event, wikiModuleId, companyId) {
      let data = {
        honorEnable: Number(event),
        id: wikiModuleId,
        companyId: companyId,
      };
      changeModuleEnable(data).then((response) => {
        Toast.success("企业荣誉状态改变成功");
      });
    },
    changeDepartmentStatus(event, wikiModuleId, companyId) {
      let data = {
        departmentEnable: Number(event),
        id: wikiModuleId,
        companyId: companyId,
      };
      changeModuleEnable(data).then((response) => {
        Toast.success("公司部门状态改变成功");
      });
    },
    changeSalaryStatus(event, wikiModuleId, companyId) {
      let data = {
        salaryEnable: Number(event),
        id: wikiModuleId,
        companyId: companyId,
      };
      changeModuleEnable(data).then((response) => {
        Toast.success("薪酬待遇状态改变成功");
      });
    },
    changeCityStatus(event, wikiModuleId, companyId) {
      let data = {
        addressEnable: Number(event),
        id: wikiModuleId,
        companyId: companyId,
      };
      changeModuleEnable(data).then((response) => {
        Toast.success("公司地址状态改变成功");
      });
    },
    changeEmployeeStatus(event, wikiModuleId, companyId) {
      let data = {
        employeeEnable: Number(event),
        id: wikiModuleId,
        companyId: companyId,
      };
      changeModuleEnable(data).then((response) => {
        Toast.success("雇员数量状态改变成功");
      });
    },
    changeProductStatus(event, wikiModuleId, companyId) {
      let data = {
        productEnable: Number(event),
        id: wikiModuleId,
        companyId: companyId,
      };
      changeModuleEnable(data).then((response) => {
        Toast.success("产品模块状态改变成功");
      });
    },
    changeMarketStatus(event, wikiModuleId, companyId) {
      let data = {
        marketEnable: Number(event),
        id: wikiModuleId,
        companyId: companyId,
      };
      changeModuleEnable(data).then((response) => {
        Toast.success("市值模块状态改变成功");
      });
    },
    changeHistoryStatus(event, wikiModuleId, companyId) {
      let data = {
        historyEnable: Number(event),
        id: wikiModuleId,
        companyId: companyId,
      };
      changeModuleEnable(data).then((response) => {
        Toast.success("发展路径状态改变成功");
      });
    },
    changeStructureStatus(event, wikiModuleId, companyId) {
      let data = {
        structureEnable: Number(event),
        id: wikiModuleId,
        companyId: companyId,
      };
      changeModuleEnable(data).then((response) => {
        Toast.success("组织架构状态改变成功");
      });
    },
    //change status function ends
    viewWiki(companyId) {
      window.open(`http://test.myworldelite.com/company/${companyId}`);
    },
    delWiki(id, index, rows) {
      this.$confirm("是否要删除该公司？", {
        confirmButtonText: "删除",
      }).then(() => {
        delCompany(id).then(() => {
          Toast.success("操作成功");
          rows.splice(index, 1);
        });
      });
    },
    editWiki(companyId) {
      this.$router.push({ path: "/wiki/edit", query: { id: companyId } });
    },
  },
};
</script>

<style lang="scss" scoped>
</style>
