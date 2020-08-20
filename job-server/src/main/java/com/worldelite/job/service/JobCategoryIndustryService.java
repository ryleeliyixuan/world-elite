package com.worldelite.job.service;

import com.worldelite.job.entity.JobCategoryIndustry;
import com.worldelite.job.entity.JobIndustry;
import com.worldelite.job.form.JobCategoryIndustryForm;
import com.worldelite.job.form.JobIndustryForm;
import com.worldelite.job.mapper.*;
import com.worldelite.job.vo.JobIndustryVo;
import lombok.NonNull;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author 熊文剑
 */
@Service
public class JobCategoryIndustryService {
    @Autowired
    JobCategoryIndustryMapper jobCategoryIndustryMapper;

    @Autowired
    JobIndustryMapper jobIndustryMapper;

    /**
     * 根据职位类型获得相关行业领域
     * @param jobCategoryId
     * @return List<JobIndustryVo>
     */
    public List<JobIndustryVo> getJobIndustryByJobCategoryId(final @NonNull long jobCategoryId) {
        final List<JobCategoryIndustry> jobCategoryIndustries = jobCategoryIndustryMapper.selectByJobCategoryId(jobCategoryId);

        if (Objects.nonNull(jobCategoryIndustries)) {
            return jobCategoryIndustries.stream()
                    .map(jobCategoryIndustry -> jobIndustryMapper.selectByPrimaryKey(jobCategoryIndustry.getIndustryId()))
                    .map(jobIndustry -> new JobIndustryVo().asVo(jobIndustry))
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    /**
     * 保存行业领域
     * @param jobIndustryForm
     */
    public void saveJobIndustry(final @NonNull JobIndustryForm jobIndustryForm) {
        if (StringUtils.isNotEmpty(jobIndustryForm.getName())) {
            final JobIndustry jobIndustry = new JobIndustry();
            jobIndustry.setName(jobIndustryForm.getName());
            jobIndustryMapper.insertSelective(jobIndustry);
        }
    }

    /**
     * 删除行业领域
     * @param jobIndustryId
     */
    public void deleteJobIndustryById(final @NonNull Long jobIndustryId) {
        jobIndustryMapper.deleteByPrimaryKey(jobIndustryId);
    }

    /**
     * 保存职位行业和职位类型映射
     * @param jobCategoryIndustryForm
     */
    public void saveJobCategoryIndustry (final @NonNull JobCategoryIndustryForm jobCategoryIndustryForm) {
        final JobCategoryIndustry jobCategoryIndustry = new JobCategoryIndustry();
        jobCategoryIndustry.setIndustryId(jobCategoryIndustryForm.getIndustryId());
        jobCategoryIndustry.setCategoryId(jobCategoryIndustryForm.getCategoryId());

        jobCategoryIndustryMapper.insertSelective(jobCategoryIndustry);
    }

    /**
     *  删除职位行业和职位类型映射
     * @param jobCategoryIndustryId
     */
    public void deleteJobCategoryIndustryById(final long jobCategoryIndustryId) {
        jobCategoryIndustryMapper.deleteByPrimaryKey(jobCategoryIndustryId);
    }
}
