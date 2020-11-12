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
        <el-image :src="require('@/assets/mock/icon9.png')"class="brief-icon1"  fit="fill"></el-image>
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
    }


</style>
