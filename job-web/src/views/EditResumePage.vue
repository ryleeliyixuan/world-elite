<template>
  <div class="app-container" v-if="getResume == true">
    <b-container>
      <el-tabs
        v-model="editableTabsValue"
        type="card"
        editable
        @edit="handleTabsEdit"
        @tab-click="saveResumeId"
      >
        <el-tab-pane
          v-for="(item, index) in resume"
          :key="item.id"
          :label="item.title"
          :name="item.id"
          style="margin-top: -100px"
        >
          <b-row>
            <b-media class="resume-wrapper">
              <div class="resume-body">
                <div
                  class="required"
                  v-if="
                    item.name == null ||
                    item.email == null ||
                    item.birth == null ||
                    item.gender == null ||
                    !item.userExpectJob.expectCity ||
                    !item.userExpectJob.expectPosition ||
                    !item.userExpectJob.expectWorkType ||
                    !item.resumeEduList
                  "
                >
                  <span class="required-text"
                    >还有必填没有填完，此简历将无法投递和被HR搜索到哦。</span
                  >
                  <span
                    ><el-link class="required-link" @click="requiredEdit(index)"
                      >去填写</el-link
                    ></span
                  >
                </div>
                <el-row
                  class="rowbasic"
                  style="
                    display: inline-flex;
                    height: 70px;
                    padding: 3px;
                    position: relative;
                  "
                >
                  <div
                    style="display: inline-flex; position: absolute"
                    class="titleText"
                  >
                    <span style="padding-top: 6px">简历名称：</span>
                    <div style="padding-top: 6px" v-if="showEditTitle == false">
                      <span>{{ item.title }}</span>
                      <svg-icon
                        class="svg-cls"
                        icon-class="edit"
                        style="
                          width: 18px;
                          height: 19px;
                          margin-left: 5px;
                          margin-right: 50px;
                        "
                        @click="editTitle"
                      ></svg-icon>
                    </div>
                    <div v-if="showEditTitle == true" style="margin-top: -3px">
                      <el-form ref="resumeTitleForm" :model="resumeTitleForm">
                        <el-form-item prop="title" class="editTitle">
                          <el-input
                            v-model="resumeTitleForm.title"
                            type="text"
                            placeholder="请输入简历名称"
                            @change="saveEditTitle"
                            maxlength="8"
                            show-word-limit
                          ></el-input>
                        </el-form-item>
                      </el-form>
                    </div>
                  </div>
                  <div class="resume-updateTime" style="margin-left: 210px">
                    更新时间：{{ item.updateTime }}
                  </div>
                  <div style="position: absolute; margin-left: 146px">
                    <div
                      class="help-text"
                      v-show="seen"
                      @mouseleave="onMouseOut"
                    >
                      HR在搜索简历时只会搜索到最符合搜索条件的那一版简历。
                      但是当你有多版简历同等程度满足HR搜索条件时，优先级更高的简历会出现在HR面前。
                    </div>
                  </div>
                  <div
                    style="
                      margin-right: 0px;
                      margin-top: -4px;
                      padding-left: 6px;
                    "
                  >
                    <div style="display: flex">
                      <div>
                        <svg-icon
                          class="svg-cls"
                          icon-class="help-mark"
                          style="
                            width: 13px;
                            height: 31px;
                            padding-top: 12px;
                            padding-left: 3px;
                          "
                          @mouseenter="onMouseOver"
                        ></svg-icon>
                        <span
                          class="resume-updateTime"
                          style="padding-left: 8px; padding-right: 9px"
                          >优先级
                          <span
                            style="
                              color: #d9001b;
                              font-size: 20px;
                              height: 3px;
                              width: 3px;
                              vertical-align: sub;
                              padding-right: 9px;
                            "
                            >*</span
                          >
                          ：</span
                        >
                      </div>
                      <div style="padding-top: 10px">
                        <el-form
                          class="selectleave"
                          ref="resumeForm5"
                          :model="resumeForm5"
                        >
                          <span v-if="showPriority == true">
                            <el-select
                              v-model="resumeForm5.priority"
                              @change="handleSavePriority"
                              placeholder="请选择"
                            >
                              <el-option
                                :label="dict.name"
                                :value="dict.value"
                                v-for="dict in priorityList"
                                :key="dict.id"
                              />
                            </el-select>
                          </span>
                        </el-form>
                      </div>
                    </div>
                  </div>
                </el-row>
                <div class="resume-box" id="Resume-Basic">
                  <div v-if="showBasicDialog == true">
                    <el-row>
                      <div
                        style="
                          width: 540px;
                          display: inline-block;
                          margin-right: 75px;
                        "
                      >
                        <img
                          src="../assets/point.png"
                          style="padding-right: 9px; padding-bottom: 7px"
                        />
                        <span class="resume-base">基本信息</span>
                        <span class="resume-red">*</span>
                      </div>
                    </el-row>
                    <div class="resume-box-edit">
                      <el-form
                        ref="resumeForm"
                        :model="resumeForm"
                        :rules="resumeFormRules"
                        label-width="80px"
                      >
                        <div style="display: flex; height: 45px">
                          <div style="width: 350px">
                            <el-form-item
                              label="姓名:"
                              prop="name"
                              class="m-input-text-width"
                            >
                              <el-input
                                v-model="resumeForm.name"
                                placeholder="你的真实姓名"
                                maxlength="20"
                                show-word-limit
                              ></el-input>
                            </el-form-item>
                          </div>
                          <div>
                            <div style="padding-top: 8px">
                              <span class="politic-title">政治面貌：</span>
                              <el-select
                                class="politic-sel"
                                v-model="resumeForm.maritalStatus"
                                placeholder=""
                              >
                                <el-option
                                  v-for="item in maritalStatusList"
                                  :key="item.value"
                                  :label="item.name"
                                  :value="item.value"
                                >
                                </el-option>
                              </el-select>
                            </div>
                          </div>
                        </div>
                        <div style="display: flex; height: 45px">
                          <div style="width: 310px; padding-left: 1px">
                            <el-form-item
                              label="性别:"
                              prop="gender"
                              class="m-input-text-width"
                            >
                              <el-radio-group
                                v-model="resumeForm.gender"
                                class="radio-gender"
                              >
                                <el-radio :label="1">男</el-radio>
                                <el-radio :label="2">女</el-radio>
                              </el-radio-group>
                            </el-form-item>
                          </div>
                          <div style="padding-left: 23px">
                            <el-form-item
                              label="现居城市:"
                              prop="curPlace"
                              class="m-input-text-width"
                            >
                              <el-input
                                v-model="resumeForm.curPlace"
                                placeholder="请输入现居城市"
                              ></el-input>
                            </el-form-item>
                          </div>
                        </div>
                        <div style="display: flex; height: 45px">
                          <div style="width: 334px">
                            <el-form-item
                              label="生日:"
                              prop="birth"
                              class="m-input-text-width"
                            >
                              <el-date-picker
                                v-model="resumeForm.birth"
                                :picker-options="oldDatePickerOptions"
                                type="date"
                                value-format="yyyy-MM-dd"
                                placeholder="选择日期"
                              ></el-date-picker>
                            </el-form-item>
                          </div>
                          <div>
                            <el-form-item
                              label="入职时间:"
                              prop="returnTime"
                              class="m-input-text-width"
                            >
                              <el-date-picker
                                v-model="resumeForm.returnTime"
                                type="date"
                                placeholder="选择日期"
                              ></el-date-picker>
                            </el-form-item>
                          </div>
                        </div>
                        <div style="display: flex">
                          <div style="width: 334px">
                            <el-form-item
                              label="邮箱:"
                              prop="email"
                              class="m-input-text-width"
                            >
                              <el-input
                                v-model="resumeForm.email"
                                placeholder="请输入新邮箱"
                              ></el-input>
                            </el-form-item>
                          </div>
                          <div>
                            <el-form-item
                              label="手机号码:"
                              prop="phone"
                              class="m-input-text-width"
                            >
                              <el-input
                                v-model="resumeForm.phone"
                                placeholder="请输入新手机号码"
                              ></el-input>
                            </el-form-item>
                          </div>
                        </div>
                        <div
                          style="
                            display: flex;
                            padding-bottom: 22px;
                            margin-left: 238px;
                          "
                        >
                          <el-button
                            class="btn1"
                            @click="handleSaveResumeBasic(index)"
                            >保存
                          </el-button>
                          <el-button
                            class="btn2"
                            @click="showBasicDialog = false"
                            >取消</el-button
                          >
                        </div>
                      </el-form>
                    </div>
                  </div>
                  <div v-if="showBasicDialog == false">
                    <el-row>
                      <div
                        style="
                          width: 540px;
                          display: inline-block;
                          margin-right: 75px;
                        "
                      >
                        <img
                          src="../assets/point.png"
                          style="padding-right: 9px; padding-bottom: 7px"
                        />
                        <span class="resume-base">基本信息</span>
                        <span class="resume-red">*</span>
                      </div>
                      <span>
                        <svg-icon
                          class="svg-cls"
                          icon-class="edit"
                          style="width: 18px; height: 19px"
                          v-on:click="handleEditResumeBasic"
                        ></svg-icon>
                      </span>
                    </el-row>
                    <div class="resume-basicinfo row mt-3">
                      <el-form
                        class="avatorHolder"
                        ref="resumeForm3"
                        :model="resumeForm3"
                      >
                        <!--                                                :on-change文件状态改变时的钩子，添加文件、上传成功和上传失败时都会被调用-->
                        <!--                                                :auto-upload是否在选取文件后立即进行上传-->
                        <upload-img :src="item.avatar" @upload="onUpload" />
                      </el-form>
                      <div class="resume-info">
                        <div class="info-other-row-l">
                          <div style="height: 34px">
                            <el-row
                              class="info-name"
                              style="width: 300px"
                              v-if="item.name && item.name != ''"
                              >{{ item.name }}</el-row
                            >
                          </div>
                          <el-row class="info-other"
                            >性别：
                            <span v-if="item.gender == 1">男</span>
                            <span v-if="item.gender == 2">女</span>
                          </el-row>
                          <el-row class="info-other"
                            >年龄：{{ item.age }} 岁</el-row
                          >
                          <el-row class="info-other"
                            >邮箱：{{ item.email }}</el-row
                          >
                          <el-row class="info-other"
                            >手机：{{ item.phone }}</el-row
                          >
                        </div>
                        <div class="info-other-row-m">
                          <el-row class="info-other"
                            >政治面貌：
                            <span v-if="item.maritalStatus == 0"
                              >中共党员（含预备党员）</span
                            >
                            <span v-if="item.maritalStatus == 1">民主党派</span>
                            <span v-if="item.maritalStatus == 2"
                              >无党派人士</span
                            >
                            <span v-if="item.maritalStatus == 3">团员</span>
                            <span v-if="item.maritalStatus == 4">群众</span>
                          </el-row>
                          <el-row class="info-other"
                            >现居城市：{{ item.curPlace }}</el-row
                          >
                          <el-row class="info-other"
                            >入职时间：{{ item.returnTime }}</el-row
                          >
                        </div>
                      </div>
                    </div>
                  </div>
                  <el-divider></el-divider>
                </div>
                <div class="resume-box" id="Resume-Edu">
                  <div v-if="showEduDialog == false">
                    <el-row>
                      <div
                        style="
                          width: 540px;
                          display: inline-block;
                          margin-right: 75px;
                        "
                      >
                        <img
                          src="../assets/point.png"
                          style="padding-right: 9px; padding-bottom: 7px"
                        />
                        <span class="resume-base">教育经历</span>
                        <span class="resume-red">*</span>
                      </div>
                      <span>
                        <svg-icon
                          icon-class="edu-add"
                          style="width: 19px; height: 19px"
                          v-on:click="handleEditResumeEdu('create')"
                        ></svg-icon>
                      </span>
                    </el-row>
                    <el-row style="padding-left: 15px">
                      <div class="resume-eduinfo">
                        <div style="display: inline-block">
                          <div
                            class="resume-edu"
                            v-for="resumeEdu in item.resumeEduList"
                            :key="resumeEdu.id"
                          >
                            <div class="edu-box-l" style="width: 330px">
                              <el-row class="edu-school"
                                >{{ resumeEdu.schoolName }}
                              </el-row>
                              <el-row
                                class="info-other"
                                v-if="
                                  resumeEdu.degree && resumeEdu.degree != ''
                                "
                                >学历：{{ resumeEdu.degree.name }}
                              </el-row>
                              <el-row class="info-other"
                                >专业：{{ resumeEdu.majorName }}
                              </el-row>
                            </div>
                            <div class="edu-box-m">
                              <el-row class="info-other"
                                >在校时间：
                                <span
                                  style="
                                    font-size: 14px;
                                    font-family: PingFangSC-Regular, PingFang SC;
                                    font-weight: 400;
                                  "
                                >
                                  {{
                                    resumeEdu.startTime == resumeEdu.finishTime
                                      ? "在读"
                                      : `${resumeEdu.startTime}到${resumeEdu.finishTime}`
                                  }}
                                </span>
                              </el-row>
                              <el-row class="info-other"
                                >GPA：{{ resumeEdu.gpa }}</el-row
                              >
                            </div>
                            <div>
                              <el-row style="height: 30px">
                                <svg-icon
                                  icon-class="edit"
                                  style="width: 18px; height: 19px"
                                  v-on:click="
                                    handleEditResumeEdu('update', resumeEdu)
                                  "
                                ></svg-icon>
                              </el-row>
                              <el-row style="height: 30px">
                                <svg-icon
                                  icon-class="edu-del"
                                  style="width: 19px; height: 21px"
                                  v-on:click="handleDelResumeEdu(resumeEdu.id)"
                                ></svg-icon>
                              </el-row>
                            </div>
                          </div>
                        </div>
                      </div>
                    </el-row>
                  </div>
                  <div v-if="showEduDialog == true">
                    <el-row>
                      <div
                        style="
                          width: 540px;
                          display: inline-block;
                          margin-right: 75px;
                        "
                      >
                        <img
                          src="../assets/point.png"
                          style="padding-right: 9px; padding-bottom: 7px"
                        />
                        <span class="resume-base">教育经历</span>
                        <span class="resume-red">*</span>
                      </div>
                    </el-row>
                    <div class="resume-box-edit">
                      <el-form
                        ref="resumeEduForm"
                        :model="resumeEduForm"
                        :rules="resumeEduFormRules"
                        label-width="100px"
                      >
                        <div style="display: flex; height: 45px">
                          <div style="width: 324px">
                            <el-form-item
                              label="学校:"
                              prop="schoolName"
                              class="m-input-text-width"
                            >
                              <el-input
                                v-model="resumeEduForm.schoolName"
                                placeholder="请输入学校名称"
                              ></el-input>
                            </el-form-item>
                          </div>
                          <div>
                            <el-form-item
                              label="在校时间："
                              prop="workingDates"
                              class="m-input-text-width"
                            >
                              <el-date-picker
                                style="width: 200px"
                                type="monthrange"
                                range-separator="-"
                                start-placeholder="入校时间"
                                end-placeholder="毕业时间"
                                value-format="yyyy-MM"
                                v-model="resumeEduForm.workingDates"
                                :disabled="schoolCheck == 1"
                              />
                            </el-form-item>
                          </div>
                        </div>
                        <div style="display: flex; height: 45px">
                          <div style="width: 310px">
                            <el-form-item
                              label="学历"
                              prop="degreeId"
                              class="m-input-text-width"
                            >
                              <el-select
                                v-model="resumeEduForm.degreeId"
                                placeholder="请选择学历"
                              >
                                <el-option
                                  :label="dict.name"
                                  :value="dict.id"
                                  v-for="dict in degreeList"
                                  :key="dict.id"
                                />
                              </el-select>
                            </el-form-item>
                          </div>
                          <div style="padding-left: 110px; padding-top: 10px">
                            <el-checkbox
                              v-model="schoolCheck"
                              :true-label="1"
                              :false-label="0"
                              class="m-input-text-width"
                              >在读
                            </el-checkbox>
                          </div>
                        </div>
                        <div style="display: flex; height: 45px">
                          <div style="width: 334px">
                            <el-form-item
                              label="专业:"
                              prop="majorName"
                              class="m2-input-text-width"
                            >
                              <el-input
                                v-model="resumeEduForm.majorName"
                                placeholder="请输入所在专业"
                              ></el-input>
                            </el-form-item>
                          </div>
                          <div>
                            <el-form-item
                              label="GPA:"
                              prop="gpa"
                              class="n-input-text-width"
                              label-width="90px"
                            >
                              <el-input
                                v-model="resumeEduForm.gpa"
                                type="number"
                                placeholder="请输入GPA"
                                min="0"
                              ></el-input>
                            </el-form-item>
                          </div>
                        </div>
                        <div
                          style="
                            display: flex;
                            padding-bottom: 22px;
                            margin-left: 238px;
                          "
                        >
                          <el-button
                            class="btn1"
                            @click="handleSaveResumeEdu(index)"
                          >
                            保存
                          </el-button>
                          <el-button class="btn2" @click="showEduDialog = false"
                            >取消</el-button
                          >
                        </div>
                      </el-form>
                    </div>
                  </div>
                  <el-divider></el-divider>
                </div>
                <div class="resume-box" id="Resume-ExpectJob">
                  <div v-if="showExpectJobDialog == false">
                    <el-row>
                      <div
                        style="
                          width: 540px;
                          display: inline-block;
                          margin-right: 75px;
                        "
                      >
                        <img
                          src="../assets/point.png"
                          style="padding-right: 9px; padding-bottom: 7px"
                        />
                        <span class="resume-base">求职意向</span>
                        <span class="resume-red">*</span>
                      </div>
                      <span>
                        <svg-icon
                          icon-class="edit"
                          style="width: 18px; height: 20px"
                          v-on:click="
                            handleEditExpectJob(), (showExpectJobDialog = true)
                          "
                        ></svg-icon>
                      </span>
                    </el-row>
                    <el-row style="padding-left: 18px">
                      <div class="resume-eduinfo">
                        <div class="resume-edu">
                          <div class="edu-box-l1">
                            <el-row class="edu-school" style="height: 24px">
                              {{ item.userExpectJob.expectPosition }}
                            </el-row>
                            <el-row class="info-other"
                              >预期薪资：
                              <span
                                v-if="
                                  item.userExpectJob.salaryId &&
                                  item.userExpectJob.salaryId != ''
                                "
                              >
                                {{ salaryNameList }}
                              </span>
                            </el-row>
                            <el-row class="info-other"
                              >工作类型：
                              <span>{{
                                item.userExpectJob.expectWorkType
                              }}</span>
                            </el-row>
                          </div>
                          <div class="edu-box-m1">
                            <el-row class="info-other"
                              >期望行业：
                              <span
                                v-if="
                                  item.userExpectJob.industry &&
                                  item.userExpectJob.industry != ''
                                "
                              >
                                {{ item.userExpectJob.industry }}
                              </span>
                            </el-row>
                            <el-row class="info-other"
                              >工作城市：
                              <span> {{ item.userExpectJob.expectCity }} </span>
                            </el-row>
                          </div>
                        </div>
                      </div>
                    </el-row>
                  </div>
                  <div v-if="showExpectJobDialog == true">
                    <el-row>
                      <div
                        style="
                          width: 540px;
                          display: inline-block;
                          margin-right: 75px;
                        "
                      >
                        <img
                          src="../assets/point.png"
                          style="padding-right: 9px; padding-bottom: 7px"
                        />
                        <span class="resume-base">求职意向</span>
                        <span class="resume-red">*</span>
                      </div>
                    </el-row>
                    <div class="resume-box-edit">
                      <div style="padding-top: 20px">
                        <el-form
                          ref="expectJobForm"
                          :model="expectJobForm"
                          :rules="expectJobFormRules"
                          label-width="100px"
                        >
                          <div style="display: flex; height: 50px">
                            <div style="width: 324px; padding-left: 10px">
                              <el-form-item
                                label="意向职位："
                                prop="expectPosition"
                                class="m-input-text-width"
                              >
                                <el-input
                                  v-model="expectJobForm.expectPosition"
                                  placeholder="请输入意向职位"
                                ></el-input>
                              </el-form-item>
                            </div>
                            <div style="padding-left: 30px">
                              <el-form-item
                                label="意向城市:"
                                prop="expectCity"
                                class="m-input-text-width"
                              >
                                <!-- <el-autocomplete
                                  v-model="expectJobForm.expectCity"
                                  :fetch-suggestions="searchCityName"
                                  value-key="name"
                                  placeholder="请输入意向城市"
                                ></el-autocomplete> -->
                                <el-cascader
                                  class="section-input-box"
                                  placeholder="意向工作城市"
                                  :show-all-levels="true"
                                  :options="cityOptions"
                                  :props="cityIdProps"
                                  clearable
                                  v-model="expectJobForm.expectCity"
                                >
                                </el-cascader>
                              </el-form-item>
                            </div>
                          </div>
                          <div
                            style="
                              display: flex;
                              height: 30px;
                              padding-left: 10px;
                            "
                          >
                            <div>
                              <el-form-item
                                label="工作类型："
                                prop="expectWorkType"
                                class="m-input-text-width"
                              >
                                <el-radio-group
                                  v-model="expectJobForm.expectWorkType"
                                  class="radio-gender"
                                >
                                  <el-radio label="全职">全职</el-radio>
                                  <el-radio label="兼职">兼职</el-radio>
                                  <el-radio label="实习">实习</el-radio>
                                </el-radio-group>
                              </el-form-item>
                            </div>
                            <div style="padding-left: 60px">
                              <el-form-item
                                label="薪资范围:"
                                class="m-input-text-width"
                              >
                                <el-select
                                  multiple
                                  collapse-tags
                                  v-model="expectJobForm.salaryId"
                                  placeholder="薪资范围"
                                  :disabled="salaryCheck == 1"
                                >
                                  <el-option
                                    v-for="item in salaryOptions"
                                    :key="item.id"
                                    :label="item.name"
                                    :value="item.id"
                                  ></el-option>
                                </el-select>
                              </el-form-item>
                            </div>
                          </div>
                          <div
                            style="
                              display: flex;
                              height: 45px;
                              padding-top: 16px;
                              margin-bottom: 40px;
                            "
                          >
                            <div style="width: 334px">
                              <el-form-item
                                label="期望行业:"
                                class="m-input-text-width"
                              >
                                <el-select
                                  v-model="expectJobForm.industry"
                                  placeholder="期望行业"
                                >
                                  <el-option
                                    v-for="item in companyIndustryOptions"
                                    :key="item.id"
                                    :label="item.name"
                                    :value="item.name"
                                  ></el-option>
                                </el-select>
                              </el-form-item>
                            </div>
                            <div style="padding-left: 116px; padding-top: 10px">
                              <el-checkbox
                                v-model="salaryCheck"
                                :true-label="1"
                                :false-label="0"
                                class="m-input-text-width"
                                >面议
                              </el-checkbox>
                            </div>
                          </div>
                          <div
                            style="
                              display: flex;
                              padding-bottom: 22px;
                              margin-left: 238px;
                              margin-top: 10px;
                            "
                          >
                            <el-button
                              class="btn1"
                              @click="handleSaveExpectJob(index)"
                              >保存
                            </el-button>
                            <el-button
                              class="btn2"
                              @click="showExpectJobDialog = false"
                              >取消
                            </el-button>
                          </div>
                        </el-form>
                      </div>
                    </div>
                  </div>
                  <el-divider></el-divider>
                </div>
                <div class="resume-box" id="Resume-Experience">
                  <div v-if="showExpDialog == false">
                    <div style="display: inline-flex">
                      <div style="width: 615px">
                        <img
                          src="../assets/point.png"
                          style="padding-right: 9px; padding-bottom: 7px"
                        />
                        <span class="resume-base">工作/实习经历</span>
                      </div>
                      <span>
                        <svg-icon
                          icon-class="edu-add"
                          style="width: 19px; height: 19px"
                          v-on:click="handleEditResumeExp('create')"
                        ></svg-icon>
                      </span>
                    </div>
                    <div
                      v-for="resumeExp in item.resumeExpList"
                      :key="resumeExp.id"
                    >
                      <div
                        style="
                          display: inline-flex;
                          margin-top: 20px;
                          padding-left: 15px;
                        "
                      >
                        <div>
                          <span class="resume-box-text">
                            {{ resumeExp.company }}</span
                          >
                        </div>
                        <div style="padding-left: 31px">
                          <span>{{ resumeExp.post }}</span>
                        </div>
                        <div style="padding-left: 31px">
                          <span>{{ resumeExp.workType }} </span>
                        </div>
                      </div>
                      <div style="display: inline-flex; padding-left: 15px">
                        <div style="width: 600px">
                          <div style="margin-top: 9px; display: inline-flex">
                            <div style="width: 380px">
                              <span class="resume-box-text-title">行业：</span>
                              <span
                                class="resume-box-text-data"
                                v-if="
                                  resumeExp.industry && resumeExp.industry != ''
                                "
                              >
                                {{
                                  resumeExp.industry
                                    .split('"')
                                    .join("")
                                    .split("[")
                                    .join("")
                                    .split("]")
                                    .join("")
                                    .split(",")
                                    .join(" ")
                                }}
                              </span>
                            </div>
                            <div>
                              <span class="resume-box-text-title"
                                >在职时间：</span
                              >
                              <span class="resume-box-text-data">
                                {{
                                  resumeExp.onWork == 1
                                    ? "在职"
                                    : `${resumeExp.startTime}到${resumeExp.finishTime}`
                                }}
                              </span>
                            </div>
                          </div>
                          <div style="display: inline-flex; margin-top: 9px">
                            <div class="resume-box-text-title">工作描述：</div>
                            <div class="resume-box-text-html">
                              {{ resumeExp.description }}
                            </div>
                          </div>
                        </div>
                        <div style="width: 20px">
                          <el-row style="height: 30px">
                            <svg-icon
                              icon-class="edit"
                              style="width: 18px; height: 19px"
                              v-on:click="
                                handleEditResumeExp('update', resumeExp)
                              "
                            ></svg-icon>
                          </el-row>
                          <el-row style="height: 30px">
                            <svg-icon
                              icon-class="edu-del"
                              style="width: 19px; height: 21px"
                              v-on:click="handleDelResumeExp(resumeExp.id)"
                            ></svg-icon>
                          </el-row>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div v-if="showExpDialog == true">
                    <div style="width: 615px">
                      <img
                        src="../assets/point.png"
                        style="padding-right: 9px; padding-bottom: 7px"
                      />
                      <span class="resume-base">工作/实习经历</span>
                    </div>
                    <div class="resume-box-edit">
                      <el-form
                        ref="resumeExpForm"
                        :model="resumeExpForm"
                        :rules="resumeExpFormRules"
                        label-width="100px"
                      >
                        <div style="display: flex; height: 45px">
                          <div style="width: 324px">
                            <el-form-item
                              label="工作类型："
                              prop="workType"
                              class="m-input-text-width"
                            >
                              <el-radio-group
                                v-model="resumeExpForm.workType"
                                class="radio-gender"
                              >
                                <el-radio label="全职">全职</el-radio>
                                <el-radio label="兼职">兼职</el-radio>
                                <el-radio label="实习">实习</el-radio>
                              </el-radio-group>
                            </el-form-item>
                          </div>
                          <div>
                            <el-form-item
                              label="在职时间："
                              class="m-input-text-width"
                            >
                              <el-date-picker
                                v-model="resumeExpForm.workingDates"
                                type="monthrange"
                                value-format="yyyy-MM"
                                start-placeholder="入职时间"
                                end-placeholder="离职时间"
                                :disabled="resumeExpForm.onWork == 1"
                              />
                            </el-form-item>
                          </div>
                        </div>
                        <div style="display: flex; height: 45px">
                          <div style="width: 310px">
                            <el-form-item
                              label="公司名称："
                              prop="company"
                              class="m-input-text-width"
                              label-width="100px"
                            >
                              <el-input
                                v-model="resumeExpForm.company"
                                placeholder="请输入公司名称"
                                maxlength="30"
                                show-word-limit
                              />
                            </el-form-item>
                          </div>
                          <div style="padding-left: 110px; padding-top: 10px">
                            <el-checkbox
                              v-model="resumeExpForm.onWork"
                              :true-label="1"
                              :false-label="0"
                              class="m-input-text-width"
                              >在职
                            </el-checkbox>
                          </div>
                        </div>
                        <div style="display: flex; height: 45px">
                          <div style="width: 320px">
                            <el-form-item
                              label="职位名称："
                              prop="post"
                              class="m-input-text-width"
                              label-width="100px"
                            >
                              <el-input
                                v-model="resumeExpForm.post"
                                placeholder="请填写职位名称"
                              >
                              </el-input>
                            </el-form-item>
                          </div>
                          <div>
                            <el-form-item
                              label="行业类型:"
                              class="m-input-text-width"
                            >
                              <el-select
                                v-model="resumeExpForm.industry"
                                placeholder="行业类型"
                              >
                                <el-option
                                  v-for="item in companyIndustryOptions"
                                  :key="item.id"
                                  :label="item.name"
                                  :value="item.name"
                                ></el-option>
                              </el-select>
                            </el-form-item>
                          </div>
                        </div>
                        <div v-if="showdepart == true">
                          <el-form-item
                            label="所属部门："
                            prop="depart"
                            class="m-input-text-width"
                          >
                            <el-input
                              v-model="resumeExpForm.depart"
                              placeholder="请填写部门信息"
                            ></el-input>
                          </el-form-item>
                        </div>
                        <div style="padding-top: 10px">
                          <el-form-item
                            label="工作内容："
                            prop="description"
                            class="m1-input-text-width"
                          >
                            <el-input
                              v-model="resumeExpForm.description"
                              type="textarea"
                              :rows="6"
                              maxlength="500"
                              show-word-limit
                              placeholder="请输入工作内容"
                              class="intro-text"
                            >
                            </el-input>
                          </el-form-item>
                        </div>

                        <div
                          style="
                            display: flex;
                            padding-bottom: 22px;
                            margin-left: 238px;
                          "
                        >
                          <el-button
                            class="btn1"
                            @click="handleSaveResumeExp(index)"
                          >
                            保存
                          </el-button>
                          <el-button class="btn2" @click="showExpDialog = false"
                            >取消</el-button
                          >
                        </div>
                      </el-form>
                    </div>
                  </div>
                  <el-divider></el-divider>
                </div>
                <div class="resume-box" id="Resume-Practice">
                  <div v-if="showPracticeDialog == false">
                    <el-row>
                      <div
                        style="
                          width: 540px;
                          display: inline-block;
                          margin-right: 75px;
                        "
                      >
                        <img
                          src="../assets/point.png"
                          style="padding-right: 9px; padding-bottom: 7px"
                        />
                        <span class="resume-base">项目/其它经历</span>
                      </div>
                      <span>
                        <svg-icon
                          icon-class="edu-add"
                          style="width: 19px; height: 19px"
                          v-on:click="handleEditResumePractice('create')"
                        ></svg-icon>
                      </span>
                    </el-row>
                    <el-row>
                      <div style="display: inline-block; padding-left: 15px">
                        <div
                          class="resume-info"
                          v-for="practice in item.resumePracticeList"
                          :key="practice.id"
                        >
                          <div class="resume-edu" style="width: 601px">
                            <el-row style="width: 601px">
                              <span class="resume-box-text">{{
                                practice.title
                              }}</span>
                              <span
                                class="resume-box-text"
                                style="padding-left: 31px"
                                >{{
                                  practice.onWork == 1
                                    ? "项目进行中"
                                    : `${practice.startTime}到${practice.finishTime}`
                                }}</span
                              >
                            </el-row>
                            <div class="edu-box">
                              <el-row class="expinfo-other-row">
                                <span class="expinfo-other" style="width: 42px"
                                  >介绍：</span
                                >
                                <span class="expinfo-other-html">{{
                                  practice.description
                                }}</span>
                              </el-row>
                            </div>
                          </div>

                          <div class="resume-editbox">
                            <el-row style="height: 30px">
                              <svg-icon
                                icon-class="edit"
                                style="width: 18px; height: 19px"
                                v-on:click="
                                  handleEditResumePractice('update', practice)
                                "
                              ></svg-icon>
                            </el-row>
                            <el-row style="height: 30px">
                              <svg-icon
                                icon-class="edu-del"
                                style="width: 19px; height: 21px"
                                v-on:click="
                                  handleDelResumePractice(practice.id)
                                "
                              ></svg-icon>
                            </el-row>
                          </div>
                        </div>
                      </div>
                    </el-row>
                  </div>
                  <div v-if="showPracticeDialog == true">
                    <el-row>
                      <div
                        style="
                          width: 540px;
                          display: inline-block;
                          margin-right: 75px;
                        "
                      >
                        <img
                          src="../assets/point.png"
                          style="padding-right: 9px; padding-bottom: 7px"
                        />
                        <span class="resume-base">项目/其它经历</span>
                      </div>
                    </el-row>
                    <div class="resume-box-edit">
                      <div style="padding-top: 20px">
                        <el-form
                          ref="resumePracticeForm"
                          :model="resumePracticeForm"
                          :rules="resumePracticeFormRules"
                          label-width="100px"
                        >
                          <div style="display: flex; height: 70px">
                            <div>
                              <el-form-item
                                label="项目名称："
                                prop="title"
                                class="m-input-text-width"
                              >
                                <el-input
                                  v-model="resumePracticeForm.title"
                                  placeholder="请填写项目名称"
                                  :maxlength="50"
                                  show-word-limit
                                ></el-input>
                              </el-form-item>
                            </div>
                            <div style="padding-right: 30px; display: contents">
                              <el-form-item
                                label="时间："
                                class="m-input-text-width"
                                prop="workingTimeFlag"
                              >
                                <el-date-picker
                                  type="monthrange"
                                  value-format="yyyy-MM"
                                  start-placeholder="开始时间"
                                  end-placeholder="结束时间"
                                  v-model="resumePracticeForm.workingDates"
                                  :disabled="resumePracticeForm.onWork == 1"
                                ></el-date-picker>
                                <el-checkbox
                                  v-model="resumePracticeForm.onWork"
                                  :true-label="1"
                                  :false-label="0"
                                  class="m-input-text-width"
                                  style="margin-left: 10px"
                                  >进行中
                                </el-checkbox>
                              </el-form-item>
                            </div>
                          </div>
                          <div>
                            <el-form-item
                              label=" 项目介绍："
                              prop="description"
                              class="m1-input-text-width"
                            >
                              <el-input
                                v-model="resumePracticeForm.description"
                                type="textarea"
                                :row="2"
                                :maxlength="500"
                                show-word-limit
                                class="intro-text"
                              ></el-input>
                            </el-form-item>
                          </div>
                          <div
                            style="
                              display: flex;
                              padding-bottom: 22px;
                              margin-left: 238px;
                              margin-top: 10px;
                            "
                          >
                            <el-button
                              class="btn1"
                              @click="handleSaveResumePractice(index)"
                              >保存
                            </el-button>
                            <el-button
                              class="btn2"
                              @click="showPracticeDialog = false"
                              >取消
                            </el-button>
                          </div>
                        </el-form>
                      </div>
                    </div>
                  </div>
                  <el-divider></el-divider>
                </div>
                <div class="resume-box" id="Resume-Language">
                  <div v-if="showLanguageDialog == false">
                    <el-row style="height: 30px">
                      <div
                        style="
                          width: 540px;
                          display: inline-block;
                          margin-right: 75px;
                        "
                      >
                        <img
                          src="../assets/point.png"
                          style="padding-right: 9px; padding-bottom: 7px"
                        />
                        <span class="resume-base">语言能力</span>
                      </div>
                      <span>
                        <svg-icon
                          icon-class="edu-add"
                          style="width: 19px; height: 19px"
                          v-on:click="handleEditResumeLanguage('create')"
                        ></svg-icon>
                      </span>
                    </el-row>
                    <div style="display: inline-block; padding-left: 15px">
                      <el-row
                        style="display: inline-flex"
                        v-for="(language, index) in item.resumeLanguageList"
                        :key="language.id"
                      >
                        <div class="resume-edu">
                          <el-row style="width: 600px; display: inline-flex">
                            <div style="width: 386px">
                              <span class="info-other"
                                >语种{{ index + 1 }}：</span
                              >
                              <span class="info-data">{{
                                language.title
                              }}</span>
                            </div>
                            <div style="width: 200px">
                              <span class="info-other">证书或分数：</span>
                              <span class="info-data">{{
                                language.description
                              }}</span>
                            </div>
                          </el-row>
                        </div>
                        <div class="resume-editbox">
                          <el-row style="height: 30px">
                            <svg-icon
                              icon-class="edit"
                              style="width: 18px; height: 19px"
                              v-on:click="
                                handleEditResumeLanguage('update', language)
                              "
                            ></svg-icon>
                          </el-row>
                          <el-row style="height: 30px">
                            <svg-icon
                              icon-class="edu-del"
                              style="width: 19px; height: 21px"
                              v-on:click="handleDelResumeLanguage(language.id)"
                            ></svg-icon>
                          </el-row>
                        </div>
                      </el-row>
                    </div>
                  </div>
                  <div v-if="showLanguageDialog == true">
                    <el-row style="height: 30px">
                      <div
                        style="
                          width: 540px;
                          display: inline-block;
                          margin-right: 75px;
                        "
                      >
                        <img
                          src="../assets/point.png"
                          style="padding-right: 9px; padding-bottom: 7px"
                        />
                        <span class="resume-base">语言能力</span>
                      </div>
                    </el-row>
                    <div class="resume-box-edit">
                      <div style="padding-top: 20px">
                        <el-form
                          ref="resumeLanguageForm"
                          :model="resumeLanguageForm"
                          :rules="resumeLanguageFormRules"
                          label-width="100px"
                        >
                          <div style="display: flex; height: 70px">
                            <div>
                              <el-form-item
                                label="语种:"
                                prop="title"
                                class="m-input-text-width"
                              >
                                <el-input
                                  v-model="resumeLanguageForm.title"
                                  placeholder="请填写语种名称"
                                  :maxlength="50"
                                  show-word-limit
                                ></el-input>
                              </el-form-item>
                            </div>
                            <div style="padding-right: 30px; margin-left: 45px">
                              <el-form-item
                                label="证书或分数："
                                prop="description"
                                class="m-input-text-width"
                              >
                                <el-input
                                  v-model="resumeLanguageForm.description"
                                  placeholder="请填写证书或分数"
                                ></el-input>
                              </el-form-item>
                            </div>
                          </div>
                          <div
                            style="
                              display: flex;
                              padding-bottom: 22px;
                              margin-left: 238px;
                              margin-top: 10px;
                            "
                          >
                            <el-button
                              class="btn1"
                              @click="handleSaveResumeLanguage(index)"
                              >保存
                            </el-button>
                            <el-button
                              class="btn2"
                              @click="showLanguageDialog = false"
                              >取消
                            </el-button>
                          </div>
                        </el-form>
                      </div>
                    </div>
                  </div>
                  <el-divider></el-divider>
                </div>
                <div class="resume-box" id="Resume-Awards">
                  <div v-if="showAwardsDialog == false">
                    <el-row style="height: 30px">
                      <div
                        style="
                          width: 540px;
                          display: inline-block;
                          margin-right: 75px;
                        "
                      >
                        <img
                          src="../assets/point.png"
                          style="padding-right: 9px; padding-bottom: 7px"
                        />
                        <span class="resume-base">证书/奖项</span>
                      </div>
                      <span>
                        <svg-icon
                          icon-class="edu-add"
                          style="width: 19px; height: 19px"
                          v-on:click="handleEditResumeAwards('create')"
                        ></svg-icon>
                      </span>
                    </el-row>
                    <div style="display: inline-block; padding-left: 15px">
                      <el-row
                        style="display: inline-flex"
                        v-for="(awards, index) in item.resumeCertificateList"
                        :key="awards.id"
                      >
                        <div class="resume-edu">
                          <el-row style="width: 600px; display: inline-flex">
                            <div style="width: 386px">
                              <span class="info-other">证书/奖项名称：</span>
                              <span class="info-data">{{ awards.title }}</span>
                            </div>
                            <div style="width: 200px">
                              <span class="info-other">获得时间：</span>
                              <span class="info-data">{{ awards.time }}</span>
                            </div>
                          </el-row>
                        </div>
                        <div class="resume-editbox">
                          <el-row style="height: 30px">
                            <svg-icon
                              icon-class="edit"
                              style="width: 18px; height: 19px"
                              v-on:click="
                                handleEditResumeAwards('update', awards)
                              "
                            ></svg-icon>
                          </el-row>
                          <el-row style="height: 30px">
                            <svg-icon
                              icon-class="edu-del"
                              style="width: 19px; height: 21px"
                              v-on:click="handleDelResumeAwards(awards.id)"
                            ></svg-icon>
                          </el-row>
                        </div>
                      </el-row>
                    </div>
                  </div>
                  <div v-if="showAwardsDialog == true">
                    <el-row style="height: 30px">
                      <div
                        style="
                          width: 540px;
                          display: inline-block;
                          margin-right: 75px;
                        "
                      >
                        <img
                          src="../assets/point.png"
                          style="padding-right: 9px; padding-bottom: 7px"
                        />
                        <span class="resume-base">证书/奖项</span>
                      </div>
                    </el-row>
                    <div class="resume-box-edit">
                      <el-form
                        ref="resumeAwardsForm"
                        :model="resumeAwardsForm"
                        :rules="resumeAwardsFormRules"
                        label-width="80px"
                      >
                        <div style="padding-top: 20px">
                          <div style="display: flex; height: 70px">
                            <div>
                              <el-form-item
                                label="证书/奖项:"
                                prop="title"
                                label-width="130px"
                                class="m-input-text-width"
                              >
                                <el-input
                                  v-model="resumeAwardsForm.title"
                                  placeholder="请填写证书/奖项名称"
                                  :maxlength="50"
                                ></el-input>
                              </el-form-item>
                            </div>
                            <div style="padding-right: 30px">
                              <el-form-item
                                label=" 获得时间："
                                prop="time"
                                label-width="130px"
                                class="m-input-text-width"
                              >
                                <el-date-picker
                                  v-model="resumeAwardsForm.time"
                                  :picker-options="oldDatePickerOptions"
                                  type="date"
                                  value-format="yyyy-MM-dd"
                                  placeholder="请选择获得时间"
                                ></el-date-picker>
                              </el-form-item>
                            </div>
                          </div>
                          <div
                            style="
                              display: flex;
                              padding-bottom: 22px;
                              margin-left: 238px;
                              margin-top: 10px;
                            "
                          >
                            <el-button
                              class="btn1"
                              @click="handleSaveResumeAwards(index)"
                              >保存
                            </el-button>
                            <el-button
                              class="btn2"
                              @click="showAwardsDialog = false"
                              >取消
                            </el-button>
                          </div>
                        </div>
                      </el-form>
                    </div>
                  </div>

                  <el-divider></el-divider>
                </div>
                <div class="resume-box" id="Resume-Skill">
                  <div v-if="showSkillDialog == false">
                    <el-row>
                      <div
                        style="
                          width: 540px;
                          display: inline-block;
                          margin-right: 75px;
                        "
                      >
                        <img
                          src="../assets/point.png"
                          style="padding-right: 9px; padding-bottom: 7px"
                        />
                        <span class="resume-base">能力标签</span>
                        <span
                          class="jobcount"
                          v-if="
                            item.resumeSkillList &&
                            item.resumeSkillList.length > 0
                          "
                          >{{ item.resumeSkillList.length }}/6</span
                        >
                        <span class="jobcount" v-else>0/6</span>
                        <span class="mark-row">
                          <svg-icon
                            icon-class="resume_qmark"
                            class="mark-icon"
                            @mouseenter="onMouseOver2"
                          ></svg-icon>
                        </span>
                        <span
                          class="mark-text"
                          v-show="seen2"
                          @mouseleave="onMouseOut2"
                          >最多可显示6个标签</span
                        >
                      </div>
                      <span>
                        <svg-icon
                          icon-class="edit"
                          style="width: 18px; height: 19px"
                          v-on:click="handleEditResumeSkill"
                        ></svg-icon>
                      </span>
                    </el-row>
                    <el-row style="margin-top: 15px">
                      <div class="resume-languageinfo">
                        <div class="resume-edu">
                          <el-tag
                            style="cursor: pointer"
                            v-for="skill in item.resumeSkillList"
                            :key="skill.id"
                            class="tag-icon"
                            >{{ skill.name }}
                          </el-tag>
                        </div>
                      </div>
                    </el-row>
                  </div>
                  <div v-if="showSkillDialog == true">
                    <el-row>
                      <div
                        style="
                          width: 540px;
                          display: inline-block;
                          margin-right: 75px;
                        "
                      >
                        <img
                          src="../assets/point.png"
                          style="padding-right: 9px; padding-bottom: 7px"
                        />
                        <span class="resume-base">能力标签</span>
                        <span class="jobcount"
                          >{{ listshowskill.length }}/6</span
                        >
                        <span class="mark-row">
                          <img
                            src="../assets/resume_qmark.png"
                            class="mark-icon"
                            @mouseenter="onMouseOver"
                          />
                        </span>
                        <span
                          class="mark-text"
                          v-show="seen"
                          @mouseleave="onMouseOut"
                          >最多可显示6个标签</span
                        >
                      </div>
                    </el-row>
                    <div class="resume-box-edit">
                      <div style="padding-top: 20px; padding-left: 40px">
                        <div class="resume-languageinfo1">
                          <div class="resume-edu">
                            <span class="tag-title">已选择：</span>
                            <el-tag
                              v-for="skill in listshowskill"
                              :key="skill.id"
                              style="
                                height: 21px;
                                border-radius: 5px;
                                cursor: pointer;
                              "
                              >{{ skill.name }}
                            </el-tag>
                          </div>
                        </div>
                        <div style="margin-top: 10px">
                          <span class="tag-title" style="padding-left: 5px"
                            >为你推荐：</span
                          >
                          <span class="tag-title-1"
                            >根据你的填写的内容智能推荐</span
                          >
                        </div>
                        <div style="margin-top: 14px; margin-left: 65px">
                          <el-tag
                            style="cursor: pointer"
                            class="skill-tag"
                            v-for="tag in skillTagListForm"
                            :key="tag.name"
                            :effect="tag.select ? 'dark' : 'plain'"
                            v-on:click="selectSkillTag(tag)"
                            >{{ tag.name }}
                          </el-tag>
                          <el-row :gutter="10">
                            <el-input
                              v-model="newSkillTag"
                              placeholder="新增标签"
                              maxlength="6"
                              class="m-input-text-width"
                              @change="handleAddNewSkillTag"
                              style="width: 200px; padding-left: 10px"
                            >
                            </el-input>
                            <svg-icon
                              icon-class="resume-add"
                              style="width: 19px; height: 18px"
                              v-on:click="handleAddNewSkillTag"
                            ></svg-icon>
                          </el-row>
                        </div>
                      </div>
                      <div style="padding-top: 20px">
                        <div
                          style="
                            display: flex;
                            padding-bottom: 22px;
                            margin-left: 238px;
                            margin-top: 10px;
                          "
                        >
                          <el-button
                            class="btn1"
                            @click="handleSaveResumeSkills"
                            :loading="posting"
                            >保存
                          </el-button>
                          <el-button
                            class="btn2"
                            @click="showSkillDialog = false"
                            >取消
                          </el-button>
                        </div>
                      </div>
                    </div>
                  </div>

                  <el-divider></el-divider>
                </div>
                <div class="resume-box" id="Resume-Introduction">
                  <div v-if="showIntroDialog == false">
                    <el-row style="height: 30px">
                      <div
                        style="
                          width: 540px;
                          display: inline-block;
                          margin-right: 75px;
                        "
                      >
                        <img
                          src="../assets/point.png"
                          style="padding-right: 9px; padding-bottom: 7px"
                        />
                        <span class="resume-base">自我介绍</span>
                      </div>
                      <span>
                        <svg-icon
                          icon-class="edit"
                          style="width: 19px; height: 19px"
                          v-on:click="handleEditResumeIntro"
                        ></svg-icon>
                      </span>
                    </el-row>
                    <el-row style="margin-bottom: 35px">
                      <div class="resume-languageinfo">
                        <div class="resume-introduction">
                          <span>{{ item.introduction }}</span>
                        </div>
                      </div>
                    </el-row>
                  </div>
                  <div v-if="showIntroDialog == true">
                    <el-row style="height: 30px">
                      <div
                        style="
                          width: 540px;
                          display: inline-block;
                          margin-right: 75px;
                        "
                      >
                        <img
                          src="../assets/point.png"
                          style="padding-right: 9px; padding-bottom: 7px"
                        />
                        <span class="resume-base">自我介绍</span>
                      </div>
                    </el-row>
                    <div class="resume-box-edit">
                      <div style="display: flex; padding: 30px 30px 30px 30px">
                        <span class="tag-title" style="width: 60px"
                          >自我介绍：</span
                        >
                        <el-form
                          ref="resumeForm1"
                          :model="resumeForm1"
                          label-width="80px"
                        >
                          <el-input
                            v-model="resumeForm1.introduction"
                            type="textarea"
                            :row="3"
                            :maxlength="300"
                            show-word-limit
                            class="intro-text"
                          ></el-input>
                        </el-form>
                      </div>

                      <div style="padding-top: 20px">
                        <div
                          style="
                            display: flex;
                            padding-bottom: 22px;
                            margin-left: 238px;
                            margin-top: 10px;
                          "
                        >
                          <el-button
                            class="btn1"
                            @click="handleSaveResumeIntro(false)"
                            >保存
                          </el-button>
                          <el-button
                            class="btn2"
                            @click="showIntroDialog = false"
                            >取消
                          </el-button>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </b-media>
            <b-col class="right-box">
              <div class="resume-preview resume-right-box">
                <el-row style="padding: 10px 10px 2px 4px">
                  <!--                                    <el-form ref="parseAttachmentForm" :model="parseAttachmentForm"-->
                  <!--                                             label-width="80px">-->
                  <!--                                        <el-upload-->
                  <!--                                                class="mt-2 upload-attach-box"-->
                  <!--                                                :limit="1"-->
                  <!--                                                :action="uploadAttachmentOptions.action"-->
                  <!--                                                :data="uploadAttachmentOptions.params"-->
                  <!--                                                :accept="uploadAttachmentOptions.acceptFileType"-->
                  <!--                                                :show-file-list="false"-->
                  <!--                                                :on-success="handleUploadParseAttachment"-->
                  <!--                                                :before-upload="beforeAttachmengUpload"-->
                  <!--                                                :file-list="fileList"-->
                  <!--                                        >-->
                  <!--                                    <el-button-->
                  <!--                                            class="left-btn"-->
                  <!--                                    >-->
                  <!--                                        <svg-icon-->
                  <!--                                                icon-class="quick-input"-->
                  <!--                                                style="width: 16px;height: 17px;margin-right: 2px"-->
                  <!--                                        ></svg-icon>快速录入-->
                  <!--                                    </el-button>-->
                  <!--                                        </el-upload>-->
                  <!--                                    </el-form>-->
                  <el-button class="left-btn" @click="handlePreview">
                    <svg-icon
                      class="svg-cls"
                      icon-class="preview"
                      style="width: 16px; height: 17px; margin-right: 2px"
                    ></svg-icon
                    >预览简历
                  </el-button>
                  <el-button @click="handleDelResume" class="left-btn"
                    ><svg-icon
                      class="svg-cls"
                      icon-class="resume-del"
                      style="width: 16px; height: 17px; margin-right: 2px"
                    ></svg-icon
                    >简历删除
                  </el-button>
                </el-row>
                <el-row style="padding: 10px">
                  <!--                                    <el-button-->
                  <!--                                            @click="exportPdf"-->
                  <!--                                            class="left-btn"-->
                  <!--                                    ><svg-icon-->
                  <!--                                            icon-class="resume-download"-->
                  <!--                                            style="width: 16px;height: 17px;margin-right: 2px"-->
                  <!--                                    ></svg-icon>简历下载-->
                  <!--                                    </el-button>-->
                </el-row>
                <el-divider></el-divider>
              </div>
              <div class="resume-attachment resume-right-box1">
                <span class="right-add">附件：</span>
                <span>
                  <svg-icon
                    class="svg-cls"
                    icon-class="tips"
                    style="width: 13px; height: 13px"
                  ></svg-icon>
                </span>
                <span
                  style="
                    font-size: 12px;
                    font-family: PingFangSC-Regular, PingFang SC;
                    font-weight: 400;
                    padding-left: 7px;
                  "
                  >附件简历内容也可被HR搜索到！</span
                >

                <div
                  style="
                    display: flex;
                    width: 265px;
                    justify-content: space-between;
                  "
                >
                  <div
                    v-if="item.attachResume && item.attachResume.length > 0"
                    style="
                      font-size: 14px;
                      font-family: PingFangSC-Medium, PingFang SC;
                      font-weight: 500;
                      display: inline-flex;
                    "
                  >
                    <svg-icon
                      icon-class="PDF"
                      style="width: 14px; height: 18px"
                    ></svg-icon>

                    {{ item.name }}个人简历
                  </div>
                  <!-- 重命名，删除按钮 -->
                  <!-- <div>
                    <svg-icon
                      icon-class="resumeOther-edit"
                      style="width: 18px; height: 18px"
                      v-on:click="handleEditAttachOthers(index)"
                    ></svg-icon>
                    <svg-icon
                      icon-class="resumeOther-del"
                      style="width: 18px; height: 20px"
                      v-on:click="handleDelAttachOthers(index)"
                    ></svg-icon>
                  </div> -->
                </div>

                <el-form
                  ref="resumeForm2"
                  :model="resumeForm2"
                  label-width="80px"
                >
                  <el-upload
                    class="mt-2 upload-attach-box"
                    :limit="1"
                    :action="uploadAttachmentOptions.action"
                    :data="uploadAttachmentOptions.params"
                    :accept="uploadAttachmentOptions.acceptFileType"
                    :show-file-list="false"
                    :on-success="handleUploadAttachmengSuccess"
                    :before-upload="beforeAttachmengUpload"
                  >
                    <el-button class="upload-btn">
                      <svg-icon
                        class="svg-cls"
                        icon-class="upload"
                        style="width: 18px; height: 15px"
                      ></svg-icon>
                      <span style="padding-left: 4px"
                        >上传附件简历</span
                      ></el-button
                    >
                  </el-upload>
                </el-form>
                <el-row style="display: inline-flex">
                  <div style="width: 210px">
                    <div v-if="showEditAttachOther == false">
                      <el-row
                        v-for="(others, index) in item.resumeMergeAttachList"
                        :key="others.id"
                      >
                        <div style="display: inline-flex">
                          <div
                            style="
                              display: inline-flex;
                              margin-bottom: 5px;
                              font-size: 14px;
                              font-family: PingFangSC-Medium, PingFang SC;
                              font-weight: 500;
                              color: #333333;
                            "
                          >
                            <div style="width: 230px">
                              <span>
                                <svg-icon
                                  icon-class="PDF"
                                  style="width: 14px; height: 18px"
                                ></svg-icon>
                              </span>
                              {{ others.name | ellipsis }}
                            </div>
                          </div>
                          <div style="display: inline-flex">
                            <el-row>
                              <svg-icon
                                icon-class="resumeOther-edit"
                                style="width: 18px; height: 18px"
                                v-on:click="handleEditAttachOthers(index)"
                              ></svg-icon>
                            </el-row>
                            <el-row>
                              <svg-icon
                                icon-class="resumeOther-del"
                                style="width: 18px; height: 20px"
                                v-on:click="
                                  (cancelAttachDialogVisible = true),
                                    (delAttachItemName = others.name),
                                    (delAttachItemIndex = index)
                                "
                              ></svg-icon>
                            </el-row>
                          </div>
                        </div>
                      </el-row>
                    </div>
                    <div v-if="showEditAttachOther == true">
                      <div style="width: 230px; display: inline-flex">
                        <span style="padding-right: 3px; line-height: 2px">
                          <svg-icon
                            icon-class="PDF"
                            style="width: 14px; height: 18px"
                          ></svg-icon>
                        </span>
                        <el-form
                          ref="resumeAttachForm1"
                          :model="resumeAttachForm1"
                          class="editTitle"
                        >
                          <el-input
                            @change="HandleEditSaveAttachOthers"
                            v-model="resumeAttachForm1.name"
                            placeholder="请输入附件名"
                          ></el-input>
                        </el-form>
                      </div>
                    </div>
                  </div>
                </el-row>
                <el-form
                  ref="resumeAttachForm"
                  :model="resumeAttachForm"
                  label-width="80px"
                >
                  <el-upload
                    class="mt-2 upload-attach-box"
                    :limit="1"
                    :action="uploadAttachmentOptions.action"
                    :data="uploadAttachmentOptions.params"
                    :show-file-list="false"
                    :on-success="handleUploadAttachOthersSuccess"
                    :before-upload="beforeAttachmengUpload"
                    :file-list="fileList"
                  >
                    <el-button class="upload-btn">
                      <svg-icon
                        class="svg-cls"
                        icon-class="upload"
                        style="width: 18px; height: 15px"
                      ></svg-icon>
                      <span style="padding-left: 4px"
                        >上传其他附件</span
                      ></el-button
                    >
                  </el-upload>
                </el-form>

                <el-divider></el-divider>
              </div>
              <div class="resume-preview resume-right-box1">
                <span class="right-add">简历完成度：</span>
                <span class="right-num"
                  >{{ item.resumeCompleteProgress }}%</span
                >
                <el-progress
                  :stroke-width="22"
                  :percentage="item.resumeCompleteProgress"
                  :status="
                    item.resumeCompleteProgress == 100 ? 'success' : 'warning'
                  "
                ></el-progress>
              </div>
            </b-col>
          </b-row>
        </el-tab-pane>
      </el-tabs>
      <el-dialog :visible.sync="showResumeDialog" :width="customWidth">
        <ResumeView :resumeDetail="this.resumeDetail"></ResumeView>
      </el-dialog>
      <el-dialog
        :visible.sync="photoEditDialog"
        title="图片剪裁"
        width="50%"
      ></el-dialog>
      <!-- 是否删除附件 -->
      <el-dialog
        class="cancel-dialog"
        :visible.sync="cancelAttachDialogVisible"
        width="30%"
      >
        <div class="text-center">
          <div style="font-size: 46px">
            <i class="el-icon-warning" style="color: #ff1744"></i>
          </div>
          <div class="text">您确定要删除“{{ delAttachItemName }}”吗</div>
        </div>
        <div class="footer text-center">
          <el-button
            type="primary"
            @click="
              (cancelAttachDialogVisible = false),
                handleDelAttachOthers(delAttachItemIndex)
            "
            >确 定</el-button
          >
          <el-button
            @click="cancelAttachDialogVisible = false"
            style="color: #4895ef"
            >取 消</el-button
          >
        </div>
      </el-dialog>
      <!--            <el-dialog :visible.sync="photoDialog"-->
      <!--                       width="50%" >-->
      <!--                <div style="margin-left: 105px">-->
      <!--            <el-form ref="resumeForm3" :model="resumeForm3">-->
      <!--                <el-upload-->
      <!--                        class="avatar-uploader"-->
      <!--                        drag-->
      <!--                        :action="uploadPicOptions.action"-->
      <!--                        :data="uploadPicOptions.params"-->
      <!--                        :accept="uploadPicOptions.acceptFileType"-->
      <!--                        :before-upload="beforeAvatarUpload"-->
      <!--                        multiple>-->
      <!--                    <div>-->
      <!--                    <div style="padding-top: 60px;padding-bottom: 30px;">-->
      <!--                    <span class="photoText">拖动头像到这里</span>-->
      <!--                    </div>-->
      <!--                    <div style="width: 250px;margin-left: 76px;">-->
      <!--                    <span class="photoText-size">建议使用JPG文件,文件大小不超过8MB,建议尺寸500×500</span>-->
      <!--                    </div>-->
      <!--                    </div>-->
      <!--                </el-upload>-->
      <!--            </el-form>-->
      <!--                    <el-row style="padding-left: 106px;padding-bottom: 10px;padding-top: 5px;">-->
      <!--                        <el-button class="img-button" @click="handleAvatarSuccess">上传照片</el-button>-->
      <!--                    </el-row>-->
      <!--                </div>-->
      <!--            </el-dialog>-->
    </b-container>
  </div>
