<template>
    <div class="app-container">
        <div class="session1-container" v-if="job">
            <div class="session1-left">
                <div class="text-gray text-small" v-if="job.companyUser && job.companyUser.company">{{job.companyUser.company.name}}招聘</div>
                <div class="session1-job-name">{{job.name}}
                    <span class="salary-text">{{`${job.minSalary}K - ${job.maxSalary}K`}}{{job.salaryMonths? ` × ${job.salaryMonths}个月` : ''}}</span>
                </div>
                <div class="mt-2">{{job.city? job.city.name: ''}} / {{job.minDegree? job.minDegree.name: ''}} / {{job.jobType? job.jobType.name: ''}}</div>
                <div class="mt-2 text-gray text-small">{{job.time}}</div>
            </div>
            <div class="session1-right">
                <el-button type="primary"
                           :icon="job.favoriteFlag === 1? 'el-icon-star-on' : 'el-icon-star-off'"
                           plain
                           :loading="favoriteLoading"
                           @click="handleFavorite">
                    {{job.favoriteFlag === 1? '已收藏': '收藏岗位'}}
                </el-button>
                <el-button
                        type="primary"
                        :loading="applyLoading"
                        icon="el-icon-s-promotion"
                        @click="handleAppleJob"
                        v-bind:disabled="job.applyFlag === 1">
                    {{job.applyFlag === 1? '已申请' : '申请岗位'}}
                </el-button>
            </div>
        </div>
        <el-divider/>
        <div class="session2-container" v-if="job">
            <div class="session2-container-bottom">
                <el-button type="primary"
                           :icon="job.favoriteFlag === 1? 'el-icon-star-on' : 'el-icon-star-off'"
                           plain
                           :loading="favoriteLoading"
                           @click="handleFavorite">
                    {{job.favoriteFlag === 1? '已收藏': '收藏岗位'}}
                </el-button>
                <el-button
                        type="primary"
                        :loading="applyLoading"
                        icon="el-icon-s-promotion"
                        @click="handleAppleJob"
                        v-bind:disabled="job.applyFlag === 1">
                    {{job.applyFlag === 1? '已申请' : '申请岗位'}}
                </el-button>
            </div>
            <div class="session2-container-left">
                <div v-html="job.description"></div>
                <div v-if="job.address" class="session2-job-address">
                    <h5>工作地址</h5>
                    <span>
                        {{job.address}}
                    </span>
                </div>
            </div>
            <div class="session2-container-right" v-if="job.companyUser && job.companyUser.company">
                <el-link :href="`/company/${job.companyUser.company.id}`" :underline="false">
                    <div class="session2-logo-container">
                        <el-image :src="job.companyUser.company.logo" class="session2-logo"></el-image>
                        <div class="company-text">{{job.companyUser.company.name}}</div>
                    </div>
                </el-link>
                <div v-if="job.companyUser.company.industry" class="text-gray text-small mt-4">
                    <i class="el-icon-menu mr-2"></i>
                    {{job.companyUser.company.industry.name}}
                </div>
                <div v-if="job.companyUser.company.stage && job.companyUser.company.property" class="text-gray text-small mt-2">
                    <i class="el-icon-collection-tag mr-2"></i>
                    {{job.companyUser.company.property.name}} . {{job.companyUser.company.stage.name}}
                </div>
                <div v-if="job.companyUser.company.scale" class="text-gray text-small mt-2">
                    <i class="el-icon-user mr-2"></i>
                    {{job.companyUser.company.scale.name}}
                </div>
                <div v-if="job.companyUser.company.homepage" class="text-gray text-small mt-2">
                    <i class="el-icon-s-home mr-2"></i>
                    <el-link :href="companyLink" target="_blank" :underline="false">{{companyLinkName}}</el-link>
                </div>
            </div>
        </div>
        <div class="mb-4">
            <h5 class="mt-4 mb-4">分享该职位</h5>
            <share :config="shareConfig"></share>
        </div>
    </div>
</template>

