<template>
  <div class="app-container">
    <div class="search-bar">
      <div class="search-filter mt-2 mt-2">
        <el-select
          v-model="listQuery.cityIds"
          multiple
          filterable
          clearable
          placeholder="城市"
          @change="handleFilter"
          size="small"
        >
          <el-option v-for="item in cityOptions" :key="item.id" :label="item.name" :value="item.id"></el-option>
        </el-select>
        <el-select
          v-model="listQuery.salaryRangeId"
          filterable
          clearable
          placeholder="薪资"
          @change="handleFilter"
          size="small"
          class="ml-2"
        >
          <el-option
            v-for="item in salaryRangeOptions"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          ></el-option>
        </el-select>
        <el-select
          v-model="listQuery.companyIndustryIds"
          multiple
          filterable
          clearable
          placeholder="行业"
          @change="handleFilter"
          size="small"
          class="ml-2"
        >
          <el-option
            v-for="item in companyIndustryOptions"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          ></el-option>
        </el-select>
        <el-select
          v-model="listQuery.companyScaleIds"
          multiple
          filterable
          clearable
          placeholder="公司规模"
          @change="handleFilter"
          size="small"
          class="ml-2"
        >
          <el-option
            v-for="item in companyScaleOptions"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          ></el-option>
        </el-select>

        <el-select
          v-model="listQuery.jobType"
          filterable
          clearable
          placeholder="工作类型"
          @change="handleFilter"
          size="small"
          class="ml-2"
        >
          <el-option
            v-for="item in jobTypeOptions"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          ></el-option>
        </el-select>
      </div>
    </div>
    <pagination
      v-show="total"
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.limit"
      @pagination="handleRouteList"
    />
    <div class="job-list w-75" v-if="pageResult.list && pageResult.list.length !== 0">
      <el-card
        shadow="hover"
        v-for="job in pageResult.list"
        :key="job.id"
        class="mb-2"
        @click.native="openJobDetail(job.id)"
      >
        <b-row>
          <b-col>
            <b-media>
              <h6 class="mt-0 mb-1">{{job.name}}</h6>
              <div class>
                <b
                  class="text-danger"
                >{{`${job.minSalary} - ${job.maxSalary}K`}}{{job.salaryMonths?` × ${job.salaryMonths}`:''}}</b>
                <span
                  class="ml-3 text-gray text-small"
                >{{`${job.city.name} / ${job.minDegree.name}`}}</span>
              </div>
            </b-media>
          </b-col>
          <b-col>
            <b-media right-align vertical-align="center">
              <template v-slot:aside>
                <el-link :href="`/company/${job.companyUser.company.id}`">
                  <b-img
                    width="48"
                    :src="job.companyUser.company.logo"
                    v-if="job.companyUser.company.logo && job.companyUser.company.logo != ''"
                  ></b-img>
                  <b-img blank blank-color="#ccc" width="48" v-else></b-img>
                </el-link>
              </template>
              <h6 class="mt-0 mb-1">{{job.companyUser.company.name}}</h6>
              <div
                class="text-gray text-small"
              >{{job.companyUser.company.industry.name}} / {{job.companyUser.company.stage.name}} / {{job.companyUser.company.scale.name}}</div>
            </b-media>
          </b-col>
        </b-row>
      </el-card>
    </div>
    <pagination
      v-show="total"
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.limit"
      @pagination="handleRouteList"
    />
  </div>
</template>

<script>
import { listByType } from "@/api/dict_api";
import { searchJob } from "@/api/job_api";
import Pagination from "@/components/Pagination";
import {
  formatListQuery,
  parseListQuery
} from "@/utils/common";

export default {
  name: "JobListPage",
  components: { Pagination },
  data() {
    return {
      listQuery: {
        keyword: "",
        salaryRangeId: undefined,
        jobType: undefined,
        cityIds: [],
        companyIndustryIds: [],
        companyScaleIds: [],
        companyStageIds: [],
        degreeIds: [],
        categoryIds: [],
        page: 1,
        limit: 10
      },
      total: 0,
      pageResult: 0,
      cityOptions: [],
      salaryRangeOptions: [],
      companyScaleOptions: [],
      companyIndustryOptions: [],
      jobTypeOptions: []
    };
  },
  created() {
    this.initData();
    this.getList();
  },
  watch: {
    $route() {
      this.getList();
    },
    "listQuery.keyword"() {
      this.$store.commit("setting/SET_KEYWORD", this.listQuery.keyword);
    }
  },
  methods: {
    initData() {
      listByType(2).then(response => (this.cityOptions = response.data.list));
      listByType(5).then(
        response => (this.companyScaleOptions = response.data.list)
      );
      listByType(6).then(
        response => (this.companyIndustryOptions = response.data.list)
      );
      listByType(8).then(
        response => (this.jobTypeOptions = response.data.list)
      );
      listByType(9).then(
        response => (this.salaryRangeOptions = response.data.list)
      );
    },
    handleFilter() {
      this.listQuery.page = 1;
      this.handleRouteList();
    },
    getList() {
      parseListQuery(this.$route.query, this.listQuery);
      if(this.listQuery.salaryRangeId){
         this.listQuery.salaryRangeId = parseInt(this.listQuery.salaryRangeId);
      }
      if(this.listQuery.jobType){
         this.listQuery.jobType = parseInt(this.listQuery.jobType);
      }
      searchJob(this.listQuery).then(response => {
        this.pageResult = response.data;
        this.total = this.pageResult.total;
      });
    },
    handleRouteList() {
      this.$router.push({
        path: this.$route.path,
        query: formatListQuery(this.listQuery)
      });
    },
    openJobDetail(id) {
      this.$router.push(`/job/${id}`);
    }
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss">
.app-container {
  width: 1000px;
  margin: 20px auto;
}

$search-height: 48px;
</style>
