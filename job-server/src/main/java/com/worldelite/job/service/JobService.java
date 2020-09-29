package com.worldelite.job.service;

import com.github.pagehelper.Page;
import com.worldelite.job.constants.*;
import com.worldelite.job.dto.LuceneIndexCmdDto;
import com.worldelite.job.entity.*;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.*;
import com.worldelite.job.mapper.JobApplyMapper;
import com.worldelite.job.mapper.JobMapper;
import com.worldelite.job.mq.JobMessage;
import com.worldelite.job.mq.MessageTaskHandler;
import com.worldelite.job.service.search.IndexService;
import com.worldelite.job.service.search.SearchService;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.document.Document;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Service
public class JobService extends BaseService {

    @Autowired
    private JobMapper jobMapper;

    @Autowired
    private JobApplyMapper jobApplyMapper;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private JobCategoryService jobCategoryService;

    @Autowired
    private DictService dictService;

    @Autowired
    private CompanyAddressService companyAddressService;

    @Autowired
    @Lazy
    private ResumeApplicantService resumeService;

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private SearchService searchService;

    @Autowired
    private IndexService indexService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageTaskHandler messageTaskHandler;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Resource(name = "luceneIndexCmdFanoutExchange")
    private FanoutExchange exchange;


    /**
     * 获取职位信息: 适用列表
     *
     * @param jobId
     * @return
     */
    public JobVo getJobInfo(Long jobId, Boolean includeCompany) {
        Job job = jobMapper.selectByPrimaryKey(jobId);
        JobVo jobVo = toJobVo(job, includeCompany);
        return jobVo;
    }

    /**
     * 获取职位信息: 适用详情
     *
     * @param jobId
     * @return
     */
    public JobVo getJobDetail(Long jobId) {
        Job job = jobMapper.selectByPrimaryKey(jobId);
        return toJobVoDetail(job);
    }

    /**
     * 保存职位
     *
     * @param jobForm
     * @return
     */
    @Transactional
    public void saveJob(JobForm jobForm) {

        Job job = null;

        if (jobForm.getId() != null) {
            job = jobMapper.selectByPrimaryKey(jobForm.getId());
            checkJobCreator(job);
        }

        if (job == null) {
            job = new Job();
        }

        CompanyUserVo companyUserVo = companyService.getCompanyUser(curUser().getId());
        if (companyUserVo != null) {
            job.setCompanyId(Long.valueOf(companyUserVo.getCompany().getId()));
        }

        job.setName(jobForm.getName());
        job.setCategoryId(jobForm.getCategoryId());
        job.setAddress(jobForm.getAddress());
        job.setCityId(jobForm.getCityId());

        if(jobForm.getDepart() != null){
            job.setDepart(jobForm.getDepart());
        }

        job.setDescription(jobForm.getDescription());
        job.setJobType(jobForm.getJobType());
        job.setRecruitType(jobForm.getRecruitType());
        job.setMinDegreeId(jobForm.getMinDegreeId());
        job.setSalaryId(jobForm.getSalaryId());
        DictVo salary = dictService.getById(jobForm.getSalaryId());
        String salaryStr = salary.getValue()==null?"":salary.getValue();
        String[] salarys = salaryStr.split("-");
        if(salarys.length==2){
            job.setMinSalary(Integer.valueOf(salarys[0]));
            job.setMaxSalary(Integer.valueOf(salarys[1]));
        }else{
            job.setMinSalary(0);
            job.setMaxSalary(0);
        }
        job.setSalaryMonths(jobForm.getSalaryMonths());
        job.setExperienceId(jobForm.getExperienceId());
        job.setSkillTags(jobForm.getSkillTags());
        job.setIndustryTags(jobForm.getIndustryTags());

        if (job.getId() == null) {
            job.setCreatorId(curUser().getId());
            job.setStatus(JobStatus.PUBLISH.value);
            job.setPubTime(new Date());
            job.setId(AppUtils.nextId());
            jobMapper.insertSelective(job);
            //发送订阅消息
            messageTaskHandler.sendJobMessage(new JobMessage(getJobInfo(job.getId(), true)));
        } else {
            if (job.getStatus() != JobStatus.PUBLISH.value) {
                job.setPubTime(new Date());
            }
            job.setUpdateTime(new Date());
            jobMapper.updateByPrimaryKeyWithBLOBs(job);
        }

        //增加索引
        Document document = indexService.saveJobItem(job.getId());

        rabbitTemplate.convertAndSend(exchange.getName(), "", new LuceneIndexCmdDto(document, OperationType.CreateOrUpdate, BusinessType.Job));
    }

