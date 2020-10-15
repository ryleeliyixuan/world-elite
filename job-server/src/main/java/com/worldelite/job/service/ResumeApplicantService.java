package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.worldelite.job.constants.*;
import com.worldelite.job.dto.LuceneIndexCmdDto;
import com.worldelite.job.entity.*;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.*;
import com.worldelite.job.mapper.*;
import com.worldelite.job.service.read.ResumeFileRead;
import com.worldelite.job.service.search.IndexService;
import com.worldelite.job.service.search.SearchService;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.util.FormUtils;
import com.worldelite.job.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.lucene.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

/**
 * 用户简历服务类
 */
@Service
@Slf4j
public class ResumeApplicantService extends  BaseService{

    @Value("${search.index.resumeindex2}")
    private String folder;

    @Autowired
    private ResumeApplicantMapper resumeApplicantMapper;

    @Autowired
    private JobApplyMapper jobApplyMapper;

    @Autowired
    private ResumeService resumeService;

    @Autowired
    private UserApplicantService userApplicantService;

    @Autowired
    private ResumeEduService resumeEduService;

    @Autowired
    private ResumeExpService resumeExpService;

    @Autowired
    private ResumePracticeService resumePracticeService;

    @Autowired
    private UserExpectJobService userExpectJobService;

    @Autowired
    private ResumeSkillService resumeSkillService;

    @Autowired
    private ResumeLinkService resumeLinkService;

    @Autowired
    private DictService dictService;

    @Autowired
    private IndexService indexService;

    @Autowired
    private SearchService searchService;

    @Autowired
    private ResumeMapper resumeMapper;

    public ResumeApplicant selectByResumeId(Long resumeId){
        return resumeApplicantMapper.selectByResumeId(resumeId);
    }

    /**
     * 获取我的默认简历，如果没有就创建一个空简历
     *
     * @return
     */
    public ResumeVo getDefaultOrCreate() {
        Long userId = curUser().getId();
        //获取用户简历
        ResumeApplicant resumeApplicant = resumeApplicantMapper.selectByUserId(userId);
        //获取用户信息
        UserApplicant userApplicant = userApplicantService.selectByPrimaryKey(userId);
        Resume defaultResume;
        //如果当前用户没有创建简历，就创建一个只有用户名的空简历
        //如果当前用户有多份简历，则返回第一份做为默认简历
        if (resumeApplicant==null) {
            String name = userApplicant.getName();
            defaultResume = resumeService.createResume(name);
            resumeApplicant = new ResumeApplicant();
            resumeApplicant.setUserId(userApplicant.getId());
            resumeApplicant.setResumeId(defaultResume.getId());
            resumeApplicantMapper.insertSelective(resumeApplicant);
        } else {
            // 第一份为默认显示简历
            Long resumeId = resumeApplicant.getResumeId();
            defaultResume = resumeService.selectByPrimaryKey(resumeId);
        }
        ResumeVo resumeVo = new ResumeVo().asVo(defaultResume);
        resumeVo.setUserId(String.valueOf(userApplicant.getId()));
        resumeVo.setAvatar(AppUtils.absOssUrl(userApplicant.getAvatar()));
        resumeVo.setEmail(userApplicant.getEmail());
        resumeVo.setPhoneCode(userApplicant.getPhoneCode());
        if(userApplicant.getPhone() != null && userApplicant.getPhone() != 0){
            resumeVo.setPhone(String.valueOf(userApplicant.getPhone()));
        }
        resumeVo.setResumeEduList(resumeEduService.getResumeEduVoList(defaultResume.getId()));
        resumeVo.setResumeExpList(resumeExpService.getResumeExpVoList(defaultResume.getId()));
        resumeVo.setResumePracticeList(resumePracticeService.getResumePracticeVoList(defaultResume.getId()));
        resumeVo.setUserExpectJob(userExpectJobService.getUserExpectJob(userId));
        resumeVo.setResumeSkillList(resumeSkillService.getResumeSkillVoList(defaultResume.getId()));
        resumeVo.setResumeLinkList(resumeLinkService.getResumeLinkVoList(defaultResume.getId()));
        resumeVo.setResumeCompleteProgress(AppUtils.calCompleteProgress(resumeVo));
        return resumeVo;
    }

    public ResumeDetail getDefaultOrCreate(ResumeType type){
        return null;
    }

