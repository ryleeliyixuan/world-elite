package com.worldelite.job.service;

import com.worldelite.job.JobApplication;
import com.worldelite.job.entity.ResumeDetail;
import com.worldelite.job.entity.ResumeEdu;
import com.worldelite.job.service.search.IndexService;
import com.worldelite.job.service.search.LuceneIndexService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = JobApplication.class)
public class LuceneIndexServiceTest {

    @Autowired
    private IndexService indexService;

    @Test
    public void testSaveResumeItem() throws InterruptedException {
        //luceneIndexService.saveResumeItem(1225070040388198400L);
        ResumeEdu resumeEdu1 = new ResumeEdu();
        resumeEdu1.setDegreeId(1);
        resumeEdu1.setSchoolId(1);
        ResumeEdu resumeEdu2 = new ResumeEdu();
        resumeEdu2.setDegreeId(2);
        resumeEdu2.setSchoolId(2);
        ResumeEdu resumeEdu3 = new ResumeEdu();
        resumeEdu3.setDegreeId(3);
        resumeEdu3.setSchoolId(3);
        List<ResumeEdu> resumeEduList = new ArrayList<>();
        resumeEduList.add(resumeEdu1);
        resumeEduList.add(resumeEdu2);
        resumeEduList.add(resumeEdu3);
        ResumeDetail resumeDetail = new ResumeDetail();
        resumeDetail.setResumeId(0L);
        resumeDetail.setResumeEduList(resumeEduList);
        indexService.saveResumeItem(resumeDetail,"");
        Thread.currentThread().join();
    }

}
