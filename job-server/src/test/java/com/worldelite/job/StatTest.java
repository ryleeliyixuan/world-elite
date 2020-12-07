package com.worldelite.job;

import com.worldelite.job.mapper.UserApplicantMapper;
import com.worldelite.job.service.UserStatInfoService;
import com.worldelite.job.vo.StatUserVo;
import freemarker.template.SimpleDate;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author twz
 * @Date 2020/12/1 0001
 * @Desc TODO
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StatTest {

    @Autowired
    private UserApplicantMapper userApplicantMapper;
    @Autowired
    private UserStatInfoService userStatInfoService;



    @Test
    public void testLocalDate() throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date d1 = sdf.parse("2020-11");
        Date d2 = sdf.parse("2020-11");
        System.out.println("d1.compareTo(d2) = " + d1.compareTo(d2));
    }


    @Test
    public void testDate() {
        System.out.println("getTime() = " + new Date("Tue Feb 04 19:08:12 CST 2020").getYear());
    }

    @Test
    public void testBetween() throws Exception {
        List<String> result = new ArrayList<>();
        String pattern = "yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date end = new Date(System.currentTimeMillis()); // 当前时间
        Calendar c1 = Calendar.getInstance();
//        c1.setTime(sdf.parse("2019"));
//        c1.setTime(new Date("Tue Feb 04 19:08:12 CST 2019"));

        Calendar c2 = Calendar.getInstance();
        c2.setTime(end);
        while(c1.before(c2) || c1.equals(c2)) {
            result.add(sdf.format(c1.getTime()));
            if (pattern.equals("yyyy/MM"))
                c1.add(Calendar.MONTH, 1);
            else
                c1.add(Calendar.YEAR, 1);
        }
        System.out.println("result = " + result);
    }


    @Test
    public void testGetDaysOfMonth() throws Exception {
        Date date = new SimpleDateFormat("yyyy/MM").parse("2020/12");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int count = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        System.out.println("count = " + count);
    }

    @Test
    public void testselectSpecifyMonthStatInfo() throws Exception{
        Date date = new SimpleDateFormat("yyyy/MM").parse("2020/11");
        System.out.println("format(date) = " + new SimpleDateFormat("yyyy/MM").format(date));
        List<Map<String, Object>> maps = userApplicantMapper.selectSpecifyMonthStatInfo(new SimpleDateFormat("yyyy/MM").format(date));
        System.out.println("maps = " + maps);
    }

    @Test
    public void testGetWeekUserStat() {
        StatUserVo vo = userStatInfoService.getWeekUserStat();
        System.out.println("vo = " + vo);
    }


    /*@Test
    public void testGetDayGrowthStat() {
        List<BigDecimal> dayGrowthStat = userStatInfoService.getDayGrowthStat(Arrays.asList(10L, 24L, 16L, 32L));
        System.out.println("dayGrowthStat = " + dayGrowthStat);
    }


    @Test
    public void testGetWeekGrowthStat() {
        List<BigDecimal> weekGrowthStat = userStatInfoService.getWeekGrowthStat(Arrays.asList(10L, 10L, 24L, 16L, 32L, 42L, 33L, 60L, 133L, 90L, 76L, 46L, 55L, 46L, 55L));
        System.out.println("weekGrowthStat = " + weekGrowthStat);
    }






    @Test
    public void test() {
        List<Map<String, Object>> maps = userApplicantMapper.selectStatInfo(30);
        List<Long> monthStats = new ArrayList<>();
        System.out.println("maps = " + maps);

        Date end = new Date(System.currentTimeMillis()); // 当前时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(end);
//        calendar.add(Calendar.MONTH, -1); // 一个月前
        calendar.add(Calendar.DAY_OF_YEAR, -30); // 7天前
        Date start = calendar.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println("start = " + sdf.format(start));
        System.out.println("end = " + sdf.format(end));

        Date indexDate = null;
        for (int i = 0; (indexDate = calendar.getTime()).before(end) ;) {
            LocalDate l1 = new LocalDate(new DateTime(indexDate));
            LocalDate l2 = new LocalDate(new DateTime(maps.get(i).get("create_time")));

            if (l1.equals(l2)) { // 相等
                monthStats.add((Long) maps.get(i).get("count"));
                if (i < maps.size() - 1) i++;
            } else { // 不相等
                monthStats.add(0L);
            }

            calendar.add(Calendar.DAY_OF_YEAR, 1); // start + 1天
        }

        System.out.println("monthStats = " + monthStats);
    }*/


}
