package com.worldelite.job.constants;

/**
 * @author yeguozhong yedaxia.github.com
 */
public enum ObjectType {

    JOB(1),COMPANY(2);
    public final byte value;

    ObjectType(int value) {
        this.value = (byte)value;
    }
}
