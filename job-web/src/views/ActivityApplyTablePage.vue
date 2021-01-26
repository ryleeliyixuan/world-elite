<template>
    <div class="app-container">
        <div class="line1">
            <div class="text" @click="$router.go(-1)" style="cursor: pointer;">活动发布</div>
            <div class="text">></div>
            <div class="text">{{getNavTitle()}}</div>
        </div>
        <div class="title">
            <div class="template-title-container" v-if="type==='1' && templateId">
                <div class="name"><span>*</span>模板名</div>
                <el-input v-if="templateTitleEditing" size="small" class="value" v-model="applyForm.templateName" maxlength="20" ref="templateTitle"
                          @keyup.enter.native="onTemplateTitleEdit" @blur="onTemplateTitleCancel"></el-input>
                <span v-else class="template-name">{{applyForm.templateName}}</span>
                <svg-icon :icon-class="templateTitleEditing?'edit':'edit'" clickable style="width: 22px; height: 26px; margin-left: 18px;"
                          @mousedown.stop.prevent="onTemplateTitleEdit"></svg-icon>
            </div>
            <el-input v-if="titleEditing" class="editing" v-model="applyForm.title" maxlength="20" clearable ref="title" @keyup.enter.native="onTitleEdit"
                      @blur="onTitleCancel"></el-input>
            <span v-else class="title-text">{{applyForm.title}}</span>
            <svg-icon :icon-class="titleEditing?'edit':'edit'" clickable style="width: 22px; height: 26px; margin-left: 18px;"
                      @mousedown.stop.prevent="onTitleEdit"></svg-icon>
        </div>
        <div class="form-container">
            <div class="form-item">
                <div class="name"><span>*</span>姓名</div>
                <el-input size="small" class="value"></el-input>
            </div>
            <div :class="['form-item',{'hidden':applyForm.genderFlag==='2'}]">
                <div class="name"><span>{{getMustStatus('genderFlag')}}</span>性别</div>
                <div class="value">
                    <div class="value-item">
                        <svg-icon :icon-class="getValueIcon('genderFlag')"></svg-icon>
                        <span class="radio-value">男</span>
                    </div>
                    <div class="value-item">
                        <svg-icon :icon-class="getValueIcon('genderFlag')"></svg-icon>
                        <span class="radio-value">女</span>
                    </div>
                </div>
                <div class="value-item" @click="onHidden('genderFlag')">
                    <svg-icon :icon-class="getStatusIcon('genderFlag')" clickable></svg-icon>
                    <span class="hide">{{getStatusText('genderFlag')}}</span>
                </div>
                <div class="value-item" @click="onChangeFlag('genderFlag')">
                    <svg-icon :icon-class="getSelectedIcon('genderFlag')" clickable></svg-icon>
                    <span class="optional">此题改为选填</span>
                </div>
            </div>
            <div :class="['form-item',{'hidden':applyForm.phoneFlag==='2'}]">
                <div class="name"><span>{{getMustStatus('phoneFlag')}}</span>手机号</div>
                <el-input size="small" class="value" :disabled="applyForm.phoneFlag==='2'"></el-input>
                <div class="value-item" @click="onHidden('phoneFlag')">
                    <svg-icon :icon-class="getStatusIcon('phoneFlag')" clickable></svg-icon>
                    <span class="hide">{{getStatusText('phoneFlag')}}</span>
                </div>
                <div class="value-item" @click="onChangeFlag('phoneFlag')">
                    <svg-icon :icon-class="getSelectedIcon('phoneFlag')" clickable></svg-icon>
                    <span class="optional">此题改为选填</span>
                </div>
            </div>
            <div :class="['form-item',{'hidden':applyForm.emailFlag==='2'}]">
                <div class="name"><span>{{getMustStatus('emailFlag')}}</span>邮箱</div>
                <el-input size="small" class="value" :disabled="applyForm.emailFlag==='2'"></el-input>
                <div class="value-item" @click="onHidden('emailFlag')">
                    <svg-icon :icon-class="getStatusIcon('emailFlag')" clickable></svg-icon>
                    <span class="hide">{{getStatusText('emailFlag')}}</span>
                </div>
                <div class="value-item" @click="onChangeFlag('emailFlag')">
                    <svg-icon :icon-class="getSelectedIcon('emailFlag')" clickable></svg-icon>
                    <span class="optional">此题改为选填</span>
                </div>
            </div>
            <div :class="['form-item',{'hidden':applyForm.schoolFlag==='2'}]">
                <div class="name"><span>{{getMustStatus('schoolFlag')}}</span>学校</div>
                <el-input size="small" class="value" :disabled="applyForm.schoolFlag==='2'"></el-input>
                <div class="value-item" @click="onHidden('schoolFlag')">
                    <svg-icon :icon-class="getStatusIcon('schoolFlag')" clickable></svg-icon>
                    <span class="hide">{{getStatusText('schoolFlag')}}</span>
                </div>
                <div class="value-item" @click="onChangeFlag('schoolFlag')">
                    <svg-icon :icon-class="getSelectedIcon('schoolFlag')" clickable></svg-icon>
                    <span class="optional">此题改为选填</span>
                </div>
            </div>
            <div :class="['form-item',{'hidden':applyForm.gradeFlag==='2'}]">
                <div class="name"><span>{{getMustStatus('gradeFlag')}}</span>年级</div>
                <el-input size="small" class="value" :disabled="applyForm.gradeFlag==='2'"></el-input>
                <div class="value-item" @click="onHidden('gradeFlag')">
                    <svg-icon :icon-class="getStatusIcon('gradeFlag')" clickable></svg-icon>
                    <span class="hide">{{getStatusText('gradeFlag')}}</span>
                </div>
                <div class="value-item" @click="onChangeFlag('gradeFlag')">
                    <svg-icon :icon-class="getSelectedIcon('gradeFlag')" clickable></svg-icon>
                    <span class="optional">此题改为选填</span>
                </div>
            </div>
            <div :class="['form-item',{'hidden':applyForm.professionFlag==='2'}]">
                <div class="name"><span>{{getMustStatus('professionFlag')}}</span>专业</div>
                <el-input size="small" class="value" :disabled="applyForm.professionFlag==='2'"></el-input>
                <div class="value-item" @click="onHidden('professionFlag')">
                    <svg-icon :icon-class="getStatusIcon('professionFlag')" clickable></svg-icon>
                    <span class="hide">{{getStatusText('professionFlag')}}</span>
                </div>
                <div class="value-item" @click="onChangeFlag('professionFlag')">
                    <svg-icon :icon-class="getSelectedIcon('professionFlag')" clickable></svg-icon>
                    <span class="optional">此题改为选填</span>
                </div>
            </div>
            <div :class="['form-item',{'hidden':applyForm.educationFlag==='2'}]">
                <div class="name"><span>{{getMustStatus('educationFlag')}}</span>学历</div>
                <div class="value">
                    <div class="value-item" v-for="education in educationList" :key="education.id">
                        <svg-icon :icon-class="getValueIcon('educationFlag')"></svg-icon>
                        <span class="radio-value">{{education.name}}</span>
                    </div>
                </div>
                <div class="value-item" @click="onHidden('educationFlag')">
                    <svg-icon :icon-class="getStatusIcon('educationFlag')" clickable></svg-icon>
                    <span class="hide">{{getStatusText('educationFlag')}}</span>
                </div>
                <div class="value-item" @click="onChangeFlag('educationFlag')">
                    <svg-icon :icon-class="getSelectedIcon('educationFlag')" clickable></svg-icon>
                    <span class="optional">此题改为选填</span>
                </div>
            </div>

            <el-dropdown v-if="applyForm.questionnaireList.length===0" trigger="click" placement="bottom-start" @command="onQuestionType">
                <div class="add-button">
                    <svg-icon icon-class="add" style="margin-right: 8px;"></svg-icon>
                    添加新问题
                </div>
                <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item class="question-type" v-for="type in questionTypeList" :key="type.value" :command="type.value">{{type.label}}
                    </el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>
            <div v-else class="question-container">
                <div v-for="(question, index) in applyForm.questionnaireList">
                    <div v-if="question.type==='1'" class="question1-container">
                        <div v-if="question.edit" class="question1-edit">
                            <div class="question1-edit-line1">
                                <div class="title">{{question.title || '标题：'}}</div>
                                <div class="operate-container">
                                    <div class="delete" @click="onQuestionDelete(index)">删除此题</div>
                                    <el-dropdown trigger="click" placement="bottom-start" @command="onQuestionInsertBefore">
                                        <div class="add-to-before">
                                            <svg-icon icon-class="apply-table-add"></svg-icon>
                                            <span>前插新题</span>
                                        </div>
                                        <el-dropdown-menu slot="dropdown">
                                            <el-dropdown-item class="question-type" v-for="type in questionTypeList" :key="type.value"
                                                              :command="{type:type.value,index}">
                                                {{type.label}}
                                            </el-dropdown-item>
                                        </el-dropdown-menu>
                                    </el-dropdown>
                                    <el-dropdown trigger="click" placement="bottom-start" @command="onQuestionInsertAfter">
                                        <div class="add-to-after">
                                            <svg-icon icon-class="apply-table-add"></svg-icon>
                                            <span>后插新题</span>
                                        </div>
                                        <el-dropdown-menu slot="dropdown">
                                            <el-dropdown-item class="question-type" v-for="type in questionTypeList" :key="type.value"
                                                              :command="{type:type.value,index}">
                                                {{type.label}}
                                            </el-dropdown-item>
                                        </el-dropdown-menu>
                                    </el-dropdown>
                                </div>
                            </div>
                            <div class="question1-edit-line2"></div>
                            <div class="question1-edit-line3"></div>
                            <div class="question1-edit-line4">
                                <el-input class="title" v-model.trim="question.title" maxlength="50" placeholder="请输入标题（必填）"></el-input>
                                <div class="type-container">
                                    <div class="must-answer" @click="question.mustAnswer = !question.mustAnswer">
                                        <svg-icon :icon-class="question.mustAnswer?'apply-table-selected' : 'apply-table-unselected'"></svg-icon>
                                        <span class="text">设为必答</span>
                                    </div>
                                    <el-select v-model="question.typeForSelect" placeholder="更改题型" size="mini" style="margin-left: 26px; width: 160px;"
                                               @change="onQuestionTypeChange">
                                        <el-option v-for="item in questionTypeList"
                                                   size="mini"
                                                   :key="item.value"
                                                   :label="item.label"
                                                   :value="{question, value:item.value}">
                                        </el-option>
                                    </el-select>
                                </div>
                                <div class="button-container">
                                    <div class="cancel" @click="onQuestionDelete(index)">取消</div>
                                    <div class="done" @click="onQuestionEditCancel(question)">完成编辑</div>
                                </div>
                            </div>
                        </div>
                        <div v-else class="question1-done">
                            <div class="question1-done-line1">
                                <div class="title"><span class="must" v-if="question.mustAnswer">*</span>{{question.title}}</div>
                                <div class="operate-container">
                                    <div class="edit" @click="onQuestionEdit(question)">编辑</div>
                                    <div class="delete" @click="onQuestionDelete(index)">删除此题</div>
                                    <el-dropdown trigger="click" placement="bottom-start" @command="onQuestionInsertBefore">
                                        <div class="add-to-before">
                                            <svg-icon icon-class="apply-table-add"></svg-icon>
                                            <span>前插新题</span>
                                        </div>
                                        <el-dropdown-menu slot="dropdown">
                                            <el-dropdown-item class="question-type" v-for="type in questionTypeList" :key="type.value"
                                                              :command="{type:type.value,index}">
                                                {{type.label}}
                                            </el-dropdown-item>
                                        </el-dropdown-menu>
                                    </el-dropdown>
                                    <el-dropdown trigger="click" placement="bottom-start" @command="onQuestionInsertAfter">
                                        <div class="add-to-after">
                                            <svg-icon icon-class="apply-table-add"></svg-icon>
                                            <span>后插新题</span>
                                        </div>
                                        <el-dropdown-menu slot="dropdown">
                                            <el-dropdown-item class="question-type" v-for="type in questionTypeList" :key="type.value"
                                                              :command="{type:type.value,index}">
                                                {{type.label}}
                                            </el-dropdown-item>
                                        </el-dropdown-menu>
                                    </el-dropdown>
                                </div>
                            </div>
                            <div class="question1-done-line2"></div>
                        </div>
                    </div>
                    <div v-else-if="question.type==='2'" class="question2-container">
                        <div v-if="question.edit" class="question2-edit">
                            <div class="question2-edit-line1">
                                <div class="title">{{question.title || '标题：'}}</div>
                                <div class="operate-container">
                                    <div class="delete" @click="onQuestionDelete(index)">删除此题</div>
                                    <el-dropdown trigger="click" placement="bottom-start" @command="onQuestionInsertBefore">
                                        <div class="add-to-before">
                                            <svg-icon icon-class="apply-table-add"></svg-icon>
                                            <span>前插新题</span>
                                        </div>
                                        <el-dropdown-menu slot="dropdown">
                                            <el-dropdown-item class="question-type" v-for="type in questionTypeList" :key="type.value"
                                                              :command="{type:type.value,index}">
                                                {{type.label}}
                                            </el-dropdown-item>
                                        </el-dropdown-menu>
                                    </el-dropdown>
                                    <el-dropdown trigger="click" placement="bottom-start" @command="onQuestionInsertAfter">
                                        <div class="add-to-after">
                                            <svg-icon icon-class="apply-table-add"></svg-icon>
                                            <span>后插新题</span>
                                        </div>
                                        <el-dropdown-menu slot="dropdown">
                                            <el-dropdown-item class="question-type" v-for="type in questionTypeList" :key="type.value"
                                                              :command="{type:type.value,index}">
                                                {{type.label}}
                                            </el-dropdown-item>
                                        </el-dropdown-menu>
                                    </el-dropdown>
                                </div>
                            </div>
                            <div class="question2-edit-line2">
                                <div v-for="option in question.optionsList" class="option-container">
                                    <svg-icon icon-class="apply-table-unselected" class-name="icon"></svg-icon>
                                    <div class="options">{{option.options}}</div>
                                </div>
                            </div>
                            <div class="question2-edit-line3"></div>
                            <div class="question2-edit-line4">
                                <el-input class="title" v-model.trim="question.title" maxlength="50" placeholder="请输入标题（必填）"></el-input>
                                <div class="type-container">
                                    <div class="must-answer" @click="question.mustAnswer = !question.mustAnswer">
                                        <svg-icon :icon-class="question.mustAnswer?'apply-table-selected' : 'apply-table-unselected'"></svg-icon>
                                        <span class="text">设为必答</span>
                                    </div>
                                    <el-select v-model="question.typeForSelect" placeholder="更改题型" size="mini" style="margin-left: 26px; width: 160px;"
                                               @change="onQuestionTypeChange">
                                        <el-option v-for="item in questionTypeList"
                                                   size="mini"
                                                   :key="item.value"
                                                   :label="item.label"
                                                   :value="{question, value:item.value}">
                                        </el-option>
                                    </el-select>
                                </div>
                                <div class="option-container">
                                    <div class="item-container" v-for="(option, index) in question.optionsList" :key="index">
                                        <el-input v-model="option.options" class="option-input" placeholder="请输入选项" maxlength="50" size="mini"></el-input>
                                        <svg-icon icon-class="apply-table-delete" clickable @click="onQuestionOptionDelete(question, index)"></svg-icon>
                                    </div>
                                    <div class="option-add-button" @click="onQuestionOptionAdd(question)">
                                        <svg-icon icon-class="add" style="margin-right: 8px;"></svg-icon>
                                        添加选项
                                    </div>
                                </div>
                                <div class="button-container">
                                    <div class="cancel" @click="onQuestionDelete(index)">取消</div>
                                    <div class="done" @click="onQuestionEditCancel(question)">完成编辑</div>
                                </div>
                            </div>
                        </div>
                        <div v-else class="question2-done">
                            <div class="question2-done-line1">
                                <div class="title"><span class="must" v-if="question.mustAnswer">*</span>{{question.title}}</div>
                                <div class="operate-container">
                                    <div class="edit" @click="onQuestionEdit(question)">编辑</div>
                                    <div class="delete" @click="onQuestionDelete(index)">删除此题</div>
                                    <el-dropdown trigger="click" placement="bottom-start" @command="onQuestionInsertBefore">
                                        <div class="add-to-before">
                                            <svg-icon icon-class="apply-table-add"></svg-icon>
                                            <span>前插新题</span>
                                        </div>
                                        <el-dropdown-menu slot="dropdown">
                                            <el-dropdown-item class="question-type" v-for="type in questionTypeList" :key="type.value"
                                                              :command="{type:type.value,index}">
                                                {{type.label}}
                                            </el-dropdown-item>
                                        </el-dropdown-menu>
                                    </el-dropdown>
                                    <el-dropdown trigger="click" placement="bottom-start" @command="onQuestionInsertAfter">
                                        <div class="add-to-after">
                                            <svg-icon icon-class="apply-table-add"></svg-icon>
                                            <span>后插新题</span>
                                        </div>
                                        <el-dropdown-menu slot="dropdown">
                                            <el-dropdown-item class="question-type" v-for="type in questionTypeList" :key="type.value"
                                                              :command="{type:type.value,index}">
                                                {{type.label}}
                                            </el-dropdown-item>
                                        </el-dropdown-menu>
                                    </el-dropdown>
                                </div>
                            </div>
                            <div class="question2-done-line2">
                                <div v-for="option in question.optionsList" class="option-container">
                                    <svg-icon icon-class="apply-table-unselected" class-name="icon"></svg-icon>
                                    <div class="options">{{option.options}}</div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div v-else-if="question.type==='3'" class="question3-container">
                        <div v-if="question.edit" class="question3-edit">
                            <div class="question3-edit-line1">
                                <div class="title">{{question.title || '标题：'}}</div>
                                <div class="operate-container">
                                    <div class="delete" @click="onQuestionDelete(index)">删除此题</div>
                                    <el-dropdown trigger="click" placement="bottom-start" @command="onQuestionInsertBefore">
                                        <div class="add-to-before">
                                            <svg-icon icon-class="apply-table-add"></svg-icon>
                                            <span>前插新题</span>
                                        </div>
                                        <el-dropdown-menu slot="dropdown">
                                            <el-dropdown-item class="question-type" v-for="type in questionTypeList" :key="type.value"
                                                              :command="{type:type.value,index}">
                                                {{type.label}}
                                            </el-dropdown-item>
                                        </el-dropdown-menu>
                                    </el-dropdown>
                                    <el-dropdown trigger="click" placement="bottom-start" @command="onQuestionInsertAfter">
                                        <div class="add-to-after">
                                            <svg-icon icon-class="apply-table-add"></svg-icon>
                                            <span>后插新题</span>
                                        </div>
                                        <el-dropdown-menu slot="dropdown">
                                            <el-dropdown-item class="question-type" v-for="type in questionTypeList" :key="type.value"
                                                              :command="{type:type.value,index}">
                                                {{type.label}}
                                            </el-dropdown-item>
                                        </el-dropdown-menu>
                                    </el-dropdown>
                                </div>
                            </div>
                            <div class="question3-edit-line2">
                                <div v-for="option in question.optionsList" class="option-container">
                                    <svg-icon icon-class="apply-table-unchecked" class-name="icon"></svg-icon>
                                    <div class="options">{{option.options}}</div>
                                </div>
                            </div>
                            <div class="question3-edit-line3"></div>
                            <div class="question3-edit-line4">
                                <el-input class="title" v-model.trim="question.title" maxlength="50" placeholder="请输入标题（必填）"></el-input>
                                <div class="type-container">
                                    <div class="must-answer" @click="question.mustAnswer = !question.mustAnswer">
                                        <svg-icon :icon-class="question.mustAnswer?'apply-table-selected' : 'apply-table-unselected'"></svg-icon>
                                        <span class="text">设为必答</span>
                                    </div>
                                    <el-select v-model="question.typeForSelect" placeholder="更改题型" size="mini" style="margin-left: 26px; width: 160px;"
                                               @change="onQuestionTypeChange">
                                        <el-option v-for="item in questionTypeList"
                                                   size="mini"
                                                   :key="item.value"
                                                   :label="item.label"
                                                   :value="{question, value:item.value}">
                                        </el-option>
                                    </el-select>
                                </div>
                                <div class="option-container">
                                    <div class="item-container" v-for="(option, index) in question.optionsList" :key="index">
                                        <el-input v-model="option.options" class="option-input" placeholder="请输入选项" maxlength="50" size="mini"></el-input>
                                        <svg-icon icon-class="apply-table-delete" clickable @click="onQuestionOptionDelete(question, index)"></svg-icon>
                                    </div>
                                    <div class="option-add-button" @click="onQuestionOptionAdd(question)">
                                        <svg-icon icon-class="add" style="margin-right: 8px;"></svg-icon>
                                        添加选项
                                    </div>
                                </div>
                                <div class="button-container">
                                    <div class="cancel" @click="onQuestionDelete(index)">取消</div>
                                    <div class="done" @click="onQuestionEditCancel(question)">完成编辑</div>
                                </div>
                            </div>
                        </div>
                        <div v-else class="question3-done">
                            <div class="question3-done-line1">
                                <div class="title"><span class="must" v-if="question.mustAnswer">*</span>{{question.title}}</div>
                                <div class="operate-container">
                                    <div class="edit" @click="onQuestionEdit(question)">编辑</div>
                                    <div class="delete" @click="onQuestionDelete(index)">删除此题</div>
                                    <el-dropdown trigger="click" placement="bottom-start" @command="onQuestionInsertBefore">
                                        <div class="add-to-before">
                                            <svg-icon icon-class="apply-table-add"></svg-icon>
                                            <span>前插新题</span>
                                        </div>
                                        <el-dropdown-menu slot="dropdown">
                                            <el-dropdown-item class="question-type" v-for="type in questionTypeList" :key="type.value"
                                                              :command="{type:type.value,index}">
                                                {{type.label}}
                                            </el-dropdown-item>
                                        </el-dropdown-menu>
                                    </el-dropdown>
                                    <el-dropdown trigger="click" placement="bottom-start" @command="onQuestionInsertAfter">
                                        <div class="add-to-after">
                                            <svg-icon icon-class="apply-table-add"></svg-icon>
                                            <span>后插新题</span>
                                        </div>
                                        <el-dropdown-menu slot="dropdown">
                                            <el-dropdown-item class="question-type" v-for="type in questionTypeList" :key="type.value"
                                                              :command="{type:type.value,index}">
                                                {{type.label}}
                                            </el-dropdown-item>
                                        </el-dropdown-menu>
                                    </el-dropdown>
                                </div>
                            </div>
                            <div class="question3-done-line2">
                                <div v-for="option in question.optionsList" class="option-container">
                                    <svg-icon icon-class="apply-table-unchecked" class-name="icon"></svg-icon>
                                    <div class="options">{{option.options}}</div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div v-else-if="question.type==='4'" class="question4-container">
                        <div v-if="question.edit" class="question4-edit">
                            <div class="question4-edit-line1">
                                <div class="title">{{question.title || '标题：'}}</div>
                                <div class="operate-container">
                                    <div class="delete" @click="onQuestionDelete(index)">删除此题</div>
                                    <el-dropdown trigger="click" placement="bottom-start" @command="onQuestionInsertBefore">
                                        <div class="add-to-before">
                                            <svg-icon icon-class="apply-table-add"></svg-icon>
                                            <span>前插新题</span>
                                        </div>
                                        <el-dropdown-menu slot="dropdown">
                                            <el-dropdown-item class="question-type" v-for="type in questionTypeList" :key="type.value"
                                                              :command="{type:type.value,index}">
                                                {{type.label}}
                                            </el-dropdown-item>
                                        </el-dropdown-menu>
                                    </el-dropdown>
                                    <el-dropdown trigger="click" placement="bottom-start" @command="onQuestionInsertAfter">
                                        <div class="add-to-after">
                                            <svg-icon icon-class="apply-table-add"></svg-icon>
                                            <span>后插新题</span>
                                        </div>
                                        <el-dropdown-menu slot="dropdown">
                                            <el-dropdown-item class="question-type" v-for="type in questionTypeList" :key="type.value"
                                                              :command="{type:type.value,index}">
                                                {{type.label}}
                                            </el-dropdown-item>
                                        </el-dropdown-menu>
                                    </el-dropdown>
                                </div>
                            </div>
                            <div class="question4-edit-line2">
                                <span class="upload-button">上传附件</span>
                                <div class="placeholder">未选择任何文件</div>
                            </div>
                            <div class="question4-edit-line3"></div>
                            <div class="question4-edit-line4">
                                <el-input class="title" v-model.trim="question.title" maxlength="50" placeholder="请输入标题（必填）"></el-input>
                                <div class="type-container">
                                    <div class="must-answer" @click="question.mustAnswer = !question.mustAnswer">
                                        <svg-icon :icon-class="question.mustAnswer?'apply-table-selected' : 'apply-table-unselected'"></svg-icon>
                                        <span class="text">设为必答</span>
                                    </div>
                                    <el-select v-model="question.typeForSelect" placeholder="更改题型" size="mini" style="margin-left: 26px; width: 160px;"
                                               @change="onQuestionTypeChange">
                                        <el-option v-for="item in questionTypeList"
                                                   size="mini"
                                                   :key="item.value"
                                                   :label="item.label"
                                                   :value="{question, value:item.value}">
                                        </el-option>
                                    </el-select>
                                </div>
                                <div class="button-container">
                                    <div class="cancel" @click="onQuestionDelete(index)">取消</div>
                                    <div class="done" @click="onQuestionEditCancel(question)">完成编辑</div>
                                </div>
                            </div>
                        </div>
                        <div v-else class="question4-done">
                            <div class="question4-done-line1">
                                <div class="title"><span class="must" v-if="question.mustAnswer">*</span>{{question.title}}</div>
                                <div class="operate-container">
                                    <div class="edit" @click="onQuestionEdit(question)">编辑</div>
                                    <div class="delete" @click="onQuestionDelete(index)">删除此题</div>
                                    <el-dropdown trigger="click" placement="bottom-start" @command="onQuestionInsertBefore">
                                        <div class="add-to-before">
                                            <svg-icon icon-class="apply-table-add"></svg-icon>
                                            <span>前插新题</span>
                                        </div>
                                        <el-dropdown-menu slot="dropdown">
                                            <el-dropdown-item class="question-type" v-for="type in questionTypeList" :key="type.value"
                                                              :command="{type:type.value,index}">
                                                {{type.label}}
                                            </el-dropdown-item>
                                        </el-dropdown-menu>
                                    </el-dropdown>
                                    <el-dropdown trigger="click" placement="bottom-start" @command="onQuestionInsertAfter">
                                        <div class="add-to-after">
                                            <svg-icon icon-class="apply-table-add"></svg-icon>
                                            <span>后插新题</span>
                                        </div>
                                        <el-dropdown-menu slot="dropdown">
                                            <el-dropdown-item class="question-type" v-for="type in questionTypeList" :key="type.value"
                                                              :command="{type:type.value,index}">
                                                {{type.label}}
                                            </el-dropdown-item>
                                        </el-dropdown-menu>
                                    </el-dropdown>
                                </div>
                            </div>
                            <div class="question4-done-line2">
                                <span class="upload-button">上传附件</span>
                                <div class="placeholder">未选择任何文件</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="button-container">
            <div class="cancel" @click="onCancel">取消</div>
            <div class="preview" @click="onPreview">预览</div>
            <div class="save-template" @click="onSaveTemplate" v-if="this.type==='0'">存为模板</div>
            <div class="submit" @click="onSubmitApplyTable" v-if="this.type==='0'">{{this.applyForm.id?"更新报名表":"保存"}}</div>
            <div class="submit" @click="onSaveTemplate" v-if="this.type==='1'">{{templateId?'更新模板':'添加模板'}}</div>
        </div>

        <el-dialog class="cancel-dialog"
                   title="取消编辑提示"
                   :center="true"
                   :visible.sync="cancelDialogVisible"
                   width="445px">
            <div class="content">已编辑的内容退出不作保存，确定取消吗？</div>
            <div class="button-container">
                <div class="cancel" @click="cancelDialogVisible = false">取消</div>
                <div class="confirm" @click="onCancelConfirm">确定</div>
            </div>
        </el-dialog>
        <el-dialog class="cancel-dialog"
                   title="存为模板"
                   :center="true"
                   :visible.sync="saveTemplateDialogVisible"
                   width="445px">
            <div class="remain">剩余可设模板数：{{templateCount.maxCount - templateCount.count}}</div>
            <el-input class="editing" v-model="applyForm.templateName" maxlength="20" clearable autofocus placeholder="请输入模板名称"></el-input>
            <div class="button-container">
                <div class="cancel" @click="saveTemplateDialogVisible = false">取消</div>
                <div class="confirm" @click="onSaveTemplateConfirm">确定</div>
            </div>
        </el-dialog>
        <el-dialog class="cancel-dialog"
                   title="确定要更新模板吗？"
                   :center="true"
                   :visible.sync="updateTemplateDialogVisible"
                   width="445px">
            <div class="new-template" @click="onSaveNewTemplate">设为新模板</div>
            <div class="button-container">
                <div class="cancel" @click="updateTemplateDialogVisible = false">取消</div>
                <div class="confirm" @click="onSaveTemplateConfirm">确定</div>
            </div>
        </el-dialog>
        <el-dialog class="cancel-dialog"
                   title="保存模板成功"
                   :center="true"
                   :visible.sync="saveTemplateSuccessDialogVisible"
                   width="445px">
            <div class="remain" style="text-align: center">剩余可设模板数：{{templateCount.maxCount - templateCount.count}}</div>
            <div class="button-container">
                <div class="confirm" style="margin-left: 0;" @click="saveTemplateSuccessDialogVisible = false">关闭</div>
            </div>
        </el-dialog>

        <preview-apply :visible.sync="previewDialogVisible" :apply="applyForm"></preview-apply>
    </div>
