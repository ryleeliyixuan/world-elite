<template>
  <div class="container">
    <div class="search-filter mt-2 mt-2">
      <el-select
        v-model="listQuery.cityId"
        filterable
        clearable
        placeholder="活动城市"
        @change="handleFilter"
        size="small"
      >
        <el-option v-for="item in cityOptions" :key="item.id" :label="item.name" :value="item.id"></el-option>
      </el-select>
    </div>
    <pagination
      v-show="total"
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.limit"
      @pagination="handleRouteList"
    />
    <div class="row">
      <div class="col-lg-10 col-md-11 col-sm-12">
        <div class="activity-list" v-if="list.length !== 0">
          <el-card
            shadow="hover"
            v-for="activity in list"
            :key="activity.id"
            class="mb-2 link-pointer"
            @click.native="openActivity(activity)"
          >
            <el-row :gutter="15">
              <el-col :span="4">
                <el-image :src="activity.thumbnail" class="w-100"></el-image>
              </el-col>
              <el-col :span="20">
                <h5>
                  {{activity.title}}
                  <el-tag type="primary">{{activity.city.name}}</el-tag>
                </h5>
                <div class="text-label">
                  <b>活动地址：</b>
                  {{activity.address}}
                </div>
                <div class="text-label">
                  <b>活动时间：</b>
                  {{activity.startTime}} 到 {{activity.finishTime}}
                </div>
                <div class="mt-2 text-desc">
                  {{activity.summary}}
                </div>
              </el-col>
            </el-row>
          </el-card>
        </div>
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
import { listByType } from "@/api/dict_api";
import { getActivityList } from "@/api/activity_api";
import Pagination from "@/components/Pagination";
import { formatListQuery, parseListQuery } from "@/utils/common";
import { mapGetters } from "vuex";

export default {
  name: "JobListPage",
  components: { Pagination },
  computed: {
    ...mapGetters(["keyword"])
  },
  data() {
    return {
      listQuery: {
        cityId: undefined,
        title: undefined,
        page: 1,
        limit: 10
      },
      total: 0,
      list: [],
      cityOptions: []
    };
  },
  created() {
    this.initData();
  },
  watch: {
    $route() {
      this.getList();
    },
    keyword() {
      this.listQuery.title = this.keyword;
      this.handleRouteList();
    }
  },
  methods: {
    initData() {
      listByType(2).then(response => (this.cityOptions = response.data.list));
      this.getList();
    },
    handleFilter() {
      this.listQuery.page = 1;
      this.handleRouteList();
    },
    getList() {
      parseListQuery(this.$route.query, this.listQuery);
      if (this.listQuery.cityId) {
        this.listQuery.cityId = parseInt(this.listQuery.cityId);
      }
      getActivityList(this.listQuery).then(response => {
        this.list = response.data.list;
        this.total = response.data.total;
      });
    },
    handleRouteList() {
      this.$router.push({
        path: this.$route.path,
        query: formatListQuery(this.listQuery)
      });
    },
    openActivity(activity) {
      this.$router.push(`/activity/${activity.id}`);
    }
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss">
.text-label {
  color: #555;
  font-size: 15px;
}

.text-desc {
  font-size: 15px;
  color: #888;
}
</style>
