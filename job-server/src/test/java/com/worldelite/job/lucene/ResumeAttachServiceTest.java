package com.worldelite.job.lucene;

import com.worldelite.job.JobApplication;
import com.worldelite.job.constants.ResumeAttachmentIndexFields;
import com.worldelite.job.entity.ResumeFile;
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
        ResumeFile resumeFile1 = createResumeFile(0L,"/resumeFile1","后台工程师");
        ResumeFile resumeFile2 = createResumeFile(1L,"/resumeFile2","测试文本2");
        ResumeFile resumeFile3 = createResumeFile(2L,"/resumeFile3","测试文本3");

        try {
            resumeAttachService.buildIndex(resumeFile1);
            resumeAttachService.buildIndex(resumeFile2);
            resumeAttachService.buildIndex(resumeFile3);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加索引测试
     */
    @Test
    public void testAppendIndex(){
        Document document = new Document();
        ResumeFile resumeFile = createResumeFile(1225070040388198400L,"/resumeFile","后台开发");
        document.add(new LongPoint(ResumeAttachmentIndexFields.RESUME_ID,resumeFile.getResumeId(),resumeFile.getResumeId()));
        document.add(new StringField(ResumeAttachmentIndexFields.RESUME_ID,Long.toString(resumeFile.getResumeId()), Field.Store.YES));
        document.add(new StringField(ResumeAttachmentIndexFields.FILE_NAME, resumeFile.getFileName(), Field.Store.YES));
        document.add(new TextField(ResumeAttachmentIndexFields.CONTENT,resumeFile.getContent(), Field.Store.NO));
        try {
            resumeAttachService.appendIndex(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除索引测试
     */
    @Test
    public void testDeleteIndex(){
        //加入id为1111的附件简历索引
        ResumeFile resumeFile = createResumeFile(1111L,"/resumeFile3","随机串索引测试");
        Document document = new Document();
        document.add(new LongPoint(ResumeAttachmentIndexFields.RESUME_ID,resumeFile.getResumeId(),resumeFile.getResumeId()));
        document.add(new StringField(ResumeAttachmentIndexFields.RESUME_ID,Long.toString(resumeFile.getResumeId()), Field.Store.YES));
        document.add(new StringField(ResumeAttachmentIndexFields.FILE_NAME, resumeFile.getFileName(), Field.Store.YES));
        document.add(new TextField(ResumeAttachmentIndexFields.CONTENT,resumeFile.getContent(), Field.Store.NO));
        try {
            //添加索引
            resumeAttachService.appendIndex(document);
            //检验是否成功添加
            List<ResumeFile> resumeFileList = resumeAttachService.searchByKeyWords(new String[]{"随机"});
            Assert.assertEquals(1,resumeFileList.size());
            //删除索引
            resumeAttachService.deleteIndex(resumeFile.getResumeId());
            //检验是否删除成功
            resumeFileList = resumeAttachService.searchByKeyWords(new String[]{"随机"});
            Assert.assertEquals(0,resumeFileList.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过关键字检索测试
     */
    @Test
    public void testSearchByKeyWords(){
        String[] keywords = new String[]{"后台开发"};
        List<ResumeFile> resumeFiles = null;
        try {
            resumeFiles = resumeAttachService.searchByKeyWords(keywords);
            System.out.println(resumeFiles.size());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 同义词效果测试
     */
    @Test
    public void testSynonyms(){
        //
    }

    /**
     * 对分词效果进行测试
     * @throws IOException
     */
    @Test
    public void testAnalysis() throws IOException {
        String[] words = resumeAttachService.analysis("后台开发");
        for(String word:words){
            System.out.println(word);
        }
    }

    /**
     * 关键词搜索测试
     * @throws IOException
     */
    //@Test
    public void searchTest() {
        //测试关键词1
        String[] keywords = new String[]{"测试","文本"};
        List<ResumeFile> resumeFiles = null;
        try {
            resumeFiles = resumeAttachService.searchByKeyWords(keywords);

        //断言会搜索出三个简历文件，且简历ID为0,1,2
        Assert.assertEquals("关键词1搜索出的简历文件数量不正确",3,resumeFiles.size());
        Assert.assertEquals("关键词1简历ID错误", 0L,resumeFiles.get(0).getResumeId().longValue());
        Assert.assertEquals("关键词1简历ID错误", 1L,resumeFiles.get(1).getResumeId().longValue());
        Assert.assertEquals("关键词1简历ID错误", 2L,resumeFiles.get(2).getResumeId().longValue());

        //测试关键词2
        keywords = new String[]{"测试","3"};
        resumeFiles = resumeAttachService.searchByKeyWords(keywords);
        //断言
        Assert.assertEquals("关键词2搜索出的简历文件数量不正确",1,resumeFiles.size());
        Assert.assertEquals("关键词2简历ID错误", 2L,resumeFiles.get(0).getResumeId().longValue());

        //测试关键词3
        keywords = new String[]{"3","文本"};
        resumeFiles = resumeAttachService.searchByKeyWords(keywords);
        //断言
        Assert.assertEquals("关键词3搜索出的简历文件数量不正确",1,resumeFiles.size());
        Assert.assertEquals("关键词3简历ID错误", 2L,resumeFiles.get(0).getResumeId().longValue());

        //测试关键词4
        keywords = new String[]{"测试文本"};
        resumeFiles = resumeAttachService.searchByKeyWords(keywords);
        Assert.assertEquals("关键词4搜索出的简历文件数量不正确",0,resumeFiles.size());} catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ResumeFile createResumeFile(Long id,String fileName,String content){
        ResumeFile resumeFile = new ResumeFile();
        resumeFile.setResumeId(id);
        resumeFile.setFileName(fileName);
        resumeFile.setContent(content);
        return resumeFile;
    }
}
