package com.worldelite.job.mapper;

import com.worldelite.job.entity.JobCategorySkill;

import java.util.List;

public interface JobCategorySkillMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_job_category_skill
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_job_category_skill
     *
     * @mbg.generated
     */
    int insert(JobCategorySkill record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_job_category_skill
     *
     * @mbg.generated
     */
    int insertSelective(JobCategorySkill record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_job_category_skill
     *
     * @mbg.generated
     */
    JobCategorySkill selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_job_category_skill
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(JobCategorySkill record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_job_category_skill
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(JobCategorySkill record);

    List<JobCategorySkill> selectByJobCategoryId(Long jobCategoryId);
}