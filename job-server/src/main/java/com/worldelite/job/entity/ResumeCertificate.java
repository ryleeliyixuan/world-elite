package com.worldelite.job.entity;

import java.util.Date;

/**
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table t_resume_certificate
 */
public class ResumeCertificate {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume_certificate.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume_certificate.resume_id
     *
     * @mbg.generated
     */
    private Long resumeId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume_certificate.title
     *
     * @mbg.generated
     */
    private String title;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume_certificate.time
     *
     * @mbg.generated
     */
    private Date time;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume_certificate.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume_certificate.update_time
     *
     * @mbg.generated
     */
    private Date updateTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume_certificate.id
     *
     * @return the value of t_resume_certificate.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume_certificate.id
     *
     * @param id the value for t_resume_certificate.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume_certificate.resume_id
     *
     * @return the value of t_resume_certificate.resume_id
     *
     * @mbg.generated
     */
    public Long getResumeId() {
        return resumeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume_certificate.resume_id
     *
     * @param resumeId the value for t_resume_certificate.resume_id
     *
     * @mbg.generated
     */
    public void setResumeId(Long resumeId) {
        this.resumeId = resumeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume_certificate.title
     *
     * @return the value of t_resume_certificate.title
     *
     * @mbg.generated
     */
    public String getTitle() {
        return title;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume_certificate.title
     *
     * @param title the value for t_resume_certificate.title
     *
     * @mbg.generated
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume_certificate.time
     *
     * @return the value of t_resume_certificate.time
     *
     * @mbg.generated
     */
    public Date getTime() {
        return time;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume_certificate.time
     *
     * @param time the value for t_resume_certificate.time
     *
     * @mbg.generated
     */
    public void setTime(Date time) {
        this.time = time;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume_certificate.create_time
     *
     * @return the value of t_resume_certificate.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume_certificate.create_time
     *
     * @param createTime the value for t_resume_certificate.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume_certificate.update_time
     *
     * @return the value of t_resume_certificate.update_time
     *
     * @mbg.generated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume_certificate.update_time
     *
     * @param updateTime the value for t_resume_certificate.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}