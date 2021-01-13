package com.worldelite.job.vo;

import lombok.Data;

import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class UserExpectJobVo {

    public void setCategory(JobCategoryVo category) {
        this.category = category;
    }

    public void setExpectCity(String expectCity) {
        this.expectCity = expectCity;
    }

    public void setSalaryId(Byte salaryId) {
        this.salaryId = salaryId;
    }

    public void setExpectWorkType(String expectWorkType) {
        this.expectWorkType = expectWorkType;
    }

    public void setExpectPosition(String expectPosition) {
        this.expectPosition = expectPosition;
    }

    private JobCategoryVo category; //期望职位
    private String expectCity; //期望城市
    private Byte salaryId; //薪资范围
    private String expectWorkType; // 工作类型
    private String expectPosition;

    private List<JobCategoryVo> categoryList;
    private List<CityVo> cityList;

}
