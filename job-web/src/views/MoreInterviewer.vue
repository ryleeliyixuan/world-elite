<template>
    <div class="app-container">
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/mock-interview' }">模拟面试</el-breadcrumb-item>
            <el-breadcrumb-item>面试官列表</el-breadcrumb-item>
        </el-breadcrumb>
        <div class="filter-container">
            <div class="filter-item">
                <div class="filter-title">行业类型:</div>
                <div class="select-item" v-for="item in industryList" @click="select1()" :key="item">{{item}}</div>

            </div>

        </div>

    </div>

</template>

<script>
    export default {
        name: "MoreInterviewer",
        data() {
            return{
                industryList:[{name:'不限',select:true},{name:'互联网',select:false},{name:'金融',select:false},{name:'咨询',select:false},{name:'媒体',select:false},{name:'房地产',select:false}],

            }
        },
    }
</script>

<style scoped lang="scss">
    .app-container{
        max-width: 1200px;
        margin: 0 auto;
        height: auto;
        padding: 20px;
        /deep/.el-breadcrumb{
            font-size: 18px;
        }
        .filter-container{
            width: 100%;
            .filter-item{
                width: 100%;
                display: flex;
                align-items: center;
                margin-top: 34px;
                .filter-title{
                    width: 120px;
                    font-size: 21px;
                    font-weight: 500;
                    color: #333333;
                }
                .select-item{
                    padding: 4px 30px;
                    background: #F5F5F5;
                    border-radius: 17px;
                    font-size: 18px;
                    color: #333333;
                    margin-right: 17px;
                }
            }

        }
    }

</style>
