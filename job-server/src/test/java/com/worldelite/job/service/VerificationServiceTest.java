package com.worldelite.job.service;

import com.worldelite.job.JobApplication;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @author yeguozhong yedaxia.github.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = JobApplication.class)
@WebAppConfiguration
public class VerificationServiceTest {

    @Autowired
    private CompanyVerificationService verificationService;

    @Test
    public void test_reject(){
        verificationService.rejectVerification(1227385762795638784L, "你注册的邮箱不是公司邮箱，请重新使用公司邮箱申请");
    }

    @Test
    public void test_pass(){
        verificationService.passVerification(1227385762795638784L);
    }
}
