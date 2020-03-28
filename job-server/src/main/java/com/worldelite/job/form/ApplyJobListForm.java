package com.worldelite.job.form;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yeguozhong yedaxia.github.com
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ApplyJobListForm extends PageForm{
    private Byte status;
    private Long userId;
    private Long resumeId;
}
