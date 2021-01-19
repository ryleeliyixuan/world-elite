package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.worldelite.job.constants.ActivityReportStatus;
import com.worldelite.job.entity.ActivityReport;
import com.worldelite.job.entity.ActivityReportExt;
import com.worldelite.job.form.ActivityReportForm;
import com.worldelite.job.form.ActivityReportListForm;
import com.worldelite.job.form.PageForm;
import com.worldelite.job.mapper.ActivityReportMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.ActivityReportVo;
import com.worldelite.job.vo.PageResult;
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

        if (activityReportForm.getReportUserId() == null)
            activityReport.setReportUserId(curUser().getId());

        return activityReportMapper.updateByPrimaryKeySelective(activityReport) == 1;
    }

    public Boolean delActivityReport(Integer id) {
        return activityReportMapper.deleteByPrimaryKey(id) == 1;
    }
}
