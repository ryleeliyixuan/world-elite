<template>
    <div class="mock-container">
        <div class="title-container">
            <div class="title-left">热门面试官</div>
            <div class="title-right" @click="onMoreInterviewer">
                更多面试官
                <img src="../assets/mock/arrow-gery.png" alt="" class="title-icon">
            </div>
        </div>
        <div class="interviewer-container">
            <div class="interviewer-left">
                <div v-for="(interviewer, index) in interviewerList"
                     v-if="index<3"
                     :key="interviewer.key"
                     :class="['left-item','index'+index]"
                     @click="onInterviewer(interviewer)">
                    <div class="item-left-container">
                        <div class="item-title">{{interviewer.nickName}}</div>
<!--                        <el-rate :value="interviewer.avgScore|toFixed1"-->
<!--                                 disabled-->
<!--                                 show-score-->
<!--                                 :colors="['#FFD740', '#FFD740', '#FFD740']"-->
<!--                                 text-color="#FFFFFF"-->
<!--                                 score-template="{value}"-->
<!--                                 class="item-rate">-->
<!--                        </el-rate>-->
                        <div class="item-bottom">
                            <img src="../assets/mock/icon1.png" alt="" class="item-bottom-icon1">
                            <div class="item-bottom-text1">{{interviewer.company?interviewer.company.name:interviewer.companyName}}</div>
                            <img src="../assets/mock/icon2.png" alt="" class="item-bottom-icon1" style="margin-left: 38px">
                            <div class="item-bottom-text1" v-if="interviewer.position">{{interviewer.position}}</div>
                        </div>
                    </div>
                    <el-image :src="interviewer.avatar" alt="" class="right-image" fit="cover"/>
                </div>
            </div>
            <div class="interviewer-right">
                <div v-for="(interviewer, index) in interviewerList"
                     v-if="index>=3 && index<8"
                     :key="interviewer.key"
                     class="right-item"
                     @click="onInterviewer(interviewer)">
                    <el-image :src="require('../assets/mock/mock'+index+'.png')" class="right-item1-img1" fit="cover"/>
                    <el-image :src="interviewer.avatar" class="right-item1-img2" fit="cover"/>
                    <div class="right-item1-name">{{interviewer.nickName}}</div>
                    <div class="right-item1-img3" >{{interviewer.company?interviewer.company.fullName:interviewer.companyName}}</div>
                    <!--                    <el-image :src="interviewer.companyLogo" class="right-item1-img3" fit="scale-down"/>-->
                    <div class="right-item1-position">{{interviewer.position}}</div>
<!--                    <div class="right-item1-score">{{interviewer.avgScore | toFixed1}}</div>-->
                    <div class="right-item-tip" v-if="interviewer.hot>5">HOT</div>
                    <div class="right-item-tip2" v-else-if="new Date(interviewer.createTime).getTime()>Date.now()-2*7*24*60*60*1000">NEW</div>
                </div>
            </div>
        </div>

        <div class="title-container" style="margin-top: 48px">
            <div class="title-left">本月新入驻面试官</div>
<!--            v-if="userIdentity!==2"-->
<!--            -->
            <div class="title-right" @click="registerInterviewer"  v-if="userIdentity===1" >立即入驻
                <img src="../assets/mock/arrow-gery.png" alt="" class="title-icon">
            </div>
        </div>

        <div class="new-interviewer-container">
            <el-image :src="require('@/assets/mock/arrow2.png')" class="interviewer-arrow" fit="cover" @click="onLeft"></el-image>
            <div class="new-interviewer-scroll" ref="scrollbar">
                <div class="item-detail"
                     v-for="interviewer in newArrivalInterviewerList"
                     :key="interviewer.key"
                     @click="onInterviewer(interviewer)">
                    <el-image :src="interviewer.avatar" class="interviewer-img" fit="cover"></el-image>
                    <div class="interviewer-bottom">
                        <div class="name-left">
                            <div class="name1">{{interviewer.nickName}}</div>
                            <div class="position">{{interviewer.position}}</div>
                        </div>
                        <!--                        <el-image :src="interviewer.companyLogo" class="company-image" fit="scale-down"></el-image>-->
                        <div class="company-name" v-if="interviewer.company">{{interviewer.company.name}}</div>
                    </div>
                </div>
            </div>
            <el-image :src="require('@/assets/mock/arrow1.png')" class="interviewer-arrow" fit="cover" @click="onRight"></el-image>
        </div>
    </div>
