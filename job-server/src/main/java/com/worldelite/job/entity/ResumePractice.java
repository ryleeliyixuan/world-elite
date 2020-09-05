package com.worldelite.job.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Database Table Remarks:
 *   校园实践
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table t_resume_practice
 */
public class ResumePractice implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume_practice.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume_practice.resume_id
     *
     * @mbg.generated
     */
    private Long resumeId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume_practice.start_time
     *
     * @mbg.generated
     */
    private Date startTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume_practice.finish_time
     *
     * @mbg.generated
     */
    private Date finishTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume_practice.title
     *
     * @mbg.generated
     */
    private String title;

    /**
     * Database Column Remarks:
     *   职位
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume_practice.post
     *
     * @mbg.generated
     */
    private String post;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume_practice.position
     *
     * @mbg.generated
     */
    private Integer position;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume_practice.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume_practice.update_time
     *
     * @mbg.generated
     */
    private Date updateTime;

    /**
     * Database Column Remarks:
     *   实践中
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume_practice.on_work
     *
     * @mbg.generated
     */
    private Byte onWork;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume_practice.description
     *
     * @mbg.generated
     */
    private String description;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume_practice.id
     *
     * @return the value of t_resume_practice.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume_practice.id
     *
     * @param id the value for t_resume_practice.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume_practice.resume_id
     *
     * @return the value of t_resume_practice.resume_id
     *
     * @mbg.generated
     */
    public Long getResumeId() {
        return resumeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume_practice.resume_id
     *
     * @param resumeId the value for t_resume_practice.resume_id
     *
     * @mbg.generated
     */
    public void setResumeId(Long resumeId) {
        this.resumeId = resumeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume_practice.start_time
     *
     * @return the value of t_resume_practice.start_time
     *
     * @mbg.generated
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume_practice.start_time
     *
     * @param startTime the value for t_resume_practice.start_time
     *
     * @mbg.generated
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume_practice.finish_time
     *
     * @return the value of t_resume_practice.finish_time
     *
     * @mbg.generated
     */
    public Date getFinishTime() {
        return finishTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume_practice.finish_time
     *
     * @param finishTime the value for t_resume_practice.finish_time
     *
     * @mbg.generated
     */
    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume_practice.title
     *
     * @return the value of t_resume_practice.title
     *
     * @mbg.generated
     */
    public String getTitle() {
        return title;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume_practice.title
     *
     * @param title the value for t_resume_practice.title
     *
     * @mbg.generated
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume_practice.post
     *
     * @return the value of t_resume_practice.post
     *
     * @mbg.generated
     */
    public String getPost() {
        return post;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume_practice.post
     *
     * @param post the value for t_resume_practice.post
     *
     * @mbg.generated
     */
    public void setPost(String post) {
        this.post = post == null ? null : post.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume_practice.position
     *
     * @return the value of t_resume_practice.position
     *
     * @mbg.generated
     */
    public Integer getPosition() {
        return position;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume_practice.position
     *
     * @param position the value for t_resume_practice.position
     *
     * @mbg.generated
     */
    public void setPosition(Integer position) {
        this.position = position;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume_practice.create_time
     *
     * @return the value of t_resume_practice.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume_practice.create_time
     *
     * @param createTime the value for t_resume_practice.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume_practice.update_time
     *
     * @return the value of t_resume_practice.update_time
     *
     * @mbg.generated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume_practice.update_time
     *
     * @param updateTime the value for t_resume_practice.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume_practice.on_work
     *
     * @return the value of t_resume_practice.on_work
     *
     * @mbg.generated
     */
    public Byte getOnWork() {
        return onWork;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume_practice.on_work
     *
     * @param onWork the value for t_resume_practice.on_work
     *
     * @mbg.generated
     */
    public void setOnWork(Byte onWork) {
        this.onWork = onWork;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume_practice.description
     *
     * @return the value of t_resume_practice.description
     *
     * @mbg.generated
     */
    public String getDescription() {
        return description;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume_practice.description
     *
     * @param description the value for t_resume_practice.description
     *
     * @mbg.generated
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }
}