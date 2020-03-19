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
    private Long creatorId;
    private CompanyAddressVo address;
    @JSONField(format = "yyyy-MM-dd")
    private Date time;

    private Byte favoriteFlag; //是否已收藏
    private Byte applyFlag; //是否已投递

    private Byte applyStatus; //投递状态
    @JSONField(format = "yyyy-MM-dd")
    private Date applyTime; //投递时间

    private Integer totalResumeCount; //投递人数
    private Integer newResumeCount; // 新投递简历数
    private Integer candidateResumeCount; // 候选简历数
    private Integer interviewResumeCount; //进入面试简历数

    @Override
    public JobVo asVo(Job job) {
        setId(String.valueOf(job.getId()));
        setName(job.getName());
        setDepart(job.getDepart());
        setCreatorId(job.getCreatorId());
        setMinSalary(job.getMinSalary());
        setMaxSalary(job.getMaxSalary());
        setSalaryMonths(job.getSalaryMonths());
        setStatus(job.getStatus());
        setDescription(job.getDescription());
        setTime(job.getPubTime());
        return this;
    }
}
