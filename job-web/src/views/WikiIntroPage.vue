<template>
  <div class="intro-box" v-if="company">
    <el-divider></el-divider>
    <div class="intro-summary">
      <div class="intro-summary-img">
        <el-image
          :src="summaryImgUrl"
          :alt="company.fullName"
          fit="cover"
        ></el-image>
      </div>
      <div class="intro-summary-text">
        {{ company.wikiSummary }}
      </div>
    </div>
    <el-divider></el-divider>
    <div class="intro-module">
      <el-row :gutter="36">
        <el-col
          class="intro-address intro-module-element"
          :xs="24"
          :sm="24"
          :md="12"
          :lg="12"
          :xl="12"
        >
          <h5 class="mt-4 mb-4">
            <i class="el-icon-location" style="color: #1e90ff"></i>
            公司地址
          </h5>
          <el-collapse
            v-model="activeAddress"
            accordion
            v-for="(addr, index) in addressList"
            :key="addr.id"
          >
            <el-collapse-item :title="addr.address" :name="index">
              <div class="map-box">
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
            </el-collapse-item>
          </el-collapse>
        </el-col>
        <el-col
          class="intro-employee intro-module-element"
          :xs="24"
          :sm="24"
          :md="12"
          :lg="12"
          :xl="12"
        >
          <h5 class="mt-4 mb-4">
            <i class="el-icon-s-cooperation" style="color: #1e90ff"></i>
            雇员数量
          </h5>
          <BarChart
            :items="employeeData"
            class="intro-employee-chart"
          ></BarChart>
        </el-col>
        <el-col
          class="intro-product intro-module-element"
          :xs="24"
          :sm="24"
          :md="12"
          :lg="12"
          :xl="12"
        >
          <h5 class="mt-4 mb-4">
            <i class="el-icon-s-flag" style="color: #1e90ff"></i> 旗下产品
          </h5>
          <el-carousel height="380px" :interval="2500" arrow="always">
            <el-carousel-item v-for="product in productList" :key="product.img">
              <img
                class="intro-product-image"
                :src="product.img"
                :alt="product.alt"
                fit="cover"
                v-on:click="select(product)"
              />
            </el-carousel-item>
          </el-carousel>
        </el-col>
        <el-col
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
                  <h5>{{ ticker }}</h5>
                </el-col>
                <el-col :span="12">
                  <span>{{ timestamp }}</span>
                </el-col>
              </el-row>
              <h6 style="color: grey">{{ market }}</h6>
            </div>
            <div class="stockstats-currentprice stockstats-section">
              <div v-if="changeNumerical <= 0">
                <h3 style="color: red">{{ currency + current }}</h3>
                <i
                  class="el-icon-caret-bottom stockstats-change-down"
                  style="color: red"
                ></i>
                <span style="color: red">
                  {{ changeNumerical + " (" + changePercentage + "%)" }}
                </span>
              </div>
              <div v-if="changeNumerical > 0">
                <h3 style="color: green">{{ currency + current }}</h3>
                <i
                  class="el-icon-caret-top stockstats-change-up"
                  style="color: green"
                ></i>
                <span v-if="changeNumerical > 0" style="color: green">
                  {{ changeNumerical + " (" + changePercentage + "%)" }}
                </span>
              </div>
            </div>
            <div class="stockstats-detail stockstats-section">
              <el-row>
                <el-col :span="12">
                  最高：<span style="font-weight: bold">{{ high }}</span>
                </el-col>
                <el-col :span="12">
                  最低：<span style="font-weight: bold">{{ low }}</span>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  今开：<span style="font-weight: bold">{{ open }}</span>
                </el-col>
                <el-col :span="12">
                  <span></span>
                </el-col>
              </el-row>
            </div>
          </el-card>
        </el-col>
        <el-col
          class="intro-timeline intro-module-element"
          :xs="24"
          :sm="24"
          :md="12"
          :lg="12"
          :xl="12"
        >
          <h5 class="mt-4 mb-4">
            <i class="el-icon-s-data" style="color: #1e90ff"></i> 发展路径
          </h5>
          <div class="block">
            <div class="radio">
              <el-radio-group v-model="reverse">
                <el-radio :label="true">时间倒序</el-radio>
                <el-radio :label="false">时间正序</el-radio>
              </el-radio-group>
            </div>
            <div class="infinite-list-wrapper" style="overflow: auto">
              <el-timeline :reverse="reverse">
                <el-timeline-item
                  v-for="(activity, index) in activities"
                  :key="index"
                  :timestamp="activity.timestamp"
                  placement="top"
                  v-infinite-scroll="load"
                  infinite-scroll-disabled="disabled"
                >
                  {{ activity.content }}
                </el-timeline-item>
              </el-timeline>
            </div>
          </div>
        </el-col>
        <el-col
          class="intro-structure intro-module-element"
          :xs="24"
          :sm="24"
          :md="12"
          :lg="12"
          :xl="12"
        >
          <h5 class="mt-4 mb-4">
            <i class="el-icon-s-custom" style="color: #1e90ff"></i>
            公司架构
          </h5>
          <div class="d-flex justify-content-end">
            <el-button
              class="intro-structure-fullscreen"
              size="mini"
              type="primary"
              icon="el-icon-full-screen"
              @click="fullScreen = true"
              plain
              >查 看</el-button
            >
          </div>
          <el-dialog title="公司结构" :visible.sync="fullScreen" width="90%">
            <TreeChart
              :items="structureList"
              class="intro-structure-chart"
            ></TreeChart>
          </el-dialog>
          <TreeChart
            :items="structureList"
            class="intro-structure-chart"
          ></TreeChart>
        </el-col>
      </el-row>
    </div>
    <el-divider></el-divider>
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
            <!-- :src="company.avatarUrl" -->
            <el-avatar :size="70"> {{ company.name }} </el-avatar>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
  <div class="noInfoMsgBox" v-else>暂 无 百 科 信 息</div>
