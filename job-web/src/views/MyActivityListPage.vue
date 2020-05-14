<template>
  <div class="app-container">
    <h5>我的活动</h5>
    <pagination
      v-show="total"
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.limit"
      @pagination="handleRouteList"
    />
    <div class="activity-list w-75" v-if="pageResult.list && pageResult.list.length !== 0">
      <el-card
        shadow="hover"
        v-for="activity in pageResult.list"
        :key="activity.id"
        class="mb-2 link-pointer"
        @click.native="openActivityDetail(activity.id)"
      >
        <b-media right-align vertical-align="center">
          <h6 class="mt-0 mb-1">{{activity.title}}</h6>
          <div class="text-label">
            <b>活动地址：</b>
            {{activity.address}}
          </div>
          <div class="text-label">
            <b>活动时间：</b>
            {{activity.startTime}} 到 {{activity.finishTime}}
          </div>
          <template v-slot:aside>
            <el-button
              type="danger"
              @click.stop="delFavorite(activity)"
              icon="el-icon-delete"
              size="small"
              :loading="favoriteLoading"
            >取消关注</el-button>
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
import { getFavoriteActivityList, doFavorite } from "@/api/favorite_api";
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
        type: 3
      }
    };
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
      getFavoriteActivityList(this.listQuery).then(response => {
        this.pageResult = response.data;
        this.total = this.pageResult.total;
      });
    },
    openActivityDetail(id) {
      this.$router.push(`/activity/${id}`);
    },
    delFavorite(acitivity) {
      this.favoriteLoading = true;
      this.favoriteForm.objectId = acitivity.id;
      doFavorite(this.favoriteForm)
        .then(() => {
          this.$message("取消成功");
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
    }
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" scoped>
.app-container {
  width: 1000px;
  margin: 20px auto;
}

.text-label {
  color: #555;
  font-size: 15px;
}
</style>
