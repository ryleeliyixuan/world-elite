package com.worldelite.job.form;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yeguozhong yedaxia.github.com
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ApplyJobListForm extends PageForm{
    private Byte status; //状态
    private Long userId; //用户ID
    private Long resumeId; //简历ID
}
