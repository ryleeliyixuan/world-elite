package com.worldelite.job.entity;

import java.util.Date;

/**
 * Database Table Remarks:
 *   OP帐号表
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table t_op_user
 */
public class OpUser {
    /**
     * Database Column Remarks:
     *   ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_op_user.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     * Database Column Remarks:
     *   用户ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_op_user.user_id
     *
     * @mbg.generated
     */
    private Long userId;

    /**
     * Database Column Remarks:
     *   创建时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_op_user.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     * Database Column Remarks:
     *   更新时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_op_user.update_time
     *
     * @mbg.generated
     */
    private Date updateTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_op_user.id
     *
     * @return the value of t_op_user.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_op_user.id
     *
     * @param id the value for t_op_user.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_op_user.user_id
     *
     * @return the value of t_op_user.user_id
     *
     * @mbg.generated
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_op_user.user_id
     *
     * @param userId the value for t_op_user.user_id
     *
     * @mbg.generated
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_op_user.create_time
     *
     * @return the value of t_op_user.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_op_user.create_time
     *
     * @param createTime the value for t_op_user.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_op_user.update_time
     *
     * @return the value of t_op_user.update_time
     *
     * @mbg.generated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_op_user.update_time
     *
     * @param updateTime the value for t_op_user.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}