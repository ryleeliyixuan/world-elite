<template>
  <div class="app-container intro-box intro-module" v-if="companyWiki">
    <el-row :gutter="24">
      <el-col class="intro-address intro-module-element" :span="24">
        <h2 class="mt-4 mb-4">
          <i class="el-icon-location" style="color: #1e90ff"></i>
          公司地址
        </h2>
        <div
          v-if="
            companyWiki.company.addressList &&
            companyWiki.company.addressList.length > 0
          "
        >
          <el-collapse
            v-model="activeAddress"
            accordion
            v-for="(addr, index) in companyWiki.company.addressList"
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
        </div>
        <div v-else class="noInfoMsgBox">暂无数据，点击添加</div>
      </el-col>
      <el-col class="intro-employee intro-module-element" :span="24">
        <h2 class="mt-4 mb-4">
          <i class="el-icon-s-cooperation" style="color: #1e90ff"></i>
          雇员数量
        </h2>
        <el-table
          v-if="companyWiki.employeeList && companyWiki.employeeList.length > 0"
          :data="companyWiki.employeeList"
          style="width: 100%"
          max-height="350"
        >
          <el-table-column prop="year" label="年份"> </el-table-column>
          <el-table-column prop="number" label="雇员人数"> </el-table-column>
          <el-table-column label="操作" width="160">
            <template slot-scope="scope">
              <!-- <el-button
                    type="primary"
                    size="mini"
                    @click="
                      handleEditEmployee(
                        2,
                        scope.row.year,
                        scope.row.number,
                        scope.row.id
                      )
                    "
                    >编辑
                  </el-button> -->
              <el-button
                type="danger"
                size="mini"
                @click="delCompanyEmployee(scope.row.id)"
                >删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <div v-else class="noInfoMsgBox">暂无数据，点击添加</div>
        <div class="mt-2 d-flex justify-content-center">
          <el-button
            type="success"
            icon="el-icon-circle-plus"
            @click="handleEditEmployee(1)"
          >
            新 增 雇 员 数 量
          </el-button>
        </div>
        <el-dialog
          title="编辑雇员数量"
          :visible.sync="showEmployeeEditDialog"
          width="70%"
        >
          <el-form
            label-position="right"
            label-width="80px"
            :model="employeeEditForm"
          >
            <el-form-item label="年份">
              <el-input v-model="employeeEditForm.year"></el-input>
            </el-form-item>
            <el-form-item label="雇员数量">
              <el-input v-model="employeeEditForm.number"></el-input>
            </el-form-item>
          </el-form>
          <span slot="footer" class="dialog-footer">
            <el-button @click="showEmployeeEditDialog = false">取 消</el-button>
            <el-button
              type="primary"
              @click="saveCompanyEmployee(), (showEmployeeEditDialog = false)"
              >保 存</el-button
            >
          </span>
        </el-dialog>
      </el-col>
      <el-col class="intro-product intro-module-element" :span="24">
        <h2 class="mt-4 mb-4">
          <i class="el-icon-s-flag" style="color: #1e90ff"></i> 旗下产品
        </h2>
        <div
          v-if="companyWiki.productList && companyWiki.productList.length > 0"
        >
          <el-row :gutter="12">
            <el-col
              :span="6"
              v-for="product in companyWiki.productList"
              :key="product.id"
            >
              <el-card :body-style="{ padding: '0px' }">
                <div class="product-image-wrapper">
                  <img
                    class="product-image-content"
                    :src="product.url"
                    :alt="product.description"
                    fit="cover"
                  />
                </div>
                <div
                  class="mt-2 d-flex justify-content-end"
                  style="padding: 14px"
                >
                  <el-button
                    type="primary"
                    size="mini"
                    @click="handleOpenProductPic(product.url)"
                  >
                    查 看 大 图
                  </el-button>
                  <el-button
                    type="danger"
                    size="mini"
                    @click="delCompanyProduct(product.id)"
                  >
                    删 除
                  </el-button>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </div>
        <div v-else class="noInfoMsgBox">暂无数据，点击添加</div>
        <el-dialog
          title="查看产品大图"
          :visible.sync="showProductDialog"
          width="80%"
        >
          <div class="product-image-dialog-wrapper">
            <img class="product-image-dialog" :src="productOpenUrl" />
          </div>
        </el-dialog>
        <div class="mt-2 d-flex justify-content-center">
          <el-upload
            class="thumbnail-uploader"
            :action="uploadPicOptions.action"
            :data="uploadPicOptions.params"
            :accept="uploadPicOptions.acceptFileType"
            :show-file-list="false"
            :on-success="handleUploadSuccess"
            :before-upload="beforeUpload"
          >
            <el-button type="success" icon="el-icon-edit">
              上 传 产 品 图 片
            </el-button>
          </el-upload>
        </div>
      </el-col>
      <el-col class="intro-valuation intro-module-element" :span="24">
        <div class="edit-button mb-3">
          <h2>
            <i class="el-icon-s-marketing" style="color: #1e90ff"></i>
            市值情况
          </h2>
        </div>
        <el-card
          v-if="companyWiki.market.url && companyWiki.market.url.length > 0"
        >
          市值信息链接(点击打开链接):
          <a :href="companyWiki.market.url" target="_blank">
            {{ companyWiki.market.url }}
          </a>
        </el-card>
        <div v-else class="noInfoMsgBox">暂无数据，点击添加</div>
        <el-dialog
          title="编辑市值情况"
          :visible.sync="showMarketDialog"
          width="50%"
        >
          <el-form label-position="top" label-width="80px" :model="marketForm">
            <el-form-item label="市值信息链接（请上传新浪股票链接）">
              <el-input v-model="marketForm.url"></el-input>
            </el-form-item>
          </el-form>
          <span slot="footer" class="dialog-footer">
            <el-button @click="showMarketDialog = false">取 消</el-button>
            <el-button
              type="primary"
              @click="saveCompanyMarket(), (showMarketDialog = false)"
              >保 存</el-button
            >
          </span>
        </el-dialog>
        <div class="mt-2 d-flex justify-content-center">
          <el-button
            type="success"
            icon="el-icon-edit"
            @click="showMarketDialog = true"
          >
            编 辑 市 值 链 接
          </el-button>
        </div>
      </el-col>
      <el-col class="intro-timeline intro-module-element" :span="24">
        <h2><i class="el-icon-s-data" style="color: #1e90ff"></i> 发展路径</h2>
        <el-table
          v-if="companyWiki.historyList && companyWiki.historyList.length > 0"
          :data="companyWiki.historyList"
          style="width: 100%"
          max-height="350"
        >
          <el-table-column prop="eventTime" label="时间" width="100">
          </el-table-column>
          <el-table-column prop="event" label="事件"> </el-table-column>
          <el-table-column label="操作" width="160">
            <template slot-scope="scope">
              <el-button
                type="primary"
                size="mini"
                @click="
                  handleEditHistory(
                    2,
                    scope.row.eventTime,
                    scope.row.event,
                    scope.row.id
                  )
                "
                >编辑
              </el-button>
              <el-button
                type="danger"
                size="mini"
                @click="delCompanyHistory(scope.row.id)"
                >删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <div v-else class="noInfoMsgBox">暂无数据，点击添加</div>
        <div class="mt-2 d-flex justify-content-center">
          <el-button
            type="success"
            icon="el-icon-circle-plus"
            @click="handleEditHistory(1)"
          >
            新 增 发 展 路 径
          </el-button>
        </div>
        <el-dialog
          title="编辑发展路径"
          :visible.sync="showHistoryEditDialog"
          width="70%"
        >
          <el-form
            label-position="right"
            label-width="80px"
            :model="historyEditForm"
          >
            <el-form-item label="时间">
              <el-date-picker
                type="date"
                placeholder="选择日期"
                v-model="historyEditForm.eventTime"
                style="width: 100%"
              ></el-date-picker>
            </el-form-item>
            <el-form-item label="事件">
              <el-input v-model="historyEditForm.event"></el-input>
            </el-form-item>
          </el-form>
          <span slot="footer" class="dialog-footer">
            <el-button @click="showHistoryEditDialog = false">取 消</el-button>
            <el-button
              type="primary"
              @click="saveCompanyHistory(), (showHistoryEditDialog = false)"
              >保 存</el-button
            >
          </span>
        </el-dialog>
      </el-col>
      <el-col class="intro-structure intro-module-element" :span="24">
        <h2 class="mt-4 mb-4">
          <i class="el-icon-s-custom" style="color: #1e90ff"></i>
          公司架构
        </h2>
        <!-- <div
          class="d-flex justify-content-end"
          v-if="companyWiki.structure && companyWiki.structure.length > 0"
        >
          <el-button
            class="intro-structure-fullscreen"
            size="mini"
            type="primary"
            icon="el-icon-full-screen"
            @click="fullScreen = true"
            plain
            >查 看 效 果 图</el-button
          >
        </div> -->
        <!-- <el-dialog title="公司结构" :visible.sync="fullScreen" width="90%">
          <TreeChart
            :items="companyWiki.structure"
            class="intro-structure-chart"
          ></TreeChart>
        </el-dialog> -->
        <el-tree
          v-if="companyWiki.structure && companyWiki.structure.length > 0"
          :data="companyWiki.structure"
          :props="defaultProps"
          default-expand-all
          :render-content="renderContent"
        >
        </el-tree>
        <div v-else class="noInfoMsgBox">暂无数据，点击添加</div>
        <div class="mt-2 d-flex justify-content-center">
          <el-button
            v-if="companyWiki.structure && companyWiki.structure.length == 0"
            type="success"
            icon="el-icon-circle-plus"
            @click="showStructureDialog = true"
          >
            新 增 公 司 架 构
          </el-button>
        </div>
        <el-dialog
          title="新增公司架构节点"
          :visible.sync="showStructureDialog"
          width="70%"
        >
          <el-form
            label-position="top"
            label-width="80px"
            :model="employeeEditForm"
          >
            <el-form-item label="节点名称">
              <el-input v-model="strucureFrom.name"></el-input>
            </el-form-item>
          </el-form>
          <span slot="footer" class="dialog-footer">
            <el-button @click="showStructureDialog = false">取 消</el-button>
            <el-button
              type="primary"
              @click="saveCompanyStructure(), (showStructureDialog = false)"
              >保 存</el-button
            >
          </span>
        </el-dialog>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import Vue from "vue";
