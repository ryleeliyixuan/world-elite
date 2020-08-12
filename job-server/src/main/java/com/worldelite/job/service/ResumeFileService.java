package com.worldelite.job.service;

import com.worldelite.job.constants.ResumeFileIndexFields;
import com.worldelite.job.entity.ResumeFile;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ResumeFileService extends BaseService{

    //索引文件夹路径
    private final String INDEX_FOLDER = "D:\\index";

    @Autowired
    private Analyzer analyzer;

    /**
     * 创建或者刷新索引
     * @param resumeFile
     * @throws IOException
     */
    public void refreshIndex(ResumeFile resumeFile) throws IOException {
        Directory directory = FSDirectory.open(new File(INDEX_FOLDER).toPath());
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(directory,config);

        //构造Document
        Document document = new Document();
        document.add(new StringField(ResumeFileIndexFields.RESUME_ID,Long.toString(resumeFile.getResumeId()), Field.Store.YES));
        document.add(new StringField(ResumeFileIndexFields.FILE_NAME, resumeFile.getFileName(), Field.Store.YES));
        document.add(new TextField(ResumeFileIndexFields.CONTENT,resumeFile.getContent(), Field.Store.NO));

        //生成索引文件
        writer.addDocument(document);
        writer.commit();
        writer.close();
    }

    /**
     * 通过关键字搜索简历文件
     * @param keywords
     * @return
     * @throws IOException
     */
    public List<ResumeFile> searchByKeyWords(String[] keywords) throws IOException {
        //创建一个Directory对象，指定索引库存放的路径
        Directory directory = FSDirectory.open(new File(INDEX_FOLDER).toPath());
        //创建IndexReader对象，需要指定Directory对象
        IndexReader indexReader = DirectoryReader.open(directory);
        //根据索引位置建立IndexSearch
        IndexSearcher searcher = new IndexSearcher(indexReader);

        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        for(String keyword : keywords){
            Term term = new Term(ResumeFileIndexFields.CONTENT,keyword);
            Query query = new TermQuery(term);
            builder.add(query, BooleanClause.Occur.MUST);
        }
        BooleanQuery query = builder.build();

        TopDocs hits = searcher.search(query, 10);
        List<ResumeFile> resumeFiles = new ArrayList<ResumeFile>();

        for(ScoreDoc scoreDoc : hits.scoreDocs){
            Document document = searcher.doc(scoreDoc.doc);
            ResumeFile resumeFile = new ResumeFile();
            resumeFile.setResumeId(Long.parseLong(document.get(ResumeFileIndexFields.RESUME_ID)));
            resumeFile.setFileName(document.get(ResumeFileIndexFields.FILE_NAME));
            resumeFile.setContent(document.get(ResumeFileIndexFields.CONTENT));
            resumeFiles.add(resumeFile);
        }

        return resumeFiles;
    }

}
