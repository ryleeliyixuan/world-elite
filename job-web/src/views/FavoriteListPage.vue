<template>
  <div class="app-container">
    <h5>我的收藏</h5>
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
        class="mb-2 link-pointer"
        @click.native="openJobDetail(job.id)"
      >
        <b-media right-align vertical-align="center">
          <p class="text-small text-gray">{{job.companyUser.company.name}} <span class="float-right">{{job.time}}</span></p>
          <h6 class="mt-0 mb-1">{{job.name}}</h6>
          <div class>
            <b
              class="text-danger"
            >{{`${job.minSalary} - ${job.maxSalary}K`}}{{job.salaryMonths?` × ${job.salaryMonths}`:''}}</b>
            <span class="ml-3 text-gray text-small">{{`${job.city.name} / ${job.minDegree.name}`}}</span>
          </div>
          <template v-slot:aside>
            <el-button
              type="danger"
              @click.stop="delFavorite(job)"
              icon="el-icon-delete"
              size="small"
              :loading="favoriteLoading"
            >取消收藏</el-button>
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
import { getFavoriteJobList, doFavorite } from "@/api/favorite_api";
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
      pageResult: 0,
      favoriteLoading: false,
      favoriteForm: {
        favorite: false,
        type: 1
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      parseListQuery(this.$route.query, this.listQuery);
      getFavoriteJobList(this.listQuery).then(response => {
        this.pageResult = response.data;
        this.total = this.pageResult.total;
      });
    },
    openJobDetail(id) {
      this.$router.push(`/job/${id}`);
    },
    delFavorite(job) {
      this.favoriteLoading = true;
      this.favoriteForm.objectId = job.id;
      doFavorite(this.favoriteForm)
        .then(() => {
          this.$message("取消收藏成功");
          this.getList();
        })
        .finally(() => {
          this.favoriteLoading = false;
        });
    },
    handleRouteList() {
      this.$router.push({
        path: this.$route.path,
        query: formatListQuery(this.listQuery)
      });
    },
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
