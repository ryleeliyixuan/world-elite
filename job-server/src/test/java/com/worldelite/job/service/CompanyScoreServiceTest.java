package com.worldelite.job.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.context.TestExecutionListeners;

@Service
public class CompanyScoreServiceTest {

    @Autowired
    private CompanyScoreService companyScoreService;

    /**
     * 禁言测试
     */
    @Test
    public void testForbidden(){

    }

    /**
     * 取消禁言测试
     */
    @Test
    public void testUnForbidden(){

    }

    /**
     * 批量禁言测试
     */
    @Test
    public void testForbiddenAll(){

    }

    /**
     * 禁言情况下添加帖子测试
     */
    @Test
    public void testAdd(){

    }

}
