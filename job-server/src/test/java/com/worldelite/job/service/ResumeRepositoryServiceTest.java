package com.worldelite.job.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.worldelite.job.JobApplication;
import com.worldelite.job.constants.BusinessType;
import com.worldelite.job.constants.OperationType;
import com.worldelite.job.dto.LuceneIndexCmdDto;
import com.worldelite.job.entity.AttachmentParser;
import com.worldelite.job.mapper.AttachmentParserMapper;
import com.worldelite.job.mapper.ResumeAttachMapper;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriterConfig;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Lazy;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;

/**
 *
 **/

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JobApplication.class)
public class ResumeRepositoryServiceTest {

    @Autowired
    private ResumeRepositoryService resumeRepositoryService;

    @Autowired
    private AttachmentParserMapper attachmentParserMapper;

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
