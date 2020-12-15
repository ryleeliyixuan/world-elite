<template>
    <div class="app-container" v-if="interviewer">
        <el-breadcrumb separator-class="el-icon-arrow-right" class="breadcrumb-container">
            <el-breadcrumb-item :to="{ path: '/mock/interview' }">模拟面试</el-breadcrumb-item>
            <el-breadcrumb-item v-if="!interviewerFromHome" :to="{ path: '/mock/interviewer/more' }">面试官列表</el-breadcrumb-item>
            <el-breadcrumb-item>{{interviewer.nickName}}</el-breadcrumb-item>
        </el-breadcrumb>
        <div class="title-detail">
            <div class="title-left">
                <div class="circle-background">
                    <el-image :src="interviewer.avatar" class="circle-image" fit="cover"></el-image>
                </div>
                <div class="basis-detail">
                    <div class="basis1">{{interviewer.nickName}}
                    </div>
                    <div class="basis2">{{interviewer.industry.name}}<span v-if="interviewer.position">·</span>{{interviewer.position}}</div>
                    <div class="basis3">
                        <el-rate
                                :value="interviewer.avgScore|toFixed1"
                                disabled
                                show-score
                                :colors="['#FFD740', '#FFD740', '#FFD740']"
                                text-color="#999999"
                                score-template="{value}"
                                class="item-rate">
                        </el-rate>

                        <span> of 5.0</span>
                    </div>
                </div>
            </div>
            <el-image v-if="interviewer.company" :src="interviewer.company.logo" class="message-image" fit="scale-down"></el-image>
        </div>
        <!--        <div class="label-container">-->
        <!--            <div class="label-circle">-->
        <!--                <el-image :src="require('@/assets/mock/icon13.png')" class="label-icon" fit="fill"></el-image>-->
        <!--            </div>-->
        <!--            <div class="label-text">“{{interviewer.introduction}}”</div>-->
        <!--        </div>-->
        <!-- <div class="tip-container">-->
        <!--     <div class="tip-item">经验丰富</div>-->
        <!-- </div>-->
        <div class="introduce-container">
            {{interviewer.description}}
        </div>
        <div class="user-info">个人信息</div>
        <div class="brief-introduction">
            <el-image v-if="interviewer.company" :src="require('@/assets/mock/icon9.png')" class="brief-icon1" fit="fill"></el-image>
            <div  class="brief-title" style="margin-right: 62px">公司：{{interviewer.company?interviewer.company.name:interviewer.companyName}}</div>
            <el-image :src="require('@/assets/mock/icon10.png')" class="brief-icon1" fit="fill"></el-image>
            <div class="brief-title" style="margin-right: 62px">行业：{{interviewer.industry.name}}</div>
            <el-image :src="require('@/assets/mock/icon12.png')" class="brief-icon1" fit="fill"></el-image>
            <div class="brief-title" style="margin-right: 62px">从业时间：{{interviewer.experienceTime.name}}</div>
        </div>
        <div class="experience-container">
            <el-image :src="require('@/assets/mock/icon8.png')" class="brief-icon1" fit="fill"></el-image>
            <div class="experience-title">从业经历：</div>
            <div class="experience-content-container">
                <div class="experience-content" v-for="(item,index) in interviewer.experience">{{(index+1)+'.'+item}}</div>
            </div>


        </div>
        <div class="user-info">和TA预约</div>

        <el-collapse class="reservation-container">
            <el-collapse-item class="reservation-item-container" v-for="reservation in reservationList" :key="reservation.id">
                <template slot="title">
                    <div class="reservation-top-container">
                        <div class="reservation-left">
                            <el-image :src="reservation.logo" class="reservation-icon1" fit="cover"></el-image>
                            <div class="reservation-title">{{reservation.direction}}</div>
                        </div>
                        <div class="reservation-right">
                            <div class="reservation-price">￥{{reservation.price|toFixed2}}<span>/半小时</span></div>
                            <div class="reservation-button" @click="onPromise(reservation)">马上预约</div>
                        </div>
                    </div>
                </template>
                <div class="reservation-introduce">
                    {{reservation.description}}
                </div>
            </el-collapse-item>
        </el-collapse>
    </div>
</template>

<script>
    export default {
        name: "interviewerdetail",
        data() {
            return {
                interviewer: undefined, // 面试官
                reservationList: [], // 可预约列表
                interviewerFromHome: undefined,
            }
        },

        mounted() {
            this.interviewerFromHome = this.$storage.getData("interviewerFrom").home;
            if (this.$route.params.id) {
                this.getData(this.$route.params.id);
            } else {
                this.$message.warning("参数错误");
            }
        },
        methods: {
            // 预约
            onPromise(reservation) {
                this.$router.push(`/mock/promise/${reservation.interviewerId}`);
            },

            // 加载数据
            getData(id) {
                this.$axios.get(`/mock/interviewer/${id}`).then(data => {
                    this.interviewer = data.data;
                })
                this.$axios.get(`/mock/interviewer/direction/${id}`).then(data => {
                    this.reservationList = data.data;
                })
            }
        }
    }
</script>

