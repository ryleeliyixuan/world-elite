package com.worldelite.job.form;

import lombok.Data;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class FavoriteForm {
    private Long objectId; //对象ID
    private Byte type; //对象类型
    private Boolean favorite; //是否已收藏
}
