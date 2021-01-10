<template>
    <div class="app-container">
        <div class="title">发布活动管理</div>
        <div class="type-container">
            <div v-for="status in statusList" :class="['text',{'select':listQuery.status===status.id}]" @click="onActivityStatus(status)">{{status.name}}</div>
            <div class="publish-button">发布活动</div>
            <el-popover class="sort-container"
                        placement="bottom-end"
                        width="136"
                        popper-class="option"
                        trigger="click"
                        content="这是一段内容,这是一段内容,这是一段内容,这是一段内容。">
                <div class="order-item" v-for="order in orderList">{{order.name}}</div>
                <svg-icon slot="reference" icon-class="sort" class-name="sort"></svg-icon>
            </el-popover>
        </div>
        <div class="line"></div>
    </div>
</template>

<script>
    import Pagination from "@/components/Pagination2";

    export default {
        name: "ActivityPublishPage",
        components: {Pagination},
        data() {
            return {
                // img: 'https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=238583692,2460632321&fm=26&gp=0.jpg',
                // loading: true,

                // 活动状态 0审核中;1草稿;2下架;3即将开始(报名即将开始和活动即将开始都是3);4报名中;5进行中;6活动结束
                statusList: [{id: undefined, name: '全部活动'}, {id: 3, name: '即将开始'}, {id: 4, name: '报名中'},
                    {id: 5, name: '进行中'}, {id: 6, name: '已结束'}, {id: 0, name: '审核处理的活动'}],

                orderList: [ // 活动排序列表
                    {sortField: "PUBLISH_TIME", name: '按发布时间顺序', sort: 'asc'},
                    {sortField: "FOLLOWER", name: '按发布时间倒序', sort: 'desc'},
                    {sortField: "REGISTRATION_TIME", name: '按活动名称排序', sort: 'asc'},
                    {sortField: "ACTIVITY_TIME", name: '按报名时间顺序', sort: 'asc'},
                    {sortField: "ACTIVITY_TIME", name: '按开始时间顺序', sort: 'asc'}
                ],
                listQuery: {
                    userId: this.$store.state.user.userId,
                    keyword: undefined, // 搜索关键字
                    status: undefined, // 活动状态
                    sort: "desc", // 降序
                    sortField: undefined, // 排序字段
                    page: 1,
                    limit: 10
                },

                dataList: [], // 活动列表
                total: 0, // 活动总数
            };
        },
        watch: {
            $route() {
                this.handleFilter();
            }
        },
        mounted() {
            let query = this.$storage.getObject(this.$options.name)
            if (query) {
                this.listQuery = query;
            }
            this.initData();
            this.getList();
        },
        methods: {
            // 初始化数据
            initData() {
                // // 活动时间
                // this.$axios.get("/dict/list", {params: {type: 22, limit: 99}}).then(data => {
                //     this.timeList = data.data.list;
                // })
                //
                // // 活动城市
                // this.$axios.get("/dict/list", {params: {type: 23, limit: 99}}).then(data => {
                //     this.cityList = data.data.list;
                // })
            },

            // 选择活动状态
            onActivityStatus(status) {
                this.listQuery.status = status.id;
                this.handleFilter();
            },

            // 点击活动，查看活动详情
            onItem(activity) {
                this.$router.push(`/activity/${activity.id}`);
            },

            // 条件改变
            handleFilter() {
                this.listQuery.page = 1;
                this.getList();
            },

            // 加载数据
            getList() {
                this.$storage.setData(this.$options.name, this.listQuery);
                this.loading = true;
                this.$axios.get("/activity/list", {params: this.listQuery}).then(response => {
                    this.loading = false;
                    this.dataList = response.data.list;
                    this.total = response.data.total;
                });
            },


        }
    };
</script>

<style>
    .option {
        padding: 0;
        background: #ECEFF1;
        box-shadow: 0 2px 4px 0 rgba(0, 0, 0, 0.26);
        border-radius: 9px;
    }

    .order-item {
        background: red;
    }
</style>

<style lang="scss" scoped>
    .app-container {
        width: 1200px !important;
        padding: 24px 30px 50px;
        background: #FFFFFF;
        box-shadow: 0 4px 16px 3px rgba(191, 199, 215, 0.31);

        .title {
            font-size: 30px;
            color: #333333;
            line-height: 42px;
        }

        .type-container {
            margin-top: 20px;
            display: flex;
            align-items: center;
            justify-content: space-between;

            .text {
                flex: 1;
                font-size: 18px;
                color: #333333;
                line-height: 47px;
                text-align: center;
                border-bottom: 3px transparent solid;
                cursor: pointer;
            }

            .select {
                color: #4895EF;
                border-bottom: 3px #4895EF solid;
            }

            .publish-button {
                width: 99px;
                height: 31px;
                background: #4895EF;
                border-radius: 21px;
                font-size: 14px;
                color: #FFFFFF;
                line-height: 31px;
                text-align: center;
                cursor: pointer;
                margin-bottom: 6px;
                margin-left: 30px;
            }

            .sort-container {
                margin-left: 18px;
                cursor: pointer;
                margin-bottom: 10px;

                .sort {
                    width: 17px;
                    height: 19px;
                }
            }
        }

        .line {
            background: #B0BEC5;
            width: 100%;
            height: 1.5px;
            box-shadow: 0px 4px 16px 3px rgba(191, 199, 215, 0.31);
            margin-top: -2px;
        }
    }

</style>
