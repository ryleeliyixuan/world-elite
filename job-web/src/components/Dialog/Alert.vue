<template>
    <el-dialog class="dialog" :title="title" :visible.sync="dialogVisible" width="480px" :show-close="false">
        <svg-icon v-if="icon" :icon-class="icon" class-name="icon" :style="iconStyle"></svg-icon>
        <div class="content" v-html="content">
        </div>
        <div class="button-container">
            <div class="confirm" @click="onConfirm">确认</div>
        </div>
    </el-dialog>
</template>

<script>
    export default {
        name: "Alert",
        props: {
            title: {
                type: String
            },
            visible: {
                type: Boolean
            },
            content: {
                type: String
            },
            icon: {
                type: String
            },
            iconStyle: {
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
            // 确认
            onConfirm() {
                this.$set(this, "dialogVisible", false);
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
            text-align: center;
        }

        .icon {
            margin-top: 20px;
            width: auto;
            height: auto;
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

            .confirm {
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