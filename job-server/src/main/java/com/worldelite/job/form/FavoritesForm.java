package com.worldelite.job.form;

import lombok.Data;

import java.util.List;

/**
 * 批量收藏表单
 */
@Data
public class FavoritesForm {
    private List<Long> objectIdList; //对象ID列表
    private Byte type; //对象类型
    private Boolean favorite; //是否已收藏
}
