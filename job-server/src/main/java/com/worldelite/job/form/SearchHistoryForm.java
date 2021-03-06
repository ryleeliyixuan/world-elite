package com.worldelite.job.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @Author twz
 * @Date 2021-01-14
 * @Desc TODO
 */
@Data
public class SearchHistoryForm extends PageForm{

    private String keyWord;
    private Integer[] cityIds;
    private Integer[] salaryIds;
    private Integer[] degreeIds;
    private Integer[] industryIds;
    private Integer[] expIds;
    private Integer[] scaleIds;
    private Integer[] defineIds;
    private Integer[] jobTypeIds;
    private Integer[] lanIds;
    private Integer[] specialIds;
    private Byte filterCount;

}
