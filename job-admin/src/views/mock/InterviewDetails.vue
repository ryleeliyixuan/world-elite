<template>
  <div style="margin-top: 50px;margin-left: 100px;margin-right: 100px;">
    <el-row type="flex" justify="space-between">
      <el-col :span="12">
        <div style="font-weight:bold; font-size: 25px;">{{interviewer?interviewer.nickName:''}}</div>
      </el-col>
      <el-col :span="12">
        <div style="float: right;">
          <el-button type="warning" size="medium" icon="el-icon-edit">编辑</el-button>
          <el-button size="medium" icon="el-icon-delete">删除</el-button>
        </div>
      </el-col>
    </el-row>
    <div style="background-color: #0b51f6; height: 1px; margin-bottom: 30px;margin-top: 10px;"></div>

    <el-tabs v-model="activeName">
      <el-tab-pane label="概况信息" name="info" class="app-container">
        <div v-if="!interviewer">
          暂无数据
        </div>
        <div v-else style="font-size: 14px; text-align: center;">
          <el-row type="flex" justify="space-between" style="height:30px;line-height:30px;">
            <el-col :span="4" class="td-left" style="background-color: #EEF3FD;">
              <div>
                用户ID
              </div>
            </el-col>
            <el-col :span="8" class="td-left">
              {{interviewer.id}}
            </el-col>
            <el-col :span="4" class="td-left" style="background-color: #EEF3FD;">
              可提供面试内容
            </el-col>
            <el-col :span="8" class="td-right">
              {{direction}}
            </el-col>
          </el-row>
          <el-row type="flex" justify="space-between" style="height:30px;line-height:30px;">
            <el-col :span="4" class="td-left" style="background-color: #EEF3FD;">
              <div>
                用户名
              </div>
            </el-col>
            <el-col :span="8" class="td-left">
              {{userInfo.name}}
            </el-col>
            <el-col :span="4" class="td-left" style="background-color: #EEF3FD;">
              一段话自述
            </el-col>
            <el-col :span="8" class="td-right" style="overflow:auto">
              <el-tooltip placement="top">
                <div slot="content">{{interviewer.description}}</div>
                <span>{{interviewer.description}}</span>
              </el-tooltip>
            </el-col>
          </el-row>
          <el-row type="flex" justify="space-between" style="height:30px;line-height:30px;">
            <el-col :span="4" class="td-left" style="background-color: #EEF3FD;">
              <div>
                从事行业
              </div>
            </el-col>
            <el-col :span="8" class="td-left">
              {{interviewer.industry?interviewer.industry.name:''}}
            </el-col>
            <el-col :span="4" class="td-left" style="background-color: #EEF3FD;">
              个人标签
            </el-col>
            <el-col :span="8" class="td-right">

            </el-col>
          </el-row>
          <el-row type="flex" justify="space-between" style="height:30px;line-height:30px;">
            <el-col :span="4" class="td-left" style="background-color: #EEF3FD;">
              <div>
                经验年限
              </div>
            </el-col>
            <el-col :span="8" class="td-left">
              {{interviewer.experienceTime?interviewer.experienceTime.name:''}}
            </el-col>
            <el-col :span="4" class="td-left" style="background-color: #EEF3FD;">
              一句话介绍
            </el-col>
            <el-col :span="8" class="td-right">
              {{interviewer.introduction}}
            </el-col>
          </el-row>
          <el-row type="flex" justify="space-between" style="height:30px;line-height:30px;">
            <el-col :span="4" class="td-left" style="background-color: #EEF3FD;">
              <div>
                从业经验
              </div>
            </el-col>
            <el-col :span="8" class="td-left" style="overflow:auto">
              <el-tooltip placement="top">
                <div slot="content">
                  <span v-html="experience"></span>
                </div>
                <span v-html="experience"></span>
              </el-tooltip>

            </el-col>
            <el-col :span="4" class="td-left" style="background-color: #EEF3FD;">
              Email
            </el-col>
            <el-col :span="8" class="td-right">
              {{userInfo.email}}
            </el-col>
          </el-row>
          <el-row type="flex" justify="space-between" style="height:30px;line-height:30px;">
            <el-col :span="4" class="td-left-last" style="background-color: #EEF3FD;">
              <div>
                所在公司
              </div>
            </el-col>
            <el-col :span="8" class="td-left-last">
              {{interviewer.company?interviewer.company.name:interviewer.companyName}}
            </el-col>
            <el-col :span="4" class="td-left-last" style="background-color: #EEF3FD;">
              电话
            </el-col>
            <el-col :span="8" class="td-right-last">
              {{userInfo.phone}}
            </el-col>
          </el-row>
        </div>
      </el-tab-pane>
      <el-tab-pane label="日程列表" name="schedule"></el-tab-pane>
      <el-tab-pane label="面试记录" name="interviewRecords"></el-tab-pane>
      <el-tab-pane label="退款记录" name="refundRecord"></el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
  import {getInterviewDirection, getInterviewerBaseInfo, getInterviewerInfo} from "@/api/mock_api";

  export default {
    name: "interviewDetails",
    data() {
      return {
        activeName: 'info',
        interviewer: undefined, // 面试官vo
        direction: '', //面试方向列表
        experience: '',
        userInfo: undefined,
      };
    },

    mounted() {
      if (this.$route.query.id) {
        this.getData(this.$route.query.id);
      } else {
        this.$message.warning("面试官ID不存在");
      }
    },
    methods: {
      getData(id) {
        getInterviewerInfo(id).then(data => {
          this.interviewer = data.data;
          if(this.interviewer.experience){
            const experienceList = this.interviewer.experience;
            if (experienceList) {
              for (let i = 0; i < experienceList.length; i++) {
                this.experience += (i+1) + '.' + experienceList[i] + '<br>';
              }
            }
          }
        })
        getInterviewerBaseInfo(id).then(data => {
          this.userInfo = data.data;
        })
        getInterviewDirection(id).then(data => {
          const directionList = data.data;
          if (directionList) {
            for (let i = 0; i < directionList.length; i++) {
              this.direction += directionList[i].direction + '/';
            }
            this.direction = this.direction.substr(0, this.direction.length - 1);
          }
        })
      },
    }
  }
</script>
<style scoped lang="scss">
  .td-left {
    border-top: 1px solid #8CA9EC;
    border-left: 1px solid #8CA9EC;
  }

  .td-right {
    border-top: 1px solid #8CA9EC;
    border-left: 1px solid #8CA9EC;
    border-right: 1px solid #8CA9EC;
  }

  .td-left-last {
    border-top: 1px solid #8CA9EC;
    border-left: 1px solid #8CA9EC;
    border-bottom: 1px solid #8CA9EC;
  }

  .td-right-last {
    border: 1px solid #8CA9EC;
  }
</style>
