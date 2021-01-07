package com.worldelite.job.constants;

/**
 * 活动报名审核状态
 */
public enum RegistrationStatus {
    DIRECT(0), //无需审核
    NOT_ACTIVITY(1), //审核中
    PASS(2), //审核通过
    INAPPROPRIATE(3); //不合适

    public final byte value;

    RegistrationStatus(int value) {
        this.value = (byte) value;
    }
}
