package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.internal.OSSCallbackErrorResponseHandler;
import com.aliyun.oss.internal.OSSUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.worldelite.job.constants.JobApplyStatus;
import com.worldelite.job.constants.ResumeAttachmentIndexFields;
import com.worldelite.job.context.LuceneContext;
import com.worldelite.job.entity.*;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.*;
import com.worldelite.job.mapper.*;
import com.worldelite.job.service.sdk.ResumeSDK;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.util.FormUtils;
import com.worldelite.job.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.lionsoul.jcseg.ISegment;
import org.lionsoul.jcseg.IWord;
import org.lionsoul.jcseg.analyzer.JcsegAnalyzer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Slf4j
@Service
public class ResumeRepositoryService extends BaseService{


    @Value("${search.index.resumeindex2}")
    private String indexFolder2;

    @Autowired
    private LuceneContext luceneContext;

    @Autowired
    private ResumeSDK resumeSDK;

    @Autowired
    private ResumeRepositoryMapper resumeRepositoryMapper;

    @Autowired
    private CompanyUserMapper companyUserMapper;

    @Autowired
    private ResumeMapper resumeMapper;

    @Autowired
    private UserApplicantMapper userApplicantMapper;

    @Autowired
    private ResumeEduMapper resumeEduMapper;

    @Autowired
    private SchoolMapper schoolMapper;

    @Autowired
    private ResumeExperienceMapper resumeExpMapper;

    @Autowired
    private DictService dictService;

    @Autowired
    private ResumeEduService resumeEduService;

    @Autowired
    private ResumeExpService resumeExpService;

    @Autowired
    private ResumePracticeService resumePracticeService;

    @Autowired
    private ResumeSkillService resumeSkillService;

    @Autowired
    private ResumeLinkService resumeLinkService;

    @Autowired
    private UserExpectJobService userExpectJobService;

    public void saveAttachment(String attachmentName) {
        //调用ResumeSDK解析简历文件
        AttachmentParser attachmentParser = resumeSDK.parse(attachmentName);
        JSONObject result = JSONObject.parseObject(attachmentParser.getAttachContent());

        //解析结果生成ResumeRepository实例并存入数据库
        //根据邮箱来判断，如果已经存在则更新
        String email = attachmentParser.getEmail();
        Long companyId = getCompanyId();
        ResumeRepository resumeRepository = new ResumeRepository();
        resumeRepository.setEmail(email);
        resumeRepository.setCompanyId(companyId);
//        List<ResumeRepository> resumeRepositoryList = resumeRepositoryMapper.selectByEmail(resumeRepository);
//        if(CollectionUtils.isEmpty(resumeRepositoryList)){
            Resume resume = resumeSDK.getResume(result);
            resume.setId(AppUtils.nextId());
            resume.setUserId(curUser().getId());
            resume.setAttachResume(AppUtils.getOssKey(attachmentName));
            resumeMapper.insertSelective(resume);
            resumeRepository = getResumeRepository(resumeRepository,result);
            resumeRepository.setCompanyId(companyId);
            resumeRepository.setUserId(curUser().getId());
            resumeRepository.setResumeId(resume.getId());
            resumeRepository.setParserId(attachmentParser.getId());
            resumeRepository.setAttachResume(AppUtils.getOssKey(attachmentName));
            resumeRepositoryMapper.insertSelective(resumeRepository);
//        }else{
//            resumeRepository = resumeRepositoryList.get(0);
//            Resume resume = resumeSDK.getResume(result);
//            resume.setId(resumeRepository.getResumeId());
//            resume.setUserId(curUser().getId());
//            resumeMapper.updateByPrimaryKeySelective(resume);
//            resumeRepository = getResumeRepository(resumeRepository,result);
//            resumeRepository.setCompanyId(companyId);
//            resumeRepository.setParserId(attachmentParser.getId());
//            resumeRepositoryMapper.updateByPrimaryKeySelective(resumeRepository);
//        }

        //生成索引文件
        //Document document = getDocument(resumeRepository);
        //addIndex(document);
    }

    public void addAttachment(Long id, String attachmentName) {
        ResumeRepository resumeRepository = resumeRepositoryMapper.selectByPrimaryKey(id);
        if(resumeRepository==null){
            throw new ServiceException("简历库ID不存在");
        }
        resumeRepository.setAttachResume(attachmentName);
        resumeRepositoryMapper.updateByPrimaryKeySelective(resumeRepository);
    }

