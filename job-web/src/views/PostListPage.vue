<template>
    <div class="app-container">
        <div class="section1-container" style="margin-bottom: 15px;">
            <el-select v-model="listQuery.cityIds"
                       multiple
                       filterable
                       clearable
                       placeholder="工作地点"
                       @change="handleFilter"
                       size="mini"
                       class="section1-select">
                <el-option v-for="item in cityOptions" :key="item.id" :label="item.name" :value="item.id"></el-option>
            </el-select>
            <el-select v-model="listQuery.salaryRangeIds"
                       multiple
                       filterable
                       clearable
                       placeholder="薪资"
                       @change="handleFilter"
                       size="mini"
                       class="section1-select">
                <el-option v-for="item in salaryRangeOptions" :key="item.id" :label="item.name"
                           :value="item.id"></el-option>
            </el-select>
            <el-select v-model="listQuery.educationIds"
                       multiple
                       filterable
                       clearable
                       placeholder="学历要求"
                       @change="handleFilter"
                       size="mini"
                       class="section1-select">
                <el-option v-for="item in educationOptions" :key="item.id" :label="item.name"
                           :value="item.id"></el-option>
            </el-select>
            <el-select v-model="listQuery.companyIndustryIds"
                       multiple
                       filterable
                       clearable
                       placeholder="岗位分类"
                       @change="handleFilter"
                       size="mini"
                       class="section1-select">
                <el-option v-for="item in companyIndustryOptions" :key="item.id" :label="item.name"
                           :value="item.id"></el-option>
            </el-select>
            <el-input style="width: auto;" v-model="queryStr" placeholder="请输入关键词" size="small"></el-input>
            <el-button type="primary" @click="handleSearch" size="mini" style="margin:0 25px; height: 30px;">搜索
            </el-button>
            <el-link @click="clearOptions" :underline="false" style="color: #b4bbc5;">清空搜索条件</el-link>
        </div>

        <div style="display: inline-block;">
            <el-tabs v-model="activeName" style="padding-left: 25px;">
                <el-tab-pane label="全部" name="first"></el-tab-pane>
                <el-tab-pane label="急招" name="second"></el-tab-pane>
                <el-tab-pane label="热招" name="third"></el-tab-pane>
                <el-tab-pane label="内推" name="fourth"></el-tab-pane>
                <el-tab-pane label="校招" name="five"></el-tab-pane>
                <el-tab-pane label="社招" name="six"></el-tab-pane>
            </el-tabs>
        </div>

        <div class="sort-options">
            <el-link @click="" :underline="false" style="color: #599EF8;">最新</el-link>
            /
            <el-link @click="" :underline="false">发布顺序</el-link>
        </div>

        <div class="section3-container">
            <!-- v-for="(company, index) in companyList" -->
            <!-- companyList[0].id -->
            <el-card v-for="i in 6"
                     class="post-item" style="display: inline-block;"
                     :body-style="{ padding: '0px' }"
                     :key="i">
                <!-- fit="contain"-->
                <el-image style="width: 100%;height: 210px;" :src="mockLogo"></el-image>
                <div class="type-logo-box">
                    <div class="type-logo school">校 招</div>
                    <div class="type-logo inner">内 推</div>
                    <div class="type-logo hot">热 招</div>
                    <div class="type-logo urgency">急 招</div>
                </div>
                <div class="mid" style="display: block">
                    <span class="post-name">游戏UI设计</span>
                    <span class="post-desc">下一款爆款游戏会是你设计的吗</span>
                    <div class="mid-2">
                        <span class="post-salary">6-12</span>
                        <span class="post-addr">北京</span>
                        <span class="post-degree">博士</span>
                    </div>
                </div>
                <span class="post-line"></span>
                <div class="bot">
                    <span class="hr-logo"><i style="font-size: 30px;" class="el-icon-user"></i></span>
                    <span class="recruiter">HR</span>
                </div>
            </el-card>
        </div>

        <div class="section2-container">
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
    import {searchJob} from "@/api/job_api";
    import Pagination from "@/components/Pagination";
    import {mapGetters} from "vuex";

    import {formatListQuery, parseListQuery} from "@/utils/common";
    import {getRecommendList} from "@/api/recommend_api";

    export default {
        name: "JobListPage",
        components: {Pagination},
        computed: {
            ...mapGetters(["keyword"])
        },
        data() {
            return {
                companyList: [{"address":"南京","candidateResumeCount":0,"category":{"id":3,"name":"后端开发","parentId":2,"position":1},"city":{"code":"320100","createTime":"2020-10-15 10:14:52","english":"Nanjing","firstChar":"N","id":320100,"lat":"32.05830027444024","lng":"118.7959997119601","name":"南京市","parentId":320000,"phoneCode":"025","pinyin":"nan jing shi","py":"NJS","shortName":"南京","type":1,"updateTime":"2020-10-15 10:27:26","zipCode":"210008"},"companyUser":{"avatar":"","company":{"completeProgress":0,"favoriteFlag":0,"fullName":"北京字节跳动科技有限公司","homepage":"https://www.bytedance.com/zh/","id":"1253552062077882368","industry":{"id":93,"name":"互联网","type":6,"value":"19"},"logo":"https://oss.myworldelite.com/pic/7f2919c915354502852ec339cb4e0481.png","name":"字节跳动","property":{"id":105,"name":"民营","type":7,"value":"2"},"scale":{"id":74,"name":"2000人以上","type":5,"value":"6"},"stage":{"id":139,"name":"已融资","type":4,"value":"9"},"synopsis":"最早将AI应用于移动互联网"},"createTime":"2020-05-27","depart":"","email":"dtqkxwhdynyrfeuisv@awdrt.net","gender":0,"id":1265849571601084416,"name":"字节跳动","phoneCode":"","post":"人力资源部","status":1,"subscribeFlag":1,"type":2,"userId":"1265849571601084416"},"creatorId":1265849571601084416,"depart":"","experience":{"id":156,"name":"不限","type":13,"value":"1"},"id":"1294957277431529472","industryTags":[],"interviewResumeCount":0,"jobType":{"id":107,"name":"全职","type":8,"value":"1"},"minDegree":{"id":2,"name":"本科及以上","type":1,"value":"1"},"name":"后端开发工程师-Data","newResumeCount":1,"recruitType":{"id":154,"name":"校招","type":12,"value":"1"},"salary":{"id":111,"name":"10K-15K","type":9,"value":"10-15"},"salaryMonths":0,"skillTags":[],"status":2,"time":"2020-08-16","totalResumeCount":1},{"address":"杭州","candidateResumeCount":0,"category":{"id":213,"name":"其他设计职位","parentId":212,"position":187},"city":{"code":"330100","createTime":"2020-10-15 10:14:53","english":"Hangzhou","firstChar":"H","id":330100,"lat":"30.24709986422286","lng":"120.2090000813704","name":"杭州市","parentId":330000,"phoneCode":"0571","pinyin":"hang zhou shi","py":"HZS","shortName":"杭州","type":1,"updateTime":"2020-10-15 10:27:35","zipCode":"310026"},"companyUser":{"avatar":"","company":{"completeProgress":0,"favoriteFlag":0,"fullName":"北京字节跳动科技有限公司","homepage":"https://www.bytedance.com/zh/","id":"1253552062077882368","industry":{"id":93,"name":"互联网","type":6,"value":"19"},"logo":"https://oss.myworldelite.com/pic/7f2919c915354502852ec339cb4e0481.png","name":"字节跳动","property":{"id":105,"name":"民营","type":7,"value":"2"},"scale":{"id":74,"name":"2000人以上","type":5,"value":"6"},"stage":{"id":139,"name":"已融资","type":4,"value":"9"},"synopsis":"最早将AI应用于移动互联网"},"createTime":"2020-05-27","depart":"","email":"dtqkxwhdynyrfeuisv@awdrt.net","gender":0,"id":1265849571601084416,"name":"字节跳动","phoneCode":"","post":"人力资源部","status":1,"subscribeFlag":1,"type":2,"userId":"1265849571601084416"},"creatorId":1265849571601084416,"depart":"","experience":{"id":156,"name":"不限","type":13,"value":"1"},"id":"1294954271319326720","industryTags":[],"interviewResumeCount":0,"jobType":{"id":108,"name":"实习","type":8,"value":"2"},"minDegree":{"id":2,"name":"本科及以上","type":1,"value":"1"},"name":"游戏AI音频实习生","newResumeCount":1,"recruitType":{"id":154,"name":"校招","type":12,"value":"1"},"salary":{"id":153,"name":"5K以下","type":9,"value":"0-5"},"salaryMonths":0,"skillTags":[],"status":2,"time":"2020-08-16","totalResumeCount":1},{"address":"上海","candidateResumeCount":0,"category":{"id":41,"name":"测试开发","parentId":36,"position":37},"city":{"code":"310000","createTime":"2020-10-15 10:14:52","english":"Shanghai","firstChar":"S","id":310000,"lat":"31.230000555064284","lng":"121.47399982458053","name":"上海市","parentId":0,"phoneCode":"","pinyin":"shang hai shi","py":"SHS","shortName":"上海","type":1,"updateTime":"2020-10-15 10:27:24","zipCode":""},"companyUser":{"avatar":"","company":{"completeProgress":0,"favoriteFlag":0,"fullName":"北京字节跳动科技有限公司","homepage":"https://www.bytedance.com/zh/","id":"1253552062077882368","industry":{"id":93,"name":"互联网","type":6,"value":"19"},"logo":"https://oss.myworldelite.com/pic/7f2919c915354502852ec339cb4e0481.png","name":"字节跳动","property":{"id":105,"name":"民营","type":7,"value":"2"},"scale":{"id":74,"name":"2000人以上","type":5,"value":"6"},"stage":{"id":139,"name":"已融资","type":4,"value":"9"},"synopsis":"最早将AI应用于移动互联网"},"createTime":"2020-05-27","depart":"","email":"dtqkxwhdynyrfeuisv@awdrt.net","gender":0,"id":1265849571601084416,"name":"字节跳动","phoneCode":"","post":"人力资源部","status":1,"subscribeFlag":1,"type":2,"userId":"1265849571601084416"},"creatorId":1265849571601084416,"depart":"","experience":{"id":156,"name":"不限","type":13,"value":"1"},"id":"1294941203617087488","industryTags":[],"interviewResumeCount":0,"jobType":{"id":108,"name":"实习","type":8,"value":"2"},"minDegree":{"id":2,"name":"本科及以上","type":1,"value":"1"},"name":"测试开发实习生-飞书视频会议","newResumeCount":0,"recruitType":{"id":154,"name":"校招","type":12,"value":"1"},"salary":{"id":153,"name":"5K以下","type":9,"value":"0-5"},"salaryMonths":0,"skillTags":[],"status":2,"time":"2020-08-16","totalResumeCount":0},{"address":"北京","candidateResumeCount":0,"category":{"id":5,"name":"C++","parentId":2,"position":3},"city":{"code":"110000","createTime":"2020-10-15 10:14:52","english":"Beijing","firstChar":"B","id":110000,"lat":"39.90459941931698","lng":"116.40700054364608","name":"北京市","parentId":0,"phoneCode":"","pinyin":"bei jing shi","py":"BJS","shortName":"北京","type":1,"updateTime":"2020-10-15 10:26:18","zipCode":""},"companyUser":{"avatar":"","company":{"completeProgress":0,"favoriteFlag":0,"fullName":"北京字节跳动科技有限公司","homepage":"https://www.bytedance.com/zh/","id":"1253552062077882368","industry":{"id":93,"name":"互联网","type":6,"value":"19"},"logo":"https://oss.myworldelite.com/pic/7f2919c915354502852ec339cb4e0481.png","name":"字节跳动","property":{"id":105,"name":"民营","type":7,"value":"2"},"scale":{"id":74,"name":"2000人以上","type":5,"value":"6"},"stage":{"id":139,"name":"已融资","type":4,"value":"9"},"synopsis":"最早将AI应用于移动互联网"},"createTime":"2020-05-27","depart":"","email":"dtqkxwhdynyrfeuisv@awdrt.net","gender":0,"id":1265849571601084416,"name":"字节跳动","phoneCode":"","post":"人力资源部","status":1,"subscribeFlag":1,"type":2,"userId":"1265849571601084416"},"creatorId":1265849571601084416,"depart":"","experience":{"id":156,"name":"不限","type":13,"value":"1"},"id":"1294936460647792640","industryTags":[],"interviewResumeCount":0,"jobType":{"id":107,"name":"全职","type":8,"value":"1"},"minDegree":{"id":2,"name":"本科及以上","type":1,"value":"1"},"name":"C++客户端开发工程师-视频架构","newResumeCount":1,"recruitType":{"id":154,"name":"校招","type":12,"value":"1"},"salary":{"id":111,"name":"10K-15K","type":9,"value":"10-15"},"salaryMonths":0,"skillTags":[],"status":2,"time":"2020-08-16","totalResumeCount":1},{"address":"杭州","candidateResumeCount":0,"category":{"id":140,"name":"其他技术职位","parentId":139,"position":125},"city":{"code":"330100","createTime":"2020-10-15 10:14:53","english":"Hangzhou","firstChar":"H","id":330100,"lat":"30.24709986422286","lng":"120.2090000813704","name":"杭州市","parentId":330000,"phoneCode":"0571","pinyin":"hang zhou shi","py":"HZS","shortName":"杭州","type":1,"updateTime":"2020-10-15 10:27:35","zipCode":"310026"},"companyUser":{"avatar":"","company":{"completeProgress":0,"favoriteFlag":0,"fullName":"北京字节跳动科技有限公司","homepage":"https://www.bytedance.com/zh/","id":"1253552062077882368","industry":{"id":93,"name":"互联网","type":6,"value":"19"},"logo":"https://oss.myworldelite.com/pic/7f2919c915354502852ec339cb4e0481.png","name":"字节跳动","property":{"id":105,"name":"民营","type":7,"value":"2"},"scale":{"id":74,"name":"2000人以上","type":5,"value":"6"},"stage":{"id":139,"name":"已融资","type":4,"value":"9"},"synopsis":"最早将AI应用于移动互联网"},"createTime":"2020-05-27","depart":"","email":"dtqkxwhdynyrfeuisv@awdrt.net","gender":0,"id":1265849571601084416,"name":"字节跳动","phoneCode":"","post":"人力资源部","status":1,"subscribeFlag":1,"type":2,"userId":"1265849571601084416"},"creatorId":1265849571601084416,"depart":"","experience":{"id":156,"name":"不限","type":13,"value":"1"},"id":"1294934722897313792","industryTags":[],"interviewResumeCount":0,"jobType":{"id":107,"name":"全职","type":8,"value":"1"},"minDegree":{"id":2,"name":"本科及以上","type":1,"value":"1"},"name":"游戏引擎开发工程师-朝夕光年(字节跳动游","newResumeCount":0,"recruitType":{"id":154,"name":"校招","type":12,"value":"1"},"salary":{"id":111,"name":"10K-15K","type":9,"value":"10-15"},"salaryMonths":0,"skillTags":[],"status":2,"time":"2020-08-16","totalResumeCount":0},{"address":"北京","candidateResumeCount":0,"category":{"id":143,"name":"产品经理","parentId":142,"position":126},"city":{"code":"110000","createTime":"2020-10-15 10:14:52","english":"Beijing","firstChar":"B","id":110000,"lat":"39.90459941931698","lng":"116.40700054364608","name":"北京市","parentId":0,"phoneCode":"","pinyin":"bei jing shi","py":"BJS","shortName":"北京","type":1,"updateTime":"2020-10-15 10:26:18","zipCode":""},"companyUser":{"avatar":"","company":{"completeProgress":0,"favoriteFlag":0,"fullName":"北京字节跳动科技有限公司","homepage":"https://www.bytedance.com/zh/","id":"1253552062077882368","industry":{"id":93,"name":"互联网","type":6,"value":"19"},"logo":"https://oss.myworldelite.com/pic/7f2919c915354502852ec339cb4e0481.png","name":"字节跳动","property":{"id":105,"name":"民营","type":7,"value":"2"},"scale":{"id":74,"name":"2000人以上","type":5,"value":"6"},"stage":{"id":139,"name":"已融资","type":4,"value":"9"},"synopsis":"最早将AI应用于移动互联网"},"createTime":"2020-05-27","depart":"","email":"dtqkxwhdynyrfeuisv@awdrt.net","gender":0,"id":1265849571601084416,"name":"字节跳动","phoneCode":"","post":"人力资源部","status":1,"subscribeFlag":1,"type":2,"userId":"1265849571601084416"},"creatorId":1265849571601084416,"depart":"","experience":{"id":156,"name":"不限","type":13,"value":"1"},"id":"1294600909809844224","industryTags":[],"interviewResumeCount":0,"jobType":{"id":107,"name":"全职","type":8,"value":"1"},"minDegree":{"id":2,"name":"本科及以上","type":1,"value":"1"},"name":"国际化策略产品经理","newResumeCount":0,"recruitType":{"id":154,"name":"校招","type":12,"value":"1"},"salary":{"id":153,"name":"5K以下","type":9,"value":"0-5"},"salaryMonths":0,"skillTags":[],"status":2,"time":"2020-08-15","totalResumeCount":0},{"address":"北京","candidateResumeCount":0,"category":{"id":143,"name":"产品经理","parentId":142,"position":126},"city":{"code":"110000","createTime":"2020-10-15 10:14:52","english":"Beijing","firstChar":"B","id":110000,"lat":"39.90459941931698","lng":"116.40700054364608","name":"北京市","parentId":0,"phoneCode":"","pinyin":"bei jing shi","py":"BJS","shortName":"北京","type":1,"updateTime":"2020-10-15 10:26:18","zipCode":""},"companyUser":{"avatar":"","company":{"completeProgress":0,"favoriteFlag":0,"fullName":"北京字节跳动科技有限公司","homepage":"https://www.bytedance.com/zh/","id":"1253552062077882368","industry":{"id":93,"name":"互联网","type":6,"value":"19"},"logo":"https://oss.myworldelite.com/pic/7f2919c915354502852ec339cb4e0481.png","name":"字节跳动","property":{"id":105,"name":"民营","type":7,"value":"2"},"scale":{"id":74,"name":"2000人以上","type":5,"value":"6"},"stage":{"id":139,"name":"已融资","type":4,"value":"9"},"synopsis":"最早将AI应用于移动互联网"},"createTime":"2020-05-27","depart":"","email":"dtqkxwhdynyrfeuisv@awdrt.net","gender":0,"id":1265849571601084416,"name":"字节跳动","phoneCode":"","post":"人力资源部","status":1,"subscribeFlag":1,"type":2,"userId":"1265849571601084416"},"creatorId":1265849571601084416,"depart":"","experience":{"id":156,"name":"不限","type":13,"value":"1"},"id":"1294590214141706240","industryTags":[],"interviewResumeCount":0,"jobType":{"id":107,"name":"全职","type":8,"value":"1"},"minDegree":{"id":2,"name":"本科及以上","type":1,"value":"1"},"name":"策略产品经理-西瓜视频","newResumeCount":0,"recruitType":{"id":154,"name":"校招","type":12,"value":"1"},"salary":{"id":111,"name":"10K-15K","type":9,"value":"10-15"},"salaryMonths":0,"skillTags":[],"status":2,"time":"2020-08-15","totalResumeCount":0},{"address":"北京","candidateResumeCount":0,"category":{"id":143,"name":"产品经理","parentId":142,"position":126},"city":{"code":"110000","createTime":"2020-10-15 10:14:52","english":"Beijing","firstChar":"B","id":110000,"lat":"39.90459941931698","lng":"116.40700054364608","name":"北京市","parentId":0,"phoneCode":"","pinyin":"bei jing shi","py":"BJS","shortName":"北京","type":1,"updateTime":"2020-10-15 10:26:18","zipCode":""},"companyUser":{"avatar":"","company":{"completeProgress":0,"favoriteFlag":0,"fullName":"北京字节跳动科技有限公司","homepage":"https://www.bytedance.com/zh/","id":"1253552062077882368","industry":{"id":93,"name":"互联网","type":6,"value":"19"},"logo":"https://oss.myworldelite.com/pic/7f2919c915354502852ec339cb4e0481.png","name":"字节跳动","property":{"id":105,"name":"民营","type":7,"value":"2"},"scale":{"id":74,"name":"2000人以上","type":5,"value":"6"},"stage":{"id":139,"name":"已融资","type":4,"value":"9"},"synopsis":"最早将AI应用于移动互联网"},"createTime":"2020-05-27","depart":"","email":"dtqkxwhdynyrfeuisv@awdrt.net","gender":0,"id":1265849571601084416,"name":"字节跳动","phoneCode":"","post":"人力资源部","status":1,"subscribeFlag":1,"type":2,"userId":"1265849571601084416"},"creatorId":1265849571601084416,"depart":"","experience":{"id":156,"name":"不限","type":13,"value":"1"},"id":"1294587161468534784","industryTags":[],"interviewResumeCount":0,"jobType":{"id":107,"name":"全职","type":8,"value":"1"},"minDegree":{"id":2,"name":"本科及以上","type":1,"value":"1"},"name":"APP产品经理","newResumeCount":0,"recruitType":{"id":154,"name":"校招","type":12,"value":"1"},"salary":{"id":111,"name":"10K-15K","type":9,"value":"10-15"},"salaryMonths":0,"skillTags":[],"status":2,"time":"2020-08-15","totalResumeCount":0},{"address":"北京","candidateResumeCount":0,"category":{"id":273,"name":"市场策划","parentId":270,"position":238},"city":{"code":"110000","createTime":"2020-10-15 10:14:52","english":"Beijing","firstChar":"B","id":110000,"lat":"39.90459941931698","lng":"116.40700054364608","name":"北京市","parentId":0,"phoneCode":"","pinyin":"bei jing shi","py":"BJS","shortName":"北京","type":1,"updateTime":"2020-10-15 10:26:18","zipCode":""},"companyUser":{"avatar":"","company":{"completeProgress":0,"favoriteFlag":0,"fullName":"北京字节跳动科技有限公司","homepage":"https://www.bytedance.com/zh/","id":"1253552062077882368","industry":{"id":93,"name":"互联网","type":6,"value":"19"},"logo":"https://oss.myworldelite.com/pic/7f2919c915354502852ec339cb4e0481.png","name":"字节跳动","property":{"id":105,"name":"民营","type":7,"value":"2"},"scale":{"id":74,"name":"2000人以上","type":5,"value":"6"},"stage":{"id":139,"name":"已融资","type":4,"value":"9"},"synopsis":"最早将AI应用于移动互联网"},"createTime":"2020-05-27","depart":"","email":"dtqkxwhdynyrfeuisv@awdrt.net","gender":0,"id":1265849571601084416,"name":"字节跳动","phoneCode":"","post":"人力资源部","status":1,"subscribeFlag":1,"type":2,"userId":"1265849571601084416"},"creatorId":1265849571601084416,"depart":"","experience":{"id":156,"name":"不限","type":13,"value":"1"},"id":"1294586995877412864","industryTags":[],"interviewResumeCount":0,"jobType":{"id":108,"name":"实习","type":8,"value":"2"},"minDegree":{"id":1,"name":"专科及以上","type":1,"value":"0"},"name":"市场研究实习生","newResumeCount":0,"recruitType":{"id":154,"name":"校招","type":12,"value":"1"},"salary":{"id":153,"name":"5K以下","type":9,"value":"0-5"},"salaryMonths":0,"skillTags":[],"status":2,"time":"2020-08-15","totalResumeCount":0},{"address":"北京","candidateResumeCount":0,"category":{"id":217,"name":"运营","parentId":215,"position":189},"city":{"code":"110000","createTime":"2020-10-15 10:14:52","english":"Beijing","firstChar":"B","id":110000,"lat":"39.90459941931698","lng":"116.40700054364608","name":"北京市","parentId":0,"phoneCode":"","pinyin":"bei jing shi","py":"BJS","shortName":"北京","type":1,"updateTime":"2020-10-15 10:26:18","zipCode":""},"companyUser":{"avatar":"","company":{"completeProgress":0,"favoriteFlag":0,"fullName":"北京字节跳动科技有限公司","homepage":"https://www.bytedance.com/zh/","id":"1253552062077882368","industry":{"id":93,"name":"互联网","type":6,"value":"19"},"logo":"https://oss.myworldelite.com/pic/7f2919c915354502852ec339cb4e0481.png","name":"字节跳动","property":{"id":105,"name":"民营","type":7,"value":"2"},"scale":{"id":74,"name":"2000人以上","type":5,"value":"6"},"stage":{"id":139,"name":"已融资","type":4,"value":"9"},"synopsis":"最早将AI应用于移动互联网"},"createTime":"2020-05-27","depart":"","email":"dtqkxwhdynyrfeuisv@awdrt.net","gender":0,"id":1265849571601084416,"name":"字节跳动","phoneCode":"","post":"人力资源部","status":1,"subscribeFlag":1,"type":2,"userId":"1265849571601084416"},"creatorId":1265849571601084416,"depart":"","experience":{"id":156,"name":"不限","type":13,"value":"1"},"id":"1294586798409580544","industryTags":[],"interviewResumeCount":0,"jobType":{"id":108,"name":"实习","type":8,"value":"2"},"minDegree":{"id":1,"name":"专科及以上","type":1,"value":"0"},"name":"用户运营实习生——社群方向","newResumeCount":0,"recruitType":{"id":154,"name":"校招","type":12,"value":"1"},"salary":{"id":153,"name":"5K以下","type":9,"value":"0-5"},"salaryMonths":0,"skillTags":[],"status":2,"time":"2020-08-15","totalResumeCount":0}],
                mockLogo: "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1768115958,649887212&fm=26&gp=0.jpg",


                listQuery: {
                    keyword: "",
                    salaryRangeIds: [],
                    jobTypes: [],
                    cityIds: [],
                    companyIndustryIds: [],
                    companyScaleIds: [],
                    companyStageIds: [],
                    degreeIds: [],
                    categoryIds: [],
                    educationIds: [],
                    page: 1,
                    limit: 10
                },
                activeName: 'second',
                queryStr: '',
                total: 0,
                pageResult: {},
                cityOptions: [],
                salaryRangeOptions: [],
                companyScaleOptions: [],
                companyIndustryOptions: [],
                jobTypeOptions: [],
                educationOptions: [],
                showNoResult: false
            };
        },
        created() {
            this.initData();
            this.getList();
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
                listByType(1).then(
                    response => (this.educationOptions = response.data.list)
                );
                listByType(2).then(response => (this.cityOptions = response.data.list)).catch(function (err) {
                    console.log(err)
                });
                listByType(5).then(
                    response => (this.companyScaleOptions = response.data.list)
                );
                listByType(6).then(
                    response => (this.companyIndustryOptions = response.data.list)
                );
                listByType(8).then(
                    response => (this.jobTypeOptions = response.data.list)
                );
                listByType(9).then(
                    response => (this.salaryRangeOptions = response.data.list)
                );
            },
            handleFilter() {
                this.listQuery.page = 1;
                this.handleRouteList();
            },
            handleSearch() {
                if (this.isHomeListPage()) {
                    this.$store.commit("setting/SET_KEYWORD", this.queryStr);
                } else {
                    this.$router.push({
                        path: "/job-list",
                        query: {queryStr: this.queryStr}
                    });
                }
            },
            isHomeListPage() {
                const cur_path = this.$route.path;
                return cur_path === "/job-list" || cur_path === "/activity-list" || cur_path === "/wiki-card" || cur_path === "/wiki-list";
            },
            getList() {
                this.showNoResult = false;
                parseListQuery(this.$route.query, this.listQuery);
                searchJob(this.listQuery).then(response => {
                    if (!response.data.list || response.data.list.length === 0) {
                        this.showNoResult = true;
                        this.total = 10;
                        getRecommendList({
                            objectType: 1, // 职位
                            page: 1,
                            limit: 10,
                            sort: "+position"
                        }).then(response => {
                            this.pageResult.list = response.data.list.map(item => item.object);
                            console.log(1111111);
                            console.log(this.pageResult.list)
                            this.total = response.data.total;
                            this.$emit("complete");

                        });
                    } else {
                        console.log(2222222);
                        console.log(this.pageResult.list);
                        this.pageResult = response.data;
                        this.total = this.pageResult.total;
                        this.$emit("complete");
                    }
                });
            },
            handleRouteList() {
                this.$router.replace({
                    path: this.$route.path,
                    query: formatListQuery(this.listQuery)
                });
            },
            openJobDetail(id) {
                this.$router.push(`/job/${id}`);
            },
            clearOptions() {
                let options = document.getElementsByClassName(".el-select__tags");
                for (let i = 0; i < options.length; i++) {
                    options.item(i).remove();
                }
                this.queryStr = '';
                console.log('clearOptions');
            }
        }
    };

