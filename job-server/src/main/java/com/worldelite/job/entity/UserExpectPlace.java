package com.worldelite.job.entity;

import java.util.Date;

/**
 * Database Table Remarks:
 *   求职意向地点
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table t_user_expect_place
 */
public class UserExpectPlace {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_user_expect_place.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_user_expect_place.user_id
     *
     * @mbg.generated
     */
    private Long userId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_user_expect_place.city_id
     *
     * @mbg.generated
     */
    private Integer cityId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_user_expect_place.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_user_expect_place.update_time
     *
     * @mbg.generated
     */
    private Date updateTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_user_expect_place.id
     *
     * @return the value of t_user_expect_place.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_user_expect_place.id
     *
     * @param id the value for t_user_expect_place.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_user_expect_place.user_id
     *
     * @return the value of t_user_expect_place.user_id
     *
     * @mbg.generated
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_user_expect_place.user_id
     *
     * @param userId the value for t_user_expect_place.user_id
     *
     * @mbg.generated
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_user_expect_place.city_id
     *
     * @return the value of t_user_expect_place.city_id
     *
     * @mbg.generated
     */
    public Integer getCityId() {
        return cityId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_user_expect_place.city_id
     *
     * @param cityId the value for t_user_expect_place.city_id
     *
     * @mbg.generated
     */
    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_user_expect_place.create_time
     *
     * @return the value of t_user_expect_place.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_user_expect_place.create_time
     *
     * @param createTime the value for t_user_expect_place.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_user_expect_place.update_time
     *
     * @return the value of t_user_expect_place.update_time
     *
     * @mbg.generated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_user_expect_place.update_time
     *
     * @param updateTime the value for t_user_expect_place.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}