package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import com.worldelite.job.entity.Activity;
import com.worldelite.job.entity.ActivityQuestionnaire;
import com.worldelite.job.entity.Questionnaire;
import com.worldelite.job.entity.QuestionnaireTemplate;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.AddTemplateFromQuestionnaireForm;
import com.worldelite.job.form.QuestionnaireForm;
import com.worldelite.job.form.QuestionnaireOptionsForm;
import com.worldelite.job.form.QuestionnaireTemplateForm;
import com.worldelite.job.mapper.QuestionnaireTemplateMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.rowset.serial.SerialException;
import javax.swing.*;
import java.util.List;

/**
 * 问卷模板服务类
 */
@Service
@Slf4j
public class QuestionnaireTemplateService extends BaseService{

    @Value("${questionnaire.template.max.count}")
    private Integer maxCount;

    @Autowired
    private QuestionnaireTemplateMapper questionnaireTemplateMapper;

    @Autowired
    private QuestionnaireService questionnaireService;

    @Autowired
    private ActivityQuestionnaireService activityQuestionnaireService;

    @Autowired
    private ActivityService activityService;

    /**
     * 添加报名表模板信息
     * @param questionnaireTemplateForm
     */
    @Transactional
    public QuestionnaireTemplateVo addQuestionnaireTemplate(QuestionnaireTemplateForm questionnaireTemplateForm){
        Long userId = curUser().getId();
        //判断当前用户是否还能添加模板
        checkTemplateCount(userId);
        QuestionnaireTemplate template = new QuestionnaireTemplate();
        BeanUtil.copyProperties(questionnaireTemplateForm,template);
        template.setUserId(userId);
        template.setTemplateName(getRepeatTemplateName(userId,template.getTemplateName()));
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
        return new QuestionnaireTemplateVo().asVo(template);
    }

    /**
     * 修改模板
     * @param id 模板ID
     * @param questionnaireTemplateForm 模板表单
     */
    @Transactional
    public QuestionnaireTemplateVo updateQuestionnaireTemplate(Integer id,QuestionnaireTemplateForm questionnaireTemplateForm){
        QuestionnaireTemplate template = questionnaireTemplateMapper.selectByPrimaryKey(id);
        if(template == null){
            throw new ServiceException(message("questionnaire.template.not.exists"));
        }
        if(template.getUserId() == null || !template.getUserId().equals(curUser().getId())){
            throw new ServiceException(message("questionnaire.template.error.owner"));
        }
        //如果名称有变化，要检查是不是名称是不是有重叠
        if(StringUtils.isNotEmpty(template.getTemplateName())
                && !template.getTemplateName().equals(questionnaireTemplateForm.getTemplateName())) {
            questionnaireTemplateForm.setTemplateName(getRepeatTemplateName(template.getUserId(),
                    questionnaireTemplateForm.getTemplateName()));
        }
        BeanUtil.copyProperties(questionnaireTemplateForm,template);
        //保存模板信息
        log.debug("修改模板{}",template.getId());
        questionnaireTemplateMapper.updateByPrimaryKeySelective(template);
        //删除问卷信息
        questionnaireService.deleteByTemplateId(template.getId());
        //遍历保存问卷信息
        QuestionnaireForm[] questionnaireForms = questionnaireTemplateForm.getQuestionnaireList();
        if(questionnaireForms != null){
            for(int i=0;i<questionnaireForms.length;i++){
                QuestionnaireForm questionnaireForm = questionnaireForms[i];
                questionnaireForm.setRegistrationTemplateId(template.getId());
                questionnaireService.addQuestionnaire(questionnaireForm);
            }
        }
        return new QuestionnaireTemplateVo().asVo(template);
    }

    /**
     * 删除模板
     * @param id 模板ID
     */
    @Transactional
    public void deleteQuestionnaireTemplate(Integer id){
        QuestionnaireTemplate template = questionnaireTemplateMapper.selectByPrimaryKey(id);
        if(template == null){
            throw new ServiceException(message("questionnaire.template.not.exists"));
        }
        if(template.getUserId() == null || !template.getUserId().equals(curUser().getId())){
            throw new ServiceException(message("questionnaire.template.error.owner"));
        }
        //删除模板
        questionnaireTemplateMapper.deleteByPrimaryKey(id);
        //删除问卷
        questionnaireService.deleteByTemplateId(template.getId());
        //删除报名表中与模板的关联
        activityQuestionnaireService.removeTemplate(template.getId());
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
     * 获取我的模板数量
     * @return
     */
    public TemplateCountVo getMyTemplateCount(){
        Integer count = questionnaireTemplateMapper.selectCountByUserId(curUser().getId());
        TemplateCountVo countVo = new TemplateCountVo();
        countVo.setCount(count);
        countVo.setMaxCount(maxCount);
        return countVo;
    }

    /**
     * 根据ID获取报名表模板详情
     * @param id
     * @return
     */
    public QuestionnaireTemplateVo getQuestionnaireTemplate(Integer id){
        QuestionnaireTemplate template = questionnaireTemplateMapper.selectByPrimaryKey(id);
        if(template == null){
            throw new ServiceException(message("questionnaire.template.not.exists"));
        }
        return getTemplateDetail(template);
    }

    /**
     * 获取模板详情，包含问卷信息
     * 模板不能为null，否则会抛出空指针异常
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

    /**
     * 检查指定用户模板是否还能添加一个模板
     * @param userId
     */
    private void checkTemplateCount(Long userId){
        Integer templateCount = questionnaireTemplateMapper.selectCountByUserId(userId);
        if(templateCount+1 > maxCount){
            throw new ServiceException(message("questionnaire.template.max.count"));
        }
    }

    /**
     * 如果模板名重复，在后面加（N）
     * N为重复的数量
     * @param userId
     * @param templateName
     */
    private String getRepeatTemplateName(Long userId,String templateName){
        QuestionnaireTemplate options = new QuestionnaireTemplate();
        options.setUserId(userId);
        options.setTemplateName(templateName);
        List<QuestionnaireTemplate> templateList = questionnaireTemplateMapper.selectByTemplateName(options);
        if(CollectionUtils.isNotEmpty(templateList)){
            for(int i=1;i<=templateList.size();i++){
                if(!containsTemplateName(templateList,templateName+"（"+i+"）")){
                    return templateName+"（"+i+"）";
                }
            }
        }
        return templateName;
    }

    /**
     * 判断模板列表是否包含模板名
     * @param templateList
     * @param templateName
     * @return
     */
    private boolean containsTemplateName(List<QuestionnaireTemplate> templateList,String templateName){
        if(StringUtils.isEmpty(templateName)){
            return false;
        }
        for(QuestionnaireTemplate template:templateList){
            if(templateName.equals(template.getTemplateName())){
                return true;
            }
        }
        return false;
    }
}
