package com.worldelite.job.service;

import com.github.pagehelper.Page;
import com.worldelite.job.constants.*;
import com.worldelite.job.entity.*;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.*;
import com.worldelite.job.mapper.CompanyUserMapper;
import com.worldelite.job.mapper.JobApplyMapper;
import com.worldelite.job.mapper.JobMapper;
import com.worldelite.job.service.search.IndexService;
import com.worldelite.job.service.search.SearchService;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private ResumeService resumeService;

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private SearchService searchService;

    @Autowired
    private IndexService indexService;

    @Autowired
    private MessageService messageService;

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

        job.setAddressId(jobForm.getAddressId());
        CompanyUserVo companyUserVo = companyService.getCompanyUser(curUser().getId());
        if (companyUserVo != null) {
            job.setCompanyId(Long.valueOf(companyUserVo.getCompany().getId()));
        }

        job.setName(jobForm.getName());
        job.setCategoryId(jobForm.getCategoryId());
        job.setAddressId(jobForm.getAddressId());

        if (jobForm.getAddressId() != null) {
            CompanyAddressVo addressVo = companyAddressService.getCompanyAddress(jobForm.getAddressId());
            if (addressVo != null) {
                job.setCityId(addressVo.getCityId());
            }
        }

        job.setDepart(jobForm.getDepart());
        job.setDescription(jobForm.getDescription());
        job.setJobType(jobForm.getJobType());
        job.setMinDegreeId(jobForm.getMinDegreeId());
        job.setMinSalary(jobForm.getMinSalary());
        job.setMaxSalary(jobForm.getMaxSalary());
        job.setSalaryMonths(jobForm.getSalaryMonths());

        if (job.getId() == null) {
            job.setCreatorId(curUser().getId());
            job.setStatus(JobStatus.PUBLISH.value);
            job.setPubTime(new Date());
            job.setId(AppUtils.nextId());
            jobMapper.insertSelective(job);
        } else {
            if(job.getStatus() != JobStatus.PUBLISH.value){
                job.setPubTime(new Date());
            }
            job.setUpdateTime(new Date());
            jobMapper.updateByPrimaryKeySelective(job);
        }

        //增加索引
        indexService.saveJobItem(job.getId());
    }

    /**
     * 用户推荐职位
     *
     * @return
     */
    public PageResult<JobVo> getUserRecommendJobList(JobSearchForm jobSearchForm) {

        // 没有登录并且没有检索条件
        if (curUser() == null && isEmptySearch(jobSearchForm)) {
            return getNewestJobList(jobSearchForm);
        }

        // 是否已附带检索条件
        boolean noRecommend = isEmptySearch(jobSearchForm);

        //已登录用户组合相关条件
        if (curUser() != null) {
            ResumeVo resumeVo = resumeService.getDefaultOrCreate(curUser().getId());
            if (resumeVo.getUserExpectJob() != null) {
                UserExpectJobVo expectJobVo = resumeVo.getUserExpectJob();

                List<JobCategoryVo> categoryList = expectJobVo.getCategoryList();

                if (CollectionUtils.isNotEmpty(categoryList)) {
                    Integer[] categoryIds = new Integer[categoryList.size()];
                    for (int i = 0, size = categoryList.size(); i != size; i++) {
                        categoryIds[i] = categoryList.get(i).getId();
                    }
                    jobSearchForm.setCategoryIds(categoryIds);
                    noRecommend = false;
                }

                if (ArrayUtils.isEmpty(jobSearchForm.getCityIds())) {
                    List<DictVo> cityList = resumeVo.getUserExpectJob().getCityList();
                    if (CollectionUtils.isNotEmpty(cityList)) {
                        Integer[] cityIds = new Integer[cityList.size()];
                        for (int i = 0, size = cityList.size(); i != size; i++) {
                            cityIds[i] = cityList.get(i).getId();
                        }
                        jobSearchForm.setCityIds(cityIds);
                        noRecommend = false;
                    }
                }

                if (jobSearchForm.getMinSalary() == null || jobSearchForm.getMaxSalary() == null) {
                    jobSearchForm.setMinSalary(expectJobVo.getMinSalary());
                    jobSearchForm.setMaxSalary(expectJobVo.getMaxSalary());
                }

            } else if (CollectionUtils.isNotEmpty(resumeVo.getResumeSkillList())) {
                StringBuilder keywordBuilder = new StringBuilder();

                for (ResumeSkillVo resumeSkillVo : resumeVo.getResumeSkillList()) {
                    keywordBuilder.append(resumeSkillVo.getName());
                }

                jobSearchForm.setKeyword(keywordBuilder.toString());

                noRecommend = false;
            }
        }

        PageResult<JobVo> jobPageResult;

        if(!noRecommend){
            jobPageResult = searchService.searchJob(jobSearchForm);
        }else{
            jobPageResult = getNewestJobList(jobSearchForm);
        }
        return jobPageResult;
    }

    private Boolean isEmptySearch(JobSearchForm jobSearchForm) {
        return org.apache.commons.lang.StringUtils.isEmpty(jobSearchForm.getKeyword())
                && jobSearchForm.getSalaryRangeId() == null
                && jobSearchForm.getJobType() == null
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
        Job options = new Job();
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
    public List<JobVo> getUserJobOptions(){
        Job options = new Job();
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
        Job job = jobMapper.selectSimpleById(jobId);
        if(curUser().getType() != UserType.ADMIN.value){
            checkJobCreator(job);
        }
        job.setStatus(JobStatus.OFFLINE.value);
        job.setUpdateTime(new Date());

        //从索引中删除
        indexService.deleteJobItem(jobId);

        // 被管理员强制下架，记录原因，并发送消息
        if(curUser().getType() == UserType.ADMIN.value){
            job.setRemark(reason);

            Message message = new Message();
            message.setFromUser(curUser().getId());
            message.setFromUser(job.getCreatorId());
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
    public void openJob(Long jobId){
        Job job = jobMapper.selectSimpleById(jobId);
        if(job == null){
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

        ResumeVo resumeVo = resumeService.getDefaultOrCreate(curUser().getId());
        if (resumeVo == null && checkResumeComplete(resumeVo)) {
            throw new ServiceException(message("job.apply.no.resume"), ApiCode.UNCOMPLETE_RESUME);
        }

        if (checkUserApply(jobId)) {
            throw new ServiceException(message("job.apply.repeat"));
        }

        JobApply newJobApply = new JobApply();
        newJobApply.setJobId(jobId);
        newJobApply.setUserId(curUser().getId());
        newJobApply.setStatus(JobApplyStatus.APPLY.value);
        newJobApply.setResumeId(Long.valueOf(resumeVo.getId()));
        jobApplyMapper.insertSelective(newJobApply);
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

    private JobVo toJobVo(Job job, Boolean includeCompany) {
        if (job == null) {
            return null;
        }
        JobVo jobVo = new JobVo().asVo(job);
        jobVo.setCategory(jobCategoryService.getById(job.getCategoryId()));
        jobVo.setMinDegree(dictService.getById(job.getMinDegreeId()));
        jobVo.setCity(dictService.getById(job.getCityId()));
        jobVo.setJobType(dictService.getById(job.getJobType()));
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
        jobVo.setAddress(companyAddressService.getCompanyAddress(job.getAddressId()));
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

    private boolean checkResumeComplete(ResumeVo resumeVo) {
        return StringUtils.isNotEmpty(resumeVo.getName())
                && resumeVo.getAge() != null
                && resumeVo.getBirth() != null
                && resumeVo.getGender() != null
                && StringUtils.isNotEmpty(resumeVo.getIntroduction())
                && CollectionUtils.isNotEmpty(resumeVo.getResumeEduList());
    }
}
