package com.worldelite.job.entity;

import java.util.Date;

/**
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table t_city
 */
public class City {
    /**
     * Database Column Remarks:
     *   城市ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_city.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     * Database Column Remarks:
     *   父级ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_city.parent_id
     *
     * @mbg.generated
     */
    private Integer parentId;

    /**
     * Database Column Remarks:
     *   名称
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_city.name
     *
     * @mbg.generated
     */
    private String name;

    /**
     * Database Column Remarks:
     *   行政区划代码
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_city.code
     *
     * @mbg.generated
     */
    private String code;

    /**
     * Database Column Remarks:
     *   固定电话区号
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_city.phone_code
     *
     * @mbg.generated
     */
    private String phoneCode;

    /**
     * Database Column Remarks:
     *   邮政编码
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_city.zip_code
     *
     * @mbg.generated
     */
    private String zipCode;

    /**
     * Database Column Remarks:
     *   简称
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_city.short_name
     *
     * @mbg.generated
     */
    private String shortName;

    /**
     * Database Column Remarks:
     *   拼音
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_city.pinyin
     *
     * @mbg.generated
     */
    private String pinyin;

    /**
     * Database Column Remarks:
     *   简拼
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_city.py
     *
     * @mbg.generated
     */
    private String py;

    /**
     * Database Column Remarks:
     *   首字母
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_city.first_char
     *
     * @mbg.generated
     */
    private String firstChar;

    /**
     * Database Column Remarks:
     *   英文
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_city.english
     *
     * @mbg.generated
     */
    private String english;

    /**
     * Database Column Remarks:
     *   经度
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_city.lng
     *
     * @mbg.generated
     */
    private String lng;

    /**
     * Database Column Remarks:
     *   纬度
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_city.lat
     *
     * @mbg.generated
     */
    private String lat;

    /**
     * Database Column Remarks:
     *   城市类型
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_city.type
     *
     * @mbg.generated
     */
    private Byte type;

    /**
     * Database Column Remarks:
     *   创建时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_city.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     * Database Column Remarks:
     *   更新时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_city.update_time
     *
     * @mbg.generated
     */
    private Date updateTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_city.id
     *
     * @return the value of t_city.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_city.id
     *
     * @param id the value for t_city.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_city.parent_id
     *
     * @return the value of t_city.parent_id
     *
     * @mbg.generated
     */
    public Integer getParentId() {
        return parentId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_city.parent_id
     *
     * @param parentId the value for t_city.parent_id
     *
     * @mbg.generated
     */
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_city.name
     *
     * @return the value of t_city.name
     *
     * @mbg.generated
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_city.name
     *
     * @param name the value for t_city.name
     *
     * @mbg.generated
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_city.code
     *
     * @return the value of t_city.code
     *
     * @mbg.generated
     */
    public String getCode() {
        return code;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_city.code
     *
     * @param code the value for t_city.code
     *
     * @mbg.generated
     */
    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_city.phone_code
     *
     * @return the value of t_city.phone_code
     *
     * @mbg.generated
     */
    public String getPhoneCode() {
        return phoneCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_city.phone_code
     *
     * @param phoneCode the value for t_city.phone_code
     *
     * @mbg.generated
     */
    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode == null ? null : phoneCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_city.zip_code
     *
     * @return the value of t_city.zip_code
     *
     * @mbg.generated
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_city.zip_code
     *
     * @param zipCode the value for t_city.zip_code
     *
     * @mbg.generated
     */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode == null ? null : zipCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_city.short_name
     *
     * @return the value of t_city.short_name
     *
     * @mbg.generated
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_city.short_name
     *
     * @param shortName the value for t_city.short_name
     *
     * @mbg.generated
     */
    public void setShortName(String shortName) {
        this.shortName = shortName == null ? null : shortName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_city.pinyin
     *
     * @return the value of t_city.pinyin
     *
     * @mbg.generated
     */
    public String getPinyin() {
        return pinyin;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_city.pinyin
     *
     * @param pinyin the value for t_city.pinyin
     *
     * @mbg.generated
     */
    public void setPinyin(String pinyin) {
        this.pinyin = pinyin == null ? null : pinyin.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_city.py
     *
     * @return the value of t_city.py
     *
     * @mbg.generated
     */
    public String getPy() {
        return py;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_city.py
     *
     * @param py the value for t_city.py
     *
     * @mbg.generated
     */
    public void setPy(String py) {
        this.py = py == null ? null : py.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_city.first_char
     *
     * @return the value of t_city.first_char
     *
     * @mbg.generated
     */
    public String getFirstChar() {
        return firstChar;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_city.first_char
     *
     * @param firstChar the value for t_city.first_char
     *
     * @mbg.generated
     */
    public void setFirstChar(String firstChar) {
        this.firstChar = firstChar == null ? null : firstChar.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_city.english
     *
     * @return the value of t_city.english
     *
     * @mbg.generated
     */
    public String getEnglish() {
        return english;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_city.english
     *
     * @param english the value for t_city.english
     *
     * @mbg.generated
     */
    public void setEnglish(String english) {
        this.english = english == null ? null : english.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_city.lng
     *
     * @return the value of t_city.lng
     *
     * @mbg.generated
     */
    public String getLng() {
        return lng;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_city.lng
     *
     * @param lng the value for t_city.lng
     *
     * @mbg.generated
     */
    public void setLng(String lng) {
        this.lng = lng == null ? null : lng.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_city.lat
     *
     * @return the value of t_city.lat
     *
     * @mbg.generated
     */
    public String getLat() {
        return lat;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_city.lat
     *
     * @param lat the value for t_city.lat
     *
     * @mbg.generated
     */
    public void setLat(String lat) {
        this.lat = lat == null ? null : lat.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_city.type
     *
     * @return the value of t_city.type
     *
     * @mbg.generated
     */
    public Byte getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_city.type
     *
     * @param type the value for t_city.type
     *
     * @mbg.generated
     */
    public void setType(Byte type) {
        this.type = type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_city.create_time
     *
     * @return the value of t_city.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_city.create_time
     *
     * @param createTime the value for t_city.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_city.update_time
     *
     * @return the value of t_city.update_time
     *
     * @mbg.generated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_city.update_time
     *
     * @param updateTime the value for t_city.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}