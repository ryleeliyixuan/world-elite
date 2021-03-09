<template>
  <div class="app-container" v-loading="!getResumePageList">
    <div class="left-container content-box">
      <el-tabs
        class="menu"
        v-model="activeTabName"
        type="card"
        editable
        @edit="handleTabsEdit"
        @tab-click="handleTabClick"
      >
        <el-tab-pane
          v-for="item in resumePageList"
          :key="item.id"
          :label="item.title"
          :name="item.id"
        >
        </el-tab-pane>
        <div>
          <div class="required mt-2" v-show="resumeIncompleted">
            <span class="required-text"
              >还有必填没有填完，此简历将无法投递和被HR搜索到哦。</span
            >
            <span
              ><el-link class="required-link" @click="editRequiredField()"
                >去填写</el-link
              ></span
            >
          </div>
          <div class="small bold mt-2 d-flex justify-content-between">
            <div>
              <span
                >简历名称：
                <span v-show="!showEditTitle">{{ curResume.title }}</span>
                <svg-icon
                  v-if="!showEditTitle"
                  class="svg-cls"
                  icon-class="edit"
                  style="
                    width: 18px;
                    height: 19px;
                    margin-left: 5px;
                    margin-right: 50px;
                  "
                  @click="
                    (showEditTitle = true), (resumeTitle = curResume.title)
                  "
                ></svg-icon>
                <el-input
                  size="mini"
                  style="width: 150px"
                  v-else
                  v-model="resumeTitle"
                  placeholder="请输入简历名称"
                  maxlength="10"
                  show-word-limit
                  @keyup.enter.native="saveResumeTitle()"
                  @blur="showEditTitle = false"
                ></el-input>
              </span>
            </div>
            <span>更新时间：{{ curResume.updateTime }}</span>
            <!-- 隐藏优先级 -->
            <!-- <span>
              <el-tooltip placement="top" width="200" effect="dark">
                <div slot="content">
                  HR在搜索简历时只会搜索到最符合搜索条件的那一版简历。<br />但是当你有多版简历同等程度满足HR搜索条件时，<br />优先级更高的简历会出现在HR面前。<br />注：在简历名称处拖拽简历顺序也可调整优先级。
                </div>
                <svg-icon
                  style="vertical-align: middle"
                  icon-class="help-mark"
                ></svg-icon>
              </el-tooltip>
              <span
                class="resume-updateTime"
                style="padding-left: 8px; padding-right: 9px"
                >优先级 <span style="color: #ff1744">*</span>：</span
              >
              <el-select
                class="priority-selector"
                v-model="curResume.priority"
                placeholder="请选择"
                @change="onChangePriority"
              >
                <el-option
                  :label="dict.name"
                  :value="dict.value"
                  v-for="dict in priorityList"
                  :key="dict.value"
                />
              </el-select>
            </span> -->
          </div>
          <!-- 基本信息 -->
          <div class="resume-module mt-4">
            <div class="line1 mb-2">
              <div>
                <div class="bullet-point"></div>
                <span class="large bold">基本信息</span
                ><span class="star large bold">*</span>
              </div>
              <svg-icon
                v-show="!showEditBasic"
                class="svg-cls"
                icon-class="edit"
                style="width: 18px; height: 19px"
                @click="handleEditBasic(curResume)"
              ></svg-icon>
            </div>
            <!-- 展示 -->
            <div v-if="!showEditBasic" class="line2">
              <upload-img
                class="uploader"
                :src="curResume.avatar"
                @upload="onUpload"
              />
              <div class="content">
                <div
                  class="bold mb-1"
                  style="font-size: 21px; margin-top: -10px"
                >
                  {{ curResume.name }}
                </div>
                <div class="entry-line">
                  <div class="entry">
                    <span class="bold small">性别：</span
                    ><span v-if="curResume.gender" class="light">{{
                      curResume.gender == 1 ? "男" : "女"
                    }}</span>
                  </div>
                  <div class="entry">
                    <span class="bold small">政治面貌：</span
                    ><span v-if="curResume.maritalStatusName" class="light">{{
                      curResume.maritalStatusName == "未选择"
                        ? ""
                        : curResume.maritalStatusName
                    }}</span>
                  </div>
                </div>
                <div class="entry-line">
                  <div class="entry">
                    <span class="bold small">年龄：</span
                    ><span class="light">{{ curResume.age }} 岁</span>
                  </div>
                  <div class="entry">
                    <span class="bold small">现居地址：</span
                    ><span v-if="curResume.curPlace" class="light">{{
                      curResume.curPlace
                    }}</span>
                  </div>
                </div>
                <div class="entry-line">
                  <div class="entry">
                    <span class="bold small">邮箱：</span
                    ><span v-if="curResume.email" class="light">{{
                      curResume.email
                    }}</span>
                  </div>
                  <div class="entry">
                    <span class="bold small">入职时间：</span
                    ><span v-if="curResume.returnTime" class="light">{{
                      curResume.returnTime
                    }}</span>
                  </div>
                </div>
                <div class="entry-line">
                  <div class="entry">
                    <span class="bold small">手机：</span
                    ><span v-if="curResume.phone" class="light">{{
                      curResume.phone
                    }}</span>
                  </div>
                  <div class="entry"></div>
                </div>
              </div>
            </div>
            <!-- 编辑 -->
            <div v-else class="edit-box">
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
              </el-form>
              <div class="footer">
                <el-button class="confirm" @click="saveResumeBasic()"
                  >保存</el-button
                >
                <el-button class="cancel" @click="showEditBasic = false"
                  >取消</el-button
                >
              </div>
            </div>
          </div>
          <el-divider></el-divider>
          <!-- 教育经历 -->
          <div class="resume-module">
            <div class="line1">
              <div>
                <div class="bullet-point"></div>
                <span class="large bold">教育经历</span
                ><span class="star large bold">*</span>
              </div>
              <svg-icon
                v-show="!showEditEdu"
                icon-class="edu-add"
                style="width: 19px; height: 19px"
                @click="showEditEdu = true"
              ></svg-icon>
            </div>
            <!-- 展示 -->
            <div
              v-if="!showEditEdu"
              class="line2 mt-3"
              style="display: inline-block"
            >
              <div
                class="content d-flex"
                v-for="resumeEdu in curResume.resumeEduList"
                :key="resumeEdu.id"
              >
                <div class="text-section">
                  <div class="bold mb-1 regular">
                    {{ resumeEdu.schoolName }}
                  </div>
                  <div class="entry-line">
                    <div class="entry">
                      <span class="bold small">学历：</span
                      ><span v-if="resumeEdu.degree.name" class="light">{{
                        resumeEdu.degree.name
                      }}</span>
                    </div>
                    <div class="entry">
                      <span class="bold small">在校时间：</span
                      ><span
                        v-if="resumeEdu.startTime && resumeEdu.finishTime"
                        class="light"
                        >{{
                          resumeEdu.startTime == resumeEdu.finishTime
                            ? "在读"
                            : `${resumeEdu.startTime}到${resumeEdu.finishTime}`
                        }}</span
                      >
                    </div>
                  </div>
                  <div class="entry-line">
                    <div class="entry">
                      <span class="bold small">专业：</span
                      ><span class="light" v-if="resumeEdu.majorName">{{
                        resumeEdu.majorName
                      }}</span>
                    </div>
                    <div class="entry">
                      <span class="bold small">GPA：</span
                      ><span class="light">{{ resumeEdu.gpa }}</span>
                    </div>
                  </div>
                </div>
                <div class="button-section">
                  <svg-icon
                    icon-class="edit"
                    style="width: 18px; height: 19px; margin-bottom: 12px"
                    @click="handleEditEdu(resumeEdu)"
                  ></svg-icon>
                  <svg-icon
                    icon-class="edu-del"
                    style="width: 19px; height: 21px"
                    @click="handleDeleteItemById(`edu`, resumeEdu.id)"
                  ></svg-icon>
                </div>
              </div>
            </div>
            <!-- 编辑 -->
            <div v-else class="edit-box">
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
                      prop="workingTimeFlag"
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
                        @change="checkEduWorkingTimeFlag()"
                      />
                      <el-checkbox
                        v-model="schoolCheck"
                        :true-label="1"
                        :false-label="0"
                        class="m-input-text-width"
                        @change="checkEduWorkingTimeFlag()"
                        >在读
                      </el-checkbox>
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
                        max="4"
                      ></el-input>
                    </el-form-item>
                  </div>
                </div>
              </el-form>
              <div class="footer">
                <el-button class="confirm" @click="saveResumeEdu()"
                  >保存</el-button
                >
                <el-button class="cancel" @click="cancelEditEdu()"
                  >取消</el-button
                >
              </div>
            </div>
          </div>
          <el-divider></el-divider>
          <!-- 求职意向 -->
          <div class="resume-module">
            <div class="line1">
              <div>
                <div class="bullet-point"></div>
                <span class="large bold">求职意向</span
                ><span class="star large bold">*</span>
              </div>
              <svg-icon
                v-show="!showEditJobOri"
                icon-class="edit"
                style="width: 18px; height: 19px"
                @click="handleEditExpectJob()"
              ></svg-icon>
            </div>
            <!-- 展示 -->
            <div
              v-if="!showEditJobOri && curResume.userExpectJob"
              class="line2 mt-3"
            >
              <div class="content d-flex">
                <div class="text-section">
                  <div class="bold mb-1 regular">
                    {{ curResume.userExpectJob.expectPosition }}
                  </div>
                  <div class="entry-line">
                    <div class="entry">
                      <span class="bold small">预期薪资：</span
                      ><span
                        v-if="curResume.userExpectJob.salaryId"
                        class="light"
                        >{{ salaryNameList }}</span
                      >
                    </div>
                    <div class="entry">
                      <span class="bold small">期望行业：</span
                      ><span
                        v-if="curResume.userExpectJob.industry"
                        class="light"
                        >{{ curResume.userExpectJob.industry }}</span
                      >
                    </div>
                  </div>
                  <div class="entry-line">
                    <div class="entry">
                      <span class="bold small">工作类型：</span
                      ><span
                        class="light"
                        v-if="curResume.userExpectJob.expectWorkType"
                        >{{ curResume.userExpectJob.expectWorkType }}</span
                      >
                    </div>
                    <div class="entry">
                      <span class="bold small">工作城市：</span
                      ><span
                        class="light"
                        v-if="curResume.userExpectJob.expectCity"
                        >{{ curResume.userExpectJob.expectCity }}</span
                      >
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <!-- 编辑 -->
            <div v-else class="edit-box">
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
                      <el-cascader
                        class="section-input-box"
                        placeholder="意向工作城市"
                        :show-all-levels="true"
                        :options="cityOptions"
                        :props="cityIdProps"
                        clearable
                        filterable
                        v-model="expectJobForm.expectCity"
                      >
                      </el-cascader>
                    </el-form-item>
                  </div>
                </div>
                <div style="display: flex; height: 30px; padding-left: 10px">
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
                    <el-form-item label="薪资范围:" class="m-input-text-width">
                      <el-select
                        multiple
                        collapse-tags
                        v-model="expectJobForm.salaryId"
                        placeholder="薪资范围"
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
                    <el-form-item label="期望行业:" class="m-input-text-width">
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
                </div>
              </el-form>
              <div class="footer">
                <el-button class="confirm" @click="saveUserExpectJob()"
                  >保存</el-button
                >
                <el-button class="cancel" @click="showEditJobOri = false"
                  >取消</el-button
                >
              </div>
            </div>
          </div>
          <el-divider></el-divider>
          <!-- 工作/实习经历 -->
          <div class="resume-module">
            <div class="line1">
              <div>
                <div class="bullet-point"></div>
                <span class="large bold">工作/实习经历</span>
              </div>
              <svg-icon
                v-show="!showEditWork"
                icon-class="edu-add"
                style="width: 19px; height: 19px"
                @click="showEditWork = true"
              ></svg-icon>
            </div>
            <!-- 展示 -->
            <div
              v-if="!showEditWork"
              class="line2 mt-3"
              style="display: inline-block"
            >
              <div
                class="content d-flex"
                v-for="resumeExp in curResume.resumeExpList"
                :key="resumeExp.id"
              >
                <div class="text-section">
                  <div class="bold mb-1 regular">
                    <span class="mr-4">{{ resumeExp.company }}</span>
                    <span class="mr-4">{{ resumeExp.post }}</span>
                    <span class="mr-4">{{ resumeExp.workType }}</span>
                  </div>
                  <div class="entry-line">
                    <div class="entry">
                      <span class="bold small">行业：</span
                      ><span v-if="resumeExp.industry" class="light">{{
                        resumeExp.industry
                      }}</span>
                    </div>
                    <div class="entry">
                      <span class="bold small">在职时间：</span
                      ><span class="light">{{
                        resumeExp.onWork == 1
                          ? "在职"
                          : `${resumeExp.startTime}到${resumeExp.finishTime}`
                      }}</span>
                    </div>
                  </div>
                  <div class="entry-line">
                    <div class="entry" style="width: 100%">
                      <span class="bold small">工作描述：</span
                      ><span class="light" v-if="resumeExp.description">
                        {{ resumeExp.description }}
                      </span>
                    </div>
                  </div>
                </div>
                <div class="button-section">
                  <svg-icon
                    icon-class="edit"
                    style="width: 18px; height: 19px; margin-bottom: 12px"
                    @click="handleEditExp(resumeExp)"
                  ></svg-icon>
                  <svg-icon
                    icon-class="edu-del"
                    style="width: 19px; height: 21px"
                    @click="handleDeleteItemById(`exp`, resumeExp.id)"
                  ></svg-icon>
                </div>
              </div>
            </div>
            <div v-else class="edit-box">
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
                      prop="workingTimeFlag"
                      class="m-input-text-width"
                    >
                      <el-date-picker
                        v-model="resumeExpForm.workingDates"
                        type="monthrange"
                        value-format="yyyy-MM"
                        start-placeholder="入职时间"
                        end-placeholder="离职时间"
                        :disabled="resumeExpForm.onWork == 1"
                        @change="checkExpWorkingTimeFlag()"
                      />
                      <el-checkbox
                        v-model="resumeExpForm.onWork"
                        :true-label="1"
                        :false-label="0"
                        class="m-input-text-width"
                        @change="checkExpWorkingTimeFlag()"
                        >在职
                      </el-checkbox>
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
                    <el-form-item label="行业类型:" class="m-input-text-width">
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
              </el-form>
              <div class="footer">
                <el-button class="confirm" @click="saveResumeExp()"
                  >保存</el-button
                >
                <el-button class="cancel" @click="cancelEditExp()"
                  >取消</el-button
                >
              </div>
            </div>
          </div>
          <el-divider></el-divider>
          <!-- 项目/其它经历 -->
          <div class="resume-module">
            <div class="line1">
              <div>
                <div class="bullet-point"></div>
                <span class="large bold">项目/其它经历</span>
              </div>
              <svg-icon
                v-show="!showEditProj"
                icon-class="edu-add"
                style="width: 19px; height: 19px"
                @click="showEditProj = true"
              ></svg-icon>
            </div>
            <!-- 展示 -->
            <div
              v-if="!showEditProj"
              class="line2 mt-3"
              style="display: inline-block"
            >
              <div
                class="content d-flex"
                v-for="practice in curResume.resumePracticeList"
                :key="practice.id"
              >
                <div class="text-section">
                  <div class="mb-1">
                    <span class="regular bold mr-4">{{ practice.title }}</span>
                    <span class="small">{{
                      practice.onWork == 1
                        ? "项目进行中"
                        : `${practice.startTime}到${practice.finishTime}`
                    }}</span>
                  </div>
                  <div class="entry-line">
                    <div class="entry" style="width: 100%">
                      <span class="bold small">介绍：</span
                      ><span class="light" v-if="practice.description">
                        {{ practice.description }}
                      </span>
                    </div>
                  </div>
                </div>
                <div class="button-section">
                  <svg-icon
                    icon-class="edit"
                    style="width: 18px; height: 19px; margin-bottom: 12px"
                    @click="handleEditPractice(practice)"
                  ></svg-icon>
                  <svg-icon
                    icon-class="edu-del"
                    style="width: 19px; height: 21px"
                    @click="handleDeleteItemById(`practice`, practice.id)"
                  ></svg-icon>
                </div>
              </div>
            </div>
            <div v-else class="edit-box">
              <el-form
                ref="resumePracticeForm"
                :model="resumePracticeForm"
                :rules="resumePracticeFormRules"
              >
                <div class="d-flex">
                  <el-form-item
                    label="项目名称："
                    prop="title"
                    class="m-input-text-width"
                    label-width="85px"
                  >
                    <el-input
                      v-model="resumePracticeForm.title"
                      placeholder="请填写项目名称"
                      :maxlength="50"
                      show-word-limit
                    ></el-input>
                  </el-form-item>
                  <div style="display: content">
                    <el-form-item
                      label="时间："
                      prop="workingTimeFlag"
                      label-width="85px"
                      class="m-input-text-width"
                    >
                      <el-date-picker
                        type="monthrange"
                        value-format="yyyy-MM"
                        start-placeholder="开始时间"
                        end-placeholder="结束时间"
                        v-model="resumePracticeForm.workingDates"
                        :disabled="resumePracticeForm.onWork == 1"
                        @change="checkPracWorkingTimeFlag()"
                      ></el-date-picker>
                      <el-checkbox
                        v-model="resumePracticeForm.onWork"
                        :true-label="1"
                        :false-label="0"
                        class="m-input-text-width"
                        style="margin-left: 10px"
                        @change="checkPracWorkingTimeFlag()"
                        >进行中
                      </el-checkbox>
                    </el-form-item>
                  </div>
                </div>
                <el-form-item
                  label="项目介绍："
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
              </el-form>
              <div class="footer">
                <el-button class="confirm" @click="saveResumePractice()"
                  >保存</el-button
                >
                <el-button class="cancel" @click="cancelEditPractice()"
                  >取消</el-button
                >
              </div>
            </div>
          </div>
          <el-divider></el-divider>
          <!-- 语言能力 -->
          <div class="resume-module">
            <div class="line1">
              <div>
                <div class="bullet-point"></div>
                <span class="large bold">语言能力</span>
              </div>
              <svg-icon
                v-show="!showEditLang"
                icon-class="edu-add"
                style="width: 19px; height: 19px"
                @click="showEditLang = true"
              ></svg-icon>
            </div>
            <!-- 展示 -->
            <div
              v-if="!showEditLang"
              class="line2 mt-3"
              style="display: inline-block"
            >
              <div
                class="content d-flex"
                v-for="(language, index) in curResume.resumeLanguageList"
                :key="index"
              >
                <div class="text-section">
                  <div class="entry-line">
                    <div class="entry">
                      <span class="bold small">语种{{ index + 1 }}：</span
                      ><span class="light" v-if="language.title">
                        {{ language.title }}
                      </span>
                    </div>
                    <div class="entry">
                      <span class="bold small">证书或分数：</span
                      ><span class="light" v-if="language.description">
                        {{ language.description }}
                      </span>
                    </div>
                  </div>
                </div>
                <div class="button-section">
                  <!--  v-on:click="handleDelResumeEdu(resumeEdu.id)" -->
                  <svg-icon
                    icon-class="edit"
                    style="width: 18px; height: 19px; margin-bottom: 12px"
                    @click="
                      (resumeLanguageForm.description = language.description),
                        (resumeLanguageForm.title = language.title),
                        (resumeLanguageForm.id = language.id),
                        (showEditLang = true)
                    "
                  ></svg-icon>
                  <svg-icon
                    icon-class="edu-del"
                    style="width: 19px; height: 21px"
                    @click="handleDeleteItemById(`language`, language.id)"
                  ></svg-icon>
                </div>
              </div>
            </div>
            <div v-else class="edit-box">
              <el-form
                ref="resumeLanguageForm"
                :model="resumeLanguageForm"
                :rules="resumeLanguageFormRules"
                class="d-flex"
              >
                <el-form-item
                  label="语种:"
                  prop="title"
                  label-width="130px"
                  class="m-input-text-width"
                >
                  <el-input
                    v-model="resumeLanguageForm.title"
                    placeholder="请填写语种名称"
                    :maxlength="50"
                  ></el-input>
                </el-form-item>
                <el-form-item
                  label="证书或分数："
                  prop="description"
                  label-width="130px"
                  class="m-input-text-width"
                >
                  <el-input
                    v-model="resumeLanguageForm.description"
                    placeholder="请填写证书或分数"
                  ></el-input>
                </el-form-item>
              </el-form>
              <div class="footer">
                <el-button class="confirm" @click="saveResumeLanguage()"
                  >保存</el-button
                >
                <el-button class="cancel" @click="cancelEditLanguage()"
                  >取消</el-button
                >
              </div>
            </div>
          </div>
          <el-divider></el-divider>
          <!-- 证书/奖项 -->
          <div class="resume-module">
            <div class="line1">
              <div>
                <div class="bullet-point"></div>
                <span class="large bold">证书/奖项</span>
              </div>
              <svg-icon
                v-show="!showEditHonor"
                icon-class="edu-add"
                style="width: 19px; height: 19px"
                @click="showEditHonor = true"
              ></svg-icon>
            </div>
            <!-- 展示 -->
            <div
              v-if="!showEditHonor"
              class="line2 mt-3"
              style="display: inline-block"
            >
              <div
                class="content d-flex"
                v-for="(awards, index) in curResume.resumeCertificateList"
                :key="index"
              >
                <div class="text-section">
                  <div class="entry-line">
                    <div class="entry">
                      <span class="bold small">证书/奖项名称：</span
                      ><span class="light" v-if="awards.title">
                        {{ awards.title }}
                      </span>
                    </div>
                    <div class="entry">
                      <span class="bold small">获得时间：</span
                      ><span class="light" v-if="awards.time">
                        {{ awards.time }}
                      </span>
                    </div>
                  </div>
                </div>
                <div class="button-section">
                  <!--  v-on:click="handleDelResumeEdu(resumeEdu.id)" -->
                  <svg-icon
                    icon-class="edit"
                    style="width: 18px; height: 19px; margin-bottom: 12px"
                    @click="
                      (certForm.title = awards.title),
                        (certForm.time = awards.time),
                        (certForm.id = awards.id),
                        (showEditHonor = true)
                    "
                  ></svg-icon>
                  <svg-icon
                    icon-class="edu-del"
                    style="width: 19px; height: 21px"
                    @click="handleDeleteItemById('award', awards.id)"
                  ></svg-icon>
                </div>
              </div>
            </div>
            <!-- 编辑 -->
            <div v-else class="edit-box">
              <el-form
                :model="certForm"
                :rules="certFormRules"
                ref="certForm"
                class="d-flex"
              >
                <el-form-item
                  label="证书/奖项:"
                  prop="title"
                  label-width="130px"
                  class="m-input-text-width"
                >
                  <el-input
                    v-model="certForm.title"
                    placeholder="请填写证书/奖项名称"
                    :maxlength="50"
                  ></el-input>
                </el-form-item>
                <el-form-item
                  label=" 获得时间："
                  prop="time"
                  label-width="130px"
                  class="m-input-text-width"
                >
                  <el-date-picker
                    v-model="certForm.time"
                    :picker-options="oldDatePickerOptions"
                    type="date"
                    value-format="yyyy-MM-dd"
                    placeholder="请选择获得时间"
                  ></el-date-picker>
                </el-form-item>
              </el-form>
              <div class="footer">
                <el-button class="confirm" @click="saveResumeAwards()"
                  >保存</el-button
                >
                <el-button
                  class="cancel"
                  @click="
                    (certForm.title = undefined),
                      (certForm.time = undefined),
                      (certForm.id = undefined),
                      (showEditHonor = false)
                  "
                  >取消</el-button
                >
              </div>
            </div>
          </div>
          <el-divider></el-divider>
          <!-- 能力标签 -->
          <div class="resume-module">
            <div class="line1">
              <div>
                <div class="bullet-point"></div>
                <span class="large bold mr-2">能力标签</span>
                <span
                  class="small bold mr-1"
                  style="color: #4f69ed"
                  v-if="selectedSkillList && selectedSkillList.length > 0"
                  >{{ selectedSkillList.length }}/6</span
                >
                <span class="small bold mr-1" style="color: #4f69ed" v-else
                  >0/6</span
                >
                <el-tooltip
                  placement="right"
                  width="200"
                  effect="dark"
                  content="最多可显示6个标签"
                >
                  <svg-icon
                    style="vertical-align: baseline"
                    icon-class="resume_qmark"
                    class="mark-icon"
                  ></svg-icon>
                </el-tooltip>
              </div>
              <svg-icon
                v-show="!showEditSkillTag"
                icon-class="edit"
                style="width: 18px; height: 19px"
                @click="showEditSkillTag = true"
              ></svg-icon>
            </div>
            <!-- 展示 -->
            <div
              v-if="!showEditSkillTag"
              class="line2 mt-3"
              style="display: inline-block"
            >
              <div class="content">
                <el-tag
                  style="cursor: pointer"
                  v-for="skill in curResume.resumeSkillList"
                  :key="skill.id"
                  class="tag-icon"
                  >{{ skill.name }}
                </el-tag>
              </div>
            </div>
            <!-- 编辑 -->
            <div v-else class="edit-box">
              <div class="selected-tag-list">
                <span class="xsmall bold">已选择：</span>
                <el-tag
                  v-for="skill in selectedSkillList"
                  :key="skill.item.id"
                  style="height: 21px; border-radius: 5px; cursor: pointer"
                  >{{ skill.item.name }}
                </el-tag>
              </div>
              <div class="mt-2 xsmall bold">
                <span>为你推荐：</span>
                <span style="color: #999999">根据你的填写的内容智能推荐</span>
              </div>
              <div class="mt-2">
                <el-tag
                  style="cursor: pointer"
                  class="skill-tag"
                  v-for="tag in skillTagList"
                  :key="tag.item.name"
                  :effect="tag.selected ? 'dark' : 'plain'"
                  @click="onSelectSkillTag(tag)"
                  >{{ tag.item.name }}
                </el-tag>
              </div>
              <el-input
                v-model="newSkillTag"
                placeholder="新增标签"
                maxlength="6"
                class="m-input-text-width mt-2"
                style="width: 190px; padding-left: 5px"
                @keyup.enter.native="addNewSkillTag()"
              >
              </el-input>
              <svg-icon
                icon-class="resume-add"
                style="width: 19px; height: 18px"
                @click="addNewSkillTag()"
              ></svg-icon>
              <div class="footer mt-4">
                <el-button class="confirm" @click="saveSkillTag()"
                  >保存</el-button
                >
                <el-button class="cancel" @click="showEditSkillTag = false"
                  >取消</el-button
                >
              </div>
            </div>
          </div>
          <el-divider></el-divider>
          <!-- 自我介绍 -->
          <div class="resume-module">
            <div class="line1">
              <div>
                <div class="bullet-point"></div>
                <span class="large bold mr-2">自我介绍</span>
              </div>
              <svg-icon
                v-show="!showEditSelfIntro"
                icon-class="edit"
                style="width: 18px; height: 19px"
                @click="showEditSelfIntro = true"
              ></svg-icon>
            </div>
            <!-- 展示 -->
            <div
              v-if="!showEditSelfIntro"
              class="line2 mt-3 content small light"
            >
              {{ curResume.introduction }}
            </div>
            <!-- 编辑 -->
            <div v-else class="edit-box">
              <div class="left-right">
                <div class="bold xsmall" style="width: 60px">自我介绍：</div>
                <el-input
                  v-model="curResume.introduction"
                  type="textarea"
                  :row="3"
                  :maxlength="300"
                  show-word-limit
                  class="intro-text"
                  placeholder="简单介绍一下自己吧~"
                ></el-input>
              </div>
              <div class="footer mt-4">
                <el-button
                  class="confirm"
                  @click="saveSelfIntro(curResume.introduction)"
                  >保存</el-button
                >
                <el-button class="cancel" @click="showEditSelfIntro = false"
                  >取消</el-button
                >
              </div>
            </div>
          </div>
        </div>
      </el-tabs>
    </div>
    <div class="right-container content-box">
      <div class="button-section">
        <el-button class="button" @click="showPreview = true">
          <svg-icon
            class="svg-cls"
            icon-class="preview"
            style="width: 16px; height: 17px; margin-right: 2px"
          ></svg-icon
          >预览简历
        </el-button>
        <el-button class="button" @click="handleDelResume"
          ><svg-icon
            class="svg-cls"
            icon-class="resume-del"
            style="width: 16px; height: 17px; margin-right: 2px"
          ></svg-icon
          >简历删除
        </el-button>
      </div>
      <el-divider></el-divider>
      <div class="section2">
        <div class="line1">
          <span style="width: 55px" class="bold large">附件：</span>
          <span>
            <svg-icon
              icon-class="tips"
              style="
                width: 13px;
                height: 13px;
                vertical-align: middle;
                margin-right: 3px;
              "
            ></svg-icon>
          </span>
          <span class="light" style="font-size: 11px"
            >附件简历内容也可被HR搜索到！</span
          >
        </div>
        <div
          v-if="curResume.attachResume && curResume.attachResume.length > 0"
          class="mt-2 small light d-inline-flex"
        >
          <svg-icon
            class="mr-2"
            icon-class="PDF"
            style="width: 14px; height: 18px"
          ></svg-icon>
          {{ curResume.name }}的个人简历
        </div>
        <el-upload
          class="mt-2 mb-4 upload-attach-box"
          ref="upload"
          :limit="1"
          :action="uploadAttachmentOptions.action"
          :data="uploadAttachmentOptions.params"
          :accept="uploadAttachmentOptions.acceptFileType"
          :show-file-list="false"
          :on-success="handleUploadAttachSuccess"
          :before-upload="beforeAttachUpload"
        >
          <el-button class="upload-btn" v-loading="uploadAttachLoading">
            <svg-icon
              class="svg-cls"
              icon-class="upload"
              style="width: 18px; height: 15px"
            ></svg-icon>
            <span style="padding-left: 4px">上传附件简历</span></el-button
          >
        </el-upload>
        <div
          class="mb-2"
          v-for="(others, index) in curResume.resumeMergeAttachList"
          :key="others.id"
        >
          <div class="d-flex justify-content-between">
            <span class="overflow">
              <span>
                <svg-icon
                  class="mr-2"
                  icon-class="PDF"
                  style="width: 14px; height: 18px; vertical-align: sub"
                ></svg-icon>
              </span>
              <el-input
                v-if="
                  showEditOtherTitle == true &&
                  showEditOtherTitleIndex === index
                "
                size="mini"
                style="width: 125px"
                v-model="otherTitle"
                placeholder="请输入附件名称"
                @keyup.enter.native="saveOtherTitle(index)"
                @blur="showEditOtherTitle = false"
              ></el-input>
              <span v-else class="small bold">{{ others.name }}</span>
            </span>
            <span>
              <svg-icon
                icon-class="resumeOther-edit"
                style="width: 18px; height: 18px"
                @click="
                  (showEditOtherTitleIndex = index), (showEditOtherTitle = true)
                "
              ></svg-icon>
              <svg-icon
                icon-class="resumeOther-del"
                style="width: 18px; height: 20px"
                @click="
                  (cancelAttachDialogVisible = true),
                    (delAttachItemName = others.name),
                    (delAttachItemIndex = index)
                "
              ></svg-icon>
            </span>
          </div>
        </div>
        <el-upload
          class="mt-2 upload-attach-box"
          ref="upload"
          :limit="1"
          :action="uploadAttachmentOptions.action"
          :data="uploadAttachmentOptions.params"
          :show-file-list="false"
          :on-success="handleUploadAttachOthersSuccess"
          :before-upload="beforeAttachUpload"
          :file-list="fileList"
        >
          <el-button class="upload-btn" v-loading="uploadOtherAttachLoading">
            <svg-icon
              class="svg-cls"
              icon-class="upload"
              style="width: 18px; height: 15px"
            ></svg-icon>
            <span style="padding-left: 4px">上传其他附件</span></el-button
          >
        </el-upload>
      </div>
      <el-divider></el-divider>
      <div class="section3">
        <span class="bold large">简历完成度：</span>
        <span class="large bold" style="color: #4895ef"
          >{{ curResume.resumeCompleteProgress }}%</span
        >
        <el-progress
          class="mt-2"
          :stroke-width="14"
          :percentage="curResume.resumeCompleteProgress"
          :status="
            curResume.resumeCompleteProgress == 100 ? 'success' : 'warning'
          "
        ></el-progress>
      </div>
    </div>
    <!-- 简历预览 -->
    <el-dialog class="resume-preview" :visible.sync="showPreview" width="750px">
      <div class="d-flex justify-content-end">
        <svg-icon
          class="mr-1"
          icon-class="resumedownload"
          style="width: 29px; height: 20x"
          @click="exportPdf"
        ></svg-icon>
        <svg-icon
          icon-class="resume-close"
          style="width: 17px; height: 17x"
          @click="showPreview = false"
        ></svg-icon>
      </div>
      <ResumeView :resumeDetail="this.curResume"></ResumeView>
    </el-dialog>
    <!-- 是否删除附件 对话框 -->
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
  </div>
