<template>
  <div class="app-container">
      <h3 class="mb-4">用户端首页设置</h3>
      <el-form :model="webHomeForm" label-width="100px" class="demo-ruleForm">
        <el-form-item label="主标题">
          <el-input v-model="webHomeForm.mainTitle"></el-input>
        </el-form-item>
        <el-form-item label="附标题">
          <el-input v-model="webHomeForm.subTitle"></el-input>
        </el-form-item>
         <el-form-item label="背景图片">
          <el-input v-model="webHomeForm.picUrl"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="saveWebHomeConfig">保存</el-button>
        </el-form-item>
      </el-form>
      <el-divider></el-divider>
      <h3 class="mb-4 mt-4">企业端首页设置</h3>
      <el-form :model="companyHomeForm" label-width="100px" class="demo-ruleForm">
        <el-form-item label="主标题">
          <el-input v-model="companyHomeForm.mainTitle"></el-input>
        </el-form-item>
        <el-form-item label="附标题">
          <el-input v-model="companyHomeForm.subTitle"></el-input>
        </el-form-item>
         <el-form-item label="背景图片">
          <el-input v-model="companyHomeForm.picUrl"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="saveCompanyHomeConfig">保存</el-button>
        </el-form-item>
      </el-form>
  </div>
</template>

<script>
import { saveHomeConfig, getHomeConfig } from '@/api/config_api'
import Toast from "@/utils/toast";

export default {
  name: "ConfigPage",
  data() {
    return {
       webHomeForm: {
         configType: 8,
         mainTitle: undefined,
         subTitle: undefined,
         picUrl: undefined,
       },
       companyHomeForm: {
         configType: 9,
         mainTitle: undefined,
         subTitle: undefined,
         picUrl: undefined,
       }
    };
  },
  created(){
    this.initData();
  },
  methods: {
     initData(){
        getHomeConfig(8).then(response => {
          if(response.data){
             this.webHomeForm = response.data
          }
        });
        getHomeConfig(9).then(response => {
          if(response.data){
            this.companyHomeForm = response.data
          }
        })
     },
     saveWebHomeConfig(){
        saveHomeConfig(this.webHomeForm).then(()=>{
          Toast.success('保存成功');
        })
     },
     saveCompanyHomeConfig(){
        saveHomeConfig(this.companyHomeForm).then(()=>{
          Toast.success('保存成功');
        })
     }
  }
};
</script>
