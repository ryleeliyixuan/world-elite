<template>
    <div class="app-container">
        <div class="line1">
            <div class="text" @click="$router.go(-1)" style="cursor: pointer;">活动管理</div>
            <div class="text">></div>
            <div class="text">报名信息管理</div>
        </div>
        <div class="title">报名信息管理</div>
        <div class="apply-info-container" v-if="activity">
            <div class="apply-info-left">
                <div class="apply-info-one">
                    {{activity.title}}
                    <div class="apply-info-one-state" :style="{'background':statusBGColorList[activity.status]}">{{getStatus()}}</div>
                </div>
                <div class="apply-info-two" style="margin-top:12px">
                    <div class="name">活动时间：</div>
                    <div class="value">
                        {{activity.activityStartTime | timestampToDateHourMinute}}-{{activity.activityFinishTime | timestampToDateHourMinute}}
                    </div>
                    <span class="name">活动地址：</span>
                    <div class="value">{{activity.city && activity.city.name}}-{{activity.address}}</div>
                </div>
                <div class="apply-info-two">
                    <div class="name">报名时间：</div>
                    <div class="value">
                        {{activity.registrationStartTime | timestampToDateHourMinute}}-{{activity.registrationFinishTime | timestampToDateHourMinute}}
                    </div>
                    <div class="name">主办方：</div>
                    <div class="value">{{activity.organizerInfoVo.organizerName}}</div>
                </div>
            </div>
            <el-image :src="require('@/assets/activity/apply-icon1.png')" class="apply-info-right"></el-image>
        </div>
        <div class="type-container" v-if="activity">
            <div class="tab-container">
                <div v-for="(status,index) in applyStatusList"
                     v-if="activity.auditType==='0' || index===0"
                     :class="['tab-item',{'select':listQuery.status===status.id}]"
                     @click="onActivityStatus(status)">
                    {{status.name}}<span class="tab-item-mount">{{"("+status.total+")"}}</span>
                </div>
            </div>
            <div class="tab-right-container">
                <div class="import-button" @click="onImportData">导出名单</div>
                <el-popover class="sort-container"
                            placement="bottom-end"
                            width="136"
                            popper-class="option"
                            trigger="hover">
                    <div class="order-item-container">
                        <div class="order-item" v-for="order in orderList" @click="onOrderItem(order)">{{order.name}}</div>
                    </div>
                    <svg-icon slot="reference" icon-class="sort" class-name="sort"></svg-icon>
                </el-popover>
            </div>
        </div>
        <div class="line"></div>
        <el-input v-model="listQuery.keywords" placeholder="请输入关键词" class="search-input" @keyup.enter.native="handleFilter">
            <el-button slot="prepend" icon="el-icon-search" @click="handleFilter"></el-button>
        </el-input>

        <div v-for="applyStatus in applyStatusList" v-if="applyStatus.id === listQuery.status">
            <div class="apply-item" v-for="item in applyStatus.dataList" :key="item.id">
                <div class="info-container">
                    <div class="name">{{item.name}}</div>
                    <div class="apply-info-two">
                        <span class="apply-info-two-title">手机：</span>
                        <span class="apply-info-two-content">{{item.phone}}</span>
                        <span class="apply-info-two-title" style="margin-left: 147px">性别：</span>
                        <span class="apply-info-two-content">{{item.gender==='1'?'男':'女'}}</span>
                    </div>
                    <div class="apply-info-two">
                        <span class="apply-info-two-title">邮箱：</span>
                        <span class="apply-info-two-content">{{item.email}}</span>
                        <span class="apply-info-two-title" style="margin-left: 147px">学校：</span>
                        <span class="apply-info-two-content">{{item.school}}</span>
                    </div>
                </div>
                <div class="apply-item-right">
                    <div class="button-container">
                        <div class="button-text">查看报名表</div>
                        <div class="button-text">查看简历</div>
                    </div>
                    <div class="button-container" style="margin-top: 13px;" v-if="1">
                        <div class="button-primary" v-if="isShowPassApply(item)">通过报名</div>
                        <div class="button-primary green" v-if="isShowPassed(item)">已通过</div>
                        <div class="button-plain" v-if="isShowNoPass(item)">不合适</div>
                        <div class="button-disabled" v-if="isShowRePass(item)">重新通过</div>
                        <div class="button-primary red" v-if="isShowRefuse(item)">不合适</div>
                    </div>
                </div>
            </div>
            <pagination :total="applyStatus.total"
                        :limit="10"
                        :page.sync="listQuery.page"
                        @pagination="getList">
            </pagination>
        </div>
        <export-apply v-if="activity" :visible.sync="exportDialogVisible" :activityId="activity.id"></export-apply>
    </div>

</template>

