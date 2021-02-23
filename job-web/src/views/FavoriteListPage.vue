<template>
  <div class="app-container container">
    <h5>我的收藏</h5>
    <el-tabs class="nav-bar" v-model="activeName" @tab-click="handleTabSelect">
      <el-tab-pane label="收藏职位" name="1"></el-tab-pane>
      <el-tab-pane label="收藏企业" name="2"></el-tab-pane>
    </el-tabs>
    <pagination
      v-show="total"
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.limit"
      @pagination="handleRouteList"
    />
    <div class="w-75" v-if="list.length !== 0">
      <div class="job-list" v-if="this.listQuery.type === 1">
        <el-card
          shadow="hover"
          v-for="job in list"
          :key="job.id"
          class="mb-2 link-pointer"
          @click.native="openJobDetail(job.id)"
        >
          <b-media right-align vertical-align="center">
            <p class="text-small text-gray">
              {{
                job.companyUser &&
                job.companyUser.company &&
                job.companyUser.company.name
              }}
              <span class="float-right">{{ job.favoriteTime }}</span>
            </p>
            <h6 class="mt-0 mb-1">{{ job.name }}</h6>
            <div class>
              <b class="text-danger"
                >{{job.minSalary}}K - {{job.maxSalary}}K{{ job.salaryMonths ? ` × ${job.salaryMonths}` : "" }}</b
              >
              <span class="ml-3 text-gray text-small">{{
                `${job.city ? job.city.name : ""} / ${
                  job.minDegree ? job.minDegree.name : ""
                }`
              }}</span>
            </div>
            <template v-slot:aside>
              <el-button
                type="danger"
                @click.stop="delFavorite(job)"
                icon="el-icon-delete"
                size="small"
                :loading="favoriteLoading"
              >
                取消收藏
              </el-button>
            </template>
          </b-media>
        </el-card>
      </div>
      <div class="company-list" v-if="this.listQuery.type == 2">
        <el-card
          shadow="hover"
          v-for="company in list"
          :key="company.id"
          class="mb-2 link-pointer"
          @click.native="openCompanyDetail(company.id)"
        >
          <el-row :gutter="15">
            <el-col :span="4">
              <el-image :src="company.logo" class="company-logo"></el-image>
            </el-col>
            <el-col :span="20">
              <b-media right-align vertical-align="center">
                <h6 class="mt-0 mb-1">{{ company.fullName }}</h6>
                <div class="text-label">
                  <span v-if="company.stage">{{ company.stage.name }} .</span>
                  <span v-if="company.property"
                    >{{ company.property.name }} .</span
                  >
                  <span v-if="company.industry"
                    >{{ company.industry.name }} .</span
                  >
                  <span v-if="company.scale">{{ company.scale.name }}</span>
                </div>
                <template v-slot:aside>
                  <el-button
                    type="danger"
                    @click.stop="delFavorite(company)"
                    icon="el-icon-delete"
                    size="small"
                    :loading="favoriteLoading"
                  >
                    取消收藏
                  </el-button>
                </template>
              </b-media>
            </el-col>
          </el-row>
        </el-card>
      </div>
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
import { getFavoriteList, doFavorite } from "@/api/favorite_api";
import Pagination from "@/components/Pagination";
import { formatListQuery, parseListQuery } from "@/utils/common";

export default {
  name: "FavoriteListPage",
  components: { Pagination },
  data() {
    return {
      listQuery: {
        page: 1,
        limit: 10,
        type: 1,
        sort: "-id",
      },
      total: 0,
      list: [],
      favoriteLoading: false,
      favoriteForm: {
        favorite: false,
        type: 1,
      },
      activeName: "1",
    };
  },
  watch: {
    $route() {
      this.getList();
    },
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      parseListQuery(this.$route.query, this.listQuery);
      this.activeName = this.listQuery.type + "";
      getFavoriteList(this.listQuery).then((response) => {
        this.list = response.data.list;
        this.total = response.data.total;
        this.$emit("complete");
      });
    },
    handleTabSelect(tab, event) {
      const type = parseInt(tab.name);
      this.listQuery.type = type;
      this.listQuery.page = 1;
      this.handleRouteList();
    },
    openJobDetail(id) {
      this.$router.push(`/job/${id}`);
    },
    openCompanyDetail(id) {
      this.$router.push(`/company/${id}`);
    },
    delFavorite(job) {
      this.favoriteLoading = true;
      this.favoriteForm.objectId = job.id;
      this.favoriteForm.type = this.listQuery.type;
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
        query: formatListQuery(this.listQuery),
      });
    },
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" scoped>
.app-container {
  width: 1000px;
  margin: 20px auto;

  .nav-bar {
    display: inline-flex;
    margin-bottom: 10px;
    align-items: center;
  }
}

.company-logo {
  width: 80px;
  height: 80px;
}
</style>
