package com.worldelite.job.constants;

/**
 * @author yeguozhong yedaxia.github.com
 */
public enum Gender {

    MALE(1), FEMALE(2);
    public final byte value;

    Gender(int value) {
        this.value = (byte)value;
    }
}
