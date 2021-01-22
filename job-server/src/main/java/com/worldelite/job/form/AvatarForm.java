package com.worldelite.job.form;

import lombok.Data;

import java.util.Date;

/**
 * @author Xiang Chao
 **/
@Data
public class AvatarForm {

    private String id;

    /**
     * 头像url链接
     */
    private String avatarUrl;

    /**
     * 头像类型
     */
    private Byte type;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
