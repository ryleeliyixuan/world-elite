package com.worldelite.job.constants;

/**
 * @author yeguozhong yedaxia.github.com
 */
public enum ActivityStatus {

    DRAFT(1),PUBLISH(2),OFFLINE(3);
    public final byte value;

    ActivityStatus(int value) {
        this.value = (byte)value;
    }
}
