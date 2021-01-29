<template>
    <div class="app-container">
        <div class="filter-container">
            <el-form :inline="true" :model="listQuery">
                <el-form-item>
                    <el-input v-model="listQuery.id" placeholder="活动ID" size="medium"
                              @keyup.enter.native="handleFilter" style="width: 150px"
                              clearable></el-input>
                </el-form-item>
                <el-form-item>
                    <el-input v-model="listQuery.title" placeholder="活动名称" size="medium"
                              @keyup.enter.native="handleFilter"
                              clearable></el-input>
                </el-form-item>
                <el-form-item>
                    <el-input v-model="listQuery.organizerName" placeholder="主办方" size="medium"
                              @keyup.enter.native="handleFilter"
                              clearable></el-input>
                </el-form-item>
                <el-form-item>
                    <el-cascader placeholder="请选择工作城市"
                                 :show-all-levels="true"
                                 :options="cityOptions"
                                 :props="cityIdProps"
                                 filterable
                                 clearable
                                 size="medium"
                                 v-model="listQuery.cityIds"
                                 @change="handleFilter">
                    </el-cascader>
                </el-form-item>

                <el-form-item>
                    <el-select
                            v-model="activityStatus"
                            filterable
                            clearable
                            size="medium"
                            placeholder="活动状态"
                            style="width: 130px"
                            :disabled="this.reviewStatus != -1"
                            @change="handleFilter"
                    >
                        <el-option
                                v-for="item in status"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id"
                        ></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-select
                            v-model="listQuery.organizerType"
                            filterable
                            clearable
                            size="medium"
                            placeholder="主办方类型"
                            style="width: 130px"
                            @change="handleFilter"
                    >
                        <el-option
                                v-for="item in organizerType"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id"
                        ></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-select
                            v-model="listQuery.reviewType"
                            filterable
                            clearable
                            size="medium"
                            placeholder="审核次数"
                            style="width: 130px"
                            @change="handleFilter"
                    >
                        <el-option
                                v-for="item in reviewType"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id"
                        ></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-date-picker
                            v-model="activityDateRange"
                            type="daterange"
                            size="medium"
                            @change="handleFilter"
                            start-placeholder="开始日期"
                            end-placeholder="结束日期">
                    </el-date-picker>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="handleFilter">查询</el-button>
                </el-form-item>
            </el-form>
        </div>

        <el-menu :default-active="reviewStatus" mode="horizontal" @select="handleSelectMenu">
            <el-menu-item index="0">待审核</el-menu-item>
            <el-menu-item index="-1">已通过</el-menu-item>
            <el-menu-item index="7">不通过</el-menu-item>
        </el-menu>

        <pagination
                :total="total"
                :page.sync="listQuery.page"
                :limit.sync="listQuery.limit"
                @pagination="handleRouteList"
        />

        <el-table
                :key="tableKey"
                v-loading="listLoading"
                :data="list"
                border
                highlight-current-row
                style="margin-top: 10px"
        >
            <el-table-column label="活动ID" prop="id"></el-table-column>
            <el-table-column label="活动名称" prop="title"></el-table-column>
            <el-table-column label="主办方" prop="organizerInfoVo.organizerName"></el-table-column>
            <el-table-column label="主办方类型">
                <template slot-scope="{row}">
                    {{showOrganizerType(row.organizerType)}}
                </template>
            </el-table-column>
            <el-table-column label="发布时间">
                <template slot-scope="{row}">
                    {{timestampToDateTime(row.createTime)}}
                </template>
            </el-table-column>
            <el-table-column label="活动时间">
                <template slot-scope="{row}">{{`${timestampToDateTime(row.activityStartTime)} ~
                    ${timestampToDateTime(row.activityFinishTime)}`}}
                </template>
            </el-table-column>
            <el-table-column label="报名时间">
                <template slot-scope="{row}">{{`${timestampToDateTime(row.registrationStartTime)} ~
                    ${timestampToDateTime(row.registrationFinishTime)}`}}
                </template>
            </el-table-column>
            <el-table-column label="活动地点" prop="address"></el-table-column>
            <el-table-column label="审核状态">
                <template slot-scope="{row}">
                    {{showReviewStatus(row.status)}}
                </template>
            </el-table-column>
            <el-table-column label="活动状态" v-if="reviewStatus == -1">
                <template slot-scope="{row}">
                    {{showActivityStatus(row.status)}}
                </template>
            </el-table-column>
            <el-table-column label="关注人数" prop="follower"></el-table-column>
            <el-table-column label="报名人数" prop="applicantQuantity"></el-table-column>
            <el-table-column label="活动详情">
                <template slot-scope="{row}">
                    <el-link :href="getActivityUrl(row.id)" type="primary" target="_blank">点击查看</el-link>
                </template>
            </el-table-column>

            <el-table-column label="操作" align="center" width="380px;">
                <template slot-scope="{row}">
                    <span style="padding-right: 10px;">
                        <el-button type="primary" size="mini"
                                   @click="showReviewList(row)"
                                   icon="el-icon-edit">审核记录
                        </el-button>
                    </span>
                    <span v-if="row.status === 0">
                        <el-button
                                type="success"
                                size="mini"
                                icon="el-icon-check"
                                @click="handleActivityReviewPass(row.id)"
                        >通过</el-button>

                        <el-button
                                type="warning"
                                size="mini"
                                icon="el-icon-close"
                                @click="handleActivityReviewFailure(row.id)"
                        >拒绝</el-button>
                    </span>

                    <span v-if="reviewStatus == -1" style="padding-left: 10px;">
                         <el-button
                                 type="primary"
                                 size="mini"
                                 icon="el-icon-sort"
                                 @click="handleActivityWeightSetUp(row.id, row.weight)"
                                 :disabled="row.status === 2"
                         >权重设置</el-button>
                    </span>

                    <span v-if="reviewStatus == -1" style="padding-left: 10px;">
                         <el-button
                                 type="danger"
                                 size="mini"
                                 icon="el-icon-delete"
                                 @click="handleTakeoff(row.id)"
                                 :disabled="row.status === 2"
                         >下架</el-button>
                    </span>
                </template>
            </el-table-column>
        </el-table>

        <pagination
                v-show="total>0"
                :total="total"
                :page.sync="listQuery.page"
                :limit.sync="listQuery.limit"
                @pagination="handleRouteList"
        />

        <el-dialog title="审核记录" :visible.sync="reviewDialog" width="70%">
            <el-table :data="reviewList" highlight-current-row :default-sort="{prop: 'id'}">
                <el-table-column property="id" label="审核ID" width="100"></el-table-column>
                <el-table-column property="activityId" label="活动ID"></el-table-column>
                <el-table-column property="updateTime" label="审核时间"></el-table-column>
                <el-table-column label="审核结果">
                    <template slot-scope="{row}">
                        {{reviewStatus2[row.status]}}
                    </template>
                </el-table-column>
                <el-table-column property="reason" label="理由"></el-table-column>
            </el-table>
        </el-dialog>
    </div>
