package com.worldelite.job.mapper;

import com.worldelite.job.entity.CompanySalary;

import java.util.List;

public interface CompanySalaryMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_company_salary
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_company_salary
     *
     * @mbg.generated
     */
    int insert(CompanySalary record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_company_salary
     *
     * @mbg.generated
     */
    int insertSelective(CompanySalary record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_company_salary
     *
     * @mbg.generated
     */
    CompanySalary selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_company_salary
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(CompanySalary record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_company_salary
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(CompanySalary record);

    List<CompanySalary> selectAndList(CompanySalary companySalary);

    void deleteByCompanyId(Long companyId);
}