    /**
     * 保存简历信息
     * @param resumeRepositoryForm
     */
    public void addResume(ResumeRepositoryForm resumeRepositoryForm){
        ResumeRepository resumeRepository = new ResumeRepository();
        //简历已经存在则更新
        String email = resumeRepositoryForm.getEmail();
        if(StringUtils.isEmpty(email)){
            throw new ServiceException("缺少邮箱信息");
        }
        resumeRepository.setCompanyId(getCompanyId());
        resumeRepository.setEmail(email);
        List<ResumeRepository> resumeRepositoryList = resumeRepositoryMapper.selectByEmail(resumeRepository);
        if(CollectionUtils.isEmpty(resumeRepositoryList)){
            resumeRepository = getResumeRepository(resumeRepository,resumeRepositoryForm);
            resumeRepository.setCompanyId(getCompanyId());
            resumeRepository.setParserId(0L);
            resumeRepositoryMapper.insertSelective(resumeRepository);
        }else{
            resumeRepository = resumeRepositoryList.get(0);
            resumeRepository = getResumeRepository(resumeRepository,resumeRepositoryForm);
            resumeRepository.setCompanyId(getCompanyId());
            resumeRepository.setParserId(0L);
            resumeRepositoryMapper.updateByPrimaryKeySelective(resumeRepository);
        }
    }

