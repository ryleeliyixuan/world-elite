package com.worldelite.job.constants;

/**
 * @author Xiang Chao
 **/
public enum  OrganizerCreditGrade {
    LEVEL1(1),
    LEVEL2(2),
    LEVEL3(3);
    public final byte value;

    OrganizerCreditGrade(int value) {
        this.value = (byte)value;
    }

    public static OrganizerCreditGrade valueOf(byte value){
        if(value == LEVEL1.value){
            return LEVEL1;
        }else if(value == LEVEL2.value){
            return LEVEL2;
        }else if(value == LEVEL3.value){
            return LEVEL3;
        }
        return null;
    }
}
