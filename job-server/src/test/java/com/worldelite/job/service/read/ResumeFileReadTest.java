package com.worldelite.job.service.read;

import com.worldelite.job.JobApplication;
import com.worldelite.job.service.read.impl.ResumePdfFileRead;
import com.worldelite.job.service.read.impl.ResumeWordFileRead;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

/**
 * 简历文件读取测试
 **/

@SpringBootTest(classes = JobApplication.class)
@ActiveProfiles("test")
public class ResumeFileReadTest {

    @Autowired
    private ResumePdfFileRead resumePdfFileRead;

    @Autowired
    private ResumeWordFileRead resumeWordFileRead;

    private String baseResourcePath;

    @BeforeEach
    public void init() {
        baseResourcePath = System.getProperty("user.dir") + "\\build\\resources\\test\\";
    }

    @Test
    public void pdfFileReadTest() throws IOException {
        String txt = resumePdfFileRead.read(new FileInputStream(new File(baseResourcePath + "李小有.pdf")));
        Assert.notNull(txt, "读取pdf失败");
        Assert.hasText("李小有 简历", "读取的简历内容不正确");

        //文件下载为同步执行
        txt = resumePdfFileRead.read(new URL("https://oss.myworldelite.com/attachment/b09e348776de4c09b6fd7977a48ae45c.pdf"));
        Assert.notNull(txt, "读取pdf失败");
        Assert.hasText("李小有 简历", "读取的简历内容不正确");

        System.out.println(txt);
    }

    @Test
    public void wordFileReadTest() throws IOException {
        String txt = resumeWordFileRead.read(baseResourcePath + "张测试.docx");
        Assert.notNull(txt, "读取docx失败");
        Assert.isTrue(txt.startsWith("个人信息") && txt.contains("张测试"), "读取的简历内容不正确");

        txt = resumeWordFileRead.read(baseResourcePath + "张测试2.doc");
        Assert.notNull(txt, "读取docx失败");
        Assert.isTrue(txt.startsWith("个人信息") && txt.contains("张测试"), "读取的简历内容不正确");

        txt = resumeWordFileRead.read(new URL("https://oss.myworldelite.com/attachment/912f7c201dcd44a692c2b3275ed81fab.docx"));
        Assert.notNull(txt, "读取docx失败");
        Assert.isTrue(txt.startsWith("个人信息") && txt.contains("张测试"), "读取的简历内容不正确");

        System.out.println(txt);
    }
}


