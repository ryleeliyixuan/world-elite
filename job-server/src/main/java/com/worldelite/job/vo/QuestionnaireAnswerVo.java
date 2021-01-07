package com.worldelite.job.vo;

import cn.hutool.core.bean.BeanUtil;
import com.worldelite.job.entity.QuestionnaireAnswer;
import lombok.Data;

/**
 * 问卷回答视图
 */
@Data
public class QuestionnaireAnswerVo implements VoConvertable<QuestionnaireAnswerVo, QuestionnaireAnswer>{

    private Integer id; //回答ID
    private Integer questionnaireId; //问卷ID
    private String answerContent; //回答内容
    private QuestionnaireOptionsVo answerOptions; //选项

    @Override
    public QuestionnaireAnswerVo asVo(QuestionnaireAnswer questionnaireAnswer) {
        BeanUtil.copyProperties(questionnaireAnswer,this);
        return this;
    }
}
