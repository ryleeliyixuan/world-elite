package com.worldelite.job.entity;

import java.util.Date;

/**
 * Database Table Remarks:
 *   百科模块表
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table t_wiki_module
 */
public class WikiModule {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_wiki_module.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_wiki_module.company_id
     *
     * @mbg.generated
     */
    private Long companyId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_wiki_module.wiki_enable
     *
     * @mbg.generated
     */
    private Byte wikiEnable;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_wiki_module.address_enable
     *
     * @mbg.generated
     */
    private Byte addressEnable;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_wiki_module.employee_enable
     *
     * @mbg.generated
     */
    private Byte employeeEnable;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_wiki_module.product_enable
     *
     * @mbg.generated
     */
    private Byte productEnable;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_wiki_module.market_enable
     *
     * @mbg.generated
     */
    private Byte marketEnable;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_wiki_module.history_enable
     *
     * @mbg.generated
     */
    private Byte historyEnable;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_wiki_module.structure_enable
     *
     * @mbg.generated
     */
    private Byte structureEnable;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_wiki_module.salary_enable
     *
     * @mbg.generated
     */
    private Byte salaryEnable;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_wiki_module.recruit_enable
     *
     * @mbg.generated
     */
    private Byte recruitEnable;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_wiki_module.environment_enable
     *
     * @mbg.generated
     */
    private Byte environmentEnable;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_wiki_module.department_enable
     *
     * @mbg.generated
     */
    private Byte departmentEnable;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_wiki_module.honor_enable
     *
     * @mbg.generated
     */
    private Byte honorEnable;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_wiki_module.post_enable
     *
     * @mbg.generated
     */
    private Byte postEnable;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_wiki_module.job_enable
     *
     * @mbg.generated
     */
    private Byte jobEnable;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_wiki_module.count_enable
     *
     * @mbg.generated
     */
    private Byte countEnable;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_wiki_module.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_wiki_module.update_time
     *
     * @mbg.generated
     */
    private Date updateTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_wiki_module.id
     *
     * @return the value of t_wiki_module.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_wiki_module.id
     *
     * @param id the value for t_wiki_module.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_wiki_module.company_id
     *
     * @return the value of t_wiki_module.company_id
     *
     * @mbg.generated
     */
    public Long getCompanyId() {
        return companyId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_wiki_module.company_id
     *
     * @param companyId the value for t_wiki_module.company_id
     *
     * @mbg.generated
     */
    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_wiki_module.wiki_enable
     *
     * @return the value of t_wiki_module.wiki_enable
     *
     * @mbg.generated
     */
    public Byte getWikiEnable() {
        return wikiEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_wiki_module.wiki_enable
     *
     * @param wikiEnable the value for t_wiki_module.wiki_enable
     *
     * @mbg.generated
     */
    public void setWikiEnable(Byte wikiEnable) {
        this.wikiEnable = wikiEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_wiki_module.address_enable
     *
     * @return the value of t_wiki_module.address_enable
     *
     * @mbg.generated
     */
    public Byte getAddressEnable() {
        return addressEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_wiki_module.address_enable
     *
     * @param addressEnable the value for t_wiki_module.address_enable
     *
     * @mbg.generated
     */
    public void setAddressEnable(Byte addressEnable) {
        this.addressEnable = addressEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_wiki_module.employee_enable
     *
     * @return the value of t_wiki_module.employee_enable
     *
     * @mbg.generated
     */
    public Byte getEmployeeEnable() {
        return employeeEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_wiki_module.employee_enable
     *
     * @param employeeEnable the value for t_wiki_module.employee_enable
     *
     * @mbg.generated
     */
    public void setEmployeeEnable(Byte employeeEnable) {
        this.employeeEnable = employeeEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_wiki_module.product_enable
     *
     * @return the value of t_wiki_module.product_enable
     *
     * @mbg.generated
     */
    public Byte getProductEnable() {
        return productEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_wiki_module.product_enable
     *
     * @param productEnable the value for t_wiki_module.product_enable
     *
     * @mbg.generated
     */
    public void setProductEnable(Byte productEnable) {
        this.productEnable = productEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_wiki_module.market_enable
     *
     * @return the value of t_wiki_module.market_enable
     *
     * @mbg.generated
     */
    public Byte getMarketEnable() {
        return marketEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_wiki_module.market_enable
     *
     * @param marketEnable the value for t_wiki_module.market_enable
     *
     * @mbg.generated
     */
    public void setMarketEnable(Byte marketEnable) {
        this.marketEnable = marketEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_wiki_module.history_enable
     *
     * @return the value of t_wiki_module.history_enable
     *
     * @mbg.generated
     */
    public Byte getHistoryEnable() {
        return historyEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_wiki_module.history_enable
     *
     * @param historyEnable the value for t_wiki_module.history_enable
     *
     * @mbg.generated
     */
    public void setHistoryEnable(Byte historyEnable) {
        this.historyEnable = historyEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_wiki_module.structure_enable
     *
     * @return the value of t_wiki_module.structure_enable
     *
     * @mbg.generated
     */
    public Byte getStructureEnable() {
        return structureEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_wiki_module.structure_enable
     *
     * @param structureEnable the value for t_wiki_module.structure_enable
     *
     * @mbg.generated
     */
    public void setStructureEnable(Byte structureEnable) {
        this.structureEnable = structureEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_wiki_module.salary_enable
     *
     * @return the value of t_wiki_module.salary_enable
     *
     * @mbg.generated
     */
    public Byte getSalaryEnable() {
        return salaryEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_wiki_module.salary_enable
     *
     * @param salaryEnable the value for t_wiki_module.salary_enable
     *
     * @mbg.generated
     */
    public void setSalaryEnable(Byte salaryEnable) {
        this.salaryEnable = salaryEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_wiki_module.recruit_enable
     *
     * @return the value of t_wiki_module.recruit_enable
     *
     * @mbg.generated
     */
    public Byte getRecruitEnable() {
        return recruitEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_wiki_module.recruit_enable
     *
     * @param recruitEnable the value for t_wiki_module.recruit_enable
     *
     * @mbg.generated
     */
    public void setRecruitEnable(Byte recruitEnable) {
        this.recruitEnable = recruitEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_wiki_module.environment_enable
     *
     * @return the value of t_wiki_module.environment_enable
     *
     * @mbg.generated
     */
    public Byte getEnvironmentEnable() {
        return environmentEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_wiki_module.environment_enable
     *
     * @param environmentEnable the value for t_wiki_module.environment_enable
     *
     * @mbg.generated
     */
    public void setEnvironmentEnable(Byte environmentEnable) {
        this.environmentEnable = environmentEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_wiki_module.department_enable
     *
     * @return the value of t_wiki_module.department_enable
     *
     * @mbg.generated
     */
    public Byte getDepartmentEnable() {
        return departmentEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_wiki_module.department_enable
     *
     * @param departmentEnable the value for t_wiki_module.department_enable
     *
     * @mbg.generated
     */
    public void setDepartmentEnable(Byte departmentEnable) {
        this.departmentEnable = departmentEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_wiki_module.honor_enable
     *
     * @return the value of t_wiki_module.honor_enable
     *
     * @mbg.generated
     */
    public Byte getHonorEnable() {
        return honorEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_wiki_module.honor_enable
     *
     * @param honorEnable the value for t_wiki_module.honor_enable
     *
     * @mbg.generated
     */
    public void setHonorEnable(Byte honorEnable) {
        this.honorEnable = honorEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_wiki_module.post_enable
     *
     * @return the value of t_wiki_module.post_enable
     *
     * @mbg.generated
     */
    public Byte getPostEnable() {
        return postEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_wiki_module.post_enable
     *
     * @param postEnable the value for t_wiki_module.post_enable
     *
     * @mbg.generated
     */
    public void setPostEnable(Byte postEnable) {
        this.postEnable = postEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_wiki_module.job_enable
     *
     * @return the value of t_wiki_module.job_enable
     *
     * @mbg.generated
     */
    public Byte getJobEnable() {
        return jobEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_wiki_module.job_enable
     *
     * @param jobEnable the value for t_wiki_module.job_enable
     *
     * @mbg.generated
     */
    public void setJobEnable(Byte jobEnable) {
        this.jobEnable = jobEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_wiki_module.count_enable
     *
     * @return the value of t_wiki_module.count_enable
     *
     * @mbg.generated
     */
    public Byte getCountEnable() {
        return countEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_wiki_module.count_enable
     *
     * @param countEnable the value for t_wiki_module.count_enable
     *
     * @mbg.generated
     */
    public void setCountEnable(Byte countEnable) {
        this.countEnable = countEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_wiki_module.create_time
     *
     * @return the value of t_wiki_module.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_wiki_module.create_time
     *
     * @param createTime the value for t_wiki_module.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_wiki_module.update_time
     *
     * @return the value of t_wiki_module.update_time
     *
     * @mbg.generated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_wiki_module.update_time
     *
     * @param updateTime the value for t_wiki_module.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}