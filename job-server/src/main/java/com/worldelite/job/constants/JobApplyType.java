package com.worldelite.job.constants;

/**
 * 职位申请类型
 */
public enum JobApplyType {

    APPLICANT(1), //用户简历
    REPOSITORY(2); //企业简历库简历

    public final byte value;

    JobApplyType(int value) {
        this.value = (byte) value;
    }
}
