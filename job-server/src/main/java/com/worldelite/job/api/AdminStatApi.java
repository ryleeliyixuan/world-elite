package com.worldelite.job.api;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.controller.BaseController;
import com.worldelite.job.service.UserStatInfoService;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.StatDateVo;
import com.worldelite.job.vo.StatUserVo;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @Author twz
 * @Date 2020/12/2 0002
 * @Desc 管理员获取用户统计数据接口
 */
@RestController
@RequestMapping("/api/admin/stat")
@AllArgsConstructor
public class AdminStatApi extends BaseController {

    private UserStatInfoService userStatInfoService;

    /**
     * 获取最近7天统计数据
     *
     * @return
     */
    @GetMapping("get-week-user-stat")
    @RequireLogin
    public ApiResult<StatUserVo> getWeekUserStat() {
        StatUserVo weekUserStat = userStatInfoService.getWeekUserStat();
        return ApiResult.ok(weekUserStat);
    }


    /**
     * 获取最近30天统计数据
     *
     * @return
     */
    @GetMapping("get-month-user-stat")
    @RequireLogin
    public ApiResult<StatUserVo> getMonthUserStat() {
        StatUserVo userStatVo = userStatInfoService.getMonthUserStat();
        return ApiResult.ok(userStatVo);
    }


    /**
     * 获取指定月份统计数据
     *
     * @param date 指定日期
     * @return
     */
    @GetMapping("get-specify-month-user-stat")
    @RequireLogin
    public ApiResult<StatUserVo> getSpecifyMonthUserStat(@DateTimeFormat(pattern = "yyyy/MM") Date date) {
        StatUserVo userStatVo = userStatInfoService.getSpecifyMonthUserStat(date);
        return ApiResult.ok(userStatVo);
    }


    /**
     * 获取指定年份统计数据
     *
     * @param date 指定日期
     * @return
     */
    @GetMapping("get-specify-year-user-stat")
    @RequireLogin
    public ApiResult<StatUserVo> getSpecifyYearUserStat(@DateTimeFormat(pattern = "yyyy") Date date) {
        StatUserVo statUserVo = userStatInfoService.getSpecifyYearUserStat(date);
        return ApiResult.ok(statUserVo);
    }


    /**
     * 获取日期列表
     *
     * @return
     */
    @GetMapping("get-date-list")
    public ApiResult<StatDateVo> getDateList() {
        StatDateVo statDateVo = userStatInfoService.getDateList();
        return ApiResult.ok(statDateVo);
    }


}
