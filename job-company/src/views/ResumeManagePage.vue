<template>
    <loading :loading="loading">
        <div class="app-container">
            <div class="menu-button-container">
                <el-menu :default-active="activeIndex" mode="horizontal" @select="handleSelectMenu">
                    <el-menu-item index="1,2">新简历</el-menu-item>
                    <el-menu-item index="3">初筛</el-menu-item>
                    <el-menu-item index="4">面试</el-menu-item>
                    <el-menu-item index="5">录用</el-menu-item>
                    <el-menu-item index="6">不合适</el-menu-item>
                    <el-menu-item index="7">简历库</el-menu-item>
                </el-menu>
                <div>
                    <el-button size="small" @click="dialogVisible3=true" type="primary">上传简历</el-button>
                    <el-button size="small" @click="dialogVisible4=true" type="primary">手动录入</el-button>
                </div>
            </div>
            <div v-if="activeIndex==='7'">
                <ResumeLibrary></ResumeLibrary>
            </div>
            <div v-else>
                <div class="mt-3">
                    <el-row :gutter="10">
                        <el-col :span="6">
                            <el-input
                                    v-model="listQuery.name"
                                    @change.native="handleFilter"
                                    class="w-100"
                                    placeholder="应聘者名称"
                                    size="small"
                            ></el-input>
                        </el-col>
                        <el-col :span="6">
                            <el-select v-model="listQuery.jobIds"
                                       placeholder="职位"
                                       multiple
                                       clearable
                                       filterable
                                       size="small"
                                       class="w-100"
                                       @change="handleFilter">
                                <el-option
                                        v-for="item in jobOptions"
                                        :key="item.id"
                                        :label="item.name"
                                        :value="item.id">
                                    <span style="float: left">{{ item.name }}</span>
                                    <span style="float: right; color: #8492a6; font-size: 13px">{{ item.city? item.city.name: '' }}</span>
                                </el-option>
                            </el-select>
                        </el-col>
                        <el-col :span="6">
                            <el-select
                                    v-model="listQuery.degreeIds"
                                    placeholder="学历"
                                    multiple
                                    clearable
                                    filterable
                                    size="small"
                                    class="w-100"
                                    @change="handleFilter">
                                <el-option
                                        v-for="item in degreeOptions"
                                        :key="item.id"
                                        :label="item.name"
                                        :value="item.id">
                                </el-option>
                            </el-select>
                        </el-col>
                    </el-row>
                </div>
                <div class="resume-list mt-3">
                    <el-card class="box-card mb-3" v-for="applyResume in pageResult.list" :key="applyResume.id">
                        <div slot="header" class="clearfix text-small" style="line-height: 40px;padding-top: 10px">
                            <el-popover
                                    v-if="applyResume.tagVos && applyResume.tagVos[0] && applyResume.tagVos[0].tagName"
                                    class="popover"
                                    placement="top-start"
                                    width="200"
                                    trigger="hover"
                                    :content="applyResume.tagVos[0].tagName">
