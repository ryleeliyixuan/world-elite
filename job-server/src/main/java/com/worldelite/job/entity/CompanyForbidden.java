package com.worldelite.job.entity;

import java.util.Date;

/**
 * Database Table Remarks:
 *   公司禁言表
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table t_company_forbidden
 */
public class CompanyForbidden {
    /**
     * Database Column Remarks:
     *   ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_forbidden.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     * Database Column Remarks:
     *   用户ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_forbidden.user_id
     *
     * @mbg.generated
     */
    private Long userId;

    /**
     * Database Column Remarks:
     *   创建时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_forbidden.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     * Database Column Remarks:
     *   更新时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_forbidden.update_time
     *
     * @mbg.generated
     */
    private Date updateTime;

    /**
     * Database Column Remarks:
     *   禁言天数
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_forbidden.days_id
     *
     * @mbg.generated
     */
    private Integer daysId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_forbidden.content
     *
     * @mbg.generated
     */
    private String content;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_forbidden.id
     *
     * @return the value of t_company_forbidden.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_forbidden.id
     *
     * @param id the value for t_company_forbidden.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_forbidden.user_id
     *
     * @return the value of t_company_forbidden.user_id
     *
     * @mbg.generated
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_forbidden.user_id
     *
     * @param userId the value for t_company_forbidden.user_id
     *
     * @mbg.generated
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_forbidden.create_time
     *
     * @return the value of t_company_forbidden.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_forbidden.create_time
     *
     * @param createTime the value for t_company_forbidden.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_forbidden.update_time
     *
     * @return the value of t_company_forbidden.update_time
     *
     * @mbg.generated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_forbidden.update_time
     *
     * @param updateTime the value for t_company_forbidden.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_forbidden.days_id
     *
     * @return the value of t_company_forbidden.days_id
     *
     * @mbg.generated
     */
    public Integer getDaysId() {
        return daysId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_forbidden.days_id
     *
     * @param daysId the value for t_company_forbidden.days_id
     *
     * @mbg.generated
     */
    public void setDaysId(Integer daysId) {
        this.daysId = daysId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_forbidden.content
     *
     * @return the value of t_company_forbidden.content
     *
     * @mbg.generated
     */
    public String getContent() {
        return content;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_forbidden.content
     *
     * @param content the value for t_company_forbidden.content
     *
     * @mbg.generated
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}