<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :inline="true" :model="listQuery">
        <el-form-item>
          <el-button type="success" @click="handleNewActivity" icon="el-icon-plus">添加活动</el-button>
        </el-form-item>
        <el-form-item>
          <el-select
            v-model="listQuery.cityId"
            filterable
            clearable
            placeholder="所在城市"
            @change="handleFilter"
          >
            <el-option
              v-for="item in cityOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleFilter">查询</el-button>
        </el-form-item>
      </el-form>
    </div>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.limit"
      @pagination="handleRouteList"
    />

    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      highlight-current-row
      style="margin-top: 10px"
    >
      <el-table-column label="ID" prop="id"></el-table-column>
      <el-table-column label="活动名称" prop="title"></el-table-column>
      <el-table-column label="活动时间">
        <template slot-scope="{row}">{{`${row.startTime} ~ ${row.finishTime}`}}</template>
      </el-table-column>
      <el-table-column label="所在城市" prop="city.name"></el-table-column>
      <el-table-column label="活动地点" prop="address"></el-table-column>
      <el-table-column label="活动详情" prop="url">
        <template slot-scope="{row}">
          <el-link :href="getActivityUrl(row.id)" type="primary" target="_blank">点击查看</el-link>
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" width="200">
        <template slot-scope="{row}">
          <el-button type="primary" size="mini" @click="handleModify(row)" icon="el-icon-edit">编辑</el-button>
          <el-popconfirm title="你确定要下架该活动？" @onConfirm="handleTakeoff(row)" v-if="row.status == 2">
            <el-button
              type="danger"
              size="mini"
              confirmButtonText="下架"
              icon="el-icon-delete"
              slot="reference"
            >删除</el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.limit"
      @pagination="handleRouteList"
    />
  </div>
</template>

<script>
import waves from "@/directive/waves"; // waves directive
import Pagination from "@/components/Pagination"; // secondary package based on el-pagination
import { getActivityList, takeoffActivity } from "@/api/activity_api";
import { listByType } from "@/api/dict_api";

export default {
  name: "ActivityList",
  components: { Pagination },
  directives: { waves },
  data() {
    return {
      tableKey: 0,
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        cityId: undefined,
        status: "2",
        page: 1,
        limit: 20,
        sort: "-id"
      },
      cityOptions: [],
      newDialogVisible: false,
      activityForm: {}
    };
  },
  created() {
    this.initData();
  },
  watch: {
    $route() {
      this.getList();
    }
  },
  methods: {
    initData() {
      listByType(2).then(response => (this.cityOptions = response.data.list));
      this.getList();
    },
    getList() {
      this.listLoading = true;
      const query = this.$route.query;
      Object.keys(query).forEach(key => {
        this.listQuery[key] = query[key];
      });
      if (query.page) {
        this.listQuery.page = parseInt(query.page);
      }
      if (query.limit) {
        this.listQuery.limit = parseInt(query.limit);
      }
      getActivityList(this.listQuery).then(response => {
        const { total, list } = response.data;
        this.list = list;
        this.total = total;
        this.listLoading = false;
      });
    },
    handleRouteList() {
      this.$router.push({ path: this.$route.path, query: this.listQuery });
    },
    handleFilter() {
      this.listQuery.page = 1;
      this.handleRouteList();
    },
    handleTakeoff(activity) {
      takeoffActivity(activity.id).then(() => {
        this.$message("操作成功");
        this.getList();
      });
    },
    handleNewActivity() {
      this.$router.push("/activity/edit");
    },
    getActivityUrl(id){
      return `${process.env.VUE_APP_WEB_HOST}/activity/${id}`
    },
    handleModify(activity) {
      this.$router.push({ path: "/activity/edit", query: { id: activity.id } });
    }
  }
};
</script>
