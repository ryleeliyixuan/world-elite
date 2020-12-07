package com.worldelite.job.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Author twz
 * @Date 2020/12/2 0002
 * @Desc 日期
 */
@Data
@Builder
public class StatDateVo {

    /**
     * 按月查看
     */
    private List<String> monthDates;

    /**
     * 按年查看
     */
    private List<String> yearDates;
}
