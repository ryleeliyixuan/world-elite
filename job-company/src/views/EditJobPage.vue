<template>
  <div class="app-container edit-job-container">
    <h5 class="mb-4">{{title}}</h5>
    <el-form
      ref="jobForm"
      size="medium"
      :model="jobForm"
      :rules="jobFormRules"
      label-width="80px"
      label-position="left"
    >
      <el-form-item label="职位名称" prop="name">
        <el-input
          v-model="jobForm.name"
          maxlength="20"
          class="text-input-width"
          show-word-limit
          placeholder="请填写职位名称"
          :disabled="isModify"
        ></el-input>
      </el-form-item>
      <el-form-item label="职位类型" prop="categoryId">
        <el-cascader
          placeholder="选择职位类型"
          :show-all-levels="false"
          :options="jobCategoryOptions"
          :props="jobCatetoryProps"
          filterable
          clearable
          v-model="jobForm.categoryId"
          :disabled="isModify"
        ></el-cascader>
      </el-form-item>
      <el-form-item label="工作城市" prop="cityId">
        <el-select v-model="jobForm.cityId" filterable clearable placeholder="请选择工作城市">
          <el-option v-for="item in cityOptions" :key="item.id" :label="item.name" :value="item.id"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="工作地点" prop="address">
        <el-input
          v-model="jobForm.address"
          maxlength="250"
          class="text-input-width"
          show-word-limit
          placeholder="请填写工作地点"
        ></el-input>
      </el-form-item>
      <el-form-item label="学历要求" prop="minDegreeId">
        <el-select v-model="jobForm.minDegreeId" filterable clearable placeholder="请选择最低学历">
          <el-option
            v-for="item in degreeOptions"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="薪资待遇" prop="salary">
        <el-select v-model="jobForm.minSalary" placeholder="最低" class="salary-option">
          <el-option
            v-for="item in minSalaryOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>
        <el-select v-model="jobForm.maxSalary" placeholder="最高" class="ml-2 salary-option">
          <el-option
            v-for="item in maxSalaryOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>
        <span class="pl-2 pr-2">×</span>
        <el-select
          v-model="jobForm.salaryMonths"
          clearable
          placeholder="薪资月数(可选)"
          class="ml-2 salary-month-option"
        >
          <el-option
            v-for="item in salaryMonthOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="工作类型" prop="jobType">
        <el-radio-group v-model="jobForm.jobType">
          <el-radio
            :label="jobType.id"
            border
            v-for="jobType in jobTypeOptions"
            :key="jobType.id"
          >{{jobType.name}}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="职位描述" prop="description">
        <quill-editor v-model="jobForm.description" :options="descriptionEditorOption"></quill-editor>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :loading="posting" @click="onSubmit">{{pubButtonText}}</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { getCategoryTree } from "@/api/category_api";
import { listByType } from "@/api/dict_api";
import { saveJob, getJobInfo } from "@/api/job_api";
import Toast from "@/utils/toast";

import "quill/dist/quill.core.css";
import "quill/dist/quill.snow.css";
import "quill/dist/quill.bubble.css";
import { quillEditor } from "vue-quill-editor";

