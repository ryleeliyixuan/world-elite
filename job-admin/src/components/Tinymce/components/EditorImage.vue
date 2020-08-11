<template>
  <el-upload
    v-loading.fullscreen.lock="fullscreenLoading"
    ref="upload"
    :action="uploadPicOptions.action"
    :data="uploadPicOptions.params"
    :accept="uploadPicOptions.acceptFileType"
    :show-file-list="false"
    :on-success="handleEditorUploadSuccess"
    :on-error="handleEditorUploadError"
    :before-upload="beforeUpload">
    <el-button size="small" type="primary">
      上传图片
    </el-button>
  </el-upload>
</template>

<script>

  import { getUploadPicToken } from '@/api/upload_api'

  export default {
    name: 'EditorImage',
    props: {
      // color: {
      //   type: String,
      //   default: '#1890ff'
      // }
    },
    data() {
      return {
        fullscreenLoading: false,
        uploadPicOptions: {
          action: '',
          params: {},
          fileUrl: '',
          acceptFileType: '.jpg,.jpeg,.png,.JPG,.JPEG,.PNG',
          localUrl: ''
        }
      }
    },
    methods: {
      beforeUpload(file) {
        this.fullscreenLoading = true;
        return new Promise((resolve, reject) => {
          getUploadPicToken(file.name)
            .then(response => {
              const { data } = response
              this.uploadPicOptions.action = data.host
              this.uploadPicOptions.params = data
              this.uploadPicOptions.fileUrl = data.host + '/' + data.key
              const _URL = window.URL || window.webkitURL
              this.uploadPicOptions.localUrl = _URL.createObjectURL(file)
              resolve(data)
            })
            .catch(error => {
              reject(error)
            })
        })
      },
      handleEditorUploadSuccess() {
        this.fullscreenLoading = false;
        this.$emit('successCBK', { url: this.uploadPicOptions.fileUrl })
      },
      handleEditorUploadError() {
        this.$message.error("图片上传失败")
        this.fullscreenLoading = false;
      }
    }
  }
</script>

<style lang="scss" scoped>
  .editor-slide-upload {
    margin-bottom: 20px;

    .el-upload--picture-card {
      width: 100%;
    }
  }
</style>
