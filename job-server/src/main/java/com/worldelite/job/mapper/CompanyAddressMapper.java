package com.worldelite.job.mapper;

import com.worldelite.job.entity.CompanyAddress;

import java.util.List;

public interface CompanyAddressMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_company_address
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_company_address
     *
     * @mbg.generated
     */
    int insert(CompanyAddress record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_company_address
     *
     * @mbg.generated
     */
    int insertSelective(CompanyAddress record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_company_address
     *
     * @mbg.generated
     */
    CompanyAddress selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_company_address
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(CompanyAddress record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_company_address
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(CompanyAddress record);

    List<CompanyAddress> selectAndList(CompanyAddress options);
}