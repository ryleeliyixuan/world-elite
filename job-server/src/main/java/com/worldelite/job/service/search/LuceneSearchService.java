package com.worldelite.job.service.search;

import com.worldelite.job.constants.JobIndexFields;
import com.worldelite.job.form.JobSearchForm;
import com.worldelite.job.form.PageForm;
import com.worldelite.job.service.DictService;
import com.worldelite.job.service.JobService;
import com.worldelite.job.vo.DictVo;
import com.worldelite.job.vo.JobVo;
import com.worldelite.job.vo.PageResult;
import com.worldelite.job.vo.SalaryRange;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * lucene 搜索实现
 *
 * @author yeguozhong yedaxia.github.com
 */
@Service
@Slf4j
public class LuceneSearchService implements SearchService {

    @Value("${search.index.path}")
    private String searchIndexPath;

    @Autowired
    private Analyzer analyzer;

    @Autowired
    private JobService jobService;

    @Autowired
    private DictService dictService;

    private static IndexReader sIndexReader;

    @Override
    public PageResult<JobVo> searchJob(JobSearchForm searchForm) {
        BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
        if (StringUtils.isNotEmpty(searchForm.getKeyword())) {
            Query query = new QueryBuilder(analyzer).createBooleanQuery(JobIndexFields.KEYWORD_INDEX, searchForm.getKeyword());
            queryBuilder.add(query, BooleanClause.Occur.MUST);
        }
        if (ArrayUtils.isNotEmpty(searchForm.getCityIds())) {
            queryBuilder.add(addMultiShouldQuery(searchForm.getCityIds(), JobIndexFields.CITY_INDEX), BooleanClause.Occur.MUST);
        }
        if (ArrayUtils.isNotEmpty(searchForm.getCompanyIndustryIds())) {
            queryBuilder.add(addMultiShouldQuery(searchForm.getCompanyIndustryIds(), JobIndexFields.COMPANY_INDUSTRY_INDEX), BooleanClause.Occur.MUST);
        }
        if (ArrayUtils.isNotEmpty(searchForm.getCompanyScaleIds())) {
            queryBuilder.add(addMultiShouldQuery(searchForm.getCompanyScaleIds(), JobIndexFields.COMPANY_SCALE_INDEX), BooleanClause.Occur.MUST);
        }
        if (ArrayUtils.isNotEmpty(searchForm.getCompanyStageIds())) {
            queryBuilder.add(addMultiShouldQuery(searchForm.getCompanyStageIds(), JobIndexFields.COMPANY_STAGE_INDEX), BooleanClause.Occur.MUST);
        }
        if (ArrayUtils.isNotEmpty(searchForm.getDegreeIds())) {
            queryBuilder.add(addMultiShouldQuery(searchForm.getDegreeIds(), JobIndexFields.MIN_DEGREE_INDEX), BooleanClause.Occur.MUST);
        }
        if (ArrayUtils.isNotEmpty(searchForm.getCategoryIds())) {
            BooleanQuery.Builder categoryQueryBuilder = new BooleanQuery.Builder();
            categoryQueryBuilder.add(addMultiShouldQuery(searchForm.getCategoryIds(), JobIndexFields.CATEGORY_FIRST_INDEX), BooleanClause.Occur.SHOULD);
            categoryQueryBuilder.add(addMultiShouldQuery(searchForm.getCategoryIds(), JobIndexFields.CATEGORY_SECOND_INDEX), BooleanClause.Occur.SHOULD);
            categoryQueryBuilder.add(addMultiShouldQuery(searchForm.getCategoryIds(), JobIndexFields.CATEGORY_THIRD_INDEX), BooleanClause.Occur.SHOULD);
            queryBuilder.add(queryBuilder.build(), BooleanClause.Occur.MUST);
        }
        if(searchForm.getMinSalary() != null && searchForm.getMaxSalary() != null){
            Query query = IntPoint.newRangeQuery(JobIndexFields.AVER_SALARY_INDEX, searchForm.getMinSalary(), searchForm.getMaxSalary());
            queryBuilder.add(query, BooleanClause.Occur.MUST);
        }
        if(ArrayUtils.isNotEmpty(searchForm.getSalaryRangeIds())){
            BooleanQuery.Builder salaryRangeQueryBuilder = new BooleanQuery.Builder();
            for(Integer rangeId: searchForm.getSalaryRangeIds()){
                SalaryRange salaryRange = dictService.getSalaryRange(rangeId);
                if(salaryRange != null){
                    Query query = IntPoint.newRangeQuery(JobIndexFields.AVER_SALARY_INDEX, salaryRange.getMin(), salaryRange.getMax());
                    salaryRangeQueryBuilder.add(query, BooleanClause.Occur.SHOULD);
                }
            }
            queryBuilder.add(salaryRangeQueryBuilder.build(), BooleanClause.Occur.MUST);
        }
        if(searchForm.getJobType() != null){
            Query query = IntPoint.newExactQuery(JobIndexFields.JOB_TYPE_INDEX, searchForm.getJobType());
            queryBuilder.add(query, BooleanClause.Occur.MUST);
        }
        if(ArrayUtils.isNotEmpty(searchForm.getJobTypes())){
            queryBuilder.add(addMultiShouldQuery(searchForm.getJobTypes(), JobIndexFields.JOB_TYPE_INDEX), BooleanClause.Occur.MUST);
        }
        return searchByQuery(queryBuilder.build(), searchForm);
    }

