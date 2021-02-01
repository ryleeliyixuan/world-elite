<template>
    <div class="app-container">
        <div class="info-container" v-if="activity">
            <el-image :src="require('@/assets/activity/activity-top-bg.png')" class="image-bg" fit="cover"></el-image>
            <div class="content-container">
                <div class="line1">
                    <div class="text" @click="$router.go(-1)" style="cursor: pointer;">活动</div>
                    <div class="text">></div>
                    <div class="text">{{activity.title}}</div>
                </div>
                <div class="line2">
                    <div class="text">{{activity.title}}</div>
                    <div class="tag">{{organizerTypeList[activity.organizerType]}}</div>
                </div>
                <div class="line3">
                    <div class="name">活动形式：</div>
                    <div class="value" :style="{color:activity.form===0?'#FFAB40':'#64FFDA'}">{{activity.form===0?'线上':'线下'}}</div>
                    <div class="name">活动状态：</div>
                    <div class="value status" :style="{background:statusBGColorList[activity.status]}">{{statusList[activity.status]}}</div>
                </div>
                <div class="line3">
                    <div class="name">活动主办方：</div>
                    <div class="value">{{activity.organizerInfoVo.organizerName}}</div>
                    <div class="name">{{/^([a-zA-Z0-9]([a-zA-Z0-9\-]{0,61}[a-zA-Z0-9])?\.)+[a-zA-Z]{2,6}$/.test(activity.address)?'活动链接：':'活动地址：'}}</div>
                    <!--  <a class="value" style="text-decoration:underline; cursor: pointer;" href="http://www.myworldelite.com" target="_blank">{{activity.address}}</a>-->
                    <div class="value">{{activity.address}}</div>
                </div>
                <div class="line3">
                    <div class="name">活动时间：</div>
                    <div class="value">
                        {{activity.activityStartTime | timestampToDateHourMinute}}-{{activity.activityFinishTime | timestampToDateHourMinute}}
                    </div>
                    <div class="name">报名时间：</div>
                    <div class="value" :style="{color:applyTimeColor}">
                        {{activity.registrationStartTime | timestampToDateHourMinute}}-{{activity.registrationFinishTime | timestampToDateHourMinute}}
                    </div>
                </div>
                <div class="line3">
                    <div class="name">发布时间：</div>
                    <div class="value">{{activity.createTime | timestampToDateHourMinute}}</div>
                </div>
            </div>
            <div class="apply-container">
                <div class="line1" v-if="this.$route.params.id !== 'preview'">
                    <div class="image-button-bg" @click="onCollect">
                        <el-image v-if="activity.attentionFlag" class="image" :src="require('@/assets/activity/collected.png')"></el-image>
                        <el-image v-else class="image" :src="require('@/assets/activity/collect.png')"></el-image>
                    </div>

                    <el-popover placement="bottom-start" trigger="hover">
                        <div slot="reference" class="image-button-bg">
                            <el-image class="image" :src="require('@/assets/activity/share.png')"></el-image>
                        </div>
                        <share :config="shareConfig"></share>
                    </el-popover>

                    <div :class="activity.registrationFlag?'apply-button2':'apply-button'" @click="onApply">{{activity.registrationFlag?'已报名':'报名'}}</div>
                </div>
                <div class="line2" v-if="activity.numberLimit && activity.numberLimit!==-1">
                    报名名额还剩<span>{{activity.numberLimit - activity.applicantQuantity}}</span>个
                </div>
            </div>
            <div class="report-button" @click="onReport" v-if="this.$route.params.id !== 'preview'">举报该活动</div>
        </div>
        <div class="activity-container" v-if="activity">
            <el-image class="image" :src="activity.poster" fit="contain"></el-image>
            <div v-html="activity.description"></div>
        </div>
        <el-dialog class="report-dialog"
                   title="举报活动"
                   :center="true"
                   :visible.sync="reportDialogVisible"
                   v-loading="reporting"
                   width="445px">
            <el-select v-model="reportForm.optionId" placeholder="请选择举报类型" class="option" size="small">
                <el-option
                    v-for="item in reportOptionList"
                    :key="item.value"
                    :label="item.name"
                    :value="item.value">
                </el-option>
            </el-select>

            <el-input type="textarea"
                      :rows="3"
                      placeholder="请详细描述举报原因（必填，300字以内）"
                      v-model="reportForm.reason"
                      maxlength="300"
                      resize="none"
                      class="reason">
            </el-input>
            <el-upload class="upload"
                       action=""
                       v-loading.fullscreen.lock="fullscreenLoadingCount>0"
                       :show-file-list="true"
                       :on-success="handleEditorUploadSuccess"
                       :on-error="handleEditorUploadError"
                       :http-request="imageUpload"
                       :accept="acceptFileType"
                       multiple
                       :limit="5">
                <svg-icon icon-class="report-upload" class="report-upload"/>
                <span class="text">上传照片等证明（选填，最多五份）</span>
            </el-upload>
            <div class="button-container">
                <div class="cancel" @click="onCancel">取消</div>
                <div class="confirm" @click="onConfirm">确定</div>
            </div>
        </el-dialog>

        <preview-apply v-if="activity"
                       submit
                       :visible.sync="previewDialogVisible"
                       :activityId="this.$route.params.id+''"
                       :apply="applyTable"
                       :resumeList="resumeList"
                       :needResume="activity.needResume"
                       @apply="onApplySuccess"></preview-apply>

        <confirm :visible.sync="showConfirmDialog" :content="confirmDialogContent" :title="confirmDialogTitle" @confirm="showApplyDialog"></confirm>
        <alert :visible.sync="showAlertDialog" :content="alertDialogContent" :title="alertDialogTitle" :icon="alertIcon" iconStyle="width:250px;"></alert>
    </div>
