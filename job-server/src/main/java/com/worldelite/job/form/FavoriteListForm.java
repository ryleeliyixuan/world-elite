package com.worldelite.job.form;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FavoriteListForm extends PageForm{
    private Byte type; //对象类型
}
