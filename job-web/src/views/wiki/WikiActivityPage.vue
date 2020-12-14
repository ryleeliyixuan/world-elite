<template>
  <div class="activity-container" v-if="activityPage">
    <div v-if="activityPage.list.length > 0">
      <el-tabs
        v-model="activeActivityName"
        type="card"
        @tab-click="handleTabClick"
      >
        <el-tab-pane label="全部" name="-1"></el-tab-pane>
        <el-tab-pane label="报名中" name="0"></el-tab-pane>
        <el-tab-pane label="进行中" name="1"></el-tab-pane>
        <el-tab-pane label="已结束" name="2"></el-tab-pane>
      </el-tabs>
      <div class="activity-item" v-if="activityList.length > 0">
        <el-row>
          <el-col
            :xs="24"
            :sm="12"
            :md="8"
            :lg="6"
            :xl="6"
            v-for="activity in activityList"
            :key="activity.id"
          >
            <a :href="activity.link" target="_blank">
              <el-card class="m-2" :body-style="{ padding: '0px' }">
                <div class="activity-item-thumbnail-wrapper">
                  <img
                    class="activity-item-thumbnail"
                    :src="activity.thumbnail"
                  />
                  <div class="activity-item-tag">
                    <div class="triangle warm-up" v-if="activity.stage == 0">
                      报名中
                    </div>
                    <div class="triangle on-stage" v-if="activity.stage == 1">
                      进行中
                    </div>
                    <div class="triangle off-stage" v-if="activity.stage == 2">
                      已结束
                    </div>
                  </div>
                </div>
                <div class="activity-item-title p-2">
                  {{ activity.title }}
                </div>
                <div
                  class="activity-item-detail text-secondary p-2"
                  style="font-size: 13px"
                >
                  <span
                    ><time class="time">{{ activity.startTime }}</time>
                    {{ activity.address }}
                  </span>
                  <a :href="activity.link" target="_blank">
                    <el-button type="text" size="mini" class="text-secondary"
                      >查看详情 <i class="el-icon-arrow-right"></i
                    ></el-button>
                  </a>
                </div>
              </el-card>
            </a>
          </el-col>
        </el-row>
      </div>
      <div v-else class="noInfoMsgBox">
        <svg-icon
          icon-class="activity-missing"
          style="height: 265px; width: 344px; margin-bottom: 17px"
        />
        <div>活动即将上线，敬请期待!</div>
      </div>
    </div>
    <div v-else class="noInfoMsgBox">
      <svg-icon
        icon-class="activity-missing"
        style="height: 265px; width: 344px; margin-bottom: 17px"
      />
      <div>活动即将上线，敬请期待!</div>
    </div>
  </div>
</template>

<script>
import Vue from "vue";
import VueAMap from "vue-amap";
import Pagination from "@/components/Pagination";
import { getActivityList } from "@/api/activity_api";
import { formatListQuery, parseListQuery } from "@/utils/common";

