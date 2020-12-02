package com.worldelite.job.form;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 企业帖子搜索表单
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CompanyPostListForm extends PageForm{
    private Long id; //帖子ID
    private Long companyId; //企业ID
    private Integer cliqueId; //圈子ID
    private Byte recommend; //是否加精
    private String title; //帖子标题
    private String[] fromNames; //发帖人名
    private String[] companyNames; //公司名
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime; //开始日期
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date endTime; //结束日期
    private String keyword; //关键词
}
