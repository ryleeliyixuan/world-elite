package com.worldelite.job.constants;

/**
 * 组织类型
 *
 * @author Xiang Chao
 **/
public enum OrganizerType {
    SCHOOL(1), //校园组织
    BUSINESS(2), //社会组织
    PERSONAL(3), //个人
    COMPANY(4); //企业
    public final byte value;

    OrganizerType(int value) {
        this.value = (byte)value;
    }
}