</template>

<script>
    export default {
        name: "MockInterview",
        data() {
            return {
                interviewerList: [],// 面试官列表
                newArrivalInterviewerList: [],// 新入驻面试官列表
                stepTimer: undefined, // 滚动列表定时器
                autoTimer: undefined,
                index: 3, // 滚动列表索引
                userIdentity:undefined,//用户身份
            }
        },
        mounted() {
            this.getInterviewerList();
            this.getNewArrivalInterviewerList();
            this.autoTimer = setInterval(() => {
                this.onRight();
            }, 2000);
            this.getInterviewerInfo()
        },
        unmount() {
            this.clearTime();
        },
        destroy() {
            this.clearTime();
        },
        methods: {
            onMoreInterviewer() {
                this.$router.push("/mock/interviewer/more");
            },
            registerInterviewer() {
                this.$router.push("/interviewSecretPage");
            },

            onInterviewer(interviewer) {
                this.$storage.setData("interviewerFrom", {home:true});
                this.$router.push(`/mock/interviewer/${interviewer.id}`);
            },
            getInterviewerInfo() {
                this.$axios.get("/mock/interviewer/my-info").then(data => {
                    this.userIdentity = data.data ? 2 : 1;
                })
            },

            onLeft() {
                if (!this.stepTimer) {

                    this.stepTimer = setInterval(() => {
                        try {
                            if (this.index === 0) {
                                this.$refs.scrollbar.scrollLeft = (this.newArrivalInterviewerList.length - 6) * 256;
                                this.index = this.newArrivalInterviewerList.length - 6;
                            }
                            let offset = (256 * (this.index - 1) - this.$refs.scrollbar.scrollLeft) * 0.1;
                            offset = offset > 0 ? Math.ceil(offset) : Math.floor(offset);
                            this.$refs.scrollbar.scrollLeft += offset;

                            if (256 * (this.index - 1) === Math.round(this.$refs.scrollbar.scrollLeft)) // 到达位置
                            {
                                this.index--;
                                clearInterval(this.stepTimer);
                                this.stepTimer = undefined;
                            }
                        } catch (e) {
                            this.clearTime();
                        }
                    }, 30)
                }
            },
            onRight() {
                if (!this.stepTimer) {
                    this.stepTimer = setInterval(() => {
                        try {
                            if (this.index === this.newArrivalInterviewerList.length - 4) {
                                this.$refs.scrollbar.scrollLeft = 256 * 2;
                                this.index = 2;
                            }
                            let offset = (256 * (this.index + 1) - this.$refs.scrollbar.scrollLeft) * 0.1;
                            offset = offset > 0 ? Math.ceil(offset) : Math.floor(offset);
                            this.$refs.scrollbar.scrollLeft += offset;

                            if (256 * (this.index + 1) === Math.round(this.$refs.scrollbar.scrollLeft)
                                || ((this.index + 1) === this.newArrivalInterviewerList.length - 4
                                    && Math.abs(this.$refs.scrollbar.scrollLeft - (this.index + 1) * 256) < 5)) // 到达位置
                            {
                                this.index++;
                                clearInterval(this.stepTimer);
                                this.stepTimer = undefined;
                            }
                        } catch (e) {
                            console.log("捕获异常");
                            this.clearTime();
                        }
                    }, 30)
                }
            },

            getInterviewerList() {
                this.$axios.get('/mock/interviewer/rank', {
                    params: {limit: 8}
                }).then(data => {
                    this.$emit("complete");
                    let list = data.data.list;
                    if (list.length < 8) {
                        for (let i = list.length; i < 8; i++) {
                            list.push({...list[0]})
                        }
                    }

                    for (let i = 0; i < list.length; i++) {
                        list[i].key = i;
                    }
                    this.interviewerList = list;
                })
            },
            getNewArrivalInterviewerList() {
                this.$axios.get('/mock/interviewer/newarrival', {
                    params: {limit: 8}
                }).then(data => {

                    let list = data.data.list;
                    if (list.length < 4) {
                        for (let i = list.length; i < 4; i++) {
                            list.push({...list[0]})
                        }
                    }
                    this.$emit("onComplete");
                    let newArrivalInterviewerList = list;
                    newArrivalInterviewerList.unshift({...list[list.length - 1]});
                    newArrivalInterviewerList.unshift({...list[list.length - 2]});
                    newArrivalInterviewerList.unshift({...list[list.length - 3]});
                    newArrivalInterviewerList.push({...newArrivalInterviewerList[3]});
                    newArrivalInterviewerList.push({...newArrivalInterviewerList[4]});
                    newArrivalInterviewerList.push({...newArrivalInterviewerList[5]});

                    for (let i = 0; i < newArrivalInterviewerList.length; i++) {
                        newArrivalInterviewerList[i].key = i;
                    }
                    this.newArrivalInterviewerList = newArrivalInterviewerList;
                    this.$nextTick(() => {
                        this.$refs.scrollbar.scrollLeft = 256 * 3;
                    })
                })
            },

            clearTime() {
                if (this.stepTimer) {
                    clearTimeout(this.stepTimer);
                    this.stepTimer = undefined;
                }

                if (this.autoTimer) {
                    clearTimeout(this.autoTimer);
                    this.autoTimer = undefined;
                }
            }
        }
    }
