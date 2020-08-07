<template>
    <div class="app-container mt-4">
        <h3 class="text-center">重设密码</h3>
        <el-form ref="form"
                 :model="form"
                 :rules="formRules"
                 label-width="100px"
                 class="mt-4"
                 label-position="left">
            <el-form-item label="登录邮箱" prop="email">
                <el-input v-model="form.email" placeholder="请输入登录邮箱"></el-input>
            </el-form-item>
            <el-form-item label="验证码" prop="validCode">
                <el-input v-model="form.validCode" placeholder="邮箱验证码">
                    <el-button slot="append"
                               type="primary"
                               :loading="sendingValidCode"
                               @click="onEmailCodeClick">
                        获取验证码
                    </el-button>
                </el-input>
            </el-form-item>
            <el-form-item label="新密码" prop="newPassword">
                <el-input v-model="form.newPassword" placeholder="不少于8位，字母+数字" type="password"></el-input>
            </el-form-item>
            <el-form-item label="重复新密码" prop="repeatPassword">
                <el-input v-model="form.repeatPassword" placeholder="重复一遍新密码" type="password"></el-input>
            </el-form-item>
            <el-form-item label="图片验证码" prop="imgValidCode">
                <el-input v-model="form.imgValidCode" placeholder="输入图片验证码">
                    <el-image
                            slot="append"
                            style="width: 100px; height: 100%"
                            :src="captchaUrl"
                            @click="refreshCaptcha">
                    </el-image>
                </el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="onSubmitClick">重设密码</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
    import {getEmailCode, resetPwd} from "@/api/auth_api";
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
                form: {
                    email: "",
                    validCode: "",
                    newPassword: "",
                    repeatPassword: "",
                    imgValidCode: ""
                },
                formRules: {
                    email: [
                        {required: true, validator: Validator.checkEmail, trigger: "blur"}
                    ],
                    validCode: [
                        {required: true, message: "验证码不能为空", trigger: "blur"}
                    ],
                    newPassword: [
                        {
                            required: true,
                            validator: Validator.checkPassword,
                            trigger: "blur"
                        }
                    ],
                    repeatPassword: [
                        {required: true, validator: checkRepeatPassword, trigger: "blur"}
                    ],
                    imgValidCode: [
                        {required: true, message: "图片验证码不能为空", trigger: "blur"}
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
                        resetPwd(this.form)
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
            }
        }
    };
</script>

<style scoped lang="scss">
    .app-container {
        width: 100%;
        margin-top: 20px;
        display: flex;
        flex-direction: column;
        align-items: center;

        .text-center {
            font-size: 24px;
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
</style>