<!--                                <el-button type="success" plain slot="reference">{{applyResume.tagVos[0].tagName}}</el-button>-->
                                <el-button type="success" plain slot="reference">{{applyResume.tagVos[0].tagName}}</el-button>
                            </el-popover>
                            <span>应聘岗位：{{applyResume.job.name}}</span>
                            <span class="ml-4">应聘时间：{{applyResume.time}}</span>
                            <div class="float-right">
                                <el-button type="primary"
                                           size="small"
                                           plain
                                           @click="handleNote(applyResume)"
                                           v-if="applyResume.applyStatus !== 5">
                                    添加笔记
                                </el-button>
                                <el-button type="primary"
                                           size="small"
                                           plain
                                           @click="handleTag(applyResume)"
                                           v-if="applyResume.applyStatus !== 5">
                                    添加标签
                                </el-button>
                                <el-button type="success"
                                           size="small"
                                           @click="handleApplyResume(3, applyResume.id)"
                                           v-if="applyResume.applyStatus === 1 || applyResume.applyStatus === 2">
                                    通过初筛
                                </el-button>
                                <el-button type="success"
                                           size="small"
                                           @click="handleApplyResume(4, applyResume.id)"
                                           v-if="applyResume.applyStatus === 3">
                                    进入面试
                                </el-button>
                                <el-button type="success"
                                           size="small"
                                           @click="handleApplyResume(5, applyResume.id)"
                                           v-if="applyResume.applyStatus === 4">
                                    录用
                                </el-button>
                                <el-button type="danger"
                                           plain
                                           size="small"
                                           @click="handleApplyResume(6, applyResume.id)"
                                           v-if="applyResume.applyStatus !== 5 && applyResume.applyStatus !== 6">
                                    不合适
                                </el-button>
                            </div>
                        </div>
                        <b-media @click="handleShowResume(applyResume)" style="cursor: pointer;">
                            <template v-slot:aside>
                                <el-badge is-dot class="item" :hidden="applyResume.applyStatus !== 1">
                                    <el-avatar :src="applyResume.resume.avatar"></el-avatar>
                                </el-badge>
                            </template>
                            <b-media-body>
                                <h6>
                                    {{applyResume.resume.name}}
                                    <span class="ml-4 text-muted text-small">
                <span
                        v-if="applyResume.resume.maxResumeEdu"
                >{{applyResume.resume.maxResumeEdu.degree.name}} /</span>
                {{applyResume.resume.age}}岁 / {{applyResume.resume.curPlace}}
              </span>
                                </h6>
                                <div class="tag-group"
                                     v-if="applyResume.resume.resumeSkillList && applyResume.resume.resumeSkillList.length !== 0">
                                    <el-tag size="small"
                                            effect="plain"
                                            type="info"
                                            class="mr-1"
                                            v-for="skill in applyResume.resume.resumeSkillList"
                                            :key="skill.id">
                                        {{skill.name}}
                                    </el-tag>
                                </div>
                                <el-row :gutter="12" class="mt-4">
                                    <el-col :span="12"
                                            v-if="applyResume.resume.resumeEduList && applyResume.resume.resumeEduList.length !== 0">
                                        <h6>教育经历</h6>
                                        <div class="text-small mt-2"
                                             v-for="edu in applyResume.resume.resumeEduList"
                                             :key="edu.id">
                                            <span>{{ edu.schoolName }} / {{ edu.majorName }}.{{edu.degree.name}}</span>
                                            <span v-if="edu.gpa">/ GPA {{edu.gpa}}</span>
                                        </div>
                                    </el-col>
                                    <el-col :span="12"
                                            v-if="applyResume.resume.resumeExpList && applyResume.resume.resumeExpList.length !== 0">
                                        <h6>工作经历</h6>
                                        <div :gutter="12"
                                             class="text-small mt-2"
                                             v-for="exp in applyResume.resume.resumeExpList"
                                             :key="exp.id">
                                            <span>{{exp.company}} / {{exp.post}} / {{exp.startTime}} - {{exp.finishTime}}</span>
                                        </div>
                                    </el-col>
                                </el-row>
                            </b-media-body>
                        </b-media>
                    </el-card>
                </div>
                <pagination
                        v-show="total"
                        :total="total"
                        :page.sync="listQuery.page"
                        :limit.sync="listQuery.limit"
                        @pagination="handleListPageRoute"/>
            </div>
            <transition name="slide-fade">
                <div class="resume-drawer" v-if="reviewDrawerVisible && activeApplyResume">
                    <div class="resume-drawer-header">
                        <el-row type="flex" class="row-bg" justify="space-between">
                            <el-col class="mt-4 ml-4">
                                <el-button type="primary"
                                           @click="handleApplyResume(3, activeApplyResume.id)"
                                           v-if="activeApplyResume.applyStatus === 1 || activeApplyResume.applyStatus === 2">
                                    通过初筛
                                </el-button>
                                <el-button type="primary"
                                           @click="handleApplyResume(4, activeApplyResume.id)"
                                           v-if="activeApplyResume.applyStatus === 3">
                                    进入面试
                                </el-button>
                                <el-button type="primary"
                                           @click="handleApplyResume(5, activeApplyResume.id)"
                                           v-if="activeApplyResume.applyStatus === 4">
                                    录用
                                </el-button>
                                <el-button type="danger"
                                           plain
                                           @click="handleApplyResume(6, activeApplyResume.id)"
                                           v-if="activeApplyResume.applyStatus !== 5 && activeApplyResume.applyStatus !== 6">
                                    不合适
                                </el-button>
                                <el-button type="primary"
                                           :loading="resumeExporting"
                                           icon="el-icon-download"
                                           @click="onDownloadResumeClick(activeApplyResume)">
                                    生成简历
                                </el-button>
                            </el-col>
                            <el-col :span="6" class="text-right">
                                <el-button type="text"
                                           class="el-icon-close text-muted close-text p-4"
                                           @click="reviewDrawerVisible=false">
                                </el-button>
                            </el-col>
                        </el-row>
                    </div>
                    <div class="resume-drawer-body pl-4 pr-4 pb-4">
                        <ResumeView :resumeId="activeApplyResume.resume.id" class="mt-3"></ResumeView>
                    </div>
                </div>
            </transition>

            <el-dialog :visible.sync="dialogVisible"
                       class="dialog-container"
                       width="800px"
                       title="笔记">
                <div style="height: 1px; margin: 5px; background: #cccccc"></div>
                <el-timeline class="dialog-content-container">
                    <el-timeline-item v-for="item in noteList" :timestamp="item.createTime" placement="top" :key="item.id">
                        <el-card>
                            <el-input class="note-text" v-if="item.edit" v-model="updateComment" size="small" type="textarea" autosize/>
                            <p class="note-text" v-else>{{item.comment}}</p>
                            <i class="el-icon-circle-check note-icon" v-if="item.edit" @click="onNoteUpdate(item)"></i>
                            <i class="el-icon-circle-close note-icon" v-if="item.edit" @click="onNoteCancel(item)"></i>
                            <i :class="['el-icon-edit','note-icon',{'note-icon-hidden':!item.edit}]" @click="onNoteEdit(item)"></i>
                            <i :class="['el-icon-close','note-icon',{'note-icon-hidden':!item.edit}]" @click="onNoteDelete(item)"></i>
                        </el-card>
                    </el-timeline-item>
                </el-timeline>
                <div class="dialog-input-container">
                    <el-input class="dialog-input" v-model="comment" placeholder="请输入您的笔记" @keyup.enter.native="onNoteSave" autofocus></el-input>
                    <el-button class="dialog-button" type="primary" :round=true @click="onNoteSave">保存
                    </el-button>
                </div>
            </el-dialog>

            <el-dialog :visible.sync="dialogVisible2"
                       class="dialog-container"
                       width="800px"
                       title="标签">
                <div style="height: 1px; margin: 5px; background: #cccccc"></div>
                <div class="dialog-input-container" style="margin-top: 30px">
                    <el-input class="dialog-input" v-model="tag" placeholder="请输入您的标签" autofocus></el-input>
                    <el-button class="dialog-button" type="primary" :round=true @click="onTagSave">保存
                    </el-button>
                </div>
            </el-dialog>
            <el-dialog :visible.sync="dialogVisible3"
                       class="dialog-container"
                       width="800px"
                       title="简历上传">
                <div style="height: 1px; margin: 5px; background: #cccccc"></div>
                <el-upload
                        class="upload-pdf"
                        drag
                        :action="uploadFileOptions.action"
                        :data="uploadFileOptions.params"
                        :accept="uploadFileOptions.acceptFileType"
                        multiple
                        :on-remove="handlerRemove"
                        :on-success="handleSuccess"
                        :before-upload="beforeFileUpload">
                    <i class="el-icon-upload"></i>
                    <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
                    <div class="el-upload__tip" slot="tip">只能上传pdf文件，且不超过2Mb</div>
                </el-upload>
            </el-dialog>
        </div>
    </loading>