<script>
    import Pagination from "@/components/Pagination2";
    import ExportApply from "@/components/activity/ExportApply";


    export default {
        name: "ActivityApplyPage",
        components: {Pagination, ExportApply},
        data() {
            return {
                activity: undefined, // 当前管理的活动
                statusBGColorList: ['#4895EF', '#C6FF00', '#B71C1C', '#FFC400', '#66BB6A', '#FF6E40', '#FF5252', '#37474F'],
                // 活动状态 0审核中;1草稿;2下架;3即将开始(报名即将开始和活动即将开始都是3);4报名中;5进行中;6活动结束;7审核未通过
                statusList: [{id: 0, name: '审核中'}, {id: 1, name: '草稿'}, {id: 2, name: '已停止'}, {id: 3, name: '即将开始'},
                    {id: 4, name: '报名中'}, {id: 5, name: '进行中'}, {id: 6, name: '已结束'}, {id: 7, name: '审核未通过'}],

                applyStatusList: [ // 报名列表
                    {id: undefined, name: '全部', dataList: [], total: 0},
                    {id: 1, name: '已通过', dataList: [], total: 0},
                    {id: 2, name: '不合适', dataList: [], total: 0},
                    {id: 3, name: '待处理', dataList: [], total: 0}
                ],

                orderList: [ // 报名人员排序选项
                    {name: '按姓名排序', sort: '+NAME'},
                    {name: '按时间顺序', sort: '+CREATE_TIME'},
                    {name: '按时间倒序', sort: '-CREATE_TIME'},
                ],

                listQuery: {
                    keywords: undefined, // 搜索关键字
                    status: undefined, // 报名状态，默认全部
                    sort: undefined, // 排序条件
                    page: 1,
                    limit: 10
                },

                exportDialogVisible: false, // 导出对话框
            }
        },
        created() {
            this.initData();
        },
        methods: {
            // 初始化数据
            initData() {
                // 活动信息
                this.$axios.get("/activity/activity-info", {params: {id: this.$route.query.id}}).then(response => {
                    this.activity = response.data;
                })

                // 加载报名信息列表
                let query = {...this.listQuery};
                this.applyStatusList.forEach(item => {
                    query.status = item.id;
                    this.$axios.post("/registration/list", query).then(response => {
                        item.dataList = response.data.list;
                        item.total = response.data.total;
                    });
                })
            },

            // 点击导出名单按钮
            onImportData() {
                this.exportDialogVisible = true;
            },

            // 是否显示通过报名按钮
            isShowPassApply() {
                // TODO
            },

            // 是否显示不合适按钮
            isShowNoPass() {
                // TODO
            },

            // 是否显示重新通过按钮
            isShowRePass() {
                // TODO
            },

            // 是否显示已通过状态
            isShowPassed() {
                // TODO
            },

            // 是否显示不合适状态
            isShowRefuse() {
                // TODO
            },

            // 获取活动状态
            getStatus() {
                let description = "";
                if (this.activity.status === 5) {
                    let number = new Date(this.activity.activityFinishTime).getDate() - new Date().getDate()
                    description = number === 0 ? " 即将结束" : " " + number + "天后结束";
                }
                return this.statusList.find(item => this.activity.status === item.id).name + description;
            },

            // 排序条件改变
            onOrderItem(order) {
                this.listQuery.sort = order.sort;
                this.handleFilter();
            },

            // 选择活动状态
            onActivityStatus(status) {
                this.listQuery.status = status.id;
                this.handleFilter();
            },

            // 条件改变
            handleFilter() {
                this.listQuery.page = 1;
                this.getList();
            },

            // 获取报名人员列表
            getList() {
                this.$storage.setData(this.$options.name, this.listQuery);
                this.loading = true;
                this.$axios.post("/registration/list", this.listQuery).then(response => {
                    this.loading = false;
                    let applyStatus = this.applyStatusList.find(item => item.id === this.listQuery.status);
                    if (applyStatus) {
                        applyStatus.dataList = response.data.list;
                        applyStatus.total = response.data.total;
                    }
                });
            }
        }
    }
</script>

<style>
    .option {
        padding: 0;
        background: #ECEFF1;
        box-shadow: 0 2px 4px 0 rgba(0, 0, 0, 0.26);
        border-radius: 9px;
    }

    .order-item-container {
        height: 109px;
        background: #ECEFF1;
        box-shadow: 0 2px 4px 0 rgba(0, 0, 0, 0.26);
        border-radius: 9px;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: space-around;
        overflow: hidden;
    }

    .order-item {
        width: 100%;
        flex: 1;
        font-size: 14px;
        font-weight: 400;
        color: #333333;
        display: flex;
        align-items: center;
        justify-content: center;
    }

    .order-item:hover {
        color: #656565;
        background: #d8dadb;
        cursor: pointer;
    }

    .popper__arrow::after {
        border-bottom-color: #ECEFF1 !important;
    }
</style>

