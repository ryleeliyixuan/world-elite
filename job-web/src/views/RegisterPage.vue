<template>
  <div class="app-container">
    <div class="login-box">
      <div class="section1">
        <div class="section1-header bold"></div>
      </div>
      <div class="section2">
        <div class="bold section2-header">Hello, <br />Welcome to JOIN US!</div>
        <div class="regular-text section2-subheader">
          加入World Elite，及时掌握职场最新动态。
        </div>
        <div class="section2-down">
          <div class="form-container">
            <form @submit="onSubmit" @reset="onReset" class="register-form">
              <div class="input">
                <div class="input-sub">EMAIL ADDRESS</div>
                <el-input
                  size="medium"
                  v-model="form.email"
                  type="email"
                  class="item-input"
                  required
                  :placeholder="$t('login_email')"
                ></el-input>
              </div>
              <div class="input short">
                <div class="input-sub">VERIFICATION CODE</div>
                <el-input
                  size="medium"
                  v-model="form.validCode"
                  required
                  :placeholder="$t('email_valid_code')"
                  ><el-button
                    slot="append"
                    type="primary"
                    @click="recieveEmailCode"
                  >
                    获取验证码
                  </el-button></el-input
                >
              </div>
              <el-alert
                class="mt-2"
                :title="$t('email_block_tip')"
                type="warning"
                v-if="showEmailBlockTip"
              ></el-alert>
              <div class="input">
                <div class="input-sub">PASSWORD</div>
                <el-input
                  show-password
                  size="medium"
                  v-model="form.password"
                  type="password"
                  @input="showPasswordRuleTip = true"
                  required
                  :placeholder="$t('login_password')"
                ></el-input>
              </div>
              <div class="input">
                <div class="input-sub">COMFIRM PASSWORD</div>
                <el-input
                  show-password
                  v-model="form.newPassword"
                  placeholder="确认密码"
                  type="password"
                ></el-input>
              </div>
              <!-- <div class="mt-4 mb-4">
                <el-checkbox v-model="subscribeFlag"
                  >订阅职位邮件消息</el-checkbox
                >
              </div> -->
              <p class="mt-2 text-policy">
                点击“同意并加入”，即表示您同意遵守{{ $t("app_name") }}的
                <a
                  href="https://www.myworldelite.com/user-protocol"
                  target="_blank"
                  >《用户协议和隐私政策》</a
                >。
              </p>
              <el-button
                block
                size="medium"
                class="register-button mt-3"
                variant="info"
                :disabled="form.privateFlag != 1"
                v-on:click="onSubmit"
                >{{ $t("agree_to_regist") }}
              </el-button>
              <p class="text-left m-2">
                {{ $t("ask_regist_before") }}
                <b-link to="/login">{{ $t("login") }}</b-link>
              </p>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import { getEmailCode, checkEmailExists } from "@/api/auth_api";
import Toast from "@/utils/toast";

