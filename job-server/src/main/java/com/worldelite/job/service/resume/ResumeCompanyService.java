package com.worldelite.job.service.resume;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.worldelite.job.constants.ResumeStatus;
import com.worldelite.job.constants.ResumeType;
import com.worldelite.job.entity.*;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.*;
import com.worldelite.job.mapper.CompanyUserMapper;
import com.worldelite.job.mapper.ResumeMapper;
import com.worldelite.job.mapper.ResumeMergeAttachMapper;
import com.worldelite.job.service.*;
import com.worldelite.job.service.sdk.ResumeSDK;
import com.worldelite.job.service.search.IndexService;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 企业简历库服务类
 */
@Slf4j
@Service
public class ResumeCompanyService extends ResumeService{

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
    private UserCorporateService userCorporateService;

    @Autowired
    private IndexService indexService;

    @Autowired
    private SearchService searchService;

    @Autowired
    private CompanyUserMapper companyUserMapper;

    @Autowired
    private UserRepositoryService userRepositoryService;

    @Autowired
    private ResumeSDK resumeSDK;

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

    @Override
    public List<ResumeDetail> getDefaultListOrCreate() {
        return null;
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
        //更新用户数据
        updateUser(resume.getUserId(),resumeForm);
        return getResumeDetail(resume.getId());
    }

    @Override
    public ResumeDetail addResume() {
        return null;
    }

    @Override
    public void updatePriority(Long resumeId, Byte order) {

    }

    @Override
    @Transactional
    public ResumeDetail parseAttachment(String attachmentName) {
        //获取OSS路径
        String fileName = AppUtils.getOssKey(attachmentName);
        String filePath = AppUtils.absOssUrl(fileName);
        JSONObject result = resumeSDK.parse(filePath);
        //保存基本信息
        ResumeForm resumeForm = resumeSDK.getResume(result);
        resumeForm.setAttachResume(fileName);
        resumeForm.setStatus(ResumeStatus.PUBLISH.value);
        resumeForm.setType(ResumeType.COMPANY.value);
        ResumeDetail resumeDetail = saveBasic(resumeForm);
        Long resumeId = resumeDetail.getResumeId();
        //保存教育信息
        List<ResumeEduForm> resumeEduFormList = resumeSDK.getResumeEdu(result);
        for(ResumeEduForm eduForm:resumeEduFormList){
            eduForm.setResumeId(resumeId);
            resumeEduService.saveResumeEdu(eduForm);
        }
        //保存工作经验
        List<ResumeExpForm> resumeExpFormList = resumeSDK.getResumeExperience(result);
        for(ResumeExpForm expForm:resumeExpFormList){
            expForm.setResumeId(resumeId);
            resumeExpService.saveResumeExp(expForm);
        }
        //保存实践经验
        List<ResumePracticeForm> practiceFormList = resumeSDK.getResumePractice(result);
        for(ResumePracticeForm practiceForm:practiceFormList){
            practiceForm.setResumeId(resumeId);
            resumePracticeService.saveResumePractice(practiceForm);
        }
        //能力标签
        ResumeSkillForm resumeSkillForm = resumeSDK.getResumeSkill(result);
        resumeSkillForm.setResumeId(resumeId);
        resumeSkillService.saveResumeSkill(resumeSkillForm);
        //社交主页
        ResumeLinkForm resumeLinkForm = resumeSDK.getResumeLink(result);
        resumeLinkForm.setResumeId(resumeId);
        resumeLinkService.saveResumeLink(resumeLinkForm);
        //生成索引
        resumeDetail = getResumeDetail(resumeId);
        indexService.saveResumeItem(resumeDetail,folder);
        //返回简历详情
        return resumeDetail;
    }

    private void updateUser(Long userId, ResumeForm resumeForm) {
        UserRepository userRepository = userRepositoryService.getUserById(userId);
        //简历对应用户不存在
        if(userRepository==null) throw new ServiceException(message("api.error.data.user"));
        userRepository.setName(resumeForm.getName());
        userRepository.setEmail(resumeForm.getEmail());
        userRepository.setGender(resumeForm.getGender());
        userRepository.setAvatar(resumeForm.getAvatar());
        userRepository.setPhone(resumeForm.getPhone());
        userRepository.setPhoneCode(resumeForm.getPhoneCode());
        userRepositoryService.saveUser(userRepository);
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
        resume.setType(ResumeType.COMPANY.value);
        if(resumeForm.getStatus()!=null) {
            resume.setStatus(resumeForm.getStatus());
        }
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
        UserRepository userRepository = userRepositoryService.getUserById(userId);
        //简历对应用户不存在
        if(userRepository==null) throw new ServiceException(message("api.error.data.user"));
        //简历ID
        resumeDetail.setResumeId(resumeId);
        //用户ID
        resumeDetail.setUserId(userId);
        //名字
        resumeDetail.setName(userRepository.getName());
        //邮箱
        resumeDetail.setEmail(userRepository.getEmail());
        //电话
        resumeDetail.setPhone(userRepository.getPhone());
        //区号
        resumeDetail.setPhoneCode(userRepository.getPhoneCode());
        //头像
        resumeDetail.setAvatar(AppUtils.absOssUrl(userRepository.getAvatar()));
        //性别
        resumeDetail.setGender(userRepository.getGender());
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
//        //意向职位
//        resumeDetail.setCategoryList(userExpectJobService.getExpectCategoryList(userId));
//        //意向城市
//        resumeDetail.setCityList(userExpectJobService.getExpectCityList(userId));
//        //薪资范围
//        resumeDetail.setSalary(userExpectJobService.getSalary(userId));
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
        //企业简历类型为企业用户简历，状态为正常状态
        resumeOptions.setType(ResumeType.COMPANY.value);
        resumeOptions.setStatus(ResumeStatus.PUBLISH.value);
        //只能查询当前登录用户所在企业的所有简历
        resumeOptions.setCompanyId(getCompanyId());
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
        //企业简历类型为企业用户简历，状态为正常状态
        resumeListForm.setType(ResumeType.COMPANY.value);
        resumeListForm.setStatus(ResumeStatus.PUBLISH.value);
        return searchService.searchResume(resumeListForm,folder);
    }

