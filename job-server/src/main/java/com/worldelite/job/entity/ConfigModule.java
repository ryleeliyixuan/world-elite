package com.worldelite.job.entity;

import java.util.Date;

/**
 * Database Table Remarks:
 *   首页配置模块表
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table t_config_module
 */
public class ConfigModule {
    /**
     * Database Column Remarks:
     *   ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_config_module.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     * Database Column Remarks:
     *   模块名
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_config_module.name
     *
     * @mbg.generated
     */
    private String name;

    /**
     * Database Column Remarks:
     *   模板类型
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_config_module.type
     *
     * @mbg.generated
     */
    private Byte type;

    /**
     * Database Column Remarks:
     *   配置类型
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_config_module.config_type
     *
     * @mbg.generated
     */
    private Byte configType;

    /**
     * Database Column Remarks:
     *   配置值
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_config_module.value
     *
     * @mbg.generated
     */
    private Byte value;

    /**
     * Database Column Remarks:
     *   排序
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_config_module.position
     *
     * @mbg.generated
     */
    private Integer position;

    /**
     * Database Column Remarks:
     *   创建时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_config_module.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     * Database Column Remarks:
     *   更新时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_config_module.update_time
     *
     * @mbg.generated
     */
    private Date updateTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_config_module.id
     *
     * @return the value of t_config_module.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_config_module.id
     *
     * @param id the value for t_config_module.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_config_module.name
     *
     * @return the value of t_config_module.name
     *
     * @mbg.generated
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_config_module.name
     *
     * @param name the value for t_config_module.name
     *
     * @mbg.generated
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_config_module.type
     *
     * @return the value of t_config_module.type
     *
     * @mbg.generated
     */
    public Byte getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_config_module.type
     *
     * @param type the value for t_config_module.type
     *
     * @mbg.generated
     */
    public void setType(Byte type) {
        this.type = type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_config_module.config_type
     *
     * @return the value of t_config_module.config_type
     *
     * @mbg.generated
     */
    public Byte getConfigType() {
        return configType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_config_module.config_type
     *
     * @param configType the value for t_config_module.config_type
     *
     * @mbg.generated
     */
    public void setConfigType(Byte configType) {
        this.configType = configType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_config_module.value
     *
     * @return the value of t_config_module.value
     *
     * @mbg.generated
     */
    public Byte getValue() {
        return value;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_config_module.value
     *
     * @param value the value for t_config_module.value
     *
     * @mbg.generated
     */
    public void setValue(Byte value) {
        this.value = value;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_config_module.position
     *
     * @return the value of t_config_module.position
     *
     * @mbg.generated
     */
    public Integer getPosition() {
        return position;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_config_module.position
     *
     * @param position the value for t_config_module.position
     *
     * @mbg.generated
     */
    public void setPosition(Integer position) {
        this.position = position;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_config_module.create_time
     *
     * @return the value of t_config_module.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_config_module.create_time
     *
     * @param createTime the value for t_config_module.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_config_module.update_time
     *
     * @return the value of t_config_module.update_time
     *
     * @mbg.generated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_config_module.update_time
     *
     * @param updateTime the value for t_config_module.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}