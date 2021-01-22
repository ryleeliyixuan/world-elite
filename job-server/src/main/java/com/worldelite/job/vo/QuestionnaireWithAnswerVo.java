package com.worldelite.job.vo;

import lombok.Data;

import java.util.List;

/**
 * 问卷和回答视图
 */
@Data
public class QuestionnaireWithAnswerVo {
    private String title; //问题
    private String mustAnswer; //是否必答
    private Integer sort; //序号
    private String type; //问题类型
    private List<String> answerContent; //答案列表
    private List<QuestionnaireOptionsVo> answerOptions; //选择列表
    private List<QuestionnaireOptionsVo> questionnaireOptions; //选项列表
}
