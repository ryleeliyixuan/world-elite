package com.worldelite.job.context.config;

import org.apache.lucene.analysis.Analyzer;
import org.lionsoul.jcseg.ISegment;
import org.lionsoul.jcseg.analyzer.JcsegAnalyzer;
import org.lionsoul.jcseg.dic.ADictionary;
import org.lionsoul.jcseg.dic.DictionaryFactory;
import org.lionsoul.jcseg.segmenter.SegmenterConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * lucene分词器
 *
 * @author yeguozhong yedaxia.github.com
 */
@Configuration
public class LuceneAnalyzerConfig {

    @Bean
    public Analyzer registerAnalyser() {
        SegmenterConfig config = new SegmenterConfig(true);
        ADictionary dictionary = DictionaryFactory.createDefaultDictionary(config);
        //重置同义词网络
        dictionary.resetSynonymsNet();
        return new JcsegAnalyzer(ISegment.COMPLEX, config, dictionary);
    }
}