</script>

<style scoped lang="scss">
    .mock-container {
        width: 1200px;
        margin: 0 auto;
        padding: 20px;

        .title-container {
            width: 100%;
            display: flex;
            align-items: center;
            justify-content: space-between;

            .title-left {
                font-size: 24px;
                color: #333333;
                font-weight: bold;
            }

            .title-right {
                display: flex;
                align-items: center;
                font-size: 21px;
                font-weight: 400;
                color: #666666;
                cursor: pointer;

                .title-icon {
                    width: 10px;
                    height: 16px;
                    margin-left: 7px;
                }
            }
        }

        .interviewer-container {
            width: 100%;
            display: flex;
            margin-top: 9px;

            .interviewer-left {
                width: 453px;
                display: flex;
                flex-direction: column;

                .left-item {
                    width: 100%;
                    height: 137px;
                    display: flex;
                    align-items: center;
                    position: relative;
                    /*margin-top: 12px;*/
                    cursor: pointer;

                    .item-left-container {
                        height: 100%;
                        width: 100%;
                        padding: 20px 20px 40px 40px;
                        display: flex;
                        flex-direction: column;
                        justify-content: space-between;

                        .item-title {
                            font-size: 21px;
                            font-weight: bold;
                            color: #FFFFFF;
                            margin-left: 95px;
                        }

                        .item-rate {
                            margin: 14px 0 7px 0;
                        }

                        .item-bottom {
                            display: flex;
                            align-items: center;

                            .item-bottom-icon1 {
                                width: 21px;
                                height: 21px;
                                margin-right: 5px;
                            }

                            .item-bottom-text1 {
                                font-size: 18px;
                                color: #FFFFFF;
                                line-height: 21px;
                            }
                        }
                    }

                    .right-image {
                        width: 98px;
                        height: 98px;
                        border-radius: 50%;
                        overflow: hidden;
                        position: absolute;
                        top: 18px;
                        right: 28px;
                    }
                }

                .index0 {
                    background: url("../assets/mock/mock0.png") no-repeat;
                    background-size: 453px 137px;
                }

                .index1 {
                    background: url("../assets/mock/mock1.png") no-repeat;
                    background-size: 453px 137px;
                }

                .index2 {
                    background: url("../assets/mock/mock2.png") no-repeat;
                    background-size: 453px 137px;
                }
            }

            .interviewer-right {
                width: 660px;
                margin-left: 45px;
                margin-top: -7px;
                display: flex;
                flex-direction: column;

                .right-item {
                    width: 100%;
                    display: flex;
                    height: 59px;
                    align-items: center;
                    margin-top: 23px;
                    cursor: pointer;

                    .right-item1-img1 {
                        width: 86px;
                        height: 29px;
                    }

                    .right-item1-img2 {
                        width: 59px;
                        height: 59px;
                        border-radius: 50%;
                        overflow: hidden;
                        margin-left: 12px;
                    }

                    .right-item1-name {
                        width: 110px;
                        font-size: 18px;
                        color: #0F3057;
                        margin-left: 12px;
                    }

                    .right-item1-img3 {
                        width: 94px;
                        margin-left: 12px;
                        color: #3D6FF4;
                        font-size: 18px;
                    }

                    .right-item1-position {
                        width: 126px;
                        font-size: 18px;
                        color: #333333;
                        margin-left: 12px;
                    }

                    .right-item1-score {
                        width: 30px;
                        font-size: 20px;
                        color: #F9A620;
                        margin-left: 12px;
                    }

                    .right-item-tip {
                        background: #EE6055;
                        box-shadow: 0 5px 13px 0 rgba(238, 96, 85, 0.5);
                        border-radius: 5px;
                        padding: 2px 8px;
                        font-size: 16px;
                        color: #FFFFFF;
                        margin-left: 12px;
                    }

                    .right-item-tip2 {
                        background: #F7B801;
                        box-shadow: 0 5px 13px 0 rgba(247, 184, 1, 0.5);
                        border-radius: 5px;
                        padding: 2px 8px;
                        font-size: 16px;
                        color: #FFFFFF;
                        margin-left: 12px;
                    }
                }
            }
        }

        .new-interviewer-container {
            margin-top: 22px;
            width: 100%;
            height: 233px;
            display: flex;
            align-items: center;
            justify-content: space-between;

            .interviewer-arrow {
                min-width: 66px;
                width: 66px;
                height: 66px;
                cursor: pointer;
            }

            .new-interviewer-scroll {
                height: 100%;
                width: auto;
                overflow: hidden;
                white-space: nowrap;

                .item-detail {
                    width: 190px;
                    height: 100%;
                    position: relative;
                    margin: 0 33px;
                    display: inline-block;
                    cursor: pointer;

                    .interviewer-img {
                        width: 190px;
                        height: 100%;
                    }

                    .interviewer-bottom {
                        width: 170px;
                        height: 57px;
                        background: rgba(255, 255, 255, 0.85);
                        position: absolute;
                        margin: 0 10px;
                        bottom: 8px;
                        padding: 10px 5px;
                        display: flex;
                        align-items: center;
                        justify-content: space-between;

                        .name-left {
                            height: 100%;
                            display: flex;
                            flex-direction: column;
                            justify-content: space-between;

                            .name1 {
                                font-size: 18px;
                                color: #333333;
                            }

                            .position {
                                font-size: 12px;
                                color: #333333;
                            }
                        }

                        .company-image {
                            width: 100px;
                            height: 62px;
                        }

                        .company-name {
                            font-size: 18px;
                            color: #3D6FF4;
                            line-height: 25px;
                        }
                    }
                }

                ::v-deep .el-image__inner {
                    vertical-align: middle !important;
                }
            }
        }
    }
</style>
