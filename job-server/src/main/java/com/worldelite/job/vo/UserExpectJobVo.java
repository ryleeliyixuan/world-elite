package com.worldelite.job.vo;

import lombok.Data;

import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class UserExpectJobVo {

    private List<JobCategoryVo> categoryList;
    private List<DictVo> cityList;
    private Integer minSalary;
    private Integer maxSalary;

}
