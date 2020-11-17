package com.worldelite.job.constants;

/**
 * 推荐类型
 */
public enum CompanyRecommendType {

    POST(1), //推荐帖子
    JOB(2); //推荐职位

    public final byte value;

    CompanyRecommendType(int value) {
        this.value = (byte) value;
    }
}
