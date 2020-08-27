<template>
    <div class="app-container">
        <div class="section1-container">
            <el-select v-model="listQuery.cityIds"
                       multiple
                       filterable
                       clearable
                       placeholder="城市"
                       @change="handleFilter"
                       size="small"
                       class="section1-select">
                <el-option v-for="item in cityOptions" :key="item.id" :label="item.name" :value="item.id"></el-option>
            </el-select>
            <el-select v-model="listQuery.salaryRangeIds"
                       multiple
                       filterable
                       clearable
                       placeholder="薪资"
                       @change="handleFilter"
                       size="small"
                       class="section1-select">
                <el-option v-for="item in salaryRangeOptions" :key="item.id" :label="item.name" :value="item.id"></el-option>
            </el-select>
            <el-select v-model="listQuery.companyIndustryIds"
                       multiple
                       filterable
                       clearable
                       placeholder="行业"
                       @change="handleFilter"
                       size="small"
                       class="section1-select">
                <el-option v-for="item in companyIndustryOptions" :key="item.id" :label="item.name" :value="item.id"></el-option>
            </el-select>
            <el-select v-model="listQuery.companyScaleIds"
                       multiple
                       filterable
                       clearable
                       placeholder="公司规模"
                       @change="handleFilter"
                       size="small"
                       class="section1-select">
                <el-option v-for="item in companyScaleOptions" :key="item.id" :label="item.name" :value="item.id"></el-option>
            </el-select>
            <el-select v-model="listQuery.jobTypes"
                       multiple
                       filterable
                       clearable
                       placeholder="工作类型"
                       @change="handleFilter"
                       size="small"
                       class="section1-select">
                <el-option v-for="item in jobTypeOptions" :key="item.id" :label="item.name" :value="item.id"></el-option>
            </el-select>
        </div>
        <div class="section2-container">
            <pagination v-show="total"
                        :total="total"
                        :page.sync="listQuery.page"
                        :limit.sync="listQuery.limit"
                        @pagination="handleRouteList"/>
        </div>

        <div class="section3-container" v-if="pageResult.list && pageResult.list.length !== 0">
            <el-card shadow="hover"
                     v-for="job in pageResult.list"
                     :key="job.id"
                     class="section3-item-container"
                     @click.native="openJobDetail(job.id)">
                <div class="section3-left-container">
                    <h6 class="section3-job-name">{{job.name}}</h6>
                    <div>
                        <b class="section3-salary">{{`${job.minSalary} - ${job.maxSalary}K`}}{{job.salaryMonths?` × ${job.salaryMonths}`:''}}</b>
                        <span class="section3-city-degree text-gray text-small">{{`${job.city? job.city.name : ''} / ${job.minDegree?job.minDegree.name:''}`}}</span>
                    </div>
                </div>
                <div class="section3-right-container" v-if="job.companyUser && job.companyUser.company">
                    <div class="section3-company-info-container">
                        <h6 class="section3-company-name">{{job.companyUser.company.name}}</h6>
                        <div class="text-gray text-small">
                            {{job.companyUser.company.industry?job.companyUser.company.industry.name + ' / ':''}}
                            {{job.companyUser.company.stage?job.companyUser.company.stage.name + ' / ':''}}
                            {{job.companyUser.company.scale?job.companyUser.company.scale.name:''}}
                        </div>
                    </div>
                    <el-link :href="`/company/${job.companyUser.company.id}`">
                        <el-image class="section3-logo" :src="job.companyUser.company.logo"
                                  v-if="job.companyUser.company.logo && job.companyUser.company.logo !== ''">
                            <div slot="error" class="section3-logo-error">
                                <i class="el-icon-picture-outline"></i>
                            </div>
                        </el-image>
                    </el-link>
                </div>
            </el-card>
        </div>
        <div class="section2-container">
            <pagination v-show="total"
                        :total="total"
                        :page.sync="listQuery.page"
                        :limit.sync="listQuery.limit"
                        @pagination="handleRouteList"/>
        </div>
    </div>
</template>

