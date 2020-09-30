<template>
    <div class="app-container">
        <h5>我的活动</h5>
        <pagination
                v-show="total"
                :total="total"
                :page.sync="listQuery.page"
                :limit.sync="listQuery.limit"
                @pagination="handleRouteList"/>
        <el-tabs class="nav-bar" v-model="activeName" @tab-click="handleTabSelect">
            <el-tab-pane label="全部关注" name="0"></el-tab-pane>
            <el-tab-pane label="即将开始" name="4"></el-tab-pane>
            <el-tab-pane label="进行中" name="5"></el-tab-pane>
            <el-tab-pane label="已结束" name="6"></el-tab-pane>
        </el-tabs>
        <div class="activity-list" v-if="pageResult.list && pageResult.list.length !== 0">
            <el-card shadow="hover"
                     v-for="activity in pageResult.list"
                     :key="activity.id"
                     class="mb-2 link-pointer"
                     @click.native="openActivityDetail(activity.id)">
                <b-media right-align vertical-align="center">
                    <div class="d-flex align-items-center">
                        <h6 class="mt-0 mb-0">{{activity.title}}</h6>
                        <span class="text-small ml-4 text-warning" style="min-width: 90px;" v-if="getTime(activity.startTime,activity.finishTime)>=0">
                            <svg-icon icon-class="timer-down"/>
                            {{getTime(activity.startTime,activity.finishTime)===0?"即将结束":getTime(activity.startTime,activity.finishTime)+"天后结束"}}
                        </span>
                        <span class="text-small ml-4 text-danger" v-else>
                            <svg-icon icon-class="clock"/>
                            即将开始
                        </span>
                    </div>
                    <div class="text-label mt-1">
                        <b>活动地址：</b>
                        {{activity.address}}
                    </div>
                    <div class="text-label">
                        <b>活动时间：</b>
                        {{activity.startTime}} 到 {{activity.finishTime}}
                    </div>
                    <template v-slot:aside>
                        <el-button class="cancel-large"
                                   type="danger"
                                   @click.stop="delFavorite(activity)"
                                   icon="el-icon-delete"
                                   size="small"
                                   :loading="favoriteLoading">
                            取消关注
                        </el-button>
                    </template>
                </b-media>
                <el-button class="cancel-small"
                           type="danger"
                           @click.stop="delFavorite(activity)"
                           icon="el-icon-delete"
                           size="small"
                           :loading="favoriteLoading">
                    取消关注
                </el-button>
            </el-card>
        </div>
        <div v-else class="text-center">暂无活动</div>
        <pagination v-show="total"
                    :total="total"
                    :page.sync="listQuery.page"
                    :limit.sync="listQuery.limit"
                    @pagination="handleRouteList"/>
    </div>
</template>

<script>
    import {getFavoriteActivityList, doFavorite} from "@/api/favorite_api";
    import Pagination from "@/components/Pagination";
    import {formatListQuery, parseListQuery} from "@/utils/common";

    export default {
        name: "MyActivityListPage",
        components: {Pagination},
        data() {
            return {
                listQuery: {
                    page: 1,
                    limit: 10,
                    sort: "-id",
                    status: undefined
                },
                total: 0,
                pageResult: 0,
                favoriteLoading: false,
                favoriteForm: {
                    favorite: false,
                    type: 3
                },
                activeName: "0",
            };
        },
        created() {
            this.getList();
        },
        watch: {
            $route() {
                this.getList();
            }
        },
        methods: {
            getList() {
                parseListQuery(this.$route.query, this.listQuery);
                getFavoriteActivityList(this.listQuery).then(response => {
                    this.pageResult = response.data;
                    this.total = this.pageResult.total;
                    this.$emit("complete");
                });
            },
            openActivityDetail(id) {
                this.$router.push(`/activity/${id}`);
            },
            delFavorite(acitivity) {
                this.favoriteLoading = true;
                this.favoriteForm.objectId = acitivity.id;
                doFavorite(this.favoriteForm)
                    .then(() => {
                        this.$message("取消成功");
                        this.getList();
                    })
                    .finally(() => {
                        this.favoriteLoading = false;
                    });
            },
            handleRouteList() {
                this.$router.push({
                    path: this.$route.path,
                    query: formatListQuery(this.listQuery)
                });
            },
            handleTabSelect(tab, event) {
                this.listQuery.status = tab.name === 0 ? undefined : tab.name;
                this.handleRouteList();
            },
            getTime(startTime, finishTime) {
                if (new Date(startTime) - new Date() > 0) { // 即将开始
                    return -1;
                } else {
                    return Math.floor((new Date(finishTime) - new Date()) / 24 / 60 / 60 / 1000);
                }
            }
        }
    };
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" scoped>
    .app-container {
        max-width: 1000px;
        margin: 20px auto;
        min-height: calc(100vh - 517px);

        .nav-bar {
            display: inline-flex;
            margin-bottom: 10px;
            align-items: center;

            .divider {
                margin: 0 20px
            }
        }

        .activity-list {
            width: 75%;

            .mb-2 link-pointer {
                margin-bottom: 10px;
            }

            .cancel-large {
                display: block;
            }

            .cancel-small {
                display: none;
            }
        }
    }

    .text-label {
        color: #555;
        font-size: 15px;
    }

    @media screen and (max-width: 700px) {
        .app-container {
            width: 100%;
            padding: 0 20px;

            .activity-list {
                width: 100%;

                .cancel-large {
                    display: none;
                }

                .cancel-small {
                    margin: 0 auto;
                    display: block;
                }
            }
        }
    }

    @media screen and (max-width: 410px) {

    }

</style>
