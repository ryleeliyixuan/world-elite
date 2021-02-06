<template>
    <div class="app-container">
        <div class="title">发布活动管理</div>
        <div class="type-container">
            <div v-for="status in menuList" :class="['text',{'select':listQuery.status===status.id}]" @click="onActivityStatus(status)">{{status.name}}</div>
            <div class="publish-button" @click="onPublish">发布活动</div>
            <el-popover class="sort-container"
                        placement="bottom-end"
                        width="136"
                        popper-class="option"
                        trigger="hover">
                <div class="publish-order-item-container">
                    <div class="publish-order-item" v-for="order in orderList" @click="onOrderItem(order)">{{order.name}}</div>
                </div>
                <svg-icon slot="reference" icon-class="sort" class-name="sort"></svg-icon>
            </el-popover>
        </div>
        <div class="line"></div>
        <el-input v-model="listQuery.keyword" placeholder="请输入关键词" class="search-input" @keyup.enter.native="handleFilter">
            <el-button slot="prepend" icon="el-icon-search" @click="handleFilter"></el-button>
        </el-input>
        <div class="activity-item" v-for="item in dataList">
            <div class="activity-item-left">
                <div class="activity-left-one">
                    <div class="activity-left-one-title">{{item.title}}</div>
                    <div class="activity-left-one-state" :style="{'background':statusBGColorList[item.status]}">{{getStatus(item)}}</div>
                </div>
                <div class="activity-left-two">{{item.organizerInfoVo && item.organizerInfoVo.organizerName}} - <span class="activity-left-place">{{item.city.name}}</span>
                    -
                    {{item.address}}
                </div>
                <div class="activity-left-three">活动时间：<span class="activity-left-time">{{item.activityStartTime | timestampToDateHourMinute}} -- {{item.activityFinishTime | timestampToDateHourMinute}}</span>
                </div>
                <div class="activity-left-three">报名时间：<span class="activity-left-time">{{item.registrationStartTime | timestampToDateHourMinute}} -- {{item.registrationFinishTime | timestampToDateHourMinute}}</span>
                </div>
                <div class="activity-left-three">发布时间：<span class="activity-left-time">{{item.createTime | timestampToDateHourMinute}}</span></div>
            </div>
            <div class="activity-item-right">
                <div class="activity-item-right-link">
                    <div class="activity-item-right-link-one" @click="onItem(item)">查看详情</div>
                    <div v-if="item.status===7" class="activity-item-right-link-one" @click="onReason(item)">查看原因</div>
                    <div :class="['activity-item-right-link-one', {'edit-disable':!isEditInfoEnable(item)}]" @click="onEdit(item)">
                        {{isShowEditInfo(item)?'编辑信息':'重新编辑'}}
                    </div>
                    <div class="activity-item-right-link-one" v-if="isShowRegistrationInformationManagement(item)"
                         @click="onRegistrationInformationManagement(item)">报名信息管理
                    </div>
                </div>
                <div :class="['activity-item-right-button', {'disabled':!isCancelButtonEnable(item)}]" @click="onCancelActivity(item)">
                    取消活动
                </div>
            </div>
        </div>

        <pagination :total="total"
                    :limit="10"
                    :page.sync="listQuery.page"
                    @pagination="getList">
        </pagination>

        <el-dialog class="cancel-dialog"
                   title="是否恢复上次未发布的草稿？"
                   :center="true"
                   :visible.sync="publishActivityDialogVisible"
                   v-loading="draftDeleting"
                   width="445px">
            <div class="content">若选择“否”，草稿将被清空无法恢复</div>
            <div class="button-container">
                <div class="cancel" @click="onPublishWithoutDraft">否</div>
                <div class="confirm" @click="onPublishWithDraft">是</div>
            </div>
        </el-dialog>
        <el-dialog class="cancel-dialog"
                   title="确定要取消该活动吗？"
                   :center="true"
                   :visible.sync="cancelActivityDialogVisible"
                   width="445px">
            <div class="content">(活动取消后无法重新上线)</div>
            <el-input type="textarea"
                      :rows="3"
                      placeholder="向报名者发送取消原因（必填）"
                      v-model="cancelActivityReason"
                      maxlength="300"
                      resize="none"
                      class="reason">
            </el-input>
            <div class="button-container">
                <div class="cancel" @click="onCancelActivityCancel">取消</div>
                <div class="confirm" @click="onCancelActivityConfirm">确定</div>
            </div>
        </el-dialog>

        <approve :visible.sync="showApproveDialog" :status="approveStatus" @close="getApprove"></approve>
    </div>
</template>

