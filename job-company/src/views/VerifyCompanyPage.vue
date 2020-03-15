<template>
  <div class="app-container">
    <h4 class="text-center pt-4 pb-4">通过招聘者身份验证后，即可发布职位</h4>
    <p class="text-danger text-center p-4" v-if="employeeForm.status == 3">{{employeeForm.remark}}</p>
    <div class="mt-4 d-flex justify-content-center">
      <el-form
        ref="employeeForm"
        :model="employeeForm"
        :rules="employeeFormRules"
        label-width="130px"
        label-position="left"
        class="w-50"
      >
        <el-form-item label="真实姓名" prop="name">
          <el-input v-model="employeeForm.name" placeholder="请输入姓名" maxlength="15" show-word-limit></el-input>
        </el-form-item>
        <el-form-item label="身份证" prop="idCard">
          <el-input
            v-model="employeeForm.idCard"
            placeholder="请输入身份证号"
            maxlength="50"
            show-word-limit
          ></el-input>
        </el-form-item>
        <el-form-item label="手持身份证照片" prop="idCardPic">
          <el-upload
            :action="uploadPicOptions.action"
            :data="uploadPicOptions.params"
            :accept="uploadPicOptions.acceptFileType"
            :before-upload="beforeUploadPic"
            :on-success="handleUploadIdCardSuccess"
            :on-remove="handleRemoveIdCardPic"
            :limit="1"
            list-type="picture"
            :file-list="idCardPicList"
          >
            <el-button size="small" type="success">点击上传</el-button> 
            <el-link type="primary" class="ml-2">照片示例</el-link>
            <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过5Mb</div>
          </el-upload>
        </el-form-item>
        <el-form-item label="职位" prop="post">
          <el-input v-model="employeeForm.post" placeholder="请输入职位" maxlength="10" show-word-limit></el-input>
        </el-form-item>
        <el-form-item label="公司全称" prop="company">
          <el-autocomplete
            v-model="employeeForm.company"
            :fetch-suggestions="searchCompanyName"
            value-key="fullName"
            placeholder="请输入公司全称"
            class="w-100"
            :maxlength="100"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="营业执照照片" prop="companyLicensePic">
          <el-upload
            :action="uploadPicOptions.action"
            :data="uploadPicOptions.params"
            :accept="uploadPicOptions.acceptFileType"
            :before-upload="beforeUploadPic"
            :on-success="handleUploadLicenseSuccess"
            :on-remove="handleRemoveLicensePic"
            :limit="1"
            list-type="picture"
            :file-list="licensePicList"
          >
            <el-button size="small" type="success">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过5Mb</div>
          </el-upload>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit">提交审核</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import { getUploadPicToken } from "@/api/upload_api";
import { serachByName } from "@/api/company_api";
import { saveVerifyInfo, getVerifyInfo  } from "@/api/verify_api";

export default {
  data() {
    return {
      employeeForm: {
        name: undefined,
        idCard: undefined,
        idCardPic: undefined,
        company: undefined,
        post: undefined,
        companyLicensePic: undefined,
        status: undefined,
        remark: undefined,
      },
      employeeFormRules: {
        name: [{ required: true, message: "请输入你的名字", trigger: "blur" }],
        idCard: [
          { required: true, message: "请输入身份证号", trigger: "blur" }
        ],
        idCardPic: [
          { required: true, message: "请上传手持身份证照片", trigger: "blur" }
        ],
        company: [
          { required: true, message: "请输入公司名称", trigger: "blur" }
        ],
        post: [{ required: true, message: "请输入职位信息", trigger: "blur" }],
        companyLicensePic: [
          { required: true, message: "请上传公司营业执照", trigger: "blur" }
        ]
      },
      uploadPicOptions: {
        action: "",
        params: {},
        fileUrl: "",
        acceptFileType: ".jpg,.jpeg,.png,.JPG,.JPEG,.PNG"
      },
      tempUploadFile: {}
    };
  },
  created(){
    getVerifyInfo().then(response => {
      const { data } = response;
      this.employeeForm.name = data.name;
      this.employeeForm.idCard = data.idCard;
      this.employeeForm.idCardPic = data.idCardPic;
      this.employeeForm.company = data.company;
      this.employeeForm.post = data.post;
      this.employeeForm.companyLicensePic = data.companyLicensePic;
      this.employeeForm.status = data.status;
      this.employeeForm.remark = data.remark;
    });
  },
  computed: {
    idCardPicList: function(){
      if(this.employeeForm.idCardPic){
        return [{name: this.employeeForm.name, url: this.employeeForm.idCardPic}]
      }
      return [];
    },
    licensePicList: function(){
      if(this.employeeForm.companyLicensePic){
        return [{name: this.employeeForm.name, url: this.employeeForm.companyLicensePic}]
      }
      return [];
    },
  },
  methods: {
    beforeUploadPic(file) {
      return new Promise((resolve, reject) => {
        getUploadPicToken(file.name)
          .then(response => {
            const { data } = response;
            this.uploadPicOptions.action = data.host;
            this.uploadPicOptions.params = data;
            this.uploadPicOptions.fileUrl = data.host + "/" + data.key;
            this.tempUploadFile[file.name] = this.uploadPicOptions.fileUrl;
            resolve(data);
          })
          .catch(error => {
            reject(error);
          });
      });
    },
    handleUploadIdCardSuccess(response, file) {
      const picUrl = this.tempUploadFile[file.name];
      this.employeeForm.idCardPic = picUrl;
    },
    handleUploadLicenseSuccess(response, file) {
      const picUrl = this.tempUploadFile[file.name];
      this.employeeForm.companyLicensePic = picUrl;
    },
    handleRemoveIdCardPic(){
      this.employeeForm.idCardPic = undefined;
    },
    handleRemoveLicensePic(){
      this.employeeForm.companyLicensePic = undefined;
    },
    searchCompanyName(keyword, cb) {
      if (keyword.length < 2) {
        return;
      }
      serachByName(keyword).then(response => {
        cb(response.data.list);
      });
    },
    onSubmit() {
      this.$refs["employeeForm"].validate(valid => {
        if (valid) {
          saveVerifyInfo(this.employeeForm).then(() => {
            this.$alert("审核通过后我们会通过邮件通知您。", "提交成功", {
              confirmButtonText: "确定",
              callback: () => {
                this.$router.push('/');
              }
            });
          });
        }
      });
    }
  }
};
</script>

<style scoped>
.app-container {
  width: 800px;
  margin: 60px auto 0;
}
</style>
