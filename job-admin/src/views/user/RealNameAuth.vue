<template>
    <div class="app-container">
        <div class="filter-container">
            <el-form :inline="true" :model="listQuery">
                <el-form-item>
                    <el-input v-model="listQuery.userId" placeholder="用户ID" clearable
                              @keyup.enter.native="handleFilter"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-input v-model="listQuery.name" placeholder="姓名" clearable
                              @keyup.enter.native="handleFilter"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-input v-model="listQuery.idNumber" placeholder="身份证号/学生证号" clearable
                              @keyup.enter.native="handleFilter"></el-input>
                </el-form-item>

                <el-form-item>
                    <el-button type="primary" @click="handleFilter">查询</el-button>
                </el-form-item>
            </el-form>
        </div>

        <el-menu :default-active="listQuery.status" mode="horizontal" @select="handleSelectMenu">
            <el-menu-item index="1">待审核</el-menu-item>
            <el-menu-item index="2">正常</el-menu-item>
            <el-menu-item index="3">已拒绝</el-menu-item>
        </el-menu>

        <pagination
                v-show="total>0"
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
            <el-table-column label="用户ID" prop="userId" width="180">
                <template slot-scope="{row}">
                    <el-button type="text" @click="showUserVerfication(row)">{{ row.userId }}</el-button>
                </template>
            </el-table-column>
            <el-table-column label="用户名" prop="name"></el-table-column>
            <el-table-column label="身份证号" prop="idNumber"></el-table-column>
            <el-table-column label="业务类型">
                <template slot-scope="{row}">
                    <el-tag>{{businessType[row.businessType]}}</el-tag>
                </template>
            </el-table-column>
            <el-table-column label="审核状态">
                <template slot-scope="{row}">
                    <el-tag>{{reviewStatus[row.status]}}</el-tag>
                </template>
            </el-table-column>
            <el-table-column label="原因" prop="reason" v-if="listQuery.status === '3'"></el-table-column>
            <el-table-column label="操作" align="center" fixed="right" width="200" v-if="listQuery.status === '1'">
                <template slot-scope="{row}">
                  <span>
                      <el-button
                              type="success"
                              size="mini"
                              icon="el-icon-check"
                              @click="handleVerifyUser(row.userId, 2)"
                      >通过</el-button>
                    <el-button
                            type="warning"
                            size="mini"
                            icon="el-icon-close"
                            @click="handleVerifyUser(row.userId, 3)"
                    >拒绝</el-button>
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

        <el-drawer title="用户实名认证审核资料" :visible.sync="verifyDrawerVisible" direction="rtl" size="50%">
            <div
                    class="drawer__footer"
                    style="padding: 12px"
                    v-if="verifyUser && verifyUser.status === 0"
            >
                <el-button type="success" icon="el-icon-check" @click="handleVerifyUser(verifyUser, 2)">通过</el-button>
                <el-button type="warning" icon="el-icon-close" @click="handleVerifyUser(verifyUser, 3)">拒绝</el-button>
            </div>
            <VerificationView :userId="verifyUser?verifyUser.userId : undefined"></VerificationView>
        </el-drawer>
    </div>
</template>

<script>
    import waves from "@/directive/waves"; // waves directive
    import Pagination from "@/components/Pagination"; // secondary package based on el-pagination
    import VerificationView from "./RealNameVerificationView";
    import {getUserRealNameAuthList, realNameAuthFailure, realNameAuthPass} from "@/api/user_api";

    export default {
        name: "UserList",
        components: {Pagination, VerificationView},
        directives: {waves},
        data() {
            return {
                tableKey: 0,
                list: [],
                total: 0,
                listLoading: true,
                listQuery: {
                    userId: undefined,
                    name: undefined,
                    idNumber: undefined,
                    status: "1",
                    page: 1,
                    limit: 20
                },
                reviewStatus: {
                    1: "审核中",
                    2: "通过",
                    3: "拒绝"
                },
                businessType: {
                    1: "活动"
                },
                verifyDrawerVisible: false,
                verifyUser: undefined,
            };
        },
        created() {
            this.getList();
        },
        watch: {
            $route() {
                this.getList();
            },
        },
        methods: {
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
                getUserRealNameAuthList(this.listQuery).then(response => {
                    const {total, list} = response.data;
                    this.list = list;
                    this.total = total;
                    this.listLoading = false;
                });
            },
            handleSelectMenu(index) {
                this.listQuery.status = index;
                this.$router.push({path: this.$route.path, query: {status: index}});
            },
            handleRouteList() {
                this.$router.push({path: this.$route.path, query: this.listQuery});
            },
            handleFilter() {
                this.listQuery.page = 1;
                this.handleRouteList();
            },

            handleVerifyUser(userId, status) {
                this.verifyDrawerVisible = false;
                if (status === 2) {
                    this.$confirm("此操作将通过实名认证审核, 是否继续?", "提示", {
                        confirmButtonText: "继续",
                        cancelButtonText: "取消",
                        type: "warning"
                    }).then(() => {
                        realNameAuthPass(userId).then(response => {
                            this.getList();
                            this.$message("操作成功");
                        });
                    });
                } else if (status === 3) {
                    this.$prompt("请填写拒绝通过的理由", "提示", {
                        confirmButtonText: "提交",
                        cancelButtonText: "取消"
                    }).then(({value}) => {
                        realNameAuthFailure(userId, value).then(response => {
                            this.getList();
                            this.$message("操作成功");
                        });
                    });
                }
            },
            showUserVerfication(user) {
                this.verifyDrawerVisible = true;
                this.verifyUser = user;
            },
        }
    };
</script>
