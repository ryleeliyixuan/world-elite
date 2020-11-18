<template>
  <div class="app-container intro-box intro-module" v-if="companyWiki">
    <el-row :gutter="24">
      <el-col class="intro-employee intro-module-element" :span="24">
        <div class="d-flex justify-content-start align-items-center">
          <h2 style="margin-right: 18px">
            <i class="el-icon-s-cooperation" style="color: #1e90ff"></i>
            雇员数量
          </h2>
          <span>
            启用该模块：
            <el-switch
              v-model="wikiModule.employeeEnable"
              active-color="#13ce66"
              inactive-color="#ff4949"
              :active-value="1"
              :inactive-value="0"
              @change="
                changeEmployeeStatus(
                  $event,
                  companyWiki.wikiModule.id,
                  companyId
                )
              "
            >
            </el-switch>
          </span>
        </div>
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
        <div class="d-flex justify-content-start align-items-center">
          <h2 style="margin-right: 18px">
            <i class="el-icon-s-flag" style="color: #1e90ff"></i> 旗下产品
          </h2>
          <span>
            启用该模块：
            <el-switch
              v-model="wikiModule.productEnable"
              active-color="#13ce66"
              inactive-color="#ff4949"
              :active-value="1"
              :inactive-value="0"
              @change="
                changeProductStatus(
                  $event,
                  companyWiki.wikiModule.id,
                  companyId
                )
              "
            >
            </el-switch>
          </span>
        </div>
        <div
          v-if="companyWiki.productList && companyWiki.productList.length > 0"
        >
          <el-table
            :data="companyWiki.productList"
            style="width: 100%"
            max-height="500"
          >
            <el-table-column prop="position" label="顺序" width="100">
            </el-table-column>
            <el-table-column prop="imageName" label="图片"> </el-table-column>
            <el-table-column prop="name" label="产品名"> </el-table-column>
            <el-table-column prop="description" label="文字说明">
            </el-table-column>
            <el-table-column label="链接（点击查看图片）"
              ><template slot-scope="scope">
                <el-link
                  :href="scope.row.url"
                  target="_blank"
                  :underline="true"
                  >{{ scope.row.url }}</el-link
                >
              </template></el-table-column
            >
            <el-table-column label="操作" width="160">
              <template slot-scope="scope">
                <el-button
                  type="danger"
                  size="mini"
                  @click="delCompanyProduct(scope.row.id)"
                >
                  删 除
                </el-button>
                <el-button
                  type="text"
                  size="mini"
                  @click="moveProductForward(scope.row.id, scope.$index)"
                >
                  上 移
                </el-button>
                <el-button
                  type="text"
                  size="mini"
                  @click="moveProductDownward(scope.row.id, scope.$index)"
                >
                  下 移
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <div v-else class="noInfoMsgBox">暂无数据，点击添加</div>
        <el-dialog
          title="编辑旗下产品"
          :visible.sync="showProductDialog"
          width="80%"
        >
          <el-form
            label-position="right"
            label-width="80px"
            ref="productForm"
            :model="productForm"
            :rules="productFormRules"
          >
            <el-form-item label="产品名">
              <el-input v-model="productForm.name"></el-input>
            </el-form-item>
            <el-form-item label="文字说明">
              <el-input v-model="productForm.description"></el-input>
            </el-form-item>
            <el-form-item label="产品图片">
              <el-upload
                class="thumbnail-uploader"
                :action="uploadPicOptions.action"
                :data="uploadPicOptions.params"
                :accept="uploadPicOptions.acceptFileType"
                :before-upload="beforeUpload"
                :on-success="handleUploadSuccess"
                list-type="picture"
              >
                <el-button type="success" size="mini" icon="el-icon-edit">
                  上 传 产 品 图 片
                </el-button>
              </el-upload>
            </el-form-item>
          </el-form>
          <span slot="footer" class="dialog-footer">
            <el-button @click="showProductDialog = false">取 消</el-button>
            <el-button
              type="primary"
              @click="saveCompanyProduct(), (showProductDialog = false)"
              >保 存</el-button
            >
          </span>
        </el-dialog>
        <div class="mt-2 d-flex justify-content-center">
          <el-button
            type="success"
            icon="el-icon-edit"
            @click="showProductDialog = true"
          >
            上 传 旗 下 产 品
          </el-button>
        </div>
      </el-col>
      <el-col class="intro-valuation intro-module-element" :span="24">
        <div class="d-flex justify-content-start align-items-center">
          <h2 style="margin-right: 18px">
            <i class="el-icon-s-marketing" style="color: #1e90ff"></i>
            市值情况
          </h2>
          <span>
            启用该模块：
            <el-switch
              v-model="wikiModule.marketEnable"
              active-color="#13ce66"
              inactive-color="#ff4949"
              :active-value="1"
              :inactive-value="0"
              @change="
                changeMarketStatus($event, companyWiki.wikiModule.id, companyId)
              "
            >
            </el-switch>
          </span>
        </div>
        <el-card
          v-if="
            companyWiki.market &&
            companyWiki.market.url &&
            companyWiki.market.url.length > 0
          "
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
        <div class="d-flex justify-content-start align-items-center">
          <h2 style="margin-right: 18px">
            <i class="el-icon-s-data" style="color: #1e90ff"></i> 发展路径
          </h2>
          <span>
            启用该模块：
            <el-switch
              v-model="wikiModule.historyEnable"
              active-color="#13ce66"
              inactive-color="#ff4949"
              :active-value="1"
              :inactive-value="0"
              @change="
                changeHistoryStatus(
                  $event,
                  companyWiki.wikiModule.id,
                  companyId
                )
              "
            >
            </el-switch>
          </span>
        </div>
        <el-table
          v-if="companyWiki.historyList && companyWiki.historyList.length > 0"
          :data="companyWiki.historyList"
          style="width: 100%"
          max-height="500"
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
                value-format="yyyy-MM-dd"
                v-model="historyEditForm.eventTime"
                :picker-options="oldDatePickerOptions"
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
        <div class="d-flex justify-content-start align-items-center">
          <h2 style="margin-right: 18px">
            <i class="el-icon-s-custom" style="color: #1e90ff"></i>
            公司架构
          </h2>
          <span>
            启用该模块：
            <el-switch
              v-model="wikiModule.structureEnable"
              active-color="#13ce66"
              inactive-color="#ff4949"
              :active-value="1"
              :inactive-value="0"
              @change="
                changeStructureStatus(
                  $event,
                  companyWiki.wikiModule.id,
                  companyId
                )
              "
            >
            </el-switch>
          </span>
        </div>
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
import { lazyAMapApiLoaderInstance } from "vue-amap";
import axios from "axios";
import BarChart from "@/components/BarChart";
import TreeChart from "@/components/TreeChart";
import Pagination from "@/components/Pagination";
import { getUploadPicToken } from "@/api/upload_api";
import { formatListQuery, parseListQuery } from "@/utils/common";

