<template>
  <div class="app-container">
    <div class="login-box text-center">
      <div class="header1 text">已成功注册World Elite账号！</div>
      <div class="content">
        <svg-icon
          icon-class="forgetpwd-success"
          style="height: 152px; width: 277px"
        />
        <div class="qr-wechat">
          <el-image
            class="qr-wechat-img"
            src="https://oss.myworldelite.com/static/worldelite-gongzhonghao.jpg"
          ></el-image>
          <p class="qr-wechat-text">关注我们公众号了解更多资讯</p>
        </div>
      </div>
      <div class="header2 text">完善简历信息，让HR更容易发现你。</div>
      <el-button @click="goToEditResume">填写简历</el-button>
    </div>
  </div>
</template>

<script>
export default {
  name: "RegisterSuccessPage",

  data() {
    return {};
  },
  created() {
    this.$emit("complete");
  },
  methods: {
    goToEditResume() {
      this.$router.push("/edit-resume");
    },
  },
};
</script>

<style scoped lang="scss">
/deep/ .el-button {
  border: 0px;
}

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
    background: #ffffff;
    margin-top: 35px;
    margin-bottom: 88px;
    padding-top: 87px;
    .text {
      font-size: 24px;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #333333;
      line-height: 33px;
    }
    .header1 {
      margin-bottom: 79px;
    }
    .header2 {
      margin-top: 71px;
      margin-bottom: 37px;
    }

    .content {
      display: flex;
      justify-content: center;
      .qr-wechat-text {
        font-size: 14px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #666666;
        line-height: 20px;
      }
    }

    .qr-wechat-img {
      margin-right: 17px;
      width: 129px;
      height: 129px;
    }

    /deep/.el-button {
      width: 151px;
      height: 35px;
      background: #4895ef;
      border-radius: 18px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin: auto;
      color: #ffffff;
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

@media screen and (max-width: 450px) {
  .app-container {
    .login-box {
      padding-right: 10px;
      padding-left: 10px;
    }
  }
}
</style>
