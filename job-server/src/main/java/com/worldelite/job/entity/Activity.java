package com.worldelite.job.entity;

import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * Database Table Remarks:
 *   活动表
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table t_activity
 */
@EqualsAndHashCode
public class Activity implements Serializable {
    /**
     * Database Column Remarks:
     *   活动id
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     * Database Column Remarks:
     *   发布者id
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity.user_id
     *
     * @mbg.generated
     */
    private Long userId;

    /**
     * Database Column Remarks:
     *   活动形式, 线上0, 线下1
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity.form
     *
     * @mbg.generated
     */
    private Byte form;

    /**
     * Database Column Remarks:
     *   城市id
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity.city_id
     *
     * @mbg.generated
     */
    private Integer cityId;

    /**
     * Database Column Remarks:
     *   发布账户类型(个人账户:1/企业账户:2/管理账户:100)
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity.user_type
     *
     * @mbg.generated
     */
    private String userType;

    /**
     * Database Column Remarks:
     *   活动标题
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity.title
     *
     * @mbg.generated
     */
    private String title;

    /**
     * Database Column Remarks:
     *   活动海报
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity.poster
     *
     * @mbg.generated
     */
    private String poster;

    /**
     * Database Column Remarks:
     *   主办方信息id
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity.organizer_id
     *
     * @mbg.generated
     */
    private Integer organizerId;

    /**
     * Database Column Remarks:
     *   主办方类型
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity.organizer_type
     *
     * @mbg.generated
     */
    private String organizerType;

    /**
     * Database Column Remarks:
     *   活动开始时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity.activity_start_time
     *
     * @mbg.generated
     */
    private Date activityStartTime;

    /**
     * Database Column Remarks:
     *   活动结束时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity.activity_finish_time
     *
     * @mbg.generated
     */
    private Date activityFinishTime;

    /**
     * Database Column Remarks:
     *   报名开始时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity.registration_start_time
     *
     * @mbg.generated
     */
    private Date registrationStartTime;

    /**
     * Database Column Remarks:
     *   报名结束时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity.registration_finish_time
     *
     * @mbg.generated
     */
    private Date registrationFinishTime;

    /**
     * Database Column Remarks:
     *   是否需要报名, 0不需要,1需要
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity.need_registration
     *
     * @mbg.generated
     */
    private Byte needRegistration;

    /**
     * Database Column Remarks:
     *   是否需要报名者简历信息, 0不需要,1需要
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity.need_resume
     *
     * @mbg.generated
     */
    private String needResume;

    /**
     * Database Column Remarks:
     *   是否仅留学生能参加,0不限制,1仅海外
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity.only_overseas_student
     *
     * @mbg.generated
     */
    private String onlyOverseasStudent;

    /**
     * Database Column Remarks:
     *   报名审核类型(是否需要审核),0不需要,1需要
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity.audit_type
     *
     * @mbg.generated
     */
    private String auditType;

    /**
     * Database Column Remarks:
     *   报名人数限制
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity.number_limit
     *
     * @mbg.generated
     */
    private Integer numberLimit;

    /**
     * Database Column Remarks:
     *   详细地址, 如果是线上活动则为链接
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity.address
     *
     * @mbg.generated
     */
    private String address;

    /**
     * Database Column Remarks:
     *   活动状态, 0:审核中 1:草稿,2:下架,3:即将开始,4:报名中,5:进行中,6:已结束,7:审核失败
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity.status
     *
     * @mbg.generated
     */
    private Byte status;

    /**
     * Database Column Remarks:
     *   报名表ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity.questionnaire_id
     *
     * @mbg.generated
     */
    private Integer questionnaireId;

    /**
     * Database Column Remarks:
     *   报名表类型，0=报名表，1=模板
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity.questionnaire_type
     *
     * @mbg.generated
     */
    private String questionnaireType;

    /**
     * Database Column Remarks:
     *   关注人数
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity.follower
     *
     * @mbg.generated
     */
    private Integer follower;

    /**
     * Database Column Remarks:
     *   报名人数
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity.applicant_quantity
     *
     * @mbg.generated
     */
    private Integer applicantQuantity;

    /**
     * Database Column Remarks:
     *   置顶权重
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity.weight
     *
     * @mbg.generated
     */
    private Integer weight;

    /**
     * Database Column Remarks:
     *   发送通知确认提示,0不再提示,1需要提示
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity.send_notice_confirm
     *
     * @mbg.generated
     */
    private String sendNoticeConfirm;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity.del_flag
     *
     * @mbg.generated
     */
    private Byte delFlag;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity.update_time
     *
     * @mbg.generated
     */
    private Date updateTime;

