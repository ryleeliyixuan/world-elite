package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import com.worldelite.job.entity.Questionnaire;
import com.worldelite.job.entity.QuestionnaireTemplate;
import com.worldelite.job.form.QuestionnaireForm;
import com.worldelite.job.form.QuestionnaireTemplateForm;
import com.worldelite.job.mapper.QuestionnaireTemplateMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.QuestionnaireTemplateVo;
import com.worldelite.job.vo.QuestionnaireVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 问卷模板服务类
 */
@Service
@Slf4j
public class QuestionnaireTemplateService extends BaseService{

    @Autowired
    private QuestionnaireTemplateMapper questionnaireTemplateMapper;

    @Autowired
    private QuestionnaireService questionnaireService;

    /**
     * 添加报名表模板信息
     * @param questionnaireTemplateForm
     */
    @Transactional
    public void addQuestionnaireTemplate(QuestionnaireTemplateForm questionnaireTemplateForm){
        QuestionnaireTemplate template = new QuestionnaireTemplate();
        BeanUtil.copyProperties(questionnaireTemplateForm,template);
        //参数没有用户ID，则认为是当前用户添加模板
        if(questionnaireTemplateForm.getUserId()==null){
            template.setUserId(curUser().getId());
        }
        //保存模板信息
        questionnaireTemplateMapper.insertSelective(template);
        log.debug("保存模板{}",template.getId());
        //遍历保存问卷信息
        QuestionnaireForm[] questionnaireForms = questionnaireTemplateForm.getQuestionnaireList();
        if(questionnaireForms != null){
            for(int i=0;i<questionnaireForms.length;i++){
                QuestionnaireForm questionnaireForm = questionnaireForms[i];
                questionnaireForm.setRegistrationTemplateId(template.getId());
                questionnaireService.addQuestionnaire(questionnaireForm);
            }
        }
    }

    /**
     * 获取我的模板列表
     * 只获取模板基本信息
     * 不带关联的问卷信息
     * @return
     */
    public List<QuestionnaireTemplateVo> getMyQuestionnaireTemplateList(){
        QuestionnaireTemplate options = new QuestionnaireTemplate();
        options.setUserId(curUser().getId());
        List<QuestionnaireTemplate> templateList = questionnaireTemplateMapper.selectAndList(options);
        return AppUtils.asVoList(templateList,QuestionnaireTemplateVo.class);
    }

    /**
     * 根据ID获取报名表模板详情
     * @param id
     * @return
     */
    public QuestionnaireTemplateVo getQuestionnaireTemplate(Integer id){
        QuestionnaireTemplate template = questionnaireTemplateMapper.selectByPrimaryKey(id);
        return getTemplateDetail(template);
    }

    /**
     * 通过活动ID获取模板详情
     * @param activityId
     * @return
     */
    public QuestionnaireTemplateVo getTemplateDetailByActivityId(Integer activityId){
        QuestionnaireTemplate template = questionnaireTemplateMapper.selectByActivityId(activityId);
        return getTemplateDetail(template);
    }

    /**
     * 获取模板详情，包含问卷信息
     * @param questionnaireTemplate
     * @return
     */
    private QuestionnaireTemplateVo getTemplateDetail(QuestionnaireTemplate questionnaireTemplate){
        //获取基本信息
        QuestionnaireTemplateVo template = new QuestionnaireTemplateVo().asVo(questionnaireTemplate);
        //获取问卷列表
        List<QuestionnaireVo> questionnaireVoList = questionnaireService.getQuestionnaireList(template.getId());
        template.setQuestionnaireList(questionnaireVoList);
        return template;
    }
}
