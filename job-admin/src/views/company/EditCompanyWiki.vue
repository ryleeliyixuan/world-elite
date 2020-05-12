<template>
  <div class="app-container wiki-container">
    <h3 v-if="company">{{company.fullName}} - 企业百科</h3>
    <EditorView v-bind:content="wikiForm.content" v-on:updateContent="wikiForm.content = $event"/>
    <el-input
        class="mt-2"
        type="textarea"
        maxlength="500"
        placeholder="请输入百科摘要，不输入则摘取正文前 150 个字符"
        show-word-limit
        v-model="wikiForm.summary"
      ></el-input>
    <el-button type="primary" @click="handleSave" class="mt-2" :loading="saveLoading">保存</el-button>
  </div>
</template>

<script>
import EditorView from '@/components/EditorView'
import { getCompanyWiki, saveCompanyWiki } from "@/api/company_api";
import Toast from "@/utils/toast";

export default {
  name: "EditCompanyWiki",
  components: {
    EditorView
  },
  created() {
    this.initData();
  },
  data() {
    return {
      company: undefined,
      saveLoading: false,
      wikiForm: {
        companyId: undefined,
        summary: undefined,
        content: undefined
      },
    };
  },
  methods: {
    initData() {
      const companyId = this.$route.query.id;
      getCompanyWiki(companyId).then(response => {
        const { data } = response;
        this.company = data.company;
        this.wikiForm.companyId = data.company.id;
        this.wikiForm.content = data.content;
        this.wikiForm.summary = data.summary;
      });
    },
    handleSave() {
      this.saveLoading = true;
      saveCompanyWiki(this.wikiForm)
        .then(() => {
          Toast.success("保存成功");
          this.initData();
        })
        .finally(() => {
          this.saveLoading = false;
        });
    }
  }
};
</script>

<style lang="scss">
.wiki-container {
  margin: 0 120px;
  .ql-container .ql-editor {
    height: calc(100vh - 220px);
    font-size: 15px;
  }
}
</style>