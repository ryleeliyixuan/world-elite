<template>

    <div class="app-container">
        <div class="main-container">
            <div class="title">面试官注册</div>
            <el-steps :active="active" class="steps">
                <el-step></el-step>
                <el-step></el-step>
                <el-step></el-step>
            </el-steps>

            <div v-if="active==1">
                <el-form ref="formOne" :model="formOne" :rules="rules">
                    <!--                    <div class="import-container">-->
                    <!--                        <div class="import-button" @click="onImport">一键导入信息</div>-->
                    <!--                    </div>-->
                    <div class="step-title">个人信息</div>
                    <div style="margin-top: 35px">
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
                                          @click="resumeForm.avatarUrl = item.avatarUrl; formOne.avatar= item.avatarUrl;"
                                          style="width: 100px; height: 100px; margin-right: 13px; cursor: pointer;"></el-image>
                            </div>
                            <el-upload class="avatar-uploader"
                                       style="margin-left: 180px;"
                                       :action="uploadPicOptions.action"
                                       :data="uploadPicOptions.params"
                                       :accept="uploadPicOptions.acceptFileType"
                                       :show-file-list="false"
                                       :on-success="handleAvatarSuccess"
                                       :before-upload="beforeAvatarUpload">
                                <el-image v-if="resumeForm.avatarUrl"
                                          :src="resumeForm.avatarUrl"
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
                                           @change="handleFilter"
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
                                <el-select v-model="formOne.experienceTimeId" clearable placeholder="请选择您的工作经验年限"
                                           @change="handleFilter" class="section1-select">
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
                    </div>
                </el-form>
            </div>
            <div v-if="active==2">
                <el-form ref="formTwo" :model="formTwo" :rules="rules">
                    <el-form-item label="可提供面试内容：" label-width="150px" :model="formTwo.direction">
                        <el-checkbox v-model="checked1">HR面试（通用）</el-checkbox>
                        <el-checkbox v-model="checked2">专业技术（测试）</el-checkbox>
                        <el-checkbox v-model="checked3">行业经验</el-checkbox>
                    </el-form-item>
                    <div v-if="checked1===true">
                        <el-form-item label="HR通用面试理想咨询价格：" label-width="300px" style="margin-left: -100px"
                                      prop="price">
                            <el-input style="width: 300px" v-model="formTwo.price"
                                      placeholder="请输入您理想资询价位(元/半小时)">
                            </el-input>
                        </el-form-item>
                        <el-form-item label="面试内容简介：" label-width="300px" style="margin-left: -100px"
                                      prop="description">
                            <el-input type="textarea" style="width: 300px" v-model="formTwo.description"
                                      placeholder="请对面试的内容进行具体描述，150字以内"
                                      :autosize="{minRows: 8,maxRows: 10}"></el-input>
                        </el-form-item>
                    </div>
                    <div v-if="checked2===true">
                        <el-form-item label="专业技术测试理想咨询价格：" label-width="300px" style="margin-left: -100px"
                                      prop="price">
                            <el-input v-model="formTwo.price" style="width: 300px"
                                      placeholder="请输入您理想资询价位(元/半小时)">
                            </el-input>
                        </el-form-item>
                        <el-form-item label="面试内容简介：" label-width="300px" style="margin-left: -100px"
                                      prop="description">
                            <el-input type="textarea" style="width: 300px" v-model="formTwo.description"
                                      placeholder="请对面试的内容进行具体描述，150字以内"
                                      :autosize="{minRows: 8,maxRows: 10}"></el-input>
                        </el-form-item>
                    </div>
                    <div v-if="checked3===true">
                        <el-form-item label="职业规划理想咨询价格：" label-width="300px" style="margin-left: -100px" prop="price">
                            <el-input v-model="formTwo.price" style="width: 300px"
                                      placeholder="请输入您理想资询价位(元/半小时)">
                            </el-input>
                        </el-form-item>
                        <el-form-item label="面试内容简介：" label-width="300px" style="margin-left: -100px"
                                      prop="description">
                            <el-input type="textarea" style="width: 300px" v-model="formTwo.description"
                                      placeholder="请对面试的内容进行具体描述，150字以内"
                                      :autosize="{minRows: 8,maxRows: 10}"></el-input>
                        </el-form-item>
                    </div>
                    <div v-if="checked1==false&&checked2===false&&checked3===false">
                        <el-alert title="至少选择一个可提供面试的内容！"
                                  type="warning"
                                  center
                                  show-icon>
                        </el-alert>
                    </div>
                </el-form>
            </div>

            <div v-if="active==3">
                <el-form ref="formThree" :model="formThree" :rules="rules">
                    <el-form-item label="您的姓名：" label-width="100px" prop="name">
                        <el-input style="width: 300px" v-model="formThree.name" placeholder="请输入您的真实姓名"></el-input>
                    </el-form-item>
                    <el-form-item label="身份证号：" label-width="100px" prop="idNumber">
                        <el-input style="width: 300px" v-model="formThree.idNumber" placeholder="请输入您的身份证号"></el-input>
                    </el-form-item>
                    <el-form-item label="身份证照片" label-width="100px" prop="avatarUrl">
                        <b-media class="col-lg-8 col-md-8 col-sm-12 col-xs-12">
                            <div class="row mt-3">
                                <div class="col-lg-4 col-md-12 col-sm-12 col-xs-12">
                                    <el-upload
                                            class="avatar-uploader mb-3"
                                            :action="uploadPicOptions.action"
                                            :data="uploadPicOptions.params"
                                            :accept="uploadPicOptions.acceptFileType"
                                            :show-file-list="false"
                                            :on-success="handleAvatarSuccess1"
                                            :before-upload="beforeAvatarUpload1"
                                    >
                                        <img
                                                v-if="(resumeForm1.avatarUrl && resumeForm1.avatarUrl !== '')"
                                                :src="resumeForm1.avatarUrl"
                                                class="avatar1"
                                        />
                                        <i v-else class="el-icon-plus avatar-uploader-icon1"></i>
                                        <div slot="tip" class="el-upload__tip">*身份证人面像*</div>
                                    </el-upload>
                                </div>
                            </div>
                        </b-media>
                        <b-media class="resume col-lg-8 col-md-8 col-sm-12 col-xs-12">
                            <div class="resume-basicinfo row mt-3">
                                <div class="avatorHolder col-lg-4 col-md-12 col-sm-12 col-xs-12">
                                    <el-upload
                                            class="avatar-uploader mb-3"
                                            :action="uploadPicOptions.action"
                                            :data="uploadPicOptions.params"
                                            :accept="uploadPicOptions.acceptFileType"
                                            :show-file-list="false"
                                            :on-success="handleAvatarSuccess2"
                                            :before-upload="beforeAvatarUpload2"
                                    >
                                        <img
                                                v-if="(resumeForm2.avatarUrl && resumeForm2.avatarUrl !== '')"
                                                :src="resumeForm2.avatarUrl"
                                                class="avatar1"
                                        />
                                        <i v-else class="el-icon-plus avatar-uploader-icon2"></i>
                                        <div slot="tip" class="el-upload__tip">*身份证国徽面*</div>
                                    </el-upload>
                                </div>
                            </div>
                        </b-media>
                        <b-media class="col-lg-8 col-md-8 col-sm-12 col-xs-12">
                            <div class="row mt-3">
                                <div class="col-lg-4 col-md-12 col-sm-12 col-xs-12">
                                    <el-upload
                                            class="avatar-uploader mb-3"
                                            :action="uploadPicOptions.action"
                                            :data="uploadPicOptions.params"
                                            :accept="uploadPicOptions.acceptFileType"
                                            :show-file-list="false"
                                            :on-success="handleAvatarSuccess3"
                                            :before-upload="beforeAvatarUpload3"
                                    >
                                        <img
                                                v-if="(resumeForm3.avatarUrl && resumeForm3.avatarUrl !== '')"
                                                :src="resumeForm3.avatarUrl"
                                                class="avatar1"
                                        />
                                        <i v-else class="el-icon-plus avatar-uploader-icon3"></i>
                                        <div slot="tip" class="el-upload__tip">*手持身份证正面照*</div>
                                    </el-upload>
                                </div>
                            </div>
                        </b-media>
                    </el-form-item>
                    <el-form-item label="注意事项：" label-width="100px">
                        <div style="line-height: 20px">
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
                    <div class="Btn">
                        <el-form-item>
                            <el-row>
                                <el-button type="primary" @click="onSubmit">提交认证信息</el-button>
                                <el-button type="primary" @click="onSubmit4">其他认证方式</el-button>
                            </el-row>
                        </el-form-item>
                    </div>
                </el-form>
            </div>
            <div v-if="active===1"></div>
            <div v-if="active===2"></div>
            <div v-if="active===3"></div>
            <div class="Btn">
                <el-row>
                    <el-button type="primary" @click="prev1" v-if="active==1">上一步</el-button>
                    <el-button type="primary" @click="next2" v-if="active==1" class="next-button">下一步</el-button>
                    <el-button type="primary" @click="prev" v-if="active==2">上一步</el-button>
                    <el-button type="primary" @click="next3" v-if="active==2">下一步</el-button>
                </el-row>
            </div>
        </div>
    </div>
