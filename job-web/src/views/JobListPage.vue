<template>
  <div class="container mt-2">
    <el-row :gutter="10">
      <el-col :span="4">
        <el-select
          v-model="listQuery.cityIds"
          multiple
          filterable
          clearable
          placeholder="城市"
          @change="handleFilter"
          size="small"
          class="w-100"
        >
          <el-option v-for="item in cityOptions" :key="item.id" :label="item.name" :value="item.id"></el-option>
        </el-select>
      </el-col>
      <el-col :span="4">
        <el-select
          v-model="listQuery.salaryRangeIds"
          multiple
          filterable
          clearable
          placeholder="薪资"
          @change="handleFilter"
          size="small"
          class="w-100"
        >
          <el-option
            v-for="item in salaryRangeOptions"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          ></el-option>
        </el-select>
      </el-col>
      <el-col :span="4">
        <el-select
          v-model="listQuery.companyIndustryIds"
          multiple
          filterable
          clearable
          placeholder="行业"
          @change="handleFilter"
          size="small"
          class="w-100"
        >
          <el-option
            v-for="item in companyIndustryOptions"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          ></el-option>
        </el-select>
      </el-col>
      <el-col :span="4">
        <el-select
          v-model="listQuery.companyScaleIds"
          multiple
          filterable
          clearable
          placeholder="公司规模"
          @change="handleFilter"
          size="small"
          class="w-100"
        >
          <el-option
            v-for="item in companyScaleOptions"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          ></el-option>
        </el-select>
      </el-col>
      <el-col :span="4">
        <el-select
          v-model="listQuery.jobTypes"
          multiple
          filterable
          clearable
          placeholder="工作类型"
          @change="handleFilter"
          size="small"
          class="w-100"
        >
          <el-option
            v-for="item in jobTypeOptions"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          ></el-option>
        </el-select>
      </el-col>
    </el-row>

    <pagination
      v-show="total"
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.limit"
      @pagination="handleRouteList"
    />

    <div class="row">
      <div class="col-lg-8 col-md-10 col-sm-12">
        <div class="job-list" v-if="pageResult.list && pageResult.list.length !== 0">
          <el-card
            shadow="hover"
            v-for="job in pageResult.list"
            :key="job.id"
            class="mb-2 link-pointer"
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
                    >{{`${job.city? job.city.name : ''} / ${job.minDegree?job.minDegree.name:''}`}}</span>
                  </div>
                </b-media>
              </b-col>
              <b-col>
                <b-media
                  right-align
                  vertical-align="center"
                  v-if="job.companyUser && job.companyUser.company"
                >
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
                  >{{job.companyUser.company.industry?job.companyUser.company.industry.name:''}} / {{job.companyUser.company.stage?job.companyUser.company.stage.name:''}} / {{job.companyUser.company.scale?job.companyUser.company.scale.name:''}}</div>
                </b-media>
              </b-col>
            </b-row>
          </el-card>
        </div>
      </div>
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
import { mapGetters } from "vuex";

import { formatListQuery, parseListQuery } from "@/utils/common";

export default {
  name: "JobListPage",
  components: { Pagination },
  computed: {
    ...mapGetters(["keyword"])
  },
  data() {
    return {
      listQuery: {
        keyword: "",
        salaryRangeIds: [],
        jobTypes: [],
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
    keyword() {
      this.listQuery.keyword = this.keyword;
      this.handleRouteList();
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
      searchJob(this.listQuery).then(response => {
        this.pageResult = response.data;
        this.total = this.pageResult.total;
      });
    },
    handleRouteList() {
      this.$router.replace({
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