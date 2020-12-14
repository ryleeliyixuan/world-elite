<template>
  <div class="app-container mb-4">
    <div class="intro-box company-info-container" v-if="companyWiki">
      <el-card
        class="intro-summary"
        style="margin-bottom: 36px"
        shadow="always"
        :body-style="{ padding: '36px' }"
        v-if="
          (companyWiki.video && companyWiki.video !== '') ||
          (companyWiki.summary && companyWiki.summary.length > 0)
        "
      >
        <div
          v-if="companyWiki.video && companyWiki.video.length > 0"
          class="intro-summary-img"
        >
          <video
            style="height: 300px"
            :src="companyWiki.video"
            :alt="companyWiki.company.fullName"
            controls="controls"
          ></video>
        </div>
        <div class="intro-summary-text">
          {{ companyWiki.summary }}
        </div>
      </el-card>
      <div v-if="companyWiki" class="intro-module">
        <el-row :gutter="36">
          <el-col
            v-if="
              companyWiki.wikiModule &&
              companyWiki.wikiModule.addressEnable == 1 &&
              companyWiki.company &&
              companyWiki.company.addressList &&
              companyWiki.company.addressList.length > 0
            "
            class="intro-address intro-module-element"
            :xs="24"
            :sm="24"
            :md="12"
            :lg="12"
            :xl="12"
          >
            <el-card
              class="stdmb"
              shadow="always"
              :body-style="{ padding: '36px' }"
            >
              <h5 class="mb-4">
                <svg-icon
                  icon-class="address-on"
                  style="height: 25px; width: 25px"
                />
                公司地址
              </h5>
              <div
                v-for="(addr, index) in companyWiki.company.addressList"
                :key="addr.id"
              >
                <div class="map-wrapper">
                  <div class="map-box mb-4">
                    <el-amap
                      :vid="'amap' + index"
                      :zoom="mapZoom"
                      :center="addr.mapWindow.position"
                    >
                      <el-amap-info-window
                        :position="addr.mapWindow.position"
                        :content="addr.mapWindow.content"
                      ></el-amap-info-window>
                    </el-amap>
                  </div>
                </div>
                <div class="regular-font">{{ addr.address }}</div>
              </div>
            </el-card>
          </el-col>
          <el-col
            v-if="
              companyWiki.wikiModule &&
              companyWiki.wikiModule.employeeEnable == 1 &&
              companyWiki.employeeList &&
              companyWiki.employeeList.length !== 0
            "
            class="intro-employee intro-module-element"
            :xs="24"
            :sm="24"
            :md="12"
            :lg="12"
            :xl="12"
          >
            <el-card
              shadow="always"
              class="stdmb intro-employee-chart-wrapper"
              :body-style="{ padding: '36px' }"
            >
              <h5 class="mb-4">
                <svg-icon
                  icon-class="employee"
                  style="height: 25px; width: 25px"
                />
                雇员数量
              </h5>
              <BarChart
                :items="companyWiki.employeeList"
                class="intro-employee-chart"
              ></BarChart>
            </el-card>
          </el-col>
          <el-col
            v-if="
              companyWiki.wikiModule &&
              companyWiki.wikiModule.productEnable == 1 &&
              companyWiki.productList &&
              companyWiki.productList.length !== 0
            "
            class="intro-product intro-module-element"
            :xs="24"
            :sm="24"
            :md="12"
            :lg="12"
            :xl="12"
          >
            <el-card class="stdmb" shadow="always">
              <h5 class="mb-4" style="padding: 16px 16px 0 16px">
                <svg-icon
                  icon-class="product"
                  style="height: 25px; width: 25px"
                />
                旗下产品/子公司
              </h5>
              <el-carousel
                indicator-position="none"
                height="380px"
                :interval="5000"
                arrow="always"
              >
                <el-carousel-item
                  v-for="product in companyWiki.productList"
                  :key="product.id"
                >
                  <div class="carousel-wrapper">
                    <div class="carousel-image">
                      <img
                        v-if="product.url.length > 0"
                        class="intro-product-image"
                        :src="product.url"
                        :alt="product.description"
                        v-on:click="select(product)"
                      />
                    </div>
                    <div class="intro-product-description regular-font">
                      {{ product.description }}
                    </div>
                  </div>
                </el-carousel-item>
              </el-carousel>
            </el-card>
          </el-col>
          <!-- <el-col
            v-if="
              companyWiki.wikiModule &&
              companyWiki.wikiModule.marketEnable === 1 &&
              companyWiki.market &&
              companyWiki.market.url &&
              companyWiki.market.url.length > 0
            "
            class="intro-valuation intro-module-element"
            :xs="24"
            :sm="24"
            :md="12"
            :lg="12"
            :xl="12"
          >
            <h5 class="mt-4 mb-4">
              <i class="el-icon-s-marketing" style="color: #1e90ff"></i>
              市值情况
            </h5>
            <el-card class="intro-valuation-stockstats" style="height: 380px">
              <div class="stockstats-basicinfo stockstats-section">
                <el-row>
                  <el-col :span="12">
                    <h5>{{ stockInfo.name }}</h5>
                  </el-col>
                  <el-col :span="12">
                    <span> {{ stockInfo.date }} {{ stockInfo.timestamp }}</span>
                  </el-col>
                </el-row>
              </div>
              <div class="stockstats-currentprice stockstats-section">
                <h1>{{ stockInfo.current }}</h1>
              </div>
              <div class="stockstats-detail stockstats-section">
                <el-row>
                  <el-col :span="12">
                    最高：<span style="font-weight: bold">{{
                      stockInfo.high
                    }}</span>
                  </el-col>
                  <el-col :span="12">
                    最低：<span style="font-weight: bold">{{
                      stockInfo.low
                    }}</span>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="12">
                    今开：<span style="font-weight: bold">{{
                      stockInfo.open
                    }}</span>
                  </el-col>
                  <el-col :span="12">
                    昨收：<span style="font-weight: bold">{{
                      stockInfo.close
                    }}</span>
                  </el-col>
                </el-row>
              </div>
            </el-card>
          </el-col> -->
          <el-col
            v-if="
              companyWiki.wikiModule &&
              companyWiki.wikiModule.historyEnable == 1 &&
              companyWiki.historyList &&
              companyWiki.historyList.length > 0
            "
            class="intro-timeline intro-module-element"
            :xs="24"
            :sm="24"
            :md="12"
            :lg="12"
            :xl="12"
          >
            <el-card
              class="stdmb"
              shadow="always"
              :body-style="{ padding: '36px' }"
            >
              <div class="module-header">
                <h5 class="mb-4">
                  <svg-icon
                    icon-class="history"
                    style="height: 25px; width: 25px"
                  />
                  发展路径
                </h5>
                <div class="timeline-switch mb-3">
                  <el-switch
                    class="timline-switch-button"
                    v-model="reverse"
                    active-text="ON"
                    inactive-text=""
                    :width="50"
                  >
                  </el-switch>
                  <span style="font-size: 14px"> 倒序 </span>
                </div>
              </div>
              <div class="block">
                <div class="infinite-list-wrapper" style="overflow: auto">
                  <el-timeline :reverse="reverse">
                    <el-timeline-item
                      v-for="history in companyWiki.historyList"
                      :key="history.id"
                      :timestamp="history.eventTime"
                      color="#F9BA49"
                      placement="top"
                      v-infinite-scroll="load"
                      infinite-scroll-disabled="disabled"
                    >
                      {{ history.event }}
                    </el-timeline-item>
                  </el-timeline>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col
            v-if="
              companyWiki.wikiModule &&
              companyWiki.wikiModule.structureEnable == 1 &&
              companyWiki.structure &&
              companyWiki.structure.length > 0
            "
            class="intro-structure intro-module-element"
            :xs="24"
            :sm="24"
            :md="12"
            :lg="12"
            :xl="12"
          >
            <el-card
              class="stdmb"
              shadow="always"
              :body-style="{ padding: '36px' }"
            >
              <div class="structure-module-header">
                <h5 class="mb-4">
                  <svg-icon
                    icon-class="structure"
                    style="height: 25px; width: 25px"
                  />
                  公司架构
                </h5>
                <el-button
                  round
                  class="intro-structure-fullscreen mb-4"
                  size="mini"
                  type="goon"
                  @click="fullScreenStructure = true"
                  plain
                >
                  <svg-icon
                    icon-class="zoom"
                    style="height: 13px; width: 13px; margin-right: 3px"
                  />查看</el-button
                >
              </div>
              <el-dialog
                :visible.sync="fullScreenStructure"
                width="90%"
                height="90%"
              >
                <StructureTreeChartFullScreen
                  :items="companyWiki.structure"
                  class="intro-structure-chart"
                ></StructureTreeChartFullScreen>
              </el-dialog>
              <TreeChart
                :items="companyWiki.structure"
                class="intro-structure-chart"
              ></TreeChart>
            </el-card>
          </el-col>
          <el-col
            v-if="
              companyWiki.wikiModule &&
              companyWiki.wikiModule.salaryEnable == 1 &&
              companyWiki.salaryList &&
              companyWiki.salaryList.length > 0
            "
            class="intro-salary intro-module-element"
            :xs="24"
            :sm="24"
            :md="12"
            :lg="12"
            :xl="12"
          >
            <div class="intro-salary-container">
              <el-card class="stdmb" shadow="always">
                <h5 class="mb-4" style="padding: 16px 16px 0 16px">
                  <svg-icon
                    icon-class="salary"
                    style="height: 25px; width: 25px"
                  />
                  薪资待遇
                </h5>
                <div class="intro-salary-pie-container">
                  <SalaryPieChart
                    class="intro-salary-pie"
                    :items="companyWiki.salaryList"
                    :name="companyWiki.company.fullName"
                  ></SalaryPieChart>
                </div>
              </el-card>
            </div>
          </el-col>
          <el-col
            v-if="
              companyWiki.wikiModule &&
              companyWiki.wikiModule.environmentEnable == 1 &&
              companyWiki.environmentList &&
              companyWiki.environmentList.length > 0
            "
            class="intro-environment intro-module-element"
            :xs="24"
            :sm="24"
            :md="12"
            :lg="12"
            :xl="12"
          >
            <el-card class="stdmb" shadow="always">
              <h5 class="mb-4" style="padding: 16px 16px 0 16px">
                <svg-icon
                  icon-class="environment"
                  style="height: 25px; width: 25px"
                />
                工作环境
              </h5>
              <el-carousel
                indicator-position="none"
                height="380px"
                :interval="5000"
                arrow="always"
              >
                <el-carousel-item
                  v-for="image in companyWiki.environmentList"
                  :key="image.id"
                >
                  <div class="carousel-wrapper">
                    <div class="carousel-image">
                      <img
                        class="intro-environment-image"
                        :src="image.imageUrl"
                        :alt="image.name"
                        v-on:click="select(image)"
                      />
                    </div>
                  </div>
                </el-carousel-item>
              </el-carousel>
            </el-card>
          </el-col>
          <el-col
            v-if="
              companyWiki.wikiModule &&
              companyWiki.wikiModule.recruitEnable == 1 &&
              companyWiki.recruitList &&
              companyWiki.recruitList.length > 0
            "
            class="intro-recruit intro-module-element"
            :xs="24"
            :sm="24"
            :md="12"
            :lg="12"
            :xl="12"
          >
            <el-card
              class="stdmb"
              shadow="always"
              :body-style="{ padding: '36px' }"
            >
              <div class="module-header">
                <h5 class="mb-4">
                  <svg-icon
                    icon-class="timeline"
                    style="height: 25px; width: 25px"
                  />
                  招聘时间线
                </h5>
                <div class="timeline-switch mb-4">
                  <el-switch
                    class="timline-switch-button"
                    v-model="reverseRecruit"
                    active-text="ON"
                    inactive-text=""
                    :width="50"
                  >
                  </el-switch>
                  <span style="font-size: 14px"> 倒序 </span>
                </div>
              </div>
              <div class="block">
                <div class="infinite-list-wrapper" style="overflow: auto">
                  <el-timeline :reverse="reverseRecruit">
                    <el-timeline-item
                      color="#F9BA49"
                      v-for="history in companyWiki.recruitList"
                      :key="history.id"
                      :timestamp="history.time"
                      placement="top"
                      v-infinite-scroll="load"
                      infinite-scroll-disabled="disabled"
                    >
                      {{ history.event }}
                    </el-timeline-item>
                  </el-timeline>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col
            v-if="
              companyWiki.wikiModule &&
              companyWiki.wikiModule.honorEnable == 1 &&
              companyWiki.honorList &&
              companyWiki.honorList.length > 0
            "
            class="intro-honor intro-module-element"
            :xs="24"
            :sm="24"
            :md="12"
            :lg="12"
            :xl="12"
          >
            <el-card
              class="stdmb"
              shadow="always"
              :body-style="{ padding: '36px' }"
            >
              <h5 class="mb-4">
                <i class="el-icon-medal-1" style="color: #1e90ff"></i> 企业荣誉
              </h5>
              <el-table
                :data="companyWiki.honorList"
                style="width: 100%"
                max-height="330"
              >
                <el-table-column prop="year" label="年份" width="100">
                </el-table-column>
                <el-table-column prop="honor" label="荣誉"> </el-table-column>
              </el-table>
            </el-card>
          </el-col>
          <el-col
            v-if="
              companyWiki.wikiModule &&
              companyWiki.wikiModule.departmentEnable == 1 &&
              companyWiki.department &&
              companyWiki.department.length > 0
            "
            class="intro-department intro-module-element"
            :xs="24"
            :sm="24"
            :md="12"
            :lg="12"
            :xl="12"
          >
            <el-card
              class="stdmb"
              shadow="always"
              :body-style="{ padding: '36px' }"
            >
              <div class="d-flex justify-content-end">
                <h5 class="mb-4">
                  <i class="el-icon-s-custom" style="color: #1e90ff"></i>
                  公司部门
                </h5>
                <el-button
                  class="intro-structure-fullscreen"
                  size="mini"
                  type="goon"
                  icon="el-icon-full-screen"
                  @click="fullScreen = true"
                  plain
                  >查看</el-button
                >
              </div>
              <el-dialog
                title="公司部门"
                :visible.sync="fullScreen"
                width="90%"
              >
                <DepartmentChart
                  :items="companyWiki.department"
                  class="intro-structure-chart"
                ></DepartmentChart>
              </el-dialog>
              <DepartmentChart
                :items="companyWiki.department"
                class="intro-structure-chart"
              ></DepartmentChart>
            </el-card>
          </el-col>
        </el-row>
      </div>
      <!-- <el-divider></el-divider>
      <el-row :gutter="36">
        <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
          <h5 class="mb-4">
            谁贡献过
            <el-button
              type="text"
              size="small"
              @click="moreContributor"
              class="edit-info-button"
            >
              更多</el-button
            >
          </h5>
          <div class="edit-info-container">
            <div
              class="edit-info"
              v-for="(contributedUser, index) in contributorDisplayed"
              :key="index"
            >
              <div v-if="contributedUser.rank <= 3">
                <el-badge
                  :value="contributedUser.rank"
                  class="contributed-badge"
                  :type="contributedUser.type"
                >
                  <el-avatar
                    size="medium"
                    :src="contributedUser.avatarUrl"
                  ></el-avatar>
                </el-badge>
              </div>
              <div v-else>
                <el-avatar
                  size="medium"
                  :src="contributedUser.avatarUrl"
                ></el-avatar>
              </div>
              <span> {{ contributedUser.name }} </span>
              <span style="font-size: x-small; color: grey">
                贡献过{{ contributedUser.frequency }}次
              </span>
            </div>
          </div>
        </el-col>
        <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
          <div class="edit-info-bar">
            <h5>
              相关推荐
              <el-button
                type="text"
                size="small"
                @click="changeComps"
                class="edit-info-button"
              >
                换一批</el-button
              >
            </h5>
          </div>
          <div class="edit-info-container">
            <div
              class="edit-info"
              v-for="(company, index) in companyShowed"
              :key="index"
            >
              <el-avatar :size="70"> {{ company.name }} </el-avatar>
            </div>
          </div>
        </el-col>
      </el-row> -->
    </div>
    <div class="noInfoMsgBox" v-else>暂 无 百 科 信 息</div>
  </div>
