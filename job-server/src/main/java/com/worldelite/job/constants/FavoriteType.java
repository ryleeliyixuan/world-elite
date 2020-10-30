package com.worldelite.job.constants;

/**
 * @author yeguozhong yedaxia.github.com
 */
public enum FavoriteType {

    JOB(1),COMPANY(2),ACTIVITY(3),RESUME(4);

    public final byte value;

    FavoriteType(int value) {
        this.value = (byte)value;
    }
}
