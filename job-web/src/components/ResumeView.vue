<template>
  <div class="resume-container" v-if="resume">
    <div class="el-row--flex" style="margin-bottom: 31px">
      <el-image :src="resume.avatar" class="avatar" />
      <div class="ml-2">
        <h6 class="mb-2">
          {{ resume.name }}
        </h6>
        <el-row>
          <el-col :span="12" v-if="resume.gender"
            ><span class="basic-title">性别：</span
            >{{ resume.gender == 1 ? "男" : "女" }}</el-col
          >
          <el-col :span="12" v-if="resume.maritalStatus"
            ><span class="basic-title">政治面貌：</span
            >{{ resume.maritalStatus }}</el-col
          >
          <el-col :span="12" v-if="resume.age"
            ><span class="basic-title">年龄：</span>{{ resume.age }}</el-col
          >
          <el-col :span="12" v-if="resume.curPlace"
            ><span class="basic-title">现居地址：</span
            >{{ resume.curPlace }}</el-col
          >
          <el-col :span="12" v-if="resume.email"
            ><span class="basic-title">邮箱：</span>{{ resume.email }}</el-col
          >
          <el-col :span="12" v-if="resume.phoneCode && resume.phone"
            ><span class="basic-title">手机：</span>+{{ resume.phoneCode }}
            {{ resume.phone }}</el-col
          >
        </el-row>
      </div>
    </div>
    <!-- 教育经历 -->
    <div
      class="resume-box"
      v-if="resume.resumeEduList && resume.resumeEduList.length != 0"
    >
      <div class="title-container">
        <div class="dot"></div>
        <h6 class="resume-title">
          教育经历 <span style="color: #ff1744">*</span>
        </h6>
      </div>
      <div
        class="mt-3 ml-3"
        v-for="resumeEdu in resume.resumeEduList"
        :key="resumeEdu.id"
      >
        <h6>
          {{ resumeEdu.schoolName }}
          <span class="ml-3 time-text"
            >{{ resumeEdu.startTime }} - {{ resumeEdu.finishTime }}</span
          >
        </h6>
        <el-row
          ><el-col :span="12" v-if="resumeEdu.degree.name"
            ><span class="basic-title">学历：</span
            >{{ resumeEdu.degree.name }}</el-col
          ><el-col :span="12" v-if="resumeEdu.gpa != 0"
            ><span class="basic-title">GPA：</span>{{ resumeEdu.gpa }}</el-col
          ><el-col :span="12" v-if="resumeEdu.majorName"
            ><span class="basic-title">专业：</span
            >{{ resumeEdu.majorName }}</el-col
          ></el-row
        >
      </div>
    </div>
    <!-- 求职意向 -->
    <div class="resume-box mt-4" v-if="resume.userExpectJob">
      <div class="title-container">
        <div class="dot"></div>
        <h6 class="resume-title">
          求职意向 <span style="color: #ff1744">*</span>
        </h6>
      </div>
      <div class="mt-3 ml-3">
        <el-row
          ><el-col :span="12"
            ><span class="basic-title">预期薪资：</span
            >{{ resume.userExpectJob.salary.name }}</el-col
          ><el-col :span="12"
            ><span class="basic-title">工作城市：</span>
            <span v-for="city in resume.userExpectJob.cityList" :key="city.id">
              {{ city.name }}
            </span></el-col
          >
          <!-- <el-col :span="12"
            ><span class="basic-title">工作类型：</span> {{ city.name }}</el-col
          > -->
          <el-col :span="12"
            ><span class="basic-title">期望行业：</span>
            <span
              v-for="item in resume.userExpectJob.categoryList"
              :key="item.id"
              >{{ item.name }}</span
            ></el-col
          ></el-row
        >
      </div>
    </div>
    <!-- 工作/实习经历 -->
    <div
      class="resume-box mt-4"
      v-if="resume.resumeExpList && resume.resumeExpList.length != 0"
    >
      <div class="title-container">
        <div class="dot"></div>
        <h6 class="resume-title">工作/实习经历</h6>
      </div>
      <div
        class="mt-4 ml-3"
        v-for="resumeExp in resume.resumeExpList"
        :key="resumeExp.id"
      >
        <h6>
          {{ resumeExp.company }}
          <span class="ml-4">{{ resumeExp.post }} </span>
        </h6>
        <el-row class="mt-2"
          ><el-col :span="12" v-if="resumeExp.depart"
            ><span class="basic-title">行业：</span
            >{{ resumeExp.depart }}</el-col
          ><el-col :span="12"
            ><span class="basic-title">在职时间：</span
            >{{
              resumeExp.onWork == 1
                ? "在职"
                : `${resumeExp.startTime} - ${resumeExp.finishTime}`
            }}</el-col
          ><el-col :span="24" class="basic-left"
            ><span class="basic-title">工作描述：</span
            ><span
              class="description-text"
              v-html="resumeExp.description"
            ></span></el-col
        ></el-row>
      </div>
    </div>
    <!-- 项目/其他经历 -->
    <div
      class="resume-box mt-4"
      v-if="resume.resumePracticeList && resume.resumePracticeList.length != 0"
    >
      <div class="title-container">
        <div class="dot"></div>
        <h6 class="resume-title">项目/其它经历</h6>
      </div>
      <div
        class="mt-4 ml-3"
        v-for="practice in resume.resumePracticeList"
        :key="practice.id"
      >
        <h6 class="mb-2">
          {{ practice.title }}
          <span class="ml-3 time-text">{{
            practice.onWork == 1
              ? "实践中"
              : `${practice.startTime} - ${practice.finishTime}`
          }}</span>
        </h6>
        <div class="basic-left">
          <span class="basic-title short">介绍：</span
          ><span class="description-text" v-html="practice.description"></span>
        </div>
      </div>
    </div>
    <!-- 语言能力 -->
    <div
      class="resume-box mt-4"
      v-if="resume.resumeLanguageList && resume.resumeLanguageList.length != 0"
    >
      <div class="title-container">
        <div class="dot"></div>
        <h6 class="resume-title">语言能力</h6>
      </div>
      <div
        class="mt-4 ml-3"
        v-for="item in resume.resumeLanguageList"
        :key="item.id"
      >
        <el-row
          ><el-col :span="12" v-if="item.title"
            ><span class="basic-title">语种：</span>{{ item.title }}</el-col
          ><el-col :span="12" v-if="item.description"
            ><span class="basic-title">证书或分数：</span
            >{{ item.description }}</el-col
          ></el-row
        >
      </div>
    </div>
    <!-- 证书/奖项 -->
    <div
      class="resume-box mt-4"
      v-if="
        resume.resumeCertificateList && resume.resumeCertificateList.length != 0
      "
    >
      <div class="title-container">
        <div class="dot"></div>
        <h6 class="resume-title">证书/奖项</h6>
      </div>
      <div
        class="mt-4 ml-3"
        v-for="resumeCert in resume.resumeCertificateList"
        :key="resumeCert.id"
      >
        <el-row
          ><el-col :span="12" v-if="resumeCert.title"
            ><span class="basic-title">证书/奖项名称：</span
            >{{ resumeCert.title }}</el-col
          ><el-col :span="12" v-if="resumeCert.time"
            ><span class="basic-title">获得时间：</span
            >{{ resumeCert.time }}</el-col
          ></el-row
        >
      </div>
    </div>
    <!-- 能力标签 -->
    <div
      class="mt-4 resume-box"
      v-if="resume.resumeSkillList && resume.resumeSkillList.length != 0"
    >
      <div class="title-container mb-2">
        <div class="dot"></div>
        <h6 class="resume-title">能力标签</h6>
        <div class="tag-acct">{{ resume.resumeSkillList.length }}/6</div>
      </div>
      <div style="margin-left: 33px">
        <el-tag
          v-for="skill in resume.resumeSkillList"
          :key="skill.id"
          effect="plain"
          type="info"
          size="mini"
          style="margin-right: 7px"
          >{{ skill.name }}</el-tag
        >
      </div>
    </div>
    <div class="resume-box mt-4">
      <div class="title-container">
        <div class="dot"></div>
        <h6 class="resume-title">自我介绍</h6>
      </div>
      <div
        class="description-text mt-2 ml-3"
        v-html="resume.introduction"
      ></div>
    </div>
  </div>
