package com.worldelite.job.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table t_message
 */
@ToString
public class Message implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_message.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_message.from_user
     *
     * @mbg.generated
     */
    private Long fromUser;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_message.to_user
     *
     * @mbg.generated
     */
    private Long toUser;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_message.content
     *
     * @mbg.generated
     */
    private String content;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_message.url
     *
     * @mbg.generated
     */
    private String url;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_message.read_flag
     *
     * @mbg.generated
     */
    private Byte readFlag;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_message.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_message.update_time
     *
     * @mbg.generated
     */
    private Date updateTime;

    private Byte msgType;

    public Byte getMsgType() {
        return msgType;
    }

    public void setMsgType(Byte msgType) {
        this.msgType = msgType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_message.id
     *
     * @return the value of t_message.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_message.id
     *
     * @param id the value for t_message.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_message.from_user
     *
     * @return the value of t_message.from_user
     *
     * @mbg.generated
     */
    public Long getFromUser() {
        return fromUser;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_message.from_user
     *
     * @param fromUser the value for t_message.from_user
     *
     * @mbg.generated
     */
    public void setFromUser(Long fromUser) {
        this.fromUser = fromUser;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_message.to_user
     *
     * @return the value of t_message.to_user
     *
     * @mbg.generated
     */
    public Long getToUser() {
        return toUser;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_message.to_user
     *
     * @param toUser the value for t_message.to_user
     *
     * @mbg.generated
     */
    public void setToUser(Long toUser) {
        this.toUser = toUser;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_message.content
     *
     * @return the value of t_message.content
     *
     * @mbg.generated
     */
    public String getContent() {
        return content;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_message.content
     *
     * @param content the value for t_message.content
     *
     * @mbg.generated
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_message.url
     *
     * @return the value of t_message.url
     *
     * @mbg.generated
     */
    public String getUrl() {
        return url;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_message.url
     *
     * @param url the value for t_message.url
     *
     * @mbg.generated
     */
    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_message.read_flag
     *
     * @return the value of t_message.read_flag
     *
     * @mbg.generated
     */
    public Byte getReadFlag() {
        return readFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_message.read_flag
     *
     * @param readFlag the value for t_message.read_flag
     *
     * @mbg.generated
     */
    public void setReadFlag(Byte readFlag) {
        this.readFlag = readFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_message.create_time
     *
     * @return the value of t_message.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_message.create_time
     *
     * @param createTime the value for t_message.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_message.update_time
     *
     * @return the value of t_message.update_time
     *
     * @mbg.generated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_message.update_time
     *
     * @param updateTime the value for t_message.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}