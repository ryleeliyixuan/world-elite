package com.worldelite.job.service;

import com.alibaba.fastjson.JSON;
import com.worldelite.job.form.EmailForm;
import org.junit.jupiter.api.Test;

/**
 * @author yeguozhong yedaxia.github.com
 */
public class ConfigServiceTests {

    @Test
    public void setEmailTemplates(){
        EmailForm emailForm = new EmailForm();
        emailForm.setSubject("你的简历标识为不合适【WorldElite】");
        emailForm.setEmailBody("投递岗位：${JOB}，请继续加油。");
        System.out.println(JSON.toJSONString(emailForm));
    }
}