export default {
  name: "NewJobPage",
  components: {
    quillEditor
  },
  data() {
    return {
      jobForm: {
        id: undefined,
        name: undefined,
        categoryId: undefined,
        depart: undefined,
        minDegreeId: undefined,
        minSalary: undefined,
        maxSalary: undefined,
        salaryMonths: undefined,
        cityId: undefined,
        address: undefined,
        jobType: undefined,
        description: undefined,
        salary: undefined
      },
      jobFormRules: {
        name: [{ required: true, message: "请输入职位名称", trigger: "blur" }],
        categoryId: [
          { required: true, message: "请选择职位类型", trigger: "change" }
        ],
        minDegreeId: [
          { required: true, message: "请选择学历要求", trigger: "change" }
        ],
        cityId: [
          { required: true, message: "请选择工作城市", trigger: "change" }
        ],
        address: [
          { required: true, message: "请填写工作地点", trigger: "blur" }
        ],
        jobType: [
          { required: true, message: "请选择工作类型", trigger: "change" }
        ],
        description: [
          { required: true, message: "请输入职位描述", trigger: "blur" }
        ],
        salary: [{ required: true, message: "请选择薪资范围", trigger: "blur" }]
      },
      jobCategoryOptions: [],
      jobCatetoryProps: {
        expandTrigger: "hover",
        value: "id",
        label: "name",
        emitPath: false,
        children: "children"
      },
      cityOptions: [],
      degreeOptions: [],
      minSalaryOptions: [],
      maxSalaryOptions: [],
      salaryMonthOptions: [],
      jobTypeOptions: [],
      descriptionEditorOption: {
        theme: "snow",
        placeholder: "岗位职责，任职要求等",
        modules: {
          toolbar: [["bold"], [{ list: "ordered" }, { list: "bullet" }]]
        }
      },
      posting: false
    };
  },
  created() {
    const jobId = this.$route.query.id;
    this.initData(jobId);
  },
  watch: {
    "jobForm.minSalary": function() {
      this.maxSalaryOptions = this.generateSalaryOptions(
        this.jobForm.minSalary,
        this.jobForm.minSalary < 10 ? 5 : 10
      );
      this.jobForm.salary =
        this.jobForm.minSalary && this.jobForm.maxSalary ? 1 : undefined;
    },
    "jobForm.maxSalary": function() {
      this.jobForm.salary =
        this.jobForm.minSalary && this.jobForm.maxSalary ? 1 : undefined;
    },
    jobForm: {
      handler() {
        if (this.jobForm.id === undefined) {
          this.$store.commit("setting/JOB_DRAFT", this.jobForm);
        }
      },
      deep: true
    }
  },
  computed: {
    title() {
      return this.$route.query.id ? "编辑职位" : "新增职位";
    },
    pubButtonText() {
      return this.$route.query.id ? "保存" : "发布";
    },
    isModify() {
      return this.$route.query.id !== undefined;
    }
  },
  methods: {
    initData(jobId) {
      getCategoryTree().then(
        response => (this.jobCategoryOptions = response.data)
      );
      listByType(1).then(response => (this.degreeOptions = response.data.list));
      listByType(8).then(
        response => (this.jobTypeOptions = response.data.list)
      );
      listByType(2).then(response => (this.cityOptions = response.data.list));
      this.minSalaryOptions = this.generateSalaryOptions(0, 250);
      this.salaryMonthOptions = this.generateSalaryMonthOptions(11, 13);
      if (jobId) {
        getJobInfo(jobId).then(response => {
          const { data } = response;
          this.jobForm.id = data.id;
          this.jobForm.name = data.name;
          this.jobForm.categoryId = data.category.id;
          this.jobForm.depart = data.depart;
          this.jobForm.minDegreeId = data.minDegree
            ? data.minDegree.id
            : undefined;
          this.jobForm.minSalary = data.minSalary;
          this.jobForm.maxSalary = data.maxSalary;
          this.jobForm.salaryMonths = data.salaryMonths;
          this.jobForm.cityId = data.city ? data.city.id : undefined;
          this.jobForm.address = data.address;
          this.jobForm.jobType = data.jobType ? data.jobType.id : undefined;
          this.jobForm.description = data.description;
        });
      } else if (this.$store.getters.jobDraft) {
        this.jobForm = this.$store.getters.jobDraft;
      }
    },
    onSubmit() {
      this.$refs["jobForm"].validate(valid => {
        if (valid) {
          this.$store.commit("setting/JOB_DRAFT", undefined);
          this.posting = true;
          saveJob(this.jobForm)
            .then(() => {
              Toast.success(this.isModify ? "保存成功" : "发布成功");
              this.$router.go(-1);
            })
            .finally(() => {
              this.posting = false;
            });
        }
      });
    },
    generateSalaryOptions(minVal, length) {
      const salaryOptions = [];
      let step;

      if (minVal < 30) {
        step = 1;
      } else if (minVal < 50) {
        step = 2;
      } else if (minVal < 80) {
        step = 5;
      } else {
        step = 10;
      }
      for (let i = 0; i != length; i++) {
        minVal = minVal + step;
        salaryOptions.push({
          label: minVal + "k",
          value: minVal
        });
      }
      return salaryOptions;
    },
    generateSalaryMonthOptions(minVal, length) {
      const salaryMonthOptions = [];
      for (let i = 0; i != length; i++) {
        minVal = minVal + 1;
        salaryMonthOptions.push({
          label: minVal + "个月",
          value: minVal
        });
      }
      return salaryMonthOptions;
    }
  }
};
</script>

<style scoped>
.app-container {
  margin: 0px 100px;
  width: 800px;
}
.text-input-width {
  width: 400px;
}
.salary-option {
  width: 120px;
}
</style>

<style lang="scss">
@import "bootstrap/scss/bootstrap.scss";

.edit-job-container {
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
}

li.el-cascader-node{
  font-size: 15px;
  line-height: 40px;
  height: 40px;
}
</style>