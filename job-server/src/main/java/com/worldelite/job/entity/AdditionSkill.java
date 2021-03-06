package com.worldelite.job.entity;

import java.util.Date;

/**
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table t_addition_skill
 */
public class AdditionSkill {
    /**
     * Database Column Remarks:
     *   技能ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_addition_skill.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     * Database Column Remarks:
     *   技能名称
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_addition_skill.name
     *
     * @mbg.generated
     */
    private String name;

    /**
     * Database Column Remarks:
     *   创建者ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_addition_skill.creator_id
     *
     * @mbg.generated
     */
    private Long creatorId;

    /**
     * Database Column Remarks:
     *   职位类型ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_addition_skill.category_id
     *
     * @mbg.generated
     */
    private Long categoryId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_addition_skill.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_addition_skill.update_time
     *
     * @mbg.generated
     */
    private Date updateTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_addition_skill.del_flag
     *
     * @mbg.generated
     */
    private Byte delFlag;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_addition_skill.id
     *
     * @return the value of t_addition_skill.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_addition_skill.id
     *
     * @param id the value for t_addition_skill.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_addition_skill.name
     *
     * @return the value of t_addition_skill.name
     *
     * @mbg.generated
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_addition_skill.name
     *
     * @param name the value for t_addition_skill.name
     *
     * @mbg.generated
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_addition_skill.creator_id
     *
     * @return the value of t_addition_skill.creator_id
     *
     * @mbg.generated
     */
    public Long getCreatorId() {
        return creatorId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_addition_skill.creator_id
     *
     * @param creatorId the value for t_addition_skill.creator_id
     *
     * @mbg.generated
     */
    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_addition_skill.category_id
     *
     * @return the value of t_addition_skill.category_id
     *
     * @mbg.generated
     */
    public Long getCategoryId() {
        return categoryId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_addition_skill.category_id
     *
     * @param categoryId the value for t_addition_skill.category_id
     *
     * @mbg.generated
     */
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_addition_skill.create_time
     *
     * @return the value of t_addition_skill.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_addition_skill.create_time
     *
     * @param createTime the value for t_addition_skill.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_addition_skill.update_time
     *
     * @return the value of t_addition_skill.update_time
     *
     * @mbg.generated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_addition_skill.update_time
     *
     * @param updateTime the value for t_addition_skill.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_addition_skill.del_flag
     *
     * @return the value of t_addition_skill.del_flag
     *
     * @mbg.generated
     */
    public Byte getDelFlag() {
        return delFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_addition_skill.del_flag
     *
     * @param delFlag the value for t_addition_skill.del_flag
     *
     * @mbg.generated
     */
    public void setDelFlag(Byte delFlag) {
        this.delFlag = delFlag;
    }
}