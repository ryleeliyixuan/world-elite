<template>
    <div class="app-container">
        <div class="section1-container">
            <div class="left-container">
                <el-carousel :interval="5000" arrow="always">
                    <el-carousel-item v-for="item in 4" :key="item">
                        <el-image style="width: 100%; height: 100%;" fit="cover" :src="img"></el-image>
                    </el-carousel-item>
                </el-carousel>
            </div>
            <div class="right-container">
                <div class="button-container">
                    <el-image class="image" :src="require('@/assets/activity/1.png')" fit="scale-down"></el-image>
                    <div class="text-container">
                        <div class="text-major">我报名的活动</div>
                        <div class="text-minor">点击查看更多</div>
                    </div>
                </div>
                <div class="button-container">
                    <div class="text-container">
                        <div class="text-major">我发布的活动</div>
                        <div class="text-minor">点击查看更多</div>
                    </div>
                    <el-image class="image" :src="require('@/assets/activity/2.png')" fit="scale-down"></el-image>
                </div>
                <div class="button-container">
                    <el-image class="image" :src="require('@/assets/activity/3.png')" fit="scale-down"></el-image>
                    <div class="text-container">
                        <div class="text-major">发布新活动</div>
                        <div class="text-minor">点击查看更多</div>
                    </div>
                </div>
            </div>
        </div>

        <div class="content-container">
            <div class="filter-container">
                <div class="filter-name">
                    <div class="title">活动</div>
                    <div class="icon">
                        <el-image :src="require('@/assets/activity/events.png')" style="width: 78px; height: 17px;"></el-image>
                        <div class="clear" @click="onClear">清空选项</div>
                    </div>
                </div>
                <div class="filter-item">
                    <div class="filter-title">活动形式：</div>
                    <div class="filter">
                        <div :class="[{'selected':item.id === listQuery.form},'select-item']" v-for="item in formList" @click="onMode(item)" :key="item.id">
                            {{item.name}}
                        </div>
                    </div>
                </div>
                <div class="filter-item" v-if="listQuery.form===1">
                    <div class="filter-title">活动城市：</div>
                    <div class="filter">
                        <div :class="[{'selected':item.value===listQuery.cityIds},'select-item']" v-for="item in cityList" @click="onCity(item)" :key="item.id">
                            {{item.name}}
                        </div>
                    </div>
                </div>
                <div class="filter-item">
                    <div class="filter-title">活动状态：</div>
                    <div class="filter">
                        <div :class="[{'selected':item.id===listQuery.status},'select-item']" v-for="item in statusList" @click="onStatus(item)" :key="item.id">
                            {{item.name}}
                        </div>
                    </div>
                </div>
                <div class="filter-item">
                    <div class="filter-title">活动时间：</div>
                    <div class="filter">
                        <div :class="[{'selected':item.id===listQuery.timeId},'select-item']" v-for="item in timeList" @click="onTime(item)" :key="item.id">
                            {{item.name}}
                        </div>
                    </div>
                </div>
                <div class="filter-item">
                    <div class="filter-title">活动排序：</div>
                    <div class="filter">
                        <div :class="[{'selected':item.id===listQuery.sortField},'select-item']" v-for="item in orderList" @click="onOrder(item)"
                             :key="item.id">
                            {{item.name}}
                        </div>
                    </div>
                </div>
            </div>

            <div class="type-container">
                <div :class="listQuery.publisherType===2?'selected':'normal'" @click="onPublishType(2)">
                    企业发布
                    <div class="line" v-if="listQuery.publisherType===2"></div>
                </div>
                <div :class="listQuery.publisherType===1?'selected':'normal'" @click="onPublishType(1)">
                    个人发布
                    <div class="line" v-if="listQuery.publisherType===1"></div>
                </div>
                <div :class="listQuery.publisherType===3?'selected':'normal'" @click="onPublishType(3)">
                    留学生专区
                    <div class="line" v-if="listQuery.publisherType===3"></div>
                </div>
            </div>

            <div class="activity-container">
                <div class="activity-item" v-for="item in dataList" :key="item.id" @click="onItem(item)">
                    <el-image class="background-image" :src="img" fit="cover"></el-image>
                    <el-image class="subscript" :src="require('@/assets/activity/geren.png')" v-if="item.userType==='1'"></el-image>
                    <el-image class="subscript" :src="require('@/assets/activity/qiye.png')" v-if="item.userType==='2'"></el-image>
                    <div class="brief">
                        <div class="line1">
                            <div class="title">{{item.title}}</div>
                            <div v-if="item.city" :class="['city',{'online':item.city.id===999992 || item.city.id ===999993}]">{{item.city.name}}</div>
                        </div>
                        <div class="line2">
                            <div class="time">{{item.activityStartTime | timestampToDateHourMinute}}-{{item.activityFinishTime | timestampToHoursMinutes}}</div>
                            <div class="count">{{item.follower}}人正在关注</div>
                        </div>
                    </div>
                </div>
                <div v-if="dataList.length===0 && loading===false" style="width:100%; display: flex; flex-direction: column; align-items: center;">
                    <el-image :src="require('@/assets/mock/empty.png')" style="width:297px; height: 285px;"></el-image>
                    <div style="font-size: 36px;font-weight: 600;color: #3D6FF4;line-height: 50px;margin-top: 30px;">Sorry，暂无匹配活动。</div>
                </div>
            </div>

            <pagination :total="total"
                        :limit="10"
                        :page.sync="listQuery.page"
                        @pagination="getList">
            </pagination>
        </div>
    </div>
