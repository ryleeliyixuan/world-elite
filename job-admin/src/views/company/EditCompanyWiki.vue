<template>
  <div class="app-container wiki-container">
    <h3 v-if="company">{{company.fullName}} - 企业百科</h3>
    <div class="quill-wrap">
      <quill-editor v-model="wikiForm.content" ref="myQuillEditor" :options="editorOption"></quill-editor>
      <el-input
        class="mt-2"
        type="textarea"
        maxlength="500"
        placeholder="请输入百科摘要，不输入则摘取正文前 150 个字符"
        show-word-limit
        v-model="wikiForm.summary"
      ></el-input>
      <el-upload
        style="display:none"
        ref="upload"
        :action="uploadPicOptions.action"
        :data="uploadPicOptions.params"
        :accept="uploadPicOptions.acceptFileType"
        :show-file-list="false"
        :on-success="handleUploadSuccess"
        :before-upload="beforeUpload"
      >
        <i class="el-icon-plus avatar-uploader-icon" id="imgInput"></i>
      </el-upload>
      <el-button type="primary" @click="handleSave" class="mt-2" :loading="saveLoading">保存</el-button>
    </div>
  </div>
</template>

<script>
import "quill/dist/quill.core.css";
import "quill/dist/quill.snow.css";
import { quillEditor } from "vue-quill-editor";
import Quill from "quill";
import ImageResize from "quill-image-resize-module";
import { getUploadPicToken } from "@/api/upload_api";
import { getCompanyWiki, saveCompanyWiki } from "@/api/company_api";
import Toast from "@/utils/toast";

Quill.register("modules/imageResize", ImageResize);

export default {
  name: "EditCompanyWiki",
  components: {
    quillEditor
  },
  created() {
    this.initData();
  },
  data() {
    return {
      company: undefined,
      saveLoading: false,
      wikiForm: {
        companyId: undefined,
        summary: undefined,
        content: undefined
      },
      uploadPicOptions: {
        action: "",
        params: {},
        fileUrl: "",
        acceptFileType: ".jpg,.jpeg,.png,.JPG,.JPEG,.PNG"
      },
      editorOption: {
        theme: "snow",
        placeholder: "公司百科",
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
      }
    };
  },
  methods: {
    initData() {
      const companyId = this.$route.query.id;
      getCompanyWiki(companyId).then(response => {
        const { data } = response;
        this.company = data.company;
        this.wikiForm.companyId = data.company.id;
        this.wikiForm.content = data.content;
        this.wikiForm.summary = data.summary;
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
    },
    handleSave() {
      this.saveLoading = true;
      saveCompanyWiki(this.wikiForm)
        .then(() => {
          Toast.success("保存成功");
          this.initData();
        })
        .finally(() => {
          this.saveLoading = false;
        });
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
.wiki-container {
  margin: 0 120px;
  .ql-container .ql-editor {
    min-height: calc(100vh - 220px);
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
}
</style>