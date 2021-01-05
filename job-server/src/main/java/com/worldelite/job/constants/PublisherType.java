package com.worldelite.job.constants;

/**
 * 活动发布类型
 *
 * @author Xiang Chao
 **/
public enum PublisherType {
    GENERAL(1), COMPANY(2), OVERSEAS(3), ADMIN(100);
    public final byte value;

    PublisherType(int value) {
        this.value = (byte) value;
    }
}
