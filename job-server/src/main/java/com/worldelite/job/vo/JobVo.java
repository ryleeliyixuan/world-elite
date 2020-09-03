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

    private String id; //职位ID
    private DictVo jobType; //职位类型
    private String name; //职位名称
    private CompanyUserVo companyUser; //企业用户
    private JobCategoryVo category; //职位类型
    private String depart; //部门
    private DictVo minDegree; // 学历要求
    private Integer minSalary; //最低薪资
    private Integer maxSalary;//最高薪资
    private Integer salaryMonths; //发放月数
    private DictVo city; //工作城市
    private Byte status; //职位状态
    private String description; //职位描述
    private Long creatorId; //创建人
    private String address; //工作地址
    @JSONField(format = "yyyy-MM-dd")
    private Date time; //发布时间

    private Byte favoriteFlag; //是否已收藏
    @JSONField(format = "yyyy-MM-dd")
    private Date favoriteTime; //收藏时间
    private Byte applyFlag; //是否已投递

    private Byte applyStatus; //投递状态
    @JSONField(format = "yyyy-MM-dd")
    private Date applyTime; //投递时间

    private Integer totalResumeCount; //投递人数
    private Integer newResumeCount; // 新投递简历数
    private Integer candidateResumeCount; // 候选简历数
    private Integer interviewResumeCount; //进入面试简历数

    private Integer experience; //工作经验
    private String industryTags; //行业领域
    private String skillTags; //技能标签

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
        setAddress(job.getAddress());
        setExperience(job.getExperience());
        setIndustryTags(job.getIndustryTags());
        setSkillTags(job.getSkillTags());
        return this;
    }
}
