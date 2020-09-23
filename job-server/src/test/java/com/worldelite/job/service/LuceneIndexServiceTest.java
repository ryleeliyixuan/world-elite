package com.worldelite.job.service;

import com.worldelite.job.JobApplication;
import com.worldelite.job.service.search.LuceneIndexService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = JobApplication.class)
public class LuceneIndexServiceTest {

    @Autowired
    private LuceneIndexService luceneIndexService;

    @Test
    public void testSaveResumeItem(){
        luceneIndexService.saveResumeItem(1225070040388198400L);
    }

}
