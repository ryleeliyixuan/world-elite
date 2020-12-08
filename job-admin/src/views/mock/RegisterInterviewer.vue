<template>
  <div class="app-container">
    <h3>面试官信息</h3>
    <div style="padding-left: 20px; padding-top: 5px; width: 40%">
      <el-form
        :model="interviewInfoForm"
        :rules="interviewInfoFormRules"
        ref="interviewInfoForm"
        label-width="125px"
        label-position="left"
      >
        <el-form-item label="*用户ID" prop="interviewerId">
          <el-input v-model="interviewInfoForm.interviewerId" placeholder="请填写用户ID" maxlength="30"
                    show-word-limit></el-input>
        </el-form-item>
        <el-form-item label="*昵称" prop="nickName">
          <el-input v-model="interviewInfoForm.nickName" placeholder="请填写用户昵称" maxlength="10"
                    show-word-limit></el-input>
        </el-form-item>
        <el-form-item label="头像：" prop="avatar">
          <div>
            <el-image v-for="item in avatarList"
                      :key="item.id"
                      :src="item.avatarUrl"
                      @click="interviewInfoForm.avatar= item.avatarUrl"
                      style="width: 100px; height: 100px; margin-right: 13px; cursor: pointer;"/>
          </div>
          <el-upload class="avatar-uploader"
                     :action="uploadPicOptions.action"
                     :data="uploadPicOptions.params"
                     :accept="uploadPicOptions.acceptFileType"
                     :show-file-list="false"
                     :on-success="handleUploadSuccess"
                     :before-upload="beforeUpload">
            <el-image v-if="interviewInfoForm.avatar"
                      :src="interviewInfoForm.avatar"
                      v-loading="loading"
                      class="avatar"/>
            <el-image v-else class="avatar-uploader-icon" :src="require('@/assets/img-upload.png')"></el-image>
            <div slot="tip" class="avatar-uploader-tip">建议大小500*500</div>
          </el-upload>
        </el-form-item>
        <el-form-item label="从事行业：" prop="industryId">
          <el-select v-model="interviewInfoForm.industryId"
                     clearable
                     placeholder="行业"
                     class="section1-select">
            <el-option v-for="item in industryOptions" :key="item.industryId" :label="item.name"
                       :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="所在公司：" prop="companyName">
          <el-input style="width: 300px" v-model="interviewInfoForm.companyName"
                    placeholder="请输入您的公司名称"></el-input>
        </el-form-item>
        <el-form-item label="从业时间：" prop="experienceTimeId">
          <el-select v-model="interviewInfoForm.experienceTimeId" clearable placeholder="请选择您的工作经验年限"
                     class="section1-select">
            <el-option v-for="item in experienceTimeOptions" :key="item.experienceTimeId"
                       :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="从业经历：" prop="experience.experienceItem">
          <div class="textarea-container">
            <div class="textarea-item" v-for="(item,index) in interviewInfoForm.experience">
              <span class="experience-title">{{index+1}}.</span>
              <el-input class="experience" type="textarea" v-model="item.experienceItem "
                        placeholder="字数不超过150字" resize="none" :autosize="{minRows: 2,maxRows: 10}"></el-input>
            </div>
            <div class="add-experience-button" @click="addExperience">添加经历</div>
          </div>
        </el-form-item>
        <el-form-item label="面试官自述：" prop="description">
          <el-input type="textarea" style="width: 360px" v-model="interviewInfoForm.description"
                    placeholder="字数不超过150字" resize="none" :autosize="{minRows: 8,maxRows: 10}"></el-input>
        </el-form-item>
        <el-form-item label="个人标签：" prop="companyName">
          <el-input style="width: 300px" v-model="interviewInfoForm.tag"
                    placeholder="请输入您的个人标签"></el-input>
        </el-form-item>
        <el-form-item label="一句话介绍：" prop="companyName">
          <el-input style="width: 300px" v-model="interviewInfoForm.introduction"
                    placeholder="请输入您的一句话介绍"></el-input>
        </el-form-item>
        <el-form-item label="可提供面试内容：" prop="companyName">
          <el-checkbox v-model="checked1">HR面试（通用）</el-checkbox>
          <el-checkbox v-model="checked2">专业技术（测试）</el-checkbox>
          <el-checkbox v-model="checked3">行业经验</el-checkbox>
        </el-form-item>
      </el-form>
    </div>

    <h3 style="padding-top: 30px">身份认证信息</h3>
    <div style="padding-left: 20px; padding-top: 5px; width: 40%">
      <el-form
        :model="idCardAuthForm"
        :rules="idCardAuthFormRules"
        ref="idCardAuthForm"
        label-width="125px"
        label-position="left"
      >

        <el-form-item label="您的姓名：" prop="name">
          <el-input style="width: 360px" v-model="idCardAuthForm.name" placeholder="请输入您的真实姓名"></el-input>
        </el-form-item>
        <el-form-item label="身份证号：" prop="idNumber">
          <el-input style="width: 360px" v-model="idCardAuthForm.idNumber" placeholder="请输入您的身份证号"></el-input>
        </el-form-item>

        <el-form-item label="身份证照片：" prop="identity">
          <div style="display: flex;">
            <div class="card-container">
              <el-upload class="card-uploader"
                         :action="uploadPicOptions.action"
                         :data="uploadPicOptions.params"
                         :accept="uploadPicOptions.acceptFileType"
                         :show-file-list="false"
                         :on-success="handleUploadSuccess"
                         :before-upload="beforeUpload">
                <el-image v-if="idCardAuthForm.faceUrl"
                          :src="idCardAuthForm.faceUrl"
                          v-loading="loading"
                          fit="scale-down"
                          class="card"/>
                <div v-else class="card-face">
                  <el-image :src="require('@/assets/add.png')" class="card-add"/>
                </div>
              </el-upload>
              <div>身份证人面像</div>
            </div>
            <div class="card-container">
              <el-upload class="card-uploader"
                         :action="uploadPicOptions.action"
                         :data="uploadPicOptions.params"
                         :accept="uploadPicOptions.acceptFileType"
                         :show-file-list="false"
                         :on-success="handleUploadSuccess"
                         :before-upload="beforeUpload">
                <el-image v-if="idCardAuthForm.emblemUrl"
                          :src="idCardAuthForm.emblemUrl"
                          v-loading="loading"
                          fit="scale-down"
                          class="card"/>
                <div v-else class="card-emblem">
                  <el-image :src="require('@/assets/add.png')" class="card-add"/>
                </div>
              </el-upload>
              <div>身份证国徽面</div>
            </div>
            <div class="card-container">
              <el-upload class="card-uploader"
                         :action="uploadPicOptions.action"
                         :data="uploadPicOptions.params"
                         :accept="uploadPicOptions.acceptFileType"
                         :show-file-list="false"
                         :on-success="handleUploadSuccess"
                         :before-upload="beforeUpload">
                <el-image v-if="idCardAuthForm.holdUrl"
                          :src="idCardAuthForm.holdUrl"
                          v-loading="loading"
                          fit="scale-down"
                          class="card"/>
                <div v-else class="card-hold">
                  <el-image :src="require('@/assets/add.png')" class="card-add"/>
                </div>
              </el-upload>
              <div>手持身份证正面(确保身份证清晰)</div>
            </div>
          </div>
        </el-form-item>
      </el-form>
    </div>

    <div align="center" style="padding: 30px">
      <el-button type="primary" @click="" icon="el-icon-plus">创建面试官</el-button>
      <el-button type="warning" @click="" icon="el-icon-plus">保存后继续创建</el-button>
      <el-button @click="" icon="el-icon-refresh-left">重置</el-button>
    </div>

  </div>
