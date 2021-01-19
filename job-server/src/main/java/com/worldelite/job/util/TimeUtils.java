package com.worldelite.job.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
public class TimeUtils {

    /**
     *
     * 根据生日计算当前周岁数
     *
     */
    public static int getCurrentAge(Date birthday) {
        // 当前时间
        Calendar curr = Calendar.getInstance();
        // 生日
        Calendar born = Calendar.getInstance();
        born.setTime(birthday);
        // 年龄 = 当前年 - 出生年
        int age = curr.get(Calendar.YEAR) - born.get(Calendar.YEAR);
        if (age <= 0) {
            return 0;
        }
        // 如果当前月份小于出生月份: age-1
        // 如果当前月份等于出生月份, 且当前日小于出生日: age-1
        int currMonth = curr.get(Calendar.MONTH);
        int currDay = curr.get(Calendar.DAY_OF_MONTH);
        int bornMonth = born.get(Calendar.MONTH);
        int bornDay = born.get(Calendar.DAY_OF_MONTH);
        if ((currMonth < bornMonth) || (currMonth == bornMonth && currDay <= bornDay)) {
            age--;
        }
        return Math.max(age, 0);
    }

    /**
     * 获取今天的最后一秒
     *
     * @param date
     * @return
     */
    public static Date finalSecOfTheDay(Date date){
        if(date == null){
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }


    /**
     * 获取几天前的日期
     *
     * @param days
     * @param format
     * @return
     */
    public static String getBeforeDate(Integer days, String format) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_MONTH,-days);
        return new SimpleDateFormat(format).format(cal.getTime());
    }


    /**
     * 获取当前日期
     *
     * @param format
     * @return
     */
    public static String getDateNow(String format) {
        return new SimpleDateFormat(format).format(new Date());
    }


    /**
     * 获取日期集合
     *
     * @param dStart
     * @param dEnd
     * @return
     */
    public static List<String> getDateList(String dStart, String dEnd, String format) {
        List<String> dateList = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date d1 = sdf.parse(dStart);
            Date d2 = sdf.parse(dEnd);
            Calendar cStart = Calendar.getInstance();
            cStart.setTime(d1);

            dateList = new ArrayList<>();
            dateList.add(dStart);
            while (d2.after(cStart.getTime())) {
                if (format.contains("d")){
                    cStart.add(Calendar.DAY_OF_MONTH, 1);
                } else {
                    cStart.add(Calendar.MONTH, 1);
                }
                dateList.add(sdf.format(cStart.getTime()));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateList;
    }


    /**
     * 获取某个月有多少天
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static Integer getDaysOfMonth(String date){
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new SimpleDateFormat("yyyy-MM").parse(date));
            return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
