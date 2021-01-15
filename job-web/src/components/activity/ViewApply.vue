<template>
    <el-dialog class="dialog"
               :visible.sync="dialogVisible"
               v-loading="exporting"
               width="618px"
               :show-close="false">
        <div slot="title" class="dialog-title">
            <div class="title-left">
                <svg-icon icon-class="left-icon" class="left-icon" @click="onLeft"/>
                <svg-icon icon-class="right-icon" class="right-icon" @click="onRight"/>

                <!-- TODO 各种按钮状态-->
                <div class="pass-button" v-if="activity.audit==='0'">通过报名</div>
                <div class="fail-button" v-if="activity.audit==='0'">不合适</div>
            </div>
            <div class="title-right">
                <svg-icon icon-class="preview-icon" class="preview-icon"/>
                <svg-icon icon-class="download-icon" class="download-icon"/>
                <svg-icon icon-class="close-icon" class="close-icon" @click="onDialogClose"/>
            </div>
        </div>

        <div class="content-container" v-if="data">
            <div class="title-container">
                <div class="number">No.0001</div>
                <div class="title">各地留学生报名分享会报名表</div>
            </div>
            <div class="form-container">
                <div class="item"><span class="required">*</span> <span class="name">姓名：</span> <span class="value">何大蕾</span></div>
                <div class="item"><span class="required">*</span> <span class="name">性别：</span> <span class="value">何大蕾</span></div>
                <div class="item"><span class="required">*</span> <span class="name">手机：</span> <span class="value">何大蕾</span></div>
                <div class="item"><span class="required">*</span> <span class="name">邮箱：</span> <span class="value">何大蕾</span></div>
                <div class="item"><span class="required">*</span> <span class="name">学校：</span> <span class="value">何大蕾</span></div>
                <div class="item"><span class="required">*</span> <span class="name">学历：</span> <span class="value">何大蕾</span></div>
                <div class="item" v-if="data.localUrl">
                    <span class="image-name">学生证：</span>
                    <el-image :src="data.localUrl" fit="scale-down" class="image"/>
                </div>
                <div class="item">
                    <span class="image-name">参加场次：</span>
                    <span class="value">{{getTime()}}</span>
                </div>
            </div>
        </div>
    </el-dialog>
</template>

<script>

    export default {
        name: "ViewApply",
        props: {
            visible: {
                type: Boolean
            },
            activity: {
                type: Object
            },
            data: {
                type: Object
            },
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
                exporting: false, // 加载

                studentCardUploadPicOptions: { // 活动海报上传
                    loading: false,
                    action: "",
                    params: {},
                    fileUrl: "",
                    localUrl: "",
                    acceptFileType: ".jpg,.jpeg,.png,.JPG,.JPEG,.PNG",
                },
            }
        },
        mounted() {
            this.initData();
        },
        methods: {
            initData() {
                this.$axios.get(`/registration/${this.data.id}`).then(data => {
                    console.log(data)
                })
            },

            // 转换参加场次时间
            getTime() {
                return '12月25日 14：00';
            },

            // 关闭对话框
            onDialogClose() {
                this.$set(this, "dialogVisible", false);
            },

            // 查看上一个
            onLeft() {

            },

            // 查看下一个
            onRight() {

            }
        }
    }
</script>

<style scoped lang="scss">
    .dialog {
        ::v-deep .el-dialog {
            border-radius: 13px;
            padding: 20px 17px;
        }

        /deep/ .el-dialog__header {
            padding: 0;
        }

        .dialog-title {
            width: 100%;
            display: flex;
            align-items: center;
            justify-content: space-between;
            border-bottom: 1px solid #CFD8DC;
            padding-bottom: 15px;

            .title-left {
                display: flex;
                align-items: center;

                .left-icon {
                    width: 26px;
                    height: 26px;
                    cursor: pointer;
                }

                .right-icon {
                    width: 26px;
                    height: 26px;
                    margin-left: 17px;
                    cursor: pointer;
                }

                .pass-button {
                    width: 96px;
                    height: 28px;
                    background: #4895EF;
                    border-radius: 21px;
                    font-size: 16px;
                    color: #FFFFFF;
                    margin-left: 14px;
                    text-align: center;
                    line-height: 28px;
                    cursor: pointer;
                }

                .fail-button {
                    width: 96px;
                    height: 28px;
                    background: #E3F0FF;
                    border-radius: 21px;
                    border: 1px solid #4895EF;
                    font-size: 16px;
                    color: #4895EF;
                    margin-left: 14px;
                    text-align: center;
                    line-height: 28px;
                    cursor: pointer;
                }
            }

            .title-right {
                display: flex;
                align-items: center;

                .preview-icon {
                    width: 25px;
                    height: 29px;
                    cursor: pointer;
                }

                .download-icon {
                    width: 29px;
                    height: 23px;
                    margin-left: 14px;
                    cursor: pointer;
                }

                .close-icon {
                    width: 29px;
                    height: 29px;
                    margin-left: 14px;
                    cursor: pointer;
                }
            }
        }

        /deep/ .el-dialog__body {
            padding: 0;
        }

        .content-container {
            width: 100%;
            padding: 12px 47px 35px;

            .title-container {
                display: flex;
                align-items: center;
                justify-content: center;
                position: relative;

                .number {
                    position: absolute;
                    left: 0;
                    transform: translateY(-50%);
                    top: 50%;
                    font-size: 14px;
                    color: #333333;
                }

                .title {
                    font-size: 21px;
                    color: #333333;
                }
            }

            .form-container {

                .item {
                    display: flex;
                    align-items: center;
                    margin-top: 10px;

                    .required {
                        color: red;
                        margin-right: 5px;
                    }

                    .name {
                        font-size: 16px;
                        font-weight: 400;
                        color: #666666;
                        line-height: 22px;
                    }

                    .value {
                        font-size: 16px;
                        font-weight: 400;
                        color: #333333;
                        line-height: 22px;
                    }

                    .image-name {
                        font-size: 16px;
                        font-weight: 400;
                        color: #666666;
                        line-height: 22px;
                        margin-left: 10px;
                    }

                    .image {
                        width: 284px;
                        height: 160px;
                    }
                }
            }
        }
    }

</style>
