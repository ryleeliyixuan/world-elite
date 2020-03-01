package com.worldelite.job.entity;

import java.util.Date;

/**
 * Database Table Remarks:
 *   工作经验
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table t_resume_experience
 */
public class ResumeExperience {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume_experience.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume_experience.resume_id
     *
     * @mbg.generated
     */
    private Long resumeId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume_experience.start_time
     *
     * @mbg.generated
     */
    private Date startTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume_experience.finish_time
     *
     * @mbg.generated
     */
    private Date finishTime;

    /**
     * Database Column Remarks:
     *   公司
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume_experience.company
     *
     * @mbg.generated
     */
    private String company;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume_experience.depart
     *
     * @mbg.generated
     */
    private String depart;

    /**
     * Database Column Remarks:
     *   职位
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume_experience.post
     *
     * @mbg.generated
     */
    private String post;

    /**
     * Database Column Remarks:
     *   排序位
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume_experience.position
     *
     * @mbg.generated
     */
    private Integer position;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume_experience.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume_experience.update_time
     *
     * @mbg.generated
     */
    private Date updateTime;

    /**
     * Database Column Remarks:
     *   描述
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume_experience.description
     *
     * @mbg.generated
     */
    private String description;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume_experience.id
     *
     * @return the value of t_resume_experience.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume_experience.id
     *
     * @param id the value for t_resume_experience.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume_experience.resume_id
     *
     * @return the value of t_resume_experience.resume_id
     *
     * @mbg.generated
     */
    public Long getResumeId() {
        return resumeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume_experience.resume_id
     *
     * @param resumeId the value for t_resume_experience.resume_id
     *
     * @mbg.generated
     */
    public void setResumeId(Long resumeId) {
        this.resumeId = resumeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume_experience.start_time
     *
     * @return the value of t_resume_experience.start_time
     *
     * @mbg.generated
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume_experience.start_time
     *
     * @param startTime the value for t_resume_experience.start_time
     *
     * @mbg.generated
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume_experience.finish_time
     *
     * @return the value of t_resume_experience.finish_time
     *
     * @mbg.generated
     */
    public Date getFinishTime() {
        return finishTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume_experience.finish_time
     *
     * @param finishTime the value for t_resume_experience.finish_time
     *
     * @mbg.generated
     */
    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume_experience.company
     *
     * @return the value of t_resume_experience.company
     *
     * @mbg.generated
     */
    public String getCompany() {
        return company;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume_experience.company
     *
     * @param company the value for t_resume_experience.company
     *
     * @mbg.generated
     */
    public void setCompany(String company) {
        this.company = company == null ? null : company.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume_experience.depart
     *
     * @return the value of t_resume_experience.depart
     *
     * @mbg.generated
     */
    public String getDepart() {
        return depart;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume_experience.depart
     *
     * @param depart the value for t_resume_experience.depart
     *
     * @mbg.generated
     */
    public void setDepart(String depart) {
        this.depart = depart == null ? null : depart.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume_experience.post
     *
     * @return the value of t_resume_experience.post
     *
     * @mbg.generated
     */
    public String getPost() {
        return post;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume_experience.post
     *
     * @param post the value for t_resume_experience.post
     *
     * @mbg.generated
     */
    public void setPost(String post) {
        this.post = post == null ? null : post.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume_experience.position
     *
     * @return the value of t_resume_experience.position
     *
     * @mbg.generated
     */
    public Integer getPosition() {
        return position;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume_experience.position
     *
     * @param position the value for t_resume_experience.position
     *
     * @mbg.generated
     */
    public void setPosition(Integer position) {
        this.position = position;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume_experience.create_time
     *
     * @return the value of t_resume_experience.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume_experience.create_time
     *
     * @param createTime the value for t_resume_experience.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume_experience.update_time
     *
     * @return the value of t_resume_experience.update_time
     *
     * @mbg.generated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume_experience.update_time
     *
     * @param updateTime the value for t_resume_experience.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume_experience.description
     *
     * @return the value of t_resume_experience.description
     *
     * @mbg.generated
     */
    public String getDescription() {
        return description;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume_experience.description
     *
     * @param description the value for t_resume_experience.description
     *
     * @mbg.generated
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }
}