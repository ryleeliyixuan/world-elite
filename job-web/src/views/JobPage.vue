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
        <el-button
          type="primary"
          :icon="job.favoriteFlag == 1? 'el-icon-star-on' : 'el-icon-star-off'"
          plain
          :loading="favoriteLoading"
          @click="handleFavorite"
        >{{job.favoriteFlag == 1? '已收藏': '收藏岗位'}}</el-button>
        <el-button
          type="primary"
          :loading="applyLoading"
          icon="el-icon-s-promotion"
          @click="handleAppleJob"
          v-bind:disabled="job.applyFlag == 1"
        >{{job.applyFlag == 1? '已申请' : '申请岗位'}}</el-button>
      </b-col>
    </b-row>
    <el-divider />
    <b-row>
      <b-col class="mr-4">
        <div v-html="job.description"></div>
        <div v-if="job.address" class="mt-4">
          <h5 class="mt-4 mb-4">工作地址</h5>
          <p>
            {{job.address}}
          </p>
        </div>
        <div>
          <h5 class="mt-4 mb-4">分享该职位</h5>
          <share :config="shareConfig"></share>
        </div>
      </b-col>
      <b-col cols="4" v-if="job.companyUser && job.companyUser.company">
        <el-link :href="`/company/${job.companyUser.company.id}`" :underline="false">
          <b-row no-gutters align-v="end">
            <b-col cols="4">
              <b-img :src="job.companyUser.company.logo" class="w-100"></b-img>
            </b-col>
            <b-col class="ml-2">
              <div class="company-text">{{job.companyUser.company.name}}</div>
            </b-col>
          </b-row>
        </el-link>
        <div v-if="job.companyUser.company.industry" class="text-gray text-small mt-4">
          <i class="el-icon-menu mr-2"></i>
          {{job.companyUser.company.industry.name}}
        </div>
        <div v-if="job.companyUser.company.stage && job.companyUser.company.property" class="text-gray text-small mt-2">
          <i class="el-icon-collection-tag mr-2"></i>
          {{job.companyUser.company.property.name}} . {{job.companyUser.company.stage.name}}
        </div>
        <div v-if="job.companyUser.company.scale" class="text-gray text-small mt-2">
          <i class="el-icon-user mr-2"></i>
          {{job.companyUser.company.scale.name}}
        </div>
        <div v-if="job.companyUser.company.homepage" class="text-gray text-small mt-2">
          <i class="el-icon-s-home mr-2"></i>
          <el-link :href="companyLink" target="_blank" :underline="false">{{companyLinkName}}</el-link>
        </div>
      </b-col>
    </b-row>
  </div>
</template>

<script>
import Vue from "vue";
import VueAMap from "vue-amap";
import { getJobInfo, applyJob } from "@/api/job_api";
import { doFavorite } from "@/api/favorite_api";
import { setPageTitle } from "@/utils/setting";
import Share from "vue-social-share";
import "vue-social-share/dist/client.css";

Vue.use(Share);
Vue.use(VueAMap);

VueAMap.initAMapApiLoader({
  key: process.env.VUE_APP_AMAP_KEY,
  v: "1.4.4"
});

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
    handleFavorite() {
      this.favoriteLoading = true;
      this.favoriteForm.favorite = !this.favoriteForm.favorite;
      doFavorite(this.favoriteForm)
        .then(() => {
          this.job.favoriteFlag = this.favoriteForm.favorite ? 1 : 0;
          this.$message("操作成功");
        })
        .finally(() => {
          this.favoriteLoading = false;
        });
    },
    handleAppleJob() {
      this.applyLoading = true;
      applyJob(this.job.id)
        .then(() => {
          this.job.applyFlag = 1;
          this.$message("申请成功");
        })
        .finally(() => {
          this.applyLoading = false;
        });
    }
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
