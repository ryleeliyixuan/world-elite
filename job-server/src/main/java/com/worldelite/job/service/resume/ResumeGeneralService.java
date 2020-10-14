package com.worldelite.job.service.resume;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.worldelite.job.constants.ResumeStatus;
import com.worldelite.job.constants.ResumeType;
import com.worldelite.job.entity.*;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.ResumeForm;
import com.worldelite.job.form.ResumeListForm;
import com.worldelite.job.mapper.ResumeMapper;
import com.worldelite.job.service.*;
import com.worldelite.job.service.search.SearchService;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.util.FormUtils;
import com.worldelite.job.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class ResumeGeneralService extends ResumeService {

    @Value("${search.index.resumeindex2}")
    private String folder;

    @Autowired
    private ResumeMapper resumeMapper;

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
    private UserApplicantService userApplicantService;

    @Autowired
    private SearchService searchService;

    @Override
    public ResumeDetail getDefaultOrCreate() {
        //获取当前登录用户对应简历
        Long userId = curUser().getId();
        List<Resume> resumeList = getResumeByUserId(userId);
        //如果简历存在则返回第一份简历的简历详情
        if(CollectionUtils.isNotEmpty(resumeList)){
            Resume resume = resumeList.get(0);
            return getResumeDetail(resume.getId());
        }
        //不存在则新建简历并返回详情
        Resume resume = newResumeBasic();
        return getResumeDetail(resume.getId());
    }

    /**
     * 通过用户用户ID获取简历列表
     * @param userId 用户ID
     * @return 简历列表
     */
    private List<Resume> getResumeByUserId(Long userId){
        ResumeOptions options = new ResumeOptions();
        options.setUserId(userId);
        return resumeMapper.selectAndList(options);
    }

    /**
     * 创建空的简历基础信息
     * @return 简历基础信息
     */
    private Resume newResumeBasic(){
        UserApplicant userApplicant = getResumeUser(null);
        Resume resume = new Resume();
        resume.setId(AppUtils.nextId());
        resume.setUserId(userApplicant.getId());
        resume.setName(userApplicant.getName());
        //用户简历类型和状态默认设定好，且不能改变
        resume.setType(ResumeType.GENERAL.value);
        resume.setStatus(ResumeStatus.PUBLISH.value);
        resumeMapper.insertSelective(resume);
        return resume;
    }

    /**
     * 获取简历对应的用户信息
     * @return 普通用户
     */
    private UserApplicant getResumeUser(Long userId){
        //默认当前登录用户
        if(userId == null) userId = curUser().getId();
        UserApplicant userApplicant = userApplicantService.selectByPrimaryKey(userId);
        if(userApplicant == null) throw new ServiceException(message("api.error.data.user"));
        return userApplicant;
    }

    @Override
    public ResumeDetail saveBasic(ResumeForm resumeForm) {
        //简历不存在则先创建空简历
        Resume resume = null;
        if(resumeForm.getId() != null){
            resume = resumeMapper.selectByPrimaryKey(resumeForm.getId());
        }
        if(resume == null){
            resume = newResumeBasic();
        }
        //用表单数据替换旧简历数据
        fillResumeBasic(resume,resumeForm);
        //更新简历数据
        resume.setUpdateTime(new Date());
        resumeMapper.updateByPrimaryKeySelective(resume);
        return getResumeDetail(resume.getId());
    }

    /**
     * 将表单中的简历基础数据提取出来
     * @param resume
     * @param resumeForm
     */
    public void fillResumeBasic(Resume resume,ResumeForm resumeForm){
        resume.setName(resumeForm.getName());
        resume.setBirth(resumeForm.getBirth());
        resume.setGender(resumeForm.getGender());
        //用户简历类型和状态默认设定好，且不能改变
        resume.setType(ResumeType.GENERAL.value);
        resume.setStatus(ResumeStatus.PUBLISH.value);
        resume.setCountryId(resumeForm.getCountryId());
        resume.setCurPlace(resumeForm.getCurPlace());
        resume.setGraduateTime(resumeForm.getGraduateTime());
        resume.setReturnTime(resumeForm.getReturnTime());
        resume.setMaxDegreeId(resumeForm.getMaxDegreeId());
        resume.setIntroduction(resumeForm.getIntroduction());
        if (StringUtils.isNotEmpty(resumeForm.getAttachResume())) {
            String newAttachResume = AppUtils.getOssKey(resumeForm.getAttachResume());
            resume.setAttachResume(newAttachResume);
        }
    }

    @Override
    public ResumeDetail deleteResumeAttachment(Long resumeId) {
        Resume resume = resumeMapper.selectByPrimaryKey(resumeId);
        if (resume != null) {
            resume.setAttachResume("");
            resumeMapper.updateByPrimaryKey(resume);
        }
        return getResumeDetail(resume.getId());
    }

    @Override
    public void deleteResume(Long resumeId) {
        //普通用户简历不支持删除
        throw new ServiceException(message("api.error.fail"));
    }

    @Override
    public ResumeDetail getResumeDetail(Long resumeId) {
        ResumeDetail resumeDetail = new ResumeDetail();
        //获取基础信息
        Resume resume = getResumeBasic(resumeId);
        //获取用户信息
        Long userId = resume.getUserId();
        UserApplicant userApplicant = getResumeUser(userId);
        //简历ID
        resumeDetail.setResumeId(resumeId);
        //用户ID
        resumeDetail.setUserId(userApplicant.getId());
        //名字
        resumeDetail.setName(userApplicant.getName());
        //邮箱
        resumeDetail.setEmail(userApplicant.getEmail());
        //电话
        resumeDetail.setPhone(userApplicant.getPhone());
        //区号
        resumeDetail.setPhoneCode(userApplicant.getPhoneCode());
        //头像
        resumeDetail.setAvatar(userApplicant.getAvatar());
        //性别
        resumeDetail.setGender(userApplicant.getGender());
        //基础信息
        resumeDetail.setResumeBasic(getResumeBasic(resumeId));
        //教育经历
        resumeDetail.setResumeEduList(resumeEduService.getResumeEduList(resumeId));
        //工作经验
        resumeDetail.setResumeExpList(resumeExpService.getResumeExpList(resumeId));
        //实践经验
        resumeDetail.setResumePracticeList(resumePracticeService.getResumePracticeList(resumeId));
        //能力标签
        resumeDetail.setResumeSkillList(resumeSkillService.getResumeSkillList(resumeId));
        //社交主页
        resumeDetail.setResumeLinkList(resumeLinkService.getResumeLinkList(resumeId));
        //意向职位
        resumeDetail.setCategoryList(userExpectJobService.getExpectCategoryList(userApplicant.getId()));
        //意向城市
        resumeDetail.setCityList(userExpectJobService.getExpectCityList(userApplicant.getId()));
        //薪资范围
        resumeDetail.setSalary(userExpectJobService.getSalary(userApplicant.getId()));
        return resumeDetail;
    }

    @Override
    public PageResult<ResumeDetail> list(ResumeListForm resumeListForm) {
        ResumeOptions resumeOptions = new ResumeOptions();
        BeanUtil.copyProperties(resumeListForm, resumeOptions);
        resumeOptions.setCategoryIds(FormUtils.joinWhereIds(resumeListForm.getCategoryIds()));
        resumeOptions.setCityIds(FormUtils.joinWhereIds(resumeListForm.getCityIds()));
        resumeOptions.setDegreeIds(FormUtils.joinWhereIds(resumeListForm.getDegreeIds()));
        resumeOptions.setSchoolIds(FormUtils.joinWhereIds(resumeListForm.getSchoolIds()));
        //用户简历类型为普通用户简历，状态为正常状态
        resumeOptions.setType(ResumeType.GENERAL.value);
        resumeOptions.setStatus(ResumeStatus.PUBLISH.value);
        AppUtils.setPage(resumeListForm);
        Page<Resume> resumePage = (Page<Resume>) resumeMapper.selectAndList(resumeOptions);
        PageResult<ResumeDetail> pageResult = new PageResult<>(resumePage);
        List<ResumeDetail> resumeList = new ArrayList<>(resumePage.size());
        for(Resume resume: resumePage) {
            resumeList.add(getResumeDetail(resume.getId()));
        }
        pageResult.setList(resumeList);
        return pageResult;
    }

    @Override
    public PageResult<ResumeDetail> search(ResumeListForm resumeListForm) {
        return searchService.searchResume(resumeListForm,folder);
    }

    @Override
    public ResumeVo toResumeVo(ResumeDetail resumeDetail) {
        //简历ID如果不存在则不进行转换
        if(resumeDetail.getResumeId() == null) return null;
        ResumeVo resumeVo = new ResumeVo().asVo(resumeDetail.getResumeBasic());
        resumeVo.setAvatar(resumeDetail.getAvatar());
        resumeVo.setEmail(resumeDetail.getEmail());
        resumeVo.setPhoneCode(resumeDetail.getPhoneCode());
        if(resumeDetail.getPhone() != null && resumeDetail.getPhone() != 0){
            resumeVo.setPhone(String.valueOf(resumeDetail.getPhone()));
        }
        //教育信息
        List<ResumeEdu> resumeEduList = resumeDetail.getResumeEduList();
        if(CollectionUtils.isNotEmpty(resumeEduList)){
            List<ResumeEduVo> resumeEduVoList = AppUtils.asVoList(resumeEduList,ResumeEduVo.class);
            resumeVo.setResumeEduList(resumeEduVoList);
            ResumeEduVo maxResumeEduVo = JSON.parseObject(JSON.toJSONString(resumeEduVoList.get(0)), ResumeEduVo.class);
            resumeVo.setMaxResumeEdu(maxResumeEduVo);
        }
        //能力标签
        List<ResumeSkill> resumeSkillList = resumeDetail.getResumeSkillList();
        if(CollectionUtils.isNotEmpty(resumeSkillList)){
            List<ResumeSkillVo> resumeSkillVoList = AppUtils.asVoList(resumeSkillList,ResumeSkillVo.class);
            resumeVo.setResumeSkillList(resumeSkillVoList);
        }
        //工作经历
        List<ResumeExperience> resumeExpList = resumeDetail.getResumeExpList();
        if(CollectionUtils.isNotEmpty(resumeExpList)){
            List<ResumeExpVo> resumeExpVoList = AppUtils.asVoList(resumeExpList,ResumeExpVo.class);
            resumeVo.setResumeExpList(resumeExpVoList);
        }
        //实践经验
        List<ResumePractice> resumePracticeList = resumeDetail.getResumePracticeList();
        if(CollectionUtils.isNotEmpty(resumePracticeList)){
            List<ResumePracticeVo> resumePracticeVoList = AppUtils.asVoList(resumePracticeList,ResumePracticeVo.class);
            resumeVo.setResumePracticeList(resumePracticeVoList);
        }
        //社交主页
        List<ResumeLink> resumeLinkList = resumeDetail.getResumeLinkList();
        if(CollectionUtils.isNotEmpty(resumeLinkList)){
            List<ResumeLinkVo> resumeLinkVoList = AppUtils.asVoList(resumeLinkList,ResumeLinkVo.class);
            resumeVo.setResumeLinkList(resumeLinkVoList);
        }
        //求职意向
        UserExpectJobVo userExpectJobVo = new UserExpectJobVo();
        if(CollectionUtils.isNotEmpty(resumeDetail.getCategoryList())){
            userExpectJobVo.setCategoryList(AppUtils.asVoList(resumeDetail.getCategoryList(),JobCategoryVo.class));
        }
        if(CollectionUtils.isNotEmpty(resumeDetail.getCityList())){
            userExpectJobVo.setCityList(AppUtils.asVoList(resumeDetail.getCityList(),DictVo.class));
        }
        if(resumeDetail.getSalary()!=null){
            userExpectJobVo.setSalary(new DictVo().asVo(resumeDetail.getSalary()));
        }
        resumeVo.setUserExpectJob(userExpectJobVo);
        //计算简历完整度
        resumeVo.setResumeCompleteProgress(AppUtils.calCompleteProgress(resumeVo));
        return resumeVo;
    }

    @Override
    public PageResult<ResumeVo> toResumeVo(PageResult<ResumeDetail> pageResult) {
        PageResult<ResumeVo> voPageResult = new PageResult<>();
        List<ResumeVo> resumeVoList = new ArrayList<>();
        for(ResumeDetail resumeDetail:pageResult.getList()){
            resumeVoList.add(toResumeVo(resumeDetail));
        }
        voPageResult.setTotal(pageResult.getTotal());
        voPageResult.setCurrentPage(pageResult.getCurrentPage());
        voPageResult.setPageCount(pageResult.getPageCount());
        voPageResult.setPageSize(pageResult.getPageSize());
        voPageResult.setHasMore(pageResult.getHasMore());
        voPageResult.setList(resumeVoList);
        return voPageResult;
    }
}
