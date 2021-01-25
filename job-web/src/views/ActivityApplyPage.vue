<template>
    <div class="app-container">
        <div v-if="activity">
            <div class="line1">
                <div class="text" @click="$router.go(-1)" style="cursor: pointer;">活动管理</div>
                <div class="text">></div>
                <div class="text">报名信息管理</div>
            </div>
            <div class="title">报名信息管理</div>
            <div class="apply-info-container">
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

            <el-input v-model="listQuery.keywords" placeholder="请输入关键词" class="search-input" @keyup.enter.native="handleFilter">
                <el-button slot="prepend" icon="el-icon-search" @click="handleFilter"></el-button>
            </el-input>

            <div class="type-container">
                <div class="tab-container">
                    <div v-for="(status,index) in applyStatusList"
                         v-if="activity.auditType==='0' || index===0"
                         :class="['tab-item',{'select':listQuery.status===status.id}]"
                         @click="onActivityStatus(status)">
                        {{status.name}}<span class="tab-item-mount">{{"("+status.total+")"}}</span>
                    </div>
                </div>
                <div class="tab-right-container">
                    <div class="import-button" @click="onExportData">导出名单</div>
                    <el-popover class="sort-container"
                                placement="bottom-end"
                                width="136"
                                popper-class="option"
                                trigger="hover">
                        <div class="apply-order-item-container">
                            <div class="apply-order-item" v-for="order in orderList" @click="onOrderItem(order)">{{order.name}}</div>
                        </div>
                        <svg-icon slot="reference" icon-class="sort" class-name="sort"></svg-icon>
                    </el-popover>
                </div>
            </div>
            <div class="line"></div>

            <div v-for="applyStatus in applyStatusList" v-if="applyStatus.id === listQuery.status" class="apply-container">
                <div class="apply-item" v-for="(item, index) in applyStatus.dataList" :key="item.id" @click="onViewApply(item,index, applyStatus.total)">
                    <div class="info-container">
                        <div class="name">{{item.name}}</div>
                        <div class="apply-info-base" v-if="applyTable">
                            <div class="apply-info-base-item" v-if="applyTable.phoneFlag!=='2'">
                                <span class="apply-info-base-name">手机：</span>
                                <span class="apply-info-base-value">{{item.phone}}</span>
                            </div>
                            <div class="apply-info-base-item" v-if="applyTable.genderFlag!=='2'">
                                <span class="apply-info-base-name">性别：</span>
                                <span class="apply-info-base-value">{{item.gender==='1'?'男':'女'}}</span>
                            </div>
                            <div class="apply-info-base-item" v-if="applyTable.emailFlag!=='2'">
                                <span class="apply-info-base-name">邮箱：</span>
                                <span class="apply-info-base-value">{{item.email}}</span>
                            </div>
                            <div class="apply-info-base-item" v-if="applyTable.schoolFlag!=='2'">
                                <span class="apply-info-base-name">学校：</span>
                                <span class="apply-info-base-value">{{item.school}}</span>
                            </div>
                            <div class="apply-info-base-item" v-if="applyTable.gradeFlag!=='2'">
                                <span class="apply-info-base-name">年级：</span>
                                <span class="apply-info-base-value">{{item.grade}}</span>
                            </div>
                            <div class="apply-info-base-item" v-if="applyTable.professionFlag!=='2'">
                                <span class="apply-info-base-name">专业：</span>
                                <span class="apply-info-base-value">{{item.profession}}</span>
                            </div>
                            <div class="apply-info-base-item" v-if="applyTable.educationFlag!=='2'">
                                <span class="apply-info-base-name">学历：</span>
                                <span class="apply-info-base-value">{{item.education && item.education.name}}</span>
                            </div>
                        </div>
                    </div>
                    <div class="apply-item-right">
                        <div class="button-container">
                            <div class="button-text">查看报名表</div>
                            <div class="button-text" @click.stop="onViewResume(item)" v-if="activity.needResume==='1'">查看简历</div>
                        </div>
                        <div class="button-container" style="margin-top: 13px;" v-if="activity.auditType==='0'">
                            <div class="button button1-1" v-if="item.status === 1" @click.stop="onResolve1(item)">通过报名</div>
                            <div class="button button1-2" v-if="item.status === 1" @click.stop="onReject1(item)">不合适</div>
                            <div class="button button2-1" v-if="item.status === 2" @click.stop="">已通过</div>
                            <div class="button button2-2" v-if="item.status === 2" @click.stop="onReject2(item)">不合适</div>
                            <div class="button button3-1" v-if="item.status === 3" @click.stop="onResolve3(item)">重新通过</div>
                            <div class="button button3-2" v-if="item.status === 3" @click.stop="">不合适</div>
                        </div>
                    </div>
                </div>
                <pagination :total="applyStatus.total"
                            :limit="10"
                            :page.sync="listQuery.page"
                            @pagination="getList">
                </pagination>
            </div>
            <export-apply v-if="exportDialogVisible" :visible.sync="exportDialogVisible" :activity="activity" :applyTable="applyTable"></export-apply>
            <view-apply v-if="viewDialogVisible" :visible.sync="viewDialogVisible" :activity="activity" :data="selectItem"></view-apply>

            <el-dialog :visible.sync="resumeDialogVisible" width="750px" :show-close="false" class="load-resume-dialog">
                <div slot="title" class="dialog-title">
                    <svg-icon icon-class="download-icon" class="download-icon" clickable @click="onLoadResume"/>
                    <svg-icon icon-class="close-icon" class="close-icon" @click="resumeDialogVisible = false;"/>
                </div>
                <ResumeView :resumeId="resumeId"></ResumeView>

                <el-dialog class="resume-cancel-dialog"
                           title="是否同时下载报名表？"
                           :center="true"
                           :visible.sync="loadResumeDialogVisible"
                           append-to-body
                           width="445px">
                    <el-checkbox v-model="andLoadingApplyTableNoTips">下载该活动简历时不再提示</el-checkbox>
                    <div class="resume-button-container">
                        <div class="cancel" @click="onLoadResumeOnly">否</div>
                        <div class="confirm" @click="onLoadResumeAndResume">是</div>
                    </div>
                </el-dialog>
            </el-dialog>
        </div>
    </div>
