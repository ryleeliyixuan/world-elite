<template>
<div class="dialog">
    <div class="content-container" v-if="applyInfo">
        <div class="title-container">
            <div class="number">{{number}}</div>
            <div class="title">各地留学生报名分享会报名表</div>
        </div>
        <div class="form-container">
            <div class="item"><span class="required">*</span> <span class="name">姓名：</span> <span class="value">{{applyInfo.name}}</span></div>
            <div class="item" v-if="applyInfo.genderFlag!=='2'">
                <span class="required">{{applyInfo.genderFlag==='0'?'*':''}}</span>
                <span class="name">性别：</span>
                <span class="value">{{applyInfo.gender==='1'?'男':applyInfo.gender==='2'?'女':''}}</span>
            </div>
            <div class="item" v-if="applyInfo.phoneFlag!=='2'">
                <span class="required">{{applyInfo.phoneFlag==='0'?'*':''}}</span>
                <span class="name">手机：</span>
                <span class="value">{{applyInfo.phone}}</span>
            </div>
            <div class="item" v-if="applyInfo.emailFlag!=='2'">
                <span class="required">{{applyInfo.emailFlag==='0'?'*':''}}</span>
                <span class="name">邮箱：</span>
                <span class="value">{{applyInfo.email}}</span>
            </div>
            <div class="item" v-if="applyInfo.schoolFlag!=='2'">
                <span class="required">{{applyInfo.schoolFlag==='0'?'*':''}}</span>
                <span class="name">学校：</span>
                <span class="value">{{applyInfo.school}}</span>
            </div>
            <div class="item" v-if="applyInfo.professionFlag!=='2'">
                <span class="required">{{applyInfo.professionFlag==='0'?'*':''}}</span>
                <span class="name">专业：</span>
                <span class="value">{{applyInfo.profession}}</span>
            </div>
            <div class="item" v-if="applyInfo.educationFlag!=='2'">
                <span class="required">{{applyInfo.educationFlag==='0'?'*':''}}</span>
                <span class="name">学历：</span>
                <span class="value">{{applyInfo.education && applyInfo.education.name}}</span>
            </div>
            <div class="question-container" v-for="(question, index) in applyInfo.questionnaireList" :key="index">
                <div v-if="question.type==='1'" class="question1-container">
                    <div class="question1-line1">
                        <div class="title"><span class="must" v-if="question.mustAnswer==='1'">*</span>{{question.title}}</div>
                    </div>
                    <div class="question1-line2">
                        <div class="answer">{{question.answer}}</div>
                    </div>
                </div>
                <div v-else-if="question.type==='2'" class="question2-container">
                    <div class="question2-line1">
                        <div class="title"><span class="must" v-if="question.mustAnswer==='1'">*</span>{{question.title}}</div>
                    </div>
                    <div class="question2-line2">
                        <div v-for="(option, index) in question.questionnaireOptions" class="option-container" :key="index">
                            <svg-icon :icon-class="option.checked?'apply-table-selected':'apply-table-unselected'" class-name="icon"></svg-icon>
                            <div class="options">{{option.options}}</div>
                        </div>
                    </div>
                </div>
                <div v-else-if="question.type==='3'" class="question3-container">
                    <div class="question3-line1">
                        <div class="title"><span class="must" v-if="question.mustAnswer==='1'">*</span>{{question.title}}</div>
                    </div>
                    <div class="question3-line2">
                        <div v-for="(option, index) in question.questionnaireOptions" class="option-container" :key="index">
                            <svg-icon :icon-class="option.checked?'apply-table-checked':'apply-table-unchecked'" class-name="icon"></svg-icon>
                            <div class="options">{{option.options}}</div>
                        </div>
                    </div>
                </div>
                <div v-else-if="question.type==='4'" class="question4-container">
                    <div class="question4-line1">
                        <div class="title"><span class="must" v-if="question.mustAnswer==='1'">*</span>{{question.title}}</div>
                    </div>
                    <div class="question4-line2">
                        <el-image v-if="isPicture(question.annexUrl)" class="image" :src="question.annexUrl" fit="contain"
                                    :preview-src-list="[question.annexUrl]"></el-image>
                        <el-link v-else :href="question.annexUrl" class="link" target="_blanl">
                            {{question.annexUrl}}
                        </el-link>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</template>

