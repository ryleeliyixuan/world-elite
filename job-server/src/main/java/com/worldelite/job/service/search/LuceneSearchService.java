package com.worldelite.job.service.search;

import com.worldelite.job.constants.JobIndexFields;
import com.worldelite.job.constants.ResumeAttachmentIndexFields;
import com.worldelite.job.constants.ResumeIndexFields;
import com.worldelite.job.context.LuceneContext;
import com.worldelite.job.entity.Dict;
import com.worldelite.job.entity.Resume;
import com.worldelite.job.entity.ResumeAttach;
import com.worldelite.job.entity.ResumeDetail;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.JobSearchForm;
import com.worldelite.job.form.PageForm;
import com.worldelite.job.form.ResumeAttachmentForm;
import com.worldelite.job.form.ResumeListForm;
import com.worldelite.job.mapper.ResumeAttachMapper;
import com.worldelite.job.mapper.ResumeMapper;
import com.worldelite.job.service.DictService;
import com.worldelite.job.service.JobCategoryService;
import com.worldelite.job.service.JobService;
import com.worldelite.job.service.ResumeAttachService;
import com.worldelite.job.service.resume.ResumeService;
import com.worldelite.job.service.resume.ResumeServiceFactory;
import com.worldelite.job.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.FloatPoint;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Value("${search.index.resumeindex1}")
    private String resumeAttachmentPath1;

    @Value("${search.index.resumeindex2}")
    private String resumeAttachmentPath2;

    @Value("${domain.oss}")
    private String ossDomain;

    @Autowired
    private Analyzer analyzer;

    @Autowired
    private JobService jobService;

    @Autowired
    private ResumeMapper resumeMapper;

    @Autowired
    private ResumeAttachMapper resumeAttachMapper;

    @Autowired
    private ResumeAttachService resumeAttachService;

    @Autowired
    private DictService dictService;

    @Autowired
    private JobCategoryService categoryService;

    @Autowired
    private LuceneContext luceneContext;

    private static IndexReader sIndexReader;

    private boolean switchFolder = false;

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
            // 把二级职位也计算上，避免结果太少
            Set<Integer> secondCategorySet = new HashSet<>();
            for (Integer categoryId : searchForm.getCategoryIds()) {
                JobCategoryVo jobCategoryVo = categoryService.getById(categoryId);
                if (jobCategoryVo != null) {
                    secondCategorySet.add(jobCategoryVo.getParentId());
                }
            }
            if (CollectionUtils.isNotEmpty(secondCategorySet)) {
                categoryQueryBuilder.add(addMultiShouldQuery(secondCategorySet.toArray(new Integer[0]), JobIndexFields.CATEGORY_SECOND_INDEX), BooleanClause.Occur.SHOULD);
            }
            queryBuilder.add(categoryQueryBuilder.build(), BooleanClause.Occur.MUST);
        }
        if (searchForm.getMinSalary() != null && searchForm.getMaxSalary() != null) {
            Query query = IntPoint.newRangeQuery(JobIndexFields.AVER_SALARY_INDEX, searchForm.getMinSalary(), searchForm.getMaxSalary());
            queryBuilder.add(query, BooleanClause.Occur.MUST);
        }
        if (ArrayUtils.isNotEmpty(searchForm.getSalaryRangeIds())) {
            BooleanQuery.Builder salaryRangeQueryBuilder = new BooleanQuery.Builder();
            for (Integer rangeId : searchForm.getSalaryRangeIds()) {
                Query query = IntPoint.newExactQuery(JobIndexFields.AVER_SALARY_INDEX,rangeId);
                salaryRangeQueryBuilder.add(query, BooleanClause.Occur.SHOULD);
            }
            queryBuilder.add(salaryRangeQueryBuilder.build(), BooleanClause.Occur.MUST);
        }
        if (searchForm.getJobType() != null) {
            Query query = IntPoint.newExactQuery(JobIndexFields.JOB_TYPE_INDEX, searchForm.getJobType());
            queryBuilder.add(query, BooleanClause.Occur.MUST);
        }
        if (ArrayUtils.isNotEmpty(searchForm.getJobTypes())) {
            queryBuilder.add(addMultiShouldQuery(searchForm.getJobTypes(), JobIndexFields.JOB_TYPE_INDEX), BooleanClause.Occur.MUST);
        }
        if(searchForm.getCompanyId() != null){
            queryBuilder.add(LongPoint.newExactQuery(JobIndexFields.COMPANY_ID, searchForm.getCompanyId()), BooleanClause.Occur.MUST);
        }
        if (searchForm.getRecruitId() != null && searchForm.getRecruitId() != 0) {
            queryBuilder.add(IntPoint.newExactQuery(JobIndexFields.RECRUIT_TYPE_INDEX, searchForm.getRecruitId()), BooleanClause.Occur.MUST);
        }


        return searchJobByQuery(queryBuilder.build(), searchForm);
    }

    @Override
    public PageResult<ResumeVo> getJobRecommendResumes(Long jobId, PageForm pageForm) {
        JobVo jobVo = jobService.getJobInfo(jobId, false);
        if (jobVo == null || (jobVo.getCity() == null && jobVo.getCategory() == null)) {
            return PageResult.emptyResult();
        }
        BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
        CityVo expectCity = jobVo.getCity();
        if (expectCity != null) {
            BooleanQuery.Builder cityBuilder = new BooleanQuery.Builder();
            cityBuilder.add(IntPoint.newExactQuery(ResumeIndexFields.EXPECT_JOB_FIRST_CITY, expectCity.getId()), BooleanClause.Occur.SHOULD);
            cityBuilder.add(IntPoint.newExactQuery(ResumeIndexFields.EXPECT_JOB_SECOND_CITY, expectCity.getId()), BooleanClause.Occur.SHOULD);
            cityBuilder.add(IntPoint.newExactQuery(ResumeIndexFields.EXPECT_JOB_THIRD_CITY, expectCity.getId()), BooleanClause.Occur.SHOULD);
            queryBuilder.add(cityBuilder.build(), BooleanClause.Occur.MUST);
        }
        JobCategoryVo jobCategoryVo = jobVo.getCategory();
        if (jobCategoryVo != null) {
            BooleanQuery.Builder categoryBuilder = new BooleanQuery.Builder();
            categoryBuilder.add(IntPoint.newExactQuery(ResumeIndexFields.EXPECT_JOB_FIRST_CATEGORY, jobCategoryVo.getId()), BooleanClause.Occur.SHOULD);
            categoryBuilder.add(IntPoint.newExactQuery(ResumeIndexFields.EXPECT_JOB_SECOND_CATEGORY, jobCategoryVo.getId()), BooleanClause.Occur.SHOULD);
            categoryBuilder.add(IntPoint.newExactQuery(ResumeIndexFields.EXPECT_JOB_THIRD_CATEGORY, jobCategoryVo.getId()), BooleanClause.Occur.SHOULD);
            queryBuilder.add(categoryBuilder.build(), BooleanClause.Occur.MUST);
        }
        return searchResumeByQuery(queryBuilder.build(), pageForm);
    }

    @Override
    public PageResult<ResumeVo> searchResumeAttachment(ResumeAttachmentForm searchForm) {
        //附件简历索引文件夹有两个，搜索时需要使用搜索索引文件夹
        IndexSearcher indexSearcher = getIndexSearcher(getSearchFolder());
        //构造搜索请求
        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        for (String keyword : searchForm.getKeywords()) {
            //目标简历附件必须包含所有关键词
            Term term = new Term(ResumeAttachmentIndexFields.CONTENT, keyword);
            Query query = new TermQuery(term);
            builder.add(query, BooleanClause.Occur.MUST);
        }
        BooleanQuery query = builder.build();

        try {
            ScoreDoc lastScoreDoc = getLastScoreDoc(searchForm, query, indexSearcher);
            TopDocs topDocs = indexSearcher.searchAfter(lastScoreDoc, query, searchForm.getLimit());
            PageResult<ResumeVo> pageResult = new PageResult<>();
            pageResult.setTotal(topDocs.totalHits);
            pageResult.setCurrentPage(searchForm.getPage());
            pageResult.setPageSize(searchForm.getLimit());
            List<ResumeVo> resumeVoList = new ArrayList<>();

            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                Document hitDoc = indexSearcher.doc(scoreDoc.doc);
                Long resumeId = NumberUtils.toLong(hitDoc.get(ResumeAttachmentIndexFields.RESUME_ID_STR));
                ResumeService resumeService = ResumeServiceFactory.getResumeService(resumeId);
                ResumeDetail resumeDetail = resumeService.getResumeDetail(resumeId);
                ResumeVo resumeVo = resumeService.toResumeVo(resumeDetail);
                resumeVoList.add(resumeVo);
            }

            pageResult.setList(resumeVoList);
            pageResult.calPageCountAndHasMore(resumeVoList);
            return pageResult;
        } catch (IOException ex) {
            log.error("searchResumeByQuery error, query: " + query.toString() + ", page: " + searchForm.toString(), ex);
            return PageResult.emptyResult();
        }
    }

    @Override
    public PageResult<ResumeAttachVo> searchResumeAttachment2(ResumeAttachmentForm searchForm) {
        //附件简历索引文件夹有两个，搜索时需要使用搜索索引文件夹
        IndexSearcher indexSearcher = getIndexSearcher(getSearchFolder());
        //构造搜索请求
        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        //预处理关键词，用与生成索引文件一样的分词器将关键词分词
        List<String> keywordList = new ArrayList<String>();
        for (String keyword : searchForm.getKeywords()) {
            String[] tempKeywords = new String[]{};
            try {
                tempKeywords = resumeAttachService.analysis(keyword);
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (String tempKeyword : tempKeywords) {
                keywordList.add(tempKeyword);
            }
        }
        for (String keyword : keywordList) {
            //目标简历附件必须包含所有关键词
            Term term = new Term(ResumeAttachmentIndexFields.CONTENT, keyword);
            Query query = new TermQuery(term);
            builder.add(query, BooleanClause.Occur.MUST);
        }
        BooleanQuery query = builder.build();

        try {
            ScoreDoc lastScoreDoc = getLastScoreDoc(searchForm, query, indexSearcher);
            TopDocs topDocs = indexSearcher.searchAfter(lastScoreDoc, query, searchForm.getLimit());
            PageResult<ResumeAttachVo> pageResult = new PageResult<>();
            pageResult.setTotal(topDocs.totalHits);
            pageResult.setCurrentPage(searchForm.getPage());
            pageResult.setPageSize(searchForm.getLimit());
            List<ResumeAttachVo> resumeVoList = new ArrayList<>();

            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                Document hitDoc = indexSearcher.doc(scoreDoc.doc);
                Long resumeId = NumberUtils.toLong(hitDoc.get(ResumeAttachmentIndexFields.RESUME_ID_STR));
                ResumeAttach resumeAttach = resumeAttachMapper.selectByResumeIdWithBLOBs(resumeId);
                Resume resume = resumeMapper.selectByPrimaryKey(resumeId);
                resumeAttach.setDocPath(ossDomain + resume.getAttachResume());
                resumeVoList.add(new ResumeAttachVo().asVo(resumeAttach));
            }

            pageResult.setList(resumeVoList);
            pageResult.calPageCountAndHasMore(resumeVoList);
            return pageResult;
        } catch (IOException ex) {
            log.error("searchResumeByQuery error, query: " + query.toString() + ", page: " + searchForm.toString(), ex);
            return PageResult.emptyResult();
        }
    }

    private Query addMultiShouldQuery(Integer[] ids, String indexField) {
        BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
        for (Integer cityId : ids) {
            Query query = IntPoint.newExactQuery(indexField, cityId);
            queryBuilder.add(query, BooleanClause.Occur.SHOULD);
        }
        return queryBuilder.build();
    }

    /**
     * 搜索简历
     *
     * @param searchForm
     * @return
     */
    public PageResult<ResumeDetail> searchResume(ResumeListForm searchForm, String searchPath) {
        try {
            IndexSearcher indexSearcher = luceneContext.getIndexSearcher(searchPath);
            Query resumeQuery = getResumeQuery(searchForm);
            //分页操作
            ScoreDoc lastScoreDoc = getLastScoreDoc(searchForm, resumeQuery, indexSearcher);
            TopDocs topDocs = indexSearcher.searchAfter(lastScoreDoc, resumeQuery, searchForm.getLimit());
            PageResult<ResumeDetail> pageResult = new PageResult<>();
            pageResult.setTotal(topDocs.totalHits);
            pageResult.setCurrentPage(searchForm.getPage());
            pageResult.setPageSize(searchForm.getLimit());

            com.worldelite.job.service.resume.ResumeService resumeService = ResumeServiceFactory.getResumeService(searchForm.getType());
            List<ResumeDetail> resumeList = new ArrayList<>();
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                Document hitDoc = indexSearcher.doc(scoreDoc.doc);
                Long resumeId = NumberUtils.toLong(hitDoc.get(ResumeIndexFields.RESUME_ID));
                ResumeDetail resumeDetail = resumeService.getResumeDetail(resumeId);
                resumeList.add(resumeDetail);
            }

            pageResult.setList(resumeList);
            pageResult.calPageCountAndHasMore(resumeList);
            return pageResult;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            ex.printStackTrace();
            throw new ServiceException(ex.getMessage());
            //return PageResult.emptyResult();
        }
    }

    /**
     * 构造简历搜索过滤器
     *
     * @param searchForm
     * @return
     */
    public Query getResumeQuery(ResumeListForm searchForm) {
        BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
        //如果没有任何过滤条件，则返回所有数据
        Query allQuery = new WildcardQuery(new Term(ResumeIndexFields.RESUME_ID_INDEX, "*"));
        queryBuilder.add(allQuery, BooleanClause.Occur.MUST);
        //简历类型
        if (searchForm.getType() != null) {
            Query typeQuery = IntPoint.newExactQuery(ResumeIndexFields.RESUME_TYPE, searchForm.getType());
            queryBuilder.add(typeQuery, BooleanClause.Occur.MUST);
        }
        //简历状态
        if (searchForm.getStatus() != null) {
            Query statusQuery = IntPoint.newExactQuery(ResumeIndexFields.RESUME_STATUS, searchForm.getStatus());
            queryBuilder.add(statusQuery, BooleanClause.Occur.MUST);
        }
        //学校
        if (ArrayUtils.isNotEmpty(searchForm.getSchoolIds())) {
            BooleanQuery.Builder schoolBuilder = new BooleanQuery.Builder();
            for (Integer id : searchForm.getSchoolIds()) {
                schoolBuilder.add(IntPoint.newExactQuery(ResumeIndexFields.SCHOOL_ID, id), BooleanClause.Occur.SHOULD);
            }
            queryBuilder.add(schoolBuilder.build(), BooleanClause.Occur.MUST);
        }
        //学历
        if (ArrayUtils.isNotEmpty(searchForm.getDegreeIds())) {
            BooleanQuery.Builder degreeBuilder = new BooleanQuery.Builder();
            for (Integer id : searchForm.getDegreeIds()) {
                degreeBuilder.add(IntPoint.newExactQuery(ResumeIndexFields.DEGREE, id), BooleanClause.Occur.SHOULD);
            }
            queryBuilder.add(degreeBuilder.build(), BooleanClause.Occur.MUST);
        }
        //GPA
        if (searchForm.getGpaRangeId() != null) {
            try {
                final Dict dict = dictService.getDict(searchForm.getGpaRangeId());
                final String[] value = dict.getValue().split("-");
                if (StringUtils.isNoneEmpty(value) && value.length == 2) {
                    float minGpa = Float.parseFloat(value[0]);
                    float maxGpa = Float.parseFloat(value[1]);

                    queryBuilder.add(FloatPoint.newRangeQuery(ResumeIndexFields.GPA, minGpa, maxGpa), BooleanClause.Occur.MUST);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        //UserId索引
        if (searchForm.getUserId() != null) {
            Query userIdQuery = LongPoint.newExactQuery(ResumeIndexFields.USER_ID, searchForm.getUserId());
            queryBuilder.add(userIdQuery, BooleanClause.Occur.MUST);
        }
        //CompanyId索引
        if (searchForm.getCompanyId() != null) {
            Query companyIdQuery = LongPoint.newExactQuery(ResumeIndexFields.COMPANY_ID, searchForm.getCompanyId());
            queryBuilder.add(companyIdQuery, BooleanClause.Occur.MUST);
        }
        //名字索引
        if (StringUtils.isNotEmpty(searchForm.getName())) {
            WildcardQuery nameQuery = new WildcardQuery(new Term(ResumeIndexFields.NAME, "*" + searchForm.getName() + "*"));
            queryBuilder.add(nameQuery, BooleanClause.Occur.MUST);
        }
        //邮箱索引
        if (StringUtils.isNotEmpty(searchForm.getEmail())) {
            WildcardQuery emailQuery = new WildcardQuery(new Term(ResumeIndexFields.EMAIL, "*" + searchForm.getEmail() + "*"));
            queryBuilder.add(emailQuery, BooleanClause.Occur.MUST);
        }
        //意向城市
        if (ArrayUtils.isNotEmpty(searchForm.getCityIds())) {
            BooleanQuery.Builder cityBuilder = new BooleanQuery.Builder();
            for (Integer id : searchForm.getCityIds()) {
                cityBuilder.add(IntPoint.newExactQuery(ResumeIndexFields.EXPECT_CITY, id), BooleanClause.Occur.SHOULD);
            }
            queryBuilder.add(cityBuilder.build(), BooleanClause.Occur.MUST);
        }
        //意向职位
        if (ArrayUtils.isNotEmpty(searchForm.getCategoryIds())) {
            BooleanQuery.Builder categoryBuilder = new BooleanQuery.Builder();
            for (Integer id : searchForm.getCategoryIds()) {
                categoryBuilder.add(IntPoint.newExactQuery(ResumeIndexFields.EXPECT_CATEGORY, id), BooleanClause.Occur.SHOULD);
            }
            queryBuilder.add(categoryBuilder.build(), BooleanClause.Occur.MUST);
        }
        //薪资范围
        if (searchForm.getSalaryId() != null) {
            Query salaryQuery = IntPoint.newExactQuery(ResumeIndexFields.SALARY, searchForm.getSalaryId());
            queryBuilder.add(salaryQuery, BooleanClause.Occur.MUST);
        }
        //性别索引
        if (searchForm.getGender() != null) {
            Query genderQuery = IntPoint.newExactQuery(ResumeIndexFields.GENDER, searchForm.getGender());
            queryBuilder.add(genderQuery, BooleanClause.Occur.MUST);
        }
        //技能索引
        if (ArrayUtils.isNotEmpty(searchForm.getSkills())) {
            BooleanQuery.Builder skillBuilder = new BooleanQuery.Builder();
            for (String skill : searchForm.getSkills()) {
                WildcardQuery skillQuery = new WildcardQuery(new Term(ResumeIndexFields.RESUME_SKILL, "*" + skill + "*"));
                skillBuilder.add(skillQuery, BooleanClause.Occur.SHOULD);
            }
            queryBuilder.add(skillBuilder.build(), BooleanClause.Occur.MUST);
        }
        return queryBuilder.build();
    }

    private PageResult<JobVo> searchJobByQuery(Query query, PageForm pageForm) {
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

    private PageResult<ResumeVo> searchResumeByQuery(Query query, PageForm pageForm) {
        IndexSearcher indexSearcher = getIndexSearcher();
        try {
            ScoreDoc lastScoreDoc = getLastScoreDoc(pageForm, query, indexSearcher);
            TopDocs topDocs = indexSearcher.searchAfter(lastScoreDoc, query, pageForm.getLimit());
            PageResult<ResumeVo> pageResult = new PageResult<>();
            pageResult.setTotal(topDocs.totalHits);
            pageResult.setCurrentPage(pageForm.getPage());
            pageResult.setPageSize(pageForm.getLimit());
            List<ResumeVo> resumeVoList = new ArrayList<>();

            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                Document hitDoc = indexSearcher.doc(scoreDoc.doc);
                Long resumeId = NumberUtils.toLong(hitDoc.get(ResumeIndexFields.RESUME_ID));
                ResumeService resumeService = ResumeServiceFactory.getResumeService(resumeId);
                ResumeDetail resumeDetail = resumeService.getResumeDetail(resumeId);
                ResumeVo resumeVo = resumeService.toResumeVo(resumeDetail);
                resumeVoList.add(resumeVo);
            }

            pageResult.setList(resumeVoList);
            pageResult.calPageCountAndHasMore(resumeVoList);
            return pageResult;
        } catch (IOException ex) {
            log.error("searchResumeByQuery error, query: " + query.toString() + ", page: " + pageForm.toString(), ex);
            return PageResult.emptyResult();
        }
    }

    private ScoreDoc getLastScoreDoc(PageForm pagerForm, Query query, IndexSearcher indexSearcher) throws IOException {
        if (pagerForm.getPage() == 1) {
            return null;//如果是第一页返回空
        }
        int num = pagerForm.getLimit() * (pagerForm.getPage() - 1);
        TopDocs tds = indexSearcher.search(query, num);
        if (tds.scoreDocs.length == 0) {
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

    private IndexSearcher getIndexSearcher(String path) {
        try {
            if (sIndexReader == null) {
                synchronized (this) {
                    if (sIndexReader == null) {
                        sIndexReader = DirectoryReader.open(FSDirectory.open(Paths.get(path)));
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

    /**
     * 获取当前索引文件重建目录
     *
     * @return
     */
    private String getBuildFolder() {
        return switchFolder ? resumeAttachmentPath1 : resumeAttachmentPath2;
    }

    /**
     * 获取当前索引文件搜索目录
     *
     * @return
     */
    private String getSearchFolder() {
        return switchFolder ? resumeAttachmentPath2 : resumeAttachmentPath1;
    }
}
