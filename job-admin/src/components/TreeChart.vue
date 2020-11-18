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
import { faTemperatureHigh } from '@fortawesome/free-solid-svg-icons';

export default {
  name: "TreeChart",
  props: {
    //接受父组件传递来的数据
    items: {
      type: Array,
    },
  },
  data() {
    return {
      root: {
        name: "",
        label: {
          normal: {
            backgroundColor: "#1E90FF",
          },
        },
        children: [],
      },
    };
  },
  mounted() {
    this.tree();
  },
  methods: {
    tree() {
      this.root.children = this.items;
      this.root.name= this.rootName;
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