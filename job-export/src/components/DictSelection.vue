<template>
  <el-select
    v-model="value"
    filterable
    clearable
    :placeholder="placeholder"
    @change="handleChange"
    :size="size"
  >
    <el-option :key="unlimitOption.id" :label="unlimitOption.name" :value="unlimitOption.id"></el-option>
    <el-option v-for="item in options" :key="item.id" :label="item.name" :value="item.id"></el-option>
  </el-select>
</template>

<script>
import { OPTION_UNLIMIT } from "@/utils/common";

export default {
  name: "DictSelection",
  props: {
    model: {
      prop: "value",
      event: "change"
    },
    placeholder: {
      type: String,
      default: ""
    },
    value: {
      type: Number
    },
    size: {
      type: String
    },
    options: {
      type: Array
    }
  },
  data() {
    return {
      unlimitOption: {
        id: OPTION_UNLIMIT,
        name: "不限"
      }
    };
  },
  methods:{
    handleChange(){
      this.$emit('change', this.value);
    }
  }
};
</script>