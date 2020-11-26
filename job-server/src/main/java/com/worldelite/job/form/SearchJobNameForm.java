package com.worldelite.job.form;

import lombok.Data;

/**
 * @Author twz
 * @Date 2020/11/26 0026
 * @Desc TODO
 */
@Data
public class SearchJobNameForm {
    /**
     * 关键字
     */
    private String keyWords;

    /**
     * 页数
     */
    private int page;

    /**
     * 搜索条数
     */
    private int size;
}
