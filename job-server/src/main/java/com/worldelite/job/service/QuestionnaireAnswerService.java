package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import com.worldelite.job.entity.QuestionnaireAnswer;
import com.worldelite.job.form.QuestionnaireAnswerForm;
import com.worldelite.job.mapper.QuestionnaireAnswerMapper;
import com.worldelite.job.vo.QuestionnaireAnswerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 问卷回答服务类
 */
@Service
public class QuestionnaireAnswerService extends BaseService{

    @Autowired
    private QuestionnaireAnswerMapper questionnaireAnswerMapper;

    @Autowired
    private QuestionnaireOptionsService questionnaireOptionsService;

    public void addQuestionnaireAnswer(QuestionnaireAnswerForm questionnaireAnswerForm){
        int maxLength = 0;
        int contentListLength = 0;
        int optionsIdListLength = 0;
        boolean hasContentList = false;
        boolean hasOptionsIdList = false;
        if(questionnaireAnswerForm.getAnswerContentList() != null){
            hasContentList = true;
            contentListLength = questionnaireAnswerForm.getAnswerContentList().length;
            if(contentListLength > maxLength){
                maxLength = questionnaireAnswerForm.getAnswerContentList().length;
            }
        }
        if(questionnaireAnswerForm.getAnswerOptionsIdList() != null){
            hasOptionsIdList = true;
            optionsIdListLength = questionnaireAnswerForm.getAnswerOptionsIdList().length;
            if(optionsIdListLength > maxLength){
                maxLength = questionnaireAnswerForm.getAnswerOptionsIdList().length;
            }
        }
        for(int i=0;i<maxLength;i++){
            QuestionnaireAnswer questionnaireAnswer = new QuestionnaireAnswer();
            questionnaireAnswer.setRegistrationId(questionnaireAnswerForm.getRegistrationId());
            questionnaireAnswer.setQuestionnaireId(questionnaireAnswerForm.getQuestionnaireId());
            if(hasContentList && i<contentListLength){
                questionnaireAnswer.setAnswerContent(questionnaireAnswerForm.getAnswerContentList()[i]);
            }
            if(hasOptionsIdList && i<optionsIdListLength){
                questionnaireAnswer.setAnswerOptionsId(questionnaireAnswerForm.getAnswerOptionsIdList()[i]);
            }
            questionnaireAnswerMapper.insertSelective(questionnaireAnswer);
        }
    }

    public List<QuestionnaireAnswerVo> getAnswerListByRegistrationId(Integer registrationId){
        QuestionnaireAnswer options = new QuestionnaireAnswer();
        options.setRegistrationId(registrationId);
        List<QuestionnaireAnswer> answerList = questionnaireAnswerMapper.selectAndList(options);
        List<QuestionnaireAnswerVo> answerVoList = new ArrayList<>(answerList.size());
        for(QuestionnaireAnswer answer:answerList){
            QuestionnaireAnswerVo answerVo = new QuestionnaireAnswerVo().asVo(answer);
            answerVo.setAnswerOptions(questionnaireOptionsService.getOptionsById(answer.getAnswerOptionsId()));
            answerVoList.add(answerVo);
        }
        return answerVoList;
    }
}
