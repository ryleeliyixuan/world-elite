package com.worldelite.job.service.strategy;

import com.worldelite.job.entity.Job;
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

        String[] LSalarySplit = LJobVo.getSalary().getValue().split("-");
        Integer LSalaryMonths = Optional.ofNullable(LJobVo.getSalaryMonths()).orElse(0);

        if (LSalarySplit.length > 1) {
            Integer maxSalary = Integer.parseInt(LSalarySplit[1]);
            if (LSalaryMonths == 0) {
                LSalary = maxSalary * MONTH_NUMBER;
            } else {
                LSalary = maxSalary * LSalaryMonths;
            }
        } else {
            LSalary = 0;
        }

        String[] RSalarySplit = RJobVo.getSalary().getValue().split("-");
        Integer RSalaryMonths = Optional.ofNullable(RJobVo.getSalaryMonths()).orElse(0);

        if (RSalarySplit.length > 1) {
            Integer maxSalary = Integer.parseInt(RSalarySplit[1]);
            if (RSalaryMonths == 0) {
                RSalary = maxSalary * MONTH_NUMBER;
            } else {
                RSalary = maxSalary * RSalaryMonths;
            }
        } else {
            RSalary = 0;
        }

        if (LSalary > RSalary) return 1;
        if (LSalary < RSalary) return -1;
        return 0;
    }
}
