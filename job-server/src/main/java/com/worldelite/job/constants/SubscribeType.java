package com.worldelite.job.constants;

/**
 * 用户订阅类型枚举
 */
public enum SubscribeType {
    JOB(1); //职位
    public final byte value;

    SubscribeType(int value) {
        this.value = (byte) value;
    }


    public static SubscribeType valueOf(byte value){
        if(value == JOB.value){
            return JOB;
        }
        return null;
    }
}