    /**
     * 简历录入-基本信息
     * @param resumeForm
     * @return
     */
    @Transactional
    public ResumeVo saveBasic(ResumeForm resumeForm) {
        //如果简历已经存在，从数据库取旧简历信息
        Resume resume = null;
        if (resumeForm.getId() != null) {
            resume = resumeMapper.selectByPrimaryKey(resumeForm.getId());
        }
        if (resume == null) {
            resume = new Resume();
        }
        //这里用户为企业用户
        //设置这个字段暂时没有用
        //留给后续扩展用
        resume.setUserId(curUser().getId());
        resume.setName(resumeForm.getName());
        resume.setBirth(resumeForm.getBirth());
        resume.setGender(resumeForm.getGender());
        resume.setCountryId(resumeForm.getCountryId());
        resume.setCurPlace(resumeForm.getCurPlace());
        resume.setGraduateTime(resumeForm.getGraduateTime());
        resume.setReturnTime(resumeForm.getReturnTime());
        resume.setMaxDegreeId(resumeForm.getMaxDegreeId());
        resume.setIntroduction(resumeForm.getIntroduction());
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(resumeForm.getAttachResume())) {
            String newAttachResume = AppUtils.getOssKey(resumeForm.getAttachResume());
            resume.setAttachResume(newAttachResume);
        }
        if (resume.getId() == null) {
            resume.setId(AppUtils.nextId());
            resumeMapper.insertSelective(resume);

            //信息插入到简历库
            ResumeRepository resumeRepository = new ResumeRepository();
            resumeRepository.setCompanyId(getCompanyId());
            resumeRepository.setResumeId(resume.getId());
            resumeRepository.setUserId(resume.getUserId());
            if(resumeForm.getPhone()!=null){
                resumeRepository.setPhone(resumeForm.getPhone().toString());
            }
            resumeRepository.setEmail(resumeForm.getEmail());
            resumeRepositoryMapper.insertSelective(resumeRepository);
        } else {
            resume.setUpdateTime(new Date());
            resumeMapper.updateByPrimaryKeySelective(resume);
        }
        return new ResumeVo().asVo(resume);
    }

    /**
     * 删除附件简历
     * @param resumeId
     */
    public void delResumeAttachment(Long resumeId){
        Resume resume = resumeMapper.selectByPrimaryKey(resumeId);
        if (resume != null) {
            resume.setAttachResume("");
            resumeMapper.updateByPrimaryKey(resume);
        }
    }

    /**
     * 获取我的默认简历，如果没有就创建一个空简历
     *
     * @param resumeId
     * @return
     */
    public ResumeVo getResumeVo(Long resumeId) {
        Resume resume = resumeMapper.selectByPrimaryKey(resumeId);
        ResumeVo resumeVo = new ResumeVo().asVo(resume);
        ResumeRepository resumeRepository = new ResumeRepository();
        resumeRepository.setCompanyId(getCompanyId());
        resumeRepository.setResumeId(resume.getId());
        List<ResumeRepository> resumeRepositoryList = resumeRepositoryMapper.selectAndList(resumeRepository);
        if(CollectionUtils.isNotEmpty(resumeRepositoryList)){
            resumeRepository = resumeRepositoryList.get(0);
            resumeVo.setEmail(resumeRepository.getEmail());
            resumeVo.setPhone(resumeRepository.getPhone());
        }
        resumeVo.setResumeEduList(resumeEduService.getResumeEduList(resume.getId()));
        resumeVo.setResumeExpList(resumeExpService.getResumeExpList(resume.getId()));
        resumeVo.setResumePracticeList(resumePracticeService.getResumePracticeList(resume.getId()));
        resumeVo.setUserExpectJob(userExpectJobService.getUserExpectJob(resume.getUserId()));
        resumeVo.setResumeSkillList(resumeSkillService.getResumeSkillList(resume.getId()));
        resumeVo.setResumeLinkList(resumeLinkService.getResumeLinkList(resume.getId()));
        resumeVo.setResumeCompleteProgress(AppUtils.calCompleteProgress(resumeVo));
        return resumeVo;
    }

    /**
     * 获取简历列表
     *
     * @param listForm
     * @return
     */
    public PageResult<ResumeVo> getResumeList(ResumeListForm listForm){

        if(listForm.getSalaryRangeId() != null){
            DictVo salaryRange =  dictService.getById(listForm.getSalaryRangeId());
            if(salaryRange != null){
                String[] values =  salaryRange.getValue().split("-");
                if(values.length == 2){
                    listForm.setMinSalary(org.apache.commons.lang.math.NumberUtils.toInt(values[0]));
                    listForm.setMaxSalary(org.apache.commons.lang.math.NumberUtils.toInt(values[1]));
                }
            }
        }

        if(listForm.getGpaRangeId() != null){
            DictVo gpaRange =  dictService.getById(listForm.getGpaRangeId());
            if(gpaRange != null){
                String[] values =  gpaRange.getValue().split("-");
                if(values.length == 2){
                    listForm.setMinGpa(org.apache.commons.lang.math.NumberUtils.toDouble(values[0]));
                    listForm.setMaxGpa(NumberUtils.toDouble(values[1]));
                }
            }
        }

        ResumeOptions resumeOptions = new ResumeOptions();
        BeanUtil.copyProperties(listForm, resumeOptions);
        resumeOptions.setCategoryIds(FormUtils.joinWhereIds(listForm.getCategoryIds()));
        resumeOptions.setCityIds(FormUtils.joinWhereIds(listForm.getCityIds()));
        resumeOptions.setDegreeIds(FormUtils.joinWhereIds(listForm.getDegreeIds()));
        resumeOptions.setSchoolIds(FormUtils.joinWhereIds(listForm.getSchoolIds()));
        resumeOptions.setCompanyId(getCompanyId());
        AppUtils.setPage(listForm);
        Page<Resume> resumePage = (Page<Resume>) resumeMapper.selectAndList(resumeOptions);
        PageResult<ResumeVo> pageResult = new PageResult<>(resumePage);
        List<ResumeVo> resumeVoList = new ArrayList<>(resumePage.size());
        for(Resume resume: resumePage){
            //ResumeVo resumeVo = new ResumeVo().asVo(resume);
            //返回更详细的简历信息
            ResumeVo resumeVo = getResumeInfo(resume.getId());
            List<ResumeEduVo> resumeEduVoList = resumeEduService.getResumeEduList(resume.getId());
            resumeVo.setResumeEduList(resumeEduVoList);
            if (CollectionUtils.isNotEmpty(resumeEduVoList)) {
                ResumeEduVo maxResumeEduVo = new ResumeEduVo();
                BeanUtil.copyProperties(resumeEduVoList.get(0), maxResumeEduVo);
                resumeVo.setMaxResumeEdu(maxResumeEduVo);
            }

            ResumeRepository resumeRepository = new ResumeRepository();
            resumeRepository.setCompanyId(getCompanyId());
            resumeRepository.setResumeId(resume.getId());
            List<ResumeRepository> resumeRepositoryList = resumeRepositoryMapper.selectAndList(resumeRepository);
            if(CollectionUtils.isNotEmpty(resumeRepositoryList)){
                resumeRepository = resumeRepositoryList.get(0);
                resumeVo.setEmail(resumeRepository.getEmail());
                resumeVo.setPhone(resumeRepository.getPhone());
            }

            resumeVo.setUserExpectJob(userExpectJobService.getUserExpectJob(resume.getUserId()));
            resumeVoList.add(resumeVo);
        }
        pageResult.setList(resumeVoList);
        return pageResult;
    }

    /**
     * 获取简历信息
     *
     * @param resumeId
     * @return
     */
    public ResumeVo getResumeInfo(Long resumeId) {
        Resume resume = resumeMapper.selectByPrimaryKey(resumeId);
        return toResumeVo(resume);
    }

    private ResumeVo toResumeVo(Resume resume) {
        ResumeVo resumeVo = new ResumeVo().asVo(resume);
        List<ResumeEduVo> resumeEduVoList = resumeEduService.getResumeEduList(resume.getId());
        resumeVo.setResumeEduList(resumeEduVoList);
        resumeVo.setResumeSkillList(resumeSkillService.getResumeSkillList(resume.getId()));
        resumeVo.setResumeExpList(resumeExpService.getResumeExpList(resume.getId()));
        if (CollectionUtils.isNotEmpty(resumeEduVoList)) {
            ResumeEduVo maxResumeEduVo = JSON.parseObject(JSON.toJSONString(resumeEduVoList.get(0)), ResumeEduVo.class);
            resumeVo.setMaxResumeEdu(maxResumeEduVo);
        }
        return resumeVo;
    }

    /**
     * 将用户简历添加到当前企业简历库
     * @param resumeId
     */
    public void addResume(Long resumeId){
        addFromUserResume(resumeId,getCompanyId());
    }

    /**
     * 将用户简历添加到对应企业简历库
     * @param resumeId
     * @param companyId
     */
    public void addFromUserResume(Long resumeId,Long companyId){
        Resume resume = getResume(resumeId);
        String email = getEmail(resume);
        checkEmail(email,companyId);
        ResumeRepository resumeRepository = new ResumeRepository();
        resumeRepository = getResumeRepository(resumeRepository,resume);
        resumeRepository.setCompanyId(companyId);
        resumeRepository.setParserId(0L);
        resumeRepositoryMapper.insertSelective(resumeRepository);
    }

    /**
     * 将一个企业的简历加入到另一个企业的简历库
     * @param id
     * @param companyId
     */
    public void addFromRepository(Long id,Long companyId){
        ResumeRepository resumeRepository = getResumeRepository(id);
        checkEmail(resumeRepository.getEmail(),companyId);
        resumeRepository.setId(null);
        resumeRepository.setCompanyId(companyId);
        resumeRepositoryMapper.insertSelective(resumeRepository);
    }

    public PageResult<ResumeVo> search(ResumeRepositoryListForm listForm){
        if(listForm.getCompanyId() == null || listForm.getCompanyId() == 0){
            listForm.setCompanyId(getCompanyId());
        }
        AppUtils.setPage(listForm);
        ResumeRepository resumeRepositoryTemp = new ResumeRepository();
        resumeRepositoryTemp.setCompanyId(listForm.getCompanyId());
        resumeRepositoryTemp.setEmail(listForm.getEmail());
        resumeRepositoryTemp.setName(listForm.getName());
        resumeRepositoryTemp.setPhone(listForm.getPhone());
        Page<ResumeRepository> page = (Page<ResumeRepository>) resumeRepositoryMapper.selectAndList(resumeRepositoryTemp);
        PageResult<ResumeVo> pageResult = new PageResult<>(page);
        List<ResumeVo> voList = new ArrayList<>(page.size());
        for (ResumeRepository resumeRepository : page) {
            ResumeVo resumeVo = new ResumeVo();
            resumeVo.setName(resumeRepository.getName());
            resumeVo.setEmail(resumeRepository.getEmail());
            resumeVo.setPhone(resumeRepository.getPhone());
            resumeVo.setIntroduction(resumeRepository.getIntroduction());
            voList.add(resumeVo);
        }
        pageResult.setList(voList);
        return pageResult;
    }