</template>

<script>
import { getResumeDetail } from "@/api/resume_api";
import { library } from "@fortawesome/fontawesome-svg-core";
import { linkName, wholeLink } from "@/utils/common";

import { faMobileAlt, faEnvelope } from "@fortawesome/free-solid-svg-icons";

library.add(faMobileAlt, faEnvelope);

export default {
  name: "ResumeView",
  props: {
    resumeId: {
      type: String,
    },
  },
  watch: {
    resumeId: function () {
      this.getResumeInfo();
    },
  },
  data() {
    return {
      resume: undefined,
    };
  },
  created() {
    this.getResumeInfo();
  },
  methods: {
    getResumeInfo() {
      if (this.resumeId) {
        getResumeDetail(this.resumeId).then(
          (response) => (this.resume = response.data)
        );
      }
    },
    linkName(link) {
      return linkName(link);
    },
    wholeLink(link) {
      return wholeLink(link);
    },
  },
};
</script>

<style lang="scss">
.resume-container {
  font-size: 15px;

  /deep/.el-col-12 {
    margin-bottom: 4px !important;
  }

  .basic-left {
    display: flex;
    .basic-title {
      flex: 1.1;
    }

    .short {
      flex: 0.8;
    }

    .description-text {
      flex: 9;
    }
  }

  .title-container {
    display: flex;
    flex-direction: row;
    align-items: center;
    .dot {
      width: 7px;
      height: 7px;
      background: #4f69ed;
      margin-right: 9px;
      border-radius: 100%;
    }
  }
  .tag-acct {
    margin-left: 13px;
    font-size: 12px;
    font-family: PingFangSC-Medium, PingFang SC;
    font-weight: 500;
    color: #4f69ed;
    line-height: 17px;
  }

  /deep/ .el-tag {
    background: #4cc9f0;
    border-radius: 5px;
    border: 0px;
    font-size: 12px;
    font-family: PingFangSC-Medium, PingFang SC;
    font-weight: 500;
    color: #ffffff;
    line-height: 17px;
  }

  .basic-title {
    font-size: 14px;
    font-family: PingFangSC-Medium, PingFang SC;
    font-weight: 500;
    color: #333333;
    line-height: 20px;
  }
  h6 {
    font-size: 18px;
    font-family: PingFangSC-Medium, PingFang SC;
    font-weight: 500;
    color: #333333;
    line-height: 25px;
    margin: 0px;
  }
  .avatar {
    width: 126px;
    height: 126px;
    border-radius: 5px;
    border: 1px solid #3f5ff4;
  }
  .description-text p {
    margin-bottom: 5px;
  }
  .time-text {
    font-size: 14px;
    font-family: PingFangSC-Medium, PingFang SC;
    font-weight: 500;
    color: #333333;
    line-height: 20px;
  }

  .position-text {
    font-size: 14px;
    font-weight: 500;
  }
}
</style>
