package com.worldelite.job.form;

import lombok.Data;

/**
 * 问卷回答表单
 */
@Data
public class QuestionnaireAnswerForm {

    private Integer questionnaireId; //问卷ID
    private String answerContent; //回答内容
    private Integer answerOptionsId; //选项ID

}
