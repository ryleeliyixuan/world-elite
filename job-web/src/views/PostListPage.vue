<template>
  <div class="app-container" style="width: 1140px">
    <div class="section1-container" style="margin-bottom: 15px">
      <el-cascader
        placeholder="工作地点"
        :show-all-levels="true"
        :options="cityOptions"
        :props="cityIdProps"
        clearable
        collapse-tags
        @change="handleFilter"
        class="cascader"
        v-model="listQuery.cityIds"
      >
      </el-cascader>
      <el-select
        v-model="listQuery.salaryRangeIds"
        multiple
        clearable
        placeholder="薪资"
        @change="handleFilter"
        class="section1-select"
      >
        <el-option
          v-for="item in salaryRangeOptions"
          :key="item.id"
          :label="item.name"
          :value="item.id"
        ></el-option>
      </el-select>
      <el-select
        v-model="listQuery.degreeIds"
        multiple
        clearable
        placeholder="学历要求"
        @change="handleFilter"
        class="section1-select"
      >
        <el-option
          v-for="item in degreeOptions"
          :key="item.id"
          :label="item.name"
          :value="item.id"
        ></el-option>
      </el-select>
      <!-- <el-select v-model="listQuery.companyIndustryIds"
                       multiple
                       clearable
                       placeholder="岗位分类"
                       @change="handleFilter"
                       class="section1-select">
                <el-option v-for="item in companyIndustryOptions" :key="item.id" :label="item.name"
                           :value="item.id"></el-option>
            </el-select> -->
      <el-input
        style="width: auto"
        v-model="queryStr"
        placeholder="请输入关键词"
        @keydown.enter.native="handleSearch"
      ></el-input>
      <el-button
        type="primary"
        @click="handleSearch"
        style="margin: 0 25px; height: 37px"
        >搜索
      </el-button>
      <el-link
        @click="clearOptions"
        :underline="false"
        style="color: #b4bbc5; height: 37px"
        >清空搜索条件</el-link
      >
    </div>

    <div style="display: inline-block">
      <el-tabs
        v-model="listQuery.recruitId"
        @tab-click="handleFilter"
        style="padding-left: 25px"
      >
        <el-tab-pane
          :label="'全部 ' + this.recruitCountResult.all"
        ></el-tab-pane>
        <!-- <el-tab-pane :label="'校招 ' + this.recruitCountResult.school" name="154"></el-tab-pane>
              <el-tab-pane :label="'社招 ' + this.recruitCountResult.community" name="155"></el-tab-pane>
              <el-tab-pane :label="'急招 ' + this.recruitCountResult.urgency" name="170"></el-tab-pane>
              <el-tab-pane :label="'热招 ' + this.recruitCountResult.hot" name="171"></el-tab-pane> -->
        <el-tab-pane
          :label="'内推 ' + this.recruitCountResult.inner"
          name="172"
        ></el-tab-pane>
      </el-tabs>
    </div>

    <div class="non-post-tip" v-if="showNoResult">
      <svg-icon
        icon-class="activity-missing"
        style="height: 265px; width: 344px; margin-bottom: 17px"
      />
      <div>很抱歉，该公司暂无岗位发布哦。</div>
    </div>

    <div class="sort-options" v-show="!showNoResult">
      <!--            <el-link :underline="false" style="color: #599EF8;">最新</el-link>-->
      <!--            /-->
      <!--            <el-link :underline="false">发布顺序</el-link>-->
    </div>

    <div class="section3-container">
      <el-card
        v-for="job in pageResult.list"
        class="section3-item-container"
        style="display: inline-block"
        :body-style="{ padding: '0px' }"
        :key="job.id"
        @click.native="openJobDetail(job.id)"
        shadow="never"
      >
        <div class="section3-left-container">
          <div class="section3-line1">
            <h6 class="section3-job-name">{{ job.name }}</h6>
            <!-- 内推 -->
            <div v-if="job.recruitType === 172" class="recruit-type">
              <svg-icon
                icon-class="job-fast-track"
                style="height: 33px; width: 105px"
              />
            </div>
          </div>
          <div>
            <!-- 职位薪资范围 -->
            <!-- 0-5k -> 5k一下 -->
            <b
              v-if="job.minSalary == 0 && job.maxSalary == 5"
              class="section3-salary"
              style="font-size: 16px"
            >
              {{ "5K以下" }}
            </b>
            <!-- 0-0k -> 不限 -->
            <b
              v-else-if="job.minSalary == 0 && job.maxSalary == 0"
              class="section3-salary"
              style="font-size: 16px"
            >
              {{ "不限" }}
            </b>
            <!-- 其他数值正常显示 -->
            <!-- 实习为“min-max 元/天” -->
            <b
              v-else-if="job.jobType.id == 107"
              class="section3-salary"
              style="font-size: 16px"
            >
              {{
                job.minSalary +
                "K-" +
                job.maxSalary +
                "K" +
                " * " +
                job.salaryMonths
              }}
            </b>
            <!-- 全职为“min-max * number” -->
            <b
              v-else-if="job.jobType.id == 108"
              class="section3-salary"
              style="font-size: 16px"
            >
              {{ job.minSalary + "-" + job.maxSalary + " 元/天" }}
            </b>
            <span
              v-if="job.experience.name"
              class="section3-city-degree"
              style="font-size: 15px"
            >
              {{ job.experience.name }}
            </span>
            <span
              v-if="job.jobType.name"
              class="section3-city-degree"
              style="font-size: 15px"
            >
              {{ job.jobType.name }}
            </span>
            <span class="section3-city-degree" style="font-size: 15px">
              {{
                `${job.city ? job.city.name : ""}/${
                  job.minDegree ? job.minDegree.name.substring(0, 2) : ""
                }`
              }}
            </span>
            <!-- 隐藏聊一聊 -->
            <!-- <el-link
                      class="chat-link"
                      @click="handleChat($event, job.creatorId, job.id)"
                      :underline="false"
                      icon="el-icon-chat-dot-round"
              >聊一聊</el-link
              > -->
          </div>
        </div>
        <div
          class="section3-right-container"
          v-if="job.companyUser && job.companyUser.company"
        >
          <div class="section3-company-info-container">
            <h6 class="section3-company-name">
              {{ job.companyUser.company.name }}
            </h6>
            <div class="section3-company-tag">
              <el-tag v-if="job.companyUser.company.industry">
                {{ job.companyUser.company.industry.name }}
              </el-tag>
              <el-tag v-if="job.companyUser.company.property">
                {{ job.companyUser.company.property.name }}
              </el-tag>
              <el-tag v-if="job.companyUser.company.scale">
                {{ job.companyUser.company.scale.name }}
              </el-tag>
            </div>
          </div>
          <el-link :href="`/company/${job.companyUser.company.id}`">
            <el-image
              class="section3-logo"
              :src="job.companyUser.company.logo"
              v-if="
                job.companyUser.company.logo &&
                job.companyUser.company.logo !== ''
              "
            >
              <div slot="error" class="section3-logo-error">
                <i class="el-icon-picture-outline"></i>
              </div>
            </el-image>
          </el-link>
        </div>
      </el-card>
    </div>
    <div class="section2-container">
      <pagination
        v-show="total"
        :total="total"
        :page.sync="listQuery.page"
        :limit.sync="listQuery.limit"
        @pagination="handleRouteList"
      />
    </div>
  </div>
