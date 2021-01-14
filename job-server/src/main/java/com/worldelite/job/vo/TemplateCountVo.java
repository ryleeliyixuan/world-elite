package com.worldelite.job.vo;

import lombok.Data;

/**
 * 报名表模板数量视图
 */
@Data
public class TemplateCountVo{
    private Integer count; //当前模板数量
    private Integer maxCount; //模板最大数量
}
