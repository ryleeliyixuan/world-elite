<template>
  <div class="app-container">
    <h5>我的投递</h5>

    <!-- 通过投递成功／通过初筛／邀请面试／不合适 四种状态来方便用户追踪申请的进度，提升用户体验。 -->
    <div class="job-list">
      <el-tabs class="nav-bar" v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="全部投递" name="0"></el-tab-pane>
        <el-tab-pane label="投递成功" name="2"></el-tab-pane>
        <el-tab-pane label="通过初筛" name="3"></el-tab-pane>
        <el-tab-pane label="邀请面试" name="4"></el-tab-pane>
        <el-tab-pane label="不合适" name="6"></el-tab-pane>
      </el-tabs>

      <pagination
              :total="total"
              :page.sync="listQuery.page"
              :limit.sync="listQuery.limit"
              @pagination="handleRouteList"
      />

      <template v-if="pageResult.list && pageResult.list.length !== 0">

        <el-card
          shadow="hover"
          v-for="job in pageResult.list"
          :key="job.id"
          class="item-card"
          @click.native="openJobDetail(job.id)"
        >
          <div class="item-container">
            <div class="item-company-container">
              <div class="text-small text-gray item-company-name">
                <span>{{ job.company.name }}</span>
                <div>
                  <span style="margin-right: 10px">{{ job.applyTime }}</span>
                  <el-button
                    type="primary"
                    plain
                    @click.stop="handleChat(job)"
                    size="small"
                    >聊一聊</el-button
                  >
                </div>
              </div>
              <h6 class="mt-0 mb-1">{{ job.name }}</h6>
              <div class="item-footer">
                <div class="item-footer-text">
                  <b class="text-danger"
                    >
                    {{job.minSalary}}K - {{job.maxSalary}}K{{ job.salaryMonths ? ` × ${job.salaryMonths}` : "" }}
                  </b
                  >
                  <span class="ml-3 text-gray text-small">{{
                    `${job.city ? job.city.name : ""} / ${
                      job.minDegree ? job.minDegree.name : ""
                    }`
                  }}</span>
                </div>
                <el-tag v-if="activeName == 0">
                  {{ job.status | statusFilter }}
                </el-tag>
              </div>
            </div>
          </div>
        </el-card>
      </template>
      <template v-else>
        <el-card shadow="hover" class="item-card">
          您没有状态为“{{ activeName | statusFilter }}”的投递
        </el-card>
      </template>
    </div>
    <pagination
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.limit"
      @pagination="handleRouteList"
    />
  </div>
</template>

<script>
import { myApplyJobList } from "@/api/job_api";
import Pagination from "@/components/Pagination";
import { formatListQuery, parseListQuery } from "@/utils/common";

export default {
  name: "JobListPage",
  components: { Pagination },
  data() {
    return {
      activeName: "0",
      listQuery: {
        page: 1,
        limit: 10,
        sort: "-id",
        status: "",
      },
      total: 0,
      pageResult: 0,
    };
  },
  filters: {
    statusFilter(status) {
      const statusMap = {
        0: "全部投递",
        1: "投递中",
        2: "投递成功",
        3: "通过初筛",
        4: "邀请面试",
        5: "录用",
        6: "不合适",
        7: "已过期",
      };
      return statusMap[status];
    },
  },
  created() {
    this.getList();
  },
  watch: {
    $route() {
      this.getList();
    },
  },
  methods: {
    getList() {
      parseListQuery(this.$route.query, this.listQuery);
      myApplyJobList(this.listQuery).then((response) => {
        this.pageResult = response.data;
        this.total = this.pageResult.total;
        this.$emit("complete");
      });
    },
    handleRouteList() {
      this.$router.push({
        path: this.$route.path,
        query: formatListQuery(this.listQuery),
      });
    },
    openJobDetail(id) {
      this.$router.push(`/job/${id}`);
    },
    handleClick(tab, event) {
      const status = parseInt(tab.name);
      if (status === 0) {
        this.listQuery.status = "";
      } else {
        this.listQuery.status = status;
      }
      this.listQuery.page = 1;
      this.handleRouteList();
      this.getList();
    },
    handleChat(item) {
      this.$router.push({
        path: "/chat",
        query: { toUser: item.creatorId, jobId: item.id },
      });
    },
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss">
.app-container {
  max-width: 1000px;
  margin: 20px auto;
  width: 100%;

  .job-list {
    width: 75%;
    .nav-bar {
      display: inline-flex;
      margin-bottom: 10px;
      align-items: center;
    }

    .item-card {
      margin-bottom: 10px;

      .item-container {
        display: flex;
        justify-content: space-between;
        align-items: center;

        .item-company-container {
          flex: 1;
          display: flex;
          flex-direction: column;

          .item-company-name {
            display: flex;
            justify-content: space-between;
            align-items: center;
          }
          .item-footer {
            display: flex;
            justify-content: space-between;
            align-items: center;
          }
        }
      }
    }
  }
}

@media screen and (max-width: 850px) {
  .app-container {
    margin: 20px;

    .job-list {
      width: 100%;
    }
  }
}

@media screen and (max-width: 450px) {
  .item-footer-text {
    display: flex;
    flex-direction: column;
  }
}
</style>
