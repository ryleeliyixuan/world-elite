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
import { faTemperatureHigh } from "@fortawesome/free-solid-svg-icons";

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
      this.root.name = this.rootName;
      const tr = this.$echarts.init(document.getElementById("tree"));
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
            edgeShape: "polyline",
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
                // width: 30,
                borderWidth:30,
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
                  position: "bottom",
                  verticalAlign: "bottom",
                  align: "center",
                  backgroundColor: "#F4C062",
                  color: "#fff",
                  shadowColor: "#F1CB85",
                  shadowBlur: 15,
                  borderWidth:20,
                  height: 50,
                  padding: [20, 8, 20, 8],
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