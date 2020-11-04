<template>
  <div id="barChart" class="barchart-container"></div>
</template>

<script>
// 引入基本模板
import echarts from "echarts/lib/echarts";
// 引入柱状图组件
import "echarts/lib/chart/bar";
// 引入提示框和图例组件
import "echarts/lib/component/tooltip";
import "echarts/lib/component/legend";

export default {
  name: "BarChart",
  props: {
    //接受父组件传递来的数据
    items: {
      type: Array,
    },
  },
  mounted() {
    //提炼接收到的数据
    let number = [];
    this.items.forEach((el) => {
      number.push(el.number);
    });
    let year = [];
    this.items.forEach((el) => {
      year.push(el.year);
    });

    const option = {
      //覆盖配置数据option
      tooltip: {},
      xAxis: {
        type: "category",
        data: year,
      },
      yAxis: {
        type: "value",
      },
      dataZoom: [
        {
          show: true,
          type: "slider",
          start: 0,
          end: 100,
        },
      ],
      series: [
        {
          data: number,
          type: "bar",
          showBackground: true,
          backgroundStyle: {
            color: "rgba(220, 220, 220, 0.8)",
          },
          itemStyle: { color: "#1E90FF" },
        },
      ],
    };
    //初始化
    const chartObj = echarts.init(document.getElementById("barChart"));
    window.onresize = chartObj.resize;
    chartObj.setOption(option);
  },
};
</script>
<style scoped>
.barchart-container {
  width: 100%;
  height: 370px;
}
</style>