    /**
     * 用户推荐职位
     *
     * @return
     */
    public PageResult<JobVo> getUserRecommendJobList(JobSearchForm jobSearchForm) {
        // 没有登录并且没有检索条件
        if (curUser() == null) {
            return getNewestJobList(jobSearchForm);
        } else {
            //已登录用户组合相关条件
            ResumeVo resumeVo = resumeService.getDefaultOrCreate();;
            if (resumeVo.getUserExpectJob() != null) {
                UserExpectJobVo expectJobVo = resumeVo.getUserExpectJob();

                List<JobCategoryVo> categoryList = expectJobVo.getCategoryList();

                if (CollectionUtils.isNotEmpty(categoryList)) {
                    Integer[] categoryIds = new Integer[categoryList.size()];
                    for (int i = 0, size = categoryList.size(); i != size; i++) {
                        categoryIds[i] = categoryList.get(i).getId();
                    }
                    jobSearchForm.setCategoryIds(categoryIds);
                }

                if (ArrayUtils.isEmpty(jobSearchForm.getCityIds())) {
                    List<DictVo> cityList = resumeVo.getUserExpectJob().getCityList();
                    if (CollectionUtils.isNotEmpty(cityList)) {
                        Integer[] cityIds = new Integer[cityList.size()];
                        for (int i = 0, size = cityList.size(); i != size; i++) {
                            cityIds[i] = cityList.get(i).getId();
                        }
                        jobSearchForm.setCityIds(cityIds);
                    }
                }

            } else if (CollectionUtils.isNotEmpty(resumeVo.getResumeSkillList())) {
                StringBuilder keywordBuilder = new StringBuilder();

                for (ResumeSkillVo resumeSkillVo : resumeVo.getResumeSkillList()) {
                    keywordBuilder.append(resumeSkillVo.getName());
                }

                jobSearchForm.setKeyword(keywordBuilder.toString());
            }
            PageResult<JobVo> jobPageResult = searchService.searchJob(jobSearchForm);
            //如果根据简历中的求职意向匹配不到相关职位时 就把全部职位返回
            if (jobPageResult == null || CollectionUtils.isEmpty(jobPageResult.getList())) {
                jobPageResult = getNewestJobList(jobSearchForm);
            }
            return jobPageResult;
        }
    }

    public Boolean isEmptySearch(JobSearchForm jobSearchForm) {
        return org.apache.commons.lang.StringUtils.isEmpty(jobSearchForm.getKeyword())
                && jobSearchForm.getSalaryRangeId() == null
                && jobSearchForm.getJobType() == null
                && org.apache.commons.lang.ArrayUtils.isEmpty(jobSearchForm.getSalaryRangeIds())
                && org.apache.commons.lang.ArrayUtils.isEmpty(jobSearchForm.getJobTypes())
                && org.apache.commons.lang.ArrayUtils.isEmpty(jobSearchForm.getCityIds())
                && org.apache.commons.lang.ArrayUtils.isEmpty(jobSearchForm.getCategoryIds())
                && org.apache.commons.lang.ArrayUtils.isEmpty(jobSearchForm.getCompanyStageIds())
                && org.apache.commons.lang.ArrayUtils.isEmpty(jobSearchForm.getCompanyIndustryIds())
                && org.apache.commons.lang.ArrayUtils.isEmpty(jobSearchForm.getCompanyScaleIds())
                && org.apache.commons.lang.ArrayUtils.isEmpty(jobSearchForm.getDegreeIds());
    }

    private PageResult<JobVo> getNewestJobList(PageForm pageForm) {
        JobListForm listForm = new JobListForm();
        // 防止网络爬虫
        listForm.setPage(Math.min(30, pageForm.getPage()));
        listForm.setLimit(Math.min(100, pageForm.getLimit()));
        listForm.setStatus(JobStatus.PUBLISH.value);
        listForm.setSort("-pub_time");
        return getJobList(listForm);
    }

