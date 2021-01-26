package com.worldelite.job.entity;

import java.util.Date;

/**
 * Database Table Remarks:
 *   活动下架表
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table t_activity_takeoff
 */
public class ActivityTakeOff {
    /**
     * Database Column Remarks:
     *   活动ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity_takeoff.activity_id
     *
     * @mbg.generated
     */
    private Integer activityId;

    /**
     * Database Column Remarks:
     *   下架原因
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity_takeoff.reason
     *
     * @mbg.generated
     */
    private String reason;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity_takeoff.del_flag
     *
     * @mbg.generated
     */
    private Byte delFlag;

    /**
     * Database Column Remarks:
     *   创建时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity_takeoff.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     * Database Column Remarks:
     *   更新时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity_takeoff.update_time
     *
     * @mbg.generated
     */
    private Date updateTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity_takeoff.activity_id
     *
     * @return the value of t_activity_takeoff.activity_id
     *
     * @mbg.generated
     */
    public Integer getActivityId() {
        return activityId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity_takeoff.activity_id
     *
     * @param activityId the value for t_activity_takeoff.activity_id
     *
     * @mbg.generated
     */
    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity_takeoff.reason
     *
     * @return the value of t_activity_takeoff.reason
     *
     * @mbg.generated
     */
    public String getReason() {
        return reason;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity_takeoff.reason
     *
     * @param reason the value for t_activity_takeoff.reason
     *
     * @mbg.generated
     */
    public void setReason(String reason) {
        this.reason = reason == null ? null : reason.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity_takeoff.del_flag
     *
     * @return the value of t_activity_takeoff.del_flag
     *
     * @mbg.generated
     */
    public Byte getDelFlag() {
        return delFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity_takeoff.del_flag
     *
     * @param delFlag the value for t_activity_takeoff.del_flag
     *
     * @mbg.generated
     */
    public void setDelFlag(Byte delFlag) {
        this.delFlag = delFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity_takeoff.create_time
     *
     * @return the value of t_activity_takeoff.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity_takeoff.create_time
     *
     * @param createTime the value for t_activity_takeoff.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity_takeoff.update_time
     *
     * @return the value of t_activity_takeoff.update_time
     *
     * @mbg.generated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity_takeoff.update_time
     *
     * @param updateTime the value for t_activity_takeoff.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}