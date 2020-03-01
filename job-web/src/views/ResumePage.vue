<template>
   <div class="resume-page">
       <ResumeView :resumeId="resumeId"></ResumeView>
   </div>
</template>

<script>
 
import ResumeView from "@/components/ResumeView";

export default {
  name: 'ResumePage',
  components: { ResumeView },
  data(){
      return {
          resumeId: undefined,
      }
  },
  created(){
      this.resumeId = this.$route.params.id
  }
}
</script>

<style scoped>
.resume-page{
    width: 900px;
    margin: 20px auto;
    border: 1px solid #EEE;
    padding: 30px 20px;
}
</style>