    /**
     * Database Column Remarks:
     *   活动介绍
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_activity.description
     *
     * @mbg.generated
     */
    private String description;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity.id
     *
     * @return the value of t_activity.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity.id
     *
     * @param id the value for t_activity.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity.user_id
     *
     * @return the value of t_activity.user_id
     *
     * @mbg.generated
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity.user_id
     *
     * @param userId the value for t_activity.user_id
     *
     * @mbg.generated
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity.form
     *
     * @return the value of t_activity.form
     *
     * @mbg.generated
     */
    public Byte getForm() {
        return form;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity.form
     *
     * @param form the value for t_activity.form
     *
     * @mbg.generated
     */
    public void setForm(Byte form) {
        this.form = form;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity.city_id
     *
     * @return the value of t_activity.city_id
     *
     * @mbg.generated
     */
    public Integer getCityId() {
        return cityId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity.city_id
     *
     * @param cityId the value for t_activity.city_id
     *
     * @mbg.generated
     */
    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity.user_type
     *
     * @return the value of t_activity.user_type
     *
     * @mbg.generated
     */
    public String getUserType() {
        return userType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity.user_type
     *
     * @param userType the value for t_activity.user_type
     *
     * @mbg.generated
     */
    public void setUserType(String userType) {
        this.userType = userType == null ? null : userType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity.title
     *
     * @return the value of t_activity.title
     *
     * @mbg.generated
     */
    public String getTitle() {
        return title;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity.title
     *
     * @param title the value for t_activity.title
     *
     * @mbg.generated
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity.poster
     *
     * @return the value of t_activity.poster
     *
     * @mbg.generated
     */
    public String getPoster() {
        return poster;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity.poster
     *
     * @param poster the value for t_activity.poster
     *
     * @mbg.generated
     */
    public void setPoster(String poster) {
        this.poster = poster == null ? null : poster.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity.organizer_id
     *
     * @return the value of t_activity.organizer_id
     *
     * @mbg.generated
     */
    public Integer getOrganizerId() {
        return organizerId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity.organizer_id
     *
     * @param organizerId the value for t_activity.organizer_id
     *
     * @mbg.generated
     */
    public void setOrganizerId(Integer organizerId) {
        this.organizerId = organizerId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity.organizer_type
     *
     * @return the value of t_activity.organizer_type
     *
     * @mbg.generated
     */
    public String getOrganizerType() {
        return organizerType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity.organizer_type
     *
     * @param organizerType the value for t_activity.organizer_type
     *
     * @mbg.generated
     */
    public void setOrganizerType(String organizerType) {
        this.organizerType = organizerType == null ? null : organizerType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity.activity_start_time
     *
     * @return the value of t_activity.activity_start_time
     *
     * @mbg.generated
     */
    public Date getActivityStartTime() {
        return activityStartTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity.activity_start_time
     *
     * @param activityStartTime the value for t_activity.activity_start_time
     *
     * @mbg.generated
     */
    public void setActivityStartTime(Date activityStartTime) {
        this.activityStartTime = activityStartTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity.activity_finish_time
     *
     * @return the value of t_activity.activity_finish_time
     *
     * @mbg.generated
     */
    public Date getActivityFinishTime() {
        return activityFinishTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity.activity_finish_time
     *
     * @param activityFinishTime the value for t_activity.activity_finish_time
     *
     * @mbg.generated
     */
    public void setActivityFinishTime(Date activityFinishTime) {
        this.activityFinishTime = activityFinishTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity.registration_start_time
     *
     * @return the value of t_activity.registration_start_time
     *
     * @mbg.generated
     */
    public Date getRegistrationStartTime() {
        return registrationStartTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity.registration_start_time
     *
     * @param registrationStartTime the value for t_activity.registration_start_time
     *
     * @mbg.generated
     */
    public void setRegistrationStartTime(Date registrationStartTime) {
        this.registrationStartTime = registrationStartTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity.registration_finish_time
     *
     * @return the value of t_activity.registration_finish_time
     *
     * @mbg.generated
     */
    public Date getRegistrationFinishTime() {
        return registrationFinishTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity.registration_finish_time
     *
     * @param registrationFinishTime the value for t_activity.registration_finish_time
     *
     * @mbg.generated
     */
    public void setRegistrationFinishTime(Date registrationFinishTime) {
        this.registrationFinishTime = registrationFinishTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity.need_registration
     *
     * @return the value of t_activity.need_registration
     *
     * @mbg.generated
     */
    public Byte getNeedRegistration() {
        return needRegistration;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity.need_registration
     *
     * @param needRegistration the value for t_activity.need_registration
     *
     * @mbg.generated
     */
    public void setNeedRegistration(Byte needRegistration) {
        this.needRegistration = needRegistration;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity.need_resume
     *
     * @return the value of t_activity.need_resume
     *
     * @mbg.generated
     */
    public String getNeedResume() {
        return needResume;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity.need_resume
     *
     * @param needResume the value for t_activity.need_resume
     *
     * @mbg.generated
     */
    public void setNeedResume(String needResume) {
        this.needResume = needResume == null ? null : needResume.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity.only_overseas_student
     *
     * @return the value of t_activity.only_overseas_student
     *
     * @mbg.generated
     */
    public String getOnlyOverseasStudent() {
        return onlyOverseasStudent;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity.only_overseas_student
     *
     * @param onlyOverseasStudent the value for t_activity.only_overseas_student
     *
     * @mbg.generated
     */
    public void setOnlyOverseasStudent(String onlyOverseasStudent) {
        this.onlyOverseasStudent = onlyOverseasStudent == null ? null : onlyOverseasStudent.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity.audit_type
     *
     * @return the value of t_activity.audit_type
     *
     * @mbg.generated
     */
    public String getAuditType() {
        return auditType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity.audit_type
     *
     * @param auditType the value for t_activity.audit_type
     *
     * @mbg.generated
     */
    public void setAuditType(String auditType) {
        this.auditType = auditType == null ? null : auditType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity.number_limit
     *
     * @return the value of t_activity.number_limit
     *
     * @mbg.generated
     */
    public Integer getNumberLimit() {
        return numberLimit;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity.number_limit
     *
     * @param numberLimit the value for t_activity.number_limit
     *
     * @mbg.generated
     */
    public void setNumberLimit(Integer numberLimit) {
        this.numberLimit = numberLimit;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity.address
     *
     * @return the value of t_activity.address
     *
     * @mbg.generated
     */
    public String getAddress() {
        return address;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity.address
     *
     * @param address the value for t_activity.address
     *
     * @mbg.generated
     */
    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity.status
     *
     * @return the value of t_activity.status
     *
     * @mbg.generated
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity.status
     *
     * @param status the value for t_activity.status
     *
     * @mbg.generated
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity.questionnaire_id
     *
     * @return the value of t_activity.questionnaire_id
     *
     * @mbg.generated
     */
    public Integer getQuestionnaireId() {
        return questionnaireId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity.questionnaire_id
     *
     * @param questionnaireId the value for t_activity.questionnaire_id
     *
     * @mbg.generated
     */
    public void setQuestionnaireId(Integer questionnaireId) {
        this.questionnaireId = questionnaireId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity.questionnaire_type
     *
     * @return the value of t_activity.questionnaire_type
     *
     * @mbg.generated
     */
    public String getQuestionnaireType() {
        return questionnaireType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity.questionnaire_type
     *
     * @param questionnaireType the value for t_activity.questionnaire_type
     *
     * @mbg.generated
     */
    public void setQuestionnaireType(String questionnaireType) {
        this.questionnaireType = questionnaireType == null ? null : questionnaireType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity.follower
     *
     * @return the value of t_activity.follower
     *
     * @mbg.generated
     */
    public Integer getFollower() {
        return follower;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity.follower
     *
     * @param follower the value for t_activity.follower
     *
     * @mbg.generated
     */
    public void setFollower(Integer follower) {
        this.follower = follower;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity.applicant_quantity
     *
     * @return the value of t_activity.applicant_quantity
     *
     * @mbg.generated
     */
    public Integer getApplicantQuantity() {
        return applicantQuantity;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity.applicant_quantity
     *
     * @param applicantQuantity the value for t_activity.applicant_quantity
     *
     * @mbg.generated
     */
    public void setApplicantQuantity(Integer applicantQuantity) {
        this.applicantQuantity = applicantQuantity;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity.weight
     *
     * @return the value of t_activity.weight
     *
     * @mbg.generated
     */
    public Integer getWeight() {
        return weight;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity.weight
     *
     * @param weight the value for t_activity.weight
     *
     * @mbg.generated
     */
    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity.send_notice_confirm
     *
     * @return the value of t_activity.send_notice_confirm
     *
     * @mbg.generated
     */
    public String getSendNoticeConfirm() {
        return sendNoticeConfirm;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity.send_notice_confirm
     *
     * @param sendNoticeConfirm the value for t_activity.send_notice_confirm
     *
     * @mbg.generated
     */
    public void setSendNoticeConfirm(String sendNoticeConfirm) {
        this.sendNoticeConfirm = sendNoticeConfirm == null ? null : sendNoticeConfirm.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity.del_flag
     *
     * @return the value of t_activity.del_flag
     *
     * @mbg.generated
     */
    public Byte getDelFlag() {
        return delFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity.del_flag
     *
     * @param delFlag the value for t_activity.del_flag
     *
     * @mbg.generated
     */
    public void setDelFlag(Byte delFlag) {
        this.delFlag = delFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity.create_time
     *
     * @return the value of t_activity.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity.create_time
     *
     * @param createTime the value for t_activity.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity.update_time
     *
     * @return the value of t_activity.update_time
     *
     * @mbg.generated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity.update_time
     *
     * @param updateTime the value for t_activity.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_activity.description
     *
     * @return the value of t_activity.description
     *
     * @mbg.generated
     */
    public String getDescription() {
        return description;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_activity.description
     *
     * @param description the value for t_activity.description
     *
     * @mbg.generated
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }
}