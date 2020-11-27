package com.worldelite.job.api;

import com.worldelite.job.JobApplication;
import com.worldelite.job.form.SearchNameForm;
import com.worldelite.job.service.CompanyNameSearchService;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.PageResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @Author twz
 * @Date 2020/11/27 0027
 * @Desc TODO
 */
@SpringBootTest(classes = JobApplication.class)
@WebAppConfiguration
@RunWith(SpringRunner.class)
public class CompanyApiTest {

    @Autowired
    private CompanyApi companyApi;

    @Test
    public void testSearchJobName() {
        companyApi.createOrRefreshJobNameIndex();
        ApiResult<PageResult<String>> result = companyApi.searchJobName(new SearchNameForm("北京", 1, 5));
        System.out.println("result = " + result);
    }

}
