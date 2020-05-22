package com.worldelite.job.vo;

import lombok.Data;

import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class UserExpectJobVo {

    private List<JobCategoryVo> categoryList; //期望职位
    private List<DictVo> cityList; //期望城市
    private Integer minSalary; //最低薪资
    private Integer maxSalary; //最高薪资

}
