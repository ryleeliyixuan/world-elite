<template>
    <div class="app-container">
        <div class="filter-container">
            <el-form :inline="true" :model="listQuery">
                <el-form-item>
                    <el-input v-model="listQuery.activityId" placeholder="活动ID" size="medium"
                              @keyup.enter.native="handleFilter"
                              style="width: 150px;"
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
                              style="width: 150px;"
                              clearable></el-input>
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
                    <el-input v-model="listQuery.reportUserName" placeholder="举报人" size="medium"
                              @keyup.enter.native="handleFilter"
                              style="width: 150px;"
                              clearable></el-input>
                </el-form-item>

                <el-form-item>
                    <el-select
                            v-model="optionIds"
                            filterable
                            multiple
                            collapse-tags
                            clearable
                            size="medium"
                            placeholder="举报原因"
                            @change="handleFilter"
                    >
                        <el-option
                                v-for="item in reasonList"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id"
                        ></el-option>
                    </el-select>
                </el-form-item>

                <el-form-item>
                    <el-button type="primary" @click="handleFilter">查询</el-button>
                </el-form-item>
            </el-form>
        </div>

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
            <el-table-column label="活动ID" prop="activityId"></el-table-column>
            <el-table-column label="活动名称" prop="title"></el-table-column>
            <el-table-column label="活动时间">
                <template slot-scope="{row}">{{`${timestampToDateTime(row.activityStartTime)} ~
                    ${timestampToDateTime(row.activityFinishTime)}`}}
                </template>
            </el-table-column>
            <el-table-column label="主办方" prop="organizerName"></el-table-column>
            <el-table-column label="主办方类型">
                <template slot-scope="{row}">
                    {{showOrganizerType(row.organizerType)}}
                </template>
            </el-table-column>
            <el-table-column label="举报人" prop="reportUserName"></el-table-column>
            <el-table-column label="举报时间">
                <template slot-scope="{row}">
                    {{timestampToDateTime(row.createTime)}}
                </template>
            </el-table-column>
            <el-table-column label="举报原因" prop="reason"></el-table-column>
        </el-table>

        <pagination
                v-show="total>0"
                :total="total"
                :page.sync="listQuery.page"
                :limit.sync="listQuery.limit"
                @pagination="handleRouteList"
        />
    </div>
</template>

<script>
    import waves from "@/directive/waves"; // waves directive
    import Pagination from "@/components/Pagination"; // secondary package based on el-pagination
    import {getActivityReportList, getOrganizerCreditList} from "@/api/activity_api";
    import {listByType} from "@/api/dict_api";

    export default {
        name: "CreditList",
        components: {Pagination},
        directives: {waves},
        data() {
            return {
                tableKey: 0,
                list: [],
                total: 0,
                listLoading: true,
                listQuery: {
                    activityId: undefined,
                    title: undefined,
                    organizerName: undefined,
                    organizerType: undefined,
                    reportUserName: undefined,
                    optionIds: undefined,
                    page: 1,
                    limit: 20,
                    sort: undefined
                },
                optionIds: [],
                activityDateRange: undefined,
                organizerType: [{id: 4, name: "企业"}, {id: 1, name: "学生组织"}, {id: 2, name: "社会组织"}, {id: 3, name: "个人"}],
                reasonList: {},
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
                listByType(24).then(response => (this.reasonList = response.data.list));
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

                if (this.activityDateRange == null) {
                    this.listQuery.activityStartTime = null;
                    this.listQuery.activityFinishTime = null;
                } else {
                    this.listQuery.activityStartTime = this.activityDateRange[0].getTime();
                    this.listQuery.activityFinishTime = this.activityDateRange[1].getTime();
                }

                this.listQuery.optionIds = this.optionIds.join(",");

                getActivityReportList(this.listQuery).then(response => {
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
                this.listQuery.page = 1;
                this.handleRouteList();
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

            showOrganizerType(type) {
                const result = this.organizerType.find(function (cc) {
                    return cc.id == type
                });

                if (result != null) return result.name;
            },
        }
    };
</script>
