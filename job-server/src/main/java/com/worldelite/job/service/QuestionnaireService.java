package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import com.worldelite.job.entity.Questionnaire;
import com.worldelite.job.form.QuestionnaireForm;
import com.worldelite.job.form.QuestionnaireOptionsForm;
import com.worldelite.job.mapper.QuestionnaireMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.QuestionnaireOptionsVo;
import com.worldelite.job.vo.QuestionnaireVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 问卷服务类
 */
@Slf4j
@Service
public class QuestionnaireService extends BaseService{

    @Autowired
    private QuestionnaireMapper questionnaireMapper;

    @Autowired
    private QuestionnaireOptionsService questionnaireOptionsService;

    public void addQuestionnaire(QuestionnaireForm questionnaireForm){
        Questionnaire questionnaire = new Questionnaire();
        BeanUtil.copyProperties(questionnaireForm,questionnaire);
        //保存问卷信息
        questionnaireMapper.insertSelective(questionnaire);
        //遍历保存问卷选项
        QuestionnaireOptionsForm[] optionsForms = questionnaireForm.getOptionsList();
        if(optionsForms != null){
            for(int i=0;i<optionsForms.length;i++){
                QuestionnaireOptionsForm questionnaireOptionsForm = optionsForms[i];
                questionnaireOptionsForm.setQuestionnaireId(questionnaire.getId());
                questionnaireOptionsService.addQuestionnaireOptions(questionnaireOptionsForm);
            }
        }
    }

    public List<QuestionnaireVo> getQuestionnaireList(Integer templateId,Byte template){
        Questionnaire options = new Questionnaire();
        options.setRegistrationTemplateId(templateId);
        options.setTemplate(template);
        List<Questionnaire> questionnaireList = questionnaireMapper.selectAndList(options);
        //基本信息
        List<QuestionnaireVo> questionnaireVoList = AppUtils.asVoList(questionnaireList,QuestionnaireVo.class);
        //选项信息
        for(QuestionnaireVo questionnaireVo:questionnaireVoList){
            List<QuestionnaireOptionsVo> optionsList = questionnaireOptionsService
                    .getQuestionnaireOptionsList(questionnaireVo.getId());
            questionnaireVo.setOptionsList(optionsList);
        }
        return questionnaireVoList;
    }

    /**
     * 删除模板对应的问卷
     * @param templateId
     */
    public void deleteByTemplateId(Integer templateId){
        questionnaireMapper.deleteByTemplateId(templateId);
    }

}
