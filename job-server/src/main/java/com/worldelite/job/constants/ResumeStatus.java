package com.worldelite.job.constants;

/**
 * 简历状态枚举
 */
public enum ResumeStatus {

    DRAFT(1), //草稿状态
    PUBLISH(2), //正式状态
    OFFLINE(3); //废弃状态

    public final byte value;

    ResumeStatus(int value) {
        this.value = (byte)value;
    }

    public static ResumeStatus valueOf(byte value){
        if(value == DRAFT.value){
            return DRAFT;
        }else if(value == PUBLISH.value){
            return PUBLISH;
        }else if(value == OFFLINE.value){
            return OFFLINE;
        }
        return null;
    }
}
