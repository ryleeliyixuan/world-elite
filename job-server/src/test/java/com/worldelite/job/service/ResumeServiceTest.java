package com.worldelite.job.service;

import cn.hutool.core.lang.Assert;
import com.worldelite.job.JobApplication;
import com.worldelite.job.entity.ResumeAttach;
import com.worldelite.job.mapper.ResumeAttachMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Lazy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.AssertionErrors;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;



@SpringBootTest(classes = JobApplication.class)
@ActiveProfiles("test")
public class ResumeServiceTest {
    @InjectMocks
    @Autowired
    @Lazy
    private ResumeService resumeService;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ResumeAttachMapper resumeAttachMapper;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        //把发送mq消息的部分mock掉
        //doNothing().when(rabbitTemplate).convertAndSend(anyString(), anyString(), new Object());
    }

    @Test
    public void attachResumeUpdateTest() throws ExecutionException, InterruptedException {
        ResumeAttach baseResumeAttach = new ResumeAttach();
        baseResumeAttach.setId(1);
        baseResumeAttach.setResumeId(10001L);
        baseResumeAttach.setUserId(10001L);
        baseResumeAttach.setAttachContent("this is a resume attach content");
        baseResumeAttach.setCreateTime(new Date());
        baseResumeAttach.setUpdateTime(new Date());
        resumeAttachMapper.insert(baseResumeAttach);

        //更新ResumeAttach表数据
        final Future<Boolean> booleanFuture = resumeService.addOrUpdateResumeIndex(baseResumeAttach.getResumeId(), baseResumeAttach.getUserId(), "https://ocs.fas.harvard.edu/files/ocs/files/undergrad_resumes_and_cover_letters.pdf");
        Assert.isTrue(booleanFuture.get());

        final ResumeAttach resumeAttach = resumeAttachMapper.selectByResumeIdWithBLOBs(baseResumeAttach.getResumeId());
        AssertionErrors.assertNotEquals("简历更新失败," + resumeAttach.getAttachContent(), baseResumeAttach.getAttachContent(), resumeAttach.getAttachContent());
    }
}
