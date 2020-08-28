<template>
    <div class="app-container">
        <div class="text-center">欢迎回来</div>
        <p class="text-center1 text-center">登录{{$t('app_name')}}，及时掌握职场最新动态。</p>
        <div class="form-container">
            <form @submit="onSubmit">
                <div class="mt-4">
                    <el-input size="medium" v-model="form.email" type="email" required placeholder="邮箱地址"></el-input>
                </div>
                <div class="mt-4">
                    <el-input
                            size="medium"
                            v-model="form.password"
                            type="password"
                            required
                            placeholder="密码"
                    ></el-input>
                </div>
                <div class="mt-4">
                    <el-checkbox v-model="form.rememberFlag" value="1" unchecked-value="0">保持登录状态</el-checkbox>
                </div>
                <b-button type="submit" size="medium" variant="info" class="mt-4 login-button" block>登录</b-button>
            </form>
        </div>
        <p class="text-center m-4">
            <el-link href="/forget-pwd" type="primary">忘记密码?</el-link>
        </p>
        <div class="text-center m-4 join-container">
            没有帐号？
            <el-link href="/register" type="primary">立即加入</el-link>
        </div>
        <el-divider>快捷登录</el-divider>
        <div class="m-4 text-center m-4-flex">
            <el-button type="success" circle class="btn-third-login" @click="goThridPartLoginUrl('wechat_open')">
                <svg-icon icon-class="wechat"/>
            </el-button>
            <el-button type="danger" circle class="btn-third-login" @click="goThridPartLoginUrl('google')">
                <svg-icon icon-class="google"/>
            </el-button>
            <el-button type="danger" circle class="btn-third-login" style="background: #ffffff;border: none"
                       @click="goThridPartLoginUrl('mylinkedin')">
                <svg-icon icon-class="linkedin" style="height: 50px;width: 50px"/>
            </el-button>
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
                    rememberFlag: 1
                },
                redirect: undefined
            };
        },
        watch: {
            $route: {
                handler: function (route) {
                    this.redirect = route.query && route.query.redirect;
                },
                immediate: true
            }
        },
        created() {
            this.$emit("complete");
        },
        methods: {
            onSubmit(evt) {
                evt.preventDefault();
                this.$store.dispatch("user/LOGIN", this.form).then(() => {
                    this.$router.push({path: this.redirect || "/"});
                });
            },
            goThridPartLoginUrl(type) {
                self.location = process.env.VUE_APP_BASE_API + '/oauth/login/' + type
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
            font-size: 24px;
        }

        .text-center1 {
            font-size: 16px;
        }

        .form-container {
            width: 400px;
            margin: 0 auto;

            /deep/ .el-input__inner {
                height: 44px;
                line-height: 44px;
            }
            /deep/  .btn-info{
                height: 44px;
                line-height: 44px;
                padding: 0;
            }

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
        .join-container{
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