</template>

<script>
    import Vue from "vue";
    import Share from "vue-social-share";
    import "vue-social-share/dist/client.css";
    import {getUploadPicToken} from "@/api/upload_api";
    import previewApply from "@/components/activity/PreviewApply";
    import confirm from "@/components/Dialog/Confirm"
    import alert from "@/components/Dialog/Alert"

    Vue.use(Share);

    export default {
        name: "ActivityDetailPage",
        components: {previewApply, confirm, alert},
        data() {
            return {
                activity: undefined,
                organizerTypeList: ['', '学生活动', '社会组织活动', '个人活动', '企业活动'],
                statusList: ['审核中', '草稿', '已停止', '即将开始', '报名中', '进行中', '活动结束', '审核未通过'],
                statusBGColorList: ['#4895EF', '#C6FF00', '#B71C1C', '#FFC400', '#66BB6A', '#FF6E40', '#FF5252', '#37474F'],
                shareConfig: {
                    url: window.location.href,
                    source: "",
                    title: "",
                    description: "",
                    image: "",
                    sites: ["wechat", "qq", "weibo", "google", "facebook", "twitter"],
                },

                // 举报
                reportDialogVisible: false, // 显示举报对话框
                reportOptionList: [], // 举报选项
                reportForm: { // 举报参数
                    activityId: undefined,
                    optionId: undefined,
                    reason: undefined,
                    evidence: [],
                },
                reporting: false, // 举报提交中

                // 上传
                fullscreenLoadingCount: 0,
                acceptFileType: '.jpg,.jpeg,.png,.JPG,.JPEG,.PNG',

                previewDialogVisible: false, // 报名表预览对话框
                applyTable: undefined, // 报名表
                resumeList: [], // 简历列表

                // 确认对话框
                showConfirmDialog: false,
                confirmDialogContent: "此活动报名后需等待主办方审核筛选，是否报名此活动？<br/>（通过筛选后会向您发送系统及邮件通知）",
                confirmDialogTitle: undefined,

                showAlertDialog: false,
                alertDialogContent: "递交报名表成功！<br/>（请耐心等待报名结果，结果将会以站内消息和邮件通知）",
                alertDialogTitle: undefined,
                alertIcon: "forgetpwd-success",
            };
        },
        computed: {
            applyTimeColor() {
                if (this.activity.registrationStartTime > Date.now()) {
                    return '#FFFFFF';
                } else if (this.activity.registrationFinishTime < Date.now()) {
                    return '#FF6E40';
                } else {
                    return '#C6FF00';
                }
            }
        },
        created() {
            this.initData();
        },
        methods: {
            // 初始化数据
            initData() {
                if (this.$route.params.id === "preview") {
                    this.activity = this.$storage.getData("activityPreview");
                    this.activity.organizerInfoVo = this.activity.organizerInfoForm;
                } else {
                    // 活动信息
                    this.$axios.get("/activity/activity-info", {params: {id: this.$route.params.id}}).then(response => {
                        this.activity = response.data;
                        this.shareConfig.title = this.activity.title;
                        // this.shareConfig.description = this.activity.description;

                        // 获取报名表信息
                        return this.$axios.get(`/activity-questionnaire/${this.activity.questionnaireId}`)
                    }).then(response => {
                        this.applyTable = response.data;
                    })

                    // 举报选项
                    this.$axios.get("/dict/list", {params: {type: 24, limit: 99}}).then(data => {
                        this.reportOptionList = data.data.list;
                    })
                }
            },

            // 点击收藏
            onCollect() {
                this.$axios.post("/favorite/favorite", {objectId: this.$route.params.id, type: 3, favorite: !this.activity.attentionFlag}).then(() => {
                    this.activity.attentionFlag = !this.activity.attentionFlag;
                })
            },

            // 点击报名按钮
            onApply() {
                if (!this.activity.registrationFlag) {
                    if (this.activity.auditType === '0' && this.activity.sendNoticeConfirm==='1') {
                        this.showConfirmDialog = true;
                    } else {
                        this.showApplyDialog();
                    }
                }
            },

            // 显示报名表
            showApplyDialog() {
                this.$axios.get("/resume/my-resume").then(response => {
                    this.resumeList = response.data;
                })
                this.previewDialogVisible = true;
            },

            // 报名成功
            onApplySuccess() {
                this.activity.registrationFlag = true;
                // TODO 当auditType==='1'时不需要审核，可能不需要显示成功对话框
                this.showAlertDialog = true;
            },

            // 点击举报活动按钮
            onReport() {
                this.reportDialogVisible = true;
            },

            // 图片上传成功
            handleEditorUploadSuccess(response, file, fileList) {
                console.log(fileList);
                this.reportForm.evidence = fileList.map(item => item.response);
                this.fullscreenLoadingCount--;
            },

            // 图片上传错误
            handleEditorUploadError(err, file, fileList) {
                this.$message.error("图片上传失败")
                this.fullscreenLoadingCount--;
            },

            // 图片上传
            imageUpload(request) {
                return new Promise((resolve, reject) => {
                    this.fullscreenLoadingCount++;
                    getUploadPicToken(request.file.name).then(response => {
                        const {data} = response
                        this.$axios.upload(data.host, request.file, data).then(() => {
                            resolve(data.host + '/' + data.key);
                        }).catch(() => {
                            reject();
                        })
                    })
                })
            },

            // 取消举报对话框
            onCancel() {
                this.reportForm.activityId = undefined;
                this.reportForm.optionId = undefined;
                this.reportForm.reason = undefined;
                this.reportForm.evidence = [];
                this.reportDialogVisible = false;
            },

            // 提交举报
            onConfirm() {
                if (!this.reportForm.optionId) {
                    this.$message.warning("请选择举报类型");
                } else if (!this.reportForm.reason) {
                    this.$message.warning("请输入举报原因");
                } else {
                    this.reportForm.activityId = this.$route.params.id;
                    this.reporting = true;
                    this.$axios.post("/activity/report", this.reportForm).then(data => {
                        this.reporting = false;
                        this.onCancel();
                        this.$message.success("已提交举报信息");
                    })
                }
            },
        }
    };
