package com.worldelite.job.mapper;

import com.worldelite.job.entity.CompanyUser;

import java.util.List;

public interface CompanyUserMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_company_user
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_company_user
     *
     * @mbg.generated
     */
    int insert(CompanyUser record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_company_user
     *
     * @mbg.generated
     */
    int insertSelective(CompanyUser record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_company_user
     *
     * @mbg.generated
     */
    CompanyUser selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_company_user
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(CompanyUser record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_company_user
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(CompanyUser record);

    List<CompanyUser> selectAndList(CompanyUser options);

    CompanyUser selectByUserId(Long userId);
}