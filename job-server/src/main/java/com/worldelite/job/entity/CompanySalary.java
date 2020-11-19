package com.worldelite.job.entity;

import java.util.Date;

/**
 * Database Table Remarks:
 *   公司薪资待遇
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table t_company_salary
 */
public class CompanySalary {
    /**
     * Database Column Remarks:
     *   薪资待遇ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_salary.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     * Database Column Remarks:
     *   公司ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_salary.company_id
     *
     * @mbg.generated
     */
    private Long companyId;

    /**
     * Database Column Remarks:
     *   岗位ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_salary.job_id
     *
     * @mbg.generated
     */
    private Long jobId;

    /**
     * Database Column Remarks:
     *   薪资范围ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_salary.salary_id
     *
     * @mbg.generated
     */
    private Integer salaryId;

    /**
     * Database Column Remarks:
     *   百分比
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_salary.percent
     *
     * @mbg.generated
     */
    private Integer percent;

    /**
     * Database Column Remarks:
     *   创建时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_salary.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     * Database Column Remarks:
     *   更新时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_salary.update_time
     *
     * @mbg.generated
     */
    private Date updateTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_salary.id
     *
     * @return the value of t_company_salary.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_salary.id
     *
     * @param id the value for t_company_salary.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_salary.company_id
     *
     * @return the value of t_company_salary.company_id
     *
     * @mbg.generated
     */
    public Long getCompanyId() {
        return companyId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_salary.company_id
     *
     * @param companyId the value for t_company_salary.company_id
     *
     * @mbg.generated
     */
    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_salary.job_id
     *
     * @return the value of t_company_salary.job_id
     *
     * @mbg.generated
     */
    public Long getJobId() {
        return jobId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_salary.job_id
     *
     * @param jobId the value for t_company_salary.job_id
     *
     * @mbg.generated
     */
    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_salary.salary_id
     *
     * @return the value of t_company_salary.salary_id
     *
     * @mbg.generated
     */
    public Integer getSalaryId() {
        return salaryId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_salary.salary_id
     *
     * @param salaryId the value for t_company_salary.salary_id
     *
     * @mbg.generated
     */
    public void setSalaryId(Integer salaryId) {
        this.salaryId = salaryId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_salary.percent
     *
     * @return the value of t_company_salary.percent
     *
     * @mbg.generated
     */
    public Integer getPercent() {
        return percent;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_salary.percent
     *
     * @param percent the value for t_company_salary.percent
     *
     * @mbg.generated
     */
    public void setPercent(Integer percent) {
        this.percent = percent;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_salary.create_time
     *
     * @return the value of t_company_salary.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_salary.create_time
     *
     * @param createTime the value for t_company_salary.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_salary.update_time
     *
     * @return the value of t_company_salary.update_time
     *
     * @mbg.generated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_salary.update_time
     *
     * @param updateTime the value for t_company_salary.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}