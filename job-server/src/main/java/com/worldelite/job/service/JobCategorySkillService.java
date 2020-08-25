package com.worldelite.job.service;

import com.worldelite.job.entity.JobCategorySkill;
import com.worldelite.job.entity.JobSkill;
import com.worldelite.job.form.JobCategorySkillForm;
import com.worldelite.job.form.JobSkillForm;
import com.worldelite.job.mapper.JobCategorySkillMapper;
import com.worldelite.job.mapper.JobSkillMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.JobSkillVo;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
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
     * 获取全部职位技能
     *
     * @return
     */
    public List<JobSkillVo> getJobSkillList() {
        final List<JobSkill> jobSkills = jobSkillMapper.selectJobSkillList();
        if (Objects.nonNull(jobSkills)) {
            return jobSkills.stream()
                    .map(jobSkill -> new JobSkillVo().asVo(jobSkill))
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    /**
     * 根据工作种类获得相关职位技能
     *
     * @param jobCategoryId
     * @return List<JobSkillVo>
     */
    public List<JobSkillVo> getJobSkillByJobCategoryId(final @NonNull long jobCategoryId) {
        final List<JobSkill> jobSkills = jobSkillMapper.selectByJobCategoryId(jobCategoryId);

        if (Objects.nonNull(jobSkills)) {
            return jobSkills.stream()
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
        JobSkill jobSkill;
        if (jobSkillForm.getId() == null || (jobSkill = jobSkillMapper.selectByPrimaryKey(jobSkillForm.getId())) == null) {
            jobSkill = new JobSkill();
            jobSkill.setName(jobSkillForm.getName());
            jobSkillMapper.insertSelective(jobSkill);
        } else {
            jobSkill.setName(jobSkillForm.getName());
            jobSkill.setUpdateTime(new Date());
            jobSkillMapper.updateByPrimaryKey(jobSkill);
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

        JobCategorySkill jobCategorySkill;
        if (jobCategorySkillForm.getId() == null || (jobCategorySkill = jobCategorySkillMapper.selectByPrimaryKey(jobCategorySkillForm.getId())) == null) {
            jobCategorySkill = new JobCategorySkill();
            jobCategorySkill.setSkillId(jobCategorySkillForm.getSkillId());
            jobCategorySkill.setCategoryId(jobCategorySkillForm.getCategoryId());
            jobCategorySkillMapper.insertSelective(jobCategorySkill);
        } else {
            jobCategorySkill.setSkillId(jobCategorySkillForm.getSkillId());
            jobCategorySkill.setCategoryId(jobCategorySkillForm.getCategoryId());
            jobCategorySkillMapper.updateByPrimaryKey(jobCategorySkill);
        }
    }

    /**
     *  删除职位技能和工作种类映射
     * @param jobCategorySkillId
     */
    public void deleteJobCategorySkillById(final long jobCategorySkillId) {
        jobCategorySkillMapper.deleteByPrimaryKey(jobCategorySkillId);
    }


}
