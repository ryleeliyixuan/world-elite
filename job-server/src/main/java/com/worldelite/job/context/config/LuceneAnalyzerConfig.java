package com.worldelite.job.context.config;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.lionsoul.jcseg.ISegment;
import org.lionsoul.jcseg.analyzer.JcsegAnalyzer;
import org.lionsoul.jcseg.dic.ADictionary;
import org.lionsoul.jcseg.dic.DictionaryFactory;
import org.lionsoul.jcseg.segmenter.SegmenterConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * lucene分词器
 *
 * @author yeguozhong yedaxia.github.com
 */
@Configuration
public class LuceneAnalyzerConfig {

    @Value("${search.index.resumepath}")
    private String resumeIndexFolder;

    @Bean
    public Analyzer registerAnalyser() throws IOException {
        SegmenterConfig config = new SegmenterConfig();
        //开启同义词功能
        config.setAppendCJKSyn(true);
        ADictionary dictionary = DictionaryFactory.createDefaultDictionary(config);
        //除了默认的词库，还要加载专门针对简历的自定义词库
        dictionary.loadDirectory(resumeIndexFolder);
        //重置同义词网络
        dictionary.resetSynonymsNet();
        return new JcsegAnalyzer(ISegment.COMPLEX, config, dictionary);
    }

}
