package com.worldelite.job.form;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yeguozhong yedaxia.github.com
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class JobSearchForm extends PageForm{
    private String keyword; //关键词
    @Deprecated
    private Integer salaryRangeId; //薪资范围
    private Integer minSalary; //最低薪资
    private Integer maxSalary; //最高信息
    @Deprecated
    private Integer jobType; //职位类型
    private Integer[] salaryRangeIds; //薪资范围
    private Integer[] jobTypes; //职位类型
    private Integer[] cityIds; //城市ID
    private Integer[] companyIndustryIds; //公司行业
    private Integer[] companyScaleIds; //公司规模
    private Integer[] companyStageIds; //公司阶段
    private Integer[] companyDefineIds; // 企业性质
    private Integer[] experienceIds; // 工作经验
    private Integer[] lanRequiredIds; // 语言要求
    private Integer[] degreeIds; //学历要求
    private Integer[] categoryIds; //职位类型
    private Long companyId; //公司id -新百科搜索公司下职位时使用
    private Integer recruitId; //招聘类型
    private Byte salaryAsc; // 薪资降序，1升序，0降序，薪资是指：月薪*月份+年终奖
    private Integer[] specialIds; //特殊筛选
}
