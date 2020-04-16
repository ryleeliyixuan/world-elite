<template>
  <div class="container">
      <h3 class="text-center">用户协议和隐私政策</h3>
      <div v-html="content" class="mt-3"></div>
  </div>
</template>

<script>

import { getUserProtocol } from '@/api/config_api'

export default {
  name: "ProtocolPage",
  data() {
    return {
        content: ''
    }
  },
  created() {
     getUserProtocol().then(response => {
         this.content = response.data;
     })
  }
};
</script>
