package com.worldelite.job.constants;

/**
 * @author yeguozhong yedaxia.github.com
 */
public enum DictType {

    DEGREE(1), //学历
    CITY(2),  // 城市
    SKILL(3),// 技能
    COMPANY_STAGE(4), //公司阶段
    COMPANY_SCALE(5), //公司规模
    INDUSTRY(6),//行业
    COMPANY_PROPERTY(7),//企业性质
    JOB_TYPE(8); //职位类型

    public final int value;

    DictType(int value){
        this.value = value;
    }
}
