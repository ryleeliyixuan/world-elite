<template>
  <div class="app-container">
    <div class="login-box">
      <div class="section1"></div>
      <div class="section2 text-center">
        <div class="section2-header">填写基本信息，开启World Elite之旅</div>
        <el-form
          :model="ruleForm"
          :rules="rules"
          ref="ruleForm"
          class="form"
          label-position="right"
        >
          <el-form-item style="align-items: center" label="昵称" prop="name">
            <el-input v-model="ruleForm.name"></el-input>
          </el-form-item>
          <el-form-item class="form-avatar" label="头像" prop="avatar">
            <div class="avatar-container">
              <div class="avatar" v-for="avatar in avatarList" :key="avatar.id">
                <div v-on:click="ruleForm.avatar = avatar.avatarUrl">
                  <el-avatar
                    :style="
                      ruleForm.avatar === avatar.avatarUrl
                        ? `border: 1px solid #4895ef`
                        : `border: 0px`
                    "
                    :src="avatar.avatarUrl"
                    :size="55"
                  ></el-avatar>
                </div>
              </div>
            </div>
            <!-- <el-avatar
              v-if="isDefaultAvatar == false"
              :style="
                ruleForm.avatar === userAvatarUrl
                  ? `border: 1px solid #4895ef`
                  : `border: 0px`
              "
              :src="userAvatarUrl"
              :size="55"
            ></el-avatar> -->
            <el-upload
              class="avatar-uploader"
              :action="uploadPicOptions.action"
              :data="uploadPicOptions.params"
              :accept="uploadPicOptions.acceptFileType"
              :show-file-list="false"
              :on-success="handleUploadSuccess"
              :before-upload="beforeUpload"
            >
              <el-avatar
                :style="
                  ruleForm.avatar === imageUrl
                    ? `border: 1px solid #4895ef`
                    : `border: 0px`
                "
                v-if="imageUrl"
                :src="imageUrl"
                :size="55"
                class="avatar"
              ></el-avatar>
              <el-avatar style="background: #ecf6fd" :size="55" v-else
                ><svg-icon
                  icon-class="register-avatar-upload"
                  style="width: 21px; height: 21px"
                ></svg-icon
              ></el-avatar>
            </el-upload>
            <div class="section2-text text-left">建议大小：500x500</div>
            <div class="section2-text text-left blue">
              此头像不用做简历头像，可使用虚拟头像哦！
            </div>
            <div></div>
          </el-form-item>
        </el-form>
        <el-button @click="modifyUser()" class="section2-next text-center"
          >下一步</el-button
        >
      </div>
    </div>
  </div>
</template>

<script>
import { defaultAvatar, modifyUser, getMyInfo } from "@/api/user_api";
import { getUploadPicToken } from "@/api/upload_api";
import Toast from "@/utils/toast";

