<template>
  <div class="app-container edit-activity">
    <el-form
      :model="activityForm"
      :rules="activityFormRules"
      ref="activityForm"
      label-width="100px"
    >
      <el-form-item label="活动图片" prop="thumbnail">
        <el-upload
          class="thumbnail-uploader"
          :action="uploadPicOptions.action"
          :data="uploadPicOptions.params"
          :accept="uploadPicOptions.acceptFileType"
          :show-file-list="false"
          :on-success="handleUploadSuccess"
          :before-upload="beforeUpload"
        >
          <el-image
            v-if="activityForm.thumbnail && activityForm.thumbnail !== ''"
            :src="activityForm.thumbnail"
            class="thumbnail"
          />
          <i v-else class="el-icon-plus thumbnail-uploader-icon"></i>
        </el-upload>
      </el-form-item>
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
      <el-form-item label="活动摘要" prop="summary">
        <el-input
          type="textarea"
          placeholder="输入活动摘要"
          v-model="activityForm.summary"
          maxlength="200"
          rows="3"
          show-word-limit
        ></el-input>
      </el-form-item>
      <el-form-item label="活动详情" prop="description">
        <div class="quill-wrap">
          <quill-editor
            v-model="activityForm.description"
            ref="myQuillEditor"
            :options="editorOption"
          ></quill-editor>
          <el-upload
            style="display:none"
            ref="upload"
            :action="uploadPicOptions.action"
            :data="uploadPicOptions.params"
            :accept="uploadPicOptions.acceptFileType"
            :show-file-list="false"
            :on-success="handleEditorUploadSuccess"
            :before-upload="beforeUpload"
          >
            <i class="el-icon-plus avatar-uploader-icon" id="imgInput"></i>
          </el-upload>
        </div>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSave" :loading="saveLoading">保存</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import "quill/dist/quill.core.css";
import "quill/dist/quill.snow.css";
import { quillEditor } from "vue-quill-editor";
import Quill from "quill";
import ImageResize from "quill-image-resize-module";
import { saveActivity, getActivityInfo } from "@/api/activity_api";
import { listByType } from "@/api/dict_api";
import { getUploadPicToken } from "@/api/upload_api";

Quill.register("modules/imageResize", ImageResize);

export default {
  name: "ActivityList",
  components: {
    quillEditor
  },
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
        address: undefined
      },
      activityFormRules: {
        title: [{ required: true, message: "请输入活动名称", trigger: "blur" }],
        thumbnail: [
          { required: true, message: "请输入活动图片", trigger: "change" }
        ],
        summary: [
          { required: true, message: "请输入活动文案", trigger: "blur" }
        ],
        description: [
          { required: true, message: "请输入活动详情", trigger: "blur" }
        ],
        cityId: [
          { required: true, message: "请选择活动城市", trigger: "change" }
        ],
        timeRange: [
          { required: true, message: "请选择活动时间", trigger: "change" }
        ],
        address: [
          { required: true, message: "请输入活动地址", trigger: "blur" }
        ]
      },
      cityOptions: [],
      uploadPicOptions: {
        action: "",
        params: {},
        fileUrl: "",
        acceptFileType: ".jpg,.jpeg,.png,.JPG,.JPEG,.PNG"
      },
      editorOption: {
        theme: "snow",
        placeholder: "活动详情",
        modules: {
          imageResize: true,
          toolbar: [
            ["bold", "italic", "underline", "strike"], // toggled buttons
            ["blockquote", "code-block"],
            [{ header: 1 }, { header: 2 }], // custom button values
            [{ list: "ordered" }, { list: "bullet" }],
            [{ script: "sub" }, { script: "super" }], // superscript/subscript
            [{ indent: "-1" }, { indent: "+1" }], // outdent/indent
            [{ header: [1, 2, 3, 4, 5, 6, false] }],
            [{ color: [] }, { background: [] }], // dropdown with defaults from theme
            [{ align: [] }],
            ["link", "image"],
            ["clean"]
          ]
        }
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
          this.activityForm.timeRange = [
            this.activityForm.startTime,
            this.activityForm.finishTime
          ];
        });
      }
    },
    handleSave() {
      this.$refs["activityForm"].validate(valid => {
        if (valid) {
          this.saveLoading = true;
          saveActivity(this.activityForm)
            .then(() => {
              this.$message("保存成功");
              this.$router.go(-1);
            })
            .finally(() => {
              this.saveLoading = false;
            });
        }
      });
    },
    beforeUpload(file) {
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
    },
    handleEditorUploadSuccess() {
      const selection = this.$refs.myQuillEditor.quill.getSelection();
      this.$refs.myQuillEditor.quill.insertEmbed(
        selection !== null ? selection.index : 0,
        "image",
        this.uploadPicOptions.fileUrl,
        Quill.sources.USER
      );
      this.$refs["upload"].clearFiles();
    },
    imgHandler(state) {
      if (state) {
        let fileInput = document.getElementById("imgInput");
        fileInput.click(); // 加一个触发事件
      }
    }
  },
  mounted() {
    this.$refs.myQuillEditor.quill
      .getModule("toolbar")
      .addHandler("image", this.imgHandler);
  }
};
</script>

<style lang="scss">
.edit-activity {
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

  .ql-container .ql-editor {
    min-height: calc(100vh - 220px);
    font-size: 15px;
  }
  .ql-toolbar {
    line-height: normal;
  }
  .ql-editor p {
    margin-bottom: 10px;
  }
  .ql-editor ol,
  .ql-editor ul {
    padding-left: 0.5em;
    margin-bottom: 10px;
  }
}
</style>