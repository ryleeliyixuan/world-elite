<template>
  <div class="app-container">
    <h3 class="text-center">加入Edlite World</h3>
    <b-container>
      <b-row align-h="center">
        <b-form @submit="onSubmit" @reset="onReset" class="register-form">
          <b-input-group class="mt-4">
            <b-form-input size="lg" v-model="form.email" type="email" required placeholder="邮箱地址"></b-form-input>
          </b-input-group>

          <b-input-group class="mt-4">
            <b-form-input size="lg" v-model="form.validCode" required placeholder="邮箱验证码"></b-form-input>
            <b-input-group-append>
              <b-button size="lg" variant="info" v-on:click="recieveEmailCode">获取验证码</b-button>
            </b-input-group-append>
          </b-input-group>

          <b-input-group class="mt-4">
            <b-form-input size="lg" v-model="form.password" type="password" required placeholder="设置不少于8位的密码"></b-form-input>
          </b-input-group>

          <p class="mt-4 mb-4 text-policy">点击“同意并加入”，即表示您同意遵守WorldEdlite的《用户协议》、《隐私政策》及《Cookie 政策》。</p>

          <b-button block size="lg" type="submit" variant="info" :disabled="form.privateFlag != 1">同意并加入</b-button>
        </b-form>
      </b-row>
       <p class="text-center m-4">
        已经有帐号？<b-link to="/login">登录</b-link>
      </p>
    </b-container>
  </div>
</template>

<script>
import { getEmailCode } from "@/api/user_api";

export default {
  data() {
    return {
      form: {
        email: "",
        validCode: "",
        password: "",
        subscribeFlag: 1,
        privateFlag: 1
      }
    };
  },
  methods: {
    onSubmit(evt) {
      evt.preventDefault();
      this.$store.dispatch("user/REGISTER", this.form).then(() => {
        this.$router.push({ path: "/edit-resume" });
      });
    },
    recieveEmailCode() {
      if (this.form.email == "") {
        this.$bvToast.toast("请先填写邮箱", {
          title: "提示",
          toaster: 'b-toaster-top-center',
          variant: "danger"
        });
        return;
      }
      getEmailCode(this.form.email).then(() => {
        this.$bvToast.toast("已发送，请查看邮箱", {
          title: "提示",
          toaster: 'b-toaster-top-center',
          variant: "success"
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
    }
  }
};
</script>

<style scoped lang="scss">
@import 'bootstrap/scss/bootstrap.scss';

.register-form {
  width: 400px;
}
.app-container {
  margin-top: 80px;
}
.text-policy{
  font-size: 14px;
  color: $gray-500;
}
</style>
