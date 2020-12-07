package com.worldelite.job.form;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 企业评分搜索表单
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CompanyScoreListForm extends PageForm{
    private Long id; //评分ID
    private Long fromId; //评分人ID
    private Long companyId; //企业ID
    private Byte recommend; //是否优质
    private Integer[] scores; //评分列表
    private String[] fromNames; //发帖人名
    private String[] companyNames; //公司名
    private Boolean anonymous; //是否匿名
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime; //开始日期
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date endTime; //结束日期
    private String keyword; //关键词
}
