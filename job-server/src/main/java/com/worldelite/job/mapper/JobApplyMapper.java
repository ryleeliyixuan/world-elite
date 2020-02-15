package com.worldelite.job.mapper;

import com.worldelite.job.entity.JobApply;

import java.util.List;

public interface JobApplyMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_job_apply
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_job_apply
     *
     * @mbg.generated
     */
    int insert(JobApply record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_job_apply
     *
     * @mbg.generated
     */
    int insertSelective(JobApply record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_job_apply
     *
     * @mbg.generated
     */
    JobApply selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_job_apply
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(JobApply record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_job_apply
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(JobApply record);

    List<JobApply> selectAndList(JobApply options);
}