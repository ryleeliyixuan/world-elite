<template>
    <div class="app-container">
        <h3 class="text-center m-4">绑定 {{$t('app_name')}} 账号</h3>
        <el-form ref="form" :model="form" label-width="100px" :rules="formRules">
            <el-form-item label="登录邮箱" prop="email">
                <el-input v-model="form.email" placeholder="请输入邮箱"></el-input>
            </el-form-item>
            <el-form-item label="邮箱验证码" prop="validCode">
                <el-input v-model="form.validCode" placeholder="邮箱验证码">
                    <el-button
                            slot="append"
                            type="primary"
                            :loading="sendingValidCode"
                            @click="onEmailCodeClick"
                    >获取验证码
                    </el-button>
                </el-input>
            </el-form-item>
            <el-form-item label="登录密码" prop="password">
                <el-input v-model="form.password" placeholder="8-20位字母+数字" type="password"></el-input>
            </el-form-item>
            <el-form-item prop="subscribeFlag">
                <el-checkbox v-model="form.subscribeFlag" :true-label="1" :false-label="0">订阅职位邮件消息</el-checkbox>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="onSubmitClick">绑定账号</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
    import {getEmailCode, bindAccount} from "@/api/auth_api";
    import Toast from "@/utils/toast";
    import Validator from "@/utils/validator";

    export default {
        name: 'BindAccountPage',
        data() {
            return {
                form: {
                    authType: undefined,
                    email: "",
                    validCode: "",
                    password: "",
                    subscribeFlag: 1
                },
                formRules: {
                    email: {
                        required: true,
                        validator: Validator.checkEmail,
                        trigger: "blur"
                    },
                    validCode: [
                        {required: true, message: "验证码不能为空", trigger: "blur"}
                    ],
                    password: [
                        {
                            required: true,
                            validator: Validator.checkPassword,
                            trigger: "blur"
                        }
                    ]
                },
                sendingValidCode: false
            };
        },
        created() {
            this.form.authType = this.$route.query.authType;
        },
        methods: {
            onSubmitClick() {
                this.$refs["form"].validate(valid => {
                    if (valid) {
                        bindAccount(this.form).then(() => {
                            Toast.success("绑定成功");
                            this.$router.replace({path: "/regist-success"});
                        })
                    }
                });
            },
            onEmailCodeClick() {
                if (this.form.email === "") {
                    Toast.error("请先输入邮箱");
                    return;
                }
                this.sendingValidCode = true;
                getEmailCode(this.form.email).then(() => {
                    Toast.success("发送成功，请登录新邮箱查看。");
                }).finally(() => {
                    this.sendingValidCode = false;
                });
            }
        }
    };
</script>

<style scoped lang="scss">
    .app-container {
        margin: 40px auto;
        width: 400px;
        min-height: calc(100vh - 477px);
    }
</style>
