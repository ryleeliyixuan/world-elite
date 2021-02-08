package com.worldelite.job.constants;

/**
 * @author yeguozhong yedaxia.github.com
 */
public enum UserType {
    GENERAL(1),
    COMPANY(2),
    ADMIN(100);
    public final byte value;

    UserType(int value) {
        this.value = (byte) value;
    }


    public static UserType valueOf(byte value){
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
