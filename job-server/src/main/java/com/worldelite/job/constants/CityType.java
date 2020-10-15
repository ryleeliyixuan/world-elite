package com.worldelite.job.constants;

/**
 * 城市类型
 * @author 邓集阶
 */
public enum CityType {

    DOMESTIC(1), //国内
    ABROAD(2); //国外
    public final byte value;

    CityType(int value) {
        this.value = (byte)value;
    }
}
