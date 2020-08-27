package com.worldelite.job.constants;

/**
 * @author yeguozhong yedaxia.github.com
 */
public enum ConfigType {

    EMAIL_ACCOUNT_VALIDATE(1), // 验证码邮件
    EMAIL_VERIFICATION_PASS(2), //企业用户审核通过邮件
    EMAIL_VERIFICATION_REJECT(3), //企业用户审核失败邮件
    EMAIL_JOB_APPLY_CANDIDATE(4), //简历通过初筛邮件通知
    EMAIL_JOB_APPLY_INTERVIEW(5), //简历面试邮件通知
    EMAIL_JOB_APPLY_OFFER(6), //简历录用邮件通知
    EMAIL_JOB_APPLY_ABANDON(7), //简历不合适邮件通知
    USER_HOME_CONFIG(8), //用户首页配置
    COMPANY_HOME_CONFIG(9), //企业首页配置
    USER_PROTOCOL(10), // 用户协议
    EMAIL_INVITE_LINK(11) //邮件邀请用户注册
    ;

    public final int value;

    ConfigType(int value) {
        this.value = value;
    }
}
