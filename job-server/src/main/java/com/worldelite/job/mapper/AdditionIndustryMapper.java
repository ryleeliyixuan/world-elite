package com.worldelite.job.mapper;

import com.worldelite.job.entity.AdditionIndustry;
import org.apache.commons.math3.analysis.function.Add;

import java.util.List;

public interface AdditionIndustryMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_addition_industry
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_addition_industry
     *
     * @mbg.generated
     */
    int insert(AdditionIndustry record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_addition_industry
     *
     * @mbg.generated
     */
    int insertSelective(AdditionIndustry record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_addition_industry
     *
     * @mbg.generated
     */
    AdditionIndustry selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_addition_industry
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(AdditionIndustry record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_addition_industry
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(AdditionIndustry record);

    List<AdditionIndustry> selectByCreatorId(AdditionIndustry additionIndustry);

    List<AdditionIndustry> selectAndList(AdditionIndustry additionIndustry);
}