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

    <el-tabs v-model="activeName" @tab-click="handleClick">
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
      <el-tab-pane label="日程列表" name="time" class="app-container">

        <div class="filter-container">
          <el-col :span="12">
            <el-button type="success" @click="addReservationDialog = true" icon="el-icon-plus" size="small">添加日程
            </el-button>
          </el-col>
          <el-col :span="12">
            <div style="float: right;">
              <el-date-picker
                v-model="timeRange"
                type="datetimerange"
                align="right"
                start-placeholder="开始时间"
                end-placeholder="结束时间"
                :default-time="['00:00:00', '00:00:00']"
                size="small">
              </el-date-picker>
              <el-button type="primary" icon="el-icon-search" @click="queryInterviewerTime()" style="margin-left: 15px;"
                         size="small">查询
              </el-button>
            </div>
          </el-col>
        </div>

        <div style="margin-top: 60px">
          <el-table
            :key="tableKey"
            v-loading="listLoading"
            :data="interviewerTimeList"
            border
            highlight-current-row
            :default-sort="{prop: 'id'}"
          >
            <el-table-column sortable label="日程ID" prop="id">
              <template slot-scope="{row}">
                {{ row.id }}
              </template>
            </el-table-column>
            <el-table-column sortable label="开始时间">
              <template slot-scope="{row}">
                {{timestampToMonthDateHoursMinutes(row.beginTime)}}
              </template>
            </el-table-column>
            <el-table-column sortable label="结束时间">
              <template slot-scope="{row}">
                {{timestampToMonthDateHoursMinutes(row.endTime)}}
              </template>
            </el-table-column>
            <el-table-column label="类型">
              <template slot-scope="{row}">
                <span v-if="row.reservationList == null || row.reservationList.length === 0">全时段可预约</span>
                <span v-else>部分时段可预约</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" fixed="right" width="300px">
              <template slot-scope="{row}">
              <span>
                <el-button
                  type="primary"
                  size="mini"
                  icon="el-icon-edit"
                  @click="showReservation(row)"
                  v-if="row.reservationList != null && row.reservationList.length !== 0"
                >查看预约</el-button>

                <el-button
                  type="warning"
                  size="mini"
                  icon="el-icon-edit"
                  @click="editReservation(row)"
                >编辑</el-button>

                <el-button
                  type="danger"
                  size="mini"
                  icon="el-icon-delete"
                  @click="deleteTime(row)"
                  v-if="row.reservationList == null || row.reservationList.length === 0"
                >删除</el-button>
              </span>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-tab-pane>
      <el-tab-pane label="面试记录" name="records"></el-tab-pane>
      <el-tab-pane label="退款记录" name="refundRecord"></el-tab-pane>
    </el-tabs>

    <el-dialog title="预约详情" :visible.sync="reservationDialog" width="70%">
      <el-table :data="reservationList" highlight-current-row :default-sort="{prop: 'id'}">
        <el-table-column property="id" label="预约ID" width="100"></el-table-column>
        <el-table-column property="userId" label="面试者ID"></el-table-column>
        <el-table-column label="开始时间">
          <template slot-scope="{row}">
            {{timestampToMonthDateHoursMinutes(row.beginTime)}}
          </template>
        </el-table-column>
        <el-table-column label="结束时间">
          <template slot-scope="{row}">
            {{timestampToMonthDateHoursMinutes(row.endTime)}}
          </template>
        </el-table-column>
        <el-table-column property="direction.direction" label="面试方向"></el-table-column>
        <el-table-column property="direction.price" label="价格"></el-table-column>
      </el-table>
    </el-dialog>

    <el-dialog :visible.sync="addReservationDialog" center width="30%" @close="closeReservationDialog">
      <div slot="title" class="header-title">
        <span v-if="reservationDialogAddMode" class="title-name">添加可供预约时间</span>
        <span v-else>编辑可供预约时间  {{reservationEditId}}</span>
      </div>
      <div style="padding: 0 10px 10px 10px;">
        <span style="padding-right: 10px">起始日期</span>
        <el-date-picker
          style="width: 80%;"
          placeholder="起始日期"
          v-model="date"
          type="date"
          :picker-options="pickerOptions">
        </el-date-picker>
      </div>
      <div style="padding: 0 10px 10px 10px;">
        <span style="padding-right: 10px">开始时间</span>
        <el-time-select
          style="width: 80%;"
          placeholder="起始时间"
          v-model="beginTime"
          :picker-options="{
                      start: startFirst,
                      step: '00:30',
                      end: endFirst
                 }">
        </el-time-select>
      </div>
      <div style="padding: 0 10px 10px 10px;">
        <span style="padding-right: 10px">结束时间</span>
        <el-time-select
          placeholder="结束时间"
          style="width: 80%;"
          v-model="endTime"
          :picker-options="{
                      start: startSecond,
                      step: '00:30',
                      end: endSecond
                    }">
        </el-time-select>
      </div>
      <div style="padding: 0 10px 0 10px;">
        <span style="padding-right: 10px">日程重复</span>
        <el-select v-model="repeat" placeholder="预约类型" style="width: 80%;" :disabled="!reservationDialogAddMode">
          <el-option
            v-for="item in typeList"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </div>

      <span slot="footer" class="dialog-footer">
        <el-button @click="addReservationDialog = false" style="width: 100px;" size="small">取消</el-button>
        <el-button type="primary" @click="addReservationConfirm" style="width: 100px; margin-left: 20px;" size="small">确定
        </el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  import {
    getInterviewDirection,
    getInterviewerBaseInfo,
    getInterviewerInfo,
    getInterviewerTimeInfo
  } from "@/api/mock_api";
  import {timestampToDate, timestampToMinute} from "@/filters/filters";

  export default {
    name: "interviewDetails",
    data() {
      return {
        tableKey: 0,
        listLoading: true,
        activeName: 'info',
        interviewer: undefined, // 面试官vo
        direction: '', //面试方向列表
        experience: '',
        userInfo: undefined,

        timeRange: undefined,
        interviewerTimeQuery: {
          beginTime: undefined,
          endTime: undefined,
          interviewerId: undefined
        },
        interviewerTimeList: [],

        reservationDialog: false,
        addReservationDialog: false,
        reservationDialogAddMode: true, //对话框添加模式, false为编辑模式
        reservationEditId: undefined,
        reservationList: [],

        date: '', // 预约日期
        startFirst: '00:00', // 预约最小开始时间
        beginTime: 0, // 预约起始时间
        endFirst: '23:30', // 预约最大开始时间
        endTime: 0, // 预约结束时间
        endSecond: '24:00', // 预约最大结束时间
        repeat: "1", // 要预约的类型
        typeList: [{label: "不重复", value: "1"},
          {label: "按周重复", value: "2"},
          {label: "按月重复", value: "3"}
        ], // 重复类型  1=不重复，2=按周重复，3=按月重复

        pickerOptions: {
          disabledDate(time) {
            const temp = new Date();
            temp.setHours(0, 0, 0, 0);
            return time.getTime() < temp;
          },
          shortcuts: [{
            text: '今天',
            onClick(picker) {
              picker.$emit('pick', new Date());
            },
          },
            {
              text: '明天',
              onClick(picker) {
                const date = new Date();
                date.setTime(date.getTime() + 3600 * 1000 * 24);
                picker.$emit('pick', date);
              }
            },
            {
              text: '后天',
              onClick(picker) {
                const date = new Date();
                date.setTime(date.getTime() + 3600 * 1000 * 48);
                picker.$emit('pick', date);
              }
            }],
        },
      };
    },

    computed: {
      startSecond() {
        return this.getHourMinutes(new Date(this.getDateTime(this.date, this.beginTime || this.startFirst) + 30 * 60 * 1000));
      }
    },
    mounted() {
      if (this.$route.query.id) {
        this.getData(this.$route.query.id);
        this.interviewerTimeQuery.interviewerId = this.$route.query.id;
      } else {
        this.$message.warning("面试官ID不存在");
      }
    },
    methods: {
      getData(id) {
        getInterviewerInfo(id).then(data => {
          this.interviewer = data.data;
          if (this.interviewer.experience) {
            const experienceList = this.interviewer.experience;
            if (experienceList) {
              for (let i = 0; i < experienceList.length; i++) {
                this.experience += (i + 1) + '.' + experienceList[i] + '<br>';
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

      handleClick(tab, event) {
        if (tab.name === 'info') {
          if (this.interviewer == null && this.$route.query.id != null)
            this.getData(this.$route.query.id);
        } else if (tab.name === 'time') {
          if (this.interviewerTimeList == null || this.interviewerTimeList.length === 0)
            this.getInterviewerTimeList();
        } else if (tab.name === 'records') {

        } else if (tab.name === 'refundRecord') {

        }
      },
      /**
       * 获取面试官日程列表
       */
      getInterviewerTimeList() {
        this.listLoading = true;
        getInterviewerTimeInfo(this.interviewerTimeQuery).then(data => {
          this.interviewerTimeList = data.data;
          this.listLoading = false;
        })
      },

      queryInterviewerTime() {
        if (this.timeRange == null) {
          this.interviewerTimeQuery.beginTime = null;
          this.interviewerTimeQuery.endTime = null;
        } else {
          this.interviewerTimeQuery.beginTime = this.timeRange[0].getTime();
          this.interviewerTimeQuery.endTime = this.timeRange[1].getTime();
        }
        this.getInterviewerTimeList();
      },

      getHandledValue(num) {
        return num < 10 ? '0' + num : num;
      },

      timestampToMonthDateHoursMinutes(timeStamp) {
        if (timeStamp) {
          const d = new Date(parseInt(timeStamp));
          const year = this.getHandledValue(d.getFullYear());
          const month = this.getHandledValue(d.getMonth() + 1);
          const day = this.getHandledValue(d.getDate());
          const hours = this.getHandledValue(d.getHours());
          const minutes = this.getHandledValue(d.getMinutes());
          return year + '/' + month + '/' + day + ' ' + hours + ":" + minutes;
        } else {
          return '';
        }
      },

      showReservation(data) {
        this.reservationDialog = true;
        this.reservationList = data.reservationList;
      },
      editReservation(data) {
        //弹出可预约时间对话框切换为编辑模式
        this.reservationDialogAddMode = false;
        const d = new Date(data.beginTime);
        d.setHours(0,0,0,0);
        this.date = d;
        this.beginTime = timestampToMinute(data.beginTime);
        this.endTime = timestampToMinute(data.endTime);
        this.reservationEditId = data.id;

        this.addReservationDialog = true;
      },
      //删除一项可预约时间
      deleteTime(data) {
        this.$confirm('将永久删除此项, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$axios.delete("/mock/interview/time/" + data.id).then(data => {
            this.$message({
              type: 'success',
              message: '删除成功!'
            });
            this.getInterviewerTimeList();
          })
        });
      },

      // 添加可预约时间确认
      addReservationConfirm() {
        if (!this.beginTime) {
          this.$message.warning("请选择预约开始时间")
        } else if (!this.endTime) {
          this.$message.warning("请选择预约结束时间")
        } else if (!this.repeat) {
          this.$message.warning("请选择预约类型")
        } else {
          let interviewerId = this.$route.query.id;
          let beginTime = this.getDate(this.date, this.beginTime);
          let endTime = this.getDate(this.date, this.endTime) - (this.endTime === "24:00" ? 1 : 0);
          let repeat = this.repeat;
          if (endTime <= beginTime) {
            this.$message.warning("结束时间必须大于开始时间")
          } else {
            if (this.reservationDialogAddMode) {
              this.$axios.post("/mock/interview/time", {interviewerId, beginTime, endTime, repeat}).then(data => {
                this.getInterviewerTimeList();
                this.$message.success("添加成功")
                this.beginTime = undefined;
                this.endTime = undefined;
                this.addReservationDialog = false;
              })
            } else {
                if(this.reservationEditId ==null)
                  this.$message.warning("要编辑的记录ID不存在");
                else{
                  repeat = '1';
                  this.$axios.patch("/mock/interview/time/" + this.reservationEditId , {beginTime, endTime, repeat}).then(data => {
                    this.getInterviewerTimeList();
                    this.$message.success("更新成功")
                    this.beginTime = undefined;
                    this.endTime = undefined;
                    this.addReservationDialog = false;
                  })
                }
            }
          }
        }
      },

      // 工具方法
      // Date + "12:00"  => (Date 12:00).getTime();
      getDateTime(date, time) {
        if (date) {
          let t = time.split(":");
          const temp = new Date(date);
          temp.setHours(t[0], t[1])
          return temp.getTime();
        } else {
          return Date.now();
        }
      },
      getHourMinutes(time, zero) {
        if (time) {
          let minutes = time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes();
          if (!zero && time.getHours() === 0 && time.getMinutes() === 0) {
            return "24:00";
          } else if (time.getHours() < 10) {
            return '0' + time.getHours() + ":" + minutes;
          } else {
            return time.getHours() + ":" + minutes;
          }
        } else {
          return "00:00";
        }
      },
      getDate(date, time) {
        let t = time.split(":");
        date.setHours(t[0], t[1], 0, 0)
        return date.getTime();
      },
      getZeroOfToday() {
        const temp = new Date();
        temp.setHours(0, 0, 0, 0);
        return temp.getTime();
      },

      closeReservationDialog() {
        this.reservationDialogAddMode = true;
        this.reservationEditId = null;
      }
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
