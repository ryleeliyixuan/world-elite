<template>
  <div>
    <div id="tree" class="treechart-container"></div>
  </div>
</template>

<script>
// 引入基本模板
import echarts from "echarts/lib/echarts";
import "echarts/lib/chart/tree";
// 引入提示框和图例组件
import "echarts/lib/component/tooltip";

export default {
  name: "TreeChart",
  props: {
    //接受父组件传递来的数据
    items: {
      type: Array,
      default() {
        //默认数据，没有数剧的情况下启用
        return [
          //none
        ];
      },
    },
  },
  
  mounted() {
    this.tree();
  },
  methods: {
    tree() {
      const tr = this.$echarts.init(document.getElementById("tree"));
      window.onresize = tr.resize;
      tr.setOption({
        tooltip: {
          trigger: "item",
          formatter: "b",
        },
        series: [
          {
            name: "树状图",
            type: "tree",
            //排列方式，横向、纵向
            orient: "vertical",
            symbolSize: 1,
            edgeShape: "polyline",
            initialTreeDepth: 10,
            label: {
              normal: {
                position: "center",
                verticalAlign: "middle",
                align: "center",
                backgroundColor: "#00BFFF",
                color: "#fff",
                padding: 3,
                formatter: ["{box|{b}}"].join("\n"),
                rich: {
                  box: {
                    height: 20,
                    color: "#fff",
                    padding: [0, 5],
                    align: "center",
                  },
                },
              },
            },
            leaves: {
              label: {
                normal: {
                  position: "center",
                  verticalAlign: "middle",
                  align: "center",
                  backgroundColor: "#A9A9A9",
                  padding: 3,
                  formatter: ["{box|{b}}"].join("\n"),
                  rich: {
                    box: {
                      height: 20,
                      color: "#fff",
                      padding: [0, 5],
                      align: "center",
                    },
                  },
                },
              },
            },
            expandAndCollapse: true,
            animationDuration: 550,
            animationDurationUpdate: 750,
            data: this.items,
          },
        ],
      });
    },
  },
};
</script>

<style scoped>
.treechart-container {
  height: 400px;
}
</style>