package com.worldelite.job.service;

import com.worldelite.job.constants.ResumeAttachmentIndexFields;
import com.worldelite.job.entity.ResumeAttach;
import com.worldelite.job.entity.ResumeFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.lionsoul.jcseg.ISegment;
import org.lionsoul.jcseg.IWord;
import org.lionsoul.jcseg.dic.ADictionary;
import org.lionsoul.jcseg.dic.DictionaryFactory;
import org.lionsoul.jcseg.segmenter.SegmenterConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ResumeAttachService extends BaseService{

    @Value("${search.index.resumeindex1}")
    private String indexFolder1;

    @Value("${search.index.resumeindex2}")
    private String indexFolder2;

    @Value("${search.index.resumepath}")
    private String resumeIndexFolder;

    @Autowired
    private Analyzer analyzer;

    private boolean switchFolder = false;

    /**
     * 对字符串进行分词，返回分词结果
     * @param keyword
     * @return
     * @throws IOException
     */
    public String[] analysis(String keyword) throws IOException {
        SegmenterConfig config = new SegmenterConfig();
        config.setAppendCJKSyn(true);
        ADictionary dictionary = DictionaryFactory.createDefaultDictionary(config);
        dictionary.loadDirectory(resumeIndexFolder);
        dictionary.resetSynonymsNet();

        ISegment seg = ISegment.COMPLEX.factory.create(config, dictionary);

        seg.reset(new StringReader(keyword));

        IWord word = null;
        List<String> wordList = new ArrayList<String>();
        while ( (word = seg.next()) != null ) {
            wordList.add(word.getValue());
        }
        return wordList.toArray(new String[wordList.size()]);
    }

    /**
     * 通过关键字搜索简历文件
     * @param keywords
     * @return
     * @throws IOException
     */
    public List<ResumeFile> searchByKeyWords(String[] keywords) throws IOException {
        //创建一个Directory对象，指定索引库存放的路径
        Directory directory = FSDirectory.open(new File(getSearchFolder()).toPath());
        //创建IndexReader对象，需要指定Directory对象
        IndexReader indexReader = DirectoryReader.open(directory);
        //根据索引位置建立IndexSearch
        IndexSearcher searcher = new IndexSearcher(indexReader);

        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        for(String keyword : keywords){
            Term term = new Term(ResumeAttachmentIndexFields.CONTENT,keyword);
            Query query = new TermQuery(term);
            builder.add(query, BooleanClause.Occur.MUST);
        }
        BooleanQuery query = builder.build();

        TopDocs hits = searcher.search(query, 10);
        List<ResumeFile> resumeFiles = new ArrayList<ResumeFile>();

        for(ScoreDoc scoreDoc : hits.scoreDocs){
            Document document = searcher.doc(scoreDoc.doc);
            ResumeFile resumeFile = new ResumeFile();
            resumeFile.setResumeId(Long.parseLong(document.get(ResumeAttachmentIndexFields.RESUME_ID)));
            resumeFile.setContent(document.get(ResumeAttachmentIndexFields.CONTENT));
            resumeFiles.add(resumeFile);
        }

        return resumeFiles;
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
     * @param resumeFile
     * @throws IOException
     */
    public void buildIndex(ResumeFile resumeFile) throws IOException {
        Directory directory = FSDirectory.open(new File(getBuildFolder()).toPath());
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        IndexWriter writer = new IndexWriter(directory,config);

        try {
            //构造Document
            Document document = new Document();
            document.add(new LongPoint(ResumeAttachmentIndexFields.RESUME_ID,resumeFile.getResumeId(),resumeFile.getResumeId()));
            document.add(new StringField(ResumeAttachmentIndexFields.RESUME_ID_STR,Long.toString(resumeFile.getResumeId()), Field.Store.YES));
            document.add(new TextField(ResumeAttachmentIndexFields.CONTENT,resumeFile.getContent(), Field.Store.NO));
            //生成索引文件
            writer.addDocument(document);
            writer.commit();
        } catch (IOException e) {
            log.error("create index error", e);
        } finally {
            IOUtils.closeQuietly(writer);
        }
    }

    /**
     * 添加索引文件
     * @param document
     * @return
     * @throws IOException
     */
    public Document appendIndex(Document document) throws IOException {
        Directory directory = FSDirectory.open(new File(getSearchFolder()).toPath());
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        IndexWriter writer = new IndexWriter(directory,config);

        try {
            writer.addDocument(document);
            writer.commit();
        } catch (IOException e) {
            log.error("append index error", e);
        } finally {
            IOUtils.closeQuietly(writer);
        }
        return document;
    }

    public Document appendIndex(ResumeAttach resumeAttach) {
        IndexWriter writer = null;
        Document document = new Document();
        try {
            Directory directory = FSDirectory.open(new File(getSearchFolder()).toPath());
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            writer = new IndexWriter(directory,config);
            document.add(new LongPoint(ResumeAttachmentIndexFields.RESUME_ID,resumeAttach.getResumeId()));
            document.add(new StringField(ResumeAttachmentIndexFields.RESUME_ID_STR,Long.toString(resumeAttach.getResumeId()), Field.Store.YES));
            document.add(new TextField(ResumeAttachmentIndexFields.CONTENT,resumeAttach.getAttachContent(), Field.Store.NO));

            writer.addDocument(document);
            writer.commit();
        } catch (IOException e) {
            log.error("append index error", e);
        } finally {
            IOUtils.closeQuietly(writer);
        }
        return document;
    }

    /**
     * 删除指定附件简历ID对应的索引文件
     * @param resumeId
     * @throws IOException
     */
    public void deleteIndex(Long resumeId) {
        IndexWriter writer = null;
        try {
            Directory directory = FSDirectory.open(new File(getSearchFolder()).toPath());
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            writer = new IndexWriter(directory,config);
            writer.deleteDocuments(LongPoint.newExactQuery(ResumeAttachmentIndexFields.RESUME_ID,resumeId));
            writer.commit();
        } catch (IOException e) {
            log.error("delete index error", e);
        } finally {
            IOUtils.closeQuietly(writer);
        }
    }

    /**
     * 通过附件简历ID获取Document对象
     * @param resumeId
     * @return
     * @throws IOException
     */
    public Document getDocumentByResumeId(Long resumeId) throws IOException {
        //创建一个Directory对象，指定索引库存放的路径
        Directory directory = FSDirectory.open(new File(getSearchFolder()).toPath());
        //创建IndexReader对象，需要指定Directory对象
        IndexReader indexReader = DirectoryReader.open(directory);
        //根据索引位置建立IndexSearch
        IndexSearcher searcher = new IndexSearcher(indexReader);

        TopDocs hits = searcher.search(LongPoint.newExactQuery(ResumeAttachmentIndexFields.RESUME_ID,resumeId), 10);
        Document document = null;

        //搜索有结果，理论上只会有一条记录，为了兼容意外情况，如果有多条记录，则取第一条
        if(hits.totalHits>=0){
            document = searcher.doc(hits.scoreDocs[0].doc);
        }

        return document;
    }
}
