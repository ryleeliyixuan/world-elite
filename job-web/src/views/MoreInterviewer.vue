<template>
    <div class="app-container">
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/mock-interview' }">模拟面试</el-breadcrumb-item>
            <el-breadcrumb-item>面试官列表</el-breadcrumb-item>
        </el-breadcrumb>
        <div class="filter-container">
            <div class="filter-item">
                <div class="filter-title">行业类型:</div>
                <div :class="item.select?'selected':'select-item'" v-for="item in industryList" @click="select1(item)" :key="item.id">{{item.name}}</div>
            </div>
            <div class="filter-item">
                <div class="filter-title">从业时长:</div>
                <div :class="item.select?'selected':'select-item'" v-for="item in yearsList" @click="select2(item)" :key="item.id">{{item.name}}</div>
            </div>
            <div class="filter-item">
                <div class="filter-title">可预约时间:</div>
                <div :class="item.select?'selected':'select-item'" v-for="item in reservationList" @click="select3(item)" :key="item.id">{{item.name}}</div>
            </div>
            <div class="filter-item">
                <div class="filter-title">推荐指标:</div>
                <div :class="item.select?'selected':'select-item'" v-for="item in recommendList" @click="select4(item)" :key="item.id">{{item.name}}</div>
            </div>
        </div>

        <div class="interviewer-container">
            <div class="interviewer-item" @click="onInterviewer">
                <div class="interviewer-left">
                    <el-image :src="require('@/assets/demo.jpg')" class="interviewer-image"  fit="fill"></el-image>
                    <div class="interviewer-message">
                        <div class="message-top">
                            <div class="message-text">Cathy</div>
                            <el-image :src="require('@/assets/mock/company-logo.png')"class="message-image"  fit="fill"></el-image>
                        </div>
                        <div class="message-bottom">
                            <el-image :src="require('@/assets/mock/icon5.png')"  class="tip-image"  fit="fill"></el-image>
                            <div class="tip-text">互联网</div>
                            <el-image :src="require('@/assets/mock/icon3.png')"  class="tip-image"  fit="fill"></el-image>
                            <div class="tip-text">战略分析师</div>
                            <el-image :src="require('@/assets/mock/icon4.png')" class="tip-image"  fit="fill"></el-image>
                            <div class="tip-text">已从业1~3年</div>
                        </div>
                    </div>
                </div>
                <div class="interviewer-right">
                    <div class="rate-top">
                        <el-image :src="require('@/assets/mock/icon6.png')" class="rate-image"  fit="fill"></el-image>
                        <div class="rate-text">5.0</div>
                    </div>
                    <div class="reservation">马上预约</div>

                </div>

            </div>

        </div>
<div class="section2-container">
    <pagination
            v-show="total"
            :total="total"
            :page.sync="listQuery.page"
            :limit.sync="listQuery.limit"
            @pagination="handleRouteList"
    />
</div>


    </div>

</template>

<script>
    import {formatListQuery} from "@/utils/common";

    export default {
        name: "MoreInterviewer",
        data() {
            return {
                industryList: [{name: '不限', select: true, id: 1}, {name: '互联网', select: false, id: 2}, {name: '金融', select: false, id: 3}, {
                    name: '咨询',
                    select: false,
                    id: 4
                }, {name: '媒体', select: false, id: 5}, {name: '房地产', select: false, id: 6}],
                yearsList: [{name: '不限', select: true, id: 1}, {name: '1~3年', select: false, id: 2}, {name: '3~5年', select: false, id: 3}, {
                    name: '5年以上',
                    select: false,
                    id: 4
                }],
                reservationList: [{name: '不限', select: true, id: 1}, {name: '1~3天内', select: false, id: 2}, {name: '3~5天内', select: false, id: 3}, {
                    name: '本周内',
                    select: false,
                    id: 4
                }, {name: '两周内', select: false, id: 5}, {name: '本月内', select: false, id: 6}],
                recommendList: [{name: '评分', select: true, id: 1}, {name: '价格', select: false, id: 2}, {name: '热度', select: false, id: 3}],
                listQuery: {
                    page: 1,
                    limit: 10,
                    type: 1,
                    sort: "-id",
                },
                total: 12,

            }
        },
        methods: {
            select1(item) {
                this.industryList.filter((e) => {
                    if (e.id === item.id) {
                        e.select = true
                    } else {
                        e.select = false
                    }
                    return e
                })

            },
            select2(item) {
                this.yearsList.filter((e) => {
                    if (e.id === item.id) {
                        e.select = true
                    } else {
                        e.select = false
                    }
                    return e
                })

            },
            select3(item) {
                this.reservationList.filter((e) => {
                    if (e.id === item.id) {
                        e.select = true
                    } else {
                        e.select = false
                    }
                    return e
                })

            },
            select4(item) {
                this.recommendList.filter((e) => {
                    if (e.id === item.id) {
                        e.select = true
                    } else {
                        e.select = false
                    }
                    return e
                })

            },
            onInterviewer(){
                this.$router.push("/interviewer-detail");
            },
            handleRouteList() {
                this.$router.push({
                    path: this.$route.path,
                    query: formatListQuery(this.listQuery),
                });
            },

        }
    }
