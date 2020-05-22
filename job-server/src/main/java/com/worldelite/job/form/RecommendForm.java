package com.worldelite.job.form;

import lombok.Data;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class RecommendForm {
    private Integer id; //推荐ID
    private Long objectId; //对象ID
    private Integer position; //排序
    private Byte objectType; //对象类型
}
