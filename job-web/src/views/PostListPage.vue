<template>
    <div class="app-container">
        <div class="section1-container" style="margin-bottom: 15px;">
            <el-select v-model="listQuery.cityIds"
                       multiple
                       filterable
                       clearable
                       placeholder="工作地点"
                       @change="handleFilter"
                       size="mini"
                       class="section1-select">
                <el-option v-for="item in cityOptions" :key="item.id" :label="item.name" :value="item.id"></el-option>
            </el-select>
            <el-select v-model="listQuery.salaryRangeIds"
                       multiple
                       filterable
                       clearable
                       placeholder="薪资"
                       @change="handleFilter"
                       size="mini"
                       class="section1-select">
                <el-option v-for="item in salaryRangeOptions" :key="item.id" :label="item.name"
                           :value="item.id"></el-option>
            </el-select>
            <el-select v-model="listQuery.degreeIds"
                       multiple
                       filterable
                       clearable
                       placeholder="学历要求"
                       @change="handleFilter"
                       size="mini"
                       class="section1-select">
                <el-option v-for="item in degreeOptions" :key="item.id" :label="item.name"
                           :value="item.id"></el-option>
            </el-select>
            <el-select v-model="listQuery.companyIndustryIds"
                       multiple
                       filterable
                       clearable
                       placeholder="岗位分类"
                       @change="handleFilter"
                       size="mini"
                       class="section1-select">
                <el-option v-for="item in companyIndustryOptions" :key="item.id" :label="item.name"
                           :value="item.id"></el-option>
            </el-select>
            <el-input style="width: auto;" v-model="queryStr" placeholder="请输入关键词" size="small"></el-input>
            <el-button type="primary" @click="handleSearch" size="mini" style="margin:0 25px; height: 30px;">搜索
            </el-button>
            <el-link @click="clearOptions" :underline="false" style="color: #b4bbc5;">清空搜索条件</el-link>
        </div>

        <div style="display: inline-block;">
            <el-tabs v-model="listQuery.recruitId"
                     @tab-click="handleFilter"
                     style="padding-left: 25px;">
                <el-tab-pane :label="'全部 ' + this.recruitCountResult.all"></el-tab-pane>
                <el-tab-pane :label="'校招 ' + this.recruitCountResult.school" name="154"></el-tab-pane>
                <el-tab-pane :label="'社招 ' + this.recruitCountResult.community" name="155"></el-tab-pane>
                <el-tab-pane :label="'急招 ' + this.recruitCountResult.urgency" name="170"></el-tab-pane>
                <el-tab-pane :label="'热招 ' + this.recruitCountResult.hot" name="171"></el-tab-pane>
                <el-tab-pane :label="'内推 ' + this.recruitCountResult.inner" name="172"></el-tab-pane>
            </el-tabs>
        </div>

      <div class="sort-options">
            <el-link :underline="false" style="color: #599EF8;">最新</el-link>
            /
            <el-link :underline="false">发布顺序</el-link>
        </div>

        <div class="section3-container">
            <el-card v-for="job in pageResult.list"
                     class="post-item" style="display: inline-block;"
                     :body-style="{ padding: '0px' }"
                     :key="job.id">
                <el-image style="width: 100%;height: 210px;" :src="job.companyUser.company.logo"></el-image>
                <div class="type-logo-box">
                    <div v-if="job.recruitType.value === '1'" class="type-logo school">校 招</div>
                    <div v-if="job.recruitType.value === '5'" class="type-logo inner">内 推</div>
                    <div v-if="job.recruitType.value === '4'" class="type-logo hot">热 招</div>
                    <div v-if="job.recruitType.value === '3'" class="type-logo urgency">急 招</div>
                </div>
                <div class="mid" style="display: block">
                    <span v-text="job.name" class="post-name"></span>
                    <span v-text="job.companyUser.company.synopsis" class="post-desc"></span>
                    <div class="mid-2">
                        <span v-text="job.salary.value" class="post-salary"></span>.
                        &nbsp;
                        <span v-text="job.address" class="post-addr"></span>.
                        &nbsp;
                        <span v-text="job.minDegree.name" class="post-degree"></span>
                    </div>
                </div>
                <span class="post-line"></span>
                <div class="bot">
                    <span class="hr-logo"><i style="font-size: 30px;" class="el-icon-user"></i></span>
                    <span v-text="job.companyUser.post" class="recruiter"></span>
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
    import {getJobRecruitCount, searchJob} from "@/api/job_api";
    import Pagination from "@/components/Pagination";
    import {mapGetters} from "vuex";

    import {formatListQuery, parseListQuery} from "@/utils/common";
    import {getRecommendList} from "@/api/recommend_api";

    export default {
        name: "JobListPage",
        components: {Pagination},
        computed: {
            ...mapGetters(["keyword"])
        },
        data() {
            return {
                //companyId: "1253552062077882368", // 1254648303499104256
                listQuery: {
                    companyId: undefined,
                    keyword: "",
                    salaryRangeIds: [],
                    jobTypes: [],
                    cityIds: [],
                    companyIndustryIds: [],
                    companyScaleIds: [],
                    companyStageIds: [],
                    degreeIds: [],
                    categoryIds: [],
                    recruitId: '0',
                    page: 1,
                    limit: 20
                },
                queryStr: "",
                total: 0,
                pageResult: {},
                recruitCountResult: {all: 0, school: 0, community: 0, urgency: 0, hot: 0, inner: 0},
                cityOptions: [],
                salaryRangeOptions: [],
                companyScaleOptions: [],
                companyIndustryOptions: [],
                jobTypeOptions: [],
                degreeOptions: [],
                showNoResult: false
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
                console.log(this.$route.params.id);
                this.listQuery.companyId = this.$route.params.id;
                listByType(1).then(
                    response => (this.degreeOptions = response.data.list)
                );
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
            handleSearch() {
                if (this.isHomeListPage()) {
                    this.$store.commit("setting/SET_KEYWORD", this.queryStr);
                } else {
                    console.log("asdsadas: " + this.listQuery.companyId)
                    console.log("dasdasda: " + this.queryStr);
                    this.listQuery.keyword = this.queryStr;
                    this.$router.push({
                        path: "/post-list/" + this.listQuery.companyId,
                        query: {searchForm: this.listQuery}
                    });
                }
            },
            isHomeListPage() {
                const cur_path = this.$route.path;
                return cur_path === "/job-list" || cur_path === "/activity-list" || cur_path === "/wiki-card" || cur_path === "/wiki-list";
            },
            getList() {
                this.showNoResult = false;
                parseListQuery(this.$route.query, this.listQuery);
                searchJob(this.listQuery).then(response => {
                    if (!response.data.list || response.data.list.length === 0) {
                        this.showNoResult = true;
                        this.total = 20;
                        getRecommendList({
                            objectType: 1, // 职位
                            page: 1,
                            limit: 20,
                            sort: "+position"
                        }).then(response => {
                            this.pageResult.list = response.data.list.map(item => item.object);
                            this.total = response.data.total;
                            this.$emit("complete");
                        });
                    } else {
                        this.pageResult = response.data;
                        this.total = this.pageResult.total;
                        this.$emit("complete");
                    }
                });

              getJobRecruitCount(this.listQuery.companyId).then(response => {
                  for ( let i = 0; i < response.data.length; i++) {
                    let recruitType = response.data[i].dictVo.name;
                    if (recruitType === "校招") {
                        this.recruitCountResult.school = response.data[i].count;
                    } else if (recruitType === "社招") {
                        this.recruitCountResult.community = response.data[i].count;
                    } else if (recruitType === "急招") {
                        this.recruitCountResult.urgency = response.data[i].count;
                    } else if (recruitType === "热招") {
                        this.recruitCountResult.hot = response.data[i].count;
                    } else if (recruitType === "内推") {
                        this.recruitCountResult.inner = response.data[i].count;
                    }
                    this.recruitCountResult.all = this.recruitCountResult.school
                                                  + this.recruitCountResult.inner
                                                  + this.recruitCountResult.community
                                                  + this.recruitCountResult.urgency
                                                  + this.recruitCountResult.hot;
                  }
                  // console.log("getJobRecruitCount this.recruitCountResult: " + JSON.stringify(this.recruitCountResult));
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
            },
            clearOptions() {
                let options = document.getElementsByClassName(".el-select__tags");
                for (let i = 0; i < options.length; i++) {
                    options.item(i).remove();
                }
                this.queryStr = '';
            }
        }
    };

</script>


<style scoped lang="scss">
    .app-container {
        max-width: 1140px;
        margin: 0 auto;
        padding: 0 20px;
        min-height: calc(100vh - 477px);

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
            margin-top: 30px;
            width: 100%;
            overflow-x: auto;
        }

        .section3-container {

            display: flex;
            flex-wrap: wrap;

            .type-logo-box {
                float: right;
                position: relative;
                bottom: 200px;
                color: white;
                font-size: 12px;
                font-weight: bold;

                .type-logo {
                    margin-bottom: 4px;
                    width: 50px;
                    height: 20px;
                    line-height: 20px;
                    text-align: center;
                }


                .school {
                    background-color: #36A9CE;
                }

                .inner {
                    background-color: #70B603;
                }
                .hot {
                    background-color: #F59A23;
                }
                .urgency {
                    background-color: #D9001B;
                }

            }

            .bot {
                margin-top: 10px;

                .hr-logo {
                    display: inline-block;
                    font-size: 10px;
                    margin-left: 10px;
                }

                .recruiter {
                    display: inline-block;
                    font-size: 13px;
                    color: #CCCCCC;
                    position: relative;
                    left: 10px;
                    top: -5px;
                }
            }
            .mid:after {
                margin-top: 12px;
                content: '';
                height: 1px;
                display: block;
                width: 100%;
                background-color: #E4E4E4;
            }

            .mid-2 {
              color: #CCCCCC;
              font-size: 12px;
              margin-left: 15px;

              .post-addr {
                display: inline-block;

                vertical-align: top;
                max-width: 60px;
                white-space: nowrap;
                overflow: hidden;
                text-overflow:ellipsis;
              }
            }

            .post-desc {
                display: block;
                font-size: 12px;
                color: #CCCCCC;
                margin-left: 15px;
                margin-bottom: 10px;
                margin-top: 2px;
                max-width: 150px;
                min-height: 17.6px;

                white-space: nowrap;
                overflow: hidden;
                text-overflow:ellipsis;
            }
            .post-name {
                display: block;
                font-weight: bold;
                font-size: 13px;
                margin-top: 5px;
                margin-bottom: 5px;
                margin-left: 15px;
                color: #757371;
                max-width: 150px;

                white-space: nowrap;
                overflow: hidden;
                text-overflow:ellipsis;
            }
            .post-item {
                min-width: 250px;
                max-width: 250px;
                height: 350px;
                flex: 1;
                margin: 0 10px 25px;
                cursor: pointer;
            }
            .post-item:hover {
                box-shadow: 4px 4px 4px rgba(116, 185, 255,0.3);
                top: -2px;
            }
            .time {
                font-size: 13px;
                color: #999;
            }
            .bottom {
                margin-top: 13px;
                line-height: 12px;
            }
            .button {
                padding: 0;
                float: right;
            }
            .image {
                width: 100%;
                display: block;
            }
            .clearfix:before,
            .clearfix:after {
                display: table;
                content: "";
            }
            .clearfix:after {
                clear: both
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
<style>
    .el-tabs__nav-wrap::after {
        opacity: 0;
    }

    .sort-options {
        display: inline-block;
        vertical-align: top;
        position: relative;
        top: 10px;
        left: 610px;
    }


</style>