<script>
    import Vue from "vue";
    import VueAMap from "vue-amap";
    import {getJobInfo, applyJob} from "@/api/job_api";
    import {doFavorite} from "@/api/favorite_api";
    import {setPageTitle} from "@/utils/setting";
    import Share from "vue-social-share";
    import "vue-social-share/dist/client.css";

    Vue.use(Share);
    Vue.use(VueAMap);

    VueAMap.initAMapApiLoader({
        key: process.env.VUE_APP_AMAP_KEY,
        v: "1.4.4"
    });

    export default {
        name: "JobPage",
        data() {
            return {
                job: undefined,
                favoriteForm: {
                    objectId: undefined,
                    type: 1,
                    favorite: false
                },
                mapZoom: 14,
                mapDialogVisible: false,
                favoriteLoading: false,
                applyLoading: false,
                shareConfig: {
                    source: "",
                    title: "",
                    description: "",
                    image: "",
                    sites: ["wechat", "qq", "weibo", "google", "facebook", "twitter"]
                }
            };
        },
        created() {
            this.initData();
        },
        watch: {
            $route() {
                this.initData();
            }
        },
        computed: {
            companyLink() {
                return this.job.companyUser.company.homepage.startsWith("http")
                    ? this.job.companyUser.company.homepage
                    : "http://" + this.job.companyUser.company.homepage;
            },
            companyLinkName() {
                return this.job.companyUser.company.homepage.replace(/http(s)?:\/\//, "");
            }
        },
        methods: {
            initData() {
                const jobId = this.$route.params.id;
                getJobInfo(jobId).then(response => {
                    this.job = response.data;
                    const title = `${this.job.name} - ${
                        this.job.companyUser.company ? this.job.companyUser.company.name : ""
                    }`;
                    setPageTitle(title);
                    this.shareConfig.title = title;
                    this.shareConfig.description = this.job.description;
                    this.favoriteForm.objectId = this.job.id;
                    this.favoriteForm.favorite = this.job.favoriteFlag == 1;
                    this.$emit("complete");
                });
            },
            handleFavorite() {
                this.favoriteLoading = true;
                this.favoriteForm.favorite = !this.favoriteForm.favorite;
                doFavorite(this.favoriteForm)
                    .then(() => {
                        this.job.favoriteFlag = this.favoriteForm.favorite ? 1 : 0;
                        this.$message("操作成功");
                    })
                    .finally(() => {
                        this.favoriteLoading = false;
                    });
            },
            handleAppleJob() {
                this.applyLoading = true;
                applyJob(this.job.id)
                    .then(() => {
                        this.job.applyFlag = 1;
                        this.$message("申请成功");
                    })
                    .finally(() => {
                        this.applyLoading = false;
                    });
            }
        }
    };
</script>

<style scoped lang="scss">
    .app-container {
        max-width: 1140px;
        margin: 0 auto;
        padding: 0 20px;

        .session1-container {
            display: flex;
            align-items: center;

            .session1-left {
                display: flex;
                flex-direction: column;
                flex: 2;

                .session1-job-name {
                    font-size: 32px;
                    margin: 16px 24px 8px 0;

                    span {
                        color: #dc3545;
                        font-size: 20px;
                    }
                }
            }

            .session1-right {
                display: flex;
                flex: 1;
                margin-left: 30px;
            }
        }

        .session2-container {
            display: flex;

            .session2-container-left {
                display: flex;
                flex-direction: column;
                flex: 2;

                .session2-job-address {
                    margin-top: 10px;

                    span {
                        line-height: 50px;
                        margin-top: 10px;
                    }
                }
            }

            .session2-container-bottom {
                display: none;
            }

            .session2-container-right {
                flex: 1;
                display: flex;
                flex-direction: column;
                margin-left: 30px;

                .el-link {
                    text-align: left;
                    display: inline;

                    .session2-logo-container {
                        display: flex;
                        align-items: flex-end;
                        justify-content: flex-start;

                        .session2-logo {
                            width: 100px;
                        }

                        .company-text {
                            font-size: 18px;
                            padding-left: 12px;
                        }
                    }
                }
            }
        }
    }

    @media screen and (max-width: 850px) {
        .app-container {
        }
    }

    @media screen and (max-width: 410px) {
        .app-container {
            .session1-container {
                .session1-right {
                    display: none;
                }
            }

            .session2-container {
                flex-direction: column-reverse;

                .session2-container-bottom {
                    display: flex;
                    justify-content: center;
                    margin: 30px;
                }

                .session2-container-right {
                    margin-left: 0;
                    margin-bottom: 30px;

                    .el-link {

                        .session2-logo-container {
                            display: flex;
                            align-items: flex-end;
                            justify-content: center;

                            .session2-logo {
                                width: 100px;
                            }

                            .company-text {
                                font-size: 18px;
                                padding-left: 12px;
                            }
                        }
                    }
                }
            }
        }
    }


    .map-box {
        height: 400px;
    }
</style>
