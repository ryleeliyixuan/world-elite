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

            <el-form-item label="职位类型" prop="categoryId">
                <el-cascader placeholder="选择职位类型"
                             :show-all-levels="false"
                             :options="jobCategoryOptions"
                             :props="jobCategoryProps"
                             filterable
                             clearable
                             v-model="jobForm.categoryId"
                             :disabled="isModify"></el-cascader>
            </el-form-item>

            <el-form-item label="工作城市" prop="cityId">
                <el-select v-model="jobForm.cityId" filterable clearable placeholder="请选择工作城市">
                    <el-option v-for="item in cityOptions" :key="item.id" :label="item.name" :value="item.id"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="工作地点" prop="address">
                <el-input v-model="jobForm.address"
                          maxlength="250"
                          class="text-input-width"
                          show-word-limit
                          placeholder="请填写工作地点"></el-input>
            </el-form-item>
            <el-form-item label="学历要求" prop="minDegreeId">
                <el-select v-model="jobForm.minDegreeId" filterable clearable placeholder="请选择最低学历">
                    <el-option v-for="item in degreeOptions"
                               :key="item.id"
                               :label="item.name"
                               :value="item.id"></el-option>
                </el-select>
            </el-form-item>
<!--            <el-form-item label="经验要求" prop="experienceId">-->
<!--                <el-select v-model="jobForm.experienceId" filterable clearable placeholder="请选择经验要求">-->
<!--                    <el-option v-for="item in experienceOptions"-->
<!--                               :key="item.id"-->
<!--                               :label="item.name"-->
<!--                               :value="item.id"></el-option>-->
<!--                </el-select>-->
<!--            </el-form-item>-->
            <el-form-item label="薪资待遇" prop="salary">
                <el-select v-model="jobForm.minSalary" placeholder="最低" class="salary-option">
                    <el-option v-for="item in minSalaryOptions"
                               :key="item.value"
                               :label="item.label"
                               :value="item.value"></el-option>
                </el-select>
                <el-select v-model="jobForm.maxSalary" placeholder="最高" class="ml-2 salary-option">
                    <el-option v-for="item in maxSalaryOptions"
                               :key="item.value"
                               :label="item.label"
                               :value="item.value"></el-option>
                </el-select>
                <span class="pl-2 pr-2">×</span>
                <el-select v-model="jobForm.salaryMonths"
                           clearable
                           placeholder="薪资月数(可选)"
                           class="ml-2 salary-month-option">
                    <el-option v-for="item in salaryMonthOptions"
                               :key="item.value"
                               :label="item.label"
                               :value="item.value"></el-option>
                </el-select>
            </el-form-item>

            <el-form-item label="工作类型" prop="jobType">
                <el-radio-group v-model="jobForm.jobType">
                    <el-radio :label="jobType.id"
                              border
                              v-for="jobType in jobTypeOptions"
                              :key="jobType.id">
                        {{jobType.name}}
                    </el-radio>
                </el-radio-group>
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
                <quill-editor v-model="jobForm.description" :options="descriptionEditorOption"></quill-editor>
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
                <el-button type="primary" size="mini" style="padding: 0 12px;line-height: 30px"  :loading="posting" @click="onSubmit" >{{pubButtonText}}</el-button>
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

    export default {
        name: "NewJobPage",
        components: {
            quillEditor
        },
        data() {
            return {
                dialogVisible: false,
                jobForm: {
                    id: undefined,
                    name: undefined,
                    categoryId: undefined,
                    depart: undefined,
                    minDegreeId: undefined,
                    minSalary: undefined,
                    maxSalary: undefined,
                    salaryMonths: undefined,
                    cityId: undefined,
                    address: undefined,
                    jobType: undefined,
                    description: undefined,
                    salary: undefined,
                    keywords: undefined,
                    experienceId: undefined
                },
                jobFormRules: {
                    name: [{required: true, message: "请输入职位名称", trigger: "blur"}],
                    categoryId: [{required: true, message: "请选择职位类型", trigger: "change"}],
                    minDegreeId: [{required: true, message: "请选择学历要求", trigger: "change"}],
                    cityId: [{required: true, message: "请选择工作城市", trigger: "change"}],
                    address: [{required: true, message: "请填写工作地点", trigger: "blur"}],
                    jobType: [{required: true, message: "请选择工作类型", trigger: "change"}],
                    description: [{required: true, message: "请输入职位描述", trigger: "blur"}],
                    salary: [{required: true, message: "请选择薪资范围", trigger: "blur"}],
                    // experienceId: [{required: true, message: "请选择经验要求", trigger: "change"}]
                },
                jobCategoryOptions: [],
                jobCategoryProps: {
                    expandTrigger: "hover",
                    value: "id",
                    label: "name",
                    emitPath: false,
                    children: "children"
                },
                cityOptions: [],
                degreeOptions: [],
                minSalaryOptions: [],
                maxSalaryOptions: [],
                salaryMonthOptions: [],
                jobTypeOptions: [],
                descriptionEditorOption: {
                    theme: "snow",
                    placeholder: "岗位职责，任职要求等",
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
            };
        },
        created() {
            const jobId = this.$route.query.id;
            this.initData(jobId);
        },
        watch: {
            "jobForm.minSalary": function () {
                this.maxSalaryOptions = this.generateSalaryOptions(this.jobForm.minSalary, this.jobForm.minSalary < 10 ? 5 : 10);
                this.jobForm.salary = this.jobForm.minSalary && this.jobForm.maxSalary ? 1 : undefined;
            },
            "jobForm.maxSalary": function () {
                this.jobForm.salary = this.jobForm.minSalary && this.jobForm.maxSalary ? 1 : undefined;
            },
            "jobForm.categoryId": function () {
                if(this.jobForm.categoryId) {
                    let p1 = this.$axios.get('/jobskill/list', {
                        params: {jobCategoryId: this.jobForm.categoryId}
                    });
                    let p2 = this.$axios.get('/jobindustry/list', { // TODO : url待替换   /jobindustry/list
                        params: {jobCategoryId: this.jobForm.categoryId}
                    });
                    Promise.all([p1, p2]).then((result) => {
                        // TODO： 测试待删除 begin
                        // result[0].data = [{name: '标签一'},
                        //     {name: '标签二', id: 1},
                        //     {name: '标签三', id: 2},
                        //     {name: '标签四', id: 3},
                        //     {name: '标签五', id: 4}];
                        // result[1].data = [{name: '标签1'},
                        //     {name: '标签2', id: 1},
                        //     {name: '标签3', id: 2},
                        //     {name: '标签4', id: 3},
                        //     {name: '标签5', id: 4}];
                        // TODO： 测试待删除 end

                        if (result[0].data.length > 0 || result[1].data.length > 0) {
                            this.jobFormRules.keywords = [{required: true, message: "请选择关键词", trigger: "blur"}];
                        } else {
                            delete this.jobFormRules.keywords;
                        }
                        if (result[0].data.length > 0) {
                            this.skillList = result[0].data.map(item => {
                                item.selected = false;
                                return item;
                            });
                        }
                        if (result[1].data.length > 0) {
                            this.industryList = result[1].data.map(item => {
                                item.selected = false;
                                return item;
                            });
                        }
                    })
                    // TODO 以下一行测试待删除
                    // this.jobDescription = "<span style='color:red'>我是一段描述我是一段描述我是一段描述我是一段描述我是一段描述我是一段描述我是一段描述我是一段描述我是一段描述我是一段描述我是一段描述我是一段描述我是一段描述我是一段描述我是一段描述我是一段描述我是一段描述我是一段描述我是一段描述我是一段描述我是一段描述</span>";
                    this.$axios.get('/jobdescexample/list', {
                        params: {categoryId: this.jobForm.categoryId}
                    }).then(data => {
                        this.jobDescription = data.data;
                    })
                }
            },
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
                listByType(8).then(response => (this.jobTypeOptions = response.data.list));
                listByType(2).then(response => (this.cityOptions = response.data.list));
                // listByType(9).then(response => (this.experienceOptions = response.data.list)); // TODO ： experienceOptions需要网络获取
                this.experienceOptions = [{id: 1, name: "不限"}, {id: 2, name: "1年以下"}, {id: 3, name: "1-3年"},
                    {id: 4, name: "3-5年"}, {id: 5, name: "5-10年"}, {id: 6, name: "10年以上"}, {id: 7, name: "在读"}];
                this.minSalaryOptions = this.generateSalaryOptions(0, 250);
                this.salaryMonthOptions = this.generateSalaryMonthOptions(11, 13);
                if (jobId) {
                    getJobInfo(jobId).then(response => {
                        const {data} = response;
                        this.jobForm.id = data.id;
                        this.jobForm.name = data.name;
                        this.jobForm.categoryId = data.category.id;
                        this.jobForm.depart = data.depart;
                        this.jobForm.minDegreeId = data.minDegree ? data.minDegree.id : undefined;
                        this.jobForm.minSalary = data.minSalary;
                        this.jobForm.maxSalary = data.maxSalary;
                        this.jobForm.salaryMonths = data.salaryMonths;
                        this.jobForm.cityId = data.city ? data.city.id : undefined;
                        this.jobForm.address = data.address;
                        this.jobForm.jobType = data.jobType ? data.jobType.id : undefined;
                        this.jobForm.description = data.description;
                        // this.jobForm.keywords = data.keywords; TODO 关键词重新编辑，需要开发
                        // this.jobForm.experienceOptions = data.experienceOptions; TODO 待解析
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
            generateSalaryOptions(minVal, length) {
                const salaryOptions = [];
                let step;

                if (minVal < 30) {
                    step = 1;
                } else if (minVal < 50) {
                    step = 2;
                } else if (minVal < 80) {
                    step = 5;
                } else {
                    step = 10;
                }
                for (let i = 0; i !== length; i++) {
                    minVal = minVal + step;
                    salaryOptions.push({
                        label: minVal + "k",
                        value: minVal
                    });
                }
                return salaryOptions;
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

        }
    };
</script>

<style scoped>
    .app-container {
        margin: 0px 100px;
        width: 800px;
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
            min-height: 200px;
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
                width: 700px;
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