</template>

<script>
    import previewApply from "@/components/activity/PreviewApply"

    export default {
        name: "ActivityAppleTablePage",
        components: {
            previewApply
        },
        data() {
            return {
                templateCount: {},// 我的模板数量/总数
                title: undefined, // 编辑前的标题
                templateTitle: undefined, // 编辑前的模板名称
                applyForm: { //
                    id: undefined, // 报名表或模板id
                    templateName: undefined, // 模板名称
                    title: '报名表', // 报名表标题
                    nameFlag: '0', // 姓名  0=必填，1=选填，2=隐藏
                    genderFlag: '0', // 性别
                    phoneFlag: '0', // 手机号
                    emailFlag: '0', // 邮箱
                    schoolFlag: '0', // 学校
                    professionFlag: '0', // 专业
                    gradeFlag: '0', // 年级
                    educationFlag: '0', // 学历
                    questionnaireList: [] // 问题列表
                },
                type: undefined, // 0：编辑报名表   1：编辑报名模板
                applyTableId: undefined, // 报名表id
                templateId: undefined, // 模板id

                titleEditing: false, // 标题是否在编辑中
                templateTitleEditing: false, // 模板名称编辑中

                educationList: [], // 学历列表
                questionTypeList: [{label: '填空题', value: '1'}, {label: '单选题', value: '2'}, {label: '多选题', value: '3'}, {label: '上传附件', value: '4'}], // 题型列表

                cancelDialogVisible: false, // 取消确认对话框
                saveTemplateDialogVisible: false, // 保存模板对话框
                updateTemplateDialogVisible: false, // 更新模板对话框
                saveTemplateSuccessDialogVisible: false, // 保存模板成功对话框
                previewDialogVisible: false, // 预览对话框
            }
        },
        created() {
            this.initData();
        },
        methods: {
            initData() {
                // id类型 0：报名表   1：报名模板
                this.type = this.$route.query.type;

                if (this.type === '0') {
                    this.applyTableId = this.$route.query.id;
                    if (this.applyTableId) {
                        this.$axios.get(`/activity-questionnaire/${this.applyTableId}`).then(response => {
                            this.applyForm = response.data;
                        })
                    }
                } else if (this.type === '1') {
                    this.templateId = this.$route.query.id;
                    if (this.templateId) {
                        this.$axios.get(`/questionnaire-template/${this.templateId}`).then(response => {
                            this.applyForm = response.data;
                        })
                    }
                }

                // 获取我的模板数量
                this.$axios.get('/questionnaire-template/my/count').then(response => {
                    this.templateCount = response.data;
                })

                // 加载学历列表
                this.$axios.get('/dict/list', {params: {type: 25, limit: 99}}).then(response => {
                    this.educationList = response.data.list;
                })
            },

            // 获取导航栏标题
            getNavTitle() {
                if (this.type === '0') {
                    return this.applyTableId ? "更新报名表" : "添加报名表"
                } else if (this.type === '1') {
                    return this.templateId ? "更新报名表模板" : "添加报名表模板"
                }
            },

            // 编辑模板名称
            onTemplateTitleEdit() {
                if (this.templateTitleEditing) {
                    this.templateTitle = undefined;
                    this.templateTitleEditing = false;
                } else {
                    this.templateTitle = this.applyForm.templateName;
                    this.templateTitleEditing = !this.templateTitleEditing;
                    if (this.templateTitleEditing) {
                        this.$nextTick(() => {
                            this.$refs['templateTitle'].focus();
                        });
                    }
                }
            },

            // 取消模板名称
            onTemplateTitleCancel() {
                if (this.templateTitleEditing) {
                    this.applyForm.templateName = this.templateTitle;
                    this.templateTitleEditing = false;
                }
            },

            // 编辑标题
            onTitleEdit() {
                if (this.titleEditing) {
                    this.title = undefined;
                    this.titleEditing = false;
                } else {
                    this.title = this.applyForm.title;
                    this.titleEditing = !this.titleEditing;
                    if (this.titleEditing) {
                        this.$nextTick(() => {
                            this.$refs['title'].focus();
                        });
                    }
                }
            },

            // 取消标题编辑
            onTitleCancel() {
                if (this.titleEditing) {
                    this.applyForm.title = this.title;
                    this.titleEditing = false;
                }
            },

            // 切换隐藏状态
            onHidden(key) {
                this.applyForm[key] = this.applyForm[key] === '2' ? '0' : '2';
            },

            // 切换选填状态
            onChangeFlag(key) {
                if (this.applyForm[key] !== '2') {
                    this.applyForm[key] = this.applyForm[key] === '1' ? '0' : '1';
                }
            },

            // 获取必填状态
            getMustStatus(key) {
                return this.applyForm[key] === '1' ? '' : '*';
            },

            // 获取单选按钮状态图标
            getValueIcon(key) {
                return this.applyForm[key] === '2' ? 'apply-table-disabled' : 'apply-table-unselected';
            },

            // 获取添加/隐藏文字
            getStatusText(key) {
                return this.applyForm[key] === '2' ? '添加' : '隐藏';
            },

            // 获取添加/隐藏图标
            getStatusIcon(key) {
                return this.applyForm[key] === '2' ? 'apply-table-add' : 'apply-table-sub';
            },

            // 获取‘此题改为选填的图标’
            getSelectedIcon(key) {
                switch (this.applyForm[key]) {
                    case '0':
                        return 'apply-table-unselected';
                    case '1':
                        return 'apply-table-selected';
                    case '2':
                        return 'apply-table-disabled';
                }
            },

            // 创建一个新题目
            newQuestion(type) {
                let question = {title: undefined, mustAnswer: '1', edit: true, type: type, typeForSelect: undefined, optionsList: []}
                switch (type) {
                    case '1': // 填空题
                        break;
                    case '2': // 单选题
                        question.optionsList.push({options: '选项1'});
                        question.optionsList.push({options: '选项2'});
                        question.optionsList.push({options: '选项3'});
                        break;
                    case '3': // 多选题
                        question.optionsList.push({options: '选项1'});
                        question.optionsList.push({options: '选项2'});
                        question.optionsList.push({options: '选项3'});
                        break;
                    case '4': // 附件上传题
                        break;
                }
                return question;
            },

            // 修改类型
            onQuestionTypeChange(object) {
                let {question, value} = object;
                question.type = value;
            },

            // 添加指定类型题目
            onQuestionType(command) {
                this.applyForm.questionnaireList.push(this.newQuestion(command));
            },

            // 编辑题目
            onQuestionEdit(question) {
                this.$set(question, 'edit', true);
            },

            // 取消编辑题目
            onQuestionEditCancel(question) {
                if (question.title) {
                    this.$set(question, 'edit', false);
                } else {
                    this.$message.warning("请输入标题");
                }
            },

            // 删除题目
            onQuestionDelete(index) {
                this.applyForm.questionnaireList.splice(index, 1);
            },

            // 前面添加新题
            onQuestionInsertBefore(item) {
                this.applyForm.questionnaireList.splice(item.index, 0, this.newQuestion(item.type))
            },

            // 后面添加新题
            onQuestionInsertAfter(item) {
                this.applyForm.questionnaireList.splice(item.index + 1, 0, this.newQuestion(item.type))
            },

            // 添加问题选项
            onQuestionOptionAdd(question) {
                if (question.optionsList.length < 10) {
                    question.optionsList.push({options: undefined});
                } else {
                    this.$message.warning("选项已达上限");
                }
            },

            // 删除问题选项
            onQuestionOptionDelete(question, index) {
                question.optionsList.splice(index, 1);
            },

            // 取消
            onCancel() {
                this.cancelDialogVisible = true;
            },

            // 取消对话框确认按钮
            onCancelConfirm() {
                this.cancelDialogVisible = false;
                this.$router.go(-1);
            },

            // 预览
            onPreview() {
                this.previewDialogVisible = true;
            },

            // 存为模板
            onSaveTemplate() {
                if (this.templateId) {
                    this.updateTemplateDialogVisible = true;
                } else if (this.templateCount.count < this.templateCount.maxCount) {
                    this.saveTemplateDialogVisible = true;
                } else {
                    this.$message.warning("可设模板数已达上限，不能保存模板")
                }
            },

            // 将已存在的模板保存为新模板
            onSaveNewTemplate() {
                this.templateId = undefined;
                this.onSaveTemplateConfirm();
            },

            // 存为模板确认按钮
            onSaveTemplateConfirm() {
                if (!this.applyForm.templateName) {
                    this.$message.warning("请输入模板名称");
                } else {
                    let request = this.templateId ? this.updateTemplate() : this.addTemplate();
                    request.then(response => {
                        if (!this.templateId) {
                            this.templateCount.count++;
                        }
                        this.templateId = response.data.id;
                        this.saveTemplateDialogVisible = false;
                        this.updateTemplateDialogVisible = false;
                        // this.saveTemplateSuccessDialogVisible = true; // TODO 确认要这个对话框  还是要下面的返回

                        this.$storage.setObject("报名表", {id: this.templateId, type: '1'});
                        this.$router.go(-1);
                    })
                }
            },

            // 添加模板
            addTemplate() {
                return this.$axios.post('/questionnaire-template', this.getFormData());
            },

            // 更新模板
            updateTemplate() {
                return this.$axios.patch(`/questionnaire-template/${this.templateId}`, this.getFormData());
            },

            // 提交报名表
            onSubmitApplyTable() {
                if (this.applyTableId) {
                    this.$axios.patch(`/activity-questionnaire/${this.applyTableId}`, this.getFormData()).then(response => {
                        console.log("更新报名表");
                        this.applyTableId = response.data.id;
                        this.$storage.setObject("报名表", {id: this.applyTableId, title: this.applyForm.title, type: '0'});
                        this.$router.go(-1);
                    })
                } else {
                    this.$axios.post('/activity-questionnaire', this.getFormData()).then(response => {
                        console.log("添加报名表");
                        this.applyTableId = response.data.id;
                        this.$storage.setObject("报名表", {id: this.applyTableId, title: this.applyForm.title, type: '0'});
                        this.$router.go(-1);
                    })
                }
            },

            // 参数转换
            getFormData() {
                let form = {...this.applyForm};
                form.questionnaireList = form.questionnaireList.map(item => {
                    delete item.typeForSelect;
                    return item;
                })
                console.log(form);
                return form;
            }
        }
    }
