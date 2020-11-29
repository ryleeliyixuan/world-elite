<template>
    <div class="app-container">
        <div class=" main-container">

            <div class="main-scrollbar">
                <el-form class="a">
                    <ul v-html="agreement1" style="overflow:auto">
                        <li v-for="i in agreement1" class="infinite-list-item">{{ i }}</li>
                    </ul>
                </el-form>
                <el-form class="ab">
                    <ul v-html="agreement1" style="overflow:auto">
                        <li v-for="i in agreement1" class="infinite-list-item">{{ i }}</li>
                    </ul>
                </el-form>
                <el-form class="b">
                    <ul v-html="agreement2" style="overflow:auto">
                        <li v-for="j in agreement2" class="infinite-list-item">{{ j }}</li>
                    </ul>
                </el-form>
            </div>
        </div>
        <div class="ab">
            <el-checkbox v-model="checked">我已阅读并同意以上协议。</el-checkbox>

        </div>
        <div style="padding-left: 400px;padding-top: 80px">
            <el-button class="Btn" @click="onSubmit" v-if="checked===true">确定</el-button>
            <el-button class="Btn1" @click="onSubmit" v-if="checked===false" disabled>确定</el-button>
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
            onSubmit(evt) {
                evt.preventDefault();
                let query = {
                    ...this.$route.query
                };
                delete query.redirect;
                this.$router.push({
                    path: this.redirect || "/RegisterInterviewerPage",
                    query
                });
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
        .main-container {
            overflow-y: auto;
            background: #F5F5F5;
                .a {
                    /*margin-left: 100px;*/
                    height: 41px;
                    background: #3D6FF4;
                    border-radius: 17px 17px 0px 0px;
                    font-size: 18px;
                    font-family: PingFangSC-Medium, PingFang SC;
                    font-weight: 500;
                    color: #FFFFFF;
                    line-height: 25px;


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
            }
        }

        .Btn {

            width: 332px;
            height: 47px;
            background: #EDF3FF;
            border-radius: 24px;
            font-size: 24px;
            font-family: PingFangSC-Medium, PingFang SC;
            font-weight: 500;
            color: #999999;
            line-height: 25px;
        }

        .Btn1 {

            width: 332px;
            height: 47px;
            background: #FFFFFF;
            border-radius: 24px;
            font-size: 24px;
            font-family: PingFangSC-Medium, PingFang SC;
            font-weight: 500;
            color: #999999;
            line-height: 25px;
        }



    @media screen and (max-width: 850px) {
        .app-container {
            flex-direction: column;
            align-items: center;

        }
    }
</style>
