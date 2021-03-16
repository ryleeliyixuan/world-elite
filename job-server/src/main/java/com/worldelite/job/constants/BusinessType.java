package com.worldelite.job.constants;

/**
 * 业务类型
 **/
public enum BusinessType {
    JOB(0, "职位更新/删除"),
    RESUME(1, "简历更新/删除"),
    ATTACH_RESUME(2, "附件简历更新/删除"),
    ACTIVITY(3, "活动更新/删除"),
    COMPANY(4, "公司更新/删除");

    BusinessType(int business, String name) {
    }
}
