package com.worldelite.job.constants;

/**
 * 实名认证业务类型
 *
 * @author Xiang Chao
 **/
public enum RealNameAuthBusinessType {
    ACTIVITY_REAL_NAME_AUTH(1); //活动实名认证

    public final byte value;

    RealNameAuthBusinessType(int value) {
        this.value = (byte) value;
    }

    public static RealNameAuthBusinessType valueOf(byte value) {
        if (value == ACTIVITY_REAL_NAME_AUTH.value) {
            return ACTIVITY_REAL_NAME_AUTH;
        }
        return null;
    }
}
