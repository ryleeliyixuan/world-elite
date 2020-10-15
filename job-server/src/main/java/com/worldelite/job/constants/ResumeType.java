package com.worldelite.job.constants;

/**
 * 简历类型枚举
 */
public enum ResumeType {

    GENERAL(1), //用户简历
    COMPANY(2), //企业简历
    ADMIN(100); //管理端简历

    public final byte value;

    ResumeType(int value) {
        this.value = (byte) value;
    }

    public static ResumeType valueOf(byte value){
        if(value == GENERAL.value){
            return GENERAL;
        }else if(value == COMPANY.value){
            return COMPANY;
        }else if(value == ADMIN.value){
            return ADMIN;
        }
        return null;
    }
}
