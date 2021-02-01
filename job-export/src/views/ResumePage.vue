<template>
      <ResumeView :resumeDetail="resumeDetail"></ResumeView>
</template>

<script>
  import ResumeView from "@/components/ResumeView";
  import { exportResumeToPdf } from "@/api/export_api";
  import { downloadFile } from "@/utils/common";
  import {getResumeDetail} from "@/api/resume_api";

  export default {
    name: "ResumePage",
    components: { ResumeView },
    data() {
      return {
        resumeId: undefined,
        resumeDetail:undefined,
        exporting: false
      };
    },
    created() {
      this.resumeId = this.$route.params.id;
      getResumeDetail(this.resumeId).then(response => {
        this.resumeDetail = response.data;
      });
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
    position: absolute;
    left: 70%;
    top: 118%;
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
