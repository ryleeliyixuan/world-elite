<template>
<div class="app-container">
    <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/mock-interview' }">模拟面试</el-breadcrumb-item>
        <el-breadcrumb-item>Cathy Liu</el-breadcrumb-item>
    </el-breadcrumb>
    <div class="title-detail">
        <div class="title-left">
            <div class="circle-background">
                <el-image :src="require('@/assets/demo.jpg')"class="circle-image"  fit="fill"></el-image>
            </div>
            <div class="basis-detail">
                <div class="basis1">Cathy Liu</div>
                <div class="basis2">互联网·战略分析师</div>
                <el-rate
                        v-model="rate"
                        disabled
                        show-score
                        :colors="colors"
                        text-color="#999999"
                        score-template="{value}"
                        class="item-rate">
                </el-rate>
            </div>
        </div>
        <el-image :src="require('@/assets/mock/company-logo.png')"class="message-image"  fit="fill"></el-image>
    </div>
    <div class="label-container">
        <div class="label-circle">
            <el-image :src="require('@/assets/mock/icon13.png')"class="label-icon"  fit="fill"></el-image>
        </div>
        <div class="label-text">“48项产品专利的抖音狂魔”</div>
    </div>
    <div class="tip-container">
        <div class="tip-item">经验丰富</div>
    </div>
    <div class="introduce-container">
        我是来自字节跳动的Cathy，我擅长的企业建站、办公协同、财务管理常用场景精选世界500强旗下公司，港股上市公司等优质服务商基于阿里云计算技术；为人处事儒雅随和云淡风轻
    </div>
    <div class="user-info">个人信息</div>
    <div class="brief-introduction">
        <el-image :src="require('@/assets/mock/icon9.png')" class="brief-icon1"  fit="fill"></el-image>
        <div class="brief-title">公司：字节跳动</div>
        <el-image :src="require('@/assets/mock/icon10.png')" class="brief-icon1"  fit="fill" style="margin-left: 62px"></el-image>
        <div class="brief-title">行业：互联网</div>
        <el-image :src="require('@/assets/mock/icon12.png')" class="brief-icon1"  fit="fill" style="margin-left: 62px"></el-image>
        <div class="brief-title">从业时间：3年</div>
    </div>
    <div class="experience-container">
        <el-image :src="require('@/assets/mock/icon8.png')" class="brief-icon1"  fit="fill" ></el-image>
        <div class="experience-title">从业经历：</div>
        <div class="experience-content">Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean euismod bibendum laoreet. Proin gravida dolor sit amet lacus accumsan et viverra justo commodo. Proin sodales pulvinar sic tempor. Sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Nam fermentum, nulla luctus pharetra vulputate, felis tellus mollis orci, sed rhoncus pronin sapien nunc accuan eget.</div>
    </div>
    <div class="user-info">和TA预约</div>
    <div class="reservation-container">
        <div class="reservation-top-container">
            <div class="reservation-left">
                <el-image :src="require('@/assets/mock/icon11.png')" class="reservation-icon1"  fit="fill" ></el-image>
                <div class="reservation-title">HR面试（通用）</div>
            </div>
            <div class="reservation-right">
                <div class="reservation-price">￥80.00<span class="reservation-price-small">/半小时</span></div>
                <div class="reservation-button">马上预约</div>
                <el-image :src="require('@/assets/mock/arrow-gery.png')" class="reservation-icon2"  fit="fill" ></el-image>
            </div>
        </div>
        <div class="reservation-introduce">
            “面试内容分为若干测评要素,主要包括综合分析能力、言语表达能力、应变能力、计划组织协调能力、人际交往的意识与技巧、自我情绪控制、求职动机与拟任职位的匹配性、举止仪表和专业能力。必要时,根据职位要求,面试内容可以增加其它测评要素。 （对应面试官入驻填写信息里面的面试内容描述）
        </div>





    </div>
</div>
</template>

<script>
    export default {
        name: "interviewerdetail",
        data(){
            return{
                rate:5,
                colors:['#FFD740','#FFD740','#FFD740'],
            }
        }
    }
</script>

