package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import com.worldelite.job.entity.QuestionnaireAnswer;
import com.worldelite.job.form.QuestionnaireAnswerForm;
import com.worldelite.job.mapper.QuestionnaireAnswerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 问卷回答服务类
 */
@Service
public class QuestionnaireAnswerService extends BaseService{

    @Autowired
    private QuestionnaireAnswerMapper questionnaireAnswerMapper;

    public void addQuestionnaireAnswer(QuestionnaireAnswerForm questionnaireAnswerForm){
        QuestionnaireAnswer questionnaireAnswer = new QuestionnaireAnswer();
        BeanUtil.copyProperties(questionnaireAnswerForm,questionnaireAnswer);
        questionnaireAnswerMapper.insertSelective(questionnaireAnswer);
    }

}
