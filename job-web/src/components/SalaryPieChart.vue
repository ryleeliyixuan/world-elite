<template>
  <div id="salaryPie" class="salary-pie-container">
    <!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
    <div
      id="main1"
      class="salaryPie"
      style="float: left; width: 100%; height: 500px"
    ></div>
  </div>
</template>


<script>
// 引入基本模板
import echarts from "echarts/lib/echarts";
// 引入柱状图组件
import "echarts/lib/chart/pie";
// 引入提示框和title组件
import "echarts/lib/component/tooltip";
import "echarts/lib/component/title";

export default {
  name: "SalaryPieChart",
  props: {
    //接受父组件传递来的数据
    items: {
      type: Array,
    },
    name: {
      type: String,
    },
  },
  mounted() {
    this.initData();
  },
  methods: {
    //初始化数据
    initData() {
      //提炼接收到的数据
      let salaryData = [];
      this.items.forEach((el) => {
        let salary = { name: el.salary, value: el.percent };
        salaryData.push(salary);
      });
      // 基于准备好的dom，初始化echarts实例
      var myChart = echarts.init(document.getElementById("main1"));
      window.onresize = myChart.resize;
      // 绘制图表
      myChart.setOption({
        title: {
          text: "薪资待遇分布图", //主标题
          padding: [100, 100, 100, 100],
          subtext: this.name, //副标题
          x: "center", //x轴方向对齐方式
          y: "top",
        },
        tooltip: {
          trigger: "item",
          formatter: "{a} <br/>{b} : {c} ({d}%)",
        },
        color: ["#2F54EB","#F5222D","#FA541C","#FFC53D","#95DE64","#00E5FF","#7C4DFF","#FA541C","#00E676","#1A237E"],
        series: [
          {
            name: "数据来源",
            type: "pie",
            radius: ["25%", "50%"],
            center: ["50%", "60%"],
            data: salaryData,
            itemStyle: {
              normal: {
                borderWidth: 4,
                borderColor: "#fff",
              },
            },
          },
        ],
      });
    },
  },
};
</script>

<style scoped lang="scss">
.salary-pie-container {
  height: 300px;
  ::v-deep canvas {
    height: 300px;
  }
}
</style>