</template>


<script>
import { listByType } from "@/api/dict_api";
import { getJobRecruitCount, searchJob } from "@/api/job_api";
import Pagination from "@/components/Pagination";
import { mapGetters } from "vuex";

import { formatListQuery, parseListQuery } from "@/utils/common";
// import {getRecommendList} from "@/api/recommend_api";

export default {
  name: "WikiJobListPage",
  components: { Pagination },
  computed: {
    ...mapGetters(["keyword"]),
  },
  data() {
    return {
      listQuery: {
        companyId: undefined,
        keyword: "",
        salaryRangeIds: [],
        jobTypes: [],
        cityIds: [],
        companyIndustryIds: [],
        companyScaleIds: [],
        companyStageIds: [],
        degreeIds: [],
        categoryIds: [],
        recruitId: "0",
        page: 1,
        limit: 20,
      },
      queryStr: "",
      total: 0,
      pageResult: {},
      recruitCountResult: {
        all: 0,
        school: 0,
        community: 0,
        urgency: 0,
        hot: 0,
        inner: 0,
      },
      cityOptions: [],
      cityIdProps: {
        multiple: true,
        lazy: true,
        lazyLoad: (node, resolve) => {
          if (node.level >= 1) {
            this.$axios
              .request({
                url: "/city/child-city",
                method: "get",
                params: { parentId: node.value },
              })
              .then((data) => {
                console.log(data.data);
                let nodes = data.data.map((second) => {
                  let children =
                    second.children &&
                    second.children.map((third) => {
                      return { id: third.id, name: third.name, leaf: true };
                    });
                  return { id: second.id, name: second.name, children };
                });
                resolve(nodes);
              });
          } else {
            resolve();
          }
        },
        expandTrigger: "hover",
        value: "id",
        label: "name",
        emitPath: false,
        children: "children",
      },
      salaryRangeOptions: [],
      companyScaleOptions: [],
      companyIndustryOptions: [],
      jobTypeOptions: [],
      degreeOptions: [],
      showNoResult: false,
    };
  },
  created() {
    this.$emit("complete");
    this.initData();
    this.getList();
  },
  watch: {
    $route() {
      this.getList();
    },
    keyword() {
      this.listQuery.keyword = this.keyword;
      this.handleRouteList();
    },
  },
  methods: {
    initData() {
      console.log(this.$route.params.id);
      this.listQuery.companyId = this.$route.params.id;

      //城市分级选择,获取第一级
      this.$axios
        .request({
          url: "/city/child-city",
          method: "get",
          params: { parentId: 0 },
        })
        .then((data) => {
          this.cityOptions = data.data;
        });

      //薪资下拉
      listByType(9).then(
        (response) => (this.salaryRangeOptions = response.data.list)
      );
      //学历下拉
      listByType(1).then(
        (response) => (this.degreeOptions = response.data.list)
      );
      //岗位分类下拉
      listByType(6).then(
        (response) => (this.companyIndustryOptions = response.data.list)
      );
    },
    handleFilter() {
      this.listQuery.page = 1;
      this.handleRouteList();
    },
    handleSearch() {
      this.listQuery.keyword = this.queryStr;
      this.$router.push({
        // path: "/post-list/" + this.listQuery.companyId,
        query: { searchForm: this.listQuery },
      });
    },
    getList() {
      this.showNoResult = false;
      parseListQuery(this.$route.query, this.listQuery);
      searchJob(this.listQuery).then((response) => {
        if (!response.data.list || response.data.list.length === 0) {
          this.showNoResult = true;
          this.pageResult = {};
          this.total = 0;
        } else {
          this.pageResult = response.data;
          this.total = this.pageResult.total;
        }
      });

      getJobRecruitCount(this.listQuery.companyId).then((response) => {
        for (let i = 0; i < response.data.length; i++) {
          let recruitType = response.data[i].dictVo.name;
          if (recruitType === "校招") {
            this.recruitCountResult.school = response.data[i].count;
          } else if (recruitType === "社招") {
            this.recruitCountResult.community = response.data[i].count;
          } else if (recruitType === "急招") {
            this.recruitCountResult.urgency = response.data[i].count;
          } else if (recruitType === "热招") {
            this.recruitCountResult.hot = response.data[i].count;
          } else if (recruitType === "内推") {
            this.recruitCountResult.inner = response.data[i].count;
          }
          this.recruitCountResult.all =
            this.recruitCountResult.school +
            this.recruitCountResult.inner +
            this.recruitCountResult.community +
            this.recruitCountResult.urgency +
            this.recruitCountResult.hot;
        }
      });
    },
    handleRouteList() {
      this.$router.replace({
        path: this.$route.path,
        query: formatListQuery(this.listQuery),
      });
    },
    openJobDetail(id) {
      this.$router.push(`/job/${id}`);
    },
    clearOptions() {
      // let options = document.getElementsByClassName(".el-select__tags");
      // for (let i = 0; i < options.length; i++) {
      //     options.item(i).remove();
      // }
      this.queryStr = "";
      this.listQuery.cityIds = "";
      this.listQuery.salaryRangeIds = "";
      this.listQuery.degreeIds = "";
      this.listQuery.companyIndustryIds = "";
      this.handleSearch();
    },
  },
};
</script>


