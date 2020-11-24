package com.worldelite.job.entity;

import java.util.Date;

/**
 * Database Table Remarks:
 *   公司发展路径
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table t_company_history
 */
public class CompanyHistory {
    /**
     * Database Column Remarks:
     *   发展路径ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_history.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     * Database Column Remarks:
     *   企业ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_history.company_id
     *
     * @mbg.generated
     */
    private Long companyId;

    /**
     * Database Column Remarks:
     *   事件时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_history.event_time
     *
     * @mbg.generated
     */
    private String eventTime;

    /**
     * Database Column Remarks:
     *   事件内容
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_history.event
     *
     * @mbg.generated
     */
    private String event;

    /**
     * Database Column Remarks:
     *   创建时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_history.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     * Database Column Remarks:
     *   更新时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_history.update_time
     *
     * @mbg.generated
     */
    private Date updateTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_history.id
     *
     * @return the value of t_company_history.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_history.id
     *
     * @param id the value for t_company_history.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_history.company_id
     *
     * @return the value of t_company_history.company_id
     *
     * @mbg.generated
     */
    public Long getCompanyId() {
        return companyId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_history.company_id
     *
     * @param companyId the value for t_company_history.company_id
     *
     * @mbg.generated
     */
    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_history.event_time
     *
     * @return the value of t_company_history.event_time
     *
     * @mbg.generated
     */
    public String getEventTime() {
        return eventTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_history.event_time
     *
     * @param eventTime the value for t_company_history.event_time
     *
     * @mbg.generated
     */
    public void setEventTime(String eventTime) {
        this.eventTime = eventTime == null ? null : eventTime.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_history.event
     *
     * @return the value of t_company_history.event
     *
     * @mbg.generated
     */
    public String getEvent() {
        return event;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_history.event
     *
     * @param event the value for t_company_history.event
     *
     * @mbg.generated
     */
    public void setEvent(String event) {
        this.event = event == null ? null : event.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_history.create_time
     *
     * @return the value of t_company_history.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_history.create_time
     *
     * @param createTime the value for t_company_history.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_history.update_time
     *
     * @return the value of t_company_history.update_time
     *
     * @mbg.generated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_history.update_time
     *
     * @param updateTime the value for t_company_history.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}