<style scoped lang="scss">
    .app-container{
        max-width: 1200px;
        margin: 0 auto;
        height: auto;
        padding: 20px;
        .title-detail{
            width: 100%;
            height: 155px;
            display: flex;
            margin-top: 30px;
            align-items: center;
            justify-content: space-between;
            .title-left{
                display: flex;
                align-items: center;
                height: 100%;
                .circle-background{
                    width: 155px;
                    height: 155px;
                    background: conic-gradient(#A2D6F9,#072AC8,#1E96FC,#A2D6F9);
                    border-radius: 50%;
                    position: relative;
                    .circle-image{
                        position: absolute;
                        height: 148px;
                        width: 148px;
                        border-radius: 50%;
                        left: 50%;
                        top: 50%;
                        transform: translate(-50%,-50%);
                    }
                }
                .basis-detail{
                    display: flex;
                    height: 100%;
                    flex-direction: column;
                    justify-content: space-between;
                    padding-top: 6px;
                    margin-left: 46px;
                    .basis1{
                        font-size: 48px;
                        font-weight: 500;
                        color: #333333;
                        line-height: 67px;
                    }
                    .basis2{
                        font-size: 24px;
                        font-weight: 400;
                        color: #666666;
                        line-height: 33px;
                    }
                    /deep/ .el-rate__icon{
                        font-size: 26px;
                    }
                }
            }
            .message-image{
                width: 376px;
                height: 98px;
            }

        }

        }
    .label-container{
        width: 100%;
        display: flex;
        align-items: center;
        margin-top: 31px;
        .label-circle{
            width: 44px;
            height: 44px;
            background: #072AC8;
            position: relative;
            .label-icon{
                width: 24px;
                height: 31px;
                position: absolute;
                top: 50%;
                left: 50%;
                transform: translate(-50%,-50%);
            }
        }
        .label-text{
            font-size: 24px;
            color: #072AC8;
            margin-left: 9px;
        }
    }
    .tip-container{
        width: 100%;
        display: flex;
        align-items: center;
        margin-top: 26px;
        .tip-item{
            padding: 2px 22px;
            background: #FA8C16;
            border-radius: 15px;
            font-size: 18px;
            color: #FFFFFF;
        }
    }
    .introduce-container{
        width: 100%;
        font-size: 24px;
        color: #333333;
        margin-top: 23px;
    }
    .user-info{
        font-size: 36px;
        color: #333333;
        margin-top: 30px;
    }
    .brief-introduction{
        width: 100%;
        display: flex;
        align-items: center;
        margin-top: 13px;
        .brief-title{
            font-size: 24px;
            color: #333333;
        }
        .brief-icon1{
            width: 26px;
            height: 25px;
            margin-right: 3px;
        }
    }
    .experience-container{
        width: 100%;
        display: flex;
       align-items: flex-start;
        margin-top: 13px;
        .brief-icon1{
            min-width: 27px;
            height: 27px;
        }
        .experience-content{
            font-size: 24px;
            color: #333333;
        }
        .experience-title{
            font-size: 24px;
            color: #333333;
            min-width: 120px;
            margin-left: 3px;
        }
    }
    .reservation-container{
        width: 100%;
        height: auto;
        background: #FFFFFF;
        box-shadow: 0px 3px 13px 1px #DEE5F4;
        margin-top: 33px;
        .reservation-top-container{
            width: 100%;
            height: 67px;
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding-right: 29px;
            .reservation-left{
                display: flex;
                align-items: center;
                .reservation-title{
                    font-size: 24px;
                    color: #333333;
                    margin-left: 12px;
                }
                .reservation-icon1{
                    width: 53px;
                    height: 67px;
                }
            }
            .reservation-right{
                display: flex;
                align-items: center;
                .reservation-price{
                    font-size: 24px;
                    color: #333333;
                    .reservation-price-small{
                        font-size: 16px;
                    }
                }
                .reservation-button{
                    width: 136px;
                    height: 41px;
                    background: rgba(255, 189, 105, 0.3);
                    border-radius: 21px;
                    border: 1px solid #F78259;
                    line-height: 41px;
                    text-align: center;
                    font-size: 18px;
                    color: #F78259;
                    margin: 0 38px 0 12px;
                }
                .reservation-icon2{
                    width: 13px;
                    height: 22px;
                }


            }

        }
        .reservation-introduce{
            width: 100%;
            height: auto;
            padding: 0 80px 26px 65px;
            font-size: 21px;
            font-weight: 400;
            color: #333333;
        }


    }


</style>