<script>
    import {listByType} from "@/api/dict_api";
    import {searchJob} from "@/api/job_api";
    import Pagination from "@/components/Pagination";
    import {mapGetters} from "vuex";

    import {formatListQuery, parseListQuery} from "@/utils/common";

    export default {
        name: "JobListPage",
        components: {Pagination},
        computed: {
            ...mapGetters(["keyword"])
        },
        data() {
            return {
                listQuery: {
                    keyword: "",
                    salaryRangeIds: [],
                    jobTypes: [],
                    cityIds: [],
                    companyIndustryIds: [],
                    companyScaleIds: [],
                    companyStageIds: [],
                    degreeIds: [],
                    categoryIds: [],
                    page: 1,
                    limit: 10
                },
                total: 0,
                pageResult: 0,
                cityOptions: [],
                salaryRangeOptions: [],
                companyScaleOptions: [],
                companyIndustryOptions: [],
                jobTypeOptions: []
            };
        },
        created() {
            this.initData();
            this.getList();
        },
        watch: {
            $route() {
                this.getList();
            },
            keyword() {
                this.listQuery.keyword = this.keyword;
                this.handleRouteList();
            }
        },
        methods: {
            initData() {
                listByType(2).then(response => (this.cityOptions = response.data.list)).catch(function (err) {
                    console.log(err)
                });
                listByType(5).then(
                    response => (this.companyScaleOptions = response.data.list)
                );
                listByType(6).then(
                    response => (this.companyIndustryOptions = response.data.list)
                );
                listByType(8).then(
                    response => (this.jobTypeOptions = response.data.list)
                );
                listByType(9).then(
                    response => (this.salaryRangeOptions = response.data.list)
                );
            },
            handleFilter() {
                this.listQuery.page = 1;
                this.handleRouteList();
            },
            getList() {
                parseListQuery(this.$route.query, this.listQuery);
                searchJob(this.listQuery).then(response => {
                    this.pageResult = response.data;
                    this.total = this.pageResult.total;
                });
            },

            handleRouteList() {
                this.$router.replace({
                    path: this.$route.path,
                    query: formatListQuery(this.listQuery)
                });
            },
            openJobDetail(id) {
                this.$router.push(`/job/${id}`);
            }
        }
    };
</script>

<style scoped lang="scss">
    .app-container {
        max-width: 1140px;
        margin: 0 auto;
        padding: 0 20px;

        .section1-container {
            min-width: 335px;
            display: flex;
            flex-wrap: wrap;

            .section1-select {
                flex: 1;
                min-width: 130px;
                margin: 0 10px 10px;
                height: 100%;
            }
        }

        .section2-container {
            width: 100%;
            overflow-x: auto;
        }

        .section3-container {
            min-width: 335px;
            .section3-item-container {
                margin-bottom: 10px;
                cursor: pointer;

                /deep/ .el-card__body {
                    display: flex;
                }
            }

            .section3-left-container {
                flex: 1;
                min-width: 290px;

                .section3-job-name {
                    margin: 0 0 4px 0;
                }

                .section3-salary {
                    color: #dc3545;
                }

                .section3-city-degree {
                    margin-left: 16px;
                }
            }

            .section3-right-container {
                flex: 1;
                min-width: 290px;
                display: flex;
                justify-content: space-between;

                .section3-company-info-container {
                    .section3-company-name {
                        margin: 0 0 4px 0;
                    }
                }

                .section3-logo {
                    width: 48px;

                    /deep/ .section3-logo-error {
                        width: 48px;
                        text-align: center;
                        font-size: 25px;
                    }
                }
            }
        }
    }
    ::-webkit-scrollbar {
        display: none;
    }

    @media screen and (max-width: 850px) {
        .app-container {
            .section3-container {
                .section3-item-container {
                    /deep/ .el-card__body {
                        display: flex;
                        flex-direction: column;
                        align-items: center;

                        .section3-left-container {
                            margin-bottom: 10px;
                        }
                    }
                }
            }
        }
    }

    @media screen and (max-width: 410px) {
        .app-container {

        }
    }
</style>

