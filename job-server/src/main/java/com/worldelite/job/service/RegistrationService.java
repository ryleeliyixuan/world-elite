package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.worldelite.job.constants.RegistrationStatus;
import com.worldelite.job.context.SpringContextHolder;
import com.worldelite.job.entity.Registration;
import com.worldelite.job.entity.RegistrationOptions;
import com.worldelite.job.event.ActivityRegistrationEvent;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.QuestionnaireAnswerForm;
import com.worldelite.job.form.RegistrationForm;
import com.worldelite.job.form.RegistrationListForm;
import com.worldelite.job.mapper.RegistrationMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 活动报名服务类
 */
@Slf4j
@Service
public class RegistrationService extends BaseService {

    @Autowired
    private RegistrationMapper registrationMapper;

    @Autowired
    private QuestionnaireAnswerService questionnaireAnswerService;

    @Autowired
    private ActivityQuestionnaireService activityQuestionnaireService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private DictService dictService;

    @Transactional
    public void addRegistration(RegistrationForm registrationForm) {
        //报名者ID不存在，认为是当前用户进行报名
        Long registrationUserId = registrationForm.getRegistrationUserId();
        if (registrationUserId == null) {
            registrationUserId = curUser().getId();
        }
        //判断报名是否已经存在
        Registration registration = registrationMapper.selectRegistrationStatusByUserId(registrationForm.getActivityId()
                , registrationUserId);
        if (registration != null) {
            throw new ServiceException(message("registration.exists"));
        }
        //保存基本信息
        registration = new Registration();
        BeanUtil.copyProperties(registrationForm, registration);
        registration.setRegistrationUserId(registrationUserId);
        //通过活动ID获取活动发布者ID
        ActivityVo activity = activityService.getActivityInfo(registrationForm.getActivityId());
        registration.setUserId(activity.getUserId());
        //获取报名编号
        Integer number = registrationMapper.selectNextRegistrationId(registration.getActivityId());
        if (number == null) {
            number = 1;
        }
        registration.setNumber(number);
        //如果报名需要审核，则状态改成待审核，否则状态为无需审核
        if (activity.getAuditType().equals("1")) {
            registration.setStatus(RegistrationStatus.DIRECT.value);
        }
        if (activity.getAuditType().equals("0")) {
            registration.setStatus(RegistrationStatus.NOT_ACTIVITY.value);
        }
        registrationMapper.insertSelective(registration);
        //保存问卷回答
        QuestionnaireAnswerForm[] answerFormList = registrationForm.getAnswerList();
        if (answerFormList != null) {
            for (int i = 0; i < answerFormList.length; i++) {
                answerFormList[i].setRegistrationId(registration.getId());
                questionnaireAnswerService.addQuestionnaireAnswer(answerFormList[i]);
            }
        }

        if (!activityService.increaseApplicant(registrationForm.getActivityId())) {
            throw new ServiceException(message("activity.applicant.failed"));
        }

        //无需审核的活动直接发送报名成功通知
        if (registration.getStatus() == RegistrationStatus.DIRECT.value)
            SpringContextHolder.publishEvent(new ActivityRegistrationEvent(this, registration.getId(), RegistrationStatus.PASS.value));
    }

    /**
     * 通过ID查询活动报名详情
     *
     * @param id
     * @return
     */
    public RegistrationVo getRegistrationDetail(Integer id) {
        Registration registration = registrationMapper.selectByPrimaryKey(id);
        if (registration == null) {
            throw new ServiceException(message("registration.not.exists"));
        }
        List<QuestionnaireAnswerVo> answerList = questionnaireAnswerService.getAnswerListByRegistrationId(id);
        RegistrationVo registrationVo = new RegistrationVo().asVo(registration);
        registrationVo.setEducation(dictService.getById(registration.getEducationId()));
        registrationVo.setAnswerList(answerList);
        return registrationVo;
    }

    /**
     * 获取带报名表的活动报名详情
     *
     * @param id
     * @return
     */
    public QuestionnaireTemplateWithAnswerVo getRegistrationWithTemplateDetail(Integer id) {
        RegistrationVo registration = getRegistrationDetail(id);
        QuestionnaireTemplateVo template = activityQuestionnaireService
                .getTemplateDetailByActivityId(registration.getActivityId());
        QuestionnaireTemplateWithAnswerVo registrationVo = new QuestionnaireTemplateWithAnswerVo();
        BeanUtil.copyProperties(registration, registrationVo);
        BeanUtil.copyProperties(template, registrationVo, "questionnaireList", "id");
        registrationVo.setQuestionnaireList(getQuestionnaireList(template.getQuestionnaireList()
                , registration.getAnswerList()));
        return registrationVo;
    }

