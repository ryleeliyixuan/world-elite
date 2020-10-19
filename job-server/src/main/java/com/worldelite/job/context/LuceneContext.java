package com.worldelite.job.context;

import com.worldelite.job.exception.ServiceException;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;
import org.apache.poi.ss.formula.functions.Index;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.lionsoul.jcseg.ISegment;
import org.lionsoul.jcseg.IWord;
import org.lionsoul.jcseg.analyzer.JcsegAnalyzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Lucene搜索引擎上下文
 * 维护了一个IndexWriter池
 * 同一个文件夹总返回同一个IndexWriter实例
 */
@Component
public class LuceneContext {

    @Autowired
    private Analyzer analyzer;

    //IndexWriter Map，文件夹名做为key，同一个文件夹将返回同一个IndexWriter实例
    //使用了线程安全的ConcurrentHashMap
    private final ConcurrentHashMap<String, IndexWriter> indexWriterMap = new ConcurrentHashMap<>();

    //IndexReader Map
    private final ConcurrentHashMap<String, IndexSearcher> indexSearcherMap = new ConcurrentHashMap<>();

    /**
     * 获取IndexWriter对象实例
     * @param folder 索引文件夹
     * @return
     * @throws IOException
     */
    public IndexWriter getIndexWriter(String folder) {
        IndexWriter indexWriter = indexWriterMap.get(folder);
        if(indexWriter != null){
            return indexWriter;
        }
        //如果目录下不存在IndexWriter实例，或者已经被关闭，则新建实例
        try {
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            indexWriter = new IndexWriter(FSDirectory.open(Paths.get(folder)), config);
            indexWriterMap.put(folder,indexWriter);
            return indexWriter;
        } catch (IOException e) {
            closeIndexWriter(folder);
            e.printStackTrace();
            throw new ServiceException("打开索引目录失败");
        }
    }

    /**
     * 获取IndexSearcher对象实例
     * @param folder 索引文件夹
     * @return
     */
    public IndexSearcher getIndexSearcher(String folder){
        IndexSearcher indexSearcher = indexSearcherMap.get(folder);
        try {
            if(indexSearcher != null){
                //判断是否需要重新打开索引文件夹
                IndexReader sIndexReader = indexSearcher.getIndexReader();
                if(sIndexReader != null) {
                    IndexReader indexReader = DirectoryReader.openIfChanged((DirectoryReader) sIndexReader);
                    if (indexReader != null) {
                        sIndexReader.close();
                        sIndexReader = indexReader;
                        indexSearcher = new IndexSearcher(sIndexReader);
                        indexSearcherMap.put(folder,indexSearcher);
                    }
                    return indexSearcher;
                }
            }
            IndexReader indexReader = DirectoryReader.open(FSDirectory.open(Paths.get(folder)));
            indexSearcher = new IndexSearcher(indexReader);
            indexSearcherMap.put(folder,indexSearcher);
            return indexSearcher;
        } catch (IOException e) {
            indexSearcherMap.remove(folder);
            e.printStackTrace();
            throw new ServiceException("搜索时打开索引文件失败");
        }
    }

    /**
     * 关闭IndexWriter
     * @param folder
     */
    public void closeIndexWriter(String folder){
        IndexWriter indexWriter = indexWriterMap.get(folder);
        IOUtils.closeQuietly(indexWriter);
        indexWriterMap.remove(folder);
    }

    /**
     * 对字符串进行分词，返回分词结果
     * @param keyword
     * @return
     * @throws IOException
     */
    public String[] analysis(String keyword) {

        ISegment seg = ISegment.COMPLEX.factory.create(((JcsegAnalyzer)analyzer).getConfig(), ((JcsegAnalyzer)analyzer).getDict());

        try {
            seg.reset(new StringReader(keyword));
            IWord word = null;
            List<String> wordList = new ArrayList<String>();
            while (true) {
                if (!((word = seg.next()) != null)) break;
                wordList.add(word.getValue());
            }
            return wordList.toArray(new String[wordList.size()]);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceException("对搜索关键字分词失败");
        }
    }

}
