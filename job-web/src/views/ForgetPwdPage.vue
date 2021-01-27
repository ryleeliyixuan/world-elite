<template>
  <div class="app-container">
    <div class="login-box">
      <div class="section1">
        <div class="section1-header"></div>
      </div>
      <div class="section2" v-loading="submitLoading" v-if="!success">
        <div class="section2-header text-center">重新设置登录密码</div>
        <el-form
          class="section2-form"
          ref="form"
          :model="form"
          :rules="formRules"
          label-width="0px"
          label-position="top"
        >
          <el-form-item label="" prop="email">
            <span class="star">*</span>
            <el-input
              v-model="form.email"
              placeholder="请输入登录邮箱"
            ></el-input>
          </el-form-item>
          <el-form-item class="short" label="" prop="validCode">
            <span class="star">*</span>
            <el-input v-model="form.validCode" placeholder="邮箱验证码">
              <el-button
                slot="append"
                type="primary"
                :loading="sendingValidCode"
                @click="onEmailCodeClick"
              >
                获取验证码
              </el-button>
            </el-input>
          </el-form-item>
          <el-form-item label="" prop="newPassword">
            <span class="star">*</span>
            <el-input
              v-model="form.newPassword"
              show-password
              placeholder="新密码字母+数字不少于8位"
              type="password"
            ></el-input>
          </el-form-item>
          <el-form-item label="" prop="repeatPassword">
            <span class="star">*</span>
            <el-input
              show-password
              v-model="form.repeatPassword"
              placeholder="确认密码"
              type="password"
            ></el-input>
          </el-form-item>
          <el-form-item class="short" label="" prop="imgValidCode">
            <span class="star">*</span>
            <el-input v-model="form.imgValidCode" placeholder="图片验证码">
              <el-image
                slot="append"
                style="width: 100px; height: 100%"
                :src="captchaUrl"
                @click="refreshCaptcha"
              >
              </el-image>
            </el-input>
          </el-form-item>
          <el-form-item class="item-center">
            <el-button type="primary" @click="onSubmitClick">下一步</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div class="section2" v-loading="submitLoading" v-else>
        <div class="section2-header text-center">密码重设成功！</div>
        <div class="center">
          <svg-icon
            icon-class="forgetpwd-success"
            style="height: 164px; width: 297px"
          />
        </div>
        <div class="center section2-login-text">马上去重新登录吧！</div>
        <div class="center"><el-button @click="onLogin">登录</el-button></div>
      </div>
    </div>
  </div>
</template>

<script>
import { getEmailCode, resetPwd } from "@/api/auth_api";
import Validator from "@/utils/validator";
import Toast from "@/utils/toast";

