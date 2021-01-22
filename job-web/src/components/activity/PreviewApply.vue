<template>
    <el-dialog class="dialog"
               v-if="apply"
               :title="apply.title"
               center
               :visible.sync="dialogVisible"
               width="900px">
        <div class="content">
            <div class="form-item">
                <div class="name"><span>*</span>姓名</div>
                <el-input size="small" class="value" v-model="applyForm.name"></el-input>
            </div>
            <div class="form-item" v-if="apply.genderFlag!=='2'">
                <div class="name"><span v-if="apply.genderFlag==='0'">*</span>性别</div>
                <div class="value">
                    <div class="value-item" @click="applyForm.gender=1">
                        <svg-icon :icon-class="applyForm.gender===1?'apply-table-selected':'apply-table-unselected'" clickable></svg-icon>
                        <span class="radio-value">男</span>
                    </div>
                    <div class="value-item" @click="applyForm.gender=2">
                        <svg-icon :icon-class="applyForm.gender===2?'apply-table-selected':'apply-table-unselected'" clickable></svg-icon>
                        <span class="radio-value">女</span>
                    </div>
                </div>
            </div>
            <div class="form-item" v-if="apply.phoneFlag!=='2'">
                <div class="name"><span v-if="apply.phoneFlag==='0'">*</span>手机号</div>
                <el-input size="small" class="value" v-model="applyForm.phone"></el-input>
            </div>
            <div class="form-item" v-if="apply.emailFlag!=='2'">
                <div class="name"><span v-if="apply.emailFlag==='0'">*</span>邮箱</div>
                <el-input size="small" class="value" v-model="applyForm.email"></el-input>
            </div>
            <div class="form-item" v-if="apply.schoolFlag!=='2'">
                <div class="name"><span v-if="apply.schoolFlag==='0'">*</span>学校</div>
                <el-input size="small" class="value" v-model="applyForm.school"></el-input>
            </div>
            <div class="form-item" v-if="apply.gradeFlag!=='2'">
                <div class="name"><span v-if="apply.gradeFlag==='0'">*</span>年级</div>
                <el-input size="small" class="value" v-model="applyForm.grade"></el-input>
            </div>
            <div class="form-item" v-if="apply.professionFlag!=='2'">
                <div class="name"><span v-if="apply.professionFlag==='0'">*</span>专业</div>
                <el-input size="small" class="value" v-model="applyForm.profession"></el-input>
            </div>
            <div class="form-item" v-if="apply.educationFlag!=='2'">
                <div class="name"><span v-if="apply.educationFlag==='0'">*</span>学历</div>
                <div class="value">
                    <div class="value-item" v-for="education in educationList" :key="education.id" @click="applyForm.educationId = education.id">
                        <svg-icon :icon-class="education.id===applyForm.educationId?'apply-table-selected':'apply-table-unselected'" clickable></svg-icon>
                        <span class="radio-value">{{education.name}}</span>
                    </div>
                </div>
            </div>
            <div v-for="(question, index) in apply.questionnaireList">
                <div v-if="question.type==='1'" class="question1-container">
                    <div class="question1-line1">
                        <div class="title"><span class="must" v-if="question.mustAnswer">*</span>{{question.title}}</div>
                    </div>
                    <div class="question1-line2">
                        <el-input v-model="question.answer" size="small"></el-input>
                    </div>
                </div>
                <div v-else-if="question.type==='2'" class="question2-container">
                    <div class="question2-line1">
                        <div class="title"><span class="must" v-if="question.mustAnswer">*</span>{{question.title}}</div>
                    </div>
                    <div class="question2-line2">
                        <div v-for="(option, index) in question.optionsList" class="option-container" @click="onRadioClick(question.optionsList, index)">
                            <svg-icon :icon-class="option.checked?'apply-table-selected':'apply-table-unselected'" class-name="icon" clickable></svg-icon>
                            <div class="options">{{option.options}}</div>
                        </div>
                    </div>
                </div>
                <div v-else-if="question.type==='3'" class="question3-container">
                    <div class="question3-line1">
                        <div class="title"><span class="must" v-if="question.mustAnswer">*</span>{{question.title}}</div>
                    </div>
                    <div class="question3-line2">
                        <div v-for="(option, index) in question.optionsList" class="option-container" @click="onCheckBoxClick(question.optionsList, index)">
                            <svg-icon :icon-class="option.checked?'apply-table-checked':'apply-table-unchecked'" class-name="icon" clickable></svg-icon>
                            <div class="options">{{option.options}}</div>
                        </div>
                    </div>
                </div>
                <div v-else-if="question.type==='4'" class="question4-container">
                    <div class="question4-line1">
                        <div class="title"><span class="must" v-if="question.mustAnswer">*</span>{{question.title}}</div>
                    </div>
                    <div class="question4-line2">
                        <el-upload class="upload-button"
                                   :action="uploadPicOptions.action"
                                   :data="uploadPicOptions.params"
                                   :accept="uploadPicOptions.acceptFileType"
                                   :show-file-list="false"
                                   :loading="uploadPicOptions.loading"
                                   :on-success="handleUploadSuccess"
                                   :before-upload="beforeUpload">
                            <span class="upload-button" @click="onFileUpload(question)">上传附件</span>
                        </el-upload>
                        <div :class="question.fileName?'text':'placeholder'">{{question.fileName || '未选择任何文件'}}</div>
                    </div>
                </div>
            </div>
        </div>
        <div class="resume-container" v-if="needResume==='1'">
            <div class="resume-title"><span>*</span>请提交在线简历</div>
            <el-select v-model="applyForm.resumeId" placeholder="选择在线简历版本" size="small" class="resume-select">
                <el-option v-for="item in resumeList"
                           size="mini"
                           :key="item.id"
                           :label="item.name"
                           :value="item.id">
                </el-option>
            </el-select>
        </div>
        <div v-if="submit" class="resume-button-container">
            <div class="cancel" @click="onCancel">取消</div>
            <div class="submit" @click="onSubmit">提交</div>
        </div>
        <div v-else class="button-container">
            <div class="cancel" @click="onCancel">关闭</div>
        </div>
    </el-dialog>
