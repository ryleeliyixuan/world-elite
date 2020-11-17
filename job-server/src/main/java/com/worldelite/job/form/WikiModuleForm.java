package com.worldelite.job.form;

import lombok.Data;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class WikiModuleForm {
    private Integer id; //ID
    private Long companyId; //公司ID
    private Byte wikiEnable; //是否启用百科基本信息
    private Byte addressEnable; //是否启用地址模块
    private Byte employeeEnable; //是否启用雇员数量模块
    private Byte productEnable; //是否启用产品模块
    private Byte marketEnable; //是否启用市值模块
    private Byte historyEnable; //是否启用发展路径模块
    private Byte structureEnable; //是否启用组织架构模块
    private Byte salaryEnable; //是否启用薪资待遇
    private Byte recruitEnable; //是否启用招聘进度
    private Byte environmentEnable; //是否启用工作环境
    private Byte departmentEnable; //是否启用部门
    private Byte honorEnable; //是否启用企业荣誉
    private Byte postEnable; //是否启用推荐帖子
    private Byte jobEnable; //是否启用推荐职位
    private Byte countEnable; //是否启用订阅统计
}
