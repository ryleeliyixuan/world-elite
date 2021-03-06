package com.worldelite.job.entity;

import java.util.Date;

/**
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table t_resume
 */
public class Resume {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume.id
     *
     * @mbg.generated
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume.user_id
     *
     * @mbg.generated
     */
    private Long userId;

    /**
     * Database Column Remarks:
     *   姓名
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume.name
     *
     * @mbg.generated
     */
    private String name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume.birth
     *
     * @mbg.generated
     */
    private Date birth;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume.gender
     *
     * @mbg.generated
     */
    private Byte gender;

    /**
     * Database Column Remarks:
     *   国籍
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume.country_id
     *
     * @mbg.generated
     */
    private Integer countryId;

    /**
     * Database Column Remarks:
     *   最高学历
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume.max_degree_id
     *
     * @mbg.generated
     */
    private Integer maxDegreeId;

    /**
     * Database Column Remarks:
     *   婚姻状况
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume.marital_status
     *
     * @mbg.generated
     */
    private Byte maritalStatus;

    /**
     * Database Column Remarks:
     *   回国时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume.return_time
     *
     * @mbg.generated
     */
    private Date returnTime;

    /**
     * Database Column Remarks:
     *   毕业时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume.graduate_time
     *
     * @mbg.generated
     */
    private Date graduateTime;

    /**
     * Database Column Remarks:
     *   现居地址
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume.cur_place
     *
     * @mbg.generated
     */
    private String curPlace;

    /**
     * Database Column Remarks:
     *   语言
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume.lang
     *
     * @mbg.generated
     */
    private Byte lang;

    /**
     * Database Column Remarks:
     *   附件简历
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume.attach_resume
     *
     * @mbg.generated
     */
    private String attachResume;

    /**
     * Database Column Remarks:
     *   附件简历名
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume.attach_resume_name
     *
     * @mbg.generated
     */
    private String attachResumeName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume.update_time
     *
     * @mbg.generated
     */
    private Date updateTime;

    /**
     * Database Column Remarks:
     *   简历状态
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume.status
     *
     * @mbg.generated
     */
    private Byte status;

    /**
     * Database Column Remarks:
     *   简历类型
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume.type
     *
     * @mbg.generated
     */
    private Byte type;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume.priority
     *
     * @mbg.generated
     */
    private Byte priority;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume.email
     *
     * @mbg.generated
     */
    private String email;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume.phone
     *
     * @mbg.generated
     */
    private String phone;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume.avatar
     *
     * @mbg.generated
     */
    private String avatar;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume.title
     *
     * @mbg.generated
     */
    private String title;

