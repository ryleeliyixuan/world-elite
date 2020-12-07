<template>
  <div class="community-post-container">
    <div class="filter">
      <div class="d-flex">
        <el-input
          style="width: 400px"
          class="p-2"
          placeholder="帖子ID"
          v-model="listQuery.id"
          clearable
        >
        </el-input>
        <el-input
          style="width: 400px"
          class="p-2"
          placeholder="标题"
          v-model="listQuery.title"
          clearable
        >
        </el-input>
        <el-select
          style="width: 300px"
          class="p-2"
          v-model="listQuery.fromNames"
          multiple
          filterable
          allow-create
          default-first-option
          placeholder="发帖人用户名"
        >
          <el-option
            v-for="item in fromNamesOptions"
            :key="item.value"
            :value="item.value"
          >
          </el-option>
        </el-select>
        <el-select
          style="width: 300px"
          class="p-2"
          v-model="listQuery.companyNames"
          multiple
          filterable
          allow-create
          default-first-option
          placeholder="公司名"
        >
          <el-option
            v-for="item in companyNamesOptions"
            :key="item.value"
            :value="item.value"
          >
          </el-option>
        </el-select>
      </div>
      <div class="d-flex">
        <el-date-picker
          style="width: 400px"
          class="p-2"
          v-model="listQuery.beginTime"
          type="datetime"
          placeholder="开始日期"
          align="right"
          :picker-options="pickerOptions"
        >
        </el-date-picker>
        <el-date-picker
          style="width: 400px"
          class="p-2"
          v-model="listQuery.endTime"
          type="datetime"
          placeholder="结束日期"
          align="right"
          :picker-options="pickerOptions"
        >
        </el-date-picker>
        <el-input
          style="width: 400px"
          class="p-2"
          placeholder="关键词"
          v-model="listQuery.keyword"
          clearable
        >
        </el-input>
        <el-button class="m-2" type="primary" @click="searchPost"
          >搜索</el-button
        >
        <el-button type="text" @click="emptyPostListQuery"
          >清空搜索条件</el-button
        >
      </div>
    </div>
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.limit"
      @pagination="handleRouteList"
    />
    <div class="d-flex justify-content-between m-2">
      <div>
        <el-button
          size="medium"
          type="danger"
          plain
          @click="deleteAllDialogVisible = true"
          >批量删除</el-button
        >
        <el-button size="medium" type="warning" plain @click="forbiddenAllDialogVisible = true">批量禁言</el-button>
      </div>
      <div>
        <el-tabs v-model="activeSort" @tab-click="handleClick">
          <el-tab-pane label="时间顺序" name="1"></el-tab-pane>
          <el-tab-pane label="时间倒序" name="2"></el-tab-pane>
          <el-tab-pane label="只看精品" name="3"></el-tab-pane>
        </el-tabs>
      </div>
    </div>
    <el-table
      ref="multipleTable"
      :data="postList"
      tooltip-effect="dark"
      style="width: 100%"
      border
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55"> </el-table-column>
      <el-table-column prop="id" label="帖子ID" width="120"> </el-table-column>
      <el-table-column prop="title" label="帖子标题" width="120">
      </el-table-column>
      <el-table-column prop="company.name" label="企业" show-overflow-tooltip>
      </el-table-column>
      <el-table-column
        prop="fromUser.name"
        label="用户名"
        show-overflow-tooltip
      >
      </el-table-column>
      <el-table-column prop="createTime" label="发帖时间" show-overflow-tooltip>
      </el-table-column>
      <el-table-column
        width="500px"
        prop="action"
        label="操作"
        show-overflow-tooltip
      >
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
            @click="handeDeletePost(scope.row.id, scope.row.title)"
            icon="el-icon-edit"
            >删除</el-button
          >
          <el-button
            type="success"
            size="mini"
            confirmButtonText="删除"
            icon="el-icon-delete "
            slot="reference"
            @click="
              handleManageComment(scope.row.id)
            "
            >评论管理</el-button
          >
          <el-button
            v-if="scope.row.forbidden == 0"
            type="warning"
            size="mini"
            icon="el-icon-turn-off-microphone"
            slot="reference"
            @click="
              (forbiddenDialogVisible = true),
                (forbiddenForm.userId = scope.row.fromUser.userId),
                (forbiddenName = scope.row.fromUser.name)
            "
            >用户禁言</el-button
          >
          <el-button
            v-else
            type="warning"
            size="mini"
            icon="el-icon-turn-off-microphone"
            slot="reference"
            @click="
              (unforbiddenDialogVisible = true),
                (unforbiddenForm.userId = scope.row.fromUser.userId),
                (unforbiddenName = scope.row.fromUser.name)
            "
            >取消禁言</el-button
          >
          <el-button
            v-if="scope.row.recommend == 0"
            type="danger"
            size="mini"
            icon="el-icon-star-off"
            @click="handleSetRecommend(scope.row.id, 1)"
            >设为精品</el-button
          >
          <el-button
            v-else
            type="danger"
            size="mini"
            icon="el-icon-star-on"
            @click="handleSetRecommend(scope.row.id, 0)"
            >取消精品</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <el-dialog :visible.sync="deleteDialogVisible" width="30%">
      <div>
        您确定要删除帖子"{{ deletedTitle }}"吗？（帖子内的评论也将被删除）
      </div>
      <el-input
        v-model="deleteForm.content"
        placeholder="请输入删帖理由"
      ></el-input>
      <span slot="footer" class="dialog-footer">
        <el-button
          type="primary"
          @click="(deleteDialogVisible = false), deletePost()"
          >确 定</el-button
        >
        <el-button @click="deleteDialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>
    <el-dialog :visible.sync="deleteAllDialogVisible" width="30%">
      <div>
        您确定要删除{{
          multipleSelection.length
        }}个帖子吗？（帖子内的评论也将被删除）
      </div>
      <el-input
        v-model="deleteForm.content"
        placeholder="请输入删帖理由"
      ></el-input>
      <span slot="footer" class="dialog-footer">
        <el-button
          type="primary"
          @click="(deleteAllDialogVisible = false), deleteAll()"
          >确 定</el-button
        >
        <el-button @click="deleteAllDialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>
    <!-- forbidden -->
    <el-dialog :visible.sync="forbiddenDialogVisible" width="30%">
      <div class="mb-2">您将对 {{ forbiddenName }} 进行禁言</div>
      <div class="d-flex justify-content-between align-items-center mb-2">
      <el-select v-model="forbiddenForm.daysId" placeholder="请选择禁言天数">
        <el-option
          v-for="item in forbiddenDaysOptions"
          :key="item.id"
          :label="item.name"
          :value="item.id"
        >
        </el-option>
      </el-select>
      <el-checkbox v-model="forbiddenForm.notice">通知用户</el-checkbox>
      </div>
      <el-input
        v-model="forbiddenForm.content"
        placeholder="请输入禁言理由"
        type="textarea"
      ></el-input>
      <span slot="footer" class="dialog-footer">
        <el-button
          type="primary"
          @click="(forbiddenDialogVisible = false), forbiddenUser()"
          >确 定</el-button
        >
        <el-button @click="forbiddenDialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>
    <!-- unforbidden -->
    <el-dialog :visible.sync="unforbiddenDialogVisible" width="30%">
      <div class="mb-2">您将对 {{ unforbiddenName }} 取消禁言</div>
      <el-checkbox v-model="unforbiddenForm.notice">通知用户</el-checkbox>
      <el-input
        v-model="unforbiddenForm.content"
        placeholder="请输入取消禁言理由"
        type="textarea"
      ></el-input>
      <span slot="footer" class="dialog-footer">
        <el-button
          type="primary"
          @click="(unforbiddenDialogVisible = false), unforbiddenUser()"
          >确 定</el-button
        >
        <el-button @click="unforbiddenDialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>
    <!-- forbidden all -->
    <el-dialog :visible.sync="forbiddenAllDialogVisible" width="30%">
      <div class="mb-2">您将禁言已选的 {{ multipleSelection.length }} 名用户</div>
      <div class="d-flex justify-content-between align-items-center mb-2">
      <el-select v-model="forbiddenForm.daysId" placeholder="请选择禁言天数">
        <el-option
          v-for="item in forbiddenDaysOptions"
          :key="item.id"
          :label="item.name"
          :value="item.id"
        >
        </el-option>
      </el-select>
      <el-checkbox v-model="forbiddenForm.notice">通知用户</el-checkbox>
      </div>
      <el-input
        v-model="forbiddenForm.content"
        placeholder="请输入禁言理由"
        type="textarea"
      ></el-input>
      <span slot="footer" class="dialog-footer">
        <el-button
          type="primary"
          @click="(forbiddenAllDialogVisible = false), forbiddenAllUser()"
          >确 定</el-button
        >
        <el-button @click="forbiddenAllDialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>
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
  searchPost,
  getPostList,
  deletePost,
  getPostDetail,
  savePost,
  deleteAll,
  //forbidden
  forbiddenUser,
  unforbiddenUser,
  forbiddenAllUser,
  //comment
  getCommentList,
  deleteComment,
} from "@/api/community_api";
import { listByType } from "@/api/dict_api";
import { formatListQuery, parseListQuery } from "@/utils/common";

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
      fromNamesOptions: [],
      companyNamesOptions: [],
      deleteDialogVisible: false,
      deleteAllDialogVisible: false,
      deletedTitle: "",
      multipleSelection: [],
      activeSort: "1",

      //forbidden
      forbiddenDaysOptions: [],
      forbiddenDialogVisible: false,
      forbiddenName: "",
      forbiddenForm: {
        userId: undefined,
        userIds: [],
        content: "",
        daysId: undefined,
        notice: true,
      },
      //unforbidden
      unforbiddenDialogVisible: false,
      unforbiddenName: "",
      unforbiddenForm: {
        userId: undefined,
        userIds: [],
        content: "",
        daysId: undefined,
        notice: true,
      },
      //forbidden all
      forbiddenAllDialogVisible: false,

      deleteForm: {
        postId: undefined,
        postIds: [],
        content: "",
      },
      listQuery: {
        page: 1,
        limit: 20,
        sort: "+create_time",
        id: undefined,
        companyId: undefined,
        cliqueId: undefined,
        recommend: undefined,
        title: "",
        fromNames: [],
        companyNames: [],
        beginTime: undefined,
        endTime: undefined,
        keyword: "",
      },
      pickerOptions: {
        shortcuts: [
          {
            text: "今天",
            onClick(picker) {
              picker.$emit("pick", new Date());
            },
          },
          {
            text: "昨天",
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() - 3600 * 1000 * 24);
              picker.$emit("pick", date);
            },
          },
          {
            text: "一周前",
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit("pick", date);
            },
          },
        ],
      },
    };
  },
  created() {
    this.initData();
  },
  watch: {
    $route() {
      this.searchPost();
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
      listByType(20).then(response => (this.forbiddenDaysOptions = response.data.list));
      searchPost(this.listQuery).then((response) => {
        parseListQuery(this.$route.query, this.listQuery);
        this.postPage = response.data;
        const { total, list } = response.data;
        this.postList = list;
        this.total = total;
        this.listLoading = false;
        this.getFromNamesFilterOptions();
        this.getCompanyNameFilterOptions();
      });
    },
    searchPost() {
      searchPost(this.listQuery).then((response) => {
        this.postPage = response.data;
        const { total, list } = response.data;
        this.postList = list;
        this.total = total;
        this.listLoading = false;
      });
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    getFromNamesFilterOptions() {
      this.fromNamesOptions = [];
      let tempFromNameSet = new Set();
      let nameSet = new Set();
      for (let i = 0; i < this.postList.length; i++) {
        let name = this.postList[i].fromUser.name;
        if (name.length > 0 && !nameSet.has(name)) {
          tempFromNameSet.add({ value: name });
          nameSet.add(name);
        }
      }
      this.fromNamesOptions = Array.from(tempFromNameSet);
    },
    getCompanyNameFilterOptions() {
      this.companyNamesOptions = [];
      let tempCompNameSet = new Set();
      let compNameSet = new Set();

      for (let i = 0; i < this.postList.length; i++) {
        let companyName = this.postList[i].company.name;

        if (!compNameSet.has(companyName)) {
          tempCompNameSet.add({ value: companyName });
          compNameSet.add(companyName);
        }
      }
      this.companyNamesOptions = Array.from(tempCompNameSet);
    },
    handeDeletePost(id, title) {
      this.deleteDialogVisible = true;
      this.deleteForm.postId = id;
      this.deletedTitle = title;
    },
    emptyPostListQuery() {
      this.listQuery.id = undefined;
      this.listQuery.title = "";
      this.listQuery.fromNames = [];
      this.listQuery.companyNames = [];
      this.listQuery.companyId = undefined;
      this.searchPost();
    },
    handleRouteList() {
      this.$router.push({
        path: this.$route.path,
        query: formatListQuery(this.listQuery),
      });
    },
    handleViewPost(companyId, postId) {
      let url =
        process.env.VUE_APP_WEB_HOST +
        `/company/${companyId}/community/postdetail?postId=${postId}`;
      window.open(url);
    },
    forbiddenUser() {
      this.forbiddenForm.notice = Number(this.forbiddenForm.notice);
      forbiddenUser(this.forbiddenForm).then(() => {
        Toast.success("成功禁言该用户");
        this.forbiddenName = "";
        this.forbiddenForm.userId = undefined;
        this.forbiddenForm.userIds = [];
        this.forbiddenForm.content = "";
        this.forbiddenForm.daysId = undefined;
        this.forbiddenForm.notice = true;
        this.initData();
      });
    },
    unforbiddenUser() {
      this.unforbiddenForm.notice = Number(this.unforbiddenForm.notice);
      unforbiddenUser(this.unforbiddenForm).then(() => {
        Toast.success("成功禁言该用户");
        this.unforbiddenName = "";
        this.unforbiddenForm.userId = undefined;
        this.unforbiddenForm.userIds = [];
        this.unforbiddenForm.content = "";
        this.unforbiddenForm.daysId = undefined;
        this.unforbiddenForm.notice = true;
        this.initData();
      });
    },
    forbiddenAllUser() {
      for (let i = 0; i < this.multipleSelection.length; i++) {
        let id = this.multipleSelection[i].fromUser.userId;
        this.forbiddenForm.userIds.push(id);
      }
      this.forbiddenForm.notice = Number(this.forbiddenForm.notice);
      forbiddenAllUser(this.forbiddenForm).then(() => {
        Toast.success("成功批量禁言用户");
        this.forbiddenForm.userId = undefined;
        this.forbiddenForm.userIds = [];
        this.forbiddenForm.content = "";
        this.forbiddenForm.daysId = undefined;
        this.forbiddenForm.notice = true;
        this.initData();
      });
    },
    deletePost() {
      deletePost(this.deleteForm).then(() => {
        Toast.success("成功删除该帖子");
        this.deletedTitle = "";
        this.deleteForm.postId = undefined;
        this.deleteForm.postIds = [];
        this.deleteForm.content = "";
        this.initData();
      });
    },
    deleteAll() {
      for (let i = 0; i < this.multipleSelection.length; i++) {
        let id = this.multipleSelection[i].id;
        this.deleteForm.postIds.push(id);
      }
      deleteAll(this.deleteForm).then(() => {
        Toast.success("成功批量删除帖子");
        this.deletedTitle = "";
        this.deleteForm.postId = undefined;
        this.deleteForm.postIds = [];
        this.deleteForm.content = "";
        this.initData();
      });
    },
    handleManageComment(id) {
      this.$router.push({ path: "/wiki/community-post/comment/", query: { ownerId: id } });
    },
    handleSetRecommend(id, recommend) {
      let data = {
        id: id,
        recommend: recommend,
      };
      savePost(data).then(() => {
        Toast.success("成功修改精品属性");
        this.searchPost();
      });
    },
    handleClick(tab, event) {
      const status = parseInt(tab.name);
      if (status == 1) {
        this.listQuery.sort = "+create_time";
        this.listQuery.recommend = undefined;
      } else if (status == 2) {
        this.listQuery.sort = "-create_time";
        this.listQuery.recommend = undefined;
      } else {
        this.listQuery.recommend = 1;
        this.listQuery.sort = "+create_time";
      }
      this.searchPost();
    },
  },
};
</script>

<style lang="scss" scoped>
.community-post-container {
  width: 100%;
}
</style>
