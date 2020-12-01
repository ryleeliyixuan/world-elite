<template>
    <div class="app-container">
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/mock/interview' }">模拟面试</el-breadcrumb-item>
            <el-breadcrumb-item>面试官列表</el-breadcrumb-item>
        </el-breadcrumb>
        <div class="filter-container">
            <div class="filter-item">
                <div class="filter-title">行业类型</div>
                <div class="filter">
                    <div :class="[{'selected':item.select},'select-item']" v-for="item in industryList" @click="select1(item)" :key="item.id">{{item.name}}
                    </div>
                </div>
            </div>
            <div class="filter-item">
                <div class="filter-title">从业时长</div>
                <div class="filter">
                    <div :class="[{'selected':item.select},'select-item']" v-for="item in experienceTimeList" @click="select2(item)" :key="item.id">
                        {{item.name}}
                    </div>
                </div>
            </div>
            <div class="filter-item">
                <div class="filter-title">可预约时间</div>
                <div class="filter">
                    <div :class="[{'selected':item.select},'select-item']" v-for="item in reservationList" @click="select3(item)" :key="item.id">{{item.name}}
                    </div>
                </div>
            </div>
            <div class="filter-item">
                <div class="filter-title">推荐指标</div>
                <div class="filter">
                    <div :class="[{'selected':item.select},'select-item']" v-for="item in recommendList" @click="select4(item)" :key="item.id">{{item.name}}
                    </div>
                </div>
            </div>
        </div>

        <div class="interviewer-container">
            <div class="interviewer-item" v-for="interviewer in interviewerList" @click="onInterviewer(interviewer)">
                <div class="interviewer-left">
                    <el-image :src="interviewer.avatar" class="interviewer-image" fit="cover"></el-image>
                    <div class="interviewer-message">
                        <div class="message-top">
                            <div class="message-text">{{interviewer.nickName}}</div>
                            <!--                            <el-image v-if="interviewer.company" :src="interviewer.company.logo" class="message-image" fit="scale-down"></el-image>-->
                            <div class="message-name" v-if="interviewer.company">{{interviewer.company.name}}</div>
                        </div>
                        <div class="message-bottom">
                            <el-image :src="require('@/assets/mock/icon5.png')" class="tip-image" fit="fill"></el-image>
                            <div v-if="interviewer.industry" class="tip-text">{{interviewer.industry.name}}</div>
                            <el-image :src="require('@/assets/mock/icon3.png')" class="tip-image" fit="fill"></el-image>
                            <div class="tip-text">{{interviewer.position}}</div>
                            <el-image :src="require('@/assets/mock/icon4.png')" class="tip-image" fit="fill"></el-image>
                            <div v-if="interviewer.experienceTime" class="tip-text">已从业{{interviewer.experienceTime.name}}</div>
                        </div>
                    </div>
                </div>
                <div class="interviewer-right">
                    <div class="rate-top">
                        <el-image :src="require('@/assets/mock/icon6.png')" class="rate-image" fit="fill"></el-image>
                        <div class="rate-text">{{interviewer.avgScore|toFixed1}}</div>
                    </div>
                    <div class="reservation" @click.stop="onPromise(interviewer)">马上预约</div>
                </div>
            </div>
            <div v-if="interviewerList.length===0 && loading===false" style="display: flex; flex-direction: column; align-items: center;">
                <el-image :src="require('@/assets/mock/empty.png')" style="width:297px; height: 285px;"></el-image>
                <div style="font-size: 36px;font-weight: 600;color: #3D6FF4;line-height: 50px;margin-top: 30px;">Sorry，暂无匹配面试官。</div>
            </div>
        </div>
        <el-pagination
                v-if="interviewerList.length>0"
                size="medium" class="pagination"
                layout="prev, pager, next, jumper"
                :total="total" :page-size="10"
                :current-page.sync="listQuery.page"
                @current-change="getList">
        </el-pagination>
    </div>
</template>