    private List<QuestionnaireWithAnswerVo> getQuestionnaireList(List<QuestionnaireVo> templateList,
                                                                 List<QuestionnaireAnswerVo> answerList) {
        List<QuestionnaireWithAnswerVo> questionnaireList = new ArrayList<>(templateList.size());
        //因为问卷有可能不是必答，问卷和回答不一定一一对应
        //通过map做一次问卷和回答的对应
        Map<Integer, List<String>> answerContentMap = new HashMap<>();
        Map<Integer, List<QuestionnaireOptionsVo>> answerOptionsMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(answerList)) {
            for (QuestionnaireAnswerVo answer : answerList) {
                Integer questionnaireId = answer.getQuestionnaireId();
                if (answerContentMap.get(questionnaireId) == null) {
                    answerContentMap.put(questionnaireId, new ArrayList<>());
                }
                if (answerOptionsMap.get(questionnaireId) == null) {
                    answerOptionsMap.put(questionnaireId, new ArrayList<>());
                }
                if (StringUtils.isNotEmpty(answer.getAnswerContent())) {
                    answerContentMap.get(questionnaireId).add(answer.getAnswerContent());
                }
                if (answer.getAnswerOptions() != null) {
                    answerOptionsMap.get(questionnaireId).add(answer.getAnswerOptions());
                }
                log.debug("问卷{}回答内容数：{}", questionnaireId, answerContentMap.get(questionnaireId).size());
                log.debug("问卷{}回答选项数：{}", questionnaireId, answerOptionsMap.get(questionnaireId).size());
            }
        }
        for (QuestionnaireVo template : templateList) {
            QuestionnaireWithAnswerVo questionnaire = new QuestionnaireWithAnswerVo();
            questionnaire.setTitle(template.getTitle());
            questionnaire.setMustAnswer(template.getMustAnswer());
            questionnaire.setType(template.getType());
            questionnaire.setSort(template.getSort());
            log.debug("获取活动问卷，ID：{}", template.getId());
            log.debug("获取活动问卷回答内容：{}", answerContentMap.get(template.getId()));
            log.debug("获取活动问卷回答选项：{}", answerOptionsMap.get(template.getId()));
            questionnaire.setAnswerContent(answerContentMap.get(template.getId()));
            questionnaire.setAnswerOptions(answerOptionsMap.get(template.getId()));
            questionnaire.setQuestionnaireOptions(template.getOptionsList());
            questionnaireList.add(questionnaire);
        }
        return questionnaireList;
    }

    /**
     * 查询活动报名基本信息
     *
     * @param registrationListForm
     * @return
     */
    public PageResult<RegistrationVo> getRegistrationList(RegistrationListForm registrationListForm) {
        AppUtils.setPage(registrationListForm);
        RegistrationOptions options = new RegistrationOptions();
        BeanUtil.copyProperties(registrationListForm, options);
        log.debug("查询报名信息，活动ID：{}", options.getActivityId());
        Page<Registration> registrationList = (Page<Registration>) registrationMapper.selectAndList(options);
        PageResult<RegistrationVo> pageResult = new PageResult<>(registrationList);
        List<RegistrationVo> registrationVoList = new ArrayList<>(registrationList.size());
        for (Registration registration : registrationList) {
            RegistrationVo registrationVo = new RegistrationVo().asVo(registration);
            registrationVo.setEducation(dictService.getById(registration.getEducationId()));
            registrationVoList.add(registrationVo);
        }
        pageResult.setList(registrationVoList);
        return pageResult;
    }

    /**
     * 通过活动报名
     *
     * @param id
     */
    public void setRegistrationPass(Integer id) {
        Registration registration = registrationMapper.selectByPrimaryKey(id);
        registration.setStatus(RegistrationStatus.PASS.value);
        registrationMapper.updateByPrimaryKeySelective(registration);

        SpringContextHolder.publishEvent(new ActivityRegistrationEvent(this, id, RegistrationStatus.PASS.value));
    }

    /**
     * 活动报名不合适
     *
     * @param id
     */
    public void setRegistrationInappropriate(Integer id) {
        Registration registration = registrationMapper.selectByPrimaryKey(id);
        registration.setStatus(RegistrationStatus.INAPPROPRIATE.value);
        registrationMapper.updateByPrimaryKeySelective(registration);

        //释放一个活动报名名额
        if (!activityService.minusApplicant(registration.getActivityId())) {
            throw new ServiceException(message("activity.release.quota.failed"));
        }

        SpringContextHolder.publishEvent(new ActivityRegistrationEvent(this, id, RegistrationStatus.INAPPROPRIATE.value));
    }
}
