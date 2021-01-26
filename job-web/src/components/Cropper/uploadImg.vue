<template>
    <div class="upload-img">
        <el-upload class="upload"
                   ref="fileUpload"
                   :auto-upload="false"
                   :action="uploadPicOptions.action"
                   :data="uploadPicOptions.params"
                   :accept="uploadPicOptions.acceptFileType"
                   :show-file-list="false"
                   :multiple="false"
                   :limit="1"
                   :http-request="httpRequest"
                   :on-change="onUploadChange"
                   :on-success="onUploadSuccess">
            <el-image v-if="resultImg" :src="resultImg" class="img"></el-image>
            <el-image v-else class="add-icon" :src="require('@/assets/mock/img-upload.png')" fit="scale-down"></el-image>
            <div slot="tip" class="tip">上传照片</div>
        </el-upload>
        <cropper v-if="cropperDialogVisible"
                 :visible.sync="cropperDialogVisible"
                 :cropper-img="cropperImage"
                 @upload-img="uploadImg"/>
    </div>
</template>

<script>
    import Cropper from './cropper.vue';
    import {getUploadPicToken} from "@/api/upload_api";

    export default {
        name: 'UploadImg',
        components: {
            Cropper
        },
        props: {
            src: {
                type: String
            }
        },
        watch: {
            src: {
                handler() {
                    this.resultImg = this.src;
                },
                immediate: true
            },
            cropperDialogVisible(value) {
                if (!value) {
                    this.$refs.fileUpload.clearFiles();
                }
            }
        },
        data() {
            return {
                uploadData: { // 上传需要的额外参数
                    siteId: 1,
                    source: 1
                },
                cropperDialogVisible: false, // 是否显示裁剪框
                cropperImage: '', // 需要裁剪的图片
                uploadFile: '', // 裁剪后的文件
                resultImg: '', // 上传成功，后台返回的路径

                uploadPicOptions: {
                    action: "",
                    params: {},
                    fileUrl: "",
                    acceptFileType: ".jpg,.jpeg,.png,.JPG,.JPEG,.PNG",
                },
            };
        },
        methods: {
            // 网络请求
            httpRequest(request) {
                return new Promise((resolve, reject) => {
                    getUploadPicToken(request.file.name).then(response => {
                        const {data} = response
                        this.$axios.upload(data.host, this.uploadFile, data).then(() => {
                            resolve(data.host + '/' + data.key);
                        }).catch(() => {
                            reject();
                        })
                    })
                })
            },

            // 上传成功
            onUploadSuccess(response) {
                this.cropperDialogVisible = false;
                this.$emit("upload", response);
            },

            // 选择文件
            onUploadChange(file) {
                if (!file.response) {
                    const {raw} = file;

                    if (raw.size / 1024 / 1024 > 5) {
                        this.$message.error('请上传5M内的图片');
                        return false;
                    }

                    let reader = new FileReader();
                    reader.onload = (e) => {
                        // 把Array Buffer转化为blob 如果是base64不需要
                        let data;
                        if (typeof e.target.result === 'object') {
                            data = window.URL.createObjectURL(new Blob([e.target.result]));
                        } else {
                            data = e.target.result;
                        }
                        this.cropperImage = data;
                        this.cropperDialogVisible = true;
                    };
                    reader.readAsArrayBuffer(raw);
                }
            },

            // 上传图片
            uploadImg(file) {
                this.uploadFile = file;
                this.$refs.fileUpload.submit();
            },

            // 更新图片
            updateCropper() {
                // this.$refs.fileUpload.$children[0].$el.click();
            },
        }
    };
</script>

<style lang="scss" scoped>
    .upload-img {
        .upload {
            display: flex;
            flex-direction: column;
            align-items: center;

            .img {
                width: 100px;
                height: 100px;
            }

            .add-icon {
                width: 100px;
                height: 100px;
            }

            .tip {
                width: 65px;
                height: 17px;
                margin-top: 7px;
                background: #EDF2FF;
                border-radius: 9px;
                font-size: 10px;
                color: #3F5FF4;
                line-height: 17px;
                text-align: center;
            }
        }
    }
</style>