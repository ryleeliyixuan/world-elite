package com.worldelite.job.entity;

import java.util.Date;

/**
 * Database Table Remarks:
 *   首页轮播图
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table t_config_carousel
 */
public class ConfigCarousel {
    /**
     * Database Column Remarks:
     *   ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_config_carousel.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     * Database Column Remarks:
     *   图片名
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_config_carousel.name
     *
     * @mbg.generated
     */
    private String name;

    /**
     * Database Column Remarks:
     *   图片路径
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_config_carousel.url
     *
     * @mbg.generated
     */
    private String url;

    /**
     * Database Column Remarks:
     *   跳转链接
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_config_carousel.target
     *
     * @mbg.generated
     */
    private String target;

    /**
     * Database Column Remarks:
     *   排序
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_config_carousel.position
     *
     * @mbg.generated
     */
    private Integer position;

    /**
     * Database Column Remarks:
     *   创建时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_config_carousel.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     * Database Column Remarks:
     *   更新时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_config_carousel.update_time
     *
     * @mbg.generated
     */
    private Date updateTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_config_carousel.id
     *
     * @return the value of t_config_carousel.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_config_carousel.id
     *
     * @param id the value for t_config_carousel.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_config_carousel.name
     *
     * @return the value of t_config_carousel.name
     *
     * @mbg.generated
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_config_carousel.name
     *
     * @param name the value for t_config_carousel.name
     *
     * @mbg.generated
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_config_carousel.url
     *
     * @return the value of t_config_carousel.url
     *
     * @mbg.generated
     */
    public String getUrl() {
        return url;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_config_carousel.url
     *
     * @param url the value for t_config_carousel.url
     *
     * @mbg.generated
     */
    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_config_carousel.target
     *
     * @return the value of t_config_carousel.target
     *
     * @mbg.generated
     */
    public String getTarget() {
        return target;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_config_carousel.target
     *
     * @param target the value for t_config_carousel.target
     *
     * @mbg.generated
     */
    public void setTarget(String target) {
        this.target = target == null ? null : target.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_config_carousel.position
     *
     * @return the value of t_config_carousel.position
     *
     * @mbg.generated
     */
    public Integer getPosition() {
        return position;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_config_carousel.position
     *
     * @param position the value for t_config_carousel.position
     *
     * @mbg.generated
     */
    public void setPosition(Integer position) {
        this.position = position;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_config_carousel.create_time
     *
     * @return the value of t_config_carousel.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_config_carousel.create_time
     *
     * @param createTime the value for t_config_carousel.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_config_carousel.update_time
     *
     * @return the value of t_config_carousel.update_time
     *
     * @mbg.generated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_config_carousel.update_time
     *
     * @param updateTime the value for t_config_carousel.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}