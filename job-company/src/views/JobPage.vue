<template>
  <div class="container app-container">
    <b-row align-v="center" v-if="job">
      <b-col>
        <div
          class="text-gray text-small"
          v-if="job.companyUser && job.companyUser.company"
        >{{job.companyUser.company.name}}招聘</div>
        <h2 class="mt-3">
          {{job.name}}
          <span
            class="text-danger ml-4 salary-text"
          >{{job.salary.name}}{{job.salaryMonths? ` × ${job.salaryMonths}个月` : ''}}</span>
        </h2>
        <div
          class="mt-2"
        >{{job.city? job.city.name: ''}} / {{job.minDegree? job.minDegree.name: ''}} / {{job.jobType? job.jobType.name: ''}}</div>
        <div class="mt-2 text-gray text-small">{{job.time}}</div>
      </b-col>
      <b-col cols="4"></b-col>
    </b-row>
    <el-divider />
    <b-row>
      <b-col class="mr-4" cols="8" v-if="job">
        <div v-html="job.description"></div>
        <div v-if="job.address" class="mt-4">
          <h5 class="mt-4 mb-4">工作地址</h5>
          <p>{{job.address}}</p>
        </div>
        <div
          class="recommend-resume-list mt-4"
          v-if="recommendResumeList && recommendResumeList.length != 0"
        >
          <h5 class="mt-4 mt-5">推荐简历</h5>
          <el-card class="box-card mb-3" v-for="resume in recommendResumeList" :key="resume.id" shadow="hover">
            <b-media @click="gotoResumeDetail(resume)" style="cursor: pointer;">
              <b-media-body>
                <h6>
                  {{resume.name}}
                  <span
                    class="ml-4 text-muted text-small"
                  >{{resume.age}}岁 / {{resume.curPlace}}</span>
                </h6>
                <div v-if="resume.maxResumeEdu" class="text-small mt-2 mb-2">
                  <span>{{ resume.maxResumeEdu.schoolName }} / {{ resume.maxResumeEdu.majorName }}</span> <span v-if="resume.maxResumeEdu.degree">.{{resume.maxResumeEdu.degree.name}}</span>
                  <span v-if="resume.maxResumeEdu.gpa">/ GPA {{resume.maxResumeEdu.gpa}}</span>
                </div>
                <div
                  class="tag-group"
                  v-if="resume.resumeSkillList && resume.resumeSkillList.length !== 0"
                >
                  <el-tag
                    size="small"
                    effect="plain"
                    type="info"
                    class="mr-1"
                    v-for="skill in resume.resumeSkillList"
                    :key="skill.id"
                  >{{skill.name}}</el-tag>
                </div>
              </b-media-body>
            </b-media>
          </el-card>
        </div>
      </b-col>
    </b-row>
  </div>
</template>

<script>
import { getJobInfo, getJobRecommendResumes } from "@/api/job_api";
import { setPageTitle } from "@/utils/setting";

export default {
  name: "JobPage",
  data() {
    return {
      job: undefined,
      favoriteForm: {
        objectId: undefined,
        type: 1,
        favorite: false
      },
      mapZoom: 14,
      mapDialogVisible: false,
      favoriteLoading: false,
      applyLoading: false,
      shareConfig: {
        source: "",
        title: "",
        description: "",
        image: "",
        sites: ["wechat", "qq", "weibo", "google", "facebook", "twitter"]
      },
      recommendResumeList: []
    };
  },
  created() {
    this.initData();
  },
  watch: {
    $route() {
      this.initData();
    }
  },
  computed: {
    companyLink() {
      return this.job.companyUser.company.homepage.startsWith("http")
        ? this.job.companyUser.company.homepage
        : "http://" + this.job.companyUser.company.homepage;
    },
    companyLinkName() {
      return this.job.companyUser.company.homepage.replace(/http(s)?:\/\//);
    }
  },
  methods: {
    initData() {
      const jobId = this.$route.params.id;
      getJobInfo(jobId).then(response => {
        this.job = response.data;
        const title = `${this.job.name} - ${
          this.job.companyUser.company ? this.job.companyUser.company.name : ""
        }`;
        setPageTitle(title);
        this.shareConfig.title = title;
        this.shareConfig.description = this.job.description;
        this.favoriteForm.objectId = this.job.id;
        this.favoriteForm.favorite = this.job.favoriteFlag == 1;
      });
      getJobRecommendResumes(jobId).then(response => {
        this.recommendResumeList = response.data;
      });
    },
    gotoResumeDetail(resume){
      this.$router.push(`/resume?resumeId=${resume.id}`)
    }
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.app-container {
  margin: 30px auto;
  min-height: calc(100vh - 430px);
}
.salary-text {
  font-size: 20px;
}
.company-text {
  font-size: 18px;
}
.map-box {
  height: 400px;
}
.text-small {
  font-size: 14px;
}
.close-text {
  font-size: 30px;
}
</style>