    /**
     * 获取简历列表
     *
     * @param listForm
     * @return
     */
    public PageResult getResumeList(ResumeListForm listForm){

        if(listForm.getSalaryId() != null){
            DictVo salaryRange =  dictService.getById(listForm.getSalaryId());
            if(salaryRange != null){
                String[] values =  salaryRange.getValue().split("-");
                if(values.length == 2){
                    listForm.setMinSalary(NumberUtils.toInt(values[0]));
                    listForm.setMaxSalary(NumberUtils.toInt(values[1]));
                }
            }
        }

        if(listForm.getGpaRangeId() != null){
            DictVo gpaRange =  dictService.getById(listForm.getGpaRangeId());
            if(gpaRange != null){
                String[] values =  gpaRange.getValue().split("-");
                if(values.length == 2){
                    listForm.setMinGpa(NumberUtils.toDouble(values[0]));
                    listForm.setMaxGpa(NumberUtils.toDouble(values[1]));
                }
            }
        }

        ResumeOptions resumeOptions = new ResumeOptions();
        BeanUtil.copyProperties(listForm, resumeOptions);
        resumeOptions.setExtQuery("id in (select resume_id from t_resume_applicant)");
        resumeOptions.setCategoryIds(FormUtils.joinWhereIds(listForm.getCategoryIds()));
        resumeOptions.setCityIds(FormUtils.joinWhereIds(listForm.getCityIds()));
        resumeOptions.setDegreeIds(FormUtils.joinWhereIds(listForm.getDegreeIds()));
        resumeOptions.setSchoolIds(FormUtils.joinWhereIds(listForm.getSchoolIds()));
        AppUtils.setPage(listForm);
        Page<Resume> resumePage = (Page<Resume>) resumeService.selectAndList(resumeOptions);
        PageResult<ResumeVo> pageResult = new PageResult<>(resumePage);
        List<ResumeVo> resumeVoList = new ArrayList<>(resumePage.size());
        for(Resume resume: resumePage){
            ResumeVo resumeVo = new ResumeVo().asVo(resume);
            ResumeApplicant resumeApplicant = resumeApplicantMapper.selectByResumeId(resume.getId());
            if(resumeApplicant == null){
                throw new ServiceException("简历缺少用户信息");
            }
            Long userId = resumeApplicant.getUserId();
            //返回更详细的简历信息
            //ResumeVo resumeVo = getResumeInfo(resume.getId());
            List<ResumeEduVo> resumeEduVoList = resumeEduService.getResumeEduVoList(resume.getId());
            resumeVo.setResumeEduList(resumeEduVoList);
            if (CollectionUtils.isNotEmpty(resumeEduVoList)) {
                ResumeEduVo maxResumeEduVo = new ResumeEduVo();
                BeanUtil.copyProperties(resumeEduVoList.get(0), maxResumeEduVo);
                resumeVo.setMaxResumeEdu(maxResumeEduVo);
            }

            UserApplicantVo userVo = userApplicantService.getUserInfo(userId);
            if(userVo != null){
                resumeVo.setEmail(userVo.getEmail());
                resumeVo.setPhone(userVo.getPhone());
                resumeVo.setPhoneCode(userVo.getPhoneCode());
            }

            resumeVo.setUserExpectJob(userExpectJobService.getUserExpectJob(userId));

            //统计简历投递情况
            JobApplyOptions applyOptions = new JobApplyOptions();
            applyOptions.setResumeId(resume.getId());
            applyOptions.setUserId(listForm.getUserId());
            final int applyTotalCount = jobApplyMapper.countJobApply(applyOptions);
            resumeVo.setApplyTotalCount(applyTotalCount);
            applyOptions.setStatuses(String.valueOf(JobApplyStatus.APPLY.value));
            final int applyingCount = jobApplyMapper.countJobApply(applyOptions);
            resumeVo.setApplyingCount(applyingCount);
            applyOptions.setStatuses(String.valueOf(JobApplyStatus.CANDIDATE.value));
            final int applyCandidateCount = jobApplyMapper.countJobApply(applyOptions);
            resumeVo.setApplyCandidateCount(applyCandidateCount);
            applyOptions.setStatuses(String.valueOf(JobApplyStatus.INTERVIEW.value));
            final int applyInterviewCount = jobApplyMapper.countJobApply(applyOptions);
            resumeVo.setApplyInterviewCount(applyInterviewCount);
            applyOptions.setStatuses(String.valueOf(JobApplyStatus.OFFER.value));
            final int  applyOfferCount = jobApplyMapper.countJobApply(applyOptions);
            resumeVo.setApplyOfferCount(applyOfferCount);
            resumeVoList.add(resumeVo);
        }
        pageResult.setList(resumeVoList);
        return pageResult;
    }