    /**
     * 获取职位列表
     *
     * @param jobListForm
     * @return
     */
    public PageResult<JobVo> getJobList(JobListForm jobListForm) {
        AppUtils.setPage(jobListForm);
        JobOptions options = new JobOptions();
        options.setId(jobListForm.getJobId());
        options.setCompanyId(jobListForm.getCompanyId());
        options.setCreatorId(jobListForm.getCreatorId());
        options.setName(jobListForm.getName());
        options.setJobType(jobListForm.getJobType());
        options.setCityId(jobListForm.getCityId());
        options.setStatus(jobListForm.getStatus());
        Page<Job> jobPage = (Page<Job>) jobMapper.selectAndList(options);
        PageResult<JobVo> pageResult = new PageResult<>(jobPage);
        List<JobVo> jobVoList = new ArrayList<>(jobPage.size());
        for (Job job : jobPage) {
            JobApplyOptions applyOptions = new JobApplyOptions();
            applyOptions.setJobId(job.getId());
            final int totalResumeCount = jobApplyMapper.countJobApply(applyOptions);
            applyOptions.setStatuses(StringUtils.join(new Byte[]{JobApplyStatus.APPLY.value, JobApplyStatus.VIEW.value}, ","));
            final int newResumeCount = jobApplyMapper.countJobApply(applyOptions);
            applyOptions.setStatuses(String.valueOf(JobApplyStatus.CANDIDATE.value));
            final int candidateResumeCount = jobApplyMapper.countJobApply(applyOptions);
            applyOptions.setStatuses(String.valueOf(JobApplyStatus.INTERVIEW.value));
            final int interviewResumeCount = jobApplyMapper.countJobApply(applyOptions);
            JobVo jobVo = toJobVo(job, true);
            jobVo.setTotalResumeCount(totalResumeCount);
            jobVo.setNewResumeCount(newResumeCount);
            jobVo.setCandidateResumeCount(candidateResumeCount);
            jobVo.setInterviewResumeCount(interviewResumeCount);
            jobVoList.add(jobVo);
        }
        pageResult.setList(jobVoList);
        return pageResult;
    }

    /**
     * 简历筛选-职位
     *
     * @return
     */
    public List<JobVo> getUserJobOptions() {
        JobOptions options = new JobOptions();
        options.setCreatorId(curUser().getId());
        options.setStatus(JobStatus.PUBLISH.value);
        List<Job> jobList = jobMapper.selectAndList(options);
        List<JobVo> jobVoList = new ArrayList<>(jobList.size());
        for (Job job : jobList) {
            jobVoList.add(toJobVo(job, false));
        }
        return jobVoList;
    }

    /**
     * 下架职位
     *
     * @param jobId
     */
    @Transactional
    public void takeOffJob(Long jobId, String reason) {
        takeOffJob(false, jobId, reason);
    }

    /**
     * 删除职位
     * @param jobId
     */
    @Transactional
    public void deleteJob(Long jobId){
        Job job = jobMapper.selectSimpleById(jobId);

        if(job == null){
            throw new ServiceException(ApiCode.OBJECT_NOT_FOUND);
        }

        if(job.getStatus() == JobStatus.PUBLISH.value){
            throw new ServiceException(message("请先下架再进行删除"), ApiCode.INVALID_OPERATION);
        }

        if (curUser().getType() != UserType.ADMIN.value) {
            checkJobCreator(job);
        }

        job.setDelFlag(Bool.TRUE);
        job.setUpdateTime(new Date());
        jobMapper.updateByPrimaryKeySelective(job);
    }

    public void takeOffJob(Boolean force, Long jobId, String reason) {
        Job job = jobMapper.selectSimpleById(jobId);
        if (!force && curUser().getType() != UserType.ADMIN.value) {
            checkJobCreator(job);
        }
        job.setStatus(JobStatus.OFFLINE.value);
        job.setUpdateTime(new Date());

        //从索引中删除
        Document document = indexService.deleteJobItem(jobId);

        //MQ广播索引更新指令
        rabbitTemplate.convertAndSend(exchange.getName(), "", new LuceneIndexCmdDto(document, OperationType.Delete, BusinessType.Job));

        // 被系统或者管理员强制下架，记录原因，并发送消息
        if (force || curUser().getType() == UserType.ADMIN.value) {

            if (curUser() != null && curUser().getType() == UserType.ADMIN.value) {
                job.setDelFlag(Bool.TRUE); // 被管理员下架则直接删除
                job.setRemark(reason);
            }

            Message message = new Message();
            message.setFromUser(curUser() == null ? -1 : curUser().getId()); // -1 表示系统下架
            message.setToUser(job.getCreatorId());
            message.setContent(message("message.job.takeoff", job.getName(), reason));
            message.setUrl(AppUtils.wholeWebUrl(String.format("job/%s", jobId)));
            messageService.sendMessage(message);
        }

        jobMapper.updateByPrimaryKeySelective(job);
    }

