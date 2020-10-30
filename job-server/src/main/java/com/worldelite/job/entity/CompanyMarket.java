package com.worldelite.job.entity;

import java.util.Date;

/**
 * Database Table Remarks:
 *   公司市值
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table t_company_market
 */
public class CompanyMarket {
    /**
     * Database Column Remarks:
     *   市值ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_market.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     * Database Column Remarks:
     *   企业ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_market.company_id
     *
     * @mbg.generated
     */
    private Long companyId;

    /**
     * Database Column Remarks:
     *   市值曲线地址
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_market.url
     *
     * @mbg.generated
     */
    private String url;

    /**
     * Database Column Remarks:
     *   创建时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_market.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     * Database Column Remarks:
     *   更新时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_market.update_time
     *
     * @mbg.generated
     */
    private Date updateTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_market.id
     *
     * @return the value of t_company_market.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_market.id
     *
     * @param id the value for t_company_market.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_market.company_id
     *
     * @return the value of t_company_market.company_id
     *
     * @mbg.generated
     */
    public Long getCompanyId() {
        return companyId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_market.company_id
     *
     * @param companyId the value for t_company_market.company_id
     *
     * @mbg.generated
     */
    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_market.url
     *
     * @return the value of t_company_market.url
     *
     * @mbg.generated
     */
    public String getUrl() {
        return url;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_market.url
     *
     * @param url the value for t_company_market.url
     *
     * @mbg.generated
     */
    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_market.create_time
     *
     * @return the value of t_company_market.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_market.create_time
     *
     * @param createTime the value for t_company_market.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_market.update_time
     *
     * @return the value of t_company_market.update_time
     *
     * @mbg.generated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_market.update_time
     *
     * @param updateTime the value for t_company_market.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}