</template>

<script>
import Vue from "vue";
import VueAMap from "vue-amap";
import BarChart from "../components/BarChart";
import TreeChart from "../components/TreeChart";
import Pagination from "@/components/Pagination";
import { formatListQuery, parseListQuery } from "@/utils/common";

import { getCompanyInfo, getCompanyWiki } from "@/api/company_api";
import { setPageTitle } from "@/utils/setting";
import { mapGetters } from "vuex";
import Toast from "@/utils/toast";

Vue.use(VueAMap);

VueAMap.initAMapApiLoader({
  key: process.env.VUE_APP_AMAP_KEY,
  v: "1.4.4",
});

export default {
  name: "WikiIntroPage",
  components: { Pagination, BarChart, TreeChart },
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
      companyWiki: undefined,
      load: true,
      //intro-address
      activeAddress: 0,
      mapZoom: 14,
      //intro-timeline
      count: 10,
      loading: false,
      reverse: true,
      //intro-structure
      fullScreen: false,
      //intro-relatives
      companyShowed: [], // 用来存放每次点击换一批出来的5个对象
      randomCompIndex: "", // 用来放5个随机数
      randomCompArr: [], // 用来放5个随机数的数组，用来循环
      //intro-contributor
      contributorDisplayed: [],
      contributorIndex: "",
      contributorIndex: [],

      //FAKE COMPANY INFO LIST START - for test only
      //summary image url
      summaryImgUrl: "http://i4.hexun.com/2019-08-01/198052912.png",

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

      //history
      activities: [
        {
          timestamp: "2009",
          content: "Bilibili正式成立",
        },
        {
          timestamp: "2012",
          content: "Bilibili移动端app正式上线",
        },
        {
          timestamp: "2013",
          content: "Bilibili首次举行bml",
        },
        {
          timestamp: "2016",
          content:
            "百度发布2016年搜索报告，bilibili成为十大新鲜关注（00后）第一位",
        },
        {
          timestamp: "2018",
          content: "Bilibili在美国纳斯达克上市，股票代码bili",
        },
        {
          timestamp: "2019",
          content: "阿里巴巴入股Bilibili",
        },
      ],

      //stock info
      ticker: "APPL",
      market: "NASDQ",
      changeNumerical: 1.53,
      changePercentage: 1.319,
      currency: "$",
      current: 117.51,
      timestamp: "10-20 7:59PM PSD",
      open: 116.2,
      low: 115.63,
      high: 118.93,

      //companyrating 公司评分
      companyRating: 3.8,

      //structurelist 公司结构
      structureList: [
        {
          name: "Bilibili Inc. Cayman Islands",
          label: {
            normal: {
              backgroundColor: "#1E90FF",
            },
          },
          children: [
            {
              name: "Bilibili HK Limited (HongKong)",
            },
            {
              name: "Hode Limited (HongKong)",
              children: [
                {
                  name: "Shanghai Bilibili Technology Co.,Ltd",
                },
                {
                  name: "Hode Shanghai Limited('Hode Technology')",
                  children: [
                    {
                      name:
                        "Shanghai Kuanyu Digital Technology Co., Ltd (Shanghai Kuanyu)",
                    },
                    {
                      name:
                        "Shanghai Hode Information Technology Co., Ltd (Shanghai Hode)",
                      children: [
                        {
                          name: "Sharejoy Network Technology Co., Ltd",
                        },
                      ],
                    },
                  ],
                },
              ],
            },
            {
              name: "Bilibili Co., Ltd (Japan)",
            },
          ],
        },
      ],
      employeeData: [
        //雇员数量
        { year: "2010", value: 1000 },
        { year: "2011", value: 2000 },
        { year: "2012", value: 4000 },
        { year: "2013", value: 3000 },
        { year: "2014", value: 1000 },
        { year: "2015", value: 1000 },
        { year: "2016", value: 1234 },
        { year: "2018", value: 2341 },
        { year: "2019", value: 8712 },
        { year: "2020", value: 12432 },
      ],
      addressList: [
        {
          //公司地址
          address: "上海市杨浦区政立路485号国正中心3号楼",
          cityId: 0,
          id: 20,
          latitude: 31.309352,
          longitude: 121.506414,
        },
      ],
      productList: [
        //产品列表
        {
          img:
            "https://x0.ifengimg.com/res/2019/F675FF42FC2459230839D9E9CDB1AF49FEDC5ABD_size414_w2560_h1707.jpeg",
          alt: "小红书产品1",
        },
        {
          img: "http://i.17173cdn.com/2fhnvk/YWxqaGBf/cms3/FVYpgtbnbfxhggm.jpg",
          alt: "小红书产品2",
        },
      ],
      //FAKE COMPANY INFO LIST END
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
    //页面一加载的时候先出来5个推荐公司
    for (var i = 0; i < 5 && i < this.recommendedComps.length; i++) {
      this.companyShowed.push(this.recommendedComps[i]);
    }
    //页面一加载的时候先出来5个最高贡献者
    for (var i = 0; i < 5 && i < this.contributedUsers.length; i++) {
      this.contributorDisplayed.push(this.contributedUsers[i]);
    }
  },
  methods: {
    initData() {
      const { id } = this.$route.params;
      //COMPANY METHODS - MAIN PAGE - INIT
      getCompanyInfo(id).then((response) => {
        this.company = response.data;
        setPageTitle(this.company.name);

        //for test use
        if (this.addressList) {
          for (const addr of this.addressList) {
            addr.mapWindow = {
              position: [addr.longitude, addr.latitude],
              content: addr.address,
            };
          }
        }
      });
      // getCompanyWiki(id).then((response) => {
      //   this.companyWiki = response.data;
      // });
    },
    //change another group of comps, clicking "换一批"
    changeComps() {
      //每次点击换一批触发这个方法
      let totalComp = this.recommendedComps.length;
      this.randomCompIndex = "";
      this.randomCompArr = [];
      this.companyShowed = []; // 每次点击换一批就让之前的数据全清空
      while (this.randomCompArr.length < 5) {
        // 用while来判断是否循环5次
        let num = parseInt(Math.floor(Math.random() * Math.floor(totalComp))); // 随机出5个0-totalcount之间的随机数
        if (this.randomCompArr.indexOf(num) == -1) {
          // 获得不重复的三个数
          this.randomCompArr.push(num);
          this.randomCompIndex = num;
          this.companyShowed.push(this.recommendedComps[this.randomCompIndex]); // 将随机数当作下标来遍历data里的内容
        }
      }
    },
    //展示更多贡献者
    moreContributor() {
      const totalContributor = this.contributedUsers.length;
      this.contributorDisplayed = [];
      for (var i = 0; i < totalContributor; i++) {
        this.contributorDisplayed.push(this.contributedUsers[i]);
      }
    },
    //MAIN PAGE ENDS
  },
};
</script>

<style scoped lang="scss">
.noInfoMsgBox {
  line-height: 80px;
  text-align: center;
  padding-bottom: 20px;
  width: 100%;
}

.app-container {
  margin: 0 auto;
  min-height: calc(100vh - 477px);

  .company-wiki-header-container {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }

  .company-info-container {
    height: calc(91px + 100vw * 0.39);

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
    padding: 0 20px 20px 20px;
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

    .intro-module-element {
      height: 450px;
      margin-bottom: 30px;
    }

    .map-box {
      height: 300px;
    }

    .intro-product-image {
      width: 100%;
      height: 100%;
      object-fit: cover;
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

    .intro-structure-fullscreen {
      position: relative;
      right: 50px;
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