</template>

<script>
    import Pagination from "@/components/Pagination2";

    export default {
        name: "ActivityListPage",
        components: {Pagination},
        data() {
            return {
                img: 'https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=238583692,2460632321&fm=26&gp=0.jpg',
                loading: true,

                formList: [{id: 0, name: '线上'}, {id: 1, name: '线下'}], // 活动形式列表
                cityList: [], // 活动城市列表
                statusList: [{id: 3, name: '即将开始'}, {id: 4, name: '报名中'},
                    {id: 5, name: '进行中'}, {id: 6, name: '已结束'}], // 活动状态 0审核中;1草稿;2下架;3即将开始(报名即将开始和活动即将开始都是3);4报名中;5进行中;6活动结束
                timeList: [], // 活动时间列表
                orderList: [{id: "PUBLISH_TIME", name: '最新发布'}, {id: "FOLLOWER", name: '最多关注'},
                    {id: "REGISTRATION_TIME", name: '最多报名'}, {id: "ACTIVITY_TIME", name: '最多开始'}], // 活动排序列表
                listQuery: {
                    keyword: undefined, // 搜索关键字
                    form: undefined, // 活动形式
                    cityIds: undefined, // 城市ID 线上国内=999992; 线上国外=999993
                    status: undefined, // 活动状态
                    timeId: undefined, // 活动时间
                    sort: "desc", // 降序
                    sortField: undefined, // 排序字段
                    publisherType: 2, // 活动发布者类型
                    page: 1,
                    limit: 12
                },

                dataList: [], // 活动列表
                total: 0, // 活动总数
            };
        },
        watch: {
            $route() {
                this.handleFilter();
            }
        },
        mounted() {
            let query = this.$storage.getObject(this.$options.name)
            if (query) {
                this.listQuery = query;
            }
            this.initData();
            this.getList();
        },
        methods: {
            // 初始化数据
            initData() {
                // 活动时间
                this.$axios.get("/dict/list", {params: {type: 22, limit: 99}}).then(data => {
                    this.timeList = data.data.list;
                })

                // 活动城市
                this.$axios.get("/dict/list", {params: {type: 23, limit: 99}}).then(data => {
                    this.cityList = data.data.list;
                })
            },

            // 活动形式
            onMode(item) {
                if (this.listQuery.form === item.id) {
                    this.listQuery.form = undefined;
                } else {
                    this.listQuery.form = item.id;
                }
                this.listQuery.cityIds = undefined;
                this.handleFilter();
            },

            // 活动城市
            onCity(item) {
                if (this.listQuery.cityIds === item.value) {
                    this.listQuery.cityIds = undefined;
                } else {
                    this.listQuery.cityIds = item.value;
                }
                this.handleFilter();
            },

            // 活动状态
            onStatus(item) {
                if (this.listQuery.status === item.id) {
                    this.listQuery.status = undefined;
                } else {
                    this.listQuery.status = item.id;
                }
                this.handleFilter();
            },

            // 活动时间
            onTime(item) {
                if (this.listQuery.timeId === item.id) {
                    this.listQuery.timeId = undefined;
                } else {
                    this.listQuery.timeId = item.id;
                }
                this.handleFilter();
            },

            // 活动时间
            onOrder(item) {
                if (this.listQuery.sortField === item.id) {
                    this.listQuery.sortField = undefined;
                } else {
                    this.listQuery.sortField = item.id;
                }
                this.handleFilter();
            },

            // 选择活动发布类型
            onPublishType(publisherType) {
                this.listQuery.publisherType = publisherType;
                this.handleFilter();
            },

            // 点击活动，查看活动详情
            onItem(activity) {
                this.$router.push(`/activity/${activity.id}`);
            },

            // 清空选项
            onClear() {
                this.listQuery.form = undefined;
                this.listQuery.cityIds = undefined;
                this.listQuery.status = undefined;
                this.listQuery.timeId = undefined;
                this.listQuery.sortField = undefined;
                this.listQuery.publisherType = 2;
                this.handleFilter();
            },

            // 条件改变
            handleFilter() {
                this.listQuery.page = 1;
                this.getList();
            },

            // 加载数据
            getList() {
                this.listQuery.keyword = this.$route.query.searchWord;
                this.$storage.setData(this.$options.name, this.listQuery);
                this.loading = true;
                this.$axios.get("/activity/list", {params: this.listQuery}).then(response => {
                    this.loading = false;
                    this.dataList = response.data.list;
                    this.total = response.data.total;
                });
            },


        }
    };
