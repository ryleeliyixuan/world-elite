<template>
  <div class="app-container">
    <div class="resume-page mt-2">
      <ResumeView :resumeId="resumeId"></ResumeView>
    </div>
    <div class="operate-box">
      <el-button
        type="primary"
        icon="el-icon-document"
        @click="exportPdf"
        :loading="exporting"
      >导出PDF</el-button>
    </div>
  </div>
</template>

<script>
import ResumeView from "@/components/ResumeView";
import { exportResumeToPdf } from "@/api/export_api";
import { downloadFile } from "@/utils/common";

export default {
  name: "ResumePreviewPage",
  components: { ResumeView },
  data() {
    return {
      resumeId: undefined,
      exporting: false
    };
  },
  created() {
    this.resumeId = this.$route.params.id;
  },
  methods: {
    exportPdf() {
      this.exporting = true;
      exportResumeToPdf(this.resumeId)
        .then(response => {
          downloadFile({
            fileKey: response.data,
            fileName: `${this.$store.getters.name}_个人简历.pdf`,
            success: ()=>{
              this.exporting = false;
            }
          });
        }).catch(() => {
          this.exporting = false;
        });
    }
  }
};
</script>

<style scoped>
.resume-page {
  width: 900px;
  margin: 20px auto;
  border: 1px solid #eee;
  padding: 30px 20px;
}
.operate-box {
  padding: 20px;
  position: fixed;
  bottom: 20px;
  left: 70%;
  background: #fff;
  border: 1px solid #eee;
}

@media screen and (max-width: 440px) {
  .app-container {
    width: 95%;
    margin: auto;
    min-height: calc(100vh - 477px);
  }

  .resume-page {
    width: 100%;
  }

  .operate-box {
    left: 55%;
  }
}
</style>
