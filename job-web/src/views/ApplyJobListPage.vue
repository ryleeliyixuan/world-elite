<template>
  <div class="app-container">
    <h5>我的投递</h5>
    <pagination
      v-show="total"
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.limit"
      @pagination="handleRouteList"
    />
    <div class="job-list w-75" v-if="pageResult.list && pageResult.list.length !== 0">
      <el-card
        shadow="hover"
        v-for="job in pageResult.list"
        :key="job.id"
        class="mb-2"
        @click.native="openJobDetail(job.id)"
      >
        <b-media right-align vertical-align="center">
          <p class="text-small text-gray">{{job.companyUser.company.name}}</p>
          <h6 class="mt-0 mb-1">{{job.name}}</h6>
          <div class>
            <b
              class="text-danger"
            >{{`${job.minSalary} - ${job.maxSalary}K`}}{{job.salaryMonths?` × ${job.salaryMonths}`:''}}</b>
            <span class="ml-3 text-gray text-small">{{`${job.city.name} / ${job.minDegree.name}`}}</span>
          </div>
          <template v-slot:aside>
            <el-tag>{{ job.applyStatus | statusFilter }}</el-tag>
          </template>
        </b-media>
      </el-card>
    </div>
    <pagination
      v-show="total"
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
      listQuery: {
        page: 1,
        limit: 10,
        sort: "-id"
      },
      total: 0,
      pageResult: 0
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
        7: "已过期"
      };
      return statusMap[status];
    }
  },
  created() {
    this.getList();
  },
  watch: {
    $route() {
      this.getList();
    }
  },
  methods: {
    getList() {
      parseListQuery(this.$route.query, this.listQuery);
      myApplyJobList(this.listQuery).then(response => {
        this.pageResult = response.data;
        this.total = this.pageResult.total;
      });
    },
    handleRouteList() {
      this.$router.push({
        path: this.$route.path,
        query: formatListQuery(this.listQuery)
      });
    },
    openJobDetail(id) {
      this.$router.push(`/job/${id}`);
    }
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss">
.app-container {
  width: 1000px;
  margin: 20px auto;
}
</style>
