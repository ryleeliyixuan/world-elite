package com.worldelite.job.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.worldelite.job.entity.Job;
import lombok.Data;

import java.util.Date;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class JobVo implements VoConvertable<JobVo, Job>{

    private String id;
    private DictVo jobType;
    private String name;
    private CompanyUserVo companyUser;
    private JobCategoryVo category;
    private String depart;
    private DictVo minDegree;
    private Integer minSalary;
    private Integer maxSalary;
    private Integer salaryMonths;
    private DictVo city;
    private Byte status;
    private String description;
    private CompanyAddressVo address;
    @JSONField(format = "yyyy-MM-dd")
    private Date time;

    private Byte favoriteFlag;
    private Byte applyFlag;

    @Override
    public JobVo asVo(Job job) {
        setId(String.valueOf(job.getId()));
        setName(job.getName());
        setDepart(job.getDepart());
        setMinSalary(job.getMinSalary());
        setMaxSalary(job.getMaxSalary());
        setSalaryMonths(job.getSalaryMonths());
        setStatus(job.getStatus());
        setDescription(job.getDescription());
        setTime(job.getCreateTime());
        return this;
    }
}
