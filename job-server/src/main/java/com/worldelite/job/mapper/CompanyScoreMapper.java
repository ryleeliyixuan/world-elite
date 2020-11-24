package com.worldelite.job.mapper;

import com.worldelite.job.entity.CompanyScore;

import java.util.List;

public interface CompanyScoreMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_company_score
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_company_score
     *
     * @mbg.generated
     */
    int insert(CompanyScore record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_company_score
     *
     * @mbg.generated
     */
    int insertSelective(CompanyScore record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_company_score
     *
     * @mbg.generated
     */
    CompanyScore selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_company_score
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(CompanyScore record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_company_score
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(CompanyScore record);

    List<CompanyScore> selectAndList(CompanyScore companyScore);

    Integer selectCompanyScore(Long companyId);
}