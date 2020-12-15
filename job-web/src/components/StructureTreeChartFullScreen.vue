<template>
  <div>
    <div id="structure-tree-fullscreen" class="treechart-container"></div>
  </div>
</template>

<script>
// 引入基本模板
import echarts from "echarts/lib/echarts";
import "echarts/lib/chart/tree";
// 引入提示框和图例组件
import "echarts/lib/component/tooltip";
import { faTemperatureHigh } from "@fortawesome/free-solid-svg-icons";

export default {
  name: "StructureTreeChartFullScreen",
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
      this.root.name = this.rootName;
      const tr = this.$echarts.init(document.getElementById("structure-tree-fullscreen"));
      window.onresize = tr.resize;
      tr.setOption({
        tooltip: {
          trigger: "item",
          triggerOn: "mousemove",
          formatter(params) {
            const item = params;
            return `
                ${item.data.name}
               `;
          },
        },
        series: [
          {
            name: "树状图",
            type: "tree",
            orient: "vertical",
            symbolSize: 43,
            symbol: "circle",
            top: 80,
            edgeShape: "curve",
            initialTreeDepth: 10,
            lineStyle: {
              color: "#F4C062",
              borderColor: "#F4C062",
            },
            label: {
              normal: {
                position: "bottom",
                verticalAlign: "bottom",
                align: "center",
                backgroundColor: "#F4C062",
                color: "#fff",
                shadowColor: "#F1CB85",
                shadowBlur: 15,
                width: 30,
                height: 50,
                padding: [32, 10, 32, 10],
                borderRadius: 100,
                fontWeight: 500,

                formatter: function (v) {
                  // console.log("--------", v);
                  var text = v.name;
                  const len = text.length;
                  var str = "";
                  if (len <= 6) {
                    str = text;
                  } else if (len > 6 && len < 10) {
                    str = text.substr(0, 6) + "\n" + text.substr(6) + "...";
                  } else if (len >= 10) {
                    str = text.substr(0, 6) + "\n" + text.substr(6, 4) + "...";
                  }
                  return str;
                },
              },
            },
            leaves: {
              label: {
                normal: {
                  verticalAlign: "bottom",
                  align: "center",
                  backgroundColor: "#F4C062",

                  width: 30,
                  height: 50,
                  padding: [30, 20, 30, 20],
                  borderRadius: 100,
                  fontWeight: 500,

                  formatter: function (v) {
                    var text = v.name;
                    const len = text.length;
                    var str = "";
                    if (len <= 6) {
                      str = text;
                    } else if (len > 6 && len < 10) {
                      str = text.substr(0, 6) + "\n" + text.substr(6) + "...";
                    } else if (len >= 10) {
                      str =
                        text.substr(0, 6) + "\n" + text.substr(6, 4) + "...";
                    }
                    return str;
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
  height: 380px;
}
</style>