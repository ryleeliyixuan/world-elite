<template>
  <div class="background-wrapper">
    <div
            v-show="A"
            class="section1-wrapper"
            v-if="!collapse"
            v-loading="paneLoading"
    >
      <div class="section1-container">
        <div v-show="A">
          <!-- 所在城市 -->
          <div class="section1-filter">
            <span class="section1-filter-title">所在城市：</span>
            <el-checkbox-group v-model="listQuery.cityIds" size="small">
              <el-checkbox-button
                      v-for="item in cityOptions"
                      :label="item.id"
                      :key="item.id"
                      @change="handleFilter(item.id)"
              >{{ item.name }}</el-checkbox-button
              >
            </el-checkbox-group>

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
                      @change="handleFilter(item.id)"
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
                      @change="handleFilter(item.id)"
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
                      @change="handleFilter(item.id)"
              >{{ item.name }}</el-checkbox-button
              >
            </el-checkbox-group>
          </div>

          <div class="section1-filter-option" v-show="!moreFilter">
            <el-button
                    class="quick"
                    @mouseover.native="quickFilter = true"
                    size="mini"
            >快速筛选</el-button>
            <el-button class="more" @click="showMoreFilter" size="mini"
            >更多筛选</el-button>
            <el-select
                    size="mini"
                    style="margin-left: 20px; margin-right: 15px; max-width: 100px;"
                    v-model="sortValue"
                    @change="sortChange"
                    class="sort-options"
                    placeholder="推荐排序">
              <el-option
                      v-for="item in sortOptions"
                      :key="item.sortValue"
                      :label="item.label"
                      :value="item.sortValue">
              </el-option>
            </el-select>
            <el-button class="empty" @click="emptyFilter" type="text"
            ><svg-icon
                    class="empty-icon"
                    icon-class="joblistdelete"
                    style="
                      height: 19px;
                      width: 19px;
                      margin-right: 6px;
                      margin-bottom: 2px;
                    "
            />清除选项</el-button>
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
                      @change="handleFilter(item.id)"
              >{{ item.name }}</el-checkbox-button
              >
            </el-checkbox-group>
          </div>

          <!-- 公司规模 -->
          <div class="section1-filter">
            <span class="section1-filter-title">公司规模：</span>
            <el-checkbox-group v-model="listQuery.companyScaleIds" size="small">
              <el-checkbox-button
                      v-for="item in companyScaleOptions"
                      :label="item.id"
                      :key="item.id"
                      @change="handleFilter(item.id)"
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
                      @change="handleFilter(item.id)"
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
                      @change="handleFilter(item.id)"
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
                      @change="handleFilter(item.id)"
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
              <el-checkbox-button> 留学经验优先 </el-checkbox-button>
              <!--<el-checkbox-button>
                  线上工作
                </el-checkbox-button>-->
            </el-checkbox-group>
          </div>

          <div class="section1-filter-option" v-show="moreFilter">
            <el-button
                    class="quick"
                    @mouseover.native="quickFilter = true"
                    size="mini"
            >快速筛选</el-button>
            <el-button class="more" @click="closeMoreFilter" size="mini"
            >收起</el-button>
            <el-select
                    size="mini"
                    style="margin-left: 20px; margin-right: 15px; max-width: 100px;"
                    v-model="sortValue"
                    @change="sortChange"
                    class="sort-options"
                    placeholder="推荐排序">
              <el-option
                      v-for="item in sortOptions"
                      :key="item.sortValue"
                      :label="item.label"
                      :value="item.sortValue">
              </el-option>
            </el-select>
            <el-button class="empty" @click="emptyFilter" type="text"
            ><svg-icon
                    class="empty-icon"
                    icon-class="joblistdelete"
                    style="
                      height: 19px;
                      width: 19px;
                      margin-right: 6px;
                      margin-bottom: 2px;
                    "
            />清除选项</el-button>
          </div>
        </div>

        <div
                v-show="quickFilter"
                class="quickFilter"
                @mouseleave="quickFilter = false"
        >
          <div class="quickFilter-left">
            <span class="quickFilter-title">搜索记录：</span>
            <div
                    class="quickFilter-caption"
                    v-if="this.historyOptions.length <= 0"
            >
              您还没有搜索过职位哟，赶紧试一试吧
            </div>
            <div
                    class="quickFilter-caption"
                    v-if="this.historyOptions.length > 0"
            >
              <div
                      class="quickFilter-history-box"
                      v-for="(item, index) in this.historyOptions"
                      @click="reHandleFilter(item)"
              >
                <div>
                  <span class="filter-keyword">{{ item.keyword }}</span>
                  <span class="filter-count"
                  >共{{ item.filterCount }}个筛选条件</span
                  >
                </div>
                <div class="filter-history-info">
                  <span
                          v-for="(cityValue, index) in item.cityValues"
                          :id="item.cityIds[index]"
                          class="city-value"
                  >{{ cityValue === "不限" ? "城市不限" : cityValue }}</span
                  >
                  <span style="margin-left: 6px"></span>
                  <span
                          v-for="(industryValue, index) in item.industryValues"
                          :id="item.industryIds[index]"
                          class="industry-value"
                  >{{
                      industryValue === "不限" ? "行业不限" : industryValue
                    }}</span
                  >
                  <span style="margin-left: 6px"></span>
                  <span
                          v-for="(salaryValue, index) in item.salaryValues"
                          :id="item.salaryIds[index]"
                          class="salary-value"
                  >{{
                      salaryValue === "不限" ? "月薪不限" : salaryValue
                    }}</span
                  >
                  <span style="margin-left: 6px"></span>
                  <span
                          v-for="(degreeValue, index) in item.degreeValues"
                          :id="item.degreeIds[index]"
                          class="degree-value"
                  >{{
                      degreeValue === "不限" ? "学历不限" : degreeValue
                    }}</span
                  >
                </div>
              </div>
            </div>
          </div>
          <!--<div class="quickFilter-right">
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
            </div>-->
        </div>
      </div>
    </div>

    <Affix>
      <div
              v-show="C"
              class="section1-wrapper"
              v-if="!collapse"
              v-loading="paneLoading"
      >
        <div class="section1-container">
          <div v-show="C" class="inp-search">
            <el-input
                    class="inp-search-child"
                    size="mini"
                    :placeholder="placeholderCity"
                    :readonly="true"
            ></el-input>
            <el-input
                    class="inp-search-child"
                    size="mini"
                    :abc="placeholderIndustry"
                    :placeholder="placeholderIndustry"
                    :readonly="true"
            ></el-input>
            <el-input
                    class="inp-search-child"
                    size="mini"
                    :placeholder="placeholderSalary"
                    :readonly="true"
            ></el-input>
            <el-input
                    class="inp-search-child"
                    size="mini"
                    :placeholder="placeholderDegree"
                    :readonly="true"
            ></el-input>
            <el-input
                    class="inp-search-child"
                    size="mini"
                    :placeholder="placeholderExp"
                    :readonly="true"
            ></el-input>
            <div class="section1-filter-option">
              <el-button
                      style="flex: 1; margin-left: 10px; height: 30px;"
                      class="more"
                      @click="reShowMoreFilter"
                      size="mini"
              >更多筛选</el-button
              >
            </div>

            <!--<el-link
              style="margin-left: 60px"
              class="sort-options"
              target="_blank"
              >推荐排序</el-link
            >
            <el-link
              class="sort-options"
              target="_blank"
              @click="onOrderPubTime"
              >最新发布</el-link
            >
            <el-link class="sort-options" target="_blank" @click="onOrderSalary"
              >薪资降序</el-link
            >-->
            <el-select
                    size="mini"
                    v-model="sortValue"
                    @change="sortChange"
                    class="sort-options"
                    placeholder="推荐排序">
              <el-option
                      v-for="item in sortOptions"
                      :key="item.sortValue"
                      :label="item.label"
                      :value="item.sortValue">
              </el-option>
            </el-select>

            <el-button class="empty" @click="emptyFilter" type="text" style="position: relative; bottom: 8px;"
            ><svg-icon
                    class="empty-icon"
                    icon-class="joblistdelete"
                    style="
                  height: 19px;
                  width: 19px;
                  margin-right: 6px;
                  margin-bottom: 2px;
                "
            />清除选项</el-button
            >
          </div>
        </div>
      </div>
    </Affix>

    <div
            class="section1-wrapper section1-wrapper-collapse"
            v-if="this.collapse"
    >
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
    <!--</Affix>-->
    <!--<el-button style="position: fixed; top: 110px; left: 1520px; color: #2d3436; font-size: 14px;" class="empty" @click="emptyFilter" type="text"
    ><svg-icon
            class="empty-icon"
            icon-class="joblistdelete"
            style="height: 19px; width: 19px; margin-right: 6px; margin-bottom: 2px;"
    />清除选项</el-button
    >-->
    <div class="app-container">
      <div v-if="showNoResult" style="text-align: center; line-height: 40px">
        暂无搜索结果，显示全部职位
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
            <h6 class="section3-job-name">{{ job.name }}</h6>
            <div class="recruit-type" v-if="job.recruitType === 154">内推</div>
            <div>
              <b class="section3-salary" style="font-size: 16px">
                <!--{{ job.salary.name}} · {{ job.salaryMonths ? `${job.salaryMonths}薪` : "12薪" }}-->
                {{ job.salary.name }}
              </b>
              <span class="section3-city-degree" style="font-size: 15px">
                {{
                  `${job.city ? job.city.name : ""}/${
                    job.minDegree ? job.minDegree.name.substring(0, 2) : ""
                  }`
                }}
              </span>
              <el-link
                      class="chat-link"
                      @click="handleChat($event, job.creatorId, job.id)"
                      :underline="false"
                      icon="el-icon-chat-dot-round"
              >聊一聊</el-link
              >
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
                <el-tag v-if="job.companyUser.company.property">
                  {{ job.companyUser.company.property.name }}
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
  import { searchJob, getCompanyJobList } from "@/api/job_api";
  import Pagination from "@/components/Pagination";
  import { mapGetters } from "vuex";

  import { formatListQuery, parseListQuery } from "@/utils/common";
  import { getRecommendList } from "@/api/recommend_api";
  import { getCityIdByName } from "@/api/city_api";

  export default {
    name: "JobListPage",
    components: { Pagination },
    data() {
      return {
        sortValue: '',
        sortOptions: [
          {
            sortValue: "1",
            label: "推荐排序"
          },
          {
            sortValue: "2",
            label: "最新发布",
          },
          {
            sortValue: "3",
            label: "薪资降序"
          }
        ],
        quickFilter: false,
        collapse: false,
        paneLoading: true,
        selectedSalary: "",

        selectedIndustry: "",
        selectedScale: "",
        selectedJobType: "",

        A: true,
        C: false,
        moreFilter: false,

        lastA: true,
        lastC: false,
        lastMoreFilter: false,

        placeholderCity: "城市不限",
        placeholderIndustry: "行业不限",
        placeholderSalary: "月薪不限",
        placeholderDegree: "学历不限",
        placeholderExp: "工作经验不限",

        initCityIds: [],
        initIndustryIds: [],
        initSalaryIds: [],
        initDegreeIds: [],
        initExpIds: [],
        initJobTypeIds: [],
        initStageIds: [],
        initDefineIds: [],
        initCategoryIds: [],
        initLanRequiredIds: [],
        initScaleIds: [],

        loading: false,
        inpCityOptions: [],
        inpCity: [],

        historyForm: {
          keyword: undefined,
          cityIds: [],
          salaryIds: [],
          degreeIds: [],
          industryIds: [],
          expIds: [],
          scaleIds: [],
          defineIds: [],
          jobTypeIds: [],
          lanIds: [],
          specialIds: [],
          filterCount: undefined,
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
          salaryAsc: 0,
        },
        orderPubTime: "-PUB_TIME",
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
      // this.getList();
    },
    mounted() {
      window.addEventListener("scroll", this.checkScrollFilter);
    },
    watch: {
      $route() {
        this.getList();
      },
      keyword() {
        this.listQuery.keyword = this.$route.query.searchWord;
        this.handleRouteList();
      },
    },
    methods: {
      initData() {
        this.paneLoading = true;
        this.$axios.get("/history/get-job-history").then((resp) => {
          if (resp !== undefined && resp.data.length >= 0) {
            this.historyOptions = resp.data;
          }
        });
        this.$axios.get("/subscribe/search-job-history").then((resp) => {
          if (resp !== undefined && this.checkSubscribeData(resp.data)) {
            this.subscribeOptions = resp.data;
          }
        });
        getCompanyJobList().then((response) => {
          this.pageResult = response.data;
          this.total = this.pageResult.total;
          this.$emit("complete");
        });
        listByType(2).then((response) => {
          let names = [];
          this.cityOptions = response.data.list;
          this.buildUnlimitedMap(this.cityOptions, "city");
          this.listQuery.cityIds.push(this.unlimitedMap["city"]);
          for (let i = 1; i < this.cityOptions.length; i++) {
            names[i] = this.cityOptions[i].name;
          }
          this.$axios
                  .post("/city/get-city-id-by-name", { cityNames: names })
                  .then((resp) => {
                    for (let i = 1; i < resp.data.length; i++) {
                      this.cityOptions[i].id = resp.data[i - 1];
                    }
                    this.refreshOptions();
                    this.buildInitIds(this.initCityIds, this.cityOptions);
                  });
        });
        listByType(5).then((response) => {
          this.companyScaleOptions = response.data.list;
          this.buildUnlimitedMap(this.companyScaleOptions, "scale");
          this.listQuery.companyScaleIds.push(this.unlimitedMap["scale"]);
          this.buildInitIds(this.initScaleIds, this.companyScaleOptions);
        });
        listByType(6).then((response) => {
          this.companyIndustryOptions = response.data.list;
          this.buildUnlimitedMap(this.companyIndustryOptions, "industry");
          this.listQuery.companyIndustryIds.push(this.unlimitedMap["industry"]);
          this.buildInitIds(this.initIndustryIds, this.companyIndustryOptions);
          this.refreshOptions();
        });
        listByType(8).then((response) => {
          this.jobTypeOptions = response.data.list;
          this.buildUnlimitedMap(this.jobTypeOptions, "jobType");
          this.listQuery.jobTypes.push(this.unlimitedMap["jobType"]);
          this.buildInitIds(this.initJobTypeIds, this.jobTypeOptions);
        });
        listByType(9).then((response) => {
          this.salaryRangeOptions = response.data.list;
          this.buildUnlimitedMap(this.salaryRangeOptions, "salary");
          this.listQuery.salaryRangeIds.push(this.unlimitedMap["salary"]);
          this.buildInitIds(this.initSalaryIds, this.salaryRangeOptions);
          this.refreshOptions();
        });
        listByType(25).then((response) => {
          this.degreeOptions = response.data.list;
          this.buildUnlimitedMap(this.degreeOptions, "degree");
          this.listQuery.degreeIds.push(this.unlimitedMap["degree"]);
          this.buildInitIds(this.initDegreeIds, this.degreeOptions);
          this.refreshOptions();
        });
        listByType(13).then((response) => {
          this.experienceOptions = response.data.list;
          this.buildUnlimitedMap(this.experienceOptions, "exp");
          this.listQuery.experienceIds.push(this.unlimitedMap["exp"]);
          this.buildInitIds(this.initExpIds, this.experienceOptions);
          this.refreshOptions();
        });
        listByType(7).then((response) => {
          this.companyDefineOptions = response.data.list;
          this.buildUnlimitedMap(this.companyDefineOptions, "define");
          this.listQuery.companyDefineIds.push(this.unlimitedMap["define"]);
          this.buildInitIds(this.initDefineIds, this.companyDefineOptions);
        });
        this.$axios
                .request({
                  url: "/skill-tag/list",
                  method: "get",
                  params: {
                    type: 163,
                    page: 1,
                    limit: 10,
                    sort: "+id",
                  },
                })
                .then((resp) => {
                  this.lanRequiredOptions = resp.data.list;
                  this.buildUnlimitedMap(this.lanRequiredOptions, "lang");
                  this.listQuery.lanRequiredIds.push(this.unlimitedMap["lang"]);
                  this.buildInitIds(this.initLanRequiredIds, this.lanRequiredOptions);
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
      sortChange() {
        switch (this.sortValue) {
          case "1":
            // todo 推荐职位列表 ， 暂时获取全部职位
            getCompanyJobList().then((response) => {
              this.pageResult = response.data;
              this.total = this.pageResult.total;
              this.$emit("complete");
            });
            break;
          case "2":
            this.onOrderPubTime();
            break;
          case "3":
            this.onOrderSalary();
            break;
          default:
            // 兜底
            break;
        }
      },
      saveLastShowStatus(A, C, moreFilter) {
        this.lastA = A;
        this.lastC = C;
        this.lastMoreFilter = moreFilter;
      },
      buildInitIds(ids, options) {
        for (let i = 0; i < options.length; i++) {
          ids.push(options[i].id);
        }
        return ids;
      },
      checkScrollFilter() {
        if (this.A && !this.moreFilter) {
          let top1 = document.getElementById("pointA").getBoundingClientRect()
                  .top;
          if (top1 <= 0) {
            this.saveLastShowStatus(this.A, this.C, this.moreFilter);
            this.closeMoreFilter();
          }
        }

        if (this.moreFilter && this.A) {
          let top1 = document.getElementById("pointMore").getBoundingClientRect()
                  .top;
          if (top1 <= 0) {
            this.saveLastShowStatus(this.A, this.C, this.moreFilter);
            this.closeMoreFilter();
          }
        }

        if (!this.moreFilter && !this.A && this.C) {
          if (this.getScrollTop() <= 0) {
            this.reShowMoreFilterByArgs();
          }
        }
      },
      getScrollTop() {
        let scrollTop = 0,
                bodyScrollTop = 0,
                documentScrollTop = 0;
        if (document.body) {
          bodyScrollTop = document.body.scrollTop;
        }
        if (document.documentElement) {
          documentScrollTop = document.documentElement.scrollTop;
        }
        scrollTop =
                bodyScrollTop - documentScrollTop > 0
                        ? bodyScrollTop
                        : documentScrollTop;
        return scrollTop;
      },
      showMoreFilter() {
        this.A = true;
        this.moreFilter = true;
        this.C = false;
      },
      checkSubscribeData(data) {
        if (data === undefined) return false;
        return !(
                data.cityIds.length <= 0 &&
                data.degreeIds.length <= 0 &&
                data.industryIds.length <= 0 &&
                data.salaryIds.length <= 0
        );
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
      reShowMoreFilterByArgs() {
        this.A = this.lastA;
        this.C = this.lastC;
        this.moreFilter = this.lastMoreFilter;
      },
      buildUnlimitedMap(options, key) {
        for (let i = 0; i < options.length; i++) {
          if (options[i].name === "不限") {
            this.unlimitedMap[key] = options[i].id;
          }
        }
      },
      onOrderPubTime() {
        this.listQuery.sort = this.orderPubTime;
        searchJob(this.listQuery).then((response) => {
          if (!response.data.list || response.data.list.length === 0) {
            this.showNoResult = true;
            this.total = 10;
            getCompanyJobList().then((response) => {
              this.pageResult = response.data;
              this.total = this.pageResult.total;
              this.$emit("complete");
            });
            /*getRecommendList({
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
            });*/
          } else {
            this.pageResult = response.data;
            this.total = this.pageResult.total;
            this.$emit("complete");
          }
        });
      },
      onOrderSalary() {
        this.listQuery.salaryAsc = 0;
        this.listQuery.limit = 10;
        this.listQuery.sort = undefined;
        this.$axios
                .post("/job/search-job-order-by-salary", this.listQuery)
                .then((resp) => {
                  if (!resp.data.list || resp.data.list.length === 0) {
                    this.showNoResult = true;
                    this.total = 10;
                    getCompanyJobList().then((response) => {
                      this.pageResult = response.data;
                      this.total = this.pageResult.total;
                      this.$emit("complete");
                    });
                  } else {
                    this.pageResult = resp.data;
                    this.total = this.pageResult.total;
                    this.$emit("complete");
                  }
                });
      },
      inpCityChange() {
        if (this.inpCity === undefined || this.inpCity.length === 0) {
          return;
        }
        this.$axios
                .post("/city/get-city-id-by-name", { cityNames: this.inpCity })
                .then((resp) => {
                  for (let i = 0; i < resp.data.length; i++) {
                    this.listQuery.cityIds.push(resp.data[i]);
                  }
                  this.listQuery.cityIds = this.distinct(this.listQuery.cityIds);
                  this.handleFilter();
                });
      },
      reHandleFilter(options) {
        this.emptyFilter();
        this.listQuery.cityIds = options.cityIds.indexOf(null) === -1 ? options.cityIds : [];
        this.listQuery.companyIndustryIds = options.industryIds.indexOf(null) === -1 ? options.industryIds : [];
        this.listQuery.degreeIds = options.degreeIds.indexOf(null) === -1 ? options.degreeIds : [];
        this.listQuery.salaryRangeIds = options.salaryIds.indexOf(null) === -1 ? options.salaryIds : [];
        this.listQuery.experienceIds = options.expIds.indexOf(null) === -1 ? options.expIds : [];
        this.listQuery.companyScaleIds = options.scaleIds.indexOf(null) === -1 ? options.scaleIds : [];
        this.listQuery.companyDefineIds = options.defineIds.indexOf(null) === -1 ? options.defineIds : [];
        this.listQuery.lanRequiredIds = options.lanIds.indexOf(null) === -1 ? options.lanIds : [];
        this.listQuery.jobTypes = options.jobTypeIds.indexOf(null) === -1 ? options.jobTypeIds : [];
        // this.listQuery.specialIds = options.specialIds.indexOf(null) === -1 ? options.specialIds : [];

        this.listQuery.page = 1;
        this.refreshInpCity(options);
        this.refreshOptions();
        this.saveSearchHistory();
        this.handleRouteList();
      },
      refreshInpCity(options) {
        this.inpCity = [];
        let historyCityValues = [];
        let optionsCityValues = [];
        let cityValues = options.cityValues;
        for (let i = 0; i < cityValues.length; i++) {
          historyCityValues.push(cityValues[i].endsWith("市") ? cityValues[i].substr(0, cityValues.length - 1) : cityValues[i]);
        }
        for (let i = 0; i < this.cityOptions.length; i++) {
          optionsCityValues.push(this.cityOptions[i].name);
          historyCityValues.push(this.cityOptions[i].name);
        }
        historyCityValues = this.distinct(historyCityValues);
        let optionsAndHistoryArr = this.getArrDifference(historyCityValues, optionsCityValues);
        for (let i = 0; i < optionsAndHistoryArr.length; i++) {
          this.inpCity.push(optionsAndHistoryArr[i]);
        }
      },
      getArrDifference(arr1, arr2) {
        return arr1.concat(arr2).filter(function(v, i, arr) {
          return arr.indexOf(v) === arr.lastIndexOf(v);
        });
      },
      getArrEqual(arr1, arr2) {
        let newArr = [];
        for (let i = 0; i < arr2.length; i++) {
          for (let j = 0; j < arr1.length; j++) {
            if (arr1[j] === arr2[i]) {
              newArr.push(arr1[j]);
            }
          }
        }
        return newArr;
      },
      handleFilter(id) {
        this.listQuery.page = 1;
        this.refreshOptions(id);
        this.saveSearchHistory();
        this.handleRouteList();
      },
      distinct(arr) {
        let hash = [];
        for (let i = 0; i < arr.length; i++) {
          if(hash.indexOf(arr[i]) === -1){
            hash.push(arr[i]);
          }
        }
        return hash;
      },
      saveSearchHistory() {
        this.formatHistoryForm();
        if (this.historyForm.filterCount <= 0) {
          return;
        }
        this.$axios.post("/history/save", this.historyForm).then((resp) => {});
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
        this.historyForm.degreeIds = this.arrCopy(this.listQuery.degreeIds);
        this.historyForm.salaryIds = this.arrCopy(this.listQuery.salaryRangeIds);
        this.historyForm.industryIds = this.arrCopy(this.listQuery.companyIndustryIds);

        this.historyForm.expIds = this.arrCopy(this.listQuery.experienceIds);
        this.historyForm.scaleIds = this.arrCopy(this.listQuery.companyScaleIds);
        this.historyForm.defineIds = this.arrCopy(this.listQuery.companyDefineIds);
        this.historyForm.jobTypeIds = this.arrCopy(this.listQuery.jobTypes);
        this.historyForm.lanIds = this.arrCopy(this.listQuery.lanRequiredIds);
        this.historyForm.specialIds = this.arrCopy(this.listQuery.specialIds);

        let count = this.getUnlimitedCount();

        // this.removeEl(this.historyForm.cityIds);
        this.historyForm.filterCount =
                this.historyForm.cityIds.length +
                this.historyForm.degreeIds.length +
                this.historyForm.salaryIds.length +
                this.historyForm.industryIds.length +
                this.historyForm.expIds.length +
                this.historyForm.scaleIds.length +
                this.historyForm.defineIds.length +
                this.historyForm.jobTypeIds.length +
                this.historyForm.lanIds.length +
                this.historyForm.specialIds.length -
                count;
      },
      getUnlimitedCount() {
        let count = 0;
        if (this.listQuery.cityIds.indexOf(this.unlimitedMap["city"]) !== -1) {
          count++;
        }
        if (this.listQuery.companyIndustryIds.indexOf(this.unlimitedMap["industry"]) !== -1) {
          count++;
        }
        if (this.listQuery.salaryRangeIds.indexOf(this.unlimitedMap["salary"]) !==-1) {
          count++;
        }
        if (this.listQuery.degreeIds.indexOf(this.unlimitedMap["degree"]) !== -1) {
          count++;
        }
        if (this.listQuery.experienceIds.indexOf(this.unlimitedMap["exp"]) !== -1) {
          count++;
        }
        if (this.listQuery.companyScaleIds.indexOf(this.unlimitedMap["scale"]) !==-1) {
          count++;
        }
        if (this.listQuery.companyDefineIds.indexOf(this.unlimitedMap["define"]) !==-1) {
          count++;
        }
        if (this.listQuery.jobTypes.indexOf(this.unlimitedMap["jobType"]) !== -1) {
          count++;
        }
        if (this.listQuery.lanRequiredIds.indexOf(this.unlimitedMap["lang"]) !== -1) {
          count++;
        }
        return count;
      },
      arrCopy(originArr) {
        let newArr = [];
        for (let i = 0; i < originArr.length; i++) {
          newArr.push(originArr[i]);
        }
        return newArr;
      },
      removeElByValue(arr, value) {
        let i = arr.indexOf(value);
        if (i > -1) {
          arr.splice(i, 1);
        }
      },
      refreshChecked(selectedIds, key, selectiveId, initIds) {
        return (
                selectedIds.indexOf(this.unlimitedMap[key]) !== -1 &&
                selectiveId !== this.unlimitedMap[key] &&
                initIds.indexOf(selectiveId) !== -1
        );
      },
      refreshOptions(id) {
        this.placeholderCity = this.getNameByIdFromOptions(this.cityOptions, this.listQuery.cityIds, "城市不限");
        this.placeholderIndustry = this.getNameByIdFromOptions( this.companyIndustryOptions, this.listQuery.companyIndustryIds, "行业不限");
        this.placeholderSalary = this.getNameByIdFromOptions( this.salaryRangeOptions, this.listQuery.salaryRangeIds, "月薪不限");
        this.placeholderDegree = this.getNameByIdFromOptions( this.degreeOptions, this.listQuery.degreeIds, "学历不限");
        this.placeholderExp = this.getNameByIdFromOptions( this.experienceOptions, this.listQuery.experienceIds, "工作经验不限");

        if (id === undefined) return;

        if (id === this.unlimitedMap["city"]) {
          this.listQuery.cityIds = [this.unlimitedMap["city"]];
          this.placeholderCity = "城市不限";
        }
        if (this.refreshChecked(this.listQuery.cityIds,"city",id,this.initCityIds)) {
          this.removeElByValue(this.listQuery.cityIds, this.unlimitedMap["city"]);
        }

        if (id === this.unlimitedMap["industry"]) {
          this.listQuery.companyIndustryIds = [this.unlimitedMap["industry"]];
          this.placeholderIndustry = "行业不限";
        }
        if (this.refreshChecked(this.listQuery.companyIndustryIds,"industry",id,this.initIndustryIds)) {
          this.removeElByValue(
                  this.listQuery.companyIndustryIds,
                  this.unlimitedMap["industry"]
          );
        }

        if (id === this.unlimitedMap["salary"]) {
          this.listQuery.salaryRangeIds = [this.unlimitedMap["salary"]];
          this.placeholderSalary = "月薪不限";
        }
        if (this.refreshChecked(this.listQuery.salaryRangeIds,"salary",id,this.initSalaryIds)) {
          this.removeElByValue(
                  this.listQuery.salaryRangeIds,
                  this.unlimitedMap["salary"]
          );
        }

        if (id === this.unlimitedMap["degree"]) {
          this.listQuery.degreeIds = [this.unlimitedMap["degree"]];
          this.placeholderDegree = "学历不限";
        }
        if (this.refreshChecked(this.listQuery.degreeIds,"degree",id,this.initDegreeIds)) {
          this.removeElByValue(
                  this.listQuery.degreeIds,
                  this.unlimitedMap["degree"]
          );
        }

        if (id === this.unlimitedMap["exp"]) {
          this.listQuery.experienceIds = [this.unlimitedMap["exp"]];
          this.placeholderExp = "工作经验不限";
        }
        if (this.refreshChecked(this.listQuery.experienceIds,"exp",id,this.initExpIds)) {
          this.removeElByValue(
                  this.listQuery.experienceIds,
                  this.unlimitedMap["exp"]
          );
        }

        if (id === this.unlimitedMap["scale"]) {
          this.listQuery.companyScaleIds = [this.unlimitedMap["scale"]];
        }
        if (this.refreshChecked(this.listQuery.companyScaleIds,"scale",id,this.initScaleIds)) {
          this.removeElByValue(
                  this.listQuery.companyScaleIds,
                  this.unlimitedMap["scale"]
          );
        }

        if (id === this.unlimitedMap["define"]) {
          this.listQuery.companyDefineIds = [this.unlimitedMap["define"]];
        }
        if (this.refreshChecked(this.listQuery.companyDefineIds,"define",id,this.initDefineIds)) {
          this.removeElByValue(
                  this.listQuery.companyDefineIds,
                  this.unlimitedMap["define"]
          );
        }

        if (id === this.unlimitedMap["jobType"]) {
          this.listQuery.jobTypes = [this.unlimitedMap["jobType"]];
        }
        if (this.refreshChecked(this.listQuery.jobTypes,"jobType",id,this.initJobTypeIds)) {
          this.removeElByValue(
                  this.listQuery.jobTypes,
                  this.unlimitedMap["jobType"]
          );
        }

        if (id === this.unlimitedMap["lang"]) {
          this.listQuery.lanRequiredIds = [this.unlimitedMap["lang"]];
        }
        if (this.refreshChecked(this.listQuery.lanRequiredIds,"lang",id,this.initLanRequiredIds)) {
          this.removeElByValue(
                  this.listQuery.lanRequiredIds,
                  this.unlimitedMap["lang"]
          );
        }
      },
      getMatchingNames(options) {
        // let matchNames = [];
        let optionsName = [];
        let inpName = [];
        for (let i = 0; i < options.length; i++) {
          optionsName.push(options[i].name);
          inpName.push(options[i].name);
        }
        for (let i = 0; i < this.inpCity.length; i++) {
          inpName.push(this.inpCity[i]);
        }
        inpName = this.distinct(inpName);

        return inpName.concat(optionsName).filter(function (v, i, arr) {
          return arr.indexOf(v) === arr.lastIndexOf(v);
        });
      },
      getNameByIdFromOptions(options, ids, origin) {
        let name = "";
        if (origin === "城市不限" && this.inpCity.length >= 0) {
          let matchNames = this.getMatchingNames(options);
          for (let i = 0; i < matchNames.length; i++) {
            name += matchNames[i] + ",";
          }
        }

        for (let i = 0; i < options.length; i++) {
          for (let j = 0; j < ids.length; j++) {
            if (options[i].id === ids[j] && options[i].name !== "不限") {
              name += options[i].name + ",";
            }
          }
        }
        if (name === "") {
          return origin;
        }
        if (name.endsWith(",")) {
          if (name.substr(0, name.length - 1) === origin) {
            return origin + name.substr(0, name.length - 1);
          }
          if (origin === "月薪不限") {
            return "月薪" + name.substr(0, name.length - 1);
          }
          if (origin === "工作经验不限") {
            return "工作经验" + name.substr(0, name.length - 1);
          }
          return name.substr(0, name.length - 1);
        }
      },
      emptyFilter() {
        this.inpCity = [];
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
          salaryAsc: 0,
        };
        this.refreshOptions();
        this.handleRouteList();
      },
      getList() {
        this.showNoResult = false;
        parseListQuery(this.$route.query, this.listQuery);
        this.listQuery.keyword = this.$route.query.searchWord;

        searchJob(this.listQuery).then((response) => {
          if (!response.data.list || response.data.list.length === 0) {
            this.showNoResult = true;
            this.total = 10;
            getCompanyJobList().then((response) => {
              this.pageResult = response.data;
              this.total = this.pageResult.total;
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
      window.removeEventListener("scroll", this.checkScrollFilter);
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

    .clear-btn {
    }
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

      .quickFilter {
        display: block;
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
            cursor: pointer;

            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;

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
              color: #7f7f7f;

              white-space: nowrap;
              overflow: hidden;
              text-overflow: ellipsis;

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
              color: #027db4;
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

      .sort-options {
        justify-content: flex-end;
        margin-left: 100px;
        margin-right: 20px;
        margin-top: 2px;
        min-width: 120px;
      }

      .inp-search {
        display: flex;
        justify-content: space-between;
        height: 30px;

        .inp-search-child {
          padding: 0px 5px;
          height: 30px;

          /deep/ .el-input__inner {
            flex: 1;
            margin-left: 5px;
            margin-right: 5px;
            border-radius: 17px;
            border: 1px solid #698ec7;
            background: #fbfbfb;
            color: #0d46f3;

            white-space: nowrap;
            overflow: hidden;
            text-overflow:ellipsis;
          }
        }
        /deep/.el-button--text {
          color: #333333;
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
        border: 1px solid #f6f9fc;
      }
      /deep/ .el-checkbox-button + .el-checkbox-button {
        margin-left: 16px;
      }
      /deep/ .el-checkbox-button .el-checkbox-button__inner {
        background: #f5f5f5;
        border-radius: 17px;
        border: 1px solid #f6f9fc;
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
        z-index: 2;
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
          text-overflow: ellipsis;
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

              position: relative;
              top: 5px;
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

