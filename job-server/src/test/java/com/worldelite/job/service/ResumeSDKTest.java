package com.worldelite.job.service;

import com.alibaba.fastjson.JSONObject;
import com.worldelite.job.JobApplication;
import com.worldelite.job.entity.AttachmentParser;
import com.worldelite.job.mapper.AttachmentParserMapper;
import com.worldelite.job.service.sdk.ResumeSDK;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * ResumeSDK测试
 */
@SpringBootTest(classes = JobApplication.class)
public class ResumeSDKTest {

    @Autowired
    private ResumeSDK resumeSDK;

    @Autowired
    private AttachmentParserMapper attachmentParserMapper;

    /**
     * 获取教育信息测试
     */
    @Test
    public void testGetResumeEduList(){
        AttachmentParser attachmentParser = attachmentParserMapper.selectByPrimaryKey(20L);
        JSONObject result = JSONObject.parseObject(attachmentParser.getAttachContent());
        resumeSDK.getResumeEduList(result);
    }

}