<style scoped lang="scss">
    .app-container {
        width: 1200px;
        margin: 0 auto;
        height: auto;
        padding: 20px;

        .breadcrumb-container {
            ::v-deep .el-breadcrumb__inner {
                font-size: 18px;
            }
        }

        .title-detail {
            width: 100%;
            height: 155px;
            display: flex;
            margin-top: 30px;
            align-items: center;
            justify-content: space-between;

            .title-left {
                display: flex;
                align-items: center;
                height: 100%;

                .circle-background {
                    width: 137px;
                    height: 137px;
                    background: conic-gradient(#A2D6F9, #072AC8, #1E96FC, #A2D6F9);
                    border-radius: 50%;
                    display: flex;
                    align-items: center;
                    justify-content: center;

                    .circle-image {
                        height: 130px;
                        width: 130px;
                        border-radius: 50%;
                    }
                }

                .basis-detail {
                    display: flex;
                    height: 100%;
                    flex-direction: column;
                    justify-content: space-between;
                    padding-top: 6px;
                    margin-left: 46px;


                    .basis1 {
                        font-size: 28px;
                        font-weight: 500;
                        color: #333333;
                        line-height: 67px;

                    }

                    .basis2 {
                        font-size: 21px;
                        font-weight: 400;
                        color: #666666;
                        line-height: 33px;
                    }

                    .basis3 {
                        display: flex;
                        align-items: center;

                        /deep/ .el-rate__icon {
                            font-size: 27px;
                        }

                        span {
                            font-size: 18px;
                            color: #999999;
                            line-height: 29px;
                            padding-left: 5px;
                        }
                    }

                    ::v-deep .el-rate {
                        height: auto;
                    }

                    ::v-deep .el-rate__icon {
                        font-size: 30px;
                        letter-spacing: -7px;
                    }

                    ::v-deep .el-rate__text {
                        font-size: 18px;
                        color: #999999;
                        line-height: 29px;
                        padding-left: 5px;
                    }
                }
            }

            .message-image {
                width: 400px;
                height: 300px;
            }
        }

        .label-container {
            width: 100%;
            display: flex;
            align-items: center;
            margin-top: 31px;

            .label-circle {
                width: 44px;
                height: 44px;
                background: #072AC8;
                position: relative;

                .label-icon {
                    width: 24px;
                    height: 31px;
                    position: absolute;
                    top: 50%;
                    left: 50%;
                    transform: translate(-50%, -50%);
                }
            }

            .label-text {
                font-size: 24px;
                color: #072AC8;
                margin-left: 9px;
            }
        }

        .tip-container {
            width: 100%;
            display: flex;
            align-items: center;
            margin-top: 26px;

            .tip-item {
                padding: 2px 22px;
                background: #FA8C16;
                border-radius: 15px;
                font-size: 18px;
                color: #FFFFFF;
            }
        }

        .introduce-container {
            width: 100%;
            font-size: 18px;
            color: #333333;
            margin-top: 23px;
        }

        .user-info {
            font-size: 24px;
            color: #333333;
            margin-top: 30px;
            margin-bottom: 16px;
        }

        .brief-introduction {
            width: 100%;
            display: flex;
            align-items: center;

            .brief-title {
                font-size: 18px;
                color: #333333;
            }

            .brief-icon1 {
                width: 21px;
                height: 20px;
                margin-right: 8px;
            }
        }

        .experience-container {
            width: 100%;
            display: flex;
            flex-direction: row;
            align-items: flex-start;
            margin-top: 13px;


            .brief-icon1 {
                min-width: 21px;
                width: 21px;
                height: 20px;
                margin-right: 8px;
            }

            .experience-content {
                font-size: 18px;
                color: #333333;
                display: block;
                margin-bottom: 7px;
            }

            .experience-title {
                font-size: 18px;
                color: #333333;
                min-width: 90px;
            }
        }


        .reservation-container {
            border: none;

            .reservation-item-container {
                width: 100%;
                height: auto;
                background: #FFFFFF;
                box-shadow: 0 3px 13px 1px #DEE5F4;
                margin-bottom: 16px;

                ::v-deep .el-collapse-item__header {
                    height: 54px;
                }

                ::v-deep .el-collapse-item__arrow {
                    font-size: 21px;
                    font-weight: 500;
                    color: #aaaaaa;
                }

                .reservation-top-container {
                    width: 100%;
                    height: 54px;
                    display: flex;
                    align-items: center;
                    justify-content: space-between;

                    .reservation-left {
                        display: flex;
                        align-items: center;

                        .reservation-title {
                            font-size: 18px;
                            color: #333333;
                            margin-left: 12px;
                        }

                        .reservation-icon1 {
                            width: 53px;
                            height: 54px;

                            /deep/ .el-image__error {
                                font-size: 12px;
                            }
                        }
                    }

                    .reservation-right {
                        display: flex;
                        align-items: center;

                        .reservation-price {
                            font-size: 18px;
                            color: #333333;
                            margin-right: 12px;

                            span {
                                font-size: 12px;
                            }
                        }

                        .reservation-button {
                            width: 109px;
                            height: 32px;
                            background: rgba(255, 189, 105, 0.3);
                            border-radius: 21px;
                            border: 1px solid #F78259;
                            line-height: 32px;
                            text-align: center;
                            font-size: 16px;
                            color: #F78259;
                            margin-right: 30px;
                        }
                    }
                }

                .reservation-introduce {
                    width: 100%;
                    height: auto;
                    padding: 0 50px 26px 65px;
                    font-size: 16px;
                    font-weight: 400;
                    color: #333333;
                }
            }
        }
    }
</style>