    /**
     * 开放职位
     *
     * @param jobId
     */
    public void openJob(Long jobId) {
        Job job = jobMapper.selectSimpleById(jobId);
        if (job == null) {
            return;
        }
        checkJobCreator(job);
        job.setStatus(JobStatus.PUBLISH.value);
        job.setPubTime(new Date());
        job.setUpdateTime(new Date());
        jobMapper.updateByPrimaryKeySelective(job);

        // 加入索引
        indexService.saveJobItem(jobId);
    }

    /**
     * 申请岗位
     *
     * @param jobId
     */
    public void applyJob(Long jobId) {

        Job job = jobMapper.selectSimpleById(jobId);
        if (job == null) {
            throw new ServiceException(ApiCode.INVALID_OPERATION);
        }

        ResumeVo resumeVo = null;
        if (resumeVo == null) {
            throw new ServiceException(message("job.apply.no.resume"), ApiCode.UNCOMPLETE_RESUME);
        }

        checkResumeComplete(resumeVo);

        if (checkUserApply(jobId)) {
            throw new ServiceException(message("job.apply.repeat"));
        }

        JobApply newJobApply = new JobApply();
        newJobApply.setJobId(jobId);
        newJobApply.setUserId(curUser().getId());
        newJobApply.setStatus(JobApplyStatus.APPLY.value);
        newJobApply.setType(JobApplyType.APPLICANT.value);
        newJobApply.setResumeId(Long.valueOf(resumeVo.getId()));
        jobApplyMapper.insertSelective(newJobApply);

        // 给职位创建者发消息
        Message message = new Message();
        message.setFromUser(curUser().getId());
        message.setToUser(job.getCreatorId());
        message.setContent(message("message.job.apply", job.getName()));
        message.setUrl("/manage-job");
        messageService.sendMessage(message);
    }

    /**
     * 获取用户申请的职位
     *
     * @param listForm
     * @return
     */
    public PageResult<JobVo> getApplyJobList(ApplyJobListForm listForm) {
        JobApply options = new JobApply();
        options.setUserId(listForm.getUserId());
        options.setResumeId(listForm.getResumeId());
        options.setStatus(listForm.getStatus());
        if (StringUtils.isEmpty(listForm.getSort())) {
            listForm.setSort("-id");
        }
        AppUtils.setPage(listForm);
        Page<JobApply> jobApplyPage = (Page<JobApply>) jobApplyMapper.selectAndList(options);
        PageResult<JobVo> pageResult = new PageResult<>(jobApplyPage);
        List<JobVo> jobVoList = new ArrayList<>(jobApplyPage.size());
        for (JobApply jobApply : jobApplyPage) {
            JobVo jobVo = getJobInfo(jobApply.getJobId(), true);
            jobVo.setApplyStatus(jobApply.getStatus());
            jobVo.setApplyTime(jobApply.getCreateTime());
            jobVoList.add(jobVo);
        }
        pageResult.setList(jobVoList);
        return pageResult;
    }

    /**
     * 推荐简历
     *
     * @param jobId
     * @param resumeId
     */
    public void recommendResumeForJob(Long jobId, Long resumeId){
        Job job = jobMapper.selectByPrimaryKey(jobId);
        if(job == null){
            throw new ServiceException(ApiCode.OBJECT_NOT_FOUND);
        }
        ResumeVo resumeVo = resumeService.getResumeDetail(resumeId);
        if(resumeVo == null){
            throw new ServiceException("简历不存在，请检查输入的简历ID", ApiCode.OBJECT_NOT_FOUND);
        }
        Message message = new Message();
        message.setFromUser(-1L);
        message.setToUser(job.getCreatorId());
        message.setContent(message("message.recommend.resume.for.job", job.getName(), resumeVo.getName()));
        message.setUrl(String.format("/resume?resumeId=%s", resumeId));
        messageService.sendMessage(message);
    }

