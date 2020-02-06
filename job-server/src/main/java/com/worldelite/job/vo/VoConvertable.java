package com.worldelite.job.vo;

import java.io.Serializable;

/**
 * 把实体转成Vo接口
 * @author yeguozhong yedaxia.github.com
 */
public interface VoConvertable<VO, Entity> extends Serializable {
    VO asVo(Entity entity);
}
