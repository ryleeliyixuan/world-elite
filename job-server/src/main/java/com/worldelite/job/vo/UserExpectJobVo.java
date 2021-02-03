package com.worldelite.job.vo;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class UserExpectJobVo {
    private JobCategoryVo category; //期望职位
    private String expectCity; //期望城市
    private String[] salaryId; //薪资范围
    private String expectWorkType; // 工作类型
    private String expectPosition;
    private String industry;

    private List<JobCategoryVo> categoryList;
    private List<CityVo> cityList;

    public void setSalaryId(String salaryId) {
        if (StringUtils.isNoneBlank(salaryId))
            this.salaryId = salaryId.split(",");
    }
}
