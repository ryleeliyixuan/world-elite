package com.worldelite.job.vo;

import com.worldelite.job.entity.Dict;
import lombok.Data;

/**
 * @author yeguozhong yedaxia.github.com
 */

@Data
public class DictVo implements VoConvertable<DictVo, Dict>{

    private Integer id; //字典ID
    private String name;  //字典名
    private Integer type;  //字典类型
    private String value;  //字典值

    @Override
    public DictVo asVo(Dict dict) {
        setId(dict.getId());
        setName(dict.getName());
        setType(dict.getType());
        setValue(dict.getValue());
        return this;
    }
}
