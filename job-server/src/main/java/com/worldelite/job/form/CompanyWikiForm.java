package com.worldelite.job.form;

import lombok.Data;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class CompanyWikiForm {
    private Long companyId; //公司ID
    private String summary; //摘要
    private String content; //百科详情
    private String video; //公司视频
    private Integer cityId; //地址ID
    private Byte cityEnable; //是否启用地址模块
    private Byte employeeEnable; //是否启用雇员数量模块
    private Byte productEnable; //是否启用产品模块
    private Byte marketEnable; //是否启用市值模块
    private Byte historyEnable; //是否启用发展路径模块
    private Byte structureEnable; //是否启用组织架构模块
}
