<template>
    <el-dialog class="dialog" :title="title" :visible.sync="dialogVisible" width="480px" :show-close="false">
        <div class="content" v-html="content">
        </div>
        <div class="button-container">
            <div class="cancel" @click="onCancel">取消</div>
            <div class="confirm" @click="onConfirm">确认</div>
        </div>
    </el-dialog>
</template>

<script>
    export default {
        name: "Confirm",
        props: {
            title: {
                type: String
            },
            visible: {
                type: Boolean
            },
            content: {
                type: String
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
            },
        },
        methods: {
            // 取消关闭弹框
            onCancel() {
                this.$set(this, "dialogVisible", false);
            },

            // 确认
            onConfirm() {
                this.$emit('confirm');
                this.onCancel();
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
            text-align: center;
            margin-top: 20px;
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