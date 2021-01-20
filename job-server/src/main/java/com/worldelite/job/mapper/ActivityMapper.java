package com.worldelite.job.mapper;

import com.worldelite.job.entity.Activity;
import com.worldelite.job.entity.ActivityOptions;

import java.util.List;

public interface ActivityMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_activity
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_activity
     *
     * @mbg.generated
     */
    int insert(Activity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_activity
     *
     * @mbg.generated
     */
    int insertSelective(Activity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_activity
     *
     * @mbg.generated
     */
    Activity selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_activity
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(Activity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_activity
     *
     * @mbg.generated
     */
    int updateByPrimaryKeyWithBLOBs(Activity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_activity
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(Activity record);

    List<Activity> selectAndList(ActivityOptions options);

    List<Activity> selectMyDraftActivity(Long userId);

    List<Activity> selectSimpleByIdAndStatus(String where);

    List<Activity> selectSmartTips(Activity record);

    /**
     * 减少一个关注
     * @param id
     * @return
     */
    int minusFollower(Integer id);

    /**
     * 增加一个关注
     * @param id
     * @return
     */
    int increaseFollower(Integer id);

    /**
     * 减少一个报名人数
     * @param id
     * @return
     */
    int minusApplicant(Integer id);

    /**
     * 增加一个报名人数
     * @param id
     * @return
     */
    int increaseApplicant(Integer id);

    List<Activity> selectAndListForAdmin(ActivityOptions options);
}