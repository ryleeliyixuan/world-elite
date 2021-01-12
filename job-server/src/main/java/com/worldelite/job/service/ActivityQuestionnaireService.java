package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import com.worldelite.job.entity.ActivityQuestionnaire;
import com.worldelite.job.form.ActivityQuestionnaireForm;
import com.worldelite.job.form.QuestionnaireForm;
import com.worldelite.job.mapper.ActivityQuestionnaireMapper;
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
    private QuestionnaireService questionnaireService;

    /**
     * 添加报名表
     * @param activityQuestionnaireForm
     */
    @Transactional
    public void addActivityQuestionnaire(ActivityQuestionnaireForm activityQuestionnaireForm){
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
                questionnaireForm.setRegistrationTemplateId(questionnaire.getId());
                questionnaireService.addQuestionnaire(questionnaireForm);
            }
        }
    }

    /**
     * 根据ID获取报名表模板详情
     * @param id
     * @return
     */
    public QuestionnaireTemplateVo getActivityQuestionnaire(Integer id){
        ActivityQuestionnaire activityQuestionnaire = activityQuestionnaireMapper.selectByPrimaryKey(id);
        return getActivityQuestionnaireDetail(activityQuestionnaire);
    }

    /**
     * 通过活动ID获取模板详情
     * @param activityId
     * @return
     */
    public QuestionnaireTemplateVo getActivityQuestionnaireByActivityId(Integer activityId){
        ActivityQuestionnaire activityQuestionnaire = activityQuestionnaireMapper.selectByActivityId(activityId);
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
        List<QuestionnaireVo> questionnaireVoList = questionnaireService.getQuestionnaireList(template.getId());
        template.setQuestionnaireList(questionnaireVoList);
        return template;
    }
}
