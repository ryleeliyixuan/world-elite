<template>
  <div class="app-container">
    <el-alert title="下载数据两小时自动清理一次" type="info"></el-alert>
    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%; margin-top:10px"
    >
      <el-table-column label="下载任务" prop="name"></el-table-column>
      <el-table-column label="进度" align="center">
        <template slot-scope="{row}">
          <el-progress :percentage="row.progress" :status="row.progress == 100 ? 'success' : ''"></el-progress>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" prop="createTime"></el-table-column>
      <el-table-column label="操作">
        <template slot-scope="{row}">
          <el-button
            v-if="row.progress==100"
            type="primary"
            @click="downloadFile(row.url)"
            icon="el-icon-download"
          >下载</el-button>
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
  </div>
</template>

<script>
import { getDownloadList } from "@/api/download_api";
import waves from "@/directive/waves"; // waves directive
import Pagination from "@/components/Pagination"; // secondary package based on el-pagination
import { getDownloadUrl } from '@/api/file_api'

export default {
  name: "DownloadList",
  components: { Pagination },
  directives: { waves },
  filters: {
    statusFilter(status) {
      const statusMap = {
        0: "success",
        1: "warning",
        3: "danger"
      };
      return statusMap[status];
    }
  },
  data() {
    return {
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        sort: "-id"
      },
      autoTimeoutId: undefined
    };
  },
  created() {
    this.getList();
  },
  watch: {
    list() {
      this.refreshTimer();
    }
  },
  methods: {
    getList() {
      this.listLoading = true;
      getDownloadList(this.listQuery).then(response => {
        this.list = response.data.list;
        this.total = response.data.total;
        this.listLoading = false;
      });
    },
    downloadFile(key) {
      const downloadFileUrl = getDownloadUrl({fileKey: key, fileName: key})
      this.$notify({
        title: `下载的文件是: ${key} `,
        type: "success",
        dangerouslyUseHTMLString: true,
        duration: 1000,
        offset: 60,
        message: `<iframe src='${downloadFileUrl}' style="display:none"></iframe>`
      });
    },
    destroyed() {
      if (this.autoTimeoutId) {
        clearTimeout(this.autoTimeoutId);
      }
    },
    refreshTimer() {
      if (this.autoTimeoutId) {
        clearTimeout(this.autoTimeoutId);
      }
      this.autoTimeoutId = setTimeout(() => {
        this.getList();
      }, 5000);
    }
  }
};
</script>
