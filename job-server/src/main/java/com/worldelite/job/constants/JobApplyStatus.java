package com.worldelite.job.constants;

/**
 * @author yeguozhong yedaxia.github.com
 */
public enum JobApplyStatus {

    APPLY(1), //投递
    VIEW(2),   //已查看
    CANDIDATE(3), //候选人
    INTERVIEW(4), //面试
    OFFER(5), //入职
    ABANDON(6) // 放弃(不合适或者超时)
    ;

    public final byte value;

    JobApplyStatus(int value) {
        this.value = (byte) value;
    }
}
