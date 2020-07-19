<template>
  <div class="app-container container">
    <b-media class="mt-4" vertical-align="center" v-if="company">
      <template v-slot:aside>
        <b-img :src="company.logo" class="company-logo" :alt="company.fullName" v-if="company.logo"></b-img>
        <b-img blank blank-color="#ccc" class="company-logo" v-else></b-img>
      </template>
      <b-media-body>
        <b-row align-v="center">
          <b-col>
            <h5 class="mt-0">{{company.name}}</h5>
            <p class="mb-0">
              <span v-if="company.stage">{{company.stage.name}} .</span>
              <span v-if="company.property">{{company.property.name}} .</span>
              <span v-if="company.industry">{{company.industry.name}} .</span>
              <span v-if="company.scale">{{company.scale.name}}</span>
            </p>
            <div class="mt-2 mb-0" v-if="company.homepage">
              <el-link :href="companyLink" target="_blank" :underline="false">
                公司官网
                <i class="el-icon-position el-icon--right"></i>
              </el-link>
            </div>
          </b-col>
          <b-col  cols="4">
            <el-button
              type="primary"
              :icon="company.favoriteFlag == 1? 'el-icon-star-on' : 'el-icon-star-off'"
              plain
              :loading="favoriteLoading"
              @click="handleFavorite"
            >{{company.favoriteFlag == 1? '已收藏': '收藏企业'}}</el-button>
          </b-col>
        </b-row>
      </b-media-body>
    </b-media>
    <el-menu :default-active="tabIndex" mode="horizontal" @select="handleSelectTab">
      <el-menu-item index="intro">公司介绍</el-menu-item>
      <el-menu-item index="job">招聘岗位</el-menu-item>
      <el-menu-item index="activity">相关活动</el-menu-item>
    </el-menu>
    <div class="mt-4" v-if="company">
      <div class="intro-box" v-if="tabIndex == 'intro'">
        <div v-if="company.jobList && company.jobList.length !== 0">
          <h5 class="mt-4 mb-4">
            热招职位
            <el-link :underline="false" class="float-right" @click="handleSelectTab('job')">
              查看全部
              <i class="el-icon-arrow-right el-icon--right"></i>
            </el-link>
          </h5>
          <div class="recommend-jobs mb-4">
            <el-row :gutter="12">
              <el-col :span="8" v-for="job in company.jobList" :key="job.id">
                <el-card shadow="hover" @click.native="onJobClick(job)" class="link-pointer">
                  <h6>
                    {{job.name}}
                    <span
                      class="text-danger float-right"
                    >{{job.minSalary}}K - {{job.maxSalary}}K</span>
                  </h6>
                  <div
                    class="text-small text-gray"
                  >{{job.minDegree? job.minDegree.name: ''}} . {{job.city? job.city.name: ''}}</div>
                </el-card>
              </el-col>
            </el-row>
          </div>
        </div>
        <h5 class="mt-4 mb-4" v-if="company.introduction">公司简介</h5>
        <div class="introdution" v-html="company.introduction"></div>
        <h5 class="mt-4 mb-4" v-if="company.addressList && company.addressList.length != 0">公司地址</h5>
        <el-collapse
          v-model="activeAddress"
          accordion
          v-for="(addr, index) in company.addressList"
          :key="addr.id"
        >
          <el-collapse-item :title="addr.address" :name="index">
            <div class="map-box">
              <el-amap :vid="'amap' + index" :zoom="mapZoom" :center="addr.mapWindow.position">
                <el-amap-info-window
                  :position="addr.mapWindow.position"
                  :content="addr.mapWindow.content"
                ></el-amap-info-window>
              </el-amap>
            </div>
          </el-collapse-item>
        </el-collapse>
        <div v-if="company.companyWiki && company.companyWiki != ''">
          <h5 class="mt-4 mb-4">公司百科</h5>
          <div v-if="token && token != ''" class="introdution ql-editor">
            <div class="wiki_sidebar" v-html="company.wikiSidebar"></div>
            <div class="wiki_content" v-html="company.companyWiki"></div>
            <div style="clear: both;"></div>
          </div>
          <div v-else>
            <p>登录后查看百科</p>
            <el-button type="primary" @click="onLoginClick">马上登录</el-button>
          </div>
        </div>
      </div>
      <div class="row job-box" v-else-if="tabIndex == 'job'">
        <div class="col-lg-8 col-md-10 col-sm-12">
          <el-card
            shadow="hover"
            v-for="job in jobPage.list"
            :key="job.id"
            class="mb-2 link-pointer"
            @click.native="onJobClick(job)"
          >
            <h6>
              {{job.name}}
              <span
                class="text-danger float-right"
              >{{job.minSalary}}K - {{job.maxSalary}}K</span>
            </h6>
            <div
              class="text-small text-gray"
            >{{job.minDegree? job.minDegree.name: ''}} . {{job.city? job.city.name: ''}}</div>
          </el-card>
          <pagination
            v-show="total"
            :total="total"
            :page.sync="listQuery.page"
            :limit.sync="listQuery.limit"
            @pagination="getCompanyJobList"
          />
        </div>
      </div>
      <div class="activity-box" v-else>

      </div>
    </div>
  </div>
