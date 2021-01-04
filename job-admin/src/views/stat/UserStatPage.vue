<template>
  <div class="app-container">
    <el-menu :default-active="listQuery.status" mode="horizontal" class="date-menu" @select="handleSelectTab">
      <el-menu-item index="week">近7天</el-menu-item>
      <el-menu-item index="month">近30天</el-menu-item>
      <el-menu-item index="specifyMonth">整月查看</el-menu-item>
      <el-menu-item index="specifyYear">整年查看</el-menu-item>
    </el-menu>

    <el-select
      v-model="specifyDate"
      filterable
      size="mini"
      class="date-select"
      v-if="isSpecify"
      @change="handleSelect"
    >
      <el-option
        v-if="isSpecifyYear"
        v-for="item in this.date.yearDates"
        :key="item"
        :label="item"
        :value="item"
      ></el-option>
      <el-option
        v-if="!isSpecifyYear"
        v-for="item in this.date.monthDates"
        :key="item"
        :label="item"
        :value="item"
      ></el-option>
    </el-select>

    <el-button @click="refresh" class="date-refresh" type="primary" icon="el-icon-refresh" size="mini">刷新</el-button>
<!--    <LineChart></LineChart>-->

    <div id="line" class="linechart-container" ref="chart"></div>

    <p class="table-title">用户数量表</p>
    <el-table
      :data="tableData"
      v-if="!this.isSpecifyYear"
      border
      highlight-current-row
      style="margin-top: 10px">
      <el-table-column prop="date"></el-table-column>
      <el-table-column prop="userTotalNum" label="用户总计增数"></el-table-column>
      <el-table-column prop="totalDayGrowthNum" label="用户总计当日增加数"></el-table-column>
      <el-table-column prop="totalDayGrowth" label="用户总计当日增长率"></el-table-column>
      <el-table-column prop="studentNum" label="学生用户增数"></el-table-column>
      <el-table-column prop="studentDayGrowthNum" label="学生用户当日增加数"></el-table-column>
      <el-table-column prop="studentDayGrowth" label="学生用户当日增长率"></el-table-column>
      <el-table-column prop="companyNum" label="企业用户增数"></el-table-column>
      <el-table-column prop="companyDayGrowthNum" label="企业用户当日增加数"></el-table-column>
      <el-table-column prop="companyDayGrowth" label="企业用户当日增长率"></el-table-column>
    </el-table>
    <el-table
      :data="tableData"
      v-if="this.isSpecifyYear"
      border
      highlight-current-row
      style="margin-top: 10px">
      <el-table-column prop="date"></el-table-column>
      <el-table-column prop="userTotalNum" label="用户总增数"></el-table-column>
      <el-table-column prop="totalMonthGrowthNum" label="用户总计当月增加数"></el-table-column>
      <el-table-column prop="totalMonthGrowth" label="用户总计当月增长率"></el-table-column>
      <el-table-column prop="studentNum" label="学生用户增数"></el-table-column>
      <el-table-column prop="studentMonthGrowthNum" label="学生用户当月增加数"></el-table-column>
      <el-table-column prop="studentMonthGrowth" label="学生用户当月增长率"></el-table-column>
      <el-table-column prop="companyNum" label="企业用户增数"></el-table-column>
      <el-table-column prop="companyMonthGrowthNum" label="企业用户当月增加数"></el-table-column>
      <el-table-column prop="companyMonthGrowth" label="企业用户当月增长率"></el-table-column>
    </el-table>
  </div>


</template>

<script>
// import LineChart from "@/components/LineChart";
import {
  getDateList,
  getMonthUserStat,
  getSpecifyMonthUserStat,
  getSpecifyYearUserStat,
  getWeekUserStat, refreshNoArg, refreshWithArg
} from "@/api/stat_api";
import echarts from "echarts/lib/echarts";
import "echarts/lib/chart/line";
import "echarts/lib/component/tooltip";


