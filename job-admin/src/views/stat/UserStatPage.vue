<template>
  <div class="app-container">
    <el-menu :default-active="listQuery.status" mode="horizontal" class="date-menu" @select="handleSelectTab">
      <el-menu-item index="week">近7天</el-menu-item>
      <el-menu-item index="month">近30天</el-menu-item>
      <el-menu-item index="specifyMonth">整月查看</el-menu-item>
      <el-menu-item index="specifyYear">整年查看</el-menu-item>
    </el-menu>

    <el-select
      v-model="date"
      filterable
      size="mini"
      class="date-select"
      v-if="isSpecify"
    >
      <el-option
        :key="2020"
        :label="2020"
        :value="2020"
      ></el-option>
      <el-option
        :key="2019"
        :label="2019"
        :value="2019"
      ></el-option>
    </el-select>

    <el-button class="date-refresh" type="primary" icon="el-icon-refresh" size="mini">刷新</el-button>
<!--    <LineChart></LineChart>-->

    <div id="line" class="linechart-container" ref="chart"></div>

    <p class="table-title">用户数量表</p>
    <el-table
      :data="tableData"
      border
      highlight-current-row
      style="margin-top: 10px">
      <el-table-column prop="date"></el-table-column>
      <el-table-column prop="name" label="用户总数"></el-table-column>
      <el-table-column prop="address" label="总日增数"></el-table-column>
      <el-table-column prop="address" label="总日增率"></el-table-column>
      <el-table-column prop="address" label="学生用户数"></el-table-column>
      <el-table-column prop="address" label="学生日增数"></el-table-column>
      <el-table-column prop="address" label="学生日增率"></el-table-column>
      <el-table-column prop="address" label="企业用户数"></el-table-column>
      <el-table-column prop="address" label="企业日增数"></el-table-column>
      <el-table-column prop="address" label="企业日增率"></el-table-column>
    </el-table>
  </div>


</template>

<script>
// import LineChart from "@/components/LineChart";
import {getWeekUserStat} from "@/api/stat_api";
import echarts from "echarts/lib/echarts";
import "echarts/lib/chart/line";
import "echarts/lib/component/tooltip";


export default {
  name: "UserStatPage",
  components: {},
  data() {
    return {
      date: 2020,
      items: undefined,
      myChart: undefined,
      listQuery: {
        status: "week"
      },
      str: undefined,
      xAxis: [],
      isSpecify: false,
      tableData: [
        {
          date: '2016-05-02',
          name: '王小虎',
          address: '上海市'
        },
        {
          date: '2016-05-02',
          name: '王小虎',
          address: '上海市'
        },
        {
          date: '2016-05-02',
          name: '王小虎',
          address: '上海市'
        },
        {
          date: '2016-05-02',
          name: '王小虎',
          address: '上海市'
        }
      ]
    }
  },
  created() {
    this.getWeekList();
  },
  mounted() {
    this.line();
  },
  methods: {
    getWeekList() {
      this.xAxis = this.getRecentX(7);
      getWeekUserStat().then(response => {
        this.items = response.data;
        this.str = JSON.stringify(this.items);
        console.log("items: " + JSON.stringify(this.items));
      });
    },
    handleSelectTab(tabIndex) {
      this.tabIndex = tabIndex;
      switch (tabIndex) {
        case "week":
          this.xAxis = this.getRecentX(7);
          this.getWeekList();
          this.refreshECharts();
          break;
        case "month":
          this.xAxis = this.getRecentX(30);
          this.refreshECharts();
          console.log(this.xAxis);
          break;
        case "specifyMonth":
          console.log("choose specify month");
          break;
        case "specifyYear":
          console.log("choose specify year");
          break;
        default:
          this.getWeekList();
      }
    },
    getRecentX(day) {
      let weekX = [];
      for (let i = 0; i < day; i++) {
        weekX[i] = this.getDay(-i);
      }
      return weekX;
    },
    getDay(day){
      let today = new Date();
      let target_day_milliseconds=today.getTime() + 1000 * 60 * 60 * 24 * day;
      today.setTime(target_day_milliseconds);
      let tYear = today.getFullYear();
      let tMonth = today.getMonth();
      let tDate = today.getDate();
      tMonth = this.doHandleMonth(tMonth + 1);
      tDate = this.doHandleMonth(tDate);
      return tYear+"/"+tMonth+"/"+tDate;
    },
    doHandleMonth(month){
      let m = month;
      if(month.toString().length === 1){
        m = "0" + month;
      }
      return m;
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
              data: [4,6,4,7,7,12,8,3,9,15]
            },
            {
              name: "学生用户数",
              type: "line",
              data: [3,4,1,2,6,6,1,2,4,9]
            },
            {
              name: "企业用户数",
              type: "line",
              data: [1,2,3,5,1,6,7,1,5,6]
            }
          ]
        }, {notMerge: true});
      } else {
        console.log("line:" + line);
      }
    },
    refreshECharts() {
      let option = this.myChart.getOption();
      option.xAxis[0].data = this.xAxis;
      this.myChart.setOption(option, {notMerge: true});
      console.log("refreshECharts:" + option.xAxis[0].data);
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
      bottom: 20px;
      left: 450px;
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