    @Override
    public void rebuildAllIndex(){
        //因为一次读取全部简历数据会对内存产生很大压力
        //所以分批次从数据库读取数据再生成索引
        List<Resume> resumeList;
        int curPage = 1;
        ResumeOptions options = new ResumeOptions();
        options.setType(ResumeType.COMPANY.value);
        do {
            PageHelper.startPage(curPage++, 100, false);
            resumeList = resumeMapper.selectAndList(options);
            for (Resume resume : resumeList) {
                //获取简历详情
                ResumeDetail resumeDetail = getResumeDetail(resume.getId());
                UserRepository userRepository = userRepositoryService.getUserById(resume.getUserId());
                //简历详情或者用户信息不存在时
                //说明该简历为异常数据
                //不创建索引，直接跳过
                if(resumeDetail!=null && userRepository!=null) {
/*                    resumeDetail.setUserId(resume.getUserId());
                    resumeDetail.setName(userRepository.getName());
                    resumeDetail.setEmail(userRepository.getEmail());
                    resumeDetail.setGender(userRepository.getGender());*/
                    //生成索引
                    indexService.saveResumeItem(resumeDetail,folder);
                }
            }
        } while (CollectionUtils.isNotEmpty(resumeList));
    }

    public void getRebuildProcess(){

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

        // 邮箱
        if (resumeDetail.getEmail() != null && resumeDetail.getEmail().length() != 0) {
            resumeVo.setEmail(resumeDetail.getEmail());
        }

        // 回国时间 & 政治面貌
        if (resumeDetail.getResumeBasic() != null) {
            resumeVo.setUpdateTime(resumeDetail.getResumeBasic().getUpdateTime());
            resumeVo.setReturnTime(resumeDetail.getResumeBasic().getReturnTime());
            resumeVo.setMaritalStatus(resumeDetail.getResumeBasic().getMaritalStatus());
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
//        //求职意向
//        UserExpectJobVo userExpectJobVo = new UserExpectJobVo();
//        if(CollectionUtils.isNotEmpty(resumeDetail.getCategoryList())){
//            userExpectJobVo.setCategoryList(AppUtils.asVoList(resumeDetail.getCategoryList(),JobCategoryVo.class));
//        }
//        if(CollectionUtils.isNotEmpty(resumeDetail.getCityList())){
//            userExpectJobVo.setCityList(AppUtils.asVoList(resumeDetail.getCityList(),CityVo.class));
//        }
//        if(resumeDetail.getSalary()!=null){
//            userExpectJobVo.setSalary(new DictVo().asVo(resumeDetail.getSalary()));
//        }
//        resumeVo.setUserExpectJob(userExpectJobVo);
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


    /**
     * 获取登录用户对应的企业ID
     * @return 企业ID
     */
    private Long getCompanyId(){
        UserCorporate user = getDefaultUser();
        CompanyUser companyUser = companyUserMapper.selectByUserId(user.getId());
        //任意企业用户都会对应一个企业，如果不存在，则判定企业数据缺失
        if(companyUser==null) throw new ServiceException(message("api.error.data.company"));
        return companyUser.getCompanyId();
    }

    /**
     * 通过用户ID获取企业简历列表
     * @param userId 用户ID
     * @return 简历列表
     */
    private List<Resume> getResumeByUserId(Long userId){
        ResumeOptions options = new ResumeOptions();
        //获取当前登录用户草稿状态的简历
        options.setCreatorId(userId);
        options.setStatus(ResumeStatus.DRAFT.value);
        options.setType(ResumeType.COMPANY.value);
        return resumeMapper.selectAndList(options);
    }

    /**
     * 创建空的简历基础信息
     * @return 简历基础信息
     */
    private Resume newResumeBasic(){
        //新建企业简历用户
        UserRepository userRepository = userRepositoryService.newUser();
        userRepositoryService.addUser(userRepository);
        Resume resume = new Resume();
        resume.setId(AppUtils.nextId());
        resume.setUserId(userRepository.getId());
        resume.setName(userRepository.getName());
        //企业简历默认为草稿状态，简历类型固定且不能改变
        resume.setType(ResumeType.COMPANY.value);
        resume.setStatus(ResumeStatus.PUBLISH.value);
        resumeMapper.insertSelective(resume);
        return resume;
    }

    /**
     * 获取简历对应的用户信息
     * @return 普通用户
     */
    private UserCorporate getResumeUser(Long userId){
        //默认当前登录用户
        if(userId == null) userId = curUser().getId();
        UserCorporate userCorporate = userCorporateService.selectByPrimaryKey(userId);
        if(userCorporate == null) throw new ServiceException(message("api.error.data.user"));
        return userCorporate;
    }

    /**
     * 获取默认简历用户
     * @return 默认简历用户
     */
    private UserCorporate getDefaultUser(){
        return getResumeUser(null);
    }
}
