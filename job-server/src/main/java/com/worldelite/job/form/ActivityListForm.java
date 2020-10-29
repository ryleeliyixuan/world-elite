package com.worldelite.job.form;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yeguozhong yedaxia.github.com
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ActivityListForm extends PageForm{
    private Integer cityId; //所在城市
    private Integer[] cityIds; //城市ID
    private Long companyId; //公司ID
    private Byte status; //活动状态
    private String title; //活动标题
    private String keyword; //搜索关键词
}
