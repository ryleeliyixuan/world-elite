<template>
  <div class="app-container">
    <h5>系统消息</h5>
    <div class="message-list mt-4" v-if="list && list.length != 0">
      <div class="mb-2 pt-3 pb-3 text-muted border-bottom" v-for="message in list" :key="message.id">
        <el-badge is-dot v-if="message.readFlag == 0" />
        {{message.content}}
        <el-link v-if="message.url && message.url != ''" :href="meesage.url" :underline="false">查看</el-link>
      </div>
    </div>
    <pagination
      v-show="total"
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.limit"
      @pagination="getList"
    />
  </div>
</template>

<script>
import { getMessageList, readAllMessage } from "@/api/message_api";
import { mapMutations } from "vuex";
import Pagination from "@/components/Pagination";

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
