<template>
    <div class="app-container">
        <div class="left-container" v-if="industryList.length>0">
            <el-scrollbar class="left-scrollbar"
                          wrap-style="overflow: hidden auto; padding-right: 20px;"
                          :native="false">
                <div class="left-scrollbar">
                    <el-link class="left-item"
                             :type="item.select?'primary':'default'"
                             :underline="false"
                             v-for="item in industryList" :key="item.id"
                             @click="onLink(item)">
                        {{item.name}}
                    </el-link>
                </div>
            </el-scrollbar>
        </div>

        <div class="main-container">
            <el-scrollbar class="main-scrollbar"
                          wrap-style="overflow: hidden auto; padding-right: 20px;"
                          :native="false"
                          ref="scrollbar">
                <div class="company-list-container" v-for="item in industryList" :key="item.id">
                    <div class="company-name-container" v-if="companyList[item.id] && companyList[item.id].length > 0">
                        <div class="company-name" :ref="item.name">{{item.name}}</div>
                        <el-link :underline="false"
                                 type="primary"
                                 v-if="companyList[item.id].length > 4"
                                 @click="onMore(item.id)">更多
                        </el-link>
                    </div>
                    <div class="company-item-container" v-if="companyList[item.id] && companyList[item.id].length > 0">
                        <el-card shadow="hover" class="item-container"
                                 v-for="(company, index) in companyList[item.id]"
                                 :key="company.id"
                                 v-if="index<4"
                                 @click.native="openCompanyDetail(company)">
                            <el-image class="item-logo" :src="company.logo" fit="contain"></el-image>
                            <div>
                                <div class="text-small text-gray">{{company.name}}</div>
                                <div class="text-small text-gray" style="min-height: 42px">
                                    <span v-if="company.stage">{{company.stage.name}} .</span>
                                    <span v-if="company.property">{{company.property.name}} .</span>
                                    <span v-if="company.industry">{{company.industry.name}} .</span>
                                    <span v-if="company.scale">{{company.scale.name}}</span>
                                </div>
                            </div>
                        </el-card>
                    </div>
                </div>
            </el-scrollbar>
        </div>
    </div>
</template>

<script>

    import {listByType} from "@/api/dict_api";
    import {mapGetters} from "vuex";

    export default {
        name: "ResumePage",
        data() {
            return {
                resumeId: undefined,
                exporting: false,
                industryList: [],//行业列表
                companyList: {}, // 公司列表
                timer: undefined,
            };
        },
        computed: {
            ...mapGetters(["keyword"])
        },
        watch: {
            keyword() {
                if (this.keyword) {
                    this.$router.replace("/wiki-list?keyword=" + this.keyword)
                } else {
                    // this.companyList = {};
                    this.getCompanyList();
                }
            }
        },
        created() {
            this.resumeId = this.$route.params.id;
            this.initData();
        },
        mounted() {
            this.$refs.scrollbar.wrap.onscroll = () => {
                if (!this.timer) { // 自动滚动时不处理
                    this.industryList.map(item => {
                        this.$set(item, "select", false)
                        return item;
                    })
                    for (let i = 0; i < this.industryList.length; i++) {
                        let card = this.$refs[this.industryList[i].name];
                        if (card && this.$refs.scrollbar.wrap.scrollTop <= card[0].offsetTop) {
                            this.industryList[i].select = true;
                            break;
                        }
                    }
                }
            }
        },
        methods: {
            initData() {
                listByType(6).then(response => {
                        this.industryList = response.data.list;
                        if (this.industryList.length > 0) {
                            this.$set(this.industryList[0], "select", true);
                        }
                        this.getCompanyList();
                    }
                );
            },

            onLink(item) {
                // 修改选中状态
                this.industryList.map(item => {
                    this.$set(item, "select", false);
                    return item;
                })
                this.$set(item, "select", true);

                let card = this.$refs[item.name];
                if (card) {
                    if (this.timer) {
                        clearInterval(this.timer);
                    }
                    this.timer = setInterval(() => {
                        let offset = (card[0].offsetTop - this.$refs.scrollbar.wrap.scrollTop) * 0.03;
                        offset = offset > 0 ? Math.ceil(offset) : Math.floor(offset);
                        this.$refs.scrollbar.wrap.scrollTop += offset;

                        if (card[0].offsetTop === Math.round(this.$refs.scrollbar.wrap.scrollTop) // 到达位置
                            || this.$refs.scrollbar.wrap.scrollHeight === Math.round(this.$refs.scrollbar.wrap.scrollTop + this.$refs.scrollbar.wrap.offsetHeight) // 滑到底部
                        ) {
                            clearInterval(this.timer);
                            setTimeout(() => {
                                this.timer = undefined;
                            }, 50)
                        }
                    }, 10)
                }

            },

            getCompanyList() {
                this.industryList.forEach(item => {
                    this.getItem(item.id)
                })
            },

            getItem(id) {
                this.$axios.get('/company/list-wiki-by-industry', {
                    params: {industryId: id, keyword: this.keyword}
                }).then(data => {
                    this.$set(this.companyList, id, data.data.list)
                })
            },

            openCompanyDetail(company) {
                this.$router.push(`/company/${company.id}`)
            },

            onMore(industryId) {
                let keywordParam = this.keyword ? "&keyword=" + this.keyword : "";
                this.$router.push("/wiki-list?industryId=" + industryId + keywordParam);
            }
        }
    };
</script>


<style scoped lang="scss">
    .app-container {
        width: 1140px;
        margin: 0 auto;
        padding: 20px;

        .left-container {
            position: absolute;
            top: 200px;
            left: 30px;
            height: calc(100% - 300px);
            overflow-y: auto;

            .left-scrollbar {
                height: 100%;

                .left-item {
                    display: block;
                    font-size: 14px;
                    margin-bottom: 20px;
                }
            }
        }

        .main-container {
            height: calc(100vh - 300px);
            overflow-y: auto;

            .main-scrollbar {
                height: 100%;
            }
        }

        .company-list-container {

            .company-name-container {
                display: flex;
                justify-content: space-between;

                .company-name {
                    font-size: 18px;
                    font-weight: bold;;
                    color: #000;
                }
            }

            .company-item-container {
                display: flex;
                align-items: center;
                margin: 10px 0 20px;

                .item-container {
                    width: 262px;
                    margin-right: 10px;
                    display: flex;
                    flex-direction: column;
                    align-items: center;

                    .item-logo {
                        height: 100px;
                        width: 100%;
                    }

                    &:hover {
                        cursor: pointer;
                    }
                }

                .item-container:last-of-type {
                    margin-right: 0;
                }
            }
        }
    }


</style>
