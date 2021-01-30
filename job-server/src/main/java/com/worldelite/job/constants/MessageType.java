package com.worldelite.job.constants;

/**
 * 消息类型
 **/
public enum MessageType {
    ACTIVITY_REGISTRATION_APPROVED(0); //报名通过通知
    public final byte value;

    MessageType(int value) {
        this.value = (byte) value;
    }
}
