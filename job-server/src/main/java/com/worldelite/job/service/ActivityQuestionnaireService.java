package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import com.worldelite.job.entity.Activity;
import com.worldelite.job.entity.ActivityQuestionnaire;
import com.worldelite.job.entity.QuestionnaireTemplate;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.ActivityQuestionnaireForm;
import com.worldelite.job.form.QuestionnaireForm;
import com.worldelite.job.form.QuestionnaireOptionsForm;
import com.worldelite.job.mapper.ActivityQuestionnaireMapper;
import com.worldelite.job.vo.ActivityVo;
import com.worldelite.job.vo.QuestionnaireOptionsVo;
import com.worldelite.job.vo.QuestionnaireTemplateVo;
import com.worldelite.job.vo.QuestionnaireVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 报名表服务类
 */
@Service
@Slf4j
public class ActivityQuestionnaireService extends BaseService{

    @Autowired
    private ActivityQuestionnaireMapper activityQuestionnaireMapper;

    @Autowired
    private QuestionnaireTemplateService questionnaireTemplateService;

    @Autowired
    private QuestionnaireService questionnaireService;

    @Autowired
    private ActivityService activityService;

    /**
     * 添加报名表
     * @param activityQuestionnaireForm
     */
    @Transactional
    public QuestionnaireTemplateVo addActivityQuestionnaire(ActivityQuestionnaireForm activityQuestionnaireForm){
        ActivityQuestionnaire questionnaire = new ActivityQuestionnaire();
        BeanUtil.copyProperties(activityQuestionnaireForm,questionnaire);
        //保存报名表信息
        activityQuestionnaireMapper.insertSelective(questionnaire);
        log.debug("添加报名表，ID={}",questionnaire.getId());
        //遍历保存问卷信息
        QuestionnaireForm[] questionnaireForms = activityQuestionnaireForm.getQuestionnaireList();
        if(questionnaireForms != null){
            for(int i=0;i<questionnaireForms.length;i++){
                QuestionnaireForm questionnaireForm = questionnaireForms[i];
                questionnaireForm.setTemplate((byte) 0);
                questionnaireForm.setRegistrationTemplateId(questionnaire.getId());
                questionnaireService.addQuestionnaire(questionnaireForm);
            }
        }
        return getSimpleActivityQuestionnaire(questionnaire);
    }

    /**
     * 修改报名表
     * @param id 报名表ID
     * @param activityQuestionnaireForm
     * @return
     */
    @Transactional
    public QuestionnaireTemplateVo updateActivityQuestionnaire(Integer id,ActivityQuestionnaireForm activityQuestionnaireForm) {
        ActivityQuestionnaire activityQuestionnaire = activityQuestionnaireMapper.selectByPrimaryKey(id);
        if (activityQuestionnaire == null) {
            throw new ServiceException(message("activity.questionnaire.not.exists"));
        }
        BeanUtil.copyProperties(activityQuestionnaireForm, activityQuestionnaire);
        //保存模板信息
        log.debug("修改报名表：{}", activityQuestionnaire.getId());
        activityQuestionnaireMapper.updateByPrimaryKeySelective(activityQuestionnaire);
        //删除问卷信息
        questionnaireService.deleteByTemplateId(activityQuestionnaire.getId());
        //遍历保存问卷信息
        QuestionnaireForm[] questionnaireForms = activityQuestionnaireForm.getQuestionnaireList();
        if (questionnaireForms != null) {
            for (int i = 0; i < questionnaireForms.length; i++) {
                QuestionnaireForm questionnaireForm = questionnaireForms[i];
                questionnaireForm.setTemplate((byte) 0);
                questionnaireForm.setRegistrationTemplateId(activityQuestionnaire.getId());
                questionnaireService.addQuestionnaire(questionnaireForm);
            }
        }
        return getSimpleActivityQuestionnaire(activityQuestionnaire);
    }

    /**
     * 根据ID获取报名表模板详情
     * @param id
     * @return
     */
    public QuestionnaireTemplateVo getActivityQuestionnaire(Integer id){
        ActivityQuestionnaire activityQuestionnaire = activityQuestionnaireMapper.selectByPrimaryKey(id);
        if (activityQuestionnaire == null) {
            throw new ServiceException(message("activity.questionnaire.not.exists"));
        }
        return getActivityQuestionnaireDetail(activityQuestionnaire);
    }

