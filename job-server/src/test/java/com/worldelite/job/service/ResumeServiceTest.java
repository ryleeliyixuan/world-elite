package com.worldelite.job.service;

import com.worldelite.job.JobApplication;
import com.worldelite.job.constants.BusinessType;
import com.worldelite.job.constants.OperationType;
import com.worldelite.job.dto.LuceneIndexCmdDto;
import com.worldelite.job.mapper.ResumeAttachMapper;
import org.apache.lucene.document.Document;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Lazy;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 *
 **/

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JobApplication.class)
public class ResumeServiceTest {
    @Autowired
    @Lazy
    private ResumeService resumeService;

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Resource(name = "luceneIndexCmdFanoutExchange")
    private FanoutExchange exchange;
    @Autowired
    private ResumeAttachMapper resumeAttachMapper;

    @Test
    public void attachResumeUpdateTest() throws InterruptedException {
        resumeService.addOrUpdateResumeIndex(1254453006126829568L, 1254453003962568704L, "https://oss.myworldelite.com/attachment/17c87b35cd8e4b7a94cf33b53815be03.pdf");
        Thread.currentThread().join();
    }

    @Test
    public void publishMsgTest() throws InterruptedException {
        //ResumeAttach resumeAttach = resumeAttachMapper.selectByResumeId(1254453006126829568L);

        //rabbitTemplate.convertAndSend(exchange.getName(), "", new LuceneUpdateCmdDto(resumeAttach, OperationType.CreateOrUpdate));
        Document document = new Document();
        rabbitTemplate.convertAndSend(exchange.getName(), "", new LuceneIndexCmdDto(document, OperationType.Delete, BusinessType.AttachResume));

        Thread.currentThread().join();
    }
}
