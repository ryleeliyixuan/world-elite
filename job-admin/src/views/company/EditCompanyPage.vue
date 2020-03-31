<template>
  <div class="edit-company-container">
    <el-row :gutter="20">
      <el-col :span="18">
        <div class="company-body">
          <div class="company-box mt-3">
            <EditResumeTitle
              title="基本信息"
              icon="el-icon-edit"
              iconText="编辑"
              v-on:edit-click="showBasicDialog = true;"
            />
            <div class="mt-2 mb-2">
              <el-upload
                class="avatar-uploader"
                :action="uploadPicOptions.action"
                :data="uploadPicOptions.params"
                :accept="uploadPicOptions.acceptFileType"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                :before-upload="beforeAvatarUpload"
              >
                <img v-if="company.logo && company.logo !== ''" :src="company.logo" class="avatar" />
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>
            </div>
            <p>公司全称：{{company.fullName}}</p>
            <p>公司简称：{{company.name}}</p>
            <p>
              公司主页：
              <el-link
                :href="companyLink"
                target="_blank"
                :underline="false"
                v-if="company.homepage"
              >
                {{companyLinkName}}
                <i class="el-icon-link el-icon--right"></i>
              </el-link>
            </p>
            <p>
              <span>公司规模：{{company.scale?company.scale.name:''}}</span>
              <span class="ml-4">所在行业：{{company.industry?company.industry.name:''}}</span>
              <span class="ml-4">公司性质：{{company.property?company.property.name:''}}</span>
              <span class="ml-4">公司阶段：{{company.stage?company.stage.name:''}}</span>
            </p>
            <p>一句话介绍：{{company.synopsis}}</p>
          </div>
          <div class="company-box box-margin">
            <EditResumeTitle
              title="公司介绍"
              icon="el-icon-edit"
              iconText="编辑"
              v-on:edit-click="showIntroDialog = true"
            />
            <div class="mt-2" v-html="company.introduction"></div>
          </div>
          <div class="company-box box-margin">
            <EditResumeTitle
              title="公司地址"
              icon="el-icon-plus"
              iconText="添加"
              v-on:edit-click="handleEditCompanyAddress"
              class="mb-4"
            />
            <el-collapse
              v-model="activeAddress"
              accordion
              v-for="(addr, index) in company.addressList"
              :key="addr.id"
            >
              <el-collapse-item :title="addr.address" :name="index">
                <div class="map-box">
                  <el-amap :vid="'amap' + index" :zoom="mapZoom" :center="addr.mapWindow.position">
                    <el-amap-info-window
                      :position="addr.mapWindow.position"
                      :content="addr.mapWindow.content"
                    ></el-amap-info-window>
                  </el-amap>
                </div>
                <div class="address-eidt mt-3">
                  <el-link
                    icon="el-icon-edit"
                    :underline="false"
                    @click="handleEditCompanyAddress('update', addr)"
                  >编辑</el-link>
                  <el-link
                    class="ml-2"
                    icon="el-icon-delete"
                    type="danger"
                    :underline="false"
                    @click="handleDelCompanAddr(addr.id)"
                  >删除</el-link>
                </div>
              </el-collapse-item>
            </el-collapse>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="preview-box">
          <el-button type="primary" style="margin-bottom: 20px" @click="goCompanyHome">查看主页</el-button>
          <p>公司主页完成度</p>
          <el-progress
            :text-inside="true"
            :stroke-width="26"
            :percentage="company.completeProgress"
          ></el-progress>
        </div>
      </el-col>
    </el-row>
    <!--编辑基本信息-->
    <el-dialog title="编辑基本信息" :visible.sync="showBasicDialog" width="700px" top="10vh">
      <el-form ref="companyForm" :model="companyForm" :rules="companyFormRules" label-width="100px">
        <el-form-item label="公司简称" prop="name">
          <el-input
            v-model="companyForm.name"
            placeholder="请填写公司简称"
            :maxlength="20"
            show-word-limit
            class="w-50"
          ></el-input>
        </el-form-item>
        <el-form-item label="公司规模" prop="scaleId">
          <el-select v-model="companyForm.scaleId" filterable clearable placeholder="请选择公司规模">
            <el-option
              v-for="item in scaleOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="公司阶段" prop="stageId">
          <el-select v-model="companyForm.stageId" filterable clearable placeholder="请选择公司阶段">
            <el-option
              v-for="item in stageOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="企业性质" prop="propertyId">
          <el-select v-model="companyForm.propertyId" filterable clearable placeholder="请选择企业性质">
            <el-option
              v-for="item in propertyOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="行业" prop="industryId">
          <el-select v-model="companyForm.industryId" filterable clearable placeholder="请选择行业">
            <el-option
              v-for="item in industryOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="公司主页" prop="homepage">
          <el-input
            v-model="companyForm.homepage"
            placeholder="请填写主页链接"
            :maxlength="100"
            show-word-limit
          ></el-input>
        </el-form-item>
        <el-form-item label="一句话介绍" prop="synopsis">
          <el-input
            v-model="companyForm.synopsis"
            placeholder="一句话介绍"
            :maxlength="120"
            show-word-limit
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="showBasicDialog = false">取 消</el-button>
        <el-button type="primary" @click="handleSaveCompanyBasic" :loading="posting">保 存</el-button>
      </div>
    </el-dialog>
    <!--编辑公司介绍-->
    <el-dialog title="编辑公司介绍" :visible.sync="showIntroDialog" width="600px" top="10vh">
      <quill-editor v-model="companyForm.introduction" :options="introEditorOption"></quill-editor>
      <div slot="footer" class="dialog-footer">
        <el-button @click="showIntroDialog = false">取 消</el-button>
        <el-button type="primary" @click="saveCompanyInfo" :loading="posting">保 存</el-button>
      </div>
    </el-dialog>
    <!--添加公司地址-->
    <el-dialog title="添加公司地址" :visible.sync="showAddressDialog" width="600px" top="10vh">
      <el-form
        ref="companyAddrForm"
        :model="companyAddrForm"
        :rules="companyAddrFormRules"
        label-width="80px"
      >
        <el-form-item label="公司地址" prop="address">
          <el-input
            v-model="companyAddrForm.address"
            placeholder="请填写公司简称"
            :maxlength="20"
            show-word-limit
          ></el-input>
        </el-form-item>
        <el-form-item label="选择定位" prop="location">
          <p class="text-gray">输入城市和写字楼位置，然后拖动定位器进行选择。</p>
          <div class="map-box">
            <el-amap vid="marker" :zoom="mapZoom" :center="poiMapMarker.position">
              <el-amap-search-box
                class="map-search-box"
                :search-option="mapSearchOption"
                :on-search-result="onSearchPoiResult"
              ></el-amap-search-box>

              <el-amap-marker
                vid="poi-marker"
                :position="poiMapMarker.position"
                :events="poiMapMarker.events"
                :draggable="poiMapMarker.draggable"
              ></el-amap-marker>
            </el-amap>
          </div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="showAddressDialog = false">取 消</el-button>
        <el-button type="primary" @click="handleSaveCompanyAddr" :loading="posting">保 存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Vue from "vue";
