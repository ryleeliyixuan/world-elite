<template>
    <el-dialog class="dialog" :visible.sync="dialogVisible" width="1000px" title="提交认证信息" :before-close="onClose">
        <el-form ref="formData" :model="formData" :rules="rules" v-if="dialogStatus===1">
            <el-form-item label="您的姓名：" prop="name">
                <el-input style="width: 360px" v-model="formData.name" placeholder="请输入您的真实姓名"></el-input>
            </el-form-item>
            <el-form-item label="身份证/学生证号：" prop="idNumber">
                <el-input style="width: 360px" v-model="formData.idNumber" placeholder="请输入您的身份证/学生证号"></el-input>
            </el-form-item>
            <el-form-item label="身份证/学生证照片：" prop="identity">
                <div style="display: flex;">
                    <div class="card-container">
                        <el-upload class="card-uploader"
                                   :action="uploadPicOptions.action"
                                   :data="uploadPicOptions.params"
                                   :accept="uploadPicOptions.acceptFileType"
                                   :show-file-list="false"
                                   :on-success="handleUploadSuccess1"
                                   :before-upload="beforeUpload1">
                            <el-image v-if="formData.faceUrl"
                                      :src="formData.faceUrl"
                                      v-loading="loading1"
                                      fit="scale-down"
                                      class="card"/>
                            <div v-else class="card-face">
                                <el-image :src="require('@/assets/mock/add.png')" class="card-add"/>
                            </div>
                        </el-upload>
                        <div class="card-name">身份证/学生证人面像</div>
                    </div>
                    <div class="card-container">
                        <el-upload class="card-uploader"
                                   :action="uploadPicOptions.action"
                                   :data="uploadPicOptions.params"
                                   :accept="uploadPicOptions.acceptFileType"
                                   :show-file-list="false"
                                   :on-success="handleUploadSuccess2"
                                   :before-upload="beforeUpload2">
                            <el-image v-if="formData.emblemUrl"
                                      :src="formData.emblemUrl"
                                      v-loading="loading2"
                                      fit="scale-down"
                                      class="card"/>
                            <div v-else class="card-emblem">
                                <el-image :src="require('@/assets/mock/add.png')" class="card-add"/>
                            </div>
                        </el-upload>
                        <div class="card-name">身份证国徽面/身份证日期面</div>
                    </div>
                    <div class="card-container">
                        <el-upload class="card-uploader"
                                   :action="uploadPicOptions.action"
                                   :data="uploadPicOptions.params"
                                   :accept="uploadPicOptions.acceptFileType"
                                   :show-file-list="false"
                                   :on-success="handleUploadSuccess3"
                                   :before-upload="beforeUpload3">
                            <el-image v-if="formData.holdUrl"
                                      :src="formData.holdUrl"
                                      v-loading="loading3"
                                      fit="scale-down"
                                      class="card"/>
                            <div v-else class="card-hold">
                                <el-image :src="require('@/assets/mock/add.png')" class="card-add"/>
                            </div>
                        </el-upload>
                        <div class="card-name">手持身份证正面/学生证正面照</div>
                    </div>
                </div>
            </el-form-item>
            <el-form-item label="注意事项：">
                <div style="line-height: 24px;color: #333333; font-size:14px;margin-left: 88px; margin-top: 38px;">
                    <p> 1、同一个身份证号只能认证一个账号；</p>
                    <p> 2、身份证国徽面与正面信息应为同一身份证的信息且在有效期内；</p>
                    <p> 3、所有上传照片需清晰且未遮挡，请勿进行美化和修改，以免认证失败；</p>
                    <p> 4、 上传本人手持身份证信息面照片中应含有本人头部或上半身；</p>
                    <p> 5、手持身份证照中本人形象需为免冠且并未化妆，五官清晰；</p>
                    <p> 6、拍照手持身份证照时对焦点请置于身份证上，保证身份证信息清晰且未遮挡；</p>
                    <p> 7、上传图片文件格式支持png，jpg和bmp；</p>
                    <p> 8、单张图片大小不超过3MB，尺寸最小为500px * 500px；</p>
                    <p> 9、所有上传信息均会被妥善保管，不会用于其他商业用途或传输给其他第三方；</p>
                </div>
            </el-form-item>
        </el-form>

        <div class="button-container" v-if="dialogStatus===1">
            <div class="button1" @click="onOther">其他认证方式</div>
            <div class="button2" @click="onConfirm">提交认证信息</div>
        </div>

        <div class="dialog-success" v-if="dialogStatus===2">
            <el-image :src="require('@/assets/activity/success-approve.png')" class="image"/>
            <div class="text1">认证资料提交成功</div>
            <div class="text2">我们将尽快完成资料审核，请在“个人中心-编辑个人信息”处查看认证审核情况</div>
            <div class="confirm" @click="onClose">确认</div>
        </div>

        <div class="dialog-success" v-else-if="dialogStatus===3">
            <el-image :src="require('@/assets/activity/other-approve.png')" class="image"/>
            <div class="text1">请联系人工客服： xiaokefu@we.com</div>
            <div class="text2">我们将尽快完成资料审核</div>
            <div class="confirm" @click="onClose">确认</div>
        </div>
    </el-dialog>
