package com.worldelite.job.mapper;

import com.worldelite.job.entity.JobSkill;

public interface JobSkillMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_job_skill
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_job_skill
     *
     * @mbg.generated
     */
    int insert(JobSkill record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_job_skill
     *
     * @mbg.generated
     */
    int insertSelective(JobSkill record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_job_skill
     *
     * @mbg.generated
     */
    JobSkill selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_job_skill
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(JobSkill record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_job_skill
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(JobSkill record);
}