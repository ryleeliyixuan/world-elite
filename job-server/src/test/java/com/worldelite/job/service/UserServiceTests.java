package com.worldelite.job.service;

import com.alibaba.fastjson.JSON;
import com.worldelite.job.JobApplication;
import com.worldelite.job.constants.UserStatus;
import com.worldelite.job.entity.Country;
import com.worldelite.job.mapper.CountryMapper;
import com.worldelite.job.vo.JobCategoryVo;
import com.worldelite.job.vo.UserVo;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = JobApplication.class)
@WebAppConfiguration
public class UserServiceTests {

    @Autowired
    private UserService userService;


    @Autowired
    private JobCategoryService jobCategoryService;

    @Test
    public void test_getCategoryTree(){
        List<JobCategoryVo> categoryVoList = jobCategoryService.getCategoryTree();
        Assert.assertTrue(!categoryVoList.isEmpty());
    }
}
