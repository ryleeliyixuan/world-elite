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
          >{{`${job.minSalary}K - ${job.maxSalary}K`}}{{job.salaryMonths? ` × ${job.salaryMonths}个月` : ''}}</span>
        </h2>
        <div class="mt-2">{{job.city? job.city.name: ''}} / {{job.minDegree? job.minDegree.name: ''}} / {{job.jobType? job.jobType.name: ''}}</div>
        <div class="mt-2 text-gray text-small">{{job.time}}</div>
      </b-col>
      <b-col cols="4">
      </b-col>
    </b-row>
    <el-divider />
    <b-row>
      <b-col class="mr-4" cols="8">
        <div v-html="job.description"></div>
        <div v-if="job.address" class="mt-4">
          <h5 class="mt-4 mb-4">工作地址</h5>
          <p>
            {{job.address}}
          </p>
        </div>
      </b-col>
    </b-row>
  </div>
</template>

<script>
import { getJobInfo } from "@/api/job_api";
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
      }
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
    },
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.app-container {
  margin: 30px auto;
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
</style>
