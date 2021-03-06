package com.worldelite.job.entity;

import java.util.Date;

/**
 * Database Table Remarks:
 *   自定义技能标签表
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table t_addition_tag
 */
public class AdditionTag {
    /**
     * Database Column Remarks:
     *   技能ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_addition_tag.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     * Database Column Remarks:
     *   技能名称
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_addition_tag.name
     *
     * @mbg.generated
     */
    private String name;

    /**
     * Database Column Remarks:
     *   创建者ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_addition_tag.creator_id
     *
     * @mbg.generated
     */
    private Long creatorId;

    /**
     * Database Column Remarks:
     *   技能类型
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_addition_tag.type
     *
     * @mbg.generated
     */
    private Integer type;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_addition_tag.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_addition_tag.update_time
     *
     * @mbg.generated
     */
    private Date updateTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_addition_tag.del_flag
     *
     * @mbg.generated
     */
    private Byte delFlag;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_addition_tag.id
     *
     * @return the value of t_addition_tag.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_addition_tag.id
     *
     * @param id the value for t_addition_tag.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_addition_tag.name
     *
     * @return the value of t_addition_tag.name
     *
     * @mbg.generated
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_addition_tag.name
     *
     * @param name the value for t_addition_tag.name
     *
     * @mbg.generated
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_addition_tag.creator_id
     *
     * @return the value of t_addition_tag.creator_id
     *
     * @mbg.generated
     */
    public Long getCreatorId() {
        return creatorId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_addition_tag.creator_id
     *
     * @param creatorId the value for t_addition_tag.creator_id
     *
     * @mbg.generated
     */
    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_addition_tag.type
     *
     * @return the value of t_addition_tag.type
     *
     * @mbg.generated
     */
    public Integer getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_addition_tag.type
     *
     * @param type the value for t_addition_tag.type
     *
     * @mbg.generated
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_addition_tag.create_time
     *
     * @return the value of t_addition_tag.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_addition_tag.create_time
     *
     * @param createTime the value for t_addition_tag.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_addition_tag.update_time
     *
     * @return the value of t_addition_tag.update_time
     *
     * @mbg.generated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_addition_tag.update_time
     *
     * @param updateTime the value for t_addition_tag.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_addition_tag.del_flag
     *
     * @return the value of t_addition_tag.del_flag
     *
     * @mbg.generated
     */
    public Byte getDelFlag() {
        return delFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_addition_tag.del_flag
     *
     * @param delFlag the value for t_addition_tag.del_flag
     *
     * @mbg.generated
     */
    public void setDelFlag(Byte delFlag) {
        this.delFlag = delFlag;
    }
}