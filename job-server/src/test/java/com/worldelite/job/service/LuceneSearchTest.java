package com.worldelite.job.service;

import com.worldelite.job.JobApplication;
import com.worldelite.job.context.LuceneContext;
import com.worldelite.job.form.ResumeListForm;
import com.worldelite.job.service.search.SearchService;
import org.apache.commons.math3.optim.univariate.SearchInterval;
import org.apache.lucene.index.IndexWriter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Async;

/**
 * Lucene搜索测试类
 */
@SpringBootTest(classes = JobApplication.class)
public class LuceneSearchTest {

    @Value("${search.index.resumeindex2}")
    private String resumeAttachmentPath2;

    @Autowired
    private SearchService searchService;

    /**
     * 测试搜索简历
     */
    @Test
    public void testSearchResume(){
        ResumeListForm form = new ResumeListForm();
        String[] skills = new String[]{"抗压"};
        //form.setSkills(skills);
        searchService.searchResume(form,resumeAttachmentPath2);
    }

}
