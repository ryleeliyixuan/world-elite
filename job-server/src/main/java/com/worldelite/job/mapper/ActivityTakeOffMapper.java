package com.worldelite.job.mapper;

import com.worldelite.job.entity.ActivityTakeOff;

public interface ActivityTakeOffMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_activity_takeoff
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer activityId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_activity_takeoff
     *
     * @mbg.generated
     */
    int insert(ActivityTakeOff record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_activity_takeoff
     *
     * @mbg.generated
     */
    int insertSelective(ActivityTakeOff record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_activity_takeoff
     *
     * @mbg.generated
     */
    ActivityTakeOff selectByPrimaryKey(Integer activityId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_activity_takeoff
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(ActivityTakeOff record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_activity_takeoff
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(ActivityTakeOff record);
}