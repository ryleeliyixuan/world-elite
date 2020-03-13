package com.worldelite.job.context;

import com.google.code.kaptcha.Constants;

/**
 * 会话键
 *
 * @author yeguozhong yedaxia.github.com
 */
public interface SessionKeys {

    /**
     * 图片验证码
     */
    String KAPTCHA_SESSION_KEY = Constants.KAPTCHA_SESSION_KEY;
}