<style scoped lang="scss">
.app-container {
  max-width: 1140px;
  width: 1140px;
  margin: 0 auto;
  padding: 0 20px;

  .non-post-tip {
    line-height: 80px;
    text-align: center;
    padding-bottom: 20px;
    width: 100%;
  }

  .section1-container {
    min-width: 335px;
    display: flex;
    flex-wrap: wrap;
    ::v-deep .el-cascader {
      margin-bottom: 10px;
    }

    .section1-select {
      flex: 1;
      min-width: 130px;
      margin: 0 10px 10px;
      height: 100%;
    }
  }

  .section2-container {
    margin-top: 30px;
    width: 100%;
    overflow-x: auto;
  }

  .section3-container {
    min-width: 335px;
    margin-bottom: 62px;

    .chat-link {
      color: #74b9ff;
      text-underline: none;
      position: relative;
      right: 8px;
      z-index: 2;
    }

    .section3-item-container {
      margin-bottom: 16px;
      height: 130px;
      width: 100%;
      cursor: pointer;
      padding: 33px 31px 35px 33px;
      background: #ffffff;
      box-shadow: 0px 4px 16px 3px rgba(191, 199, 215, 0.31);
      border-radius: 8px;
      border: 0px;

      /deep/ .el-card__body {
        display: flex;
      }
    }

    .section3-left-container {
      flex: 0.6;
      min-width: 290px;

      .section3-line1 {
        display: flex;
        align-items: center;
        margin-bottom: 13px;
        .section3-job-name {
          font-size: 18px;
          font-family: PingFangSC-Medium, PingFang SC;
          font-weight: 500;
          color: #333333;
          line-height: 25px;
          display: inline-block;
          max-width: 275px;
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
        }
      }

      .section3-salary {
        font-size: 18px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #4895ef;
        line-height: 25px;
        margin-right: 22px;
      }

      .section3-city-degree {
        margin-right: 22px;
        font-size: 16px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #333333;
        line-height: 22px;
      }
    }

    .section3-right-container {
      flex: 1;
      min-width: 290px;
      display: flex;
      justify-content: space-between;

      .section3-company-info-container {
        .section3-company-name {
          margin: 0 0 13px 0;
          font-size: 18px;
          font-family: PingFangSC-Medium, PingFang SC;
          font-weight: 500;
          color: #333333;
          line-height: 25px;
        }
        .section3-company-tag {
          /deep/.el-tag {
            height: 25px;
            padding: 2px 12px;
            background: #f4f5f8;
            border-radius: 4px;
            border: 0px;
            font-size: 14px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: #698ec7;
            line-height: 20px;

            position: relative;
            top: 5px;
          }
          /deep/ .el-tag + .el-tag {
            margin-left: 7px;
          }
        }
      }

      .section3-logo {
        width: 48px;

        /deep/ .section3-logo-error {
          width: 48px;
          text-align: center;
          font-size: 25px;
        }
      }
    }
  }
}

::-webkit-scrollbar {
  display: none;
}

@media screen and (max-width: 850px) {
  .app-container {
    .section3-container {
      .section3-item-container {
        /deep/ .el-card__body {
          display: flex;
          flex-direction: column;
          align-items: center;

          .section3-left-container {
            margin-bottom: 10px;
          }
        }
      }
    }
  }
}

@media screen and (max-width: 410px) {
  .app-container {
  }
}
</style>
<style>
.el-tabs__nav-wrap::after {
  opacity: 0;
}

.sort-options {
  display: inline-block;
  vertical-align: top;
  position: relative;
  top: 10px;
  left: 525px;
}
</style>
