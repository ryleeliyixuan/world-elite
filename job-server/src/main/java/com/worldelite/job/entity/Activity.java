package com.worldelite.job.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Database Table Remarks:
 *   活动表
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table t_activity
 */
public class Activity implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     * Database Column Remarks:
     *   活动发布者
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity.user_id
     *
     * @mbg.generated
     */
    private Long userId;

    /**
     * Database Column Remarks:
     *   所在城市
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity.city_id
     *
     * @mbg.generated
     */
    private Integer cityId;

    /**
     * Database Column Remarks:
     *   标题
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity.title
     *
     * @mbg.generated
     */
    private String title;

    /**
     * Database Column Remarks:
     *   图片缩略图
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity.thumbnail
     *
     * @mbg.generated
     */
    private String thumbnail;

    /**
     * Database Column Remarks:
     *   摘要
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity.summary
     *
     * @mbg.generated
     */
    private String summary;

    /**
     * Database Column Remarks:
     *   链接
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity.url
     *
     * @mbg.generated
     */
    private String url;

    /**
     * Database Column Remarks:
     *   状态
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity.status
     *
     * @mbg.generated
     */
    private Byte status;

    /**
     * Database Column Remarks:
     *   开始时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity.start_time
     *
     * @mbg.generated
     */
    private Date startTime;

    /**
     * Database Column Remarks:
     *   结束时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity.finish_time
     *
     * @mbg.generated
     */
    private Date finishTime;

    /**
     * Database Column Remarks:
     *   活动地址
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity.address
     *
     * @mbg.generated
     */
    private String address;

    /**
     * Database Column Remarks:
     *   主办方
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity.organizer
     *
     * @mbg.generated
     */
    private String organizer;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity.update_time
     *
     * @mbg.generated
     */
    private Date updateTime;

    /**
     * Database Column Remarks:
     *   活动详情
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity.description
     *
     * @mbg.generated
     */
    private String description;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity.id
     *
     * @return the value of t_activity.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity.id
     *
     * @param id the value for t_activity.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity.user_id
     *
     * @return the value of t_activity.user_id
     *
     * @mbg.generated
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity.user_id
     *
     * @param userId the value for t_activity.user_id
     *
     * @mbg.generated
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity.city_id
     *
     * @return the value of t_activity.city_id
     *
     * @mbg.generated
     */
    public Integer getCityId() {
        return cityId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity.city_id
     *
     * @param cityId the value for t_activity.city_id
     *
     * @mbg.generated
     */
    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity.title
     *
     * @return the value of t_activity.title
     *
     * @mbg.generated
     */
    public String getTitle() {
        return title;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity.title
     *
     * @param title the value for t_activity.title
     *
     * @mbg.generated
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity.thumbnail
     *
     * @return the value of t_activity.thumbnail
     *
     * @mbg.generated
     */
    public String getThumbnail() {
        return thumbnail;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity.thumbnail
     *
     * @param thumbnail the value for t_activity.thumbnail
     *
     * @mbg.generated
     */
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail == null ? null : thumbnail.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity.summary
     *
     * @return the value of t_activity.summary
     *
     * @mbg.generated
     */
    public String getSummary() {
        return summary;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity.summary
     *
     * @param summary the value for t_activity.summary
     *
     * @mbg.generated
     */
    public void setSummary(String summary) {
        this.summary = summary == null ? null : summary.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity.url
     *
     * @return the value of t_activity.url
     *
     * @mbg.generated
     */
    public String getUrl() {
        return url;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity.url
     *
     * @param url the value for t_activity.url
     *
     * @mbg.generated
     */
    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity.status
     *
     * @return the value of t_activity.status
     *
     * @mbg.generated
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity.status
     *
     * @param status the value for t_activity.status
     *
     * @mbg.generated
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity.start_time
     *
     * @return the value of t_activity.start_time
     *
     * @mbg.generated
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity.start_time
     *
     * @param startTime the value for t_activity.start_time
     *
     * @mbg.generated
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity.finish_time
     *
     * @return the value of t_activity.finish_time
     *
     * @mbg.generated
     */
    public Date getFinishTime() {
        return finishTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity.finish_time
     *
     * @param finishTime the value for t_activity.finish_time
     *
     * @mbg.generated
     */
    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity.address
     *
     * @return the value of t_activity.address
     *
     * @mbg.generated
     */
    public String getAddress() {
        return address;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity.address
     *
     * @param address the value for t_activity.address
     *
     * @mbg.generated
     */
    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity.organizer
     *
     * @return the value of t_activity.organizer
     *
     * @mbg.generated
     */
    public String getOrganizer() {
        return organizer;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity.organizer
     *
     * @param organizer the value for t_activity.organizer
     *
     * @mbg.generated
     */
    public void setOrganizer(String organizer) {
        this.organizer = organizer == null ? null : organizer.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity.create_time
     *
     * @return the value of t_activity.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity.create_time
     *
     * @param createTime the value for t_activity.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity.update_time
     *
     * @return the value of t_activity.update_time
     *
     * @mbg.generated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity.update_time
     *
     * @param updateTime the value for t_activity.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity.description
     *
     * @return the value of t_activity.description
     *
     * @mbg.generated
     */
    public String getDescription() {
        return description;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity.description
     *
     * @param description the value for t_activity.description
     *
     * @mbg.generated
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }
}