</template>

<script>
    import {getUploadPicToken} from "@/api/upload_api";
    import {checkPicSize} from "@/utils/common";

    export default {
        name: "PreviewApply",
        props: {
            visible: {
                type: Boolean
            },
            submit: {
                type: Boolean
            },
            apply: {
                type: Object
            },
            activityId: {
                type: String
            },
            resumeList: {
                type: Array
            },
            needResume: {
                type: String
            }
        },
        computed: {
            dialogVisible: {
                get() {
                    return this.visible;
                },
                set(val) {
                    this.$emit('update:visible', val);
                }
            }
        },
        data() {
            return {
                applyForm: { // 报名表单
                    activityId: undefined, // 活动id
                    name: undefined, // 姓名
                    gender: undefined, // 性别 1=男，2=女
                    phone: undefined, // 手机号
                    email: undefined, // 邮箱
                    school: undefined, // 学校
                    grade: undefined, // 年级
                    profession: undefined, // 专业
                    educationId: undefined, // 学历Id，通过获取学历列表接口获取
                    resumeId: undefined, // 活动不需要上传简历时可为空，通过获取简历列表接口获取
                    answerList: undefined // 答案列表
                },

                questionFileUpload: {}, // 当前上传附件的题目
                uploadPicOptions: {
                    loading: false,
                    action: "",
                    params: {},
                    fileUrl: "",
                    acceptFileType: ".jpg,.jpeg,.png,.JPG,.JPEG,.PNG",
                    fileName: undefined
                },
                educationList: [],
            }
        },
        mounted() {
            this.applyForm.activityId = this.activityId;

            // 加载学历列表
            this.$axios.get('/dict/list', {params: {type: 25, limit: 99}}).then(response => {
                this.educationList = response.data.list;
            })
        },
        methods: {
            // 单选选中
            onRadioClick(optionList, index) {
                optionList.forEach((option, i) => {
                    this.$set(option, 'checked', i === index);
                })
            },

            // 多选选中
            onCheckBoxClick(optionList, index) {
                this.$set(optionList[index], 'checked', !optionList[index].checked);
            },

            // 图片上传成功
            handleUploadSuccess(response, file, fileList) {
                this.questionFileUpload.fileUrl = this.uploadPicOptions.fileUrl;
                this.questionFileUpload.fileName = this.uploadPicOptions.fileName;
                this.uploadPicOptions.loading = false;
            },

            // 附件上传获取上传信息
            beforeUpload(file) {
                return new Promise((resolve, reject) => {
                    if (checkPicSize(file)) {
                        reject();
                    } else {
                        this.uploadPicOptions.loading = true;
                        this.uploadPicOptions.fileName = file.name;
                        getUploadPicToken(file.name).then((response) => {
                            const {data} = response;
                            this.uploadPicOptions.action = data.host;
                            this.uploadPicOptions.params = data;
                            this.uploadPicOptions.fileUrl = data.host + "/" + data.key;
                            resolve(data);
                        }).catch((error) => {
                            reject(error);
                        });
                    }
                });
            },

            // 上传附件
            onFileUpload(question) {
                this.questionFileUpload = question;
            },

            // 取消
            onCancel() {
                this.dialogVisible = false;
            },

            // 提交
            onSubmit() {
                if (this.checkParams()) {
                    this.applyForm.answerList = this.apply.questionnaireList.map(item => {
                        let answer = {questionnaireId: undefined, answerContentList: undefined, answerOptionsIdList: undefined};
                        answer.questionnaireId = item.id;
                        switch (item.type) {
                            case '1':
                                answer.answerContentList = [item.answer];
                                break;
                            case '2':
                                answer.answerOptionsIdList = item.optionsList.filter(option => option.checked).map(option => option.id);
                                break;
                            case '3':
                                answer.answerOptionsIdList = item.optionsList.filter(option => option.checked).map(option => option.id);
                                break;
                            case '4':
                                answer.answerContentList = [item.fileUrl];
                                break;
                        }
                        return answer;
                    })
                    this.$axios.post('/registration', this.applyForm).then(() => {
                        this.$message.success("报名成功");
                        this.$set(this, 'dialogVisible', false);
                    })
                }
            },

            // 参数检查
            checkParams() {
                let message = undefined;

                let index = this.apply.questionnaireList.findIndex(question => {
                    let result = false;
                    if (question.mustAnswer === '1') {
                        switch (question.type) {
                            case '1':
                                result = !question.answer;
                                break;
                            case '2':
                            case '3':
                                result = question.optionsList.filter(q => q.checked===true).length === 0;
                                break;
                            case '4':
                                result = !question.fileUrl;
                                break;
                        }
                    }
                    return result;
                });

                if (!this.applyForm.name) {
                    message = "请输入姓名";
                } else if (this.apply.genderFlag === '0' && !this.applyForm.gender) {
                    message = "请选择性别";
                } else if (this.apply.phoneFlag === '0' && !this.applyForm.phone) {
                    message = "请输入手机号";
                } else if (this.apply.emailFlag === '0' && !this.applyForm.email) {
                    message = "请输入邮箱";
                } else if (this.apply.schoolFlag === '0' && !this.applyForm.school) {
                    message = "请输入学校";
                } else if (this.apply.gradeFlag === '0' && !this.applyForm.grade) {
                    message = "请输入年级";
                } else if (this.apply.professionFlag === '0' && !this.applyForm.profession) {
                    message = "请输入专业";
                } else if (this.apply.educationFlag === '0' && !this.applyForm.educationId) {
                    message = "请选择学历";
                } else if (index >= 0) {
                    message = "请完成其他必答题";
                } else if (this.needResume === '1' && !this.applyForm.resumeId) {
                    message = "请选择在线简历";
                }
                !!message && this.$message.warning(message);
                return !message;
            }
        }
    }