</template>

<script>
import Vue from "vue";
import VueAMap from "vue-amap";
import axios from "axios";
import BarChart from "@/components/BarChart";
import TreeChart from "@/components/TreeChart";
import StructureTreeChartFullScreen from "@/components/StructureTreeChartFullScreen";
import DepartmentChart from "@/components/DepartmentChart";
import SalaryPieChart from "@/components/SalaryPieChart";
import Pagination from "@/components/Pagination";
import { formatListQuery, parseListQuery } from "@/utils/common";

import { getCompanyWiki } from "@/api/company_api";
import { setPageTitle } from "@/utils/setting";
import { mapGetters } from "vuex";
import Toast from "@/utils/toast";
import { fas } from "@fortawesome/free-solid-svg-icons";

Vue.use(VueAMap);

VueAMap.initAMapApiLoader({
  key: process.env.VUE_APP_AMAP_KEY,
  v: "1.4.4",
});

export default {
  name: "WikiIntroPage",
  components: {
    Pagination,
    BarChart,
    TreeChart,
    DepartmentChart,
    SalaryPieChart,
    StructureTreeChartFullScreen,
  },
  props: {
    //接受父组件传递来的数据
    companyId: {
      type: String,
    },
  },
  data() {
    return {
      //MAIN PAGE ATTRIBUTE
      company: undefined,
      companyWiki: {
        employeeList: [],
        market: { url: "" },
        productList: [],
        structure: [],
        company: {
          addressList: [],
        },
      },
      load: true,
      //intro-address
      activeAddress: 0,
      mapZoom: 14,
      //intro-timeline
      count: 10,
      loading: false,
      reverse: true,
      //intro-structure
      fullScreenStructure: false,
      fullScreen: false,
      reverseRecruit: true,

      //intro-relatives
      companyShowed: [], // 用来存放每次点击换一批出来的5个对象
      randomCompIndex: "", // 用来放5个随机数
      randomCompArr: [], // 用来放5个随机数的数组，用来循环
      //intro-contributor
      contributorDisplayed: [],
      contributorIndex: "",
      contributorIndex: [],

      //FAKE COMPANY INFO LIST START - for test only
      //recommended company
      recommendedComps: [
        {
          name: "company1",
          avatarUrl:
            "https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png",
        },
        {
          name: "company2",
          avatarUrl:
            "https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png",
        },
        {
          name: "company3",
          avatarUrl:
            "https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png",
        },
        {
          name: "company4",
          avatarUrl:
            "https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png",
        },
        {
          name: "company5",
          avatarUrl:
            "https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png",
        },
        {
          name: "company6",
          avatarUrl:
            "https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png",
        },
        {
          name: "company7",
          avatarUrl:
            "https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png",
        },
        {
          name: "company8",
          avatarUrl:
            "https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png",
        },
        {
          name: "company9",
          avatarUrl:
            "https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png",
        },
        {
          name: "company10",
          avatarUrl:
            "https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png",
        },
      ],

      //who has contributed
      contributedUsers: [
        {
          name: "user1",
          avatarUrl:
            "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
          rank: 1,
          type: "danger",
          frequency: 10,
        },
        {
          name: "user2",
          avatarUrl:
            "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
          rank: 2,
          type: "success",
          frequency: 9,
        },
        {
          name: "user3",
          avatarUrl:
            "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
          rank: 3,
          type: "warning",
          frequency: 8,
        },
        {
          name: "user4",
          avatarUrl:
            "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
          rank: 4,
          frequency: 7,
        },
        {
          name: "user5",
          avatarUrl:
            "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
          rank: 5,
          frequency: 6,
        },
        {
          name: "user6",
          avatarUrl:
            "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
          rank: 6,
          frequency: 5,
        },
        {
          name: "user7",
          avatarUrl:
            "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
          rank: 7,
          frequency: 4,
        },
        {
          name: "user8",
          avatarUrl:
            "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
          rank: 8,
          frequency: 3,
        },
        {
          name: "user9",
          avatarUrl:
            "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
          rank: 9,
          frequency: 2,
        },
      ],

      //stock info
      stockInfo: {
        name: "",
        open: undefined,
        close: undefined,
        current: undefined,
        low: undefined,
        high: undefined,
        date: undefined,
        timestamp: undefined,
      },
      stockUrl: "",
      sinaMarketInfo: undefined,
    };
  },
  created() {
    this.initData();
  },
  computed: {
    companyLink() {
      return this.company.homepage.startsWith("http")
        ? this.company.homepage
        : "http://" + this.company.homepage;
    },
    ...mapGetters(["token", "userId"]),
    noMore() {
      return this.count >= 20;
    },
    disabled() {
      return this.loading || this.noMore;
    },
  },
  watch: {
    $route() {
      this.initData();
    },
  },
  mounted() {
    // //页面一加载的时候先出来5个推荐公司
    // for (var i = 0; i < 5 && i < this.recommendedComps.length; i++) {
    //   this.companyShowed.push(this.recommendedComps[i]);
    // }
    // //页面一加载的时候先出来5个最高贡献者
    // for (var i = 0; i < 5 && i < this.contributedUsers.length; i++) {
    //   this.contributorDisplayed.push(this.contributedUsers[i]);
    // }
  },
  methods: {
    initData() {
      const { id } = this.$route.params;
      //http://hq.sinajs.cn/list=hk00700
      getCompanyWiki(id).then((response) => {
        this.companyWiki = response.data;
        if (
          this.companyWiki.company &&
          this.companyWiki.company.addressList &&
          this.companyWiki.company.addressList.length > 0
        ) {
          for (const addr of this.companyWiki.company.addressList) {
            addr.mapWindow = {
              position: [addr.longitude, addr.latitude],
              content: addr.address,
            };
          }
        }
        if (
          this.companyWiki.market &&
          this.companyWiki.market.url &&
          this.companyWiki.market.url.length > 0
        ) {
          this.stockUrl = this.companyWiki.market.url;
          this.getMarketInfo();
        }
      });
    },
    getMarketInfo() {
      if (this.stockUrl) {
        //http://hq.sinajs.cn/list=hk00700
        var url = this.stockUrl.split(".");
        this.stockUrl = "/" + url[1] + "." + url[2];
        console.log(this.stockUrl, "------stockurl------");
        axios.get(this.stockUrl).then((response) => {
          this.sinaMarketInfo = response.data;
          // console.log(response.data, "------sinajs------");
          var elements = this.sinaMarketInfo.split(",");
          this.stockInfo.name = elements[1];
          this.stockInfo.open = elements[2];
          this.stockInfo.close = elements[3];
          this.stockInfo.current = elements[4];
          this.stockInfo.high = elements[5];
          this.stockInfo.low = elements[6];
          this.stockInfo.date = elements[17];
          var timestamp = elements[elements.length - 1];
          this.stockInfo.timestamp = timestamp.substr(0, 5);
        });
      }
    },
    // //change another group of comps, clicking "换一批"
    // changeComps() {
    //   //每次点击换一批触发这个方法
    //   let totalComp = this.recommendedComps.length;
    //   this.randomCompIndex = "";
    //   this.randomCompArr = [];
    //   this.companyShowed = []; // 每次点击换一批就让之前的数据全清空
    //   while (this.randomCompArr.length < 5) {
    //     // 用while来判断是否循环5次
    //     let num = parseInt(Math.floor(Math.random() * Math.floor(totalComp))); // 随机出5个0-totalcount之间的随机数
    //     if (this.randomCompArr.indexOf(num) == -1) {
    //       // 获得不重复的三个数
    //       this.randomCompArr.push(num);
    //       this.randomCompIndex = num;
    //       this.companyShowed.push(this.recommendedComps[this.randomCompIndex]); // 将随机数当作下标来遍历data里的内容
    //     }
    //   }
    // },
    // //展示更多贡献者
    // moreContributor() {
    //   const totalContributor = this.contributedUsers.length;
    //   this.contributorDisplayed = [];
    //   for (var i = 0; i < totalContributor; i++) {
    //     this.contributorDisplayed.push(this.contributedUsers[i]);
    //   }
    // },
  },
};
</script>