</template>

<script>
    import {listByType} from "@/api/dict_api";
    import Pagination from "@/components/Pagination";
    import {mapGetters} from "vuex";
    import {getUserId, getToken} from '@/utils/auth'
    import {formatListQuery, parseListQuery} from "@/utils/common";
    import {addInterview, addInterviewDirection, addInterviewAuth} from "@/api/interview_api";
    import Toast from "@/utils/toast";
    import {getResumeInfo} from "@/api/interview_api";
    import {getUploadPicToken} from "@/api/upload_api";
    import {checkPicSize} from "@/utils/common";

    export default {
        components: {
            Pagination
        },
        computed: {
            ...mapGetters(["keyword"])
        },
        data() {
            const state = {
                token: getToken(),
                userId: getUserId(),
            }
            console.log(state.userId)
            console.log(state.token)
            var isIdNumber = (rule, value, callback) => {
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
            var isPrice = (rule, value, callback) => {
                if (!value) {
                    callback();
                } else {
                    var reg = /^-?\d{1,4}(?:\.\d{1,2})?$/;
                    if (reg.test(value)) {
                        callback();
                    } else {
                        callback(new Error("数字格式:0-9999或小数点后可加1到2位"));//如:1 或1.8 或1.85
                    }
                }
            };
            return {
                uploadPicOptions: {
                    action: "",
                    params: {},
                    fileUrl: "",
                    acceptFileType: ".jpg,.jpeg,.png,.JPG,.JPEG,.PNG",
                },
                loading: false,
                resume: {},
                posting: false,
                listQuery: {
                    keyword: "",
                    page: 1,
                    limit: 10,
                    industryId: '',
                    experienceTimeId: ''
                },
                dialogImageUrl: '',
                dialogVisible: false,
                checked1: true,
                checked2: false,
                checked3: false,
                active: 1,
                total: 0,
                industryOptions: [],
                experienceTimeOptions: [],
                showNoResult: false,
                resumeForm: {
                    "avatarUrl": "",
                    "createTime": "",
                    "delFlag": "0",
                    "id": "",
                    "type": "",
                    "updateTime": ""
                },
                resumeForm1: {
                    "avatarUrl": "",
                    "createTime": "",
                    "delFlag": "0",
                    "id": "",
                    "type": "",
                    "updateTime": ""
                },
                resumeForm2: {
                    "avatarUrl": "",
                    "createTime": "",
                    "delFlag": "0",
                    "id": "",
                    "type": "",
                    "updateTime": ""
                },
                resumeForm3: {
                    "avatarUrl": "",
                    "createTime": "",
                    "delFlag": "0",
                    "id": "",
                    "type": "",
                    "updateTime": ""
                },
                formOne: {
                    nickName: '',
                    avatar: '',
                    industryId: '',
                    experienceTimeId: '',
                    companyName: '',
                    description: '',
                    experience: [{experienceItem: ''}],
                    delFlag: 0,
                },

                formTwo: {
                    direction: '',
                    description: '',
                    interviewerId: state.userId,
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
                    nickName: [
                        {required: true, message: '请输入用户昵称', trigger: 'blur'},
                        {max: 20, message: '昵称不超过20字符'},

                    ],
                    faceUrl: [
                        {required: true, message: '请上传身份证人面像', trigger: 'change'},
                    ],
                    emblemUrl: [
                        {required: true, message: '请上传身份证国徽面', trigger: 'change'},
                    ],
                    holdUrl: [
                        {required: true, message: '请上传手持身份证正面照', trigger: 'change'},
                    ],
                    avatar: [
                        {required: true, message: "请上传头像", trigger: "change"}
                    ],
                    industryId: [
                        {required: true, message: "请选择行业", trigger: "change"}
                    ],
                    experienceTimeId: [
                        {required: true, message: "请选择从业时间", trigger: "change"}
                    ],
                    name: [
                        {required: true, message: '请输入您的姓名', trigger: 'blur'},
                    ],
                    idNumber: [{
                        required: true,
                        trigger: 'blur',
                        validator: isIdNumber
                    }
                    ],

                    companyName: [
                        {required: true, message: '请输入您的公司名称', trigger: 'blur'},
                    ],
                    description: [{required: true, message: '请输入面试官自述', trigger: 'blur'},
                        {max: 150, message: '长度不超过150个字'}
                    ],
                    experience: [{
                        required: true,
                        message: '请输入从业经历',
                        trigger: 'blur'
                    },
                        {
                            max: 150,
                            message: '长度不超过150个字',
                        }
                    ],
                    price: [{
                        required: true,
                        message: '请输入您理想资询价位',
                        trigger: 'blur'
                    },
                        {
                            validator: isPrice,
                            trigger: 'blur'
                        }
                    ]
                },
                avatarList: [],
            };
        },
        created() {
            this.$emit("complete");
            this.initData();
            this.getList();
        },
        watch: {
            $route() {
                this.getList();
            },
            keyword() {
                this.library.keyword = this.keyword;
                this.handleRouteList();
            }
        },
        methods: {
            initData() {
                listByType(18).then(
                    response => (this.industryOptions = response.data.list)
                );
                listByType(13).then(
                    response => (this.experienceTimeOptions = response.data.list)
                );
                this.getList();
                this.getResumeInfo();

                this.$axios.get("/mock/avatar").then(data => {
                    this.avatarList = data.data;
                })
            },
            beforeAvatarUpload(file) {
                return new Promise((resolve, reject) => {
                    if (checkPicSize(file)) {
                        reject();
                    } else {
                        this.loading = true;
                        this.resumeForm.avatarUrl = URL.createObjectURL(file);
                        this.formOne.avatar = URL.createObjectURL(file);
                        getUploadPicToken(file.name)
                            .then((response) => {
                                const {data} = response;
                                this.uploadPicOptions.action = data.host;
                                this.uploadPicOptions.params = data;
                                this.uploadPicOptions.fileUrl = data.host + "/" + data.key;
                                resolve(data);
                            })
                            .catch((error) => {
                                reject(error);
                            });
                    }
                });
            },
            beforeAvatarUpload1(file) {
                return new Promise((resolve, reject) => {
                    if (checkPicSize(file)) {
                        reject();
                    } else {
                        getUploadPicToken(file.name)
                            .then((response) => {
                                const {data} = response;
                                this.uploadPicOptions.action = data.host;
                                this.uploadPicOptions.params = data;
                                this.uploadPicOptions.fileUrl = data.host + "/" + data.key;
                                resolve(data);
                            })
                            .catch((error) => {
                                reject(error);
                            });
                    }
                });
            },
            beforeAvatarUpload2(file) {
                return new Promise((resolve, reject) => {
                    if (checkPicSize(file)) {
                        reject();
                    } else {
                        getUploadPicToken(file.name)
                            .then((response) => {
                                const {data} = response;
                                this.uploadPicOptions.action = data.host;
                                this.uploadPicOptions.params = data;
                                this.uploadPicOptions.fileUrl = data.host + "/" + data.key;
                                resolve(data);
                            })
                            .catch((error) => {
                                reject(error);
                            });
                    }
                });
            },
            beforeAvatarUpload3(file) {
                return new Promise((resolve, reject) => {
                    if (checkPicSize(file)) {
                        reject();
                    } else {
                        getUploadPicToken(file.name)
                            .then((response) => {
                                const {data} = response;
                                this.uploadPicOptions.action = data.host;
                                this.uploadPicOptions.params = data;
                                this.uploadPicOptions.fileUrl = data.host + "/" + data.key;
                                resolve(data);
                            })
                            .catch((error) => {
                                reject(error);
                            });
                    }
                });
            },
            addExperience() {
                this.formOne.experience.push({experienceItem: ''})
            },
            handleAvatarSuccess() {
                this.loading = false;
                console.log(this.formOne.avatar);
            },
            handleAvatarSuccess1() {
                this.resumeForm1.avatarUrl = this.uploadPicOptions.fileUrl;
                this.formThree.faceUrl = this.uploadPicOptions.fileUrl;
                console.log(this.formThree.emblemUrl);
            },
            handleAvatarSuccess2() {
                this.resumeForm2.avatarUrl = this.uploadPicOptions.fileUrl;
                this.formThree.emblemUrl = this.uploadPicOptions.fileUrl;
                console.log(this.formThree.emblemUrl);
            },
            handleAvatarSuccess3() {
                this.resumeForm3.avatarUrl = this.uploadPicOptions.fileUrl;
                this.formThree.holdUrl = this.uploadPicOptions.fileUrl;
                console.log(this.formThree.holdUrl);
            },
            getResumeInfo() {
                getResumeInfo().then((response) => {
                    this.resume = response.data;
                });
            },

            getList() {
                this.showNoResult = false;
                parseListQuery(this.$route.query, this.listQuery);
            },
            handleFilter() {
                this.listQuery.page = 1;
                this.handleRouteList();
            },
            handleRouteList() {
                this.$router.replace({
                    path: this.$route.path,
                    query: formatListQuery(this.listQuery)
                });
            },
            prev() {
                --this.active;
                if (this.active < 0) this.active = 0;
            },
            prev1() {
                let query = {
                    ...this.$route.query
                };
                this.$router.push({
                    path: this.redirect || "/InterviewSecretPage",
                    query
                });
            },
            onSubmit() {
                this.$refs["formThree"].validate(valid => {
                    if (valid) {
                        addInterviewAuth(this.formThree).then(() => {

                                Toast.success("提交成功");
                                let query = {
                                    ...this.$route.query
                                };
                                this.$router.push({
                                    path: this.redirect || "/RegisterOther",
                                    query
                                });
                            }
                        )
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                })
            },
            onSubmit1() {
                this.$refs["formOne"].validate(valid => {
                    if (valid) {
                        addInterview(this.formOne).then(() => {
                            Toast.success("提交成功，请点击下一步");
                        })

                    } else {
                        console.log('error submit!!');

                    }
                })
            },
            next2() {
                this.$refs["formOne"].validate(valid => {
                    if (valid) {
                        this.formOne.experience = this.formOne.experience.map(item => item.experienceItem)
                        addInterview(this.formOne).then(() => {
                            Toast.success("提交成功");
                        })
                        this.active++;
                    } else {
                        console.log('error submit!!');

                    }
                });
            },
            next3() {
                this.$refs["formTwo"].validate(valid => {
                    if (valid) {
                        addInterviewDirection(this.formTwo).then(() => {
                                Toast.success("提交成功");
                            }
                        )
                        this.active++;
                    } else {
                        console.log('error submit!!');

                    }
                });
            },

            onSubmit4(evt) {
                evt.preventDefault();
                let query = {
                    ...this.$route.query
                };
                delete query.redirect;
                this.$router.push({
                    path: this.redirect || "/RegisterInterviewerSuccess",
                    query
                });
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

        .main-container {
            overflow-y: auto;

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

            ::v-deep .el-form-item {
                margin-top: 30px;
            }

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
            }

            /deep/ .el-textarea__inner {
                border-radius: 13px;
            }

            .avatar-uploader .el-upload {
                cursor: pointer;
                position: relative;
                overflow: hidden;
            }

            .avatar-uploader .el-upload:hover {
                border-color: #3a8ee6;
            }

            $avatarSize: 100px;

            .avatar-uploader .avatar-uploader-icon {
                border: 1px solid #333333;
                border-radius: 50%;
                font-size: 28px;
                color: #8c939d;
                width: $avatarSize;
                height: $avatarSize;
                line-height: $avatarSize;
                text-align: center;
                padding: 36px 31px;
            }

            .avatar-uploader .avatar-uploader-tip {
                font-size: 18px;
                font-weight: 400;
                color: #999999;
                line-height: 25px;
                margin-top: -3px;
            }

            .avatar-uploader .avatar-uploader-icon1 {
                border: 1px dashed #d9d9d9;
                font-size: 28px;
                color: #3a8ee6;
                width: 161px;
                height: 111px;
                line-height: 120px;
                text-align: center;
                background-image: url("../assets/idcard1.png");
            }

            .avatar-uploader .avatar-uploader-icon2 {
                border: 1px dashed #d9d9d9;
                font-size: 28px;
                color: #3a8ee6;
                width: 161px;
                height: 111px;
                line-height: 120px;
                text-align: center;
                background-image: url("../assets/idcard2.png");
            }

            .avatar-uploader .avatar-uploader-icon3 {
                border: 1px dashed #d9d9d9;
                font-size: 28px;
                color: #3a8ee6;
                width: 161px;
                height: 111px;
                line-height: 121px;
                text-align: center;
                background-image: url("../assets/idcard3.png");
            }


            .avatar-uploader .avatar1 {
                width: 161px;
                height: 121px;
                display: block;
            }

            .avatar-uploader .avatar {
                width: $avatarSize;
                height: $avatarSize;
                display: block;
            }

            .upload-attach-box .el-upload {
                display: block;
            }

            .upload-attach-box .el-upload button {
                width: 100%;
                font-size: 18px;
                padding: 12px;
            }

            .section1-select {
                flex: 1;
                width: 360px;
                /*margin: 0 5px 5px;*/
                height: 100%;
            }

            .Btn {
                margin-left: 400px;
                margin-top: 12px;

                /deep/ .el-button--primary {
                    width: 136px;
                    height: 40px;
                    background: #FFFFFF;
                    border-radius: 21px;
                    border: 1px solid #3D6FF4;
                    font-size: 18px;
                    color: #3D6FF4;
                }

                .next-button {
                    width: 136px;
                    height: 40px;
                    background: #EDF3FF;
                    border-radius: 21px;
                    font-size: 18px;
                    border: 1px solid #EDF3FF;
                    color: #3D6FF4;
                }
            }

            .mb-3, .my-3 {
                margin-bottom: 0px;
            }
        }
    }

</style>