<script>

    import request from '@/utils/request'

    export default {
        name: "ViewApply",
        props: {
            applyId: {
                type: Number
            }
        },
        computed: {
            number() {
                return "No." + ("0000" + this.index).substr(-4);
            }
        },
        data() {
            return {
                applyInfo: undefined, // 报名信息
                index: undefined, // 当前查看报名表的编号
            }
        },
        mounted() {
            this.loading = this.$loading({
                lock: true,
                text: 'Loading',
                spinner: 'el-icon-loading',
                background: 'rgba(0, 0, 0, 0.7)'
            });
            this.initData();
        },
        methods: {
            initData() {
                this.getData();
            },
            // 加载简历数据
            getData() {
                request({
                    url: `/registration/${this.applyId}`,
                    method: 'get'}).then(response => {
                    response.data.questionnaireList.forEach(question => {
                        this.handlerQuestion(question);
                    })
                    this.applyInfo = response.data;
                    this.index = this.applyInfo.number;
                    this.loading.close();
                }).catch(() => {
                    console.log("数据异常");
                });
            },
            // 处理问题答案
            handlerQuestion(question) {
                switch (question.type) {
                    case "1":
                        question.answer = question.answerContent.length > 0 && question.answerContent[0];
                        break;
                    case "2":
                    case "3":
                        question.questionnaireOptions.forEach(option => {
                            option.checked = question.mustAnswer === '1' ? question.answerOptions.findIndex(answer => answer.id === option.id) >= 0 : false;
                        })
                        break;
                    case "4":
                        question.annexUrl = question.answerContent.length > 0 && question.answerContent[0];
                        break;
                }
            },

            // 判断地址是否为图片
            isPicture(url) {
                return /\.(gif|jpg|jpeg|png|GIF|JPEG|JPG|PNG)$/.test(url);
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

                .button {
                    width: 96px;
                    height: 28px;
                    border-radius: 4px;
                    font-size: 14px;
                    line-height: 28px;
                    text-align: center;
                    cursor: pointer;
                    margin-left: 14px;
                }

                .button1-1 {
                    background: #4895EF;
                    color: #FFFFFF;
                }

                .button1-2 {
                    background: #E3F0FF;
                    border: 1px solid #4895EF;
                    color: #4895EF;
                }

                .button2-1 {
                    background: #81C784;
                    color: #FFFFFF;
                }

                .button2-2 {
                    background: #ECEFF1;
                    color: #78909C;
                }

                .button3-1 {
                    background: #ECEFF1;
                    color: #78909C;
                }

                .button3-2 {
                    background: #EC5454;
                    color: #FFFFFF;
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
                        width: 12px;
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
                }

                .question-container {

                    .question1-container {
                        margin-top: 18px;

                        .question1-line1 {
                            display: flex;
                            align-items: flex-start;
                            justify-content: space-between;

                            .title {
                                font-size: 16px;
                                color: #666666;
                                line-height: 22px;

                                .must {
                                    color: red;
                                    margin-right: 5px;
                                }
                            }
                        }

                        .question1-line2 {
                            margin-top: 5px;
                            margin-left: 12px;

                            .answer {
                                font-size: 18px;
                                color: #333333;
                                line-height: 25px;
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
                            margin-top: -4px;
                            width: calc(100% + 70px);

                            .option-container {
                                display: flex;
                                align-items: flex-start;
                                margin: 4px 70px 4px 0;

                                .icon {
                                    display: flex;
                                    align-items: center;
                                    height: 25px;
                                    flex-shrink: 0;
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
                            margin-top: -4px;
                            width: calc(100% + 70px);

                            .option-container {
                                display: flex;
                                align-items: flex-start;
                                margin: 4px 70px 4px 0;

                                .icon {
                                    display: flex;
                                    align-items: center;
                                    height: 25px;
                                    flex-shrink: 0;
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
                            margin-top: 4px;
                            margin-left: 12px;

                            .image {
                                width: 200px;
                                display: block;
                            }

                            .link {
                                font-size: 18px;
                            }
                        }
                    }

                }
            }
        }
    }
</style>
