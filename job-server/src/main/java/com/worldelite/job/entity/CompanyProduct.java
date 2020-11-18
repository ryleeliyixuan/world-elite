package com.worldelite.job.entity;

import java.util.Date;

/**
 * Database Table Remarks:
 *   企业产品表
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table t_company_product
 */
public class CompanyProduct {
    /**
     * Database Column Remarks:
     *   产品ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_product.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     * Database Column Remarks:
     *   企业ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_product.company_id
     *
     * @mbg.generated
     */
    private Long companyId;

    /**
     * Database Column Remarks:
     *   产品名
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_product.name
     *
     * @mbg.generated
     */
    private String name;

    /**
     * Database Column Remarks:
     *   产品描述
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_product.description
     *
     * @mbg.generated
     */
    private String description;

    /**
     * Database Column Remarks:
     *   图片地址
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_product.image_url
     *
     * @mbg.generated
     */
    private String imageUrl;

    /**
     * Database Column Remarks:
     *   图片名
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_product.image_name
     *
     * @mbg.generated
     */
    private String imageName;

    /**
     * Database Column Remarks:
     *   产品链接
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_product.url
     *
     * @mbg.generated
     */
    private String url;

    /**
     * Database Column Remarks:
     *   排序
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_product.position
     *
     * @mbg.generated
     */
    private Integer position;

    /**
     * Database Column Remarks:
     *   创建时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_product.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     * Database Column Remarks:
     *   更新时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_company_product.update_time
     *
     * @mbg.generated
     */
    private Date updateTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_product.id
     *
     * @return the value of t_company_product.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_product.id
     *
     * @param id the value for t_company_product.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_product.company_id
     *
     * @return the value of t_company_product.company_id
     *
     * @mbg.generated
     */
    public Long getCompanyId() {
        return companyId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_product.company_id
     *
     * @param companyId the value for t_company_product.company_id
     *
     * @mbg.generated
     */
    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_product.name
     *
     * @return the value of t_company_product.name
     *
     * @mbg.generated
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_product.name
     *
     * @param name the value for t_company_product.name
     *
     * @mbg.generated
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_product.description
     *
     * @return the value of t_company_product.description
     *
     * @mbg.generated
     */
    public String getDescription() {
        return description;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_product.description
     *
     * @param description the value for t_company_product.description
     *
     * @mbg.generated
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_product.image_url
     *
     * @return the value of t_company_product.image_url
     *
     * @mbg.generated
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_product.image_url
     *
     * @param imageUrl the value for t_company_product.image_url
     *
     * @mbg.generated
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl == null ? null : imageUrl.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_product.image_name
     *
     * @return the value of t_company_product.image_name
     *
     * @mbg.generated
     */
    public String getImageName() {
        return imageName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_product.image_name
     *
     * @param imageName the value for t_company_product.image_name
     *
     * @mbg.generated
     */
    public void setImageName(String imageName) {
        this.imageName = imageName == null ? null : imageName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_product.url
     *
     * @return the value of t_company_product.url
     *
     * @mbg.generated
     */
    public String getUrl() {
        return url;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_product.url
     *
     * @param url the value for t_company_product.url
     *
     * @mbg.generated
     */
    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_product.position
     *
     * @return the value of t_company_product.position
     *
     * @mbg.generated
     */
    public Integer getPosition() {
        return position;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_product.position
     *
     * @param position the value for t_company_product.position
     *
     * @mbg.generated
     */
    public void setPosition(Integer position) {
        this.position = position;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_product.create_time
     *
     * @return the value of t_company_product.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_product.create_time
     *
     * @param createTime the value for t_company_product.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_company_product.update_time
     *
     * @return the value of t_company_product.update_time
     *
     * @mbg.generated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_company_product.update_time
     *
     * @param updateTime the value for t_company_product.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}