package com.worldelite.job.form;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yeguozhong yedaxia.github.com
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ApplyResumeListForm extends PageForm{
    private String name; //用户简历名称
    private Byte applyStatus; //投递状态
    private Long[] jobIds;
}