</template>

<script>
import VueSticky from "vue-sticky";
import EditResumeTitle from "@/components/EditResumeTitle";
import { library } from "@fortawesome/fontawesome-svg-core";
import { getAllCountries, getCurrentCountry } from "@/api/country_api";
import {
  getResumeInfo,
  parseAttachment,
  getResumeDetail,
  saveResumeBasic,
  saveResumeEdu,
  saveResumeOther,
  delResumeEdu,
  saveResumeExp,
  delResumeExp,
  saveResumePractice,
  delResumePractice,
  saveResumeSkills,
  saveResumeLink,
  delResumeLink,
  delResumeAttachment,
  saveResumeLanguage,
  delResumeLanguage,
  saveResumeAwards,
  delResumeAwards,
  delResume,
} from "@/api/resume_api";
import { searchSchool } from "@/api/school_api";
import { listByType, listByTypeSalary } from "@/api/dict_api";
import { serachByName } from "@/api/company_api";
import { getCityByName } from "@/api/city_api";
import { getCategoryTree } from "@/api/category_api";
import { saveUserExpectJob } from "@/api/user_api";
import { getUploadPicToken, getUploadAttachmentToken } from "@/api/upload_api";
import "quill/dist/quill.core.css";
import "quill/dist/quill.snow.css";
import "quill/dist/quill.bubble.css";
import { quillEditor } from "vue-quill-editor";
import Toast from "@/utils/toast";
import { checkPicSize, checkAttachmentSize } from "@/utils/common";
import { exportResumeToPdf } from "@/api/export_api";
import {
  faEdit,
  faFemale,
  faMale,
  faMobileAlt,
  faEnvelope,
  faPlusSquare,
  faPencilAlt,
  faTrashAlt,
} from "@fortawesome/free-solid-svg-icons";
import { downloadFile } from "@/utils/common";
import ResumeView from "@/components/ResumeView";
import { addResume } from "../api/resume_api";
import UploadImg from "@/components/Cropper/uploadImg";

