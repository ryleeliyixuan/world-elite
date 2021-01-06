package com.worldelite.job.form;

import lombok.Data;

/**
 * 问卷回答表单
 */
@Data
public class QuestionnaireAnswerForm {

    private Integer registrationId; //报名ID
    private Integer questionnaireId; //问卷ID
    private String[] answerContentList; //回答内容
    private Integer[] answerOptionsIdList; //选项ID

}
