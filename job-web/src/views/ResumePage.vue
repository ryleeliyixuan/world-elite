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
  name: "ResumePage",
  components: { ResumeView },
  data() {
    return {
      resumeId: undefined,
      exporting: false
    };
  },
  created() {
    this.resumeId = this.$route.params.id;
    this.$emit("complete");
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
  left: calc((100% - 900px) * 0.5 + 920px);
  background: #fff;
  border: 1px solid #eee;
}
</style>
