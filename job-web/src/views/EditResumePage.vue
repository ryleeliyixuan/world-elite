<template>
  <div class="app-container">
    <b-container>
      <b-row>
        <b-col cols="8">
          <div class="resume-body">
            <div class="resume-box">
              <EditResumeTitle
                title="基本信息"
                :icon="['fa', 'pencil-alt']"
                iconText="编辑"
                v-on:edit-click="handleEditResumeBasic"
              />
              <b-media class="mt-3">
                <template v-slot:aside>
                  <b-img blank blank-color="#ccc" width="100" alt="placeholder"></b-img>
                </template>
                <h4>
                  {{resume.name}}
                  <font-awesome-icon
                    :icon="['fa', 'male']"
                    class="text-primary gender-text"
                    v-if="resume.gender === 1"
                  />
                  <font-awesome-icon
                    :icon="['fa', 'female']"
                    class="text-danger gender-text"
                    v-else
                  />
                </h4>
                <p>
                  <span v-if="resume.graduateTime">{{resume.graduateTime}}毕业 / </span>
                  <span v-if="resume.curPlace">现居{{resume.curPlace}} / </span>
                  <span v-if="resume.returnTime">{{resume.returnTime}}回国 / </span>
                  <span v-if="resume.age">{{resume.age}}岁</span>
                </p>
                <p>
                  <span class="mr-3">
                    <font-awesome-icon :icon="['fa', 'envelope']" />
                    {{resume.email}}
                  </span>
                  <span class="ml-3" v-if="resume.phone && resume.phone != 0">
                    <font-awesome-icon :icon="['fa', 'mobile-alt']" />
                    +{{resume.phoneCode}} {{resume.phone}}
                  </span>
                </p>
              </b-media>
            </div>
            <div class="resume-box">
              <EditResumeTitle
                title="自我介绍"
                :icon="['fa', 'pencil-alt']"
                iconText="编辑"
                v-on:edit-click="handleEditResumeIntro"
              />
              <div class="mt-3 description-text" v-html="resume.introduction"></div>
            </div>
            <div class="resume-box">
              <EditResumeTitle
                title="教育经历"
                :icon="['fa', 'plus-square']"
                iconText="添加"
                v-on:edit-click="handleEditResumeEdu('create')"
              />
              <div class="mt-3">
                <b-media
                  class="edit-box pt-2"
                  v-for="resumeEdu in resume.resumeEduList"
                  :key="resumeEdu.id"
                >
                  <h6>
                    {{resumeEdu.schoolName}}
                    <span
                      class="ml-3 time-text"
                    >{{resumeEdu.startTime}}到{{resumeEdu.finishTime}}</span>
                  </h6>
                  <p>{{resumeEdu.degree.name}} / {{resumeEdu.majorName}} / GPA {{resumeEdu.gpa}}</p>
                  <div class="edit-item-box">
                    <a class="p-2" v-on:click="handleEditResumeEdu('update', resumeEdu)">
                      <font-awesome-icon :icon="['fa', 'pencil-alt']" />
                    </a>
                    <a class="p-2">
                      <font-awesome-icon :icon="['fa', 'trash-alt']" />
                    </a>
                  </div>
                </b-media>
              </div>
            </div>
            <div class="resume-box">
              <EditResumeTitle title="工作经验" :icon="['fa', 'plus-square']" iconText="添加" />
              <div class="mt-3">
                <b-media class="edit-box pt-2 pb-4">
                  <h6>
                    广州市不待信息科技有限公司
                    <span class="ml-3 position-text">研发部 . Java工程师</span>
                    <span class="ml-3 time-text">2019.01-2020.01</span>
                  </h6>
                  <div class="description-text mt-3">
                    1.为公司的日常账务管理、资产结账、成本核算等板块提供支持和运营管理。
                    2.熟练掌握SAP系统；负责固定资产采购订单签核、AP、转固、处置、减值等日常账务处理。
                    3.每月固定资产结账，编制管理报表；每月编制资本支出、折旧滚动预测报表；编制年度资本预算；组织固定资产盘点。
                    4.负责成本核算；参与年度预算编制，年度预算标准成本制定与维护，生产成本及销售成本核算，月末结账，编制相关管理报表及盈利预测表，完成主管交代的其他工作。
                  </div>
                </b-media>
                <b-media class="edit-box pt-2">
                  <h6>
                    广州市不待信息科技有限公司
                    <span class="ml-3 position-text">研发部 . Java工程师</span>
                    <span class="ml-3 time-text">2019.01-2020.01</span>
                  </h6>
                  <div class="description-text mt-3">
                    1.为公司的日常账务管理、资产结账、成本核算等板块提供支持和运营管理。
                    2.熟练掌握SAP系统；负责固定资产采购订单签核、AP、转固、处置、减值等日常账务处理。
                    3.每月固定资产结账，编制管理报表；每月编制资本支出、折旧滚动预测报表；编制年度资本预算；组织固定资产盘点。
                    4.负责成本核算；参与年度预算编制，年度预算标准成本制定与维护，生产成本及销售成本核算，月末结账，编制相关管理报表及盈利预测表，完成主管交代的其他工作。
                  </div>
                </b-media>
              </div>
            </div>
            <div class="resume-box">
              <EditResumeTitle title="校园实践" :icon="['fa', 'plus-square']" iconText="添加" />
              <div class="mt-3"></div>
            </div>
          </div>
        </b-col>
        <b-col>
          <div class="resume-objective">
            <b-row>
              <b-col cols="auto" class="mr-auto">
                <h3>求职意向</h3>
              </b-col>
              <b-col cols="auto" align-self="center">
                <a>
                  <font-awesome-icon :icon="['fa', 'edit']" />编辑
                </a>
              </b-col>
            </b-row>
            <div class="mt-3">
              <p>广州 / 深圳 / 北京</p>
              <p>互联网 / 通信 / 制造业</p>
              <p>软件工程师 / Java工程师 / Android开发</p>
              <p>15K ~ 20K</p>
            </div>
          </div>
          <div class="resume-preview">
            <p>简历完成度</p>
            <b-progress :value="80" :max="100" show-value class="mb-3" variant="info"></b-progress>
            <b-button variant="info" block>预览简历</b-button>
          </div>
          <div class="resume-nav" v-sticky="{stickyTop: 2}">
            <b-nav vertical>
              <b-nav-item active>基本信息</b-nav-item>
              <b-nav-item>自我介绍</b-nav-item>
              <b-nav-item>教育经历</b-nav-item>
              <b-nav-item>工作经历</b-nav-item>
            </b-nav>
          </div>
        </b-col>
      </b-row>
    </b-container>
    <!--Basic Info-->
    <el-dialog title="基本信息" :visible.sync="showBasicDialog" width="600px" top="10vh">
      <el-form ref="resumeForm" :model="resumeForm" label-width="80px">
        <el-form-item label="姓名">
          <el-input v-model="resumeForm.name" placeholder="你的真实姓名" maxlength="20" show-word-limit></el-input>
        </el-form-item>
        <el-form-item label="生日">
          <el-date-picker
            v-model="resumeForm.birth"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择日期"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="resumeForm.gender">
            <el-radio :label="1" border>男</el-radio>
            <el-radio :label="2" border>女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="所在城市">
          <el-input
            v-model="resumeForm.curPlace"
            placeholder="比如：洛杉矶"
            maxlength="50"
            show-word-limit
          ></el-input>
        </el-form-item>
        <el-form-item label="毕业时间">
          <el-date-picker
            v-model="resumeForm.graduateTime"
            type="month"
            value-format="yyyy-MM"
            placeholder="选择日期"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="回国时间">
          <el-date-picker
            v-model="resumeForm.returnTime"
            type="month"
            placeholder="选择日期"
            value-format="yyyy-MM"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input
            placeholder="请输入电话号码"
            type="number"
            v-model="resumeForm.phone"
            maxlength="20"
            show-word-limit
          >
            <el-select
              v-model="resumeForm.phoneCode"
              slot="prepend"
              placeholder="电话区号"
              clearable
              filterable
            >
              <el-option
                :value="country.phoneCode"
                :label="country.chineseName + '+' + country.phoneCode"
                v-for="country in countryOptions"
                :key="country.id"
              >
                <span style="float: left">{{ country.chineseName }}</span>
                <span style="float: right; color: #8492a6; font-size: 13px">+{{ country.phoneCode }}</span>
              </el-option>
            </el-select>
          </el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="showBasicDialog = false">取 消</el-button>
        <el-button type="primary" @click="handleSaveResumeBasic" :loading="posting">保 存</el-button>
      </span>
    </el-dialog>
    <!--编辑自我介绍-->
    <el-dialog title="自我介绍" :visible.sync="showIntroDialog" width="650px" top="10vh">
      <quill-editor v-model="resumeForm.introduction" :options="editorOption"></quill-editor>
      <span slot="footer" class="dialog-footer">
        <el-button @click="showIntroDialog = false">取 消</el-button>
        <el-button type="primary" @click="handleSaveResumeBasic" :loading="posting">保 存</el-button>
      </span>
    </el-dialog>
    <!--编辑教育经历-->
    <el-dialog title="添加教育经历" :visible.sync="showEduDialog" width="500px" top="10vh">
      <el-form ref="resumeEduForm" :model="resumeEduForm" label-width="80px">
        <el-form-item label="学校名称">
          <el-autocomplete
            v-model="resumeEduForm.schoolName"
            :fetch-suggestions="searchSchoolName"
            value-key="name"
            placeholder="请输入学校名称"
            class="w-100"
          />
        </el-form-item>
        <el-form-item label="在校时间">
          <el-col :span="11">
            <el-date-picker
              type="month"
              placeholder="入校时间"
              value-format="yyyy-MM"
              v-model="resumeEduForm.startTime"
              class="w-100"
            />
          </el-col>
          <el-col class="text-center" :span="2">-</el-col>
          <el-col :span="11">
            <el-date-picker
              type="month"
              value-format="yyyy-MM"
              placeholder="毕业时间"
              v-model="resumeEduForm.finishTime"
              class="w-100"
            />
          </el-col>
        </el-form-item>
        <el-form-item label="学历">
          <el-select v-model="resumeEduForm.degreeId" placeholder="请选择学历">
            <el-option
              :label="dict.name"
              :value="dict.id"
              v-for="dict in degreeOptions"
              :key="dict.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="专业">
          <el-input v-model="resumeEduForm.majorName" placeholder="请输入所学专业"></el-input>
        </el-form-item>
        <el-form-item label="GPA">
          <el-input v-model="resumeEduForm.gpa" type="number" placeholder="请输入GPA"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="showEduDialog = false">取 消</el-button>
        <el-button type="primary" @click="handleSaveResumeEdu" :loading="posting">保 存</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import VueSticky from "vue-sticky";
