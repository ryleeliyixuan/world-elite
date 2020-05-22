package com.worldelite.job.form;

import lombok.Data;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class ResumeSkillForm {
    private Long resumeId; //简历ID
    private String[] tagList; //技能标签
}