export default {
  name: "WikiActivityPage",
  components: { Pagination },
  data() {
    return {
      listQuery: {
        cityIds: [],
        keyword: undefined,
        companyId: undefined,
        status: "2",
        sort: "-id",
        page: 1,
        limit: 10,
      },
      companyName: undefined,
      activityPage: { list: [] },
      activityList: [],
      activeActivityName: "全部",
    };
  },
  created() {
    this.initData();
  },
  watch: {
    $route() {
      this.initData();
    },
  },
  methods: {
    initData() {
      // parseListQuery(this.$route.query, this.listQuery);
      const id = this.$route.params.id;
      this.companyName = this.$route.params.name;
      // console.log("params", this.$route.params.name);
      this.listQuery.companyId = id;
      getActivityList(this.listQuery).then((response) => {
        let temp_list = response.data.list;
        let actual_list = [];
        let today = new Date();
        let today_year = today.getFullYear();
        let today_month = "" + today.getMonth() + 1;
        let today_date = "" + today.getDate();
        if (today_month.length < 2) {
          today_month = "0" + today_month;
        }
        if (today_date.length < 2) {
          today_date = "0" + today_date;
        }
        //filter activities based on organizer, getting the actual list
        for (let i = 0; i < temp_list.length; i++) {
          let activity = temp_list[i];
          if (activity.organizer == this.companyName) {
            let activity_year = activity.startTime.substring(0, 4);
            let activity_month = activity.startTime.substring(5, 7);
            let activity_date = activity.startTime.substring(8, 10);
            if (activity_year == today_year) {
              activity.startTime = activity.startTime.substring(0, 10);
              activity.link = "/activity/" + activity.id;
              //inspect status of activities + categorize
              let timeline = [
                activity.curTime,
                activity.startTime,
                activity.finishTime,
              ];
              timeline.sort(function (a, b) {
                let aa = a.split("-").join();
                let bb = b.split("-").join();
                return aa < bb ? -1 : aa > bb ? 1 : 0;
              });
              let cur = activity.curTime;
              if (timeline[0] == cur) {
                activity.stage = 0;
              } else if (timeline[1] == cur) {
                activity.stage = 1;
              } else {
                activity.stage = 2;
              }
              actual_list.push(activity);
            }
          }
        }
        //sort activities by start time
        actual_list.sort(function (a, b) {
          let aa = a.startTime.split("-").join();
          let bb = b.startTime.split("-").join();
          return aa < bb ? -1 : aa > bb ? 1 : 0;
        });

        this.activityPage = response.data;
        this.activityPage.list = actual_list;
        this.activityList = actual_list;
        this.$emit("complete");
      });
    },
    handleRouteList() {
      this.$router.replace({
        path: this.$route.path,
        query: formatListQuery(this.listQuery),
      });
    },
    handleTabClick(tab, event) {
      const status = parseInt(tab.name);
      this.activityList = [];
      if (status === -1) {
        this.activityList = this.activityPage.list;
      } else {
        for (var i = 0; i < this.activityPage.list.length; i++) {
          let activity = this.activityPage.list[i];
          if (activity.stage === status) {
            this.activityList.push(activity);
          }
        }
      }
      this.handleRouteList();
    },
  },
};
</script>

<style scoped lang="scss">
.activity-container {
  width: 100%;

  .activity-item-thumbnail-wrapper {
    position: relative;
    width: 100%;
    height: 0;
    padding-top: 100%;
    .activity-item-thumbnail {
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
    }
    .activity-item-tag {
      .triangle {
        position: absolute;
        top: 10%;
        right: 0;
        padding: 0 5px;
        width: 70px;
        height: 25px;
        display: flex;
        flex-direction: row;
        justify-content: center;
        align-items: center;
        color: #fff;
        font-weight: bold;
        font-size: 13px;
      }
      .triangle::before {
        content: "";
        position: absolute;
        right: 70px;
        width: 0;
        height: 0;
        border: 12px solid;
        border-color: transparent red transparent transparent;
      }

      .warm-up::before {
        border-color: transparent #36a9ce transparent transparent;
      }
      .warm-up {
        background-color: #36a9ce;
      }
      .on-stage {
        background-color: hsl(17, 91%, 55%);
      }
      .on-stage::before {
        border-color: transparent hsl(17, 91%, 55%) transparent transparent;
      }
      .off-stage {
        background-color: black;
      }
      .off-stage::before {
        border-color: transparent black transparent transparent;
      }
    }
  }

  .activity-item-title {
    display: flex;
    align-items: center;
    height: 50px;
    font-weight: bold;
    margin-bottom: 8px;
  }

  .activity-item-detail {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
  }

  .noInfoMsgBox {
    line-height: 80px;
    text-align: center;
    padding-bottom: 20px;
    width: 100%;
  }
}
</style>