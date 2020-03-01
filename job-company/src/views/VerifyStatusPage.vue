<template>
  <div class="app-container">
    <h5 class="text-danger" v-if="result">
      <i class="el-icon-warning" />
      <span v-if="result.status == 1">账号正在审核中，有问题可联系xxx</span>
      <span v-else-if="result.status === 2">你已成功通过申请</span>
      <span v-else>审核被拒绝，原因是：</span>
    </h5>
     <div v-if="result.status == 3" class="p-2">
      <p>{{result.remark}}</p>
      <p>请按照提示再次修改，重新进行提交。</p>
      <p><el-link href="/verify" type="primary" :underline="false">修改资料</el-link></p>
     </div>
  </div>
</template>

<script>
import { getVerifyInfo } from "@/api/verify_api";

export default {
  name: "VerifyStatusPage",
  created() {
    getVerifyInfo().then(response => {
      this.result = response.data;
    });
  },
  data() {
    return {
      result: undefined
    };
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.app-container {
  width: 900px;
  margin: 30px auto;
}
</style>
