<template>
    <div class="app-container">
        <div class=" main-container">
            <div class="a" v-html="agreement1"/>
            <div class="ab" v-html="agreement1"/>
            <div class="b" v-html="agreement2"/>
        </div>
        <div class="check-container" @click="onAgree">
            <div class="check-circle">
                <div class="inner-circle" v-if="checked"></div>
            </div>
            <div class="check-text">我已阅读并同意以上协议。</div>
        </div>
        <div class="bottom-button">
            <el-button class="Btn" @click="onSubmit" v-if="checked===true">确定</el-button>
            <el-button class="Btn1" v-if="checked===false" disabled>确定</el-button>
        </div>
    </div>
</template>
<script>
    import {agreement} from "@/utils/agreement";

    export default {
        data() {
            return {
                checked: false,
                agreement1: agreement.title,
                agreement2: agreement.content
            }
        },
        created() {
            this.$emit("complete");
        },
        methods: {
            onAgree() {
                this.checked = !this.checked
            },
            onSubmit() {
                this.$router.push("/RegisterInterviewerPage");
            },
        }
    }
</script>
<style scoped lang="scss">
    .app-container {
        max-width: 1140px;
        margin: 0 auto;
        min-height: calc(100vh - 477px);
        padding-bottom: 200px;

        .check-container {
            display: flex;
            align-items: center;
            margin-top: 20px;
            margin-left: 20px;
            cursor: pointer;

            .check-circle {
                width: 24px;
                height: 24px;
                border-radius: 50%;
                border: 1px solid #3E70F5;
                display: flex;
                align-items: center;
                justify-content: center;

                .inner-circle {
                    width: 12px;
                    height: 12px;
                    border-radius: 50%;
                    background-color: #3E70F5;
                }
            }

            .check-text {
                font-size: 18px;
                color: #3D6FF4;
                margin-left: 13px;
            }
        }

        .main-container {
            background: #F5F5F5;

            .a {
                /*margin-left: 100px;*/
                height: 41px;
                background: #3D6FF4;
                border-radius: 17px 17px 0 0;
                font-size: 18px;
                font-family: PingFangSC-Medium, PingFang SC;
                color: #FFFFFF;
                line-height: 41px;
                padding-left: 10px;
            }

            .align-center {
                width: 885px;
                background: #F5F5F5;
                border-radius: 17px;
                margin: 0 auto;
            }

            .ab {
                padding-left: 400px;
                height: 33px;
                font-size: larger;
                font-family: PingFangSC-Medium, PingFang SC;
                font-weight: 500;
                color: #333333;
                line-height: 33px;
            }

            .b {
                padding: 20px;
            }
        }
    }

    .bottom-button {
        text-align: center;

        .Btn {
            width: 332px;
            background: #EDF3FF;
            border-radius: 24px;
            font-size: 24px;
            font-family: PingFangSC-Medium, PingFang SC;
            color: #999999;
            line-height: 25px;
        }

        .Btn1 {
            width: 332px;
            background: #FFFFFF;
            border-radius: 24px;
            font-size: 24px;
            color: #999999;
            line-height: 25px;
        }
    }

    @media screen and (max-width: 850px) {
        .app-container {
            flex-direction: column;
            align-items: center;

        }
    }
</style>