<style scoped lang="scss">
//el-dialog
/deep/ .el-dialog {
  border-radius: 32px;
}
//el-card
/deep/ .el-card {
  border: 0px;
  box-shadow: 0px 18px 14px 3px rgba(205, 213, 224, 0.5);
}

.intro-employee-chart-wrapper {
  position: relative;
  .intro-employee-chart {
    position: absolute;
    top: 36px;
    right: 0;
    padding: 0 12px 0 36px;
  }
}

.regular-font {
  font-family: PingFangSC-Medium, PingFang SC;
  font-weight: 500;
  color: #333333;
  line-height: 25px;
}

/deep/.el-button--goon.is-active,
.el-button--goon:active {
  background: #f1f2fb;
  border-color: #f1f2fb;
  color: #2853ff;
}

/deep/.el-button--goon:focus,
.el-button--goon:hover {
  background: #f1f2fb;
  border-color: #f1f2fb;
  color: #2853ff;
}

/deep/.el-button--goon {
  color: #2853ff;
  background-color: #f1f2fb;
  border-color: #f1f2fb;
}

.module-header {
  display: flex;
  justify-content: space-between;

  .timeline-switch {
    display: flex;
    position: relative;

    .timline-switch-button {
      position: absolute;
      right: 0;
      top: 0;
    }
  }
}