<script>
    export default {
        name: "MoreInterviewer",
        data() {
            return {
                loading: true,
                industryList: [], // 行业标签列表
                experienceTimeList: [], // 从业时长标签列表
                reservationList: [], // 可预约时间标签列表
                recommendList: [
                    {name: '评分', select: false, id: 1, type: 'SCORE'},
                    {name: '价格', select: false, id: 2, type: 'PRICE'},
                    {name: '热度', select: false, id: 3, type: 'HOT'}], // 推荐指标标签列表
                listQuery: {
                    industryId: undefined, // 行业Id
                    experienceTimeId: undefined, // 从业时长Id
                    appointAbleTimeId: undefined, // 可预约时长
                    sortField: undefined, // 排序字段[SCORE-按分数, PRICE-按价格, HOT-按热度]
                    orderBy: "desc", // 降序
                    page: 1,
                    limit: 10,
                },
                interviewerList: [], // 面试官列表
                total: 0,
            }
        },
        created() {
            this.initData();
        },
        mounted() {
            this.getList();
        },
        methods: {
            initData() {

                // 行业标签
                this.$axios.get("/dict/list", {params: {type: 18, limit: 99}}).then(data => {
                    this.industryList = data.data.list.map(item => {
                        item.select = false;
                        return item;
                    })
                })

                // 从业时长
                this.$axios.get("/dict/list", {params: {type: 13, limit: 99}}).then(data => {
                    this.experienceTimeList = data.data.list.filter(item => item.name !== '不限').map(item => {
                        item.select = false;
                        return item;
                    })
                })

                // 可预约时间
                this.$axios.get("/dict/list", {params: {type: 16, limit: 99}}).then(data => {
                    this.reservationList = data.data.list;
                })
            },

            // 行业类型
            select1(item) {
                if (item.select) {
                    item.select = false;
                    this.listQuery.industryId = undefined;
                } else {
                    this.listQuery.industryId = item.id;
                    this.industryList.forEach((industry) => industry.select = industry.id === item.id);
                }
                this.getList();
            },

            // 从业时长
            select2(item) {
                if (item.select) {
                    item.select = false;
                    this.listQuery.experienceTimeId = undefined;
                } else {
                    this.listQuery.experienceTimeId = item.id;
                    this.experienceTimeList.forEach((experienceTime) => experienceTime.select = experienceTime.id === item.id)
                }
                this.getList();
            },

            // 可预约时间
            select3(item) {
                if (item.select) {
                    item.select = false;
                    this.listQuery.appointAbleTimeId = undefined;
                } else {
                    this.listQuery.appointAbleTimeId = item.id;
                    this.reservationList.forEach((reservation) => reservation.select = reservation.id === item.id);
                }
                this.getList();
            },

            // 推荐指标
            select4(item) {
                if (item.select) {
                    item.select = false;
                    this.listQuery.sortField = undefined;
                } else {
                    this.listQuery.sortField = item.type;
                    this.recommendList.forEach((recommend) => recommend.select = recommend.id === item.id);
                }
                this.getList();
            },

            onInterviewer(interviewer) {
                this.$storage.setData("interviewerFrom", {home:false});
                this.$router.push(`/mock/interviewer/${interviewer.id}`);
            },

            // 马上预约
            onPromise(interviewer) {
                this.$router.push(`/mock/promise/${interviewer.id}`);
            },

            getList() {
                this.loading = true;
                this.$axios.get("/mock/interviewer/list", {params: this.listQuery}).then(data => {
                    // this.loading = false;
                    this.interviewerList = data.data.list;
                    this.total = data.data.total;
                })
            }
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
            margin-top: 27px;

            .filter-item {
                width: 100%;
                display: flex;
                align-items: center;
                margin-top: 9px;

                .filter-title {
                    width: 80px;
                    min-width: 80px;
                    font-size: 16px;
                    color: #333333;
                    margin-top: 4px;
                    margin-right: 40px;
                    text-align: right;
                }

                .filter {
                    display: flex;
                    align-items: center;
                    flex-wrap: wrap;

                    .select-item {
                        margin-top: 4px;
                        width: 86px;
                        height: 25px;
                        background: #F5F5F5;
                        border-radius: 17px;
                        border: 1px solid #F5F5F5;
                        font-size: 14px;
                        color: #333333;
                        margin-right: 16px;
                        cursor: pointer;
                        line-height: 25px;
                        text-align: center;
                    }

                    .selected {
                        border: 1px solid #698EC7;
                        background: #F4F5F8;
                        color: #698EC7;
                    }
                }
            }
        }

        .interviewer-container {
            width: 100%;
            margin-top: 31px;

            .interviewer-item {
                width: 100%;
                height: 118px;
                padding: 16px 44px 16px 6px;
                display: flex;
                align-items: center;
                box-shadow: 0 3px 13px 1px #DEE5F4;
                margin-bottom: 31px;
                justify-content: space-between;
                cursor: pointer;

                .interviewer-left {
                    display: flex;
                    align-items: center;

                    .interviewer-image {
                        width: 87px;
                        height: 87px;
                        border-radius: 5px;
                    }

                    .interviewer-message {
                        height: 87px;
                        display: flex;
                        margin-left: 23px;
                        flex-direction: column;
                        justify-content: space-between;

                        .message-top {
                            display: flex;
                            align-items: center;

                            .message-text {
                                font-size: 24px;
                                color: #333333;
                            }

                            .message-image {
                                width: 130px;
                                height: 31px;
                                margin-left: 17px;
                            }

                            .message-name {
                                margin-left: 22px;
                                color: #3D6FF4;
                            }
                        }

                        .message-bottom {
                            display: flex;
                            align-items: center;

                            .tip-image {
                                width: 23px;
                                height: 23px;
                                margin-right: 8px;
                            }

                            .tip-text {
                                font-size: 16px;
                                color: #333333;
                                margin-right: 33px;
                            }
                        }
                    }
                }

                .interviewer-right {
                    height: 72px;
                    display: flex;
                    flex-direction: column;
                    align-items: center;
                    justify-content: space-between;

                    .rate-top {
                        display: flex;
                        align-items: center;

                        .rate-image {
                            width: 26px;
                            height: 26px;
                        }

                        .rate-text {
                            font-size: 21px;
                            color: #999999;
                            margin-left: 7px;
                        }
                    }

                    .reservation {
                        width: 123px;
                        height: 31px;
                        background: rgba(255, 189, 105, 0.3);
                        border-radius: 21px;
                        border: 1px solid #F78259;
                        font-size: 16px;
                        color: #F78259;
                        line-height: 31px;
                        text-align: center;
                    }
                }
            }
        }

        .pagination {
            margin-top: 20px;
            align-items: center;
            justify-content: center;

            ::v-deep .number, ::v-deep .more {
                width: 37px;
                height: 37px;
                border-radius: 50%;
                background: #EEEEEE;
                box-shadow: 0 5px 11px 0 #CCCCCC;
                line-height: 37px;
                text-align: center;
                margin: 0 6px;
                color: #999999;

                &.active {
                    color: white;
                    background: #4C90FC;
                    box-shadow: 0 5px 11px 0 rgba(30, 150, 252, 0.5);
                }
            }

            ::v-deep .btn-prev, ::v-deep .btn-next {
                width: 37px;
                height: 37px;
                border-radius: 50%;
                background: #EEEEEE;
                box-shadow: 0 5px 11px 0 #CCCCCC;
                line-height: 37px;
                text-align: center;
                margin: 0 6px;
                color: #999999;
                padding: 0;

                & .el-icon {
                    font-size: 18px;
                }
            }

            ::v-deep .el-pagination__jump {
                font-size: 18px;
                color: #999999;
                line-height: 25px;
                height: 37px;

                .el-input {
                    width: 66px;
                    height: 37px;
                    margin: 0 6px;

                    .el-input__inner {
                        width: 66px;
                        height: 37px;
                        font-size: 18px;
                    }
                }
            }
        }
    }
</style>
