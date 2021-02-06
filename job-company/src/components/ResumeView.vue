<template>
  <div class="resume-container" v-if="resumeDetail">
    <el-row class="session">
      <el-col :span="4">
        <el-image v-if="resumeDetail.avatar" :src="resumeDetail.avatar" class="avatar"/>
        <div v-else class="avatar">暂无照片</div>
      </el-col>
      <el-col :span="18" :offset="1">
        <el-row class="title">{{resumeDetail.name}}</el-row>
        <el-row>
          <el-col :span="12"><span class="key">性别：</span><span class="value">{{resumeDetail.gender===1?"男":"女"}}</span></el-col>
          <el-col :span="12"><span class="key">政治面貌：</span><span class="value">{{getMaritalStatus()}}</span></el-col>
        </el-row>
        <el-row>
          <el-col :span="12"><span class="key">年龄：</span><span class="value">{{resumeDetail.age}}岁</span></el-col>
          <el-col :span="12"><span class="key">现居地址：</span><span class="value">{{resumeDetail.curPlace}}</span></el-col>
        </el-row>
        <el-row>
          <el-col :span="12"><span class="key">邮箱：</span><span class="value">{{resumeDetail.email}}</span></el-col>
          <el-col :span="12"><span class="key">入职时间：</span><span class="value">{{resumeDetail.returnTime}}</span></el-col>
        </el-row>
        <el-row>
          <span class="key">手机：</span><span class="value">{{resumeDetail.phone}}</span>
        </el-row>
      </el-col>
    </el-row>
    <div v-if="resumeDetail.resumeEduList" class="session">
      <el-row class="title point required">教育经历</el-row>
      <div class="content" v-for="resumeEdu in resumeDetail.resumeEduList" :key="resumeEdu.id">
        <el-row class="sub-title">
          <div>{{resumeEdu.schoolName}}</div>
          <div>{{resumeEdu.startTime == resumeEdu.finishTime?'在读':`${resumeEdu.startTime}至${resumeEdu.finishTime}`}}</div>
        </el-row>
        <el-row>
          <el-col :span="12"><span class="key">学历：</span><span class="value">{{resumeEdu.degree.name}}</span></el-col>
          <el-col :span="12"><span class="key">GPA：</span><span class="value">{{resumeEdu.gpa}}</span></el-col>
        </el-row>
        <el-row>
          <el-col :span="12"><span class="key">专业：</span><span class="value">{{resumeEdu.majorName}}</span></el-col>
        </el-row>
      </div>
    </div>
    <div v-if="resumeDetail.userExpectJob.expectPosition" class="session">
      <el-row class="title point required">求职意向</el-row>
      <div class="content">
        <el-row class="sub-title">
          <div>{{resumeDetail.userExpectJob.expectPosition}}</div>
        </el-row>
        <el-row>
          <el-col :span="12"><span class="key">预期薪资：</span><span class="value">{{getExpectSalary()}}</span></el-col>
          <el-col :span="12"><span class="key">工作城市：</span><span class="value">{{resumeDetail.userExpectJob.expectCity}}</span></el-col>
        </el-row>
        <el-row>
          <el-col :span="12"><span class="key">工作类型：</span><span class="value">{{resumeDetail.userExpectJob.expectWorkType}}</span></el-col>
          <el-col :span="12" v-if="resumeDetail.userExpectJob.category"><span class="key">期望行业：</span><span class="value">{{resumeDetail.userExpectJob.category.name}}</span>
          </el-col>
        </el-row>
      </div>
    </div>
    <div v-if="resumeDetail.resumeExpList" class="session">
      <el-row class="title point">工作/实习经历</el-row>
      <div class="content" v-for="resumeExp in resumeDetail.resumeExpList" :key="resumeExp.id">
        <el-row class="sub-title">
          <div v-if="resumeExp.company">{{resumeExp.company}}</div>
          <div v-if="resumeExp.workType">{{resumeExp.workType}}</div>
          <div v-if="resumeExp.post">{{resumeExp.post}}</div>
        </el-row>
        <el-row>
          <el-col :span="12"><span class="key">行业：</span><span class="value">{{resumeExp.industry}}</span></el-col>
          <el-col :span="12"><span class="key">在职时间：</span><span class="value">{{resumeExp.onWork == 1? '在职': `${resumeExp.startTime}至${resumeExp.finishTime}`}}</span>
          </el-col>
        </el-row>
        <el-row type="flex">
          <span class="key">工作描述：</span><span class="value" v-html="resumeExp.description"></span>
        </el-row>
      </div>
    </div>
    <div v-if="resumeDetail.resumePracticeList" class="session">
      <el-row class="title point">项目/其它经历</el-row>
      <div class="content" v-for="practice in resumeDetail.resumePracticeList" :key="practice.id">
        <el-row class="sub-title">
          <div v-if="practice.title">{{practice.title}}</div>
          <div>{{practice.onWork == 1? '项目进行中': `${practice.startTime} - ${practice.finishTime}`}}</div>
        </el-row>
        <el-row type="flex">
          <span class="key">介绍：</span><span class="value" v-html="practice.description"></span>
        </el-row>
      </div>
    </div>
    <div v-if="resumeDetail.resumeLanguageList" class="session">
      <el-row class="title point">项目/其它经历</el-row>
      <div class="content" v-for="(language,index) in resumeDetail.resumeLanguageList" :key="language.id">
        <el-row>
          <el-col :span="12"><span class="key">语种{{index+1}}：</span><span class="value">{{language.title}}</span></el-col>
          <el-col :span="12"><span class="key">证书或分数：</span><span class="value">{{language.description}}</span></el-col>
        </el-row>
      </div>
    </div>
    <div v-if="resumeDetail.resumeCertificateList" class="session">
      <el-row class="title point">证书/奖项</el-row>
      <div class="content" v-for="awards in resumeDetail.resumeLanguageList" :key="awards.id">
        <el-row>
          <el-col :span="12"><span class="key">证书/奖项名称：</span><span class="value">{{awards.title}}</span></el-col>
          <el-col :span="12"><span class="key">获得时间：</span><span class="value">{{awards.time}}</span></el-col>
        </el-row>
      </div>
    </div>
    <div v-if="resumeDetail.introduction" class="session">
      <el-row class="title point">自我介绍</el-row>
      <el-row class="content value" v-html="resumeDetail.introduction"></el-row>
    </div>
  </div>
