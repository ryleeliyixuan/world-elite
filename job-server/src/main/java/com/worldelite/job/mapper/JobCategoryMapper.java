package com.worldelite.job.mapper;

import com.worldelite.job.entity.JobCategory;

import java.util.List;

public interface JobCategoryMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_job_category
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_job_category
     *
     * @mbg.generated
     */
    int insert(JobCategory record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_job_category
     *
     * @mbg.generated
     */
    int insertSelective(JobCategory record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_job_category
     *
     * @mbg.generated
     */
    JobCategory selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_job_category
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(JobCategory record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_job_category
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(JobCategory record);

    List<JobCategory> selectAndList(JobCategory options);
}