export default {
  name: "RegisterPage",

  data() {
    return {
      showEmailBlockTip: false,
      showPasswordRuleTip: false,
      subscribeFlag: "",
      form: {
        email: "",
        validCode: "",
        password: "",
        newPassword: "",
        subscribeFlag: 1,
        privateFlag: 1,
      },
    };
  },
  created() {
    this.$emit("complete");
  },
  methods: {
    onSubmit(evt) {
      evt.preventDefault();
      if (this.subscribeFlag) {
        this.form.subscribeFlag = 1;
      } else {
        this.form.subscribeFlag = 2;
      }
      if (this.form.password != this.form.newPassword) {
        this.$message.error("确认密码与密码不符，请重新输入密码！");
        return;
      }
      this.$store.dispatch("user/REGISTER", this.form).then(() => {
        this.$router.push({ path: "/register-basic" });
      });
    },
    recieveEmailCode() {
      if (this.form.email == "") {
        Toast.error("请先填写邮箱");
        return;
      }
      checkEmailExists(this.form.email).then(() => {
        getEmailCode(this.form.email).then(() => {
          Toast.success("已发送，请登录邮箱查看");
          this.showEmailBlockTip = true;
        });
      });
    },
    onReset(evt) {
      evt.preventDefault();
      // Reset our form values
      this.form.email = "";
      this.form.name = "";
      this.form.food = null;
      this.form.checked = [];
      // Trick to reset/clear native browser form validation state
      this.show = false;
      this.$nextTick(() => {
        this.show = true;
      });
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

      .section1-header {
        margin-left: 20px;
        margin-top: 84px;
        margin-bottom: 26px;
      }
    }
    .section2 {
      flex: 1;
      background: #ffffff;
      border-top-right-radius: 24px;
      border-bottom-right-radius: 24px;

      .section2-header {
        margin: 50px 0 9px 40px;
      }
      .section2-subheader {
        margin-left: 40px;
        margin-bottom: 25px;
      }

      .section2-down {
        display: flex;
        flex-direction: column;
        justify-content: center;

        .form-container {
          margin: 0 auto;
          width: 339px;

          .input {
            width: 339px;
            position: relative;
            margin-top: 24px;
            .input-sub {
              padding-left: 5px;
              padding-right: 5px;
              position: absolute;
              z-index: 1;
              left: 33px;
              top: -8px;
              background: #ffffff;
              font-size: 12px;
              font-family: PingFangSC-Regular, PingFang SC;
              font-weight: 400;
              color: #999999;
              line-height: 17px;
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
              background-color: white;
            }
          }

          .short {
            /deep/.el-input__inner {
              width: 195px;
              height: 0;
              padding: 1.4em 0.5em;
              background-clip: content-box;
            }
          }

          .forget-password {
            margin-top: 9px;
            margin-bottom: 27px;
          }

          /deep/ .el-input__inner {
            width: 339px;
            // height: 39px;
            background: #ffffff;
            border-radius: 20px;
            border: 1px solid #cccccc;
            height: 0;
            padding: 1.4em 0.5em;
            background-clip: content-box;
          }

          /deep/ .el-input__inner::placeholder {
            padding-left: 18px;
          }

          /deep/ .el-input__inner:focus {
            border: 1px solid #4895ef;
          }
          /deep/ .btn-info {
            height: 44px;
            line-height: 44px;
            padding: 0;
          }

          .login-button {
            width: 339px;
            height: 35px;
            background: #4895ef;
            border-radius: 18px;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-bottom: 9px;
          }
        }

        .third-party-login-text {
          font-size: 16px;
          font-family: PingFangSC-Medium, PingFang SC;
          font-weight: 500;
          color: #333333;
          line-height: 22px;
          margin-top: 27px;
          margin-bottom: 21px;
        }
      }
    }
  }

  .text-left {
    text-align: start;
    font-size: 14px;
    font-family: PingFangSC-Regular, PingFang SC;
    font-weight: 400;
    color: #999999;
    line-height: 20px;
  }

  .form-container {
    display: flex;
    justify-content: center;

    .register-form {
      width: 400px;
    }

    .item-container {
      width: 100%;
      height: 44px;
      margin-top: 30px;

      /deep/ .el-input__inner {
        height: 44px;
        line-height: 44px;
      }
    }

    .item2-container {
      display: flex;
      align-items: center;
    }

    .register-button {
      color: #ffffff;
      height: 35px;
      background: #4895ef;
      border-radius: 18px;
      width: 339px;
    }

    .text-policy {
      font-size: 14px;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      color: #999999;
      line-height: 20px;
    }
  }
}

@media screen and (max-width: 850px) {
  .app-container {
    padding: 20px 20px 0;
    .text-center {
      font-size: 18px;
    }

    .register-form {
      width: 300px;

      .item-container {
        margin-top: 20px;
      }
    }
  }
}
</style>
