package com.worldelite.job.constants;

/**
 * @author yeguozhong yedaxia.github.com
 */
public enum VerificationStatus {
    REVIEWING(1), PASS(2), REJECT(3);

    public final byte value;

    VerificationStatus(int value) {
        this.value = (byte)value;
    }
}