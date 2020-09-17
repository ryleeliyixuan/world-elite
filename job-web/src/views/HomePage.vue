<template>
    <div class="app-container">
        <div class="section1-container">
            <div class="section1-left">
                <h1 class="section1-name link-pointer" @click="onDetail">小米未来星</h1>
                <div class="section1-tip">小米集团2021全球校园招聘</div>
                <div class="section1-button-container">
                    <div class="section1-button-detail link-pointer" @click="onDetail">了解详情</div>
                    <div class="section1-button-deliver link-pointer" @click="onDelivery">马上投递</div>
                </div>
            </div>
            <div class="section1-right">
                <img class="section1-image link-pointer"  src="../assets/item1.jpg" @click="onDetail"/>
            </div>
        </div>
        <div class="company-recommend-box">
            <h3 class="main-title">百科推荐</h3>
            <div class="recommend-company-container">
                <div class="company-item-container" v-for="recommendCompany in recommendCompanyList" :key="recommendCompany.id">
                    <el-card shadow="hover" class="link-pointer company-card" v-if="recommendCompany.object">
                        <el-link :href="`/company/${recommendCompany.object.id}`" :underline="false">
                            <div class="company-card-inner">
                                <el-image class="company-logo" :src="recommendCompany.object.logo"></el-image>
                                <h6 class="company-name">{{recommendCompany.object.name}}</h6>
                                <div class="company-tag text-gray text-small">
                                    <span v-if="recommendCompany.object.stage">{{recommendCompany.object.stage.name}} .</span>
                                    <span v-if="recommendCompany.object.property">{{recommendCompany.object.property.name}} .</span>
                                    <span v-if="recommendCompany.object.industry">{{recommendCompany.object.industry.name}} .</span>
                                    <span v-if="recommendCompany.object.scale">{{recommendCompany.object.scale.name}}</span>
                                </div>
                                <div class="company-des text-gray text-small">{{recommendCompany.object.synopsis}}</div>
                            </div>
                        </el-link>
                    </el-card>
                </div>
            </div>
        </div>
        <div class="job-recommend-box">
            <h3 class="main-title">职位精选</h3>
            <div class="recommend-job-container">
                <div class="job-item-container" v-for="recommendJob in recommendJobList" :key="recommendJob.id">
                    <el-card shadow="hover" class="link-pointer job-card" v-if="recommendJob.object">
                        <div class="company-card-inner">
                            <el-link :href="`/job/${recommendJob.object.id}`" :underline="false">
                                <h6> {{recommendJob.object.name}}
                                    <span class="job-salary">{{`${recommendJob.object.minSalary} - ${recommendJob.object.maxSalary}K`}}</span>
                                </h6>
                                <div class="text-gray text-small">{{`${recommendJob.object.city? recommendJob.object.city.name : ''} /
                                    ${recommendJob.object.minDegree?recommendJob.object.minDegree.name:''}`}}
                                </div>
                            </el-link>
                            <el-divider></el-divider>
                            <el-link :href="`/company/${recommendJob.object.companyUser.company.id}`" :underline="false"
                                     v-if="recommendJob.object.companyUser && recommendJob.object.companyUser.company">
                                <div class="job-company-container">
                                    <el-image class="company-logo" :src="recommendJob.object.companyUser.company.logo"></el-image>
                                    <div class="company-info">
                                        <div class="company-name">{{recommendJob.object.companyUser.company.name}}</div>
                                        <div class="text-gray text-small company-tag">
                                            {{recommendJob.object.companyUser.company.industry?recommendJob.object.companyUser.company.industry.name:''}} /
                                            {{recommendJob.object.companyUser.company.stage?recommendJob.object.companyUser.company.stage.name:''}} /
                                            {{recommendJob.object.companyUser.company.scale?recommendJob.object.companyUser.company.scale.name:''}}
                                        </div>
                                    </div>
                                </div>
                            </el-link>
                        </div>
                    </el-card>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import {getRecommendList} from "@/api/recommend_api";
    import {getHomeConfig} from "@/api/config_api";
    import {mapGetters} from "vuex";

    export default {
        name: "HomePage",
        created() {
            this.initData();
        },
        computed: {
            ...mapGetters(["token"])
        },
        data() {
            return {
                recommendCompanyList: [],
                recommendJobList: [],
                homeConfig: {}
            };
        },
        methods: {
            initData() {
                getRecommendList({
                    objectType: 2, // 公司
                    page: 1,
                    limit: 12,
                    sort: "+position"
                }).then(response => {
                    this.recommendCompanyList = response.data.list;
                    this.$emit("complete");
                });
                getRecommendList({
                    objectType: 1, // 职位
                    page: 1,
                    limit: 9,
                    sort: "+position"
                }).then(response => {
                    this.recommendJobList = response.data.list;
                });
                getHomeConfig(8).then(response => {
                    this.homeConfig = response.data;
                });
            },
            onDetail(){
                this.$router.push("/mi");
            },
            onDelivery(){
                window.open('https://app.mokahr.com/campus_apply/xiaomi/26250#/jobs')
            }
        }
    };
