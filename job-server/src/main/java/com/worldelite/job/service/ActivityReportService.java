package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.excel.util.DateUtils;
import com.github.pagehelper.Page;
import com.worldelite.job.constants.ActivityReportStatus;
import com.worldelite.job.context.MessageResource;
import com.worldelite.job.entity.ActivityReport;
import com.worldelite.job.entity.ActivityReportExt;
import com.worldelite.job.entity.Message;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.ActivityReportForm;
import com.worldelite.job.form.ActivityReportListForm;
import com.worldelite.job.form.EmailForm;
import com.worldelite.job.mapper.ActivityReportMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.ActivityReportVo;
import com.worldelite.job.vo.PageResult;
import com.worldelite.job.vo.UserApplicantVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ActivityReportService extends BaseService {
    private final ActivityReportMapper activityReportMapper;
    private final DictService dictService;

    private final MessageService messageService;
    private final MessageResource messageSource;
    private final UserApplicantService userApplicantService;
    private final IEmailService emailService;

    public PageResult<ActivityReportVo> getActivityReportList(ActivityReportListForm reportListForm) {
        AppUtils.setPage(reportListForm);
        Page<ActivityReportExt> activityReportPage = (Page<ActivityReportExt>) activityReportMapper.selectAndList(reportListForm);
        PageResult<ActivityReportVo> pageResult = new PageResult<>(activityReportPage);

        List<ActivityReportVo> activityReportVos = new ArrayList<>(activityReportPage.size());
        for (ActivityReport report : activityReportPage) {
            activityReportVos.add(toActivityReportVo(report));
        }

        pageResult.setList(activityReportVos);
        return pageResult;
    }

    public ActivityReportVo getActivityReport(Integer id) {
        final ActivityReport activityReport = activityReportMapper.selectByPrimaryKey(id);
        if (activityReport == null) return null;

        return toActivityReportVo(activityReport);
    }

    private ActivityReportVo toActivityReportVo(ActivityReport activityReport) {
        ActivityReportVo reportVo = new ActivityReportVo().asVo(activityReport);
        reportVo.setOptionVo(dictService.getById(activityReport.getOptionId()));

        return reportVo;
    }

    public Boolean addActivityReport(ActivityReportForm activityReportForm) {
        ActivityReport activityReport = new ActivityReport();
        BeanUtil.copyProperties(activityReportForm, activityReport);

        if (activityReportForm.getReportUserId() == null)
            activityReport.setReportUserId(curUser().getId());

        activityReport.setStatus(ActivityReportStatus.REVIEWING.value);
        return activityReportMapper.insertSelective(activityReport) == 1;
    }

    public Boolean updateActivityReport(ActivityReportForm activityReportForm) {
        ActivityReport activityReport = new ActivityReport();
        BeanUtil.copyProperties(activityReportForm, activityReport);

        return activityReportMapper.updateByPrimaryKeySelective(activityReport) == 1;
    }

    public Boolean delActivityReport(Integer id) {
        return activityReportMapper.deleteByPrimaryKey(id) == 1;
    }

    /**
     * 驳回举报
     *
     * @param id
     * @param result
     * @return
     */
    public Boolean turnDownReport(Integer id, String result) {
        ActivityReportExt activityReport;

        ActivityReportListForm activityReportListForm = new ActivityReportListForm();
        activityReportListForm.setId(id);

        final List<ActivityReportExt> activityReportExts = activityReportMapper.selectAndList(activityReportListForm);
        if (activityReportExts.size() == 1) {
            activityReport = activityReportExts.get(0);
        }else{
            throw new ServiceException(message("activity.report.not.exists"));
        }

        if(activityReport.getStatus() != ActivityReportStatus.REVIEWING.value)
            throw new ServiceException(message("activity.report.turndown.failed"));

        String msg = messageSource.getMessage("activity.report.turndown", DateUtils.format(activityReport.getCreateTime()), activityReport.getTitle(), activityReport.getActivityId(), result);
        sendMsg(activityReport.getReportUserId(), msg, String.format("/activity/%s", activityReport.getActivityId()), 1);

        ActivityReport report = new ActivityReport();
        report.setId(id);
        report.setResult(result);
        report.setStatus(ActivityReportStatus.TURN_DOWN.value);
        return activityReportMapper.updateByPrimaryKeySelective(report) == 1;
    }

    private void sendMsg(Long userId, String msg, String url, int type) {
        Message message = new Message();
        message.setToUser(userId);
        message.setContent(msg);
        message.setUrl(url);
        message.setMsgType((byte) type);
        messageService.sendMessage(message);

        //邮件通知
        UserApplicantVo user = userApplicantService.getUserInfo(userId);
        if (user != null) {
            String email = user.getEmail();
            if (StringUtils.isNotEmpty(email)) {
                EmailForm emailForm = new EmailForm();
                emailForm.setAddress(email);
                emailForm.setSubject(msg);
                emailForm.setEmailBody(msg);
                emailService.sendEmail(emailForm);
            }
        }
    }
}
