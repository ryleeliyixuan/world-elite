<template>
    <div class="app-container">
        <div class="section1-container">
            <el-carousel :interval="2500" arrow="always">
                <el-carousel-item v-for="banner in banners" :key="banner.img">
                    <el-image
                            class="section1-image"
                            :src="banner.img"
                            :alt="banner.alt"
                            v-on:click="select(banner)"
                            fit="cover"
                    ></el-image>
                </el-carousel-item>
            </el-carousel>
        </div>
        <div class="job-recommend-box">
            <h3 class="main-title">职位精选</h3>
            <div class="recommend-job-container">
                <div
                        class="job-item-container"
                        v-for="recommendJob in recommendJobList"
                        :key="recommendJob.id"
                >
                    <el-card
                            shadow="hover"
                            class="link-pointer job-card"
                            v-if="recommendJob.object"
                    >
                        <div class="company-card-inner">

                            <el-link
                                    :href="`/job/${recommendJob.object.id}`"
                                    :underline="false">
                                <h6 style="display: flex;align-items: center">
                                    <span class="job-name">{{ recommendJob.object.name }}</span>
                                    <span class="job-salary">{{
                   recommendJob.object.salary.name
                  }}</span>
                                </h6>
                                <div class="text-gray text-small">
                                    {{
                                    `${
                                    recommendJob.object.city
                                    ? recommendJob.object.city.name
                                    : ""
                                    } /
                                    ${
                                    recommendJob.object.minDegree
                                    ? recommendJob.object.minDegree.name
                                    : ""
                                    }`
                                    }}
                                </div>
                            </el-link>

                            <el-divider></el-divider>
                            <el-link
                                    :href="`/company/${recommendJob.object.companyUser.company.id}`"
                                    :underline="false"
                                    v-if="
                  recommendJob.object.companyUser &&
                  recommendJob.object.companyUser.company
                "
                            >
                                <div class="job-company-container">
                                    <el-image
                                            class="company-logo"
                                            :src="recommendJob.object.companyUser.company.logo"
                                    ></el-image>
                                    <div class="company-info">
                                        <div class="company-name">
                                            {{ recommendJob.object.companyUser.company.name }}
                                        </div>
                                        <div class="text-gray text-small company-tag">
                                            {{
                                            recommendJob.object.companyUser.company.industry
                                            ? recommendJob.object.companyUser.company.industry
                                            .name
                                            : ""
                                            }}
                                            /
                                            {{
                                            recommendJob.object.companyUser.company.stage
                                            ? recommendJob.object.companyUser.company.stage.name
                                            : ""
                                            }}
                                            /
                                            {{
                                            recommendJob.object.companyUser.company.scale
                                            ? recommendJob.object.companyUser.company.scale.name
                                            : ""
                                            }}
                                        </div>
                                    </div>
                                </div>
                            </el-link>
                        </div>
                    </el-card>
                </div>
            </div>
        </div>
        <div class="company-recommend-box">
            <h3 class="main-title">百科推荐</h3>
            <div class="recommend-company-container">
                <div
                        class="company-item-container"
                        v-for="recommendCompany in recommendCompanyList"
                        :key="recommendCompany.id"
                >
                    <el-card
                            shadow="hover"
                            class="link-pointer company-card"
                            v-if="recommendCompany.object"
                    >
                        <el-link
                                :href="`/company/${recommendCompany.object.id}`"
                                :underline="false"
                        >
                            <div class="company-card-inner">
                                <el-image
                                        class="company-logo"
                                        :src="recommendCompany.object.logo"
                                ></el-image>
                                <h6 class="company-name">{{ recommendCompany.object.name }}</h6>
                                <div class="company-tag text-gray text-small">
                  <span v-if="recommendCompany.object.stage"
                  >{{ recommendCompany.object.stage.name }} .</span
                  >
                                    <span v-if="recommendCompany.object.property"
                                    >{{ recommendCompany.object.property.name }} .</span
                                    >
                                    <span v-if="recommendCompany.object.industry"
                                    >{{ recommendCompany.object.industry.name }} .</span
                                    >
                                    <span v-if="recommendCompany.object.scale">{{
                    recommendCompany.object.scale.name
                  }}</span>
                                </div>
                                <div class="company-des text-gray text-small">
                                    {{ recommendCompany.object.synopsis }}
                                </div>
                            </div>
                        </el-link>
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
            ...mapGetters(["token"]),
        },
        data() {
            return {
                recommendCompanyList: [],
                recommendJobList: [],
                homeConfig: {},
                banners: [
                    {
                        img: require("../assets/banner/xiaomi1.jpg"),
                        alt: "小米未来星项目招聘",
                        url: "http://www.myworldelite.com/mi",
                    },
                    {
                        img: require("../assets/banner/vip1.jpg"),
                        alt: "唯品会2021校园招聘",
                        url:
                            "https://recruitment.corp.vipshop.com/wt/VIPS/web/index?brandCode=186838262#/",
                    },
                    {
                        img: require("../assets/banner/qunar1.jpg"),
                        alt: "去哪儿2021校园招聘",
                        url: "https://app.mokahr.com/campus_apply/qunar/4207#/",
                    },
                    {
                        img: require("../assets/banner/kuaishou1.jpg"),
                        alt: "快手2021校园招聘",
                        url: "https://campus.kuaishou.cn/recruit/campus/e/#/campus/index/",
                    },
                ],
            };
        },
        methods: {
            initData() {
                getRecommendList({
                    objectType: 2, // 公司
                    page: 1,
                    limit: 12,
                    sort: "+position",
                }).then((response) => {
                    this.recommendCompanyList = response.data.list;
                    this.$emit("complete");
                });
                getRecommendList({
                    objectType: 1, // 职位
                    page: 1,
                    limit: 9,
                    sort: "+position",
                }).then((response) => {
                    this.recommendJobList = response.data.list;
                });
                getHomeConfig(8).then((response) => {
                    this.homeConfig = response.data;
                });
            },
            select: function (banner) {
                window.open(banner.url);
            },
        },
    };
</script>

<style scoped lang="scss">
    .app-container {
        min-width: 375px;
        min-height: calc(100vh - 477px);

        .section1-container {
            max-width: 1200px;
            max-height: 600px;
            height: 50vw;
            width: calc(100vw - 20px);
            margin: 0 auto 30px;

            .el-carousel {
                height: 100%;

                /deep/ .el-carousel__container {
                    height: 100%;

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

                            .company-name,
                            .company-tag {
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
                            .job-name{
                                display: inline-block;
                                max-width: 240px;
                                overflow: hidden;
                                text-overflow: ellipsis;
                                white-space: nowrap;
                            }

                            .job-salary {
                                color: #dc3545;
                                margin-left: 4px;
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
            .company-recommend-box {
                padding: 30px 15px 0;
            }
        }
    }

    @media screen and (max-width: 420px) {
        .app-container {
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