.noInfoMsgBox {
  line-height: 80px;
  text-align: center;
  padding-bottom: 20px;
  width: 100%;
}

.stdmb {
  margin-bottom: 36px;
  height: 500px;
}

.map-wrapper {
  border-radius: 36px;
  .map-box {
    height: 330px;
    border-radius: 36px;
  }
}

/deep/ .el-vue-amap {
  background-color: white !important;
}
/deep/ .amap-maps {
  border-radius: 36px;
}
/deep/ .amap-logo {
  display: none !important;
}

/deep/ .el-timeline-item__tail {
  border-left: #f9ba49 2px solid;
}

//off
/deep/ .el-switch__label * {
  font-size: 5px;
}
/deep/ .el-switch__label--left {
  position: relative;
  left: 60px;
  color: #fff;
  z-index: -1111;
}
/deep/ .el-switch__label--right {
  position: relative;
  right: 60px;
  color: #fff;
  z-index: -1111;
}
/deep/ .el-switch__label.is-active {
  z-index: 1111;
  color: #fff;
}

//on
/deep/ .el-switch__label--left {
  position: relative;
  left: 55px;
  color: #fff;
  z-index: -1111;
}
/deep/ .el-switch__label--right {
  position: relative;
  right: 55px;
  color: #fff;
  z-index: -1111;
}
/deep/ .el-switch__label--right.is-active {
  z-index: 1111;
  color: #fff !important;
}
/deep/ .el-switch__label--left.is-active {
  z-index: 1111;
  color: #9c9c9c !important;
}

