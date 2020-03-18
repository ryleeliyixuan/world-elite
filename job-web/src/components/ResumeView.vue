<template>
  <div class="resume-container" v-if="resume">
    <div class="el-row--flex">
      <el-image :src="resume.avatar" class="avatar" />
      <div class="ml-2">
        <h5>
          {{resume.name}}
          <i
            class="text-danger gender-text"
            v-bind:class="[resume.gender === 1? 'el-icon-female': 'el-icon-male']"
          ></i>
        </h5>
        <div>
          <span v-if="resume.returnTime">{{resume.maxResumeEdu.degree.name}}&nbsp;/&nbsp;</span>
          <span v-if="resume.graduateTime">{{resume.graduateTime}}毕业&nbsp;/&nbsp;</span>
          <span v-if="resume.curPlace">{{resume.curPlace}}&nbsp;/&nbsp;</span>
          <span v-if="resume.age">{{resume.age}}岁&nbsp;/&nbsp;</span>
          <span v-if="resume.age">{{resume.returnTime}} 回国</span>
        </div>
        <div class="mt-2">
          <span class="mr-3">
            <font-awesome-icon :icon="['fa', 'envelope']" />
            {{resume.email}}
          </span>
          <span class="ml-3" v-if="resume.phone && resume.phone != 0">
            <font-awesome-icon :icon="['fa', 'mobile-alt']" />
            +{{resume.phoneCode}} {{resume.phone}}
          </span>
        </div>
      </div>
    </div>

    <div class="resume-box mt-4" v-if="resume.resumeExpList && resume.resumeExpList.length != 0">
      <h6 class="resume-title">工作经验</h6>
      <div class="mt-4" v-for="resumeExp in resume.resumeExpList" :key="resumeExp.id">
        <h6>
          {{resumeExp.company}}
          <span
            class="ml-3 position-text"
          >{{resumeExp.depart}} . {{resumeExp.post}}</span>
          <span class="ml-3 time-text">{{resumeExp.startTime}}到{{resumeExp.finishTime}}</span>
        </h6>
        <div class="description-text" v-html="resumeExp.description"></div>
      </div>
    </div>
    <div class="resume-box mt-4" v-if="resume.resumeEduList && resume.resumeEduList.length != 0">
      <h6 class="resume-title">教育经历</h6>
      <div class="mt-4" v-for="resumeEdu in resume.resumeEduList" :key="resumeEdu.id">
        <h6>
          {{resumeEdu.schoolName}}
          <span
            class="ml-3 time-text"
          >{{resumeEdu.startTime}}到{{resumeEdu.finishTime}}</span>
        </h6>
        <p>{{resumeEdu.degree.name}} / {{resumeEdu.majorName}} / GPA {{resumeEdu.gpa}}</p>
      </div>
    </div>
    <div
      class="resume-box mt-4"
      v-if="resume.resumePracticeList && resume.resumePracticeList.length != 0"
    >
      <h6 class="resume-title">实践经验</h6>
      <div class="mt-4" v-for="practice in resume.resumePracticeList" :key="practice.id">
        <h6>
          {{practice.title}}
          <span
            class="ml-3 time-text"
          >{{practice.startTime}}到{{practice.finishTime}}</span>
        </h6>
        <div class="description-text" v-html="practice.description"></div>
      </div>
    </div>
    <div class="resume-box mt-4">
      <h6 class="resume-title">个人介绍</h6>
      <div class="mt-4 mb-4" v-if="resume.resumeSkillList && resume.resumeSkillList.length != 0">
        <el-tag
          v-for="skill in resume.resumeSkillList"
          :key="skill.id"
          effect="plain"
          type="info"
          size="mini"
          class="mr-2"
        >{{ skill.name }}</el-tag>
      </div>
      <div class="description-text" v-html="resume.introduction"></div>
      <div class="mt-3" v-if="resume.resumeLinkList && resume.resumeLinkList.length !== 0">
        <b>社交平台：</b>
        <el-link
          v-for="resumeLink in resume.resumeLinkList"
          class="ml-2"
          :underline="false"
          :key="resumeLink.id"
          :href="wholeLink(resumeLink.link)"
          target="_blank"
        >{{resumeLink.name}}</el-link>
      </div>
      <div class="mt-3" v-if="resume.attachResume">
        <el-link :underline="false" type="primary" :href="resume.attachResume">
          <b class="el-icon-link">附件简历</b>
        </el-link>
      </div>
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
      type: String
    }
  },
  watch: {
    resumeId: function() {
      this.getResumeInfo();
    }
  },
  data() {
    return {
      resume: undefined
    };
  },
  created() {
    this.getResumeInfo();
  },
  methods: {
    getResumeInfo() {
      if (this.resumeId) {
        getResumeDetail(this.resumeId).then(
          response => (this.resume = response.data)
        );
      }
    },
    linkName(link) {
      return linkName(link);
    },
    wholeLink(link) {
      return wholeLink(link);
    }
  }
};
</script>

<style lang="scss">
.resume-container {
  font-size: 15px;
  h5 {
    font-size: 20px;
  }
  h6 {
    font-size: 16px;
  }
  .avatar {
    width: 90px;
    height: 90px;
  }
  .description-text p {
    margin-bottom: 5px;
  }
  .time-text {
    font-size: 14px;
    font-weight: 400;
  }
  .resume-title {
    border-left: 3px solid #409eff;
    padding-left: 10px;
  }
}
</style>