    /**
     * 获取模板详情，包含问卷信息
     * @param activityQuestionnaire
     * @return
     */
    private QuestionnaireTemplateVo getActivityQuestionnaireDetail(ActivityQuestionnaire activityQuestionnaire){
        //获取基本信息
        QuestionnaireTemplateVo template = new QuestionnaireTemplateVo();
        BeanUtil.copyProperties(activityQuestionnaire,template);
        //获取问卷列表
        List<QuestionnaireVo> questionnaireVoList = questionnaireService.getQuestionnaireList(template.getId(), (byte) 0);
        template.setQuestionnaireList(questionnaireVoList);
        return template;
    }

    private QuestionnaireTemplateVo getActivityQuestionnaireDetail(Integer id){
        ActivityQuestionnaire activityQuestionnaire = activityQuestionnaireMapper.selectByPrimaryKey(id);
        return getActivityQuestionnaireDetail(activityQuestionnaire);
    }

    /**
     * 通过模板添加报名表
     * @param type ID类型
     * @param id ID
     * @return
     */
    public QuestionnaireTemplateVo addActivityQuestionnaireFromTemplate(String type,Integer id){
        //类型为报名表，则直接返回
        if("0".equals(type)){
            return getSimpleActivityQuestionnaire(id);
        }
        //通过模板添加报名表
        if("1".equals(type)){
            QuestionnaireTemplateVo template = questionnaireTemplateService.getQuestionnaireTemplate(id);
            ActivityQuestionnaireForm activityQuestionnaireForm = new ActivityQuestionnaireForm();
            BeanUtil.copyProperties(template,activityQuestionnaireForm);
            activityQuestionnaireForm.setQuestionnaireList(getQuestionnaireFormArray(template.getQuestionnaireList()));
            return addActivityQuestionnaire(activityQuestionnaireForm);
        }
        throw new ServiceException(message("api.error.invalid.param"));
    }

    /**
     * 将报名表转换成简单的视图对象
     * @param activityQuestionnaire
     * @return
     */
    public QuestionnaireTemplateVo getSimpleActivityQuestionnaire(ActivityQuestionnaire activityQuestionnaire){
        QuestionnaireTemplateVo template = new QuestionnaireTemplateVo();
        BeanUtil.copyProperties(activityQuestionnaire,template);
        return template;
    }


    public QuestionnaireTemplateVo getSimpleActivityQuestionnaire(Integer id){
        ActivityQuestionnaire activityQuestionnaire = activityQuestionnaireMapper.selectByPrimaryKey(id);
        return getSimpleActivityQuestionnaire(activityQuestionnaire);
    }

    /**
     * 移除报名表和模板的关联
     * @param templateId
     */
    public void removeTemplate(Integer templateId){
        activityQuestionnaireMapper.removeTemplate(templateId);
    }

    /**
     * 将问卷视图列表转换成问卷表单
     * @param questionnaireList
     * @return
     */
    private QuestionnaireForm[] getQuestionnaireFormArray(List<QuestionnaireVo> questionnaireList){
        QuestionnaireForm[] forms = new QuestionnaireForm[questionnaireList.size()];
        int i = 0;
        for(QuestionnaireVo questionnaire:questionnaireList){
            QuestionnaireForm form = new QuestionnaireForm();
            BeanUtil.copyProperties(questionnaire,form);
            form.setOptionsList(getQuestionnaireOptionsArray(questionnaire.getOptionsList()));
            forms[i++] = form;
        }
        return forms;
    }

    /**
     * 选项视图转换成选项表单
     * @param questionnaireOptionsList
     * @return
     */
    private QuestionnaireOptionsForm[] getQuestionnaireOptionsArray(List<QuestionnaireOptionsVo> questionnaireOptionsList){
        QuestionnaireOptionsForm[] optionsForms = new QuestionnaireOptionsForm[questionnaireOptionsList.size()];
        for(int i=0;i<optionsForms.length;i++){
            optionsForms[i] = new QuestionnaireOptionsForm();
            BeanUtil.copyProperties(questionnaireOptionsList.get(i),optionsForms[i]);
        }
        return optionsForms;
    }

    public QuestionnaireTemplateVo getTemplateDetailByActivityId(Integer activityId){
        ActivityVo activityVo = activityService.getActivityInfo(activityId);
        if(activityVo==null || activityVo.getQuestionnaireId()==null){
            throw new ServiceException(message("api.error.invalid.param"));
        }
        return getActivityQuestionnaireDetail(activityVo.getQuestionnaireId());
    }
}
