package com.worldelite.job.entity;

import java.util.Date;

/**
 * Database Table Remarks:
 *   公司模块推荐表
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table t_company_recommend
 */
public class CompanyRecommend {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_recommend.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_recommend.company_id
     *
     * @mbg.generated
     */
    private Long companyId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_recommend.object_id
     *
     * @mbg.generated
     */
    private Long objectId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_recommend.object_type
     *
     * @mbg.generated
     */
    private Byte objectType;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_recommend.position
     *
     * @mbg.generated
     */
    private Integer position;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_recommend.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_recommend.update_time
     *
     * @mbg.generated
     */
    private Date updateTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_recommend.id
     *
     * @return the value of t_company_recommend.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_recommend.id
     *
     * @param id the value for t_company_recommend.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_recommend.company_id
     *
     * @return the value of t_company_recommend.company_id
     *
     * @mbg.generated
     */
    public Long getCompanyId() {
        return companyId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_recommend.company_id
     *
     * @param companyId the value for t_company_recommend.company_id
     *
     * @mbg.generated
     */
    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_recommend.object_id
     *
     * @return the value of t_company_recommend.object_id
     *
     * @mbg.generated
     */
    public Long getObjectId() {
        return objectId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_recommend.object_id
     *
     * @param objectId the value for t_company_recommend.object_id
     *
     * @mbg.generated
     */
    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_recommend.object_type
     *
     * @return the value of t_company_recommend.object_type
     *
     * @mbg.generated
     */
    public Byte getObjectType() {
        return objectType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_recommend.object_type
     *
     * @param objectType the value for t_company_recommend.object_type
     *
     * @mbg.generated
     */
    public void setObjectType(Byte objectType) {
        this.objectType = objectType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_recommend.position
     *
     * @return the value of t_company_recommend.position
     *
     * @mbg.generated
     */
    public Integer getPosition() {
        return position;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_recommend.position
     *
     * @param position the value for t_company_recommend.position
     *
     * @mbg.generated
     */
    public void setPosition(Integer position) {
        this.position = position;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_recommend.create_time
     *
     * @return the value of t_company_recommend.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_recommend.create_time
     *
     * @param createTime the value for t_company_recommend.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_recommend.update_time
     *
     * @return the value of t_company_recommend.update_time
     *
     * @mbg.generated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_recommend.update_time
     *
     * @param updateTime the value for t_company_recommend.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}