package com.worldelite.job.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class UserExpectJobForm {
    private Integer[] cityIds; //意向城市
    private Integer[] categoryIds; //意向职位
    private Integer minSalary; //最低薪资
    private Integer maxSalary; //最高薪资
    private Long userId; //用户ID
}
