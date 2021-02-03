<template>
    <el-pagination v-if="total>0"
        size="medium" class="pagination"
        layout="prev, pager, next, jumper"
        :total="total"
        :page-size.sync="pageSize"
        :current-page.sync="currentPage"
        :hide-on-single-page="true"
        @current-change="handleCurrentChange">
    </el-pagination>

</template>

<script>
    import {scrollTo} from '@/utils/scroll-to'

    export default {
        name: 'Pagination',
        data() {
            return {}
        },
        props: {
            total: {
                required: true,
                type: Number
            },
            page: {
                type: Number,
                default: 1
            },
            limit: {
                type: Number,
                default: 20
            },
        },
        computed: {
            currentPage: {
                get() {
                    return this.page
                },
                set(val) {
                    this.$emit('update:page', val)
                }
            },
            pageSize: {
                get() {
                    return this.limit
                },
                set(val) {
                    this.$emit('update:limit', val)
                }
            }
        },
        methods: {
            handleCurrentChange(val) {
                this.$emit('pagination', {page: val, limit: this.pageSize})
                scrollTo(0, 800, () => {
                });
            }
        }
    }
</script>

<style scoped lang="scss">
    .pagination {
        padding: 20px 0 40px;
        align-items: center;
        justify-content: center;

        ::v-deep .number, ::v-deep .more {
            width: 37px;
            height: 37px;
            border-radius: 50%;
            background: #EEEEEE;
            box-shadow: 0 5px 11px 0 #CCCCCC;
            line-height: 37px;
            text-align: center;
            margin: 0 6px;
            color: #999999;

            &.active {
                color: white;
                background: #4C90FC;
                box-shadow: 0 5px 11px 0 rgba(30, 150, 252, 0.5);
            }
        }

        ::v-deep .btn-prev, ::v-deep .btn-next {
            width: 37px;
            height: 37px;
            border-radius: 50%;
            background: #EEEEEE;
            box-shadow: 0 5px 11px 0 #CCCCCC;
            line-height: 37px;
            text-align: center;
            margin: 0 6px;
            color: #999999;
            padding: 0;

            & .el-icon {
                font-size: 18px;
            }
        }

        ::v-deep .el-pagination__jump {
            font-size: 18px;
            color: #999999;
            line-height: 25px;
            height: 37px;

            .el-input {
                width: 66px;
                height: 37px;
                margin: 0 6px;

                .el-input__inner {
                    width: 66px;
                    height: 37px;
                    font-size: 18px;
                }
            }
        }
    }

</style>
