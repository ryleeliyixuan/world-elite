<template>
  <div class="background-wrapper">
    <Affix>
      <div class="section1-wrapper" v-if="!collapse"  v-loading="paneLoading">
        <div class="section1-container">

          <div v-show="A">
            <!-- 所在城市 -->
            <div class="section1-filter">
              <span class="section1-filter-title">所在城市：</span>
              <el-checkbox-group
                      v-model="listQuery.cityIds"
                      size="small"
              >
                <el-checkbox-button
                        v-for="item in cityOptions"
                        :label="item.id"
                        :key="item.id"
                        @change="handleFilter"
                >{{ item.name }}</el-checkbox-button
                >
              </el-checkbox-group>
              <!--<el-input class="inp-city" v-model="inpCity" size="mini" placeholder="输入国内外其他城市名，支持多个"></el-input>-->

              <el-select
                      class="inp-city"
                      v-model="inpCity"
                      clearable
                      multiple
                      filterable
                      allow-create
                      default-first-option
                      remote
                      reserve-keyword
                      placeholder="输入国内外其他城市名，支持多个"
                      size="mini"
                      @change="inpCityChange"
                      >
              </el-select>
            </div>

            <!-- 行业类型 -->
            <div class="section1-filter">
              <span class="section1-filter-title">行业类型：</span>
              <el-checkbox-group
                      v-model="listQuery.companyIndustryIds"
                      size="small"
              >
                <el-checkbox-button
                        v-for="item in companyIndustryOptions"
                        :label="item.id"
                        :key="item.id"
                        @change="handleFilter"
                >{{ item.name }}</el-checkbox-button
                >
              </el-checkbox-group>
            </div>

            <!-- 月薪区间 -->
            <div class="section1-filter">
              <span class="section1-filter-title">月薪区间：</span>
              <el-checkbox-group v-model="listQuery.salaryRangeIds" size="small">
                <el-checkbox-button
                        v-for="item in salaryRangeOptions"
                        :label="item.id"
                        :key="item.id"
                        @change="handleFilter"
                >{{ item.name }}</el-checkbox-button
                >
              </el-checkbox-group>
            </div>

            <!-- 学历要求 -->
            <div class="section1-filter">
              <span id="pointA" class="section1-filter-title">学历要求：</span>
              <el-checkbox-group v-model="listQuery.degreeIds" size="small">
                <el-checkbox-button
                        v-for="item in degreeOptions"
                        :label="item.id"
                        :key="item.id"
                        @change="handleFilter"
                >{{ item.name }}</el-checkbox-button
                >
              </el-checkbox-group>
            </div>

            <div class="section1-filter-option" v-show="!moreFilter">
              <el-button
                      class="quick"
                      @click="quickFilter = !quickFilter"
                      size="mini"
              >快速筛选</el-button>
              <el-button
                      class="more"
                      @click="showMoreFilter"
                      size="mini"
              >更多筛选</el-button>
            </div>

          </div>

          <div v-show="moreFilter">
            <!-- 工作经验 -->
            <div class="section1-filter">
              <span class="section1-filter-title">工作经验：</span>
              <el-checkbox-group v-model="listQuery.experienceIds" size="small">
                <el-checkbox-button
                        v-for="item in experienceOptions"
                        :label="item.id"
                        :key="item.id"
                        @change="handleFilter"
                >{{ item.name }}</el-checkbox-button
                >
              </el-checkbox-group>
            </div>

            <!-- 公司规模 -->
            <div class="section1-filter">
              <span class="section1-filter-title">公司规模：</span>
              <el-checkbox-group
                      v-model="listQuery.companyScaleIds"
                      size="small"
              >
                <el-checkbox-button
                        v-for="item in companyScaleOptions"
                        :label="item.id"
                        :key="item.id"
                        @change="handleFilter"
                >{{ item.name }}</el-checkbox-button
                >
              </el-checkbox-group>
            </div>

            <!-- 企业性质 -->
            <div class="section1-filter">
              <span class="section1-filter-title">企业性质：</span>
              <el-checkbox-group
                      v-model="listQuery.companyDefineIds"
                      size="small"
              >
                <el-checkbox-button
                        v-for="item in companyDefineOptions"
                        :label="item.id"
                        :key="item.id"
                        @change="handleFilter"
                >{{ item.name }}</el-checkbox-button
                >
              </el-checkbox-group>
            </div>

            <!-- 工作类型 -->
            <div class="section1-filter">
              <span class="section1-filter-title">工作类型：</span>
              <el-checkbox-group v-model="listQuery.jobTypes" size="small">
                <el-checkbox-button
                        v-for="item in jobTypeOptions"
                        :label="item.id"
                        :key="item.id"
                        @change="handleFilter"
                >{{ item.name }}</el-checkbox-button
                >
              </el-checkbox-group>
            </div>

            <!-- 语言要求 -->
            <div class="section1-filter">
              <span class="section1-filter-title">语言要求：</span>
              <el-checkbox-group v-model="listQuery.lanRequiredIds" size="small">
                <el-checkbox-button
                        v-for="item in lanRequiredOptions"
                        :label="item.id"
                        :key="item.id"
                        @change="handleFilter"
                >{{ item.name }}</el-checkbox-button
                >
              </el-checkbox-group>
            </div>

            <!-- 特殊筛选 -->
            <div class="section1-filter">
              <span id="pointMore" class="section1-filter-title">特殊筛选：</span>
              <el-checkbox-group v-model="listQuery.specialIds" size="small">
                <!--<el-checkbox-button>
                  内推
                </el-checkbox-button>-->
                <el-checkbox-button>
                  留学经验优先
                </el-checkbox-button>
                <!--<el-checkbox-button>
                  线上工作
                </el-checkbox-button>-->
              </el-checkbox-group>
            </div>

            <div class="section1-filter-option" v-show="moreFilter">
              <el-button
                      class="quick"
                      @click="quickFilter = !quickFilter"
                      size="mini"
              >快速筛选</el-button
              >
              <el-button
                      class="more"
                      @click="closeMoreFilter"
                      size="mini"
              >收起</el-button
              >
            </div>
          </div>

          <div v-show="C">
            <div class="inp-search">
              <el-input class="inp-search-child" size="mini" :placeholder="placeholderCity" :readonly="true"></el-input>
              <el-input class="inp-search-child" size="mini" :placeholder="placeholderIndustry" :readonly="true"></el-input>
              <el-input class="inp-search-child" size="mini" :placeholder="placeholderSalary" :readonly="true"></el-input>
              <el-input class="inp-search-child" size="mini" :placeholder="placeholderDegree" :readonly="true"></el-input>
              <el-input class="inp-search-child" size="mini" :placeholder="placeholderExp" :readonly="true"></el-input>
              <div class="section1-filter-option">
                <el-button
                        style="flex: 1; margin-left: 10px;"
                        class="more"
                        @click="reShowMoreFilter"
                        size="mini"
                >更多筛选</el-button>
              </div>

              <el-link style="margin-left: 60px;" class="sort-options" target="_blank">推荐排序</el-link>
              <el-link class="sort-options" target="_blank" @click="onOrderPubTime">最新发布</el-link>
              <el-link class="sort-options" target="_blank" @click="onOrderSalary">薪资降序</el-link>
            </div>
          </div>

          <div v-show="quickFilter" class="quickFilter">
            <div class="quickFilter-left">
              <span class="quickFilter-title">搜索记录：</span>
              <div class="quickFilter-caption" v-if="this.historyOptions.length <= 0">
                您还没有搜索过职位哟，赶紧试一试吧
              </div>
              <div class="quickFilter-caption" v-if="this.historyOptions.length > 0">
                <div class="quickFilter-history-box" v-for="(item, index) in this.historyOptions">
                  <div>
                    <span class="filter-keyword">{{item.keyword}}</span>
                    <span class="filter-count">共{{item.filterCount}}个筛选条件</span>
                  </div>
                  <div class="filter-history-info">
                    <span v-for="(cityValue, index) in item.cityValues"
                          :id="item.cityIds[index]"
                          class="city-value"
                          @click="reHandleFilter(item.cityIds[index], 'cityIds')"
                    >{{cityValue}}</span>
                    <span style="margin-left: 6px;"></span>
                    <span v-for="(industryValue, index) in item.industryValues"
                          :id="item.industryIds[index]"
                          class="industry-value"
                          @click="reHandleFilter(item.industryIds[index], 'companyIndustryIds')"
                    >{{industryValue}}</span>
                    <span style="margin-left: 6px;"></span>
                    <span v-for="(salaryValue, index) in item.salaryValues"
                          :id="item.salaryIds[index]"
                          class="salary-value"
                          @click="reHandleFilter(item.salaryIds[index], 'salaryRangeIds')"
                    >{{salaryValue}}</span>
                    <span style="margin-left: 6px;"></span>
                    <span v-for="(degreeValue, index) in item.degreeValues"
                          :id="item.degreeIds[index]"
                          class="degree-value"
                          @click="reHandleFilter(item.degreeIds[index], 'degreeIds')"
                    >{{degreeValue}}</span>
                  </div>
                </div>
              </div>
            </div>
            <div class="quickFilter-right">
              <span class="quickFilter-title">订阅的职位：</span>
              <div v-if="!this.subscribeOptions">
                <div class="quickFilter-caption">
                  暂未订阅职位哦，点击下方及时获取职位信息
                </div>
                <div class="quickFilter-right-click">
                  <svg-icon
                          icon-class="joblistfilterplus"
                          style="height: 20px; width: 20px"
                  />
                  <span class="quickFilter-right-click-text" @click="handleSubscribe">点我订阅职位</span>
                </div>
              </div>
              <div v-if="this.subscribeOptions" class="quickFilter-caption">
                <div class="quickFilter-history-box">
                  <div>
                    <span class="filter-keyword">{{this.subscribeOptions.keyword}}</span>
                    <span class="filter-count">共{{this.subscribeOptions.filterCount}}个筛选条件</span>
                  </div>
                  <div class="filter-history-info">
                    <span v-for="(cityValue, index) in this.subscribeOptions.cityValues"
                          :id="this.subscribeOptions.cityIds[index]"
                          class="city-value"
                          @click=""
                    >{{cityValue}}</span>
                    <span style="margin-left: 6px;"></span>
                    <span v-for="(industryValue, index) in this.subscribeOptions.industryValues"
                          :id="this.subscribeOptions.industryIds[index]"
                          class="industry-value"
                    >{{industryValue}}</span>
                    <span style="margin-left: 6px;"></span>
                    <span v-for="(salaryValue, index) in this.subscribeOptions.salaryValues"
                          :id="this.subscribeOptions.salaryIds[index]"
                          class="salary-value"
                    >{{salaryValue}}</span>
                    <span style="margin-left: 6px;"></span>
                    <span v-for="(degreeValue, index) in this.subscribeOptions.degreeValues"
                          :id="this.subscribeOptions.degreeIds[index]"
                          class="degree-value"
                    >{{degreeValue}}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="section1-wrapper section1-wrapper-collapse" v-else>
        <div class="section1-container section1-collapse">
          <el-tag
                  class="section1-collapse-tag"
                  v-if="selectedIndustry && selectedIndustry.length > 0"
          >{{ selectedIndustry }}
          </el-tag>
          <el-tag
                  class="section1-collapse-tag"
                  v-if="selectedSalary && selectedSalary.length > 0"
          >{{ selectedSalary }}
          </el-tag>
          <el-tag
                  class="section1-collapse-tag"
                  v-if="selectedScale && selectedScale.length > 0"
          >{{ selectedScale }}
          </el-tag>
          <el-tag
                  class="section1-collapse-tag"
                  v-if="selectedJobType && selectedJobType.length > 0"
          >{{ selectedJobType }}
          </el-tag>
          <el-button
                  class="more"
                  @click="(moreFilter = true), goToTop()"
                  size="mini"
          >更多筛选</el-button
          >
        </div>
      </div>
    </Affix>
    <el-button style="position: absolute; top: 110px; left: 1520px; color: #2d3436; font-size: 14px;" class="empty" @click="emptyFilter" type="text"
    ><svg-icon
            class="empty-icon"
            icon-class="joblistdelete"
            style="height: 19px; width: 19px; margin-right: 6px; margin-bottom: 2px;"
    />清除选项</el-button
    >
    <div class="app-container">
      <div class="section2-container" style="padding-bottom: 40px; padding-top: 70px;">
        <pagination
                v-show="total"
                :total="total"
                :page.sync="listQuery.page"
                :limit.sync="listQuery.limit"
                @pagination="handleRouteList"
        />
      </div>
      <div v-if="showNoResult" style="text-align: center; line-height: 40px">
        暂无搜索结果，显示推荐职位
      </div>
      <div
              class="section3-container"
              v-if="pageResult.list && pageResult.list.length !== 0"
      >
        <el-card
                v-show="job.salary && job.companyUser.company.name !== ''"
                shadow="never"
                :body-style="{ padding: '0px' }"
                v-for="job in pageResult.list"
                :key="job.id"
                class="section3-item-container"
                @click.native="openJobDetail(job.id)"
        >
          <div class="section3-left-container">
            <h6 class="section3-job-name" >{{ job.name }}</h6>
            <div class="recruit-type" v-if="job.recruitType === 154">内推</div>
            <div>
              <b class="section3-salary" style="font-size: 16px;"
              >{{ job.salary.name}} · {{ job.salaryMonths ? `${job.salaryMonths}薪` : "12薪" }}</b
              >
              <span class="section3-city-degree" style="font-size: 15px;">{{
                `${job.city ? job.city.name : ""} / ${
                  job.minDegree ? job.minDegree.name : ""
                }`
              }}</span>
              <el-link class="chat-link" @click="handleChat($event, job.creatorId, job.id)" :underline="false" icon="el-icon-chat-dot-round">聊一聊</el-link>
            </div>
          </div>
          <div
                  class="section3-right-container"
                  v-if="job.companyUser && job.companyUser.company"
          >
            <div class="section3-company-info-container">
              <h6 class="section3-company-name">
                {{ job.companyUser.company.name }}
              </h6>
              <div class="section3-company-tag">
                <el-tag v-if="job.companyUser.company.industry">
                  {{ job.companyUser.company.industry.name }}
                </el-tag>
                <el-tag v-if="job.companyUser.company.stage">
                  {{ job.companyUser.company.stage.name }}
                </el-tag>
                <el-tag v-if="job.companyUser.company.scale">
                  {{ job.companyUser.company.scale.name }}
                </el-tag>
              </div>
            </div>
            <el-link :href="`/company/${job.companyUser.company.id}`">
              <el-image
                      class="section3-logo"
                      :src="job.companyUser.company.logo"
                      v-if="
                  job.companyUser.company.logo &&
                  job.companyUser.company.logo !== ''
                "
              >
                <div slot="error" class="section3-logo-error">
                  <i class="el-icon-picture-outline"></i>
                </div>
              </el-image>
            </el-link>
          </div>
        </el-card>
      </div>
      <div class="section2-container">
        <pagination
                v-show="total"
                :total="total"
                :page.sync="listQuery.page"
                :limit.sync="listQuery.limit"
                @pagination="handleRouteList"
        />
      </div>
    </div>
  </div>
