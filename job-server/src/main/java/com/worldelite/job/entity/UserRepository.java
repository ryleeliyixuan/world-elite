package com.worldelite.job.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Database Table Remarks:
 *   企业简历库用户表
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table t_user_repository
 */
public class UserRepository implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_user_repository.id
     *
     * @mbg.generated
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_user_repository.name
     *
     * @mbg.generated
     */
    private String name;

    /**
     * Database Column Remarks:
     *   用户邮箱
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_user_repository.email
     *
     * @mbg.generated
     */
    private String email;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_user_repository.phone_code
     *
     * @mbg.generated
     */
    private String phoneCode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_user_repository.phone
     *
     * @mbg.generated
     */
    private Long phone;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_user_repository.avatar
     *
     * @mbg.generated
     */
    private String avatar;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_user_repository.gender
     *
     * @mbg.generated
     */
    private Byte gender;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_user_repository.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_user_repository.update_time
     *
     * @mbg.generated
     */
    private Date updateTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_user_repository.id
     *
     * @return the value of t_user_repository.id
     *
     * @mbg.generated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_user_repository.id
     *
     * @param id the value for t_user_repository.id
     *
     * @mbg.generated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_user_repository.name
     *
     * @return the value of t_user_repository.name
     *
     * @mbg.generated
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_user_repository.name
     *
     * @param name the value for t_user_repository.name
     *
     * @mbg.generated
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_user_repository.email
     *
     * @return the value of t_user_repository.email
     *
     * @mbg.generated
     */
    public String getEmail() {
        return email;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_user_repository.email
     *
     * @param email the value for t_user_repository.email
     *
     * @mbg.generated
     */
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_user_repository.phone_code
     *
     * @return the value of t_user_repository.phone_code
     *
     * @mbg.generated
     */
    public String getPhoneCode() {
        return phoneCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_user_repository.phone_code
     *
     * @param phoneCode the value for t_user_repository.phone_code
     *
     * @mbg.generated
     */
    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode == null ? null : phoneCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_user_repository.phone
     *
     * @return the value of t_user_repository.phone
     *
     * @mbg.generated
     */
    public Long getPhone() {
        return phone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_user_repository.phone
     *
     * @param phone the value for t_user_repository.phone
     *
     * @mbg.generated
     */
    public void setPhone(Long phone) {
        this.phone = phone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_user_repository.avatar
     *
     * @return the value of t_user_repository.avatar
     *
     * @mbg.generated
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_user_repository.avatar
     *
     * @param avatar the value for t_user_repository.avatar
     *
     * @mbg.generated
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar == null ? null : avatar.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_user_repository.gender
     *
     * @return the value of t_user_repository.gender
     *
     * @mbg.generated
     */
    public Byte getGender() {
        return gender;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_user_repository.gender
     *
     * @param gender the value for t_user_repository.gender
     *
     * @mbg.generated
     */
    public void setGender(Byte gender) {
        this.gender = gender;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_user_repository.create_time
     *
     * @return the value of t_user_repository.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_user_repository.create_time
     *
     * @param createTime the value for t_user_repository.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_user_repository.update_time
     *
     * @return the value of t_user_repository.update_time
     *
     * @mbg.generated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_user_repository.update_time
     *
     * @param updateTime the value for t_user_repository.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}