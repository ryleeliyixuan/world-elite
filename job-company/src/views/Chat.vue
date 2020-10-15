<template>
    <div class="app-container">
        <div class="chat-container">
            <div class="user-list">
                <div style="padding-right: 20px">
                    <el-input
                            placeholder="搜索联系人"
                            suffix-icon="el-icon-search"
                            v-model="searchDate">
                    </el-input>
                </div>

                <div class="top-button-container">
                    <div class="filter-main-container">
                        <el-button type="primary" size="mini">筛选</el-button>
                        <el-card class="filter-container">
                            <div class="filter-title-container">
                                <img src="../assets/close.png" alt="" class="filter-close">
                            </div>

                            <div class="check-container">
                                <el-checkbox v-model="watched">只看未读</el-checkbox>
                                <el-checkbox v-model="apply">只看已投递</el-checkbox>
                            </div>

                            <div class="search-input-container">
                                <el-autocomplete
                                        class="inline-input"
                                        v-model="state2"
                                        :fetch-suggestions="querySearch"
                                        placeholder="请输入内容"
                                        :trigger-on-focus="false"
                                        @select="handleSelect"
                                ></el-autocomplete>
                            </div>




                        </el-card>
                    </div>
                    <el-button type="primary" size="mini"  icon="el-icon-s-fold" style="margin-left: 20px" @click="onManage">
                        {{manage?'完成':'管理'}}</el-button>

                </div>
                <el-scrollbar class="friend-container"  wrap-style="overflow: hidden auto; padding-right: 40px;">
                    <div class="friend-item" v-for="item in userList" >
                        <el-checkbox v-model="item.checked" class="friends-checked" v-if="manage"></el-checkbox>
                        <div style="width: 40px;height: 40px;position: relative">
                            <img :src="item.avatar" alt="" style="width: 40px;height: 40px">
                            <span class="circle-number" v-if="item.news!=0">{{item.news}}</span>
                        </div>

                            <div class="friend-right-container">
                                <div class="top-content">
                                    <div class="friend-name">{{item.name}}</div>
                                    <div class="friend-title" style="font-size: 8px">{{item.title}}</div>
                                    <div class="friend-title">{{item.time}}</div>
                                </div>
                                <div class="bottom-content">
                                    <div class="friend-title">{{item.message}}</div>
                                    <div class="friend-state">{{item.state}}</div>
                                </div>
                            </div>
                    </div>
                </el-scrollbar>
                <div class="select-container" >
                    <el-button type="danger" size="mini" v-if="manage">删除</el-button>
                    <el-button type="primary" size="mini" v-if="manage">已读</el-button>
                    <el-button type="primary" size="mini" v-if="manage">未读</el-button>
                </div>
            </div>
            <div class="chat-detail">
                <div class="detail-title">
                    <span class="title-name">Alice</span> 10分钟前活跃
                    <span class="see-resume">查看人才信息</span>
                </div>
                <el-scrollbar class="content-container" wrap-style="overflow: hidden auto; padding-right: 40px;">
                    <el-card class="box-card">
                        <img src="../assets/demo.jpg" alt="" style="width: 60px;height: 60px">
                        <div class="card-right">
                            <div><span class="item-card" style="font-size: 18px;color:#303133">Alice</span><span class="item-card">2020年毕业</span><span class="item-card">本科</span><span class="item-card">22岁</span><span class="item-card">10k-13k</span></div>
                            <div><span class="item-card">南加州大学</span><span class="item-card">计算机科学</span><span class="item-card">2020.05</span></div>
                        </div>
                    </el-card>
                    <div class="seeker-container">
                        <img src="../assets/demo.jpg" alt="" style="width: 40px;height: 40px">
                        <div class="seeker-word">您好</div>
                    </div>
                    <div class="hr-container">
                        <div class="hr-inner-container">
                            <img src="../assets/demo.jpg" alt="" style="width: 40px;height: 40px">
                            <div class="hr-word">您好
                                <img src="../assets/arrow.png" alt="" class="hr-arrow">
                            </div>
                        </div>

                    </div>
                </el-scrollbar>
                <div class="operating-container">
                    <div class="icon-container">
                        <img src="../assets/invite.png" alt="" class="icon-style">
                        <span class="icon-text">邀请面试</span>
                        <img src="../assets/annex.png" alt="" class="icon-style">
                        <span class="icon-text">附件</span>
                        <img src="../assets/word.png" alt="" class="icon-style">
                        <span class="icon-text">常用语</span>
                        <div class="report-container">
                            <img src="../assets/report.png" alt="" class="report-icon">
                            <span class="report-text">举报</span>
                            <img src="../assets/black.png" alt="" class="report-icon">
                            <span class="report-text">拉黑</span>
                        </div>
                    </div>
                    <div class="input-container">
                        <el-input
                                type="textarea"
                                :rows="2"
                                placeholder="请输入内容"
                                v-model="textarea">
                        </el-input>
                        <el-button type="primary">发送</el-button>

                    </div>

                </div>

            </div>
        </div>
    </div>

