package com.worldelite.job.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 活动报名信息数据库查询类
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class RegistrationOptions extends Registration {
    private String keywords; //关键词
}