<template>
  <div class="app-container edit-job-container">
    <h5 class="mb-4">{{ title }}</h5>
    <el-form
      ref="jobForm"
      size="medium"
      :model="jobForm"
      :rules="jobFormRules"
      label-width="100px"
      label-position="left"
    >
      <el-form-item label="公司全称" prop="companyId" v-if="isOP">
        <el-select
          v-model="jobForm.companyId"
          filterable
          remote
          reserve-keyword
          :remote-method="searchCompanyOptions"
          @click.native="searchCompanyOptions()"
          placeholder="请输入公司名称"
          size="small"
        >
          <el-option
            v-for="item in companyOptions"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="职位名称" prop="name">
        <el-input
          v-model="jobForm.name"
          maxlength="40"
          class="text-input-width"
          show-word-limit
          placeholder="请填写职位名称"
          :disabled="isModify"
        ></el-input>
      </el-form-item>

      <el-form-item label="职位类别" prop="categoryId">
        <el-cascader
          placeholder="选择职位类别"
          :show-all-levels="false"
          :options="jobCategoryOptions"
          :props="jobCategoryProps"
          filterable
          clearable
          v-model="jobForm.categoryId"
          @change="onJobCategoryChange"
          :disabled="isModify"
        ></el-cascader>
      </el-form-item>

      <el-form-item label="工作类型" prop="jobType">
        <el-select
          v-model="jobForm.jobType"
          placeholder="全职"
          class="salary-option"
        >
          <el-option
            v-for="item in jobTypeOptions1"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          >
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="薪资待遇" prop="salary">
        <!-- 全职 -->
        <el-select
          v-model="jobForm.minSalary"
          placeholder="最低薪资"
          class="salary-option mr-2"
          @change="onSalaryChange"
          v-if="jobForm.jobType == 107"
        >
          <el-option
            v-for="item in salaryOptions"
            v-if="item !== 50"
            :key="item"
            :label="item === 0 ? '面议' : item + 'K'"
            :value="item"
          >
          </el-option>
        </el-select>
        <!-- 实习 -->
        <el-select
          v-model="jobForm.minSalary"
          placeholder="最低薪资"
          class="salary-option mr-2"
          @change="onSalaryChange"
          v-else
        >
          <el-option
            v-for="item in internSalaryOptions"
            :key="item"
            :label="item === 0 ? '面议' : item"
            :value="item"
          >
          </el-option>
        </el-select>
        <span class="pl-2 pr-2" v-if="jobForm.minSalary !== 0">-</span>
        <!-- 全职 -->
        <el-select
          v-model="jobForm.maxSalary"
          placeholder="最高薪资"
          class="salary-option ml-2 mr-2"
          v-if="jobForm.minSalary !== 0 && jobForm.jobType == 107"
        >
          <el-option
            v-for="item in salaryOptions"
            v-if="item > jobForm.minSalary"
            :key="item"
            :label="item === 0 ? '面议' : item + 'K'"
            :value="item"
          >
          </el-option>
        </el-select>
        <!-- 实习 -->
        <el-select
          v-model="jobForm.maxSalary"
          placeholder="最高薪资"
          class="salary-option ml-2 mr-2"
          v-else-if="jobForm.minSalary !== 0 && jobForm.jobType != 107"
        >
          <el-option
            v-for="item in internSalaryOptions"
            v-if="item > jobForm.minSalary"
            :key="item"
            :label="item === 0 ? '面议' : item"
            :value="item"
          >
          </el-option>
        </el-select>

        <span
          class="pl-2 pr-2"
          v-if="jobForm.minSalary !== 0 && jobForm.jobType == 107"
          >×</span
        >
        <el-select
          v-model="jobForm.salaryMonths"
          placeholder="薪资月数"
          class="salary-option ml-2 mr-2"
          v-if="jobForm.minSalary !== 0 && jobForm.jobType == 107"
        >
          <el-option
            v-for="item in salaryMonthOptions"
            :key="item"
            :label="item"
            :value="item"
          ></el-option>
        </el-select>
        <span
          class="pl-2 pr-2"
          v-if="jobForm.minSalary !== 0 && jobForm.jobType == 107"
          >薪</span
        >
        <span class="pl-2 pr-2" v-else-if="jobForm.minSalary !== 0">元/天</span>
      </el-form-item>

      <el-form-item label="工作城市" prop="cityId">
        <el-cascader
          placeholder="请选择工作城市"
          :show-all-levels="true"
          :options="cityOptions"
          :props="cityIdProps"
          filterable
          clearable
          v-model="jobForm.cityId"
          ref="cityCascader"
          @change="getLocation"
          :disabled="isModify"
        >
        </el-cascader>
      </el-form-item>

      <el-form-item label="工作地址" prop="address">
        <el-input
          v-model="jobForm.address"
          maxlength="250"
          class="text-input-width"
          show-word-limit
          placeholder="请填写工作地址"
        ></el-input>
      </el-form-item>

      <el-form-item label="选择定位" prop="position">
        <p class="text-gray">输入城市和写字楼位置，然后拖动定位器进行选择。</p>
        <div class="edit-map-box">
          <el-amap
            vid="marker"
            :zoom="mapZoom"
            :center="poiMapMarker.position"
            :plugin="mapPlugin"
            :events="events"
          >
            <el-amap-search-box
              class="map-search-box"
              :search-option="mapSearchOption"
              :on-search-result="onSearchPoiResult"
            ></el-amap-search-box>
            <el-amap-marker
              vid="poi-marker"
              :position="poiMapMarker.position"
              :events="poiMapMarker.events"
              :draggable="poiMapMarker.draggable"
            ></el-amap-marker>
          </el-amap>
        </div>
      </el-form-item>

      <el-form-item label="学历要求" prop="minDegreeId">
        <el-select
          v-model="jobForm.minDegreeId"
          filterable
          clearable
          placeholder="请选择最低学历"
        >
          <el-option
            v-for="item in degreeOptions"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="经验要求" prop="experienceId">
        <el-select
          v-model="jobForm.experienceId"
          filterable
          clearable
          placeholder="请选择经验要求"
        >
          <el-option
            v-for="item in experienceOptions"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="其他要求" prop="additionIds">
        <el-checkbox-group v-model="additionNames">
          <el-checkbox
            v-for="item in additionOptions"
            :key="item.id"
            :label="item.name"
          ></el-checkbox>
        </el-checkbox-group>
      </el-form-item>

      <el-form-item label="语言要求" prop="languageId">
        <el-select
          v-model="jobForm.languageId"
          filterable
          clearable
          placeholder="请选择语言要求"
        >
          <el-option
            v-for="item in languageOptions"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          ></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="职位描述" prop="description">
        <quill-editor
          v-model="jobForm.description"
          :options="descriptionEditorOption"
          style="min-height: 200px"
          @change="onEditorChange($event)"
        >
        </quill-editor>
        <div
          style="bottom: 0; text-align: right; position: absolute; right: 12px"
        >
          {{ contentLength }}/2000
        </div>
      </el-form-item>

      <div class="job-description-container">
        <div class="job-description-show">
          <el-button
            v-if="jobDescriptionShow"
            @click="jobForm.description = jobDescription"
            type="text"
            style="font-weight: bold; font-size: 15px"
          >
            复制样例
          </el-button>
          <el-button
            v-if="jobDescription"
            @click="jobDescriptionShow = !jobDescriptionShow"
            type="text"
            style="font-weight: bold; font-size: 15px"
          >
            {{ jobDescriptionShow ? "关闭样例" : "查看样例" }}
          </el-button>
        </div>
        <div
          v-if="jobDescriptionShow"
          v-html="jobDescription"
          class="job-description"
        ></div>
      </div>
      <el-form-item>
        <el-button
          type="primary"
          size="mini"
          style="padding: 0 12px; line-height: 30px; margin-top: 60px"
          @click="onPreview"
          >预览
        </el-button>
        <el-button
          type="primary"
          size="mini"
          style="
            padding: 0 12px;
            line-height: 30px;
            margin-top: 60px;
            margin-left: 30px;
          "
          :loading="posting"
          @click="onSubmit"
          >{{ pubButtonText }}
        </el-button>
      </el-form-item>
    </el-form>

    <el-dialog
      :visible.sync="dialogVisible"
      class="dialog-container"
      width="800px"
      title="添加职位关键词"
      :before-close="dialogClose"
    >
      <div class="selected-container">
        <span>已选择</span>
        <div class="selected-right">
          <el-tag
            v-for="tag in industryList"
            v-if="tag.selected"
            :key="tag.name"
            closable
            @close="onDeleteTag(tag)"
          >
            {{ tag.name }}
          </el-tag>
          <el-tag
            v-for="tag in skillList"
            v-if="tag.selected"
            :key="tag.name"
            closable
            @close="onDeleteTag(tag)"
          >
            {{ tag.name }}
          </el-tag>
          <el-tag
            v-for="tag in industryAdditionList"
            v-if="tag.selected"
            :key="tag.name"
            closable
            @close="onDeleteTag(tag)"
          >
            {{ tag.name }}
          </el-tag>
          <el-tag
            v-for="tag in skillAdditionList"
            v-if="tag.selected"
            :key="tag.name"
            closable
            @close="onDeleteTag(tag)"
          >
            {{ tag.name }}
          </el-tag>
        </div>
      </div>

      <div class="industry-container" v-if="industryList.length">
        <el-select
          class="mb-4"
          v-model="searchTag"
          filterable
          remote
          reserve-keyword
          placeholder="请输入关键词"
          :remote-method="remoteMethod"
          @change="onSearchTagChange"
          :loading="searchTagLoading"
        >
          <el-option
            v-for="item in searchTagOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          >
          </el-option>
        </el-select>
        <div class="title-container">
          行业领域<span class="title-tips">(最多可选2个行业)</span>
        </div>
        <div class="industry-tag-container">
          <el-tag
            v-for="tag in industryList"
            :key="tag.name"
            :class="['skill-style', { 'skill-style-select': tag.selected }]"
            @click="onIndustrySelect(tag)"
          >
            {{ tag.name }}
          </el-tag>
        </div>
        <div class="industry-tag-container">
          <el-tag
            v-for="tag in industryAdditionList"
            :key="tag.name"
            closable
            @close="onDeleteIndustryAddition(tag)"
            :class="['skill-style', { 'skill-style-select': tag.selected }]"
            @click="onIndustrySelect(tag)"
          >
            {{ tag.name }}
          </el-tag>
        </div>
        <div class="skill-keywords">
          <el-input
            type="text"
            placeholder="添加关键词"
            v-model="industryKeywords"
            maxlength="7"
            show-word-limit
            size="small"
            @focus="showIndustryAddButton = true"
            @blur="onIndustryKeywordsBlue"
          >
          </el-input>
          <el-link
            type="primary"
            v-show="showIndustryAddButton"
            @click="addIndustryKeywords"
            class="ml-4"
            style="color: #409eff"
            >添加</el-link
          >
        </div>
      </div>

      <div class="industry-container" v-if="skillList.length">
        <div class="title-container">
          技能要求<span class="title-tips">(最多可选4个技能)</span>
        </div>
        <div class="industry-tag-container">
          <el-tag
            v-for="tag in skillList"
            :key="tag.id"
            :class="['skill-style', { 'skill-style-select': tag.selected }]"
            @click="onSkillSelect(tag)"
          >
            {{ tag.name }}
          </el-tag>
        </div>
        <div class="industry-tag-container">
          <el-tag
            v-for="tag in skillAdditionList"
            :key="tag.id"
            closable
            @close="onDeleteSkillAddition(tag)"
            :class="['skill-style', { 'skill-style-select': tag.selected }]"
            @click="onSkillSelect(tag)"
          >
            {{ tag.name }}
          </el-tag>
        </div>
        <div class="skill-keywords">
          <el-input
            type="text"
            placeholder="添加关键词"
            v-model="skillKeywords"
            maxlength="7"
            show-word-limit
            size="small"
            @focus="showSkillAddButton = true"
            @blur="onSkillKeywordsBlue"
          >
          </el-input>
          <el-link
            type="primary"
            v-show="showSkillAddButton"
            @click="addSkillKeywords"
            class="ml-4"
            style="color: #409eff"
            >添加</el-link
          >
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false" size="small">取 消</el-button>
        <el-button type="primary" size="small" @click="onConfirm"
          >确 定</el-button
        >
      </span>
    </el-dialog>
    <!-- 预览功能 -->
    <el-dialog
      title="预览"
      :visible.sync="dialogVisible2"
      width="80%"
      class="preview-container"
    >
      <el-card :body-style="{ padding: '0px' }" shadow="never">
        <div class="session1-container" v-if="job">
          <div class="session1-left">
            <div class="text1 mb-2" v-if="job && !isOP && job.companyName">
              {{ job.companyName }}招聘
            </div>
            <div class="text1 mb-2" v-if="job && isOP">WE内推岗位</div>
            <div class="session1-job-name mb-2">
              <span v-if="job.name">{{ job.name }}</span>
              <!-- 薪酬 -->
              <!-- 面议 -->
              <span v-if="job.minSalary == 0" class="salary-text ml-4">
                面议
              </span>
              <!-- 全职 -->
              <span v-else-if="job.jobType == `全职`" class="salary-text ml-4">
                {{
                  job.minSalary +
                  "K-" +
                  job.maxSalary +
                  "K" +
                  " * " +
                  job.salaryMonths
                }}
              </span>
              <!-- 实习/兼职 -->
              <span v-else class="salary-text ml-4">
                {{ job.minSalary + "-" + job.maxSalary + " 元/天" }}
              </span>
            </div>
            <div class="text3 mb-2">
              {{ job.city ? job.city : "城市不限" }} |
              {{
                job.minDegree
                  ? job.minDegree == "不限"
                    ? "学历不限"
                    : job.minDegree
                  : ""
              }}
              | {{ job.jobType ? job.jobType : "" }} |
              {{
                job.experience
                  ? job.experience == "不限"
                    ? "工作经验不限"
                    : job.experience
                  : ""
              }}
              <span v-for="(item, index) in job.additionNames" :key="index">
                | {{ item }}
              </span>
            </div>
            <div class="text4">{{ nowDate }}</div>
          </div>
        </div>
        <div class="session2-container" v-if="job">
          <div class="session2-container-left">
            <div v-html="job.description"></div>
          </div>
          <div class="divider"></div>
          <div class="session2-container-right">
            <div class="mb-4" v-if="job && isOP">
              <svg-icon
                icon-class="job-fast-track"
                style="height: 70px; width: 180px; margin-left: -25px"
              />
              <div>
                "WE内推"是World
                Elite最具竞争力的产品之一，致力于推出一个让每个学生都放心的渠道获取内推资源，凭借自己的努力走出自己的职业道路。
              </div>
            </div>
            <div
              class="session2-container-right-1"
              v-if="job && !isOP && companyInfo"
            >
              <div class="basic-info mb-2">公司基本信息:</div>
              <el-link :href="`/company/${companyInfo.id}`" :underline="false">
                <div class="session2-logo-container">
                  <el-image
                    :src="companyInfo.logo"
                    class="session2-logo"
                  ></el-image>
                </div>
              </el-link>
              <div v-if="companyInfo.industry" class="info-text mt-2">
                <svg-icon
                  icon-class="companycategory"
                  style="height: 19px; width: 19px; margin-right: 9px"
                />
                {{ companyInfo.industry.name }}
              </div>
              <div
                v-if="companyInfo.stage && companyInfo.property"
                class="info-text mt-2"
              >
                <svg-icon
                  icon-class="companytype"
                  style="height: 19px; width: 19px; margin-right: 9px"
                />
                {{ companyInfo.property.name }} |
                {{ companyInfo.stage.name }}
              </div>
              <div v-if="companyInfo.scale" class="info-text mt-2">
                <svg-icon
                  icon-class="employeedata"
                  style="height: 19px; width: 21px; margin-right: 9px"
                />
                {{ companyInfo.scale.name }}
              </div>
              <div v-if="companyInfo.homepage" class="info-text mt-2">
                <svg-icon
                  icon-class="website"
                  style="height: 17px; width: 21px; margin-right: 12px"
                />
                <el-link
                  :href="companyLink"
                  target="_blank"
                  :underline="false"
                  class="website-text"
                  >{{ companyLinkName }}
                </el-link>
              </div>
            </div>
            <div
              class="session2-container-right-2"
              v-if="job && job.address && job.latitude && job.longitude"
            >
              <div class="basic-info mb-2">工作地址:</div>
              <div class="map-wrapper">
                <div class="map-box mb-4">
                  <el-amap
                    :vid="'amap'"
                    :zoom="mapZoom"
                    :center="addr.mapWindow.position"
                  >
                    <el-amap-marker :position="addr.mapWindow.position">
                    </el-amap-marker>
                    <el-amap-info-window
                      :position="addr.mapWindow.position"
                      :content="addr.mapWindow.content"
                      :close-when-click-map="true"
                    ></el-amap-info-window>
                  </el-amap>
                </div>
              </div>
              <div class="address-label">
                <div class="address-label-icon-wrapper">
                  <svg-icon
                    icon-class="jobaddress"
                    style="
                      height: 19px;
                      width: 15px;
                      margin-right: 7px;
                      top: 3px;
                      position: absolute;
                    "
                  />
                </div>
                <div class="info-text">{{ job.address }}</div>
              </div>
            </div>
          </div>
        </div>
      </el-card>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dialogVisible2 = false"
          >确 定</el-button
        >
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getCategoryTree } from "@/api/category_api";
import { listByType } from "@/api/dict_api";
import { getJobInfo } from "@/api/job_api";
import { getNowDate } from "@/utils/dateUtil";
import { getCompanyInfo, myCompanyInfo } from "@/api/company_api";

