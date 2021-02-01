<template>
    <div class="app-container">
        <div class="section1-container">
            <div class="left-container">
                <el-carousel :interval="5000" arrow="always">
                    <el-carousel-item v-for="(item, index) in bannerList" :key="index" @click.native="onBannerItem(item)">
                        <el-image style="width: 100%; height: 100%; cursor: pointer" fit="cover" :src="item.poster"></el-image>
                    </el-carousel-item>
                </el-carousel>
            </div>
            <div class="right-container">
                <div class="button-container" @click="onMyActivity">
                    <el-image class="image" :src="require('@/assets/activity/1.png')" fit="scale-down"></el-image>
                    <div class="text-container">
                        <div class="text-major">报名和关注的活动</div>
                        <div class="text-minor">点击查看更多</div>
                    </div>
                </div>
                <div class="button-container" @click="onPublish">
                    <div class="text-container">
                        <div class="text-major">我发布的活动</div>
                        <div class="text-minor">点击查看更多</div>
                    </div>
                    <el-image class="image" :src="require('@/assets/activity/2.png')" fit="scale-down"></el-image>
                </div>
                <div class="button-container" @click="onNewActivity">
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
                <div class="filter-item" v-if="listQuery.form!==0">
                    <div class="filter-title">活动城市：</div>
                    <div class="filter">
                        <div :class="[{'selected':item.selected},'select-item']" v-for="item in cityList" @click="onCity(item)" :key="item.id">
                            {{item.name}}
                        </div>
                        <el-select class="search-city"
                                   v-model="searchCityIds"
                                   multiple
                                   filterable
                                   remote
                                   placeholder="输入国内外城市名，支持多个"
                                   :remote-method="searchCity"
                                   :loading="loading"
                        @change="handleFilter">
                            <el-option
                                v-for="item in cityOptions"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id">
                            </el-option>
                            <div slot="prefix" class="search-button">
                                搜索
                            </div>
                        </el-select>
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
                    <el-image class="background-image" :src="item.poster" fit="cover"></el-image>
                    <el-image class="subscript" :src="require('@/assets/activity/geren.png')" v-if="item.userType==='1'"></el-image>
                    <el-image class="subscript" :src="require('@/assets/activity/qiye.png')" v-if="item.userType==='2'"></el-image>
                    <div class="brief">
                        <div class="line1">
                            <div class="title">{{item.title}}</div>
                            <div v-if="item.city" :class="['city',{'online':item.city.id===3}]">{{item.city.name}}</div>
                            <div class="status" :style="{'background':statusBGColorList[item.status]}">{{getStatus(item)}}</div>
                        </div>
                        <div class="line2">
                            <div class="time">{{getTime(item)}}</div>
                            <div class="count">{{item.follower}}人正在关注</div>
                        </div>
                    </div>
                </div>
                <div v-if="dataList.length===0 && loading===false" style="width:100%; display: flex; flex-direction: column; align-items: center;">
                    <el-image :src="require('@/assets/activity/empty.png')" style="width:344px; height: 265px;"></el-image>
                    <div style="font-size: 16px;color: #333333;line-height: 22px;margin-top: 24px;">暂无活动，敬请期待!</div>
                </div>
            </div>

            <pagination :total="total"
                        :limit="10"
                        :page.sync="listQuery.page"
                        @pagination="getList">
            </pagination>
        </div>

        <approve :visible.sync="showApproveDialog" @close="getApprove"></approve>
    </div>
</template>

