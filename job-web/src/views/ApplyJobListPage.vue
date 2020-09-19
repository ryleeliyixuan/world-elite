<template>
  <div class="app-container">
    <h5>我的投递</h5>
      <!-- v-show="total" -->
    <pagination
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.limit"
      @pagination="handleRouteList"
    />
    <!-- v-if="pageResult.list && pageResult.list.length !== 0" -->
    <div class="job-list" >
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="待查看" name="1"></el-tab-pane>
        <el-tab-pane label="已查看" name="2"></el-tab-pane>
        <el-tab-pane label="待通知" name="3"></el-tab-pane>
        <el-tab-pane label="面试" name="4"></el-tab-pane>
        <el-tab-pane label="已入职" name="5"></el-tab-pane>
        <el-tab-pane label="不合适" name="6"></el-tab-pane>
        <el-tab-pane label="已过期" name="7"></el-tab-pane>
      </el-tabs>
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
              <span>{{job.companyUser.company.name}}</span>
              <span>{{job.applyTime}}</span>
            </div>
            <h6 class="mt-0 mb-1">{{job.name}}</h6>
            <div class>
              <b
                class="text-danger"
              >{{`${job.minSalary} - ${job.maxSalary}K`}}{{job.salaryMonths?` × ${job.salaryMonths}`:''}}</b>
              <span
                class="ml-3 text-gray text-small"
              >{{`${job.city?job.city.name:''} / ${job.minDegree?job.minDegree.name:''}`}}</span>
            </div>
          </div>
          <div>
            <el-tag>{{ job.status | statusFilter }}</el-tag>
          </div>
        </div>
      </el-card>
      <el-card
        shadow="hover"
        class="item-card"
        v-if="!pageResult.list || !pageResult.list.length"
      >
        您没有状态为“{{ activeName | statusFilter }}”的投递
      </el-card>
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
      activeName: "1",
      listQuery: {
        page: 1,
        limit: 10,
        sort: "-id",
        status: 1,
      },
      total: 0,
      pageResult: 0,
    };
  },
  filters: {
    statusFilter(status) {
      const statusMap = {
        1: "待查看",
        2: "已查看",
        3: "待通知",
        4: "面试",
        5: "已入职",
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
      this.listQuery.status = status;
      this.getList();
    },
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss">
.app-container {
  max-width: 1000px;
  margin: 20px auto;
  min-height: calc(100vh - 477px);

  .job-list {
    width: 75%;

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
</style>
