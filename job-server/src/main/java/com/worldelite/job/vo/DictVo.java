package com.worldelite.job.vo;

import com.worldelite.job.entity.Dict;
import lombok.Data;

/**
 * @author yeguozhong yedaxia.github.com
 */

@Data
public class DictVo implements VoConvertable<DictVo, Dict>{

    private Integer id;
    private String name;
    private Integer type;
    private String value;

    @Override
    public DictVo asVo(Dict dict) {
        setId(dict.getId());
        setName(dict.getName());
        setType(dict.getType());
        setValue(dict.getValue());
        return this;
    }
}