/deep/ .el-switch__core {
  height: 19px;
}

/deep/ .el-switch__core:after {
  width: 15px;
  height: 15px;
}

//carousel arrow
/deep/ .el-carousel__arrow {
  top: 40%;
  width: 40px;
  height: 40px;
}

.app-container {
  margin: 0 auto;
  width: 100%;
  min-height: calc(100vh - 477px);
  background: #f6f8fb;

  .company-info-container {
    height: calc(91px + 100vw * 0.39);
    width: 100%;

    .company-info {
      position: relative;
      display: flex;
      flex-direction: column;
      align-items: center;

      .company-info-banner {
        width: 100%;
        z-index: -1;
      }

      .company-info-stat {
        width: 230px;
        height: 50px;
        position: absolute;
        right: 7%;
        top: 77%;
      }

      .company-info-logo {
        width: 100px;
        height: 100px;
        border: 2px solid white;
      }

      .company-info-description {
        position: absolute;
        top: calc(100% - 50px);
        display: flex;
        flex-direction: column;
        align-items: center;
      }

      .company-rating-dialog-content {
        margin-bottom: 12px;
      }
    }
  }

  .company-wiki-content-container {
    width: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;

    .intro-summary {
      display: flex;
      flex-direction: row;

      .intro-summary-img {
        margin-right: 20px;
        max-width: 600px;
      }
    }

    .carousel-wrapper {
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;

      .intro-product-image {
        width: 340px;
        height: 290px;
        object-fit: fit;
        border-radius: 36px;
      }
      .intro-product-description {
        width: 340px;
        word-wrap: break-all;
        word-break: normal;
        overflow: hidden;
      }
    }

    .intro-environment-image {
      width: 340px;
      height: 355px;
      object-fit: fit;
      border-radius: 36px;
    }

    .intro-product-description {
      margin: 12px;
    }

    .intro-valuation-stockstats {
      display: flex;
      flex-direction: column;
      align-content: stretch;

      .stockstats-section {
        margin-top: 24px;
        height: 80px;
      }
    }

    .infinite-list-wrapper {
      height: 350px;
    }

    .structure-module-header {
      position: relative;
      display: flex;
      justify-content: space-between;
      .intro-structure-fullscreen {
        position: absolute;
        top: 0;
        right: 0;
      }
    }

    .intro-salary-container {
      position: relative;
      width: 100%;
      .intro-salary-pie-container {
        position: absolute;
        left: 0;
        top: 0;
        width: 100%;
        height: 380px;
      }
    }

    .edit-info-container {
      display: flex;
      flex-flow: row wrap;
      justify-content: flex-start;
      margin-bottom: 24px;

      .edit-info {
        width: 20%;
        margin-bottom: 12px;

        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
      }
    }
  }
}

@media screen and (min-width: 1000px) {
  .app-container {
    .company-info-container {
      height: 525px;
    }
  }
}

@media screen and (max-width: 768px) {
  .app-container {
    .company-info-container {
      height: 350px;

      .company-info {
        display: flex;
        flex-direction: column;

        .company-info-stat {
          width: 230px;
          height: 25px;
          position: absolute;
          width: 100%;
          left: 7%;
          top: 50%;
        }
      }
    }

    .company-wiki-content-container {
      .intro-summary {
        display: flex;
        flex-direction: column;

        .intro-summary-img {
          margin-bottom: 12px;
        }
      }

      .edit-info-container {
        .edit-info {
          width: 33%;
        }
      }
    }
  }
}

@media screen and (max-width: 415px) {
  .app-container {
    .company-info-container {
      height: 300px;
    }
  }
}
</style>