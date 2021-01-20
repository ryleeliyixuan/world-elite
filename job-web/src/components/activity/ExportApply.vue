<template>
    <el-dialog class="dialog"
               title="导出名单"
               :visible.sync="dialogVisible"
               v-loading="exporting"
               width="445px">
        <div class="content">
            <el-select v-model="query.status" placeholder="选择导出板块" class="select" size="small" v-if="activity.audit==='0'">
                <el-option
                    v-for="item in options"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                </el-option>
            </el-select>
            <div class="title-text">信息选择<span>(仅支持报名表默认信息)</span></div>

            <div class="select-container">
                <div class="column">
                    <div class="checkbox" style="cursor: default;">
                        <svg-icon icon-class="checked-disabled"></svg-icon>
                        <div>姓名</div>
                    </div>
                    <div class="checkbox" @click="onCheckBox('phoneFlag')" v-if="applyTable.phoneFlag">
                        <svg-icon :icon-class="query.phoneFlag?'checked':'unchecked'"></svg-icon>
                        <div>手机号</div>
                    </div>
                    <div class="checkbox" @click="onCheckBox('schoolFlag')" v-if="applyTable.schoolFlag">
                        <svg-icon :icon-class="query.schoolFlag?'checked':'unchecked'"></svg-icon>
                        <div>学校</div>
                    </div>
                    <div class="checkbox" @click="onCheckBox('gradeFlag')" v-if="applyTable.gradeFlag">
                        <svg-icon :icon-class="query.gradeFlag?'checked':'unchecked'"></svg-icon>
                        <div>年级</div>
                    </div>
                    <div class="checkbox" @click="onCheckBox('registrationPdfFlag')">
                        <svg-icon :icon-class="query.registrationPdfFlag?'checked':'unchecked'"></svg-icon>
                        <div>同时下载该板块所有用户报名表</div>
                    </div>
                    <div class="checkbox" @click="onCheckBox('resumePdfFlag')" v-if="applyTable.needResume==='1'">
                        <svg-icon :icon-class="query.resumePdfFlag?'checked':'unchecked'"></svg-icon>
                        <div>同时下载该板块所有用户简历</div>
                    </div>
                </div>
                <div class="column">
                    <div class="checkbox" @click="onCheckBox('genderFlag')" v-if="applyTable.genderFlag">
                        <svg-icon :icon-class="query.genderFlag?'checked':'unchecked'"></svg-icon>
                        <div>性别</div>
                    </div>
                    <div class="checkbox" @click="onCheckBox('emailFlag')" v-if="applyTable.emailFlag">
                        <svg-icon :icon-class="query.emailFlag?'checked':'unchecked'"></svg-icon>
                        <div>邮箱</div>
                    </div>
                    <div class="checkbox" @click="onCheckBox('professionFlag')" v-if="applyTable.professionFlag">
                        <svg-icon :icon-class="query.professionFlag?'checked':'unchecked'"></svg-icon>
                        <div>专业</div>
                    </div>
                    <div class="checkbox" @click="onCheckBox('educationFlag')" v-if="applyTable.educationFlag">
                        <svg-icon :icon-class="query.educationFlag?'checked':'unchecked'"></svg-icon>
                        <div>学历</div>
                    </div>
                </div>
            </div>
        </div>
        <div class="button-container">
            <div class="cancel" @click="onCancel">取消</div>
            <div class="confirm" @click="onConfirm">确认</div>
        </div>
    </el-dialog>
</template>

<script>
    import {downloadFile} from "@/utils/common"

    export default {
        name: "ExportApply",
        props: {
            visible: {
                type: Boolean
            },
            activity: {
                type: Object
            },
            applyTable:{
                type: Object
            }
        },
        computed: {
            dialogVisible: {
                get() {
                    return this.visible
                },
                set(val) {
                    this.$emit('update:visible', val)
                }
            }
        },
        data() {
            return {
                exporting: false,
                options: [{
                    value: undefined,
                    label: "导出全部"
                }, {
                    value: 2,
                    label: "导出已通过"
                }, {
                    value: 1,
                    label: "导出待处理"
                }],
                query: {
                    activityId: undefined, // 活动id
                    status: undefined, // 查询状态
                    nameFlag: 1, //是否导出姓名字段，0=不导出，1=导出，可以不传，默认为导出，建议必传
                    genderFlag: 1, // 性别
                    phoneFlag: 1, // 手机号
                    emailFlag: 1, // 邮箱
                    schoolFlag: 1, // 学校
                    professionFlag: 1, // 专业
                    gradeFlag: 1, // 年级
                    educationFlag: 1, // 学历
                    resumePdfFlag: 1, // PDF简历
                    registrationPdfFlag: 1 // PDF报名表
                }
            }
        },
        mounted() {
            this.query.resumePdfFlag = this.activity.needResume;
        },
        methods: {
            onCheckBox(key) {
                this.query[key] = this.query[key] === 1 ? 0 : 1;
            },

            // 取消
            onCancel() {
                this.dialogVisible = false;
            },

            // 确认
            onConfirm() {
                this.query.activityId = this.activity.id;
                this.exporting = true;
                this.$axios.post("/export/export-registration-list", this.query).then(data => {
                    if (data.data.registrationExcel) {
                        downloadFile({
                            fileKey: data.data.registrationExcel, fileName: '报名名单.xlsx', success: () => {
                                this.exporting = false;
                                this.$set(this, "dialogVisible", false);
                            }
                        });
                    }

                    if (data.data.registrationPdf) {
                        downloadFile({
                            fileKey: data.data.registrationPdf, fileName: '所有用户报名表.zip', success: () => {
                                this.exporting = false;
                                this.$set(this, "dialogVisible", false);
                            }
                        });
                    }

                    if (data.data.resumePdf) {
                        downloadFile({
                            fileKey: data.data.resumePdf, fileName: '所有用户简历.zip', success: () => {
                                this.exporting = false;
                                this.$set(this, "dialogVisible", false);
                            }
                        });
                    }
                })
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
            font-size: 21px;
            color: #333333;
            line-height: 29px;
        }

        ::v-deep .el-dialog__body {
            padding: 0 20px 30px;
        }

        .content {
            font-size: 16px;
            font-weight: 400;
            color: #666666;
            line-height: 22px;

            .select {
                width: 320px;
            }

            .title-text {
                font-size: 18px;
                color: #333333;
                margin-top: 13px;

                span {
                    font-size: 14px;
                    color: #999999;
                }

            }

            .select-container {
                display: flex;

                .column {
                    display: flex;
                    flex-direction: column;
                    margin-right: 38px;

                    .checkbox {
                        margin-top: 11px;
                        display: flex;
                        align-items: center;
                        cursor: pointer;

                        div {
                            margin-left: 9px;
                            font-size: 16px;
                            font-weight: 400;
                            color: #333333;
                            line-height: 22px;
                        }
                    }
                }
            }
        }

        .button-container {
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

            .confirm {
                margin-left: 21px;
                width: 107px;
                height: 35px;
                background: #4895EF;
                border-radius: 18px;
                border: 1px solid #FFFFFF;
                font-size: 16px;
                color: #FFFFFF;
                line-height: 35px;
                text-align: center;
                cursor: pointer;
            }
        }
    }

</style>
