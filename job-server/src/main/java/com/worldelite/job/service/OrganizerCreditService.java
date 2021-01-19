package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import com.worldelite.job.constants.ActivityReportStatus;
import com.worldelite.job.constants.ActivityStatus;
import com.worldelite.job.constants.OrganizerCreditGrade;
import com.worldelite.job.constants.OrganizerType;
import com.worldelite.job.entity.*;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.ActivityReportListForm;
import com.worldelite.job.form.OrganizerCreditForm;
import com.worldelite.job.mapper.ActivityMapper;
import com.worldelite.job.mapper.ActivityReportMapper;
import com.worldelite.job.mapper.OrganizerCreditMapper;
import com.worldelite.job.mapper.OrganizerInfoMapper;
import com.worldelite.job.vo.OrganizerCreditVo;
import com.worldelite.job.vo.PageResult;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 举办方信用等级
 *
 * @author Xiang Chao
 **/
@Service
@AllArgsConstructor
public class OrganizerCreditService extends BaseService {
    private final OrganizerCreditMapper organizerCreditMapper;
    private final ActivityMapper activityMapper;
    private final ActivityReportMapper activityReportMapper;
    private final OrganizerInfoMapper organizerInfoMapper;

    public PageResult<OrganizerCreditVo> getOrganizerCreditList(OrganizerCreditForm organizerCreditForm) {
        ActivityOptions options = new ActivityOptions();
        BeanUtil.copyProperties(organizerCreditForm, options);

        List<OrganizerCreditVo> creditVos = new ArrayList<>();

        final List<Activity> list = activityMapper.selectAndListForAdmin(options);

        final Map<Long, List<Activity>> collect = list.stream().collect(Collectors.groupingBy(Activity::getUserId));
        collect.values().forEach(activities -> creditVos.add(getOrganizerCredit(activities)));

        if (creditVos.size() == 0) return null;

        List<OrganizerCreditVo> resultVo = creditVos;
        if (organizerCreditForm.getCredit() != null) {
            resultVo = creditVos.stream().filter(organizerCreditVo -> organizerCreditVo.getCredit().equals(organizerCreditForm.getCredit())).collect(Collectors.toList());
        }

        PageResult<OrganizerCreditVo> pageResult = new PageResult<>();
        pageResult.setTotal(resultVo.size());
        pageResult.setCurrentPage(organizerCreditForm.getPage());
        pageResult.setPageSize(organizerCreditForm.getLimit());
        pageResult.calPageCountAndHasMore(resultVo);

        int from;
        if (organizerCreditForm.getPage() > 1)
            from = (organizerCreditForm.getPage() - 1) * organizerCreditForm.getLimit();
        else
            from = 0;

        pageResult.setList(resultVo.stream().sorted(Comparator.comparingLong(OrganizerCreditVo::getUserId)).skip(from).limit(organizerCreditForm.getLimit()).collect(Collectors.toList()));
        return pageResult;
    }

    public OrganizerCreditVo getOrganizerCredit(Long userId) {
        if (userId == null)
            throw new ServiceException(message(""));

        ActivityOptions options = new ActivityOptions();
        options.setUserId(userId);
        final List<Activity> list = activityMapper.selectAndListForAdmin(options);
        return getOrganizerCredit(list);
    }