export default {
  name: "RegisterBasicPage",

  data() {
    return {
      uploadPicOptions: {
        action: "",
        params: {},
        fileUrl: "",
        acceptFileType: ".jpg,.jpeg,.png,.JPG,.JPEG,.PNG",
        loading: false,
      },
      userAvatarUrl: "",
      isDefaultAvatar: false,
      imageUrl: "",
      avatarList: [],
      ruleForm: {
        name: "",
        avatar: "",
      },
      rules: {
        name: [{ required: true, message: "请输入您的昵称", trigger: "blur" }],
        avatar: [
          { required: true, message: "请选择您的头像", trigger: "blur" },
        ],
      },
    };
  },
  created() {
    this.initData();
    this.defaultAvatar();
    this.$emit("complete");
  },
  watch: {
    imageUrl(oldVal, newVal) {
      this.defaultAvatar();
    },
  },
  methods: {
    initData() {
      getMyInfo().then((response) => {
        this.ruleForm.avatar = response.data.avatar;
        this.userAvatarUrl = response.data.avatar;
        // console.log("-------", this.userAvatarUrl);
        this.ruleForm.name = response.data.name;
      });
    },
    defaultAvatar() {
      defaultAvatar().then((response) => {
        this.avatarList = response.data.list;
        for (var avatar in this.avatarList) {
          if (avatar.avatarUrl == this.userAvatarUrl) {
            this.isDefaultAvatar = true;
            return;
          }
        }
      });
    },
    modifyUser() {
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          modifyUser(this.ruleForm)
            .then(() => {
              Toast.success("成功设置昵称与头像");
            })
            .finally(() => {
              this.$router.push("/register-job-orientation");
            });
        }
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
            this.imageUrl = data.host + "/" + data.key;
            this.imageUrl = URL.createObjectURL(file);
            this.ruleForm.avatar = this.imageUrl;
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

<style scoped lang="scss">
.app-container {
  max-width: 1200px;
  .bold {
    font-size: 36px;
    font-family: Roboto-Medium, Roboto;
    font-weight: 500;
    color: #333333;
    line-height: 39px;
  }

  .login-box {
    height: 655px;
    box-shadow: 0px 4px 16px 3px rgba(191, 199, 215, 0.31);
    border-radius: 24px;
    display: flex;
    background: #f6f9fc;
    margin-top: 35px;
    margin-bottom: 88px;

    .section1 {
      background: url("../assets/login-background.svg") repeat;
      flex: 1;
    }
    .section2 {
      flex: 1;
      background: #ffffff;
      border-top-right-radius: 24px;
      border-bottom-right-radius: 24px;
      .section2-header {
        margin-top: 93px;
        margin-bottom: 40px;
        font-size: 24px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #333333;
        line-height: 33px;
      }

      .form {
        .form-avatar {
          /deep/.el-form-item__label {
            padding-top: 15px;
          }
        }
        /deep/ .el-form-item__label {
          font-size: 16px;
          font-family: PingFangSC-Medium, PingFang SC;
          font-weight: 500;
          color: #999999;
          line-height: 22px;
        }

        /deep/.el-form-item {
          display: flex;
          justify-content: center;
        }

        /deep/.el-input__inner {
          width: 339px;
          height: 35px;
          background: #ffffff;
          border-radius: 20px;
          border: 1px solid #4895ef;
        }

        /deep/.el-form-item__content {
          display: flex;
          flex-direction: column;
          width: 339px;
        }

        .avatar-container {
          display: flex;
          .avatar {
            margin-right: 16px;
          }
        }

        .avatar-uploader {
          display: flex;
        }

        .section2-text {
          font-size: 14px;
          font-family: PingFangSC-Regular, PingFang SC;
          font-weight: 400;
          color: #999999;
          line-height: 20px;
        }

        .blue {
          color: #4895ef;
        }
      }

      .section2-next {
        margin-top: 62px;
      }

      .el-button {
        width: 339px;
        height: 35px;
        background: #4895ef;
        border-radius: 18px;
        color: white;
        font-size: 16px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #ffffff;
        line-height: 22px;
        display: flex;
        align-content: center;
        align-items: center;
        justify-content: center;
        margin-right: auto;
        margin-left: auto;
      }
    }
  }
}

@media screen and (max-width: 850px) {
  .app-container {
    padding: 20px 20px 0;
    .login-box {
      .section1 {
        display: none;
      }

      .section2 {
        border-radius: 24px;
      }
    }
    
    .text-center {
      font-size: 18px;
    }
  }
}

@media screen and (max-width: 450px) {
  /deep/.el-form-item {
    flex-direction: column !important;
  }

  .app-container {
    padding: 20px 20px 0;

    .login-box {
      .section1 {
        display: none;
      }

      .section2 {
        border-radius: 24px;

        .form {
          .avatar-uploader {
            margin-right: 16px;
          }
          /deep/.el-input__inner {
            width: 200px;
            margin: auto;
          }

          /deep/.el-form-item__label {
            margin: auto;
          }
          /deep/ .el-form-item__content {
            justify-content: center;
            align-items: center;
          }
        }

        .section2-next{
          width: 200px;
        }
      }
    }

    .text-center {
      font-size: 18px;
    }

    .text-center1 {
      font-size: 14px;
    }
  }
}
</style>
