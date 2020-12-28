package com.worldelite.job.form;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yeguozhong yedaxia.github.com
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ActivityListForm extends PageForm{
    private Integer[] cityIds; //城市ID,线上国内=999992; 线上国外=999993
    private Long companyId; //公司ID
    private Integer timeId; //时间id
    private String publisherType; //活动发布者类型
    private Byte status; //活动状态
    private String keyword; //搜索关键词
}