    /**
     * 统计某用户的全部活动的信用等级信息
     *
     * @param list
     * @return
     */
    public OrganizerCreditVo getOrganizerCredit(List<Activity> list) {

        final int total = list.size();
        //已通过,未通过,已结束(计算举办成功率), 举报量
        AtomicInteger passedCount = new AtomicInteger();
        AtomicInteger notPassedCount = new AtomicInteger();
        AtomicInteger endCount = new AtomicInteger();
        AtomicInteger reportCount = new AtomicInteger();

        StringBuilder organizerNames = new StringBuilder();
        StringBuilder organizerTypes = new StringBuilder();

        HashSet<String> organizerTypeSet = new HashSet<>();
        HashSet<String> organizerNameSet = new HashSet<>();

        list.forEach(activity -> {
            if (activity.getStatus() == ActivityStatus.REVIEWING.value || activity.getStatus() == ActivityStatus.REVIEW_FAILURE.value) {
                notPassedCount.getAndIncrement();
            } else {
                passedCount.getAndIncrement();
            }

            if (activity.getStatus() == ActivityStatus.END.value)
                endCount.getAndIncrement();

            ActivityReportListForm report = new ActivityReportListForm();
            report.setActivityId(activity.getId());
            report.setStatus(ActivityReportStatus.OFFLINE.value);
            final List<ActivityReportExt> activityReports = activityReportMapper.selectAndList(report);
            if (activityReports.size() != 0)
                reportCount.getAndIncrement();

            if (Byte.parseByte(activity.getOrganizerType()) == OrganizerType.SCHOOL.value) {
                organizerTypeSet.add("学生组织/");
            } else if (Byte.parseByte(activity.getOrganizerType()) == OrganizerType.BUSINESS.value) {
                organizerTypeSet.add("社会组织/");
            } else if (Byte.parseByte(activity.getOrganizerType()) == OrganizerType.PERSONAL.value) {
                organizerTypeSet.add("个人/");
            } else if (Byte.parseByte(activity.getOrganizerType()) == OrganizerType.COMPANY.value) {
                organizerTypeSet.add("企业/");
            }

            final OrganizerInfo organizerInfo = organizerInfoMapper.selectByPrimaryKey(activity.getOrganizerId());
            if (organizerInfo != null) organizerNameSet.add(organizerInfo.getOrganizerName() + "/");
        });

        organizerTypeSet.forEach(organizerTypes::append);
        organizerTypes.deleteCharAt(organizerTypes.length() - 1);

        organizerNameSet.forEach(organizerNames::append);
        organizerNames.deleteCharAt(organizerNames.length() - 1);

        Long userId = list.size() > 0 ? list.get(0).getUserId() : null;
        //构造vo
        OrganizerCreditVo creditVo = new OrganizerCreditVo();
        creditVo.setUserId(userId);
        creditVo.setOrganizerName(organizerNames.toString());
        creditVo.setOrganizerType(organizerTypes.toString());

        final OrganizerCredit organizerCredit = organizerCreditMapper.selectByPrimaryKey(userId);
        if (organizerCredit != null) creditVo.setCredit(organizerCredit.getCredit());

        creditVo.setTotalActivity(total);
        creditVo.setPassedActivity(passedCount.get());
        creditVo.setNotPassActivity(notPassedCount.get());
        creditVo.setReportActivity(reportCount.get());
        if (total != 0) {
            creditVo.setHoldRate(endCount.get() / ((float) total));
            creditVo.setReportRate(reportCount.get() / ((float) total));
        }

        if (creditVo.getCredit() == null) {
            if (creditVo.getHoldRate() > 0.9 && creditVo.getReportRate() < 0.3) {
                creditVo.setCredit(OrganizerCreditGrade.LEVEL1.value);
            }
            if ((creditVo.getHoldRate() > 0.6 && creditVo.getHoldRate() < 0.9) && creditVo.getReportRate() < 0.3) {
                creditVo.setCredit(OrganizerCreditGrade.LEVEL2.value);
            }

            if (creditVo.getHoldRate() < 0.6 || creditVo.getReportRate() > 0.3) {
                creditVo.setCredit(OrganizerCreditGrade.LEVEL3.value);
            }
        }

        return creditVo;
    }

    public Boolean addOrUpdateOrganizerCredit(Long userId, Byte credit) {
        if (userId == null)
            throw new ServiceException(message("用户ID不能为空"));
        final OrganizerCreditGrade organizerCreditGrade = OrganizerCreditGrade.valueOf(credit);
        if (organizerCreditGrade == null)
            throw new ServiceException("信用等级值不正确");

        OrganizerCredit organizerCredit = new OrganizerCredit();
        organizerCredit.setUserId(userId);
        organizerCredit.setCredit(organizerCreditGrade.value);

        final OrganizerCredit oCredit = organizerCreditMapper.selectByPrimaryKey(userId);
        if(oCredit == null){
            return organizerCreditMapper.insertSelective(organizerCredit) == 1;
        }else{
            return organizerCreditMapper.updateByPrimaryKeySelective(organizerCredit) == 1;
        }
    }

    public Boolean delOrganizerCredit(Long userId) {
        if (userId == null)
            throw new ServiceException(message(""));

        return organizerCreditMapper.deleteByPrimaryKey(userId) == 1;
    }
}
