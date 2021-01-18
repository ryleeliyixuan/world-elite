package com.worldelite.job.constants;

public enum ActivityReportStatus {

    REVIEWING(0), //审核中
    TURN_DOWN(1), //驳回
    OFFLINE(2); //下架

    public final byte value;

    ActivityReportStatus(int value) {
        this.value = (byte)value;
    }
}
