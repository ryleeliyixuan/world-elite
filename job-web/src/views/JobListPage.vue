<template>
  <div class="background-wrapper">
    <Affix @on-change="scrollPane">
      <div class="section1-wrapper">
        <div class="section1-container" v-if="!collapse">
          <!-- <el-cascader
          placeholder="城市"
          :show-all-levels="true"
          :options="cityOptions"
          :props="cityIdProps"
          clearable
          @change="handleFilter"
          class="cascader"
          v-model="listQuery.cityIds"
        >
        </el-cascader> -->
          <!-- 行业类型 -->
          <div class="section1-filter">
            <span class="section1-filter-title">行业类型：</span>
            <el-checkbox-group
              v-model="listQuery.companyIndustryIds"
              size="small"
            >
              <el-checkbox-button
                v-for="item in companyIndustryOptions"
                :label="item.id"
                :key="item.id"
                @change="handleFilter"
                >{{ item.name }}</el-checkbox-button
              >
            </el-checkbox-group>
          </div>
          <!-- 月薪区间 -->
          <div class="section1-filter">
            <span class="section1-filter-title">月薪区间：</span>
            <el-checkbox-group v-model="listQuery.salaryRangeIds" size="small">
              <el-checkbox-button
                v-for="item in salaryRangeOptions"
                :label="item.id"
                :key="item.id"
                @change="handleFilter"
                >{{ item.name }}</el-checkbox-button
              >
            </el-checkbox-group>
          </div>
          <div v-show="moreFilter">
            <div class="section1-filter">
              <span class="section1-filter-title">公司规模：</span>
              <el-checkbox-group
                v-model="listQuery.companyScaleIds"
                size="small"
              >
                <el-checkbox-button
                  v-for="item in companyScaleOptions"
                  :label="item.id"
                  :key="item.id"
                  @change="handleFilter"
                  >{{ item.name }}</el-checkbox-button
                >
              </el-checkbox-group>
            </div>
            <div class="section1-filter">
              <span class="section1-filter-title">工作类型：</span>
              <el-checkbox-group v-model="listQuery.jobTypes" size="small">
                <el-checkbox-button
                  v-for="item in jobTypeOptions"
                  :label="item.id"
                  :key="item.id"
                  @change="handleFilter"
                  >{{ item.name }}</el-checkbox-button
                >
              </el-checkbox-group>
            </div>
          </div>
          <div class="section1-filter-option">
            <el-button
              class="quick"
              @click="quickFilter = !quickFilter"
              size="mini"
              >快速筛选</el-button
            >
            <el-button
              class="more"
              @click="moreFilter = !moreFilter"
              size="mini"
              >更多筛选</el-button
            >
            <el-button class="empty" @click="emptyFilter" type="text"
              ><svg-icon
                class="empty-icon"
                icon-class="joblistdelete"
                style="height: 19px; width: 19px"
              />清除选项</el-button
            >
          </div>
          <div v-show="quickFilter" class="quickFilter">
            <div class="quickFilter-left">
              <span class="quickFilter-title">搜索记录：</span>
              <div class="quickFilter-caption">
                您还没有搜索过职位哟，赶紧试一试吧
              </div>
            </div>
            <div class="quickFilter-right">
              <span class="quickFilter-title">订阅的职位：</span>
              <div class="quickFilter-caption">
                暂未订阅职位哦，点击下方及时获取职位信息
              </div>
              <div class="quickFilter-right-click">
                <svg-icon
                  icon-class="joblistfilterplus"
                  style="height: 20px; width: 20px"
                />
                <span class="quickFilter-right-click-text">点我订阅职位</span>
              </div>
            </div>
          </div>
        </div>
        <div class="section1-container section1-collapse" v-else>
          <el-tag
            class="section1-collapse-tag"
            v-if="selectedIndustry && selectedIndustry.length > 0"
            >{{ selectedIndustry }}
          </el-tag>
          <el-tag
            class="section1-collapse-tag"
            v-if="selectedSalary && selectedSalary.length > 0"
            >{{ selectedSalary }}
          </el-tag>
          <el-tag
            class="section1-collapse-tag"
            v-if="selectedScale && selectedScale.length > 0"
            >{{ selectedScale }}
          </el-tag>
          <el-tag
            class="section1-collapse-tag"
            v-if="selectedJobType && selectedJobType.length > 0"
            >{{ selectedJobType }}
          </el-tag>
          <el-button
            class="more"
            @click="(moreFilter = true), goToTop()"
            size="mini"
            >更多筛选</el-button
          >
        </div>
      </div>
    </Affix>
    <div class="app-container">
      <div v-if="showNoResult" style="text-align: center; line-height: 40px">
        暂无搜索结果，显示推荐职位
      </div>
      <div
        class="section3-container"
        v-if="pageResult.list && pageResult.list.length !== 0"
      >
        <el-card
          v-show="job.salary && job.companyUser.company.name !== ''"
          shadow="never"
          :body-style="{ padding: '0px' }"
          v-for="job in pageResult.list"
          :key="job.id"
          class="section3-item-container"
          @click.native="openJobDetail(job.id)"
        >
          <div class="section3-left-container">
            <h6 class="section3-job-name">{{ job.name }}</h6>
            <div>
              <b class="section3-salary"
                >{{ job.salary.name
                }}{{ job.salaryMonths ? ` · ${job.salaryMonths}薪` : "" }}</b
              >
              <span class="section3-city-degree">{{
                `${job.city ? job.city.name : ""} / ${
                  job.minDegree ? job.minDegree.name : ""
                }`
              }}</span>
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
                <el-tag v-if="job.companyUser.company.stage">
                  {{ job.companyUser.company.stage.name }}
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
  </div>
