<template>
   <div>
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
      this.resumeId = this.$route.query.resumeId
  },
  methods: {
  }
}
</script>

<style scoped>
</style>
