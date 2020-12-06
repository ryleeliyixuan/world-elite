package com.worldelite.job.service;

import com.worldelite.job.constants.StatInfoType;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.vo.StatDateVo;
import com.worldelite.job.vo.StatUserVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author twz
 * @Date 2020/12/1 0001
 * @Desc 统计用户数据service
 */
@AllArgsConstructor
@Service
public class UserStatInfoService extends BaseService {

    private final UserApplicantService userApplicantService;
    private final UserCorporateService userCorporateService;


    /**
     * 获取最近7天统计数据
     *
     * @return
     */
    public StatUserVo getWeekUserStat() {
        List<Long> studentStat = userApplicantService.selectStatInfo(StatInfoType.WEEK_DAYS.value); // 7天学生用户数据
        List<Long> companyStat = userCorporateService.selectStatInfo(StatInfoType.WEEK_DAYS.value); // 7天企业用户数据
        return getUserStatBase(studentStat, companyStat);
    }


    /**
     * 获取近三十天统计数据
     *
     * @return
     */
    public StatUserVo getMonthUserStat() {
        List<Long> studentStat = userApplicantService.selectStatInfo(StatInfoType.MONTH_DAYS.value); // 30天学生用户数据
        List<Long> companyStat = userCorporateService.selectStatInfo(StatInfoType.MONTH_DAYS.value); // 30天企业用户数据
        return getUserStatBase(studentStat, companyStat);
    }


    /**
     * 获取指定月份的统计数据
     *
     * @param date 日期
     * @return
     */
    public StatUserVo getSpecifyMonthUserStat(Date date) {
        String dateStr = new SimpleDateFormat("yyyy-MM").format(date);
        List<Long> studentStat = userApplicantService.selectSpecifyMonthStatInfo(dateStr);
        List<Long> companyStat = userCorporateService.selectSpecifyMonthStatInfo(dateStr);
        return getUserStatBase(studentStat, companyStat);
    }


    /**
     * 获取指定年份统计数据
     *
     * @param date 日期
     * @return
     */
    public StatUserVo getSpecifyYearUserStat(Date date) {
        String dateStr = new SimpleDateFormat("yyyy").format(date);
        List<Long> studentStat = userApplicantService.selectSpecifyYearStatInfo(dateStr);
        List<Long> companyStat = userCorporateService.selectSpecifyYearStatInfo(dateStr);
        return getSpecifyYearUserStat(studentStat, companyStat);
    }


    /**
     * 获取日期列表
     *
     * @return
     */
    public StatDateVo getDateList() {
        Date firstUserTime = userApplicantService.selectFirstCreateTime();
        Date firstCorporateTime = userCorporateService.selectFirstCreateTime();
        String start = new SimpleDateFormat("yyyy/MM").format(getMinTime(firstUserTime, firstCorporateTime));
        return getDateList(start);
    }


    /**
     * 获取指定年份统计数据
     *
     * @param studentStat 学生用户数
     * @param companyStat 企业用户数
     * @return
     */
    private StatUserVo getSpecifyYearUserStat(List<Long> studentStat, List<Long> companyStat) {
        List<Long> userTotalStat = getUserTotalStat(studentStat, companyStat); // n月总用户数据

        List<Integer> totalMonthGrowthNumStat = getGrowthNumStat(userTotalStat); // 总月增数
        List<Integer> studentMonthGrowthNumStat = getGrowthNumStat(studentStat); // 学生月增数
        List<Integer> companyMonthGrowthNumsStat = getGrowthNumStat(companyStat); // 企业月增数

        List<BigDecimal> totalMonthGrowthStat = getGrowthStat(userTotalStat); // 总月增率
        List<BigDecimal> studentMonthGrowthStat = getGrowthStat(studentStat); // 学生月增率
        List<BigDecimal> companyMonthGrowthStat = getGrowthStat(studentStat); // 企业月增率

        return StatUserVo.builder()
                .userTotalNums(userTotalStat)
                .studentNums(studentStat)
                .companyNums(companyStat)
                .totalMonthGrowthNums(totalMonthGrowthNumStat)
                .studentMonthGrowthNums(studentMonthGrowthNumStat)
                .companyMonthGrowthNums(companyMonthGrowthNumsStat)
                .totalMonthGrowths(totalMonthGrowthStat)
                .studentMonthGrowths(studentMonthGrowthStat)
                .companyMonthGrowths(companyMonthGrowthStat)
                .build();
    }


    /**
     * 获取最早时间
     *
     * @param firstUserTime 用户最早注册时间
     * @param firstCorporateTime 企业最早注册时间
     * @return 总最早注册时间
     */
    private Date getMinTime(Date firstUserTime, Date firstCorporateTime) {
        return firstUserTime.before(firstCorporateTime) ? firstUserTime : firstCorporateTime;
    }


    /**
     * 获取日期列表
     *
     * @param start 最早注册时间
     * @return
     */
    private StatDateVo getDateList(String start) {
        return StatDateVo.builder()
                .yearDates(getYearDateList(start))
                .monthDates(getMonthDateList(start))
                .build();
    }


    /**
     * 获取start - new Date().now()之间所有的月份
     *
     * @param start 开始时间
     * @return 倒序返回
     */
    private List<String> getMonthDateList(String start) {
        return getDateListBase(start, "yyyy/MM");
    }


    /**
     * 获取start - new Date().now()之间所有的年份
     *
     * @param start 开始时间
     * @return 倒序返回
     */
    private List<String> getYearDateList(String start) {
        return getDateListBase(start, "yyyy");
    }


