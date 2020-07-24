<template>
    <div class="app-container">
        <div class="section1-container">
            <img src="../assets/item2.jpg" alt="" class="section1-image">
        </div>
        <div class="section2-container">
            <div class="section2-inner">
                <div class="section2-title"><span style="color:#f56600">小米</span>未来星</div>
                <div class="section2-item1">
                    小米集团成立于2010年4月，2018年7月9日在香港交易所主板挂牌上市（1810. HK) , 是一家以手机、智能硬件和IoT 平台为核心的互联 <br class="section2-br">
                    网公司。目前，目前小米集团已进入全球逾 90 个市场，全球互联网企业中排名第七，并在诸多市场处于领先地位。<br class="section2-br">
                    此外，小米的管理体系及应届生培养通道，为有激情、有梦想的年轻人提供了绝佳的成长平台。<br class="section2-br">
                    小米未来星计划是面向全球高校顶级博士及硕士毕业生打造的，以培养小米未来技术精英以及领域专家为定位的专项校招项目。未来星<br class="section2-br">
                    们将以探索性基础研究与工程实现为己任，助力小米在手机、智能硬件、loT、人工智能、互联网金融等各个前沿领域保持领先的行业竞<br class="section2-br">
                    争力，用科技改善人类生活，用智慧创造新的未来。<br>
                </div>
            </div>
        </div>
        <div class="section3-container">


        </div>


    </div>
</template>

<script>
    export default {
        name: "mi"
    }
</script>

<style scoped lang="scss">
    .app-container {
        min-width: 375px;

        .section1-container {
            max-width: 1140px;
            height: auto;
            margin: 0 auto;

            .section1-image {
                width: 100%;
                height: auto;
            }
        }

        .section2-container {
            max-width: 1140px;
            background: #f8f8f8;
            padding: 50px 0 50px 0;
            margin: auto;

            .section2-inner {
                max-width: 1140px;
                height: auto;
                margin: 0 auto 30px;

                .section2-title {
                    font-size: 54px;
                    width: 100%;
                    text-align: center;
                    color: #0060ff;
                    font-weight: bold;
                    line-height: 54px;
                    margin-bottom: 70px;
                }

                .section2-item1 {
                    width: 100%;
                    text-align: center;
                    font-size: 18px;
                    line-height: 42px;
                    color: #7e8287;
                }
            }
        }
    }

    @media screen and (max-width: 800px) {
        .app-container {
            .section1-container {
                padding: 0 20px;
            }

            .section2-container {
                padding: 30px 0 30px 0;

                .section2-inner {
                    .section2-title {
                        font-size: 30px;
                        line-height: 30px;
                        margin-bottom: 30px;
                    }

                    .section2-item1 {
                        font-size: 16px;
                        line-height: 32px;
                        text-indent: 2em;
                        text-align: left;
                        padding: 0 20px;
                        .section2-br {
                            display: none;
                        }
                    }

                }
            }


        }
    }

    @media screen and (max-width: 400px) {
        .app-container {
            .section1-container {
                width: 100%;
                padding: 0 20px;
            }
            .section2-container {
                padding: 30px 0 30px 0;

                .section2-inner {
                    .section2-title {
                        font-size: 30px;
                        line-height: 30px;
                        margin-bottom: 30px;
                    }

                    .section2-item1 {
                        font-size: 16px;
                        line-height: 32px;
                        text-indent: 2em;
                        text-align: left;
                        padding: 0 20px;

                        .section2-br {
                            display: none;
                        }
                    }

                }
            }
        }
    }

</style>
