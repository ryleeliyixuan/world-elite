package com.worldelite.job.constants;

/**
 * @author yeguozhong yedaxia.github.com
 */
public enum AuthType {

    GOOGLE(1),
    WECHAT_OPEN(2),
    LINKEDIN(3);
    public final byte value;

    AuthType(int value) {
        this.value = (byte)value;
    }
}
