<template>
  <div class="editor-container">
    <div class="quill-wrap">
      <quill-editor v-model="childContent" ref="myQuillEditor" :options="editorOption"></quill-editor>
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
  </div>
</template>

<script>
import "quill/dist/quill.core.css";
import "quill/dist/quill.snow.css";
import { quillEditor } from "vue-quill-editor";
import Quill from "quill";
import ImageResize from "quill-image-resize-module";
import { getUploadPicToken } from "@/api/upload_api";

var BaseImageFormat = Quill.import("formats/image");
const ImageFormatAttributesList = ["alt", "height", "width", "style"];

class ImageFormat extends BaseImageFormat {
  static formats(domNode) {
    return ImageFormatAttributesList.reduce(function(formats, attribute) {
      if (domNode.hasAttribute(attribute)) {
        formats[attribute] = domNode.getAttribute(attribute);
      }
      return formats;
    }, {});
  }
  format(name, value) {
    if (ImageFormatAttributesList.indexOf(name) > -1) {
      if (value) {
        this.domNode.setAttribute(name, value);
      } else {
        this.domNode.removeAttribute(name);
      }
    } else {
      super.format(name, value);
    }
  }
}

Quill.register(ImageFormat, true);
Quill.register("modules/imageResize", ImageResize);

export default {
  name: "EditorView",
  components: {
    quillEditor
  },
  props: {
    content: {
      type: String
    }
  },
  mounted() {
    this.$refs.myQuillEditor.quill
      .getModule("toolbar")
      .addHandler("image", this.imgHandler);
  },
  watch:{
    childContent(newVal){
      this.$emit("updateContent", newVal)
    },
    content(newVal){
      this.childContent = newVal;
    }
  },
  data() {
    return {
      childContent: this.content,
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
      }
    };
  },
  methods: {
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
  }
};
</script>
<style lang="scss">
.ql-snow .ql-editor {
  p {
    font-size: 1rem;
    font-weight: 400;
    line-height: 1.5;
    margin-top: 0;
    margin-bottom: 1rem;
  }
  h1,
  h2,
  h3,
  h4,
  h5,
  h6 {
    margin-top: 0;
    margin-bottom: 0.5rem;
    font-weight: 500;
    line-height: 1.2;
  }
  h1 {
    font-size: 2.5rem;
  }
  h2 {
    font-size: 2rem;
  }
  h3 {
    font-size: 1.75rem;
  }
  h4 {
    font-size: 1.5rem;
  }
  h5 {
    font-size: 1.25rem;
  }
  h5 {
    font-size: 1rem;
  }
  ol,
  ul {
    padding-left: 0.5em;
    margin-bottom: 10px;
  }
}
</style>