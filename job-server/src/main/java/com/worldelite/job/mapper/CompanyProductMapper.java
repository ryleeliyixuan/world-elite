package com.worldelite.job.mapper;

import com.worldelite.job.entity.CompanyProduct;

import java.util.List;

public interface CompanyProductMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_company_product
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_company_product
     *
     * @mbg.generated
     */
    int insert(CompanyProduct record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_company_product
     *
     * @mbg.generated
     */
    int insertSelective(CompanyProduct record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_company_product
     *
     * @mbg.generated
     */
    CompanyProduct selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_company_product
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(CompanyProduct record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_company_product
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(CompanyProduct record);

    List<CompanyProduct> selectAndList(CompanyProduct record);

    void deleteByCompanyId(Long companyId);
}