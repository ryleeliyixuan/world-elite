package com.worldelite.job.context.config;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.ControlledRealTimeReopenThread;
import org.apache.lucene.search.SearcherFactory;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.lionsoul.jcseg.ISegment;
import org.lionsoul.jcseg.analyzer.JcsegAnalyzer;
import org.lionsoul.jcseg.dic.ADictionary;
import org.lionsoul.jcseg.dic.DictionaryFactory;
import org.lionsoul.jcseg.segmenter.SegmenterConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * lucene分词器
 *
 * @author yeguozhong yedaxia.github.com
 */
@Configuration
public class LuceneJobNameConfig {

    /**
     * lucene索引,存放位置
     */
    @Value(value = "${search.index.job}")
    private String LUCENE_INDEX_DIR;

    @Autowired
    private Analyzer analyzer;

//    /**
//     * 创建一个 Analyzer 实例
//     *
//     * @return
//     */
//    @Bean
//    public Analyzer registerAnalyser() {
//        SegmenterConfig config = new SegmenterConfig(true);
//        ADictionary dictionary = DictionaryFactory.createDefaultDictionary(config);
//        //重置同义词网络
//        dictionary.resetSynonymsNet();
//        return new JcsegAnalyzer(ISegment.COMPLEX, config, dictionary);
//    }


    /**
     * 索引位置
     *
     * @return
     * @throws IOException
     */
    @Bean(name = "jobDirectory")
    public Directory directory() throws IOException {

        Path path = Paths.get(LUCENE_INDEX_DIR);
        File file = path.toFile();
        if (!file.exists()) {
            //如果文件夹不存在,则创建
            file.mkdirs();
        }
        return FSDirectory.open(path);
    }


    /**
     * 创建indexWriter
     *
     * @param jobDirectory
     * @param analyzer
     * @return
     * @throws IOException
     */
    @Bean("jobIndexWriter")
    public IndexWriter indexWriter(Directory jobDirectory, Analyzer analyzer) throws IOException {
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(jobDirectory, indexWriterConfig);
        // 清空索引
        //indexWriter.deleteAll();
        //indexWriter.commit();
        return indexWriter;
    }


    /**
     * SearcherManager管理
     *
     * @throws IOException
     */
    @Bean("jobSearcherManager")
    public SearcherManager searcherManager(IndexWriter jobIndexWriter) throws IOException {
        SearcherManager searcherManager = new SearcherManager(jobIndexWriter, false, false, new SearcherFactory());
        ControlledRealTimeReopenThread cRTReopenThead = new ControlledRealTimeReopenThread(jobIndexWriter, searcherManager,
                5.0, 0.025);
        cRTReopenThead.setDaemon(true);
        //线程名称
        cRTReopenThead.setName("IndexReader Update Thread");
        // 开启线程
        cRTReopenThead.start();
        return searcherManager;
    }
}
