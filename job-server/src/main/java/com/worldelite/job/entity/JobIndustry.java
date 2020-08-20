package com.worldelite.job.entity;

import java.util.Date;

/**
 * Database Table Remarks:
 *   行业表
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table t_job_industry
 */
public class JobIndustry {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_job_industry.id
     *
     * @mbg.generated
     */
    private Long id;

    /**
     * Database Column Remarks:
     *   行业名称
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_job_industry.name
     *
     * @mbg.generated
     */
    private String name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_job_industry.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_job_industry.update_time
     *
     * @mbg.generated
     */
    private Date updateTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_job_industry.del_flag
     *
     * @mbg.generated
     */
    private String delFlag;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_job_industry.id
     *
     * @return the value of t_job_industry.id
     *
     * @mbg.generated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_job_industry.id
     *
     * @param id the value for t_job_industry.id
     *
     * @mbg.generated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_job_industry.name
     *
     * @return the value of t_job_industry.name
     *
     * @mbg.generated
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_job_industry.name
     *
     * @param name the value for t_job_industry.name
     *
     * @mbg.generated
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_job_industry.create_time
     *
     * @return the value of t_job_industry.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_job_industry.create_time
     *
     * @param createTime the value for t_job_industry.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_job_industry.update_time
     *
     * @return the value of t_job_industry.update_time
     *
     * @mbg.generated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_job_industry.update_time
     *
     * @param updateTime the value for t_job_industry.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_job_industry.del_flag
     *
     * @return the value of t_job_industry.del_flag
     *
     * @mbg.generated
     */
    public String getDelFlag() {
        return delFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_job_industry.del_flag
     *
     * @param delFlag the value for t_job_industry.del_flag
     *
     * @mbg.generated
     */
    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag == null ? null : delFlag.trim();
    }
}