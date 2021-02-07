<template>
    <div class="app-container">
        <div class="title">{{this.$route.query.id?'活动编辑':'活动发布'}}</div>
        <div class="content-container">
            <div class="line">
                <div class="name">
                    活动名称<span>*</span>
                </div>
                <el-input placeholder="请输入活动名（最多20字）"
                          v-model="activityForm.title"
                          size="small"
                          class="input">
                </el-input>
                <div class="option-container" @click="onOverseasStudent">
                    <svg-icon v-if="activityForm.onlyOverseasStudent!=='1'" icon-class="unselected" style="margin: 0 3px 0 14px;"></svg-icon>
                    <svg-icon v-else icon-class="selected" style="margin: 0 3px 0 14px;"></svg-icon>
                    <div class="option-text">此活动为留学生活动</div>
                </div>
                <el-tooltip class="item" effect="dark" content="受众人群仅限留学生群体" placement="top">
                    <svg-icon icon-class="questionMask" style="width: 10px; height: 11px; margin-left: 4px;"></svg-icon>
                </el-tooltip>
            </div>
            <div class="line">
                <div class="name" style="align-self: flex-start;">
                    <el-tooltip class="item" effect="dark" content="资料仅用作审核，不会公开或用于其他用途。" placement="top">
                        <svg-icon icon-class="questionMask" style="width: 10px; height: 11px; margin: 8px 4px;"></svg-icon>
                    </el-tooltip>
                    主办方<span>*</span>
                </div>
                <el-input placeholder="请输入主办方名称（最多20字）"
                          v-model="activityForm.organizerInfoForm.organizerName"
                          size="small"
                          class="input">
                </el-input>
            </div>
            <div class="line">
                <div class="name">
                    活动城市/线上<span>*</span>
                </div>
                <el-cascader placeholder="城市"
                             :show-all-levels="false"
                             :options="cityOptions"
                             :props="cityIdProps"
                             clearable
                             size="small"
                             style="width: 100px;"
                             v-model="activityForm.cityId">
                </el-cascader>
                <div class="name" style="margin-left: 10px;">
                    详细地址/线上链接<span>*</span>
                </div>
                <el-input placeholder="请在此处填写详细地址/线上地址"
                          v-model="activityForm.address"
                          size="small"
                          class="input">
                </el-input>
            </div>
            <div class="line">
                <div class="name" style="align-self: flex-start;">
                    报名需求<span>*</span>
                </div>
                <div class="value-container">
                    <div class="value-line-container">
                        <div class="option-container" @click="onNeedRegistration('1')">
                            <svg-icon v-if="this.$route.query.id && activityForm.needRegistration==='1'" icon-class="selected-disabled" style="margin-right: 3px;"></svg-icon>
                            <svg-icon v-else-if="this.$route.query.id && activityForm.needRegistration==='0'" icon-class="unselect-disabled." style="margin-right: 3px;"></svg-icon>
                            <svg-icon v-else-if="activityForm.needRegistration!=='1'" icon-class="unselected" style="margin-right: 3px;"></svg-icon>
                            <svg-icon v-else icon-class="selected" style="margin-right: 3px;"></svg-icon>
                            <div class="option-text">需要报名</div>
                        </div>
                        <div class="option-container" @click="onNeedRegistration('0')" style="margin-left: 21px">
                            <svg-icon v-if="this.$route.query.id && activityForm.needRegistration==='1'" icon-class="selected-disabled" style="margin-right: 3px;"></svg-icon>
                            <svg-icon v-else-if="this.$route.query.id && activityForm.needRegistration==='0'" icon-class="unselect-disabled." style="margin-right: 3px;"></svg-icon>
                            <svg-icon v-else-if="activityForm.needRegistration!=='0'" icon-class="unselected" style="margin-right: 3px;"></svg-icon>
                            <svg-icon v-else icon-class="selected" style="margin-right: 3px;"></svg-icon>
                            <div class="option-text">无需报名</div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="line" v-if="activityForm.needRegistration==='1'">
                <div class="name">
                    报名时间<span>*</span>
                </div>
                <el-date-picker v-model="registrationTime"
                                type="datetimerange"
                                align="right"
                                start-placeholder="开始时间"
                                end-placeholder="结束时间"
                                size="small"
                                format="yyyy-MM-dd HH:mm"
                                :default-time="['09:00:00', '17:00:00']"
                                :picker-options="registrationTimeOption">
                </el-date-picker>
            </div>
            <div class="line" v-if="activityForm.needRegistration==='1'">
                <div class="name" style="align-self: flex-start;">
                    <el-tooltip class="item" effect="dark" placement="top">
                        <div slot="content">若报名无需审核，学生报名即可参加；若需<br/>要审核，可在活动报名管理信息页进行审核<br/>操作。</div>
                        <svg-icon icon-class="questionMask" style="width: 10px; height: 11px; margin: 8px 4px;"></svg-icon>
                    </el-tooltip>
                    报名审核<span>*</span>
                </div>
                <div class="value-container">
                    <div class="value-line-container">
                        <div class="option-container" @click="onAuditType('0')">
                            <svg-icon v-if="activityForm.auditType!=='0'" icon-class="unselected" style="margin-right: 3px;"></svg-icon>
                            <svg-icon v-else icon-class="selected" style="margin-right: 3px;"></svg-icon>
                            <div class="option-text">需要审核</div>
                        </div>
                        <div class="option-container" @click="onAuditType('1')" style="margin-left: 21px">
                            <svg-icon v-if="activityForm.auditType!=='1'" icon-class="unselected" style="margin-right: 3px;"></svg-icon>
                            <svg-icon v-else icon-class="selected" style="margin-right: 3px;"></svg-icon>
                            <div class="option-text">无需审核</div>
                        </div>
                    </div>
                    <div v-if="activityForm.auditType==='1'">
                        <div class="line">
                            <div class="name" style="width: auto;">人数限制</div>
                            <div class="option-container" @click="onNumberLimit(-1)">
                                <svg-icon v-if="activityForm.numberLimit!==-1" icon-class="unselected" style="margin-right: 3px;"></svg-icon>
                                <svg-icon v-else icon-class="selected" style="margin-right: 3px;"></svg-icon>
                                <div class="option-text">不限</div>
                            </div>
                            <div class="option-container" @click="onNumberLimit()" style="margin-left: 21px">
                                <svg-icon v-if="activityForm.numberLimit===-1" icon-class="unselected" style="margin-right: 3px;"></svg-icon>
                                <svg-icon v-else icon-class="selected" style="margin-right: 3px;"></svg-icon>
                                <div class="option-text">有限制</div>
                            </div>
                            <el-input placeholder="请在此输入具体人数"
                                      v-if="activityForm.numberLimit!==-1"
                                      v-model.number="activityForm.numberLimit"
                                      size="small"
                                      class="input"
                                      style="width: 150px; margin: 0 5px;">
                            </el-input>
                            <div v-if="activityForm.numberLimit!==-1">人</div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="line">
                <div class="name">
                    活动时间<span>*</span>
                </div>
                <el-date-picker v-model="activityTime"
                                type="datetimerange"
                                align="right"
                                start-placeholder="开始时间"
                                end-placeholder="结束时间"
                                size="small"
                                format="yyyy-MM-dd HH:mm"
                                :default-time="['09:00:00', '17:00:00']"
                                :picker-options="activityTimeOption">
                </el-date-picker>
            </div>
            <div class="line">
                <div class="name" style="align-self: flex-start; margin-top: 5px;">
                    活动介绍<span>*</span>
                </div>
                <tinymce v-model="activityForm.description"
                         width="100%"
                         class="textarea"
                         placeholder="请在此处输入活动简介及相关信息">
                </tinymce>
            </div>
            <div class="line" v-if="activityForm.needRegistration==='1'">
                <div class="name">
                    活动报名表<span>*</span>
                </div>
                <div v-if="this.$route.query.id" class="edit-button" @click="onPreviewRegistration">{{applyTableTitle}}</div>
                <div v-else style="display: flex; align-items: center;">
                    <div v-if="applyTableTitle" :class="['edit-button',{'edit-button-disabled':useTemplate}]"
                         @click="!useTemplate && onEditRegistrationTemplate()">
                        {{applyTableTitle}}
                    </div>
                    <div v-else :class="['add-button',{'add-button-disabled':useTemplate}]" @click="!useTemplate && onAddRegistrationTemplate()">
                        <svg-icon icon-class="add" style="margin-right: 8px;"></svg-icon>
                        添加报名表
                    </div>
                    <el-switch v-model="useTemplate" size="small" style="margin: 0 10px;" :disabled="!!this.$route.query.id"></el-switch>
                    <el-select v-model="templateId"
                               placeholder="使用模板"
                               class="select"
                               size="small"
                               :disabled="!useTemplate || !!this.$route.query.id"
                               style="margin-right:15px;">
                        <el-option
                            v-for="item in registrationTemplateList"
                            :key="item.id"
                            :label="item.templateName"
                            :value="item.id">
                        </el-option>
                    </el-select>
                    <el-popover
                        :disabled="!useTemplate"
                        placement="top-start"
                        trigger="click">
                        <div class="template-settings-container">
                            <div class="line1">
                                模板管理
                                <span>{{registrationTemplateList.length}}/{{templateCount.maxCount}}</span>
                                <el-tooltip class="item" effect="dark" placement="top">
                                    <div slot="content">最多可保存{{templateCount.maxCount}}份报名表模板</div>
                                    <svg-icon icon-class="questionMask" style="width: 10px; height: 11px; margin: 4px;"></svg-icon>
                                </el-tooltip>
                            </div>
                            <div class="line2" v-for="item in registrationTemplateList">
                                <span>{{item.templateName}}</span>
                                <div class="operate-container">
                                    <svg-icon icon-class="template-edit" clickable @click="onTemplateEdit(item)"></svg-icon>
                                    <svg-icon icon-class="template-delete" clickable @click="onTemplateDelete(item)"></svg-icon>
                                </div>
                            </div>
                            <div class="line3" v-if="registrationTemplateList.length<templateCount.maxCount" @click="onTemplateAdd">
                                添加模板
                                <svg-icon icon-class="template-add" clickable style="margin-left: 5px;"></svg-icon>
                            </div>
                        </div>

                        <svg-icon slot="reference" :icon-class="useTemplate?'settings-enable':'settings-disable'"
                                  style="width: 17px; height: 17px; vertical-align: -4px;"></svg-icon>
                    </el-popover>
                </div>
            </div>
            <div class="poster-container">
                <div class="line" style="margin-top: 0;">
                    <div class="name" style="width: auto;">
                        活动海报<span>*</span>
                    </div>
                    <div class="tips">为确保正常显示，请上传16:9的海报</div>
                </div>
                <el-upload class="poster-uploader"
                           :action="posterUploadPicOptions.action"
                           :data="posterUploadPicOptions.params"
                           :accept="posterUploadPicOptions.acceptFileType"
                           :show-file-list="false"
                           :on-success="posterUploadSuccess"
                           :before-upload="posterUpload">
                    <el-image v-if="posterUploadPicOptions.localUrl || activityForm.poster"
                              :src="posterUploadPicOptions.localUrl || activityForm.poster"
                              v-loading="posterUploadPicOptions.loading"
                              fit="scale-down"
                              class="poster-uploader"/>
                    <div v-else class="poster-add">
                        <svg-icon icon-class="activity-upload" class="upload"></svg-icon>
                    </div>
                </el-upload>
            </div>
        </div>
        <div class="button-container">
            <div class="cancel" @click="onCancel">取消</div>
            <div class="preview" @click="onPreview">预览</div>
            <div class="submit" @click="onSubmit" :loading="publishing">提交</div>
        </div>
        <el-dialog class="cancel-dialog"
                   title="您确定要取消发布吗？"
                   :center="true"
                   :visible.sync="cancelDialogVisible"
                   v-loading="draftSaving"
                   width="445px">
            <el-checkbox v-model="saveDraft">将已填内容保存至草稿，下次点击发布活动可恢复</el-checkbox>
            <div class="button-container">
                <div class="cancel" @click="cancelDialogVisible = false">取消</div>
                <div class="confirm" @click="onSaveDraft">确定</div>
            </div>
        </el-dialog>
        <preview-apply :visible.sync="previewDialogVisible" :activityId="this.$route.query.id+''" :apply="applyTable"></preview-apply>
    </div>