</script>

<style scoped lang="scss">
    .dialog {
        ::v-deep .el-dialog {
            border-radius: 13px;
        }

        ::v-deep .el-dialog__title {
            font-size: 30px;
            color: #333333;
            line-height: 42px;
            font-weight: bold;
        }

        ::v-deep .el-dialog__body {
            padding: 0 90px 50px;
        }

        .content {

            .form-item {
                display: flex;
                align-items: center;
                margin-top: 14px;

                .name {
                    min-width: 80px;
                    font-size: 18px;
                    font-weight: 400;
                    color: #666666;
                    line-height: 25px;

                    span {
                        color: red;
                        margin-right: 5px;
                    }
                }

                .value-item {
                    display: flex;
                    align-items: center;
                    margin-left: 21px;
                    cursor: pointer;

                    &:first-of-type {
                        margin-left: 0;
                    }

                    .radio-value {
                        margin-left: 5px;
                        font-size: 16px;
                        color: #333333;
                        line-height: 22px;
                    }

                    .hide {
                        margin-left: 5px;
                        font-size: 14px;
                        color: #4895EF;
                        line-height: 20px;
                    }

                    .optional {
                        margin-left: 5px;
                        font-size: 14px;
                        color: #666666;
                        line-height: 20px;
                    }
                }

                .value {
                    width: auto;
                    min-width: 277px;
                    display: flex;
                    align-items: center;

                    ::v-deep .el-input__inner {
                        width: 277px;
                        background: #FFFFFF;
                        border-radius: 16px;
                    }
                }
            }

            .question1-container {
                margin-top: 18px;

                .question1-line1 {
                    display: flex;
                    align-items: flex-start;
                    justify-content: space-between;

                    .title {
                        font-size: 18px;
                        color: #666666;
                        line-height: 25px;

                        .must {
                            color: red;
                            margin-right: 5px;
                        }
                    }
                }

                .question1-line2 {
                    margin-top: 18px;

                    ::v-deep .el-input__inner {
                        font-size: 16px;
                        color: #333333;
                        line-height: 30px;
                        width: 720px;
                        height: 30px;
                        background: #FFFFFF;
                        border-radius: 15px;
                        border: 1px solid #CCDBF5;
                    }
                }
            }

            .question2-container {
                margin-top: 18px;

                .question2-line1 {
                    display: flex;
                    align-items: flex-start;
                    justify-content: space-between;

                    .title {
                        font-size: 18px;
                        color: #666666;
                        line-height: 25px;

                        .must {
                            color: red;
                            margin-right: 5px;
                        }
                    }
                }

                .question2-line2 {
                    display: flex;
                    flex-wrap: wrap;
                    margin-top: 7px;
                    width: calc(100% + 70px);

                    .option-container {
                        display: flex;
                        align-items: flex-start;
                        margin: 7px 70px 7px 0;
                        cursor: pointer;

                        .icon {
                            display: flex;
                            align-items: center;
                            height: 25px;
                        }

                        .options {
                            margin-left: 10px;
                            font-size: 18px;
                            font-weight: 500;
                            color: #666666;
                            line-height: 25px;
                        }
                    }
                }
            }

            .question3-container {
                margin-top: 18px;

                .question3-line1 {
                    display: flex;
                    align-items: flex-start;
                    justify-content: space-between;

                    .title {
                        font-size: 18px;
                        color: #666666;
                        line-height: 25px;

                        .must {
                            color: red;
                            margin-right: 5px;
                        }
                    }
                }

                .question3-line2 {
                    display: flex;
                    flex-wrap: wrap;
                    margin-top: 7px;
                    width: calc(100% + 70px);

                    .option-container {
                        display: flex;
                        align-items: flex-start;
                        margin: 7px 70px 7px 0;
                        cursor: pointer;

                        .icon {
                            display: flex;
                            align-items: center;
                            height: 25px;
                        }

                        .options {
                            margin-left: 10px;
                            font-size: 18px;
                            font-weight: 500;
                            color: #666666;
                            line-height: 25px;
                        }
                    }
                }
            }

            .question4-container {
                margin-top: 18px;

                .question4-line1 {
                    display: flex;
                    align-items: flex-start;
                    justify-content: space-between;

                    .title {
                        font-size: 18px;
                        color: #666666;
                        line-height: 25px;

                        .must {
                            color: red;
                            margin-right: 5px;
                        }
                    }
                }

                .question4-line2 {
                    margin-top: 18px;
                    width: 100%;
                    max-width: 720px;
                    height: 30px;
                    background: #FFFFFF;
                    box-shadow: 0 2px 6px 0 #AAC5D8;
                    border-radius: 15px;
                    display: flex;
                    align-items: center;
                    padding: 0 3px;

                    .upload-button {
                        width: 84px;
                        height: 24px;
                        background: #4895EF;
                        border-radius: 12px;
                        font-size: 14px;
                        font-weight: 400;
                        color: #FFFFFF;
                        line-height: 24px;
                        text-align: center;
                        cursor: pointer;
                    }

                    .placeholder {
                        font-size: 14px;
                        font-weight: 400;
                        color: #B0BEC5;
                        line-height: 20px;
                        margin-left: 9px;
                    }

                    .text {
                        font-size: 14px;
                        font-weight: 400;
                        color: #666666;
                        line-height: 20px;
                        margin-left: 9px;
                    }
                }
            }
        }

        .resume-container {
            display: flex;
            flex-direction: column;
            border-top: 1px solid #CFD8DC;
            margin: 18px -20px 0;
            padding: 18px 20px;

            .resume-title {
                font-size: 18px;
                color: #333333;
                line-height: 25px;

                span {
                    color: red;
                    font-size: 18px;
                    line-height: 25px;
                }
            }

            .resume-select {
                width: 277px;
                margin-top: 13px;

                ::v-deep .el-input__inner {
                    width: 277px;
                    background: #FFFFFF;
                    border-radius: 16px;
                }
            }
        }

        .resume-button-container {
            display: flex;
            align-items: center;
            justify-content: center;
            margin-top: 34px;

            .cancel {
                width: 107px;
                height: 35px;
                background: #FFFFFF;
                border-radius: 18px;
                border: 1px solid #4895EF;
                font-size: 16px;
                color: #4895EF;
                line-height: 35px;
                text-align: center;
                cursor: pointer;
            }

            .submit {
                width: 106px;
                height: 35px;
                background: #4895EF;
                border-radius: 18px;
                font-size: 16px;
                font-weight: 500;
                color: #FFFFFF;
                line-height: 35px;
                text-align: center;
                margin-left: 21px;
                cursor: pointer;
            }
        }

        .button-container {
            display: flex;
            align-items: center;
            justify-content: center;
            margin-top: 34px;

            .cancel {
                width: 134px;
                height: 35px;
                background: #ECEFF1;
                border-radius: 18px;
                border: 1px solid #ECEFF1;
                font-size: 16px;
                color: #333333;
                line-height: 35px;
                text-align: center;
                cursor: pointer;
            }
        }
    }

</style>