import VueAMap from "vue-amap";
import axios from "axios";
import BarChart from "@/components/BarChart";
import TreeChart from "@/components/TreeChart";
import Pagination from "@/components/Pagination";
import { getUploadPicToken } from "@/api/upload_api";
import { formatListQuery, parseListQuery } from "@/utils/common";

import { getCompanyWiki } from "@/api/company_api";
import {
  saveCompanyMarket,
  saveCompanyHistory,
  delCompanyHistory,
  saveCompanyEmployee,
  delCompanyEmployee,
  saveCompanyProduct,
  delCompanyProduct,
  saveCompanyStructure,
  delCompanyStructure,
  listCompanyStructure,
} from "@/api/wiki_api";
import Toast from "@/utils/toast";
import { faUserInjured } from "@fortawesome/free-solid-svg-icons";

Vue.use(VueAMap);

VueAMap.initAMapApiLoader({
  key: process.env.VUE_APP_AMAP_KEY,
  v: "1.4.4",
});

export default {
  name: "WikiEditPage",
  components: { Pagination, BarChart, TreeChart },
  data() {
    return {
      uploadPicOptions: {
        action: "",
        params: {},
        fileUrl: "",
        acceptFileType: ".jpg,.jpeg,.png,.JPG,.JPEG,.PNG",
      },
      //MARKET
      showMarketDialog: false,
      marketForm: {
        url: "",
      },
      //HISTORY
      showHistoryEditDialog: false,
      historyEditForm: {
        eventTime: undefined,
        event: undefined,
      },
      //employee
      showEmployeeEditDialog: false,
      employeeEditForm: {
        year: undefined,
        number: undefined,
      },
      //product
      showProductDialog: false,
      productOpenUrl: "",
      productForm: {
        url: undefined,
      },
      //structure
      showStructureDialog: false,
      options: [],
      structureList: [],
      strucureFrom: {
        parentId: undefined,
        name: undefined,
      },
      defaultProps: {
        children: "children",
        label: "name",
      },
      structureData: {},
      //MAIN PAGE ATTRIBUTE
      company: undefined,
      companyId: undefined,
      companyWiki: {
        employeeList: [],
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
      fullScreen: false,
      //intro-relatives
      companyShowed: [], // 用来存放每次点击换一批出来的5个对象
      randomCompIndex: "", // 用来放5个随机数
      randomCompArr: [], // 用来放5个随机数的数组，用来循环
      //intro-contributor
      contributorDisplayed: [],
      contributorIndex: "",
      contributorIndex: [],

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
  methods: {
    initData() {
      this.companyId = this.$route.query.id;
      getCompanyWiki(this.companyId).then((response) => {
        this.companyWiki = response.data;
        if (this.companyWiki.company.addressList) {
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

        if (
          this.companyWiki.structure &&
          this.companyWiki.structure.length > 0
        ) {
          this.getStructure();
        }
      });
    },
    renderContent(h, { node, data, store }) {
      return (
        <span style="flex: 1; display: flex; align-items: center; justify-content: space-between; font-size: 14px; padding-right: 8px;">
          <span>
            <span>{node.label}</span>
          </span>
          <span>
            <el-button
              style="font-size: 12px;"
              type="text"
              on-click={() => this.append(data)}
            >
              增加子节点
            </el-button>
            <el-button
              style="font-size: 12px;"
              type="text"
              on-click={() => this.remove(node, data)}
            >
              删除
            </el-button>
          </span>
        </span>
      );
    },
    append(data) {
      this.showStructureDialog = true;
      this.structureData = data;
    },
    remove(node, data) {
      this.$confirm("是否要删除该条节点？", {
        confirmButtonText: "删除",
      }).then(() => {
        const parent = node.parent;
        const children = parent.data.children || parent.data;
        const index = children.findIndex((d) => d.id === data.id);
        delCompanyStructure(data.id).then((response) => {
          Toast.success("组织结构节点删除成功");
          children.splice(index, 1);
        });
      });
    },
    saveCompanyStructure() {
      let data = {
        companyId: this.companyId,
        parentId: this.structureData.id,
        name: this.strucureFrom.name,
      };
      saveCompanyStructure(data).then((response) => {
        Toast.success("组织结构节点增加成功");
        this.initData();
      });
    },
    getStructure() {
      listCompanyStructure(this.companyId).then((response) => {
        this.structureList = response.data;
      });
    },
    getMarketInfo() {
      if (this.stockUrl) {
        this.marketForm.url = this.stockUrl;
        var url = this.stockUrl.split(".");
        this.stockUrl = "/" + url[1] + "." + url[2];
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
    saveCompanyMarket() {
      let data = {
        companyId: this.companyId,
        url: this.marketForm.url,
      };
      saveCompanyMarket(data).then((response) => {
        Toast.success("市值信息保存成功");
        this.initData();
      });
    },
    handleEditHistory(type, eventTime, event, id) {
      this.showHistoryEditDialog = true;
      if (type === 1) {
        this.historyEditForm.eventTime = undefined;
        this.historyEditForm.event = undefined;
        this.historyEditForm.id = undefined;
      } else {
        this.historyEditForm.eventTime = eventTime;
        this.historyEditForm.event = event;
        this.historyEditForm.id = id;
      }
    },
    saveCompanyHistory(eventTime, event) {
      let data = {
        id: this.historyEditForm.id,
        companyId: this.companyId,
        eventTime: this.historyEditForm.eventTime,
        event: this.historyEditForm.event,
      };
      saveCompanyHistory(data).then((response) => {
        Toast.success("发展路径修改成功");
        this.initData();
      });
    },
    delCompanyHistory(historyId) {
      this.$confirm("是否要删除该条发展路径？", {
        confirmButtonText: "删除",
      }).then(() => {
        delCompanyHistory(historyId).then((response) => {
          Toast.success("发展路径删除成功");
          this.initData();
        });
      });
    },
    handleEditEmployee(type, year, number, id) {
      this.showEmployeeEditDialog = true;
      if (type === 1) {
        this.employeeEditForm.year = undefined;
        this.employeeEditForm.number = undefined;
        this.employeeEditForm.id = undefined;
      } else {
        this.employeeEditForm.year = year;
        this.employeeEditForm.number = number;
        this.employeeEditForm.id = id;
      }
    },
    saveCompanyEmployee(year, number) {
      let data = {
        id: this.employeeEditForm.id,
        companyId: this.companyId,
        year: this.employeeEditForm.year,
        number: Number(this.employeeEditForm.number),
      };
      saveCompanyEmployee(data).then((response) => {
        Toast.success("雇员数量修改成功");
        this.initData();
      });
    },
    delCompanyEmployee(employeeId) {
      this.$confirm("是否要删除该条雇员数量？", {
        confirmButtonText: "删除",
      }).then(() => {
        delCompanyEmployee(employeeId).then((response) => {
          Toast.success("雇员数量删除成功");
          this.initData();
        });
      });
    },
    beforeUpload(file) {
      return new Promise((resolve, reject) => {
        getUploadPicToken(file.name)
          .then((response) => {
            const { data } = response;
            this.uploadPicOptions.action = data.host;
            this.uploadPicOptions.params = data;
            this.uploadPicOptions.fileUrl = data.host + "/" + data.key;
            resolve(data);
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    handleUploadSuccess() {
      let data = {
        companyId: this.companyId,
        url: this.uploadPicOptions.fileUrl,
      };
      saveCompanyProduct(data).then((response) => {
        Toast.success("产品图片上传成功");
        this.initData();
      });
    },
    delCompanyProduct(id) {
      this.$confirm("是否要删除该产品图片？", {
        confirmButtonText: "删除",
      }).then(() => {
        delCompanyProduct(id).then((response) => {
          Toast.success("产品图片删除成功");
          this.initData();
        });
      });
    },
    handleOpenProductPic(url) {
      this.showProductDialog = true;
      this.productOpenUrl = url;
    },
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

.edit-button {
  display: flex;
  flex-direction: row;
  align-content: center;
  justify-content: space-between;
}

.map-box {
  height: 300px;
}

.product-image-dialog-wrapper {
  .product-image-dialog {
    position: relative;
    width: 100%;
    height: 100%;
  }
}

.product-image-wrapper {
  position: relative;
  width: 100%;
  height: 0;
  padding-top: 100%;
  .product-image-content {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
  }
}

.app-container {
  margin: 0px 12px 12px 12px;
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
</style>