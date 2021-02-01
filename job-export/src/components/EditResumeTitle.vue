<template>
  <b-row>
    <b-col cols="auto" class="mr-auto">
      <h3>{{title}}</h3>
    </b-col>
    <b-col cols="auto" align-self="center">
      <a class="edit-text" v-on:click="$emit('edit-click')">
        <font-awesome-icon :icon="icon" /> {{iconText}}
      </a>
    </b-col>
  </b-row>
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
        type: Array
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

</style>