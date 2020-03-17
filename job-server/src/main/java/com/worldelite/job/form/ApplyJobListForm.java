package com.worldelite.job.form;

import lombok.Data;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class ApplyJobListForm extends PageForm{
    private Byte status;
    private Long userId;
    private Long resumeId;
}