</template>

<script>
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
import { listByType, listByTypeSalary } from "@/api/dict_api";
import { saveUserExpectJob } from "@/api/user_api";
import { getUploadPicToken, getUploadAttachmentToken } from "@/api/upload_api";
import Toast from "@/utils/toast";
import { checkPicSize, checkAttachmentSize } from "@/utils/common";
import { exportResumeToPdf } from "@/api/export_api";
import ResumeView from "@/components/ResumeView";
import UploadImg from "@/components/Cropper/uploadImg";
import { downloadFile } from "@/utils/common";

export default {
  name: "EditResumePage",
  components: {
    UploadImg,
    ResumeView,
  },
  data() {
    return {
      uploadAttachLoading: false, // 简历上传加载
      uploadOtherAttachLoading: false, // 简历上传加载
      getResumePageList: false, // 是否获取简历列表
      resumeIncompleted: true, // 简历完整性
      resumePageList: [], // 简历列表
      activeTabIndex: 0, // 现处tab的index
      activeTabName: "", // 现处tab的name
      resumeId: undefined, // 现处简历的id
      curResume: {}, // 现处简历
      userId: undefined, // 用户id
      resumeTitle: "", // 编辑简历名称
      otherTitle: "", // 编辑其他附件名称
      showEditOtherTitleIndex: -1, // 编辑其他附件index

      salaryNameList: "", // 薪水id转化为名字列表
      salaryOptions: [], // 获取salaryOptions
      skillTagOptions: [], // 获取技能标签选项
      companyIndustryOptions: [], // 获取行业列表选项
      skillTagList: [], // 获取技能标签列表
      degreeList: [], // 学历选项
      cityOptions: [], // 城市选择框属性
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
      maritalStatusList: [
        {
          value: -1,
          id: 0,
          name: "未选择",
        },
        {
          value: 0,
          id: 1,
          name: "中共党员（含预备党员）",
        },
        {
          id: 2,
          value: 1,
          name: "民主党派",
        },
        {
          id: 3,
          value: 2,
          name: "无党派人士",
        },
        {
          id: 4,
          value: 3,
          name: "团员",
        },
        {
          id: 3,
          value: 4,
          name: "群众",
        },
      ],
      priorityList: [
        {
          value: 0,
          name: "第一",
        },
        {
          value: 1,
          name: "第二",
        },
        {
          value: 2,
          name: "第三",
        },
        {
          value: 3,
          name: "第四",
        },
      ],

      showPreview: false, // 预览

      // 能力标签
      selectedSkillList: [], // 已选
      newSkillTag: "",

      // 编辑框
      showEditOtherTitle: false, // 其他附件名称
      showEditBasic: false,
      showEditEdu: false,
      showEditJobOri: false,
      showEditWork: false,
      showEditProj: false,
      showEditLang: false,
      showEditHonor: false,
      showEditSkillTag: false,
      showEditSelfIntro: false,
      showEditTitle: false,
      // 取消对话框
      cancelAttachDialogVisible: false,
      delAttachItemName: "",
      delAttachItemIndex: undefined,

      // 上传附件选项
      fileList: [],
      uploadAttachmentOptions: {
        action: "",
        params: {},
        fileUrl: "",
        fileName: "",
        acceptFileType: ".pdf,.doc,docx",
      },
      // uploadOtherOptions: {
      //   action: "",
      //   params: {},
      //   fileUrl: "",
      //   acceptFileType: ".pdf,.doc,docx",
      // },

      // 基本信息
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
      // 教育经历
      schoolCheck: false,
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
        workingTimeFlag: undefined,
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
        workingTimeFlag: [
          { required: true, message: "请选择在校时间", trigger: "change" },
        ],
      },
      // 求职意向
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
      // 工作实习经历
      resumeExpForm: {
        id: undefined,
        resumeId: undefined,
        workingTimeFlag: undefined,
        workType: undefined,
        workingDates: undefined,
        startTime: undefined,
        finishTime: undefined,
        company: undefined,
        depart: undefined,
        post: undefined,
        description: undefined,
        onWork: undefined,
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
      // 项目/其它经历
      resumePracticeForm: {
        id: undefined,
        resumeId: undefined,
        workingDates: undefined,
        startTime: undefined,
        finishTime: undefined,
        title: undefined,
        post: undefined,
        description: undefined,
        workingTimeFlag: undefined,
        onWork: undefined,
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
      // 语言能力
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
      // 证书
      certForm: {
        id: undefined,
        resumeId: undefined,
        title: undefined,
        time: undefined,
      },
      certFormRules: {
        title: [
          { required: true, message: "请输入证书/奖项名称", trigger: "blur" },
        ],
        time: [{ required: true, message: "请输入获得时间", trigger: "blur" }],
      },

      // 时间组件选项
      oldDatePickerOptions: {
        disabledDate(time) {
          return time.getTime() >= Date.now() - 8.64e7;
        },
      },
    };
  },
  created() {
    this.initData();
  },
  methods: {
    // 初始化数据
    initData() {
      // 初始化预期薪资
      this.$axios
        .request({
          url: "/dict/list",
          method: "get",
          params: { type: 26, sort: "+id" },
        })
        .then((response) => {
          this.salaryOptions = response.data.list;
        });
      // 获取技能标签选项
      listByType(3).then((response) => {
        this.skillTagOptions = response.data.list;
      });
      // 获取行业选项
      listByType(6).then(
        (response) => (this.companyIndustryOptions = response.data.list)
      );
      // 获取学历选项
      listByType(25).then((response) => {
        let degreeOptions = response.data.list;
        this.degreeList = [];
        for (const item of degreeOptions) {
          if (
            item.name != "不限" &&
            item.name != "专科及以下" &&
            item.name != "MBA" &&
            item.name != "EMBA"
          ) {
            this.degreeList.push(item);
          }
        }
      });
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
      // 获取简历信息
      this.getResumeInfo(0); // 默认返回列表中的第一个为activetab
    },
    // 获取简历信息
    getResumeInfo(index) {
      // 关闭所有编辑框
      this.showEditBasic = false;
      this.showEditEdu = false;
      this.showEditJobOri = false;
      this.showEditWork = false;
      this.showEditProj = false;
      this.showEditLang = false;
      this.showEditHonor = false;
      this.showEditSkillTag = false;
      this.showEditSelfIntro = false;
      // 重置所有属性
      this.selectedSkillList = [];

      getResumeInfo()
        .then((response) => {
          if (response.data && response.data.length > 0) {
            console.log("&&&&&&&&", index);
            this.resumePageList = response.data;
            this.curResume = this.resumePageList[index];
            if (this.curResume) {
              this.activeTabName = this.curResume.id;
              this.resumeId = this.curResume.id;
              this.userId = this.resumePageList[0].userId;

              // 判断简历是否完整
              if (
                this.curResume.userExpectJob &&
                this.curResume.userExpectJob.category &&
                this.curResume.resumeEduList &&
                this.curResume.name != "" &&
                this.curResume.birth &&
                this.curResume.birth != "" &&
                this.curResume.email &&
                this.curResume.email != ""
              ) {
                this.resumeIncompleted = false;
              } else {
                this.resumeIncompleted = true;
              }

              //获取salaryId和salaryList
              let salaryNameArr = [];
              let salaryList = [];
              this.salaryNameList = [];
              this.expectJobForm.salaryId = [];
              if (
                this.curResume.userExpectJob &&
                this.curResume.userExpectJob.salaryId
              ) {
                let tempSalaryList = this.curResume.userExpectJob.salaryId;
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
              }

              // 获取完整的skillTagList = 自己的skill tag + skillTagOptions
              if (this.curResume.resumeSkillList) {
                this.selectedSkillList = this.curResume.resumeSkillList; // 已选skill tags
              } else {
                this.selectedSkillList = [];
              }
              let tempList = [];
              // 不在skillTagOptions中，但在selectedSkillList中 = 自己添加的tag
              tempList = this.selectedSkillList
                .filter(
                  (item) =>
                    !this.skillTagOptions.find(
                      (skill) => skill.name == item.name
                    )
                )
                .map((item) => {
                  return { item, selected: true };
                });
              // skillTagOptions中的所有tag
              this.skillTagOptions.forEach((item) => {
                if (
                  this.selectedSkillList.find(
                    (skill) => skill.name == item.name
                  )
                ) {
                  tempList.push({ item, selected: true });
                } else {
                  tempList.push({ item, selected: false });
                }
              });
              this.skillTagList = tempList;
              // 解构赋值 selectedSkillList
              this.selectedSkillList = this.selectedSkillList.map((item) => {
                return { item, selected: true };
              });

              // 获取政治面貌
              this.maritalStatusList.forEach((status) => {
                if (status.value == this.curResume.maritalStatus) {
                  this.curResume.maritalStatusName = status.name;
                }
              });
            }
          }
        })
        .finally(() => {
          this.getResumePageList = true;
        });
    },
    // 编辑简历名称
    saveResumeTitle() {
      saveResumeBasic({ id: this.resumeId, title: this.resumeTitle })
        .then(() => {
          this.getResumeInfo(this.activeTabIndex);
        })
        .finally(() => {
          this.showEditTitle = false;
          this.$message({
            message: "操作成功",
            type: "success",
          });
        });
    },
    // 编辑必填项目
    editRequiredField() {
      if (
        !(this.curResume.userExpectJob && this.curResume.userExpectJob.category)
      ) {
        this.showEditJobOri = true;
      }

      if (!this.curResume.resumeEduList) {
        this.showEditEdu = true;
      }

      if (
        !(
          this.curResume.name != "" &&
          this.curResume.birth &&
          this.curResume.birth != "" &&
          this.curResume.email &&
          this.curResume.email != ""
        )
      ) {
        this.showEditBasic = true;
      }
    },
    // 按照id删除项目
    handleDeleteItemById(type, id) {
      this.$confirm("是否要删除该记录？", {
        confirmButtonText: "删除",
      }).then(() => {
        if (type === "award") {
          delResumeAwards(id).then(() => {
            this.getResumeInfo(this.activeTabIndex);
            Toast.success("删除成功");
          });
        } else if (type === "language") {
          delResumeLanguage(id).then(() => {
            this.getResumeInfo(this.activeTabIndex);
            Toast.success("删除成功");
          });
        } else if (type === "practice") {
          delResumePractice(id).then(() => {
            this.getResumeInfo(this.activeTabIndex);
            Toast.success("删除成功");
          });
        } else if (type === "exp") {
          delResumeExp(id).then(() => {
            this.getResumeInfo(this.activeTabIndex);
            Toast.success("删除成功");
          });
        } else if (type === "edu") {
          delResumeEdu(id).then(() => {
            this.getResumeInfo(this.activeTabIndex);
            Toast.success("删除成功");
          });
        }
      });
    },
    // 编辑基本信息
    handleEditBasic(resume) {
      this.resumeForm.id = this.resumeId;
      this.resumeForm.name = resume.name;
      this.resumeForm.avatar = resume.avatar;
      this.resumeForm.birth = resume.birth;
      this.resumeForm.gender = resume.gender;
      this.resumeForm.email = resume.email;
      this.resumeForm.returnTime = resume.returnTime;
      this.resumeForm.graduateTime = resume.graduateTime;
      this.resumeForm.curPlace = resume.curPlace;
      this.resumeForm.phone = resume.phone;
      this.resumeForm.priority = resume.priority;
      this.resumeForm.maritalStatus = resume.maritalStatus;
      this.resumeForm.introduction = resume.introduction;
      this.showEditBasic = true;
    },
    // 保存基本信息
    saveResumeBasic() {
      this.$refs["resumeForm"].validate((valid) => {
        if (valid) {
          this.resumeForm.resumeId = this.curResume.id;
          saveResumeBasic(this.resumeForm)
            .then(() => {
              this.getResumeInfo(this.activeTabIndex);
            })
            .finally(() => {
              this.showEditBasic = false;
            });
        }
      });
    },
    // 编辑工作教育经历，给表单赋值
    handleEditEdu(resumeEdu) {
      if (!resumeEdu) {
        return;
      }
      this.resumeEduForm.id = resumeEdu.id;
      this.resumeEduForm.schoolName = resumeEdu.schoolName;
      this.resumeEduForm.majorName = resumeEdu.majorName;
      this.resumeEduForm.degreeId = resumeEdu.degree.id;
      this.resumeEduForm.startTime = resumeEdu.startTime;
      this.resumeEduForm.finishTime = resumeEdu.finishTime;
      this.resumeEduForm.gpa = resumeEdu.gpa;
      if (this.resumeEduForm.startTime == this.resumeEduForm.finishTime) {
        this.schoolCheck = true;
        this.resumeEduForm.workingDates = [
          this.curResume.updateTime,
          this.curResume.updateTime,
        ];
      } else {
        this.resumeEduForm.workingDates = [
          this.resumeEduForm.startTime,
          this.resumeEduForm.finishTime,
        ];
      }
      this.checkEduWorkingTimeFlag();
      this.showEditEdu = true;
    },
    // 检查workingtimeflag
    checkEduWorkingTimeFlag() {
      if (
        (!this.schoolCheck ||
          (this.schoolCheck && this.schoolCheck == false)) &&
        this.resumeEduForm.workingDates &&
        this.resumeEduForm.workingDates.length == 2
      ) {
        this.resumeEduForm.workingTimeFlag = 1;
      } else if (this.schoolCheck == true) {
        this.resumeEduForm.workingTimeFlag = 1;
      } else {
        this.resumeEduForm.workingTimeFlag = undefined;
      }
      console.log(
        "this.resumeEduForm.workingTimeFlag",
        this.resumeEduForm.workingTimeFlag
      );
    },
    // 保存教育经历
    saveResumeEdu() {
      let gpa = this.resumeEduForm.gpa;
      if (gpa < 0 || gpa > 4) {
        this.$message({
          message: "GPA不能小于0,且不能大于4",
          type: "warning",
        });
        return;
      }
      this.$refs["resumeEduForm"].validate((valid) => {
        if (valid) {
          if (this.schoolCheck == true) {
            this.resumeEduForm.startTime = this.curResume.updateTime;
            this.resumeEduForm.finishTime = this.curResume.updateTime;
          } else {
            this.resumeEduForm.startTime = this.resumeEduForm.workingDates[0];
            this.resumeEduForm.finishTime = this.resumeEduForm.workingDates[1];
          }
          this.resumeEduForm.resumeId = this.resumeId;
          saveResumeEdu(this.resumeEduForm)
            .then(() => {
              this.getResumeInfo(this.activeTabIndex);
            })
            .finally(() => {
              this.cancelEditEdu();
            });
        }
      });
    },
    // 取消保存教育经历
    cancelEditEdu() {
      this.schoolCheck = false;
      this.resumeEduForm.id = undefined;
      this.resumeEduForm.schoolName = undefined;
      this.resumeEduForm.majorName = undefined;
      this.resumeEduForm.degreeId = undefined;
      this.resumeEduForm.workingDates = undefined;
      this.resumeEduForm.startTime = undefined;
      this.resumeEduForm.finishTime = undefined;
      this.resumeEduForm.gpa = undefined;
      this.resumeEduForm.workingTimeFlag = undefined;
      this.showEditEdu = false;
    },
    // 编辑求职意向，给表单赋值
    handleEditExpectJob() {
      this.expectJobForm.resumeId = this.resumeId;
      this.expectJobForm.expectWorkType = this.curResume.userExpectJob.expectWorkType;
      this.expectJobForm.expectPosition = this.curResume.userExpectJob.expectPosition;
      this.expectJobForm.expectCity = this.curResume.userExpectJob.expectCity;
      this.expectJobForm.industry = this.curResume.userExpectJob.industry;
      this.showEditJobOri = true;
    },
    // 保存求职意向
    saveUserExpectJob() {
      this.$refs["expectJobForm"].validate((valid) => {
        if (valid) {
          this.expectJobForm.resumeId = this.resumeId;
          if (
            this.expectJobForm.salaryId &&
            this.expectJobForm.salaryId.length > 0
          ) {
            let temp = this.expectJobForm.salaryId.toString();
            this.expectJobForm.salaryId = temp;
          }
          saveUserExpectJob(this.expectJobForm)
            .then(() => {
              this.getResumeInfo(this.activeTabIndex);
            })
            .finally(() => {
              this.showEditJobOri = false;
            });
        }
      });
    },
    // 编辑项目经历，给表单赋值
    handleEditPractice(resumePractice) {
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
      this.checkPracWorkingTimeFlag();
      this.showEditProj = true;
    },
    // 取消保存项目经历
    cancelEditPractice() {
      this.resumePracticeForm.id = undefined;
      this.resumePracticeForm.workingDates = undefined;
      this.resumePracticeForm.startTime = undefined;
      this.resumePracticeForm.finishTime = undefined;
      this.resumePracticeForm.title = undefined;
      this.resumePracticeForm.description = undefined;
      this.resumePracticeForm.post = undefined;
      this.resumePracticeForm.onWork = undefined;
      this.resumePracticeForm.workingTimeFlag = undefined;
      this.showEditProj = false;
    },
    // 检查workingtimeflag
    checkPracWorkingTimeFlag() {
      if (
        (!this.resumePracticeForm.onWork ||
          (this.resumePracticeForm.onWork &&
            this.resumePracticeForm.onWork == 0)) &&
        this.resumePracticeForm.workingDates &&
        this.resumePracticeForm.workingDates.length == 2
      ) {
        this.resumePracticeForm.workingTimeFlag = 1;
      } else if (this.resumePracticeForm.onWork == 1) {
        this.resumePracticeForm.workingTimeFlag = 1;
      } else {
        this.resumePracticeForm.workingTimeFlag = undefined;
      }
    },
    // 保存项目经历
    saveResumePractice() {
      this.$refs["resumePracticeForm"].validate((valid) => {
        if (valid) {
          this.resumePracticeForm.resumeId = this.resumeId;
          if (
            this.resumePracticeForm.workingDates &&
            this.resumePracticeForm.workingDates.length == 2
          ) {
            this.resumePracticeForm.startTime = this.resumePracticeForm.workingDates[0];
            this.resumePracticeForm.finishTime = this.resumePracticeForm.workingDates[1];
          }
          saveResumePractice(this.resumePracticeForm)
            .then(() => {
              this.getResumeInfo(this.activeTabIndex);
            })
            .finally(() => {
              this.cancelEditPractice();
            });
        }
      });
    },
    // 检查workingtimeflag
    checkExpWorkingTimeFlag() {
      if (
        (!this.resumeExpForm.onWork ||
          (this.resumeExpForm.onWork && this.resumeExpForm.onWork == 0)) &&
        this.resumeExpForm.workingDates &&
        this.resumeExpForm.workingDates.length == 2
      ) {
        this.resumeExpForm.workingTimeFlag = 1;
      } else if (this.resumeExpForm.onWork == 1) {
        this.resumeExpForm.workingTimeFlag = 1;
      } else {
        this.resumeExpForm.workingTimeFlag = undefined;
      }
    },
    // 取消保存工作经验
    cancelEditExp() {
      this.resumeExpForm.id = undefined;
      this.resumeExpForm.startTime = undefined;
      this.resumeExpForm.finishTime = undefined;
      this.resumeExpForm.company = undefined;
      this.resumeExpForm.post = undefined;
      this.resumeExpForm.workType = undefined;
      this.resumeExpForm.description = undefined;
      this.resumeExpForm.workingDates = undefined;
      this.resumeExpForm.onWork = undefined;
      this.resumeExpForm.industry = undefined;
      this.resumeExpForm.workingTimeFlag = undefined;
      this.showEditWork = false;
    },
    // 编辑工作实习经历，给表单赋值
    handleEditExp(resumeExp) {
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
      this.checkExpWorkingTimeFlag();
      this.showEditWork = true;
    },
    // 保存工作实习经历
    saveResumeExp() {
      this.$refs["resumeExpForm"].validate((valid) => {
        if (valid) {
          this.resumeExpForm.depart = "xxx";
          this.resumeExpForm.resumeId = this.resumeId;
          if (
            this.resumeExpForm.workingDates &&
            this.resumeExpForm.workingDates.length == 2
          ) {
            this.resumeExpForm.startTime = this.resumeExpForm.workingDates[0];
            this.resumeExpForm.finishTime = this.resumeExpForm.workingDates[1];
          }
          saveResumeExp(this.resumeExpForm)
            .then(() => {
              this.getResumeInfo(this.activeTabIndex);
            })
            .finally(() => {
              this.cancelEditExp();
            });
        }
      });
    },
    // 取消编辑语言能力
    cancelEditLanguage() {
      this.resumeLanguageForm.title = undefined;
      this.resumeLanguageForm.description = undefined;
      this.resumeLanguageForm.resumeId = undefined;
      this.resumeLanguageForm.id = undefined;
      this.showEditLang = false;
    },
    // 保存语言能力
    saveResumeLanguage() {
      this.$refs["resumeLanguageForm"].validate((valid) => {
        if (valid) {
          this.resumeLanguageForm.resumeId = this.resumeId;
          saveResumeLanguage(this.resumeLanguageForm)
            .then(() => {
              this.getResumeInfo(this.activeTabIndex);
            })
            .finally(() => {
              this.cancelEditLanguage();
            });
        }
      });
    },
    // 取消编辑奖项
    cancelEditAwards() {
      this.certForm.title = undefined;
      this.certForm.time = undefined;
      this.certForm.id = undefined;
      this.showEditHonor = false;
    },
    // 保存证书奖项
    saveResumeAwards() {
      this.$refs["certForm"].validate((valid) => {
        if (valid) {
          this.certForm.resumeId = this.resumeId;
          saveResumeAwards(this.certForm)
            .then(() => {
              this.getResumeInfo(this.activeTabIndex);
            })
            .finally(() => {
              this.cancelEditAwards();
            });
        }
      });
    },
    // 选择技能标签
    onSelectSkillTag(tag) {
      // 如果选择新标签且标签数量超过6个，返回
      if (tag.selected === false && this.selectedSkillList.length >= 6) {
        this.$message({
          message: "标签数量不能超过6个",
          type: "warning",
        });
        return;
      }

      tag.selected = !tag.selected;
      if (tag.selected === false) {
        // 如果tag之前已被选择，从列表中删除
        this.selectedSkillList = this.selectedSkillList.filter(
          (x) => x.item.name != tag.item.name
        );
      } else {
        // 如果tag之前没有被选择，增加到列表中
        this.selectedSkillList.push(tag);
      }
    },
    // 添加新的技能标签
    addNewSkillTag() {
      // 如果内容为空，返回
      if (this.newSkillTag == "") {
        return;
      }
      // 如果标签数量超过6个，返回
      if (this.selectedSkillList.length >= 6) {
        this.$message({
          message: "标签数量不能超过6个",
          type: "warning",
        });
        return;
      }
      this.selectedSkillList.push({
        item: { name: this.newSkillTag },
        select: true,
      });
      this.skillTagList.push({
        item: { name: this.newSkillTag },
        selected: true,
      });
      this.newSkillTag = "";
    },

    // 保存能力标签
    saveSkillTag() {
      let data = {
        resumeId: this.resumeId,
        tagList: this.selectedSkillList.map((tag) => {
          return tag.item.name;
        }),
      };
      saveResumeSkills(data)
        .then(() => {
          this.getResumeInfo(this.activeTabIndex);
        })
        .finally(() => {
          this.showEditSkillTag = false;
        });
    },
    // 点击切换tab
    handleTabClick(tab) {
      // console.log("----", tab);
      this.activeTabIndex = String(tab.index);
      this.getResumeInfo(this.activeTabIndex);
    },
    // 点击添加tab
    handleTabsEdit() {
      let total = this.resumePageList.length;
      // 小于4份简历，添加新简历
      if (total < 4) {
        saveResumeBasic({
          userId: this.userId,
          title: "简历" + (total + 1),
        })
          .then(() => {
            this.activeTabIndex = total;
            this.getResumeInfo(this.activeTabIndex);
          })
          .finally(() => {
            this.$message({
              message: "操作成功",
              type: "success",
            });
          });
      } else {
        //大于四份简历，不添加
        this.$message({
          message: "最多可编辑4份在线简历",
          type: "warning",
        });
      }
    },
    // 上传更新简历头像
    onUpload(url) {
      saveResumeBasic({ userId: this.userId, id: this.resumeId, avatar: url })
        .then(() => {
          this.getResumeInfo(this.activeTabIndex);
        })
        .finally(() => {
          this.$message({
            message: "操作成功",
            type: "success",
          });
        });
    },
    // 保存自我介绍
    saveSelfIntro(introduction) {
      saveResumeBasic({
        userId: this.userId,
        id: this.resumeId,
        introduction: introduction,
      })
        .then(() => {
          this.getResumeInfo(this.activeTabIndex);
        })
        .finally(() => {
          this.$message({
            message: "操作成功",
            type: "success",
          });
          this.showEditSelfIntro = false;
        });
    },
    // 更改简历优先级
    onChangePriority(val) {
      saveResumeBasic({ userId: this.userId, id: this.resumeId, priority: val })
        .then(() => {
          this.getResumeInfo(this.activeTabIndex);
        })
        .finally(() => {
          this.$message({
            message: "操作成功",
            type: "success",
          });
        });
    },
    // 删除简历
    handleDelResume() {
      // 只有一份简历直接删除
      if (this.resumePageList.length > 1) {
        this.$confirm("是否要删除该简历？", {
          confirmButtonText: "删除",
        }).then(() => {
          delResume(this.resumeId).then(() => {
            this.getResumeInfo(0);
          });
        });
      } else {
        //否则，不删除
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
    },
    // 上传附件
    beforeAttachUpload(file) {
      return new Promise((resolve, reject) => {
        if (checkAttachmentSize(file)) {
          reject();
        } else {
          // console.log("file", file);
          getUploadAttachmentToken(file.name)
            .then((response) => {
              const { data } = response;
              this.uploadAttachmentOptions.fileName = file.name;
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
    // 上传附件
    handleUploadAttachSuccess() {
      // console.log("-------", this.$refs);
      this.uploadAttachLoading = true;
      saveResumeBasic({
        userId: this.userId,
        id: this.resumeId,
        attachResume: this.uploadAttachmentOptions.fileUrl,
      })
        .then(() => {
          this.getResumeInfo(this.activeTabIndex);
        })
        .finally(() => {
          this.$refs["upload"].clearFiles();
          this.$message({
            message: "操作成功",
            type: "success",
          });
          this.uploadAttachLoading = false;
        });
    },
    // 上传其他附件
    handleUploadAttachOthersSuccess() {
      this.uploadOtherAttachLoading = true;
      let data = [];
      if (
        this.curResume.resumeMergeAttachList &&
        this.curResume.resumeMergeAttachList.length > 0
      ) {
        data = Array.from(this.curResume.resumeMergeAttachList).map((item) => {
          return { link: item.resumeAttach, name: item.name };
        });
      }

      data.push({
        link: this.uploadAttachmentOptions.fileUrl,
        name: this.uploadAttachmentOptions.fileName,
      });
      saveResumeBasic({
        userId: this.userId,
        id: this.resumeId,
        attachOthers: data,
      })
        .then(() => {
          this.getResumeInfo(this.activeTabIndex);
        })
        .finally(() => {
          this.$refs["upload"].clearFiles();
          this.$message({
            message: "操作成功",
            type: "success",
          });
          this.uploadOtherAttachLoading = false;
        });
    },
    // 删除其他附件
    handleDelAttachOthers() {
      let tempList = Array.from(this.curResume.resumeMergeAttachList).map(
        (item) => {
          return { link: item.resumeAttach, name: item.name };
        }
      );
      let data = tempList.filter((item, index) => {
        return index != this.delAttachItemIndex;
      });
      saveResumeBasic({
        userId: this.userId,
        id: this.resumeId,
        attachOthers: data,
      })
        .then(() => {
          this.getResumeInfo(this.activeTabIndex);
        })
        .finally(() => {
          this.$message({
            message: "操作成功",
            type: "success",
          });
        });
    },
    // 编辑其他附件名称
    handleEditOtherTitle() {},
    // 保存其他附件名称
    saveOtherTitle(index) {
      let data = this.curResume.resumeMergeAttachList;
      data[index].name = this.otherTitle;

      saveResumeBasic({
        userId: this.userId,
        id: this.resumeId,
        attachOthers: data,
      })
        .then(() => {
          this.getResumeInfo(this.activeTabIndex);
        })
        .finally(() => {
          this.$refs["upload"].clearFiles();
          this.$message({
            message: "操作成功",
            type: "success",
          });
        });
    },
    // 简历导出下载
    exportPdf() {
      exportResumeToPdf(this.resumeId)
        .then((response) => {
          downloadFile({
            fileKey: response.data,
            fileName: `${this.$store.getters.name}_个人简历.pdf`,
            success: () => {
              this.$set(this, "showPreview", false);
            },
          });
        })
        .catch(() => {});
    },
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" scoped>
.app-container {
  margin: 78px auto !important;
  min-height: calc(100vh - 477px);
  background: rgba(213, 226, 240, 0.21);
  display: flex;

  .resume-preview {
    /deep/ .el-dialog__header {
      display: none;
    }
    /deep/.el-dialog__body{
      padding-top: 20px;
    }
    /deep/.el-dialog__headerbtn {
      display: none;
    }
  }

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

    /deep/ .el-dialog__body {
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
        }
      }
    }
  }

  .left-container {
    flex: 3;
    margin-right: 42px;
    padding: 0 61px;
    padding-bottom: 20px;

    .menu {
      margin-top: 41px;
    }

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

    .resume-module {
      min-height: 100px;
      .line1 {
        display: flex;
        justify-content: space-between;
        .bullet-point {
          width: 7px;
          height: 7px;
          background: #4f69ed;
          border-radius: 5px;
          display: inline-block;
          margin-right: 7px;
          vertical-align: middle;
          margin-bottom: 5px;
        }
        .star {
          margin-left: 5px;
          color: #ff1744;
        }
      }

      .line2 {
        display: flex;
        padding-left: 10px;
        width: 100%;

        .uploader {
          width: 110px;
          margin-right: 13px;
        }

        .content {
          width: 100%;

          .text-section {
            width: 100%;
            margin-right: 10px;
          }

          .button-section {
            margin-top: 12px;
            width: 20px;
          }
        }

        .content + .content {
          margin-top: 16px;
        }
        .entry-line {
          display: flex;
          justify-content: space-between;
          width: 100%;
          .entry {
            width: 50%;
          }
        }

        .entry-line + .entry-line {
          margin-top: 4px;
        }
      }

      .edit-box {
        background: #f6f9fc;
        padding: 15px 50px;
        margin-top: 12px;

        .left-right {
          display: flex;
          flex-direction: row;
        }

        .footer {
          display: flex;
          justify-content: center;

          .confirm {
            width: 75px;
            height: 23px;
            background: #4895ef;
            border-radius: 12px;
            border: 1px solid #4895ef;

            font-size: 12px;
            font-family: PingFangSC-Medium, PingFang SC;
            font-weight: 500;
            color: #ffffff;
            line-height: 17px;

            display: flex;
            align-items: center;
            justify-content: center;
          }

          .cancel {
            width: 75px;
            height: 23px;
            background: #ffffff;
            border-radius: 12px;
            border: 1px solid #4895ef;

            font-size: 12px;
            font-family: PingFangSC-Medium, PingFang SC;
            font-weight: 500;
            color: #4895ef;
            line-height: 17px;
            display: flex;
            align-items: center;
            justify-content: center;
          }
        }

        .line {
          display: flex;
          justify-content: space-around;
        }

        .selected-tag-list {
          .tag-title {
            font-size: 12px;
            font-family: PingFangSC-Medium, PingFang SC;
            font-weight: 500;
            vertical-align: text-top;
          }
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
            width: 85px;
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
      }
    }
  }

  .right-container {
    flex: 1;
    padding: 41px 21px;
    height: fit-content;
    .button-section {
      display: flex;
      .button {
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

    .section2 {
      .line1 {
        display: flex;
        justify-content: space-between;
        align-items: center;
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
    }
  }

  .content-box {
    background: #ffffff;
    box-shadow: 0px 4px 16px 3px rgba(191, 199, 215, 0.31);
  }

  .overflow {
    width: 200px;
    word-break: break-all;
    display: -webkit-box;
    -webkit-line-clamp: 1;
    -webkit-box-orient: vertical;
    overflow: hidden;
  }

  .tag-title {
    font-size: 12px;
    font-family: PingFangSC-Medium, PingFang SC;
    font-weight: 500;
    vertical-align: text-top;
  }

  .xsmall {
    font-size: 12px;
    font-family: PingFangSC-Medium, PingFang SC;
    color: #333333;
    line-height: 20px;
  }

  .small {
    font-size: 14px;
    font-family: PingFangSC-Medium, PingFang SC;
    color: #333333;
    line-height: 20px;
  }

  .regular {
    font-size: 16px;
    font-family: PingFangSC-Medium, PingFang SC;
    color: #333333;
    line-height: 20px;
  }

  .large {
    font-size: 18px;
    font-family: PingFangSC-Medium, PingFang SC;
    color: #333333;
    line-height: 20px;
  }

  .light {
    font-weight: 400;
  }

  .bold {
    font-weight: 500;
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

  //priority
  .priority-selector {
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

  //progress
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

  //tag
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
  //menu
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
}
</style>