export default {
  name: "UserStatPage",
  components: {},
  data() {
    return {
      lastWeekURL: '/admin/stat/get-week-user-stat',
      lastMonthURL: '/admin/stat/get-month-user-stat',
      specialMonthURL: '/admin/stat/get-specify-month-user-stat',
      specialYearURL: '/admin/stat/get-specify-year-user-stat',
      date: {
        monthDates: [],
        yearDates: []
      },
      specifyDate: undefined,
      items: [],
      myChart: undefined,
      listQuery: {
        status: "week"
      },
      xAxis: [],
      isSpecify: false,
      isSpecifyYear: false,
      tableData: [],
      lastReqInfo: {
        url: this.lastWeekURL,
        param: undefined
      }
    }
  },
  beforeCreate() {

  },
  created() {
    this.getWeekList();
    this.getDate();
  },
  mounted() {
    this.line();
  },
  methods: {
    getDate() {
      getDateList().then(response => {
        this.date.monthDates = response.data.monthDates;
        this.date.yearDates = response.data.yearDates;
      });
    },
    getWeekList() {
      this.xAxis = this.getRecentX(7, true);
      getWeekUserStat().then(response => {
        this.items = response.data;
        this.formatData2Table(response.data);
        this.refreshECharts();
        this.saveReqInfo(this.lastWeekURL);
      });
    },
    getMonthList() {
      getMonthUserStat().then(response => {
        this.items = response.data;
        this.formatData2Table(response.data);
        this.refreshECharts();
        this.saveReqInfo(this.lastMonthURL);
      });
    },
    getSpecifyMonthList() {
      getSpecifyMonthUserStat(this.specifyDate).then(response => {
        this.items = response.data;
        this.formatData2Table(response.data);
        this.refreshECharts();
        this.saveReqInfo(this.specialMonthURL, this.specifyDate);
      });
    },
    getSpecifyYearList() {
      getSpecifyYearUserStat(this.specifyDate).then(response => {
        this.items = response.data;
        this.formatYearData2Table(response.data);
        this.refreshECharts();
        this.saveReqInfo(this.specialYearURL, this.specifyDate);
      });
    },
    refresh() {
      switch (this.lastReqInfo.url) {
        case this.lastWeekURL:
          this.getWeekList();
          break;
        case this.lastMonthURL:
          this.getMonthList();
          break;
        case this.specialMonthURL:
          this.getSpecifyMonthList();
          break;
        case this.specialYearURL:
          this.getSpecifyYearList();
          break;
        default:
          this.getWeekList();
      }
    },
    handleSelect() {
      if (this.isSpecifyYear) {
        this.getSpecifyYearList();
      } else {
        this.getSpecifyMonthList();
      }
    },
    handleSelectTab(tabIndex) {
      this.tabIndex = tabIndex;
      switch (tabIndex) {
        case "week":
          this.isSpecify = false;
          this.isSpecifyYear = false;
          this.xAxis = this.getRecentX(7, true);
          this.getWeekList();
          break;
        case "month":
          this.isSpecify = false;
          this.isSpecifyYear = false;
          this.xAxis = this.getRecentX(30, false);
          this.getMonthList(); // 这里异步发送请求，等到refreshECharts执行完了，请求才响应过来 axios是异步请求
          break;
        case "specifyMonth":
          this.isSpecify = true;
          this.isSpecifyYear = false;
          this.specifyDate = this.date.monthDates[0];  // undefined
          this.xAxis = this.getSpecifyMonthX(this.specifyDate); // 获取指定月份日期 单位：日
          this.getSpecifyMonthList();
          break;
        case "specifyYear":
          this.isSpecify = true;
          this.isSpecifyYear = true;
          this.xAxis = this.getYearX(); // 获取指定年份日期 单位：月
          this.specifyDate = this.date.yearDates[0]; // undefined
          this.getSpecifyYearList();
          break;
        default:
          this.getWeekList();
      }
    },
    getSpecifyMonthX(date) {
      let year = date.split("/")[0];
      let month = date.split("/")[1];
      const daysOfMonth = [];
      month = parseInt(month, 10);
      const lastDayOfMonth = new Date(year, month, 0).getDate();
      for (let i = 1; i <= lastDayOfMonth; i++) {
        if (i < 10) {
          daysOfMonth.push("0" + i); //判断是部否小于10，如果小于加0，，例如“01”
        } else {
          daysOfMonth.push(i+"");
        }
      }
      return daysOfMonth;
    },
    getYearX() {
      let yearX = [];
      for (let i = 1; i <= 12; i++) {
        yearX.push(i);
      }
      return yearX;
    },
    getRecentX(day, isWeek) {
      let weekX = [];
      for (let i = day - 1; i >= 0; i--) {
        weekX[i] = this.getDay(-i, isWeek);
      }
      return this.reverseArr(weekX);
    },
    getDay(day, isWeek){
      let today = new Date();
      let target_day_milliseconds=today.getTime() + 1000 * 60 * 60 * 24 * day;
      today.setTime(target_day_milliseconds);
      let tYear = today.getFullYear();
      let tMonth = today.getMonth();
      let tDate = today.getDate();
      tMonth = this.doHandleMonth(tMonth + 1);
      tDate = this.doHandleMonth(tDate);
      return isWeek ? tYear + "/" + tMonth + "/" + tDate : tDate;
    },
    doHandleMonth(month){
      let m = month;
      if(month.toString().length === 1){
        m = "0" + month;
      }
      return m;
    },
    reverseArr(xAxis){
      let reverseTemp;
      for(let i = 0; i < xAxis.length / 2; i++){
        reverseTemp = xAxis[i];
        xAxis[i] = xAxis[xAxis.length-1-i];
        xAxis[xAxis.length - 1 - i] = reverseTemp;
      }
      return xAxis;
    },
    formatData2Table(data) {
      this.tableData = [];
      for (let i = 0; i < data.userTotalNums.length; i++) {
        let item = {};
        item.date = this.xAxis[i];
        item.userTotalNum = data.userTotalNums[i];
        item.studentNum = data.studentNums[i];
        item.companyNum = data.companyNums[i];
        item.totalDayGrowthNum = data.totalDayGrowthNums[i];
        item.studentDayGrowthNum = data.studentDayGrowthNums[i];
        item.companyDayGrowthNum = data.companyDayGrowthNums[i];
        item.totalDayGrowth = data.totalDayGrowths[i] !== undefined ? data.totalDayGrowths[i] * 100 + "%" : "";
        item.studentDayGrowth = data.studentDayGrowths[i] !== undefined ? data.studentDayGrowths[i] * 100 + "%" : "";
        item.companyDayGrowth = data.companyDayGrowths[i] !== undefined ? data.companyDayGrowths[i] * 100 + "%" : "";
        this.tableData.push(item);
      }
    },
    formatYearData2Table(data) {
      this.tableData = [];
      for (let i = 0; i < data.userTotalNums.length; i++) {
        let item = {};
        item.date = this.xAxis[i];
        item.userTotalNum = data.userTotalNums[i];
        item.studentNum = data.studentNums[i];
        item.companyNum = data.companyNums[i];
        item.totalMonthGrowthNum = data.totalMonthGrowthNums[i];
        item.studentMonthGrowthNum = data.studentMonthGrowthNums[i];
        item.companyMonthGrowthNum = data.companyMonthGrowthNums[i];
        item.totalMonthGrowth = data.totalMonthGrowths[i] !== undefined ? data.totalMonthGrowths[i] * 100 + "%" : "";
        item.studentMonthGrowth = data.studentMonthGrowths[i] !== undefined ? data.studentMonthGrowths[i] * 100 + "%" : "";
        item.companyMonthGrowth = data.companyMonthGrowths[i] !== undefined ? data.companyMonthGrowths[i] * 100 + "%" : "";
        this.tableData.push(item);
      }
    },
    saveReqInfo(url, param) {
      this.lastReqInfo.url = url;
      this.lastReqInfo.param = param;
    },
    line() {
      let line = document.getElementById("line");
      if (line) {
        this.myChart = echarts.init(line);
        window.onresize = this.myChart.resize;
        this.myChart.setOption({
          title: {
            text: "用户数量趋势图",
            left: "center",
            textStyle: {
              fontWeight: "bold"
            },
          },
          tooltip: {
            trigger: "axis",
          },
          legend: {
            data: ['用户总数',"学生用户数","企业用户数"],
            icon: "rect",
            type: "scroll",
            selectorPosition: "end",
            orient: "horizontal",
            x: "center",
            y: "bottom"
          },
          grid: {
            width: "80%",
            left: "5%"
          },
          xAxis: {
            type: "category",
            boundaryGap: false,
            data: this.xAxis,
            axisLine: {
              lineStyle: {
                width:'1'
              }
            }
          },
          yAxis: {
            type: "value"
          },
          color: ['#027DB4', "#B5F7E3", "#81D3F8"],
          series: [
            {
              name: "用户总数",
              type: "line",
              data: this.items.userTotalNums
            },
            {
              name: "学生用户数",
              type: "line",
              data: this.items.studentNums
            },
            {
              name: "企业用户数",
              type: "line",
              data: this.items.companyNums
            }
          ]
        });
      } else {
        console.log("line:" + line);
      }
    },
    refreshECharts() {
      let option = this.myChart.getOption();
      option.xAxis[0].data = this.xAxis;
      option.series[0].data = this.items.userTotalNums;
      option.series[1].data = this.items.studentNums;
      option.series[2].data = this.items.companyNums;
      this.myChart.setOption(option);
    }
  }
}
</script>

<style scoped lang="scss">
  .app-container {
    display: block;
    position: relative;
    left: 50%;
    transform: translateX(-50%);

    .date-menu {
      display: inline-block;
      position: relative;
      left: 150px;
    }

    .table-title {
      position: relative;
      margin-top: 80px;
      font-weight: bold;
      text-align: center;
      font-size: 18px;
      right: 60px;
      color: #333333;
    }

    .table-title:before {
      content: '';
      height: 1px;
      width: 100%;
    }

    .date-select {
      width: 15%;
      display: inline-block;
      position: relative;
      bottom: 22px;
      left: 200px;
    }

    .date-refresh {
      position: relative;
      top: 15px;
      float: right;
    }

    .stat-table {
      width: 100%;
      font-size: 13px;
      font-weight: normal;
      color: red;
      margin-top: 20px;
    }

  }

  .linechart-container {
    width: 687px;
    height: 320px;
    position: relative;
    left: 150px;
    top: 30px;
  }

</style>
