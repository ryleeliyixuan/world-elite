package com.worldelite.job.entity;

import java.util.Date;

/**
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table t_user_corporate_tag
 */
public class UserCorporateTag {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_user_corporate_tag.id
     *
     * @mbg.generated
     */
    private Long id;

    /**
     * Database Column Remarks:
     *   职位申请ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_user_corporate_tag.job_apply_id
     *
     * @mbg.generated
     */
    private Long jobApplyId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_user_corporate_tag.tag_name
     *
     * @mbg.generated
     */
    private String tagName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_user_corporate_tag.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_user_corporate_tag.update_time
     *
     * @mbg.generated
     */
    private Date updateTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_user_corporate_tag.del_flag
     *
     * @mbg.generated
     */
    private String delFlag;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_user_corporate_tag.id
     *
     * @return the value of t_user_corporate_tag.id
     *
     * @mbg.generated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_user_corporate_tag.id
     *
     * @param id the value for t_user_corporate_tag.id
     *
     * @mbg.generated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_user_corporate_tag.job_apply_id
     *
     * @return the value of t_user_corporate_tag.job_apply_id
     *
     * @mbg.generated
     */
    public Long getJobApplyId() {
        return jobApplyId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_user_corporate_tag.job_apply_id
     *
     * @param jobApplyId the value for t_user_corporate_tag.job_apply_id
     *
     * @mbg.generated
     */
    public void setJobApplyId(Long jobApplyId) {
        this.jobApplyId = jobApplyId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_user_corporate_tag.tag_name
     *
     * @return the value of t_user_corporate_tag.tag_name
     *
     * @mbg.generated
     */
    public String getTagName() {
        return tagName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_user_corporate_tag.tag_name
     *
     * @param tagName the value for t_user_corporate_tag.tag_name
     *
     * @mbg.generated
     */
    public void setTagName(String tagName) {
        this.tagName = tagName == null ? null : tagName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_user_corporate_tag.create_time
     *
     * @return the value of t_user_corporate_tag.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_user_corporate_tag.create_time
     *
     * @param createTime the value for t_user_corporate_tag.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_user_corporate_tag.update_time
     *
     * @return the value of t_user_corporate_tag.update_time
     *
     * @mbg.generated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_user_corporate_tag.update_time
     *
     * @param updateTime the value for t_user_corporate_tag.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_user_corporate_tag.del_flag
     *
     * @return the value of t_user_corporate_tag.del_flag
     *
     * @mbg.generated
     */
    public String getDelFlag() {
        return delFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_user_corporate_tag.del_flag
     *
     * @param delFlag the value for t_user_corporate_tag.del_flag
     *
     * @mbg.generated
     */
    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag == null ? null : delFlag.trim();
    }
}