    /**
     * Database Column Remarks:
     *   自我介绍
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_resume.introduction
     *
     * @mbg.generated
     */
    private String introduction;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume.id
     *
     * @return the value of t_resume.id
     *
     * @mbg.generated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume.id
     *
     * @param id the value for t_resume.id
     *
     * @mbg.generated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume.user_id
     *
     * @return the value of t_resume.user_id
     *
     * @mbg.generated
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume.user_id
     *
     * @param userId the value for t_resume.user_id
     *
     * @mbg.generated
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume.name
     *
     * @return the value of t_resume.name
     *
     * @mbg.generated
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume.name
     *
     * @param name the value for t_resume.name
     *
     * @mbg.generated
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume.birth
     *
     * @return the value of t_resume.birth
     *
     * @mbg.generated
     */
    public Date getBirth() {
        return birth;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume.birth
     *
     * @param birth the value for t_resume.birth
     *
     * @mbg.generated
     */
    public void setBirth(Date birth) {
        this.birth = birth;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume.gender
     *
     * @return the value of t_resume.gender
     *
     * @mbg.generated
     */
    public Byte getGender() {
        return gender;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume.gender
     *
     * @param gender the value for t_resume.gender
     *
     * @mbg.generated
     */
    public void setGender(Byte gender) {
        this.gender = gender;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume.country_id
     *
     * @return the value of t_resume.country_id
     *
     * @mbg.generated
     */
    public Integer getCountryId() {
        return countryId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume.country_id
     *
     * @param countryId the value for t_resume.country_id
     *
     * @mbg.generated
     */
    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume.max_degree_id
     *
     * @return the value of t_resume.max_degree_id
     *
     * @mbg.generated
     */
    public Integer getMaxDegreeId() {
        return maxDegreeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume.max_degree_id
     *
     * @param maxDegreeId the value for t_resume.max_degree_id
     *
     * @mbg.generated
     */
    public void setMaxDegreeId(Integer maxDegreeId) {
        this.maxDegreeId = maxDegreeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume.marital_status
     *
     * @return the value of t_resume.marital_status
     *
     * @mbg.generated
     */
    public Byte getMaritalStatus() {
        return maritalStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume.marital_status
     *
     * @param maritalStatus the value for t_resume.marital_status
     *
     * @mbg.generated
     */
    public void setMaritalStatus(Byte maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume.return_time
     *
     * @return the value of t_resume.return_time
     *
     * @mbg.generated
     */
    public Date getReturnTime() {
        return returnTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume.return_time
     *
     * @param returnTime the value for t_resume.return_time
     *
     * @mbg.generated
     */
    public void setReturnTime(Date returnTime) {
        this.returnTime = returnTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume.graduate_time
     *
     * @return the value of t_resume.graduate_time
     *
     * @mbg.generated
     */
    public Date getGraduateTime() {
        return graduateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume.graduate_time
     *
     * @param graduateTime the value for t_resume.graduate_time
     *
     * @mbg.generated
     */
    public void setGraduateTime(Date graduateTime) {
        this.graduateTime = graduateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume.cur_place
     *
     * @return the value of t_resume.cur_place
     *
     * @mbg.generated
     */
    public String getCurPlace() {
        return curPlace;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume.cur_place
     *
     * @param curPlace the value for t_resume.cur_place
     *
     * @mbg.generated
     */
    public void setCurPlace(String curPlace) {
        this.curPlace = curPlace == null ? null : curPlace.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume.lang
     *
     * @return the value of t_resume.lang
     *
     * @mbg.generated
     */
    public Byte getLang() {
        return lang;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume.lang
     *
     * @param lang the value for t_resume.lang
     *
     * @mbg.generated
     */
    public void setLang(Byte lang) {
        this.lang = lang;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume.attach_resume
     *
     * @return the value of t_resume.attach_resume
     *
     * @mbg.generated
     */
    public String getAttachResume() {
        return attachResume;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume.attach_resume
     *
     * @param attachResume the value for t_resume.attach_resume
     *
     * @mbg.generated
     */
    public void setAttachResume(String attachResume) {
        this.attachResume = attachResume == null ? null : attachResume.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume.attach_resume_name
     *
     * @return the value of t_resume.attach_resume_name
     *
     * @mbg.generated
     */
    public String getAttachResumeName() {
        return attachResumeName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume.attach_resume_name
     *
     * @param attachResumeName the value for t_resume.attach_resume_name
     *
     * @mbg.generated
     */
    public void setAttachResumeName(String attachResumeName) {
        this.attachResumeName = attachResumeName == null ? null : attachResumeName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume.create_time
     *
     * @return the value of t_resume.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume.create_time
     *
     * @param createTime the value for t_resume.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume.update_time
     *
     * @return the value of t_resume.update_time
     *
     * @mbg.generated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume.update_time
     *
     * @param updateTime the value for t_resume.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume.status
     *
     * @return the value of t_resume.status
     *
     * @mbg.generated
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume.status
     *
     * @param status the value for t_resume.status
     *
     * @mbg.generated
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume.type
     *
     * @return the value of t_resume.type
     *
     * @mbg.generated
     */
    public Byte getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume.type
     *
     * @param type the value for t_resume.type
     *
     * @mbg.generated
     */
    public void setType(Byte type) {
        this.type = type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume.priority
     *
     * @return the value of t_resume.priority
     *
     * @mbg.generated
     */
    public Byte getPriority() {
        return priority;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume.priority
     *
     * @param priority the value for t_resume.priority
     *
     * @mbg.generated
     */
    public void setPriority(Byte priority) {
        this.priority = priority;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume.email
     *
     * @return the value of t_resume.email
     *
     * @mbg.generated
     */
    public String getEmail() {
        return email;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume.email
     *
     * @param email the value for t_resume.email
     *
     * @mbg.generated
     */
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume.phone
     *
     * @return the value of t_resume.phone
     *
     * @mbg.generated
     */
    public String getPhone() {
        return phone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume.phone
     *
     * @param phone the value for t_resume.phone
     *
     * @mbg.generated
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume.avatar
     *
     * @return the value of t_resume.avatar
     *
     * @mbg.generated
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume.avatar
     *
     * @param avatar the value for t_resume.avatar
     *
     * @mbg.generated
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar == null ? null : avatar.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume.title
     *
     * @return the value of t_resume.title
     *
     * @mbg.generated
     */
    public String getTitle() {
        return title;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume.title
     *
     * @param title the value for t_resume.title
     *
     * @mbg.generated
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_resume.introduction
     *
     * @return the value of t_resume.introduction
     *
     * @mbg.generated
     */
    public String getIntroduction() {
        return introduction;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_resume.introduction
     *
     * @param introduction the value for t_resume.introduction
     *
     * @mbg.generated
     */
    public void setIntroduction(String introduction) {
        this.introduction = introduction == null ? null : introduction.trim();
    }
}