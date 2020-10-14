<template>
    <div class="app-container">
        <div class="chat-container">
            <div class="user-list">
                <el-input
                        placeholder="搜索联系人"
                        suffix-icon="el-icon-search"
                        v-model="searchDate" class="search-container">
                </el-input>
                <div class="top-button-container">
                    <el-button type="primary" size="mini">筛选</el-button>
                    <el-button type="danger" size="mini" style="margin-left: 20px">完成管理</el-button>
                </div>
                <div class="friend-container">
                    <div class="friend-item" v-for="item in userList" >
                            <img :src="item.avatar" alt="" style="width: 40px;height: 40px">
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
                </div>
                <div>
                </div>

            </div>
            <div class="chat-detail">
                <div class="detail-title">
                    <span class="title-name">Alice</span> 10分钟前活跃
                    <span class="see-resume">查看人才信息</span>
                </div>
                <div class="content-container">

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
                userList: [{name: "alice", avatar: "../assets/demo.jpg", title: "人力资源专员", time: "2019-7-5", state: '未投', message: '您好'},
                    {name: "alice", avatar: "../assets/demo.jpg", title: "人力资源专员", time: "前天8:00", state: '已投', message: '您好'},
                    {name: "alice", avatar: "../assets/demo.jpg", title: "人力资源专员", time: "前天9:00", state: '未投', message: '您好'},
                    {name: "alice", avatar: "../assets/demo.jpg", title: "人力资源专员", time: "前天10:00", state: '未投', message: '您好'},
                    {name: "alice", avatar: "../assets/demo.jpg", title: "人力资源专员", time: "前天10:00", state: '未投', message: '您好'},
                    {name: "alice", avatar: "../assets/demo.jpg", title: "人力资源专员", time: "前天10:00", state: '未投', message: '您好'},
                    {name: "alice", avatar: "../assets/demo.jpg", title: "人力资源专员", time: "前天10:00", state: '未投', message: '您好'},
                    {name: "alice", avatar: "../assets/demo.jpg", title: "人力资源专员", time: "前天10:00", state: '未投', message: '您好'},
                    {name: "alice", avatar: "../assets/demo.jpg", title: "人力资源专员", time: "前天10:00", state: '未投', message: '您好'},
                    {name: "alice", avatar: "../assets/demo.jpg", title: "人力资源专员", time: "前天10:00", state: '未投', message: '您好'},
                    {name: "alice", avatar: "../assets/demo.jpg", title: "人力资源专员", time: "前天10:00", state: '未投', message: '您好'},
                    {name: "alice", avatar: "../assets/demo.jpg", title: "人力资源专员", time: "前天10:00", state: '未投', message: '您好'},
                    {name: "alice", avatar: "../assets/demo.jpg", title: "人力资源专员", time: "前天10:00", state: '未投', message: '您好'},
                    {name: "alice", avatar: "../assets/demo.jpg", title: "人力资源专员", time: "前天10:00", state: '未投', message: '您好'},
                ],
                searchDate: '',//搜索内容
            };
        },
    }
</script>

<style scoped>
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
        padding: 20px 20px 50px 20px !important;
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
    }
    .friend-container{
        width: 100%;
        height: 500px;
        margin-top: 20px;
        overflow: auto;
        padding-right: 10px;
    }
    .friend-item{
        width: 100%;
        height: 50px;
        display: flex;
        align-items: center;
        justify-content: space-between;
        border-bottom: 1px solid white;
        padding-bottom: 8px;

    }
    .item-container{
        width: 100%;
        height: 200px;
    }
    .friend-right-container{
        width: 83%;
        height: 40px;

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
        font-size: 12px;
        color: #909399;
    }
    .content-container{
        width: 100%;
        height: 500px;
        background-color: #fff;
    }
    .title-name{
        font-size: 18px;
        color: #169BD5;
        font-weight: bold;
        margin-right: 5px;
    }
</style>
