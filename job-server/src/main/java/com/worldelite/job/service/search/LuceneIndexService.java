package com.worldelite.job.service.search;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.worldelite.job.constants.JobIndexFields;
import com.worldelite.job.constants.JobStatus;
import com.worldelite.job.constants.ResumeIndexFields;
import com.worldelite.job.context.LuceneContext;
import com.worldelite.job.entity.*;
import com.worldelite.job.mapper.JobMapper;
import com.worldelite.job.mapper.ResumeMapper;
import com.worldelite.job.service.JobCategoryService;
import com.worldelite.job.service.JobService;
import com.worldelite.job.service.UserExpectJobService;
import com.worldelite.job.service.resume.ResumeService;
import com.worldelite.job.service.resume.ResumeServiceFactory;
import com.worldelite.job.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
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

    @Value("${search.index.resumeindex2}")
    private String folder;

    @Autowired
    private Analyzer analyzer;

    @Autowired
    private JobService jobService;

    @Autowired
    private ResumeMapper resumeMapper;

    @Autowired
    private JobMapper jobMapper;

    @Autowired
    private JobCategoryService jobCategoryService;

    @Autowired
    private UserExpectJobService userExpectJobService;

    @Autowired
    private LuceneContext luceneContext;

    @Override
    public void createOrRefresh() {
        IndexWriter indexWriter = null;
        try {
            indexWriter = createIndexWriter(IndexWriterConfig.OpenMode.CREATE);
            createOrRefreshJobIndex(indexWriter);
            createOrRefreshResumeIndex(indexWriter);
            indexWriter.commit();
        } catch (IOException e) {
            log.error("createOrRefresh error", e);
        } finally {
            IOUtils.closeQuietly(indexWriter);
        }
    }

    private void createOrRefreshJobIndex(IndexWriter indexWriter) throws IOException {
        List<Job> jobList;
        int curPage = 1;
        JobOptions options = new JobOptions();
        options.setStatus(JobStatus.PUBLISH.value);
        do {
            PageHelper.startPage(curPage++, 100, false);
            jobList = jobMapper.selectAndList(options);
            for (Job job : jobList) {
                Document doc = createJobDoc(job.getId());
                indexWriter.addDocument(doc);
            }
        } while (CollectionUtils.isNotEmpty(jobList));
    }

    private void createOrRefreshResumeIndex(IndexWriter indexWriter) throws IOException {
        List<Resume> resumeList;
        int curPage = 1;
        ResumeOptions options = new ResumeOptions();
        do {
            PageHelper.startPage(curPage++, 100, false);
            resumeList = resumeMapper.selectAndList(options);
            for (Resume resume : resumeList) {
                Document doc = createResumeDoc(resume.getId());
                if (doc != null) {
                    indexWriter.addDocument(doc);
                }
            }
        } while (CollectionUtils.isNotEmpty(resumeList));
    }

    @Override
    public Document saveJobItem(Long jobId) {
        IndexWriter indexWriter = null;
        Document document = null;
        try {
            indexWriter = createIndexWriter(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            //先删除，再添加
            indexWriter.deleteDocuments(LongPoint.newExactQuery(JobIndexFields.JOB_ID_INDEX, jobId));
            document = createJobDoc(jobId);
            indexWriter.addDocument(document);
            indexWriter.commit();
        } catch (IOException e) {
            log.error("saveJobItem error ", e);
        } finally {
            IOUtils.closeQuietly(indexWriter);
        }
        return document;
    }

    @Override
    public Document saveJobItem(Document document) {
        Long jobId = NumberUtils.toLong(document.get(JobIndexFields.JOB_ID));
        return saveJobItem(jobId);
    }

    @Override
    public Document deleteJobItem(Long jobId) {
        IndexWriter indexWriter = null;
        Document document = null;
        try {
            document = new Document();
            document.add(new StoredField(JobIndexFields.JOB_ID, jobId));
            indexWriter = createIndexWriter(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            indexWriter.deleteDocuments(LongPoint.newExactQuery(JobIndexFields.JOB_ID_INDEX, jobId));
            indexWriter.commit();
        } catch (IOException e) {
            log.error("deleteJobItem error ", e);
        } finally {
            IOUtils.closeQuietly(indexWriter);
        }
        return document;
    }

    @Override
    public Document deleteJobItem(Document document) {
        Long jobId = NumberUtils.toLong(document.get(JobIndexFields.JOB_ID));
        return deleteJobItem(jobId);
    }

    private Document createJobDoc(Long jobId) {
        JobVo jobVo = jobService.getJobDetail(jobId);
        final Document doc = new Document();
        doc.add(new StoredField(JobIndexFields.JOB_ID, jobId));
        doc.add(new LongPoint(JobIndexFields.JOB_ID_INDEX, jobId));
        StringBuilder keyWordBuilder = new StringBuilder();
        keyWordBuilder.append(jobVo.getName());
        if (jobVo.getCategory() != null) {
            doc.add(new IntPoint(JobIndexFields.CATEGORY_THIRD_INDEX, jobVo.getCategory().getId()));
            keyWordBuilder.append(jobVo.getCategory().getName());
            JobCategoryVo secondCategory = jobCategoryService.getById(jobVo.getCategory().getParentId());
            if (secondCategory != null) {
                doc.add(new IntPoint(JobIndexFields.CATEGORY_SECOND_INDEX, secondCategory.getId()));
                keyWordBuilder.append(secondCategory.getName());
                JobCategoryVo firstCategory = jobCategoryService.getById(secondCategory.getParentId());
                if (firstCategory != null) {
                    doc.add(new IntPoint(JobIndexFields.CATEGORY_FIRST_INDEX, firstCategory.getId()));
                    keyWordBuilder.append(firstCategory.getName());
                }
            }
        }
        if (jobVo.getCity() != null) {
            doc.add(new IntPoint(JobIndexFields.CITY_INDEX, jobVo.getCity().getId()));
            doc.add(new IntPoint(JobIndexFields.CITY_PARENT_INDEX, jobVo.getCity().getParentId()));
            keyWordBuilder.append(jobVo.getCity().getName());
        }

        if (jobVo.getLanguage() != null) {
            doc.add(new IntPoint(JobIndexFields.LAN_REQUIRED_INDEX, jobVo.getLanguage().getId()));
            keyWordBuilder.append(jobVo.getLanguage().getName());
        }

        if (jobVo.getAdditions() != null && jobVo.getAdditions().size() > 0) {
            StringBuilder sb = new StringBuilder();
            jobVo.getAdditions().forEach(dictVo -> sb.append(dictVo.getId()).append(StrUtil.COLON));
            doc.add(new StringField(JobIndexFields.ADDITIONS_INDEX, sb.toString(), Field.Store.YES));
        }

        if (jobVo.getTime() != null) {
            doc.add(new LongPoint(JobIndexFields.JOB_PUBLISH_TIME_INDEX, jobVo.getTime().getTime()));
            doc.add(new NumericDocValuesField(JobIndexFields.JOB_PUBLISH_TIME_INDEX, jobVo.getTime().getTime()));
        }

        if (jobVo.getExperience() != null) {
            doc.add(new IntPoint(JobIndexFields.EXPERIENCE_INDEX, jobVo.getExperience().getId()));
        }

        if (jobVo.getCompany() != null) {
            CompanyVo companyVo = jobVo.getCompany();
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
                if (companyVo.getProperty() != null) {
                    doc.add(new IntPoint(JobIndexFields.COMPANY_DEFINE_INDEX, jobVo.getCompany().getProperty().getId()));
                }
            }
        }
        if (jobVo.getJobType() != null) {
            doc.add(new IntPoint(JobIndexFields.JOB_TYPE_INDEX, jobVo.getJobType().getId()));
        }
        if (jobVo.getMinDegree() != null) {
            doc.add(new IntPoint(JobIndexFields.MIN_DEGREE_INDEX, jobVo.getMinDegree().getId()));
        }
        if (jobVo.getRecruitType() != null) {
            doc.add(new IntPoint(JobIndexFields.RECRUIT_TYPE_INDEX, jobVo.getRecruitType()));
        }
        if (jobVo.getMinSalary() != null && jobVo.getMaxSalary() != null) {
            final int totalAverageSalary = getTotalAverageSalary(jobVo);
            doc.add(new IntPoint(JobIndexFields.AVER_SALARY_INDEX, totalAverageSalary));
            doc.add(new NumericDocValuesField(JobIndexFields.AVER_SALARY_INDEX, totalAverageSalary));
        }
        //防止空指针,不想一级一级判空了
        try {
            if (jobVo.getCompany().getId() != null) {
                doc.add(new LongPoint(JobIndexFields.COMPANY_ID, Long.parseLong(jobVo.getCompany().getId())));
            }
        } catch (Exception ignored) {
        }

        doc.add(new TextField(JobIndexFields.KEYWORD_INDEX, keyWordBuilder.toString(), Field.Store.NO));
        return doc;
    }

    @Override
    public void saveResumeItem(Long resumeId) {
        IndexWriter indexWriter = null;
        try {
            indexWriter = createIndexWriter(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            //先删除，再添加
            indexWriter.deleteDocuments(LongPoint.newExactQuery(ResumeIndexFields.RESUME_ID_INDEX, resumeId));
            Document doc = createResumeDoc(resumeId);
            if (doc != null) {
                indexWriter.addDocument(doc);
            }
            indexWriter.commit();
        } catch (IOException e) {
            log.error("saveResumeItem error ", e);
        } finally {
            IOUtils.closeQuietly(indexWriter);
        }
    }

    public Document saveResumeItem(ResumeDetail resumeDetail, String indexPath) {
        Document doc = createResumeDoc(resumeDetail);
        try {
            IndexWriter indexWriter = luceneContext.getIndexWriter(indexPath);
            if (doc != null) {
                Term term = new Term(ResumeIndexFields.RESUME_ID_INDEX, String.valueOf(resumeDetail.getResumeId()));
                indexWriter.updateDocument(term, doc);
                indexWriter.commit();
            }
        } catch (IOException e) {
            log.error("saveResumeItem error ", e);
        } finally {
            luceneContext.closeIndexWriter(indexPath);
        }
        return doc;
    }

    @Override
    public void deleteResumeItem(Long resumeId) {
        IndexWriter indexWriter = null;
        try {
            indexWriter = createIndexWriter(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            indexWriter.deleteDocuments(LongPoint.newExactQuery(ResumeIndexFields.RESUME_ID_INDEX, resumeId));
            indexWriter.commit();
        } catch (IOException e) {
            log.error("deleteResumeItem error ", e);
        } finally {
            IOUtils.closeQuietly(indexWriter);
        }
    }

    /**
     * 生成简历Document
     *
     * @param resume
     * @return
     */
    private Document createResumeDoc(ResumeDetail resume) {
        //必须有简历基础信息才开始生成索引
        if (resume == null || resume.getResumeId() == null || resume.getResumeBasic() == null) return null;
        if (StringUtils.isAnyBlank(resume.getName(), resume.getResumeBasic().getName())) return null;
        if (StringUtils.isAllBlank(resume.getEmail())) return null;

        Document document = new Document();
        Resume resumeBasic = resume.getResumeBasic();
        //保存简历ID的值，用于搜索时返回
        document.add(new StoredField(ResumeIndexFields.RESUME_ID, resume.getResumeId()));
        //生成简历ID的索引，不会保存值
        document.add(new StringField(ResumeIndexFields.RESUME_ID_INDEX, String.valueOf(resume.getResumeId()), Field.Store.NO));
        //简历类型
        if (resumeBasic.getType() != null) {
            document.add(new IntPoint(ResumeIndexFields.RESUME_TYPE, resumeBasic.getType()));
        }
        //简历状态
        if (resumeBasic.getStatus() != null) {
            document.add(new IntPoint(ResumeIndexFields.RESUME_STATUS, resumeBasic.getStatus()));
        }
        //为教育信息生成索引
        if (CollectionUtils.isNotEmpty(resume.getResumeEduList())) {
            for (ResumeEdu resumeEdu : resume.getResumeEduList()) {
                //学校
                if (resumeEdu.getSchoolId() != null)
                    document.add(new IntPoint(ResumeIndexFields.SCHOOL_ID, resumeEdu.getSchoolId()));
                //学历
                if (resumeEdu.getDegreeId() != null)
                    document.add(new IntPoint(ResumeIndexFields.DEGREE, resumeEdu.getDegreeId()));
                //GPA
                if (resumeEdu.getGpa() != null) {
                    document.add(new FloatPoint(ResumeIndexFields.GPA, resumeEdu.getGpa().floatValue()));
                }
            }
        }
        //UserId索引
        if (resume.getUserId() != null) {
            document.add(new LongPoint(ResumeIndexFields.USER_ID, resume.getUserId()));
        }
        //CompanyId索引
        if (resume.getCompanyId() != null) {
            document.add(new LongPoint(ResumeIndexFields.COMPANY_ID, resume.getCompanyId()));
        }
        //名字索引
        if (resume.getName() != null) {
            document.add(new StringField(ResumeIndexFields.NAME, resume.getName(), Field.Store.NO));
        }
        //邮箱索引
        if (resume.getEmail() != null) {
            document.add(new StringField(ResumeIndexFields.EMAIL, resume.getEmail(), Field.Store.NO));
        }
        //意向城市
        addExpectCity(document, resume.getCityList());
        //意向职位
        addExpectCategory(document, resume.getCategoryList());
        //薪资范围
        if (resume.getSalary() != null) {
            document.add(new IntPoint(ResumeIndexFields.SALARY, resume.getSalary().getId()));
        }
        //性别索引
        if (resume.getGender() != null) {
            document.add(new IntPoint(ResumeIndexFields.RESUME_ID_INDEX, resume.getGender()));
        }
        //技能索引
        addResumeSkill(document, resume.getResumeSkillList());
        return document;
    }

    private Document createResumeDoc(Long resumeId) {
        ResumeService resumeService = ResumeServiceFactory.getResumeService(resumeId);
        ResumeDetail resumeDetail = resumeService.getResumeDetail(resumeId);
        ResumeVo resumeVo = resumeService.toResumeVo(resumeDetail);
        if (checkIfResumeNotComplete(resumeVo)) return null;

        final Document doc = new Document();
        doc.add(new StoredField(ResumeIndexFields.RESUME_ID, resumeId));
        doc.add(new LongPoint(ResumeIndexFields.RESUME_ID_INDEX, resumeId));
        UserExpectJobVo expectJobVo = null;

        if (expectJobVo != null) {
            if (CollectionUtils.isNotEmpty(expectJobVo.getCityList())) {
                int index = 1;
                for (CityVo city : expectJobVo.getCityList()) {
                    if (index == 1) {
                        doc.add(new IntPoint(ResumeIndexFields.EXPECT_JOB_FIRST_CITY, city.getId()));
                    }
                    if (index == 2) {
                        doc.add(new IntPoint(ResumeIndexFields.EXPECT_JOB_SECOND_CITY, city.getId()));
                    }
                    if (index == 3) {
                        doc.add(new IntPoint(ResumeIndexFields.EXPECT_JOB_THIRD_CITY, city.getId()));
                    }
                    index++;
                }
            }

            if (CollectionUtils.isNotEmpty(expectJobVo.getCategoryList())) {
                int index = 1;
                for (JobCategoryVo categoryVo : expectJobVo.getCategoryList()) {
                    if (index == 1) {
                        doc.add(new IntPoint(ResumeIndexFields.EXPECT_JOB_FIRST_CATEGORY, categoryVo.getId()));
                    }
                    if (index == 2) {
                        doc.add(new IntPoint(ResumeIndexFields.EXPECT_JOB_SECOND_CATEGORY, categoryVo.getId()));
                    }
                    if (index == 3) {
                        doc.add(new IntPoint(ResumeIndexFields.EXPECT_JOB_THIRD_CATEGORY, categoryVo.getId()));
                    }
                    index++;
                }
            }

        }
        return doc;
    }

    private IndexWriter createIndexWriter(IndexWriterConfig.OpenMode openMode) throws IOException {
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(openMode);
        return new IndexWriter(FSDirectory.open(Paths.get(searchIndexPath)), config);
    }

    private Boolean checkIfResumeNotComplete(ResumeVo resumeVo) {
        return StringUtils.isEmpty(resumeVo.getName())
                || resumeVo.getAge() == null
                || resumeVo.getBirth() == null
                || resumeVo.getGender() == null
                || StringUtils.isEmpty(resumeVo.getAvatar())
                || StringUtils.isEmpty(resumeVo.getIntroduction())
                || CollectionUtils.isEmpty(resumeVo.getResumeExpList())
                || CollectionUtils.isEmpty(resumeVo.getResumePracticeList())
                || CollectionUtils.isEmpty(resumeVo.getResumeSkillList());
    }

    /**
     * 添加技能标签索引
     * 技能标签可以用户自定义
     * 所以使用标签内容做索引，使用ID做索引没有意义
     * 搜索需要使用模糊搜索
     *
     * @param document
     * @param resumeSkillList
     */
    private void addResumeSkill(Document document, List<ResumeSkill> resumeSkillList) {
        if (CollectionUtils.isEmpty(resumeSkillList)) return;
        for (ResumeSkill resumeSkill : resumeSkillList) {
            if (StringUtils.isNotEmpty(resumeSkill.getName())) {
                document.add(new StringField(ResumeIndexFields.RESUME_SKILL, resumeSkill.getName(), Field.Store.NO));
            }
        }
    }

    /**
     * 添加意向职位索引
     *
     * @param document
     * @param categoryList
     */
    private void addExpectCategory(Document document, List<JobCategory> categoryList) {
        if (CollectionUtils.isEmpty(categoryList)) return;
        for (JobCategory category : categoryList) {
            //理论上职位ID不可能为NULL
            //这里是防止测试库数据异常
            if (category.getId() != null) {
                document.add(new IntPoint(ResumeIndexFields.EXPECT_CATEGORY, category.getId()));
            }
        }
    }

    /**
     * 添加意向城市索引
     *
     * @param document
     * @param cityList
     */
    private void addExpectCity(Document document, List<City> cityList) {
        if (CollectionUtils.isEmpty(cityList)) return;
        for (City city : cityList) {
            if (city.getId() != null) {
                document.add(new IntPoint(ResumeIndexFields.EXPECT_CITY, city.getId()));
            }
        }
    }

    private int getTotalAverageSalary(JobVo jobVo) {

        //职位类型全职,实习,兼职
        //全职时salaryMonths有效, 实习,兼职按照12个月
        if (jobVo.getJobType() != null) {
            int jobType = jobVo.getJobType().getId();

            //处理最大薪资
            int maxSalary, minSalary;
            if (jobVo.getMaxSalary() == null || jobVo.getMaxSalary() <= 0)
                maxSalary = 0;
            else
                maxSalary = jobVo.getMaxSalary();

            if (jobVo.getMinSalary() == null || jobVo.getMinSalary() <= 0)
                minSalary = 0;
            else
                minSalary = jobVo.getMinSalary();


            int totalAverageSalary;
            if (jobType == 108 || jobType == 1003) {
                //实习,兼职按天计算工资需要特殊处理, 每月按照20天算,得到年总天数*平均日工资
                totalAverageSalary = 20 * 12 * ((minSalary + maxSalary) / 2);
            } else {
                //处理月
                int salaryMonths;
                if (jobVo.getSalaryMonths() == null || jobVo.getSalaryMonths() <= 0)
                    salaryMonths = 12;
                else
                    salaryMonths = jobVo.getSalaryMonths();
                //全职计算平均年薪
                totalAverageSalary = salaryMonths * ((minSalary + maxSalary) / 2) * 1000;
            }

            return totalAverageSalary;
        }

        return 0;
    }
}