</script>

<style lang="scss" scoped>
    .app-container {
        width: 1200px !important;
        margin: 0 auto;

        .section1-container {
            min-width: 335px;
            display: flex;
            justify-content: space-between;

            .left-container {
                width: 796px;

                ::v-deep .el-carousel__container {
                    height: 448px;
                }
            }

            .right-container {
                width: 385px;
                height: 448px;
                display: flex;
                flex-direction: column;
                justify-content: space-between;

                .button-container {
                    height: 137px;
                    background: #FFFFFF;
                    box-shadow: 0 3px 7px 0 #CCCCCC;
                    display: flex;
                    align-items: center;
                    padding: 10px;
                    cursor: pointer;


                    .image {
                        flex: 1;
                        height: 100%;
                    }

                    .text-container {
                        display: flex;
                        flex-direction: column;
                        align-items: center;
                        flex: 1;

                        .text-major {
                            font-size: 21px;
                            color: #333333;
                            line-height: 29px;
                        }

                        .text-minor {
                            font-size: 14px;
                            font-weight: 400;
                            color: #999999;
                            line-height: 20px;
                            margin-top: 4px;
                        }
                    }
                }
            }
        }

        .content-container {
            background: #FFFFFF;
            box-shadow: 0 4px 16px 3px rgba(191, 199, 215, 0.31);

            .filter-container {
                width: 100%;
                margin-top: 31px;
                padding: 22px 39px;

                .filter-name {
                    margin-bottom: 20px;

                    .title {
                        font-size: 24px;
                        color: #333333;
                        line-height: 24px;
                        font-weight: 600;
                    }

                    .icon {
                        display: flex;
                        align-items: center;

                        .clear {
                            margin-left: 20px;
                            font-size: 14px;
                            font-weight: 400;
                            color: #666666;
                            line-height: 20px;
                            cursor: pointer;
                        }
                    }
                }

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
                        margin: 4px 20px 0 5px;
                        text-align: right;
                        font-weight: 600;
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

            .type-container {
                display: flex;
                align-items: center;
                border-bottom: 1px #B0BEC5 solid;
                margin: 20px 18px;

                .selected {
                    font-size: 21px;
                    font-weight: 600;
                    color: #304FFE;
                    line-height: 29px;
                    display: flex;
                    flex-direction: column;
                    align-items: center;
                    padding: 5px 10px;
                    position: relative;
                    cursor: pointer;

                    .line {
                        width: 100%;
                        height: 6px;
                        border-radius: 3px;
                        background: #304FFE;
                        position: absolute;
                        bottom: -3px;
                    }
                }

                .normal {
                    font-size: 18px;
                    color: #333333;
                    line-height: 25px;
                    padding: 5px 10px;
                    cursor: pointer;
                }
            }

            .activity-container {
                width: 100%;
                display: flex;
                flex-wrap: wrap;
                min-height: 400px;
                padding: 0 19px 30px;

                .activity-item {
                    position: relative;
                    width: 377px;
                    height: 212px;
                    margin: 10px 5px;
                    cursor: pointer;

                    .background-image {
                        width: 100%;
                        height: 100%;
                    }

                    .subscript {
                        position: absolute;
                        top: 0;
                        left: 0;
                        width: 66px;
                        height: 62px;
                    }

                    .brief {
                        position: absolute;
                        width: 100%;
                        left: 0;
                        bottom: 0;
                        height: 57px;
                        background: rgba(0, 0, 0, 0.55);
                        display: flex;
                        flex-direction: column;
                        padding: 5px;

                        .line1 {
                            display: flex;
                            align-items: center;

                            .title {
                                font-size: 18px;
                                color: #FFFFFF;
                                line-height: 25px;
                                overflow: hidden;
                                text-overflow: ellipsis;
                                white-space: nowrap;
                            }

                            .city {
                                background: #00c853;
                                height: 19px;
                                line-height: 22px;
                                padding: 0 5px;
                                border-radius: 4px;
                                font-size: 13px;
                                flex-shrink: 0;
                                margin-left: 7px;
                                color: #ffffff;
                            }

                            .online {
                                background: #ffab00;
                            }
                        }

                        .line2 {
                            display: flex;
                            align-items: center;
                            justify-content: space-between;

                            .time {
                                font-size: 14px;
                                color: #FFFFFF;
                                line-height: 20px;
                            }

                            .count {
                                font-size: 12px;
                                color: #FFFFFF;
                                line-height: 17px;
                            }
                        }
                    }
                }
            }
        }
    }
</style>