</template>

<script>
    import {checkPicSize} from "@/utils/common";
    import {getUploadPicToken} from "@/api/upload_api";

    export default {
        name: "ApproveDialog",
        props: {
            visible: {
                type: Boolean
            },
        },

        computed: {
            dialogVisible: {
                get() {
                    return this.visible
                },
                set(val) {
                    this.$emit('update:visible', val)
                }
            }
        },

        data() {
            let identity = (rule, value, callback) => {
                if (!this.formData.faceUrl && !this.formData.emblemUrl && !this.formData.holdUrl) {
                    callback("请按要求上传三张身份证/学生证照片");
                } else if (!this.formData.faceUrl) {
                    callback("请上传身份证/学生证人面像");
                } else if (!this.formData.emblemUrl) {
                    callback("请上传身份证国徽面/学生证日期面");
                } else if (!this.formData.holdUrl) {
                    callback("请上传手持身份证/学生证正面照");
                } else {
                    callback();
                }
            }

            return {
                loading1: false,
                loading2: false,
                loading3: false,

                formData: {
                    name: '',
                    idNumber: '',
                    faceUrl: '',
                    emblemUrl: '',
                    holdUrl: '',
                    businessType: 1 // 实名认证业务类型.1:活动"
                },

                rules: {
                    name: [{required: true, message: '请输入您的姓名', trigger: 'blur'}],
                    idNumber: [{required: true, message: '请输入身份证/学生证号', trigger: 'blur'}],
                    identity: [{required: true, validator: identity, trigger: 'blur'}],
                },

                uploadPicOptions: {
                    action: "",
                    params: {},
                    fileUrl: "",
                    acceptFileType: ".jpg,.jpeg,.png,.JPG,.JPEG,.PNG",
                },

                dialogStatus: 1, // 对话框内容 1：表单，   2：成功，   3：其他认证
            }
        },

        methods: {
            // 关闭
            onClose() {
                this.$emit("close")
                this.dialogStatus = 1;
                this.$set(this, "dialogVisible", false);
            },

            // 其他认证
            onOther() {
                this.dialogStatus = 3;
            },

            // 提交认证
            onConfirm() {
                this.$refs["formData"].validate(valid => {
                    if (valid) {
                        this.$axios.post('/realnameauth/', this.formData).then(() => {
                                this.$message.success("提交成功");
                                this.dialogStatus = 2;
                            }
                        )
                    } else {
                        return false;
                    }
                })
            },

            handleUploadSuccess1() {
                this.loading1 = false;
                this.formData.faceUrl = this.uploadPicOptions.fileUrl;
                this.$refs["formData"].validateField("identity");
            },

            handleUploadSuccess2() {
                this.loading2 = false;
                this.formData.emblemUrl = this.uploadPicOptions.fileUrl;
                this.$refs["formData"].validateField("identity");
            },

            handleUploadSuccess3() {
                this.loading3 = false;
                this.formData.holdUrl = this.uploadPicOptions.fileUrl;
                this.$refs["formData"].validateField("identity");
            },

            beforeUpload1(file) {
                return new Promise((resolve, reject) => {
                    if (checkPicSize(file)) {
                        reject();
                    } else {
                        this.loading1 = true;
                        // this.formData.faceUrl = URL.createObjectURL(file);
                        getUploadPicToken(file.name).then((response) => {
                            const {data} = response;
                            this.uploadPicOptions.action = data.host;
                            this.uploadPicOptions.params = data;
                            this.uploadPicOptions.fileUrl = data.host + "/" + data.key;
                            this.$refs["formData"].validateField("identity");
                            resolve(data);
                        }).catch((error) => {
                            reject(error);
                        });
                    }
                    console.log(this.formData.faceUrl);
                });
            },
            beforeUpload2(file) {
                return new Promise((resolve, reject) => {
                    if (checkPicSize(file)) {
                        reject();
                    } else {
                        this.loading2 = true;
                        // this.formData.emblemUrl = URL.createObjectURL(file);
                        getUploadPicToken(file.name).then((response) => {
                            const {data} = response;
                            this.uploadPicOptions.action = data.host;
                            this.uploadPicOptions.params = data;
                            this.uploadPicOptions.fileUrl = data.host + "/" + data.key;
                            this.$refs["formData"].validateField("identity");
                            resolve(data);
                        }).catch((error) => {
                            reject(error);
                        });
                    }
                });
            },

            beforeUpload3(file) {
                return new Promise((resolve, reject) => {
                    if (checkPicSize(file)) {
                        reject();
                    } else {
                        this.loading3 = true;
                        // this.formData.emblemUrl = URL.createObjectURL(file);
                        getUploadPicToken(file.name).then((response) => {
                            const {data} = response;
                            this.uploadPicOptions.action = data.host;
                            this.uploadPicOptions.params = data;
                            this.uploadPicOptions.fileUrl = data.host + "/" + data.key;
                            this.$refs["formData"].validateField("identity");
                            resolve(data);
                        }).catch((error) => {
                            reject(error);
                        });
                    }
                });
            },
        }
    }