<script>
    import Pagination from "@/components/Pagination2";
    import approve from "@/components/activity/ApproveDialog";

    export default {
        name: "ActivityPublishPage",
        components: {Pagination, approve},
        data() {
            return {
                // 活动状态 0审核中;1草稿;2下架;3即将开始(报名即将开始和活动即将开始都是3);4报名中;5进行中;6活动结束;7审核未通过
                statusList: [{id: 0, name: '审核中'}, {id: 1, name: '草稿'}, {id: 2, name: '已停止'}, {id: 3, name: '即将开始'},
                    {id: 4, name: '报名中'}, {id: 5, name: '进行中'}, {id: 6, name: '已结束'}, {id: 7, name: '审核未通过'}],
                statusBGColorList: ['#4895EF', '#C6FF00', '#B71C1C', '#FFC400', '#66BB6A', '#FF6E40', '#FF5252', '#37474F'],
                menuList: [{id: undefined, name: '全部活动'}, {id: 3, name: '即将开始'}, {id: 4, name: '报名中'}, {id: 5, name: '进行中'},
                    {id: 6, name: '已结束'}, {id: "0,7", name: '审核处理的活动'}],
                orderList: [ // 活动排序列表
                    {name: '按发布时间顺序', sort: '+CREATE_TIME'},
                    {name: '按发布时间倒序', sort: '-CREATE_TIME'},
                    {name: '按活动名称排序', sort: '+TITLE'},
                    {name: '按报名时间顺序', sort: '+REGISTRATION_START_TIME'},
                    {name: '按开始时间顺序', sort: '+ACTIVITY_START_TIME'}
                ],

                listQuery: {
                    userId: this.$store.state.user.userId,
                    keyword: undefined, // 搜索关键字
                    status: undefined, // 活动状态
                    sort: undefined, // 排序条件
                    page: 1,
                    limit: 10
                },

                dataList: [], // 活动列表
                total: 0, // 活动总数

                cancelActivity: undefined, // 要取消的活动
                cancelActivityDialogVisible: false, // 取消活动确认对话框
                cancelActivityReason: undefined, // 取消活动原因

                activityDraft: undefined, // 草稿信息
                publishActivityDialogVisible: false,// 发布活动，加载草稿确认对话框
                draftDeleting: false, // 草稿删除中

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
                Object.keys(query).forEach(key => {
                    this.listQuery[key] = query[key];
                })
            }
            this.initData();
            this.getList();
        },
        methods: {
            // 初始化数据
            initData() {
                // 加载远程草稿
                this.$axios.get("/activity/my/draft-activity-info").then(data => {
                    this.activityDraft = data.data;
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

            // 选择活动状态
            onActivityStatus(status) {
                this.listQuery.status = status.id;
                this.handleFilter();
            },

            // 排序条件改变
            onOrderItem(order) {
                this.listQuery.sort = order.sort;
                this.handleFilter();
            },

            // 条件改变
            handleFilter() {
                this.listQuery.page = 1;
                this.getList();
            },

            // 点击活动，查看活动详情
            onItem(activity) {
                this.$router.push(`/activity/${activity.id}`);
            },

            // 查看审核未通过原因
            onReason(activity) {
                this.$axios.get(`/activity/review/newest/${activity.id}`).then(data => {
                    this.$alert(data.data && data.data.reason || "原因未知", {showClose: false});
                })
            },

            // 编辑或重新编辑活动
            onEdit(activity) {
                if (this.isEditInfoEnable(activity)) {
                    this.$storage.removeObject('activityPreview');
                    this.$router.push({path: '/activity/edit', query: {id: activity.id}});
                }
            },

            // 点击报名信息活动管理
            onRegistrationInformationManagement(activity) {
                this.$router.push({path: '/activity/apply', query: {id: activity.id}});
            },

            // 取消活动
            onCancelActivity(activity) {
                if (this.isCancelButtonEnable(activity)) {
                    this.cancelActivity = activity;
                    this.cancelActivityDialogVisible = true;
                }
            },

            // 关闭取消活动对话框
            onCancelActivityCancel() {
                this.cancelActivity = undefined;
                this.cancelActivityDialogVisible = false;
            },

            // 取消活动确认按钮
            onCancelActivityConfirm() {
                if (this.cancelActivityReason) {
                    this.$axios.request({
                        method: "POST",
                        url: "/activity/takeoff",
                        params: {id: this.cancelActivity.id, reason: this.cancelActivityReason}
                    }).then(() => {
                        this.cancelActivityReason = undefined;
                        this.cancelActivityDialogVisible = false;
                        this.$message.success("已取消发布");
                        this.getList();
                    });
                } else {
                    this.$message.warning("请填写取消原因");
                }
            },

            // 点击发布活动
            onPublish() {
                if (this.approveStatus === 1) { // 审核中
                    this.$alert("您的实名认证尚在审核中，我们会尽快处理")
                } else if (this.approveStatus === 3) { // 审核被拒绝
                    this.showApproveDialog = true;
                } else if (this.approveStatus === 2) { // 审核已通过，可以发布新活动
                    if (this.activityDraft) {
                        this.publishActivityDialogVisible = true;
                    } else {
                        this.onPublishWithDraft();
                    }
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

            // 加载草稿发布新活动
            onPublishWithDraft() {
                this.publishActivityDialogVisible = false;
                // 发布新活动前先清楚预览数据
                this.$storage.removeObject('activityPreview');
                this.$router.push('/activity/edit');
            },

            // 不使用草稿发布新活动
            onPublishWithoutDraft() {
                if (this.activityDraft) {
                    this.$axios.request({
                        method: "POST",
                        url: "/activity/delete",
                        params: {id: this.activityDraft.id}
                    }).then(() => {
                        this.onPublishWithDraft();
                    });
                } else {
                    this.onPublishWithDraft();
                }
            },

            // 获取活动状态
            getStatus(activity) {
                let description = "";
                if (activity.status === 5) {
                    let number = Math.floor((activity.activityFinishTime- new Date().getTime()) / 1000 / 60 / 60 / 24);
                    description = number === 0 ? " 即将结束" : (" " + number + "天后结束");
                }
                return this.statusList.find(item => activity.status === item.id).name + description;
            },

            // 加载数据
            getList() {
                this.$storage.setData(this.$options.name, this.listQuery);
                this.loading = true;
                this.$axios.get("/activity/list", {params: this.listQuery}).then(response => {
                    this.loading = false;
                    this.dataList = response.data.list;
                    this.total = response.data.total;
                });
            },

            // 加载取消按钮背景色
            isCancelButtonEnable(item) {
                // {id: 0, name: '审核中'}, {id: 1, name: '草稿'}, {id: 2, name: '已停止'}, {id: 3, name: '即将开始'},
                // {id: 4, name: '报名中'}, {id: 5, name: '进行中'}, {id: 6, name: '已结束'}, {id: 7, name: '审核未通过'}
                switch (item.status) {
                    case 0:
                    case 2:
                    case 6:
                    case 7:
                        return false;
                    default:
                        return true;
                }
            },

            // 是否显示报名信息管理按钮
            isShowRegistrationInformationManagement(item) {
                switch (item.status) {
                    case 0:
                    case 7:
                        return false;
                    default:
                        return true;
                }
            },

            // 是否显示编辑信息 or 重新编辑
            isShowEditInfo(item) {
                switch (item.status) {
                    case 0:
                    case 7:
                        return false;
                    default:
                        return true;
                }
            },

            // 编辑信息按钮是否可用
            isEditInfoEnable(item) {
                switch (item.status) {
                    case 2:
                    case 6:
                        return false;
                    default:
                        return true;
                }
            }
        }
    };
</script>

<style>
    .option {
        padding: 0;
        background: #ECEFF1;
        box-shadow: 0 2px 4px 0 rgba(0, 0, 0, 0.26);
        border-radius: 9px;
    }

    .publish-order-item-container {
        height: 183px;
        background: #ECEFF1;
        box-shadow: 0 2px 4px 0 rgba(0, 0, 0, 0.26);
        border-radius: 9px;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: space-around;
        overflow: hidden;
    }

    .publish-order-item {
        width: 100%;
        flex: 1;
        font-size: 14px;
        font-weight: 400;
        color: #333333;
        display: flex;
        align-items: center;
        justify-content: center;
    }

    .publish-order-item:hover {
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

            .text {
                flex: 1;
                font-size: 18px;
                color: #333333;
                line-height: 47px;
                text-align: center;
                border-bottom: 3px transparent solid;
                cursor: pointer;
            }

            .select {
                color: #4895EF;
                border-bottom: 3px #4895EF solid;
            }

            .publish-button {
                width: 99px;
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

            .sort-container {
                margin-left: 18px;
                cursor: pointer;
                margin-bottom: 10px;

                .sort {
                    width: 17px;
                    height: 19px;
                }
            }
        }

        .line {
            background: #B0BEC5;
            width: 100%;
            height: 1.5px;
            box-shadow: 0 4px 16px 3px rgba(191, 199, 215, 0.31);
            margin-top: -2px;
        }

        .search-input {
            width: 452px;
            height: 38px;
            background: #ECEFF1;
            border: 1px solid #ECEFF1;
            border-radius: 19px;
            overflow: hidden;
            margin: 17px auto 31px;
            display: flex;
            align-items: center;

            ::v-deep .el-input-group__prepend, ::v-deep .el-input__inner {
                background: #ECEFF1;
                border: none;
            }
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
                        padding: 0 10px;
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
                display: flex;
                flex-direction: column;
                align-items: flex-end;

                .activity-item-right-link {
                    display: flex;
                    align-items: center;

                    .activity-item-right-link-one {
                        font-size: 14px;
                        line-height: 14px;
                        color: #4895EF;
                        border-bottom: 1px solid #4895EF;
                        margin-left: 31px;
                        cursor: pointer;
                    }

                    .edit-disable {
                        color: #999999;
                        border-bottom: 1px solid #999999;
                        cursor: not-allowed;
                    }
                }

                .activity-item-right-button {
                    width: 96px;
                    height: 28px;
                    background: #FF5252;
                    border-radius: 21px;
                    text-align: center;
                    line-height: 28px;
                    font-size: 14px;
                    color: #FFFFFF;
                    margin-top: 13px;
                    cursor: pointer;
                }

                .disabled {
                    cursor: not-allowed;
                    background: #ECEFF1;
                }
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
