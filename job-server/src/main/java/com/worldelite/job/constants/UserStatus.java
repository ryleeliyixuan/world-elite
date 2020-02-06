package com.worldelite.job.constants;

/**
 * @author yeguozhong yedaxia.github.com
 */
public enum UserStatus {

    NOT_ACTIVATE(0),
    NORMAL(1),
    BLACK(100);

    public final byte value;

    UserStatus(int value) {
        this.value = (byte)value;
    }
}
