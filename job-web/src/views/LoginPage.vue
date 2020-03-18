<template>
  <div class="app-container">
    <h3 class="text-center">欢迎回来</h3>
    <p class="text-center">登录WorldElite，及时掌握职场最新动态。</p>
    <b-container>
      <b-row align-h="center">
        <b-form @submit="onSubmit" class="register-form">
          <b-input-group class="mt-4">
            <b-form-input size="lg" v-model="form.email" type="email" required placeholder="邮箱地址"></b-form-input>
          </b-input-group>

          <b-input-group class="mt-4">
            <b-form-input
              size="lg"
              v-model="form.password"
              type="password"
              required
              placeholder="密码"
            ></b-form-input>
          </b-input-group>

          <b-button type="submit" size="lg" variant="info" class="mt-4" block>登录</b-button>
        </b-form>
      </b-row>
      <p class="text-center m-4">
        <b-link href="/forget-pwd">忘记密码?</b-link>
      </p>
      <p class="text-center m-4">
        没有帐号？
        <b-link to="/register">立即加入</b-link>
      </p>
      <el-divider>快捷登录</el-divider>
      <div class="m-4 text-center">
        <el-button type="success" circle class="btn-third-login" @click="goThridPartLoginUrl('wechat_open')"><svg-icon icon-class="wechat" /></el-button>
        <el-button type="danger" circle class="btn-third-login" @click="goThridPartLoginUrl('google')"><svg-icon icon-class="google" /></el-button>
     </div>
    </b-container>
  </div>
</template>

<script>
export default {
  name: "LoginPage",
  data() {
    return {
      form: {
        email: "",
        password: ""
      },
      redirect: undefined
    };
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect;
      },
      immediate: true
    }
  },
  methods: {
    onSubmit(evt) {
      evt.preventDefault();
      this.$store.dispatch("user/LOGIN", this.form).then(() => {
        this.$router.push({ path: this.redirect || "/" });
      });
    },
    goThridPartLoginUrl(type){
      self.location = process.env.VUE_APP_BASE_API + '/oauth/login/' + type
    }
  }
};
</script>

<style scoped>
.register-form {
  width: 400px;
}
.app-container {
  width: 600px;
  margin: 80px auto 0;
}
.btn-third-login{
    font-size: 28px;
    width: 50px;
    height: 50px;
    text-align: center;
    padding: 0px;
}
</style>
