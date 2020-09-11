package com.worldelite.job.service;

import com.worldelite.job.JobApplication;
import com.worldelite.job.form.SchoolForm;
import com.worldelite.job.mapper.CountryMapper;
import com.worldelite.job.mapper.SchoolMapper;
import com.worldelite.job.vo.CountryVo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.Assert;

import java.io.IOException;

/**
 * @author yeguozhong yedaxia.github.com
 */
@SpringBootTest(classes = JobApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class CountryServiceTests {

    @Autowired
    private CountryService countryService;

    @Test
    public void getCountryByIpTest() {
        final CountryVo countryByIp = countryService.getCountryByIp("8.8.8.8");
        Assert.isTrue(countryByIp.getId() == 206);
        Assert.isTrue(countryByIp.getCountryCode().equals("US"));
    }
}
