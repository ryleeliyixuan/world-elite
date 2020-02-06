package com.worldelite.job.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.worldelite.job.JobApplication;
import com.worldelite.job.entity.School;
import com.worldelite.job.mapper.CountryMapper;
import com.worldelite.job.mapper.SchoolMapper;
import org.apache.tomcat.util.http.fileupload.util.Streams;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author yeguozhong yedaxia.github.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = JobApplication.class)
@WebAppConfiguration
public class CountryServiceTests {

    @Autowired
    private CountryService countryService;

    @Autowired
    private SchoolMapper schoolMapper;

    @Autowired
    private CountryMapper countryMapper;

    @Test
    public void init_school_data() throws IOException {
        String school_json = Streams.asString(new FileInputStream("/Users/yeguozhong/Desktop/crawler-china-mainland-universities/china_mainland_universities.json"));
        JSONObject jsonObject = JSON.parseObject(school_json);
        for(String key: jsonObject.keySet()){
            JSONArray jsonArray = jsonObject.getJSONObject(key).getJSONArray("all");
            for(Object name: jsonArray.toArray()){
                if(StringUtils.isBlank(name.toString()))
                    continue;
                School school = new School();
                school.setCountryId(40);
                school.setName(name.toString());
                schoolMapper.insertSelective(school);
            }
        }
    }
}
