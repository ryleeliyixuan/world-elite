<template>
    <div class="app-container">
        <div class="text-center">加入 {{$t('app_name')}}</div>
        <div class="form-container">
            <form @submit="onSubmit" @reset="onReset" class="register-form">
                <div class="item-container">
                    <el-input
                            size="medium"
                            v-model="form.email"
                            type="email"
                            class="item-input"
                            required
                            :placeholder="$t('login_email')"
                    ></el-input>
                </div>
                <div class="item-container item2-container">
                    <el-input
                            size="medium"
                            v-model="form.validCode"
                            required
                            :placeholder="$t('email_valid_code')"
                    ></el-input>
                    <div>
                        <el-button
                                size="medium"
                                variant="info"
                                v-on:click="recieveEmailCode"
                                class="register-button"
                        >{{$t('get_valid_code')}}
                        </el-button>
                    </div>
                </div>
                <el-alert
                        class="mt-2"
                        :title="$t('email_block_tip')"
                        type="warning"
                        v-if="showEmailBlockTip"
                ></el-alert>
                <div class="item-container">
                    <el-input
                            size="medium"
                            v-model="form.password"
                            type="password"
                            @input="showPasswordRuleTip = true"
                            required
                            :placeholder="$t('login_password')"
                    ></el-input>
                </div>

                <el-alert
                        class="mt-2"
                        :title="$t('password_rule')"
                        type="warning"
                        v-if="showPasswordRuleTip"
                ></el-alert>

                <div class="mt-4 mb-4">
                    <el-checkbox v-model="subscribeFlag">订阅职位邮件消息</el-checkbox>
                </div>

                <p class="mt-4 text-policy">
                    点击“同意并加入”，即表示您同意遵守{{$t('app_name')}}的
                    <a
                            href="https://www.myworldelite.com/user-protocol"
                            target="_blank"
                    >《用户协议和隐私政策》</a>。
                </p>
                <el-button
                        block
                        size="medium"
                        class="register-button register2-button"
                        variant="info"
                        :disabled="form.privateFlag != 1"
                        v-on:click="onSubmit"
                >{{$t('agree_to_regist')}}
                </el-button>
            </form>
        </div>
        <p class="text-center m-4">
            {{$t('ask_regist_before')}}
            <b-link to="/login">{{$t('login')}}</b-link>
        </p>
    </div>
</template>
<script>
    import {getEmailCode, checkEmailExists} from "@/api/auth_api";
    import Toast from "@/utils/toast";

    export default {
        name: "RegisterPage",

        data() {
            return {
                showEmailBlockTip: false,
                showPasswordRuleTip: false,
                subscribeFlag: '',
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
                if (this.subscribeFlag) {
                    this.form.subscribeFlag = 1
                } else {
                    this.form.subscribeFlag = 2
                }
                this.$store.dispatch("user/REGISTER", this.form).then(() => {
                    this.$router.replace({path: "/regist-success"});
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
    .app-container {
        max-width: 1140px;
        margin: 0 auto;
        padding: 40px 20px 0;

        .text-center {
            text-align: center;
            font-size: 24px;
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
                background-color: #17A2B8;
                color: #ffffff;
                height: 44px;
            }

            .register2-button {
                width: 100%;
                height: 44px;
            }

            .text-policy {
                font-size: 14px;
            }
        }
    }

    @media screen and (max-width: 850px) {
        .app-container {
            padding: 20px 20px 0;
            .text-center{
                font-size: 18px;
            }
            .register-form {
               width: 300px;
                .item-container{
                    margin-top: 20px;
                }
            }
        }
    }


</style>
