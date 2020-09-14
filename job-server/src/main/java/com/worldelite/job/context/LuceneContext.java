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
    private final ConcurrentHashMap<String, IndexWriter> indexWriterMap = new ConcurrentHashMap<String,IndexWriter>();

    //IndexReader Map
    private final ConcurrentHashMap<String, IndexReader> indexReaderMap = new ConcurrentHashMap<String,IndexReader>();

    @Value("${search.index.resumeindex2}")
    private String indexFolder2;


    @PostConstruct
    public void initMap() throws IOException {
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        IndexWriter indexWriter = new IndexWriter(FSDirectory.open(Paths.get(indexFolder2)), config);
        indexWriterMap.put(indexFolder2,indexWriter);

        IndexReader indexReader = DirectoryReader.open(FSDirectory.open(Paths.get(indexFolder2)));
        indexReaderMap.put(indexFolder2,indexReader);
    }


    /**
     * 获取IndexWriter对象实例
     * @param folder 索引文件夹
     * @return
     * @throws IOException
     */
    public IndexWriter getIndexWriter(String folder) {
        return indexWriterMap.get(folder);
    }

    /**
     * 获取IndexSearcher对象实例
     * @param folder 索引文件夹
     * @return
     */
    public IndexSearcher getIndexSearcher(String folder){
        IndexReader indexReader = null;
        try {
            indexReader = DirectoryReader.openIfChanged((DirectoryReader) indexReaderMap.get(folder));
            return new IndexSearcher(indexReader);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceException("搜索时打开索引文件失败");
        }
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
