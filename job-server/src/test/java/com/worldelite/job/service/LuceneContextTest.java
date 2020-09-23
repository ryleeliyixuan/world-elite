package com.worldelite.job.service;

import com.worldelite.job.JobApplication;
import com.worldelite.job.context.LuceneContext;
import org.apache.lucene.index.IndexWriter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Async;

/**
 * Lucene上下文环境测试类
 */
@SpringBootTest(classes = JobApplication.class)
public class LuceneContextTest {

    @Autowired
    private LuceneContext luceneContext;

    @Value("${search.index.resumeindex2}")
    private String folder;

    @Test
    public void testGetIndexWriter() throws InterruptedException {

        IndexWriter indexWriter1 = getIndexWriter();
        System.out.println("当前Writer："+indexWriter1);
        IndexWriter indexWriter2 = getIndexWriter();
        luceneContext.closeIndexWriter(folder );
        System.out.println("当前Writer："+indexWriter2);
        IndexWriter indexWriter3 = getIndexWriter();
        System.out.println("当前Writer："+indexWriter3);
        IndexWriter indexWriter4 = getIndexWriter();
        System.out.println("当前Writer："+indexWriter4);

        Thread.currentThread().join();
    }

    @Async
    private IndexWriter getIndexWriter(){
        return luceneContext.getIndexWriter(folder);
    }

}
