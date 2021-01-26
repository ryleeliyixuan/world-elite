<template>
  <div class="app-container">
    <h3 class="mb-4">轮播图设置</h3>
    <el-form :model="webHomeForm" label-width="100px" class="demo-ruleForm">
      <el-form-item label="主标题">
        <el-input v-model="webHomeForm.mainTitle"></el-input>
      </el-form-item>
      <el-form-item label="附标题">
        <el-input v-model="webHomeForm.subTitle"></el-input>
      </el-form-item>
      <el-form-item label="背景图片">
        <el-input v-model="webHomeForm.picUrl"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="saveWebHomeConfig">保存</el-button>
      </el-form-item>
    </el-form>
    <el-divider></el-divider>
    <h3 class="mb-4 mt-4">推荐板块设置</h3>
    <el-form :model="companyHomeForm" label-width="150px" class="demo-ruleForm">
      <el-form-item label="板块1（上方）：">
        <el-button type="success">上传图片</el-button>
        <!-- <el-upload
          class="thumbnail-uploader"
          :action="uploadPicOptions.action"
          :data="uploadPicOptions.params"
          :accept="uploadPicOptions.acceptFileType"
          :show-file-list="false"
          :on-success="handleUploadSuccess"
          :before-upload="beforeUpload"
        >
          <el-image
            v-if="thumbnail || activityForm.thumbnail"
            :src="thumbnail || activityForm.thumbnail"
            v-loading="uploadPicOptions.loading"
            class="thumbnail"
          />
          <i v-else class="el-icon-plus thumbnail-uploader-icon"></i>
        </el-upload> -->
        <el-input v-model="companyHomeForm.mainTitle"></el-input>
      </el-form-item>
      <el-form-item label="板块2（下方）：">
        <el-button type="success">上传图片</el-button>
        <el-input v-model="companyHomeForm.subTitle"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="saveCompanyHomeConfig"
          >保存</el-button
        >
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { saveWebHomeConfig, getWebHomeConfig } from "@/api/config_api";
import Toast from "@/utils/toast";

export default {
  name: "ConfigPicPage",
  data() {
    return {
      webHomeForm: {
        configType: 8,
        mainTitle: undefined,
        subTitle: undefined,
        picUrl: undefined,
      },
      companyHomeForm: {
        configType: 9,
        mainTitle: undefined,
        subTitle: undefined,
        picUrl: undefined,
      },
      uploadPicOptions: {
        action: "",
        params: {},
        fileUrl: "",
        acceptFileType: ".jpg,.jpeg,.png,.JPG,.JPEG,.PNG",
        loading: false,
      },
    };
  },
  created() {
    this.initData();
  },
  methods: {
    initData() {
      getWebHomeConfig(8).then((response) => {
        if (response.data) {
          this.webHomeForm = response.data;
        }
      });
    },
    saveWebHomeConfig() {
      saveHomeConfig(this.webHomeForm).then(() => {
        Toast.success("保存成功");
      });
    },
    saveCompanyHomeConfig() {
      saveHomeConfig(this.companyHomeForm).then(() => {
        Toast.success("保存成功");
      });
    },
    beforeUpload(file) {
      document.body.style.overflow = "auto";
      // document.querySelector("body").setAttribute("style", "overflow: auto !important;")
      return new Promise((resolve, reject) => {
        this.uploadPicOptions.loading = true;
        getUploadPicToken(file.name)
          .then((response) => {
            const { data } = response;
            console.log(data);
            this.uploadPicOptions.action = data.host;
            this.uploadPicOptions.params = data;
            this.uploadPicOptions.fileUrl = data.host + "/" + data.key;
            this.activityForm.thumbnail = data.host + "/" + data.key;
            this.thumbnail = URL.createObjectURL(file);
            resolve(data);
          })
          .catch((error) => {
            this.uploadPicOptions.loading = false;
            reject(error);
          });
      });
    },
    handleUploadSuccess() {
      console.log(document.querySelector("body"));
      this.uploadPicOptions.loading = false;
    },
  },
};
</script>