</template>

<script>
import { listByType } from "@/api/dict_api";
import { searchJob } from "@/api/job_api";
import Pagination from "@/components/Pagination";
import { mapGetters } from "vuex";

import { formatListQuery, parseListQuery } from "@/utils/common";
import { getRecommendList } from "@/api/recommend_api";

export default {
  name: "JobListPage",
  components: { Pagination },
  computed: {
    ...mapGetters(["keyword"]),
  },
  data() {
    return {
      moreFilter: false,
      quickFilter: false,
      collapse: false,

      selectedSalary: "",
      selectedIndustry: "",
      selectedScale: "",
      selectedJobType: "",

      listQuery: {
        keyword: "",
        salaryRangeIds: [],
        jobTypes: [],
        cityIds: [],
        companyIndustryIds: [],
        companyScaleIds: [],
        companyStageIds: [],
        degreeIds: [],
        categoryIds: [],
        page: 1,
        limit: 10,
      },
      total: 0,
      pageResult: {},
      salaryRangeOptions: [],
      companyScaleOptions: [],
      companyIndustryOptions: [],
      jobTypeOptions: [],
      showNoResult: false,
      cityIdProps: {
        multiple: true,
        lazy: false,
        // lazyLoad: (node, resolve) => {
        //     if (node.level === 1) {
        //         this.$axios.request({
        //             url: "/city/list",
        //             method: "get",
        //             params: {type: node.value}
        //         }).then(data => {
        //             let nodes = data.data.map(second => {
        //                 let children = second.children && second.children.map(third => {
        //                     return {id: third.id, name: third.name, leaf: true}
        //                 })
        //                 return {id: second.id, name: second.name, children}
        //             });
        //             resolve(nodes);
        //         })
        //     } else {
        //         resolve();
        //     }
        // },
        expandTrigger: "hover",
        value: "id",
        label: "name",
        emitPath: false,
        children: "children",
      },
      cityOptions: [
        { id: 1, name: "国内", children: [{ id: 0, name: "加载中" }] },
        { id: 2, name: "国外" },
      ],
    };
  },
  created() {
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
      listByType(5).then(
        (response) => (this.companyScaleOptions = response.data.list)
      );
      listByType(6).then(
        (response) => (this.companyIndustryOptions = response.data.list)
      );
      listByType(8).then(
        (response) => (this.jobTypeOptions = response.data.list)
      );
      listByType(9).then(
        (response) => (this.salaryRangeOptions = response.data.list)
      );
      this.$axios
        .request({
          url: "/city/list",
          method: "get",
          params: { type: 1 },
        })
        .then((data) => {
          this.cityOptions[0].children = data.data.map((second) => {
            let children =
              second.children &&
              second.children.map((third) => {
                return { id: third.id, name: third.name, leaf: true };
              });
            return { id: second.id, name: second.name, children };
          });
        });

      this.$axios
        .request({
          url: "/city/list",
          method: "get",
          params: { type: 2 },
        })
        .then((data) => {
          this.cityOptions[1].children = data.data.map((second) => {
            let children =
              second.children &&
              second.children.map((third) => {
                return { id: third.id, name: third.name, leaf: true };
              });
            return { id: second.id, name: second.name, children };
          });
        });
    },
    goToTop() {
      let top = document.documentElement.scrollTop || document.body.scrollTop;
      // 实现滚动效果
      const timeTop = setInterval(() => {
        document.body.scrollTop = document.documentElement.scrollTop = top -= 50;
        if (top <= 0) {
          clearInterval(timeTop);
        }
      }, 10);
    },
    scrollPane(status) {
      this.collapse = status;
      this.selectedSalary = "";
      this.selectedIndustry = "";
      this.selectedScale = "";
      this.selectedJobType = "";

      for (let i = 0; i < this.salaryRangeOptions.length; i++) {
        let item = this.salaryRangeOptions[i];
        if (this.listQuery.salaryRangeIds.indexOf(item.id) != -1) {
          this.selectedSalary += item.name + "，";
        }
      }
      for (let i = 0; i < this.companyIndustryOptions.length; i++) {
        let item = this.companyIndustryOptions[i];
        if (this.listQuery.companyIndustryIds.indexOf(item.id) != -1) {
          this.selectedIndustry += item.name + "，";
        }
      }
      for (let i = 0; i < this.companyScaleOptions.length; i++) {
        let item = this.companyScaleOptions[i];
        if (this.listQuery.companyScaleIds.indexOf(item.id) != -1) {
          this.selectedScale += item.name + "，";
        }
      }

      for (let i = 0; i < this.jobTypeOptions.length; i++) {
        let item = this.jobTypeOptions[i];
        if (this.listQuery.jobTypes.indexOf(item.id) != -1) {
          this.selectedJobType += item.name + "，";
        }
      }

      this.selectedSalary = this.selectedSalary.substr(
        0,
        this.selectedSalary.length - 1
      );
      this.selectedIndustry = this.selectedIndustry.substr(
        0,
        this.selectedIndustry.length - 1
      );
      this.selectedScale = this.selectedScale.substr(
        0,
        this.selectedScale.length - 1
      );
      this.selectedJobType = this.selectedJobType.substr(
        0,
        this.selectedJobType.length - 1
      );
    },
    handleFilter() {
      this.listQuery.page = 1;
      this.handleRouteList();
    },
    emptyFilter() {
      this.listQuery = {
        keyword: "",
        salaryRangeIds: [],
        jobTypes: [],
        cityIds: [],
        companyIndustryIds: [],
        companyScaleIds: [],
        companyStageIds: [],
        degreeIds: [],
        categoryIds: [],
        page: 1,
        limit: 10,
      };
      this.handleRouteList();
    },
    getList() {
      this.showNoResult = false;
      parseListQuery(this.$route.query, this.listQuery);
      searchJob(this.listQuery).then((response) => {
        if (!response.data.list || response.data.list.length === 0) {
          this.showNoResult = true;
          this.total = 10;
          getRecommendList({
            objectType: 1, // 职位
            page: 1,
            limit: 10,
            sort: "+position",
          }).then((response) => {
            this.pageResult.list = response.data.list.map(
              (item) => item.object
            );
            this.total = response.data.total;
            this.$emit("complete");
          });
        } else {
          this.pageResult = response.data;
          this.total = this.pageResult.total;
          this.$emit("complete");
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
  },
};
</script>

<style scoped lang="scss">
.section1-collapse {
  display: flex;
  flex-direction: row;
  align-items: center;
  /deep/ .el-tag + .el-tag {
    margin-left: 15px;
  }
  .section1-collapse-tag {
    background: #f4f5f8;
    border-radius: 17px;
    border: 1px solid #698ec7;
    height: 25px;
    width: fit-content;
    display: flex;
    align-items: center;

    span {
      font-size: 14px;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #698ec7;
      line-height: 20px;
    }
  }
  /deep/ .el-button {
    margin-left: 16px;
    background: #4895ef;
    border-radius: 17px;
    width: 86px;
    font-size: 14px;
    font-family: PingFangSC-Medium, PingFang SC;
    font-weight: 500;
    color: #ffffff;
    line-height: 20px;
    padding-top: 3px;
    padding-bottom: 3px;
  }
}

.background-wrapper {
  background: #f6f9fc;
  max-width: 3000px !important;
  padding: 0 !important;
}

.section1-wrapper {
  width: 100%;
  background: #fbfbfb;
  box-shadow: 0px 4px 16px 3px rgba(191, 199, 215, 0.31);
  margin-bottom: 31px;
  display: flex;
  justify-content: center;
  padding: 31px 0px;
  position: sticky;
  position: -webkit-sticky; // 兼容 -webkit 内核的浏览器
  top: 10px; // 必须设一个值，否则不生效

  .section1-container {
    max-width: 1140px;
    flex: 1;
    padding: 0px 30px;
  }

  .quickFilter {
    display: flex;
    flex-direction: row;
    background: #eef5ff;
    border-radius: 14px;
    height: 136px;
    padding: 13px 43px 17px 29px;
    margin-bottom: 50px;
    margin-top: 17px;

    .quickFilter-title {
      margin-bottom: 9px;
      font-size: 16px;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #4895ef;
      line-height: 22px;
    }
    .quickFilter-caption {
      font-size: 16px;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      color: #666666;
      line-height: 22px;
    }

    .quickFilter-left {
      flex: 3;
    }
    .quickFilter-right {
      flex: 1;
      .quickFilter-right-click {
        margin-top: 9px;
        display: flex;
        align-items: center;
        .quickFilter-right-click-text {
          margin-left: 4px;
          font-size: 16px;
          font-family: PingFangSC-Medium, PingFang SC;
          font-weight: 500;
          color: #333333;
          line-height: 22px;
        }
      }
    }
  }

  .section1-filter {
    display: flex;
    flex-direction: row;
    align-items: center;
    margin-bottom: 11px;

    .section1-filter-title {
      font-size: 16px;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #333333;
      line-height: 22px;
    }
    /deep/ .el-checkbox-button {
      margin-bottom: 0px;
    }
    /deep/ .el-checkbox-button + .el-checkbox-button {
      margin-left: 16px;
    }
    /deep/ .el-checkbox-button .el-checkbox-button__inner {
      background: #f5f5f5;
      border-radius: 17px;
      border: 0px;
      font-size: 14px;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #333333;
      line-height: 20px;
      padding: 3px 12px;
    }
    /deep/ .is-checked {
      background: #f4f5f8;
      border-radius: 17px;
      border: 1px solid #698ec7;

      .el-checkbox-button__inner {
        font-size: 14px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #698ec7;
        line-height: 20px;
      }
    }
  }
  .section1-filter-option {
    display: flex;
    justify-content: flex-end;

    /deep/ .el-button {
      border-radius: 17px;
      width: 86px;
      font-size: 14px;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #ffffff;
      line-height: 20px;
      padding-top: 3px;
      padding-bottom: 3px;
    }

    .quick {
      background: #9ccc65;
    }

    .more {
      background: #4895ef;
    }

    .empty {
      background: #fbfbfb;
      font-size: 14px;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #333333;
      line-height: 20px;

      .empty-icon {
        margin-right: 6px;
      }
    }
  }
}
.app-container {
  max-width: 1140px;
  margin: 0 auto;
  padding: 0 20px;
  min-height: calc(100vh - 477px);

  .section2-container {
    width: 100%;
    overflow-x: auto;
    padding-bottom: 99px;
    /deep/ .el-pager {
      display: flex;
      align-items: center;
      justify-content: center;
    }
    /deep/ .pagination-container {
      padding: 0px;
    }
    /deep/ .el-pagination {
      display: flex;
      justify-content: center;
      align-items: center;
    }
    /deep/ .el-pagination__total {
      display: none;
    }
    /deep/.el-pagination__sizes {
      display: none;
    }
    /deep/.el-pagination.is-background .el-pager li:not(.disabled).active {
      background: #3d6ff4;
      box-shadow: 0px 5px 11px 0px rgba(30, 150, 252, 0.5);
      border-radius: 100%;
      width: 29px;
      height: 30px;
      font-size: 14px;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #ffffff;
      line-height: 20px;
    }
    /deep/ .el-pagination.is-background .btn-next {
      width: 29px;
      height: 29px;
      background: #eeeeee;
      box-shadow: 0px 5px 11px 0px #cccccc;
      border-radius: 100%;
      font-size: 14px;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #999999;
      line-height: 20px;
    }
    /deep/ .el-pagination.is-background .btn-prev {
      width: 29px;
      height: 29px;
      background: #eeeeee;
      box-shadow: 0px 5px 11px 0px #cccccc;
      border-radius: 100%;
      font-size: 14px;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #999999;
      line-height: 20px;
    }
    /deep/ .el-pagination.is-background .el-pager li {
      width: 29px;
      height: 29px;
      background: #eeeeee;
      box-shadow: 0px 5px 11px 0px #cccccc;
      border-radius: 100%;
      font-size: 14px;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #999999;
      line-height: 20px;
      display: flex;
      align-items: center;
      justify-content: center;
    }
    /deep/ .el-pagination__jump {
      margin-left: 13px;
      font-size: 14px;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #999999;
      line-height: 20px;
      padding: 4px;
    }
    /deep/.el-input__inner {
      width: 48px;
      height: 23px;
      background: #ffffff;
      border-radius: 7px;
      opacity: 0.7;
      border: 1px solid #cccccc;
    }
    /deep/.el-pagination__editor.el-input {
      margin-left: 7px;
      margin-right: 7px;
    }
  }

  .section3-container {
    min-width: 335px;
    margin-bottom: 62px;

    .section3-item-container {
      margin-bottom: 16px;
      height: 130px;
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

      .section3-job-name {
        margin: 0 0 13px 0;
        font-size: 18px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #333333;
        line-height: 25px;
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
</style>