<script>
    import Pagination from "@/components/Pagination2";
    import approve from "@/components/activity/ApproveDialog";
    import {timestampToDate} from "@/filters/filters"

    export default {
        name: "ActivityListPage",
        components: {Pagination, approve},
        data() {
            return {
                img: 'https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=238583692,2460632321&fm=26&gp=0.jpg',
                loading: true,

                formList: [{id: 0, name: '线上'}, {id: 1, name: '线下'}], // 活动形式列表
                cityList: [], // 活动城市列表
                cityOptions: [], // 可选的城市列表
                searchCityIds: undefined, // 搜索城市关键字
                statusList: [{id: 3, name: '即将开始'}, {id: 4, name: '报名中'}, {id: 5, name: '进行中'}, {id: 6, name: '已结束'}],
                // 活动状态 0审核中;1草稿;2下架;3即将开始(报名即将开始和活动即将开始都是3);4报名中;5进行中;6活动结束;7审核未通过
                statusBGColorList: ['#4895EF', '#C6FF00', '#B71C1C', '#FFC400', '#66BB6A', '#FF6E40', '#FF5252', '#37474F'],

                timeList: [], // 活动时间列表
                orderList: [{id: "PUBLISH_TIME", name: '最新发布'}, {id: "FOLLOWER", name: '最多关注'},
                    {id: "REGISTRATION_TIME", name: '最近报名'}, {id: "ACTIVITY_TIME", name: '最近开始'}], // 活动排序列表
                listQuery: {
                    keyword: undefined, // 搜索关键字
                    form: undefined, // 活动形式
                    cityIds: undefined, // 城市ID
                    status: undefined, // 活动状态
                    timeId: undefined, // 活动时间
                    sort: "desc", // 降序
                    sortField: undefined, // 排序字段
                    publisherType: 2, // 活动发布者类型
                    page: 1,
                    limit: 12
                },

                bannerList: [], // 轮播图列表
                dataList: [], // 活动列表
                total: 0, // 活动总数

                showApproveDialog: false, // 显示实名认证对话框
                approveStatus: undefined, // 实名认证状态   审核状态.1:审核中,2:通过,3拒绝",
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
                this.$axios.get("/dict/list", {params: {type: 22, limit: 99, sort: '+id'}}).then(data => {
                    this.timeList = data.data.list;
                })

                // 活动城市
                this.$axios.get("/dict/list", {params: {type: 23, limit: 99, sort: '+id'}}).then(data => {
                    if (this.listQuery.cityIds) {
                        let cityIds = this.listQuery.cityIds.split(",");
                        this.cityList = data.data.list.map(item => {
                            item.selected = cityIds.includes(item.value)
                        })
                    } else {
                        this.cityList = data.data.list;
                    }
                })

                // 活动城市
                this.$axios.get("/activity/carousel").then(data => {
                    this.bannerList = data.data;
                })

                // 查看我的实名认证状态
                this.getApprove();
            },

            // 查看我的实名认证状态
            getApprove() {
                this.$axios.get(`/realnameauth/${this.$store.state.user.userId}`).then(response => {
                    this.approveStatus = response.data && response.data.status;
                })
            },

            // 点击轮播图活动
            onBannerItem(activity) {
                this.$router.push(`/activity/${activity.id}`);
            },

            // 报名和关注的活动
            onMyActivity() {
                this.$router.push('/my-activities');
            },

            // 我发布的活动
            onPublish() {
                this.$router.push('/activity/publish');
            },

            // 发布新活动
            onNewActivity() {
                if (this.approveStatus === 1) { // 审核中
                    this.$alert("您的实名认证尚在审核中，我们会尽快处理")
                } else if (this.approveStatus === 3) { // 审核被拒绝
                    this.showApproveDialog = true;
                } else if (this.approveStatus === 2) { // 审核已通过，可以发布新活动
                    // 发布新活动前先清楚预览数据
                    this.$storage.removeObject('activityPreview');
                    this.$router.push('/activity/edit');
                } else { // 未提交审核信息
                    this.$confirm('首次发布活动需要进行实名认证，点击“去认证”进入认证页', '提示', {
                        confirmButtonText: '去认证',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        this.showApproveDialog = true;
                    }).catch(() => {

                    });
                }
            },

            // 活动形式
            onMode(item) {
                if (this.listQuery.form === item.id) {
                    this.listQuery.form = undefined;
                } else {
                    this.listQuery.form = item.id;
                }
                this.cityList.forEach(item => {
                    item.selected = false;
                })
                this.handleFilter();
            },

            // 活动城市
            onCity(item) {
                this.$set(item, "selected", !item.selected);
                this.handleFilter();
            },

            // 搜索城市
            searchCity(keyword) {
                if (!keyword || keyword.length < 1) {
                    return;
                }
                this.$axios.get("/city/get-to-city-level-by-name", {params: {name: keyword}}).then((response) => {
                    this.cityOptions = response.data
                });
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
                this.listQuery.cityIds = this.searchCityIds.join(",");
                this.$storage.setData(this.$options.name, this.listQuery);
                this.loading = true;
                this.$axios.get("/activity/list", {params: this.listQuery}).then(response => {
                    this.loading = false;
                    this.dataList = response.data.list;
                    this.total = response.data.total;
                });
            },

            // 获取活动状态
            getStatus(activity) {
                return this.statusList.find(item => activity.status === item.id).name;
            },

            // 处理时间
            getTime(item) {
                let start = timestampToDate(item.activityStartTime);
                let finish = timestampToDate(item.activityFinishTime);
                return start === finish ? start : start + " - " + finish;
            }
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
                padding: 22px 23px 22px 39px;;

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
                    margin-top: 9px;

                    .filter-title {
                        width: 80px;
                        min-width: 80px;
                        font-size: 16px;
                        color: #333333;
                        margin: 5px 20px 0 5px;
                        text-align: right;
                        font-weight: 600;
                    }

                    .filter {
                        display: flex;
                        align-items: center;
                        flex-wrap: wrap;

                        .select-item {
                            margin-top: 4px;
                            min-width: 86px;
                            padding: 0 10px;
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

                        .search-city {
                            margin-top: 4px;
                            margin-right: 16px;

                            ::v-deep .el-input__inner {
                                width: 290px;
                                padding: 0 0 0 13px;
                                height: 29px;
                                background: #FFFFFF;
                                border-radius: 18px;
                                border: 1px solid #CFD8DC;
                                display: flex;
                                align-items: center;
                            }

                            ::v-deep .el-input__prefix {
                                right: 5px;

                                .search-button {
                                    width: 82px;
                                    height: 24px;
                                    background: #4895EF;
                                    box-shadow: 0 3px 3px 0 #A7C7F8;
                                    border-radius: 18px;
                                    font-size: 14px;
                                    color: #FFFFFF;
                                    line-height: 24px;
                                    text-align: center;
                                    margin-top: 2px;
                                }
                            }
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

                    &:hover {
                        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.5);
                        cursor: pointer;
                    }

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

                            .status {
                                height: 19px;
                                line-height: 22px;
                                padding: 0 5px;
                                border-radius: 4px;
                                font-size: 13px;
                                flex-shrink: 0;
                                margin-left: 7px;
                                color: #ffffff;
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
