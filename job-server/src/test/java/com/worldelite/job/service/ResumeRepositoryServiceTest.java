package com.worldelite.job.service;

import com.alibaba.fastjson.JSONObject;
import com.worldelite.job.JobApplication;
import com.worldelite.job.entity.AttachmentParser;
import com.worldelite.job.mapper.AttachmentParserMapper;
import com.worldelite.job.service.sdk.ResumeSDK;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 *
 **/
@SpringBootTest(classes = JobApplication.class)
public class ResumeRepositoryServiceTest {

    @Autowired
    private ResumeRepositoryService resumeRepositoryService;

    @Autowired
    private ResumeSDK resumeSDK;

    @Autowired
    private AttachmentParserMapper attachmentParserMapper;

    public ResumeRepositoryServiceTest() {
    }

    @Test
    public void testSaveAttachment(){
        resumeRepositoryService.saveAttachment("https://oss.myworldelite.com/attachment/1085befd058d44af9f3a0a2c86e49ce2.pdf");
    }

    @Test
    public void testBuildIndex() {
//        resumeRepositoryService.buildIndex();
//        Thread.currentThread().join();
        AttachmentParser attachmentParser = new AttachmentParser();
        attachmentParser.setEmail("aaaa");
        attachmentParser.setAttachContent("aaa");
        int result = attachmentParserMapper.insertSelective(attachmentParser);
        System.out.println(result);
        System.out.println(attachmentParser.getId());
    }
}
