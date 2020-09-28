<template>
    <div class="app-container edit-job-container">
        <h5 class="mb-4">{{title}}</h5>
        <el-form ref="jobForm"
                 size="medium"
                 :model="jobForm"
                 :rules="jobFormRules"
                 label-width="100px"
                 label-position="left">
            <el-form-item label="职位名称" prop="name">
                <el-input v-model="jobForm.name"
                          maxlength="20"
                          class="text-input-width"
                          show-word-limit
                          placeholder="请填写职位名称"
                          :disabled="isModify"></el-input>
            </el-form-item>

            <el-form-item label="职位类别" prop="categoryId">
                <el-cascader placeholder="选择职位类别"
                             :show-all-levels="false"
                             :options="jobCategoryOptions"
                             :props="jobCategoryProps"
                             filterable
                             clearable
                             v-model="jobForm.categoryId"
                             @change="onJobCategoryChange"
                             :disabled="isModify"></el-cascader>
            </el-form-item>

            <el-form-item label="工作城市" prop="cityId">
                <el-select v-model="jobForm.cityId" filterable clearable placeholder="请选择工作城市">
                    <el-option v-for="item in cityOptions" :key="item.id" :label="item.name" :value="item.id"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="工作地址" prop="address">
                <el-input v-model="jobForm.address"
                          maxlength="250"
                          class="text-input-width"
                          show-word-limit
                          placeholder="请填写工作地址"></el-input>
            </el-form-item>
            <el-form-item label="学历要求" prop="minDegreeId">
                <el-select v-model="jobForm.minDegreeId" filterable clearable placeholder="请选择最低学历">
                    <el-option v-for="item in degreeOptions"
                               :key="item.id"
                               :label="item.name"
                               :value="item.id"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="经验要求" prop="experience">
                <el-select v-model="jobForm.experienceId" filterable clearable placeholder="请选择经验要求">
                    <el-option v-for="item in experienceOptions"
                               :key="item.id"
                               :label="item.name"
                               :value="item.id"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="薪资待遇" prop="salary">
                <el-select v-model="jobForm.salaryId" placeholder="薪资范围" class="salary-option">
                    <el-option v-for="item in SalaryOptions"
                               :key="item.id"
                               :label="item.name"
                               :value="item.id"
                    ></el-option>
                </el-select>
                <span class="pl-2 pr-2">×</span>
                <el-select v-model="jobForm.salaryMonths"
                           clearable
                           placeholder="薪资月数(可选)"
                           class="ml-2 salary-month-option" style="margin-left: 0px!important;">
                    <el-option v-for="item in salaryMonthOptions"
                               :key="item.value"
                               :label="item.label"
                               :value="item.value"></el-option>
                </el-select>
            </el-form-item>

            <el-form-item label="工作类型" prop="jobType">
                <el-select v-model="jobForm.jobType" placeholder="校招" class="salary-option">
                    <el-option v-for="item in jobTypeOptions2"
                               :key="item.id"
                               :label="item.name"
                               :value="item.id"
                    ></el-option>
                </el-select>

                <el-select v-model="jobForm.recruitType" placeholder="全职" class="salary-option" style="margin-left: 20px">
                    <el-option v-for="item in jobTypeOptions1"
                               :key="item.id"
                               :label="item.name"
                               :value="item.id"
                    ></el-option>
                </el-select>

            </el-form-item>

            <el-form-item label="职位关键词" prop="keywords" v-if="skillList.length">
                <div class="keywords-container">
                    <el-tag v-for="tag in industryList" v-if="tag.selected"
                            :key="tag.name"
                            closable
                            @close="onDeleteTag(tag)">
                        {{tag.name}}
                    </el-tag>
                    <el-tag v-for="tag in skillList" v-if="tag.selected"
                            :key="tag.name"
                            closable
                            @close="onDeleteTag(tag)">
                        {{tag.name}}
                    </el-tag>
                    <span class="position-world" @click="onKeywords">
                        <i class="el-icon-plus position-icon"></i>
                    </span>
                </div>
            </el-form-item>
            <el-form-item label="职位描述" prop="description">
                <quill-editor v-model="jobForm.description"
                              :options="descriptionEditorOption"
                              style="min-height: 200px;"
                              @change="onEditorChange($event)">
                </quill-editor>
                <div style="bottom: 0; text-align: right; position: absolute; right: 12px;">
                    {{contentLength}}/300
                </div>
            </el-form-item>
            <div class="job-description-container">
                <div class="job-description-show">
                    <el-button v-if="jobDescriptionShow" @click="jobForm.description=jobDescription" type="text">
                        复制样例
                    </el-button>
                    <el-button v-if="jobDescription" @click="jobDescriptionShow=!jobDescriptionShow" type="text">
                        {{jobDescriptionShow?'关闭样例':'查看样例'}}
                    </el-button>
                </div>
                <div v-if="jobDescriptionShow" v-html="jobDescription" class="job-description"></div>
            </div>
            <el-form-item>
                <el-button type="primary" size="mini" style="padding: 0 12px;line-height: 30px" :loading="posting" @click="onSubmit">{{pubButtonText}}
                </el-button>
            </el-form-item>
        </el-form>

        <el-dialog :visible.sync="dialogVisible"
                   class="dialog-container"
                   width="800px"
                   title="添加职位关键词">
            <div class="selected-container">
                <span>已选择</span>
                <div class="selected-right">
                    <el-tag v-for="tag in industryList" v-if="tag.selected"
                            :key="tag.name"
                            closable
                            @close="onDeleteTag(tag)">
                        {{tag.name}}
                    </el-tag>
                    <el-tag v-for="tag in skillList" v-if="tag.selected"
                            :key="tag.name"
                            closable
                            @close="onDeleteTag(tag)">
                        {{tag.name}}
                    </el-tag>
                </div>
            </div>

            <div class="industry-container" v-if="industryList.length">
                <el-select
                        class="mb-4"
                        v-model="searchTag"
                        filterable
                        remote
                        reserve-keyword
                        placeholder="请输入关键词"
                        :remote-method="remoteMethod"
                        @change="onSearchTagChange"
                        :loading="searchTagLoading">
                    <el-option
                            v-for="item in searchTagOptions"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value">
                    </el-option>
                </el-select>
                <div class="title-container">行业领域<span class="title-tips">(最多可选2个行业)</span></div>
                <div class="industry-tag-container">
                    <el-tag
                            v-for="tag in industryList"
                            :key="tag.name"
                            :class="['skill-style', {'skill-style-select':tag.selected}]"
                            @click="onIndustrySelect(tag)">
                        {{tag.name}}
                    </el-tag>
                </div>
            </div>

            <div class="industry-container" v-if="skillList.length">
                <div class="title-container">技能要求<span class="title-tips">(最多可选4个技能)</span></div>
                <div class="industry-tag-container">
                    <el-tag v-for="tag in skillList"
                            :key="tag.name"
                            :class="['skill-style', {'skill-style-select':tag.selected}]"
                            @click="onSkillSelect(tag)">
                        {{tag.name}}
                    </el-tag>
                </div>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false" size="small">取 消</el-button>
                <el-button type="primary" size="small" @click="onConfirm">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
    import {getCategoryTree} from "@/api/category_api";
    import {listByType} from "@/api/dict_api";
    import {saveJob, getJobInfo} from "@/api/job_api";
    import Toast from "@/utils/toast";

    import "quill/dist/quill.core.css";
    import "quill/dist/quill.snow.css";
    import "quill/dist/quill.bubble.css";
    import {quillEditor} from "vue-quill-editor";
    import {delCompanyAddr} from "@/api/company_api";

    export default {
        name: "NewJobPage",
        components: {
            quillEditor
        },
        data() {
            return {
                dialogVisible: false,
                Salary: '',
                jobForm: {
                    id: undefined,
                    name: undefined,
                    categoryId: undefined,
                    depart: undefined,
                    minDegreeId: undefined,
                    salaryId: undefined,
                    salaryMonths: undefined,
                    cityId: undefined,
                    address: undefined,
                    recruitType: undefined,
                    jobType: undefined,
                    description: undefined,
                    keywords: undefined,
                    experienceId: undefined,
                    skillTags: [],
                    industryTags: []

                },
                jobFormRules: {
                    name: [{required: true, message: "请输入职位名称", trigger: "blur"}],
                    categoryId: [{required: true, message: "请选择职位类型", trigger: "change"}],
                    minDegreeId: [{required: true, message: "请选择学历要求", trigger: "change"}],
                    cityId: [{required: true, message: "请选择工作城市", trigger: "change"}],
                    address: [{required: true, message: "请填写工作地点", trigger: "blur"}],
                    jobType: [{required: true, message: "请选择工作类型", trigger: "change"}],
                    recruitType: [{required: true, message: "请选择工作类型", trigger: "change"}],
                    description: [{required: true, message: "请输入职位描述", trigger: "blur"}],
                    salaryId: [{required: true, message: "请选择薪资范围", trigger: "blur"}],
                    experienceId: [{required: true, message: "请选择经验要求", trigger: "change"}]
                },
                jobCategoryOptions: [],
                jobCategoryProps: {
                    expandTrigger: "hover",
                    value: "id",
                    label: "name",
                    emitPath: true,
                    children: "children"
                },
                cityOptions: [],
                degreeOptions: [],
                salaryMonthOptions: [],
                jobTypeOptions1: [],
                jobTypeOptions2: [],
                SalaryOptions: [],//薪资范围
                descriptionEditorOption: {
                    theme: "snow",
                    placeholder: "1.岗位职责 2.任职要求 3.相应技能",
                    modules: {
                        toolbar: [["bold"], [{list: "ordered"}, {list: "bullet"}]]
                    }
                },
                posting: false,
                experienceOptions: [], // 经验要求
                skillList: [],//技能列表
                industryList: [],//行业列表
                jobDescription: undefined, // 职位描述
                jobDescriptionShow: false, // 职位描述开关
                contentLength: 0, // 字数统计
                searchTagLoading: false,
                searchTag: undefined,
                searchTagOptions: []
            };
        },
        created() {
            const jobId = this.$route.query.id;
            this.initData(jobId);
        },
        mounted() {

        },
        watch: {
            jobForm: {
                handler() {
                    if (this.jobForm.id === undefined) {
                        this.$store.commit("setting/JOB_DRAFT", this.jobForm);
                    }
                },
                deep: true
            }
        },
        computed: {
            title() {
                return this.$route.query.id ? "编辑职位" : "新增职位";
            },
            pubButtonText() {
                return this.$route.query.id ? "保存" : "发布";
            },
            isModify() {
                return this.$route.query.id !== undefined;
            }
        },
        methods: {
            initData(jobId) {
                getCategoryTree().then(response => (this.jobCategoryOptions = response.data));
                listByType(1).then(response => (this.degreeOptions = response.data.list));
                listByType(8).then(response => (this.jobTypeOptions1 = response.data.list));
                listByType(2).then(response => (this.cityOptions = response.data.list));
                listByType(9).then(response => (this.SalaryOptions = response.data.list));
                listByType(13).then(response => (this.experienceOptions = response.data.list));
                listByType(12).then(response => (this.jobTypeOptions2 = response.data.list));

                this.salaryMonthOptions = this.generateSalaryMonthOptions(0, 24);
                if (jobId) {
                    getJobInfo(jobId).then(response => {
                        const {data} = response;
                        this.jobForm.id = data.id;
                        this.jobForm.name = data.name;
                        this.jobForm.categoryId = data.category.id;
                        this.jobForm.depart = data.depart;
                        this.jobForm.minDegreeId = data.minDegree ? data.minDegree.id : undefined;
                        this.jobForm.salaryId = data.salary ? data.salary.id : undefined
                        this.jobForm.salaryMonths = data.salaryMonths;
                        this.jobForm.cityId = data.city ? data.city.id : undefined;
                        this.jobForm.address = data.address;
                        this.jobForm.jobType = data.jobType ? data.jobType.id : undefined;
                        this.jobForm.description = data.description;
                        this.jobForm.recruitType = data.recruitType ? data.recruitType.id : undefined
                        this.jobForm.experienceId = data.experience ? data.experience.id : undefined
                        this.jobForm.skillTags = data.skillTags;
                        this.jobForm.industryTags = data.industryTags;

                        getCategoryTree().then(response => {
                            this.jobCategoryOptions = response.data

                            // 查找分类id
                            for (let i = 0; i < this.jobCategoryOptions.length; i++) {
                                let first = this.jobCategoryOptions[i];
                                for (let j = 0; j < first.children.length; j++) {
                                    let second = first.children[j];
                                    for (let k = 0; k < second.children.length; k++) {
                                        let third = second.children[k];
                                        if (third.id === this.jobForm.categoryId) {
                                            this.jobForm.categoryId = [first.id, second.id, third.id];
                                            this.onJobCategoryChange();
                                            return;
                                        }
                                    }
                                }
                            }
                        });
                    });
                } else if (this.$store.getters.jobDraft) {
                    this.jobForm = this.$store.getters.jobDraft;
                }
            },
            onSubmit() {
                this.$refs["jobForm"].validate(valid => {
                    if (valid) {
                        this.$store.commit("setting/JOB_DRAFT", undefined);
                        this.posting = true;
                        this.jobForm.industryTags = this.industryList.filter(item => item.selected).map(item => item.name);
                        this.jobForm.skillTags = this.skillList.filter(item => item.selected).map(item => item.name);
                        saveJob(this.jobForm)
                            .then(() => {
                                Toast.success(this.isModify ? "保存成功" : "发布成功");
                                this.$router.go(-1);
                            })
                            .finally(() => {
                                this.posting = false;
                            });
                    }
                });
            },

            generateSalaryMonthOptions(minVal, length) {
                const salaryMonthOptions = [];
                for (let i = 0; i !== length; i++) {
                    minVal = minVal + 1;
                    salaryMonthOptions.push({
                        label: minVal + "个月",
                        value: minVal
                    });
                }
                return salaryMonthOptions;
            },

            // 显示关键选择框
            onKeywords() {
                this.dialogVisible = true;
            },

            // 关键词选择确认
            onConfirm() {
                this.dialogVisible = false;
                let industry = this.industryList.filter(item => item.selected).map(item => item.name);
                let skill = this.skillList.filter(item => item.selected).map(item => item.name);
                this.jobForm.keywords = industry.concat(skill).toString();
            },
            // 技能选择
            onSkillSelect(tag) {
                if (tag.selected === true || this.skillList.filter(item => item.selected).length < 4) {
                    tag.selected = !tag.selected;
                } else {
                    this.$message.warning("最多选择4个技能");
                }
            },

            // 行业选择
            onIndustrySelect(tag) {
                if (tag.selected === true || this.industryList.filter(item => item.selected).length < 2) {
                    tag.selected = !tag.selected;
                } else {
                    this.$message.warning("最多选择2个行业");
                }
            },

            // 删除关键词
            onDeleteTag(tag) {
                tag.selected = false;
            },

            // 选择职位分类，获取标签
            onJobCategoryChange() {
                if (this.jobForm.categoryId && this.jobForm.categoryId.length === 3) {
                    let p1 = this.$axios.get('/jobskill/list', {
                        params: {jobCategoryId: this.jobForm.categoryId[1]} // 取选中标签的父级id
                    });
                    let p2 = this.$axios.get('/jobindustry/list', {
                        params: {jobCategoryId: this.jobForm.categoryId[2]}
                    });
                    Promise.all([p1, p2]).then((result) => {
                        if (result[0].data.length > 0 || result[1].data.length > 0) {
                            this.jobFormRules.keywords = [{required: true, message: "请选择关键词", trigger: "blur"}];
                        } else {
                            delete this.jobFormRules.keywords;
                        }
                        if (result[0].data.length > 0) {
                            this.skillList = result[0].data.map(item => {
                                item.selected = this.jobForm.skillTags && this.jobForm.skillTags.includes(item.name);
                                return item;
                            });
                        }
                        if (result[1].data.length > 0) {
                            this.industryList = result[1].data.map(item => {
                                item.selected = this.jobForm.industryTags && this.jobForm.industryTags.includes(item.name);
                                return item;
                            });
                        }
                    })
                    this.$axios.get('/jobdescexample/list', {
                        params: {categoryId: this.jobForm.categoryId[2]}
                    }).then(data => {
                        this.jobDescription = data.data && data.data.description;
                    })
                    this.jobForm.categoryId = this.jobForm.categoryId[2];
                }
            },

            onEditorChange(event) {
                event.quill.deleteText(300, 1);
                this.contentLength = event.quill.getLength() - 1
            },

            remoteMethod(query) {
                if (query !== '') {
                    this.searchTagLoading = true;
                    this.$axios.request({
                        url: "/category/tag-keywords",
                        method: "get",
                        params: {name: query, categoryId: this.jobForm.categoryId}
                    }).then(data => {
                        this.searchTagLoading = false;
                        this.searchTagOptions = [];
                        this.searchTagOptions = this.searchTagOptions.concat(
                            data.data.industryTags.map(item => {
                                let obj = {}
                                obj.label = item;
                                obj.value = "industry," + item;
                                return obj;
                            }));
                        this.searchTagOptions = this.searchTagOptions.concat(data.data.skillTags.map(item => {
                            let obj = {}
                            obj.label = item;
                            obj.value = "skill," + item;
                            return obj;
                        }));
                    })
                } else {
                    this.searchTagOptions = [];
                }
            },

            onSearchTagChange(value) {
                let obj = value.split(",");
                let type = obj[0];
                let tag = obj[1];
                if (type === "industry") {
                    if (this.industryList.filter(item => item.selected).length < 2) {
                        this.industryList.forEach(industry => {
                            if (industry.name === tag) {
                                industry.selected = true;
                            }
                        })
                    } else {
                        this.$message.warning("最多选择2个行业");
                    }
                } else if (type === "skill") {
                    if (this.skillList.filter(item => item.selected).length < 2) {
                        this.skillList.forEach(skill => {
                            if (skill.name === tag) {
                                skill.selected = true;
                            }
                        })
                    } else {
                        this.$message.warning("最多选择2个技能");
                    }
                }
            }
        }
    };
