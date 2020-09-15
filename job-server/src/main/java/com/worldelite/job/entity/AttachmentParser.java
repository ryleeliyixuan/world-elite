package com.worldelite.job.entity;

import java.util.Date;

/**
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table t_attachment_parser
 */
public class AttachmentParser {
    /**
     * Database Column Remarks:
     *   解析ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_attachment_parser.id
     *
     * @mbg.generated
     */
    private Long id;

    /**
     * Database Column Remarks:
     *   邮箱
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_attachment_parser.email
     *
     * @mbg.generated
     */
    private String email;

    /**
     * Database Column Remarks:
     *   记录时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_attachment_parser.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     * Database Column Remarks:
     *   更新时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_attachment_parser.update_time
     *
     * @mbg.generated
     */
    private Date updateTime;

    /**
     * Database Column Remarks:
     *   删除标识
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_attachment_parser.del_flag
     *
     * @mbg.generated
     */
    private Byte delFlag;

    /**
     * Database Column Remarks:
     *   附件简历内容
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_attachment_parser.attach_content
     *
     * @mbg.generated
     */
    private String attachContent;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_attachment_parser.id
     *
     * @return the value of t_attachment_parser.id
     *
     * @mbg.generated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_attachment_parser.id
     *
     * @param id the value for t_attachment_parser.id
     *
     * @mbg.generated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_attachment_parser.email
     *
     * @return the value of t_attachment_parser.email
     *
     * @mbg.generated
     */
    public String getEmail() {
        return email;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_attachment_parser.email
     *
     * @param email the value for t_attachment_parser.email
     *
     * @mbg.generated
     */
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_attachment_parser.create_time
     *
     * @return the value of t_attachment_parser.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_attachment_parser.create_time
     *
     * @param createTime the value for t_attachment_parser.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_attachment_parser.update_time
     *
     * @return the value of t_attachment_parser.update_time
     *
     * @mbg.generated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_attachment_parser.update_time
     *
     * @param updateTime the value for t_attachment_parser.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_attachment_parser.del_flag
     *
     * @return the value of t_attachment_parser.del_flag
     *
     * @mbg.generated
     */
    public Byte getDelFlag() {
        return delFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_attachment_parser.del_flag
     *
     * @param delFlag the value for t_attachment_parser.del_flag
     *
     * @mbg.generated
     */
    public void setDelFlag(Byte delFlag) {
        this.delFlag = delFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_attachment_parser.attach_content
     *
     * @return the value of t_attachment_parser.attach_content
     *
     * @mbg.generated
     */
    public String getAttachContent() {
        return attachContent;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_attachment_parser.attach_content
     *
     * @param attachContent the value for t_attachment_parser.attach_content
     *
     * @mbg.generated
     */
    public void setAttachContent(String attachContent) {
        this.attachContent = attachContent == null ? null : attachContent.trim();
    }
}