</script>

<style scoped lang="scss">
    .dialog {

        ::v-deep .el-dialog__body {
            height: 722px;
        }

        .el-form {

            .el-form-item {
                ::v-deep .el-form-item__label {
                    font-size: 16px;
                    color: #333333;
                    line-height: 32px;
                    width: 180px;
                    margin: 5px 0 0 0;
                }

                ::v-deep .el-form-item__error {
                    margin-left: 180px;
                }

                ::v-deep .el-input__inner, ::v-deep .el-textarea__inner {
                    width: 360px;
                    height: 32px;
                    background: #FFFFFF;
                    border-radius: 16px;
                    border: 1px solid #CCCCCC;
                    font-size: 14px;
                    color: #999999;
                    line-height: 32px;
                }

                .card-container {
                    display: flex;
                    align-items: center;
                    flex-direction: column;
                    margin-right: 30px;

                    .card-uploader {
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        width: 229px;
                        height: 154px;
                        background: #FFFFFF;
                        border-radius: 7px;
                        border: 1px solid #CCCCCC;

                        .el-upload {
                            width: 229px;
                            height: 154px;
                        }

                        .card {
                            width: 229px;
                            height: 154px;
                            display: flex;
                        }

                        .card-face {
                            background-image: url("../../assets/idcard1.png");
                            width: 161px;
                            height: 111px;
                            display: flex;
                            align-items: center;
                            justify-content: center;
                        }

                        .card-emblem {
                            background-image: url("../../assets/idcard2.png");
                            width: 161px;
                            height: 111px;
                            display: flex;
                            align-items: center;
                            justify-content: center;
                        }

                        .card-hold {
                            background-image: url("../../assets/idcard3.png");
                            width: 121px;
                            height: 121px;
                            display: flex;
                            align-items: center;
                            justify-content: center;
                        }

                        .card-add {
                            width: 45px;
                            height: 45px;
                        }
                    }

                    .card-name {
                        font-size: 14px;
                        color: #333333;
                        line-height: 25px;
                        margin-top: 8px;
                        text-align: center;
                    }
                }
            }
        }

        .button-container {
            display: flex;
            align-items: center;
            justify-content: center;
            margin-top: 34px;

            .button1 {
                width: 227px;
                height: 41px;
                background: #FFFFFF;
                border-radius: 21px;
                border: 1px solid #3D6FF4;
                font-size: 18px;
                color: #3D6FF4;
                line-height: 41px;
                text-align: center;
                margin: 0 15px;
                padding: 0;
                cursor: pointer;

                &:hover {
                    color: #0d46f3;
                    border: 1px solid #0d46f3;
                }
            }

            .button2 {
                width: 227px;
                height: 41px;
                background: #EDF3FF;
                border-radius: 21px;
                font-size: 18px;
                color: #3D6FF4;
                line-height: 41px;
                text-align: center;
                margin: 0 15px;
                padding: 0;
                cursor: pointer;
                border: 1px #EDF3FF solid;

                &:hover {
                    color: #0d46f3;
                    border: 1px solid #0d46f3;
                }
            }
        }

        .dialog-success {
            display: flex;
            flex-direction: column;
            align-items: center;
            padding-top: 80px;

            .text1 {
                font-size: 21px;
                color: #333333;
                line-height: 29px;
                margin-top: 34px;
            }

            .text2 {
                font-size: 18px;
                color: #999999;
                line-height: 25px;
                margin-top: 34px;
            }

            .confirm {
                width: 237px;
                height: 35px;
                background: #4895EF;
                border-radius: 18px;
                margin-top: 81px;
                font-size: 16px;
                color: #FFFFFF;
                line-height: 35px;
                text-align: center;
                cursor: pointer;
            }
        }
    }
</style>
