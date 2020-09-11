<template>
    <loading :loading="loading">
        <div :class="['app-container',{'vh-100':loading}]" v-if="resumeType===1">
            <div class="mt-3">
                <el-row :gutter="10">
                    <el-col :span="6">
                        <el-select v-model="resumeType" placeholder="简历类型" @change="handleResumeType" class="w-100" size="small">
                            <el-option v-for="item in resumeTypeOptions"
                                       :key="item.value"
                                       :label="item.name"
                                       :value="item.value"
                                       class="w-100"
                                       size="small">
                            </el-option>
                        </el-select>
                    </el-col>
                    <el-col :span="6">
                        <el-input v-model="listQuery.name"
                                  clearable
                                  @change="handleFilter"
                                  class="w-100"
                                  placeholder="名字"
                                  size="small">
                        </el-input>
                    </el-col>

                    <el-col :span="6">
                        <el-select
                                v-model="listQuery.degreeIds"
                                clearable
                                multiple
                                placeholder="学历"
                                @change="handleFilter"
                                class="w-100"
                                size="small">
                            <el-option v-for="item in degreeOptions"
                                       :key="item.id"
                                       :label="item.name"
                                       :value="item.id">
                            </el-option>
                        </el-select>
                    </el-col>
                    <el-col :span="6">
                        <el-select
                                v-model="listQuery.cityIds"
                                multiple
                                clearable
                                filterable
                                placeholder="意向城市"
                                @change="handleFilter"
                                class="w-100"
                                size="small">
                            <el-option
                                    v-for="item in cityOptions"
                                    :key="item.id"
                                    :label="item.name"
                                    :value="item.id">
                            </el-option>
                        </el-select>
                    </el-col>
                </el-row>
            </div>
            <div class="mt-3">
                <el-row :gutter="10">
                    <el-col :span="6">
                        <el-cascader
                                placeholder="意向职位"
                                :show-all-levels="false"
                                :options="jobCategoryOptions"
                                :props="jobCategoryProps"
                                filterable
                                clearable
                                v-model="listQuery.categoryIds"
                                @change="handleFilter"
                                class="w-100"
                                size="small">
                        </el-cascader>
                    </el-col>
                    <el-col :span="6">
                        <el-input @focus="moreCondition=true"
                                  class="w-100"
                                  placeholder="更多细化搜索"
                                  size="small"
                                  v-if="!moreCondition">
                        </el-input>
                    </el-col>
                    <transition name="el-zoom-in-top">
                        <el-col :span="6" v-if="moreCondition">
                            <el-select v-model="listQuery.schoolIds"
                                       multiple
                                       filterable
                                       remote
                                       reserve-keyword
                                       :loading="loadingSchoolOptions"
                                       :remote-method="searchSchoolOptions"
                                       placeholder="学校"
                                       @change="handleFilter"
                                       class="w-100"
                                       size="small">
                                <el-option v-for="item in schoolOptions"
                                           :key="item.id"
                                           :label="item.name"
                                           :value="item.id">
                                </el-option>
                            </el-select>
                        </el-col>
                    </transition>
                    <transition name="el-zoom-in-top">
                        <el-col :span="6" v-if="moreCondition">
                            <el-select
                                    v-model="listQuery.salaryRangeId"
                                    filterable
                                    clearable
                                    placeholder="薪资范围"
                                    @change="handleFilter"
                                    class="w-100"
                                    size="small">
                                <el-option
                                        v-for="item in salaryRangeOptions"
                                        :key="item.id"
                                        :label="item.name"
                                        :value="item.id">
                                </el-option>
                            </el-select>
                        </el-col>
                    </transition>
                    <transition name="el-zoom-in-top">
                        <el-col :span="6" v-if="moreCondition">
                            <el-select
                                    v-model="listQuery.gpaRangeId"
                                    filterable
                                    clearable
                                    placeholder="GPA范围"
                                    @change="handleFilter"
                                    class="w-100"
                                    size="small">
                                <el-option
                                        v-for="item in gpaRangeOptions"
                                        :key="item.id"
                                        :label="item.name"
                                        :value="item.id">
                                </el-option>
                            </el-select>
                        </el-col>
                    </transition>
                </el-row>
            </div>

            <transition name="el-zoom-in-top">
                <div class="mt-3" v-if="moreCondition">
                    <el-row :gutter="10">
                        <el-col :span="6">
                            <el-select v-model="listQuery.gender" clearable placeholder="性别" @change="handleFilter" class="w-100" size="small">
                                <el-option v-for="item in genderOptions"
                                           :key="item.value"
                                           :label="item.name"
                                           :value="item.value"
                                           class="w-100"
                                           size="small">
                                </el-option>
                            </el-select>
                        </el-col>

                    </el-row>
                </div>
            </transition>
            <pagination
                    v-show="pageResult.total"
                    :total="pageResult.total"
                    :page.sync="listQuery.page"
                    :limit.sync="listQuery.limit"
                    @pagination="handleRouteList"/>
            <el-card class="box-card mb-3" v-for="resume in pageResult.list" :key="resume.id">
                <b-media @click="handleShowResume(resume)" style="cursor: pointer;">
                    <template v-slot:aside>
                        <el-badge class="item">
                            <el-avatar :src="resume.avatar"></el-avatar>
                        </el-badge>
                    </template>
                    <b-media-body>
                        <h6>
                            {{resume.name || ""}}
                            <span class="ml-4 text-muted text-small">
                            <span v-if="resume.maxResumeEdu">{{resume.maxResumeEdu.degree.name}} /</span>
                            {{resume.age}}岁 / {{resume.curPlace}}
                        </span>
                        </h6>
                        <div class="tag-group" v-if="resume.resumeSkillList && resume.resumeSkillList.length !== 0">
                            <el-tag size="small"
                                    effect="plain"
                                    type="info"
                                    class="mr-1"
                                    v-for="skill in resume.resumeSkillList"
                                    :key="skill.id">
                                {{skill.name}}
                            </el-tag>
                        </div>
                        <el-row :gutter="12" class="mt-4">
                            <el-col :span="12" v-if="resume.resumeEduList && resume.resumeEduList.length !== 0">
                                <h6>教育经历</h6>
                                <div class="text-small mt-2"
                                     v-for="edu in resume.resumeEduList"
                                     :key="edu.id">
                                    <span>{{ edu.schoolName }} / {{ edu.majorName }}.{{edu.degree.name}}</span>
                                    <span v-if="edu.gpa">/ GPA {{edu.gpa}}</span>
                                </div>
                            </el-col>
                            <el-col :span="12"
                                    v-if="resume.resumeExpList && resume.resumeExpList.length !== 0">
                                <h6>工作经历</h6>
                                <div :gutter="12"
                                     class="text-small mt-2"
                                     v-for="exp in resume.resumeExpList"
                                     :key="exp.id">
                                    <span>{{exp.company}} / {{exp.post}} / {{exp.startTime}} - {{exp.finishTime}}</span>
                                </div>
                            </el-col>
                        </el-row>
                    </b-media-body>
                </b-media>
            </el-card>

            <transition name="slide-fade">
                <div class="resume-drawer" v-if="resumeSelect">
                    <div class="resume-drawer-body pl-4 pr-4 pb-4">
                        <el-button type="text"
                                   class="el-icon-close button-close"
                                   @click="resumeSelect = undefined">
                        </el-button>
                        <ResumeView :resumeId="resumeSelect.id" class="mt-3"></ResumeView>
                    </div>
                </div>
            </transition>
            <pagination
                    v-show="pageResult.total"
                    :total="pageResult.total"
                    :page.sync="listQuery.page"
                    :limit.sync="listQuery.limit"
                    @pagination="handleRouteList"/>
        </div>
        <div class="app-container" v-else>
            <div class="mt-3">
                <el-row :gutter="10">
                    <el-col :span="6">
                        <el-select v-model="resumeType" placeholder="简历类型" @change="handleResumeType" class="w-100" size="small">
                            <el-option v-for="item in resumeTypeOptions"
                                       :key="item.value"
                                       :label="item.name"
                                       :value="item.value"
                                       class="w-100"
                                       size="small">
                            </el-option>
                        </el-select>
                    </el-col>
                    <el-col :span="6">
                        <el-input v-model="listAttachQuery.keywords"
                                  clearable
                                  @change="handleAttachFilter"
                                  class="w-100"
                                  placeholder="关键词"
                                  size="small">
                        </el-input>
                    </el-col>
                </el-row>
            </div>
            <pagination
                    v-show="attachPageResult.total"
                    :total="attachPageResult.total"
                    :page.sync="listAttachQuery.page"
                    :limit.sync="listAttachQuery.limit"
                    @pagination="handleAttachRouteList"/>
            <el-card class="box-card mt-3" v-for="resume in attachPageResult.list" :key="resume.id">
                <div v-html="resume.email" class="mb-2"></div>
                <div v-html="resume.attachContent" @click="onAttachResume(resume)" class="attach-content"></div>
            </el-card>
            <pagination
                    v-show="attachPageResult.total"
                    :total="attachPageResult.total"
                    :page.sync="listAttachQuery.page"
                    :limit.sync="listAttachQuery.limit"
                    @pagination="handleAttachRouteList"/>

            <div v-if="showPDF" class="pdf-mask" @click="showPDF=false;">
                <div ref="pdf" class="pdf-container"></div>
            </div>
        </div>
    </loading>
