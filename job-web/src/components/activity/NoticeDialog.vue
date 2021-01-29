<template>
    <el-dialog class="dialog"
               :visible.sync="dialogVisible"
               center
               width="445px">
        <div slot="title" style="display: flex; align-items: center; justify-content: center;">
            <span style="font-size: 18px;color: #333333;line-height: 25px;font-weight: bold;">发送通知消息</span>
            <el-tooltip effect="dark" placement="top">
                <div slot="content">发送通知后报名者会收到站内<br/>信及邮件提醒</div>
                <svg-icon icon-class="questionMask" style="width: 10px; height: 11px; margin-left: 4px;"></svg-icon>
            </el-tooltip>
        </div>
        <div class="content">
            <el-input type="textarea"
                      :rows="3"
                      placeholder="恭喜您成功报名“各地留学生交流分享会”。"
                      v-model="message"
                      maxlength="300"
                      resize="none"
                      show-word-limit
                      class="reason">
            </el-input>
            <el-checkbox v-model="notNotice">该活动默认发送此通知不再确认</el-checkbox>
        </div>
        <div class="button-container">
            <div class="cancel" @click="onCancel">取消</div>
            <div class="confirm" @click="onConfirm">确认</div>
        </div>
    </el-dialog>
</template>

<script>

    export default {
        name: "NoticeDialog",
        props: {
            visible: {
                type: Boolean
            },
            activityId: {
                type: Number
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
                message: "恭喜您成功报名“各地留学生交流分享会”。", // 通知消息
                notNotice: false, // 是否不再显示通知消息对话框
            }
        },
        mounted() {
        },
        methods: {
            // 取消
            onCancel() {
                this.dialogVisible = false;
            },

            // 确认
            onConfirm() {
                // TODO 提交通知消息
                console.log(this.message);
                if (this.notNotice) {
                    // TODO  提交不在提示通知消息
                    console.log("提交不在提示通知消息");
                }
                this.$emit("confirm")
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
            display: flex;
            flex-direction: column;
            align-items: center;

            .reason {
                margin-top: 17px;
                margin-bottom: 6px;
                font-size: 18px;
                color: #333333;
                line-height: 25px;

                ::-webkit-input-placeholder {
                    font-size: 16px;
                    line-height: 25px;
                }

                :-moz-placeholder {
                    font-size: 16px;
                    line-height: 25px;
                }

                :-ms-input-placeholder {
                    font-size: 16px;
                    line-height: 25px;
                }

                ::v-deep .el-textarea__inner {
                    width: 395px;
                    height: 93px;
                    background: #FFFFFF;
                    border-radius: 6px;
                    border: 1px solid #B0BEC5;
                }
            }
        }

        .button-container {
            display: flex;
            align-items: center;
            justify-content: center;

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