    private JobVo toJobVo(Job job, Boolean includeCompany) {
        if (job == null) {
            return null;
        }
        JobVo jobVo = new JobVo().asVo(job);
        jobVo.setCategory(jobCategoryService.getById(job.getCategoryId()));
        jobVo.setMinDegree(dictService.getById(job.getMinDegreeId()));
        jobVo.setCity(dictService.getById(job.getCityId()));
        jobVo.setJobType(dictService.getById(job.getJobType()));
        jobVo.setRecruitType(dictService.getById(job.getRecruitType()));
        jobVo.setSalary(dictService.getById(job.getSalaryId()));
        jobVo.setExperience(dictService.getById(job.getExperienceId()));
        if (includeCompany) {
            jobVo.setCompanyUser(companyService.getCompanyUser(job.getCreatorId()));
        }
        return jobVo;
    }

    private JobVo toJobVoDetail(Job job) {
        if (job == null) {
            return null;
        }
        JobVo jobVo = toJobVo(job, true);
        if (curUser() != null) {
            Boolean favorite = favoriteService.checkUserFavorite(job.getId(), FavoriteType.JOB);
            jobVo.setFavoriteFlag(favorite ? Bool.TRUE : Bool.FALSE);
            Boolean apply = checkUserApply(job.getId());
            jobVo.setApplyFlag(apply ? Bool.TRUE : Bool.FALSE);
        } else {
            jobVo.setFavoriteFlag(Bool.FALSE);
            jobVo.setApplyFlag(Bool.FALSE);
        }
        return jobVo;
    }

    /**
     * 检查用户是否申请了刚岗位
     *
     * @return
     */
    private Boolean checkUserApply(Long jobId) {

        JobApply options = new JobApply();
        options.setJobId(jobId);
        options.setUserId(curUser().getId());

        List<JobApply> jobApplyList = jobApplyMapper.selectAndList(options);

        if (CollectionUtils.isEmpty(jobApplyList)) {
            return false;
        }

        for (JobApply jobApply : jobApplyList) {
            if (jobApply.getStatus() != JobApplyStatus.ABANDON.value) {
                return true;
            }
        }

        return false;
    }

    private void checkJobCreator(Job job) {
        if (job != null && job.getCreatorId() != null && !job.getCreatorId().equals(curUser().getId())) {
            throw new ServiceException(ApiCode.PERMISSION_DENIED);
        }
    }

    private void checkResumeComplete(ResumeVo resumeVo) {
        if (StringUtils.isEmpty(resumeVo.getName())
                || resumeVo.getAge() == null
                || resumeVo.getBirth() == null
                || resumeVo.getGender() == null) {

            throw new ServiceException("请先完善简历：基本信息未填写", ApiCode.UNCOMPLETE_RESUME);
        }

        if (StringUtils.isEmpty(resumeVo.getAvatar())) {
            throw new ServiceException("请先完善简历：简历头像未上传", ApiCode.UNCOMPLETE_RESUME);
        }

        if (StringUtils.isEmpty(resumeVo.getIntroduction())) {
            throw new ServiceException("请先完善简历：自我介绍未填写", ApiCode.UNCOMPLETE_RESUME);
        }

        if (CollectionUtils.isEmpty(resumeVo.getResumeEduList())) {
            throw new ServiceException("请先完善简历：教育经历未填写", ApiCode.UNCOMPLETE_RESUME);
        }


        if (CollectionUtils.isEmpty(resumeVo.getResumeExpList())) {
            throw new ServiceException("请先完善简历：工作经验未填写", ApiCode.UNCOMPLETE_RESUME);
        }

        if (CollectionUtils.isEmpty(resumeVo.getResumePracticeList())) {
            throw new ServiceException("请先完善简历：实践经验未填写", ApiCode.UNCOMPLETE_RESUME);
        }

        if (CollectionUtils.isEmpty(resumeVo.getResumeSkillList())) {
            throw new ServiceException("请先完善简历：能力标签未填写", ApiCode.UNCOMPLETE_RESUME);
        }
    }
}