</template>

<script>

    import Pagination from "@/components/Pagination";
    import PDFObject from 'pdfobject'
    import {listByType} from "@/api/dict_api";
    import {searchSchool} from "@/api/school_api";
    import {getCategoryTree} from "@/api/category_api";
    import {formatListQuery, parseListQuery} from "@/utils/common";
    import ResumeView from "@/components/ResumeView";
    import Toast from "@/utils/toast";

    export default {
        name: "ResumeSearchPage",
        components: {Pagination, ResumeView},
        data() {
            return {
                resumeType: 1,
                resumeTypeOptions: [
                    {name: "在线简历", value: 1},
                    {name: "附件简历", value: 2}
                ],
                moreCondition: false,
                resumeSelect: undefined,
                listAttachQuery: {
                    keywords: undefined,
                    page: 1,
                    limit: 10,
                    sort: "-id"
                },
                listQuery: {
                    name: undefined,
                    gender: undefined,
                    degreeIds: undefined,
                    schoolIds: undefined,
                    categoryIds: undefined,
                    cityIds: undefined,
                    salaryRangeId: undefined,
                    gpaRangeId: undefined,
                    page: 1,
                    limit: 10,
                    sort: "-id"
                },
                userGender: {
                    1: "男",
                    2: "女"
                },
                genderOptions: [
                    {name: "男", value: 1},
                    {name: "女", value: 2}
                ],
                degreeOptions: [],
                schoolOptions: [],
                jobCategoryOptions: [],
                cityOptions: [],
                salaryRangeOptions: [],
                gpaRangeOptions: [],
                loadingSchoolOptions: false,

                jobCategoryProps: {
                    multiple: true,
                    expandTrigger: "hover",
                    value: "id",
                    label: "name",
                    emitPath: false,
                    children: "children"
                },

                pageResult: {
                    total: 0
                },
                attachPageResult: {
                    total: 0
                },
                showPDF: false,
                loading: true
            }
        },
        created() {
            this.initData();
        },
        watch: {
            $route() {
                this.resumeSelect = undefined;
                if (this.resumeType === 1) {
                    this.getList();
                } else if (this.resumeType === 2) {
                    this.getAttachList();
                }
            },
            resumeType() {
                this.resumeSelect = undefined;
                if (this.resumeType === 1) {
                    this.getList();
                } else if (this.resumeType === 2) {
                    this.getAttachList();
                }
            },
            "listQuery.degreeIds": function (newVal, oldVal) {
                if (newVal.length > 3) {
                    this.listQuery.degreeIds = oldVal;
                    this.handleRouteList();
                    Toast.error("搜索学历不能超过3个");
                }
            },
            "listQuery.cityIds": function (newVal, oldVal) {
                if (newVal.length > 3) {
                    this.listQuery.cityIds = oldVal;
                    this.handleRouteList();
                    Toast.error("搜索城市不能超过3个");
                }
            },
            "listQuery.categoryIds": function (newVal, oldVal) {
                if (newVal.length > 3) {
                    this.$nextTick(() => {
                        this.listQuery.categoryIds = oldVal;
                        this.handleRouteList();
                    })
                    Toast.error("搜索职位不能超过3个");
                }
            },
            loading: function (newVal) {
                if (!newVal) {
                    this.$emit("complete");
                }
            }
        },
        methods: {
            handleResumeType() {
            },
            handleShowResume(resume) {
                this.resumeSelect = resume;
            },
            handleRouteList() {
                this.$router.push({
                    path: this.$route.path,
                    query: formatListQuery(this.listQuery)
                });
            },
            handleAttachRouteList() {
                this.$router.push({
                    path: this.$route.path,
                    query: formatListQuery(this.listAttachQuery)
                });
            },
            handleFilter() {
                this.listQuery.page = 1;
                this.handleRouteList();
            },
            handleAttachFilter() {
                this.listAttachQuery.page = 1;
                this.handleAttachRouteList();
            },
            searchSchoolOptions(query) {
                if (query !== "") {
                    this.loadingSchoolOptions = true;
                    searchSchool(query)
                        .then(response => (this.schoolOptions = response.data))
                        .finally(() => (this.loadingSchoolOptions = false));
                } else {
                    this.schoolOptions = [];
                }
            },
            initData() {
                listByType(1).then(response => (this.degreeOptions = response.data.list));
                listByType(2).then(response => (this.cityOptions = response.data.list));
                listByType(9).then(
                    response => (this.salaryRangeOptions = response.data.list)
                );
                listByType(10).then(
                    response => (this.gpaRangeOptions = response.data.list)
                );
                getCategoryTree().then(
                    response => (this.jobCategoryOptions = response.data)
                );
                if (this.resumeType === 1) {
                    this.getList();
                } else {
                    this.getAttachList();
                }
            },
            getList() {
                parseListQuery(this.$route.query, this.listQuery);
                if (this.$route.query.degreeIds) {
                    this.listQuery.degreeIds = this.$route.query.degreeIds.split(",").map(id => parseInt(id));
                }
                if (this.$route.query.cityIds) {
                    this.listQuery.cityIds = this.$route.query.cityIds.split(",").map(id => parseInt(id));
                }
                if (this.$route.query.categoryIds) {
                    this.listQuery.categoryIds = this.$route.query.categoryIds.split(",").map(id => parseInt(id));
                }
                this.$axios.request({
                    url: "/resume/list",
                    method: "post",
                    data: this.listQuery
                }).then(data => {
                    this.pageResult = data.data;
                    this.loading = false;
                })
            },
            getAttachList() {
                parseListQuery(this.$route.query, this.listAttachQuery);
                let keywords = [];
                if (this.listAttachQuery.keywords) {
                    keywords = this.listAttachQuery.keywords.replace("，", ",").split(",");
                }
                this.$axios.request({
                    url: "/resume/list-attachment",
                    method: "post",
                    data: {...this.listAttachQuery, keywords}
                }).then(data => {
                    data.data.list = data.data.list.map(item => {
                        let emails = item.attachContent.match(/\w+@[a-zA-Z0-9]{2,10}(?:\.[a-z]{2,4}){1,3}/);
                        item.email = emails && emails.length > 0 ? "邮箱：" + item.attachContent.match(/\w+@[a-zA-Z0-9]{2,10}(?:\.[a-z]{2,4}){1,3}/)[0] : "暂无邮箱";
                        let index = item.attachContent.indexOf(keywords[0]);
                        if (index > item.attachContent.length - 140) {
                            index = item.attachContent.length - 140
                        }
                        if (index > 150) {
                            item.attachContent = "..." + item.attachContent.substring(index - 150);
                        }
                        keywords.forEach(keyword => {
                            item.attachContent = item.attachContent.replace(eval("/" + keyword + "/g"), "<span style='background: yellow;'>" + keyword + "</span>");
                        })
                        return item;
                    });
                    this.attachPageResult = data.data;
                    this.loading = false;
                })
            },
            onAttachResume(resume) {
                console.log(resume);
                this.showPDF = true;
                this.$nextTick(() => {
                    PDFObject.embed(resume.docPath, this.$refs.pdf);
                })
            }
        }
    }
