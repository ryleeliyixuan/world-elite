package com.worldelite.job.form;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yeguozhong yedaxia.github.com
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SchoolListForm extends PageForm{
    private String name; //学校
    private Integer[] schoolIds; //学校ID列表
}
