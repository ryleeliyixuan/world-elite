<template>
    <div class="app-container">
        <div class="filter-container">
            <el-form :inline="true" :model="listQuery">
                <el-form-item>
                    <el-input v-model="listQuery.organizerName" placeholder="主办方" size="medium"
                              @keyup.enter.native="handleFilter"
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
                    <el-select
                            v-model="listQuery.credit"
                            filterable
                            clearable
                            size="medium"
                            placeholder="信用等级"
                            style="width: 130px"
                            @change="handleFilter"
                    >
                        <el-option
                                v-for="item in creditType"
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
            <el-table-column label="主办方" prop="organizerName"></el-table-column>
            <el-table-column label="主办方类型" prop="organizerType"></el-table-column>
            <el-table-column label="发布活动数" prop="totalActivity"></el-table-column>
            <el-table-column label="已通过数" prop="passedActivity"></el-table-column>
            <el-table-column label="不通过数" prop="notPassActivity"></el-table-column>
            <el-table-column label="举办率">
                <template slot-scope="{row}">
                    {{Number(row.holdRate*100).toFixed(2)}}%
                </template>
            </el-table-column>
            <el-table-column label="被投诉活动数" prop="reportActivity"></el-table-column>
            <el-table-column label="投诉率">
                <template slot-scope="{row}">
                    {{Number(row.reportRate*100).toFixed(2)}}%
                </template>
            </el-table-column>

            <el-table-column label="信用等级">
                <template slot-scope="{row}">
                    {{showCreditLevel(row.credit)}}
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
    </div>
</template>

<script>
    import waves from "@/directive/waves"; // waves directive
    import Pagination from "@/components/Pagination"; // secondary package based on el-pagination
    import {getOrganizerCreditList} from "@/api/activity_api";

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
                    organizerName: undefined,
                    organizerType: undefined,
                    credit: undefined,
                    page: 1,
                    limit: 20,
                    sort: undefined
                },

                organizerType: [{id: 4, name: "企业"}, {id: 1, name: "学生组织"}, {id: 2, name: "社会组织"}, {id: 3, name: "个人"}],
                creditType: [{id: 1, name: "一级"}, {id: 2, name: "二级"}, {id: 3, name: "三级"}],
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

                getOrganizerCreditList(this.listQuery).then(response => {
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

            showCreditLevel(level) {
                const result = this.creditType.find(function (cc) {
                    return cc.id === level
                });

                if (result != null) return result.name;
            },
        }
    };
</script>
