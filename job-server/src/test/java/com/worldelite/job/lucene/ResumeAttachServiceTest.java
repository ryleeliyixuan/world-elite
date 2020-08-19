package com.worldelite.job.lucene;

import com.worldelite.job.JobApplication;
import com.worldelite.job.constants.ResumeAttachmentIndexFields;
import com.worldelite.job.entity.ResumeAttach;
import com.worldelite.job.service.ResumeAttachService;
import org.apache.lucene.document.*;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = JobApplication.class)
@WebAppConfiguration
public class ResumeAttachServiceTest {

    @Autowired
    private ResumeAttachService resumeAttachService;

    /**
     * 新建/重建索引测试
     */
    @Test
    public void testBuildIndex(){
        //构建三个测试用简历文件实体对象
        List<ResumeAttach> resumeAttachList = new ArrayList<ResumeAttach>();
        ResumeAttach resumeAttach1 = createResumeAttach(0L,"后台工程师");
        ResumeAttach resumeAttach2 = createResumeAttach(1L,"测试文本2");
        ResumeAttach resumeAttach3 = createResumeAttach(2L,"测试文本3");
        resumeAttachList.add(resumeAttach1);
        resumeAttachList.add(resumeAttach2);
        resumeAttachList.add(resumeAttach3);
        resumeAttachService.buildIndex(resumeAttachList);
    }

    private ResumeAttach createResumeAttach(Long id,String content){
        ResumeAttach resumeAttach = new ResumeAttach();
        resumeAttach.setResumeId(id);
        resumeAttach.setAttachContent(content);
        return resumeAttach;
    }
}
