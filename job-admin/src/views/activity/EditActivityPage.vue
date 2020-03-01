<template>
  <div class="app-container edit-activity">
    <el-form
      :model="activityForm"
      :rules="activityFormRules"
      ref="activityForm"
      label-width="100px"
    >
      <el-form-item label="活动名称" prop="title">
        <el-input v-model="activityForm.title" placeholder="填写活动名称" maxlength="80" show-word-limit></el-input>
      </el-form-item>
      <el-form-item label="活动城市" prop="cityId">
        <el-select v-model="activityForm.cityId" filterable clearable placeholder="所在城市">
          <el-option v-for="item in cityOptions" :key="item.id" :label="item.name" :value="item.id"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="活动地点" prop="address">
        <el-input
          v-model="activityForm.address"
          placeholder="填写活动地点"
          maxlength="100"
          show-word-limit
        ></el-input>
      </el-form-item>
      <el-form-item label="活动时间" prop="timeRange">
        <el-date-picker
          v-model="activityForm.timeRange"
          value-format="yyyy-MM-dd HH:mm"
          format="yyyy-MM-dd HH:mm"
          type="datetimerange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="报名链接" prop="url">
        <el-input v-model="activityForm.url" placeholder="填写报名链接" maxlength="100" show-word-limit></el-input>
      </el-form-item>
      <el-form-item label="活动文案" prop="summary">
        <el-input
          type="textarea"
          placeholder="请输入内容"
          v-model="activityForm.summary"
          maxlength="500"
          rows="5"
          show-word-limit
        ></el-input>
      </el-form-item>
      <el-form-item label="活动图片" prop="thumbnail">
        <el-upload
          class="thumbnail-uploader"
          :action="uploadPicOptions.action"
          :data="uploadPicOptions.params"
          :accept="uploadPicOptions.acceptFileType"
          :show-file-list="false"
          :on-success="handleUploadSuccess"
          :before-upload="beforePicUpload"
        >
          <el-image
            v-if="activityForm.thumbnail && activityForm.thumbnail !== ''"
            :src="activityForm.thumbnail"
            class="thumbnail"
          />
          <i v-else class="el-icon-plus thumbnail-uploader-icon"></i>
        </el-upload>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSave" :loading="saveLoading">保存</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { saveActivity, getActivityInfo } from "@/api/activity_api";
import { listByType } from "@/api/dict_api";
import { getUploadPicToken } from "@/api/upload_api";

export default {
  name: "ActivityList",
  data() {
    return {
      activityForm: {
        id: undefined,
        title: undefined,
        thumbnail: undefined,
        summary: undefined,
        cityId: undefined,
        description: undefined,
        startTime: undefined,
        finishTime: undefined,
        timeRange: [],
        address: undefined,
        url: undefined
      },
      activityFormRules: {
        title: [{ required: true, message: "请输入活动名称", trigger: "blur" }],
        thumbnail: [
          { required: true, message: "请输入活动图片", trigger: "change" }
        ],
        summary: [
          { required: true, message: "请输入活动文案", trigger: "blur" }
        ],
        cityId: [
          { required: true, message: "请选择活动城市", trigger: "change" }
        ],
        timeRange: [
          { required: true, message: "请选择活动时间", trigger: "change" }
        ],
        address: [
          { required: true, message: "请输入活动地址", trigger: "blur" }
        ],
        url: [{ required: true, message: "请输入报名链接", trigger: "blur" }]
      },
      cityOptions: [],
      uploadPicOptions: {
        action: "",
        params: {},
        fileUrl: "",
        acceptFileType: ".jpg,.jpeg,.png,.JPG,.JPEG,.PNG"
      },
      saveLoading: false
    };
  },
  created() {
    this.initData();
  },
  watch: {
    "activityForm.timeRange": function() {
      if (
        this.activityForm.timeRange &&
        this.activityForm.timeRange.length == 2
      ) {
        this.activityForm.startTime = this.activityForm.timeRange[0];
        this.activityForm.finishTime = this.activityForm.timeRange[1];
      } else {
        this.activityForm.startTime = undefined;
        this.activityForm.finishTime = undefined;
      }
    }
  },
  methods: {
    initData() {
      listByType(2).then(response => (this.cityOptions = response.data.list));
      this.getActivityInfo();
    },
    getActivityInfo() {
      const activityId = this.$route.query.id;
      if (activityId) {
        getActivityInfo(activityId).then(response => {
          const { data } = response;
          Object.keys(data).forEach(key => {
            this.activityForm[key] = data[key];
          });
          this.activityForm.cityId = data.city.id;
          this.activityForm.timeRange = [this.activityForm.startTime, this.activityForm.finishTime];
        });
      }
    },
    handleSave() {
      this.saveLoading = true;
      this.$refs['activityForm'].validate(valid => {
        if (valid) {
          saveActivity(this.activityForm).then(() => {
            this.$message("保存成功");
            this.$router.go(-1);
          }).finally(() => {
              this.saveLoading = false
          });
        }
      });
    },
    beforePicUpload(file) {
      return new Promise((resolve, reject) => {
        getUploadPicToken(file.name)
          .then(response => {
            const { data } = response;
            this.uploadPicOptions.action = data.host;
            this.uploadPicOptions.params = data;
            this.uploadPicOptions.fileUrl = data.host + "/" + data.key;
            resolve(data);
          })
          .catch(error => {
            reject(error);
          });
      });
    },
    handleUploadSuccess() {
      this.activityForm.thumbnail = this.uploadPicOptions.fileUrl;
    }
  }
};
</script>

<style lang="scss">
.edit-activity {
  width: 60%;

  .thumbnail-uploader .el-upload {
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }

  .thumbnail-uploader .el-upload:hover {
    border-color: #ccc;
  }

  $avatarSize: 200px;

  .thumbnail-uploader .thumbnail-uploader-icon {
    border: 1px dashed #d9d9d9;
    font-size: 28px;
    color: #8c939d;
    width: $avatarSize;
    height: $avatarSize;
    line-height: $avatarSize;
    text-align: center;
  }

  .thumbnail-uploader .thumbnail {
    width: $avatarSize;
    height: $avatarSize;
    display: block;
  }
}
</style>