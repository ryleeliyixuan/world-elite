<template>
    <div class="mock-container">
        <div class="title-container">
            <div class="title-left">面试官排行榜</div>
            <div class="title-right"  @click="onMoreInterviewer">更多面试官
                <img src="../assets/mock/arrow-gery.png" alt="" class="title-icon"></div>
        </div>

        <div class="interviewer-container">
            <div class="interviewer-left">
                <div class="left-item1">
                    <div class="item-left-container">
                        <div class="item-title">Cathy Liu</div>
                        <el-rate
                                v-model="rate"
                                disabled
                                show-score
                                :colors="colors"
                                text-color="#FFFFFF"
                                score-template="{value}"
                        class="item-rate">
                        </el-rate>
                        <div class="item-bottom">
                            <img src="../assets/mock/icon1.png" alt="" class="item-bottom-icon1">
                            <div class="item-bottom-text1">阿里巴巴</div>
                            <img src="../assets/mock/icon2.png" alt="" class="item-bottom-icon1" style="margin-left: 38px">
                            <div class="item-bottom-text1">战略分析师</div>
                        </div>

                    </div>
                        <img src="../assets/demo.jpg" alt="" class="right-image">
                </div>
                <div class="left-item2">
                    <div class="item-left-container">
                        <div class="item-title">Cathy Liu</div>
                        <el-rate
                                v-model="rate"
                                disabled
                                show-score
                                :colors="colors"
                                text-color="#FFFFFF"
                                score-template="{value}"
                                class="item-rate">
                        </el-rate>
                        <div class="item-bottom">
                            <img src="../assets/mock/icon1.png" alt="" class="item-bottom-icon1">
                            <div class="item-bottom-text1">阿里巴巴</div>
                            <img src="../assets/mock/icon2.png" alt="" class="item-bottom-icon1" style="margin-left: 38px">
                            <div class="item-bottom-text1">战略分析师</div>
                        </div>

                    </div>
                    <img src="../assets/demo.jpg" alt="" class="right-image">
                </div>
                <div class="left-item3">
                    <div class="item-left-container">
                        <div class="item-title">Cathy Liu</div>
                        <el-rate
                                v-model="rate"
                                disabled
                                show-score
                                :colors="colors"
                                text-color="#FFFFFF"
                                score-template="{value}"
                                class="item-rate">
                        </el-rate>
                        <div class="item-bottom">
                            <img src="../assets/mock/icon1.png" alt="" class="item-bottom-icon1">
                            <div class="item-bottom-text1">阿里巴巴</div>
                            <img src="../assets/mock/icon2.png" alt="" class="item-bottom-icon1" style="margin-left: 38px">
                            <div class="item-bottom-text1">战略分析师</div>
                        </div>

                    </div>
                    <img src="../assets/demo.jpg" alt="" class="right-image">
                </div>
            </div>
            <div class="interviewer-right">
                <div class="right-item1">
                    <img src="../assets/mock/mock4.png" alt="" class="right-item1-img1">
                    <img src="../assets/demo.jpg" alt="" class="right-item1-img2">
                    <div class="right-item1-name">健壮的大姐姐</div>
                    <img src="../assets/item9.jpg" alt="" class="right-item1-img3">
                    <div class="right-item1-position">资深产品经理</div>
                    <div class="right-item1-score">4.7</div>
                    <div class="right-item-tip">HOT</div>
                </div>
                <div class="right-item2">
                    <img src="../assets/mock/mock5.png" alt="" class="right-item1-img1">
                    <img src="../assets/demo.jpg" alt="" class="right-item1-img2">
                    <div class="right-item1-name">健壮的大姐姐</div>
                    <img src="../assets/item9.jpg" alt="" class="right-item1-img3">
                    <div class="right-item1-position">资深产品经理</div>
                    <div class="right-item1-score">4.7</div>
                </div>
                <div class="right-item3">
                    <img src="../assets/mock/mock6.png" alt="" class="right-item1-img1">
                    <img src="../assets/demo.jpg" alt="" class="right-item1-img2">
                    <div class="right-item1-name">健壮的大姐姐</div>
                    <img src="../assets/item9.jpg" alt="" class="right-item1-img3">
                    <div class="right-item1-position">资深产品经理</div>
                    <div class="right-item1-score">4.7</div>
                </div>
                <div class="right-item4">
                    <img src="../assets/mock/mock7.png" alt="" class="right-item1-img1">
                    <img src="../assets/demo.jpg" alt="" class="right-item1-img2">
                    <div class="right-item1-name">健壮的大姐姐</div>
                    <img src="../assets/item9.jpg" alt="" class="right-item1-img3">
                    <div class="right-item1-position">资深产品经理</div>
                    <div class="right-item1-score">4.7</div>
                </div>
                <div class="right-item5" style="margin-bottom: 0">
                    <img src="../assets/mock/mock8.png" alt="" class="right-item1-img1">
                    <img src="../assets/demo.jpg" alt="" class="right-item1-img2">
                    <div class="right-item1-name">健壮的大姐姐</div>
                    <img src="../assets/item9.jpg" alt="" class="right-item1-img3">
                    <div class="right-item1-position">资深产品经理</div>
                    <div class="right-item1-score">4.7</div>
                </div>
            </div>
        </div>

        <div class="title-container" style="margin-top: 70px">
            <div class="title-left">本月新入驻面试官</div>
            <div class="title-right">立即入驻
                <img src="../assets/mock/arrow-gery.png" alt="" class="title-icon"></div>
        </div>

        <div class="new-interviewer-container">
            <el-image :src="require('@/assets/mock/arrow2.png')" class="interviewer-arrow"  fit="fill" @click="onLeft" ></el-image>
            <div class="transition-container">
                <transition  name='fade' @after-leave="afterLeave">
                    <div :class="['interviewer-detail',flag1?'interviewer-detail-translate':'']" ref="interview">
                        <div class="item-detail">
                            <el-image :src="require('@/assets/demo.jpg')" class="interviewer-img"  fit="fill"></el-image>
                            <div class="interviewer-bottom">
                                <div class="name-left">
                                    <div class="name1">Cathy</div>
                                    <div class="position">资深产品经理1</div>
                                </div>
                                <el-image :src="require('@/assets/mock/company-logo.png')" class="company-image"  fit="fill"></el-image>
                            </div>
                        </div>
                        <div class="item-detail">
                            <el-image :src="require('@/assets/demo.jpg')" class="interviewer-img"  fit="fill" ></el-image>
                            <div class="interviewer-bottom">
                                <div class="name-left">
                                    <div class="name1">Cathy</div>
                                    <div class="position">资深产品经理2</div>
                                </div>
                                <el-image :src="require('@/assets/mock/company-logo.png')" class="company-image"  fit="fill"></el-image>
                            </div>
                        </div>
                        <div class="item-detail">
                            <el-image :src="require('@/assets/demo.jpg')" class="interviewer-img"  fit="fill"></el-image>
                            <div class="interviewer-bottom">
                                <div class="name-left">
                                    <div class="name1">Cathy</div>
                                    <div class="position">资深产品经理3</div>
                                </div>
                                <el-image :src="require('@/assets/mock/company-logo.png')" class="company-image"  fit="fill"></el-image>
                            </div>
                        </div>
                        <div class="item-detail">
                            <el-image :src="require('@/assets/demo.jpg')" class="interviewer-img"  fit="fill"></el-image>
                            <div class="interviewer-bottom">
                                <div class="name-left">
                                    <div class="name1">Cathy</div>
                                    <div class="position">资深产品经理4</div>
                                </div>
                                <el-image :src="require('@/assets/mock/company-logo.png')" class="company-image"  fit="fill"></el-image>
                            </div>
                        </div>
                        <div class="item-detail">
                            <el-image :src="require('@/assets/demo.jpg')" class="interviewer-img"  fit="fill"></el-image>
                            <div class="interviewer-bottom">
                                <div class="name-left">
                                    <div class="name1">Cathy</div>
                                    <div class="position">资深产品经理5</div>
                                </div>
                                <el-image :src="require('@/assets/mock/company-logo.png')" class="company-image"  fit="fill"></el-image>
                            </div>
                        </div>
                    </div>
                </transition>

            </div>

            <el-image :src="require('@/assets/mock/arrow1.png')" class="interviewer-arrow"  fit="fill" @click="onRight"></el-image>

        </div>
    </div>
