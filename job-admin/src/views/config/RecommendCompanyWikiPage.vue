<template>
  <div class="app-container">
      <div class="mb-2">
          <el-button type="success" @click="onEditRecommendClick" icon="el-icon-plus">推荐百科</el-button>
      </div>
      <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%; margin-top:10px"
    >
      <el-table-column label="ID" prop="id"></el-table-column>
      <el-table-column label="企业ID" prop="object.id"></el-table-column>
      <el-table-column label="企业名称" align="center" prop="object.fullName"></el-table-column>
      <el-table-column label="排序" align="center" prop="position"></el-table-column>
      <el-table-column label="创建时间" prop="createTime"></el-table-column>
      <el-table-column label="操作">
         <template slot-scope="{row}">
          <el-button type="primary" size="mini" @click="onEditRecommendClick(row)">修改</el-button>
          <el-button type="danger" size="mini" @click="onDelRecommendClick(row.id)">取消推荐</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.limit"
      @pagination="getList"
    />

    <el-dialog
      title="推荐百科"
      :visible.sync="dialogVisible"
      width="30%"
    >
      <el-form ref="form" :model="recommendForm" label-width="60px" label-position="top">
        <el-form-item label="企业ID">
          <el-input v-model="recommendForm.objectId" placeholder="企业ID" type="number"></el-input>
        </el-form-item>
        <el-form-item label="排序">
          <el-input v-model="recommendForm.position" placeholder="排序" type="number"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleSaveRecommend">保存</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>
import waves from "@/directive/waves"; // waves directive
import Pagination from "@/components/Pagination"; // secondary package based on el-pagination
import Toast from "@/utils/toast";
import { getRecommendList, saveRecommend, delRecommend } from '@/api/recommend_api'
import { formatListQuery, parseListQuery } from "@/utils/common";

export default {
  name: "RecommendList",
  components: { Pagination},
  directives: { waves },
  data() {
    return {
      tableKey: 0,
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        objectType: 3,
        page: 1,
        limit: 20,
        sort: "+position"
      },
      recommendForm: {
         id: undefined,
         objectId: undefined,
         objectType: 3,
         position: undefined
      },
      dialogVisible: false
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
      this.getList();
    },
    getList() {
      this.listLoading = true;
      const query = this.$route.query;
      parseListQuery(query, this.listQuery);
      getRecommendList(this.listQuery).then(response => {
          const { data } = response;
          this.total = data.total;
          this.list = data.list;
      }).finally(()=>{
          this.listLoading = false;
      })
    },
    handleRouteList() {
      this.$router.push({
        path: this.$route.path,
        query: formatListQuery(this.listQuery)
      });
    },
    handleFilter() {
      this.listQuery.page = 1;
      this.handleRouteList();
    },
    onEditRecommendClick(record){
      this.dialogVisible = true;
      if(record && record.id) {
         this.recommendForm.id = record.id;
         this.recommendForm.objectId = record.object.id;
         this.recommendForm.position = record.position;
      }else{
         this.recommendForm.id = undefined;
         this.recommendForm.objectId = undefined;
         this.recommendForm.position = undefined;
      }
    },
    handleSaveRecommend(){
        this.dialogVisible = false;
        saveRecommend(this.recommendForm).then(()=>{
          Toast.success('操作成功');
          this.getList();
        })
    },
    onDelRecommendClick(id){
        this.$confirm("此操作将取消推荐, 是否继续?", "提示", {
        confirmButtonText: "继续",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
           delRecommend(id).then(() => {
              Toast.success('操作成功');
              this.getList();
           })
        })
    }
  }
};
</script>
