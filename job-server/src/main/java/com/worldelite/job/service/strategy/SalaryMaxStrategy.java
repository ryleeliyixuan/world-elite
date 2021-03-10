package com.worldelite.job.service.strategy;

import com.worldelite.job.vo.JobVo;

import java.util.Comparator;

/**
 * 职位薪资比较
 */
public class SalaryMaxStrategy implements Comparator<JobVo> {

    private final static Integer MONTH_NUMBER = 12;

    @Override
    public int compare(JobVo lJobVo, JobVo rJobVo) {

        //职位类型全职,实习,兼职
        //全职时salaryMonths有效, 否则
        if (lJobVo.getJobType() != null && rJobVo.getJobType() != null) {
            int lJobType = lJobVo.getJobType().getId();
            int rJobType = rJobVo.getJobType().getId();

            //处理月
            int lSalaryMonths, rSalaryMonths;
            if (lJobVo.getSalaryMonths() == null || lJobVo.getSalaryMonths() <= 0)
                lSalaryMonths = MONTH_NUMBER;
            else
                lSalaryMonths = lJobVo.getSalaryMonths();

            if (rJobVo.getSalaryMonths() == null || rJobVo.getSalaryMonths() <= 0)
                rSalaryMonths = MONTH_NUMBER;
            else
                rSalaryMonths = rJobVo.getSalaryMonths();

            //处理最大薪资
            int lMaxSalary, rMaxSalary;
            if (lJobVo.getMaxSalary() == null || lJobVo.getMaxSalary() <= 0)
                lMaxSalary = 0;
            else
                lMaxSalary = lJobVo.getMaxSalary();

            if (rJobVo.getMaxSalary() == null || rJobVo.getMaxSalary() <= 0)
                rMaxSalary = 0;
            else
                rMaxSalary = rJobVo.getMaxSalary();


            //实习,兼职按天计算工资需要特殊处理, 每月按照20天算,得到年总天数
            //全职计算年薪
            float lTotalSalary, rTotalSalary;
            if (lJobType == 108 || lJobType == 1003) {
                lTotalSalary = 20 * MONTH_NUMBER * lMaxSalary;
            } else {
                lTotalSalary = lSalaryMonths * lMaxSalary * 1000;
            }

            if (rJobType == 108 || rJobType == 1003) {
                rTotalSalary = 20 * MONTH_NUMBER * rMaxSalary;
            } else {
                rTotalSalary = rSalaryMonths * rMaxSalary * 1000;
            }

            return Float.compare(lTotalSalary, rTotalSalary);

        } else
            return 0;
    }
}
