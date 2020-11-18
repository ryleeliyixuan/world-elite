<template>
  <div>
    <h3>
      {{title}}
      <el-link class="edit-text" type="primary" v-on:click="$emit('edit-click')" :icon="icon">
        {{iconText}}
      </el-link>
    </h3>
  </div>
</template>

<script>
export default {
  name: "EditResumeTitle",
  props: {
    title: {
      type: String,
      default: ""
    },
    icon: {
      type: String
    },
    iconText: {
      type: String,
      default: ""
    }
  }
};
</script>

<style scoped lang="scss">
h3 {
  font-size: 22px;
}
.edit-text{
  float: right;
  font-size: 16px;
}
</style>