export default {
  name: "ForgetPwdPage",
  data() {
    const checkRepeatPassword = (rule, value, callback) => {
      if (!value) {
        callback(new Error("请再次输入密码"));
      } else if (value !== this.form.newPassword) {
        callback(new Error("两次输入密码不一致!"));
      } else {
        callback();
      }
    };
    return {
      captchaUrl: process.env.VUE_APP_BASE_API + "/auth/get-captcha",
      sendingValidCode: false,
      success: false,
      submitLoading: false,
      form: {
        email: "",
        validCode: "",
        newPassword: "",
        repeatPassword: "",
        imgValidCode: "",
      },
      formRules: {
        email: [
          { required: true, validator: Validator.checkEmail, trigger: "blur" },
        ],
        validCode: [
          { required: true, message: "验证码不能为空", trigger: "blur" },
        ],
        newPassword: [
          {
            required: true,
            validator: Validator.checkPassword,
            trigger: "blur",
          },
        ],
        repeatPassword: [
          { required: true, validator: checkRepeatPassword, trigger: "blur" },
        ],
        imgValidCode: [
          { required: true, message: "图片验证码不能为空", trigger: "blur" },
        ],
      },
    };
  },
  created() {
    this.$emit("complete");
  },
  methods: {
    refreshCaptcha() {
      this.captchaUrl = this.captchaUrl + "?" + new Date().getTime();
    },
    onSubmitClick() {
      this.submitLoading = true;
      this.$refs["form"].validate((valid) => {
        if (valid) {
          resetPwd(this.form)
            .then(() => {
              this.success = true;
            })
            .finally(() => {
              this.refreshCaptcha(); //刷新图片验证码
            });
        }
      });
      this.submitLoading = false;
    },
    onEmailCodeClick() {
      if (this.form.email === "") {
        Toast.error("请先输入邮箱");
        return;
      }
      this.sendingValidCode = true;
      getEmailCode(this.form.email)
        .then(() => {
          Toast.success("发送成功，请登录邮箱查看。");
        })
        .finally(() => {
          this.sendingValidCode = false;
          this.refreshCaptcha(); //刷新图片验证码
        });
    },
    onLogin() {
      this.$router.push("/login");
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

  .center {
    display: flex;
    justify-content: center;

    /deep/ .el-button {
      width: 134px;
      height: 35px;
      background: #4895ef;
      border-radius: 18px;
      font-size: 16px;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #ffffff;
      line-height: 22px;
      display: flex;
      justify-content: center;
      align-items: center;
    }
  }

  .login-box {
    height: 655px;
    width: 100%;
    box-shadow: 0px 4px 16px 3px rgba(191, 199, 215, 0.31);
    border-radius: 24px;
    display: flex;
    background: #f6f9fc;
    margin-top: 35px;
    margin-bottom: 88px;
    .section1 {
      flex: 1;
      background: url("../assets/login-background.svg") repeat;
      background-size: cover;

      .section1-header {
      }
    }
    .section2 {
      flex: 1;
      .section2-header {
        margin-top: 98px;
        margin-bottom: 54px;
      }

      .section2-login-text {
        font-size: 18px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #333333;
        line-height: 25px;
        margin-top: 40px;
        margin-bottom: 15px;
      }

      .section2-form {
        margin: auto;

        .item-center {
          margin-top: 60px;
          /deep/.el-form-item__content {
            display: flex;
            justify-content: center;
          }
        }

        /deep/.el-input__inner {
          width: 339px;
          height: 39px;
          background: #ffffff;
          border-radius: 20px;
          border: 1px solid #cccccc;
          font-size: 16px;
          font-family: PingFangSC-Regular, PingFang SC;
          font-weight: 400;
          color: #999999;
          line-height: 22px;
        }

        /deep/.el-form-item__content {
          display: flex;
        }

        .star {
          margin-right: 5px;
          width: 10px;
          height: 25px;
          font-size: 18px;
          font-family: PingFangSC-Regular, PingFang SC;
          font-weight: 400;
          color: #f44336;
          line-height: 25px;
        }

        .short {
          /deep/.el-input__inner {
            width: 195px;
          }
        }

        /deep/.el-button {
          margin-left: 5px;
          display: flex;
          justify-content: center;
          align-items: center;
          width: 120px;
          height: 35px;
          background: #4895ef;
          border-radius: 18px;
          font-size: 16px;
          font-family: PingFangSC-Medium, PingFang SC;
          font-weight: 500;
          color: #ffffff;
          line-height: 22px;
        }

        /deep/.el-input-group__append {
          border: 0px;
        }
      }
    }
  }

  .text-center {
    font-size: 21px;
    font-family: PingFangSC-Medium, PingFang SC;
    font-weight: 500;
    color: #333333;
    line-height: 29px;
  }

  /deep/ .el-form {
    width: 315px;

    .el-form-item {
      display: flex;
      flex-direction: column;

      .el-form-item__content {
        margin-left: 0 !important;
        text-align: center;
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

        .section2-form {
          /deep/.el-input {
            width: auto;
          }
          /deep/.el-input__inner {
            width: 200px;
          }

          .el-input-group__append {
            .el-button {
              width: 75px;
              font-size: 12px;
            }

            .el-image {
              width: 80px !important;
            }
          }
        }

        /deep/ .el-input-group__append,
        .el-input-group__prepend {
          padding: 0 12px;
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
