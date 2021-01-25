package com.worldelite.job.entity;

import java.util.Date;

/**
 * Database Table Remarks:
 *   头像
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table t_avatar
 */
public class Avatar {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_avatar.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     * Database Column Remarks:
     *   头像url链接
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_avatar.avatar_url
     *
     * @mbg.generated
     */
    private String avatarUrl;

    /**
     * Database Column Remarks:
     *   头像类型
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_avatar.type
     *
     * @mbg.generated
     */
    private Byte type;

    /**
     * Database Column Remarks:
     *   删除标记, 0:未删除;1:已删除
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_avatar.del_flag
     *
     * @mbg.generated
     */
    private Byte delFlag;

    /**
     * Database Column Remarks:
     *   创建时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_avatar.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     * Database Column Remarks:
     *   更新时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_avatar.update_time
     *
     * @mbg.generated
     */
    private Date updateTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_avatar.id
     *
     * @return the value of t_avatar.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_avatar.id
     *
     * @param id the value for t_avatar.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_avatar.avatar_url
     *
     * @return the value of t_avatar.avatar_url
     *
     * @mbg.generated
     */
    public String getAvatarUrl() {
        return avatarUrl;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_avatar.avatar_url
     *
     * @param avatarUrl the value for t_avatar.avatar_url
     *
     * @mbg.generated
     */
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl == null ? null : avatarUrl.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_avatar.type
     *
     * @return the value of t_avatar.type
     *
     * @mbg.generated
     */
    public Byte getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_avatar.type
     *
     * @param type the value for t_avatar.type
     *
     * @mbg.generated
     */
    public void setType(Byte type) {
        this.type = type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_avatar.del_flag
     *
     * @return the value of t_avatar.del_flag
     *
     * @mbg.generated
     */
    public Byte getDelFlag() {
        return delFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_avatar.del_flag
     *
     * @param delFlag the value for t_avatar.del_flag
     *
     * @mbg.generated
     */
    public void setDelFlag(Byte delFlag) {
        this.delFlag = delFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_avatar.create_time
     *
     * @return the value of t_avatar.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_avatar.create_time
     *
     * @param createTime the value for t_avatar.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_avatar.update_time
     *
     * @return the value of t_avatar.update_time
     *
     * @mbg.generated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_avatar.update_time
     *
     * @param updateTime the value for t_avatar.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}