    /**
     * 获取start - new Date().now()之间所有的日期
     *
     * @param start 开始时间
     * @param pattern SimpleDateFormat pattern
     * @return
     */
    private List<String> getDateListBase(String start, String pattern) {
        List<String> result = new ArrayList<>();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            Date end = new Date(System.currentTimeMillis()); // 当前时间
            Calendar c1 = Calendar.getInstance();
            c1.setTime(sdf.parse(start));

            Calendar c2 = Calendar.getInstance();
            c2.setTime(end);
            while(c1.before(c2) || c1.equals(c2)) {
                result.add(sdf.format(c1.getTime()));
                if ("yyyy/MM".equals(pattern))
                    c1.add(Calendar.MONTH, 1);
                else
                    c1.add(Calendar.YEAR, 1);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        Collections.reverse(result);
        return result;
    }


    /**
     * 获取时间内统计数据
     *
     * @param studentStat
     * @param companyStat
     * @return
     */
    private StatUserVo getUserStatBase(List<Long> studentStat, List<Long> companyStat) {
        List<Long> userTotalStat = getUserTotalStat(studentStat, companyStat); // n天总用户数据

        List<Integer> totalDayGrowthNumStat = getGrowthNumStat(userTotalStat); // 总日增数
        List<Integer> studentDayGrowthNumStat = getGrowthNumStat(studentStat); // 学生日增数
        List<Integer> companyDayGrowthNumsStat = getGrowthNumStat(companyStat); // 企业日增数

        List<BigDecimal> totalDayGrowthStat = getGrowthStat(userTotalStat); // 总日增率
        List<BigDecimal> studentDayGrowthStat = getGrowthStat(studentStat); // 学生日增率
        List<BigDecimal> companyDayGrowthStat = getGrowthStat(studentStat); // 企业日增率

        return StatUserVo.builder()
                .userTotalNums(studentStat)
                .userTotalNums(userTotalStat)
                .companyNums(companyStat)
                .totalDayGrowthNums(totalDayGrowthNumStat)
                .studentDayGrowthNums(studentDayGrowthNumStat)
                .companyDayGrowthNums(companyDayGrowthNumsStat)
                .totalDayGrowths(totalDayGrowthStat)
                .studentDayGrowths(studentDayGrowthStat)
                .companyDayGrowths(companyDayGrowthStat)
                .build();
    }



    /**
     * 获取日/月增数
     *
     * @param stat 数据集合
     * @return
     */
    private List<Integer> getGrowthNumStat(List<Long> stat) {
        if (stat == null) return null;

        List<Integer> totalGrowthNumStat = new ArrayList<>();
        for (int i = 0; i < stat.size() - 1; i++) {
            int j = i + 1;
            totalGrowthNumStat.add(Integer.parseInt(stat.get(j) - stat.get(i) + ""));
        }
        return totalGrowthNumStat;
    }



    /**
     * 获取总用户数据
     *
     * @param studentStat 学生用户数据
     * @param companyStat 企业用户数据
     * @return 总用户数据
     */
    private List<Long> getUserTotalStat(List<Long> studentStat, List<Long> companyStat) {
        if (studentStat == null || companyStat == null) {
            return null;
        }
        if (studentStat.size() != companyStat.size()) {
            throw new ServiceException(message("api.error.data.date"));
        }

        List<Long> userTotalStat = new ArrayList<>();
        for (int i = 0; i < studentStat.size(); i++) {
            userTotalStat.add(studentStat.get(i) + companyStat.get(i));
        }
        return userTotalStat;
    }



    /**
     * 获取近七日增长率
     *
     * @param userTotalStat
     * @return
     */
    @Deprecated
    private List<BigDecimal> getWeekGrowthStat(List<Long> userTotalStat) {
        if (userTotalStat == null) return null;

        int size;
        if ((size = userTotalStat.size()) == 7) {
            BigDecimal value = BigDecimal.valueOf((userTotalStat.get(size - 1) - userTotalStat.get(0)) /
                    Double.parseDouble(userTotalStat.get(0).toString()))
                    .setScale(2, BigDecimal.ROUND_HALF_UP);
            return Collections.singletonList(value);
        }

        List<BigDecimal> weekGrowthStat = new ArrayList<>();
        for (int i = 0; i < userTotalStat.size() - 7; i += 7) {
            int j = i + 7;
            if (userTotalStat.get(i) == 0) {
                weekGrowthStat.add(null);
            } else {
                BigDecimal value = BigDecimal.valueOf((userTotalStat.get(j) - userTotalStat.get(i)) /
                        Double.parseDouble(userTotalStat.get(i).toString()))
                        .setScale(2, BigDecimal.ROUND_HALF_UP);
                weekGrowthStat.add(value);
            }
        }
        return weekGrowthStat;
    }



    /**
     * 获取日/月增率
     *
     * @param stat  数据集合
     * @return
     */
    private List<BigDecimal> getGrowthStat(List<Long> stat) {
        if (stat == null) return null;

        List<BigDecimal> growthStat = new ArrayList<>();
        for (int i = 0; i < stat.size() - 1; i++) {
            int j = i + 1;
            if (stat.get(i) == 0) {
                growthStat.add(null);
            } else {
                BigDecimal value = BigDecimal.valueOf((stat.get(j) - stat.get(i)) /
                        Double.parseDouble(stat.get(i).toString()))
                        .setScale(2, BigDecimal.ROUND_HALF_UP);
                growthStat.add(value);
            }
        }
        return growthStat;
    }
}