    private Query addMultiShouldQuery(Integer[] ids, String indexField) {
        BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
        for (Integer cityId : ids) {
            Query query = IntPoint.newExactQuery(indexField, cityId);
            queryBuilder.add(query, BooleanClause.Occur.SHOULD);
        }
        return queryBuilder.build();
    }

    private PageResult<JobVo> searchByQuery(Query query, PageForm pageForm) {
        IndexSearcher indexSearcher = getIndexSearcher();
        try {
            ScoreDoc lastScoreDoc = getLastScoreDoc(pageForm, query, indexSearcher);
            TopDocs topDocs = indexSearcher.searchAfter(lastScoreDoc, query, pageForm.getLimit());
            PageResult<JobVo> pageResult = new PageResult<>();
            pageResult.setTotal(topDocs.totalHits);
            pageResult.setCurrentPage(pageForm.getPage());
            pageResult.setPageSize(pageForm.getLimit());
            List<JobVo> subtitleVoList = new ArrayList<>();

            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                Document hitDoc = indexSearcher.doc(scoreDoc.doc);
                Long jobId = NumberUtils.toLong(hitDoc.get(JobIndexFields.JOB_ID));
                JobVo jobVo = jobService.getJobInfo(jobId, true);
                subtitleVoList.add(jobVo);
            }

            pageResult.setList(subtitleVoList);
            pageResult.calPageCountAndHasMore(subtitleVoList);
            return pageResult;
        } catch (IOException ex) {
            log.error("searchByKeyword error, query: " + query.toString() + ", page: " + pageForm.toString(), ex);
            return PageResult.emptyResult();
        }
    }

    private ScoreDoc getLastScoreDoc(PageForm pagerForm, Query query, IndexSearcher indexSearcher) throws IOException {
        if (pagerForm.getPage() == 1) {
            return null;//如果是第一页返回空
        }
        int num = pagerForm.getLimit() * (pagerForm.getPage() - 1);
        TopDocs tds = indexSearcher.search(query, num);
        if(tds.scoreDocs.length == 0){
            return null;
        }
        return num <= tds.scoreDocs.length ? tds.scoreDocs[num - 1] : tds.scoreDocs[tds.scoreDocs.length - 1];
    }

    private IndexSearcher getIndexSearcher() {
        try {
            if (sIndexReader == null) {
                synchronized (this) {
                    if (sIndexReader == null) {
                        sIndexReader = DirectoryReader.open(FSDirectory.open(Paths.get(searchIndexPath)));
                    }
                }
            } else {
                IndexReader indexReader = DirectoryReader.openIfChanged((DirectoryReader) sIndexReader);
                if (indexReader != null) {
                    sIndexReader.close();
                    sIndexReader = indexReader;
                }
            }
            return new IndexSearcher(sIndexReader);
        } catch (IOException ex) {
            log.error("get index searcher error", ex);
            return null;
        }
    }
}