</script>

<style scoped lang="scss">
    .app-container {
        max-width: 1200px;
        margin: 0 auto;
        height: auto;
        padding: 20px;

        /deep/ .el-breadcrumb {
            font-size: 18px;
        }

        .filter-container {
            width: 100%;

            .filter-item {
                width: 100%;
                display: flex;
                align-items: center;
                margin-top: 30px;

                .filter-title {
                    width: 120px;
                    font-size: 21px;
                    font-weight: 500;
                    color: #333333;
                }

                .select-item {
                    padding: 4px 30px;
                    background: #F5F5F5;
                    border-radius: 17px;
                    font-size: 18px;
                    color: #333333;
                    margin-right: 17px;
                    cursor: pointer;
                }

                .selected {
                    border: 1px solid #698EC7;
                    background: #F4F5F8;
                    color: #698EC7;
                    margin-right: 17px;
                    padding: 4px 30px;
                    border-radius: 17px;
                    font-size: 18px;
                    cursor: pointer;

                }
            }

        }

        .interviewer-container {
            width: 100%;
            height: auto;
            .interviewer-item{
                width: 100%;
                height: 118px;
                padding: 16px 32px 20px 11px;
                display: flex;
                align-items: center;
                box-shadow: 0px 3px 13px 1px #DEE5F4;
                margin-top: 63px;
                justify-content: space-between;
                cursor: pointer;
                .interviewer-left{
                    display: flex;
                    align-items: center;
                    .interviewer-image{
                        width: 87px;
                        height: 87px;
                    }
                    .interviewer-message{
                        height: 87px;
                        display: flex;
                        margin-left: 23px;
                        flex-direction: column;
                        justify-content: space-between;
                        .message-top{
                            display: flex;
                            align-items: center;
                            .message-text{
                                font-size: 28px;
                                color: #333333;
                            }
                            .message-image{
                                width: 118px;
                                height: 30px;
                                margin-left: 17px;
                            }
                        }
                        .message-bottom{
                            display: flex;
                            align-items: center;
                            .tip-image{
                                width: 25px;
                                height: 25px;
                                margin-right: 8px;
                            }
                            .tip-text{
                                font-size: 18px;
                                color: #333333;
                                margin-right: 38px;
                            }

                        }
                    }
                }
                .interviewer-right{
                    height: 87px;
                    display: flex;
                    flex-direction: column;
                    align-items: center;
                    justify-content: space-between;
                    .rate-top{
                        display: flex;
                        align-items: center;
                        .rate-image{
                            width: 26px;
                            height: 26px;
                        }
                        .rate-text{
                            font-size: 21px;
                            color: #999999;
                            margin-left: 1px;
                        }
                    }
                    .reservation{
                        background: rgba(255, 189, 105, 0.3);
                        border-radius: 21px;
                        border: 1px solid #F78259;
                        padding: 8px 32px;
                        font-size: 18px;
                        color: #F78259;
                    }
                }
            }

        }
        .section2-container {
            width: 100%;
            overflow-x: auto;
        }

    }

</style>
