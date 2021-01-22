package com.worldelite.job.constants;

/**
 * 头像类型
 **/
public enum AvatarType {
    USER_PROFILE(0); //用户个人资料
    public final byte value;

    AvatarType(int value) {
        this.value = (byte) value;
    }
}
