package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.worldelite.job.constants.ActivityReportStatus;
import com.worldelite.job.entity.ActivityReport;
import com.worldelite.job.form.ActivityReportForm;
import com.worldelite.job.form.PageForm;
import com.worldelite.job.mapper.ActivityReportMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.ActivityReportVo;
import com.worldelite.job.vo.PageResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class ActivityReportService extends BaseService {
    private final ActivityReportMapper activityReportMapper;

    public PageResult<ActivityReportVo> getActivityReportList(ActivityReportForm activityReportForm, PageForm pageForm) {
        ActivityReport options = new ActivityReport();
        BeanUtil.copyProperties(activityReportForm, options);
        AppUtils.setPage(pageForm);
        Page<ActivityReport> activityReportPage = (Page<ActivityReport>) activityReportMapper.selectAndList(options);
        PageResult<ActivityReportVo> pageResult = new PageResult<>(activityReportPage);
        pageResult.setList(AppUtils.asVoList(activityReportPage, ActivityReportVo.class));
        return pageResult;
    }

    public ActivityReportVo getActivityReport(Integer id) {
        final ActivityReport activityReport = activityReportMapper.selectByPrimaryKey(id);
        if(activityReport == null) return null;

        return new ActivityReportVo().asVo(activityReport);
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

        if (activityReportForm.getReportUserId() == null)
            activityReport.setReportUserId(curUser().getId());

        return activityReportMapper.updateByPrimaryKeySelective(activityReport) == 1;
    }

    public Boolean delActivityReport(Integer id) {
        return activityReportMapper.deleteByPrimaryKey(id) == 1;
    }
}
