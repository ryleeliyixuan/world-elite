package com.worldelite.job.form;

import lombok.Data;

/**
 * 首页配置模块查询表单
 */
@Data
public class ConfigModuleListForm extends PageForm{
    private Integer id; //ID
    private Byte type; //模块类型
    private Byte configType; //配置类型
}
