<template>
    <div class="app-container">
        <div class="title">面试官注册</div>
        <el-steps :active="active" class="steps">
            <el-step></el-step>
            <el-step></el-step>
            <el-step></el-step>
        </el-steps>

        <div v-if="active===1">
            <!-- <div class="import-container">-->
            <!--     <div class="import-button" @click="onImport">一键导入信息</div>-->
            <!-- </div>-->
            <div class="step-title">个人信息</div>
            <el-form ref="formOne" :model="formOne" :rules="rules">
                <el-form-item label="昵称：" prop="nickName">
                    <div class="nickname-container">
                        <el-input style="width: 360px" v-model="formOne.nickName" placeholder="请输入您的昵称"></el-input>
                        <div class="warn-tip">
                            <el-image :src="require('@/assets/mock/warn.png')" class="warn-icon"></el-image>
                            <div class="warn-introduce">头像与昵称是您希望呈现在面试者预约页面的形象，可以使用化名或者卡通形象。</div>
                        </div>
                    </div>
                </el-form-item>
                <el-form-item label="头像：" prop="avatar">
                    <div>
                        <el-image v-for="item in avatarList"
                                  :key="item.id"
                                  :src="item.avatarUrl"
                                  @click="formOne.avatar= item.avatarUrl;"
                                  style="width: 100px; height: 100px; margin-right: 13px; cursor: pointer;"/>
                    </div>
                    <el-upload class="avatar-uploader"
                               style="margin-left: 180px;"
                               :action="uploadPicOptions.action"
                               :data="uploadPicOptions.params"
                               :accept="uploadPicOptions.acceptFileType"
                               :show-file-list="false"
                               :on-success="handleUploadSuccess"
                               :before-upload="beforeUpload">
                        <el-image v-if="formOne.avatar"
                                  :src="formOne.avatar"
                                  v-loading="loading"
                                  class="avatar"/>
                        <el-image v-else class="avatar-uploader-icon" :src="require('@/assets/mock/img-upload.png')"></el-image>
                        <div slot="tip" class="avatar-uploader-tip">建议大小500*500</div>
                    </el-upload>
                </el-form-item>
                <div>
                    <el-form-item label="从事行业：" prop="industryId">
                        <el-select v-model="formOne.industryId"
                                   clearable
                                   placeholder="行业"
                                   class="section1-select">
                            <el-option v-for="item in industryOptions" :key="item.industryId" :label="item.name" :value="item.id"></el-option>
                        </el-select>
                    </el-form-item>
                </div>
                <div>
                    <el-form-item label="所在公司：" prop="companyName">
                        <el-input style="width: 300px" v-model="formOne.companyName"
                                  placeholder="请输入您的公司名称"></el-input>
                    </el-form-item>
                </div>
                <div>
                    <el-form-item label="从业时间：" prop="experienceTimeId">
                        <el-select v-model="formOne.experienceTimeId" clearable placeholder="请选择您的工作经验年限" class="section1-select">
                            <el-option v-for="item in experienceTimeOptions" :key="item.experienceTimeId"
                                       :label="item.name" :value="item.id"></el-option>
                        </el-select>
                    </el-form-item>
                </div>
                <div>
                    <el-form-item label="从业经历：" prop="experience.experienceItem">
                        <div class="textarea-container">
                            <div class="textarea-item" v-for="(item,index) in formOne.experience">
                                <span class="experience-title">{{index+1}}.</span>
                                <el-input class="experience" type="textarea" v-model="item.experienceItem "
                                          placeholder="字数不超过150字" resize="none" :autosize="{minRows: 2,maxRows: 10}"></el-input>
                            </div>
                            <div class="add-experience-button" @click="addExperience">添加经历</div>
                        </div>
                    </el-form-item>
                </div>
                <div>
                    <el-form-item label="面试官自述：" prop="description">
                        <el-input type="textarea" style="width: 360px" v-model="formOne.description"
                                  placeholder="字数不超过150字" resize="none" :autosize="{minRows: 8,maxRows: 10}"></el-input>
                    </el-form-item>
                </div>
            </el-form>
        </div>
        <div v-if="active===2" class="interview-info">
            <div class="step-title">面试信息</div>
            <div class="direction-container">
                <div style="width: 340px; text-align: right;">可提供面试内容：</div>
                <el-checkbox v-model="checked1">HR面试（通用）</el-checkbox>
                <el-checkbox v-model="checked2">专业技术（测试）</el-checkbox>
                <el-checkbox v-model="checked3">行业经验</el-checkbox>
            </div>
            <div v-if="!(checked1||checked2||checked3)" style="margin-top: 30px;">
                <el-alert title="至少选择一个可提供面试的内容！"
                          type="warning"
                          center
                          show-icon>
                </el-alert>
            </div>
            <el-form ref="formTwo" :model="formTwo" :rules="rules" v-if="checked1===true">
                <el-form-item label="HR通用面试理想咨询价格：" label-width="340px" prop="price">
                    <el-input style="width: 300px" v-model="formTwo.price" placeholder="请输入您理想资询价位(元/半小时)"></el-input>
                </el-form-item>
                <el-form-item label="面试内容简介：" label-width="340px" prop="description">
                    <el-input type="textarea" style="width: 300px" v-model="formTwo.description" placeholder="请对面试的内容进行具体描述，150字以内"
                              :autosize="{minRows: 8,maxRows: 10}" resize="none"></el-input>
                </el-form-item>
            </el-form>
            <el-form ref="formTwo2" :model="formTwo2" :rules="rules" v-if="checked2===true">
                <el-form-item label="专业技术测试理想咨询价格：" label-width="340px" prop="price">
                    <el-input v-model="formTwo2.price" style="width: 300px" placeholder="请输入您理想资询价位(元/半小时)"></el-input>
                </el-form-item>
                <el-form-item label="面试内容简介：" label-width="340px" prop="description">
                    <el-input type="textarea" style="width: 300px" v-model="formTwo2.description" placeholder="请对面试的内容进行具体描述，150字以内"
                              :autosize="{minRows: 8,maxRows: 10}" resize="none"></el-input>
                </el-form-item>
            </el-form>
            <el-form ref="formTwo3" :model="formTwo3" :rules="rules" v-if="checked3===true">
                <el-form-item label="职业规划理想咨询价格：" label-width="340px" prop="price">
                    <el-input v-model="formTwo3.price" style="width: 300px" placeholder="请输入您理想资询价位(元/半小时)"></el-input>
                </el-form-item>
                <el-form-item label="面试内容简介：" label-width="340px" prop="description">
                    <el-input type="textarea" style="width: 300px" v-model="formTwo3.description" placeholder="请对面试的内容进行具体描述，150字以内"
                              :autosize="{minRows: 8,maxRows: 10}" resize="none"></el-input>
                </el-form-item>
            </el-form>
        </div>
        <div v-if="active===3">
            <div class="step-title">提交认证信息</div>
            <el-form ref="formThree" :model="formThree" :rules="rules">
                <el-form-item label="您的姓名：" prop="name">
                    <el-input style="width: 360px" v-model="formThree.name" placeholder="请输入您的真实姓名"></el-input>
                </el-form-item>
                <el-form-item label="身份证号：" prop="idNumber">
                    <el-input style="width: 360px" v-model="formThree.idNumber" placeholder="请输入您的身份证号"></el-input>
                </el-form-item>
                <el-form-item label="身份证照片：" prop="identity">
                    <div style="display: flex;">
                        <div class="card-container">
                            <el-upload class="card-uploader"
                                       :action="uploadPicOptions.action"
                                       :data="uploadPicOptions.params"
                                       :accept="uploadPicOptions.acceptFileType"
                                       :show-file-list="false"
                                       :on-success="handleUploadSuccess1"
                                       :before-upload="beforeUpload1">
                                <el-image v-if="formThree.faceUrl"
                                          :src="formThree.faceUrl"
                                          v-loading="loading1"
                                          fit="scale-down"
                                          class="card"/>
                                <div v-else class="card-face">
                                    <el-image :src="require('@/assets/mock/add.png')" class="card-add"/>
                                </div>
                            </el-upload>
                            <div class="card-name">身份证人面像</div>
                        </div>
                        <div class="card-container">
                            <el-upload class="card-uploader"
                                       :action="uploadPicOptions.action"
                                       :data="uploadPicOptions.params"
                                       :accept="uploadPicOptions.acceptFileType"
                                       :show-file-list="false"
                                       :on-success="handleUploadSuccess2"
                                       :before-upload="beforeUpload2">
                                <el-image v-if="formThree.emblemUrl"
                                          :src="formThree.emblemUrl"
                                          v-loading="loading2"
                                          fit="scale-down"
                                          class="card"/>
                                <div v-else class="card-emblem">
                                    <el-image :src="require('@/assets/mock/add.png')" class="card-add"/>
                                </div>
                            </el-upload>
                            <div class="card-name">身份证国徽面</div>
                        </div>
                        <div class="card-container">
                            <el-upload class="card-uploader"
                                       :action="uploadPicOptions.action"
                                       :data="uploadPicOptions.params"
                                       :accept="uploadPicOptions.acceptFileType"
                                       :show-file-list="false"
                                       :on-success="handleUploadSuccess3"
                                       :before-upload="beforeUpload3">
                                <el-image v-if="formThree.holdUrl"
                                          :src="formThree.holdUrl"
                                          v-loading="loading3"
                                          fit="scale-down"
                                          class="card"/>
                                <div v-else class="card-hold">
                                    <el-image :src="require('@/assets/mock/add.png')" class="card-add"/>
                                </div>
                            </el-upload>
                            <div class="card-name">手持身份证正面<br/>(确保身份证清晰)</div>
                        </div>
                    </div>
                </el-form-item>
                <el-form-item label="注意事项：">
                    <div style="line-height: 35px;color: #333333; font-size:18px;margin-left: 48px; margin-top: 50px;">
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
            <div class="button-container">
                <div class="button1" @click="onOtherAuth">其他认证方式</div>
                <div class="button2" @click="onSubmit">提交认证信息</div>
            </div>
        </div>
        <div v-if="active===1"></div>
        <div v-if="active===2"></div>
        <div v-if="active===3"></div>
        <div class="button-container">
            <div class="button1" @click="prev1" v-if="active===1">上一步</div>
            <div class="button2" @click="next2" v-if="active===1">下一步</div>
            <div class="button1" @click="active = 1" v-if="active===2">上一步</div>
            <div class="button2" @click="next3" v-if="active===2">下一步</div>
        </div>
    </div>
