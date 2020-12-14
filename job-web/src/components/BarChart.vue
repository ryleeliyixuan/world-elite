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
      tooltip: {
        trigger: "item",
        position: "top",
      },
      legend: {
        orient: "horizontal",
        right: 36,
        top: 2,
        itemWidth: 36,
        itemHeight: 10,
        data: ["人数"],
      },
      xAxis: {
        type: "category",
        data: year,
        axisLine: {
          show: false,
        },
        axisTick: {
          //y轴刻度线
          show: false,
        },
      },
      yAxis: {
        type: "value",
        splitLine: {
          //网格线
          lineStyle: {
            type: "dashed", //设置网格线类型 dotted：虚线   solid:实线
          },
          show: true, //隐藏或显示
        },
        axisLine: {
          show: false,
        },
        axisTick: {
          //y轴刻度线
          show: false,
        },
      },
      dataZoom: [
        {
          show: true,
          type: "slider",
          showDataShadow: false,
          bottom: 10,
          height: 4,
          borderColor: "transparent",
          backgroundColor: "#6FB0FC",
          handleIcon:
            "M512 512m-208 0a6.5 6.5 0 1 0 416 0 6.5 6.5 0 1 0-416 0Z M512 192C335.264 192 192 335.264 192 512c0 176.736 143.264 320 320 320s320-143.264 320-320C832 335.264 688.736 192 512 192zM512 800c-159.072 0-288-128.928-288-288 0-159.072 128.928-288 288-288s288 128.928 288 288C800 671.072 671.072 800 512 800z",
          handleSize: 20,
          handleColor: "#0252D5",
          handleStyle: {
            shadowBlur: 6,
            shadowOffsetX: 1,
            shadowOffsetY: 2,
            shadowColor: "rgba(111, 176, 252, 0.59)",
          },
          labelFormatter: "",
        },
      ],
      series: [
        {
          name: "人数",
          data: number,
          type: "bar",
          backgroundStyle: {
            color: "rgba(220, 220, 220, 0.8)",
          },
          barWidth: 20, //柱图宽度
          itemStyle: {
            color: new echarts.graphic.LinearGradient(
              0,
              0,
              0,
              1, //4个参数用于配置渐变色的起止位置, 这4个参数依次对应右/下/左/上四个方位. 而0 0 0 1则代表渐变色从正上方开始
              [
                { offset: 0, color: "#6FB1FC" },
                { offset: 0.5, color: "#4364F7" },
                { offset: 1, color: "#0052D4" },
              ] //数组, 用于配置颜色的渐变过程. 每一项为一个对象, 包含offset和color两个参数. offset的范围是0 ~ 1, 用于表示位置
            ),
            barBorderRadius: [11, 11, 11, 11],
          },
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
  height: 410px;
}
</style>