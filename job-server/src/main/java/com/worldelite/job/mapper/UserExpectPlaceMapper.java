package com.worldelite.job.mapper;

import com.worldelite.job.entity.UserExpectJob;
import com.worldelite.job.entity.UserExpectPlace;

import java.util.List;

public interface UserExpectPlaceMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_user_expect_place
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_user_expect_place
     *
     * @mbg.generated
     */
    int insert(UserExpectPlace record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_user_expect_place
     *
     * @mbg.generated
     */
    int insertSelective(UserExpectPlace record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_user_expect_place
     *
     * @mbg.generated
     */
    UserExpectPlace selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_user_expect_place
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(UserExpectPlace record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_user_expect_place
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(UserExpectPlace record);

    List<UserExpectPlace> selectAndList(UserExpectPlace options);

    int deleteByUserId(Long userId);
}