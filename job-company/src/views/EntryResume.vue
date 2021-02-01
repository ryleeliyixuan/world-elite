<template>
    <div class="app-container">
        <b-container>
            <h4 class="mb-4">{{$t('complete_resume')}}</h4>
            <b-row>
                <b-media class="resume col-lg-8 col-md-8 col-sm-12 col-xs-12">
                    <div class="resume-body">
                        <div class="resume-box" id="Resume-Basic">
                            <EditResumeTitle
                                    title="基本信息"
                                    :icon="['fa', 'pencil-alt']"
                                    iconText="编辑"
                                    v-on:edit-click="handleEditResumeBasic"
                            />
                            <div class="resume-basicinfo row mt-3">
                                <div class="avatorHolder col-lg-4 col-md-12 col-sm-12 col-xs-12">
                                    <el-upload class="avatar-uploader mb-3"
                                               :action="uploadPicOptions.action"
                                               :data="uploadPicOptions.params"
                                               :accept="uploadPicOptions.acceptFileType"
                                               :show-file-list="false"
                                               :on-success="handleAvatarSuccess"
                                               :before-upload="beforeAvatarUpload"
                                               v-loading="avatarLoading">
                                        <el-image v-if="localAvatarURL"
                                                  :src="localAvatarURL"
                                                  fit="cover"
                                                  class="avatar"></el-image>
                                        <el-image v-else-if="resume.avatar && resume.avatar !== ''"
                                                  :src="resume.avatar"
                                                  fit="cover"
                                                  class="avatar"></el-image>
                                        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                                    </el-upload>
                                    <div class="el-upload__tip">建议大小：500 x 500</div>
                                </div>

                                <div class="resume-info col-lg-8 col-md-12 col-sm-12 col-xs-12">
                                    <h4 v-show="resume.name">
                                        {{resume.name}}
                                        <font-awesome-icon
                                                :icon="['fa', 'male']"
                                                class="text-primary gender-text"
                                                v-if="resume.gender === 1"
                                        />
                                        <font-awesome-icon
                                                :icon="['fa', 'female']"
                                                class="text-danger gender-text"
                                                v-else
                                        />
                                    </h4>
                                    <p>
                                        <span v-if="resume.graduateTime">{{resume.graduateTime}}毕业 /</span>
                                        <span v-if="resume.curPlace">现居{{resume.curPlace}} /</span>
                                        <span v-if="resume.returnTime">{{resume.returnTime}}回国 /</span>
                                        <span v-if="resume.age">{{resume.age}}岁</span>
                                    </p>
                                    <p>
                                        <span class="editemail mr-4" v-show="resume.email">
                                          <font-awesome-icon :icon="['fa', 'envelope']"/>
                                          {{resume.email}}
                                        </span>
                                        <span class="phoneNumber" v-if="resume.phone && resume.phone != 0">
                                          <font-awesome-icon :icon="['fa', 'mobile-alt']"/>
                                          +{{resume.phoneCode}} {{resume.phone}}
                                        </span>
                                    </p>
                                </div>
                            </div>
                        </div>
                        <div class="resume-box" id="Resume-Introduction">
                            <EditResumeTitle
                                    title="自我介绍"
                                    :icon="['fa', 'pencil-alt']"
                                    iconText="编辑"
                                    v-on:edit-click="handleEditResumeIntro"
                            />
                            <div class="mt-3 description-text" v-html="resume.introduction"></div>
                        </div>


                        <div class="resume-box" id="Resume-Education">
                            <EditResumeTitle
                                    title="教育经历"
                                    :icon="['fa', 'plus-square']"
                                    iconText="添加"
                                    v-on:edit-click="handleEditResumeEdu('create')"
                            />
                            <div class="mt-3">
                                <div
                                        class="edit-box pt-2"
                                        v-for="resumeEdu in resume.resumeEduList"
                                        :key="resumeEdu.id"
                                        v-on:mouseover="resumeEdu.showEditFlag = 1"
                                        v-on:mouseout="resumeEdu.showEditFlag = 0"
                                >
                                    <b-media>
                                        <h6>
                                            {{resumeEdu.schoolName}}
                                            <span
                                                    class="ml-3 time-text"
                                            >{{resumeEdu.startTime}}到{{resumeEdu.finishTime}}</span>
                                        </h6>
                                        <p
                                                v-if="resumeEdu.gpa > 0 && resumeEdu.gpa!==''"
                                        >{{resumeEdu.degree.name}} / {{resumeEdu.majorName}} / GPA {{resumeEdu.gpa}}</p>
                                        <p v-else>{{resumeEdu.degree.name}} / {{resumeEdu.majorName}}</p>
                                        <div class="edit-item-box" v-show="resumeEdu.showEditFlag == 1">
                                            <a
                                                    class="p-2 edit-text"
                                                    v-on:click="handleEditResumeEdu('update', resumeEdu)"
                                            >
                                                <font-awesome-icon :icon="['fa', 'pencil-alt']"/>
                                            </a>
                                            <a class="p-2 edit-text" v-on:click="handleDelResumeEdu(resumeEdu.id)">
                                                <font-awesome-icon :icon="['fa', 'trash-alt']"/>
                                            </a>
                                        </div>
                                    </b-media>
                                </div>
                            </div>
                        </div>
                        <div class="resume-box" id="Resume-Experience">
                            <EditResumeTitle
                                    title="工作经验"
                                    :icon="['fa', 'plus-square']"
                                    iconText="添加"
                                    v-on:edit-click="handleEditResumeExp('create')"
                            />
                            <div class="mt-3">
                                <div
                                        class="edit-box pt-2 pb-4"
                                        v-for="resumeExp in resume.resumeExpList"
                                        :key="resumeExp.id"
                                        v-on:mouseover="resumeExp.showEditFlag = 1"
                                        v-on:mouseout="resumeExp.showEditFlag = 0"
                                >
                                    <b-media>
                                        <h6>
                                            {{resumeExp.company}}
                                            <span
                                                    class="ml-3 position-text"
                                            >{{resumeExp.depart}} . {{resumeExp.post}}</span>
                                            <span
                                                    class="ml-3 time-text"
                                            >{{resumeExp.onWork == 1? '在职': `${resumeExp.startTime}到${resumeExp.finishTime}`}}</span>
                                        </h6>
                                        <div class="description-text mt-3" v-html="resumeExp.description"></div>
                                        <div class="edit-item-box" v-show="resumeExp.showEditFlag == 1">
                                            <a
                                                    class="p-2 edit-text"
                                                    v-on:click="handleEditResumeExp('update', resumeExp)"
                                            >
                                                <font-awesome-icon :icon="['fa', 'pencil-alt']"/>
                                            </a>
                                            <a class="p-2 edit-text" v-on:click="handleDelResumeExp(resumeExp.id)">
                                                <font-awesome-icon :icon="['fa', 'trash-alt']"/>
                                            </a>
                                        </div>
                                    </b-media>
                                </div>
                            </div>
                        </div>
                        <div class="resume-box" id="Resume-Practice">
                            <EditResumeTitle
                                    title="实践经验"
                                    :icon="['fa', 'plus-square']"
                                    iconText="添加"
                                    v-on:edit-click="handleEditResumePractice('create')"
                            />
                            <div class="mt-3">
                                <div
                                        class="edit-box pt-2 pb-4"
                                        v-for="practice in resume.resumePracticeList"
                                        :key="practice.id"
                                        v-on:mouseover="practice.showEditFlag = 1"
                                        v-on:mouseout="practice.showEditFlag = 0"
                                >
                                    <b-media>
                                        <h6>
                                            {{practice.title}}
                                            <span
                                                    class="ml-3 position-text"
                                                    v-if="practice.post"
                                            >{{practice.post}}</span>
                                            <span
                                                    class="ml-3 time-text"
                                            >{{practice.onWork == 1? '实践中': `${practice.startTime}到${practice.finishTime}`}}</span>
                                        </h6>
                                        <div class="description-text mt-3" v-html="practice.description"></div>
                                        <div class="edit-item-box" v-show="practice.showEditFlag == 1">
                                            <a
                                                    class="p-2 edit-text"
                                                    v-on:click="handleEditResumePractice('update', practice)"
                                            >
                                                <font-awesome-icon :icon="['fa', 'pencil-alt']"/>
                                            </a>
                                            <a class="p-2 edit-text" v-on:click="handleDelResumePractice(practice.id)">
                                                <font-awesome-icon :icon="['fa', 'trash-alt']"/>
                                            </a>
                                        </div>
                                    </b-media>
                                </div>
                            </div>
                        </div>
                        <div class="resume-box" id="Resume-Skill">
                            <EditResumeTitle
                                    title="能力标签"
                                    :icon="['fa', 'pencil-alt']"
                                    iconText="编辑"
                                    v-on:edit-click="handleEditResumeSkill"
                            />
                            <div class="mt-3">
                                <el-tag
                                        v-for="skill in resume.resumeSkillList"
                                        :key="skill.id"
                                        effect="plain"
                                        class="mr-2"
                                >{{ skill.name }}
                                </el-tag>
                            </div>
                        </div>
                        <div class="resume-box" id="Resume-Link">
                            <EditResumeTitle
                                    title="社交主页"
                                    :icon="['fa', 'plus-square']"
                                    iconText="添加"
                                    v-on:edit-click="handleEditResumeLink"
                            />
                            <div class="mt-3">
                                <div
                                        class="link-item edit-box"
                                        v-for="resumeLink in resume.resumeLinkList"
                                        :key="resumeLink.id"
                                        v-on:mouseover="resumeLink.showEditFlag = 1"
                                        v-on:mouseout="resumeLink.showEditFlag = 0"
                                >
                                    <b>{{resumeLink.name}}：</b>
                                    <b-link :href="resumeLink.link" target="_blank">{{resumeLink.link}}</b-link>
                                    <div class="edit-item-box" v-show="resumeLink.showEditFlag == 1">
                                        <a
                                                class="p-2 edit-text"
                                                v-on:click="handleEditResumeLink('update', resumeLink)"
                                        >
                                            <font-awesome-icon :icon="['fa', 'pencil-alt']"/>
                                        </a>
                                        <a class="p-2 edit-text" v-on:click="handleDelResumeLink(resumeLink.id)">
                                            <font-awesome-icon :icon="['fa', 'trash-alt']"/>
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </b-media>
                <b-col class="resume-sidebar col-lg-4 col-md-4 col-sm-12 col-xs-12 mt-4">
                    <div class="resume-objective resume-right-box">
                        <EditResumeTitle
                                title="求职意向"
                                :icon="['fa', 'pencil-alt']"
                                iconText="编辑"
                                v-on:edit-click="handleEditExpectJob"
                        />
                        <div class="mt-3" v-if="resume.userExpectJob">
                            <p>
                                <el-tag
                                        v-for="city in  resume.userExpectJob.cityList"
                                        :key="city.id"
                                        effect="plain"
                                        class="mr-2"
                                        size="small"
                                >{{ city.name }}
                                </el-tag>
                            </p>
                            <p>
                                <el-tag
                                        v-for="category in  resume.userExpectJob.categoryList"
                                        :key="category.id"
                                        effect="plain"
                                        class="mr-2"
                                        size="small"
                                >{{ category.name }}
                                </el-tag>
                            </p>
                            <p v-if="resume.userExpectJob.salary">
                                <el-tag
                                        effect="plain"
                                        class="mr-2"
                                        size="small"
                                >{{resume.userExpectJob.salary.name}}</el-tag>
                            </p>
                        </div>
                    </div>
                    <div class="resume-attachment resume-right-box">
                        <EditResumeTitle title="附件简历"/>
                        <el-upload
                                class="mt-2 upload-attach-box"
                                :limit="1"
                                :action="uploadAttachmentOptions.action"
                                :data="uploadAttachmentOptions.params"
                                :accept="uploadAttachmentOptions.acceptFileType"
                                :show-file-list="false"
                                :on-success="handleUploadAttachmengSuccess"
                                :before-upload="beforeAttachmengUpload"
                        >
                            <el-button size="small" type="primary" icon="el-icon-upload">点击上传</el-button>
                            <div slot="tip" class="el-upload__tip">大小不超过5Mb</div>
                        </el-upload>
                        <div v-if="resume.attachResume">
                            <el-link :href="resume.attachResume" type="primary" icon="el-icon-link">下载附件简历</el-link>
                            <el-link type="danger" class="ml-4" @click="onDelResumeAttachClick">
                                <i class="el-icon-delete"></i> 删除
                            </el-link>
                        </div>
                    </div>
                    <div class="resume-preview resume-right-box">
                        <p>简历完成度</p>
                        <el-progress
                                :text-inside="true"
                                :stroke-width="22"
                                :percentage="resume.resumeCompleteProgress"
                                :status="resume.resumeCompleteProgress == 100? 'success': 'warning'"
                        ></el-progress>
                        <el-button
                                type="primary"
                                class="mt-2 btn-preview"
                                @click="handlePreview"
                                icon="el-icon-document"
                        >预览简历
                        </el-button>
                    </div>
                    <!-- v-sticky="{stickyTop: 2"}" -->
                    <div class="resume-nav-container">
                        <b-navbar class="fixed-bottom" toggleable="sm" type="bright" variant="light">
                            <b-collapse id="navbar-toggle-collapse" is-nav>
                                <b-navbar-nav class="navbar">
                                    <b-nav-item @click="goAnchor('#Resume-Basic')">基本信息</b-nav-item>
                                    <b-nav-item @click="goAnchor('#Resume-Introduction')">自我介绍</b-nav-item>
                                    <b-nav-item @click="goAnchor('#Resume-Education')">教育经历</b-nav-item>
                                    <b-nav-item @click="goAnchor('#Resume-Experience')">工作经验</b-nav-item>
                                    <b-nav-item @click="goAnchor('#Resume-Practice')">实践经验</b-nav-item>
                                    <b-nav-item @click="goAnchor('#Resume-Skill')">能力标签</b-nav-item>
                                    <b-nav-item @click="goAnchor('#Resume-Link')">社交链接</b-nav-item>
                                </b-navbar-nav>
                            </b-collapse>

                            <b-navbar-brand href="#">简历导航</b-navbar-brand>
                            <b-navbar-toggle target="navbar-toggle-collapse">
                                <template v-slot:default="{ expanded }">
                                    <font-awesome-icon :icon="['fa', 'chevron-down']" v-if="expanded"/>
                                    <font-awesome-icon :icon="['fa', 'chevron-up']" v-else/>
                                </template>
                            </b-navbar-toggle>
                        </b-navbar>
                    </div>
                </b-col>
            </b-row>
        </b-container>
        <!--Basic Info-->
        <el-dialog title="编辑基本信息" :visible.sync="showBasicDialog" width="80%" top="9vh">
            <el-form ref="resumeForm" :model="resumeForm" :rules="resumeFormRules" label-width="80px">
                <el-form-item label="姓名" prop="name">
                    <el-input
                            v-model="resumeForm.name"
                            class="m-input-text-width"
                            placeholder="你的真实姓名"
                            maxlength="20"
                            show-word-limit
                    ></el-input>
                </el-form-item>
                <el-form-item label="生日" prop="birth">
                    <el-date-picker
                            v-model="resumeForm.birth"
                            :picker-options="oldDatePickerOptions"
                            type="date"
                            value-format="yyyy-MM-dd"
                            placeholder="选择日期"
                    ></el-date-picker>
                </el-form-item>
                <el-form-item label="性别" prop="gender">
                    <el-radio-group v-model="resumeForm.gender">
                        <el-radio :label="1" border>男</el-radio>
                        <el-radio :label="2" border>女</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="所在城市" prop="curPlace">
                    <el-input
                            v-model="resumeForm.curPlace"
                            class="m-input-text-width"
                            placeholder="比如：洛杉矶"
                            maxlength="50"
                            show-word-limit
                    ></el-input>
                </el-form-item>
                <el-form-item label="邮箱" prop="email">
                    <el-input
                            v-model="resumeForm.email"
                            class="m-input-text-width"
                            placeholder="比如：****@163.com"
                            show-word-limit
                    ></el-input>
                </el-form-item>
                <el-form-item label="毕业时间" prop="graduateTime">
                    <el-date-picker
                            v-model="resumeForm.graduateTime"
                            type="month"
                            format="yyyy-MM"
                            value-format="yyyy-MM"
                            placeholder="选择日期"
                    ></el-date-picker>
                </el-form-item>
                <el-form-item label="回国时间" prop="returnTime">
                    <el-date-picker
                            v-model="resumeForm.returnTime"
                            type="month"
                            placeholder="选择日期"
                            format="yyyy-MM"
                            value-format="yyyy-MM"
                    ></el-date-picker>
                </el-form-item>
                <el-form-item label="联系电话" prop="phone">
                    <el-input
                            placeholder="请输入电话号码"
                            type="number"
                            v-model="resumeForm.phone"
                            maxlength="20"
                            show-word-limit
                    >
                        <el-select
                                v-model="resumeForm.phoneCode"
                                slot="prepend"
                                placeholder="电话区号"
                                clearable
                                filterable
                        >
                            <el-option
                                    :value="country.phoneCode"
                                    :label="country.chineseName + '+' + country.phoneCode"
                                    v-for="country in countryOptions"
                                    :key="country.id"
                            >
                                <span style="float: left">{{ country.chineseName }}</span>
                                <span style="float: right; color: #8492a6; font-size: 13px; width: 300px;">+{{ country.phoneCode }}</span>
                            </el-option>
                        </el-select>
                    </el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
        <el-button @click="showBasicDialog = false">取 消</el-button>
        <el-button type="primary" @click="handleSaveResumeBasic(true)" :loading="posting">保 存</el-button>
      </span>
        </el-dialog>
        <!--编辑自我介绍-->
        <el-dialog title="编辑自我介绍" :visible.sync="showIntroDialog" width="80%" top="9vh">
            <quill-editor v-model="resumeForm.introduction" :options="introEditorOption"></quill-editor>
            <span slot="footer" class="dialog-footer">
        <el-button @click="showIntroDialog = false">取 消</el-button>
        <el-button type="primary" @click="handleSaveResumeBasic(false)" :loading="posting">保 存</el-button>
      </span>
        </el-dialog>
        <!--编辑教育经历-->
        <el-dialog title="添加教育经历" :visible.sync="showEduDialog" width="80%" top="9vh">
            <el-form
                    ref="resumeEduForm"
                    :model="resumeEduForm"
                    :rules="resumeEduFormRules"
                    label-width="80px"
            >
                <el-form-item label="学校名称" prop="schoolName">
                    <el-autocomplete
                            v-model="resumeEduForm.schoolName"
                            :fetch-suggestions="searchSchoolName"
                            :trigger-on-focus="false"
                            value-key="name"
                            placeholder="请输入学校名称"
                            class="w-100"
                    />
                </el-form-item>
                <el-form-item label="在校时间" prop="workingDates">
                    <el-date-picker
                            type="monthrange"
                            start-placeholder="入校时间"
                            end-placeholder="毕业时间"
                            value-format="yyyy-MM"
                            v-model="resumeEduForm.workingDates"
                            class="w-100"
                    />
                </el-form-item>
                <el-form-item label="学历" prop="degreeId">
                    <el-select v-model="resumeEduForm.degreeId" placeholder="请选择学历">
                        <el-option
                                :label="dict.name"
                                :value="dict.id"
                                v-for="dict in degreeOptions"
                                :key="dict.id"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item label="专业" prop="majorName">
                    <el-input v-model="resumeEduForm.majorName" placeholder="请输入所学专业"></el-input>
                </el-form-item>
                <el-form-item label="GPA" prop="gpa">
                    <el-input v-model="resumeEduForm.gpa" type="number" placeholder="请输入GPA" min="0"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
        <el-button @click="showEduDialog = false">取 消</el-button>
        <el-button type="primary" @click="handleSaveResumeEdu" :loading="posting">保 存</el-button>
      </span>
        </el-dialog>
        <!--编辑工作经历-->
        <el-dialog title="添加工作经验" :visible.sync="showExpDialog" width="80%" top="9vh">
            <el-form
                    ref="resumeExpForm"
                    :model="resumeExpForm"
                    :rules="resumeExpFormRules"
                    label-width="80px"
            >
                <el-form-item label="公司名称" prop="company">
                    <el-autocomplete
                            v-model="resumeExpForm.company"
                            :fetch-suggestions="searchCompanyName"
                            :trigger-on-focus="false"
                            value-key="fullName"
                            placeholder="请输入公司名称"
                            class="w-100"
                            :maxlength="100"
                            show-word-limit
                    />
                </el-form-item>
                <el-form-item label="所属部门" prop="depart">
                    <el-input
                            v-model="resumeExpForm.depart"
                            placeholder="请填写部门信息"
                            :maxlength="40"
                            show-word-limit
                    ></el-input>
                </el-form-item>
                <el-form-item label="职位名称" prop="post">
                    <el-input
                            v-model="resumeExpForm.post"
                            placeholder="请填写职位名称"
                            :maxlength="40"
                            show-word-limit
                    ></el-input>
                </el-form-item>
                <el-form-item label="在职时间" prop="workingTimeFlag">
                    <el-date-picker
                            v-model="resumeExpForm.workingDates"
                            type="monthrange"
                            value-format="yyyy-MM"
                            start-placeholder="入职时间"
                            end-placeholder="离职时间"
                            :disabled="resumeExpForm.onWork == 1"
                    ></el-date-picker>
                    <el-checkbox
                            class="ml-2"
                            v-model="resumeExpForm.onWork"
                            label="在职"
                            border
                            :true-label="1"
                            :false-label="0"
                    ></el-checkbox>
                </el-form-item>
                <el-form-item label="工作内容" prop="description">
                    <quill-editor v-model="resumeExpForm.description" :options="expEditorOption"></quill-editor>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="showExpDialog = false">取 消</el-button>
                <el-button type="primary" @click="handleSaveResumeExp" :loading="posting">保 存</el-button>
            </div>
        </el-dialog>
        <!--编辑实践经验-->
        <el-dialog title="添加实践经验" :visible.sync="showPracticeDialog" width="80%" top="9vh">
            <el-form
                    ref="resumePracticeForm"
                    :model="resumePracticeForm"
                    :rules="resumePracticeFormRules"
                    label-width="80px"
            >
                <el-form-item label="实践主题" prop="title">
                    <el-input
                            v-model="resumePracticeForm.title"
                            placeholder="请填写实践主题"
                            :maxlength="50"
                            show-word-limit
                    ></el-input>
                </el-form-item>
                <el-form-item label="担任职位" prop="post">
                    <el-input
                            v-model="resumePracticeForm.post"
                            placeholder="请填写担任职位"
                            :maxlength="40"
                            show-word-limit
                    ></el-input>
                </el-form-item>
                <el-form-item label="实践时间" prop="workingTimeFlag">
                    <div class="experience-date-picker">
                        <el-date-picker
                                v-model="resumePracticeForm.workingDates"
                                align="center"
                                type="monthrange"
                                value-format="yyyy-MM"
                                start-placeholder="开始时间"
                                end-placeholder="结束时间"
                                :disabled="resumePracticeForm.onWork == 1"
                        ></el-date-picker>
                    </div>
                    <el-checkbox
                            class="ml-2"
                            v-model="resumePracticeForm.onWork"
                            label="实践中"
                            border
                            :true-label="1"
                            :false-label="0"
                    ></el-checkbox>
                </el-form-item>
                <el-form-item label="实践内容" prop="description">
                    <quill-editor v-model="resumePracticeForm.description" :options="practiceEditorOption"></quill-editor>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="showPracticeDialog = false">取 消</el-button>
                <el-button type="primary" @click="handleSaveResumePractice" :loading="posting">保 存</el-button>
            </div>
        </el-dialog>
        <!--编辑求职意向-->
        <el-dialog title="编辑求职意向" :visible.sync="showExpectJobDialog" width="80%" top="9vh">
            <el-form ref="expectJobForm" :model="expectJobForm" label-width="80px">
                <el-form-item label="意向城市" prop="cityIds">
                    <el-cascader placeholder="最多选择三个"
                                 :show-all-levels="false"
                                 :options="cityOptions"
                                 :props="cityIdProps"
                                 filterable
                                 clearable
                                 class="w-100"
                                 v-model="expectJobForm.cityIds">
                    </el-cascader>
                </el-form-item>
                <el-form-item label="意向职位" prop="categoryIds">
                    <el-cascader
                            placeholder="最多选择三个"
                            :show-all-levels="false"
                            :options="jobCategoryOptions"
                            :props="jobCatetoryProps"
                            filterable
                            clearable
                            v-model="expectJobForm.categoryIds"
                            class="w-100"
                    ></el-cascader>
                </el-form-item>
                <el-form-item label="薪资范围">
                    <el-select v-model="expectJobForm.salaryId" placeholder="薪资范围" class="w-100">
                        <el-option v-for="item in salaryOptions"
                                   :key="item.id"
                                   :label="item.name"
                                   :value="item.id"
                        ></el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="showExpectJobDialog = false">取 消</el-button>
                <el-button type="primary" @click="handleSaveExpectJob" :loading="posting">保 存</el-button>
            </div>
        </el-dialog>
        <!--编辑技能标签-->
        <el-dialog title="编辑技能标签" :visible.sync="showSkillDialog" width="80%" top="9vh">
            <p>请选择你的能力标签（最多5个），支持自定义标签。</p>
            <el-row :gutter="10">
                <el-col :span="16">
                    <el-input v-model="newSkillTag" placeholder="请输入能力标签" maxlength="6" show-word-limit></el-input>
                </el-col>
                <el-col :span="4">
                    <el-button type="primary" icon="el-icon-plus" v-on:click="handleAddNewSkillTag">添加</el-button>
                </el-col>
            </el-row>
            <div class="mt-4">
                <el-tag
                        class="skill-tag"
                        v-for="tag in this.skillTagListForm"
                        :key="tag.name"
                        :effect="tag.select? 'dark': 'plain'"
                        v-on:click="selectSkillTag(tag)"
                >{{tag.name}}
                </el-tag>
            </div>
            <div slot="footer" class="dialog-footer">
                <el-button @click="showSkillDialog = false">取 消</el-button>
                <el-button type="primary" @click="handleSaveResumeSkills" :loading="posting">保 存</el-button>
            </div>
        </el-dialog>
        <!--编辑社交链接-->
        <el-dialog title="添加社交链接" :visible.sync="showLinkDialog" width="80%" top="9vh">
            <el-form
                    ref="resumeLinkForm"
                    :model="resumeLinkForm"
                    :rules="resumeLinkFormRules"
                    label-width="80px"
            >
                <el-form-item label="社交平台" prop="name">
                    <el-input
                            v-model="resumeLinkForm.name"
                            placeholder="请填写社交平台"
                            class="w-50"
                            :maxlength="20"
                            show-word-limit
                    ></el-input>
                </el-form-item>
                <el-form-item label="主页链接" prop="link">
                    <el-input
                            v-model="resumeLinkForm.link"
                            placeholder="请填写主页链接"
                            :maxlength="100"
                            show-word-limit
                    ></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="showLinkDialog = false">取 消</el-button>
                <el-button type="primary" @click="handleSaveResumeLink" :loading="posting">保 存</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    import VueSticky from "vue-sticky";
    import EditResumeTitle from "@/components/EditResumeTitle";
    import {library} from "@fortawesome/fontawesome-svg-core";
    import {getAllCountries, getCurrentCountry} from "@/api/country_api";
    import {
        getResumeInfo,
        saveResumeBasic,
        saveResumeEdu,
        delResumeEdu,
        saveResumeExp,
        delResumeExp,
        saveResumePractice,
        delResumePractice,
        saveResumeSkills,
        saveResumeLink,
        delResumeLink,
        delResumeAttachment,
        saveUserExpectJob
    } from "@/api/resume_api";
    import {searchSchool} from "@/api/school_api";
    import {listByType} from "@/api/dict_api";
    import {serachByName} from "@/api/company_api";
    import {getCategoryTree} from "@/api/category_api";
    import {getUploadPicToken, getUploadAttachmentToken} from "@/api/upload_api";

    import "quill/dist/quill.core.css";
    import "quill/dist/quill.snow.css";
    import "quill/dist/quill.bubble.css";
    import {quillEditor} from "vue-quill-editor";
    import Toast from "@/utils/toast";
    import {checkPicSize, checkAttachmentSize} from "@/utils/common";

    import {
        faEdit,
        faFemale,
        faMale,
        faMobileAlt,
        faEnvelope,
        faPlusSquare,
        faPencilAlt,
        faTrashAlt,
        faChevronUp,
        faChevronDown,
    } from "@fortawesome/free-solid-svg-icons";

    library.add(
        faEdit,
        faFemale,
        faMale,
        faMobileAlt,
        faEnvelope,
        faPlusSquare,
        faPencilAlt,
        faTrashAlt,
        faChevronUp,
        faChevronDown
    );

    export default {
        name: "EditResumePage",
        data() {
            return {
                resumeForm: {
                    id: undefined,
                    name: "",
                    birth: undefined,
                    gender: 1,
                    maritalStatus: 0,
                    returnTime: undefined,
                    graduateTime: undefined,
                    curPlace: "",
                    phoneCode: undefined,
                    phone: undefined,
                    introduction: "",
                    attachResume: undefined,
                },
                resumeFormRules: {
                    name: [{required: true, message: "请输入姓名", trigger: "blur"}],
                    birth: [{required: true, message: "请选择生日", trigger: "change"}],
                    gender: [{required: true, message: "请选择性别", trigger: "change"}],
                    curPlace: [
                        {required: true, message: "请输入所在城市", trigger: "blur"},
                    ],
                    email: [
                        {required: true, message: "请输入邮箱", trigger: "blur"},
                    ],
                    graduateTime: [
                        {required: true, message: "请选择毕业时间", trigger: "change"},
                    ],
                },
                resumeEduForm: {
                    id: undefined,
                    resumeId: undefined,
                    schoolName: undefined,
                    majorName: undefined,
                    degreeId: undefined,
                    startTime: undefined,
                    finishTime: undefined,
                    workingDates: undefined,
                    gpa: undefined,
                },
                resumeEduFormRules: {
                    schoolName: [
                        {required: true, message: "请输入学校名称", trigger: "blur"},
                    ],
                    majorName: [
                        {required: true, message: "请输入所学专业", trigger: "blur"},
                    ],
                    degreeId: [
                        {required: true, message: "请选择学历", trigger: "change"},
                    ],
                    workingDates: [
                        {
                            required: true,
                            message: "请选择在校时间",
                            trigger: "change",
                        },
                    ],
                },
                resumeExpForm: {
                    id: undefined,
                    resumeId: undefined,
                    workingTimeFlag: 1,
                    workingDates: undefined,
                    startTime: undefined,
                    finishTime: undefined,
                    company: undefined,
                    depart: undefined,
                    post: undefined,
                    description: undefined,
                    onWork: 0,
                },
                resumeExpFormRules: {
                    company: [
                        {required: true, message: "请输入公司名称", trigger: "blur"},
                    ],
                    depart: [{required: true, message: "请输入部门", trigger: "blur"}],
                    post: [{required: true, message: "请输入职位", trigger: "blur"}],
                    workingTimeFlag: [
                        {
                            required: true,
                            message: "请输入在职时间",
                            trigger: "change",
                        },
                    ],
                    description: [
                        {required: true, message: "请输入工作内容", trigger: "blur"},
                    ],
                },
                resumePracticeForm: {
                    id: undefined,
                    resumeId: undefined,
                    workingDates: undefined,
                    startTime: undefined,
                    finishTime: undefined,
                    title: undefined,
                    post: undefined,
                    description: undefined,
                    workingTimeFlag: 1,
                    onWork: 0,
                },
                resumePracticeFormRules: {
                    title: [{required: true, message: "请输入标题", trigger: "blur"}],
                    workingTimeFlag: [
                        {required: true, message: "请输入实践时间", trigger: "change"},
                    ],
                    description: [
                        {required: true, message: "请输入实践内容", trigger: "blur"},
                    ],
                },
                resumeLinkForm: {
                    id: undefined,
                    resumeId: undefined,
                    name: undefined,
                    link: undefined,
                },
                resumeLinkFormRules: {
                    name: [{required: true, message: "请输入平台名称", trigger: "blur"}],
                    link: [{required: true, message: "请输入主页链接", trigger: "blur"}],
                },
                expectJobForm: {
                    cityIds: [],
                    categoryIds: [],
                    salaryId: undefined,
                },
                uploadPicOptions: {
                    action: "",
                    params: {},
                    fileUrl: "",
                    acceptFileType: ".jpg,.jpeg,.png,.JPG,.JPEG,.PNG",
                },
                uploadAttachmentOptions: {
                    action: "",
                    params: {},
                    fileUrl: "",
                    acceptFileType: ".pdf,.doc,docx",
                },
                resume: {},
                countryOptions: [],
                degreeOptions: [],
                jobCategoryOptions: [],
                skillTagOptions: [],
                showBasicDialog: false,
                showIntroDialog: false,
                showEduDialog: false,
                showExpDialog: false,
                showPracticeDialog: false,
                showExpectJobDialog: false,
                showUploadAvatarIcon: false,
                showSkillDialog: false,
                showLinkDialog: false,
                showPreviewDialog: false,
                posting: false,
                introEditorOption: {
                    theme: "snow",
                    placeholder: "关于你的能力，特长和成就。",
                    modules: {
                        toolbar: [["bold"], [{list: "ordered"}, {list: "bullet"}]],
                    },
                },
                expEditorOption: {
                    theme: "bubble",
                    placeholder: "关于你在这个岗位的职责，工作内容和成就。",
                    modules: {
                        toolbar: [["bold"], [{list: "ordered"}, {list: "bullet"}]],
                    },
                },
                practiceEditorOption: {
                    theme: "bubble",
                    placeholder: "关于你在实践中的所获。",
                    modules: {
                        toolbar: [["bold"], [{list: "ordered"}, {list: "bullet"}]],
                    },
                },
                jobCatetoryProps: {
                    multiple: true,
                    expandTrigger: "hover",
                    value: "id",
                    label: "name",
                    emitPath: false,
                    children: "children",
                },
                newSkillTag: "",
                skillTagListForm: [],
                salaryOptions: [],//薪资范围
                oldDatePickerOptions: {
                    disabledDate(time) {
                        return time.getTime() >= Date.now() - 8.64e7;
                    },
                },
                resumeId: undefined, // 简历id，保存基本信息后赋值
                avatarLoading: false,
                localAvatarURL: undefined,
                cityIdProps: {
                    multiple: true,
                    lazy: true,
                    lazyLoad: (node, resolve) => {
                        if (node.level >= 1) {
                            this.$axios.request({
                                url: "/city/child-city",
                                method: "get",
                                params: {parentId: node.value}
                            }).then(data => {
                                console.log(data.data);
                                let nodes = data.data.map(second => {
                                    let children = second.children && second.children.map(third => {
                                        return {id:third.id, name:third.name, leaf:true}
                                    })
                                    return {id:second.id, name:second.name, children}
                                });
                                resolve(nodes);
                            })
                        } else {
                            resolve();
                        }
                    },
                    expandTrigger: "hover",
                    value: "id",
                    label: "name",
                    emitPath: false,
                    children: "children"
                },
                cityOptions: [],
            };
        },
        watch: {
            "expectJobForm.cityIds": function (newVal, oldVal) {
                if (newVal.length > 3) {
                    this.expectJobForm.cityIds = oldVal;
                    Toast.error("意向城市不能超过3个");
                }
            },
            "expectJobForm.categoryIds": function (newVal, oldVal) {
                if (newVal.length > 3) {
                    this.expectJobForm.categoryIds = oldVal;
                    Toast.error("意向职位不能超过3个");
                }
            },
            "resumeEduForm.gpa": function (newVal, oldVal) {
                if (newVal < 0) {
                    this.resumeEduForm.gpa = 0;
                }
            },
        },
        directives: {
            sticky: VueSticky,
        },
        components: {
            EditResumeTitle,
            quillEditor,
        },
        created() {
            this.initData();
        },
        methods: {
            initData() {
                getAllCountries().then((response) => {
                    this.countryOptions = response.data;
                });
                getCurrentCountry().then((response) => {
                    const {data} = response;
                    if (data && data.phoneCode && this.resumeForm.phoneCode === undefined) {
                        this.resumeForm.phoneCode = data.phoneCode;
                    }
                });
                listByType(1).then(
                    (response) => (this.degreeOptions = response.data.list)
                );
                listByType(3).then(
                    (response) => (this.skillTagOptions = response.data.list)
                );
                getCategoryTree().then(
                    (response) => (this.jobCategoryOptions = response.data)
                );

                this.resumeId = this.$route.query.resumeId;
                this.userId = this.$route.query.userId;
                this.getResumeInfo();
                listByType(9).then(response => (this.salaryOptions = response.data.list));

                //城市分级选择,获取第一级
                this.$axios.request({
                    url: "/city/child-city",
                    method: "get",
                    params: {parentId: 0}
                }).then(data => {
                    this.cityOptions = data.data;
                })
            },
            beforeAvatarUpload(file) {
                return new Promise((resolve, reject) => {
                    if (checkPicSize(file)) {
                        reject();
                    } else {
                        this.avatarLoading = true;
                        getUploadPicToken(file.name)
                            .then((response) => {
                                const {data} = response;
                                this.uploadPicOptions.action = data.host;
                                this.uploadPicOptions.params = data;
                                this.uploadPicOptions.fileUrl = data.host + "/" + data.key;
                                this.localAvatarURL = URL.createObjectURL(file);
                                resolve(data);
                            })
                            .catch((error) => {
                                this.localAvatarURL = undefined;
                                this.avatarLoading = false;
                                reject(error);
                            });
                    }
                });
            },
            handleAvatarSuccess() {
                this.resumeForm.avatar = this.uploadPicOptions.fileUrl;
                this.resume.avatar = this.uploadPicOptions.fileUrl;
                this.avatarLoading = false;
                this.handleSaveResumeBasic(false);
            },
            beforeAttachmengUpload(file) {
                return new Promise((resolve, reject) => {
                    if (checkAttachmentSize(file)) {
                        reject();
                    } else {
                        getUploadAttachmentToken(file.name)
                            .then((response) => {
                                const {data} = response;
                                this.uploadAttachmentOptions.action = data.host;
                                this.uploadAttachmentOptions.params = data;
                                this.uploadAttachmentOptions.fileUrl = data.host + "/" + data.key;
                                resolve(data);
                            })
                            .catch((error) => {
                                reject(error);
                            });
                    }
                });
            },
            handleUploadAttachmengSuccess() {
                this.resumeForm.attachResume = this.uploadAttachmentOptions.fileUrl;
                this.resume.attachResume = this.uploadAttachmentOptions.fileUrl;
                this.handleSaveResumeBasic(false);
            },
            getResumeInfo() {
                if (this.resumeId) {
                    getResumeInfo(this.resumeId).then((response) => {
                        this.resume = response.data;
                        this.setResumeFormValues();
                    });
                }
            },
            setResumeFormValues() {
                this.resumeForm.id = this.resume.id;
                this.resumeForm.name = this.resume.name;
                this.resumeForm.birth = this.resume.birth;
                this.resumeForm.gender = this.resume.gender;
                this.resumeForm.returnTime = this.resume.returnTime;
                this.resumeForm.graduateTime = this.resume.graduateTime;
                this.resumeForm.curPlace = this.resume.curPlace;
                this.resumeForm.phoneCode = this.resume.phoneCode;
                this.resumeForm.phone = this.resume.phone;
                this.resumeForm.introduction = this.resume.introduction;
            },
            handleEditResumeBasic() {
                this.showBasicDialog = true;
                this.setResumeFormValues();
                this.$nextTick(() => {
                    this.$refs["resumeForm"].clearValidate();
                });
            },
            handleEditResumeIntro() {
                this.showIntroDialog = true;
                this.setResumeFormValues();
            },
            handleEditResumeEdu(type, resumeEdu) {
                this.showEduDialog = true;
                this.resumeEduForm.resumeId = this.resume.id;
                if (type === "update") {
                    this.resumeEduForm.id = resumeEdu.id;
                    this.resumeEduForm.schoolName = resumeEdu.schoolName;
                    this.resumeEduForm.majorName = resumeEdu.majorName;
                    this.resumeEduForm.degreeId = resumeEdu.degree.id;
                    this.resumeEduForm.startTime = resumeEdu.startTime;
                    this.resumeEduForm.finishTime = resumeEdu.finishTime;
                    this.resumeEduForm.gpa = resumeEdu.gpa;
                    this.resumeEduForm.workingDates = [
                        this.resumeEduForm.startTime,
                        this.resumeEduForm.finishTime,
                    ];
                } else {
                    this.resumeEduForm.id = undefined;
                    this.resumeEduForm.schoolName = undefined;
                    this.resumeEduForm.majorName = undefined;
                    this.resumeEduForm.degreeId = undefined;
                    (this.resumeEduForm.workingDates = undefined),
                        (this.resumeEduForm.startTime = undefined);
                    this.resumeEduForm.finishTime = undefined;
                    this.resumeEduForm.gpa = undefined;
                }
                this.$nextTick(() => {
                    this.$refs["resumeEduForm"].clearValidate();
                });
            },
            handleEditResumeExp(type, resumeExp) {
                this.showExpDialog = true;
                this.resumeExpForm.resumeId = this.resume.id;
                if (type === "update") {
                    this.resumeExpForm.id = resumeExp.id;
                    this.resumeExpForm.startTime = resumeExp.startTime;
                    this.resumeExpForm.finishTime = resumeExp.finishTime;
                    this.resumeExpForm.company = resumeExp.company;
                    this.resumeExpForm.depart = resumeExp.depart;
                    this.resumeExpForm.post = resumeExp.post;
                    this.resumeExpForm.description = resumeExp.description;
                    this.resumeExpForm.onWork = resumeExp.onWork;
                    if (resumeExp.startTime && resumeExp.finishTime) {
                        this.resumeExpForm.workingDates = [
                            this.resumeExpForm.startTime,
                            this.resumeExpForm.finishTime,
                        ];
                    }
                } else {
                    this.resumeExpForm.id = undefined;
                    this.resumeExpForm.startTime = undefined;
                    this.resumeExpForm.finishTime = undefined;
                    this.resumeExpForm.company = undefined;
                    this.resumeExpForm.depart = undefined;
                    this.resumeExpForm.post = undefined;
                    this.resumeExpForm.description = undefined;
                    this.resumeExpForm.workingDates = undefined;
                    this.resumeExpForm.onWork = 0;
                }
                this.$nextTick(() => {
                    this.$refs["resumeExpForm"].clearValidate();
                });
            },
            handleEditResumePractice(type, resumePractice) {
                this.showPracticeDialog = true;
                this.resumePracticeForm.resumeId = this.resume.id;
                if (type === "update") {
                    this.resumePracticeForm.id = resumePractice.id;
                    this.resumePracticeForm.startTime = resumePractice.startTime;
                    this.resumePracticeForm.finishTime = resumePractice.finishTime;
                    this.resumePracticeForm.title = resumePractice.title;
                    this.resumePracticeForm.description = resumePractice.description;
                    this.resumePracticeForm.post = resumePractice.post;
                    this.resumePracticeForm.onWork = resumePractice.onWork;

                    this.resumePracticeForm.workingDates = [
                        this.resumePracticeForm.startTime,
                        this.resumePracticeForm.finishTime,
                    ];
                } else {
                    this.resumePracticeForm.id = undefined;
                    this.resumePracticeForm.workingDates = undefined;
                    this.resumePracticeForm.startTime = undefined;
                    this.resumePracticeForm.finishTime = undefined;
                    this.resumePracticeForm.title = undefined;
                    this.resumePracticeForm.description = undefined;
                    this.resumePracticeForm.post = undefined;
                    this.resumePracticeForm.onWork = undefined;
                }
                this.$nextTick(() => {
                    this.$refs["resumePracticeForm"].clearValidate();
                });
            },
            handleEditExpectJob() {
                this.showExpectJobDialog = true;
                if (this.resume.userExpectJob) {
                    this.expectJobForm.cityIds = this.resume.userExpectJob.cityList.map(
                        (city) => {
                            return city.id;
                        }
                    );
                    this.expectJobForm.categoryIds = this.resume.userExpectJob.categoryList.map(
                        (category) => {
                            return category.id;
                        }
                    );
                }
                this.$nextTick(() => {
                    this.$refs["expectJobForm"].clearValidate();
                });
            },
            handleEditResumeSkill() {
                this.showSkillDialog = true;
                this.skillTagListForm.length = 0;
                if (
                    this.resume.resumeSkillList &&
                    this.resume.resumeSkillList.length !== 0
                ) {
                    for (const resumeSkill of this.resume.resumeSkillList) {
                        this.skillTagListForm.push({name: resumeSkill.name, select: true});
                    }
                }
                for (const skillTag of this.skillTagOptions) {
                    const found = this.skillTagListForm.find(
                        (selectTag) => skillTag.name == selectTag.name
                    );
                    if (!found) {
                        this.skillTagListForm.push({name: skillTag.name, select: false});
                    }
                }
            },
            handleEditResumeLink(type, resumeLink) {
                this.showLinkDialog = true;
                this.resumeLinkForm.resumeId = this.resume.id;
                if (type === "update") {
                    this.resumeLinkForm.id = resumeLink.id;
                    this.resumeLinkForm.name = resumeLink.name;
                    this.resumeLinkForm.link = resumeLink.link;
                } else {
                    this.resumeLinkForm.id = undefined;
                    this.resumeLinkForm.name = undefined;
                    this.resumeLinkForm.link = undefined;
                }
                this.$nextTick(() => {
                    this.$refs["resumeLinkForm"].clearValidate();
                });
            },
            handleDelResumeEdu(id) {
                this.handleDeleteItemById(delResumeEdu, id);
            },
            handleDelResumeExp(id) {
                this.handleDeleteItemById(delResumeExp, id);
            },
            handleDelResumePractice(id) {
                this.handleDeleteItemById(delResumePractice, id);
            },
            handleDelResumeLink(id) {
                this.handleDeleteItemById(delResumeLink, id);
            },
            handleSaveResumeBasic(validation = false) {
                if (validation) {
                    this.$refs["resumeForm"].validate((valid) => {
                        if (valid) {
                            this.postSaveResumeBasic();
                        }
                    });
                } else {
                    this.postSaveResumeBasic();
                }
                // this.$axios.get('/api/resume-repository/save-resume-basic', {
                //     params: {categoryId: this.jobForm.categoryId[2]}
                // }).then(data => {
                //     this.jobDescription = data.data && data.data.description;
                // })
            },
            postSaveResumeBasic() {
                this.posting = true;
                saveResumeBasic(this.resumeForm)
                    .then((data) => {
                        console.log(data);
                        this.showBasicDialog = false;
                        this.showIntroDialog = false;
                        this.resumeId = data.data.id;
                        this.userId = data.data.userId;
                        this.$router.push({path: this.$route.path, query: {resumeId: this.resumeId, userId: this.userId}});
                        this.getResumeInfo();
                        Toast.success("保存成功");
                    })
                    .finally(() => {
                        this.posting = false;
                    });
            },
            handleSaveResumeEdu() {
                this.$refs["resumeEduForm"].validate((valid) => {
                    if (valid) {
                        this.posting = true;
                        this.resumeEduForm.startTime = this.resumeEduForm.workingDates[0];
                        this.resumeEduForm.finishTime = this.resumeEduForm.workingDates[1];
                        saveResumeEdu(this.resumeEduForm)
                            .then(() => {
                                this.getResumeInfo();
                                this.showEduDialog = false;
                            })
                            .finally(() => {
                                this.posting = false;
                            });
                    }
                });

            },
            handleSaveResumeExp() {
                this.$refs["resumeExpForm"].validate((valid) => {
                    if (valid) {
                        this.posting = true;
                        if (
                            this.resumeExpForm.workingDates &&
                            this.resumeExpForm.workingDates.length == 2
                        ) {
                            this.resumeExpForm.startTime = this.resumeExpForm.workingDates[0];
                            this.resumeExpForm.finishTime = this.resumeExpForm.workingDates[1];
                        }
                        saveResumeExp(this.resumeExpForm)
                            .then(() => {
                                this.getResumeInfo();
                                this.showExpDialog = false;
                            })
                            .finally(() => {
                                this.posting = false;
                            });
                    }
                });
            },
            handleSaveResumePractice() {
                this.$refs["resumePracticeForm"].validate((valid) => {
                    if (valid) {
                        this.posting = true;
                        if (
                            this.resumePracticeForm.workingDates &&
                            this.resumePracticeForm.workingDates.length == 2
                        ) {
                            this.resumePracticeForm.startTime = this.resumePracticeForm.workingDates[0];
                            this.resumePracticeForm.finishTime = this.resumePracticeForm.workingDates[1];
                        }
                        saveResumePractice(this.resumePracticeForm)
                            .then(() => {
                                this.getResumeInfo();
                                this.showPracticeDialog = false;
                            })
                            .finally(() => {
                                this.posting = false;
                            });
                    }
                });
            },
            handleSaveExpectJob() {
                this.$refs["expectJobForm"].validate((valid) => {
                    if (valid) {
                        this.posting = true;
                        this.expectJobForm.userId = this.userId;
                        saveUserExpectJob(this.expectJobForm)
                            .then(() => {
                                this.getResumeInfo();
                                this.showExpectJobDialog = false;
                            })
                            .finally(() => {
                                this.posting = false;
                            });
                    }
                });
            },
            handleSaveResumeSkills() {
                const selectSkillTags = {
                    resumeId: this.resume.id,
                    tagList: this.skillTagListForm
                        .filter((tag) => tag.select == true)
                        .map((tag) => {
                            return tag.name;
                        }),
                };
                this.posting = true;
                saveResumeSkills(selectSkillTags)
                    .then(() => {
                        this.getResumeInfo();
                        this.showSkillDialog = false;
                    })
                    .finally(() => {
                        this.posting = false;
                    });
            },
            handleSaveResumeLink() {
                this.$refs["resumeLinkForm"].validate((valid) => {
                    if (valid) {
                        this.posting = true;
                        saveResumeLink(this.resumeLinkForm)
                            .then(() => {
                                this.getResumeInfo();
                                this.showLinkDialog = false;
                            })
                            .finally(() => {
                                this.posting = false;
                            });
                    }
                });

            },
            searchSchoolName(keyword, cb) {
                if (!keyword || keyword.length < 2) {
                    return;
                }
                searchSchool(keyword).then((response) => {
                    cb(response.data);
                });
            },
            searchCompanyName(keyword, cb) {
                if (!keyword || keyword.length < 2) {
                    return;
                }
                serachByName(keyword).then((response) => {
                    cb(response.data.list);
                });
            },
            handleDeleteItemById(delById, id) {
                this.$confirm("是否要删除该记录？", {
                    confirmButtonText: "删除",
                }).then(() => {
                    delById(id).then(() => {
                        Toast.success("删除成功");
                        this.getResumeInfo();
                    });
                });
            },
            selectSkillTag(skillTag) {
                if (skillTag.select) {
                    skillTag.select = false;
                } else {
                    if (this.checkSelectSkillTagCount()) {
                        skillTag.select = true;
                    }
                }
            },
            handleAddNewSkillTag() {
                if (this.newSkillTag != "" && this.checkSelectSkillTagCount()) {
                    this.skillTagListForm.unshift({
                        name: this.newSkillTag,
                        select: true,
                    });
                    this.newSkillTag = "";
                }
            },
            checkSelectSkillTagCount() {
                const selectTagList = this.skillTagListForm.filter(
                    (tag) => tag.select == true
                );
                if (selectTagList.length > 4) {
                    Toast.error("标签数量不能超过5个");
                    return false;
                } else {
                    return true;
                }
            },
            goAnchor(selector) {
                const anchor = this.$el.querySelector(selector);
                const scrollTop = anchor.offsetTop - 80;
                document.body.scrollTop = scrollTop;
                document.documentElement.scrollTop = scrollTop;
                window.pageYOffset = scrollTop;
            },
            handlePreview() {
                this.$router.push({path: `/resume/${this.resume.id}`});
            },
            onDelResumeAttachClick() {
                this.$confirm("是否要删除附件简历？", {
                    confirmButtonText: "删除",
                }).then(() => {
                    delResumeAttachment(this.resume.id).then(() => {
                        this.getResumeInfo();
                    });
                });
            },
        },
    };
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
    $border-style: 1px solid #eee;

    .app-container {
        margin: 20px auto;
        min-height: calc(100vh - 477px);
    }

    .resume-body {
        background: #fff;
        padding: 20px;
        border: $border-style;
    }

    .resume-right-box {
        background: #fff;
        padding: 20px;
        border: $border-style;
        margin-bottom: 15px;
    }

    .resume-box {
        margin-bottom: 48px;
    }

    .time-text {
        font-size: 14px;
        font-weight: 400;
    }

    .position-text {
        font-size: 14px;
        font-weight: 500;
    }

    .gender-text {
        font-size: 16px;
    }

    .description-text {
        font-size: 15px;
        line-height: 1.7em;
    }

    .edit-box {
        position: relative;
    }

    .edit-item-box {
        position: absolute;
        right: 10px;
        top: 10px;
        background: #fff;
    }

    .el-input .el-select {
        width: 120px;
    }

    .skill-tag {
        margin-bottom: 5px;
        margin-right: 5px;
        cursor: pointer;
    }

    .link-item {
        padding-top: 8px;
        padding-bottom: 8px;
    }

    .btn-preview {
        width: 100%;
        font-size: 18px;
    }

    .m-input-text-width {
        width: 220px;
    }

    @media screen and (max-width: 440px) {
        .app-container {
            width: 100%;
        }

        .editemail {
            display: block;
        }

        /deep/ .navbar {
            justify-content: space-around;
            margin: 10px;
            padding: 10px;
            scroll-margin-bottom: 10px;
        }

        /deep/ .el-date-range-picker {
            position: relative;
            display: flex;
        }
    }
</style>

<style lang="scss" scoped>
    @import "bootstrap/scss/bootstrap.scss";

    .ql-container .ql-editor {
        min-height: 200px;
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

    .ql-bubble {
        border: 1px solid #dcdfe6;
        border-radius: 4px;
    }

    .b-toaster {
        z-index: 10000;
    }

    a.edit-text {
        cursor: pointer;
        font-size: 14px;
        color: $info;
    }

    .ql-editor.ql-blank::before {
        font-style: normal;
        color: #c0c4cc;
        font-size: 15px;
    }

    $avatarSize: 100px;

    .avatar-uploader {
        width: $avatarSize;
        height: $avatarSize;
    }

    .avatar-uploader .el-upload {
        cursor: pointer;
        position: relative;
        overflow: hidden;
    }

    .avatar-uploader .el-upload:hover {
        border-color: $info;
    }

    .avatar-uploader .avatar-uploader-icon {
        border: 1px dashed #d9d9d9;
        font-size: 28px;
        color: #8c939d;
        width: $avatarSize;
        height: $avatarSize;
        line-height: $avatarSize;
        text-align: center;
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
</style>
