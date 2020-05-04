<template>
  <div class="container">
    <pagination
      v-show="total"
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.limit"
      @pagination="handleRouteList"
    />
    <div class="row">
      <div class="col-lg-10 col-md-11 col-sm-12">
        <div class="company-wiki-list" v-if="list.length !== 0">
          <el-card
            shadow="hover"
            v-for="company in list"
            :key="company.id"
            class="mb-2 link-pointer"
            @click.native="openCompanyDetail(company)"
          >
            <el-row :gutter="15">
              <el-col :span="4">
                <el-image :src="company.logo" class="w-100"></el-image>
              </el-col>
              <el-col :span="20">
                <h5>{{company.fullName}}</h5>
                <div class="text-label">
                  <span v-if="company.stage">{{company.stage.name}} .</span>
                  <span v-if="company.property">{{company.property.name}} .</span>
                  <span v-if="company.industry">{{company.industry.name}} .</span>
                  <span v-if="company.scale">{{company.scale.name}}</span>
                </div>
                <div class="mt-2 text-desc">{{company.wikiSummary}}</div>
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
import { getCompanyWikiList } from "@/api/company_api";
import Pagination from "@/components/Pagination";
import { formatListQuery, parseListQuery } from "@/utils/common";
import { mapGetters } from "vuex";

export default {
  name: "WikiListPage",
  components: { Pagination },
  computed: {
    ...mapGetters(["keyword"])
  },
  data() {
    return {
      listQuery: {
        keyword: undefined,
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
      this.listQuery.keyword = this.keyword;
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
      getCompanyWikiList(this.listQuery).then(response => {
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
    openCompanyDetail(company) {
        this.$router.push(`/company/${company.id}`)
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
