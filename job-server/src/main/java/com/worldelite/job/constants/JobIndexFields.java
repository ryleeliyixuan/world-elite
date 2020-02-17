package com.worldelite.job.constants;

/**
 * 职位索引字段，index为检索字段，其他为存储字段
 *
 * @author yeguozhong yedaxia.github.com
 */
public interface JobIndexFields {

    /**
     * 存储字段
     */
    String JOB_ID = "job_id";

    /**
     * 关键字字段：职位名称 + 职位类别 + 城市 + 公司 + 行业 + 工作类型
     */
    String KEYWORD_INDEX = "keyword";

    /**
     * 城市
     */
    String CITY_INDEX = "city";

    /**
     * 行业
     */
    String COMPANY_INDUSTRY_INDEX = "industry";

    /**
     * 一级类别
     */
    String CATEGORY_FIRST_INDEX = "first_category";

    /**
     * 二级类别
     */
    String CATEGORY_SECOND_INDEX = "second_category";

    /**
     * 三级类别
     */
    String CATEGORY_THIRD_INDEX = "third_category";

    /**
     * 最低+最高薪资 / 2
     */
    String AVER_SALARY_INDEX = "aver_salary";


    /**
     * 公司规模
     */
    String COMPANY_SCALE_INDEX = "company_scale";

    /**
     * 公司阶段
     */
    String COMPANY_STAGE_INDEX = "company_stage";

    /**
     * 最低学历
     */
    String MIN_DEGREE_INDEX = "min_degree";

    /**
     * 工作类型
     */
    String JOB_TYPE_INDEX = "job_type";
}
