<template>
  <div class="resume-container" v-if="resumeDetail">
    <div class="resume-box" style="display: flex">
      <div class="avatar-uploader">
      <el-image v-if="resumeDetail.avatar && resumeDetail.avatar!=''"
                :src="resumeDetail.avatar"
                class="avatar" />
      <i v-else>
        <span style="width: 100px;height: 100px;font-size: 14px;padding-left: 32px;line-height: 9;">暂无照片</span>
      </i>
      </div>
      <div>
<!--        基本信息-->
        <div class="resume-info" style="display: flex">
          <div class="info-other-row-l">
            <el-row class="info-name" style="width: 300px">{{resumeDetail.name}}</el-row>
            <el-row class="info-other">性别：
              <span v-if="resumeDetail.gender==1">男</span>
              <span v-if="resumeDetail.gender==2">女</span>
            </el-row>
            <el-row class="info-other">年龄：{{resumeDetail.age}} 岁</el-row>
            <el-row class="info-other">邮箱：{{resumeDetail.email}}</el-row>
            <el-row class="info-other">手机：{{resumeDetail.phone}}</el-row>
          </div>
          <div class="info-other-row-m">
            <el-row class="info-other">政治面貌：
              <span v-if="resumeDetail.maritalStatus==0">中共党员（含预备党员）</span>
              <span v-if="resumeDetail.maritalStatus==1">民主党派</span>
              <span v-if="resumeDetail.maritalStatus==2">无党派人士</span>
              <span v-if="resumeDetail.maritalStatus==3">团员</span>
              <span v-if="resumeDetail.maritalStatus==4">群众</span>
            </el-row>
            <el-row class="info-other">现居地址：{{resumeDetail.curPlace}}</el-row>
            <el-row class="info-other">入职时间：{{resumeDetail.returnTime}}</el-row>
          </div>
        </div>
      </div>
    </div>
    <div
      v-if="resumeDetail.resumeEduList && resumeDetail.resumeEduList!=''"
      class="resume-box"
    >
      <el-row>
        <div style="width: 540px;margin-right: 75px">
          <img src="../assets/point.png" style="padding-right: 9px;padding-bottom: 7px">
          <span class="resume-base">教育经历</span>
          <span class="resume-red">*</span>
        </div>
      </el-row>
          <div class="resume-edu" v-for="resumeEdu in resumeDetail.resumeEduList"
               :key="resumeEdu.id">
            <div style="display: flex;padding-top: 13px;padding-left: 13px;">
            <div class="edu-box-l" style="width: 350px;">
              <el-row class="edu-school" style="padding-top: 10px;">{{resumeEdu.schoolName}}</el-row>
              <el-row class="info-other" style="padding-top: 10px;">学历：{{resumeEdu.degree.name}}</el-row>
              <el-row class="info-other" style="padding-top: 10px;">专业：{{resumeEdu.majorName}}</el-row>
            </div>
            <div class="edu-box-m" style="padding-top: 22px">
              <el-row class="info-other" style="padding-top: 10px;">在校时间：
                <span style="font-size: 14px;
                             font-family: PingFangSC-Regular, PingFang SC;
                             font-weight: 400;">
                   {{resumeEdu.startTime == resumeEdu.finishTime? '在读': `${resumeEdu.startTime}到${resumeEdu.finishTime}`}}
                </span>
              </el-row>
              <el-row class="info-other" style="padding-top: 10px;">GPA：{{resumeEdu.gpa}}</el-row>
            </div>
            </div>
          </div>
    </div>
    <div
      class="resume-box mt-4"
      style="margin-top: 10px"
      v-if="resumeDetail.userExpectJob && resumeDetail.userExpectJob!=''"
    >
      <el-row>
        <div style="width: 540px; display: inline-block;margin-right: 75px">
          <img src="../assets/point.png" style="padding-right: 9px;padding-bottom: 7px">
          <span class="resume-base">求职意向</span>
          <span class="resume-red">*</span>
        </div>
      </el-row>
      <div class="resume-eduinfo" style="padding-left: 13px;">
        <div class="resume-edu" style="display: inline-flex;">
          <div class="edu-box-l1">
            <el-row class="edu-school" style="padding-top: 10px;">
              {{resumeDetail.userExpectJob.expectPosition}}
            </el-row>
            <el-row class="info-other" style="padding-top: 10px;">预期薪资：
              <span v-if="resumeDetail.userExpectJob.salaryId && resumeDetail.userExpectJob.salaryId!=''">
                <span v-if="resumeDetail.userExpectJob.salaryId==1">5K-8K</span>
                <span v-if="resumeDetail.userExpectJob.salaryId==2">8K-10K</span>
                <span v-if="resumeDetail.userExpectJob.salaryId==3">10K-15K</span>
                <span v-if="resumeDetail.userExpectJob.salaryId==4">15K-20K</span>
                <span v-if="resumeDetail.userExpectJob.salaryId==5">20K-30K</span>
                <span v-if="resumeDetail.userExpectJob.salaryId==6">30K-50K</span>
                <span v-if="resumeDetail.userExpectJob.salaryId==7">50K以上</span>
                <span v-if="resumeDetail.userExpectJob.salaryId==8">5K以下</span>
                <span v-if="resumeDetail.userExpectJob.salaryId==9">面议</span>
               </span>
            </el-row>
            <el-row class="info-other" style="padding-top: 10px;">工作类型：
              <span class="expjob-data"
              >{{resumeDetail.userExpectJob.expectWorkType}}
              </span >
            </el-row>
          </div>
          <div class="edu-box-m1">
            <el-row class="info-other" style="padding-top: 10px;">期望行业：
              <span v-if="resumeDetail.userExpectJob.category&&resumeDetail.userExpectJob.category!=''"
              > {{ resumeDetail.userExpectJob.category.name }} </span>
            </el-row>
            <el-row class="info-other" style="padding-top: 10px;">工作城市：
              <span class="resume"
              >{{resumeDetail.userExpectJob.expectCity}}</span>
            </el-row>
          </div>
        </div>
      </div>
    </div>
    <div
      class="resume-box mt-4"
      v-if="resumeDetail.resumeExpList && resumeDetail.resumeExpList!= ''"
    >
      <div style="width: 615px">
        <img src="../assets/point.png" style="padding-right: 9px;padding-bottom: 7px">
        <span class="resume-base">工作/实习经历</span>
      </div>

      <div v-for="resumeExp in resumeDetail.resumeExpList"
           :key="resumeExp.id" style="display: grid;padding-left: 13px;">
        <div style="display: inline-flex;margin-top: 20px">
          <div>
            <span class="resume-box-text"> {{resumeExp.company}}</span>
          </div>
          <div style="padding-left: 31px">
            <span>{{resumeExp.post}}</span>
          </div>
          <div style="padding-left: 31px">
            <span>{{resumeExp.workType}}</span>
          </div>
        </div>
        <div style="display: inline-flex">
          <div style="width: 615px">
            <div style="margin-top: 9px;display: inline-flex" >
              <div style="width: 380px">
                <span class="resume-box-text-title">行业：</span>
                <span
                      class="resume-box-text-data"
                >{{ resumeExp.industry}}
                </span>
              </div>
              <div>
                <span class="resume-box-text-title">在职时间：</span>
                <span class="resume-box-text-data">
                    {{resumeExp.onWork == 1? '在职': `${resumeExp.startTime}到${resumeExp.finishTime}`}}
                </span>
              </div>
            </div>
            <div style="display: inline-flex;margin-top: 9px">
              <span class="resume-box-text-title">工作描述：</span>
              <span class="resume-box-text-html">{{resumeExp.description}}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div
            class="mt-4 resume-box"
            v-if="resumeDetail.resumePracticeList && resumeDetail.resumePracticeList!=''"
    >
      <el-row>
        <div style="width: 540px; display: inline-block;margin-right: 75px">
          <img src="../assets/point.png" style="padding-right: 9px;padding-bottom: 7px">
          <span class="resume-base">项目/其它经历</span>
        </div>
      </el-row>
      <div class="resume-info" v-for="practice in resumeDetail.resumePracticeList"
           :key="practice.id" style="margin-bottom: 10px">
        <div class="resume-edu" style="width: 540px;padding-top: 10px;">
          <el-row style="width: 616px;height: 40px;padding-top: 10px;">
            <span class="resume-box-text">{{practice.title}}</span>
            <span class="resume-box-text-title" style="padding-left: 31px">{{practice.onWork == 1? '项目进行中': `${practice.startTime} - ${practice.finishTime}`}}</span>
          </el-row>
          <div class="edu-box">
            <el-row class="expinfo-other-row">
              <span class="expinfo-other" style="width: 42px">介绍：</span>
              <span class="expinfo-other-html" >{{practice.description}}</span>
            </el-row>
          </div>
        </div>
      </div>
    </div>
    <div
            class="mt-4 resume-box"
            v-if="resumeDetail.resumeLanguageList && resumeDetail.resumeLanguageList != ''"
    >
      <el-row style="height: 30px">
        <div style="width: 540px; display: inline-block;margin-right: 75px">
          <img src="../assets/point.png" style="padding-right: 9px;padding-bottom: 7px">
          <span class="resume-base">语言能力</span>
        </div>
      </el-row>
      <el-row style="display: inline-flex;" v-for="(language,index) in resumeDetail.resumeLanguageList"
              :key="language.id">
        <div class="resume-edu">
          <el-row style="width: 615px;display: inline-flex;padding-left: 13px;">
            <div style="width: 386px">
              <span class="info-other">语种{{index+1}}: </span>
                <span class="info-other">{{language.title}}</span>
            </div>
            <div style="width: 200px">
              <span class="info-other">证书或分数：</span>
               <span class="info-other">{{language.description}}</span>
            </div>
          </el-row>
        </div>
      </el-row>
    </div>
    <div
            class="mt-4 resume-box"
            v-if="resumeDetail.resumeCertificateList && resumeDetail.resumeCertificateList!= ''"
    >
      <el-row style="height: 30px">
        <div style="width: 540px; display: inline-block;margin-right: 75px">
          <img src="../assets/point.png" style="padding-right: 9px;padding-bottom: 7px">
          <span class="resume-base">证书/奖项</span>
        </div>
      </el-row>
      <el-row style="display: inline-flex" v-for="(awards,index) in resumeDetail.resumeCertificateList"
              :key="awards.id">
        <div class="resume-edu">
          <el-row style="width: 615px;display: inline-flex;padding-left: 13px;">
            <div style="width: 386px">
              <span class="info-other">证书/奖项名称：</span>
              <span class="info-other">{{awards.title}}</span>
            </div>
            <div style="width: 200px">
              <span class="info-other">获得时间：</span>
              <span class="info-other">{{awards.time}}</span>
            </div>
          </el-row>
        </div>
      </el-row>
    </div>
    <div
            class="mt-4 resume-box"
            v-if="resumeDetail.resumeSkillList && resumeDetail.resumeSkillList!= ''"
    >
      <el-row>
        <div style="width: 540px; display: inline-block;margin-right: 75px">
          <img src="../assets/point.png" style="padding-right: 9px;padding-bottom: 7px">
          <span class="resume-base">能力标签</span>
        </div>
      </el-row>
      <el-row style="margin-top: 15px">
        <div class="resume-languageinfo">
          <div class="resume-edu">
            <el-tag
                    v-for="skill in resumeDetail.resumeSkillList"
                    :key="skill.id"
                    class="tag-icon"
            >{{ skill.name }}
            </el-tag>
          </div>
        </div>
      </el-row>
    </div>
    <div class="resume-box mt-4" style="margin-bottom: 100px">
      <el-row style="height: 30px">
        <div style="width: 540px; display: inline-block;margin-right: 75px">
          <img src="../assets/point.png" style="padding-right: 9px;padding-bottom: 7px">
          <span class="resume-base">自我介绍</span>
        </div>
      </el-row>
      <div class="resume-languageinfo" style="width: 550px">
        <div class="resume-introduction">
          <span>{{resumeDetail.introduction}}</span>
        </div>
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
    resumeDetail: {
      type: Object,
    },
  },
  watch: {
    resumeDetail:  {
      handler (n, o) {
        console.log('子组件中的resumeId值： ' + n.id)
      },
      deep: true // 深度监听父组件传过来对象变化
    },
  },
  data() {
    return {
      resumeId: undefined,
      resume:undefined
    };
  },
  created() {

  },
  methods: {

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

  .resume-box {
    padding-left: 62px;
    padding-top: 18px;
    .avatar-uploader{
      border: 1px solid #3F5FF4;
      border-radius: 5px;
      color: #8c939d;
      width: 126px;
      height: 126px;
      line-height: 100px;


    }
    .resume-info{
      padding-left: 13px;
      .info-other-row-l {
        /*margin-left: 20px;*/
        width: 256px;
        .info-name {
          width: 97px;
          height: 29px;
          font-size: 21px;
          font-family: PingFangSC-Medium, PingFang SC;
          font-weight: 500;
          color: #333333;
          line-height: 29px;
        }
        .info-other {
          height: 20px;
          font-size: 14px;
          font-family: PingFangSC-Medium, PingFang SC;
          font-weight: 500;
          color: #333333;
        }
      }
      .info-other-row-m {
        margin-top: 30px;
        padding-bottom: 4px;
      padding-left: 60px;
      }
    }
      .resume-box-text{
        font-size: 16px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #333333;
        line-height: 22px;
      }
      .resume-box-text-title{
        font-size: 14px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #333333;
        line-height: 20px;
      }
      .resume-box-text-data{
        font-size: 14px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #333333;
        line-height: 20px;
      }
      .resume-box-text-html{
        display:inline-block;
        word-wrap: break-word;
        width: 478px;
        font-size: 14px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #666666;
        line-height: 20px;
      }
    .resume-base {
      width: 117px;
      height: 25px;
      font-size: 18px;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #333333;
      line-height: 25px;
      padding-right: 1px;
      padding-bottom: 13px;
    }
    .resume-red {
      padding-left: 5px;
      color: #D9001B;
      font-size: 20px;
      height: 9px;
      width: 9px;
    }
    .resume-languageinfo {
      padding-left: 18px;
      display: inline-flex;

      .resume-introduction {
        display:inline-block;
        word-wrap: break-word;
        width: 548px;
        font-size: 14px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #666666;
        line-height: 20px;
      }
    }



  }
  .resume-eduinfo {


    .resume-edu1 {
      width: 616px;
      padding-top: 13px;

      .info-other {
        height: 20px;
        font-size: 14px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #333333;
      }
    }

    .resume-edu {
      padding-top: 13px;
      display: block;
      ::v-deep.el-tag--light {
        height: 21px;
        font-size: 12px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #FFFFFF;
        background: #4CC9F0;
        border-radius: 5px;
        line-height: 10px;
        margin: 5px;
      }
      .edu-box-l {
        width: 346px;
      }
      .edu-box-l1 {
        width: 346px;
      }
      .edu-box-m1 {
           margin-top: 24px;
           margin-right: 20px;

           .info-other {
             height: auto;
             font-size: 14px;
             font-family: PingFangSC-Medium, PingFang SC;
             font-weight: 500;
             color: #333333;
           }
         }
      .edu-box {
        display: inline-flex;
        .expinfo-other-row {
          display: inline-flex;


          .expinfo-other {
            width: 70px;
            font-size: 14px;
            font-family: PingFangSC-Medium, PingFang SC;
            font-weight: 500;
            color: #333333;
          }

          .expinfo-other-html {
            display:inline-block;
            word-wrap: break-word;
            width: 478px;
            font-size: 14px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: #666666;
            line-height: 20px;
          }
        }
      }


      .edu-box-m {
        margin-top: 24px;
        width: 200px;
        margin-right: 20px;
      }
      .edu-box-m1 {
        margin-top: 24px;
        margin-right: 20px;
        .info-other{
          height: auto;
          font-size: 14px;
          font-family: PingFangSC-Medium, PingFang SC;
          font-weight: 500;
          color: #333333;
        }
      }

      .resume-editbox {
        padding-top: 30px;
      }
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
    padding-top: 7px;
  }
  .avatar-uploader .avatar {
    width: 126px;
    height: 126px;
    display: block;
  }
  .avatar {
    width: 126px;
    height: 126px;
    border-radius: 5px;
    border: 1px solid #3F5FF4;
  }
  .description-text p {
    margin-bottom: 5px;
  }
  .time-text {
    font-size: 14px;
    font-weight: 400;
  }

  .position-text {
    font-size: 14px;
    font-weight: 500;
  }
}
</style>
