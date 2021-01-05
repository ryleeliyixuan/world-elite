package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.worldelite.job.entity.*;
import com.worldelite.job.form.QuestionnaireAnswerForm;
import com.worldelite.job.form.RegistrationForm;
import com.worldelite.job.form.RegistrationListForm;
import com.worldelite.job.mapper.RegistrationMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 活动报名服务类
 */
@Service
public class RegistrationService extends BaseService{

    @Autowired
    private RegistrationMapper registrationMapper;

    @Autowired
    private QuestionnaireAnswerService questionnaireAnswerService;

    @Autowired
    private QuestionnaireTemplateService questionnaireTemplateService;

    @Autowired
    private ActivityService activityService;

    @Transactional
    public void addRegistration(RegistrationForm registrationForm){
        //保存基本信息
        Registration registration = new Registration();
        BeanUtil.copyProperties(registrationForm,registration);
        //报名者ID不存在，认为是当前用户进行报名
        if(registrationForm.getRegistrationUserId() == null){
            registration.setRegistrationUserId(curUser().getId());
        }
        //通过活动ID获取活动发布者ID
        //Todo
        //ActivityVo activity = activityService.getSimpleActivity(registrationForm.getActivityId());
        //registration.setUserId(activity.getUserId());
        registrationMapper.insertSelective(registration);
        //保存问卷回答
        QuestionnaireAnswerForm[] answerFormList = registrationForm.getAnswerList();
        if(answerFormList != null){
            for(int i=0;i<answerFormList.length;i++){
                questionnaireAnswerService.addQuestionnaireAnswer(answerFormList[i]);
            }
        }
    }

    /**
     * 通过ID查询活动报名详情
     * @param id
     * @return
     */
    public RegistrationVo getRegistrationDetail(Integer id){
        Registration registration = registrationMapper.selectByPrimaryKey(id);
        return new RegistrationVo().asVo(registration);
    }

    /**
     * 获取带模板信息的活动报名详情
     * @param id
     * @return
     */
    public RegistrationWithTemplateVo getRegistrationWithTemplateDetail(Integer id){
        RegistrationVo registration = getRegistrationDetail(id);
        QuestionnaireTemplateVo template = questionnaireTemplateService
                .getTemplateDetailByActivityId(registration.getActivityId());
        RegistrationWithTemplateVo registrationWithTemplateVo = new RegistrationWithTemplateVo();
        registrationWithTemplateVo.setRegistration(registration);
        registrationWithTemplateVo.setTemplate(template);
        return registrationWithTemplateVo;
    }

    /**
     * 分页获取活动对应的报名信息
     * 报名信息不包含问卷回答
     * @param activityId
     * @return
     */
    public PageResult<RegistrationVo> getRegistrationList(Integer activityId){
        Registration options = new Registration();
        options.setActivityId(activityId);
        List<Registration> registrationList = registrationMapper.selectAndList(options);
        PageResult<RegistrationVo> pageResult = new PageResult<>();
        pageResult.setList(AppUtils.asVoList(registrationList,RegistrationVo.class));
        return pageResult;
    }

    /**
     * 查询活动报名基本信息
     * @param registrationListForm
     * @return
     */
    public PageResult<RegistrationVo> getRegistrationList(RegistrationListForm registrationListForm){
        AppUtils.setPage(registrationListForm);
        RegistrationOptions options = new RegistrationOptions();
        BeanUtil.copyProperties(registrationListForm,options);
        Page<Registration> registrationList = (Page<Registration>) registrationMapper.selectAndList(options);
        PageResult<RegistrationVo> pageResult = new PageResult<>(registrationList);
        pageResult.setList(AppUtils.asVoList(registrationList,RegistrationVo.class));
        return pageResult;
    }
}
