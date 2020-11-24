package com.worldelite.job.entity;

import java.util.Date;

/**
 * Database Table Remarks:
 *   企业雇员数量表
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table t_company_employee
 */
public class CompanyEmployee {
    /**
     * Database Column Remarks:
     *   雇员数量ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_employee.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     * Database Column Remarks:
     *   企业ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_employee.company_id
     *
     * @mbg.generated
     */
    private Long companyId;

    /**
     * Database Column Remarks:
     *   年份
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_employee.year
     *
     * @mbg.generated
     */
    private Date year;

    /**
     * Database Column Remarks:
     *   雇员数量
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_employee.number
     *
     * @mbg.generated
     */
    private Integer number;

    /**
     * Database Column Remarks:
     *   创建时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_employee.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     * Database Column Remarks:
     *   更新时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_employee.update_time
     *
     * @mbg.generated
     */
    private Date updateTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_employee.id
     *
     * @return the value of t_company_employee.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_employee.id
     *
     * @param id the value for t_company_employee.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_employee.company_id
     *
     * @return the value of t_company_employee.company_id
     *
     * @mbg.generated
     */
    public Long getCompanyId() {
        return companyId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_employee.company_id
     *
     * @param companyId the value for t_company_employee.company_id
     *
     * @mbg.generated
     */
    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_employee.year
     *
     * @return the value of t_company_employee.year
     *
     * @mbg.generated
     */
    public Date getYear() {
        return year;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_employee.year
     *
     * @param year the value for t_company_employee.year
     *
     * @mbg.generated
     */
    public void setYear(Date year) {
        this.year = year;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_employee.number
     *
     * @return the value of t_company_employee.number
     *
     * @mbg.generated
     */
    public Integer getNumber() {
        return number;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_employee.number
     *
     * @param number the value for t_company_employee.number
     *
     * @mbg.generated
     */
    public void setNumber(Integer number) {
        this.number = number;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_employee.create_time
     *
     * @return the value of t_company_employee.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_employee.create_time
     *
     * @param createTime the value for t_company_employee.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_employee.update_time
     *
     * @return the value of t_company_employee.update_time
     *
     * @mbg.generated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_employee.update_time
     *
     * @param updateTime the value for t_company_employee.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}