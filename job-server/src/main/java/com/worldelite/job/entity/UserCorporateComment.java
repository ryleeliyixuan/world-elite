package com.worldelite.job.entity;

import java.util.Date;

/**
 * Database Table Remarks:
 *   用户笔记
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table t_user_corporate_comment
 */
public class UserCorporateComment {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_user_corporate_comment.id
     *
     * @mbg.generated
     */
    private Long id;

    /**
     * Database Column Remarks:
     *   简历ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_user_corporate_comment.resume_id
     *
     * @mbg.generated
     */
    private Long resumeId;

    /**
     * Database Column Remarks:
     *   笔记
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_user_corporate_comment.comment
     *
     * @mbg.generated
     */
    private String comment;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_user_corporate_comment.del_flag
     *
     * @mbg.generated
     */
    private Byte delFlag;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_user_corporate_comment.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_user_corporate_comment.update_time
     *
     * @mbg.generated
     */
    private Date updateTime;

    /**
     * Database Column Remarks:
     *   创建者ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_user_corporate_comment.creator_id
     *
     * @mbg.generated
     */
    private Long creatorId;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_user_corporate_comment.id
     *
     * @return the value of t_user_corporate_comment.id
     *
     * @mbg.generated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_user_corporate_comment.id
     *
     * @param id the value for t_user_corporate_comment.id
     *
     * @mbg.generated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_user_corporate_comment.resume_id
     *
     * @return the value of t_user_corporate_comment.resume_id
     *
     * @mbg.generated
     */
    public Long getResumeId() {
        return resumeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_user_corporate_comment.resume_id
     *
     * @param resumeId the value for t_user_corporate_comment.resume_id
     *
     * @mbg.generated
     */
    public void setResumeId(Long resumeId) {
        this.resumeId = resumeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_user_corporate_comment.comment
     *
     * @return the value of t_user_corporate_comment.comment
     *
     * @mbg.generated
     */
    public String getComment() {
        return comment;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_user_corporate_comment.comment
     *
     * @param comment the value for t_user_corporate_comment.comment
     *
     * @mbg.generated
     */
    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_user_corporate_comment.del_flag
     *
     * @return the value of t_user_corporate_comment.del_flag
     *
     * @mbg.generated
     */
    public Byte getDelFlag() {
        return delFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_user_corporate_comment.del_flag
     *
     * @param delFlag the value for t_user_corporate_comment.del_flag
     *
     * @mbg.generated
     */
    public void setDelFlag(Byte delFlag) {
        this.delFlag = delFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_user_corporate_comment.create_time
     *
     * @return the value of t_user_corporate_comment.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_user_corporate_comment.create_time
     *
     * @param createTime the value for t_user_corporate_comment.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_user_corporate_comment.update_time
     *
     * @return the value of t_user_corporate_comment.update_time
     *
     * @mbg.generated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_user_corporate_comment.update_time
     *
     * @param updateTime the value for t_user_corporate_comment.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_user_corporate_comment.creator_id
     *
     * @return the value of t_user_corporate_comment.creator_id
     *
     * @mbg.generated
     */
    public Long getCreatorId() {
        return creatorId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_user_corporate_comment.creator_id
     *
     * @param creatorId the value for t_user_corporate_comment.creator_id
     *
     * @mbg.generated
     */
    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }
}