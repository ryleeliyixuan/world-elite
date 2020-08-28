<template>
    <div class="app-container">
        <div class="section1-container">
            <el-select
                    v-model="listQuery.cityIds"
                    filterable
                    clearable
                    multiple
                    placeholder="活动城市"
                    @change="handleFilter"
                    size="small"
                    class="section1-select"
            >
                <el-option v-for="item in cityOptions" :key="item.id" :label="item.name" :value="item.id"></el-option>
            </el-select>
        </div>

        <div class="section2-container">
            <pagination
                    v-show="total"
                    :total="total"
                    :page.sync="listQuery.page"
                    :limit.sync="listQuery.limit"
                    @pagination="handleRouteList"
            />
        </div>

        <div class="section3-container">
            <div class="activity-list" v-if="list.length !== 0">
                <el-card
                        shadow="hover"
                        v-for="activity in list"
                        :key="activity.id"
                        class="mb-2 link-pointer"
                        @click.native="openActivity(activity)"
                >
                    <div class="section3-inner-container">
                        <div class="section3-image-container">
                            <el-image :src="activity.thumbnail" class="section-image"></el-image>
                        </div>

                        <div class="section3-right-container">
                            <h5>
                                {{activity.title}}
                                <el-tag type="primary" class="section3-tag">{{activity.city.name}}</el-tag>
                            </h5>
                            <div class="text-label" v-if="activity.organizer && activity.organizer != ''">
                                <b>主办方：</b>
                                {{activity.organizer}}
                            </div>
                            <div class="text-label">
                                <b>活动地址：</b>
                                {{activity.address}}
                            </div>
                            <div class="text-label">
                                <b>活动时间：</b>
                                {{activity.startTime}} 到 {{activity.finishTime}}
                            </div>
                            <div class="mt-2 text-desc">
                                {{activity.summary}}
                            </div>
                        </div>
                    </div>
                </el-card>
            </div>
        </div>

        <div class="section2-container">
            <pagination
                    v-show="total"
                    :total="total"
                    :page.sync="listQuery.page"
                    :limit.sync="listQuery.limit"
                    @pagination="handleRouteList"
            />
        </div>
    </div>
</template>

<script>
    import {listByType} from "@/api/dict_api";
    import {getActivityList} from "@/api/activity_api";
    import Pagination from "@/components/Pagination";
    import {formatListQuery, parseListQuery} from "@/utils/common";
    import {mapGetters} from "vuex";

    export default {
        name: "JobListPage",
        components: {Pagination},
        computed: {
            ...mapGetters(["keyword"])
        },
        data() {
            return {
                listQuery: {
                    cityIds: [],
                    keyword: undefined,
                    status: "2",
                    sort: "-id",
                    page: 1,
                    limit: 10
                },
                total: 0,
                list: [],
                cityOptions: []
            };
        },
        created() {
            this.initData();
        },
        watch: {
            $route() {
                this.getList();
            },
            keyword() {
                this.listQuery.keyword = this.keyword;
                this.handleRouteList();
            }
        },
        methods: {
            initData() {
                listByType(2).then(response => (this.cityOptions = response.data.list));
                this.getList();
            },
            handleFilter() {
                this.listQuery.page = 1;
                this.handleRouteList();
            },
            getList() {
                parseListQuery(this.$route.query, this.listQuery);
                getActivityList(this.listQuery).then(response => {
                    this.list = response.data.list;
                    this.total = response.data.total;
                });
            },
            handleRouteList() {
                this.$router.push({
                    path: this.$route.path,
                    query: formatListQuery(this.listQuery)
                });
            },
            openActivity(activity) {
                this.$router.push(`/activity/${activity.id}`);
            }
        }
    };
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" scoped>
    .app-container {
        max-width: 1140px;
        margin: 0 auto;
        padding: 0 20px;

        .section1-container {
            min-width: 335px;

            .section1-select {
                flex: 1;
                min-width: 130px;
                margin: 0 10px 10px;
                height: 100%;
            }
        }

        .section2-container {
            width: 100%;
            overflow-x: auto;
        }

        .section3-container {
            .section3-inner-container {
              display: flex;
                align-items: center;
                .section3-image-container {
                    width: 109px;
                    height: 109px;
                    .section-image{
                        width: 109px;
                        height: 109px;
                    }
                }
                .section3-right-container{
                    margin-left: 30px;
                    .text-label {
                        color: #555;
                        font-size: 15px;
                    }
                    .text-desc {
                        font-size: 15px;
                        color: #888;
                    }
                    .section3-tag{
                     height: 20px;
                        line-height: 20px;
                    }
                }
            }
        }

    }
    ::-webkit-scrollbar {
        display: none;
    }

    @media screen and (max-width: 850px) {
        .app-container {
            .section3-container {
                .section3-inner-container {
                        display: flex;
                        flex-direction: column;
                        align-items: center;
                    .section3-image-container{
                        margin-bottom: 20px;

                    }
                    .section3-right-container{
                        margin: 0;
                    }

                }
            }
        }
    }
</style>
