package com.worldelite.job.vo;

import lombok.Data;

import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class UserExpectJobVo {

    private List<JobCategoryVo> categoryList; //期望职位
    private List<CityVo> cityList; //期望城市
    private DictVo salary; //薪资范围
    private String expectWorkType; // 工作类型
}