</template>


<script>
import Vue from "vue";
import VueAMap from "vue-amap";
import Pagination from "@/components/Pagination";

import { getCompanyInfo } from "@/api/company_api";
import { getCompanyJobList } from "@/api/job_api";
import { setPageTitle } from "@/utils/setting";
import { doFavorite } from "@/api/favorite_api";
import { mapGetters } from "vuex";

Vue.use(VueAMap);

VueAMap.initAMapApiLoader({
  key: process.env.VUE_APP_AMAP_KEY,
  v: "1.4.4"
});

export default {
  name: "CompanyHomePage",
  components: { Pagination },
  data() {
    return {
      companyId: undefined,
      company: undefined,
      total: 0,
      jobPage: {},
      tabIndex: "intro",
      activeAddress: 0,
      mapZoom: 14,
      listQuery: {
        companyId: undefined,
        page: 1,
        limit: 10,
        sort: "-pub_time"
      },
      favoriteForm: {
        objectId: undefined,
        type: 2,
        favorite: false
      },
      favoriteLoading: false
    };
  },
  created() {
    this.initData();
    window.addEventListener('scroll', this.handleScroll);
  },
  computed: {
    companyLink() {
      return this.company.homepage.startsWith("http")
        ? this.company.homepage
        : "http://" + this.company.homepage;
    },
    ...mapGetters(["token"])
  },
  watch: {
    $route() {
      this.initData();
    }
  },
  mounted: {

  },
  methods: {
    initData() {
      this.companyId = this.$route.params.id;
      this.listQuery.companyId = this.companyId;
      this.favoriteForm.objectId = this.companyId;
      getCompanyInfo(this.companyId).then(response => {
        this.company = response.data;
        setPageTitle(this.company.name);
        if (this.company.addressList) {
          for (const addr of this.company.addressList) {
            addr.mapWindow = {
              position: [addr.longitude, addr.latitude],
              content: addr.address
            };
          }
        }
      }).then(response => {
        if (this.company != null && this.company.companyWiki != null){
          let ulist = document.createElement("div");
          let content = document.createElement("div");
          content.innerHTML = this.company.companyWiki;

          const url = "/company/" + this.$route.params.id;

          // crete table of contents
          let idIndex = 0;
          let h_list = content.querySelectorAll("h3, h4, h5, h6");
          for (let i = 0; i < h_list.length; i++){
            let h = h_list[i];
            // get plain text
            let text = h.innerText;
            // assign id
            let idAtt = document.createAttribute("id");
            idAtt.value = "s" + idIndex++;
            h.setAttributeNode(idAtt);

            // create list item and link
            let item = document.createElement("li");
            let link = document.createElement("a");
            let hrefAtt = document.createAttribute("href");
            hrefAtt.value = url + "#" + h.id;

            link.setAttributeNode(hrefAtt);
            let num = parseInt(h.tagName.substring(1,2), 10)-2;
            for (let j = 1; j < num; j++){
              link.innerHTML += "&emsp;";
            }
            link.innerHTML += text;
            // append
            item.appendChild(link);
            ulist.appendChild(item);
          }
          this.company.companyWiki = content.innerHTML;
          this.company.wikiSidebar = ulist.innerHTML;
        }
      });
      this.getCompanyJobList();
    },
    handleScroll(event){
      let h_list = document.getElementsByClassName("wiki_content")[0].querySelectorAll("h3, h4, h5, h6");
      let table = document.getElementsByClassName("wiki_sidebar")[0].getElementsByTagName("a");

      for (let i = 0; i < h_list.length; i++){
        let element = h_list[i];
        let position = element.getBoundingClientRect();

        let next_element = null;
        let next_position = null;
        if (i != h_list.length - 1){
          next_element = h_list[i+1];
          next_position = next_element.getBoundingClientRect();
        }

        if (next_element == null){
          if (position.top <= 10){
            for (let j = 0; j < table.length; j++){
              table[j].style.color = "#707070";
            }
            table[i].style.color = "#551A8B";
            break;
          }
          else if (position.bottom < 0){
            for (let j = 0; j < table.length; j++){
              table[j].style.color = "#707070";
            }
            table[i].style.color = "#551A8B";
            break;
          }
        }
        else {
          if (position.top >= 0 && position.top <= 10){
            for (let j = 0; j < table.length; j++){
              table[j].style.color = "#707070";
            }
            table[i].style.color = "#551A8B";
            break;
          }
          else if (position.top < 10 && next_position.top > 10){
            for (let j = 0; j < table.length; j++){
              table[j].style.color = "#707070";
            }
            table[i].style.color = "#551A8B";
            break;
          }
          else if (position.bottom < 10 && next_position.top > 10){
            for (let j = 0; j < table.length; j++){
              table[j].style.color = "#707070";
            }
            table[i].style.color = "#551A8B";
            break;
          }
        }

      }
    },
    getCompanyJobList() {
      getCompanyJobList(this.listQuery).then(response => {
        this.jobPage = response.data;
        this.total = response.data.total;
      });
    },
    handleSelectTab(tabIndex) {
      this.tabIndex = tabIndex;
      if (tabIndex == "intro") {
        this.listQuery.page = 1;
        this.getCompanyJobList();
      }
      else if (tabIndex == "activity"){
        // TODO
      }
    },
    handleFavorite() {
      this.favoriteLoading = true;
      this.favoriteForm.favorite = !this.favoriteForm.favorite;
      doFavorite(this.favoriteForm)
        .then(() => {
          this.company.favoriteFlag = this.favoriteForm.favorite ? 1 : 0;
          this.$message("操作成功");
        })
        .finally(() => {
          this.favoriteLoading = false;
        });
    },
    onJobClick(job) {
      this.$router.push({ name: "job", params: { id: job.id } });
    },
    onLoginClick(){
      this.$router.push('/login');
    }
  }
};

</script>

<style scoped>
.company-logo {
  width: 100px;
  height: 100px;
}
.map-box {
  height: 200px;
}
.app-container {
  margin: 0 auto;
}
.wiki_sidebar{
  position: sticky;
  font-size: 17px;
  top: 50px;
  width: 20%;
  float: left;
  overflow-y: scroll;
  max-height: 85vh;
  padding-left: 20px;
  list-style-type: none;
}
.wiki_content{
  width: 75%;
  float: right;
}
</style>

<style>
.wiki_sidebar a{
  color: #707070;
}
</style>
