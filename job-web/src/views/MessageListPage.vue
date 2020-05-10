<template>
  <div class="app-container">
    <h5>系统消息</h5>
    <div class="message-list mt-4" v-if="list && list.length != 0">
      <el-button icon="el-icon-delete" type="danger" size="mini" @click="handleDeleteAll">清除全部</el-button>
      <div class="mb-2 mt-3 pt-3 pb-3 text-muted border-bottom" v-for="message in list" :key="message.id">
        <div class="d-flex">
          <el-badge is-dot v-if="message.readFlag == 0" />
          <div class="flex-fill">
            <b-col>{{message.content}}<el-link v-if="message.url && message.url != ''" :href="message.url" :underline="false">查看<i class="el-icon-d-arrow-right"></i></el-link></b-col>
          </div>
          <div>
              <el-link type="info" :underline="false" @click.stop="handleDeleteMessage(message.id)"><i class="el-icon-close"></i></el-link>
          </div>
        </div>
      </div>
    </div>
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
import { getMessageList, readAllMessage, deleteMessage, deleteAllMessage } from "@/api/message_api";
import { mapMutations } from "vuex";
import Pagination from "@/components/Pagination";
import Toast from '@/utils/toast'

export default {
  name: "MessageListPage",
  components: { Pagination },
  data() {
    return {
      listQuery: {
        page: 1,
        limit: 20
      },
      list: [],
      total: 0
    };
  },
  created() {
    this.getList();
  },
  methods: {
    ...mapMutations({
      setMessageCount: "setting/MESSAGE_COUNT"
    }),
    getList() {
      getMessageList(this.listQuery).then(response => {
        const { data } = response;
        this.list = data.list;
        this.total = data.total;
        if (this.listQuery.page === 1) {
          readAllMessage().then(() => {
              this.setMessageCount(0)
          });
        }
      });
    },
    handleDeleteMessage(id){
      deleteMessage(id).then(() => {
        this.getList();
      })
    },
    handleDeleteAll(){
      this.$confirm("确定要清除所有消息？", {
        confirmButtonText: "清除"
      }).then(() => {
        deleteAllMessage().then(() => {
          Toast.success("操作成功");
          this.getList();
        });
      });
    }
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.app-container {
  width: 800px;
  margin: 0 auto;
}
.border-bottom{
   border-bottom: #EEE 1px solid
}
</style>
