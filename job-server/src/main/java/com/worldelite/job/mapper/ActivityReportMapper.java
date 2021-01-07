package com.worldelite.job.mapper;

import com.worldelite.job.entity.ActivityReport;

import java.util.List;

public interface ActivityReportMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_activity_report
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_activity_report
     *
     * @mbg.generated
     */
    int insert(ActivityReport record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_activity_report
     *
     * @mbg.generated
     */
    int insertSelective(ActivityReport record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_activity_report
     *
     * @mbg.generated
     */
    ActivityReport selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_activity_report
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(ActivityReport record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_activity_report
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(ActivityReport record);

    List<ActivityReport> selectAndList(ActivityReport record);
}