package com.worldelite.job.constants;

/**
 * 活动形式
 *
 * @author Xiang Chao
 **/
public enum ActivityForm {
    ONLINE(0), //线上
    OFFLINE(1); //线下

    public final byte value;

    ActivityForm(int value) {
        this.value = (byte)value;
    }
}