</template>

<script>
    import waves from "@/directive/waves"; // waves directive
    import Pagination from "@/components/Pagination"; // secondary package based on el-pagination
    import {
        activityReviewFailure,
        activityReviewPass, activityWeightSetup,
        getActivityList,
        getActivityReviewInfo,
        takeoffActivity
    } from "@/api/activity_api";

    export default {
        name: "ActivityList",
        components: {Pagination},
        directives: {waves},
        data() {
            return {
                tableKey: 0,
                list: [],
                total: 0,
                listLoading: true,
                listQuery: {
                    id: undefined,
                    title: undefined,
                    organizerName: undefined,
                    cityIds: undefined,
                    status: 0,
                    organizerType: undefined,
                    reviewType: undefined,
                    activityStartTime: undefined,
                    activityFinishTime: undefined,
                    page: 1,
                    limit: 20,
                    sort: "-id"
                },
                reviewStatus: "0",
                activityStatus: undefined,
                activityDateRange: undefined,
                cityIdProps: {
                    lazy: true,
                    lazyLoad: (node, resolve) => {
                        if (node.level === 1) {
                            this.$axios.request({
                                url: "/city/list",
                                method: "get",
                                params: {type: node.value}
                            }).then(data => {
                                console.log(data.data);
                                let nodes = data.data.map(second => {
                                    let children = second.children && second.children.map(third => {
                                        return {id: third.id, name: third.name, leaf: true}
                                    })
                                    return {id: second.id, name: second.name, children}
                                });
                                resolve(nodes);
                            })
                        } else {
                            resolve();
                        }
                    },
                    expandTrigger: "hover",
                    value: "id",
                    label: "name",
                    emitPath: false,
                    children: "children"
                },
                cityOptions: [{id: 1, name: "国内"}, {id: 2, name: "国外"}],
                status: [{id: 3, name: "即将开始"}, {id: 4, name: "报名中"}, {id: 5, name: "进行中"},
                    {id: 6, name: "已结束"}, {id: 2, name: "已下架"}],
                organizerType: [{id: 4, name: "企业"}, {id: 1, name: "学生组织"}, {id: 2, name: "社会组织"}, {id: 3, name: "个人"}],
                reviewType: [{id: 1, name: "初次审核"}, {id: 2, name: "编辑后审核"}],
                newDialogVisible: false,
                activityForm: {},
                takeoffForm: {
                    id: undefined,
                    reason: undefined
                },

                reviewDialog: false,
                reviewList: [],

                reviewStatus2: {
                    "1": "审核中",
                    "2": "通过",
                    "3": "拒绝"
                },
            };
        },
        created() {
            this.initData();
        },
        watch: {
            $route() {
                this.getList();
            }
        },
        methods: {
            initData() {
                this.getList();
            },
            getList() {
                this.listLoading = true;
                const query = this.$route.query;
                Object.keys(query).forEach(key => {
                    this.listQuery[key] = query[key];
                });
                if (query.page) {
                    this.listQuery.page = parseInt(query.page);
                }
                if (query.limit) {
                    this.listQuery.limit = parseInt(query.limit);
                }

                if (query.status == 7)
                    this.reviewStatus = "7";
                else if (query.status == 0)
                    this.reviewStatus = "0";
                else {
                    this.reviewStatus = "-1";

                    if(this.activityStatus == null)
                        this.listQuery.status = -1;
                    else
                        this.listQuery.status = this.activityStatus;
                }


                if (this.activityDateRange == null) {
                    this.listQuery.activityStartTime = null;
                    this.listQuery.activityFinishTime = null;
                } else {
                    this.listQuery.activityStartTime = this.activityDateRange[0].getTime();
                    this.listQuery.activityFinishTime = this.activityDateRange[1].getTime();
                }

                getActivityList(this.listQuery).then(response => {
                    const {total, list} = response.data;
                    this.list = list;
                    this.total = total;
                    this.listLoading = false;
                });
            },
            handleRouteList() {
                this.$router.push({path: this.$route.path, query: this.listQuery});
            },
            handleFilter() {
                if (this.reviewStatus != -1) {
                    this.listQuery.status = this.reviewStatus;
                    this.activityStatus = null;
                } else {
                    if(this.activityStatus == null)
                        this.listQuery.status = -1;
                    else
                        this.listQuery.status = this.activityStatus;
                }

                this.listQuery.page = 1;
                this.handleRouteList();
            },

            getActivityUrl(id) {
                return `${process.env.VUE_APP_WEB_HOST}/activity/${id}`
            },

            handleSelectMenu(index) {
                this.reviewStatus = index;

                if (this.reviewStatus != -1) {
                    this.listQuery.status = this.reviewStatus;
                    this.activityStatus = null;
                } else {
                    if(this.activityStatus == null)
                        this.listQuery.status = -1;
                    else
                        this.listQuery.status = this.activityStatus;
                }

                this.$router.push({path: this.$route.path, query: this.listQuery});
            },

            getHandledValue(num) {
                return num < 10 ? '0' + num : num;
            },
            timestampToDateTime(timeStamp) {
                if (timeStamp) {
                    const d = new Date(parseInt(timeStamp));
                    const year = this.getHandledValue(d.getFullYear());
                    const month = this.getHandledValue(d.getMonth() + 1);
                    const day = this.getHandledValue(d.getDate());
                    const hours = this.getHandledValue(d.getHours());
                    const minutes = this.getHandledValue(d.getMinutes());
                    const second = this.getHandledValue(d.getSeconds());
                    return year + '-' + month + '-' + day + ' ' + hours + ':' + minutes + ':' + second;
                } else {
                    return '';
                }
            },

            showReviewStatus(status) {
                if (status == null) return '';

                if (status == 0) return '待审核';
                else if (status == 7) return '不通过';
                else return '已通过';
            },

            showActivityStatus(status) {
                const result = this.status.find(function (cc) {
                    return cc.id === status
                });

                if (result != null) return result.name;
            },

            showOrganizerType(type) {
                const result = this.organizerType.find(function (cc) {
                    return cc.id == type
                });

                if (result != null) return result.name;
            },

            handleTakeoff(activityId) {
                this.$prompt("请填写下架理由", "提示", {
                    confirmButtonText: "确定并通知主办方",
                    cancelButtonText: "取消"
                }).then(({value}) => {
                    this.takeoffForm.id = activityId;
                    this.takeoffForm.reason = value;
                    takeoffActivity(this.takeoffForm).then(response  => {
                        if(response.code === 0){
                            this.getList();
                            this.$message.success("下架活动成功");
                        }
                        else
                            this.$message.error("下架活动失败");


                    });
                });
            },

            handleActivityWeightSetUp(activityId, weight){

                this.$prompt("请设置活动权重,越大排名越前 -- 当前权重:"+weight, "权重设置", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    inputPattern: /^([1-9]?\d|100)$/,
                inputErrorMessage: '权重只能是0-100的纯数字'
                }).then(({value}) => {
                    console.log(value);

                    activityWeightSetup(activityId, value).then(response  => {
                        if(response.code === 0){
                            this.getList();
                            this.$message.success("活动权重设置成功");
                        }
                        else
                            this.$message.error("活动权重设置失败");
                    });
                });
            },

            handleActivityReviewPass(activityId) {
                this.$confirm("此操作将通过活动审核, 是否继续?", "提示", {
                    confirmButtonText: "确认",
                    cancelButtonText: "取消",
                    type: "warning"
                }).then(() => {
                    activityReviewPass(activityId).then(response => {
                        if(response.code === 0){
                            this.getList();
                            this.$message("操作成功");
                        }
                    });
                });
            },

            handleActivityReviewFailure(activityId) {
                this.$prompt("请填写审核不通过理由", "提示", {
                    confirmButtonText: "确定并通知主办方",
                    cancelButtonText: "取消"
                }).then(({value}) => {
                    activityReviewFailure(activityId, value).then(response  => {
                        if(response.code === 0){
                            this.getList();
                            this.$message.success("活动审核不通过成功");
                        }
                        else
                            this.$message.error("活动审核不通过失败");
                    });
                });
            },

            showReviewList(activity){
                getActivityReviewInfo(activity.id).then(response => {
                    if(response.code === 0){
                        this.reviewList = response.data.list;
                        this.reviewDialog = true;
                    }else{
                        this.$message.error("获取审核列表失败");
                    }
                });
            },
        }
    };
</script>
