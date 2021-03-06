package com.worldelite.job.mapper;

import com.worldelite.job.entity.School;

import java.util.List;

public interface SchoolMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_school
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_school
     *
     * @mbg.generated
     */
    int insert(School record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_school
     *
     * @mbg.generated
     */
    int insertSelective(School record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_school
     *
     * @mbg.generated
     */
    School selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_school
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(School record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_school
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(School record);

    List<School> selectLikeName(String name);

    School selectByName(String name);

    List<School> selectAndList(School options);
}