</template>

<script>
    import {listByType} from "@/api/dict_api";
    import Pagination from "@/components/Pagination";
    import {getUserId} from '@/utils/auth'
    import {addInterview, addInterviewDirection, addInterviewAuth} from "@/api/interview_api";
    import Toast from "@/utils/toast";
    import {getUploadPicToken} from "@/api/upload_api";
    import {checkPicSize} from "@/utils/common";

    export default {
        name: "registerInterviewerPage",
        components: {
            Pagination
        },
        data() {
            let isIdNumber = (rule, value, callback) => {
                if (!value) {
                    callback(new Error("请输入身份证号"));
                } else {
                    const reg = /^\d{6}(18|19|20)?\d{2}(0[1-9]|1[0-2])(([0-2][1-9])|10|20|30|31)\d{3}(\d|X|x)$/;
                    const card = reg.test(value);
                    if (!card) {
                        callback(new Error("身份证格式如:423024xxxx0216xxxx"));
                    } else {
                        callback();
                    }
                }
            };
            let isPrice = (rule, value, callback) => {
                if (!value) {
                    callback();
                } else {
                    let reg = /^-?\d{1,4}(?:\.\d{1,2})?$/;
                    if (reg.test(value)) {
                        callback();
                    } else {
                        callback(new Error("数字格式:0-9999或小数点后可加1到2位"));//如:1 或1.8 或1.85
                    }
                }
            };

            let identity = (rule, value, callback) => {
                if (!this.formThree.faceUrl) {
                    callback("请上传身份证人面像");
                } else if (!this.formThree.emblemUrl) {
                    callback("请上传身份证国徽面");
                } else if (!this.formThree.holdUrl) {
                    callback("请上传手持身份证正面照");
                } else {
                    callback();
                }
            }
            return {
                uploadPicOptions: {
                    action: "",
                    params: {},
                    fileUrl: "",
                    acceptFileType: ".jpg,.jpeg,.png,.JPG,.JPEG,.PNG",
                },
                active: 1,
                loading: false,
                loading1: false,
                loading2: false,
                loading3: false,
                checked1: true,
                checked2: false,
                checked3: false,
                formOne: {
                    nickName: '',
                    avatar: '',
                    industryId: '',
                    experienceTimeId: '',
                    companyName: '',
                    description: '',
                    experience: [{experienceItem: ''}],
                },
                //hr
                formTwo: {
                    direction: 'HR面试（通用）',
                    description: '',
                    interviewerId: getUserId(),
                    price: '',
                },
                //技术
                formTwo2: {
                    direction: '专业技术（测试）',
                    description: '',
                    interviewerId: getUserId(),
                    price: '',
                },
                //经验
                formTwo3: {
                    direction: '行业经验',
                    description: '',
                    interviewerId: getUserId(),
                    price: '',
                },
                formThree: {
                    name: '',
                    idNumber: '',
                    faceUrl: '',
                    emblemUrl: '',
                    holdUrl: ''
                },
                rules: {
                    nickName: [{required: true, message: '请输入用户昵称', trigger: 'blur'}, {max: 20, message: '昵称不超过20字符'}],
                    identity: [{validator: identity, trigger: 'blur'}],
                    avatar: [{required: true, message: "请上传头像", trigger: "change"}],
                    industryId: [{required: true, message: "请选择行业", trigger: "change"}],
                    experienceTimeId: [{required: true, message: "请选择从业时间", trigger: "change"}],
                    name: [{required: true, message: '请输入您的姓名', trigger: 'blur'}],
                    idNumber: [{required: true, trigger: 'blur', validator: isIdNumber}],
                    companyName: [{required: true, message: '请输入您的公司名称', trigger: 'blur'}],
                    description: [{required: true, message: '请输入面试官自述', trigger: 'blur'}, {max: 150, message: '长度不超过150个字'}],
                    experience: [{required: true, message: '请输入从业经历', trigger: 'blur'}, {max: 150, message: '长度不超过150个字'}],
                    price: [{required: true, message: '请输入您理想资询价位', trigger: 'blur'}, {validator: isPrice, trigger: 'blur'}]
                },
                avatarList: [],
                industryOptions: [],
                experienceTimeOptions: [],
            };
        },
        created() {
            this.initData();
            this.$emit("complete");
        },
        methods: {
            initData() {
                listByType(18).then(
                    response => (this.industryOptions = response.data.list)
                );

                listByType(13).then(
                    response => (this.experienceTimeOptions = response.data.list)
                );

                this.$axios.get("/mock/avatar").then(data => {
                    this.avatarList = data.data;
                })
            },

            beforeUpload(file) {
                return new Promise((resolve, reject) => {
                    if (checkPicSize(file)) {
                        reject();
                    } else {
                        this.loading = true;
                        this.formOne.avatar = URL.createObjectURL(file);
                        getUploadPicToken(file.name).then((response) => {
                            const {data} = response;
                            this.uploadPicOptions.action = data.host;
                            this.uploadPicOptions.params = data;
                            this.uploadPicOptions.fileUrl = data.host + "/" + data.key;
                            resolve(data);
                        }).catch((error) => {
                            reject(error);
                        });
                    }
                });
            },
            beforeUpload1(file) {
                return new Promise((resolve, reject) => {
                    if (checkPicSize(file)) {
                        reject();
                    } else {
                        this.loading1 = true;
                        this.formThree.faceUrl = URL.createObjectURL(file);
                        getUploadPicToken(file.name).then((response) => {
                            const {data} = response;
                            this.uploadPicOptions.action = data.host;
                            this.uploadPicOptions.params = data;
                            this.uploadPicOptions.fileUrl = data.host + "/" + data.key;
                            resolve(data);
                        }).catch((error) => {
                            reject(error);
                        });
                    }
                });
            },
            beforeUpload2(file) {
                return new Promise((resolve, reject) => {
                    if (checkPicSize(file)) {
                        reject();
                    } else {
                        this.loading2 = true;
                        this.formThree.emblemUrl = URL.createObjectURL(file);
                        getUploadPicToken(file.name).then((response) => {
                            const {data} = response;
                            this.uploadPicOptions.action = data.host;
                            this.uploadPicOptions.params = data;
                            this.uploadPicOptions.fileUrl = data.host + "/" + data.key;
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
                        this.formThree.holdUrl = URL.createObjectURL(file);
                        getUploadPicToken(file.name).then((response) => {
                            const {data} = response;
                            this.uploadPicOptions.action = data.host;
                            this.uploadPicOptions.params = data;
                            this.uploadPicOptions.fileUrl = data.host + "/" + data.key;
                            resolve(data);
                        }).catch((error) => {
                            reject(error);
                        });
                    }
                });
            },

            handleUploadSuccess() {
                this.loading = false;
            },
            handleUploadSuccess1() {
                this.$refs["formThree"].validateField('identity');
                this.loading1 = false;
            },
            handleUploadSuccess2() {
                this.$refs["formThree"].validateField('identity');
                this.loading2 = false;
            },
            handleUploadSuccess3() {
                this.$refs["formThree"].validateField('identity');
                this.loading3 = false;
            },

            addExperience() {
                this.formOne.experience.push({experienceItem: ''})
            },

            prev1() {
                this.$router.push("/InterviewSecretPage");
            },

            next2() {
                this.$refs["formOne"].validate(valid => {
                    if (valid) {
                        let formOne = {...this.formOne};
                        formOne.experience = formOne.experience.map(item => item.experienceItem)
                        addInterview(formOne).then(() => {
                            Toast.success("提交成功");
                        })
                        this.active = 2;
                    } else {
                        console.log('error submit!!');
                    }
                });
            },

            validate(filedName) {
                return new Promise((resolve) => {
                    this.$refs[filedName].validate(valid => {
                        resolve(valid)
                    });
                })
            },

            next3() {
                let p = [];
                if (this.checked1) {
                    p.push(this.validate('formTwo'));
                }
                if (this.checked2) {
                    p.push(this.validate('formTwo2'));
                }
                if (this.checked3) {
                    p.push(this.validate('formTwo3'));
                }
                Promise.all(p).then(result => {
                    let valid = result.find(item => !item);
                    if (valid === undefined) {
                        let request = [];
                        if (this.checked1) {
                            request.push(addInterviewDirection(this.formTwo));
                        }
                        if (this.checked2) {
                            request.push(addInterviewDirection(this.formTwo2));
                        }
                        if (this.checked3) {
                            request.push(addInterviewDirection(this.formTwo3));
                        }

                        Promise.all(request).then(() => {
                            Toast.success("提交成功");
                            this.active = 3;
                        })
                    }
                })
            },

            onSubmit() {
                this.$refs["formThree"].validate(valid => {
                    if (valid) {
                        addInterviewAuth(this.formThree).then(() => {
                                Toast.success("提交成功");
                                this.$router.push("/RegisterOther");
                            }
                        )
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                })
            },

            onOtherAuth() {
                this.$router.push("/RegisterInterviewerSuccess");
            },

            //一键导入
            onImport() {

            },
        }
    }
</script>

<style scoped lang="scss">
    .app-container {
        width: 1140px;
        padding: 20px 20px 200px 20px;
        margin: 0 auto;
        min-height: calc(100vh - 477px);

        .title {
            font-size: 36px;
            color: #333333;
            line-height: 50px;
            margin-bottom: 30px;
        }

        .steps {
            width: 722px;
            margin-bottom: 50px;

            ::v-deep .el-step__line {
                top: 29px;
                left: 15px;
            }

            ::v-deep .el-step__icon {
                width: 60px;
                height: 60px;
                font-size: 24px;
                background: #EDF3FF;
                border-radius: 50%;
                border: none;
            }

            ::v-deep .el-step__head {
                &.is-process, &.is-wait {
                    color: #999999;
                    border: none;

                    .el-step__icon {
                        background: #EEEEEE;
                    }
                }
            }
        }

        .interview-info {
            /deep/ .el-form-item__error {
                margin-left: 0;
            }
        }

        .import-container {
            width: 722px;
            height: auto;
            display: flex;
            justify-content: center;
            margin-bottom: 62px;

            .import-button {
                width: 379px;
                height: 40px;
                background: #FFFFFF;
                border-radius: 21px;
                border: 1px solid #3D6FF4;
                text-align: center;
                line-height: 40px;
                font-size: 18px;
                font-family: PingFangSC-Medium, PingFang SC;
                font-weight: 500;
                color: #3D6FF4;
                box-sizing: content-box;
            }

        }

        .step-title {
            font-size: 30px;
            color: #333333;
        }

        .direction-container {
            display: flex;
            align-items: center;
            font-size: 24px;
            font-family: PingFangSC-Medium, PingFang SC;
            font-weight: 500;
            color: #333333;
            line-height: 33px;

            /deep/ .el-checkbox {
                margin-right: 107px;
                transform-origin: left;
                transform: scale(1.7);
                margin-bottom: 0;
            }
        }

        .el-form {
            .el-form-item {
                margin-top: 30px;

                ::v-deep .el-form-item__label {
                    font-size: 24px;
                    color: #333333;
                    line-height: 33px;
                    width: 180px;
                    margin: 5px 0 0 0;
                }

                ::v-deep .el-form-item__error {
                    margin-left: 180px;
                }

                ::v-deep .el-input__inner, ::v-deep .el-textarea__inner {
                    width: 360px;
                    height: 43px;
                    background: #FFFFFF;
                    border-radius: 22px;
                    border: 1px solid #CCCCCC;
                    font-size: 18px;
                    color: #999999;
                    line-height: 25px;
                }

                .nickname-container {
                    display: flex;
                    flex-direction: row;
                    align-items: self-start;

                    .warn-tip {
                        display: flex;
                        flex-direction: row;
                        align-content: flex-start;

                        .warn-icon {
                            width: 17px;
                            height: 17px;
                            margin-left: 13px;
                        }

                        .warn-introduce {
                            width: 209px;
                            height: 100px;
                            font-size: 18px;
                            color: #999999;
                            line-height: 25px;
                        }
                    }
                }

                .textarea-container {
                    width: 360px;
                    height: auto;
                    display: inline-block;

                    .textarea-item {
                        width: 100%;
                        height: auto;
                        display: flex;
                        flex-direction: row;
                        align-items: flex-start;
                        margin-bottom: 13px;

                        .experience-title {
                            font-size: 20px;
                            color: #333333;
                            line-height: 48px;
                        }

                        .experience {
                            ::v-deep .el-textarea__inner {
                                width: 332px;
                                margin-left: 10px;
                            }
                        }

                        /deep/ .el-textarea__inner {
                            border-radius: 13px;
                        }
                    }

                    .add-experience-button {
                        width: 136px;
                        height: 40px;
                        background: #EDF3FF;
                        border-radius: 21px;
                        line-height: 40px;
                        text-align: center;
                        font-size: 18px;
                        color: #3D6FF4;
                        margin: 13px auto 0;
                        cursor: pointer;
                    }

                    /deep/ .el-textarea__inner {
                        border-radius: 13px;
                    }
                }

                .avatar-uploader {
                    .avatar {
                        width: 100px;
                        height: 100px;
                        display: block;
                    }

                    .avatar-uploader-icon {
                        border: 1px solid #333333;
                        border-radius: 50%;
                        font-size: 28px;
                        color: #8c939d;
                        width: 100px;
                        height: 100px;
                        line-height: 100px;
                        text-align: center;
                        padding: 36px 31px;
                    }

                    .avatar-uploader-tip {
                        font-size: 18px;
                        font-weight: 400;
                        color: #999999;
                        line-height: 25px;
                        margin-top: -3px;
                    }
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
                            background-image: url("../assets/idcard1.png");
                            width: 161px;
                            height: 111px;
                            display: flex;
                            align-items: center;
                            justify-content: center;
                        }

                        .card-emblem {
                            background-image: url("../assets/idcard2.png");
                            width: 161px;
                            height: 111px;
                            display: flex;
                            align-items: center;
                            justify-content: center;
                        }

                        .card-hold {
                            background-image: url("../assets/idcard3.png");
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
                        font-size: 18px;
                        color: #333333;
                        line-height: 25px;
                        margin-top: 13px;
                        text-align: center;
                    }
                }
            }
        }

        .button-container {
            display: flex;
            justify-content: center;
            align-items: center;
            margin-top: 80px;

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

                &:hover {
                    color: #0d46f3;
                    border: 1px solid #0d46f3;
                }
            }
        }
    }
</style>
