<template>
  <div class="app-container" v-if="verification">
    <h2>{{verification.name}}</h2>
    <p>
      <b>身份证：</b>
      {{verification.idCard}}
    </p>
    <el-image style="width: 300px;" :src="verification.idCardPic"></el-image>
    <p>{{verification.company}} . {{verification.post}}</p>
    <el-image style="width: 500px;" :src="verification.companyLicensePic"></el-image>
  </div>
</template>

<script>
import { getUserVerifyInfo } from "@/api/verify_api";

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
      this.getUserVerifyInfo();
    }
  },
  created() {
    this.getUserVerifyInfo();
  },
  methods: {
    getUserVerifyInfo() {
      if (this.userId) {
        getUserVerifyInfo(this.userId).then(response => {
          this.verification = response.data;
        });
      }
    }
  }
};
</script>
