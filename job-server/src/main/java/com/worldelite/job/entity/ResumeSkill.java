package com.worldelite.job.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Database Table Remarks:
 *   技能
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table t_resume_skill
 */
public class ResumeSkill implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume_skill.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume_skill.resume_id
     *
     * @mbg.generated
     */
    private Long resumeId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume_skill.name
     *
     * @mbg.generated
     */
    private String name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume_skill.level
     *
     * @mbg.generated
     */
    private Byte level;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume_skill.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume_skill.update_time
     *
     * @mbg.generated
     */
    private Date updateTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume_skill.id
     *
     * @return the value of t_resume_skill.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume_skill.id
     *
     * @param id the value for t_resume_skill.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume_skill.resume_id
     *
     * @return the value of t_resume_skill.resume_id
     *
     * @mbg.generated
     */
    public Long getResumeId() {
        return resumeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume_skill.resume_id
     *
     * @param resumeId the value for t_resume_skill.resume_id
     *
     * @mbg.generated
     */
    public void setResumeId(Long resumeId) {
        this.resumeId = resumeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume_skill.name
     *
     * @return the value of t_resume_skill.name
     *
     * @mbg.generated
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume_skill.name
     *
     * @param name the value for t_resume_skill.name
     *
     * @mbg.generated
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume_skill.level
     *
     * @return the value of t_resume_skill.level
     *
     * @mbg.generated
     */
    public Byte getLevel() {
        return level;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume_skill.level
     *
     * @param level the value for t_resume_skill.level
     *
     * @mbg.generated
     */
    public void setLevel(Byte level) {
        this.level = level;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume_skill.create_time
     *
     * @return the value of t_resume_skill.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume_skill.create_time
     *
     * @param createTime the value for t_resume_skill.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume_skill.update_time
     *
     * @return the value of t_resume_skill.update_time
     *
     * @mbg.generated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume_skill.update_time
     *
     * @param updateTime the value for t_resume_skill.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}