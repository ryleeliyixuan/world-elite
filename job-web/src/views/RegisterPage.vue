<template>
  <div class="app-container">
    <h3 class="text-center">加入 {{$t('app_name')}}</h3>
    <b-container>
      <b-row align-h="center">
        <b-form @submit="onSubmit" @reset="onReset" class="register-form">
          <b-input-group class="mt-4">
            <b-form-input
              size="lg"
              v-model="form.email"
              type="email"
              required
              :placeholder="$t('login_email')"
            ></b-form-input>
          </b-input-group>
          <b-input-group class="mt-4">
            <b-form-input
              size="lg"
              v-model="form.validCode"
              required
              :placeholder="$t('email_valid_code')"
            ></b-form-input>
            <b-input-group-append>
              <b-button
                size="lg"
                variant="info"
                v-on:click="recieveEmailCode"
              >{{$t('get_valid_code')}}</b-button>
            </b-input-group-append>
          </b-input-group>
          <el-alert
            class="mt-2"
            :title="$t('email_block_tip')"
            type="warning"
            v-if="showEmailBlockTip"
          ></el-alert>
          <b-input-group class="mt-4">
            <b-form-input
              size="lg"
              v-model="form.password"
              type="password"
              @input="showPasswordRuleTip = true"
              required
              :placeholder="$t('login_password')"
            ></b-form-input>
          </b-input-group>
          <el-alert
            class="mt-2"
            :title="$t('password_rule')"
            type="warning"
            v-if="showPasswordRuleTip"
          ></el-alert>
          <b-input-group class="mt-4 mb-4">
            <b-form-checkbox v-model="form.subscribeFlag" value="1" unchecked-value="0">订阅职位邮件消息</b-form-checkbox>
          </b-input-group>
          <p class="mt-4 text-policy">
            点击“同意并加入”，即表示您同意遵守{{$t('app_name')}}的
            <a
              href="http://www.myworldelite.com/user-protocol"
              target="_blank"
            >《用户协议和隐私政策》</a>。
          </p>
          <b-button
            block
            size="lg"
            type="submit"
            variant="info"
            :disabled="form.privateFlag != 1"
          >{{$t('agree_to_regist')}}</b-button>
        </b-form>
      </b-row>
      <p class="text-center m-4">
        {{$t('ask_regist_before')}}
        <b-link to="/login">{{$t('login')}}</b-link>
      </p>
    </b-container>
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
        this.$router.replace({ path: "/regist-success" });
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
    }
  }
};
</script>

<style scoped lang="scss">
@import "bootstrap/scss/bootstrap.scss";

.register-form {
  width: 400px;
}
.app-container {
  margin-top: 80px;
}
.text-policy {
  font-size: 14px;
  color: $gray-500;
}
</style>
