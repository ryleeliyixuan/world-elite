package com.worldelite.job.vo;

import com.worldelite.job.entity.QuestionnaireAnswer;
import lombok.Data;

/**
 * 问卷回答视图
 */
@Data
public class QuestionnaireAnswerVo implements VoConvertable<QuestionnaireAnswerVo, QuestionnaireAnswer>{

    private String answerContent; //回答内容
    private QuestionnaireOptionsVo answerOptions; //选项

    @Override
    public QuestionnaireAnswerVo asVo(QuestionnaireAnswer questionnaireAnswer) {
        setAnswerContent(questionnaireAnswer.getAnswerContent());
        return this;
    }
}
