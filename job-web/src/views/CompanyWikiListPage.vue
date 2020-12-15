<template>
    <div class="app-container">
   <!--     <div class="section1-container">
            <pagination v-show="total"
                        :total="total"
                        :page.sync="listQuery.page"
                        :limit.sync="listQuery.limit"
                        @pagination="handleRouteList"/>
        </div> -->
        <div class="section2-container" v-if="list.length !== 0">
            <el-card shadow="hover"
                     v-for="company in list"
                     :key="company.id"
                     class="section2-item-container link-pointer"
                     @click.native="openCompanyDetail(company)">
                <el-image :src="company.logo" class="section2-left-container"></el-image>
                <div class="section2-right-container">
                    <h5>{{company.fullName}}</h5>
                    <div class="text-label">
                        <span v-if="company.stage">{{company.stage.name}} |</span>
                        <span v-if="company.property"> {{company.property.name}} |</span>
                        <span v-if="company.industry"> {{company.industry.name}} |</span>
                        <span v-if="company.scale"> {{company.scale.name}}</span>
                    </div>
                    <div class="text-desc">{{company.wikiSummary}}</div>
                </div>
            </el-card>
        </div>

        <div class="section1-container">
            <pagination v-show="total"
                        :total="total"
                        :page.sync="listQuery.page"
                        :limit.sync="listQuery.limit"
                        @pagination="handleRouteList"/>
        </div>
    </div>
</template>

<script>
    import {listByType} from "@/api/dict_api";
    import {getCompanyWikiList} from "@/api/company_api";
    import Pagination from "@/components/Pagination";
    import {formatListQuery, parseListQuery} from "@/utils/common";
    import {mapGetters} from "vuex";

    export default {
        name: "WikiListPage",
        components: {Pagination},
        computed: {
            ...mapGetters(["keyword"])
        },
        data() {
            return {
                listQuery: {
                    industryId:undefined,
                    keyword: undefined,
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
                if (this.keyword) {
                    this.listQuery.keyword = this.keyword;
                    this.handleRouteList();
                } else {
                    this.$router.replace("/wiki-card")
                }
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
                getCompanyWikiList(this.listQuery).then(response => {
                    this.list = response.data.list;
                    this.total = response.data.total;
                    this.$emit("complete");
                });
            },
            handleRouteList() {
                this.$router.push({
                    path: this.$route.path,
                    query: formatListQuery(this.listQuery)
                });
            },
            openCompanyDetail(company) {
                this.$router.push(`/company/${company.id}`)
            }
        }
    };
</script>

<style scoped lang="scss">
    .text-label {
        color: #555;
        font-size: 15px;
    }

    .text-desc {
        font-size: 15px;
        color: #888;
        margin-top: 8px;
    }

    .app-container {
        max-width: 1140px;
        margin: 0 auto;
        padding: 0 20px;
        min-width: 375px;
        min-height: calc(100vh - 477px);

        .section1-container {
            width: 100%;
            overflow-x: auto;
        }

        .section2-container {
            .section2-item-container {
                margin-bottom: 8px;
                display: flex;

                /deep/ .el-card__body {
                    display: flex;

                    .section2-left-container {
                        max-width: 100px;
						max-height: 100px;
                        margin-right: 20px;
                    }

                    .section2-right-container {
                        display: flex;
                        flex: 1;
                        flex-direction: column;
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
            .section2-container {
                .section2-item-container {
                    /deep/ .el-card__body {
                        flex-direction: column;
                        align-items: center;

                        .section2-left-container {
                            margin-bottom: 10px;
                        }
                    }
                }
            }
        }
    }

    @media screen and (max-width: 410px) {
        .app-container {

        }
    }


</style>