</template>

<script>
    export default {
        name: "Chat",
        data() {
            return {
                userList: [{name: "alice", avatar: require("../assets/demo.jpg"), title: "人力资源专员", time: "2019-7-5", state: '未投', message: '您好',checked:false,news:'1'},
                    {name: "alice", avatar: require("../assets/demo.jpg"), title: "人力资源专员", time: "前天8:00", state: '已投', message: '您好',checked:false,news:'1'},
                    {name: "alice", avatar: require("../assets/demo.jpg"), title: "人力资源专员", time: "前天9:00", state: '未投', message: '您好',checked:false,news:'0'},
                    {name: "alice", avatar: require("../assets/demo.jpg"), title: "人力资源专员", time: "前天10:00", state: '未投', message: '您好',checked:false,news:'1'},
                    {name: "alice", avatar: require("../assets/demo.jpg"), title: "人力资源专员", time: "前天10:00", state: '未投', message: '您好',checked:false,news:'1'},
                    {name: "alice", avatar: require("../assets/demo.jpg"), title: "人力资源专员", time: "前天10:00", state: '未投', message: '您好',checked:false,news:'1'},
                    {name: "alice", avatar: require("../assets/demo.jpg"), title: "人力资源专员", time: "前天10:00", state: '未投', message: '您好',checked:false,news:'0'},
                    {name: "alice", avatar: require("../assets/demo.jpg"), title: "人力资源专员", time: "前天10:00", state: '未投', message: '您好',checked:false,news:'1'},
                    {name: "alice", avatar: require("../assets/demo.jpg"), title: "人力资源专员", time: "前天10:00", state: '未投', message: '您好',checked:false,news:'1'},
                    {name: "alice", avatar: require("../assets/demo.jpg"), title: "人力资源专员", time: "前天10:00", state: '未投', message: '您好',checked:false,news:'0'},
                    {name: "alice", avatar: require("../assets/demo.jpg"), title: "人力资源专员", time: "前天10:00", state: '未投', message: '您好',checked:false,news:'1'},
                    {name: "alice", avatar: require("../assets/demo.jpg"), title: "人力资源专员", time: "前天10:00", state: '未投', message: '您好',checked:false,news:'1'},
                    {name: "alice", avatar: require("../assets/demo.jpg"), title: "人力资源专员", time: "前天10:00", state: '未投', message: '您好',checked:false,news:'1'},
                    {name: "alice", avatar: require("../assets/demo.jpg"), title: "人力资源专员", time: "前天10:00", state: '未投', message: '您好',checked:false,news:'1'},
                ],
                searchDate: '',//搜索内容
                textarea:'',//输入文本内容
                manage:false,//选择
                watched:false,//只看未读
                apply:false,//只看已投递
                state2:'',

            };
        },

        methods: {
            onManage(){
                this.manage=!this.manage
            },
        }
    }
</script>