</template>

<script>
    import {getUploadPicToken} from "@/api/upload_api";
    import {checkPicSize} from "@/utils/common";
    import tinymce from "@/components/Tinymce"
    import previewApply from "@/components/activity/PreviewApply";

    export default {
        name: "ActivityEditPage",
        components: {tinymce, previewApply},
        data() {
            return {
                templateCount: {},// 我的模板数量/总数
                activityForm: { // 活动表单
                    title: undefined, // 活动名称
                    address: undefined, // 详细地址/线上链接
                    onlyOverseasStudent: '0', // 是否仅留学生能参加,0不限制,1仅海外
                    organizerType: '4', // 举办方类型; 1:校园组织;2:社会组织;3:个人;4:企业
                    cityId: undefined, // 城市id
                    activityStartTime: undefined, // 活动开始时间
                    activityFinishTime: undefined, // 活动结束时间
                    registrationStartTime: undefined, // 报名开始时间
                    registrationFinishTime: undefined, // 报名结束时间
                    auditType: '1', // 报名审核类型(是否需要审核),0需要,1不需要
                    needRegistration: '1', // 是否需要报名,0不需要,1需要
                    numberLimit: -1, // 无需审核时的人数限制，无限制为-1
                    needResume: '1', // 是否需要报名者简历信息, 0不需要,1需要
                    description: undefined, // 活动详情
                    organizerInfoForm: { // 组织信息
                        organizerName: undefined, // 组织名称
                        school: undefined, // 所属学校,若是校园组织
                        businessLicenseUrl: undefined, // 营业执照,若是社会组
                    },
                    poster: undefined, // 活动海报
                    questionnaireType: '0', // 0：报名表  1：报名模板
                    questionnaireId: undefined, // 报名表或模板id
                },

                registrationTime: [], // 报名起止时间，临时保存用户选择时间
                registrationTimeOption: {
                    disabledDate(time) {
                        let zero = new Date();
                        zero.setHours(0, 0, 0, 0);
                        return time.getTime() < zero.getTime();
                    }
                },

                activityTime: [], // 活动起止时间，临时保存用户选择时间
                activityTimeOption: {
                    disabledDate(time) {
                        let zero = new Date();
                        zero.setHours(0, 0, 0, 0);
                        return time.getTime() < zero.getTime();
                    }
                },

                cityIdProps: { // 城市选择框属性
                    multiple: false,
                    lazy: false,
                    emitPath: false,
                    checkStrictly: false,
                    expandTrigger: "hover",
                    value: "id",
                    label: "name",
                    children: "children"
                },
                cityOptions: [], // 城市列表

                registrationTemplateList: [], // 报名模板列表

                businessLicenseUploadPicOptions: { // 营业执照上传
                    loading: false,
                    action: "",
                    params: {},
                    fileUrl: "",
                    localUrl: "",
                    acceptFileType: ".jpg,.jpeg,.png,.JPG,.JPEG,.PNG",
                },

                posterUploadPicOptions: { // 活动海报上传
                    loading: false,
                    action: "",
                    params: {},
                    fileUrl: "",
                    localUrl: "",
                    acceptFileType: ".jpg,.jpeg,.png,.JPG,.JPEG,.PNG",
                },

                cancelDialogVisible: false, // 取消发布
                saveDraft: false, // 是否存草稿
                draftSaving: false, // 草稿保存中

                publishing: false, // 发布中

                useTemplate: false, // 是否使用模板
                applyTableTitle: undefined, // 报名表名称
                applyTableId: undefined, // 报名表id
                templateId: undefined, // 模板id
                applyTable: undefined, // 报名表

                previewDialogVisible: false, // 报名表预览对话框
            };
        },
        watch: {
            // 活动时间
            activityTime(value) {
                if (value && value.length === 2) {
                    this.activityForm.activityStartTime = value[0].getTime();
                    this.activityForm.activityFinishTime = value[1].getTime();
                    this.registrationTimeOption = {
                        disabledDate: (time) => {
                            return (time.getTime() < Date.now()) || (time.getTime() > value[1].getTime());
                        }
                    };
                } else {
                    this.activityForm.activityStartTime = undefined;
                    this.activityForm.activityFinishTime = undefined;
                    this.registrationTimeOption = {
                        disabledDate: (time) => {
                            let zero = new Date();
                            zero.setHours(0, 0, 0, 0);
                            return time.getTime() < zero.getTime();
                        }
                    };
                }
            },
            // 报名时间
            registrationTime(value) {
                if (value && value.length === 2) {
                    this.activityForm.registrationStartTime = value[0].getTime();
                    this.activityForm.registrationFinishTime = value[1].getTime();
                    this.activityTimeOption = {
                        disabledDate: (time) => {
                            return (time.getTime() < value[0].getTime()) || (time.getTime() < value[1].getTime());
                        }
                    };
                } else {
                    this.activityForm.registrationStartTime = undefined;
                    this.activityForm.registrationFinishTime = undefined;
                    this.activityTimeOption = {
                        disabledDate: (time) => {
                            let zero = new Date();
                            zero.setHours(0, 0, 0, 0);
                            return time.getTime() < zero.getTime();
                        }
                    };
                }
            },
            // 是否使用模板
            useTemplate(value) {
                this.activityForm.questionnaireType = value ? '1' : '0';
            }
        },

        created() {
            // 优先加载预览的内容，从列表页进入时会删除预览数据
            let activityForm = this.$storage.getObject('activityPreview');
            if (activityForm) {
                Object.keys(activityForm).forEach(key => {
                    this.activityForm[key] = activityForm[key];
                })
                if (activityForm.activityStartTime && activityForm.activityFinishTime) {
                    this.activityTime = [new Date(activityForm.activityStartTime), new Date(activityForm.activityFinishTime)];
                }
                if (activityForm.registrationStartTime && activityForm.registrationFinishTime) {
                    this.registrationTime = [new Date(activityForm.registrationStartTime), new Date(activityForm.registrationFinishTime)];
                }
                this.useTemplate = activityForm.questionnaireType === '1';
            } else if (this.$route.query.id) { // 编辑指定活动
                this.$axios.get("/activity/activity-info", {params: {id: this.$route.query.id}}).then(data => {
                    this.handlerActivity(data.data);
                })
            } else { // 没有预览数据时加载远程草稿
                this.$axios.get("/activity/my/draft-activity-info").then(data => {
                    this.handlerActivity(data.data);
                })
            }

            let applyObject = this.$storage.getObject("报名表");
            if (applyObject) {
                if (applyObject.type === '0') {
                    this.applyTableTitle = applyObject.title;
                    this.applyTableId = applyObject.id;
                    this.templateId = undefined;
                } else if (applyObject.type === '1') {
                    this.applyTableTitle = undefined;
                    this.applyTableId = undefined;
                    this.templateId = applyObject.id;
                }
                this.activityForm.questionnaireId = this.useTemplate ? this.templateId : this.applyTableId;
                this.$storage.removeObject("报名表");
                this.$storage.setObject('activityPreview', this.activityForm);
            } else if (this.activityForm.questionnaireId) { // 存在报名表id 或 报名模板  0：报名表  1：报名模板
                if (this.activityForm.questionnaireType === '0') { // 报名表
                    this.$axios.get(`/activity-questionnaire/${this.activityForm.questionnaireId}`).then(response => {
                        this.applyTableTitle = response.data.title;
                        this.applyTableId = response.data.id;
                        this.applyTable = response.data;
                    })
                } else if (this.activityForm.questionnaireType === '1') { // 报名模板
                    this.$axios.get(`/questionnaire-template/${this.activityForm.questionnaireId}`).then(response => {
                        this.templateId = response.data.id;
                    })
                }
            }

            this.initData();
        },
        methods: {
            // 初始化数据
            initData() {
                // 获取我的模板
                this.getTemplate();

                // 获取全部城市
                this.$axios.request({
                    url: "/city/list",
                    method: "get",
                }).then(data => {
                    let municipality = ["北京市", "上海市", "天津市", "重庆市"];
                    this.cityOptions = data.data.map(first => {
                        if (first.children) {
                            first.children = first.children.map(second => {
                                if (municipality.includes(second.name)) {
                                    delete second.children;
                                } else if (second.children) {
                                    second.children = second.children.map(third => {
                                        delete third.children;
                                        return third;
                                    })
                                }
                                return second;
                            })
                        }
                        return first;
                    })
                })
            },

            // 处理请求结果为本地对象
            handlerActivity(activityForm) {
                if (activityForm) {
                    Object.keys(activityForm).forEach(key => {
                        this.activityForm[key] = activityForm[key];
                    })
                    this.activityTime = [new Date(activityForm.activityStartTime), new Date(activityForm.activityFinishTime)];
                    this.registrationTime = [new Date(activityForm.registrationStartTime), new Date(activityForm.registrationFinishTime)];
                    if (this.activityForm.city) {
                        this.activityForm.cityId = this.activityForm.city.id;
                    }
                    this.activityForm.organizerInfoForm = this.activityForm.organizerInfoVo;

                    // 加载报名表
                    if (this.activityForm.questionnaireId) {
                        this.$axios.get(`/activity-questionnaire/${this.activityForm.questionnaireId}`).then(response => {
                            this.applyTable = response.data;
                            this.applyTableTitle = response.data.title;
                            this.applyTableId = response.data.id;
                        });
                    }
                }
            },

            // 点击留学生
            onOverseasStudent() {
                this.activityForm.onlyOverseasStudent = this.activityForm.onlyOverseasStudent === '0' ? '1' : '0';
            },

            // 举办方类型
            onOrganizerType(type) {
                this.activityForm.organizerType = type;
            },

            // 审核类型类型
            onAuditType(type) {
                this.activityForm.auditType = type;
            },

            // 是否需要报名
            onNeedRegistration(type) {
                if (!this.$route.query.id) {
                    this.activityForm.needRegistration = type;
                }
                if (type==='0'){
                    this.activityForm.auditType = '1';
                }
            },

            // 人数限制
            onNumberLimit(number) {
                this.activityForm.numberLimit = number;
            },

            // 是否需要报名者简历信息
            onNeedResume(type) {
                this.activityForm.needResume = type;
            },

            // 营业执照上传成功
            businessLicenseUploadSuccess() {
                this.businessLicenseUploadPicOptions.loading = false;
                this.activityForm.organizerInfoForm.businessLicenseUrl = this.businessLicenseUploadPicOptions.fileUrl;
            },

            // 营业执照上传获取上传信息
            beforeBusinessLicenseUpload(file) {
                return new Promise((resolve, reject) => {
                    if (checkPicSize(file)) {
                        reject();
                    } else {
                        this.businessLicenseUploadPicOptions.loading = true;
                        this.businessLicenseUploadPicOptions.localUrl = URL.createObjectURL(file);
                        getUploadPicToken(file.name).then((response) => {
                            const {data} = response;
                            this.businessLicenseUploadPicOptions.action = data.host;
                            this.businessLicenseUploadPicOptions.params = data;
                            this.businessLicenseUploadPicOptions.fileUrl = data.host + "/" + data.key;
                            resolve(data);
                        }).catch((error) => {
                            reject(error);
                        });
                    }
                });
            },

            // 活动海报上传成功
            posterUploadSuccess() {
                this.posterUploadPicOptions.loading = false;
                this.activityForm.poster = this.posterUploadPicOptions.fileUrl;
            },

            // 活动海报上传获取上传信息
            posterUpload(file) {
                return new Promise((resolve, reject) => {
                    if (checkPicSize(file)) {
                        reject();
                    } else {
                        this.posterUploadPicOptions.loading = true;
                        this.posterUploadPicOptions.localUrl = URL.createObjectURL(file);
                        getUploadPicToken(file.name).then((response) => {
                            const {data} = response;
                            this.posterUploadPicOptions.action = data.host;
                            this.posterUploadPicOptions.params = data;
                            this.posterUploadPicOptions.fileUrl = data.host + "/" + data.key;
                            resolve(data);
                        }).catch((error) => {
                            reject(error);
                        });
                    }
                });
            },

            // 预览报名表
            onPreviewRegistration() {
                this.previewDialogVisible = true;
            },

            // 添加报名表
            onAddRegistrationTemplate() {
                this.$storage.setObject('activityPreview', this.activityForm);
                this.$router.push({path: "/activity/apply/table", query: {type: '0'}})
            },

            // 编辑报名表
            onEditRegistrationTemplate() {
                this.$storage.setObject('activityPreview', this.activityForm);
                this.$router.push({path: "/activity/apply/table", query: {id: this.applyTableId, type: '0'}});
            },

            // 模板编辑
            onTemplateEdit(template) {
                this.$storage.setObject('activityPreview', this.activityForm);
                this.$router.push({path: "/activity/apply/table", query: {id: template.id, type: '1'}})
            },

            // 模板删除
            onTemplateDelete(template) {
                this.$axios.delete(`/questionnaire-template/${template.id}`).then(() => {
                    this.getTemplate();
                    this.$message.success("模板已删除");
                })
            },

            // 模板添加
            onTemplateAdd() {
                this.$storage.setObject('activityPreview', this.activityForm);
                this.$router.push({path: "/activity/apply/table", query: {type: '1'}})
            },

            // 点击取消
            onCancel() {
                if (this.$route.query.id) { // 编辑时，不保存草稿
                    this.$storage.removeObject('activityPreview');
                    this.$router.go(-1);
                } else { // 发布新活动取消时，提示用户保存草稿
                    this.cancelDialogVisible = true;
                }
            },

            // 保存草稿并取消
            onSaveDraft() {
                if (this.saveDraft) {
                    this.draftSaving = true;
                    this.activityForm.status = 1;
                    this.$axios.post("/activity/save", this.activityForm).then(data => {
                        this.draftSaving = false;
                        this.$storage.removeObject('activityPreview');
                        this.$router.go(-1);
                    })
                } else {
                    this.$router.go(-1);
                }
            },

            // 点击预览
            onPreview() {
                if (this.checkForm()) {
                    this.$storage.setObject("activityPreview", this.activityForm);
                    this.$router.push('/activity/preview');
                }
            },

            // 点击提交
            onSubmit() {
                if (this.checkForm()) {
                    this.activityForm.status = undefined; // 删除草稿状态
                    this.activityForm.form = this.activityForm.cityId === 3 ? 0 : 1; // 线上=0，线下=1
                    this.publishing = true;
                    if (this.$route.query.id) {
                        this.activityForm.id = this.$route.query.id;
                    }
                    this.$axios.post("/activity/save", this.activityForm).then(data => {
                        this.publishing = false;
                        this.$storage.removeObject('activityPreview');
                        this.onPublished();
                    })
                }
            },

            // 查看我已发布的活动
            onPublished() {
                this.$router.push('/activity/publish');
            },

            // 检查参数是否填写完整
            checkForm() {
                let message = undefined;
                this.activityForm.questionnaireId = this.useTemplate ? this.templateId : this.applyTableId;
                if (!this.activityForm.title) {
                    message = "请输入活动名称";
                }
                    // else if (!this.activityForm.organizerType) { // TODO 修改主办方验证
                    //     message = "请选择主办方类型";
                    //     // 举办方类型; 1:校园组织;2:社会组织;3:个人;4:企业
                    // } else if (!this.activityForm.organizerInfoForm.organizerName && !this.activityForm.organizerInfoForm.school) {
                    //     message = "请输入组织名称";
                    // } else if (this.activityForm.organizerType === '2' && !this.activityForm.organizerInfoForm.businessLicenseUrl) {
                    //     message = "请上传营业执照";
                    // } else if (!this.activityForm.organizerInfoForm.organizerName) { // TODO 修改主办方验证 END
                    //     message = "请输入组织名称";
                // }
                else if (!this.activityForm.cityId) {
                    message = "请选择活动城市";
                } else if (!this.activityForm.activityStartTime || !this.activityForm.activityFinishTime) {
                    message = "请选择活动时间";
                } else if (this.activityForm.needRegistration==='1'&&(!this.activityForm.registrationStartTime || !this.activityForm.registrationFinishTime)) {
                    message = "请选择报名时间";
                } else if (this.activityForm.needRegistration==='1'&&this.activityForm.auditType === '0' && !this.activityForm.numberLimit) {
                    message = "请输入限制报名人数";
                } else if (!this.activityForm.description) {
                    message = "请输入活动介绍";
                } else if (!this.activityForm.poster) {
                    message = "请上传活动海报";
                } else if (this.activityForm.needRegistration==='1'&&!this.activityForm.questionnaireId) {
                    message = "请添加报名表或选择报名表模板";
                }
                message && this.$message.warning(message);
                return !message;
            },

            // 获取我的模板
            getTemplate() {
                // 获取报名模板
                this.$axios.get("/questionnaire-template/my/list").then(data => {
                    this.registrationTemplateList = data.data;
                })

                // 获取我的模板数量
                this.$axios.get('/questionnaire-template/my/count').then(response => {
                    this.templateCount = response.data;
                })
            }
        }
    };
