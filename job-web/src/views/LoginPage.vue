<template>
  <div class="app-container">
    <div class="login-box">
      <div class="section1">
        <div class="section1-header bold"></div>
      </div>
      <div class="section2">
        <div class="bold section2-header">Hello, <br />Welcome Back!</div>
        <div class="regular-text section2-subheader">
          登录{{ $t("app_name") }}，及时掌握职场最新动态。
        </div>
        <div class="section2-down">
          <div class="form-container">
            <form @submit="onSubmit">
              <div class="input">
                <div class="input-sub">EMAIL ADDRESS</div>
                <el-input
                  v-model="form.email"
                  type="email"
                  required
                  placeholder="邮箱地址"
                ></el-input>
              </div>
              <div class="input">
                <div class="input-sub">PASSWORD</div>
                <el-input
                  :show-password="true"
                  v-model="form.password"
                  type="password"
                  required
                  placeholder="密码"
                ></el-input>
              </div>
              <div class="forget-password">
                <el-link href="/forget-pwd" type="primary" class="grey"
                  >忘记密码?</el-link
                >
              </div>
              <b-button type="submit" variant="info" class="login-button" block
                >登录</b-button
              >
            </form>
          </div>
          <div class="grey text-center">
            还没有帐号？
            <el-link href="/register" type="primary">前往注册</el-link>
          </div>
          <div class="text-center third-party-login-text">其他登录方式</div>
          <div class="text-center m-4-flex">
            <el-button
              type="success"
              circle
              class="btn-third-login"
              style="background: #ffffff; border: none"
              @click="goThridPartLoginUrl('wechat_open')"
            >
              <svg-icon icon-class="wechat" style="height: 39px; width: 39px" />
            </el-button>
            <el-button
              type="danger"
              circle
              class="btn-third-login"
              style="background: #ffffff; border: none"
              @click="goThridPartLoginUrl('google')"
            >
              <svg-icon icon-class="google" style="height: 39px; width: 39px" />
            </el-button>
            <el-button
              type="danger"
              circle
              class="btn-third-login"
              style="background: #ffffff; border: none"
              @click="goThridPartLoginUrl('mylinkedin')"
            >
              <svg-icon
                icon-class="linkedin"
                style="height: 39px; width: 39px"
              />
            </el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "LoginPage",
  data() {
    return {
      form: {
        email: "",
        password: "",
        rememberFlag: "",
      },
      redirect: undefined,
      timer: null, //定时器
      currentIndex: 0,
    };
  },
  watch: {
    $route: {
      handler: function (route) {
        this.redirect = route.query && route.query.redirect;
        console.log(this.redirect);
      },
      immediate: true,
    },
  },
  created() {
    this.$emit("complete");
  },
  methods: {
    onSubmit(evt) {
      evt.preventDefault();
      this.$store.dispatch("user/LOGIN", this.form).then(() => {
        let query = { ...this.$route.query };
        delete query.redirect;
        this.$router.replace({ path: this.redirect || "/", query });
      });
    },
    goThridPartLoginUrl(type) {
      self.location = process.env.VUE_APP_BASE_API + "/oauth/login/" + type;
    },
  },
};
</script>

<style scoped lang="scss">
.app-container {
  max-width: 1200px;

  .login-box {
    height: 655px;
    box-shadow: 0px 4px 16px 3px rgba(191, 199, 215, 0.31);
    border-radius: 24px;
    display: flex;
    background: #f6f9fc;
    margin-top: 35px;
    margin-bottom: 88px;

    .regular-text {
      font-size: 16px;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      color: #333333;
      line-height: 22px;
    }

    .bold {
      font-size: 36px;
      font-family: Roboto-Medium, Roboto;
      font-weight: 500;
      color: #333333;
      line-height: 39px;
    }

    .grey {
      font-size: 14px;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      color: #999999;
      line-height: 20px;
    }

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
          }

          .forget-password {
            margin-top: 9px;
            margin-bottom: 27px;
          }

          /deep/ .el-input__inner {
            width: 339px;
            height: 39px;
            background: #ffffff;
            border-radius: 20px;
            border: 1px solid #cccccc;
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

  .text-center {
    font-size: 24px;
  }

  .text-center1 {
    font-size: 16px;
  }

  .m-4-flex {
    display: flex;
    align-items: center;
    justify-content: center;

    .btn-third-login {
      font-size: 28px;
      width: 50px;
      height: 50px;
      text-align: center;
      padding: 0px;
    }
  }
  .join-container {
    display: flex;
    align-items: center;
    justify-content: center;
  }
}
@media screen and (max-width: 850px) {
  .app-container {
    padding: 20px 20px 0;
    .text-center {
      font-size: 18px;
    }

    .text-center1 {
      font-size: 14px;
    }
    .form-container {
      width: 300px;
    }
  }
}
</style>
