package com.worldelite.job.read;

import com.worldelite.job.JobApplication;
import com.worldelite.job.service.read.impl.ResumePdfFileRead;
import com.worldelite.job.service.read.impl.ResumeWordFileRead;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

/**
 * 简历文件读取测试
 **/

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JobApplication.class)
public class ResumeFileReadTest {

    @Autowired
    ResumePdfFileRead resumePdfFileRead;

    @Autowired
    ResumeWordFileRead resumeWordFileRead;

    String baseResourcePath;

    @Before
    public void init() {
        baseResourcePath = System.getProperty("user.dir") + "\\build\\resources\\test\\";
    }

    @Test
    public void pdfFileReadTest() throws IOException {
        String txt = resumePdfFileRead.read(baseResourcePath + "李小有.pdf");
        Assert.assertNotNull(txt);
        Assert.assertTrue(txt.startsWith("李小有 简历"));

        txt = resumePdfFileRead.read(new File(baseResourcePath + "李小有.pdf"));
        Assert.assertNotNull(txt);
        Assert.assertTrue(txt.startsWith("李小有 简历"));

        txt = resumePdfFileRead.read(new FileInputStream(new File(baseResourcePath + "李小有.pdf")));
        Assert.assertNotNull(txt);
        Assert.assertTrue(txt.startsWith("李小有 简历"));

        //文件下载为同步执行
        txt = resumePdfFileRead.read(new URL("https://oss.myworldelite.com/attachment/b09e348776de4c09b6fd7977a48ae45c.pdf"));
        Assert.assertNotNull(txt);
        Assert.assertTrue(txt.startsWith("李小有 简历"));

        System.out.println(txt);
    }

    @Test
    public void wordFileReadTest() throws IOException {
        String txt = resumeWordFileRead.read(baseResourcePath + "张测试.docx");
        Assert.assertNotNull(txt);
        Assert.assertTrue(txt.startsWith("个人信息") && txt.contains("张测试"));

        txt = resumeWordFileRead.read(baseResourcePath + "张测试2.doc");
        Assert.assertNotNull(txt);
        Assert.assertTrue(txt.startsWith("个人信息") && txt.contains("张测试"));

        txt = resumeWordFileRead.read(new URL("https://oss.myworldelite.com/attachment/912f7c201dcd44a692c2b3275ed81fab.docx"));
        Assert.assertNotNull(txt);
        Assert.assertTrue(txt.startsWith("个人信息") && txt.contains("张测试"));

        System.out.println(txt);
    }
}


