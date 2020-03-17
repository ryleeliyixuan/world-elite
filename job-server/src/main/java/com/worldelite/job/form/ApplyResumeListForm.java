package com.worldelite.job.form;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yeguozhong yedaxia.github.com
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ApplyResumeListForm extends PageForm{
    private Long creatorId; //职位发布者
    private String name; //用户简历名称
    private Byte[] statuses; //投递状态
    private Long[] jobIds; //职位
    private Integer[] degreeIds; //学历
}