</script>


<style scoped lang="scss">
    .app-container {
        max-width: 1140px;
        margin: 0 auto;
        padding: 0 20px;
        min-height: calc(100vh - 477px);

        .section1-container {
            min-width: 335px;
            display: flex;
            flex-wrap: wrap;

            .section1-select {
                flex: 1;
                min-width: 130px;
                margin: 0 10px 10px;
                height: 100%;
            }
        }

        .section2-container {
            margin-top: 30px;
            width: 100%;
            overflow-x: auto;
            text-align: center;
        }

        .section3-container {

            display: flex;
            flex-wrap: wrap;

            .type-logo-box {
                float: right;
                position: relative;
                bottom: 200px;
                color: white;
                font-size: 12px;
                font-weight: bold;

                .type-logo {
                    margin-bottom: 4px;
                    width: 50px;
                    height: 20px;
                    line-height: 20px;
                    text-align: center;
                }


                .school {
                    background-color: #36A9CE;
                }

                .inner {
                    background-color: #70B603;
                }
                .hot {
                    background-color: #F59A23;
                }
                .urgency {
                    background-color: #D9001B;
                }

            }

            .bot {
                margin-top: 10px;

                .hr-logo {
                    display: inline-block;
                    font-size: 10px;
                    margin-left: 10px;
                }

                .recruiter {
                    display: inline-block;
                    font-size: 13px;
                    color: #CCCCCC;
                    position: relative;
                    left: 10px;
                    top: -5px;
                }
            }
            .mid:after {
                margin-top: 12px;
                content: '';
                height: 1px;
                display: block;
                width: 100%;
                background-color: #E4E4E4;
            }
            .mid-2 {
                color: #CCCCCC;
                font-size: 12px;
                margin-left: 15px;
            }
            .post-desc {
                display: block;
                font-size: 12px;
                color: #CCCCCC;
                margin-left: 15px;
                margin-bottom: 10px;
            }
            .post-name {
                display: block;
                font-weight: bold;
                font-size: 13px;
                margin-top: 5px;
                margin-bottom: 5px;
                margin-left: 15px;
                color: #757371;
            }
            .post-item {
                min-width: 250px;
                max-width: 250px;
                height: 350px;
                flex: 1;
                margin: 0 10px 25px;
            }
            .time {
                font-size: 13px;
                color: #999;
            }
            .bottom {
                margin-top: 13px;
                line-height: 12px;
            }
            .button {
                padding: 0;
                float: right;
            }
            .image {
                width: 100%;
                display: block;
            }
            .clearfix:before,
            .clearfix:after {
                display: table;
                content: "";
            }
            .clearfix:after {
                clear: both
            }
        }
    }

    ::-webkit-scrollbar {
        display: none;
    }

    @media screen and (max-width: 850px) {
        .app-container {
            .section3-container {
                .section3-item-container {
                    /deep/ .el-card__body {
                        display: flex;
                        flex-direction: column;
                        align-items: center;

                        .section3-left-container {
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
<style>
    .el-tabs__nav-wrap::after {
        opacity: 0;
    }

    .sort-options {
        display: inline-block;
        vertical-align: top;
        position: relative;
        top: 10px;
        left: 610px;
    }


</style>
