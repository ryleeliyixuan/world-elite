package com.worldelite.job.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author twz
 * @Date 2020/11/26 0026
 * @Desc TODO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchNameForm {
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
