<template>
    <div class="app-container">
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
                        <span v-if="company.stage">{{company.stage.name}}</span>
                        <span v-if="company.property"> | {{company.property.name}}</span>
                        <span v-if="company.industry"> | {{company.industry.name}}</span>
                        <span v-if="company.scale"> | {{company.scale.name}}</span>
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
                        @pagination="getList"/>
        </div>
    </div>
</template>

<script>
    import Pagination from "@/components/Pagination";

    export default {
        name: "WikiListPage",
        components: {Pagination},
        data() {
            return {
                listQuery: {
                    industryId: undefined,
                    keyword: undefined,
                    page: 1,
                    limit: 10
                },
                total: 0,
                list: [],
            };
        },
        watch: {
            $route() {
                this.getList();
            }
        },
        mounted() {
            let query = this.$storage.getObject(this.$options.name)
            if (query) {
                this.listQuery = query;
            }
            this.getList();
        },
        methods: {
            getList() {
                console.log("-----", this.$route);
                this.listQuery.industryId = this.$route.query.industryId;
                this.listQuery.keyword = this.$route.query.searchWord;
                this.$storage.setData(this.$options.name, this.listQuery);
                this.$axios.get("/company/list-wiki-by-industry", {params: this.listQuery}).then(response => {
                    this.list = response.data.list;
                    this.total = response.data.total;
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