    public PageResult<ResumeVo> getResumeVoList(ResumeListForm listForm){
        return null;
    }

    /**
     * 获取简历详情
     *
     * @param resumeId
     * @return
     */
    public ResumeVo getResumeDetail(Long resumeId) {
        ResumeVo resumeVo = resumeService.getResumeVo(resumeId);
        ResumeApplicant resumeApplicant = resumeApplicantMapper.selectByResumeId(resumeId);
        UserApplicant userApplicant = userApplicantService.selectByPrimaryKey(resumeApplicant.getUserId());
        resumeVo.setUserId(String.valueOf(userApplicant.getId()));
        resumeVo.setAvatar(userApplicant.getAvatar());
        resumeVo.setEmail(userApplicant.getEmail());
        if(userApplicant.getPhone() != null && userApplicant.getPhone() != 0){
            resumeVo.setPhone(String.valueOf(userApplicant.getPhone()));
        }
        return resumeVo;
    }

    /**
     * 简历 - 保存基础信息
     *
     * @param resumeForm
     * @return
     */
    @Transactional
    public ResumeVo saveBasic(ResumeForm resumeForm) {
        Resume resume = resumeService.saveBasic(resumeForm);
        //保存或者更新用户信息
        ResumeApplicant resumeApplicant = resumeApplicantMapper.selectByUserId(curUser().getId());
        if(resumeApplicant == null){
            resumeApplicant = new ResumeApplicant();
            resumeApplicant.setUserId(curUser().getId());
            resumeApplicant.setResumeId(resume.getId());
            resumeApplicantMapper.insertSelective(resumeApplicant);
        }else{
            resumeApplicant.setResumeId(resume.getId());
            resumeApplicantMapper.updateByPrimaryKeySelective(resumeApplicant);
        }
        UserForm userForm = new UserForm();
        userForm.setId(resumeApplicant.getUserId());
        userForm.setAvatar(AppUtils.getOssKey(resumeForm.getAvatar()));
        userForm.setName(resumeForm.getName());
        userForm.setGender(resumeForm.getGender());
        userForm.setPhoneCode(resumeForm.getPhoneCode());
        userForm.setPhone(resumeForm.getPhone());
        userApplicantService.modifyUser(userForm);
        //Todo 创建索引
        return new ResumeVo().asVo(resume);
    }

    /**
     * 删除附件简历
     *
     * @param resumeId
     */
    public void delResumeAttachment(Long resumeId){
        //Todo 做一些权限检查
        resumeService.delResumeAttachment(resumeId);
        //Todo 更新索引
    }

    /**
     * 重建所有用户简历索引
     */
    public void rebuildAllIndex(){
        //因为一次读取全部简历数据会对内存产生很大压力
        //所以分批次从数据库读取数据再生成索引
        List<Resume> resumeList;
        int curPage = 1;
        ResumeOptions options = new ResumeOptions();
        do {
            PageHelper.startPage(curPage++, 100, false);
            resumeList = resumeMapper.selectAndList(options);
            for (Resume resume : resumeList) {
                //获取简历详情
                ResumeDetail resumeDetail = resumeService.getResumeDetail(resume.getUserId(),resume.getId());
                UserApplicant userApplicant = userApplicantService.selectByPrimaryKey(resume.getUserId());
                //简历详情或者用户信息不存在时
                //说明该简历为异常数据
                //不创建索引，直接跳过
                if(resume!=null && userApplicant!=null) {
                    resumeDetail.setUserId(resume.getUserId());
                    resumeDetail.setName(userApplicant.getName());
                    resumeDetail.setEmail(userApplicant.getEmail());
                    resumeDetail.setGender(userApplicant.getGender());
                }
                //生成索引
                indexService.saveResumeItem(resumeDetail,folder);
            }
        } while (CollectionUtils.isNotEmpty(resumeList));
    }

    /**
     * Todo 获取重建索引进度
     */
    public void getRebuildProcess(){

    }

}
