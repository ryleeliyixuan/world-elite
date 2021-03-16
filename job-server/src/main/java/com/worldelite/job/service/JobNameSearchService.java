package com.worldelite.job.service;

import com.worldelite.job.constants.JobIndexFields;
import com.worldelite.job.constants.JobStatus;
import com.worldelite.job.entity.Job;
import com.worldelite.job.entity.JobOptions;
import com.worldelite.job.form.SearchNameForm;
import com.worldelite.job.mapper.JobMapper;
import com.worldelite.job.vo.PageResult;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

/**
 * @Author twz
 * @Date 2020/11/26 0026
 * @Desc TODO
 */
@Slf4j
@Service
//@AllArgsConstructor
public class JobNameSearchService {
    @Autowired
    private JobMapper jobMapper;

    @Autowired
    private Analyzer analyzer;

    @Resource(name = "jobIndexWriter")
    private IndexWriter indexWriter;

    @Resource(name = "jobSearcherManager")
    private SearcherManager searcherManager;


    @SneakyThrows
    public PageResult<String> searchJobName(SearchNameForm searchNameForm) {
        return searchJobName(searchNameForm.getKeyWords(), searchNameForm.getPage(), searchNameForm.getSize());
    }

    /**
     * 按职位名搜索
     *
     * @param keyWords
     * @param page
     * @param size
     * @return
     */
    @SneakyThrows
    public PageResult<String> searchJobName(String keyWords, int page, int size) {
        List<String> arrayList = new ArrayList<>();
        IndexSearcher indexSearcher = null;
        try {
            searcherManager.maybeRefresh();
            indexSearcher = searcherManager.acquire();

            BooleanQuery.Builder builder = new BooleanQuery.Builder();
            builder.add(new QueryParser(JobIndexFields.JOB_NAME, analyzer).parse(keyWords), BooleanClause.Occur.MUST);

            ScoreDoc lastScoreDoc = getLastScoreDoc(builder.build(), indexSearcher, page, size);
            final TopDocs topDocs = indexSearcher.searchAfter(lastScoreDoc, builder.build(), size);
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                final Document doc = indexSearcher.doc(scoreDoc.doc);

                arrayList.add(doc.get(JobIndexFields.JOB_NAME));
            }

            PageResult<String> pageResult = new PageResult<>();
            pageResult.setTotal(topDocs.totalHits);
            pageResult.setCurrentPage(page);
            pageResult.setPageSize(size);
            pageResult.setList(removeDuplicate(arrayList));
            pageResult.calPageCountAndHasMore(arrayList);

            return pageResult;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        } finally {
            if (indexSearcher != null)
                searcherManager.release(indexSearcher);
        }
        return new PageResult<String>().emptyResult();
    }


    private List<String> removeDuplicate(List<String> list) {
        Set<String> set = new HashSet<>(list);
        list.clear();
        list.addAll(set);
        return list;
    }


    /**
     * 刷新职位名索引
     */
    @SneakyThrows
    public void createOrRefreshJobNameIndex() {
        log.info("create or refresh all job name index.");
        List<Document> docs = new ArrayList<>();
        JobOptions options = new JobOptions();
        options.setStatus(JobStatus.PUBLISH.value);
        List<Job> jobs = jobMapper.selectAndList(options);
        jobs.forEach(job -> {
            Document doc = new Document();
            doc.add(new TextField(JobIndexFields.JOB_NAME, job.getName(), Field.Store.YES));

            docs.add(doc);
        });

        try {
            //删除旧的,虽然delete会自动提交,但是防止删除操作延迟到添加新的以后执行把新加的给删了 这里还是强制提交一次
            //将删除操作flush
            indexWriter.deleteAll();
            indexWriter.commit();

            //添加新的
            indexWriter.addDocuments(docs);
            indexWriter.commit();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }




    /**
     * 获取指定搜索页的上一页ScoreDoc,以实现深度分页查询
     */
    private ScoreDoc getLastScoreDoc(Query query, IndexSearcher indexSearcher, int page, int size) throws IOException {
        if (page == 1) {
            return null;//如果是第一页返回空
        }
        int num = size * (page - 1);
        TopDocs tds = indexSearcher.search(query, num);
        if (tds.scoreDocs.length == 0) {
            return null;
        }
        return num <= tds.scoreDocs.length ? tds.scoreDocs[num - 1] : tds.scoreDocs[tds.scoreDocs.length - 1];
    }

}