</template>

<script>
    import Pagination from "@/components/Pagination2";
    import ExportApply from "@/components/activity/ExportApply";
    import ViewApply from "@/components/activity/ViewApply";
    import ResumeView from "@/components/ResumeView";
    import {downloadFile} from "@/utils/common";

    export default {
        name: "ActivityApplyPage",
        components: {Pagination, ExportApply, ViewApply, ResumeView},
        data() {
            return {
                activity: undefined, // 当前管理的活动
                applyTable: undefined, // 报名表详情
                statusBGColorList: ['#4895EF', '#C6FF00', '#B71C1C', '#FFC400', '#66BB6A', '#FF6E40', '#FF5252', '#37474F'],
                // 活动状态 0审核中;1草稿;2下架;3即将开始(报名即将开始和活动即将开始都是3);4报名中;5进行中;6活动结束;7审核未通过
                statusList: [{id: 0, name: '审核中'}, {id: 1, name: '草稿'}, {id: 2, name: '已停止'}, {id: 3, name: '即将开始'},
                    {id: 4, name: '报名中'}, {id: 5, name: '进行中'}, {id: 6, name: '已结束'}, {id: 7, name: '审核未通过'}],

                applyStatusList: [ // 报名列表
                    {id: undefined, name: '全部', dataList: [], total: 0},
                    {id: 2, name: '已通过', dataList: [], total: 0},
                    {id: 3, name: '不合适', dataList: [], total: 0},
                    {id: 1, name: '待处理', dataList: [], total: 0}
                ],

                orderList: [ // 报名人员排序选项
                    {name: '按姓名排序', sort: '+NAME'},
                    {name: '按时间顺序', sort: '+CREATE_TIME'},
                    {name: '按时间倒序', sort: '-CREATE_TIME'},
                ],

                listQuery: {
                    activityId: undefined, // 活动id
                    keywords: undefined, // 搜索关键字
                    status: undefined, // 报名状态，默认全部
                    sort: undefined, // 排序条件
                    page: 1,
                    limit: 10
                },

                exportDialogVisible: false, // 导出对话框
                viewDialogVisible: false, // 查看报名表对话框

                selectItem: undefined, // 选中的数据

                resumeId: undefined, // 要查看的简历id
                applyTableId: undefined, // 要查看的报名表id

                resumeDialogVisible: false, // 简历对话框
                andLoadingApplyTableNoTips: false, // 下载简历时，是否不要提示用户下载报名表
                onlyResume: false, // 不显示下载简历提示对话框时，是否仅下载简历
                loadResumeDialogVisible: false, // 是否显示下载简历对话框
            }
        },
        created() {
            this.initData();
        },
        methods: {
            // 初始化数据
            initData() {
                this.listQuery.activityId = this.$route.query.id;

                // 活动信息
                this.$axios.get("/activity/activity-info", {params: {id: this.$route.query.id}}).then(response => {
                    this.activity = response.data;

                    // 获取报名表信息
                    return this.$axios.get(`/activity-questionnaire/${this.activity.questionnaireId}`)
                }).then(response => {
                    this.applyTable = response.data;
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

                // 下载简历用到的信息
                let object = this.$storage.getData('andLoadingApplyTableNoTips:' + this.listQuery.activityId);
                this.andLoadingApplyTableNoTips = object.tips || false;
                this.onlyResume = object.onlyResume || false;
            },

            // 点击导出名单按钮
            onExportData() {
                this.exportDialogVisible = true;
            },

            // 点击查看报名表
            onViewApply(item, index, total) {
                let data = {};
                data.number = (this.listQuery.page - 1) * this.listQuery.limit + (index + 1);
                data.query = this.listQuery;
                this.selectItem = data;
                this.viewDialogVisible = true;
            },

            // 查看简历
            onViewResume(item) {
                this.resumeId = item.resumeId;
                this.applyTableId = item.id;
                this.resumeDialogVisible = true;
            },

            // 点击下载简历
            onLoadResume() {
                if (this.andLoadingApplyTableNoTips) {
                    if (this.onlyResume) {
                        this.loadResume();
                    } else {
                        this.loadResume();
                        this.loadApplyTable();
                    }
                } else {
                    this.loadResumeDialogVisible = true;
                }
            },

            // 下载简历对话框中点击 否
            onLoadResumeOnly() {
                this.$storage.setData('andLoadingApplyTableNoTips:' + this.activity.id, {tips: this.andLoadingApplyTableNoTips, onlyResume: true});
                this.onlyResume = true;
                this.loadResumeDialogVisible = false;
                this.loadResume();
            },

            // 下载简历对话框中点击 是
            onLoadResumeAndResume() {
                this.$storage.setData('andLoadingApplyTableNoTips:' + this.activity.id, {tips: this.andLoadingApplyTableNoTips, onlyResume: false});
                this.onlyResume = false;
                this.loadResumeDialogVisible = false;
                this.loadApplyTable();
                this.loadResume();
            },

            // 下载报名表
            loadApplyTable() {
                this.$axios.get("/export/registration-to-pdf", {params: {registrationId: this.applyTableId}}).then(response => {
                    if (response.data) {
                        downloadFile({
                            fileKey: response.data, fileName: '报名表.pdf', success: () => {
                            }
                        });
                    }
                })
            },

            // 下载简历
            loadResume() {
                this.$axios.get("/export/resume-to-pdf", {params: {resumeId: this.resumeId}}).then(response => {
                    downloadFile({
                        fileKey: response.data, fileName: '简历.pdf', success: () => {
                        }
                    });
                })
            },


            // 点击通过报名（待处理中）
            onResolve1(item) {
                this.$axios.patch(`/registration/pass/${item.id}`).then(() => {
                    this.getList();
                    this.applyStatusList[1].total++;
                    this.applyStatusList[3].total--;
                })
            },

            // 点击不合适（待处理中）
            onReject1(item) {
                this.$axios.patch(`/registration/inappropriate/${item.id}`).then(() => {
                    this.getList();
                    this.applyStatusList[2].total++;
                    this.applyStatusList[3].total--;
                })
            },

            // 点击通过报名（不合适中）
            onResolve3(item) {
                this.$axios.patch(`/registration/pass/${item.id}`).then(() => {
                    this.getList();
                    this.applyStatusList[1].total++;
                    this.applyStatusList[2].total--;
                })
            },

            // 点击不合适（已通过中）
            onReject2(item) {
                this.$axios.patch(`/registration/inappropriate/${item.id}`).then(() => {
                    this.getList();
                    this.applyStatusList[1].total--;
                    this.applyStatusList[2].total++;
                })
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

    .apply-order-item-container {
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

    .apply-order-item {
        width: 100%;
        flex: 1;
        font-size: 14px;
        font-weight: 400;
        color: #333333;
        display: flex;
        align-items: center;
        justify-content: center;
    }

    .apply-order-item:hover {
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

        .type-container {
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
                    cursor: pointer;
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

        .apply-container {
            padding: 14px 16px 0;

            .apply-item {
                margin-bottom: 21px;
                padding: 21px 7px 20px 15px;
                border-radius: 8px;
                border: 1px solid #4895EF;
                display: flex;
                align-items: center;
                justify-content: space-between;
                cursor: pointer;

                .info-container {
                    flex: 1;

                    .name {
                        font-size: 18px;
                        color: #333333;
                    }

                    .apply-info-base {
                        font-size: 14px;
                        color: #333333;
                        margin-top: 9px;
                        display: flex;
                        flex-wrap: wrap;
                        height: 52px;
                        overflow: hidden;

                        .apply-info-base-item {
                            width: 50%;
                            height: 26px;

                            .apply-info-base-name {
                                color: #999999;
                            }

                            .apply-info-base-value {
                                display: inline-block;
                                width: 120px;
                            }
                        }
                    }
                }

                .apply-item-right {
                    display: flex;
                    flex-direction: column;
                    justify-content: center;
                    width: 220px;
                    flex-shrink: 0;

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

                        .button {
                            width: 96px;
                            height: 28px;
                            border-radius: 4px;
                            font-size: 14px;
                            line-height: 28px;
                            text-align: center;
                            cursor: pointer;
                        }

                        .button1-1 {
                            background: #4895EF;
                            color: #FFFFFF;
                        }

                        .button1-2 {
                            background: #E3F0FF;
                            border: 1px solid #4895EF;
                            color: #4895EF;
                        }

                        .button2-1 {
                            background: #81C784;
                            color: #FFFFFF;
                        }

                        .button2-2 {
                            background: #ECEFF1;
                            color: #78909C;
                        }

                        .button3-1 {
                            background: #ECEFF1;
                            color: #78909C;
                        }

                        .button3-2 {
                            background: #EC5454;
                            color: #FFFFFF;
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
        }


        .load-resume-dialog {
            .dialog-title {
                display: flex;
                align-items: center;
                justify-content: flex-end;

                .download-icon {
                    width: 29px;
                    height: 23px;
                    margin-left: 14px;
                    cursor: pointer;
                }

                .close-icon {
                    width: 29px;
                    height: 29px;
                    margin-left: 14px;
                    cursor: pointer;
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

    .resume-cancel-dialog {
        ::v-deep .el-dialog {
            border-radius: 13px;
        }

        /deep/ .el-dialog__body {
            display: flex;
            flex-direction: column;
            align-items: center;
        }

        .resume-button-container {
            display: flex;
            align-items: center;
            justify-content: center;
            margin-top: 26px;

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

</style>
