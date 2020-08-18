package com.worldelite.job.constants;

/**
 * 业务类型
 **/
public enum BusinessType {
    Job(0, "职位更新/删除"),
    AttachResume(1, "附件简历更新/删除");

    private int business;
    private String name;

    BusinessType(int business, String name) {
        this.business = business;
        this.name = name;
    }
}