library.add(
  faEdit,
  faFemale,
  faMale,
  faMobileAlt,
  faEnvelope,
  faPlusSquare,
  faPencilAlt,
  faTrashAlt
);

export default {
  name: "EditResumePage",
  data() {
    return {
      cancelAttachDialogVisible: false, //是否删除附件对话框
      delAttachItemName: "", // 将要被删除的附件名字
      delAttachItemIndex: undefined, // 将要被删除的附件INDEX
      cityOptions: [],
      // 城市选择框属性
      cityIdProps: {
        multiple: false,
        lazy: false,
        emitPath: false,
        checkStrictly: false,
        expandTrigger: "hover",
        value: "name",
        label: "name",
        children: "children",
      },
      customWidth: "800px",
      salaryCheck: false,
      schoolCheck: false,
      resumeIndex: undefined,
      showQequired: false,
      requiredCount: 0,
      companyIndustryOptions: [],
      nullResumeId: undefined,
      editIndex: undefined,
      workType: undefined,
      showResumeDialog: false,
      photoEditDialog: false,
      photoDialog: false,
      showPriority: true,
      showEditTitle: false,
      shownew: false,
      resumeTitleForm: {
        id: undefined,
        title: "新简历",
      },
      showEditAttachOther: false,
      i: 0,
      getResume: false,
      editableTabsValue: undefined,
      tabIndex: 0,
      seen: false,
      seen2: false,
      maritalStatusList: [
        {
          value: "0",
          id: 1,
          name: "中共党员（含预备党员）",
        },
        {
          id: 2,
          value: "1",
          name: "民主党派",
        },
        {
          id: 3,
          value: "2",
          name: "无党派人士",
        },
        {
          id: 4,
          value: "3",
          name: "团员",
        },
        {
          id: 3,
          value: "4",
          name: "群众",
        },
      ],
      priorityList: [
        {
          id: 1,
          value: "0",
          name: "第一",
        },
        {
          id: 2,
          value: "1",
          name: "第二",
        },
        {
          id: 3,
          value: "2",
          name: "第三",
        },
        {
          id: 4,
          value: "3",
          name: "第四",
        },
      ],
      categoryList: {},
      cityList: {},
      resumeForm1: {
        id: undefined,
        resumeId: undefined,
        introduction: undefined,
        title: undefined,
      },
      parseAttachmentForm: {
        name: undefined,
        asAttachment: false,
      },
      resumeForm2: {
        id: undefined,
        resumeId: undefined,
        attachResume: undefined,
      },
      resumeForm3: {
        id: undefined,
        resumeId: undefined,
        avatar: undefined,
      },
      resumeForm5: {
        id: undefined,
        resumeId: undefined,
        priority: undefined,
      },
      resumeForm: {
        priority: undefined,
        id: undefined,
        name: undefined,
        birth: undefined,
        gender: undefined,
        avatar: undefined,
        maritalStatus: undefined,
        returnTime: undefined,
        graduateTime: undefined,
        curPlace: undefined,
        phoneCode: undefined,
        phone: undefined,
        introduction: undefined,
        attachResume: undefined,
        email: undefined,
      },
      resumeFormRules: {
        name: [{ required: true, message: "请输入姓名", trigger: "blur" }],
        email: [{ required: true, message: "请输入新邮箱", trigger: "blur" }],
        birth: [{ required: true, message: "请选择生日", trigger: "change" }],
        gender: [{ required: true, message: "请选择性别", trigger: "blur" }],
      },
      resumeEduForm: {
        workType: undefined,
        faceToke: undefined,
        inSchool: undefined,
        id: undefined,
        resumeId: undefined,
        schoolName: undefined,
        majorName: undefined,
        degreeId: undefined,
        startTime: undefined,
        finishTime: undefined,
        workingDates: undefined,
        gpa: undefined,
        showEditFlag: 0,
      },
      resumeEduFormRules: {
        schoolName: [
          { required: true, message: "请输入学校名称", trigger: "blur" },
        ],
        majorName: [
          { required: true, message: "请输入所学专业", trigger: "blur" },
        ],
        degreeId: [
          { required: true, message: "请选择学历", trigger: "change" },
        ],
        workingDates: [
          { required: true, message: "请选择在校时间", trigger: "change" },
        ],
      },
      resumeExpForm: {
        id: undefined,
        resumeId: undefined,
        workingTimeFlag: 1,
        workType: undefined,
        workingDates: undefined,
        startTime: undefined,
        finishTime: undefined,
        company: undefined,
        depart: undefined,
        post: undefined,
        description: undefined,
        onWork: 0,
        industry: undefined,
      },
      showdepart: false,
      resumeExpFormRules: {
        company: [
          { required: true, message: "请输入公司名称", trigger: "blur" },
          { max: 30, message: "长度在不超过30字符", trigger: "blur" },
        ],
        workType: [
          { required: true, message: "请选择工作类型", trigger: "blur" },
        ],
        depart: [{ required: true, message: "请输入部门", trigger: "blur" }],
        post: [{ required: true, message: "请输入职位", trigger: "blur" }],
        workingTimeFlag: [
          {
            required: true,
            message: "请输入在职时间",
            trigger: "change",
          },
        ],
        description: [
          { required: true, message: "请输入工作内容", trigger: "blur" },
        ],
      },
      resumePracticeForm: {
        id: undefined,
        resumeId: undefined,
        workingDates: undefined,
        startTime: undefined,
        finishTime: undefined,
        title: undefined,
        post: undefined,
        description: undefined,
        workingTimeFlag: 1,
        onWork: 0,
      },
      resumePracticeFormRules: {
        title: [{ required: true, message: "请输入标题", trigger: "blur" }],
        workingTimeFlag: [
          { required: true, message: "请输入项目时间", trigger: "change" },
        ],
        description: [
          { required: true, message: "请输入项目内容", trigger: "blur" },
        ],
      },
      resumeAttachForm: {
        id: undefined,
        resumeId: undefined,
        attachOthers: [],
      },
      resumeAttachForm1: {
        link: undefined,
        name: "附件",
      },
      newResumeForm: {
        id: undefined,
        title: "新简历",
        priority: undefined,
      },
      resumeAttachForm2: {
        link: undefined,
        name: undefined,
      },
      resumeLanguageForm: {
        id: undefined,
        resumeId: undefined,
        title: undefined,
        description: undefined,
      },
      resumeLanguageFormRules: {
        title: [{ required: true, message: "请输入语种名称", trigger: "blur" }],
        description: [
          { required: true, message: "请输入证书或分数", trigger: "blur" },
        ],
      },
      resumeAwardsForm: {
        id: undefined,
        resumeId: undefined,
        title: undefined,
        time: undefined,
      },
      resumeAwardsFormRules: {
        title: [
          { required: true, message: "请输入证书/奖项名称", trigger: "blur" },
        ],
        time: [{ required: true, message: "请输入获得时间", trigger: "blur" }],
      },
      expectJobForm: {
        resumeId: undefined,
        expectWorkType: "",
        expectPosition: undefined,
        expectCity: undefined,
        salaryId: undefined,
        categoryId: 122,
        industry: undefined,
      },
      expectJobFormRules: {
        expectPosition: [
          { required: true, message: "请输入意向职位", trigger: "blur" },
        ],
        expectWorkType: [
          { required: true, message: "请选择工作类型", trigger: "blur" },
        ],
        expectCity: [
          { required: true, message: "请输入意向城市", trigger: "blur" },
        ],
      },
      uploadPicOptions: {
        action: "",
        params: {},
        fileUrl: "",
        acceptFileType: ".jpg,.jpeg,.png,.JPG,.JPEG,.PNG",
      },
      fileList: [],
      uploadAttachmentOptions: {
        action: "",
        params: {},
        fileUrl: "",
        acceptFileType: ".pdf,.doc,docx",
      },
      resume: {},
      newFlag: false,
      newResumeId: undefined,
      newIndex: undefined,
      resumeId: undefined,
      resumeDetail: undefined,
      countryOptions: [],
      degreeOptions: [],
      degreeList: [],
      jobCategoryOptions: [],
      skillTagOptions: [],
      salaryOptions: [],
      categoryListOptions: [],
      // categoryList
      industryNameOptions: [],
      showBasicDialog: false,
      showIntroDialog: false,
      showEduDialog: false,
      showExpDialog: false,
      showPracticeDialog: false,
      showExpectJobDialog: false,
      showUploadAvatarIcon: false,
      showSkillDialog: false,
      showLinkDialog: false,
      showPreviewDialog: false,
      showLanguageDialog: false,
      showAwardsDialog: false,
      posting: false,
      introEditorOption: {
        theme: "snow",
        placeholder: "",
        modules: {
          toolbar: [["bold"], [{ list: "ordered" }, { list: "bullet" }]],
        },
      },
      expEditorOption: {
        theme: "bubble",
        placeholder: "关于你在这个岗位的职责，工作内容和成就。",
        modules: {
          toolbar: [["bold"], [{ list: "ordered" }, { list: "bullet" }]],
        },
      },
      practiceEditorOption: {
        theme: "bubble",
        placeholder: "关于你在项目中的所获。",
        modules: {
          toolbar: [["bold"], [{ list: "ordered" }, { list: "bullet" }]],
        },
      },
      jobCatetoryProps: {
        multiple: false,
        expandTrigger: "hover",
        value: "id",
        label: "name",
        emitPath: false,
        children: "children",
        checkStrictly: true,
      },
      jobCatetoryProps1: {
        expandTrigger: "hover",
        value: "name",
        label: "name",
        emitPath: false,
        children: "children",
        checkStrictly: true,
      },
      workTypeOptions: [
        { id: 1, value: "全职", name: "全职" },
        { id: 2, value: "兼职", name: "兼职" },
        { id: 3, value: "实习", name: "实习" },
      ],
      newSkillTag: "",
      skillTagListForm: [],
      listskill: [],
      listshowskill: [],
      oldDatePickerOptions: {
        disabledDate(time) {
          return time.getTime() >= Date.now() - 8.64e7;
        },
      },
      salaryNameList: "",
    };
  },
  mounted() {
    window.onresize = () => {
      return () => {
        this.setDialogWidth();
      };
    };
  },
  watch: {
    "expectJobForm.cityIds": function (newVal, oldVal) {
      if (newVal.length > 3) {
        this.expectJobForm.cityIds = oldVal;
        Toast.error("意向城市不能超过3个");
      }
    },
    "resumeEduForm.gpa": function (newVal, oldVal) {
      if (newVal < 0) {
        this.resumeEduForm.gpa = 0;
      }
    },
  },
  directives: {
    sticky: VueSticky,
  },
  components: {
    EditResumeTitle,
    quillEditor,
    ResumeView,
    UploadImg,
  },
  created() {
    this.initData();
  },
  methods: {
    setDialogWidth() {
      var val = document.body.clientWidth;
      const def = 450; // 默认宽度
      if (val < def) {
        this.dialogWidth = "100%";
      } else {
        this.dialogWidth = def + "px";
      }
    },
    saveResumeId(tab, event) {
      this.newResumeId = tab.name;
      this.newIndex = tab.index;
      console.log(tab.index);
      console.log(this.newResumeId);
      console.log(this.editableTabsValue);
      console.log(this.resume[0].id);
      this.handleEditPriority();
    },
    handleTabsEdit(targetName, action) {
      if (action === "add") {
        if (this.resume.length < 4) {
          this.addResume();
          let newTabName = this.resume.length + 1;
          console.log(newTabName);
          console.log(this.resume[this.resume.length]);
        } else {
          this.$message({
            message: "最多可编辑4份在线简历",
            type: "warning",
          });
        }
      }
    },
    onMouseOver() {
      this.seen = true;
    },
    onMouseOver2() {
      this.seen2 = true;
    },
    onMouseOut() {
      this.seen = false;
    },
    onMouseOut2() {
      this.seen2 = false;
    },
    initData() {
      getAllCountries().then((response) => {
        this.countryOptions = response.data;
      });

      getCurrentCountry().then((response) => {
        const { data } = response;
        if (data && data.phoneCode && this.resumeForm.phoneCode === undefined) {
          this.resumeForm.phoneCode = data.phoneCode;
        }
      });
      listByType(25).then((response) => {
        this.degreeOptions = response.data.list;
        console.log(this.degreeOptions);
        for (const item of this.degreeOptions) {
          if (
            item.name != "不限" &&
            item.name != "专科及以下" &&
            item.name != "MBA" &&
            item.name != "EMBA"
          ) {
            this.degreeList.push(item);
          }
        }
        console.log(this.degreeList);
      });
      listByType(3).then(
        (response) => (this.skillTagOptions = response.data.list)
      );
      getCategoryTree().then(
        (response) => (this.jobCategoryOptions = response.data)
      );
      this.getResumeInfo();
      listByType(6).then(
        (response) => (this.companyIndustryOptions = response.data.list)
      );
      listByTypeSalary(26).then((response) => {
        for (const item of response.data.list) {
          if (item.name != "面议" && item.name != "5K以下") {
            this.salaryOptions.push(item);
          }
        }
      });
    },
    beforeAvatarUpload(file) {
      return new Promise((resolve, reject) => {
        if (checkPicSize(file)) {
          reject();
        } else {
          getUploadPicToken(file.name)
            .then((response) => {
              const { data } = response;
              this.uploadPicOptions.action = data.host;
              this.uploadPicOptions.params = data;
              this.uploadPicOptions.fileUrl = data.host + "/" + data.key;
              resolve(data);
            })
            .catch((error) => {
              reject(error);
            });
        }
      });
    },
    onUpload(url) {
      console.log(url);
      this.resumeForm3.avatar = url;
      this.handleSaveResumeAvatar(false);
    },
    handleAvatarSuccess() {
      this.resumeForm3.avatar = this.uploadPicOptions.fileUrl;
      this.resume.avatar = this.uploadPicOptions.fileUrl;
      console.log(this.resumeForm3.avatar);
      this.handleSaveResumeAvatar(false);
      this.photoDialog = false;
    },
    beforeAttachmengUpload(file) {
      return new Promise((resolve, reject) => {
        if (checkAttachmentSize(file)) {
          reject();
        } else {
          getUploadAttachmentToken(file.name)
            .then((response) => {
              const { data } = response;
              this.uploadAttachmentOptions.action = data.host;
              this.uploadAttachmentOptions.params = data;
              this.uploadAttachmentOptions.fileUrl = data.host + "/" + data.key;
              resolve(data);
            })
            .catch((error) => {
              reject(error);
            });
        }
      });
    },
    handleUploadParseAttachment() {
      this.parseAttachmentForm.name = this.uploadAttachmentOptions.fileUrl;
      console.log(this.parseAttachmentForm.name);
      this.handleSaveParseAttachment(false);
      this.fileList = [];
    },
    handleUploadAttachmengSuccess() {
      this.resumeForm2.attachResume = this.uploadAttachmentOptions.fileUrl;
      this.handleSaveResumeAttachResume(false);
    },
    handleUploadAttachOthersSuccess() {
      let nowAttachOthers = [];
      let otherAttach;
      let i = 0;
      let n = 0;
      this.resumeAttachForm1.link = this.uploadAttachmentOptions.fileUrl;
      if (this.newFlag == true) {
        n = this.resume.length - 1;
      } else if (this.newIndex && this.newIndex != "") {
        n = this.newIndex;
      } else {
        n = 0;
      }
      if (
        this.resume[n].resumeMergeAttachList &&
        this.resume[n].resumeMergeAttachList.length > 0
      ) {
        for (i; i < this.resume[n].resumeMergeAttachList.length; i++) {
          otherAttach = this.resume[n].resumeMergeAttachList[i];
          console.log(otherAttach);
          nowAttachOthers.push(otherAttach);
        }
        console.log(nowAttachOthers);
        let result = nowAttachOthers.map((value, index) => {
          return { link: value.resumeAttach, name: value.name };
        });
        result.push(this.resumeAttachForm1);
        this.resumeAttachForm.attachOthers = result;
        this.handleSaveAttachOthersResume(false);
      } else {
        console.log("0000");
        this.resumeAttachForm.attachOthers.push(this.resumeAttachForm1);
        this.handleSaveAttachOthersResume(false);
      }
      this.fileList = [];
    },
    handleEditAttachOthers(index) {
      this.showEditAttachOther = true;
      this.editIndex = index;
      console.log(index);
      let i;
      if (this.newFlag == true) {
        i = this.resume.length - 1;
      } else if (this.newIndex && this.newIndex != "") {
        i = this.newIndex;
      } else {
        i = 0;
      }
      this.resumeAttachForm1.name = this.resume[i].resumeMergeAttachList[
        index
      ].name;
      this.resumeAttachForm1.link = this.resume[i].resumeMergeAttachList[
        index
      ].resumeAttach;
      console.log(this.resumeAttachForm1);
      console.log(this.resume[i].resumeMergeAttachList[index]);
      console.log(this.resume[i].resumeMergeAttachList[index].name);
    },
    HandleEditSaveAttachOthers() {
      console.log(this.resumeAttachForm1);
      console.log(this.editIndex);
      let index = this.editIndex;
      let otherAttach;
      let nowAttachOthers = [];
      let i = 0;
      let n;
      if (this.newFlag == true) {
        n = this.resume.length - 1;
      } else if (this.newIndex && this.newIndex != "") {
        n = this.newIndex;
      } else {
        n = 0;
      }
      console.log("2222");
      for (i; i < this.resume[n].resumeMergeAttachList.length; i++) {
        otherAttach = this.resume[n].resumeMergeAttachList[i];
        nowAttachOthers.push(otherAttach);
      }
      let result = nowAttachOthers.map((value, index) => {
        return { link: value.resumeAttach, name: value.name };
      });
      this.$set(result, index, this.resumeAttachForm1);
      console.log(result);
      this.resumeAttachForm.attachOthers = result;
      this.handleSaveAttachOthersResume(false);
      this.showEditAttachOther = false;
    },
    handleDelAttachOthers(index) {
      // this.list.splice(index, 1);
      console.log(this.resumeAttachForm1);
      console.log(index);
      let otherAttach;
      let nowAttachOthers = [];
      let i = 0;
      let n = 0;
      if (this.newFlag == true) {
        n = this.resume.length - 1;
      } else if (this.newIndex && this.newIndex != "") {
        n = this.newIndex;
      } else {
        n = 0;
      }
      console.log("3333");
      if (this.resume[n].resumeMergeAttachList.length <= 1) {
        this.resumeAttachForm.attachOthers = nowAttachOthers;
        this.handleSaveAttachOthersResume(false);
        this.showEditAttachOther = false;
      } else {
        console.log("444");
        for (i; i < this.resume[n].resumeMergeAttachList.length; i++) {
          otherAttach = this.resume[n].resumeMergeAttachList[i];
          nowAttachOthers.push(otherAttach);
        }
        let result = nowAttachOthers.map((value, index) => {
          return { link: value.resumeAttach, name: value.name };
        });
        this.$delete(result, index);
        console.log(result);
        this.resumeAttachForm.attachOthers = result;
        this.handleSaveAttachOthersResume(false);
        this.showEditAttachOther = false;
      }
    },
    getResumeInfo() {
      getResumeInfo().then((response) => {
        this.resume = response.data;
        this.nullResumeId = this.resume[this.resume.length - 1].id;
        this.getResume = true;
        let newTabIndex;
        let i = this.resume.length;
        for (i; i > 0; i--) {
          if (this.resume[i - 1].title && this.resume[i - 1].title != "") {
          } else {
            this.newResumeForm.title = "简历" + i;
            this.newResumeForm.id = this.resume[i - 1].id;
            this.newResumeForm.priority = 5;
            saveResumeBasic(this.newResumeForm).then(() => {
              this.getResumeInfo();
            });
          }
        }
        if (this.newIndex && this.newIndex != "") {
          newTabIndex = this.newIndex;
          if (this.resume[newTabIndex] && this.resume[newTabIndex] != "") {
            this.editableTabsValue = this.resume[newTabIndex].id;
            this.resumeIndex = this.newIndex;
          } else if (this.newFlag == true) {
            this.editableTabsValue = this.resume[this.resume.length - 1].id;
            this.resumeIndex = this.resume.length - 1;
          } else {
            this.editableTabsValue = this.resume[0].id;
            this.resumeIndex = 0;
          }
        } else if (this.newFlag == true) {
          this.editableTabsValue = this.resume[this.resume.length - 1].id;
          this.resumeIndex = this.resume.length - 1;
        } else {
          this.editableTabsValue = this.resume[0].id;
          this.resumeIndex = this.resume.length - 1;
        }

        this.setSalary();
      });
    },
    addResume() {
      addResume().then(() => {
        this.newFlag = true;
        getResumeInfo().then((response) => {
          this.resume = response.data;
          this.nullResumeId = this.resume[this.resume.length - 1].id;
          this.getResume = true;
          let newTabIndex;
          let i = this.resume.length;
          for (i; i > 0; i--) {
            if (this.resume[i - 1].title && this.resume[i - 1].title != "") {
            } else {
              this.newResumeForm.title = "简历" + i;
              this.newResumeForm.id = this.resume[i - 1].id;
              this.newResumeForm.priority = 5;
              saveResumeBasic(this.newResumeForm).then(() => {
                this.getResumeInfoAdd();
              });
            }
          }
          if (this.newIndex && this.newIndex != "") {
            newTabIndex = this.newIndex;
            if (this.resume[newTabIndex] && this.resume[newTabIndex] != "") {
              this.editableTabsValue = this.resume[newTabIndex].id;
              this.resumeIndex = this.newIndex;
            } else {
              this.editableTabsValue = this.resume[this.resume.length - 1].id;
              this.resumeIndex = this.resume.length - 1;
            }
          } else {
            this.editableTabsValue = this.resume[this.resume.length - 1].id;
            this.resumeIndex = this.resume.length - 1;
          }
          this.setSalary();
        });
      });
    },
    setSalary() {
      let i;
      if (this.newFlag == true) {
        i = this.resume.length - 1;
      } else if (this.newIndex && this.newIndex != "") {
        i = this.newIndex;
      } else {
        i = 0;
      }
      //初始化预期薪资
      //获取salaryOptions
      this.$axios
        .request({
          url: "/dict/list",
          method: "get",
          params: { type: 26, sort: "+id" },
        })
        .then((response) => {
          this.salaryOptions = response.data.list;
        });

      //获取salaryId -> salaryList
      let tempSalaryList = this.resume[i].userExpectJob.salaryId;
      let salaryNameArr = [];
      tempSalaryList.forEach((item) => {
        this.salaryOptions.forEach((option) => {
          if (option.id == item) {
            salaryNameArr.push(option.name);
          }
        });
      });
      this.salaryNameList = salaryNameArr.toString();
    },
    getResumeInfoAdd() {
      getResumeInfo().then((response) => {
        this.resume = response.data;
        this.nullResumeId = this.resume[this.resume.length - 1].id;
        this.getResume = true;
        let newTabIndex;
        let i = this.resume.length;
        if (this.newFlag == true) {
          this.editableTabsValue = this.resume[this.resume.length - 1].id;
          this.resumeIndex = this.resume.length - 1;
        } else if (this.newIndex && this.newIndex != "") {
          newTabIndex = this.newIndex;
          if (this.newFlag == true) {
            this.editableTabsValue = this.resume[this.resume.length - 1].id;
            this.resumeIndex = this.resume.length - 1;
          } else if (
            this.resume[newTabIndex] &&
            this.resume[newTabIndex] != ""
          ) {
            this.editableTabsValue = this.resume[newTabIndex].id;
            this.resumeIndex = this.newIndex;
          } else {
            this.editableTabsValue = this.resume[0].id;
            this.resumeIndex = 0;
          }
        } else {
          this.editableTabsValue = this.resume[this.resume.length - 1].id;
          this.resumeIndex = this.resume.length - 1;
        }
      });
    },
    setResumeFormValues() {
      this.resumeForm.id = this.newResumeId;
      this.resumeForm.name = this.resume.name;
      this.resumeForm.avatar = this.resume.avatar;
      this.resumeForm.birth = this.resume.birth;
      this.resumeForm.gender = this.resume.gender;
      this.resumeForm.email = this.resume.email;
      this.resumeForm.returnTime = this.resume.returnTime;
      this.resumeForm.graduateTime = this.resume.graduateTime;
      this.resumeForm.curPlace = this.resume.curPlace;
      this.resumeForm.phone = this.resume.phone;
      this.resumeForm.priority = this.resume.priority;
      this.resumeForm.maritalStatus = this.resume.maritalStatus;
      this.resumeForm.introduction = this.resume.introduction;
    },
    editTitle() {
      this.showEditTitle = true;
      if (this.newIndex && this.newIndex) {
        this.resumeTitleForm.title = this.resume[this.newIndex].title;
      } else {
        this.resumeTitleForm.title = this.resume[this.resume.length - 1].title;
      }
    },
    saveEditTitle() {
      this.$refs["resumeTitleForm"][0].validate((valid) => {
        if (valid) {
          let i;
          if (this.newFlag == true) {
            i = this.resume.length - 1;
          } else if (this.newIndex && this.newIndex != "") {
            i = this.newIndex;
          } else {
            i = 0;
          }
          this.resumeTitleForm.id = this.resume[i].id;
          saveResumeBasic(this.resumeTitleForm)
            .then(() => {
              this.getResumeInfo();
              this.showEditTitle = false;
            })
            .finally(() => {
              this.posting = false;
            });
        }
      });
    },
    requiredEdit(index) {
      console.log("----------", this.resume[index]);
      if (
        !this.resume[index].name ||
        this.resume[index].name === "" ||
        !this.resume[index].birth ||
        this.resume[index].birth === "" ||
        !this.resume[index].gender ||
        String(this.resume[index].gender) === "" ||
        !this.resume[index].email ||
        this.resume[index].email === ""
      ) {
        this.handleEditResumeBasic();
      }

      if (this.resume[index].priority == "" || !this.resume[index].priority) {
        this.handleEditPriority();
      }

      if (
        this.resume[index].resumeEduList == "" ||
        !this.resume[index].resumeEduList
      ) {
        this.handleEditResumeEdu();
      }

      if (
        !this.resume[index].userExpectJob.expectPosition ||
        this.resume[index].userExpectJob.expectPosition == "" ||
        !this.resume[index].userExpectJob.expectCity ||
        this.resume[index].userExpectJob.expectCity == "" ||
        !this.resume[index].userExpectJob.expectWorkType ||
        this.resume[index].userExpectJob.expectWorkType == ""
      ) {
        this.handleEditExpectJob();
      }
    },
    handleEditResumeBasic() {
      console.log("-----");
      this.showBasicDialog = true;
      let i;
      if (this.newFlag == true) {
        i = this.resume.length - 1;
      } else if (this.newIndex && this.newIndex) {
        i = this.newIndex;
      } else {
        i = 0;
      }
      this.resumeForm.id = this.resume[i].id;
      this.resumeForm.name = this.resume[i].name;
      this.resumeForm.avatar = this.resume[i].avatar;
      this.resumeForm.birth = this.resume[i].birth;
      if (this.resume[i].gender == 0) {
        this.resumeForm.gender = undefined;
      } else {
        this.resumeForm.gender = this.resume[i].gender;
        console.log(this.resume[i].gender);
      }
      this.resumeForm.email = this.resume[i].email;
      this.resumeForm.returnTime = this.resume[i].returnTime;
      this.resumeForm.graduateTime = this.resume[i].graduateTime;
      this.resumeForm.curPlace = this.resume[i].curPlace;
      this.resumeForm.phone = this.resume[i].phone;
      if (this.resume[i].maritalStatus == 0) {
        this.resumeForm.maritalStatus = "中共党员（含预备党员）";
      } else if (this.resume[i].maritalStatus == 1) {
        this.resumeForm.maritalStatus = "民主党派";
      } else if (this.resume[i].maritalStatus == 2) {
        this.resumeForm.maritalStatus = "无党派人士";
      } else if (this.resume[i].maritalStatus == 3) {
        this.resumeForm.maritalStatus = "团员";
      } else if (this.resume[i].maritalStatus == 4) {
        this.resumeForm.maritalStatus = "群众";
      }
      this.resumeForm.introduction = this.resume[i].introduction;
      this.$nextTick(() => {
        this.$refs["resumeForm"][0].clearValidate();
      });
    },
    handleEditResumeIntro() {
      this.showIntroDialog = true;
      let i;
      if (this.newFlag == true) {
        i = this.resume.length - 1;
      } else if (this.newIndex && this.newIndex != "") {
        i = this.newIndex;
      } else {
        i = 0;
      }
      this.resumeForm1.resumeId = this.resume[i].id;
      this.resumeForm1.introduction = this.resume[i].introduction;
      // this.setResumeFormValues();
      this.$nextTick(() => {
        this.$refs["resumeForm1"][0].clearValidate();
      });
    },

    handleSaveResumeAvatar() {
      this.$refs["resumeForm3"][0].validate((valid) => {
        if (valid) {
          let i;
          if (this.newFlag == true) {
            i = this.resume.length - 1;
          } else if (this.newIndex && this.newIndex != "") {
            i = this.newIndex;
          } else {
            i = 0;
          }
          this.resumeForm3.id = this.resume[i].id;
          saveResumeBasic(this.resumeForm3)
            .then(() => {
              this.getResumeInfo();
            })
            .finally(() => {
              this.posting = false;
            });
        }
      });
    },
    handleEditResumeEdu(type, resumeEdu) {
      this.showEduDialog = true;
      let i;
      if (this.newFlag == true) {
        i = this.resume.length - 1;
      } else if (this.newIndex && this.newIndex != "") {
        i = this.newIndex;
      } else {
        i = 0;
      }
      this.resumeEduForm.resumeId = this.resume[i].id;
      if (type === "update") {
        this.resumeEduForm.id = resumeEdu.id;
        this.resumeEduForm.schoolName = resumeEdu.schoolName;
        this.resumeEduForm.majorName = resumeEdu.majorName;
        this.resumeEduForm.degreeId = resumeEdu.degree.id;
        this.resumeEduForm.startTime = resumeEdu.startTime;
        this.resumeEduForm.finishTime = resumeEdu.finishTime;
        if (this.resumeEduForm.startTime == this.resumeEduForm.finishTime) {
          this.schoolCheck == true;
        }
        this.resumeEduForm.gpa = resumeEdu.gpa;
        if (this.schoolCheck == true) {
          this.resumeEduForm.workingDates = [
            this.resume[i].updateTime,
            this.resume[i].updateTime,
          ];
        } else {
          this.resumeEduForm.workingDates = [
            this.resumeEduForm.startTime,
            this.resumeEduForm.finishTime,
          ];
        }
        console.log(this.resumeEduForm.workingDates);
      } else {
        this.schoolCheck = false;
        this.resumeEduForm.id = undefined;
        this.resumeEduForm.schoolName = undefined;
        this.resumeEduForm.majorName = undefined;
        this.resumeEduForm.degreeId = undefined;
        this.resumeEduForm.workingDates = undefined;
        this.resumeEduForm.startTime = undefined;
        this.resumeEduForm.finishTime = undefined;
        this.resumeEduForm.gpa = undefined;
      }
      this.$nextTick(() => {
        this.$refs["resumeEduForm"][0].clearValidate();
      });
    },
    handleEditResumeLanguage(type, resumeLanguage) {
      this.showLanguageDialog = true;
      // console.log(resumeLanguage.description)
      let i;
      if (this.newFlag == true) {
        i = this.resume.length - 1;
      } else if (this.newIndex && this.newIndex != "") {
        i = this.newIndex;
      } else {
        i = 0;
      }
      this.resumeLanguageForm.resumeId = this.resume[i].id;
      if (type === "update") {
        this.resumeLanguageForm.id = resumeLanguage.id;
        this.resumeLanguageForm.title = resumeLanguage.title;
        this.resumeLanguageForm.description = resumeLanguage.description;
      } else {
        this.resumeLanguageForm.id = undefined;
        this.resumeLanguageForm.title = undefined;
        this.resumeLanguageForm.description = undefined;
      }
      this.$nextTick(() => {
        this.$refs["resumeLanguageForm"][0].clearValidate();
      });
    },
    handleEditResumeAwards(type, resumeAwards) {
      this.showAwardsDialog = true;
      let i;
      if (this.newFlag == true) {
        i = this.resume.length - 1;
      } else if (this.newIndex && this.newIndex != "") {
        i = this.newIndex;
      } else {
        i = 0;
      }
      this.resumeAwardsForm.resumeId = this.resume[i].id;
      if (type === "update") {
        this.resumeAwardsForm.id = resumeAwards.id;
        this.resumeAwardsForm.title = resumeAwards.title;
        this.resumeAwardsForm.time = resumeAwards.time;
      } else {
        this.resumeAwardsForm.id = undefined;
        this.resumeAwardsForm.title = undefined;
        this.resumeAwardsForm.time = undefined;
      }
      this.$nextTick(() => {
        this.$refs["resumeAwardsForm"][0].clearValidate();
      });
    },
    handleEditResumeExp(type, resumeExp) {
      this.showExpDialog = true;
      this.resumeExpForm.resumeId = this.resume.id;
      this.resumeExpForm.depart = "xxx";
      if (type === "update") {
        this.resumeExpForm.id = resumeExp.id;
        this.resumeExpForm.startTime = resumeExp.startTime;
        this.resumeExpForm.finishTime = resumeExp.finishTime;
        this.resumeExpForm.company = resumeExp.company;
        this.resumeExpForm.industry = resumeExp.industry;
        this.resumeExpForm.post = resumeExp.post;
        this.resumeExpForm.workType = resumeExp.workType;
        this.resumeExpForm.description = resumeExp.description;
        this.resumeExpForm.onWork = resumeExp.onWork;
        if (resumeExp.startTime && resumeExp.finishTime) {
          this.resumeExpForm.workingDates = [
            this.resumeExpForm.startTime,
            this.resumeExpForm.finishTime,
          ];
        }
      } else {
        this.resumeExpForm.id = undefined;
        this.resumeExpForm.startTime = undefined;
        this.resumeExpForm.finishTime = undefined;
        this.resumeExpForm.company = undefined;
        this.resumeExpForm.post = undefined;
        this.resumeExpForm.workType = undefined;
        this.resumeExpForm.description = undefined;
        this.resumeExpForm.workingDates = undefined;
        this.resumeExpForm.onWork = 0;
        this.resumeExpForm.industry = undefined;
      }
      this.$nextTick(() => {
        this.$refs["resumeExpForm"][0].clearValidate();
      });
    },
    handleEditResumePractice(type, resumePractice) {
      this.showPracticeDialog = true;
      let i;
      if (this.newFlag == true) {
        i = this.resume.length - 1;
      } else if (this.newIndex && this.newIndex != "") {
        i = this.newIndex;
      } else {
        i = 0;
      }
      this.resumePracticeForm.resumeId = this.resume[i].id;
      if (type === "update") {
        this.resumePracticeForm.id = resumePractice.id;
        this.resumePracticeForm.startTime = resumePractice.startTime;
        this.resumePracticeForm.finishTime = resumePractice.finishTime;
        this.resumePracticeForm.title = resumePractice.title;
        this.resumePracticeForm.description = resumePractice.description;
        this.resumePracticeForm.post = resumePractice.post;
        this.resumePracticeForm.onWork = resumePractice.onWork;
        if (resumePractice.startTime && resumePractice.finishTime) {
          this.resumePracticeForm.workingDates = [
            this.resumePracticeForm.startTime,
            this.resumePracticeForm.finishTime,
          ];
        }
      } else {
        this.resumePracticeForm.id = undefined;
        this.resumePracticeForm.workingDates = undefined;
        this.resumePracticeForm.startTime = undefined;
        this.resumePracticeForm.finishTime = undefined;
        this.resumePracticeForm.title = undefined;
        this.resumePracticeForm.description = undefined;
        this.resumePracticeForm.post = undefined;
        this.resumePracticeForm.onWork = undefined;
      }
      this.$nextTick(() => {
        this.$refs["resumePracticeForm"][0].clearValidate();
      });
    },
    handleEditExpectJob() {
      //获取全部城市cityOptions
      this.$axios
        .request({
          url: "/city/list",
          method: "get",
        })
        .then((data) => {
          let municipality = ["北京市", "上海市", "天津市", "重庆市"];
          this.cityOptions = data.data.map((first) => {
            if (first.children) {
              first.children = first.children.map((second) => {
                if (municipality.includes(second.name)) {
                  delete second.children;
                } else if (second.children) {
                  second.children = second.children.map((third) => {
                    delete third.children;
                    return third;
                  });
                }
                return second;
              });
            }
            return first;
          });
          this.cityOptions.push({ name: "不限" });
        });
      let i;
      if (this.newFlag == true) {
        i = this.resume.length - 1;
      } else if (this.newIndex && this.newIndex != "") {
        i = this.newIndex;
      } else {
        i = 0;
      }

      //初始化预期薪资
      //获取salaryId -> salaryList
      let tempSalaryList = this.resume[i].userExpectJob.salaryId;
      let salaryList = [];
      let salaryNameArr = [];
      tempSalaryList.forEach((item) => {
        this.salaryOptions.forEach((option) => {
          if (option.id == item) {
            salaryList.push(option.id);
            salaryNameArr.push(option.name);
          }
        });
      });
      this.salaryNameList = salaryNameArr.toString();
      this.expectJobForm.salaryId = salaryList;

      this.expectJobForm.resumeId = this.resume[i].id;
      this.expectJobForm.expectWorkType = this.resume[
        i
      ].userExpectJob.expectWorkType;
      this.expectJobForm.expectPosition = this.resume[
        i
      ].userExpectJob.expectPosition;

      this.expectJobForm.expectCity = this.resume[i].userExpectJob.expectCity;
      this.expectJobForm.industry = this.resume[i].userExpectJob.industry;
      this.$nextTick(() => {
        this.$refs["expectJobForm"][0].clearValidate();
      });
    },

    handleEditResumeSkill() {
      this.showSkillDialog = true;
      this.skillTagListForm.length = 0;
      let i;
      if (this.newFlag == true) {
        i = this.resume.length - 1;
      } else if (this.newIndex && this.newIndex != "") {
        i = this.newIndex;
      } else {
        i = 0;
      }
      if (
        this.resume[i].resumeSkillList &&
        this.resume[i].resumeSkillList.length !== 0
      ) {
        for (const resumeSkill of this.resume[i].resumeSkillList) {
          this.skillTagListForm.push({ name: resumeSkill.name, select: true });
        }
      }
      for (const skillTag of this.skillTagOptions) {
        const found = this.skillTagListForm.find(
          (selectTag) => skillTag.name == selectTag.name
        );
        if (!found) {
          this.skillTagListForm.push({ name: skillTag.name, select: false });
        }
      }
      console.log(this.resume[i].resumeSkillList);
      let result = this.resume[i].resumeSkillList.map((value, index) => {
        return { name: value.name, select: true };
      });
      this.listshowskill = result;
      this.listskill = this.listshowskill;
      console.log(this.listskill);
    },
    handleDelResumeEdu(id) {
      this.handleDeleteItemById(delResumeEdu, id);
      this.getResumeInfo();
    },
    handleDelResumeExp(id) {
      this.handleDeleteItemById(delResumeExp, id);
    },
    handleDelResumePractice(id) {
      this.handleDeleteItemById(delResumePractice, id);
    },
    handleDelResumeLanguage(id) {
      this.handleDeleteItemById(delResumeLanguage, id);
    },
    handleDelResumeAwards(id) {
      this.handleDeleteItemById(delResumeAwards, id);
      console.log(id);
    },
    handleSaveParseAttachment() {
      this.$refs["parseAttachmentForm"][0].validate((valid) => {
        console.log(this.parseAttachmentForm);
        if (valid) {
          parseAttachment(this.parseAttachmentForm)
            .then(() => {
              this.getResumeInfo();
            })
            .finally(() => {
              this.posting = false;
            });
        }
      });
    },
    handleSaveResumeAttachResume() {
      this.$refs["resumeForm2"][0].validate((valid) => {
        if (valid) {
          let i;
          if (this.newFlag == true) {
            i = this.resume.length - 1;
          } else if (this.newIndex && this.newIndex != "") {
            i = this.newIndex;
          } else {
            i = 0;
          }
          this.resumeForm2.id = this.resume[i].id;
          this.resumeForm2.resumeId = this.resume[i].id;
          saveResumeBasic(this.resumeForm2)
            .then(() => {
              this.getResumeInfo();
            })
            .finally(() => {
              this.posting = false;
            });
        }
      });
    },
    handleSaveAttachOthersResume() {
      this.$refs["resumeAttachForm"][0].validate((valid) => {
        if (valid) {
          let i;
          if (this.newFlag == true) {
            i = this.resume.length - 1;
          } else if (this.newIndex && this.newIndex != "") {
            i = this.newIndex;
          } else {
            i = 0;
          }
          console.log(this.resumeAttachForm.attachOthers);
          this.resumeAttachForm.id = this.resume[i].id;
          this.resumeAttachForm.resumeId = this.resume[i].id;
          saveResumeBasic(this.resumeAttachForm)
            .then(() => {
              this.getResumeInfo();
            })
            .finally(() => {
              this.posting = false;
            });
        }
      });
      this.$nextTick(() => {
        this.$refs["resumeAttachForm"][0].clearValidate();
      });
    },
    handleEditPriority() {
      let i;
      if (this.newFlag == true) {
        i = this.resume.length - 1;
      } else if (this.newIndex && this.newIndex != "") {
        i = this.newIndex;
      } else {
        i = 0;
      }
      this.resumeForm5.resumeId = this.resume[i].id;
      if (this.resume[i].priority == 0) {
        this.resumeForm5.priority = "第一";
      } else if (this.resume[i].priority == 1) {
        this.resumeForm5.priority = "第二";
      } else if (this.resume[i].priority == 2) {
        this.resumeForm5.priority = "第三";
      } else if (this.resume[i].priority == 3) {
        this.resumeForm5.priority = "第四";
      }
      // this.setResumeFormValues();
      this.$nextTick(() => {
        this.$refs["resumeForm5"][0].clearValidate();
      });
    },
    handleSavePriority() {
      let i;
      if (this.newFlag == true) {
        i = this.resume.length - 1;
      } else if (this.newIndex && this.newIndex != "") {
        i = this.newIndex;
      } else {
        i = 0;
      }
      this.$refs["resumeForm5"][0].validate((valid) => {
        if (valid) {
          for (const item of this.resume) {
            if (this.resumeForm5.priority == item.priority) {
              this.$confirm(
                "该优先级已存在" +
                  item.title +
                  "中，确认将会把" +
                  item.title +
                  "的优先级后延",
                "",
                {
                  confirmButtonText: "确定",
                  cancelButtonText: "取消",
                  type: "warning",
                  center: true,
                }
              )
                .then(() => {
                  this.resumeForm5.id = this.resume[i].id;
                  saveResumeBasic(this.resumeForm5)
                    .then(() => {
                      this.getResumeInfo();
                      this.resumeForm5.id = item.id;
                      this.resumeForm5.priority = item.priority + 1;
                      saveResumeBasic(this.resumeForm5)
                        .then(() => {
                          this.getResumeInfo();
                          this.handleEditPriority();
                        })
                        .finally(() => {
                          this.posting = false;
                        });
                    })
                    .finally(() => {
                      this.posting = false;
                    });
                  this.$message({
                    type: "success",
                    message: "修改成功!",
                  });
                })
                .catch(() => {
                  this.$message({
                    type: "info",
                    message: "已取消",
                  });
                });
            } else {
              this.resumeForm5.id = this.resume[i].id;
              saveResumeBasic(this.resumeForm5)
                .then(() => {
                  this.getResumeInfo();
                })
                .finally(() => {
                  this.posting = false;
                });
            }
          }
        }
      });
    },
    handleSaveResumeIntro() {
      this.$refs["resumeForm1"][0].validate((valid) => {
        if (valid) {
          let i;
          if (this.newFlag == true) {
            i = this.resume.length - 1;
          } else if (this.newIndex && this.newIndex != "") {
            i = this.newIndex;
          } else {
            i = 0;
          }
          this.resumeForm1.id = this.resume[i].id;
          saveResumeBasic(this.resumeForm1)
            .then(() => {
              this.getResumeInfo();
              this.showIntroDialog = false;
            })
            .finally(() => {
              this.posting = false;
            });
        }
      });
    },
    handleSaveResumeBasic(index) {
      console.log(index);
      console.log(this.$refs["resumeForm"][index]);
      this.$refs["resumeForm"][index].validate((valid) => {
        if (valid) {
          let i;
          if (this.newFlag == true) {
            i = this.resume.length - 1;
          } else if (this.newIndex && this.newIndex != "") {
            i = this.newIndex;
          } else {
            i = 0;
          }
          if (this.resumeForm.maritalStatus == "中共党员（含预备党员）") {
            this.resumeForm.maritalStatus = 0;
          } else if (this.resumeForm.maritalStatus == "民主党派") {
            this.resumeForm.maritalStatus = 1;
          } else if (this.resumeForm.maritalStatus == "无党派人士") {
            this.resumeForm.maritalStatus = 2;
          } else if (this.resumeForm.maritalStatus == "团员") {
            this.resumeForm.maritalStatus = 3;
          } else if (this.resumeForm.maritalStatus == "群众") {
            this.resumeForm.maritalStatus = 4;
          }
          this.resumeForm.resumeId = this.resume[i].id;
          saveResumeBasic(this.resumeForm)
            .then(() => {
              this.getResumeInfo();
              this.showBasicDialog = false;
            })
            .finally(() => {
              this.posting = false;
            });
        }
      });
    },
    postSaveResumeBasic() {
      this.posting = true;
      this.resumeForm.id = this.newResumeId;
      saveResumeBasic(this.resumeForm)
        .then(() => {
          this.showBasicDialog = false;
          this.showIntroDialog = false;
          this.getResumeInfo();
          Toast.success("保存成功");
        })
        .finally(() => {
          this.posting = false;
        });
    },
    handleSaveResumeEdu(index) {
      let i;
      if (this.newFlag == true) {
        i = this.resume.length - 1;
      } else if (this.newIndex && this.newIndex != "") {
        i = this.newIndex;
      } else {
        i = 0;
      }
      if (this.schoolCheck == true) {
        this.resumeEduForm.workingDates = [
          this.resume[i].updateTime,
          this.resume[i].updateTime,
        ];
      }
      console.log(this.resume[i].updateTime);
      this.$refs["resumeEduForm"][index].validate((valid) => {
        if (valid) {
          this.resumeEduForm.startTime = this.resumeEduForm.workingDates[0];
          this.resumeEduForm.finishTime = this.resumeEduForm.workingDates[1];
          this.resumeEduForm.resumeId = this.resume[i].id;
          saveResumeEdu(this.resumeEduForm)
            .then(() => {
              this.getResumeInfo();
              this.showEduDialog = false;
            })
            .finally(() => {
              this.posting = false;
            });
        }
      });
    },
    handleSaveResumeExp(index) {
      this.$refs["resumeExpForm"][index].validate((valid) => {
        if (valid) {
          this.resumeExpForm.depart = "xxx";
          let i;
          if (this.newFlag == true) {
            i = this.resume.length - 1;
          } else if (this.newIndex && this.newIndex != "") {
            i = this.newIndex;
          } else {
            i = 0;
          }
          this.resumeExpForm.resumeId = this.resume[i].id;
          if (
            this.resumeExpForm.workingDates &&
            this.resumeExpForm.workingDates.length == 2
          ) {
            this.resumeExpForm.startTime = this.resumeExpForm.workingDates[0];
            this.resumeExpForm.finishTime = this.resumeExpForm.workingDates[1];
          }
          saveResumeExp(this.resumeExpForm)
            .then(() => {
              this.getResumeInfo();
              this.showExpDialog = false;
            })
            .finally(() => {
              this.posting = false;
            });
        }
      });
    },
    handleSaveResumePractice(index) {
      this.$refs["resumePracticeForm"][index].validate((valid) => {
        if (valid) {
          let i;
          if (this.newFlag == true) {
            i = this.resume.length - 1;
          } else if (this.newIndex && this.newIndex) {
            i = this.newIndex;
          } else {
            i = 0;
          }
          this.resumePracticeForm.resumeId = this.resume[i].id;
          if (
            this.resumePracticeForm.workingDates &&
            this.resumePracticeForm.workingDates.length == 2
          ) {
            this.resumePracticeForm.startTime = this.resumePracticeForm.workingDates[0];
            this.resumePracticeForm.finishTime = this.resumePracticeForm.workingDates[1];
          }
          if (
            !this.resumePracticeForm.startTime ||
            this.resumePracticeForm.startTime == "" ||
            !this.resumePracticeForm.finishTime ||
            this.resumePracticeForm.finishTime == ""
          ) {
            return;
          }
          saveResumePractice(this.resumePracticeForm)
            .then(() => {
              this.getResumeInfo();
              this.showPracticeDialog = false;
            })
            .finally(() => {
              this.posting = false;
            });
        }
      });
    },
    handleSaveResumeLanguage(index) {
      this.$refs["resumeLanguageForm"][index].validate((valid) => {
        if (valid) {
          let i;
          if (this.newFlag == true) {
            i = this.resume.length - 1;
          } else if (this.newIndex && this.newIndex != "") {
            i = this.newIndex;
          } else {
            i = 0;
          }
          this.resumeLanguageForm.resumeId = this.resume[
            this.resume.length - 1
          ].id;
          saveResumeLanguage(this.resumeLanguageForm)
            .then(() => {
              this.getResumeInfo();
              this.showLanguageDialog = false;
            })
            .finally(() => {
              this.posting = false;
            });
        }
      });
    },
    handleSaveResumeAwards(index) {
      this.$refs["resumeAwardsForm"][index].validate((valid) => {
        if (valid) {
          let i;
          if (this.newFlag == true) {
            i = this.resume.length - 1;
          } else if (this.newIndex && this.newIndex != "") {
            i = this.newIndex;
          } else {
            i = 0;
          }
          this.resumeAwardsForm.resumeId = this.resume[i].id;
          saveResumeAwards(this.resumeAwardsForm)
            .then(() => {
              this.getResumeInfo();
              this.showAwardsDialog = false;
            })
            .finally(() => {
              this.posting = false;
            });
        }
      });
    },
    handleSaveExpectJob(index) {
      this.$refs["expectJobForm"][index].validate((valid) => {
        if (valid) {
          if (this.newResumeId && this.newResumeId != "") {
            this.expectJobForm.resumeId = this.newResumeId;
          } else if (this.newFlag == true) {
            this.expectJobForm.resumeId = this.resume[
              this.resume.length - 1
            ].id;
          } else {
            this.expectJobForm.resumeId = this.resume[0].id;
          }

          if (this.salaryCheck == true) {
            this.expectJobForm.salaryId = 1038;
          } else if (
            this.expectJobForm.salaryId &&
            this.expectJobForm.salaryId.length > 0
          ) {
            let temp = this.expectJobForm.salaryId.toString();
            this.expectJobForm.salaryId = temp;
          }
          this.expectJobForm.categoryId = 122;
          saveUserExpectJob(this.expectJobForm)
            .then(() => {
              this.getResumeInfo();
              this.expectJobForm.salaryId = "  ";
              this.showExpectJobDialog = false;
            })
            .finally(() => {
              this.posting = false;
            });
        }
      });
    },
    handleSaveResumeSkills() {
      let i;
      if (this.newFlag == true) {
        i = this.resume.length - 1;
      } else if (this.newIndex && this.newIndex != "") {
        i = this.newIndex;
      } else {
        i = 0;
      }
      const selectSkillTags = {
        resumeId: this.resume[i].id,
        tagList: this.skillTagListForm
          .filter((tag) => tag.select == true)
          .map((tag) => {
            return tag.name;
          }),
      };
      saveResumeSkills(selectSkillTags)
        .then(() => {
          this.getResumeInfo();
          this.showSkillDialog = false;
        })
        .finally(() => {
          this.posting = false;
        });
    },
    searchSchoolName(keyword, cb) {
      if (!keyword || keyword.length < 2) {
        return;
      }
      searchSchool(keyword).then((response) => {
        cb(response.data);
      });
    },
    searchCompanyName(keyword, cb) {
      if (!keyword || keyword.length < 2) {
        return;
      }
      serachByName(keyword).then((response) => {
        cb(response.data.list);
      });
    },
    searchCityName(keyword, cb) {
      if (!keyword || keyword.length < 1) {
        return;
      }
      getCityByName(keyword).then((response) => {
        cb(response.data);
      });
    },
    handleDeleteItemById(delById, id) {
      this.$confirm("是否要删除该记录？", {
        confirmButtonText: "删除",
      }).then(() => {
        delById(id).then(() => {
          Toast.success("删除成功");
          this.getResumeInfo();
        });
      });
    },
    selectSkillTag(skillTag) {
      if (skillTag.select) {
        skillTag.select = false;
        console.log(skillTag);
      } else {
        if (this.checkSelectSkillTagCount()) {
          skillTag.select = true;
          console.log(skillTag);
        }
      }
      this.listskill.push();
      console.log(this.listskill);
      const selectTagList = this.skillTagListForm.filter(
        (tag) => tag.select == true
      );
      console.log(selectTagList);
      this.listshowskill = selectTagList;
    },
    handleAddNewSkillTag() {
      if (this.newSkillTag != "" && this.checkSelectSkillTagCount()) {
        this.skillTagListForm.unshift({
          name: this.newSkillTag,
          select: true,
        });
        console.log(this.newSkillTag);
        this.listshowskill.push({ name: this.newSkillTag, select: true });
        this.newSkillTag = "";
      }
    },
    checkSelectSkillTagCount() {
      const selectTagList = this.skillTagListForm.filter(
        (tag) => tag.select == true
      );
      if (selectTagList.length > 5) {
        Toast.error("标签数量不能超过6个");
        return false;
      } else {
        return true;
      }
    },
    handlePreview() {
      if (this.newResumeId && this.newResumeId != "") {
        this.resumeId = this.newResumeId;
      } else if (this.newFlag == true) {
        this.resumeId = this.resume[this.resume.length - 1].id;
      } else {
        this.resumeId = this.resume[0].id;
      }
      getResumeDetail(this.resumeId).then((response) => {
        this.resumeDetail = response.data;
        console.log(this.resumeDetail);
      });

      this.showResumeDialog = true;
      console.log(this.resumeId);

      // this.$router.push({path: `/resume/${this.resume.id}`});
    },

    exportPdf() {
      if (this.newResumeId && this.newResumeId != "") {
        this.resumeId = this.newResumeId;
      } else {
        this.resumeId = this.resume[this.resume.length - 1].id;
      }
      exportResumeToPdf(this.resumeId)
        .then((response) => {
          downloadFile({
            fileKey: response.data,
            fileName: `${this.$store.getters.name}_个人简历.pdf`,
            success: () => {
              this.$set(this, "showResumeDialog", false);
            },
          });
        })
        .catch(() => {});
    },
    handleDelResume() {
      let i;
      if (this.newFlag == true) {
        i = this.resume.length - 1;
      } else if (this.newIndex && this.newIndex != "") {
        i = this.newIndex;
      } else {
        i = 0;
      }
      if (this.resume.length > 1) {
        this.$confirm("是否要删除该简历？", {
          confirmButtonText: "删除",
        }).then(() => {
          let id = this.resume[i].id;
          console.log(id);
          delResume(id).then(() => {
            this.getResumeInfo();
          });
        });
      } else {
        this.$confirm("简历至少存在一份！", "", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(() => {})
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消删除",
            });
          });
      }
      this.getResumeInfo();
    },
    onDelResumeAttachClick() {
      this.$confirm("是否要删除附件简历？", {
        confirmButtonText: "删除",
      }).then(() => {
        delResumeAttachment(this.resume.id).then(() => {
          this.getResumeInfo();
        });
      });
    },
  },

  filters: {
    ellipsis(value) {
      let len = value.length;
      if (!value) return "";
      if (value.length > 20) {
        return value.substring(0, 8) + "......" + value.substring(len - 8, len);
      }
      return value;
    },
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" scoped>
$border-style: 1px solid #eee;

.app-container {
  /*margin: 20px auto;*/
  /*min-height: calc(100vh - 477px);*/
  /*background: rgba(213, 226, 240, 0.21);*/

  .cancel-dialog {
    .text {
      margin-top: 12px;
      margin-bottom: 30px;
      font-size: 21px;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #333333;
      line-height: 29px;
    }

    /deep/ .el-dialog__body{
      padding-top: 0;
    }

    /deep/ .el-dialog {
      background: #ffffff;
      border-radius: 27px;
      .footer {
        display: flex;
        align-items: center;
        justify-content: center;
        /deep/ .el-button {
          width: 101px;
          height: 31px;
          border-radius: 16px;

          border: 1px solid #4895ef;
          display: flex;
          align-items: center;
          justify-content: center;

          span {
          }
        }
      }
    }
  }
  min-width: 375px;
  ::v-deep.el-tabs__nav-scroll {
    width: fit-content;
    height: 45px;
    background: #ffffff;
    border-radius: 25px;
    border: 1px solid #cccccc;
  }
  ::v-deep.el-tabs--card > .el-tabs__header {
    width: fit-content;
    height: 35px;
    border-radius: 18px;
    border-bottom: 0px;
    /*margin-left: 56px;*/
  }
  ::v-deep.el-tabs--card > .el-tabs__header .el-tabs__nav {
    border: 0px;
    border-bottom: none;
    border-radius: 4px 4px 0 0;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
  }
  ::v-deep.el-tabs__item .el-icon-close:before {
    transform: scale(0.9);
    display: none;
  }
  ::v-deep.el-tabs--card > .el-tabs__header .el-tabs__item .el-icon-close {
    position: relative;
    font-size: 12px;
    width: 0;
    height: 14px;
    vertical-align: middle;
    line-height: 15px;
    overflow: hidden;
    top: -1px;
    right: -2px;
  }
  ::v-deep.el-tabs--card > .el-tabs__header .el-tabs__item {
    border-left: none;
    width: 80px;
    height: 35px;
    background: #4895ef;
    border-radius: 18px;
    margin: 5px;
    font-size: 18px;
    font-family: PingFangSC-Medium, PingFang SC;
    font-weight: 500;
    color: #ffffff;
    line-height: 34px;
    text-align: center;
  }
  ::v-deep.el-tabs--card
    > .el-tabs__header
    .el-tabs__item.is-active.is-closable {
    width: fit-content;
    height: 35px;
    background: #4895ef;
    border-radius: 18px;
    font-size: 18px;
    font-family: PingFangSC-Medium, PingFang SC;
    font-weight: 500;
    color: #ffffff;
    line-height: 35px;
    font-size: 18px;
    font-family: PingFangSC-Medium, PingFang SC;
    font-weight: 500;
    color: #ffffff;
    margin: 4px 4px 3px 4px;
  }
  ::v-deep.el-tabs--card > .el-tabs__header .el-tabs__item.is-closable {
    width: fit-content;
    height: 35px;
    margin: 3px 6px 3px 3px;
    background: #ffffff;
    border-radius: 18px;
    font-size: 18px;
    font-family: PingFangSC-Medium, PingFang SC;
    font-weight: 500;
    color: #333333;
  }
  ::v-deep.el-tabs--card
    > .el-tabs__header
    .el-tabs__item.is-active.is-closable
    .el-icon-close,
  .el-tabs--card
    > .el-tabs__header
    .el-tabs__item.is-closable:hover
    .el-icon-close {
    width: 0px;
  }
  ::v-deep.el-tabs__new-tab {
    float: right;
    border: 1.5px solid #4b97f0;
    height: 28px;
    width: 28px;
    line-height: 0px;
    margin: 8px 0 10px 10px;
    border-radius: 20px;
    padding-top: 3px;
    font-size: 20px;
    color: #4b97f0;
    cursor: pointer;
    -webkit-transition: all 0.15s;
    transition: all 0.15s;
  }
  ::v-deep.el-tabs--card > .el-tabs__header.el-tabs__new-tab {
    float: right;
    border: 1px solid #d3dce6;
    height: 18px;
    width: 18px;
    line-height: 18px;
    margin: 12px 0 9px 10px;
    border-radius: 50px;
    text-align: center;
    font-size: 12px;
    color: #d3dce6;
    cursor: pointer;
    transition: all 0.15s;
  }
  ::v-deep .el-dialog__headerbtn .el-dialog__close {
    color: #4895ef;
    border: 1px solid;
    line-height: 16px;
    border-radius: 50%;
  }
  ::v-deep.el-tag--light {
    height: 21px;
    font-size: 12px;
    font-family: PingFangSC-Medium, PingFang SC;
    font-weight: 500;
    color: #ffffff;
    background: #4cc9f0;
    border-color: #4cc9f0;
    border-radius: 5px;
    line-height: 10px;
    margin: 5px;
    padding-top: 5px;
  }
  .selectleave {
    ::v-deep.el-input--suffix .el-input__inner {
      padding-right: 30px;
      height: 25px;
      width: 89px;
      background: #ebf3fd;
      border-radius: 3px;
      border: 1px solid #4895ef;
      font-size: 14px;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #4895ef;
    }

    ::v-deep.el-input__icon {
      height: 32px;
      width: 25px;
      text-align: center;
      -webkit-transition: all 0.3s;
      transition: all 0.3s;
      line-height: 40px;
      color: #4895ef;
    }
  }
  .resume-wrapper {
    margin-top: 100px;
    width: 760px;
    background: #ffffff;
    box-shadow: 0px 18px 14px 3px rgba(205, 213, 224, 0.3);
    padding: 20px 0px 20px 0px;
    margin-right: 42px;
    margin-bottom: 100px;

    .resume-body {
      /*padding-top: 119px;*/
      padding-left: 68px;
      .required {
        margin-left: -53px;
        margin-top: -20px;
        height: 25px;
        background: #ffebee;
        font-size: 11px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #666666;
        line-height: 23px;
        .required-text {
          padding-left: 54px;
        }
        .required-link {
          font-size: 11px;
          font-family: PingFangSC-Regular, PingFang SC;
          font-weight: 400;
          color: #4895ef;
        }
      }

      .titleText {
        font-size: 14px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #333333;
        ::v-deep.svg-cls {
          width: 1em;
          vertical-align: -0.25em;
          fill: currentColor;
          overflow: hidden;
        }
      }
      .editTitle {
        font-size: 12px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #333333;
        ::v-deep.el-form-item__error {
          color: #f56c6c;
          font-size: 12px;
          line-height: 0px;
          padding-top: 4px;
          position: absolute;
          top: 90%;
          left: 0;
        }

        ::v-deep .ej-btn {
          width: 75px;
          height: 23px;
          border-radius: 12px;
          border: 1px solid #4895ef;
          font-size: 12px;
          font-family: PingFangSC-Medium, PingFang SC;
          font-weight: 500;
          color: #4895ef;
          line-height: 1px;
        }

        ::v-deep .el-date-editor .el-range-separator {
          padding: 0 5px;
          line-height: 14px;
          width: 5%;
          color: #303133;
        }

        ::v-deep .el-input__icon {
          height: 100%;
          width: 25px;
          text-align: center;
          -webkit-transition: all 0.3s;
          transition: all 0.3s;
          line-height: unset;
          color: #4895ef;
        }

        ::v-deep .el-input {
          position: relative;
          font-size: 14px;
          display: inline-block;
          width: 140px;
        }

        ::v-deep .el-input__inner {
          width: 140px;
          height: 23px;
          background: #ffffff;
          border-radius: 12px;
          border: 1px solid #ccdbf5;
          font-size: 11px;
          font-family: PingFangSC-Regular, PingFang SC;
          font-weight: 400;
          color: #999999;
          padding-left: 4px;
        }

        ::v-deep .el-form-item__label {
          width: 80px;
          font-size: 12px;
          font-weight: 500;
          color: #333333;
          line-height: 36px;
          /*padding: 0 0px 0 0;*/
        }

        ::v-deep.el-date-editor .el-range-input,
        .el-date-editor .el-range-separator {
          height: 100%;
          width: 70px;
          margin: 0;
          text-align: center;
          display: inline-block;
          font-size: 11px;
        }

        ::v-deep.el-form-item__content {
          line-height: 40px;
          position: relative;
          font-size: 11px;
        }
      }

      .resume-point {
        padding-top: 10px;
        width: 7px;
        height: 7px;
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

      .resume-base1 {
        height: 25px;
        font-size: 18px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #333333;
        line-height: 25px;
        padding-right: 1px;
        padding-bottom: 13px;
      }

      .avatorHolder {
        padding-left: 30px;
        .avatar-uploader .el-upload {
          cursor: pointer;
          position: relative;
          overflow: hidden;
          margin-bottom: 7px;
        }

        $avatarSize: 100px;

        .avatar-uploader .avatar-uploader-icon {
          border: 1px solid #3f5ff4;
          border-radius: 5px;
          font-size: 28px;
          color: #8c939d;
          width: $avatarSize;
          height: $avatarSize;
          line-height: $avatarSize;
          text-align: center;
        }

        .avatar-uploader .avatar {
          width: $avatarSize;
          height: $avatarSize;
          display: block;
        }

        .upload-attach-box .el-upload {
          display: block;
        }

        .upload-attach-box .el-upload button {
          height: 31px;
          margin-top: 17px;
          font-size: 18px;
          padding-top: 7px;
          width: 192px;
          background: #4895ef;
          border-radius: 50px;
          font-size: 14px;
          font-family: PingFangSC-Medium, PingFang SC;
          font-weight: 500;
          color: #ffffff;
        }
      }

      .resume-red {
        padding-left: 5px;
        color: #d9001b;
        font-size: 20px;
        height: 9px;
        width: 9px;
      }

      .jobcount {
        padding-left: 13px;
        width: 19px;
        padding-right: 5px;
        height: 17px;
        font-size: 12px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #4f69ed;
        line-height: 17px;
      }

      .mark-row {
        width: 100px;
        padding-right: 6px;

        .mark-icon {
          width: 11px;
          height: 10px;
          vertical-align: text-top;
        }
      }

      .mark-text {
        width: 78px;
        height: 22px;
        background: #eeeeee;
        border-radius: 5px;
        font-size: 6px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #666666;
        line-height: 9px;
        vertical-align: text-top;
      }
      .rowbasic {
        ::v-deep.svg-cls {
          width: 1em;
          vertical-align: -0.25em;
          fill: currentColor;
          overflow: hidden;
        }
        .resume-updateTime {
          width: 204px;
          height: 20px;
          font-size: 14px;
          font-family: PingFangSC-Medium, PingFang SC;
          font-weight: 500;
          color: #666666;
          line-height: 35px;
          padding-bottom: 10px;
        }
        .help-text {
          padding: 3px;
          width: 263px;
          height: 80px;
          background: #eeeeee;
          border-radius: 5px;
          font-size: 4px;
          font-family: PingFangSC-Regular, PingFang SC;
          font-weight: 400;
          color: #666666;
          line-height: 15px;
        }
        .selectleave {
          ::v-deep.el-input--suffix .el-input__inner {
            padding-right: 30px;
            height: 25px;
            width: 89px;
            background: #ebf3fd;
            border-radius: 3px;
            border: 1px solid #4895ef;
            font-size: 14px;
            font-family: PingFangSC-Medium, PingFang SC;
            font-weight: 500;
            color: #4895ef;
          }

          ::v-deep.el-input__icon {
            height: 32px;
            width: 25px;
            text-align: center;
            -webkit-transition: all 0.3s;
            transition: all 0.3s;
            line-height: 40px;
            color: #4895ef;
          }
        }
      }

      .img-button {
        margin-top: 8px;
        margin-left: 6px;
        padding-left: 12px;
        width: 75px;
        height: 17px;
        background: #edf2ff;
        border-radius: 50px;
        font-size: 10px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #3f5ff4;
        line-height: 0px;
        border-color: #edf2ff;
      }

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
          width: 478px;
          display: inline-block;
          word-wrap: break-word;
          font-size: 14px;
          font-family: PingFangSC-Regular, PingFang SC;
          font-weight: 400;
          color: #666666;
          line-height: 20px;
        }
      }

      .info-other-row-m {
        margin-top: 45px;
        padding-bottom: 4px;
        .info-other {
          height: 20px;
          font-size: 14px;
          font-family: PingFangSC-Medium, PingFang SC;
          font-weight: 500;
          color: #333333;
        }
      }

      .info-other-row-l {
        margin-left: 20px;
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
          padding-top: 10px;
        }
      }

      .info-other-row {
        height: 32px;
        line-height: 32px;
        margin-right: 10px;

        .expjob-data {
          width: 300px;
          height: 20px;
          font-size: 14px;
          font-family: PingFangSC-Regular, PingFang SC;
          font-weight: 400;
          color: #333333;
          line-height: 20px;
        }

        .info-other {
          height: 20px;
          font-size: 14px;
          font-family: PingFangSC-Medium, PingFang SC;
          font-weight: 500;
          color: #333333;
          padding-top: 10px;
        }
      }
      .resume-info {
        display: inline-flex;
      }
    }
  }
  .right-box {
    margin-top: 100px;
    height: fit-content;
    padding-bottom: 100px;
    width: 26%;
    background: #ffffff;
    box-shadow: 0px 18px 14px 3px rgba(205, 213, 224, 0.3);
  }
  .resume-right-box {
    background: #fff;
    padding: 41px 25px 0px 20px;
    /*<!--border: $border-style;-->*/
    /*margin-bottom: 15px;*/
    ::v-deep.svg-cls {
      width: 1em;
      vertical-align: -0.25em;
      fill: currentColor;
      overflow: hidden;
    }
    .left-btn {
      height: 39px;
      background: #ffffff;
      box-shadow: 0px 2px 4px 0px rgba(72, 149, 239, 0.68);
      border-radius: 20px;
      font-size: 14px;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #4895ef;
      line-height: 12px;
      ::v-deep.svg-cls {
        width: 1em;
        vertical-align: -0.25em;
        fill: currentColor;
        overflow: hidden;
      }
    }
  }
  .resume-right-box1 {
    background: #fff;
    padding: 10px 10px 0px 20px;
    /*<!--border: $border-style;-->*/
    /*margin-bottom: 15px;*/
    .editTitle {
      font-size: 12px;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #333333;
      ::v-deep .el-input {
        position: relative;
        font-size: 14px;
        display: inline-block;
        width: 135px;
        ::v-deep .el-input__inner {
          width: 130px;
          height: 23px;
          background: #ffffff;
          border-radius: 12px;
          border: 1px solid #ccdbf5;
          font-size: 11px;
          font-family: PingFangSC-Regular, PingFang SC;
          font-weight: 400;
          color: #999999;
          padding-left: 4px;
        }
      }

      ::v-deep.el-form-item__error {
        color: #f56c6c;
        font-size: 12px;
        line-height: 0px;
        padding-top: 4px;
        position: absolute;
        top: 90%;
        left: 0;
      }

      .ej-btn {
        width: 75px;
        height: 23px;
        border-radius: 12px;
        border: 1px solid #4895ef;
        font-size: 12px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #4895ef;
        line-height: 1px;
      }

      ::v-deep .el-date-editor .el-range-separator {
        padding: 0 5px;
        line-height: 14px;
        width: 5%;
        color: #303133;
      }

      ::v-deep .el-input__icon {
        height: 100%;
        width: 25px;
        text-align: center;
        -webkit-transition: all 0.3s;
        transition: all 0.3s;
        line-height: unset;
        color: #4895ef;
      }

      ::v-deep .el-form-item__label {
        width: 80px;
        font-size: 12px;
        font-weight: 500;
        color: #333333;
        line-height: 36px;
        /*padding: 0 0px 0 0;*/
      }

      ::v-deep.el-date-editor .el-range-input,
      .el-date-editor .el-range-separator {
        height: 100%;
        width: 70px;
        margin: 0;
        text-align: center;
        display: inline-block;
        font-size: 11px;
      }

      ::v-deep.el-form-item__content {
        line-height: 40px;
        position: relative;
        font-size: 11px;
      }
    }
    .upload-btn {
      width: 192px;
      height: 31px;
      background: #4895ef;
      border-radius: 16px;
      font-size: 14px;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #ffffff;
      padding-top: 7px;
      margin-left: 36px;
      ::v-deep.svg-cls {
        width: 1em;
        vertical-align: -0.25em;
        fill: currentColor;
        overflow: hidden;
      }
    }
    .input-others {
      height: 23px;
      background: #ffffff;
      border-radius: 12px;
      border: 1px solid #ccdbf5;
    }
    .right-add {
      font-size: 18px;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #333333;
      line-height: 25px;
      margin-right: 10px;
    }
    ::v-deep.svg-cls {
      width: 1em;
      vertical-align: -0.25em;
      fill: currentColor;
      overflow: hidden;
    }

    .right-num {
      height: 25px;
      font-size: 18px;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #4895ef;
      line-height: 25px;
    }
    ::v-deep.el-progress-bar__outer {
      height: 6px;
      border-radius: 100px;
      background-color: #ebeef5;
      overflow: hidden;
      position: relative;
      vertical-align: middle;
    }
    /deep/.el-progress-bar {
      padding-right: 20px;
      width: 100%;
      margin-right: -55px;
      -webkit-box-sizing: border-box;
      box-sizing: border-box;
    }
    /deep/.el-progress.is-warning .el-progress-bar__inner {
      background-color: #fdc500;
    }
    /deep/.el-progress__text i {
      vertical-align: middle;
      display: none;
    }
  }
  .resume-box-edit {
    width: 640px;
    height: auto;
    background: #f6f9fc;
    .tag-title {
      font-size: 12px;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      vertical-align: text-top;
    }

    .intro-text {
      width: 500px;
      height: auto;
      background: #ffffff;
      border-radius: 12px;
      border: 1px solid #ccdbf5;
      ::v-deep.el-textarea__inner {
        min-height: 150px;
        height: 150px;
        /* width: 500px; */
        border-radius: 12px;
        border: 0px solid #ccdbf5;
      }
      ::v-deep.ql-toolbar.ql-snow {
        border: 0px solid #ccc;
        -webkit-box-sizing: border-box;
        box-sizing: border-box;
        font-family: "Helvetica Neue", "Helvetica", "Arial", sans-serif;
        padding: 8px;
      }

      ::v-deep.ql-container.ql-snow {
        border: 0px;
      }
    }
    .resume-languageinfo1 {
      padding-left: 18px;
      .tag-title {
        font-size: 12px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        vertical-align: text-top;
      }
      ::v-deep.el-tag--light {
        height: 21px;
        font-size: 12px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #ffffff;
        background: #4cc9f0;
        border-color: #4cc9f0;
        border-radius: 5px;
        line-height: 10px;
      }
    }

    .tag-title-1 {
      font-size: 12px;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      color: #999999;
    }

    .btn1 {
      width: 75px;
      height: 23px;
      background: #4895ef;
      border-radius: 12px;
      border: 1px solid #4895ef;
      font-size: 12px;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #ffffff;
      line-height: 2px;
    }

    .btn2 {
      width: 75px;
      height: 23px;
      background: #ffffff;
      border-radius: 12px;
      border: 1px solid #4895ef;
      font-size: 12px;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #4895ef;
      line-height: 2px;
    }

    .n-input-text-width {
      font-size: 12px;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #333333;

      ::v-deep .el-input {
        position: relative;
        font-size: 14px;
        display: inline-block;
        width: 173px;
      }

      ::v-deep .el-input__inner {
        width: 173px;
        height: 23px;
        background: #ffffff;
        border-radius: 12px;
        border: 1px solid #ccdbf5;
        font-size: 11px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #999999;
      }

      ::v-deep .el-form-item__label {
        width: 80px;
        font-size: 12px;
        font-weight: 500;
        color: #333333;
        line-height: 36px;
        padding: 0px;
        text-align: center;
      }
    }

    .m1-input-text-width {
      font-size: 12px;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #333333;
      ::v-deep .el-form-item__error {
        color: #f56c6c;
        font-size: 12px;
        line-height: 1;
        padding-top: 4px;
        position: absolute;
        top: 100%;
      }

      ::v-deep .el-date-editor .el-range-separator {
        padding: 0 5px;
        line-height: 14px;
        width: 5%;
        color: #303133;
      }

      ::v-deep .el-input__icon {
        height: 100%;
        width: 25px;
        text-align: center;
        -webkit-transition: all 0.3s;
        transition: all 0.3s;
        line-height: unset;
        color: #4895ef;
      }

      ::v-deep .el-input {
        position: relative;
        font-size: 14px;
        display: inline-block;
        width: 173px;
      }

      ::v-deep .el-form-item__label {
        width: 80px;
        font-size: 12px;
        font-weight: 500;
        color: #333333;
        line-height: 36px;
        /*padding: 0 0px 0 0;*/
      }

      ::v-deep.el-form-item__content {
        line-height: 40px;
        position: relative;
        font-size: 11px;
      }

      ::v-deep.ql-bubble {
        width: 512px;
        height: 94px;
        background: #ffffff;
        border-radius: 12px;
        border: 1px solid #ccdbf5;
      }

      ::v-deep .ql-container .ql-editor {
        min-height: 93px;
        font-size: 15px;
      }
    }
    .m2-input-text-width {
      font-size: 12px;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #333333;
      ::v-deep.el-form-item__error {
        color: #f56c6c;
        font-size: 12px;
        line-height: 0px;
        padding-top: 4px;
        position: absolute;
        top: 90%;
      }

      .ej-btn {
        width: 75px;
        height: 23px;
        border-radius: 12px;
        border: 1px solid #4895ef;
        font-size: 12px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #4895ef;
        line-height: 1px;
      }

      ::v-deep .el-date-editor .el-range-separator {
        padding: 0 5px;
        line-height: 14px;
        width: 5%;
        color: #303133;
      }

      ::v-deep .el-input__icon {
        height: 100%;
        width: 25px;
        text-align: center;
        -webkit-transition: all 0.3s;
        transition: all 0.3s;
        line-height: unset;
        color: #4895ef;
      }

      ::v-deep .el-input {
        position: relative;
        font-size: 14px;
        display: inline-block;
        width: 173px;
      }

      ::v-deep .el-input__inner {
        width: 173px;
        height: 23px;
        background: #ffffff;
        border-radius: 12px;
        border: 1px solid #ccdbf5;
        font-size: 11px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #999999;
      }

      ::v-deep .el-form-item__label {
        width: 80px;
        font-size: 12px;
        font-weight: 500;
        color: #333333;
        line-height: 36px;
        /*padding: 0 0px 0 0;*/
      }

      ::v-deep.el-date-editor .el-range-input,
      .el-date-editor .el-range-separator {
        height: 100%;
        width: 70px;
        margin: 0;
        text-align: center;
        display: inline-block;
        font-size: 11px;
      }

      ::v-deep.el-form-item__content {
        line-height: 40px;
        position: relative;
        font-size: 11px;
      }
    }
    .m-input-text-width {
      font-size: 12px;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #333333;
      ::v-deep.el-form-item__error {
        color: #f56c6c;
        font-size: 12px;
        line-height: 0px;
        padding-top: 4px;
        position: absolute;
        top: 90%;
        left: 0;
      }

      .ej-btn {
        width: 75px;
        height: 23px;
        border-radius: 12px;
        border: 1px solid #4895ef;
        font-size: 12px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #4895ef;
        line-height: 1px;
      }

      ::v-deep .el-date-editor .el-range-separator {
        padding: 0 5px;
        line-height: 14px;
        width: 5%;
        color: #303133;
      }

      ::v-deep .el-input__icon {
        height: 100%;
        width: 25px;
        text-align: center;
        -webkit-transition: all 0.3s;
        transition: all 0.3s;
        line-height: unset;
        color: #4895ef;
      }

      ::v-deep .el-input {
        position: relative;
        font-size: 14px;
        display: inline-block;
        width: 173px;
      }

      ::v-deep .el-input__inner {
        width: 173px;
        height: 23px;
        background: #ffffff;
        border-radius: 12px;
        border: 1px solid #ccdbf5;
        font-size: 11px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #999999;
      }

      ::v-deep .el-form-item__label {
        width: 80px;
        font-size: 12px;
        font-weight: 500;
        color: #333333;
        line-height: 36px;
        /*padding: 0 0px 0 0;*/
      }

      ::v-deep.el-date-editor .el-range-input,
      .el-date-editor .el-range-separator {
        height: 100%;
        width: 70px;
        margin: 0;
        text-align: center;
        display: inline-block;
        font-size: 11px;
      }

      ::v-deep.el-form-item__content {
        line-height: 40px;
        position: relative;
        font-size: 11px;
      }
    }

    .politic-title {
      font-size: 12px;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #333333;
    }

    .politic-sel {
      width: 175px;
      height: 23px;

      ::v-deep .el-input {
        position: relative;
        font-size: 14px;
        display: inline-block;
      }

      ::v-deep .el-input__inner {
        width: 175px;
        height: 23px;
        background: #ffffff;
        border-radius: 12px;
        border: 1px solid #ccdbf5;
        font-size: 11px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #999999;
      }

      ::v-deep .el-input__suffix {
        right: 5px;
        -webkit-transition: all 0.3s;
        transition: all 0.3s;
        height: 32px;
      }
    }

    .radio-gender {
      ::v-deep.el-radio-group {
        padding-top: 10px;
        font-size: 0;
      }
      ::v-deep .el-radio-button__inner .el-radio-group {
        display: inline-block;
        line-height: 1;
        vertical-align: baseline;
        padding-left: 29px;
      }
      ::v-deep .el-radio {
        color: #606266;
        cursor: pointer;
        margin-right: 10px;
      }
      ::v-deep.el-form-item__error {
        color: #f56c6c;
        font-size: 12px;
        line-height: 0px;
        padding-top: 0px;
        position: absolute;
        left: -6px;
      }
    }
  }
  .resume-box {
    margin-bottom: 21px;
    width: 640px;
    ::v-deep.svg-cls {
      width: 1em;
      vertical-align: -0.25em;
      fill: currentColor;
      overflow: hidden;
    }
    .resume-box-text {
      font-size: 16px;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #333333;
      line-height: 22px;
    }

    .resume-box-text-title {
      font-size: 14px;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #333333;
      line-height: 20px;
    }

    .resume-box-text-data {
      font-size: 14px;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      color: #333333;
      line-height: 20px;
    }

    .resume-box-text-html {
      /*overflow:auto;*/
      display: inline-block;
      word-wrap: break-word;
      width: 516px;
      font-size: 14px;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      color: #666666;
      line-height: 20px;
    }
  }
  .resume-languageinfo {
    padding-left: 18px;
    display: inline-flex;

    .resume-introduction {
      display: inline-block;
      word-wrap: break-word;
      width: 548px;
      font-size: 14px;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      color: #666666;
      line-height: 20px;
    }
  }
  .resume-eduinfo {
    display: inline-flex;

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
      display: inline-flex;
      ::v-deep.el-tag--light {
        height: 21px;
        font-size: 12px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #ffffff;
        background: #4cc9f0;
        border-color: #4cc9f0;
        border-radius: 5px;
        line-height: 10px;
        margin: 5px;
        cursor: pointer;
      }

      .info-other {
        height: auto;
        font-size: 14px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #333333;
        padding-top: 10px;
      }

      .info-data {
        font-size: 14px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #333333;
        padding-top: 10px;
      }
      .edu-box {
        display: inline-flex;
      }

      .edu-box-l {
        width: 346px;
        .edu-school {
          font-size: 16px;
          font-family: PingFangSC-Medium, PingFang SC;
          font-weight: 500;
          color: #333333;
          line-height: 22px;
          height: 22px;
        }
        .info-other {
          height: auto;
          font-size: 14px;
          font-family: PingFangSC-Medium, PingFang SC;
          font-weight: 500;
          color: #333333;
          padding-top: 10px;
        }
      }

      .edu-box-l1 {
        width: 346px;

        .edu-school {
          font-size: 16px;
          font-family: PingFangSC-Medium, PingFang SC;
          font-weight: 500;
          color: #333333;
          line-height: 22px;
          height: 22px;
        }
        .info-other {
          height: auto;
          font-size: 14px;
          font-family: PingFangSC-Medium, PingFang SC;
          font-weight: 500;
          color: #333333;
          padding-top: 10px;
        }
      }

      .edu-box-m {
        margin-top: 24px;
        width: 250px;
        margin-right: 20px;
        .info-other {
          height: auto;
          font-size: 14px;
          font-family: PingFangSC-Medium, PingFang SC;
          font-weight: 500;
          color: #333333;
          padding-top: 10px;
        }
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
          padding-top: 10px;
        }
      }

      .resume-editbox {
        padding-top: 30px;
      }
    }
  }
  .el-input .el-select {
    width: 120px;
  }
  ::v-deep .el-tag--dark {
    height: 21px;
    font-size: 12px;
    font-family: PingFangSC-Medium, PingFang SC;
    font-weight: 500;
    color: #ffffff;
    background: #4cc9f0;
    border-color: #4cc9f0;
    border-radius: 5px;
    line-height: 20px;
    margin: 5px;
  }
  ::v-deep.el-tag--plain {
    height: 21px;
    background: #ffffff;
    border-radius: 5px;
    border: 1px solid #4cc9f0;
    font-size: 12px;
    font-family: PingFangSC-Medium, PingFang SC;
    font-weight: 500;
    color: #4cc9f0;
    line-height: 20px;
    margin: 5px;
  }
  .btn-preview {
    width: 100%;
    font-size: 18px;
  }
}
</style>
<style lang="scss">
@import "bootstrap/scss/bootstrap.scss";
</style>