import {
  getCompanyWiki,
  delCompanyAddr,
  saveCompanyAddr,
  changeModuleEnable,
} from "@/api/company_api";
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
  plugin: [
    "AMap.Autocomplete",
    "AMap.PlaceSearch",
    "AMap.Scale",
    "AMap.Geocoder",
  ],
  v: "1.4.4",
});

let geocoder;

lazyAMapApiLoaderInstance.load().then(() => {
  // eslint-disable-next-line no-undef
  geocoder = new AMap.Geocoder({ extensions: "base" });
});

export default {
  name: "WikiEditPage",
  components: { Pagination, BarChart, TreeChart },
  data() {
    return {
      posting: false,
      wikiModule: {
        id: undefined,
        addressEnable: undefined,
        employeeEnable: "0",
      },
      employeeEnable: undefined,
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
      oldDatePickerOptions: {
        disabledDate(time) {
          return time.getTime() >= Date.now() - 8.64e7;
        },
      },
      //employee
      showEmployeeEditDialog: false,
      employeeEditForm: {
        year: undefined,
        number: undefined,
      },
      //product
      showProductDialog: false,
      productIdList: [],
      productForm: {
        id: undefined,
        companyId: undefined,
        url: "",
        description: "",
        imageName: "",
        name: "",
        position: undefined,
      },
      productFormRules: {
        name: [{ required: true, message: "请填写产品名", trigger: "blur" }],
        description: [
          { required: true, message: "请输入文字说明", trigger: "blur" },
        ],
        url: [{ required: true, message: "请上传产品图片", trigger: "blur" }],
      },
      uploadPicOptions: {
        action: "",
        params: {},
        fileUrl: "",
        acceptFileType: ".jpg,.jpeg,.png,.JPG,.JPEG,.PNG",
        imageName: "",
      },
      //structure
      showStructureDialog: false,
      options: [],
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
        market: {},
        productList: [],
        structure: [],
        company: {
          addressList: [],
        },
      },
      load: true,
    };
  },
  watch: {
    $route() {
      this.initData();
    },
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
      this.productForm.companyId = this.companyId;
      getCompanyWiki(this.companyId).then((response) => {
        this.companyWiki = response.data;
        this.wikiModule = response.data.wikiModule;
        if (
          this.companyWiki.market &&
          this.companyWiki.market.url &&
          this.companyWiki.market.url.length > 0
        ) {
          this.marketForm.url = this.companyWiki.market.url;
        }
        this.getProductIdList();
      });
    },
    getProductIdList() {
      this.productIdList = []; //empty list
      for (let i = 0; i < this.companyWiki.productList.length; i++) {
        let product = this.companyWiki.productList[i];
        this.productIdList.push(product.id);
      }
    },
    //change module status
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
    //tree
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
      this.uploadPicOptions.imageName = file.name;
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
      this.productForm.url = this.uploadPicOptions.fileUrl;
      this.productForm.imageName = this.uploadPicOptions.imageName;
      this.$message.success("图片上传成功");
      const len = this.companyWiki.productList.length;
      this.productForm.position = len > 0 ? len : 0;
    },
    saveCompanyProduct() {
      console.log("---productform----", this.productForm);
      this.$refs["productForm"].validate((valid) => {
        if (valid) {
          saveCompanyProduct(this.productForm).then(() => {
            Toast.success("旗下产品上传成功");
            this.initData();
            this.productForm.url = undefined;
            this.productForm.imageName = undefined;
            this.productForm.name = undefined;
            this.productForm.description = undefined;
          });
        }
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
    moveProductForward(id, index) {
      if (index === 0) {
        this.$message.warning("该图片已在第一位，位置不能上升");
      } else {
        const newP = index - 1;
        let data = {
          companyId: this.companyId,
          id: id,
          position: newP,
        };
        saveCompanyProduct(data);
        let data2 = {
          companyId: this.companyId,
          id: this.productIdList[index - 1],
          position: index,
        };
        saveCompanyProduct(data2).then(() => {
          Toast.success("产品图片上升成功");
          this.initData();
        });
      }
    },
    moveProductDownward(id, index) {
      if (index === this.companyWiki.productList.length - 1) {
        this.$message.warning("该图片已在最后一位，位置不能下移");
      } else {
        const newP = index + 1;
        let data = {
          companyId: this.companyId,
          id: id,
          position: newP,
        };
        saveCompanyProduct(data);
        let data2 = {
          companyId: this.companyId,
          id: this.productIdList[index + 1],
          position: index,
        };
        saveCompanyProduct(data2).then(() => {
          Toast.success("产品图片下移成功");
          this.initData();
        });
      }
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

.map-box {
  height: 300px;
}
.map-search-box {
  position: absolute;
  top: 70px;
  left: 20px;
}

.intro-module-element {
  margin-bottom: 36px;
}

.app-container {
  margin: 0px 12px 12px 12px;
  min-height: calc(100vh - 477px);
}
</style>