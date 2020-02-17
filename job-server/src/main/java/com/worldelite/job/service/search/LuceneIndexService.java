package com.worldelite.job.service.search;

import com.github.pagehelper.PageHelper;
import com.worldelite.job.constants.JobIndexFields;
import com.worldelite.job.constants.JobStatus;
import com.worldelite.job.entity.Job;
import com.worldelite.job.mapper.JobMapper;
import com.worldelite.job.service.CompanyService;
import com.worldelite.job.service.JobCategoryService;
import com.worldelite.job.service.JobService;
import com.worldelite.job.vo.CompanyVo;
import com.worldelite.job.vo.JobCategoryVo;
import com.worldelite.job.vo.JobVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

/**
 * lucene 索引实现
 *
 * @author yeguozhong yedaxia.github.com
 */
@Slf4j
@Service
public class LuceneIndexService implements IndexService {

    @Value("${search.index.path}")
    private String searchIndexPath;

    @Autowired
    private Analyzer analyzer;

    @Autowired
    private JobService jobService;

    @Autowired
    private JobMapper jobMapper;

    @Autowired
    private JobCategoryService jobCategoryService;

    @Override
    public void createOrRefresh() {
        IndexWriter indexWriter = null;
        try {
            indexWriter = createIndexWriter(IndexWriterConfig.OpenMode.CREATE);
            List<Job> jobList;
            int curPage = 1;
            Job options = new Job();
            options.setStatus(JobStatus.PUBLISH.value);
            do {
                PageHelper.startPage(curPage++, 100, false);
                jobList = jobMapper.selectAndList(options);
                for (Job job : jobList) {
                    Document doc = createJobDoc(job.getId());
                    indexWriter.addDocument(doc);
                }
            } while (CollectionUtils.isNotEmpty(jobList));
            indexWriter.commit();
        } catch (IOException e) {
            log.error("createOrRefresh error", e);
        } finally {
            IOUtils.closeQuietly(indexWriter);
        }
    }

    @Override
    public void saveJobItem(Long jobId) {
        IndexWriter indexWriter = null;
        try {
            indexWriter = createIndexWriter(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            //先删除，再添加
            indexWriter.deleteDocuments(LongPoint.newExactQuery(JobIndexFields.JOB_ID, jobId));
            indexWriter.addDocument(createJobDoc(jobId));
            indexWriter.commit();
        } catch (IOException e) {
            log.error("saveJobItem error ", e);
        } finally {
            IOUtils.closeQuietly(indexWriter);
        }
    }

    @Override
    public void deleteJobItem(Long jobId) {
        IndexWriter indexWriter = null;
        try {
            indexWriter = createIndexWriter(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            indexWriter.deleteDocuments(LongPoint.newExactQuery(JobIndexFields.JOB_ID, jobId));
            indexWriter.commit();
        } catch (IOException e) {
            log.error("deleteJobItem error ", e);
        } finally {
            IOUtils.closeQuietly(indexWriter);
        }
    }

    private Document createJobDoc(Long jobId) {
        JobVo jobVo = jobService.getJobDetail(jobId);
        final Document doc = new Document();
        doc.add(new StoredField(JobIndexFields.JOB_ID, jobId));
        StringBuilder keyWordBuilder = new StringBuilder();
        keyWordBuilder.append(jobVo.getName());
        if (jobVo.getCategory() != null) {
            doc.add(new IntPoint(JobIndexFields.CATEGORY_THIRD_INDEX, jobVo.getCategory().getId()));
            keyWordBuilder.append(jobVo.getCategory().getName());
            JobCategoryVo secondCategory = jobCategoryService.getById(jobVo.getCategory().getParentId());
            if(secondCategory != null){
                doc.add(new IntPoint(JobIndexFields.CATEGORY_SECOND_INDEX, secondCategory.getId()));
                keyWordBuilder.append(secondCategory.getName());
                JobCategoryVo firstCategory = jobCategoryService.getById(secondCategory.getParentId());
                if(firstCategory != null){
                    doc.add(new IntPoint(JobIndexFields.CATEGORY_FIRST_INDEX, firstCategory.getId()));
                    keyWordBuilder.append(firstCategory.getName());
                }
            }
        }
        if (jobVo.getCity() != null) {
            doc.add(new IntPoint(JobIndexFields.CITY_INDEX, jobVo.getCity().getId()));
            keyWordBuilder.append(jobVo.getCity().getName());
        }
        if (jobVo.getCompanyUser() != null) {
            CompanyVo companyVo = jobVo.getCompanyUser().getCompany();
            if (companyVo != null) {
                keyWordBuilder.append(companyVo.getName());
                if (companyVo.getScale() != null) {
                    doc.add(new IntPoint(JobIndexFields.COMPANY_SCALE_INDEX, companyVo.getScale().getId()));
                }
                if (companyVo.getStage() != null) {
                    doc.add(new IntPoint(JobIndexFields.COMPANY_STAGE_INDEX, companyVo.getStage().getId()));
                }
                if (companyVo.getIndustry() != null) {
                    doc.add(new IntPoint(JobIndexFields.COMPANY_INDUSTRY_INDEX, companyVo.getIndustry().getId()));
                    keyWordBuilder.append(companyVo.getIndustry().getName());
                }
            }
        }
        if (jobVo.getJobType() != null) {
            doc.add(new IntPoint(JobIndexFields.JOB_TYPE_INDEX, jobVo.getJobType().getId()));
        }
        if (jobVo.getMinDegree() != null) {
            doc.add(new IntPoint(JobIndexFields.MIN_DEGREE_INDEX, jobVo.getMinDegree().getId()));
        }
        if(jobVo.getMinSalary() != null && jobVo.getMaxSalary() != null){
            doc.add(new IntPoint(JobIndexFields.AVER_SALARY_INDEX, (jobVo.getMinSalary() + jobVo.getMaxSalary()) / 2));
        }

        doc.add(new TextField(JobIndexFields.KEYWORD_INDEX, keyWordBuilder.toString(), Field.Store.NO));
        return doc;
    }

    private IndexWriter createIndexWriter(IndexWriterConfig.OpenMode openMode) throws IOException {
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(openMode);
        return new IndexWriter(FSDirectory.open(Paths.get(searchIndexPath)), config);
    }
}
