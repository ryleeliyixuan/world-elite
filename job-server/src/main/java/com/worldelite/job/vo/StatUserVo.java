package com.worldelite.job.vo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author twz
 * @Date 2020/12/1 0001
 * @Desc 用户统计数据
 */
@Data
@Builder
public class StatUserVo {

    /*用户数*/
    /**
     * 用户总数
     */
    private List<Long> userTotalNums;

    /**
     * 学生用户数
     */
    private List<Long> studentNums;

    /**
     * 企业用户数
     */
    private List<Long> companyNums;


    /*日增数*/
    /**
     * 总日增数
     */
    private List<Integer> totalDayGrowthNums;

    /**
     * 学生日增数
     */
    private List<Integer> studentDayGrowthNums;

    /**
     * 企业日增数
     */
    private List<Integer> companyDayGrowthNums;


    /*月增数*/
    /**
     * 总月增数
     */
    private List<Integer> totalMonthGrowthNums;

    /**
     * 学生月增数
     */
    private List<Integer> studentMonthGrowthNums;

    /**
     * 企业月增数
     */
    private List<Integer> companyMonthGrowthNums;


    /*日增率*/
    /**
     * 学生日增率
     */
    private List<BigDecimal> studentDayGrowths;

    /**
     * 企业日增率
     */
    private List<BigDecimal> companyDayGrowths;

    /**
     * 总日增率(xx.xx%)
     */
    private List<BigDecimal> totalDayGrowths;


    /*月增率*/
    /**
     * 学生月增率
     */
    private List<BigDecimal> studentMonthGrowths;

    /**
     * 企业月增率
     */
    private List<BigDecimal> companyMonthGrowths;

    /**
     * 总月增率
     */
    private List<BigDecimal> totalMonthGrowths;




    /**
     * 近1年增长率
     */
//    private BigDecimal yearGrowth;

    /**
     * 7日增长率
     */
//    private List<BigDecimal> weekGrowths;
}
