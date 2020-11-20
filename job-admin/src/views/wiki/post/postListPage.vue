<template>
  <div class="community-post-container">
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.limit"
      @pagination="handleRouteList"
    />
    <el-table
      ref="multipleTable"
      :data="postList"
      tooltip-effect="dark"
      style="width: 100%"
      border
      @selection-change="handleSelectionChange"
    >
      <el-table-column prop="id" label="帖子ID" width="120"> </el-table-column>
      <el-table-column prop="title" label="帖子标题" width="120">
      </el-table-column>
      <el-table-column prop="companyId" label="企业" show-overflow-tooltip>
      </el-table-column>
      <el-table-column
        prop="fromUser.name"
        label="用户名"
        show-overflow-tooltip
      >
      </el-table-column>
      <el-table-column prop="createTime" label="发帖时间" show-overflow-tooltip>
      </el-table-column>
      <el-table-column prop="action" label="操作" show-overflow-tooltip>
        <template slot-scope="scope">
          <el-button
            type="primary"
            size="mini"
            @click="handleViewPost(scope.row.companyId, scope.row.id)"
            icon="el-icon-edit"
            >查看</el-button
          >
          <el-button
            type="danger"
            size="mini"
            @click="deletePost(scope.row.id)"
            icon="el-icon-edit"
            >删除</el-button
          >
          <el-button
            type="success"
            size="mini"
            confirmButtonText="删除"
            icon="el-icon-delete"
            slot="reference"
            @click="
              handleManageComment(scope.row.id, scope.$index, postPage.list)
            "
            >评论管理</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.limit"
      @pagination="handleRouteList"
    />
  </div>
</template>

<script>
import waves from "@/directive/waves"; // waves directive
import Pagination from "@/components/Pagination";
import Toast from "@/utils/toast";
import {
  //post
  getPostList,
  deletePost,
  getPostDetail,
  //comment
  getCommentList,
  deleteComment,
} from "@/api/community_api";

export default {
  name: "PostListPage",
  components: { Pagination },
  directives: { waves },
  data() {
    return {
      total: 0,
      listLoading: true,
      postList: [],
      activeName: "1",
      listQuery: {
        page: 1,
        limit: 20,
        sort: "-id",
        id: undefined,
        companyId: undefined,
        cliqueId: undefined,
        title: "",
      },
    };
  },
  created() {
    this.initData();
  },
  watch: {
    $route() {
      this.getPostList();
    },
  },
  methods: {
    initData() {
      this.listLoading = true;
      const query = this.$route.query;
      Object.keys(query).forEach((key) => {
        this.listQuery[key] = query[key];
      });
      if (query.page) {
        this.listQuery.page = parseInt(query.page);
      }
      if (query.limit) {
        this.listQuery.limit = parseInt(query.limit);
      }

      getPostList(this.listQuery).then((response) => {
        const { total, list } = response.data;
        this.postList = list;
        this.total = total;
        this.listLoading = false;
      });
    },
    getPostList() {
      getPostList(this.listQuery).then((response) => {
        this.postPage = response.data;
      });
    },
    handleRouteList() {
      this.companyList = [];
      this.$router.push({
        path: this.$route.path,
        query: formatListQuery(this.listQuery),
      });
    },
    handleViewPost(companyId, postId) {
      //http://test.myworldelite.com/company/1253557897332019200/community/post?page=1&limit=5&sort=-hots&id=1322733186255613952&companyId=1253557897332019200
      window.open(
        `http://test.myworldelite.com/company/${companyId}/community/post?page=1&limit=5&sort=-hots&id=${postId}&companyId=${companyId}`
      );
    },
    deletePost(id) {
      this.$confirm("是否要删除该帖子？", {
        confirmButtonText: "删除",
      }).then(() => {
        deletePost(id).then(() => {
          Toast.success("成功删除该帖子");
          this.initData();
        });
      });
    },
    handleManageComment() {
      this.activeName = 2;
    },
    handleClick(tab, event) {},
    toggleSelection(rows) {
      if (rows) {
        rows.forEach((row) => {
          this.$refs.multipleTable.toggleRowSelection(row);
        });
      } else {
        this.$refs.multipleTable.clearSelection();
      }
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
  },
};
</script>

<style lang="scss" scoped>
.community-post-container {
  width: 100%;
}
</style>
