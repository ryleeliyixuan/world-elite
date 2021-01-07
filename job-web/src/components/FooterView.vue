<template>
    <div>
        <div class="app-footer">
            <div class="section1-container">

                <div class="section1-item">
                    <h5 class="section1-text">友情链接</h5>
                    <div>
                        <el-link class="link-text" href="https://www.myworldelite.com/">{{$t('app_name')}}</el-link>
                    </div>
                    <div>
                        <el-link class="link-text" href="https://company.myworldelite.com/">{{$t('app_name')}}企业端</el-link>
                    </div>
                </div>

                <div class="section1-item">
                    <h5 class="section1-text">用户帮助</h5>
                    <div>
                        <el-link class="link-text" href="https://www.myworldelite.com/user-protocol">用户协议以及隐私政策</el-link>
                    </div>
                </div>

                <div class="section1-item">
                    <h5 class="section1-text">联系我们</h5>
                    <a class="link-text" href="mailto:career@myworldelite.com">邮箱：career@myworldelite.com</a>
                </div>

                <div class="section1-item">
                    <div class="qr-wechat">
                        <el-image class="qr-wechat-img" src="https://oss.myworldelite.com/static/worldelite-gongzhonghao.jpg"></el-image>
                    </div>
                </div>

            </div>
        </div>

        <div class="footer-copyright text-small">
            <span><a href="https://beian.miit.gov.cn/">蜀ICP备20009332</a></span>
            <span>©2020 成都寰球菁英信息科技有限公司</span>
        </div>
    </div>
</template>

<script>
    export default {
        name: "FooterView",
        data() {
            return {};
        },
        methods: {}
    };
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
    .app-footer {
        padding: 0 20px 10px;

        .section1-container {
            max-width: 1140px;
            height: auto;
            display: flex;
            align-items: flex-start;
            justify-content: space-between;
            margin: 0 auto;
            flex-wrap: wrap;

            .section1-item {
                padding: 20px 40px 0;
                display: flex;
                flex-direction: column;

                .section1-text {
                    margin-bottom: 14px;
                }
            }

            .link-text {
                font-size: 14px;
                font-weight: 400;
                margin-bottom: 5px;
            }

            .qr-wechat {
                text-align: center;

                .qr-wechat-img {
                    width: 100px;
                    height: 100px;
                }
            }
        }
    }

    .footer-copyright {
        border-top: 1px solid rgb(245, 238, 238);
        padding: 20px 0;
        display: flex;
        justify-content: center;
        flex-wrap: wrap;
        span {
            padding: 0 16px;
        }
    }

    @media screen and (max-width: 1140px) {
        .app-footer {
            .section1-container {
                justify-content: center;
                .section1-item {
                    align-items: center;
                    width: 212px;
                    padding: 20px 0;
                }
            }
        }
    }



</style>
