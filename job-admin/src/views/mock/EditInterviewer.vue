<template>
  <div class="app-container">
    <h3>面试官信息</h3>
    <div style="padding-left: 20px; padding-top: 5px; width: 40%;">
      <el-form
        :model="interviewInfoForm"
        :rules="rules"
        ref="interviewInfoForm"
        label-width="125px"
        label-position="left"
        style="width: 100%">

        <el-form-item label="用户ID" prop="id">
          <el-input v-model="interviewInfoForm.id" placeholder="请填写用户ID" maxlength="30" disabled
                    show-word-limit></el-input>
        </el-form-item>
        <el-form-item label="昵称" prop="nickName">
          <el-input v-model="interviewInfoForm.nickName" placeholder="请填写用户昵称" maxlength="10"
                    show-word-limit></el-input>
        </el-form-item>
        <el-form-item label="头像：" prop="avatar">
          <div>
            <el-image v-for="item in avatarList"
                      :key="item.id"
                      :src="item.avatarUrl"
                      @click="onDefaultAvatar(item)"
                      style="width: 100px; height: 100px; margin-right: 13px; cursor: pointer;"/>
          </div>
          <el-upload class="avatar-uploader"
                     :action="uploadPicOptions.action"
                     :data="uploadPicOptions.params"
                     :accept="uploadPicOptions.acceptFileType"
                     :show-file-list="false"
                     :on-success="handleUploadSuccessAvatar"
                     :before-upload="beforeUploadAvatar">
            <el-image v-if="localAvatar || interviewInfoForm.avatar"
                      :src="localAvatar || interviewInfoForm.avatar"
                      v-loading="loadingAvatar"
                      class="avatar"/>
            <el-image v-else class="avatar-uploader-icon"
                      :src="require('@/assets/img-upload.png')"></el-image>
            <div slot="tip" class="avatar-uploader-tip">建议大小500*500</div>
          </el-upload>
        </el-form-item>
        <el-form-item label="从事行业：" prop="industryId">
          <el-select v-model="interviewInfoForm.industryId" clearable placeholder="行业" style="width: 100%;">
            <el-option v-for="item in industryOptions" :key="item.industryId" :label="item.name"
                       :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="所在公司：" prop="companyName">
          <el-input v-model="interviewInfoForm.companyName"
                    placeholder="请输入您的公司名称"></el-input>
        </el-form-item>
        <div>
          <el-form-item label="所在职位：" prop="position">
            <el-input v-model="interviewInfoForm.position"
                      placeholder="请输入您的职位名称"></el-input>
          </el-form-item>
        </div>
        <el-form-item label="从业时间：" prop="experienceTimeId">
          <el-select v-model="interviewInfoForm.experienceTimeId" clearable placeholder="请选择您的工作经验年限"
                     style="width: 100%;">
            <el-option v-for="item in experienceTimeOptions" :key="item.experienceTimeId"
                       :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="从业经历：" prop="experienceIm">
          <div>
            <div v-for="(item,index) in interviewInfoForm.experience">
              <span>{{index+1}}.</span>
              <el-input v-model="item.experienceItem "
                        type="textarea"
                        placeholder="字数不超过150字"
                        style="margin-top: 5px; "
                        :autosize="{minRows: 1,maxRows: 10}">
              </el-input>
            </div>
            <el-button type="success" @click="addExperience" style="margin-top: 10px;">添加经历</el-button>
          </div>
        </el-form-item>
        <el-form-item label="面试官自述：" prop="description">
          <el-input type="textarea" v-model="interviewInfoForm.description"
                    placeholder="字数不超过150字" :autosize="{minRows: 8,maxRows: 10}"></el-input>
        </el-form-item>
        <el-form-item label="一句话介绍：" prop="introduction">
          <el-input v-model="interviewInfoForm.introduction"
                    placeholder="请输入您的一句话介绍"></el-input>
        </el-form-item>
      </el-form>
    </div>

    <h3 style="padding-top: 30px">面试信息</h3>
    <div style="padding-left: 20px; padding-top: 5px; width: 40%">
      <el-form ref="interviewDirection"
               :model="interviewDirection"
               :rules="rules"
               label-width="125px"
               label-position="left">
        <div>
          <div v-for="(item, index) in interviewDirectionArray">
            <el-divider>{{item.direction}}</el-divider>
            <el-form-item label="可供面试内容：" prop="direction">
              <el-input v-model="item.direction" placeholder="请输入可提供面试内容"></el-input>
            </el-form-item>
            <el-form-item label="面试咨询价格：" prop="price">
              <el-input v-model="item.price" placeholder="请输入您理想资询价位(元/半小时)"></el-input>
            </el-form-item>
            <el-form-item label="面试内容简介：" prop="description">
              <el-input type="textarea" v-model="item.description"
                        placeholder="请对面试的内容进行具体描述，150字以内"
                        :autosize="{minRows: 8,maxRows: 10}"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="danger" icon="el-icon-delete" size="small" @click="delDirection(index)"
                         style="margin-top: 10px;margin-bottom: 30px;">删除 {{item.direction}}
              </el-button>
            </el-form-item>
          </div>
          <el-button type="success" @click="addDirection" style="margin-top: 10px;">添加面试方向信息</el-button>
        </div>
      </el-form>
    </div>

    <h3 style="padding-top: 30px">身份认证信息</h3>
    <div style="padding-left: 20px; padding-top: 5px; width: 40%">
      <el-form
        :model="idCardAuthForm"
        :rules="rules"
        ref="idCardAuthForm"
        label-width="125px"
        label-position="left"
      >

        <el-form-item label="您的姓名：" prop="name">
          <el-input v-model="idCardAuthForm.name" placeholder="请输入您的真实姓名"></el-input>
        </el-form-item>
        <el-form-item label="身份证号：" prop="idNumber">
          <el-input v-model="idCardAuthForm.idNumber" placeholder="请输入您的身份证号"></el-input>
        </el-form-item>

        <el-form-item label="身份证照片：" prop="identity">
          <div style="display: flex;">
            <div class="card-container">
              <el-upload class="card-uploader"
                         :action="uploadPicOptions.action"
                         :data="uploadPicOptions.params"
                         :accept="uploadPicOptions.acceptFileType"
                         :show-file-list="false"
                         :on-success="handleUploadSuccessFace"
                         :before-upload="beforeUploadFacePic">
                <el-image v-if="idCardAuthForm.faceUrl"
                          :src="idCardAuthForm.faceUrl"
                          v-loading="loadingFace"
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
                         :on-success="handleUploadSuccessEmblem"
                         :before-upload="beforeUploadEmblemPic">
                <el-image v-if="idCardAuthForm.emblemUrl"
                          :src="idCardAuthForm.emblemUrl"
                          v-loading="loadingEmblem"
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
                         :on-success="handleUploadSuccessHold"
                         :before-upload="beforeUploadHoldPic">
                <el-image v-if="idCardAuthForm.holdUrl"
                          :src="idCardAuthForm.holdUrl"
                          v-loading="loadingHold"
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
      <el-button type="primary" @click="updateInterviewer" icon="el-icon-plus">更新面试官信息</el-button>
      <el-button type="warning" @click="updateAndContinue" icon="el-icon-plus">保存后继续创建</el-button>
      <el-button @click="resetForm" icon="el-icon-refresh-left">重置</el-button>
    </div>

  </div>
