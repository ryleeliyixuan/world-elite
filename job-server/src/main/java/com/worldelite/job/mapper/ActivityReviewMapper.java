package com.worldelite.job.mapper;

import com.worldelite.job.entity.ActivityReview;

import java.util.List;

public interface ActivityReviewMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_activity_review
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_activity_review
     *
     * @mbg.generated
     */
    int insert(ActivityReview record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_activity_review
     *
     * @mbg.generated
     */
    int insertSelective(ActivityReview record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_activity_review
     *
     * @mbg.generated
     */
    ActivityReview selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_activity_review
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(ActivityReview record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_activity_review
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(ActivityReview record);

    List<ActivityReview> selectAndList(ActivityReview options);

    int deleteByActivityId(Integer activityId);

    List<ActivityReview> selectByActivityId(Integer activityId);
}