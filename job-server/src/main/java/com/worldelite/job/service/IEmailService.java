package com.worldelite.job.service;

import com.worldelite.job.form.EmailForm;

/**
 * 邮件发送接口
 * @author yeguozhong yedaxia.github.com
 */
public interface IEmailService {
    void sendEmail(EmailForm emailForm);
}
