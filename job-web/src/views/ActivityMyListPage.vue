<template>
    <div class="app-container">
        <div class="title">报名和关注的活动</div>
        <div class="type-container">
            <div class="type-container-type">
                <div v-for="status in menuList" :class="['text',{'select':activeMenuId===status.id}]" @click="onActivityStatus(status)">{{status.name}}</div>
            </div>
            <div class="publish-button" @click="onPublish">查看我发布的活动</div>
        </div>
        <div class="line"></div>
        <div class="activity-item" v-for="item in dataList" :key="item.id">
            <div class="activity-item-left">
                <div class="activity-left-one">
                    <div class="activity-left-one-title">{{item.title}}</div>
                    <div class="activity-left-one-state" :style="{'background':statusBGColorList[item.status]}">{{getStatus(item)}}</div>
                </div>
                <div class="activity-left-two">{{item.organizerInfoVo && item.organizerInfoVo.organizerName}} - <span class="activity-left-place">
                    {{item.city.name}}</span> - {{item.address}}
                </div>
                <div class="activity-left-three">活动时间：<span class="activity-left-time">{{item.activityStartTime | timestampToDateHourMinute}} -- {{item.activityFinishTime | timestampToDateHourMinute}}</span>
                </div>
                <div class="activity-left-three">报名时间：<span class="activity-left-time">{{item.registrationStartTime | timestampToDateHourMinute}} -- {{item.registrationFinishTime | timestampToDateHourMinute}}</span>
                </div>
                <div class="activity-left-three">发布时间：<span class="activity-left-time">{{item.createTime | timestampToDateHourMinute}}</span></div>
            </div>
            <div class="activity-item-right" v-if="activeMenuId===1" @click="onCancelCollect(item)">
                取消活动
            </div>
        </div>

        <pagination :total="total"
                    :limit="10"
                    :page.sync="listQuery.page"
                    @pagination="getList">
        </pagination>
    </div>
</template>

