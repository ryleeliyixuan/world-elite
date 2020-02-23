package com.worldelite.job.constants;

/**
 * @author yeguozhong yedaxia.github.com
 */
public enum JobApplyStatus {

    APPLY(1), //投递中
    VIEW(2),   //已查看
    CANDIDATE(3), //初筛
    INTERVIEW(4), //面试
    OFFER(5), //录用
    ABANDON(6), // 不合适
    EXPIRED(7) // 已过期
    ;

    public final byte value;

    JobApplyStatus(int value) {
        this.value = (byte) value;
    }
}