</template>

<script>
    export default {
        name: "MockInterview",
        data() {
          return{
              rate:5,
              colors:['#FFD740','#FFD740','#FFD740'],
              flag1:false,
              InterViewerList:[],//面试官排行榜
          }
        },
        mounted() {
            this.$emit("onComplete");
            this.getInterViewList();
        },
        methods: {
            onMoreInterviewer() {
                this.$router.push("/more-interviewer");
            },
            afterLeave(){
                this. flag1=false
                console.log("123456")
            },
            onLeft(){
            //     this.$refs.interview.transform.translate(-241,0);
                this. flag1=true

            },
            onRight(){

            },
            getInterViewList(){
                this.$axios.get('/mock/interviewer/rank', {
                    params: {limit: 10}
                }).then(data => {
                    console.log(123456)
                console.log(data)
                })
            },
        }
    }
</script>

<style scoped lang="scss">

    .mock-container{
        max-width: 1200px;
        margin: 0 auto;
        height: auto;
        padding: 20px;
        .title-container{
            width: 100%;
            display: flex;
            align-items: center;
            justify-content: space-between;
            .title-left{
                font-size: 24px;
                color: #333333;
                font-weight: bold;
            }
            .title-right{
                display: flex;
                align-items: center;
                font-size: 21px;
                font-weight: 400;
                color: #666666;
                cursor: pointer;
                .title-icon{
                    width: 10px;
                    height: 16px;
                    margin-left: 7px;
                }
            }
        }
        .interviewer-container{
            width:100%;
            display: flex;
            margin-top: 9px;
            height: 490px;
            .interviewer-left{
                width: 453px;
                height: 100%;
                display: flex;
                flex-direction: column;
                justify-content: space-between;
                .left-item1,
                .left-item2,
                .left-item3{
                    width: 453px;
                    height: 157px;
                    background: url("../assets/mock/mock1.png") no-repeat ;
                    background-size: 453px 157px;
                    display: flex;
                    align-items: center;
                    position: relative;

                    .item-left-container{
                        width: auto;
                        height: 100%;
                        padding: 20px 20px 20px 40px;
                        .item-title{
                            width: 100%;
                            font-size: 28px;
                            font-weight: bold;
                            color: #FFFFFF;
                            margin-left: 95px;
                        }
                        .item-rate{
                            margin: 17px 0 7px 0;
                        }
                        .item-bottom{
                            display: flex;
                            align-items: center;
                            .item-bottom-icon1{
                                width: 21px;
                                height: 21px;
                                margin-right: 5px;
                            }
                            .item-bottom-text1{
                                font-size: 18px;
                                color: #FFFFFF;
                            }
                        }
                    }
                    .right-image{
                        width: 112px;
                        height: 112px;
                        border-radius: 50%;
                        overflow: hidden;
                        position: absolute;
                        top: 23px;
                        right: 28px;

                    }
                }

            }
            .interviewer-right{
                max-width: 605px;
                height: 100%;
                margin-left: 45px;
                display: flex;
                flex-direction: column;
                justify-content: space-between;
                padding-bottom: 20px;
                .right-item1,
                .right-item2,
                .right-item3,
                .right-item4,
                .right-item5{
                    width: 605px;
                    display: flex;
                    height: 77px;
                    align-items: center;
                    justify-content: space-between;
                    position: relative;
                    .right-item-tip{
                        position: absolute;
                        top: 50%;
                        transform: translateY(-50%);
                        right: -60px;
                        background: #EE6055;
                        box-shadow: 0px 5px 13px 0px rgba(238, 96, 85, 0.5);
                        border-radius: 5px;
                        padding: 2px 8px;
                        font-size: 16px;
                        color: #FFFFFF;
                    }
                    .right-item1-img1{
                        width: 100px;
                        height: 36px;
                    }
                    .right-item1-img2{
                        width: 77px;
                        height: 77px;
                        border-radius: 50%;
                        overflow: hidden;
                    }
                    .right-item1-name{
                        font-size: 21px;
                        color: #0F3057;

                    }
                    .right-item1-img3{
                        width: 94px;
                        height: 27px;
                    }
                    .right-item1-position{
                        font-size: 21px;
                        color: #333333;
                    }
                    .right-item1-score{
                        font-size: 21px;
                        color: #F9A620;
                    }

                }

            }
        }
        .new-interviewer-container{
            margin-top: 22px;
            width: 100%;
            height: 303px;
            display: flex;
            align-items: center;
            justify-content: space-between;
            .interviewer-arrow{
                width: 66px;
                height: 66px;
            }
            .transition-container{
                width: 1036px;
                height: 100%;
                overflow: hidden;
                .fade-enter-active, .fade-leave-active{
                    transition: all 20s;
                }
                .fade-enter{
                    opacity: 0.8;
                    transform: translateX(-241px);
                }
                .fade-enter-to{
                    opacity: 0.8;
                    transform: translateX(-241px);
                }
                .fade-leave{
                    opacity: 1;
                }
                .fade-leave-to{
                    opacity: 0;
                    transform: translateX(-241px);
                }

                .interviewer-detail-translate{
                    transform: translateX(-241px);
                }
                .interviewer-detail{
                    width: 100%;
                    height: 100%;
                    display: flex;
                    align-items: center;
                    .item-detail{
                        width: 241px;
                        height: 100%;
                        position: relative;
                        margin-right: 23px;
                        .interviewer-img{
                            width: 241px;
                            height: 100%;
                        }
                        .interviewer-bottom{
                            width: 197px;
                            height: 62px;
                            background: rgba(255, 255, 255, 0.85);
                            position: absolute;
                            left: 50%;
                            transform: translateX(-50%);
                            bottom: 22px;
                            padding: 10px 5px 10px 9px;
                            display: flex;
                            align-items: center;
                            .name-left{
                                height: 100%;
                                display: flex;
                                flex-direction: column;
                                justify-content: space-between;
                                .name1{
                                    font-size: 18px;
                                    color: #333333;
                                }
                                .position{
                                    font-size: 12px;
                                    color: #333333;
                                }
                            }
                            .company-image{
                                width: 100px;
                                height: 36px;
                            }
                        }
                    }

                }

            }








        }
    }
</style>
