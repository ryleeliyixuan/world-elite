package com.worldelite.job.constants;

/**
 * @Author twz
 * @Date 2020/12/1 0001
 * @Desc TODO
 */
public enum StatInfoType {

    /**
     * 近七天
     */
    WEEK_DAYS(7),

    /**
     * 近三十天
     */
    MONTH_DAYS(30);


    public final int value;
    StatInfoType(int value) {
        this.value = value;
    }
}