</template>

<script>
  import {listByType} from "@/api/dict_api";

  export default {
    name: "ResumeView",
    props: {
      resumeDetail: {
        type: Object,
      },
    },
    data() {
      return {
        salaryList: [], // 薪资列表
      };
    },
    created() {
      listByType(9).then((response) => {
        this.salaryList = response.data.list;
      });
    },
    methods: {
      getMaritalStatus() {
        let marital = ["中共党员（含预备党员）", "民主党派", "无党派人士", "团员", "群众"];
        return marital[this.resumeDetail.maritalStatus];
      },

      getExpectSalary() {
        let salary = this.salaryList.filter(it => it.id === this.resumeDetail.userExpectJob.salaryId);
        return salary ? salary.name : "";
      }
    },
  };
</script>

<style lang="scss">
  .resume-container {
    width: 720px;
    font-size: 14px;
    color: #333333;

    .title {
      font-size: 18px;
      line-height: 28px;
      font-weight: 600;
      margin-bottom: 6px;
      vertical-align: middle;

      span {
        font-size: 12px;
        color: #4F69ED;
        line-height: 17px;
      }
    }

    .sub-title {
      font-size: 16px;
      font-weight: 600;
      line-height: 26px;

      div {
        display: inline-block;
        margin-right: 31px;
      }
    }

    .key {
      font-weight: 600;
      font-size: 14px;
      line-height: 24px;
      flex-shrink: 0;
    }

    .value {
      font-size: 14px;
      font-weight: 500;
      line-height: 24px;
    }

    .point {
      &::before {
        display: inline-block;
        width: 7px;
        height: 7px;
        content: "";
        margin-right: 10px;
        margin-bottom: 3px;
        border-radius: 50%;
        background: #4F69ED;
      }
    }

    .required {
      &::after {
        color: red;
        content: "*";
        vertical-align: text-top;
        margin-left: 7px;
        display: inline-block;
      }
    }

    .session {
      margin-bottom: 31px;

      .avatar {
        border: 1px solid #3F5FF4;
        border-radius: 5px;
        color: #8c939d;
        width: 126px;
        line-height: 126px;
        text-align: center;
      }

      .content {
        padding: 0 17px;
        margin-bottom: 20px;

        &:last-of-type {
          margin-bottom: 0;
        }

        .tag {
          display: inline-block;
          height: 21px;
          background: #4CC9F0;
          border-radius: 5px;
          padding: 0 15px;
          line-height: 21px;
          font-size: 12px;
          color: #FFFFFF;
          margin-left: 7px;
        }
      }
    }
  }
</style>