</template>

<script>
  import { listByType, listByTypeAll } from "@/api/dict_api";
  import { searchJob } from "@/api/job_api";
  import Pagination from "@/components/Pagination";
  import { mapGetters } from "vuex";

  import { formatListQuery, parseListQuery } from "@/utils/common";
  import { getRecommendList } from "@/api/recommend_api";
  import { getCityIdByName } from "@/api/city_api";

  export default {
    name: "JobListPage",
    components: { Pagination },
    computed: {
      ...mapGetters(["keyword"]),
    },
    data() {
      return {
        moreFilter: false,
        quickFilter: false,
        collapse: false,
        paneLoading: true,

        selectedSalary: "",
        selectedIndustry: "",
        selectedScale: "",
        selectedJobType: "",

        A: true,
        C: false,

        placeholderCity: "城市",
        placeholderIndustry: "行业",
        placeholderSalary: "月薪",
        placeholderDegree: "学历",
        placeholderExp: "工作经验",

        loading: false,
        inpCityOptions: [],
        inpCity: [],

        historyForm: {
          keyword: undefined,
          cityIds: [],
          salaryIds: [],
          degreeIds: [],
          industryIds: [],
          filterCount: undefined
        },
        historyOptions: [],
        subscribeOptions: undefined,
        listQuery: {
          keyword: "",
          salaryRangeIds: [],
          jobTypes: [],
          cityIds: [],
          companyIndustryIds: [],
          companyScaleIds: [],
          companyStageIds: [],
          companyDefineIds: [],
          degreeIds: [],
          experienceIds: [],
          categoryIds: [],
          lanRequiredIds: [],
          specialIds: [],
          page: 1,
          limit: 10,
          sort: undefined,
          salaryAsc: 0
        },
        orderPubTime: '+PUB_TIME',
        total: 0,
        pageResult: {},
        unlimitedMap: {},
        salaryRangeOptions: [],
        companyScaleOptions: [],
        companyIndustryOptions: [],
        jobTypeOptions: [],
        cityOptions: [],
        cityOptionsAll: [],
        degreeOptions: [],
        experienceOptions: [],
        companyDefineOptions: [],
        lanRequiredOptions: [],
        showNoResult: false,
        cityIdProps: {
          multiple: true,
          lazy: false,
          // lazyLoad: (node, resolve) => {
          //     if (node.level === 1) {
          //         this.$axios.request({
          //             url: "/city/list",
          //             method: "get",
          //             params: {type: node.value}
          //         }).then(data => {
          //             let nodes = data.data.map(second => {
          //                 let children = second.children && second.children.map(third => {
          //                     return {id: third.id, name: third.name, leaf: true}
          //                 })
          //                 return {id: second.id, name: second.name, children}
          //             });
          //             resolve(nodes);
          //         })
          //     } else {
          //         resolve();
          //     }
          // },
          expandTrigger: "hover",
          value: "id",
          label: "name",
          emitPath: false,
          children: "children",
        },
      };
    },
    created() {
      this.initData();
      this.getList();
    },
    mounted() {
      window.addEventListener("scroll", this.checkScrollFilter);
    },
    watch: {
      $route() {
        this.getList();
      },
      keyword() {
        this.listQuery.keyword = this.keyword;
        this.handleRouteList();
      },
    },
    methods: {
      initData() {
        this.paneLoading = true;
        this.$axios.get("/history/get-job-history").then(resp => {
          if (resp !== undefined && resp.data.length >= 0){
            this.historyOptions = resp.data;
          }
        });
        this.$axios.get("/subscribe/search-job-history").then(resp => {
          if (resp !== undefined && this.checkSubscribeData(resp.data)) {
            this.subscribeOptions = resp.data;
          }
        });
        listByType(2).then(
                (response) => {
                  let names = [];
                  this.cityOptions = response.data.list;
                  this.buildUnlimitedMap(this.cityOptions, "city");
                  for (let i = 1; i < this.cityOptions.length; i++) {
                    names[i] = this.cityOptions[i].name;
                  }
                  this.$axios.post("/city/get-city-id-by-name", {cityNames: names}).then(resp => {
                    for (let i = 1; i < resp.data.length; i++) {
                      this.cityOptions[i].id = resp.data[i - 1];
                    }
                  });
                }
        );
        listByType(5).then(
                (response) => {
                  this.companyScaleOptions = response.data.list;
                  this.buildUnlimitedMap(this.companyScaleOptions, "scale");
                }
        );
        listByType(6).then(
                (response) => {
                  this.companyIndustryOptions = response.data.list;
                  this.buildUnlimitedMap(this.companyIndustryOptions, "industry");
                }
        );
        listByType(8).then(
                (response) => {
                  this.jobTypeOptions = response.data.list;
                  this.buildUnlimitedMap(this.jobTypeOptions, "jobType");
                }
        );
        listByType(9).then(
                (response) => {
                  this.salaryRangeOptions = response.data.list;
                  this.buildUnlimitedMap(this.salaryRangeOptions, "salary");
                }
        );
        listByType(1).then(
                (response) => {
                  this.degreeOptions = response.data.list;
                  this.buildUnlimitedMap(this.degreeOptions, "degree");
                }
        );
        listByType(13).then(
                (response) => {
                  this.experienceOptions = response.data.list;
                  this.buildUnlimitedMap(this.experienceOptions, "exp");
                }
        );
        listByType(7).then(
                (response) => {
                  this.companyDefineOptions = response.data.list;
                  this.buildUnlimitedMap(this.companyDefineOptions, "define");
                }
        );
        this.$axios.request({
          url: "/skill-tag/list",
          method: "get",
          params: {
            type: 163,
            page: 1,
            limit: 10,
            sort: '+id'
          }
        }).then((resp) => {
          this.lanRequiredOptions = resp.data.list;
          this.buildUnlimitedMap(this.lanRequiredOptions, "lang");
        });
        this.paneLoading = false;
      },
      goToTop() {
        let top = document.documentElement.scrollTop || document.body.scrollTop;
        // 实现滚动效果
        const timeTop = setInterval(() => {
          document.body.scrollTop = document.documentElement.scrollTop = top -= 50;
          if (top <= 0) {
            clearInterval(timeTop);
          }
        }, 10);
      },
      checkScrollFilter() {
        if (this.A && !this.moreFilter) {
          let top1 = document.getElementById("pointA").offsetTop;
          let gun = document.documentElement.scrollTop || document.body.scrollTop;
          let top = top1 - gun;
          if (top <= 0) {
            this.closeMoreFilter();
          }else{
            this.reShowMoreFilter();
          }
        }

        if (this.moreFilter && this.A) {
          let top1 = document.getElementById("pointMore").offsetTop;
          let gun = document.documentElement.scrollTop || document.body.scrollTop;
          let top = top1 - gun;
          if (top <= 0) {
            this.closeMoreFilter();
          }else{
            this.reShowMoreFilter();
          }
        }
      },
      showMoreFilter() {
        this.A = true;
        this.moreFilter = true;
        this.C = false;
      },
      checkSubscribeData(data) {
        if (data === undefined) return false;
        return !(data.cityIds.length <= 0 && data.degreeIds.length <= 0 && data.industryIds.length <= 0 && data.salaryIds.length <= 0);
      },
      closeMoreFilter() {
        this.A = false;
        this.moreFilter = false;
        this.C = true;
      },
      reShowMoreFilter() {
        this.A = true;
        this.moreFilter = true;
        this.C = false;
      },
      buildUnlimitedMap(options, key) {
        for (let i = 0; i < options.length; i++) {
          if (options[i].name === "不限") {
            this.unlimitedMap[key] = options[i].id;
          }
        }
      },
      /*scrollPane(status) {
        this.collapse = status;
        this.selectedSalary = "";
        this.selectedIndustry = "";
        this.selectedScale = "";
        this.selectedJobType = "";

        for (let i = 0; i < this.salaryRangeOptions.length; i++) {
          let item = this.salaryRangeOptions[i];
          if (this.listQuery.salaryRangeIds.indexOf(item.id) !== -1) {
            this.selectedSalary += item.name + "，";
          }
        }
        for (let i = 0; i < this.companyIndustryOptions.length; i++) {
          let item = this.companyIndustryOptions[i];
          if (this.listQuery.companyIndustryIds.indexOf(item.id) !== -1) {
            this.selectedIndustry += item.name + "，";
          }
        }
        for (let i = 0; i < this.companyScaleOptions.length; i++) {
          let item = this.companyScaleOptions[i];
          if (this.listQuery.companyScaleIds.indexOf(item.id) !== -1) {
            this.selectedScale += item.name + "，";
          }
        }

        for (let i = 0; i < this.jobTypeOptions.length; i++) {
          let item = this.jobTypeOptions[i];
          if (this.listQuery.jobTypes.indexOf(item.id) !== -1) {
            this.selectedJobType += item.name + "，";
          }
        }

        this.selectedSalary = this.selectedSalary.substr(
                0,
                this.selectedSalary.length - 1
        );
        this.selectedIndustry = this.selectedIndustry.substr(
                0,
                this.selectedIndustry.length - 1
        );
        this.selectedScale = this.selectedScale.substr(
                0,
                this.selectedScale.length - 1
        );
        this.selectedJobType = this.selectedJobType.substr(
                0,
                this.selectedJobType.length - 1
        );
      },*/
      onOrderPubTime() {
        this.listQuery.sort = this.orderPubTime;
        this.handleFilter();
      },
      onOrderSalary() {
        this.listQuery.salaryAsc = 0;
        this.listQuery.limit = 100000;
        this.$axios.post("/job/search-job-order-by-salary", this.listQuery).then(
                (resp) => {
                  if (!resp.data.list || resp.data.list.length === 0) {
                    this.showNoResult = true;
                    this.total = 10;
                    getRecommendList({
                      objectType: 1, // 职位
                      page: 1,
                      limit: 10,
                      sort: "+position",
                    }).then((resp) => {
                      this.pageResult.list = resp.data.list.map(
                              (item) => item.object
                      );
                      this.total = resp.data.total;
                      this.$emit("complete");
                    });
                  } else {
                    this.pageResult = resp.data;
                    this.total = this.pageResult.total;
                    this.$emit("complete");
                  }
                }
        );

      },
      inpCityChange() {
        if (this.inpCity === undefined || this.inpCity.length === 0) {
          return;
        }
        this.$axios.post("/city/get-city-id-by-name", {cityNames: this.inpCity}).then(resp => {
          this.listQuery.cityIds = resp.data;
          this.handleFilter();
        })
      },
      reHandleFilter(id, key) {
        this.emptyFilter();
        this.listQuery[key].push(id);
        this.listQuery.page = 1;
        this.saveSearchHistory();
        this.refreshOptions();
        this.handleRouteList();
      },
      handleFilter() {
        this.listQuery.page = 1;
        this.saveSearchHistory();
        this.refreshOptions();
        this.handleRouteList();
      },
      saveSearchHistory() {
        this.formatHistoryForm();
        this.$axios.post("/history/save", this.historyForm).then(resp => {
        });
      },
      handleChat(event, creatorId, jobId) {
        this.$router.push({
          path: "/chat",
          query: { toUser: creatorId, jobId: jobId },
        });
        event.stopPropagation();
      },
      handleSubscribe() {
        this.$router.push({
          path: "/favorites",
        });
      },
      formatHistoryForm() {
        if (this.$route.query.searchWord !== undefined) {
          this.historyForm.keyword = this.$route.query.searchWord;
        }

        this.historyForm.cityIds = this.arrCopy(this.listQuery.cityIds);
        // this.historyForm.cityIds = this.listQuery.cityIds;
        this.historyForm.degreeIds = this.listQuery.degreeIds;
        this.historyForm.salaryIds = this.listQuery.salaryRangeIds;
        this.historyForm.industryIds = this.listQuery.companyIndustryIds;

        this.removeEl(this.historyForm.cityIds);
        this.historyForm.filterCount = this.historyForm.cityIds.length
                + this.historyForm.degreeIds.length
                + this.historyForm.salaryIds.length
                + this.historyForm.industryIds.length;
      },
      arrCopy(originArr) {
        let newArr = [];
        for (let i = 0; i < originArr.length; i++) {
          newArr.push(originArr[i]);
        }
        return newArr;
      },
      removeEl(arr) {
        let i = arr.indexOf(this.unlimitedMap["city"]);
        if (i > -1) {
          arr.splice(i, 1);
        }
      },
      refreshOptions() {
        if (this.listQuery.cityIds.indexOf(this.unlimitedMap["city"]) !== -1) {
          this.listQuery.cityIds = [];
          this.placeholderCity = "不限";
        }
        if (this.listQuery.companyIndustryIds.indexOf(this.unlimitedMap["industry"]) !== -1) {
          this.listQuery.companyIndustryIds = [];
          this.placeholderIndustry = "不限";
        }
        if (this.listQuery.salaryRangeIds.indexOf(this.unlimitedMap["salary"]) !== -1) {
          this.listQuery.salaryRangeIds = [];
          this.placeholderSalary = "不限";
        }
        if (this.listQuery.degreeIds.indexOf(this.unlimitedMap["degree"]) !== -1) {
          this.listQuery.degreeIds = [];
          this.placeholderDegree = "不限";
        }
        if (this.listQuery.experienceIds.indexOf(this.unlimitedMap["exp"]) !== -1) {
          this.listQuery.experienceIds = [];
          this.placeholderExp = "不限";
        }
        if (this.listQuery.companyScaleIds.indexOf(this.unlimitedMap["scale"]) !== -1) {
          this.listQuery.companyScaleIds = [];
        }
        if (this.listQuery.companyDefineIds.indexOf(this.unlimitedMap["define"]) !== -1) {
          this.listQuery.companyDefineIds = [];
        }
        if (this.listQuery.jobTypes.indexOf(this.unlimitedMap["jobType"]) !== -1) {
          this.listQuery.jobTypes = [];
        }
        if (this.listQuery.lanRequiredIds.indexOf(this.unlimitedMap["lang"]) !== -1) {
          this.listQuery.lanRequiredIds = [];
        }

        this.placeholderCity = this.getNameByIdFromOptions(this.cityOptions, this.listQuery.cityIds, "城市");
        this.placeholderIndustry = this.getNameByIdFromOptions(this.companyIndustryOptions, this.listQuery.companyIndustryIds, "行业");
        this.placeholderSalary = this.getNameByIdFromOptions(this.salaryRangeOptions, this.listQuery.salaryRangeIds, "月薪");
        this.placeholderDegree = this.getNameByIdFromOptions(this.degreeOptions, this.listQuery.degreeIds, "学历");
        this.placeholderExp = this.getNameByIdFromOptions(this.experienceOptions, this.listQuery.experienceIds, "工作经验");
      },
      getNameByIdFromOptions(options, ids, origin) {
        let name = "";
        for (let i = 0; i < options.length; i++) {
          for (let j = 0; j < ids.length; j++) {
            if (options[i].id === ids[j]) {
              name += options[i].name + ",";
            }
          }
        }
        if (name === "") {
          return origin;
        }
        if (name.endsWith(",")) {
          return name.substr(0, name.length - 1);
        }
      },
      emptyFilter() {
        this.listQuery = {
          keyword: "",
          salaryRangeIds: [],
          jobTypes: [],
          cityIds: [],
          companyIndustryIds: [],
          companyScaleIds: [],
          companyStageIds: [],
          companyDefineIds: [],
          degreeIds: [],
          experienceIds: [],
          categoryIds: [],
          lanRequiredIds: [],
          specialIds: [],
          page: 1,
          limit: 10,
          sort: undefined,
          salaryAsc: 0
        };
        this.refreshOptions();
        this.handleRouteList();
      },
      getList() {
        this.showNoResult = false;
        parseListQuery(this.$route.query, this.listQuery);
        searchJob(this.listQuery).then((response) => {
          if (!response.data.list || response.data.list.length === 0) {
            this.showNoResult = true;
            this.total = 10;
            getRecommendList({
              objectType: 1, // 职位
              page: 1,
              limit: 10,
              sort: "+position",
            }).then((response) => {
              this.pageResult.list = response.data.list.map(
                      (item) => item.object
              );
              this.total = response.data.total;
              this.$emit("complete");
            });
          } else {
            this.pageResult = response.data;
            this.total = this.pageResult.total;
            this.$emit("complete");
          }
        });
      },
      handleRouteList() {
        this.$router.replace({
          path: this.$route.path,
          query: formatListQuery(this.listQuery),
        });
      },
      openJobDetail(id) {
        this.$router.push(`/job/${id}`);
      },
    },
    beforeDestroy() {
      window.removeEventListener("scroll",this.checkScrollFilter)
    },
  };
