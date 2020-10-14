<template>
    <div class="app-container mt-4">
        <h3 class="text-center">修改密码</h3>
        <el-form
                ref="form"
                :model="form"
                :rules="formRules"
                label-width="100px"
                class="mt-4"
                label-position="left"
        >
            <el-form-item label="旧密码" prop="oldPassword">
                <el-input v-model="form.oldPassword" placeholder="输入旧密码" type="password"></el-input>
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
    import {getEmailCode, modifyPwd} from "@/api/auth_api";
    import Validator from "@/utils/validator";
    import Toast from "@/utils/toast";

    export default {
        name: "ModifyPwdPage",
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
                    oldPassword: "",
                    newPassword: "",
                    repeatPassword: "",
                    imgValidCode: ""
                },
                formRules: {
                    oldPassword: [
                        {required: true, message: "旧密码不能为空", trigger: "blur"}
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
        mounted() {
            this.$emit("complete");
        },
        methods: {
            refreshCaptcha() {
                this.captchaUrl = this.captchaUrl + "?" + new Date().getTime();
            },
            onSubmitClick() {
                this.$refs["form"].validate(valid => {
                    if (valid) {
                        modifyPwd(this.form)
                            .then(() => {
                                Toast.success('修改密码成功');
                            })
                            .finally(() => {
                                this.refreshCaptcha(); //刷新图片验证码
                            });
                    }
                });
            }
        }
    };
</script>

<style scoped>
    .app-container {
        width: 400px;
        margin: 20px auto;
        min-height: calc(100vh - 452px);
    }
</style>