</template>

<script>
  import {listByType} from "@/api/dict_api";
  import {
    addInterviewAuth,
    delInterviewerInfo,
    getAvatar,
    getInterviewAuth,
    getInterviewDirection,
    getInterviewerInfo,
    updateInterviewAuth
  } from "@/api/mock_api";
  import {getUploadPicToken} from "@/api/upload_api";
  import {checkPicSize} from "@/utils/common";
  import Toast from "@/utils/toast";

  export default {
    name: "editInterviewer",
    data() {
      let isIdNumber = (rule, value, callback) => {
        if (!value) {
          callback(new Error("请输入身份证号"));
        } else {
          const reg = /^\d{6}(18|19|20)?\d{2}(0[1-9]|1[0-2])(([0-2][1-9])|10|20|30|31)\d{3}(\d|X|x)$/;
          const card = reg.test(value);
          if (!card) {
            callback(new Error("身份证格式如:423024xxxx0216xxxx"));
          } else {
            callback();
          }
        }
      };
      let isPrice = (rule, value, callback) => {
        if (!value) {
          callback();
        } else {
          let reg = /^-?\d{1,4}(?:\.\d{1,2})?$/;
          if (reg.test(value)) {
            callback();
          } else {
            callback(new Error("数字格式:0-9999或小数点后可加1到2位"));//如:1 或1.8 或1.85
          }
        }
      };
      let experienceIm = (rule, value, callback) => {
        if (!this.interviewInfoForm.experience) {
          callback("请输入从业经历")
        } else {
          callback()
        }
      }
      let identity = (rule, value, callback) => {
        if (!this.idCardAuthForm.faceUrl && !this.idCardAuthForm.emblemUrl && !this.idCardAuthForm.holdUrl) {
          callback("请按要求上传三张身份证照片");
        } else if (!this.idCardAuthForm.faceUrl) {
          callback("请上传身份证人面像");
        } else if (!this.idCardAuthForm.emblemUrl) {
          callback("请上传身份证国徽面");
        } else if (!this.idCardAuthForm.holdUrl) {
          callback("请上传手持身份证正面照");
        } else {
          callback();
        }
      }
      return {
        interviewInfoForm: {
          id: '',
          nickName: '',
          avatar: '',
          industryId: '',
          experienceTimeId: '',
          companyName: '',
          position: '',
          description: '',
          introduction: '',
          experience: [],
        },
        interviewDirectionArray: [],
        interviewDirection: {
          id: '',
          direction: '',
          description: '',
          interviewerId: '',
          price: '',
          logo: ''
        },

        //身份证图片上传form
        idCardAuthForm: {
          id: '',
          name: '',
          idNumber: '',
          faceUrl: '',
          emblemUrl: '',
          holdUrl: ''
        },
        rules: {
          id: [{required: true, message: '请输入用户ID', trigger: 'blur'}, {max: 20, message: 'ID不超过20字符'}],
          nickName: [{required: true, message: '请输入用户昵称', trigger: 'blur'}, {max: 20, message: '昵称不超过20字符'}],
          identity: [{validator: identity, trigger: 'blur'}],
          avatar: [{required: true, message: "请上传头像", trigger: "change"}],
          industryId: [{required: true, message: "请选择行业", trigger: "change"}],
          experienceTimeId: [{required: true, message: "请选择从业时间", trigger: "change"}],
          name: [{required: true, message: '请输入您的姓名', trigger: 'blur'}],
          idNumber: [{required: true, trigger: 'blur', validator: isIdNumber}],
          companyName: [{required: true, message: '请输入您的公司名称', trigger: 'blur'}],
          description: [{required: true, message: '请输入面试官自述', trigger: 'blur'}, {max: 150, message: '长度不超过150个字'}],
          introduction: [{required: true, message: '请输入一句话介绍', trigger: 'blur'}, {max: 150, message: '长度不超过150个字'}],
          experienceIm: [{required: true, validator: experienceIm, trigger: 'blur'}],
          position: [{required: true, message: '请输入您的职位名称', trigger: 'blur'}, {max: 15, message: '职位不超过15字符'}],
          price: [{required: true, message: '请输入您理想资询价位', trigger: 'blur'}, {validator: isPrice, trigger: 'blur'}],
          direction: [{required: true, message: '请输入面试方向', trigger: 'blur'}, {max: 15, message: '职位不超过15字符'}]
        },


        localAvatar: undefined,
        loading: false,
        loadingAvatar: false,
        loadingFace: false,
        loadingEmblem: false,
        loadingHold: false,

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

    mounted() {
      if (this.$route.query.interviewerId) {
        this.interviewInfoForm.id = this.$route.query.interviewerId;
      } else {
        this.$message.warning("面试官ID不存在");
      }
    },

    created() {
      this.initData();
    },
    methods: {
      initData() {
        listByType(18).then(
          response => (this.industryOptions = response.data.list)
        );

        listByType(21).then(
          response => (this.experienceTimeOptions = response.data.list)
        );

        getAvatar().then(
          response => (this.avatarList = response.data)
        );

        getInterviewerInfo(this.$route.query.interviewerId).then(data => {
          const interviewer = data.data;

          const experienceList = interviewer.experience;
          if (experienceList) {
            for (let i = 0; i < experienceList.length; i++) {
              this.interviewInfoForm.experience.push({experienceItem: experienceList[i]});
            }
          }

          if (interviewer.experienceTime) {
            this.interviewInfoForm.experienceTimeId = interviewer.experienceTime.id;
          }

          if (interviewer.industry) {
            this.interviewInfoForm.industryId = interviewer.industry.id;
          }

          this.interviewInfoForm.companyName = interviewer.companyName;
          this.interviewInfoForm.nickName = interviewer.nickName;
          this.interviewInfoForm.position = interviewer.position;
          this.interviewInfoForm.description = interviewer.description;
          this.interviewInfoForm.introduction = interviewer.introduction;
          this.interviewInfoForm.avatar = interviewer.avatar;
        })

        getInterviewDirection(this.$route.query.interviewerId).then(data => {
          const directionList = data.data;
          if (directionList) {
            for (let i = 0; i < directionList.length; i++) {
              this.interviewDirectionArray.push({
                id: directionList[i].id,
                direction: directionList[i].direction,
                description: directionList[i].description,
                interviewerId: directionList[i].interviewerId,
                price: directionList[i].price,
                logo: directionList[i].logo
              });
            }
          }
        })

        getInterviewAuth(this.$route.query.interviewerId).then(data => {
          const auth = data.data;
          if (auth) {
            this.idCardAuthForm.id = auth.id;
            this.idCardAuthForm.name = auth.name;
            this.idCardAuthForm.idNumber = auth.idNumber;
            this.idCardAuthForm.faceUrl = auth.faceUrl;
            this.idCardAuthForm.emblemUrl = auth.emblemUrl;
            this.idCardAuthForm.holdUrl = auth.holdUrl;
          }
        })
      },

      beforeUploadAvatar(file) {
        return new Promise((resolve, reject) => {
          if (checkPicSize(file)) {
            reject();
          } else {
            this.loadingAvatar = true;
            this.localAvatar = URL.createObjectURL(file);
            getUploadPicToken(file.name).then((response) => {
              const {data} = response;
              this.uploadPicOptions.action = data.host;
              this.uploadPicOptions.params = data;
              this.uploadPicOptions.fileUrl = data.host + "/" + data.key;
              this.interviewInfoForm.avatar = this.uploadPicOptions.fileUrl;
              this.$refs["interviewInfoForm"].validateField("avatar");
              resolve(data);
            }).catch((error) => {
              reject(error);
            });
          }
        });
      },
      beforeUploadFacePic(file) {
        return new Promise((resolve, reject) => {
          if (checkPicSize(file)) {
            reject();
          } else {
            this.loadingFace = true;
            getUploadPicToken(file.name).then((response) => {
              const {data} = response;
              this.uploadPicOptions.action = data.host;
              this.uploadPicOptions.params = data;
              this.uploadPicOptions.fileUrl = data.host + "/" + data.key;
              this.idCardAuthForm.faceUrl = this.uploadPicOptions.fileUrl;
              resolve(data);
            }).catch((error) => {
              reject(error);
            });
          }
        });
      },
      beforeUploadEmblemPic(file) {
        return new Promise((resolve, reject) => {
          if (checkPicSize(file)) {
            reject();
          } else {
            this.loadingEmblem = true;
            getUploadPicToken(file.name).then((response) => {
              const {data} = response;
              this.uploadPicOptions.action = data.host;
              this.uploadPicOptions.params = data;
              this.uploadPicOptions.fileUrl = data.host + "/" + data.key;
              this.idCardAuthForm.emblemUrl = this.uploadPicOptions.fileUrl;
              resolve(data);
            }).catch((error) => {
              reject(error);
            });
          }
        });
      },
      beforeUploadHoldPic(file) {
        return new Promise((resolve, reject) => {
          if (checkPicSize(file)) {
            reject();
          } else {
            this.loadingHold = true;
            getUploadPicToken(file.name).then((response) => {
              const {data} = response;
              this.uploadPicOptions.action = data.host;
              this.uploadPicOptions.params = data;
              this.uploadPicOptions.fileUrl = data.host + "/" + data.key;
              this.idCardAuthForm.holdUrl = this.uploadPicOptions.fileUrl;
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
      handleUploadSuccessAvatar() {
        this.loadingAvatar = false;
      },
      handleUploadSuccessFace() {
        this.loadingFace = false;
      },
      handleUploadSuccessEmblem() {
        this.loadingEmblem = false;
      },
      handleUploadSuccessHold() {
        this.loadingHold = false;
      },
      addExperience() {
        this.interviewInfoForm.experience.push({experienceItem: ''})
      },
      addDirection() {
        this.interviewDirectionArray.push({
          direction: '',
          description: '',
          interviewerId: '',
          price: '',
        });
      },
      delDirection(index) {
        if (this.interviewDirectionArray[index] && this.interviewDirectionArray[index].id) {
          this.$confirm("此操作将删除此面试方向, 是否继续?", "提示", {
            confirmButtonText: "继续",
            cancelButtonText: "取消",
            type: "warning"
          }).then(() => {
            delInterviewerInfo(this.interviewDirectionArray[index].id).then(response => {
              this.$message.success("删除面试方向成功");
            });
          });
        } else {
          Array.prototype.remove = function (from, to) {
            const rest = this.slice((to || from) + 1 || this.length);
            this.length = from < 0 ? this.length + from : from;
            return this.push.apply(this, rest);
          };

          this.interviewDirectionArray.remove(index);
        }
      },
      onDefaultAvatar(item) {
        this.interviewInfoForm.avatar = item.avatarUrl;
        this.$refs["interviewInfoForm"].validateField("avatar");
        //选择头像后 滚动条会消失....
        document.querySelector("body").setAttribute("style", "overflow: auto !important;")
      },

      validate(filedName) {
        return new Promise((resolve) => {
          this.$refs[filedName].validate(valid => {
            resolve(valid)
          });
        })
      },

      updateInterviewer() {
        //提交面试官基础信息
        let result = true;
        this.$refs["interviewInfoForm"].validate(valid => {
          if (valid) {
            let interviewInfoForm = {...this.interviewInfoForm};
            interviewInfoForm.experience = interviewInfoForm.experience.map(item => item.experienceItem)
            updateInterview(interviewInfoForm).then(() => {
              Toast.success("提交面试官基础信息成功");
            });
          } else {
            result = false;
            Toast.error("请检查页面错误输入");
          }
        });

        //提交面试方向信息
        let request = [];
        for (let i = 0; i < this.interviewDirectionArray.length; i++) {
          this.interviewDirection = this.interviewDirectionArray[i];
          this.interviewDirection.interviewerId = this.interviewInfoForm.id;

          if (this.interviewDirection.id)
            request.push(updateInterviewDirection(this.interviewDirection));
          else
            request.push(addInterviewDirection(this.interviewDirection));
        }

        Promise.all(request).then(() => {
          Toast.success("提交面试方向信息成功");
        })

        //提交身份证信息
        this.$refs["idCardAuthForm"].validate(valid => {
          if (valid) {
            if (this.idCardAuthForm.id) {
              updateInterviewAuth(this.idCardAuthForm).then(() => {
                  Toast.success("修改面试官身份证信息成功");
                }
              )
            } else {
              this.idCardAuthForm.id = this.$route.query.interviewerId;
              addInterviewAuth(this.idCardAuthForm).then(() => {
                  Toast.success("提交面试官身份证信息成功");
                }
              )
            }
          }
        })

        /*console.log(result);
        //添加后跳转回面试官列表页面. result仅校验数据输入是否正确
        if (result)
          this.$router.push("/mock/interviewer");*/
      },

      updateAndContinue() {
        this.updateInterviewer();
        this.$router.push("/mock/registerInterviewer");
      },
      resetForm() {
        location.reload();
      },
    }
  }
</script>
<style scoped lang="scss">
  .avatar-uploader {
    .avatar {
      width: 100px;
      height: 100px;
      display: block;
    }

    .avatar-uploader-icon {
      border: 1px solid #333333;
      border-radius: 50%;
      font-size: 28px;
      color: #8c939d;
      width: 100px;
      height: 100px;
      line-height: 100px;
      text-align: center;
      padding: 36px 31px;
    }

    .avatar-uploader-tip {
      color: #999999;
      line-height: 25px;
      margin-top: -3px;
    }
  }

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