</script>

<style scoped lang="scss">

    .app-container {
        width: 1200px !important;

        .info-container {
            position: relative;
            width: 100%;
            height: 268px;
            box-shadow: 0px 4px 16px 3px rgba(191, 199, 215, 0.31);

            .image-bg {
                width: 100%;
                height: 100%;
            }

            .content-container {
                position: absolute;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                padding: 26px 20px;

                .line1 {
                    display: flex;
                    align-items: center;

                    .text {
                        font-size: 16px;
                        font-weight: 400;
                        color: #DDDDDD;
                        line-height: 22px;
                        padding-right: 8px;
                    }
                }

                .line2 {
                    display: flex;
                    align-items: center;
                    margin-top: 15px;

                    .text {
                        font-size: 30px;
                        color: #FFFFFF;
                        line-height: 42px;
                        margin-right: 17px;
                    }

                    .tag {
                        height: 34px;
                        background: #42A5F5;
                        border-radius: 6px;
                        line-height: 34px;
                        padding: 0 23px;
                        font-size: 18px;
                        color: #FFFFFF;
                    }
                }

                .line3 {
                    display: flex;
                    align-items: center;
                    margin-top: 9px;

                    .name {
                        width: 110px;
                        text-align: right;
                        font-size: 18px;
                        color: #EEEEEE;
                        line-height: 25px;
                    }

                    .value {
                        font-size: 18px;
                        color: #FFFFFF;
                        line-height: 25px;
                        width: 372px;
                    }

                    .status {
                        width: auto;
                        padding: 0 13px;
                        height: 30px;
                        border-radius: 5px;
                        line-height: 30px;
                        text-align: center;
                    }
                }
            }

            .apply-container {
                position: absolute;
                top: 56px;
                right: 20px;

                .line1 {
                    display: flex;
                    align-items: center;

                    .image-button-bg {
                        width: 34px;
                        height: 34px;
                        background: #FFFFFF;
                        box-shadow: 0 4px 7px 0 #253668;
                        border-radius: 17px;
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        margin-right: 10px;

                        ::v-deep .el-image {
                            line-height: 100%;
                        }

                        .image {

                        }
                    }

                    .apply-button {
                        width: 114px;
                        height: 34px;
                        background: #FFFFFF;
                        box-shadow: 0 4px 7px 0 #253668;
                        border-radius: 17px;
                        font-size: 18px;
                        color: #333333;
                        line-height: 34px;
                        text-align: center;
                        cursor: pointer;
                    }

                    .apply-button2 {
                        width: 114px;
                        height: 34px;
                        background: #DDDDDD;
                        box-shadow: 0 4px 7px 0 #253668;
                        border-radius: 17px;
                        font-size: 18px;
                        color: #666666;
                        line-height: 34px;
                        text-align: center;
                        cursor: pointer;
                    }
                }

                .line2 {
                    margin-top: 21px;
                    font-size: 18px;
                    color: #DDDDDD;
                    line-height: 25px;
                    text-align: right;

                    span {
                        display: inline-block;
                        padding: 0 10px;
                        height: 25px;
                        background: #0D47A1;
                        border-radius: 3px;
                        font-size: 18px;
                        color: #FFFFFF;
                        line-height: 25px;
                        text-align: center;
                        margin: 0 5px;
                    }
                }
            }

            .report-button {
                position: absolute;
                right: 35px;
                bottom: 29px;
                font-size: 18px;
                color: #CFD8DC;
                line-height: 25px;
                cursor: pointer;
            }
        }

        .activity-container {
            width: 100%;
            padding: 22px 36px 26px;
            background: #FFFFFF;
            box-shadow: 0 4px 16px 3px rgba(191, 199, 215, 0.31);

            .image {
                width: 100%;
                height: auto;
            }
        }

        .report-dialog {
            ::v-deep .el-dialog {
                border-radius: 13px;
            }

            .option {
                ::v-deep .el-input__inner {
                    background: #FFFFFF;
                    border-radius: 6px;
                    border: 1px solid #B0BEC5;
                }
            }

            .reason {
                margin-top: 17px;

                ::-webkit-input-placeholder {
                    font-size: 16px;
                    line-height: 25px;
                }

                :-moz-placeholder {
                    font-size: 16px;
                    line-height: 25px;
                }

                :-ms-input-placeholder {
                    font-size: 16px;
                    line-height: 25px;
                }

                ::v-deep .el-textarea__inner {
                    width: 395px;
                    height: 93px;
                    background: #FFFFFF;
                    border-radius: 6px;
                    border: 1px solid #B0BEC5;
                }
            }

            .upload {
                display: flex;
                align-items: center;
                margin-top: 10px;
                flex-direction: column;

                ::v-deep .el-upload {
                    display: flex;
                    align-items: center;
                }

                .report-upload {
                    width: 20px;
                    height: 20px;
                }

                .text {
                    font-size: 16px;
                    font-weight: 400;
                    color: #4895EF;
                    line-height: 22px;
                    margin-left: 9px;
                }
            }

            .button-container {
                display: flex;
                align-items: center;
                justify-content: center;
                margin-top: 26px;

                .cancel {
                    width: 107px;
                    height: 35px;
                    background: #FFFFFF;
                    border-radius: 18px;
                    border: 1px solid #4895EF;
                    font-size: 16px;
                    color: #4895EF;
                    line-height: 35px;
                    text-align: center;
                    cursor: pointer;
                }

                .confirm {
                    margin-left: 21px;
                    width: 107px;
                    height: 35px;
                    background: #4895EF;
                    border-radius: 18px;
                    border: 1px solid #FFFFFF;
                    font-size: 16px;
                    color: #FFFFFF;
                    line-height: 35px;
                    text-align: center;
                    cursor: pointer;
                }
            }
        }
    }

</style>
