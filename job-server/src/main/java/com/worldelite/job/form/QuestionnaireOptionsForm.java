package com.worldelite.job.form;

import lombok.Data;

/**
 * 报名表问题选项表单
 */
@Data
public class QuestionnaireOptionsForm {
    private Integer questionnaireId; //问卷ID
    private String options; //选项标签
    private Integer sort; //排序
}
