package com.worldelite.job.service;

import com.worldelite.job.JobApplication;
import com.worldelite.job.entity.ResumeAttach;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = JobApplication.class)
@WebAppConfiguration
public class ResumeAttachServiceTest {

    @Autowired
    private ResumeAttachService resumeAttachService;

    /**
     * 新建/重建索引测试
     */
    @Test
    public void testBuildIndex() throws InterruptedException {
        //构建三个测试用简历文件实体对象
        List<ResumeAttach> resumeAttachList1 = new ArrayList<ResumeAttach>();
        List<ResumeAttach> resumeAttachList2 = new ArrayList<ResumeAttach>();
        ResumeAttach resumeAttach1 = createResumeAttach(0L,"后台工程师");
        ResumeAttach resumeAttach2 = createResumeAttach(1L,"测试文本2");
        ResumeAttach resumeAttach3 = createResumeAttach(2L,"测试文本3");
        resumeAttachList1.add(resumeAttach1);
        resumeAttachList1.add(resumeAttach2);
        resumeAttachList2.add(resumeAttach3);
        //开三个线程，一个线程写入resumeAttachList1，一个写入resumeAttachList2
        //另外一个线程进行查询
        new Thread(){
            @Override
            public void run() {
                resumeAttachService.buildIndex(resumeAttachList1);
            }
        }.start();
        new Thread(){
            @Override
            public void run() {
                resumeAttachService.buildIndex(resumeAttachList2);
            }
        }.start();
        Thread.sleep(600);
        new Thread(){
            @Override
            public void run() {
                Document document = resumeAttachService.getDocumentByResumeId(1L);
                System.out.println("搜索完成");
            }
        }.start();
        //睡眠一段时间，等待三个线程执行完成
        Thread.sleep(3000);
    }

    /**
     * 从附件简历数据库重建所有索引
     */
    @Test
    public void buildAll(){
        resumeAttachService.buildIndex();
    }

    /**
     * 从简历数据库重建所有索引文件
     */
    @Test
    public void buildResumeIndex() throws InterruptedException {
        resumeAttachService.buildResumeIndex();
        Thread.currentThread().join();
    }

    @Test
    public void documentTest(){
        Document document = new Document();
        document.add(new StringField("aaa","bbb", Field.Store.NO));
        System.out.println(document.get("aaa"));
    }

    private ResumeAttach createResumeAttach(Long id,String content){
        ResumeAttach resumeAttach = new ResumeAttach();
        resumeAttach.setResumeId(id);
        resumeAttach.setAttachContent(content);
        return resumeAttach;
    }
}
