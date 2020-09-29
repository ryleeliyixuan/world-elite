<template>
  <div class="app-container">
    <div v-if="result">
      <h5 class="text-danger">
        <i class="el-icon-warning" />
        <span v-if="result.status == 1">我们将尽快完成审核，请联系专属客服跟进审核进度，客服微信：dwj0826</span>
        <span v-else-if="result.status === 2">你已成功通过申请</span>
        <span v-else>审核被拒绝，原因是：</span>
      </h5>
      <div v-if="result.status == 3" class="p-2">
        <p>{{result.remark}}</p>
        <p>请按照提示再次修改，重新进行提交。</p>
        <p>
          <el-link href="/verify" type="primary" :underline="false">修改资料</el-link>
        </p>
      </div>
      <div v-if="result.status == 2" class="p-2">
         <p>重新登录后即可开始发布招聘职位。</p>
         <p>
          <el-link href="/login" type="primary" :underline="false">重新登录</el-link>
        </p>
      </div>
    </div>
    <div v-else>
        <p>你还没有提交资料，请先提交提交申请资料</p>
        <p>
          <el-link href="/verify" type="primary" :underline="false">申请入驻</el-link>
        </p>
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
    min-height: calc(100vh - 430px);
}
</style>
