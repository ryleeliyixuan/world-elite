package com.worldelite.job.service;

import com.alibaba.fastjson.JSON;
import com.worldelite.job.JobApplication;
import com.worldelite.job.form.EmailForm;
import com.worldelite.job.mapper.ConfigMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

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