<style lang="scss" scoped>
    .app-container {
        width: 1200px !important;
        padding: 24px 30px 50px;
        background: #FFFFFF;
        box-shadow: 0 4px 16px 3px rgba(191, 199, 215, 0.31);

        .line1 {
            display: flex;
            align-items: center;
            margin-bottom: 21px;

            .text {
                font-size: 16px;
                font-weight: 400;
                color: #999999;
                line-height: 22px;
                padding-right: 8px;
            }
        }

        .apply-info-container {
            width: 100%;
            height: 128px;
            background: #E9F5FF;
            border-radius: 8px;
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding-right: 24px;
            margin-top: 13px;

            .apply-info-left {
                height: 100%;
                padding: 15px 0 19px 12px;

                .apply-info-one {
                    display: flex;
                    align-items: center;
                    font-size: 24px;
                    color: #333333;

                    .apply-info-one-state {
                        width: 64px;
                        height: 22px;
                        border-radius: 3px;
                        text-align: center;
                        line-height: 22px;
                        font-size: 14px;
                        font-weight: 500;
                        color: #FFFFFF;
                        margin-left: 7px;
                    }
                }

                .apply-info-two {
                    display: flex;
                    align-items: center;
                    margin-top: 9px;

                    .name {
                        width: 70px;
                        text-align: right;
                        font-size: 14px;
                        font-weight: 400;
                        color: #333333;
                        line-height: 20px;
                    }

                    .value {
                        width: 372px;
                        font-size: 14px;
                        font-weight: 400;
                        color: #333333;
                        line-height: 20px;
                    }
                }

            }

            .apply-info-right {
                width: 97px;
                height: 118px;
            }
        }

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
            width: 100%;

            .tab-container {
                display: flex;
                align-items: center;

                .tab-item {
                    font-size: 18px;
                    color: #333333;
                    padding: 0 20px 12px 20px;
                    margin-right: 100px;
                    border-bottom: 3px transparent solid;
                    cursor: pointer;

                    .tab-item-mount {
                        font-size: 14px;
                        color: #999999;
                        margin-left: 3px;
                    }
                }

                .select {
                    color: #4895EF;
                    border-bottom: 3px #4895EF solid;
                }
            }

            .tab-right-container {
                display: flex;
                align-items: center;
                padding-bottom: 20px;

                .import-button {
                    width: 99px;
                    height: 25px;
                    background: #4895EF;
                    border-radius: 21px;
                    text-align: center;
                    line-height: 25px;
                    font-size: 14px;
                    color: #FFFFFF;
                }

                .sort-container {
                    margin-left: 18px;
                    cursor: pointer;

                    .sort {
                        width: 17px;
                        height: 19px;
                    }
                }
            }
        }

        .line {
            background: #B0BEC5;
            width: 100%;
            height: 1px;
            box-shadow: 0 4px 16px 3px rgba(191, 199, 215, 0.31);
            margin-top: -3px;
        }

        .search-input {
            width: 452px;
            height: 38px;
            background: #ECEFF1;
            border: 1px solid #ECEFF1;
            border-radius: 19px;
            overflow: hidden;
            margin: 17px auto 36px;
            display: flex;
            align-items: center;

            ::v-deep .el-input-group__prepend, ::v-deep .el-input__inner {
                background: #ECEFF1;
                border: none;
            }
        }

        .apply-item {
            width: 1095px;
            margin: 0 auto 21px;
            padding: 21px 7px 20px 15px;
            border-radius: 8px;
            border: 1px solid #4895EF;
            display: flex;
            align-items: center;
            justify-content: space-between;

            .info-container {
                height: 100%;

                .name {
                    font-size: 18px;
                    color: #333333;
                }

                .apply-info-two {
                    font-size: 14px;
                    color: #333333;
                    margin-top: 9px;

                    .apply-info-two-title {
                        color: #999999;
                    }

                    .apply-info-two-content {
                        display: inline-block;
                        width: 120px;
                    }
                }
            }

            .apply-item-right {
                display: flex;
                flex-direction: column;
                justify-content: center;
                width: 220px;

                .button-container {
                    display: flex;
                    align-items: center;
                    justify-content: space-around;

                    .button-text {
                        width: 96px;
                        font-size: 14px;
                        font-weight: 500;
                        color: #4895EF;
                        line-height: 20px;
                        text-decoration: underline;
                        text-align: center;
                        cursor: pointer;
                    }

                    .button-plain {
                        width: 96px;
                        height: 28px;
                        background: #E3F0FF;
                        border-radius: 21px;
                        border: 1px solid #4895EF;
                        font-size: 14px;
                        color: #4895EF;
                        line-height: 28px;
                        text-align: center;
                        cursor: pointer;
                    }

                    .button-primary {
                        width: 96px;
                        height: 28px;
                        background: #4895EF;
                        border-radius: 21px;
                        font-size: 14px;
                        color: #FFFFFF;
                        line-height: 28px;
                        text-align: center;
                        cursor: pointer;
                    }

                    .button-disabled {
                        width: 96px;
                        height: 28px;
                        background: #ECEFF1;
                        border-radius: 21px;
                        font-size: 14px;
                        font-weight: 500;
                        color: #78909C;
                        line-height: 28px;
                        text-align: center;
                        cursor: pointer;
                    }

                    .green {
                        background: #81C784;
                    }

                    .red {
                        background: #EC5454;
                    }
                }
            }
        }

        .cancel-dialog {
            ::v-deep .el-dialog {
                border-radius: 13px;
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
                margin-top: 34px;

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
