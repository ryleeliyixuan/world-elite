package com.worldelite.job.lucene;

import com.worldelite.job.JobApplication;
import com.worldelite.job.entity.ResumeFile;
import com.worldelite.job.service.ResumeFileService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = JobApplication.class)
public class ResumeFileServiceTest {

    @Autowired
    private ResumeFileService resumeFileService;

    /**
     * 创建索引测试
     * @throws IOException
     */
    @Test
    public void refreshIndexTest() throws IOException {
        //构建三个测试用简历文件实体对象
        ResumeFile resumeFile1 = createResumeFile(0L,"/resumeFile1","测试文本1");
        ResumeFile resumeFile2 = createResumeFile(1L,"/resumeFile2","测试文本2");
        ResumeFile resumeFile3 = createResumeFile(2L,"/resumeFile3","测试文本3");

        resumeFileService.refreshIndex(resumeFile1);
        resumeFileService.refreshIndex(resumeFile2);
        resumeFileService.refreshIndex(resumeFile3);
    }

    /**
     * 关键词搜索测试
     * @throws IOException
     */
    @Test
    public void searchTest() throws IOException {
        //测试关键词1
        String[] keywords = new String[]{"测试","文本"};
        List<ResumeFile> resumeFiles = resumeFileService.searchByKeyWords(keywords);
        //断言会搜索出三个简历文件，且简历ID为0,1,2
        Assert.assertEquals("关键词1搜索出的简历文件数量不正确",3,resumeFiles.size());
        Assert.assertEquals("关键词1简历ID错误", 0L,resumeFiles.get(0).getResumeId().longValue());
        Assert.assertEquals("关键词1简历ID错误", 1L,resumeFiles.get(1).getResumeId().longValue());
        Assert.assertEquals("关键词1简历ID错误", 2L,resumeFiles.get(2).getResumeId().longValue());

        //测试关键词2
        keywords = new String[]{"测试","3"};
        resumeFiles = resumeFileService.searchByKeyWords(keywords);
        //断言
        Assert.assertEquals("关键词2搜索出的简历文件数量不正确",1,resumeFiles.size());
        Assert.assertEquals("关键词2简历ID错误", 2L,resumeFiles.get(0).getResumeId().longValue());

//        //测试关键词3
        keywords = new String[]{"3","文本"};
        resumeFiles = resumeFileService.searchByKeyWords(keywords);
        //断言
        Assert.assertEquals("关键词3搜索出的简历文件数量不正确",1,resumeFiles.size());
        Assert.assertEquals("关键词3简历ID错误", 2L,resumeFiles.get(0).getResumeId().longValue());

//        //测试关键词4
        keywords = new String[]{"测试文本"};
        resumeFiles = resumeFileService.searchByKeyWords(keywords);
        Assert.assertEquals("关键词4搜索出的简历文件数量不正确",0,resumeFiles.size());
    }

    private ResumeFile createResumeFile(Long id,String fileName,String content){
        ResumeFile resumeFile = new ResumeFile();
        resumeFile.setResumeId(id);
        resumeFile.setFileName(fileName);
        resumeFile.setContent(content);
        return resumeFile;
    }
}