import "quill/dist/quill.core.css";
import "quill/dist/quill.snow.css";
import "quill/dist/quill.bubble.css";
import { quillEditor } from "vue-quill-editor";
import { opSaveJob, saveJob } from "../api/job_api";
import Toast from "@/utils/toast";
import Vue from "vue";
import VueAMap, { lazyAMapApiLoaderInstance } from "vue-amap";
import { listByTypeWithSort } from "@/api/dict_api";
import { mapGetters } from "vuex";

Vue.use(VueAMap);

VueAMap.initAMapApiLoader({
  key: process.env.VUE_APP_AMAP_KEY,
  plugin: [
    "AMap.Autocomplete",
    "AMap.PlaceSearch",
    "AMap.Scale",
    "AMap.Geocoder",
    "AMap.ToolBar",
  ],
  v: "1.4.4",
});

let geocoder;

lazyAMapApiLoaderInstance.load().then(() => {
  // eslint-disable-next-line no-undef
  geocoder = new AMap.Geocoder({ extensions: "base" });
});

let id = 0;
export default {
  name: "NewJobPage",
  components: {
    quillEditor,
  },
  data() {
    const checkCompany = (rule, value, callback) => {
      if (this.isOP && !this.jobForm.companyId) {
        return callback(new Error("请选择公司"));
      } else {
        callback();
      }
    };
    const checkSalary = (rule, value, callback) => {
      if (this.jobForm.minSalary && !this.jobForm.maxSalary) {
        return callback(new Error("请选择最高薪资"));
      } else {
        callback();
      }
    };
    const checkPosition = (rule, value, callback) => {
      if (!this.jobForm.latitude || !this.jobForm.longitude) {
        return callback(new Error("请选择位置"));
      } else {
        callback();
      }
    };
    return {
      // 预览用
      companyLinkName: "",
      companyLink: "",
      companyInfo: {
        id: undefined,
        name: "",
        logo: "",
        property: { name: "" },
        stage: { name: "" },
        industry: { name: "" },
        scale: { name: "" },
        homepage: "",
      },
      job: {
        companyName: "",
        name: "",
        minDegree: "",
        experience: "",
        city: "",
        additionNames: [],
        salaryMonths: undefined,
        minSalary: undefined,
        maxSalary: undefined,
        jobType: undefined,
        time: undefined,
        description: undefined,
        latitude: undefined,
        longitude: undefined,
        address: undefined,
      }, 
      addr: {
        mapWindow: {},
      },

      dialogVisible: false,
      dialogConfirm: false,
      dialogVisible2: false, //预览弹框
      Salary: "",
      nowDate: "", //当前时间
      previewSalary: "", //薪资
      previewCity: "", //预览城市
      previewJobType: "", //预览工作类型
      previewRecruitType: "",
      previewMinDegree: "",
      industryKeywords: "", //新增技能关键词
      skillKeywords: "", //新增技能关键词
      jobForm: {
        companyId: undefined,
        id: undefined,
        name: undefined,
        categoryId: undefined,
        minDegreeId: undefined,
        salaryMonths: undefined,
        minSalary: undefined,
        maxSalary: undefined,
        cityId: undefined,
        latitude: undefined,
        longitude: undefined,
        address: undefined,
        recruitType: undefined,
        jobType: undefined,
        description: undefined,
        keywords: undefined,
        experienceId: undefined,
        additionIds: [],
        skillTags: [],
        industryTags: [],
        industryAdditionTags: [],
        skillAdditionTags: [],
        languageId: undefined,
      },
      jobFormRules: {
        name: [{ required: true, message: "请输入职位名称", trigger: "blur" }],
        categoryId: [
          { required: true, message: "请选择职位类型", trigger: "change" },
        ],
        minDegreeId: [
          { required: true, message: "请选择学历要求", trigger: "change" },
        ],
        address: [
          { required: true, message: "请填写工作地点", trigger: "blur" },
        ],
        jobType: [
          { required: true, message: "请选择工作类型", trigger: "change" },
        ],
        recruitType: [
          { required: true, message: "请选择工作类型", trigger: "change" },
        ],
        description: [
          { required: true, message: "请输入职位描述", trigger: "blur" },
        ],
        salary: [{ required: true, validator: checkSalary }],
        companyId: [{ required: true, validator: checkCompany }],
        position: [
          { required: true, validator: checkPosition, trigger: "change" },
        ],
        experienceId: [
          { required: true, message: "请选择经验要求", trigger: "change" },
        ],
        languageId: [
          { required: true, message: "请选择语言要求", trigger: "change" },
        ],
        cityId: [
          { required: true, message: "请选择工作城市", trigger: "change" },
        ],
      },
      jobCategoryOptions: [],
      jobCategoryProps: {
        expandTrigger: "hover",
        value: "id",
        label: "name",
        emitPath: true,
        children: "children",
      },
      cityIdProps: {
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
      cityOptions: [],
      degreeOptions: [],
      jobTypeOptions1: [],
      descriptionEditorOption: {
        theme: "snow",
        placeholder: "1.岗位职责 2.任职要求等",
        modules: {
          toolbar: [["bold"], [{ list: "ordered" }, { list: "bullet" }]],
        },
      },
      posting: false,
      experienceOptions: [], // 经验要求
      additionOptions: [],
      additionNames: [],
      languageOptions: [],
      internSalaryOptions: [
        0,
        50,
        100,
        150,
        200,
        250,
        300,
        350,
        400,
        450,
        500,
        550,
        600,
        650,
        700,
        750,
        800,
      ],
      salaryOptions: [
        0,
        1,
        2,
        3,
        4,
        5,
        6,
        7,
        8,
        9,
        10,
        12,
        14,
        16,
        18,
        20,
        22,
        24,
        26,
        28,
        30,
        35,
        40,
        45,
        50,
      ],
      salaryMonthOptions: [12, 13, 14, 15, 16, 17, 18, 19, 20],
      skillList: [], //技能列表
      skillAdditionList: [], // 用户添加的技能列表
      industryList: [], //行业列表
      industryAdditionList: [], // 用户添加的行业列表
      jobDescription: undefined, // 职位描述
      jobDescriptionShow: false, // 职位描述开关
      contentLength: 0, // 字数统计
      searchTagLoading: false,
      searchTag: undefined,
      searchTagOptions: [],
      showIndustryAddButton: false,
      showSkillAddButton: false,
      secondCategoryId: undefined, // 选中的二级职位id
      isOP: false, // 是否为超级账号
      companyOptions: [],

      mapZoom: 14,
      poiMapMarker: {
        position: [121.5273285, 31.21515044],
        events: {
          dragend: (e) => {
            console.log(this);
            this.poiMapMarker.position = [e.lnglat.lng, e.lnglat.lat];
            this.getCompanyAddress(e.lnglat.lng, e.lnglat.lat);
          },
        },
        draggable: true,
      },
      mapPlugin: [
        {
          pName: "ToolBar",
          events: {
            init(instance) {
              console.log(instance);
            },
          },
        },
      ],
      mapSearchOption: {
        city: "北京",
        citylimit: false,
      },
      events: {
        init(instance) {
          console.log(instance);
        },
        // 点击获取地址的数据
        click: (e) => {
          this.poiMapMarker.position = [e.lnglat.lng, e.lnglat.lat];
          this.getCompanyAddress(e.lnglat.lng, e.lnglat.lat);
        },
      },
    };
  },
  created() {
    const jobId = this.$route.query.id;
    this.initData(jobId);
  },
  mounted() {},
  watch: {
    jobForm: {
      handler() {
        if (this.jobForm.id === undefined) {
          this.$store.commit("setting/JOB_DRAFT", this.jobForm);
        }
      },
      deep: true,
    },

    poiMapMarker: {
      handler(location) {
        this.jobForm.longitude = location.position[0];
        this.jobForm.latitude = location.position[1];
      },
      deep: true,
    },
  },
  computed: {
    //  ...mapGetters(["userId"]),
    title() {
      return this.$route.query.id ? "编辑职位" : "新增职位";
    },
    pubButtonText() {
      return this.$route.query.id ? "保存" : "发布";
    },
    isModify() {
      return this.$route.query.id !== undefined;
    },
  },

  methods: {
    initData(jobId) {
      // console.log("à1111111", this.$route);
      getCategoryTree().then(
        (response) => (this.jobCategoryOptions = response.data)
      );
      listByTypeWithSort(25, "-value").then(
        (response) =>
          (this.degreeOptions = this.amendOptions(response.data.list))
      );
      listByType(8).then(
        (response) =>
          (this.jobTypeOptions1 = this.amendjobTypeOptions(response.data.list))
      );
      listByTypeWithSort(13, "+value").then(
        (response) => (this.experienceOptions = response.data.list)
      );
      listByType(28).then(
        (response) => (this.languageOptions = response.data.list)
      );
      listByType(27).then(
        (response) => (this.additionOptions = response.data.list)
      );

      myCompanyInfo().then((response) => {
        if (response.data) {
          let info = response.data;
          this.companyInfo = info;
          this.companyInfo.logo = info.logo;
          this.companyInfo.id = info.id;
          this.companyInfo.name = info.name;
          this.companyInfo.property.name = info.property.name;
          this.companyInfo.stage.name = info.stage.name;
          this.companyInfo.industry.name = info.industry.name;
          this.companyInfo.scale.name = info.scale.name;
          this.companyInfo.homepage = info.homepage;
          this.companyLinkName = info.homepage.replace(/http(s)?:\/\//, "");
          this.companyLink = info.homepage.startsWith("http")
            ? info.homepage
            : "http://" + info.homepage;
        }
      });

      if (jobId) {
        getJobInfo(jobId).then((response) => {
          const { data } = response;
          console.log(data);
          // TODO 通过data.company.id 获取公司名称
          this.jobForm.companyId = data.company.id;
          // this.companyOptions = [{id: data.company.id, name: "字节跳动"}]
          this.jobForm.id = data.id;
          this.jobForm.name = data.name;
          this.jobForm.categoryId = data.category.id;
          this.jobForm.minDegreeId = data.minDegree
            ? data.minDegree.id
            : undefined;
          this.jobForm.salaryMonths = data.salaryMonths;
          this.jobForm.cityId = data.city ? data.city.id : undefined;
          // TODO 通过data.city.id 获取城市名称
          this.jobForm.address = data.address;
          this.jobForm.jobType = data.jobType ? data.jobType.id : undefined;
          this.jobForm.description = data.description;
          this.jobForm.recruitType = data.recruitType
            ? data.recruitType.id
            : undefined;
          this.jobForm.experienceId = data.experience
            ? data.experience.id
            : undefined;
          this.jobForm.skillTags = data.skillTags;
          this.jobForm.industryTags = data.industryTags;
          this.jobForm.keywords = data.skillTags.concat(data.industryTags);
          this.additionNames = data.additions.map((item) => item.name);
          this.jobForm.languageId = data.language
            ? data.language.id
            : undefined;

          getCategoryTree().then((response) => {
            this.jobCategoryOptions = response.data;

            // 查找分类id
            for (let i = 0; i < this.jobCategoryOptions.length; i++) {
              let first = this.jobCategoryOptions[i];
              for (let j = 0; j < first.children.length; j++) {
                let second = first.children[j];
                for (let k = 0; k < second.children.length; k++) {
                  let third = second.children[k];
                  if (third.id === this.jobForm.categoryId) {
                    this.jobForm.categoryId = [first.id, second.id, third.id];
                    this.onJobCategoryChange();
                    return;
                  }
                }
              }
            }
          });
        });
      } else if (this.$store.getters.jobDraft) {
        this.jobForm = this.$store.getters.jobDraft;
      }

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

      this.$axios
        .request({
          url: "/usercorporate/check-op",
          method: "get",
        })
        .then((data) => {
          this.isOP = data.data;
        });
    },

    amendOptions(options) {
      let validOptions = [];
      for (let i = 0; i < options.length; i++) {
        if (
          options[i].name !== "高中" &&
          options[i].name !== "专科" &&
          options[i].name !== "MBA" &&
          options[i].name !== "EMBA"
        ) {
          validOptions.push(options[i]);
        }
      }
      return validOptions;
    },

    amendjobTypeOptions(options) {
      let validOptions = [];
      for (let i = 0; i < options.length; i++) {
        if (options[i].name !== "不限") {
          validOptions.push(options[i]);
        }
      }
      return validOptions;
    },

    onSubmit() {
      this.$refs["jobForm"].validate((valid) => {
        if (valid) {
          this.$store.commit("setting/JOB_DRAFT", undefined);
          this.posting = true;
          this.jobForm.industryTags = this.industryList
            .filter((item) => item.selected)
            .map((item) => item.name);
          this.jobForm.skillTags = this.skillList
            .filter((item) => item.selected)
            .map((item) => item.name);
          this.jobForm.additionIds = this.additionOptions
            .filter((item) => this.additionNames.includes(item.name))
            .map((item) => item.id);
          this.jobForm.recruitType = this.isOP ? 172 : 0;
          console.log(this.jobForm);

          if (this.isOP) {
            opSaveJob(this.jobForm)
              .then(() => {
                Toast.success(this.isModify ? "保存成功" : "发布成功");
                this.$store.commit("setting/JOB_DRAFT", undefined);
                this.$router.go(-1);
              })
              .finally(() => {
                this.posting = false;
              });
          } else {
            saveJob(this.jobForm)
              .then(() => {
                Toast.success(this.isModify ? "保存成功" : "发布成功");
                this.$store.commit("setting/JOB_DRAFT", undefined);
                this.$router.go(-1);
              })
              .finally(() => {
                this.posting = false;
              });
          }
        }
      });
    },
    // 预览职位
    onPreview() {
      this.$refs["jobForm"].validate((valid) => {
        if (valid) {
          this.job.name = this.jobForm.name;
          this.job.minSalary = this.jobForm.minSalary;
          this.job.maxSalary = this.jobForm.maxSalary;
          this.job.salaryMonths = this.jobForm.salaryMonths;
          this.job.description = this.jobForm.description;
          this.job.additionNames = this.additionNames;
          this.nowDate = getNowDate();
          // 如果不是超级账户，获取公司名称
          if (!this.isOP) {
            this.job.companyName = this.companyInfo.name;
          }
          // 处理jobtype
          this.job.jobType = this.jobTypeOptions1.find(
            (option) => option.id === this.jobForm.jobType
          ).name;
          // 处理mindegree
          this.job.minDegree = this.degreeOptions.find(
            (option) => option.id === this.jobForm.minDegreeId
          ).name;
          // 处理experience
          this.job.experience = this.experienceOptions.find(
            (option) => option.id === this.jobForm.experienceId
          ).name;

          // 处理地址
          this.job.address = this.jobForm.address;
          this.job.latitude = this.jobForm.latitude;
          this.job.longitude = this.jobForm.longitude;
          if (
            this.job &&
            this.job.address &&
            this.job.latitude &&
            this.job.longitude
          ) {
            this.addr.mapWindow = {
              position: [this.job.longitude, this.job.latitude],
              content: this.job.address,
            };
          }

          this.dialogVisible2 = true;
        }
      });
    },

    // 显示关键选择框
    onKeywords() {
      this.dialogVisible = true;
    },

    dialogClose(done) {
      if (!this.dialogConfirm) {
        this.industryAdditionList.forEach((item) => {
          item.selected = false;
        });
        this.skillAdditionList.forEach((item) => {
          item.selected = false;
        });
        this.industryList.forEach((item) => {
          item.selected = false;
        });
        this.skillList.forEach((item) => {
          item.selected = false;
        });
      }
      done();
    },

    // 关键词选择确认
    onConfirm() {
      this.dialogVisible = false;
      this.dialogConfirm = true;
      let industry = this.industryList
        .filter((item) => item.selected)
        .map((item) => item.name);
      let skill = this.skillList
        .filter((item) => item.selected)
        .map((item) => item.name);
      let industryAddition = this.industryAdditionList
        .filter((item) => item.selected)
        .map((item) => item.name);
      let skillAddition = this.skillAdditionList
        .filter((item) => item.selected)
        .map((item) => item.name);
      this.jobForm.keywords = industry
        .concat(skill)
        .concat(industryAddition)
        .concat(skillAddition)
        .toString();
    },

    // 技能选择
    onSkillSelect(tag) {
      if (
        tag.selected === true ||
        this.skillAdditionList.filter((item) => item.selected).length +
          this.skillList.filter((item) => item.selected).length <
          4
      ) {
        tag.selected = !tag.selected;
      } else {
        this.$message.warning("最多选择4个技能");
      }
    },

    // 行业选择
    onIndustrySelect(tag) {
      if (
        tag.selected === true ||
        this.industryAdditionList.filter((item) => item.selected).length +
          this.industryList.filter((item) => item.selected).length <
          2
      ) {
        tag.selected = !tag.selected;
      } else {
        this.$message.warning("最多选择2个行业");
      }
    },

    // 删除用户自定义的行业标签
    onDeleteIndustryAddition(tag) {
      this.$axios
        .request({
          url: "/jobindustry/delete-addition",
          method: "post",
          params: { id: tag.id },
        })
        .then(() => {
          this.industryAdditionList = this.industryAdditionList.filter(
            (item) => item.id !== tag.id
          );
        });
    },

    // 删除用户自定义的技能标签
    onDeleteSkillAddition(tag) {
      this.$axios
        .request({
          url: "/jobskill/delete-addition",
          method: "post",
          params: { id: tag.id },
        })
        .then(() => {
          this.skillAdditionList = this.skillAdditionList.filter(
            (item) => item.id !== tag.id
          );
        });
    },

    // 删除关键词
    onDeleteTag(tag) {
      tag.selected = false;
    },

    // 选择职位分类，获取标签
    onJobCategoryChange() {
      if (this.jobForm.categoryId && this.jobForm.categoryId.length === 3) {
        this.secondCategoryId = this.jobForm.categoryId[1];
        let p1 = this.$axios.get("/jobskill/list", {
          params: { jobCategoryId: this.secondCategoryId }, // 取选中标签的父级id
        });
        let p2 = this.$axios.get("/jobindustry/list", {});
        let p3 = this.$axios.get("/jobindustry/list-addition", {});
        let p4 = this.$axios.get("/jobskill/list-addition", {
          params: { categoryId: this.secondCategoryId },
        });
        Promise.all([p1, p2, p3, p4]).then((result) => {
          if (result[0].data.length > 0 || result[1].data.length > 0) {
            this.jobFormRules.keywords = [
              { required: true, message: "请选择关键词", trigger: "blur" },
            ];
          } else {
            delete this.jobFormRules.keywords;
          }
          if (result[0].data.length > 0) {
            this.skillList = result[0].data.map((item) => {
              item.selected =
                this.jobForm.skillTags &&
                this.jobForm.skillTags.includes(item.name);
              return item;
            });
          }
          if (result[1].data.length > 0) {
            this.industryList = result[1].data.map((item) => {
              item.selected =
                this.jobForm.industryTags &&
                this.jobForm.industryTags.includes(item.name);
              return item;
            });
          }
          if (result[2].data.length > 0) {
            this.industryAdditionList = result[2].data.map((item) => {
              item.selected =
                this.jobForm.industryAdditionTags &&
                this.jobForm.industryAdditionTags.includes(item.name);
              return item;
            });
          }
          if (result[3].data.length > 0) {
            this.skillAdditionList = result[3].data.map((item) => {
              item.selected =
                this.jobForm.skillAdditionTags &&
                this.jobForm.skillAdditionTags.includes(item.name);
              return item;
            });
          }
        });
        this.$axios
          .get("/jobdescexample/list", {
            params: { categoryId: this.jobForm.categoryId[2] },
          })
          .then((data) => {
            this.jobDescription = data.data && data.data.description;
          });
        this.jobForm.categoryId = this.jobForm.categoryId[2];
      }
    },

    onEditorChange(event) {
      event.quill.deleteText(2000, 1);
      this.contentLength = event.quill.getLength() - 1;
    },

    remoteMethod(query) {
      if (query !== "") {
        this.searchTagLoading = true;
        this.$axios
          .request({
            url: "/category/tag-keywords",
            method: "get",
            params: { name: query, categoryId: this.jobForm.categoryId },
          })
          .then((data) => {
            this.searchTagLoading = false;
            this.searchTagOptions = [];
            this.searchTagOptions = this.searchTagOptions.concat(
              data.data.industryTags.map((item) => {
                let obj = {};
                obj.label = item;
                obj.value = "industry," + item;
                return obj;
              })
            );
            this.searchTagOptions = this.searchTagOptions.concat(
              data.data.skillTags.map((item) => {
                let obj = {};
                obj.label = item;
                obj.value = "skill," + item;
                return obj;
              })
            );
          });
      } else {
        this.searchTagOptions = [];
      }
    },

    onSearchTagChange(value) {
      let obj = value.split(",");
      let type = obj[0];
      let tag = obj[1];
      if (type === "industry") {
        if (this.industryList.filter((item) => item.selected).length < 2) {
          this.industryList.forEach((industry) => {
            if (industry.name === tag) {
              industry.selected = true;
            }
          });
        } else {
          this.$message.warning("最多选择2个行业");
        }
      } else if (type === "skill") {
        if (this.skillList.filter((item) => item.selected).length < 2) {
          this.skillList.forEach((skill) => {
            if (skill.name === tag) {
              skill.selected = true;
            }
          });
        } else {
          this.$message.warning("最多选择2个技能");
        }
      }
    },

    onIndustryKeywordsBlue() {
      setTimeout(() => {
        this.showIndustryAddButton = false;
      }, 200);
    },

    onSkillKeywordsBlue() {
      setTimeout(() => {
        this.showSkillAddButton = false;
      }, 200);
    },

    addIndustryKeywords() {
      if (
        this.industryAdditionList.filter((item) => item.selected).length +
          this.industryList.filter((item) => item.selected).length <
        2
      ) {
        this.$axios
          .request({
            url: "/jobindustry/save-addition",
            data: { name: this.industryKeywords },
            method: "post",
          })
          .then((data) => {
            this.industryAdditionList.push({
              id: data.data.id,
              name: data.data.name,
              selected: true,
            });
            this.industryKeywords = "";
          });
      } else {
        this.$message.warning("最多选择2个行业");
      }
    },

    addSkillKeywords() {
      if (
        this.skillAdditionList.filter((item) => item.selected).length +
          this.skillList.filter((item) => item.selected).length <
        4
      ) {
        this.$axios
          .request({
            url: "/jobskill/save-addition",
            data: {
              name: this.skillKeywords,
              categoryId: this.secondCategoryId,
            },
            method: "post",
          })
          .then((data) => {
            this.skillAdditionList.push({
              id: data.data.id,
              name: data.data.name,
              selected: true,
            });
            this.skillKeywords = "";
          });
      } else {
        this.$message.warning("最多选择4个技能");
      }
    },

    searchCompanyOptions(query) {
      this.$axios
        .request({
          url: "/company/search",
          params: { name: query },
          method: "get",
        })
        .then((response) => {
          this.companyOptions = response.data.list;
        });
    },

    onSalaryChange() {
      if (this.jobForm.minSalary >= this.jobForm.maxSalary) {
        this.jobForm.maxSalary = undefined;
      }
    },

    onSearchPoiResult(pois) {
      let latSum = 0;
      let lngSum = 0;
      if (pois.length > 0) {
        pois.forEach((poi) => {
          let { lng, lat } = poi;
          lngSum += lng;
          latSum += lat;
        });
        let center = {
          lng: lngSum / pois.length,
          lat: latSum / pois.length,
        };
        this.poiMapMarker.position = [center.lng, center.lat];
      }
    },

    getCompanyAddress(lng, lat) {
      // 这里通过高德 SDK 完成。
      let geocoder = new AMap.Geocoder({
        radius: 1000,
        extensions: "all",
      });
      geocoder.getAddress([lng, lat], (status, result) => {
        if (status === "complete" && result.info === "OK") {
          if (result && result.regeocode) {
            this.jobForm.address = result.regeocode.formattedAddress;
          }
        }
      });
    },

    getLocation() {
      let e = this.$refs["cityCascader"].getCheckedNodes()[0].pathLabels;
      // console.log("eeeeeeeee", e);
      if (e.length == 1) {
        this.job.city = e[0];
      } else if (e.length > 1) {
        this.job.city = e[1];
        let addr = e.join("");
        let geocoder = new AMap.Geocoder({});
        geocoder.getLocation(addr, (status, result) => {
          if (status === "complete" && result.geocodes.length) {
            let lnglat = result.geocodes[0].location;
            this.poiMapMarker.position = [lnglat.lng, lnglat.lat];
          }
        });
      }
    },
  },
};
</script>

<style scoped>
.app-container {
  margin: 0 auto;
  width: 1200px;
}

/deep/ .el-select-dropdown__wrap {
  max-width: 200px !important;
}

.text-input-width {
  width: 400px;
}

.salary-option {
  width: 120px;
}

.salary-text {
  font-size: 20px;
}

/deep/ .el-dialog__body {
  padding: 0 20px !important;
}
</style>

<style lang="scss" scoped>
@import "bootstrap/scss/bootstrap.scss";

.edit-job-container {
  .keywords-container {
    display: flex;
    align-items: center;

    span {
      margin-right: 20px;
    }
  }

  .position-world {
    width: 80px;
    height: 32px;
    border-radius: 16px;
    border: 1px dashed #409eff;
    display: flex;
    align-items: center;
    justify-content: center;

    .position-icon {
      font-size: 16px;
      color: #409eff;
    }
  }

  .dialog-container {
    .selected-container {
      width: 100%;
      display: flex;
      align-items: center;
      padding-bottom: 20px;
      border-bottom: 1px solid #e4e7ed;
      height: 50px;

      .selected-right {
        width: calc(100% - 70px);
        margin-left: 20px;

        /deep/ .el-tag {
          border: none;
          background: #409eff;
          margin-right: 8px;
          border-radius: 16px;
          line-height: 32px;
          color: white;

          /deep/ .el-icon-close {
            color: white;
          }

          /deep/ .el-icon-close:hover {
            color: white;
            background-color: #409eff;
          }
        }
      }
    }

    .industry-container {
      width: 100%;
      padding-top: 20px;

      .title-container {
        font-size: 15px;
        font-weight: bold;

        .title-tips {
          font-weight: 500;
          color: #909399;
        }
      }

      .industry-tag-container {
        width: 100%;
        padding-top: 20px;
        display: flex;
        flex-wrap: wrap;

        /deep/ .el-tag {
          background: #ffffff;
          margin-right: 8px;
          border-radius: 16px;
          line-height: 32px;
          color: #303133;
          border: 1px solid #e4e7ed;

          /deep/ .el-icon-close {
            color: white;
          }

          /deep/ .el-icon-close:hover {
            color: white;
            background-color: #409eff;
          }
        }

        .skill-style {
          margin-bottom: 10px;
        }

        .skill-style-select {
          background-color: #409eff;
          color: white;
        }

        .skill-style:hover {
          cursor: pointer;
        }
      }
    }

    .submit-container {
      padding-top: 30px;
    }
  }

  .ql-container .ql-editor {
    min-height: 200px !important;
    font-size: 15px;
  }

  .ql-editor p {
    margin-bottom: 10px;
  }

  .ql-editor ol,
  .ql-editor ul {
    padding-left: 0.5em;
    margin-bottom: 10px;
  }

  .ql-bubble {
    border: 1px solid #dcdfe6;
    border-radius: 4px;
  }

  .ql-editor.ql-blank::before {
    font-style: normal;
    color: #c0c4cc;
    font-size: 15px;
  }

  .salary-month-option {
    width: 140px;
  }

  .job-description-container {
    display: flex;
    flex-direction: column;
    align-items: flex-end;

    .job-description-show {
      width: 1098px;
      display: flex;
      align-items: center;
    }

    .job-description {
      width: 1098px;
      border: 1px solid #c0c4cc;
      padding: 15px;
      margin-bottom: 30px;
    }
  }
}

li.el-cascader-node {
  font-size: 15px;
  line-height: 40px;
  height: 40px;
}

button:focus {
  outline: 0;
}

.skill-keywords {
  width: 260px;

  /deep/ .el-input {
    width: 140px;
    border-radius: 20px;

    /deep/ .el-input__inner {
      border-radius: 20px;
    }
  }
}

.map-box {
  height: 200px;
}

.edit-map-box {
  height: 300px;
  position: relative;
  width: 680px;
}

.map-search-box {
  position: absolute;
  top: 16px;
  right: 16px;
}

.preview-box {
  background: #fff;
  padding: 20px;
  border: 1px solid #eee;
}

.preview-container {
  max-width: 1140px;
  margin: 0 auto;
  padding: 0 20px;
  min-height: calc(100vh - 477px);
  padding-top: 31px;
  padding-bottom: 194px;

  /deep/.el-card {
    background: #ffffff;
    box-shadow: 0px 4px 16px 3px rgba(191, 199, 215, 0.31);
  }

  .session1-container {
    height: 198px;
    display: flex;
    align-items: center;
    background: url("../assets/job/job-background.svg") no-repeat;
    background-size: 1200px 198px;

    .session1-left {
      padding: 28px 50px;
      display: flex;
      flex-direction: column;
      flex: 2;

      .text1 {
        font-size: 16px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #e0e0e0;
        line-height: 22px;
      }

      .session1-job-name {
        font-size: 24px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #fafafa;
        line-height: 33px;

        .salary-text {
          font-size: 24px;
          font-family: PingFangSC-Medium, PingFang SC;
          font-weight: 500;
          color: #eeff41;
          line-height: 33px;
        }
      }

      .text3 {
        font-size: 18px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #fafafa;
        line-height: 25px;
      }

      .text4 {
        font-size: 16px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #e0e0e0;
        line-height: 22px;
      }
    }

    .session1-right {
      display: flex;
      flex: 1;
      margin-left: 30px;
      /deep/.el-button {
        box-shadow: 0px 5px 13px 0px #2868c0;
        border: 0px;
        height: 45px;
        width: 45px;
      }

      /deep/.el-button + .el-button {
        margin-left: 0px;
      }

      .apply {
        /deep/ .el-button {
          background: #ffffff;
          box-shadow: 0px 5px 13px 0px #2868c0;
          border-radius: 20px;
          min-width: 125px;

          span {
            font-size: 16px;
            font-family: PingFangSC-Medium, PingFang SC;
            font-weight: 500;
            color: #333333;
            line-height: 22px;
          }
        }
      }
    }
  }

  .session2-container {
    display: flex;
    padding: 31px 68px 50px 50px;

    .session2-container-left {
      display: flex;
      flex-direction: column;
      flex: 3;
      margin-right: 50px;

      .session2-job-address {
        margin-top: 10px;

        span {
          line-height: 50px;
          margin-top: 10px;
        }
      }
    }

    .session2-container-bottom {
      display: none;
    }

    .divider {
      width: 1px;
      background: #cccccc;
      border-radius: 1px;
    }

    .session2-container-right {
      flex: 1;
      display: flex;
      flex-direction: column;
      margin-left: 50px;
      min-width: 200px;

      .session2-container-right-2 {
        margin-top: 60px;
        .address-label {
          display: flex;
          flex-direction: row;
          justify-content: flex-start;
          .address-label-icon-wrapper {
            position: relative;
            width: 40px;
            height: 19px;
          }
        }
        /deep/ .amap-logo {
          display: none !important;
        }

        /deep/ .amap-info {
          display: none !important;
        }
        /deep/ .el-vue-amap {
          background-color: white !important;
        }
        /deep/ .amap-maps {
          border-radius: 13px;
        }
      }

      .session2-container-right-3 {
        margin-top: 60px;
      }

      .basic-info {
        font-size: 18px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #333333;
        line-height: 25px;
      }

      .el-link {
        text-align: left;
        display: inline;

        .session2-logo-container {
          display: flex;
          align-items: flex-end;
          justify-content: flex-start;

          .session2-logo {
            width: 130px;
          }

          .company-text {
            font-size: 18px;
            padding-left: 12px;
          }
        }
      }

      .info-text {
        font-size: 16px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #333333;
        line-height: 22px;
      }

      .website-text {
        font-size: 16px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #4895ef;
        line-height: 22px;
      }
    }
  }
}
</style>