//    public PageResult<ResumeRepositoryVo> search(ResumeRepositoryListForm resumeRepositoryListForm){
//        ResumeRepository resumeRepository = new ResumeRepository();
//        Page<ResumeRepository> resumeRepositoryPage = (Page<ResumeRepository>) resumeRepositoryMapper.selectAndList(resumeRepository);
//        PageResult<ResumeRepositoryVo> pageResult = new PageResult<ResumeRepositoryVo>(resumeRepositoryPage);
//        return pageResult;
//    }
//
//    public PageResult<ResumeRepositoryVo> searchByKeyword(ResumeRepositoryListForm form) {
//        return search(form,getCompanyId());
//    }
//
//    public PageResult<ResumeRepositoryVo> search(ResumeRepositoryListForm form){
//        IndexSearcher indexSearcher = luceneContext.getIndexSearcher(indexFolder2);
//        //构造搜索请求
//        BooleanQuery.Builder builder = new BooleanQuery.Builder();
//        String[] keywords = luceneContext.analysis(form.getKeyword());
//        //只在给定企业ID中搜索
//        Query companyQuery = LongPoint.newExactQuery(ResumeAttachmentIndexFields.COMPANY_ID_INDEX,companyId);
//        builder.add(companyQuery, BooleanClause.Occur.MUST);
//        for(String word : keywords){
//            //目标简历附件必须包含所有关键词
//            Term contentTerm = new Term(ResumeAttachmentIndexFields.CONTENT,word);
//            Query contentQuery = new TermQuery(contentTerm);
//            builder.add(contentQuery, BooleanClause.Occur.MUST);
//        }
//        BooleanQuery query = builder.build();
//
//        try {
//            ScoreDoc lastScoreDoc = getLastScoreDoc(form, query, indexSearcher);
//            TopDocs topDocs = indexSearcher.searchAfter(lastScoreDoc, query, form.getLimit());
//            PageResult<ResumeRepositoryVo> pageResult = new PageResult<>();
//            pageResult.setTotal(topDocs.totalHits);
//            pageResult.setCurrentPage(form.getPage());
//            pageResult.setPageSize(form.getLimit());
//            List<ResumeRepositoryVo> resumeRepositoryVoList = new ArrayList<>();
//
//            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
//                Document hitDoc = indexSearcher.doc(scoreDoc.doc);
//                Long resumeId = NumberUtils.toLong(hitDoc.get(ResumeAttachmentIndexFields.RESUME_ID));
//                ResumeRepositoryVo resumeRepositoryVo = new ResumeRepositoryVo().asVo(resumeRepositoryMapper.selectByPrimaryKey(resumeId));
//                resumeRepositoryVoList.add(resumeRepositoryVo);
//            }
//
//            pageResult.setList(resumeRepositoryVoList);
//            pageResult.calPageCountAndHasMore(resumeRepositoryVoList);
//            return pageResult;
//        } catch (IOException ex) {
//            log.error("searchResumeByQuery error, query: " + query.toString() + ", page: " + form.toString(), ex);
//            return PageResult.emptyResult();
//        }
//    }

    /**
     * 全文检索Document
     * 目前是把个字段拼接成一个大字符串
     * 然后建索引
     * 可以改成每个字段单独见索引
     * 查询的时候再合并查询结果
     * @param resumeRepository
     * @return
     */
    private Document getDocument(ResumeRepository resumeRepository) {
        Document document = new Document();
        document.add(new LongPoint(ResumeAttachmentIndexFields.RESUME_ID_INDEX,resumeRepository.getId()));
        document.add(new StoredField(ResumeAttachmentIndexFields.RESUME_ID,resumeRepository.getId()));
        document.add(new LongPoint(ResumeAttachmentIndexFields.COMPANY_ID_INDEX,resumeRepository.getCompanyId()));
        document.add(new StoredField(ResumeAttachmentIndexFields.COMPANY_ID,resumeRepository.getCompanyId()));
        document.add(new StringField(ResumeAttachmentIndexFields.CONTENT,getContent(resumeRepository), Field.Store.NO));
        return document;
    }

    /**
     * 通过ID获取简历对象
     * @param id
     * @return
     */
    private ResumeRepository getResumeRepository(Long id){
        ResumeRepository resumeRepository = resumeRepositoryMapper.selectByPrimaryKey(id);
        if(resumeRepository==null){
            throw new ServiceException("原简历不存在");
        }
        return resumeRepository;
    }

    /**
     * 将ResumeSDK返回的解析结果映射进ResumeRepository对象
     * @param resumeRepository
     * @param result
     * @return
     */
    private ResumeRepository getResumeRepository(ResumeRepository resumeRepository,JSONObject result){
        resumeRepository.setName(result.getString("name"));
        resumeRepository.setBirth(result.getString("birthday"));
        resumeRepository.setEmail(result.getString("email"));
        resumeRepository.setPhone(result.getString("phone"));
        resumeRepository.setIntroduction(result.getString("cont_my_desc"));
        return resumeRepository;
    }

    /**
     * 从表单生成简历
     * @param resumeRepository
     * @param form
     * @return
     */
    private ResumeRepository getResumeRepository(ResumeRepository resumeRepository,ResumeRepositoryForm form){
        resumeRepository.setName(form.getName());
        resumeRepository.setBirth(form.getBirth());
        resumeRepository.setEmail(form.getEmail());
        resumeRepository.setPhone(form.getPhone());
        resumeRepository.setIntroduction(form.getIntroduction());
        return resumeRepository;
    }

    /**
     * 将Resume映射进ResumeRepository对象
     * @param resumeRepository
     * @param resume
     * @return
     */
    private ResumeRepository getResumeRepository(ResumeRepository resumeRepository,Resume resume){
        DateFormat format = DateFormat.getTimeInstance(DateFormat.FULL, Locale.CHINA);
        String birth = "";
        if(resume.getBirth()!=null){
            birth = format.format(resume.getBirth());
        }
        resumeRepository.setName(resume.getName());
        resumeRepository.setEmail(getEmail(resume));
        resumeRepository.setPhone(getPhone(resume));
        resumeRepository.setIntroduction(resume.getIntroduction());
        return resumeRepository;
    }

    /**
     * 生成全文索引字符串
     * @param resumeRepository
     * @return
     */
    private String getContent(ResumeRepository resumeRepository){
        StringBuilder content = new StringBuilder();
        content.append(resumeRepository.getName()+" ");
        content.append(resumeRepository.getEmail()+" ");
        content.append(resumeRepository.getBirth()+" ");
        content.append(resumeRepository.getIntroduction()+" ");
        content.append(resumeRepository.getPhone()+" ");
        return content.toString();
    }

    /**
     * 获取非空的简历对象
     * @param resumeId
     * @return
     */
    private Resume getResume(Long resumeId){
        Resume resume = resumeMapper.selectByPrimaryKey(resumeId);
        if(resume==null){
            throw new ServiceException("简历信息不存在");
        }
        return resume;
    }

    private String getEmail(Resume resume){
        UserApplicant userApplicant = userApplicantMapper.selectByPrimaryKey(resume.getUserId());
        if(userApplicant==null){
            throw new ServiceException("该简历对应用户不存在");
        }
        if(StringUtils.isEmpty(userApplicant.getEmail())){
            throw new ServiceException("该简历对应邮箱不存在");
        }
        return userApplicant.getEmail();
    }

    private String getPhone(Resume resume){
        UserApplicant userApplicant = userApplicantMapper.selectByPrimaryKey(resume.getUserId());
        if(userApplicant==null){
            throw new ServiceException("该简历对应用户不存在");
        }
        return userApplicant.getPhone().toString();
    }

    private void checkEmail(String email,Long companyId){
        email = email==null?"":email;
        ResumeRepository resumeRepository = new ResumeRepository();
        resumeRepository.setEmail(email);
        resumeRepository.setCompanyId(companyId);
        List<ResumeRepository> resumeRepositoryList = resumeRepositoryMapper.selectByEmail(resumeRepository);
        if(CollectionUtils.isNotEmpty(resumeRepositoryList)){
            throw new ServiceException("该邮箱已经存在简历");
        }
    }

    /**
     * 获取当前登录用户关联的公司
     * @return
     */
    public Long getCompanyId() {
        CompanyUser companyUser = companyUserMapper.selectByUserId(curUser().getId());
        if(companyUser==null || companyUser.getCompanyId()==0){
            throw new ServiceException("登录用户未关联公司");
        }
        return companyUser.getCompanyId();
    }

    private void addIndex(Document document) {
        IndexWriter indexWriter = getIndexWriter();
        try {
            indexWriter.addDocument(document);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceException("写入索引失败");
        }
        commit(indexWriter);
    }

    @Async
    public void buildIndex() {
        IndexWriter indexWriter = getIndexWriter();
        commit(indexWriter);
    }

    private IndexWriter getIndexWriter(){
        return luceneContext.getIndexWriter(indexFolder2);
    }

    private void commit(IndexWriter indexWriter){
        try {
            indexWriter.commit();
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceException("写入索引失败");
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

}