import "quill/dist/quill.core.css";
import "quill/dist/quill.snow.css";
import "quill/dist/quill.bubble.css";
import { quillEditor } from "vue-quill-editor";
import VueAMap from "vue-amap";
import { lazyAMapApiLoaderInstance } from "vue-amap";
import EditResumeTitle from "./EditResumeTitle";
import { getUploadPicToken } from "@/api/upload_api";
import { listByType } from "@/api/dict_api";

import {
  getCompanyInfo,
  saveCompany,
  saveCompanyAddr,
  delCompanyAddr
} from "@/api/company_api";

Vue.use(VueAMap);

VueAMap.initAMapApiLoader({
  key: process.env.VUE_APP_AMAP_KEY,
  plugin: [
    "AMap.Autocomplete",
    "AMap.PlaceSearch",
    "AMap.Scale",
    "AMap.Geocoder"
  ],
  v: "1.4.4"
});

let geocoder;

lazyAMapApiLoaderInstance.load().then(() => {
  // eslint-disable-next-line no-undef
  geocoder = new AMap.Geocoder({ extensions: "base" });
});

export default {
  name: "EditCompanyPage",
  components: {
    EditResumeTitle,
    quillEditor
  },
  data() {
    return {
      company: {},
      companyForm: {
        id: undefined,
        name: undefined,
        logo: undefined,
        scaleId: undefined,
        stageId: undefined,
        industryId: undefined,
        propertyId: undefined,
        homepage: undefined,
        introduction: undefined,
        synopsis: undefined
      },
      companyFormRules: {
        name: [{ required: true, message: "请输入公司简称", trigger: "blur" }],
        scaleId: [
          { required: true, message: "请选择公司规模", trigger: "change" }
        ],
        stageId: [
          { required: true, message: "请选择公司阶段", trigger: "change" }
        ],
        industryId: [
          { required: true, message: "请选择行业", trigger: "change" }
        ],
        propertyId: [
          { required: true, message: "请选择企业性质", trigger: "change" }
        ]
      },
      companyAddrForm: {
        id: undefined,
        companyId: undefined,
        address: undefined,
        location: undefined,
        latitude: undefined,
        longitude: undefined
      },
      companyAddrFormRules: {
        address: [
          { required: true, message: "请输入公司地址", trigger: "blur" }
        ],
        location: [
          { required: true, message: "请选择公司坐标", trigger: "blur" }
        ]
      },
      activeAddress: 0,
      uploadPicOptions: {
        action: "",
        params: {},
        fileUrl: "",
        acceptFileType: ".jpg,.jpeg,.png,.JPG,.JPEG,.PNG"
      },
      introEditorOption: {
        theme: "snow",
        placeholder: "公司介绍",
        modules: {
          toolbar: [["bold"], [{ list: "ordered" }, { list: "bullet" }]]
        }
      },
      showBasicDialog: false,
      showIntroDialog: false,
      showAddressDialog: false,
      posting: false,
      scaleOptions: [],
      stageOptions: [],
      industryOptions: [],
      propertyOptions: [],

      mapZoom: 14,
      poiMapMarker: {
        position: [121.5273285, 31.21515044],
        events: {
          dragend: e => {
            this.poiMapMarker.position = [e.lnglat.lng, e.lnglat.lat];
          }
        },
        draggable: true
      },
      mapSearchOption: {
        city: "北京",
        citylimit: false
      }
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
    companyLinkName() {
      return this.company.homepage.replace(/http(s)?:\/\//);
    }
  },
  watch: {
    companyAddrForm: {
      handler(addr) {
        this.companyAddrForm.location =
          addr.latitude && addr.longitude ? "1" : undefined;
      },
      deep: true
    },
    poiMapMarker: {
      handler(location) {
        this.companyAddrForm.longitude = location.position[0];
        this.companyAddrForm.latitude = location.position[1];
      },
      deep: true
    },
    $route() {
      this.loadCompanyInfo();
    }
  },
  methods: {
    initData() {
      this.loadCompanyInfo();
      listByType(4).then(response => (this.stageOptions = response.data.list));
      listByType(5).then(response => (this.scaleOptions = response.data.list));
      listByType(6).then(
        response => (this.industryOptions = response.data.list)
      );
      listByType(7).then(
        response => (this.propertyOptions = response.data.list)
      );
    },
    loadCompanyInfo() {
      const companyId = this.$route.query.id;
      if (companyId === undefined) {
        return;
      }
      getCompanyInfo(companyId).then(response => {
        this.company = response.data;
        this.companyForm.id = this.company.id;
        this.companyForm.name = this.company.name;
        this.companyForm.logo = this.company.logo;
        this.companyForm.scaleId = this.company.scale
          ? this.company.scale.id
          : undefined;
        this.companyForm.stageId = this.company.stage
          ? this.company.stage.id
          : undefined;
        this.companyForm.industryId = this.company.industry
          ? this.company.industry.id
          : undefined;
        this.companyForm.propertyId = this.company.property
          ? this.company.property.id
          : undefined;
        this.companyForm.homepage = this.company.homepage;
        this.companyForm.introduction = this.company.introduction;
        this.companyForm.synopsis = this.company.synopsis;

        this.companyAddrForm.companyId = this.company.id;

        if (this.company.addressList) {
          for (const addr of this.company.addressList) {
            addr.mapWindow = {
              position: [addr.longitude, addr.latitude],
              content: addr.address
            };
          }
        }
      });
    },
    handleEditCompanyAddress(type, address) {
      this.showAddressDialog = true;
      if ("update" == type) {
        this.companyAddrForm.id = address.id;
        this.companyAddrForm.address = address.address;
        this.companyAddrForm.latitude = address.latitude;
        this.companyAddrForm.longitude = address.longitude;
        this.poiMapMarker.position = [address.longitude, address.latitude];
      } else {
        this.companyAddrForm.id = undefined;
        this.companyAddrForm.address = undefined;
        this.companyAddrForm.location = undefined;
        this.companyAddrForm.latitude = undefined;
        this.companyAddrForm.longitude = undefined;
      }
    },
    beforeAvatarUpload(file) {
      return new Promise((resolve, reject) => {
        getUploadPicToken(file.name)
          .then(response => {
            const { data } = response;
            this.uploadPicOptions.action = data.host;
            this.uploadPicOptions.params = data;
            this.uploadPicOptions.fileUrl = data.host + "/" + data.key;
            resolve(data);
          })
          .catch(error => {
            reject(error);
          });
      });
    },
    handleAvatarSuccess() {
      this.companyForm.logo = this.uploadPicOptions.fileUrl;
      this.saveCompanyInfo();
    },
    handleSaveCompanyBasic() {
      this.$refs["companyForm"].validate(valid => {
        if (valid) {
          this.saveCompanyInfo();
        }
      });
    },
    handleSaveCompanyAddr() {
      geocoder.getAddress(this.poiMapMarker.position, (status, result) => {
        if (status === "complete" && result.info === "OK") {
          const city = result.regeocode.addressComponent.city;
          if(city != ''){
              this.companyAddrForm.city = city.endsWith('市') ? city.substr(0, city.length - 1): city;
          }
          
          // 直辖市
          const province = result.regeocode.addressComponent.province;
          if(this.companyAddrForm.city == '' && province.endsWith('市')){
            this.companyAddrForm.city = province.substr(0, province - 1);
          }
          
          this.$refs["companyAddrForm"].validate(valid => {
            if (valid) {
              this.posting = true;
              saveCompanyAddr(this.companyAddrForm)
                .then(() => {
                  this.$message("保存成功");
                  this.loadCompanyInfo();
                  this.showAddressDialog = false;
                })
                .finally(() => {
                  this.posting = false;
                });
            }
          });
        }
      });
    },
    handleDelCompanAddr(id) {
      this.$confirm("是否要删除该记录？", {
        confirmButtonText: "删除"
      }).then(() => {
        delCompanyAddr(id).then(() => {
          this.$message("操作成功");
          this.loadCompanyInfo();
        });
      });
    },
    saveCompanyInfo() {
      this.posting = true;
      saveCompany(this.companyForm)
        .then(() => {
          this.$message("保存成功");
          this.loadCompanyInfo();
          this.showBasicDialog = false;
          this.showAddressDialog = false;
          this.showIntroDialog = false;
        })
        .finally(() => {
          this.posting = false;
        });
    },
    onSearchPoiResult(pois) {
      let latSum = 0;
      let lngSum = 0;
      if (pois.length > 0) {
        pois.forEach(poi => {
          let { lng, lat } = poi;
          lngSum += lng;
          latSum += lat;
        });
        let center = {
          lng: lngSum / pois.length,
          lat: latSum / pois.length
        };
        this.poiMapMarker.position = [center.lng, center.lat];
      }
    },
    goCompanyHome(){
      window.open(`${process.env.VUE_APP_WEB_HOST}/company/${this.company.id}`)
    }
  }
};
</script>

<style lang="scss">
.edit-company-container {
  padding: 12px;
  width: 1000px;
  $border-style: 1px solid #eee;
  
  .company-body {
    background: #fff;
    padding: 20px;
    border: $border-style;
    font-size: 15px;
  }

  .box-margin {
    margin-top: 40px;
  }

  .map-box {
    height: 200px;
  }

  .map-search-box {
    position: absolute;
    top: 70px;
    left: 20px;
  }

  .preview-box {
    background: #fff;
    padding: 20px;
    border: $border-style;
  }

  .ql-container .ql-editor {
    min-height: 200px;
    font-size: 15px;
  }
  .ql-editor p {
    margin-bottom: 10px;
  }
  .ql-editor ol,
  .ql-editor ul {
    padding-left: 0.5em;
    margin-bottom: 10px;
  }
  .ql-bubble {
    border: 1px solid #dcdfe6;
    border-radius: 4px;
  }
  .b-toaster {
    z-index: 10000;
  }

  a.edit-text {
    cursor: pointer;
    font-size: 14px;
  }

  .ql-editor.ql-blank::before {
    font-style: normal;
    color: #c0c4cc;
    font-size: 15px;
  }

  .avatar-uploader .el-upload {
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }

  .avatar-uploader .el-upload:hover {
    border-color: #ccc;
  }

  $avatarSize: 100px;

  .avatar-uploader .avatar-uploader-icon {
    border: 1px dashed #d9d9d9;
    font-size: 28px;
    color: #8c939d;
    width: $avatarSize;
    height: $avatarSize;
    line-height: $avatarSize;
    text-align: center;
  }

  .avatar-uploader .avatar {
    width: $avatarSize;
    height: $avatarSize;
    display: block;
  }
}
</style>