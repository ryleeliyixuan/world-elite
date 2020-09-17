<template>
  <div class="app-container">
    <h3 class="text-center">用户协议和隐私政策</h3>
    <div v-html="content" class="mt-3"></div>
  </div>
</template>

<script>
import { getUserProtocol } from "@/api/config_api";

export default {
  name: "UserProtocolPage",
  data() {
    return {
      content: "",
    };
  },
  created() {
    getUserProtocol().then((response) => {
      this.content = response.data;
    });
  },
};
</script>


<style scoped lang="scss">
.app-container {
  width: 100%;
  padding: 5%;
}

@media screen and (max-width: 440px) {
  .app-container {
    width: 100%;
    padding: 5%;

    .text-center {
      font-size: 24px;
    }
  }
}
</style>