</script>

<style scoped lang="scss">
    .app-container {
        width: 1200px !important;
        padding: 24px 30px 50px;
        background: #FFFFFF;
        box-shadow: 0 4px 16px 3px rgba(191, 199, 215, 0.31);

        .line1 {
            display: flex;
            align-items: center;
            margin-bottom: 21px;

            .text {
                font-size: 16px;
                font-weight: 400;
                color: #999999;
                line-height: 22px;
                padding-right: 8px;
            }
        }

        .title {
            display: flex;
            align-items: center;
            justify-content: center;
            position: relative;

            .template-title-container {
                position: absolute;
                left: 0;
                top: 0;
                display: flex;
                align-items: center;

                .name {
                    min-width: 80px;
                    font-size: 18px;
                    font-weight: 400;
                    color: #666666;
                    line-height: 25px;

                    span {
                        color: red;
                        margin-right: 5px;
                        font-size: 18px;
                    }
                }

                ::v-deep .el-input__inner {
                    width: 277px;
                    background: #FFFFFF;
                    border-radius: 20px;
                    border: 1px solid #B0BEC5;
                }

                .template-name {
                    font-size: 18px;
                    color: #333333;
                    line-height: 22px;
                    font-weight: normal;
                }
            }

            .editing {
                width: 277px;
                font-size: 18px;
                font-weight: 400;
                color: #666666;
                line-height: 25px;

                ::v-deep .el-input__inner {
                    width: 277px;
                    background: #FFFFFF;
                    border-radius: 20px;
                    border: 1px solid #B0BEC5;
                }
            }

            .title-text {
                font-size: 30px;
                font-weight: bold;
                color: #333333;
                line-height: 42px;
            }
        }

        .form-container {
            margin-top: 38px;

            .form-item {
                display: flex;
                align-items: center;
                margin-top: 14px;

                .name {
                    min-width: 80px;
                    font-size: 18px;
                    font-weight: 400;
                    color: #666666;
                    line-height: 25px;

                    span {
                        color: red;
                        margin-right: 5px;
                        display: inline-block;
                        width: 7px;
                    }
                }

                .value-item {
                    display: flex;
                    align-items: center;
                    margin-left: 21px;
                    cursor: pointer;

                    &:first-of-type {
                        margin-left: 0;
                    }

                    .radio-value {
                        margin-left: 5px;
                        font-size: 16px;
                        color: #333333;
                        line-height: 22px;
                    }

                    .hide {
                        margin-left: 5px;
                        font-size: 14px;
                        color: #4895EF;
                        line-height: 20px;
                    }

                    .optional {
                        margin-left: 5px;
                        font-size: 14px;
                        color: #666666;
                        line-height: 20px;
                    }
                }

                .value {
                    width: auto;
                    min-width: 277px;
                    display: flex;
                    align-items: center;

                    ::v-deep .el-input__inner {
                        width: 277px;
                        background: #FFFFFF;
                        border-radius: 16px;
                        border: 1px solid #B0BEC5;
                    }
                }
            }

            .hidden {
                .name {
                    color: #B0BEC5;
                }

                .value {
                    ::v-deep .el-input__inner {
                        background: #ECEFF1;
                        border: 1px solid transparent;
                    }
                }

                .value-item {

                    .radio-value {
                        color: #B0BEC5;
                    }

                    .optional {
                        color: #B0BEC5;
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
                margin-top: 36px;
            }

            .question-container {
                padding-top: 24px;

                .question1-container {
                    margin-top: 13px;

                    .question1-edit {
                        width: 556px;

                        .question1-edit-line1 {
                            display: flex;
                            align-items: flex-start;
                            justify-content: space-between;

                            .title {
                                font-size: 21px;
                                color: #333333;
                                line-height: 29px;
                            }

                            .operate-container {
                                display: flex;
                                align-items: center;
                                flex-shrink: 0;
                                height: 29px;

                                .delete {
                                    font-size: 14px;
                                    color: #4895EF;
                                    line-height: 20px;
                                    margin-left: 19px;
                                    cursor: pointer;
                                }

                                .add-to-before, .add-to-after {
                                    display: flex;
                                    align-items: center;
                                    margin-left: 19px;
                                    cursor: pointer;

                                    span {
                                        font-size: 14px;
                                        color: #4895EF;
                                        line-height: 20px;
                                        margin-left: 7px;
                                    }
                                }

                            }
                        }

                        .question1-edit-line2 {
                            width: 100%;
                            max-width: 720px;
                            height: 30px;
                            background: #FFFFFF;
                            box-shadow: 0 2px 6px 0 #AAC5D8;
                            border-radius: 15px;
                            margin-top: 13px;
                        }

                        .question1-edit-line3 {
                            width: 25px;
                            height: 25px;
                            border: 25px solid;
                            border-color: transparent transparent #F0F6FF;
                            margin-left: 44px;
                            margin-top: -12px;
                        }

                        .question1-edit-line4 {
                            width: 100%;
                            background: #F0F6FF;
                            border-radius: 15px;
                            padding: 25px 12px 16px;

                            .title {
                                ::v-deep .el-input__inner {
                                    background: #FFFFFF;
                                    border-radius: 18px;
                                    font-size: 16px;
                                    color: #333333;
                                    line-height: 22px;
                                }
                            }

                            .type-container {
                                display: flex;
                                align-items: center;
                                margin-top: 13px;

                                .must-answer {
                                    display: flex;
                                    align-items: center;
                                    cursor: pointer;

                                    .text {
                                        font-size: 14px;
                                        color: #333333;
                                        line-height: 20px;
                                        margin-left: 5px;
                                    }
                                }
                            }

                            .button-container {
                                display: flex;
                                align-items: center;
                                justify-content: center;
                                margin-top: 36px;

                                .cancel {
                                    width: 134px;
                                    height: 35px;
                                    background: #ECEFF1;
                                    border-radius: 18px;
                                    border: 1px solid #ECEFF1;
                                    font-size: 16px;
                                    font-weight: 500;
                                    color: #333333;
                                    line-height: 35px;
                                    text-align: center;
                                    cursor: pointer;
                                }

                                .done {
                                    width: 133px;
                                    height: 35px;
                                    background: #4895EF;
                                    border-radius: 18px;
                                    font-size: 16px;
                                    font-weight: 500;
                                    color: #FFFFFF;
                                    line-height: 35px;
                                    text-align: center;
                                    margin-left: 19px;
                                    cursor: pointer;
                                }
                            }
                        }
                    }

                    .question1-done {
                        .question1-done-line1 {
                            display: flex;
                            align-items: flex-start;
                            justify-content: space-between;

                            .title {
                                font-size: 18px;
                                color: #666666;
                                line-height: 25px;

                                .must {
                                    color: red;
                                    margin-right: 5px;
                                }
                            }

                            .operate-container {
                                display: flex;
                                align-items: center;
                                flex-shrink: 0;
                                height: 29px;

                                .delete, .edit {
                                    font-size: 14px;
                                    color: #4895EF;
                                    line-height: 20px;
                                    margin-left: 19px;
                                    cursor: pointer;
                                }

                                .add-to-before, .add-to-after {
                                    display: flex;
                                    align-items: center;
                                    margin-left: 19px;
                                    cursor: pointer;

                                    span {
                                        font-size: 14px;
                                        color: #4895EF;
                                        line-height: 20px;
                                        margin-left: 7px;
                                    }
                                }

                            }
                        }

                        .question1-done-line2 {
                            width: 100%;
                            max-width: 720px;
                            height: 30px;
                            background: #FFFFFF;
                            box-shadow: 0 2px 6px 0 #AAC5D8;
                            border-radius: 15px;
                            margin-top: 13px;
                        }
                    }
                }

                .question2-container {
                    margin-top: 13px;

                    .question2-edit {
                        width: 556px;

                        .question2-edit-line1 {
                            display: flex;
                            align-items: flex-start;
                            justify-content: space-between;

                            .title {
                                font-size: 21px;
                                color: #333333;
                                line-height: 29px;
                            }

                            .operate-container {
                                display: flex;
                                align-items: center;
                                flex-shrink: 0;
                                height: 29px;

                                .delete {
                                    font-size: 14px;
                                    color: #4895EF;
                                    line-height: 20px;
                                    margin-left: 19px;
                                    cursor: pointer;
                                }

                                .add-to-before, .add-to-after {
                                    display: flex;
                                    align-items: center;
                                    margin-left: 19px;
                                    cursor: pointer;

                                    span {
                                        font-size: 14px;
                                        color: #4895EF;
                                        line-height: 20px;
                                        margin-left: 7px;
                                    }
                                }

                            }
                        }

                        .question2-edit-line2 {
                            width: 100%;
                            max-width: 720px;
                            margin-top: 13px;

                            .option-container {
                                display: flex;
                                align-items: flex-start;

                                .icon {
                                    display: flex;
                                    align-items: center;
                                    height: 22px;
                                    flex-shrink: 0;
                                }

                                .options {
                                    margin-left: 13px;
                                    font-size: 16px;
                                    font-weight: 500;
                                    color: #333333;
                                    line-height: 22px;
                                }
                            }
                        }

                        .question2-edit-line3 {
                            width: 25px;
                            height: 25px;
                            border: 25px solid;
                            border-color: transparent transparent #F0F6FF;
                            margin-left: 44px;
                            margin-top: -12px;
                        }

                        .question2-edit-line4 {
                            width: 100%;
                            background: #F0F6FF;
                            border-radius: 15px;
                            padding: 25px 12px 16px;

                            .title {
                                ::v-deep .el-input__inner {
                                    background: #FFFFFF;
                                    border-radius: 18px;
                                    font-size: 16px;
                                    color: #333333;
                                    line-height: 22px;
                                }
                            }

                            .type-container {
                                display: flex;
                                align-items: center;
                                margin-top: 13px;

                                .must-answer {
                                    display: flex;
                                    align-items: center;
                                    cursor: pointer;

                                    .text {
                                        font-size: 14px;
                                        color: #333333;
                                        line-height: 20px;
                                        margin-left: 5px;
                                    }
                                }
                            }

                            .option-container {
                                display: flex;
                                flex-direction: column;
                                padding: 10px 0 3px;

                                .item-container {
                                    display: flex;
                                    align-items: center;
                                    margin-bottom: 7px;

                                    .option-input {
                                        width: 260px;
                                        margin-right: 7px;

                                        ::v-deep .el-input__inner {
                                            background: #FFFFFF;
                                            border-radius: 5px;
                                            font-size: 14px;
                                            color: #666666;
                                            line-height: 20px;
                                        }
                                    }
                                }

                                .option-add-button {
                                    width: 110px;
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
                            }

                            .button-container {
                                display: flex;
                                align-items: center;
                                justify-content: center;
                                margin-top: 36px;

                                .cancel {
                                    width: 134px;
                                    height: 35px;
                                    background: #ECEFF1;
                                    border-radius: 18px;
                                    border: 1px solid #ECEFF1;
                                    font-size: 16px;
                                    font-weight: 500;
                                    color: #333333;
                                    line-height: 35px;
                                    text-align: center;
                                    cursor: pointer;
                                }

                                .done {
                                    width: 133px;
                                    height: 35px;
                                    background: #4895EF;
                                    border-radius: 18px;
                                    font-size: 16px;
                                    font-weight: 500;
                                    color: #FFFFFF;
                                    line-height: 35px;
                                    text-align: center;
                                    margin-left: 19px;
                                    cursor: pointer;
                                }
                            }
                        }
                    }

                    .question2-done {
                        .question2-done-line1 {
                            display: flex;
                            align-items: flex-start;
                            justify-content: space-between;

                            .title {
                                font-size: 18px;
                                color: #666666;
                                line-height: 25px;

                                .must {
                                    color: red;
                                    margin-right: 5px;
                                }
                            }

                            .operate-container {
                                display: flex;
                                align-items: center;
                                flex-shrink: 0;
                                height: 29px;

                                .delete, .edit {
                                    font-size: 14px;
                                    color: #4895EF;
                                    line-height: 20px;
                                    margin-left: 19px;
                                    cursor: pointer;
                                }

                                .add-to-before, .add-to-after {
                                    display: flex;
                                    align-items: center;
                                    margin-left: 19px;
                                    cursor: pointer;

                                    span {
                                        font-size: 14px;
                                        color: #4895EF;
                                        line-height: 20px;
                                        margin-left: 7px;
                                    }
                                }

                            }
                        }

                        .question2-done-line2 {
                            width: 100%;
                            max-width: 720px;

                            .option-container {
                                display: flex;
                                align-items: flex-start;

                                .icon {
                                    display: flex;
                                    align-items: center;
                                    height: 25px;
                                }

                                .options {
                                    margin-left: 13px;
                                    font-size: 18px;
                                    font-weight: 500;
                                    color: #666666;
                                    line-height: 25px;
                                }
                            }
                        }
                    }
                }

                .question3-container {
                    margin-top: 13px;

                    .question3-edit {
                        width: 556px;

                        .question3-edit-line1 {
                            display: flex;
                            align-items: flex-start;
                            justify-content: space-between;

                            .title {
                                font-size: 21px;
                                color: #333333;
                                line-height: 29px;
                            }

                            .operate-container {
                                display: flex;
                                align-items: center;
                                flex-shrink: 0;
                                height: 29px;

                                .delete {
                                    font-size: 14px;
                                    color: #4895EF;
                                    line-height: 20px;
                                    margin-left: 19px;
                                    cursor: pointer;
                                }

                                .add-to-before, .add-to-after {
                                    display: flex;
                                    align-items: center;
                                    margin-left: 19px;
                                    cursor: pointer;

                                    span {
                                        font-size: 14px;
                                        color: #4895EF;
                                        line-height: 20px;
                                        margin-left: 7px;
                                    }
                                }

                            }
                        }

                        .question3-edit-line2 {
                            width: 100%;
                            max-width: 720px;
                            margin-top: 13px;

                            .option-container {
                                display: flex;
                                align-items: flex-start;

                                .icon {
                                    display: flex;
                                    align-items: center;
                                    height: 22px;
                                }

                                .options {
                                    margin-left: 13px;
                                    font-size: 16px;
                                    font-weight: 500;
                                    color: #333333;
                                    line-height: 22px;
                                }
                            }
                        }

                        .question3-edit-line3 {
                            width: 25px;
                            height: 25px;
                            border: 25px solid;
                            border-color: transparent transparent #F0F6FF;
                            margin-left: 44px;
                            margin-top: -12px;
                        }

                        .question3-edit-line4 {
                            width: 100%;
                            background: #F0F6FF;
                            border-radius: 15px;
                            padding: 25px 12px 16px;

                            .title {
                                ::v-deep .el-input__inner {
                                    background: #FFFFFF;
                                    border-radius: 18px;
                                    font-size: 16px;
                                    color: #333333;
                                    line-height: 22px;
                                }
                            }

                            .type-container {
                                display: flex;
                                align-items: center;
                                margin-top: 13px;

                                .must-answer {
                                    display: flex;
                                    align-items: center;
                                    cursor: pointer;

                                    .text {
                                        font-size: 14px;
                                        color: #333333;
                                        line-height: 20px;
                                        margin-left: 5px;
                                    }
                                }
                            }

                            .option-container {
                                display: flex;
                                flex-direction: column;
                                padding: 10px 0 3px;

                                .item-container {
                                    display: flex;
                                    align-items: center;
                                    margin-bottom: 7px;

                                    .option-input {
                                        width: 260px;
                                        margin-right: 7px;

                                        ::v-deep .el-input__inner {
                                            background: #FFFFFF;
                                            border-radius: 5px;
                                            font-size: 14px;
                                            color: #666666;
                                            line-height: 20px;
                                        }
                                    }
                                }

                                .option-add-button {
                                    width: 110px;
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
                            }

                            .button-container {
                                display: flex;
                                align-items: center;
                                justify-content: center;
                                margin-top: 36px;

                                .cancel {
                                    width: 134px;
                                    height: 35px;
                                    background: #ECEFF1;
                                    border-radius: 18px;
                                    border: 1px solid #ECEFF1;
                                    font-size: 16px;
                                    font-weight: 500;
                                    color: #333333;
                                    line-height: 35px;
                                    text-align: center;
                                    cursor: pointer;
                                }

                                .done {
                                    width: 133px;
                                    height: 35px;
                                    background: #4895EF;
                                    border-radius: 18px;
                                    font-size: 16px;
                                    font-weight: 500;
                                    color: #FFFFFF;
                                    line-height: 35px;
                                    text-align: center;
                                    margin-left: 19px;
                                    cursor: pointer;
                                }
                            }
                        }
                    }

                    .question3-done {
                        .question3-done-line1 {
                            display: flex;
                            align-items: flex-start;
                            justify-content: space-between;

                            .title {
                                font-size: 18px;
                                color: #666666;
                                line-height: 25px;

                                .must {
                                    color: red;
                                    margin-right: 5px;
                                }
                            }

                            .operate-container {
                                display: flex;
                                align-items: center;
                                flex-shrink: 0;
                                height: 29px;

                                .delete, .edit {
                                    font-size: 14px;
                                    color: #4895EF;
                                    line-height: 20px;
                                    margin-left: 19px;
                                    cursor: pointer;
                                }

                                .add-to-before, .add-to-after {
                                    display: flex;
                                    align-items: center;
                                    margin-left: 19px;
                                    cursor: pointer;

                                    span {
                                        font-size: 14px;
                                        color: #4895EF;
                                        line-height: 20px;
                                        margin-left: 7px;
                                    }
                                }

                            }
                        }

                        .question3-done-line2 {
                            width: 100%;
                            max-width: 720px;

                            .option-container {
                                display: flex;
                                align-items: flex-start;

                                .icon {
                                    display: flex;
                                    align-items: center;
                                    height: 25px;
                                }

                                .options {
                                    margin-left: 13px;
                                    font-size: 18px;
                                    font-weight: 500;
                                    color: #666666;
                                    line-height: 25px;
                                }
                            }
                        }
                    }
                }

                .question4-container {
                    margin-top: 13px;

                    .question4-edit {
                        width: 556px;

                        .question4-edit-line1 {
                            display: flex;
                            align-items: flex-start;
                            justify-content: space-between;

                            .title {
                                font-size: 21px;
                                color: #333333;
                                line-height: 29px;
                            }

                            .operate-container {
                                display: flex;
                                align-items: center;
                                flex-shrink: 0;
                                height: 29px;

                                .delete {
                                    font-size: 14px;
                                    color: #4895EF;
                                    line-height: 20px;
                                    margin-left: 19px;
                                    cursor: pointer;
                                }

                                .add-to-before, .add-to-after {
                                    display: flex;
                                    align-items: center;
                                    margin-left: 19px;
                                    cursor: pointer;

                                    span {
                                        font-size: 14px;
                                        color: #4895EF;
                                        line-height: 20px;
                                        margin-left: 7px;
                                    }
                                }

                            }
                        }

                        .question4-edit-line2 {
                            width: 100%;
                            max-width: 720px;
                            height: 30px;
                            background: #FFFFFF;
                            box-shadow: 0 2px 6px 0 #AAC5D8;
                            border-radius: 15px;
                            margin-top: 13px;
                            display: flex;
                            align-items: center;
                            padding: 0 3px;

                            .upload-button {
                                width: 84px;
                                height: 24px;
                                background: #4895EF;
                                border-radius: 12px;
                                font-size: 14px;
                                font-weight: 400;
                                color: #FFFFFF;
                                line-height: 24px;
                                text-align: center;
                            }

                            .placeholder {
                                font-size: 14px;
                                font-weight: 400;
                                color: #B0BEC5;
                                line-height: 20px;
                                margin-left: 9px;
                            }
                        }

                        .question4-edit-line3 {
                            width: 25px;
                            height: 25px;
                            border: 25px solid;
                            border-color: transparent transparent #F0F6FF;
                            margin-left: 44px;
                            margin-top: -12px;
                        }

                        .question4-edit-line4 {
                            width: 100%;
                            background: #F0F6FF;
                            border-radius: 15px;
                            padding: 25px 12px 16px;

                            .title {
                                ::v-deep .el-input__inner {
                                    background: #FFFFFF;
                                    border-radius: 18px;
                                    font-size: 16px;
                                    color: #333333;
                                    line-height: 22px;
                                }
                            }

                            .type-container {
                                display: flex;
                                align-items: center;
                                margin-top: 13px;

                                .must-answer {
                                    display: flex;
                                    align-items: center;
                                    cursor: pointer;

                                    .text {
                                        font-size: 14px;
                                        color: #333333;
                                        line-height: 20px;
                                        margin-left: 5px;
                                    }
                                }
                            }

                            .button-container {
                                display: flex;
                                align-items: center;
                                justify-content: center;
                                margin-top: 36px;

                                .cancel {
                                    width: 134px;
                                    height: 35px;
                                    background: #ECEFF1;
                                    border-radius: 18px;
                                    border: 1px solid #ECEFF1;
                                    font-size: 16px;
                                    font-weight: 500;
                                    color: #333333;
                                    line-height: 35px;
                                    text-align: center;
                                    cursor: pointer;
                                }

                                .done {
                                    width: 133px;
                                    height: 35px;
                                    background: #4895EF;
                                    border-radius: 18px;
                                    font-size: 16px;
                                    font-weight: 500;
                                    color: #FFFFFF;
                                    line-height: 35px;
                                    text-align: center;
                                    margin-left: 19px;
                                    cursor: pointer;
                                }
                            }
                        }
                    }

                    .question4-done {
                        .question4-done-line1 {
                            display: flex;
                            align-items: flex-start;
                            justify-content: space-between;

                            .title {
                                font-size: 18px;
                                color: #666666;
                                line-height: 25px;

                                .must {
                                    color: red;
                                    margin-right: 5px;
                                }
                            }

                            .operate-container {
                                display: flex;
                                align-items: center;
                                flex-shrink: 0;
                                height: 29px;

                                .delete, .edit {
                                    font-size: 14px;
                                    color: #4895EF;
                                    line-height: 20px;
                                    margin-left: 19px;
                                    cursor: pointer;
                                }

                                .add-to-before, .add-to-after {
                                    display: flex;
                                    align-items: center;
                                    margin-left: 19px;
                                    cursor: pointer;

                                    span {
                                        font-size: 14px;
                                        color: #4895EF;
                                        line-height: 20px;
                                        margin-left: 7px;
                                    }
                                }

                            }
                        }

                        .question4-done-line2 {
                            width: 100%;
                            max-width: 720px;
                            height: 30px;
                            background: #FFFFFF;
                            box-shadow: 0 2px 6px 0 #AAC5D8;
                            border-radius: 15px;
                            margin-top: 13px;
                            display: flex;
                            align-items: center;
                            padding: 0 3px;

                            .upload-button {
                                width: 84px;
                                height: 24px;
                                background: #4895EF;
                                border-radius: 12px;
                                font-size: 14px;
                                font-weight: 400;
                                color: #FFFFFF;
                                line-height: 24px;
                                text-align: center;
                            }

                            .placeholder {
                                font-size: 14px;
                                font-weight: 400;
                                color: #B0BEC5;
                                line-height: 20px;
                                margin-left: 9px;
                            }
                        }
                    }
                }
            }
        }

        .button-container {
            display: flex;
            align-items: center;
            justify-content: center;
            margin-top: 150px;

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
                margin-right: 21px;
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
                margin-right: 21px;
                cursor: pointer;
            }

            .save-template {
                width: 134px;
                height: 35px;
                background: #EBF3FD;
                border-radius: 18px;
                border: 1px solid #EBF3FD;
                font-size: 16px;
                font-weight: 500;
                color: #4895EF;
                line-height: 35px;
                text-align: center;
                margin-right: 21px;
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

            .content {
                font-size: 16px;
                color: #333333;
                line-height: 22px;
                text-align: center;
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

            .remain {
                font-size: 16px;
                font-weight: 400;
                color: #999999;
                line-height: 22px;
                text-align: right;
                width: 100%;
                margin-bottom: 6px;
            }

            .editing {
                font-size: 18px;
                font-weight: 400;
                color: #666666;
                line-height: 25px;

                ::v-deep .el-input__inner {
                    background: #FFFFFF;
                    border-radius: 20px;
                    border: 1px solid #B0BEC5;
                }
            }

            .new-template {
                text-align: center;
                color: #4895EF;
                line-height: 16px;
                font-size: 16px;
                margin-bottom: 50px;
                cursor: pointer;
            }
        }
    }

    .el-dropdown-menu {
        background: #ECF7FF;
        border-radius: 0 0 14px 14px;

        .question-type {
            width: 122px;
            text-align: center;
            line-height: 27px;
        }
    }
</style>