<script>
    import Pagination from "@/components/Pagination2";

    export default {
        name: "ActivityPublishPage",
        components: {Pagination},
        data() {
            return {
                // 活动状态 0审核中;1草稿;2下架;3即将开始(报名即将开始和活动即将开始都是3);4报名中;5进行中;6活动结束;7审核未通过
                statusList: [{id: 0, name: '审核中'}, {id: 1, name: '草稿'}, {id: 2, name: '已停止'}, {id: 3, name: '即将开始'},
                    {id: 4, name: '报名中'}, {id: 5, name: '进行中'}, {id: 6, name: '已结束'}, {id: 7, name: '审核未通过'}],
                statusBGColorList: ['#4895EF', '#C6FF00', '#B71C1C', '#FFC400', '#66BB6A', '#FF6E40', '#FF5252', '#37474F'],
                menuList: [{id: 1, name: '我的关注'}, {id: 2, name: '我的报名'}],
                activeMenuId: 1,// 激活的菜单id
                listQuery: {
                    page: 1,
                    limit: 10,
                    sort: "-id"
                },

                loading: undefined, // 加载中
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
            this.initData();
            this.getList();
        },
        methods: {
            // 初始化数据
            initData() {
            },

            // 选择活动状态
            onActivityStatus(status) {
                if (this.activeMenuId !== status.id) {
                    this.activeMenuId = status.id;
                    this.handleFilter();
                }
            },

            // 条件改变
            handleFilter() {
                this.listQuery.page = 1;
                this.getList();
            },

            // 查看我发布的活动
            onPublish() {
                this.$router.push('/activity/publish');
            },

            // 点击活动，查看活动详情
            onItem(activity) {
                this.$router.push(`/activity/${activity.id}`);
            },

            // 取消关注
            onCancelCollect(item) {
                this.$axios.post("/favorite/favorite", {objectId: item.id, type: 3, favorite: false}).then(() => {
                    this.getList();
                })
            },

            // 获取活动状态
            getStatus(activity) {
                let description = "";
                if (activity.status === 5) {
                    let number = new Date(activity.activityFinishTime).getDate() - new Date().getDate()
                    description = number === 0 ? " 即将结束" : " " + number + "天后结束";
                }
                return this.statusList.find(item => activity.status === item.id).name + description;
            },

            // 加载数据
            getList() {
                let url = this.activeMenuId === 1 ? "/favorite/my-favorite-activities" : "/registration/my-registration-activities";
                this.loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                this.$axios.get(url, {params: this.listQuery}).then(response => {
                    this.loading.close();
                    this.dataList = response.data.list;
                    this.total = response.data.total;
                }).catch((error) => {
                    this.dataList = [];
                    this.loading.close();
                    this.$message.error(error)
                });
            },
        }
    };
</script>

<style lang="scss" scoped>
    .app-container {
        width: 1200px !important;
        padding: 24px 30px 50px;
        background: #FFFFFF;
        box-shadow: 0 4px 16px 3px rgba(191, 199, 215, 0.31);

        .title {
            font-size: 30px;
            color: #333333;
            line-height: 42px;
        }

        .type-container {
            margin-top: 20px;
            display: flex;
            align-items: center;
            justify-content: space-between;

            .type-container-type {
                display: flex;
                align-items: center;

                .text {
                    flex: 1;
                    font-size: 18px;
                    color: #333333;
                    line-height: 47px;
                    text-align: center;
                    border-bottom: 3px transparent solid;
                    cursor: pointer;
                    width: 124px;
                }

                .select {
                    color: #4895EF;
                    border-bottom: 3px #4895EF solid;
                }
            }

            .publish-button {
                width: 151px;
                height: 31px;
                background: #4895EF;
                border-radius: 21px;
                font-size: 14px;
                color: #FFFFFF;
                line-height: 31px;
                text-align: center;
                cursor: pointer;
                margin-bottom: 6px;
                margin-left: 30px;
            }
        }

        .line {
            background: #B0BEC5;
            width: 100%;
            height: 1.5px;
            box-shadow: 0 4px 16px 3px rgba(191, 199, 215, 0.31);
            margin-top: -2px;
            margin-bottom: 21px;
        }

        .activity-item {
            width: 1095px;
            margin: 0 auto 21px;
            padding: 22px 28px 23px 18px;
            border-radius: 8px;
            border: 1px solid #4895EF;
            display: flex;
            align-items: center;
            justify-content: space-between;

            .activity-item-left {

                .activity-left-one {
                    display: flex;
                    align-items: center;

                    .activity-left-one-title {
                        font-size: 18px;
                        color: #333333;
                        line-height: 25px;
                    }

                    .activity-left-one-state {
                        width: 81px;
                        height: 22px;
                        background: #FFC400;
                        border-radius: 3px;
                        font-size: 14px;
                        color: #FFFFFF;
                        line-height: 22px;
                        text-align: center;
                        margin-left: 7px;
                    }
                }

                .activity-left-two {
                    font-size: 14px;
                    color: #333333;
                    margin-top: 11px;

                    .activity-left-place {
                        color: #4895EF;
                    }
                }

                .activity-left-three {
                    font-size: 14px;
                    color: #999999;
                    margin-top: 6px;

                    .activity-left-time {
                        font-size: 14px;
                        color: #333333;
                    }
                }
            }

            .activity-item-right {
                width: 96px;
                height: 28px;
                background: #FF5252;
                border-radius: 21px;
                font-size: 14px;
                color: #FFFFFF;
                line-height: 28px;
                text-align: center;
                cursor: pointer;
            }
        }

        .reason {
            margin-top: 14px;
        }

        .cancel-dialog {
            ::v-deep .el-dialog {
                border-radius: 13px;
            }

            ::v-deep .el-dialog__body {
                padding-top: 4px;
            }

            .content {
                font-size: 16px;
                font-weight: 400;
                color: #666666;
                line-height: 22px;
                text-align: center;
            }

            .button-container {
                display: flex;
                align-items: center;
                justify-content: center;
                margin-top: 14px;

                .cancel {
                    width: 107px;
                    height: 35px;
                    background: #FFFFFF;
                    border-radius: 18px;
                    border: 1px solid #4895EF;
                    font-size: 16px;
                    color: #4895EF;
                    line-height: 35px;
                    text-align: center;
                    cursor: pointer;
                }

                .confirm {
                    margin-left: 21px;
                    width: 107px;
                    height: 35px;
                    background: #4895EF;
                    border-radius: 18px;
                    border: 1px solid #FFFFFF;
                    font-size: 16px;
                    color: #FFFFFF;
                    line-height: 35px;
                    text-align: center;
                    cursor: pointer;
                }
            }
        }
    }

</style>
