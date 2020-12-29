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
    private Long userId; //用户id|公司id
    private String userType; //个人账户:1/企业账户:2/管理账户:100
    private Integer timeId; //时间id-搜索专用
    private String publisherType; //活动发布者类型
    private Byte status; //活动状态
    private String keyword; //搜索关键词
    public String sortField; //排序字段

}