</script>

<style scoped lang="scss">
    .app-container {
        max-width: 1200px;
        margin: 0 auto;
    }

    .resume-drawer {
        right: 0;
        height: 100%;
        width: 50%;
        top: 0;
        bottom: 0;
        z-index: 100;
        position: fixed;
        box-sizing: border-box;
        background-color: rgb(255, 255, 255);
        display: flex;
        flex-direction: column;
        box-shadow: rgba(0, 0, 0, 0.2) 0 8px 10px -5px,
        rgba(0, 0, 0, 0.14) 0px 16px 24px 2px, rgba(0, 0, 0, 0.12) 0px 6px 30px 5px;
    }

    .resume-drawer-body {
        position: absolute;
        top: 0;
        bottom: 0;
        width: 100%;
        overflow: auto;
    }

    .slide-fade-enter-active {
        transition: all .5s ease;
    }

    .slide-fade-leave-active {
        transition: all .5s ease;
    }

    .slide-fade-enter, .slide-fade-leave-to {
        transform: translateX(50%);
        opacity: 0;
    }

    .button-close {
        position: absolute;
        right: 0;
        top: 0;
        width: 100px;
        height: 100px;
        font-size: 30px;
    }

    .box-card:hover {
        cursor: pointer;
    }

    .pdf-mask {
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: #333333CC;
        z-index: 100;
    }

    .pdf-container {
        position: fixed;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        width: 800px;
        height: calc(100% - 80px);
        max-height: 1165px;
    }

    .attach-content {
        word-break: break-all;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-box-orient: vertical;
        -webkit-line-clamp: 5;
        overflow: hidden;
    }
</style>