<style scoped lang="scss">
    .app-container {
        min-height: calc(100vh - 430px);
    }

    .chat-container {
        width: 1200px;
        height: auto;
        margin: 0 auto;
        display: flex;
        align-self: center;
        justify-content: flex-start;
        margin-top: 50px;
    }

    .user-list {
        width: 32%;
        height: 100%;
        background-color: rgba(215, 215, 215, 0.6);
        padding: 20px 0 0 20px !important;
    }

    .chat-detail {
        width:68%;
        height:auto;
        background-color: rgba(242, 242, 242, 1);
        border: 2px solid rgb(242, 242, 242);
    }

    .top-button-container{
        width: 100%;
        display: flex;
        align-items: center;
        margin-top: 20px;
        position: relative;
    }
    .filter-main-container{
        position: relative;
    }
    .filter-container{
        position: absolute;
        width: 240px;
        height: 240px;
        top:40px;
        left: 0;
        z-index: 999;
    }
    .filter-container /deep/.el-card__body{
        width: 240px;
        height: auto;
        align-items: flex-start;
       flex-wrap: wrap;
        padding: 10px 15px;
    }
    .filter-title-container{
        width: 100%;
        height: 20px;
       text-align: right;
    }
    .filter-close{
        width: 20px;
        height: 20px;
    }
    .friend-container{
        width: 100%;
        height: 500px;
        margin-top: 20px;
        overflow: auto;
    }
    .friend-container::-webkit-scrollbar {
       display: none;
   }

    ::v-deep .el-scrollbar__wrap::-webkit-scrollbar {
        display: none;
    }


    .friend-item{
        width: 100%;
        height: 50px;
        display: flex;
        align-items: center;
        justify-content: space-between;
        border-bottom: 1px solid white;
        padding-bottom: 8px;
        padding-top: 8px;

    }
    .item-container{
        width: 100%;
        height: 200px;
    }
    .friend-right-container{
        width: 84%;
        height: 40px;
        margin-left: 4px;

    }
    .top-content{
        width: 100%;
        height: 25px;
        display: flex;
        align-items: center;
        justify-content: space-between;
        font-size: 10px;
    }
    .bottom-content{
        width: 95%;
        height: 20px;
        display: flex;
        align-items: center;
        justify-content: space-between;
        font-size: 10px;
    }
    .friend-name{
        font-size: 20px;
        color: #5C6984;
    }
    .friend-title {
        color: #909399;
        font-size: 12px;
    }
    .friend-state{
        color: #606266;
    }
    .detail-title{
        width: 100%;
        height: 50px;
        line-height: 50px;
        text-align: center;
        font-size: 10px;
        color: #909399;
        position: relative;
    }
    .content-container{
        width: 100%;
        height: 500px;
        background-color: #fff;
        padding: 20px 20px 0 20px;
    }
    .title-name{
        font-size: 18px;
        color: #169BD5;
        font-weight: bold;
        margin-right: 5px;
    }
    .see-resume{
        color:#169BD5;
        position: absolute;
        right: 30px;
        top: 50%;
        transform: translateY(-50%);
    }
    .box-card{
        width: 500px;
        height: 100px;
        display: flex;
        align-items: center;
    }
  /deep/ .el-card__body{
      width: 500px;
      height: 100px;
      display: flex;
      align-items: center;

    }
    .card-right{
        width: 380px;
        height: 60px;
        margin-left: 20px;
        padding-top: 8px;
    }
    .item-card{
        margin-right: 8px;
        color: #606266;
        font-size: 14px;
    }
    .seeker-container{
        width: 40%;
        height: auto;
        display: flex;
        align-items: flex-start;
        margin-top: 20px;
    }
    .seeker-word{
        width: 90%;
        height: auto;
        padding: 8px 8px 0 20px;
        font-size: 14px;
        word-wrap:break-word;
        word-break:break-all;
    }
    .hr-container{
        width: 100%;
        height: auto;
        display: flex;
        flex-direction: row-reverse;
        align-items: flex-start;
    }
    .hr-inner-container{
        width: 40%;
        display: flex;
        height: auto;
        flex-direction: row-reverse;
        align-items: flex-start;
    }
    .hr-word{
        max-width: 90%;
        height: auto;
        padding: 8px;
        font-size: 14px;
        word-wrap:break-word;
        word-break:break-all;
        background-color: #95EC69;
        display: flex;
        flex-direction: row-reverse;
        margin-right: 20px;
        border-radius: 6px;
        position: relative;
    }
    .hr-arrow{
        width: 11px;
        position: absolute;
        top: 20px;
        right: -11px;
        z-index: 999;
    }
    .operating-container{
        width: 100%;
        height: auto;
        padding: 0 10px;

    }

    .icon-container{
        width: 100%;
        height: 40px;
        display: flex;
        align-items: center;
        position: relative;
    }
    .report-container{
        position: absolute;
        top: 50%;
        right: 30px;
        transform: translateY(-50%);
    }
    .icon-style{
        width: 18px;

    }
    .icon-text{
        font-size: 14px;
        color: #606266;
        margin-right: 10px;
    }
    .input-container{
        width: 100%;
        display: flex;
        align-items: center;
    }
    /deep/ .el-textarea__inner{
        height: 76px;
    }
    /deep/  .input-container .el-button{
        height: 76px;
        width: 60px;
        padding: 12px;
    }
    .report-container{
        display: flex;
        align-items: center;

    }
    .report-icon{
        width: 14px;
        margin-right: 4px;
    }
    .report-text{
        font-size: 14px;
        color: #909399;
        margin-right: 8px;
    }
    .friends-checked{
        margin-right: 6px;
        margin-top: 6px;
    }
    .select-container{
        width: 100%;
        height: 50px;
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 0 30px;
    }
    .circle-number{
        width: 12px;
        height: 12px;
        border-radius: 50%;
        background-color:#FF4F4A;
        position: absolute;
        right: -4px;
        top: -4px;
        color: #ffffff;
        font-size: 8px;
        text-align: center;
        line-height: 12px;
    }
    .check-container{
        width: 100%;
        height: 30px;
        display: flex;
        align-items: center;
        margin-top: 10px;
    }
    .search-input-container{
        width: 100%;
        height: auto;
    }


</style>
