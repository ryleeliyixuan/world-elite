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
     * 工作名称
     */
    String JOB_NAME = "job_name";

    String JOB_NAME_MARK = "job_name_mark";

    /**
     * 索引ID
     */
    String JOB_ID_INDEX = "job_id_index";

    /**
     * 关键字字段：职位名称 + 职位类别 + 城市 + 公司 + 行业 + 工作类型
     */
    String KEYWORD_INDEX = "keyword";

    /**
     * 城市
     */
    String CITY_INDEX = "city";

    /**
     * 定位
     */
    String COMPANY_DEFINE_INDEX = "company_define";

    /**
     * 语言要求
     */
    String LAN_REQUIRED_INDEX = "lan_required";

    /**
     * 工作经验
     */
    String EXPERIENCE_INDEX = "experience";

    /**
     * 发布时间
     */
    String JOB_PUBLISH_TIME_INDEX = "pub_time";

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
     * 招聘类型
     */
    String RECRUIT_TYPE_INDEX = "recruit_type_index";

    /**
     * 工作类型
     */
    String JOB_TYPE_INDEX = "job_type";

    /**
     * 公司id- 新百科公司下职位搜索时用
     */
    String COMPANY_ID = "company_id";
}
