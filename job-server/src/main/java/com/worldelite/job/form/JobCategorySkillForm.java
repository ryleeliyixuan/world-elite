package com.worldelite.job.form;

import lombok.Data;

@Data
public class JobCategorySkillForm {
    private Long id; //职位类别技能关系表id
    private Long categoryId; //职位类别Id
    private Long skillId;   //职位技能Id
}