import EditResumeTitle from "@/components/EditResumeTitle";
import { library } from "@fortawesome/fontawesome-svg-core";
import Vue from "vue";
import ElementUI from "element-ui";
import "element-ui/lib/theme-chalk/index.css";
import { getAllCountries, getCurrentCountry } from "@/api/country_api";
import {
  getResumeInfo,
  saveResumeBasic,
  saveResumeEdu
} from "@/api/resume_api";
import { searchSchool } from "@/api/school_api";
import { listByType } from "@/api/dict_api";

import "quill/dist/quill.core.css";
import "quill/dist/quill.snow.css";
import "quill/dist/quill.bubble.css";
import { quillEditor } from "vue-quill-editor";

import {
  faEdit,
  faFemale,
  faMale,
  faMobileAlt,
  faEnvelope,
  faPlusSquare,
  faPencilAlt,
  faTrashAlt
} from "@fortawesome/free-solid-svg-icons";

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

Vue.use(ElementUI);

export default {
  name: "EditResumePage",
  data() {
    return {
      resumeForm: {
        id: undefined,
        name: "",
        birth: undefined,
        gender: 1,
        maritalStatus: 0,
        returnTime: undefined,
        graduateTime: undefined,
        curPlace: "",
        phoneCode: undefined,
        phone: undefined,
        introduction: ""
      },
      resumeEduForm: {
        id: undefined,
        resumeId: undefined,
        schoolName: undefined,
        majorName: undefined,
        degreeId: undefined,
        startTime: undefined,
        finishTime: undefined,
        gpa: undefined
      },
      resume: {},
      countryOptions: [],
      degreeOptions: [],
      showBasicDialog: false,
      showIntroDialog: false,
      showEduDialog: false,
      posting: false,
      editorOption: {
        theme: "snow",
        placeholder: "关于你的能力，特长和成就。",
        modules: {
          toolbar: [["bold"], [{ list: "ordered" }, { list: "bullet" }]]
        }
      }
    };
  },
  directives: {
    sticky: VueSticky
  },
  components: {
    EditResumeTitle,
    quillEditor
  },
  created() {
    this.initData();
  },
  methods: {
    initData() {
      getAllCountries().then(response => {
        this.countryOptions = response.data;
      });
      getCurrentCountry().then(response => {
        const { data } = response.data;
        if (data && data.phoneCode && this.resumeForm.phoneCode === undefined) {
          this.resumeForm.phoneCode = data.phoneCode;
        }
      });
      listByType(1).then(response => (this.degreeOptions = response.data.list));
      this.getResumeInfo();
    },
    getResumeInfo() {
      getResumeInfo().then(response => {
        this.resume = response.data;
      });
    },
    setResumeFormValues() {
      this.resumeForm.id = this.resume.id;
      this.resumeForm.name = this.resume.name;
      this.resumeForm.birth = this.resume.birth;
      this.resumeForm.gender = this.resume.gender;
      this.resumeForm.returnTime = this.resume.returnTime;
      this.resumeForm.graduateTime = this.resume.graduateTime;
      this.resumeForm.curPlace = this.resume.curPlace;
      this.resumeForm.phoneCode = this.resume.phoneCode;
      this.resumeForm.phone = this.resume.phone;
      this.resumeForm.introduction = this.resume.introduction;
    },
    handleEditResumeBasic() {
      this.setResumeFormValues();
      this.showBasicDialog = true;
    },
    handleEditResumeIntro() {
      this.setResumeFormValues();
      this.showIntroDialog = true;
    },
    handleEditResumeEdu(type, resumeEdu) {
      this.showEduDialog = true;
      this.resumeEduForm.resumeId = this.resume.id;
      if (type === "update") {
         this.resumeEduForm.id = resumeEdu.id
         this.resumeEduForm.schoolName = resumeEdu.schoolName
         this.resumeEduForm.majorName = resumeEdu.majorName
         this.resumeEduForm.degreeId = resumeEdu.degree.id
         this.resumeEduForm.startTime = resumeEdu.startTime
         this.resumeEduForm.finishTime = resumeEdu.finishTime
         this.resumeEduForm.gpa = resumeEdu.gpa
      }
    },
    handleSaveResumeBasic() {
      this.posting = true;
      saveResumeBasic(this.resumeForm)
        .then(() => {
          this.showBasicDialog = false;
          this.showIntroDialog = false;
          this.getResumeInfo();
        })
        .finally(() => {
          this.posting = false;
        });
    },
    handleSaveResumeEdu() {
      this.posting = true;
      saveResumeEdu(this.resumeEduForm)
        .then(() => {
          this.getResumeInfo();
          this.showEduDialog = false;
        })
        .finally(() => {
          this.posting = false;
        });
    },
    searchSchoolName(keyword, cb) {
      if (keyword.length < 2) {
        return;
      }
      searchSchool(keyword).then(response => {
        cb(response.data);
      });
    }
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
$border-style: 1px solid #eee;

.app-container {
  margin: 20px auto;
}

.resume-body {
  background: #fff;
  padding: 20px;
  border: $border-style;
}

.resume-objective {
  background: #fff;
  padding: 20px;
  border: $border-style;
}

.resume-nav {
  background: #fff;
  padding: 20px;
  margin-top: 20px;
  border: $border-style;
}

.resume-preview {
  background: #fff;
  padding: 20px;
  margin-top: 20px;
  border: $border-style;
}

.resume-box {
  margin-bottom: 48px;
}

.time-text {
  font-size: 14px;
  font-weight: 400;
}

.position-text {
  font-size: 14px;
  font-weight: 500;
}

.gender-text {
  font-size: 16px;
}

.description-text {
  font-size: 15px;
  line-height: 1.7em;
}

.edit-box {
  position: relative;
}

.edit-item-box {
  position: absolute;
  right: 10px;
  top: 10px;
  background: #fff;
}

.el-input .el-select {
  width: 120px;
}
</style>

<style lang="scss">
.ql-container .ql-editor {
  min-height: 200px;
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
.b-toaster {
  z-index: 10000;
}
</style>