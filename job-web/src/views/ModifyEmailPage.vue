<template>
  <div class="app-container mt-4">
    <h3 class="text-center">变更邮箱地址</h3>
    <el-form
      ref="form"
      :model="form"
      :rules="formRules"
      label-width="100px"
      class="mt-4"
      label-position="left"
    >
      <el-form-item label="登录密码" prop="password">
        <el-input v-model="form.password" placeholder="请输入登录密码" type="password"></el-input>
      </el-form-item>
      <el-form-item label="新邮箱" prop="newEmail">
        <el-input v-model="form.newEmail" placeholder="请输入新邮箱"></el-input>
      </el-form-item>
      <el-form-item label="验证码" prop="validCode">
        <el-input v-model="form.validCode" placeholder="邮箱验证码">
          <el-button
            slot="append"
            type="primary"
            :loading="sendingValidCode"
            @click="onEmailCodeClick"
          >获取验证码</el-button>
        </el-input>
      </el-form-item>
      <el-form-item label="图片验证码" prop="imgValidCode">
        <el-input v-model="form.imgValidCode" placeholder="输入图片验证码">
          <el-image
            slot="append"
            style="width: 100px; height: 100%"
            :src="captchaUrl"
            @click="refreshCaptcha"
          ></el-image>
        </el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmitClick">重设密码</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { getEmailCode } from "@/api/auth_api";
import { modifyEmail } from "@/api/user_api";
import Validator from "@/utils/validator";
import Toast from "@/utils/toast";
export default {
  name: "ForgetPwdPage",
  data() {
    return {
      captchaUrl: process.env.VUE_APP_BASE_API + "/auth/get-captcha",
      sendingValidCode: false,
      form: {
        newEmail: "",
        validCode: "",
        password: "",
        imgValidCode: ""
      },
      formRules: {
        newEmail: [
          { required: true, validator: Validator.checkEmail, trigger: "blur" }
        ],
        validCode: [
          { required: true, message: "验证码不能为空", trigger: "blur" }
        ],
        password: [
          {
            required: true,
            message: "密码不能为空",
            trigger: "blur"
          }
        ],
        imgValidCode: [
          { required: true, message: "图片验证码不能为空", trigger: "blur" }
        ]
      }
    };
  },
  methods: {
    refreshCaptcha() {
      this.captchaUrl = this.captchaUrl + "?" + new Date().getTime();
    },
    onSubmitClick() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          modifyEmail(this.form)
            .then(() => {
              this.$confirm("重置密码成功", "提示", {
                confirmButtonText: "重新登录",
                type: "success"
              }).then(() => {
                this.$router.replace("/login");
              });
            })
            .finally(() => {
              this.refreshCaptcha(); //刷新图片验证码
            });
        }
      });
    },
    onEmailCodeClick() {
      if (this.form.newEmail === "") {
        Toast.error("请先输入邮箱");
        return;
      }
      this.sendingValidCode = true;
      getEmailCode(this.form.newEmail)
        .then(() => {
          Toast.success("发送成功，请登录新邮箱查看。");
        })
        .finally(() => {
          this.sendingValidCode = false;
          this.refreshCaptcha(); //刷新图片验证码
        });
    }
  }
};
</script>

<style scoped>
.app-container {
  width: 400px;
  margin: 20px auto;
}
</style>