</script>

<style scoped>
    .app-container {
        margin: 0 auto;
        width: 1200px;
    }

    /deep/ .el-select-dropdown__wrap {
        max-width: 200px !important;
    }

    .text-input-width {
        width: 400px;
    }

    .salary-option {
        width: 120px;
    }
</style>

<style lang="scss" scoped>
    @import "bootstrap/scss/bootstrap.scss";

    .edit-job-container {
        .keywords-container {
            display: flex;
            align-items: center;

            span {
                margin-right: 20px;
            }
        }

        .position-world {
            width: 80px;
            height: 32px;
            border-radius: 16px;
            border: 1px dashed #409EFF;
            display: flex;
            align-items: center;
            justify-content: center;

            .position-icon {
                font-size: 16px;
                color: #409EFF;
            }
        }

        .dialog-container {
            .selected-container {
                width: 100%;
                display: flex;
                align-items: center;
                padding-bottom: 20px;
                border-bottom: 1px solid #E4E7ED;
                height: 50px;

                .selected-right {
                    width: calc(100% - 70px);
                    margin-left: 20px;

                    /deep/ .el-tag {
                        border: none;
                        background: #409EFF;
                        margin-right: 8px;
                        border-radius: 16px;
                        line-height: 32px;
                        color: white;

                        /deep/ .el-icon-close {
                            color: white;
                        }

                        /deep/ .el-icon-close:hover {
                            color: white;
                            background-color: #409EFF;
                        }

                    }
                }
            }

            .industry-container {
                width: 100%;
                padding-top: 20px;

                .title-container {
                    font-size: 15px;
                    font-weight: bold;

                    .title-tips {
                        font-weight: 500;
                        color: #909399;
                    }
                }

                .industry-tag-container {
                    width: 100%;
                    padding-top: 20px;
                    display: flex;
                    flex-wrap: wrap;


                    /deep/ .el-tag {
                        background: #ffffff;
                        margin-right: 8px;
                        border-radius: 16px;
                        line-height: 32px;
                        color: #303133;
                        border: 1px solid #E4E7ED;

                        /deep/ .el-icon-close {
                            color: white;
                        }

                        /deep/ .el-icon-close:hover {
                            color: white;
                            background-color: #409EFF;
                        }

                    }

                    .skill-style {
                        margin-bottom: 10px;
                    }

                    .skill-style-select {
                        background-color: #409EFF;
                        color: white;
                    }

                    .skill-style:hover {
                        cursor: pointer
                    }
                }

            }

            .submit-container {
                padding-top: 30px;
            }
        }


        .ql-container .ql-editor {
            min-height: 200px !important;
            font-size: 15px;
        }

        .ql-editor p {
            margin-bottom: 10px;
        }

        .ql-editor ol,
        .ql-editor ul {
            padding-left: 0.5em;
            margin-bottom: 10px;
        }

        .ql-bubble {
            border: 1px solid #dcdfe6;
            border-radius: 4px;
        }

        .ql-editor.ql-blank::before {
            font-style: normal;
            color: #c0c4cc;
            font-size: 15px;
        }

        .salary-month-option {
            width: 140px;
        }

        .job-description-container {
            display: flex;
            flex-direction: column;
            align-items: flex-end;

            .job-description-show {
                display: flex;
                align-items: center;
            }

            .job-description {
                width: 1098px;
                border: 1px solid #c0c4cc;
                padding: 15px;
                margin-bottom: 30px;
            }
        }

    }

    li.el-cascader-node {
        font-size: 15px;
        line-height: 40px;
        height: 40px;
    }

    button:focus {
        outline: 0;
    }
</style>