</script>

<style>
    label {
        margin: 0;
    }
</style>

<style scoped lang="scss">

    .app-container {
        width: 1200px !important;
        padding: 24px 30px 50px;
        background: #FFFFFF;
        box-shadow: 0 4px 16px 3px rgba(191, 199, 215, 0.31);
        margin: 17px auto;

        .option-container {
            display: flex;
            align-items: center;
            cursor: pointer;
            text-align: left;

            .option-text {
                font-size: 16px;
                font-weight: 400;
                color: #333333;
                line-height: 22px;
            }
        }

        .title {
            font-size: 30px;
            color: #333333;
            line-height: 42px;
            display: flex;
            align-items: flex-end;

            span {
                font-size: 18px;
                font-weight: 400;
                color: #999999;
                line-height: 32px;
                margin-left: 9px;
                text-decoration: underline;
                cursor: pointer;
            }
        }

        .content-container {
            position: relative;
            margin-top: 17px;

            .line {
                display: flex;
                align-items: center;
                margin-top: 17px;

                .name {
                    width: 175px;
                    flex-shrink: 0;
                    display: flex;
                    align-items: flex-start;
                    justify-content: flex-end;
                    font-size: 18px;
                    font-weight: 400;
                    color: #666666;
                    line-height: 32px;
                    margin-right: 11px;

                    span {
                        color: #F4511E;
                        margin-left: 5px;
                    }
                }

                .input {
                    width: 260px;
                }

                .textarea {

                    ::-webkit-input-placeholder {
                        font-size: 16px;
                        line-height: 32px;
                    }

                    :-moz-placeholder {
                        font-size: 16px;
                        line-height: 32px;
                    }

                    :-ms-input-placeholder {
                        font-size: 16px;
                        line-height: 32px;
                    }

                    ::v-deep .el-textarea__inner {
                        flex: 1;
                        height: 111px;
                        background: #FFFFFF;
                        border-radius: 6px;
                        border: 1px solid #B0BEC5;
                    }
                }

                .select {
                    width: 181px;
                }

                .tips {
                    font-size: 14px;
                    font-weight: 400;
                    color: #B0BEC5;
                    line-height: 20px;
                    margin-left: 5px;
                }

                .value-container {
                    display: flex;
                    flex-direction: column;

                    .value-line-container {
                        display: flex;
                        align-items: center;
                        height: 32px;
                    }

                    .business-license-uploader {
                        width: 77px;
                        height: 77px;

                        .business-license-add {
                            width: 77px;
                            height: 77px;
                            border-radius: 5px;
                            background: #FFFFFF;
                            border: 2px dashed #B0BEC5;
                            display: flex;
                            align-items: center;
                            justify-content: center;

                            .upload {
                                width: 28px;
                                height: 24px;
                            }
                        }
                    }
                }

                .add-button {
                    width: 122px;
                    height: 30px;
                    background: #4895EF;
                    border-radius: 15px;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    font-size: 14px;
                    font-weight: 400;
                    color: #FFFFFF;
                    line-height: 30px;
                    cursor: pointer;
                }

                .add-button-disabled {
                    background: #DDDDDD;
                    cursor: not-allowed;
                }

                .edit-button {
                    font-size: 16px;
                    color: #4895EF;
                    line-height: 22px;
                    cursor: pointer;
                }

                .edit-button-disabled {
                    color: #DDDDDD;
                    cursor: not-allowed;
                }
            }

            .poster-container {
                position: absolute;
                right: 0;
                top: 0;

                .poster-uploader {
                    width: 369px;
                    height: 208px;

                    .poster-add {
                        width: 369px;
                        height: 208px;
                        border-radius: 5px;
                        background: #FFFFFF;
                        border: 2px dashed #B0BEC5;
                        display: flex;
                        align-items: center;
                        justify-content: center;

                        .upload {
                            width: 63px;
                            height: 54px;
                        }
                    }
                }
            }
        }

        .button-container {
            display: flex;
            align-items: center;
            justify-content: center;
            margin-top: 60px;

            .cancel {
                width: 134px;
                height: 35px;
                background: #ECEFF1;
                border-radius: 18px;
                border: 1px solid #ECEFF1;
                font-size: 16px;
                color: #333333;
                line-height: 35px;
                text-align: center;
                cursor: pointer;
            }

            .preview {
                width: 134px;
                height: 35px;
                background: #FFFFFF;
                border-radius: 18px;
                border: 1px solid #4895EF;
                font-size: 16px;
                font-weight: 500;
                color: #4895EF;
                line-height: 35px;
                text-align: center;
                margin: 0 21px;
                cursor: pointer;
            }

            .submit {
                width: 133px;
                height: 35px;
                background: #4895EF;
                border-radius: 18px;
                font-size: 16px;
                font-weight: 500;
                color: #FFFFFF;
                line-height: 35px;
                text-align: center;
                cursor: pointer;
            }
        }

        .cancel-dialog {
            ::v-deep .el-dialog {
                border-radius: 13px;
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

    .template-settings-container {
        display: flex;
        flex-direction: column;

        .line1 {
            display: flex;
            align-items: flex-end;
            font-size: 18px;
            color: #333333;
            line-height: 25px;
            font-weight: bold;

            span {
                font-size: 12px;
                color: #999999;
                line-height: 17px;
                margin-left: 5px;
            }
        }

        .line2 {
            display: flex;
            align-items: center;
            justify-content: space-between;
            margin-top: 8px;
            font-size: 16px;
            color: #333333;
            line-height: 22px;

            .operate-container {
                width: 38px;
                flex-shrink: 0;
                display: flex;
                align-items: center;
                justify-content: space-between;
                margin-left: 46px;
            }
        }

        .line3 {
            display: flex;
            align-items: center;
            margin-top: 8px;
            font-size: 16px;
            color: #333333;
            line-height: 22px;
            cursor: pointer;
        }
    }
</style>
