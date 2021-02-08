package com.worldelite.job.service.strategy;

import com.worldelite.job.vo.JobVo;

import java.util.Comparator;
import java.util.Optional;

/**
 * @Author twz
 * @Date 2021-01-13
 * @Desc TODO
 */
public class SalaryMaxStrategy implements Comparator<JobVo> {

    private final static Integer MONTH_NUMBER = 12;

    @Override
    public int compare(JobVo LJobVo, JobVo RJobVo) {
        Integer LSalary;
        Integer RSalary;

        Integer LSalaryMonths = Optional.ofNullable(LJobVo.getSalaryMonths()).orElse(0);

        if (LSalaryMonths == 0) {
            LSalary = LJobVo.getSalaryMonths() * MONTH_NUMBER;
        } else {
            LSalary = LJobVo.getSalaryMonths() * LSalaryMonths;
        }

        RSalary = RJobVo.getMaxSalary()*RJobVo.getSalaryMonths();

        if (LSalary > RSalary) return 1;
        if (LSalary < RSalary) return -1;
        return 0;
    }
}
