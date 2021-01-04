package com.worldelite.job.constants;

/**
 * @author yeguozhong yedaxia.github.com
 */
public enum ActivityStatus {

    REVIEWING(0), //审核中
    DRAFT(1), //草稿
    OFFLINE(2), //下架
    WILL(3), //即将开始
    SIGN_UP(4), //报名中
    ACTIVE(5), //进行中
    END(6); //已结束
    public final byte value;

    ActivityStatus(int value) {
        this.value = (byte)value;
    }
}
