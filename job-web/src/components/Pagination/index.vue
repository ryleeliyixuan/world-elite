<template>
    <div class="pagination-container">
        <el-pagination
                :background="background"
                :current-page.sync="currentPage"
                :page-size.sync="pageSize"
                :layout="layout"
                :page-sizes="pageSizes"
                :total="total"
                v-bind="$attrs"
                :small="small"
                :hide-on-single-page="hideOnSinglePage"
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
        />
    </div>

</template>

<script>
    import {scrollTo} from '@/utils/scroll-to'

    export default {
        name: 'Pagination',
        data() {
            return {
                background: false,
                layout: 'total, sizes, prev, pager, next, jumper',
                small: false,
                hideOnSinglePage: true
            }
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
            pageSizes: {
                type: Array,
                default() {
                    return [10, 20, 30, 50]
                }
            },

            autoScroll: {
                type: Boolean,
                default: true
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
        created() {
            this.handleWidthChange(window.innerWidth < 410);
            // window.onresize = () => {
            //     this.handleWidthChange(window.innerWidth < 410);
            // }
        },
        methods: {
            handleWidthChange(small) {
                this.background = !small;
                this.small = small;
                this.layout = small ? "prev, pager, next" : 'total, sizes, prev, pager, next, jumper';
            },
            handleSizeChange(val) {
                this.$emit('pagination', {page: this.currentPage, limit: val})
                if (this.autoScroll) {
                    scrollTo(0, 800)
                }
            },
            handleCurrentChange(val) {
                this.$emit('pagination', {page: val, limit: this.pageSize})
                if (this.autoScroll) {
                    scrollTo(0, 800)
                }
            }
        }
    }
</script>

<style scoped>
    .pagination-container {
        background: #fff;
        padding: 20px 0;
    }

    @media screen and (max-width: 410px) {
        .pagination-container {
            text-align: center;
        }
    }

</style>