</template>

<script>
    import {getCategoryTree} from "@/api/category_api";
    import Pagination from "@/components/Pagination";
    import {applyResumeList, applyResumeListOnline, applyResumeListAttachment, handleApplyResume} from "@/api/resume_api";
    import {searchSchool} from "@/api/school_api";
    import {listByType} from "@/api/dict_api";
    import {getUserJobOptions} from "@/api/job_api";
    import ResumeView from "@/components/ResumeView";
    import ResumeLibrary from "@/components/ResumeLibrary";
    import {exportResumeToPdf} from "@/api/export_api";
    import {checkPicSize, downloadFile, formatListQuery, parseListQuery} from "@/utils/common";
    import {getUploadAttachmentToken} from "@/api/upload_api";

    export default {
        name: "ResumeManagePage",
        components: {Pagination, ResumeView, ResumeLibrary},
        data() {
            return {
                activeIndex: "1,2",
                listQuery: {
                    name: undefined,
                    // jobIds: [],
                    degreeIds: [],
                    gender: undefined,
                    schoolIds: [],
                    categoryIds: [],
                    cityIds: [],
                    salaryRangeId: undefined,
                    gpaRangeId: undefined,
                    statuses: [1, 2],
                    page: 1,
                    limit: 20
                },
                keywords: undefined,
                total: 0,
                activeApplyResume: undefined,
                reviewDrawerVisible: false,
                resumeExporting: false,
                pageResult: {},
                degreeOptions: [],
                jobOptions: [],
                statusMessageMap: {
                    3: "此操作将把该简历标识为初筛，并通知应聘者，是否继续？",
                    4: "此操作将把该简历标识为面试，并通知应聘者，是否继续？",
                    5: "此操作将把该简历标识为录用，并通知应聘者，是否继续？",
                    6: "此操作将把该简历标识为不合适，并通知应聘者，是否继续？"
                },
                dialogVisible: false,
                dialogVisible2: false,
                dialogVisible3: false,
                dialogVisible4: false,
                comment: "",
                updateComment: '',
                lastEditComment: undefined,
                noteList: [],
                jobApplyItem: undefined,
                tag: '',//标签
                jobCategoryProps: {
                    multiple: true,
                    expandTrigger: "hover",
                    value: "id",
                    label: "name",
                    emitPath: false,
                    children: "children"
                },
                loading: true,
                uploadFileOptions: {
                    action: "",
                    params: {},
                    fileUrl: "",
                    acceptFileType: ".pdf"
                }
            };
        },
        watch: {
            $route: "getList",
        },
        created() {
            this.initData();
        },
        methods: {
            initData() {
                listByType(1).then(response => (this.degreeOptions = response.data.list));
                getUserJobOptions().then(response => (this.jobOptions = response.data));
                this.getList();
            },
            handleSelectMenu(index) {
                this.activeIndex = index;
                this.$nextTick(() => {
                    this.$router.push({
                        path: "/manage-resume",
                        query: {statuses: index}
                    });
                });
            },
            getList() {
                if (this.$route.query.statuses) {
                    this.activeIndex = this.$route.query.statuses;
                }
                if (this.activeIndex === '7') {
                    this.loading = false;
                    this.$emit("complete");
                } else {
                    parseListQuery(this.$route.query, this.listQuery);
                    if (this.$route.query.jobIds) {
                        // long 类型当做string处理，浏览器会丢失精度
                        this.listQuery.jobIds = this.$route.query.jobIds.split(',');
                    }
                    applyResumeList(this.listQuery).then(response => {
                        this.pageResult = response.data;
                        this.total = this.pageResult.total;
                        this.loading = false;
                        this.$emit("complete");
                    });
                }
            },
            handleListPageRoute() {
                this.$router.push({
                    path: this.$route.path,
                    query: formatListQuery(this.listQuery)
                });
            },
            handleFilter() {
                this.listQuery.page = 1;
                this.handleListPageRoute();
            },

            handleNote(item) {
                this.dialogVisible = true;
                this.jobApplyItem = item;
                this.getNoteList(item.id);
            },

            handleTag(item) {
                this.dialogVisible2 = true;
                this.jobApplyItem = item;
                this.tag = item.tagVos && item.tagVos[0].tagName;
            },

            onNoteSave() {
                this.$axios.request({
                    url: '/usercorporate/comment/save',
                    method: "post",
                    data: {
                        jobApplyId: this.jobApplyItem.id,
                        comment: this.comment
                    }
                }).then(() => {
                    this.comment = '';
                    this.getNoteList(this.jobApplyItem.id);
                })
            },
            onNoteDelete(item) {
                this.$confirm("确认删除吗？", "提示", {
                    confirmButtonText: "继续",
                    cancelButtonText: "取消",
                    type: "warning"
                }).then(() => {
                    this.$axios.request({
                        url: '/usercorporate/comment/delete',
                        method: 'post',
                        params: {userCorporateCommentId: item.id}
                    }).then(() => {
                        this.getNoteList(this.jobApplyItem.id);
                    })
                });
            },

            onNoteEdit(item) {
                if (this.lastEditComment) {
                    this.onNoteCancel(this.lastEditComment);
                }
                this.lastEditComment = item;
                this.updateComment = item.comment;
                item.edit = true;

            },

            onNoteCancel(item) {
                item.edit = false;
                this.updateComment = '';
            },

            onNoteUpdate(item) {
                item.comment = this.updateComment;
                this.$axios.request({
                    url: '/usercorporate/comment/save',
                    method: "post",
                    data: {
                        jobApplyId: this.jobApplyItem.id,
                        comment: item.comment,
                        id: item.id
                    }
                }).then(() => {
                    this.comment = '';
                    this.getNoteList(this.jobApplyItem.id);
                })
            },

            getNoteList(jobApplyId) {
                this.$axios.get('/usercorporate/comment/list', {
                    params: {jobApplyId}
                }).then(data => {
                    this.noteList = data.data.map(item => {
                        item.edit = false;
                        return item;
                    })
                })
            },
            onTagSave() {
                let tag = this.jobApplyItem.tagVos && this.jobApplyItem.tagVos[0];
                if (tag) {
                    tag.tagName = this.tag;
                } else {
                    this.jobApplyItem.tagVos = [];
                    this.jobApplyItem.tagVos.push(this.tag);
                }
                this.$axios.post('/usercorporate/tag/save', {
                    jobApplyId: this.jobApplyItem.id,
                    tagName: this.tag,
                    id: tag && tag.id
                }).then(() => {
                    this.comment = ''
                    this.dialogVisible2 = false;
                })
            },

            handleApplyResume(status, id) {
                const message = this.statusMessageMap[status];
                this.$confirm(message, "提示", {
                    confirmButtonText: "继续",
                    cancelButtonText: "取消",
                    type: "warning"
                }).then(() => {
                    handleApplyResume({id: id, status: status}).then(() => {
                        this.$message("操作成功");
                        this.reviewDrawerVisible = false;
                        this.getList();
                    });
                });
            },
            handleShowResume(applyResume) {
                this.reviewDrawerVisible = true;
                this.activeApplyResume = applyResume;
                if (applyResume.applyStatus === 1) {
                    handleApplyResume({id: applyResume.id, status: 2}).then(() => {
                        applyResume.applyStatus = 2;
                    });
                }
            },
            onDownloadResumeClick(applyResume) {
                this.resumeExporting = true;
                let fileName = `${applyResume.resume.name}_${applyResume.job.name}`;
                if (applyResume.resume.maxResumeEdu) {
                    fileName += `_${applyResume.resume.maxResumeEdu.schoolName}_${applyResume.resume.maxResumeEdu.majorName}`;
                }
                exportResumeToPdf(applyResume.resume.id).then(response => {
                    downloadFile({
                        fileKey: response.data,
                        fileName: `${fileName}.pdf`,
                        success: () => {
                            this.resumeExporting = false;
                        }
                    });
                }).catch(() => {
                    this.resumeExporting = false;
                });
            },

            // 文件上传
            beforeFileUpload(file) {
                return new Promise((resolve, reject) => {
                    if (checkPicSize(file)) {
                        reject();
                    }
                    getUploadAttachmentToken(file.name)
                        .then(response => {
                            const {data} = response;
                            this.uploadFileOptions.action = data.host;
                            this.uploadFileOptions.params = data;
                            this.uploadFileOptions.fileUrl = data.host + "/" + data.key;
                            file.url = this.uploadFileOptions.fileUrl;
                            resolve(data);
                        })
                        .catch(error => {
                            reject(error);
                        });
                });
            },
            handleSuccess(response, file, fileList) {
                console.log(file.name + "  " + file.raw.url);
                console.log(fileList);
                this.$axios.request({
                    url: "/resume-repository/save-attachment",
                    method: "post",
                    params: {"attachmentName": file.raw.url},
                }).then(data => {
                    console.log(data);
                })
            },

            handlerRemove(file) {
                console.log(file);
            }
        }
    };