</template>

<script>
  import {listByType} from "@/api/dict_api";
  import {getAvatar} from "@/api/mock_api";
  import {getUploadPicToken} from "@/api/upload_api";
  import {checkPicSize} from "@/utils/common";

  export default {
    name: "registerInterviewer",
    data() {
      return {
        interviewInfoForm: {
          // interviewerId: '',
          // nickName: '',
          // avatar: '',
          // industryId: '',
          // experienceTimeId: '',
          // companyName: '',
          // description: '',
          // experience: [{experienceItem: ''}],
          //
          // direction: 'HR面试（通用）',
          // direction_description: '',
          // price: ''
        },
        interviewInfoFormRules: {
          // interviewerId: [{required: true, message: '请输入用户ID', trigger: 'blur'}, {max: 20, message: 'ID不超过20字符'}],
          // nickName: [{required: true, message: '请输入用户昵称', trigger: 'blur'}, {max: 20, message: '昵称不超过20字符'}],
          // identity: [{validator: identity, trigger: 'blur'}],
          // avatar: [{required: true, message: "请上传头像", trigger: "change"}],
          // industryId: [{required: true, message: "请选择行业", trigger: "change"}],
          // experienceTimeId: [{required: true, message: "请选择从业时间", trigger: "change"}],
          // name: [{required: true, message: '请输入您的姓名', trigger: 'blur'}],
          // idNumber: [{required: true, trigger: 'blur', validator: isIdNumber}],
          // companyName: [{required: true, message: '请输入您的公司名称', trigger: 'blur'}],
          // description: [{required: true, message: '请输入面试官自述', trigger: 'blur'}, {max: 150, message: '长度不超过150个字'}],
          // experience: [{required: true, message: '请输入从业经历', trigger: 'blur'}, {max: 150, message: '长度不超过150个字'}],
          // price: [{required: true, message: '请输入您理想资询价位', trigger: 'blur'}, {validator: isPrice, trigger: 'blur'}]
        },
        idCardAuthForm: {},
        idCardAuthFormRules: {},
        avatarList: [],
        industryOptions: [],
        experienceTimeOptions: [],
        uploadPicOptions: {
          action: "",
          params: {},
          fileUrl: "",
          acceptFileType: ".jpg,.jpeg,.png,.JPG,.JPEG,.PNG",
        },
      };
    },
    created() {
      this.initData();
    },
    methods: {
      initData() {
        listByType(18).then(
          response => (this.industryOptions = response.data.list)
        );

        listByType(13).then(
          response => (this.experienceTimeOptions = response.data.list)
        );

        getAvatar().then(
          response => (this.avatarList = response.data)
        );
      },
      beforeUpload(file) {
        return new Promise((resolve, reject) => {
          if (checkPicSize(file)) {
            reject();
          } else {
            this.loading = true;
            this.formOne.avatar = URL.createObjectURL(file);
            getUploadPicToken(file.name).then((response) => {
              const {data} = response;
              this.uploadPicOptions.action = data.host;
              this.uploadPicOptions.params = data;
              this.uploadPicOptions.fileUrl = data.host + "/" + data.key;
              resolve(data);
            }).catch((error) => {
              reject(error);
            });
          }
        });
      },
      handleUploadSuccess() {
        this.loading = false;
      },
      addExperience() {
        this.formOne.experience.push({experienceItem: ''})
      },
    }
  }
</script>
<style scoped lang="scss">
  .card-container {
    display: flex;
    align-items: center;
    flex-direction: column;
    margin-right: 30px;

    .card-uploader {
      display: flex;
      align-items: center;
      justify-content: center;
      width: 229px;
      height: 154px;
      background: #FFFFFF;
      border-radius: 7px;
      border: 1px solid #CCCCCC;

      .el-upload {
        width: 229px;
        height: 154px;
      }

      .card {
        width: 229px;
        height: 154px;
        display: flex;
      }

      .card-face {
        background-image: url("../../assets/idcard1.png");
        width: 161px;
        height: 111px;
        display: flex;
        align-items: center;
        justify-content: center;
      }

      .card-emblem {
        background-image: url("../../assets/idcard2.png");
        width: 161px;
        height: 111px;
        display: flex;
        align-items: center;
        justify-content: center;
      }

      .card-hold {
        background-image: url("../../assets/idcard3.png");
        width: 121px;
        height: 121px;
        display: flex;
        align-items: center;
        justify-content: center;
      }

      .card-add {
        width: 45px;
        height: 45px;
      }
    }

  }
</style>
