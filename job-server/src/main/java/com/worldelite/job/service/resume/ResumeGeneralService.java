package com.worldelite.job.service.resume;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.worldelite.job.constants.*;
import com.worldelite.job.dto.LuceneIndexCmdDto;
import com.worldelite.job.entity.*;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.*;
import com.worldelite.job.mapper.JobApplyMapper;
import com.worldelite.job.mapper.ResumeMapper;
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
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
    private JobApplyMapper jobApplyMapper;

    @Autowired
    private ResumeMergeAttachService resumeMergeAttachService;

    @Autowired
    private ResumeLanguageService resumeLanguageService;

    @Autowired
    private ResumeCertificateService resumeCertificateService;

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

    @Autowired
    private IndexService indexService;

    @Autowired
    private ResumeSDK resumeSDK;

    @Autowired
    private MessageService messageService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Resource(name = "luceneIndexCmdFanoutExchange")
    private FanoutExchange exchange;

    @Resource(name = "luceneIndexCmdQueue")
    private Queue queue;

    //only return single resume
    @Override
    public ResumeDetail getDefaultOrCreate() {
        //????????????????????????????????????
        Long userId = curUser().getId();
        List<Resume> resumeList = getResumeByUserId(userId);
        //?????????????????????????????????????????????????????????
        if (CollectionUtils.isNotEmpty(resumeList)) {
            Resume resume = resumeList.get(0);
            return getResumeDetail(resume.getId());
        }
        //???????????????????????????????????????
        Resume resume = newResumeBasic();
        return getResumeDetail(resume.getId());
    }

    //return one or more resume
    @Override
    public List<ResumeDetail> getDefaultListOrCreate() {
        Long userId = curUser().getId();
        List<Resume> resumeList = getResumeByUserId(userId);
        List<ResumeDetail> lists = new ArrayList<>();
        //if exist one or more
        if (resumeList.size() != 0) {
            for (Resume one : resumeList) {
                lists.add(getResumeDetail(one.getId()));
            }
            return lists;
        }
        //if not exist
        Resume resume = newResumeBasic();
        lists.add(getResumeDetail(resume.getId()));
        return lists;
    }

    /**
     * ??????????????????ID??????????????????
     *
     * @param userId ??????ID
     * @return ????????????
     */
    private List<Resume> getResumeByUserId(Long userId) {
        ResumeOptions options = new ResumeOptions();
        options.setUserId(userId);
        return resumeMapper.selectAndList(options);
    }

    /**
     * ??????????????????????????????
     *
     * @return ??????????????????
     */
    private Resume newResumeBasic() {
        //get basic info from t_user_applicant
        UserApplicant userApplicant = getResumeUser(null);
        Resume resume = new Resume();
        resume.setId(AppUtils.nextId());
        resume.setUserId(userApplicant.getId());
        //????????????, ????????????????????????????????????
        //resume.setName(userApplicant.getName());
        //????????????????????????????????????????????????????????????
        resume.setType(ResumeType.GENERAL.value);
        resume.setStatus(ResumeStatus.PUBLISH.value);
        resumeMapper.insertSelective(resume);
        return resume;
    }

    /**
     * ?????????????????????????????????
     *
     * @return ????????????
     */
    private UserApplicant getResumeUser(Long userId) {
        //????????????????????????
        if (userId == null) userId = curUser().getId();
        UserApplicant userApplicant = userApplicantService.selectByPrimaryKey(userId);
        if (userApplicant == null) throw new ServiceException(message("api.error.data.user"));
        return userApplicant;
    }

    @Override
    public ResumeDetail saveBasic(ResumeForm resumeForm) {
        //this is the most important function!
        //????????????????????????????????????
        Resume resume = null;
        if (resumeForm.getId() != null) {
            resume = resumeMapper.selectByPrimaryKey(resumeForm.getId());
        }
        if (resume == null) {
            resume = newResumeBasic();
            resumeForm.setId(resume.getId());
        }
        //????????????????????????????????????
        //copy data from resumeForm to resume
        if (resume.getId() == null) {
            return null;
        }
        fillResumeBasic(resume, resumeForm);
        //??????????????????
        resume.setUpdateTime(new Date());
        resumeMapper.updateByPrimaryKeySelective(resume);
        //we don't need to update user application because each user will have multiple resumes
        //*****************************************
//        UserApplicant userApplicant = new UserApplicant();
//        userApplicant.setId(curUser().getId());
//        userApplicant.setName(resumeForm.getName());
//        userApplicant.setPhoneCode(resumeForm.getPhoneCode());
//        userApplicant.setPhone(resumeForm.getPhone());
//        if (StringUtils.isNotEmpty(resumeForm.getAvatar())) {
//            userApplicant.setAvatar(AppUtils.getOssKey(resumeForm.getAvatar()));
//        }
//        userApplicant.setGender(resumeForm.getGender());
//        userApplicant.setUpdateTime(new Date());
//        userApplicantService.updateByPrimaryKeySelective(userApplicant);

        //????????????
        log.debug("??????ID???{}", resume.getId());
        ResumeDetail resumeDetail = getResumeDetail(resume.getId());
        if (resumeDetail.calcCompletion() > 50) {
            indexService.saveResumeItem(resumeDetail, folder);

            //MQ????????????????????????
            LuceneIndexCmdDto indexCmdDto = new LuceneIndexCmdDto(resumeDetail.getResumeId(), OperationType.CREATE_OR_UPDATE, BusinessType.RESUME, queue.getActualName());
            rabbitTemplate.convertAndSend(exchange.getName(), StrUtil.EMPTY, indexCmdDto);
            log.info("Lucene index synchronize command message [saveResumeItem] {}", indexCmdDto.toString());
        }

        return resumeDetail;
    }

    @Override
    public ResumeDetail addResume() {
        Resume resume = newResumeBasic();
        resume.setUpdateTime(new Date());
        resumeMapper.updateByPrimaryKeySelective(resume);
        ResumeDetail resumeDetail = getResumeDetail(resume.getId());

        if (resumeDetail.calcCompletion() > 50) {
            indexService.saveResumeItem(resumeDetail, folder);

            //MQ????????????????????????
            LuceneIndexCmdDto indexCmdDto = new LuceneIndexCmdDto(resumeDetail.getResumeId(), OperationType.CREATE_OR_UPDATE, BusinessType.RESUME, queue.getActualName());
            rabbitTemplate.convertAndSend(exchange.getName(), StrUtil.EMPTY, indexCmdDto);
            log.info("Lucene index synchronize command message [saveResumeItem] {}", indexCmdDto.toString());
        }

        return getResumeDetail(resume.getId());
    }

    @Override
    public void updatePriority(Long resumeId, Byte order) {
        Resume options = new Resume();
        options.setId(resumeId);
        List<Resume> resumeList = resumeMapper.selectAndList(options);
        List<Long> queue = new ArrayList<>(resumeList.size());
        for (Resume resume : resumeList) {
            queue.set(resume.getPriority(), resume.getId());
        }
        queue.remove(resumeId);
        queue.add(order, resumeId);
        for (int i = 0; i < queue.size(); i++) {
            options = new Resume();
            options.setId(queue.get(i));
            options.setPriority((byte) i);
            resumeMapper.updateByPrimaryKeySelective(options);
        }

    }

    @Override
    public ResumeVo parseAttachment(ParseAttachmentForm parseAttachmentForm) {
        //??????OSS??????
        String attachmentName = parseAttachmentForm.getName();
        String fileName = AppUtils.getOssKey(attachmentName);
        String filePath = AppUtils.absOssUrl(fileName);
        JSONObject result = resumeSDK.parse(filePath);
        ResumeVo resumeVo = new ResumeVo();

        resumeVo.setName(result.getString("name")); //??????
        resumeVo.setGender(resumeSDK.getGender(result.getString("gender"))); //??????
//        resumeVo.setAge(1);
        resumeVo.setBirth(resumeSDK.getDate(result.getString("birthday"))); //??????
//        resumeVo.setMaxResumeEdu(new ResumeEduVo()); //??????
//        resumeVo.setReturnTime(new Date()); //????????????
        resumeVo.setGraduateTime(resumeSDK.getDate(result.getString("grad_time"))); //????????????
        resumeVo.setCurPlace(result.getString("living_address_norm")); //????????????
//        resumeVo.setPhoneCode(""); //????????????
//        resumeVo.setPhone(""); //????????????
        resumeVo.setEmail(result.getString("email")); //??????
        resumeVo.setIntroduction(result.getString("cont_my_desc")); //????????????
        resumeVo.setType(ResumeType.GENERAL.value); //????????????
        resumeVo.setStatus(ResumeStatus.PUBLISH.value); //????????????

//        resumeVo.setMaritalStatus((byte) 1); // ????????????
//        resumeVo.setPriority((byte) 2);

        //??????????????????
        JSONArray eduList = result.getJSONArray("education_objs");
        if (eduList != null) {
            List<ResumeEduVo> resumeEduList = new ArrayList<>();
            for (int i = 0; i < eduList.size(); i++) {
                ResumeEduVo resumeEdu = new ResumeEduVo();
                JSONObject edu = eduList.getJSONObject(i);
                resumeEdu.setStartTime(resumeSDK.getDate(edu.getString("start_date")));
                resumeEdu.setFinishTime(resumeSDK.getDate(edu.getString("end_date")));
                resumeEdu.setSchoolName(edu.getString("edu_college"));
                resumeEdu.setMajorName(edu.getString("edu_major"));
                //Todo ??????degree
//                resumeEdu.setDegree(new);
                try {
                    resumeEdu.setGpa(Double.parseDouble(edu.getString("edu_gpa")));
                } catch (Exception e) {
                    resumeEdu.setGpa(0.0);
                }
                resumeEduList.add(resumeEdu);
            }
            resumeVo.setResumeEduList(resumeEduList);
        }

        //??????????????????
        JSONArray experienceList = result.getJSONArray("job_exp_objs");
        if (experienceList != null) {
            List<ResumeExpVo> resumeExpList = new ArrayList<>();
            for (int i = 0; i < experienceList.size(); i++) {
                ResumeExpVo resumeExp = new ResumeExpVo();
                JSONObject experience = experienceList.getJSONObject(i);
                resumeExp.setStartTime(resumeSDK.getDate(experience.getString("start_date")));
                resumeExp.setFinishTime(resumeSDK.getDate(experience.getString("end_date")));
                resumeExp.setCompany(experience.getString("job_cpy"));
                resumeExp.setDepart(experience.getString("job_dept"));
                resumeExp.setPost(experience.getString("job_position"));
                resumeExp.setDescription(experience.getString("job_content"));
                resumeExpList.add(resumeExp);
            }
            resumeVo.setResumeExpList(resumeExpList);
        }

        //??????????????????
        JSONArray practiceList = result.getJSONArray("proj_exp_objs");
        if (practiceList != null) {
            List<ResumePracticeVo> resumePracticeList = new ArrayList<>();
            for (int i = 0; i < practiceList.size(); i++) {
                JSONObject practice = practiceList.getJSONObject(i);
                ResumePracticeVo resumePractice = new ResumePracticeVo();
                resumePractice.setStartTime(resumeSDK.getDate(practice.getString("start_date")));
                resumePractice.setFinishTime(resumeSDK.getDate(practice.getString("end_date")));
                resumePractice.setTitle(practice.getString("proj_name"));
                resumePractice.setPost(practice.getString("proj_position"));
                resumePractice.setDescription(practice.getString("proj_content"));
                resumePracticeList.add(resumePractice);
            }
            resumeVo.setResumePracticeList(resumePracticeList);
        }

        //??????????????????
        JSONArray skillList = result.getJSONArray("skills_objs");
        if (skillList != null) {
            List<ResumeSkillVo> resumeSkillList = new ArrayList<>();
            for (int i = 0; i < skillList.size(); i++) {
                JSONObject skill = skillList.getJSONObject(i);
                ResumeSkillVo resumeSkillVo = new ResumeSkillVo();
                resumeSkillVo.setName(skill.getString("skills_name"));
                resumeSkillList.add(resumeSkillVo);
            }
            resumeVo.setResumeSkillList(resumeSkillList);
        }

        //??????????????????
        String link = result.getString("blog");
        if (StringUtils.isNotEmpty(link)) {
            List<ResumeLinkVo> resumeLinkList = new ArrayList<>();
            ResumeLinkVo resumeLinkVo = new ResumeLinkVo();
            resumeLinkVo.setName("??????");
            resumeLinkVo.setLink(link);
            resumeLinkList.add(resumeLinkVo);
            resumeVo.setResumeLinkList(resumeLinkList);
        }

        //??????????????????
        if (parseAttachmentForm != null && parseAttachmentForm.getAsAttachment()) {
            resumeVo.setAttachResume(fileName); //????????????
            resumeVo.setAttachResumeName(attachmentName);
        }
        return resumeVo;
    }

    @Override
    public void parseAttachment(String attachmentName) {
        String fileName = AppUtils.getOssKey(attachmentName);
        String filePath = AppUtils.absOssUrl(fileName);
        JSONObject result = resumeSDK.parse(filePath);
        //??????????????????
        ResumeForm resumeForm = resumeSDK.getResume(result);
        resumeForm.setAttachResume(fileName);
        resumeForm.setStatus(ResumeStatus.PUBLISH.value);
        resumeForm.setType(ResumeType.COMPANY.value);
        ResumeDetail resumeDetail = saveBasic(resumeForm);
        Long resumeId = resumeDetail.getResumeId();
        //??????????????????
        List<ResumeEduForm> resumeEduFormList = resumeSDK.getResumeEdu(result);
        for (ResumeEduForm eduForm : resumeEduFormList) {
            eduForm.setResumeId(resumeId);
            resumeEduService.saveResumeEdu(eduForm);
        }
        //??????????????????
        List<ResumeExpForm> resumeExpFormList = resumeSDK.getResumeExperience(result);
        for (ResumeExpForm expForm : resumeExpFormList) {
            expForm.setResumeId(resumeId);
            resumeExpService.saveResumeExp(expForm);
        }
        //??????????????????
        List<ResumePracticeForm> practiceFormList = resumeSDK.getResumePractice(result);
        for (ResumePracticeForm practiceForm : practiceFormList) {
            practiceForm.setResumeId(resumeId);
            resumePracticeService.saveResumePractice(practiceForm);
        }
        //????????????
        ResumeSkillForm resumeSkillForm = resumeSDK.getResumeSkill(result);
        resumeSkillForm.setResumeId(resumeId);
        resumeSkillService.saveResumeSkill(resumeSkillForm);
        //????????????
        ResumeLinkForm resumeLinkForm = resumeSDK.getResumeLink(result);
        resumeLinkForm.setResumeId(resumeId);
        resumeLinkService.saveResumeLink(resumeLinkForm);
        //????????????
        resumeDetail = getResumeDetail(resumeId);
        indexService.saveResumeItem(resumeDetail, folder);
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param resume
     * @param resumeForm
     */
    public void fillResumeBasic(Resume resume, ResumeForm resumeForm) {
        resume.setId(resumeForm.getId());
        resume.setName(resumeForm.getName());
        resume.setBirth(resumeForm.getBirth());
        resume.setGender(resumeForm.getGender());
        //????????????????????????????????????????????????????????????
        resume.setType(ResumeType.GENERAL.value);
        resume.setStatus(ResumeStatus.PUBLISH.value);
        resume.setCountryId(resumeForm.getCountryId());
        resume.setCurPlace(resumeForm.getCurPlace());
        resume.setGraduateTime(resumeForm.getGraduateTime());
        resume.setReturnTime(resumeForm.getReturnTime());
        resume.setMaxDegreeId(resumeForm.getMaxDegreeId());
        resume.setIntroduction(resumeForm.getIntroduction());
        resume.setEmail(resumeForm.getEmail());
        resume.setPhone(resumeForm.getPhone() == null ? null : resumeForm.getPhone() + "");
        resume.setMaritalStatus(resumeForm.getMaritalStatus());
        resume.setPriority(resumeForm.getPriority());
        resume.setTitle(resumeForm.getTitle());
        //******************************
        resumeMergeAttachService.saveResumeMergeAttaches(resumeForm.getAttachOthers(), resume);

        if (StringUtils.isNotEmpty(resumeForm.getAttachResume())) {
            String newAttachResume = AppUtils.getOssKey(resumeForm.getAttachResume());
            resume.setAttachResume(newAttachResume);
            resume.setAttachResumeName(resumeForm.getAttachResumeName());
        }
        if (StringUtils.isNotEmpty(resumeForm.getAvatar())) {
            resume.setAvatar(AppUtils.getOssKey(resumeForm.getAvatar()));
        }
    }


    @Override
    public ResumeDetail deleteResumeAttachment(Long resumeId) {
        Resume resume = resumeMapper.selectByPrimaryKey(resumeId);
        if (resume != null) {
            resume.setAttachResume("");
            resume.setAttachResumeName("");
            resumeMapper.updateByPrimaryKey(resume);
        }

        //TODO ?????????????????????????????? ????????????null
        /*if (resume != null) {
            return getResumeDetail(resume.getId());
        }*/
        return null;
    }

    @Override
    public void deleteResume(Long resumeId) {
        resumeMapper.deleteByPrimaryKey(resumeId);

        if (resumeId == null) return;
        indexService.deleteResumeItem(resumeId);
        //MQ????????????????????????
        LuceneIndexCmdDto indexCmdDto = new LuceneIndexCmdDto(resumeId, OperationType.DELETE, BusinessType.RESUME, queue.getActualName());
        rabbitTemplate.convertAndSend(exchange.getName(), StrUtil.EMPTY, indexCmdDto);
        log.info("Lucene index synchronize command message [deleteResume] {}", indexCmdDto.toString());
    }

    @Override
    public void rebuildAllIndex() {
        //??????????????????????????????????????????????????????????????????
        //??????????????????????????????????????????????????????
        List<Resume> resumeList;
        int curPage = 1;
        ResumeOptions options = new ResumeOptions();
        options.setType(ResumeType.GENERAL.value);
        do {
            PageHelper.startPage(curPage++, 100, false);
            resumeList = resumeMapper.selectAndList(options);
            for (Resume resume : resumeList) {
                //??????????????????
                try {
                    ResumeDetail resumeDetail = getResumeDetail(resume.getId());
                    UserApplicant userApplicant = userApplicantService.selectByPrimaryKey(resume.getUserId());
                    //??????????????????????????????????????????
                    //??????????????????????????????
                    //??????????????????????????????
                    if (resumeDetail != null && userApplicant != null && resumeDetail.calcCompletion() > 50) {
                        /*resumeDetail.setUserId(resume.getUserId());
                        resumeDetail.setName(userApplicant.getName());
                        resumeDetail.setEmail(userApplicant.getEmail());
                        resumeDetail.setGender(userApplicant.getGender());*/

                        //????????????
                        indexService.saveResumeItem(resumeDetail, folder);
                    }
                } catch (ServiceException e) {
                    log.error(e.getMessage(), e);
                }
            }
        } while (CollectionUtils.isNotEmpty(resumeList));
    }

    public void getRebuildProcess() {

    }

    @Override
    public ResumeDetail getResumeDetail(Long resumeId) {
        ResumeDetail resumeDetail = new ResumeDetail();
        //??????????????????
        Resume resume = getResumeBasic(resumeId);

        if (resume == null) return null;
        //??????????????????
        UserApplicant userApplicant = getResumeUser(resume.getUserId());
        //??????ID
        resumeDetail.setResumeId(resumeId);
        //??????ID
        resumeDetail.setUserId(userApplicant.getId());
        //??????
        resumeDetail.setName(resume.getName());
        //??????
        resumeDetail.setEmail(resume.getEmail());
        resumeDetail.setTitle(resume.getTitle());
        //??????
        if (!StringUtils.isEmpty(resume.getPhone())) {
            resumeDetail.setPhone(Long.parseLong(resume.getPhone()));
        }
        // ????????????
//        resumeDetail.setWorkType(resume.getWorkType());
        //??????
        resumeDetail.setPhoneCode(userApplicant.getPhoneCode());
        //??????
//        resumeDetail.setAvatar(AppUtils.absOssUrl(userApplicant.getAvatar()));
        resumeDetail.setAvatar(AppUtils.absOssUrl(resume.getAvatar()));
        //??????
        resumeDetail.setGender(userApplicant.getGender());
        //????????????
        resumeDetail.setResumeBasic(getResumeBasic(resumeId));
        //????????????
        resumeDetail.setResumeEduList(resumeEduService.getResumeEduList(resumeId));
        //????????????
        resumeDetail.setResumeExpList(resumeExpService.getResumeExpList(resumeId));
        //????????????
        resumeDetail.setResumePracticeList(resumePracticeService.getResumePracticeList(resumeId));
        //????????????
        resumeDetail.setResumeSkillList(resumeSkillService.getResumeSkillList(resumeId));
//        //????????????
//        resumeDetail.setResumeLinkList(resumeLinkService.getResumeLinkList(resumeId));
//        //????????????
//        resumeDetail.setCategoryList(userExpectJobService.getExpectCategoryList(resumeId));
//        //????????????
//        resumeDetail.setCityList(userExpectJobService.getExpectCityList(userApplicant.getId()));
//        //????????????
//        resumeDetail.setSalary(userExpectJobService.getSalary(userApplicant.getId()));
//        // ??????????????????
//        resumeDetail.setExpectWorkType(userExpectJobService.getExpectWorkType(userApplicant.getId()));
        resumeDetail.setUserExpectJobVo(userExpectJobService.getUserExpectJob(resumeId));
        //language
        resumeDetail.setResumeLanguageList(resumeLanguageService.getResumeLanguageList(resumeId));
        //other attaches
        resumeDetail.setResumeMergeAttachList(resumeMergeAttachService.getResumeMergeAttaches(resumeId));
        //certificate
        resumeDetail.setResumeCertificateList(resumeCertificateService.getResumeCertificateList(resumeId));
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
        //???????????????????????????????????????????????????????????????
        resumeOptions.setType(ResumeType.GENERAL.value);
        resumeOptions.setStatus(ResumeStatus.PUBLISH.value);
        AppUtils.setPage(resumeListForm);
        Page<Resume> resumePage = (Page<Resume>) resumeMapper.selectAndList(resumeOptions);
        PageResult<ResumeDetail> pageResult = new PageResult<>(resumePage);
        List<ResumeDetail> resumeList = new ArrayList<>(resumePage.size());
        for (Resume resume : resumePage) {
            resumeList.add(getResumeDetail(resume.getId()));
        }
        pageResult.setList(resumeList);
        return pageResult;
    }

    @Override
    public PageResult<ResumeDetail> search(ResumeListForm resumeListForm) {
        resumeListForm.setType(ResumeType.GENERAL.value);
        resumeListForm.setStatus(ResumeStatus.PUBLISH.value);
        return searchService.searchResume(resumeListForm, folder);
    }

    @Override
    public ResumeVo toResumeVo(ResumeDetail resumeDetail) {
        //??????ID?????????????????????????????????
        if (resumeDetail.getResumeId() == null) return null;
        ResumeVo resumeVo = new ResumeVo().asVo(resumeDetail.getResumeBasic());
        resumeVo.setAvatar(resumeDetail.getAvatar());
        resumeVo.setEmail(resumeDetail.getEmail());
        resumeVo.setPhoneCode(resumeDetail.getPhoneCode());
        resumeVo.setTitle(resumeDetail.getTitle());

        if (resumeDetail.getPhone() != null && resumeDetail.getPhone() != 0) {
            resumeVo.setPhone(String.valueOf(resumeDetail.getPhone()));
        }

        // ??????
        if (resumeDetail.getEmail() != null && resumeDetail.getEmail().length() != 0) {
            resumeVo.setEmail(resumeDetail.getEmail());
        }

        // ???????????? & ????????????
        if (resumeDetail.getResumeBasic() != null) {
            resumeVo.setUpdateTime(resumeDetail.getResumeBasic().getUpdateTime());
            resumeVo.setReturnTime(resumeDetail.getResumeBasic().getReturnTime());
            resumeVo.setMaritalStatus(resumeDetail.getResumeBasic().getMaritalStatus());
            resumeVo.setPriority(resumeDetail.getResumeBasic().getPriority());
            resumeVo.setAttachResume(resumeDetail.getResumeBasic().getAttachResume());
            resumeVo.setAttachResumeName(resumeDetail.getResumeBasic().getAttachResumeName());
        }

        //????????????
        List<ResumeEdu> resumeEduList = resumeDetail.getResumeEduList();
        if (CollectionUtils.isNotEmpty(resumeEduList)) {
            List<ResumeEduVo> resumeEduVoList = AppUtils.asVoList(resumeEduList, ResumeEduVo.class);
            resumeVo.setResumeEduList(resumeEduVoList);
            ResumeEduVo maxResumeEduVo = JSON.parseObject(JSON.toJSONString(resumeEduVoList.get(0)), ResumeEduVo.class);
            resumeVo.setMaxResumeEdu(maxResumeEduVo);
        }
        //????????????
        List<ResumeSkill> resumeSkillList = resumeDetail.getResumeSkillList();
        if (CollectionUtils.isNotEmpty(resumeSkillList)) {
            List<ResumeSkillVo> resumeSkillVoList = AppUtils.asVoList(resumeSkillList, ResumeSkillVo.class);
            resumeVo.setResumeSkillList(resumeSkillVoList);
        }
        //????????????
        List<ResumeExperience> resumeExpList = resumeDetail.getResumeExpList();
        if (CollectionUtils.isNotEmpty(resumeExpList)) {
            List<ResumeExpVo> resumeExpVoList = AppUtils.asVoList(resumeExpList, ResumeExpVo.class);
            resumeVo.setResumeExpList(resumeExpVoList);
        }
        //????????????
        List<ResumePractice> resumePracticeList = resumeDetail.getResumePracticeList();
        if (CollectionUtils.isNotEmpty(resumePracticeList)) {
            List<ResumePracticeVo> resumePracticeVoList = AppUtils.asVoList(resumePracticeList, ResumePracticeVo.class);
            resumeVo.setResumePracticeList(resumePracticeVoList);
        }
//        //????????????
//        List<ResumeLink> resumeLinkList = resumeDetail.getResumeLinkList();
//        if (CollectionUtils.isNotEmpty(resumeLinkList)) {
//            List<ResumeLinkVo> resumeLinkVoList = AppUtils.asVoList(resumeLinkList, ResumeLinkVo.class);
//            resumeVo.setResumeLinkList(resumeLinkVoList);
//        }
        //????????????
//        UserExpectJobVo userExpectJobVo = new UserExpectJobVo();
//        if (CollectionUtils.isNotEmpty(resumeDetail.getCategoryList())) {
//            userExpectJobVo.setCategoryList(AppUtils.asVoList(resumeDetail.getCategoryList(), JobCategoryVo.class));
//        }
//        if (CollectionUtils.isNotEmpty(resumeDetail.getCityList())) {
//            userExpectJobVo.setCityList(AppUtils.asVoList(resumeDetail.getCityList(), CityVo.class));
//        }
//        if (resumeDetail.getSalary() != null) {
//            userExpectJobVo.setSalary(new DictVo().asVo(resumeDetail.getSalary()));
//        }
//        if (!StringUtils.isEmpty(resumeDetail.getExpectWorkType())) {
//            userExpectJobVo.setExpectWorkType(resumeDetail.getExpectWorkType());
//        }
        resumeVo.setUserExpectJob(resumeDetail.getUserExpectJobVo());
        //attach others
        List<ResumeMergeAttach> resumeMergeAttachList = resumeDetail.getResumeMergeAttachList();
        if (CollectionUtils.isNotEmpty(resumeMergeAttachList)) {
            List<ResumeMergeAttachVo> resumeMergeAttachVoList = AppUtils.asVoList(resumeMergeAttachList, ResumeMergeAttachVo.class);
            resumeVo.setResumeMergeAttachList(resumeMergeAttachVoList);
        }
        //language
        List<ResumeLanguage> resumeLanguageList = resumeDetail.getResumeLanguageList();
        if (CollectionUtils.isNotEmpty(resumeLanguageList)) {
            List<ResumeLanguageVo> resumeLanguageVoList = AppUtils.asVoList(resumeLanguageList, ResumeLanguageVo.class);
            resumeVo.setResumeLanguageList(resumeLanguageVoList);
        }
        //certificate
        List<ResumeCertificate> resumeCertificateList = resumeDetail.getResumeCertificateList();
        if (CollectionUtils.isNotEmpty(resumeCertificateList)) {
            List<ResumeCertificateVo> resumeCertificateVoList = AppUtils.asVoList(resumeCertificateList, ResumeCertificateVo.class);
            resumeVo.setResumeCertificateList(resumeCertificateVoList);
        }

        //?????????????????????
        resumeVo.setResumeCompleteProgress(AppUtils.calCompleteProgress(resumeVo));
        return resumeVo;
    }

    @Override
    public PageResult<ResumeVo> toResumeVo(PageResult<ResumeDetail> pageResult) {
        PageResult<ResumeVo> voPageResult = new PageResult<>();
        List<ResumeVo> resumeVoList = new ArrayList<>();
        for (ResumeDetail resumeDetail : pageResult.getList()) {
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
     * ????????????????????????
     *
     * @param resumeId
     * @param userId
     * @param resumeVo
     * @return
     */
    private int getApplyOfferCount(Long resumeId, Long userId, ResumeVo resumeVo) {
        JobApplyOptions applyOptions = new JobApplyOptions();
        applyOptions.setResumeId(resumeId);
        applyOptions.setUserId(userId);
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
        return jobApplyMapper.countJobApply(applyOptions);
    }
}