</script>

<style scoped lang="scss">
    .app-container {
        min-width: 375px;

        .section1-container {
            max-width: 1140px;
            height: auto;
            display: flex;
            align-items: center;
            margin: 0 auto 30px;

            .section1-left {
                min-width: 300px;

                .section1-name {
                    font-size: 60px;
                    font-weight: bold;
                    color: #1a1a1a;
                }

                .section1-tip {
                    font-size: 20px;
                    color: #dddddd;
                    font-weight: bold;
                }

                .section1-button-container {
                    display: flex;
                    align-items: center;
                    margin-top: 33px;

                    .section1-button-detail {
                        padding: 5px 20px;
                        background-color: #fe660b;
                        color: #ffffff;
                        border-radius: 17px;
                    }

                    .section1-button-deliver {
                        padding: 5px 20px;
                        background-color: #0062ff;
                        color: #ffffff;
                        border-radius: 17px;
                        margin-left: 17px;

                    }
                }
            }

            .section1-right {
                flex: 1;
                margin-left: 60px;

                .section1-image {
                    width: 100%;
                    height: auto;
                }
            }
        }

        .company-recommend-box {
            background: #f7fbfd;
            padding: 50px;

            .main-title {
                margin-bottom: 40px;
                color: #333;
                text-align: center;
            }

            .recommend-company-container {
                max-width: 1140px;
                margin: 0 auto;
                display: flex;
                flex-wrap: wrap;
                justify-content: center;

                .company-item-container {
                    padding: 10px;

                    .company-card {
                        border: none;
                        text-align: center;

                        .company-card-inner {
                            width: 224px;

                            .company-logo {
                                width: 100px;
                                height: 100px;
                            }

                            .company-name, .company-tag {
                                text-align: center;
                                overflow: hidden;
                                text-overflow: ellipsis;
                                white-space: nowrap;
                            }

                            .company-des {
                                text-align: center;
                                overflow: hidden;
                                text-overflow: ellipsis;
                                white-space: nowrap;
                                margin-top: 8px;
                            }
                        }
                    }
                }
            }
        }

        .job-recommend-box {
            background: #f7fbfd;
            padding: 0 50px 20px;

            .main-title {
                margin-bottom: 40px;
                color: #333;
                text-align: center;
            }

            .recommend-job-container {
                max-width: 1140px;
                margin: 0 auto;
                display: flex;
                flex-wrap: wrap;
                justify-content: center;

                .job-item-container {
                    padding: 10px;

                    .job-card {
                        border: none;
                        text-align: center;

                        .company-card-inner {
                            width: 317px;

                            .job-salary {
                                color: #dc3545;
                            }

                            .job-company-container {
                                display: flex;
                                align-items: center;

                                .company-logo {
                                    width: 50px;
                                    height: 50px;
                                }

                                .company-info {
                                    margin-left: 5px;
                                    display: flex;
                                    flex-direction: column;
                                    align-items: center;
                                    flex: 1;

                                    .company-name {
                                        margin-bottom: 4px;
                                        overflow: hidden;
                                        text-overflow: ellipsis;
                                        white-space: nowrap;
                                    }

                                    .company-tag {
                                        overflow: hidden;
                                        text-overflow: ellipsis;
                                        white-space: nowrap;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @media screen and (max-width: 850px) {
        .app-container {
            .section1-container {
                flex-direction: column;
                align-items: center;
                .section1-left {
                    max-width: 400px;
                    display: flex;
                    flex-direction: column;
                    align-items: center;
                    margin-bottom: 50px;
                }

                .section1-right {
                    max-width: 400px;
                    margin: 0;
                    width: 100%;
                    padding: 0 30px;
                }
            }

            .company-recommend-box {
                padding: 30px 15px 0;
            }
        }
    }

    @media screen and (max-width: 420px) {
        .app-container {
            .section1-container {
                width: 100%;
                display: flex;
                flex-direction: column;
                align-items: center;
                justify-content: center;

                .section1-left {
                    width: 100%;
                    display: flex;
                    flex-direction: column;
                    align-items: center;

                    .section1-name {
                        font-size: 54px;
                    }
                }
            }

            .company-recommend-box {
                padding: 15px;

                .recommend-company-container {
                    .company-item-container {
                        width: 100%;
                        padding: 0;
                    }
                }
            }
        }
    }
</style>
