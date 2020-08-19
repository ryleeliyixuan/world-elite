package com.worldelite.job.service;

import com.worldelite.job.entity.JobCategorySkill;
import com.worldelite.job.entity.JobSkill;
import com.worldelite.job.form.JobCategorySkillForm;
import com.worldelite.job.form.JobSkillForm;
import com.worldelite.job.mapper.JobCategorySkillMapper;
import com.worldelite.job.mapper.JobSkillMapper;
import com.worldelite.job.vo.JobSkillVo;
import lombok.NonNull;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author 王星瀚
 */
@Service
public class JobCategorySkillService {

    @Autowired
    private JobCategorySkillMapper jobCategorySkillMapper;
    @Autowired
    private JobSkillMapper jobSkillMapper;

    /**
     * 根据工作种类获得相关职位技能
     * @param jobCategoryId
     * @return List<JobSkillVo>
     */
    public List<JobSkillVo> getJobSkillByJobCategoryId(final @NonNull long jobCategoryId) {
        final List<JobCategorySkill> jobCategorySkills = jobCategorySkillMapper.selectByJobCategoryId(jobCategoryId);

        if (Objects.nonNull(jobCategorySkills)) {
            return jobCategorySkills.stream()
                    .map(jobCategorySkill -> jobSkillMapper.selectByPrimaryKey(jobCategorySkill.getSkillId()))
                    .map(jobSkill -> new JobSkillVo().asVo(jobSkill))
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    /**
     * 保存职位技能
     * @param jobSkillForm
     */
    public void saveJobSkill(final @NonNull JobSkillForm jobSkillForm) {
        if (StringUtils.isNotEmpty(jobSkillForm.getName())) {
            final JobSkill jobSkill = new JobSkill();
            jobSkill.setName(jobSkillForm.getName());
            jobSkillMapper.insertSelective(jobSkill);
        }
    }

    /**
     * 删除职位技能
     * @param jobSkillId
     */
    public void deleteJobSkillById(final @NonNull Long jobSkillId) {
        jobSkillMapper.deleteByPrimaryKey(jobSkillId);
    }

    /**
     * 保存职位技能和工作种类映射
     * @param jobCategorySkillForm
     */
    public void saveJobCategorySkill (final @NonNull JobCategorySkillForm jobCategorySkillForm) {
        final JobCategorySkill jobCategorySkill = new JobCategorySkill();
        jobCategorySkill.setSkillId(jobCategorySkillForm.getSkillId());
        jobCategorySkill.setCategoryId(jobCategorySkillForm.getCategoryId());

        jobCategorySkillMapper.insertSelective(jobCategorySkill);
    }

    /**
     *  删除职位技能和工作种类映射
     * @param jobCategorySkillId
     */
    public void deleteJobCategorySkillById(final long jobCategorySkillId) {
        jobCategorySkillMapper.deleteByPrimaryKey(jobCategorySkillId);
    }
}
