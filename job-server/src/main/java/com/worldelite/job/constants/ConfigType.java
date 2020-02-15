package com.worldelite.job.constants;

/**
 * @author yeguozhong yedaxia.github.com
 */
public enum ConfigType {

    VALIDATE_EMAIL_BODY(1),
    VERIFICATION_PASS_EMAIL(2),
    VERIFICATION_REJECT_EMAIL(3),
    ;
    public final int value;

    ConfigType(int value) {
        this.value = value;
    }
}
