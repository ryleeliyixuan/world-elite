package com.worldelite.job.vo;

import com.worldelite.job.entity.SearchJobHistory;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Author twz
 * @Date 2021-01-14
 * @Desc TODO
 */
@Data
@Builder
public class SearchJobHistoryVo{

    private String keyWord;
    private List<Integer> cityIds;
    private List<Integer> degreeIds;
    private List<Integer> salaryIds;
    private List<Integer> industryIds;
    private List<Integer> expIds;
    private List<Integer> scaleIds;
    private List<Integer> defineIds;
    private List<Integer> jobTypeIds;
    private List<Integer> lanIds;
    private List<Integer> specialIds;

    private List<String> cityValues;
    private List<String> degreeValues;
    private List<String> salaryValues;
    private List<String> industryValues;
    private List<String> expValues;
    private List<String> scaleValues;
    private List<String> defineValues;
    private List<String> jobTypeValues;
    private List<String> lanValues;
    private List<String> specialValues;

    private Byte filterCount;

}
