package com.worldelite.job.vo;

import cn.hutool.core.bean.BeanUtil;
import com.worldelite.job.entity.ConfigModule;
import lombok.Data;

/**
 * 首页配置模块视图
 */
@Data
public class ConfigModuleVo implements VoConvertable<ConfigModuleVo,ConfigModule>{

    private Integer id; //ID
    private String name; //模板名
    private Byte type; //模板类型
    private Byte configType; //配置类型
    private Byte value; //模板值
    private Integer position; //排序

    @Override
    public ConfigModuleVo asVo(ConfigModule configModule) {
        if(configModule == null){
            return null;
        }
        BeanUtil.copyProperties(configModule,this);
        return this;
    }
}
