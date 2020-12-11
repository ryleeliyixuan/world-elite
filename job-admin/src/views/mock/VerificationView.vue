<template>
  <div class="app-container" v-if="verification">
    <h2>{{verification.name}}</h2>
    <p>
      <b>身份证：</b>
      {{verification.idNumber}}
    </p>

    <p>身份证人像面</p>
    <el-image style="width: 300px;" :src="verification.faceUrl"></el-image>
    <p>身份证国徽面</p>
    <el-image style="width: 500px;" :src="verification.emblemUrl"></el-image>
    <p>手持身份证</p>
    <el-image style="width: 500px;" :src="verification.holdUrl"></el-image>
  </div>
</template>

<script>
import { getInterviewerVerifyInfo } from "@/api/verify_api";

export default {
  name: "VerificationView",
  props: {
    userId: {
      required: true
    }
  },
  data() {
    return {
      verification: undefined
    };
  },
  watch: {
    userId() {
      this.getInterviewerVerifyInfo();
    }
  },
  created() {
    this.getInterviewerVerifyInfo();
  },
  methods: {
    getInterviewerVerifyInfo() {
      if (this.userId) {
        getInterviewerVerifyInfo(this.userId).then(response => {
          this.verification = response.data;
        });
      }
    }
  }
};
</script>