</script>

<style lang="scss" scoped>

    .app-container {
        margin: 0 auto;
        width: 1000px;

        .menu-button-container {
            display: flex;
            align-items: center;
            justify-content: space-between;
        }
    }

    .text-small {
        font-size: 14px;
    }

    .close-text {
        font-size: 30px;
    }

    .resume-drawer {
        right: 0;
        height: 100%;
        width: 50%;
        top: 0;
        bottom: 0;
        z-index: 100;
        position: fixed;
        box-sizing: border-box;
        background-color: rgb(255, 255, 255);
        display: flex;
        flex-direction: column;
        box-shadow: rgba(0, 0, 0, 0.2) 0 8px 10px -5px,
        rgba(0, 0, 0, 0.14) 0px 16px 24px 2px, rgba(0, 0, 0, 0.12) 0px 6px 30px 5px;
    }

    .resume-drawer-header {
        position: absolute;
        width: 100%;
        z-index: 1;
        background: #fff;
        height: 80px;
        border-bottom: 1px solid #eee;
    }

    .resume-drawer-body {
        position: absolute;
        top: 0;
        bottom: 0;
        width: 100%;
        padding-top: 80px;
        overflow: auto;
    }

    /deep/ .el-card__header {
        position: relative;

        .popover {
            position: absolute;
            top: 0;
            left: 0;
            border: none;

            /deep/ .el-button {
                padding: 8px;
                width: 87px;
                overflow: hidden;
                text-overflow: ellipsis;
                white-space: nowrap;
            }
        }
    }


    .dialog-container {
        text-align: center;

        /deep/ .el-dialog__body {
            padding: 0 20px 30px;
        }

    }

    .dialog-content-container {
        height: 600px;
        overflow-y: auto;
        text-align: left;
        padding: 50px 50px 20px;

        .note-item-container {

        }

        /deep/ .el-card__body {
            display: flex;
            align-items: flex-start;

            .note-text {
                font-size: 16px;
                line-height: 32px;
                flex: 1;
                max-width: calc(100% - 50px);
            }

            .note-icon {
                font-size: 16px;
                /*margin-bottom: 16px;*/
                line-height: 24px;
                padding: 4px;
            }

            .note-icon-hidden {
                display: none;
            }

            &:hover .note-icon {
                display: inline-block;
                cursor: pointer;
            }
        }
    }

    .dialog-input-container {
        display: flex;
        align-items: center;
        margin-bottom: 16px;

        .dialog-input {
            flex: 1;
            margin-right: 30px;

            /deep/ input {
                border-radius: 20px;
            }
        }

        .dialog-button {
            width: 120px;
            /*text-align: center;*/
            /*height: 60px;*/
            /*border-radius: 30px;*/
        }
    }

    .upload-pdf {
        margin: 50px auto;
        max-width: 360px;

        /deep/ .el-upload-list {
            height: 200px;
            overflow: auto;
        }
    }

    .slide-fade-enter-active {
        transition: all .5s ease;
    }

    .slide-fade-leave-active {
        transition: all .5s ease;
    }

    .slide-fade-enter, .slide-fade-leave-to
        /* .slide-fade-leave-active for below version 2.1.8 */
    {
        transform: translateX(50%);
        opacity: 0;
    }
</style>
