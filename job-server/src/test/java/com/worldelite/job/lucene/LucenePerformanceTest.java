package com.worldelite.job.lucene;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.jupiter.api.Test;
import org.lionsoul.jcseg.ISegment;
import org.lionsoul.jcseg.analyzer.JcsegAnalyzer;
import org.lionsoul.jcseg.dic.ADictionary;
import org.lionsoul.jcseg.dic.DictionaryFactory;
import org.lionsoul.jcseg.dic.HashMapDictionary;
import org.lionsoul.jcseg.segmenter.SegmenterConfig;

import javax.print.Doc;
import java.io.*;
import java.util.Date;

/**
 * Lucene性能测试
 */
public class LucenePerformanceTest {

    //测试文件路径
    private final String TEST_FILE = "D:\\lucene_test.txt";
    //索引文件夹路径
    private final String INDEX_FOLDER = "D:\\index";

    @Test
    public void test() throws IOException {
//        for(int i=10;i<10000;i+=2000){
//            performanceTest(i);
//        }
        searchContent("唐","诗","TEST");
    }

    /**
     * 测试不同文件数创建索引和搜索的效率
     * @param number
     */
    private void performanceTest(int number) throws IOException {
        //清空索引文件夹
        removeDir(new File(INDEX_FOLDER));
        //创建索引
        long indexTime = writeIndex(number);
        //搜索
        long searchTime = searchContent(RandomStringUtils.randomAlphanumeric(5));
        //输出耗时
        System.out.println("文件数："+number+",创建索引耗时："+indexTime+"ms,"+"搜索耗时："+searchTime+"ms");
    }

    /**
     * 将文本内容创建索引
     * @param number
     * @return
     */
    private long writeIndex(int number) throws IOException {
        Directory directory = FSDirectory.open(new File(INDEX_FOLDER).toPath());
        //SegmenterConfig segmenterConfig = new SegmenterConfig();
        //ADictionary dictionary = DictionaryFactory.createDefaultDictionary(segmenterConfig);
        //Analyzer analyzer = new JcsegAnalyzer(ISegment.COMPLEX, segmenterConfig, dictionary);
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(directory,config);

        //读取测试文件内容，转换成文本
        File file = new File(TEST_FILE);
        String txt = loadFileToString(file);

        Date beginTime = new Date();
        for(int i=0;i<number;i++) {
            //创建n个Document对象
            Document document = new Document();
            //添加文件名字段，这里使用随机字符串模拟
            document.add(new StringField("name", RandomStringUtils.randomAlphanumeric(20), Field.Store.YES));
            //添加文件内容
            document.add(new TextField("content", txt, Field.Store.NO));
            writer.addDocument(document);
        }

        //生成索引文件
        writer.commit();
        writer.close();
        return new Date().getTime()-beginTime.getTime();
    }

    /**
     * 搜索内容
     * @param contents
     * @return
     * @throws IOException
     */
    private long searchContent(String... contents) throws IOException {
        //创建一个Directory对象，指定索引库存放的路径
        Directory directory = FSDirectory.open(new File(INDEX_FOLDER).toPath());
        //创建IndexReader对象，需要指定Directory对象
        IndexReader indexReader = DirectoryReader.open(directory);
        //根据索引位置建立IndexSearch
        IndexSearcher searcher = new IndexSearcher(indexReader);

        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        for(String content : contents){
            Term term = new Term("content",content);
            Query query = new TermQuery(term);
            builder.add(query, BooleanClause.Occur.MUST);
        }

        BooleanQuery query = builder.build();

        //搜素开始时间
        Date beginTime = new Date();

        TopDocs hits = searcher.search(query, 10);

        System.out.println(hits.totalHits);

        //搜索完成时间
        Date endTime = new Date();

        //搜索耗费时间
        return endTime.getTime()-beginTime.getTime();
    }

    /**
     * 将文件的内容转换成字符串返回.
     * @param file
     * @return
     */
    private String loadFileToString(File file){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            StringBuffer buffer = new StringBuffer();
            String line = null;
            while((line=reader.readLine()) != null){
                buffer.append(line);
            }
            reader.close();
            return buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 清空文件夹下所有文件
     * @param dir
     */
    private void removeDir(File dir) {
        File[] files=dir.listFiles();
        for(File file:files){
            if(file.isDirectory()){
                removeDir(file);
            }else{
                file.delete();
            }
        }
    }
}