</script>

<style scoped lang="scss">
  .section1-wrapper-collapse {
    height: 38px;
    .section1-collapse {
      display: flex;
      flex-direction: row;
      align-items: center;
      /deep/ .el-tag + .el-tag {
        margin-left: 15px;
      }
      .section1-collapse-tag {
        background: #f4f5f8;
        border-radius: 17px;
        border: 1px solid #698ec7;
        height: 25px;
        width: fit-content;
        display: flex;
        align-items: center;

        span {
          font-size: 14px;
          font-family: PingFangSC-Medium, PingFang SC;
          font-weight: 500;
          color: #698ec7;
          line-height: 20px;
        }
      }
      /deep/ .el-button {
        margin-left: 16px;
        background: #4895ef;
        border-radius: 17px;
        width: 86px;
        font-size: 14px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #ffffff;
        line-height: 20px;
        padding-top: 3px;
        padding-bottom: 3px;
      }
    }
  }

  .background-wrapper {
    background: #f6f9fc;
    max-width: 3000px !important;
    padding: 0 !important;
    margin-top: 0 !important;
  }

  .section1-wrapper {
    width: 100%;
    background: #fbfbfb;
    box-shadow: 0px 4px 16px 3px rgba(191, 199, 215, 0.31);
    margin-bottom: 31px;
    display: flex;
    justify-content: center;
    padding: 31px 0px;
    position: sticky;
    position: -webkit-sticky; // 兼容 -webkit 内核的浏览器
    top: 10px; // 必须设一个值，否则不生效

    .section1-container {
      max-width: 1140px;
      flex: 1;
      padding: 0px 30px;

      .sort-options {
        justify-content: flex-end;
        margin-left: 5px;
        margin-right: 5px;
      }

      .inp-search {
        display: flex;
        justify-content: space-between;

        .inp-search-child {
          flex: 1;
          margin-left: 5px;
          margin-right: 5px;
        }

      }

    }

    .quickFilter {
      display: flex;
      flex-direction: row;
      background: #eef5ff;
      border-radius: 14px;
      height: 136px;
      padding: 13px 43px 17px 29px;
      margin-bottom: 50px;
      margin-top: 17px;

      .quickFilter-title {
        margin-bottom: 9px;
        font-size: 16px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #4895ef;
        line-height: 22px;
      }
      .quickFilter-caption {
        font-size: 16px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #666666;
        line-height: 22px;
        display: flex;

        .quickFilter-history-box {
          border-radius: 4px;
          border: 1px solid #636e72;
          padding: 2px 4px;
          /*margin-left: 10px;*/
          margin-right: 15px;
          margin-top: 15px;
          flex: 1;

          white-space: nowrap;
          overflow: hidden;
          text-overflow:ellipsis;

          .filter-keyword {
            font-size: 14px;
            color: #333333;
            margin-left: 6px;
            margin-right: 8px;
            position: relative;
            bottom: 2px;
          }

          .filter-history-info {
            font-size: 11px;
            color: #7F7F7F;

            white-space: nowrap;
            overflow: hidden;
            text-overflow:ellipsis;

            span {
              margin-left: 3px;
              cursor: pointer;
            }

            /*.city-value:nth-child(1) {
              margin-left: 6px;
            }

            .city-value:after {
              content: ' ';
            }

            .industry-value:nth-child(2) {
              margin-left: 6px;
            }

            .salary-value:nth-child(3) {
              margin-left: 6px;
            }*/

          }


          .filter-count {
            font-size: 11px;
            color: #027DB4;
            cursor: pointer;
          }


        }


      }

      .quickFilter-left {
        flex: 3;
      }
      .quickFilter-right {
        flex: 1;
        margin-left: 25px;
        .quickFilter-right-click {
          margin-top: 9px;
          display: flex;
          align-items: center;
          .quickFilter-right-click-text {
            margin-left: 4px;
            font-size: 16px;
            font-family: PingFangSC-Medium, PingFang SC;
            font-weight: 500;
            color: #333333;
            line-height: 22px;
            cursor: pointer;
          }
        }
      }
    }
    .section1-filter {
      display: flex;
      flex-direction: row;
      align-items: center;
      margin-bottom: 11px;

      .inp-city {
        width: 230px;
        margin-left: 50px;
      }

      .section1-filter-title {
        font-size: 16px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #333333;
        line-height: 22px;
      }
      /deep/ .el-checkbox-button {
        margin-bottom: 0px;
      }
      /deep/ .el-checkbox-button + .el-checkbox-button {
        margin-left: 16px;
      }
      /deep/ .el-checkbox-button .el-checkbox-button__inner {
        background: #f5f5f5;
        border-radius: 17px;
        border: 0px;
        font-size: 14px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #333333;
        line-height: 20px;
        padding: 3px 12px;
      }
      /deep/ .is-checked {
        background: #f4f5f8;
        border-radius: 17px;
        border: 1px solid #698ec7;

        .el-checkbox-button__inner {
          font-size: 14px;
          font-family: PingFangSC-Medium, PingFang SC;
          font-weight: 500;
          color: #698ec7;
          line-height: 20px;
        }
      }
    }
    .section1-filter-option {
      display: flex;
      justify-content: flex-end;

      /deep/ .el-button {
        border-radius: 17px;
        width: 86px;
        font-size: 14px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #ffffff;
        line-height: 20px;
        padding-top: 3px;
        padding-bottom: 3px;
      }

      .quick {
        background: #9ccc65;
      }

      .more {
        background: #4895ef;
      }

      .empty {
        background: #fbfbfb;
        font-size: 14px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #333333;
        line-height: 20px;
      }
    }
  }
  .app-container {
    max-width: 1140px;
    margin: 0 auto;
    padding: 0 20px;
    min-height: calc(100vh - 477px);

    .section2-container {
      width: 100%;
      overflow-x: auto;
      padding-bottom: 99px;
      /deep/ .el-pager {
        display: flex;
        align-items: center;
        justify-content: center;
      }
      /deep/ .pagination-container {
        padding: 0px;
      }
      /deep/ .el-pagination {
        display: flex;
        justify-content: center;
        align-items: center;
      }
      /deep/ .el-pagination__total {
        display: none;
      }
      /deep/.el-pagination__sizes {
        display: none;
      }
      /deep/.el-pagination.is-background .el-pager li:not(.disabled).active {
        background: #3d6ff4;
        box-shadow: 0px 5px 11px 0px rgba(30, 150, 252, 0.5);
        border-radius: 100%;
        width: 29px;
        height: 30px;
        font-size: 14px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #ffffff;
        line-height: 20px;
      }
      /deep/ .el-pagination.is-background .btn-next {
        width: 29px;
        height: 29px;
        background: #eeeeee;
        box-shadow: 0px 5px 11px 0px #cccccc;
        border-radius: 100%;
        font-size: 14px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #999999;
        line-height: 20px;
      }
      /deep/ .el-pagination.is-background .btn-prev {
        width: 29px;
        height: 29px;
        background: #eeeeee;
        box-shadow: 0px 5px 11px 0px #cccccc;
        border-radius: 100%;
        font-size: 14px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #999999;
        line-height: 20px;
      }
      /deep/ .el-pagination.is-background .el-pager li {
        width: 29px;
        height: 29px;
        background: #eeeeee;
        box-shadow: 0px 5px 11px 0px #cccccc;
        border-radius: 100%;
        font-size: 14px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #999999;
        line-height: 20px;
        display: flex;
        align-items: center;
        justify-content: center;
      }
      /deep/ .el-pagination__jump {
        margin-left: 13px;
        font-size: 14px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #999999;
        line-height: 20px;
        padding: 4px;
      }
      /deep/.el-input__inner {
        width: 48px;
        height: 23px;
        background: #ffffff;
        border-radius: 7px;
        opacity: 0.7;
        border: 1px solid #cccccc;
      }
      /deep/.el-pagination__editor.el-input {
        margin-left: 7px;
        margin-right: 7px;
      }
    }

    .section3-container {
      min-width: 335px;
      margin-bottom: 62px;

      .chat-link {
        color: #74b9ff;
        text-underline: none;
        position: relative;
        right: 8px;
        z-index: 999;
      }

      .section3-item-container {
        margin-bottom: 16px;
        height: 130px;
        cursor: pointer;
        padding: 33px 31px 35px 33px;
        background: #ffffff;
        box-shadow: 0px 4px 16px 3px rgba(191, 199, 215, 0.31);
        border-radius: 8px;
        border: 0px;

        /deep/ .el-card__body {
          display: flex;
        }
      }

      .section3-left-container {
        flex: 0.6;
        min-width: 290px;

        .recruit-type {
          color: #fff;
          background-color: #ff9f43;
          border-radius: 3px;
          display: inline-block;
          padding: 1px 4px;
          margin-left: 10px;
          vertical-align: top;
        }

        .section3-job-name {
          margin: 0 0 13px 0;
          font-size: 18px;
          font-family: PingFangSC-Medium, PingFang SC;
          font-weight: 500;
          color: #333333;
          line-height: 25px;
          display: inline-block;
          max-width: 205px;

          white-space: nowrap;
          overflow: hidden;
          text-overflow:ellipsis;
        }

        .section3-salary {
          font-size: 18px;
          font-family: PingFangSC-Medium, PingFang SC;
          font-weight: 500;
          color: #4895ef;
          line-height: 25px;
          margin-right: 22px;
        }

        .section3-city-degree {
          margin-right: 22px;
          font-size: 16px;
          font-family: PingFangSC-Regular, PingFang SC;
          font-weight: 400;
          color: #333333;
          line-height: 22px;
        }
      }

      .section3-right-container {
        flex: 1;
        min-width: 290px;
        display: flex;
        justify-content: space-between;

        .section3-company-info-container {
          .section3-company-name {
            margin: 0 0 13px 0;
            font-size: 18px;
            font-family: PingFangSC-Medium, PingFang SC;
            font-weight: 500;
            color: #333333;
            line-height: 25px;
          }
          .section3-company-tag {
            /deep/.el-tag {
              height: 25px;
              padding: 2px 12px;
              background: #f4f5f8;
              border-radius: 4px;
              border: 0px;
              font-size: 14px;
              font-family: PingFangSC-Regular, PingFang SC;
              font-weight: 400;
              color: #698ec7;
              line-height: 20px;
            }
            /deep/ .el-tag + .el-tag {
              margin-left: 7px;
            }
          }
        }

        .section3-logo {
          width: 48px;

          /deep/ .section3-logo-error {
            width: 48px;
            text-align: center;
            font-size: 25px;
          }
        }
      }
    }
  }

  ::-webkit-scrollbar {
    display: none;
  }

  @media screen and (max-width: 850px) {
    .app-container {
      .section3-container {
        .section3-item-container {
          /deep/ .el-card__body {
            display: flex;
            flex-direction: column;
            align-items: center;

            .section3-left-container {
              margin-bottom: 10px;
            }
          }
        }
      }
    }
  }
</style>

