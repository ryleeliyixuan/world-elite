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
      default() {
        //默认数据，没有数剧的情况下启用
        return [
          { year: "2015", value: 0 },
          { year: "2016", value: 0 },
          { year: "2018", value: 0 },
          { year: "2019", value: 0 },
          { year: "2020", value: 0 },
        ];
      },
    },
  },
  mounted() {
    //提炼接收到的数据
    let values = [];
    this.items.forEach((el) => {
      values.push(el.value);
    });
    let years = [];
    this.items.forEach((el) => {
      years.push(el.year);
    });

    const option = {
      //覆盖配置数据option
      tooltip: {},
      xAxis: {
        type: "category",
        data: years,
      },
      yAxis: {
        type: "value",
      },
      dataZoom: [
        {
          show: true,
          type: "slider",
          start: 50,
          end: 100,
          // fillerColor: "#F0F8FF",
        },
      ],
      series: [
        {
          data: values,
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
  height: 370px;
}
</style>