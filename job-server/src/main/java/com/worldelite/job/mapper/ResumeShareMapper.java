package com.worldelite.job.mapper;

import com.worldelite.job.entity.ResumeShare;

import java.util.List;

public interface ResumeShareMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_resume_share
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_resume_share
     *
     * @mbg.generated
     */
    int insert(ResumeShare record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_resume_share
     *
     * @mbg.generated
     */
    int insertSelective(ResumeShare record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_resume_share
     *
     * @mbg.generated
     */
    ResumeShare selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_resume_share
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(ResumeShare record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_resume_share
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(ResumeShare record);

    List<ResumeShare> selectAndList(ResumeShare resumeShare);
}