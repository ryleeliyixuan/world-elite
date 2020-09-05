package com.worldelite.job.mapper;

import com.worldelite.job.entity.Job;
import com.worldelite.job.entity.JobOptions;

import java.util.List;

public interface JobMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_job
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_job
     *
     * @mbg.generated
     */
    int insert(Job record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_job
     *
     * @mbg.generated
     */
    int insertSelective(Job record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_job
     *
     * @mbg.generated
     */
    Job selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_job
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(Job record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_job
     *
     * @mbg.generated
     */
    int updateByPrimaryKeyWithBLOBs(Job record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_job
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(Job record);

    List<Job> selectAndList(JobOptions options);

    Job selectSimpleById(Long id);

    /**
     * 智能搜索职位名称
     * @param name
     * @return
     */
    List<Job> selectSmartTips(String name);
}