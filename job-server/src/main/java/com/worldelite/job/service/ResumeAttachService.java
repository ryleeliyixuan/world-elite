package com.worldelite.job.service;

import com.worldelite.job.constants.ResumeAttachmentIndexFields;
import com.worldelite.job.constants.ResumeIndexFields;
import com.worldelite.job.entity.Resume;
import com.worldelite.job.entity.ResumeAttach;
import com.worldelite.job.entity.ResumeOptions;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.mapper.ResumeAttachMapper;
import com.worldelite.job.mapper.ResumeMapper;
import com.worldelite.job.vo.ApiCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.lionsoul.jcseg.ISegment;
import org.lionsoul.jcseg.IWord;
import org.lionsoul.jcseg.analyzer.JcsegAnalyzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ResumeAttachService extends BaseService{

    @Value("${search.index.resumeindex1}")
    private String indexFolder1;

    @Value("${search.index.resumeindex2}")
    private String indexFolder2;

    @Value("${domain.oss}")
    private String ossDomain;

    @Autowired
    private Analyzer analyzer;

    @Autowired
    private ResumeMapper resumeMapper;

    @Autowired
    private ResumeAttachMapper resumeAttacheMapper;

    @Autowired
    private ResumeService resumeService;

    private boolean switchFolder = false;

    /**
     * 对字符串进行分词，返回分词结果
     * @param keyword
     * @return
     * @throws IOException
     */
    public String[] analysis(String keyword) throws IOException {

        ISegment seg = ISegment.COMPLEX.factory.create(((JcsegAnalyzer)analyzer).getConfig(), ((JcsegAnalyzer)analyzer).getDict());

        seg.reset(new StringReader(keyword));

        IWord word = null;
        List<String> wordList = new ArrayList<String>();
        while ( (word = seg.next()) != null ) {
            wordList.add(word.getValue());
        }
        return wordList.toArray(new String[wordList.size()]);
    }

    /**
     * 获取当前索引文件重建目录
     * @return
     */
    private String getBuildFolder(){
        return switchFolder?indexFolder1:indexFolder2;
    }

    /**
     * 获取当前索引文件搜索目录
     * @return
     */
    private String getSearchFolder(){
        return switchFolder?indexFolder2:indexFolder1;
    }

    /**
     * 新建或者重新生成所有的附件简历索引文件
     * 注意：此方法会覆盖旧的索引文件
     * @param resumeAttacheList
     * @throws IOException
     */
    public void buildIndex(List<ResumeAttach> resumeAttacheList) {
        IndexWriter writer = null;
        try {
            writer = getIndexWriter(getSearchFolder(),IndexWriterConfig.OpenMode.CREATE);
            int i = 1;
            for(ResumeAttach resumeAttach:resumeAttacheList) {
                Document document = new Document();
                document.add(new LongPoint(ResumeAttachmentIndexFields.RESUME_ID, resumeAttach.getResumeId()));
                document.add(new StringField(ResumeAttachmentIndexFields.RESUME_ID_STR, Long.toString(resumeAttach.getResumeId()), Field.Store.YES));
                document.add(new TextField(ResumeAttachmentIndexFields.CONTENT, resumeAttach.getAttachContent(), Field.Store.NO));
                System.out.println((i++)+":"+resumeAttach.getAttachContent());
                writer.addDocument(document);
            }
            writer.commit();
        } catch (IOException | InterruptedException e) {
            log.error("create index error", e);
        } finally {
            IOUtils.closeQuietly(writer);
        }
    }

    /**
     * 直接读取数据库重建索引
     */
    public void buildIndex() {
        List<ResumeAttach> resumeAttacheList = resumeAttacheMapper.listAll();
        buildIndex(resumeAttacheList);
    }

    /**
     * 添加索引文件
     * @param document
     * @return
     * @throws IOException
     */
    public Document appendIndex(Document document) {
        IndexWriter writer = null;
        try {
            writer = getIndexWriter(getSearchFolder(),IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            writer.addDocument(document);
            writer.commit();
        } catch (IOException | InterruptedException e) {
            log.error("append index error", e);
        } finally {
            IOUtils.closeQuietly(writer);
        }
        return document;
    }

    public Document appendIndex(ResumeAttach resumeAttach) {
        Document document = new Document();
        document.add(new LongPoint(ResumeAttachmentIndexFields.RESUME_ID,resumeAttach.getResumeId()));
        document.add(new StringField(ResumeAttachmentIndexFields.RESUME_ID_STR,Long.toString(resumeAttach.getResumeId()), Field.Store.YES));
        document.add(new TextField(ResumeAttachmentIndexFields.CONTENT,resumeAttach.getAttachContent(), Field.Store.NO));
        return appendIndex(document);
    }

    /**
     * 更新索引
     * 先删除再添加
     * @param resumeAttach
     * @return
     */
    public Document updateIndex(ResumeAttach resumeAttach){
        deleteIndex(resumeAttach.getResumeId());
        return appendIndex(resumeAttach);
    }

    public Document updateIndex(Document document){
        deleteIndex(document);
        return appendIndex(document);
    }

    /**
     * 删除指定附件简历ID对应的索引文件
     * @param resumeId
     * @throws IOException
     */
    public Document deleteIndex(Long resumeId) {
        IndexWriter writer = null;
        Document document = new Document();
        try {
            writer = getIndexWriter(getSearchFolder(),IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            writer.deleteDocuments(LongPoint.newExactQuery(ResumeAttachmentIndexFields.RESUME_ID,resumeId));
            writer.commit();
        } catch (IOException | InterruptedException e) {
            log.error("delete index error", e);
        } finally {
            IOUtils.closeQuietly(writer);
        }
        return document;
    }

    public Document deleteIndex(Document document) {
        Long resumeId = NumberUtils.toLong(document.get(ResumeAttachmentIndexFields.RESUME_ID_STR));
        deleteIndex(resumeId);
        return document;
    }

    /**
     * 通过附件简历ID获取Document对象
     * @param resumeId
     * @return
     * @throws IOException
     */
    public Document getDocumentByResumeId(Long resumeId) {
        Document document = null;
        IndexSearcher searcher = null;
        try {
            //创建一个Directory对象，指定索引库存放的路径
            Directory directory = FSDirectory.open(new File(getSearchFolder()).toPath());
            //创建IndexReader对象，需要指定Directory对象
            IndexReader indexReader = DirectoryReader.open(directory);
            //根据索引位置建立IndexSearch
            searcher = new IndexSearcher(indexReader);

            TopDocs hits = searcher.search(LongPoint.newExactQuery(ResumeAttachmentIndexFields.RESUME_ID, resumeId), 10);

            //搜索有结果，理论上只会有一条记录，为了兼容意外情况，如果有多条记录，则取第一条
            if (hits.totalHits > 0) {
                document = searcher.doc(hits.scoreDocs[0].doc);
            }
        }catch (IOException e) {
            log.error("search index error", e);
        }
        return document;
    }

    /**
     * 阻塞式获取IndexWriter实例
     * 当目标索引文件夹被锁住时
     * 该方法会不断尝试直到锁被释放
     *
     * TODO 是否需要加入超时时间，超时则放弃本次写入？
     *
     * @param path
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    private IndexWriter getIndexWriter(String path, IndexWriterConfig.OpenMode mode) throws IOException, InterruptedException {
        IndexWriter writer = null;
        IndexWriterConfig config = null;
        Directory directory = FSDirectory.open(new File(getSearchFolder()).toPath());
        //阻塞线程，直到锁被释放，获得IndexWriter对象
        Long timestramp = System.currentTimeMillis();
        while(true){
            try {
                System.out.println("获取IndexWriter"+timestramp);
                config = new IndexWriterConfig(analyzer);
                config.setOpenMode(mode);
                writer = new IndexWriter(directory, config);
                System.out.println("IndexWriter获取成功"+timestramp);
                break;
            }catch (IOException e){
                System.out.println("IndexWriter获取失败"+timestramp);
                Thread.sleep(500);
                continue;
            }
        }
        return writer;
    }

    /**
     * 从简历表构建搜索
     */
    public void buildResumeIndex() {
        //获取所有的简历数据
        List<Resume> resumeList = resumeMapper.selectAndList(new ResumeOptions());
        for(Resume resume:resumeList){
            String attachResume = resume.getAttachResume();
            //判断简历文件是否存在
            if(StringUtils.isNotEmpty(attachResume) && attachResume.startsWith("attachment")){
                //解析简历文件，解析结果插入附件简历表
                resumeService.addOrUpdateResumeIndex(resume.getId(),resume.getUserId(),ossDomain+resume.getAttachResume());
            }
        }
        //解析方法为异步方法，需要解析完成才能继续建索引
        try {
            Thread.currentThread().join();
            //从附件简历表重建索引文件
            buildIndex();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new ServiceException("索引解析出错", ApiCode.FAIL);
        }
    }
}
