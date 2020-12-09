package com.worldelite.job.service;

import com.worldelite.job.constants.ActivityIndexFields;
import com.worldelite.job.constants.CompanyIndexFields;
import com.worldelite.job.entity.Activity;
import com.worldelite.job.entity.ActivityOptions;
import com.worldelite.job.entity.Company;
import com.worldelite.job.entity.CompanyOptions;
import com.worldelite.job.form.SearchNameForm;
import com.worldelite.job.mapper.ActivityMapper;
import com.worldelite.job.mapper.CompanyMapper;
import com.worldelite.job.vo.PageResult;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Nuo Xu
 */
@Service
@Slf4j
public class ActivityTitleSearchService {

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private Analyzer analyzer;

    @Resource(name = "activityIndexWriter")
    private IndexWriter indexWriter;

    @Resource(name = "activitySearcherManager")
    private SearcherManager searcherManager;


    /**
     * 刷新职位名索引
     */
    @SneakyThrows
    public void createOrRefreshActivityTitleIndex() {
        List<Document> docs = new ArrayList<>();
        ActivityOptions options = new ActivityOptions();
        List<Activity> activities = activityMapper.selectAndList(options);
        activities.forEach(activity -> {
            Document doc = new Document();
            doc.add(new TextField(ActivityIndexFields.ACTIVITY_TITLE, activity.getTitle(), Field.Store.YES));
            docs.add(doc);
        });

        try {
            indexWriter.deleteDocuments(new Term(ActivityIndexFields.ACTIVITY_TITLE));
            indexWriter.commit();

            indexWriter.addDocuments(docs);
            indexWriter.commit();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @SneakyThrows
    public PageResult<String> searchActivityTitle(SearchNameForm searchNameForm) {
        return searchActivityTitle(searchNameForm.getKeyWords(), searchNameForm.getPage(), searchNameForm.getSize());
    }


    @SneakyThrows
    public PageResult<String> searchActivityTitle(String keyWords, int page, int size) {
        List<String> arrayList = new ArrayList<>();
        IndexSearcher indexSearcher = null;
        try {
            searcherManager.maybeRefresh();
            indexSearcher = searcherManager.acquire();

            BooleanQuery.Builder builder = new BooleanQuery.Builder();
            builder.add(new QueryParser(ActivityIndexFields.ACTIVITY_TITLE, analyzer).parse(keyWords), BooleanClause.Occur.MUST);

            ScoreDoc lastScoreDoc = getLastScoreDoc(builder.build(), indexSearcher, page, size);
            final TopDocs topDocs = indexSearcher.searchAfter(lastScoreDoc, builder.build(), size);
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                final Document doc = indexSearcher.doc(scoreDoc.doc);

                arrayList.add(doc.get(ActivityIndexFields.ACTIVITY_TITLE));
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
