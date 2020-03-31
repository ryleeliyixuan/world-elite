<template>
  <div class="app-container">
    <div class="intro-box">
      <b-img :src="picUrl" class="app-bg-img"></b-img>
      <div class="slogon-box">
        <h1 class="slogon">留学生专属招聘平台</h1>
        <p class="slogon-description mt-2">轻松找到心仪的国内工作和实习机会</p>
        <el-button type="primary" round class="button-join" @click="handleJoin">马上加入</el-button>
      </div>
    </div>
    <div class="content-box">
      <h4 class="mb-4">每日职位精选</h4>
      <div class="recommend-jobs">
        <el-row :gutter="20">
          <el-col
            :span="8"
            class="job-item"
            v-for="recommendJob in recommendJobList"
            :key="recommendJob.id"
          >
            <el-card shadow="hover" class="link-pointer" v-if="recommendJob.object">
              <el-link
                :href="`/job/${recommendJob.object.id}`"
                :underline="false"
                style="display: block"
              >
                <h6>
                  {{recommendJob.object.name}}
                  <span
                    class="text-danger float-right"
                  >{{`${recommendJob.object.minSalary} - ${recommendJob.object.maxSalary}K`}}</span>
                </h6>
                <div
                  class="text-gray text-small"
                >{{`${recommendJob.object.city? recommendJob.object.city.name : ''} / ${recommendJob.object.minDegree?recommendJob.object.minDegree.name:''}`}}</div>
              </el-link>
              <el-divider></el-divider>
              <el-link
                :href="`/company/${recommendJob.object.companyUser.company.id}`"
                :underline="false"
                v-if="recommendJob.object.companyUser && recommendJob.object.companyUser.company"
              >
                <b-media>
                  <template v-slot:aside>
                    <el-image
                      class="company-logo"
                      :src="recommendJob.object.companyUser.company.logo"
                    ></el-image>
                  </template>
                  <div class="mb-1 text-truncate">{{recommendJob.object.companyUser.company.name}}</div>
                  <div
                    class="text-gray text-small text-truncate"
                  >{{recommendJob.object.companyUser.company.industry?recommendJob.object.companyUser.company.industry.name:''}} / {{recommendJob.object.companyUser.company.stage?recommendJob.object.companyUser.company.stage.name:''}} / {{recommendJob.object.companyUser.company.scale?recommendJob.object.companyUser.company.scale.name:''}}</div>
                </b-media>
              </el-link>
            </el-card>
          </el-col>
        </el-row>
      </div>
      <h4 class="mb-4 mt-4">每日公司推荐</h4>
      <div class="recommend-companys">
        <el-row :gutter="20">
          <el-col
            :span="6"
            class="company-item"
            v-for="recommendCompany in recommendCompanyList"
            :key="recommendCompany.id"
          >
            <el-card shadow="hover" class="link-pointer text-center" v-if="recommendCompany.object">
              <el-link :href="`/company/${recommendCompany.object.id}`" :underline="false" style="display: block">
                <el-image class="company-logo" :src="recommendCompany.object.logo"></el-image>
                <h6 class="text-center text-truncate">{{recommendCompany.object.name}}</h6>
                <div class="text-gray text-small text-center text-truncate">
                  <span
                    v-if="recommendCompany.object.stage"
                  >{{recommendCompany.object.stage.name}} .</span>
                  <span
                    v-if="recommendCompany.object.property"
                  >{{recommendCompany.object.property.name}} .</span>
                  <span
                    v-if="recommendCompany.object.industry"
                  >{{recommendCompany.object.industry.name}} .</span>
                  <span v-if="recommendCompany.object.scale">{{recommendCompany.object.scale.name}}</span>
                </div>
                <div
                  class="text-gray text-small text-center mt-2 text-truncate"
                >{{recommendCompany.object.synopsis}}</div>
              </el-link>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </div>
  </div>
</template>

<script>
import { getRecommendList } from "@/api/recommend_api";

export default {
  name: "HomePage",
  created() {
    this.initData();
  },
  data() {
    return {
      recommendJobList: [],
      recommendCompanyList: [],
      picUrl:
        "http://worldelite-debug.oss-cn-shanghai.aliyuncs.com/static/home-ad.svg"
    };
  },
  methods: {
    initData() {
      getRecommendList({
        objectType: 1,
        page: 1,
        limit: 9,
        sort: "+position"
      }).then(response => {
        this.recommendJobList = response.data.list;
      });
      getRecommendList({
        objectType: 2,
        page: 1,
        limit: 12,
        sort: "+position"
      }).then(response => {
        this.recommendCompanyList = response.data.list;
      });
    },
    handleJoin() {
      this.$router.push("/register");
    }
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.intro-box {
  position: relative;
  width: 1200px;
  margin: 0 auto;
  height: calc(100vh - 110px);
}

.intro-box .app-bg-img {
  position: absolute;
  width: 500px;
  height: 500px;
  right: 100px;
  z-index: -1;
}

.intro-box .slogon-box {
  padding-top: 180px;
}

.job-item,
.company-item {
  margin-bottom: 20px;
}

.job-item .company-logo {
  width: 50px;
  height: 50px;
}

.company-item .company-logo {
  width: 100px;
  height: 100px;
}

.content-box {
  width: 1200px;
  margin: 0 auto;
}

.slogon {
  font-size: 60px;
  font-weight: 400;
  color: #333;
}

.slogon-description {
  font-size: 20px;
  font-weight: 350;
  color: #888;
}

.button-join {
  width: 200px;
  height: 50px;
  border-radius: 50px;
  font-size: 20px;
}
</style>
