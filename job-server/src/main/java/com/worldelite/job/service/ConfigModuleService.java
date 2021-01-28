package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.worldelite.job.entity.ConfigModule;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.ConfigModuleForm;
import com.worldelite.job.form.ConfigModuleListForm;
import com.worldelite.job.mapper.ConfigModuleMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.ConfigModuleVo;
import com.worldelite.job.vo.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页配置模块服务类
 */
@Slf4j
@Service
public class ConfigModuleService extends BaseService{

    @Autowired
    private ConfigModuleMapper configModuleMapper;

    /**
     * 更新模块信息
     * @param configModuleForm 模块表单
     */
    public void updateConfigModule(ConfigModuleForm configModuleForm){
        Integer id = configModuleForm.getId();
        if(id == null){
            throw new ServiceException(message("api.error.invalid.param"));
        }
        ConfigModule configModule = configModuleMapper.selectByPrimaryKey(id);
        if(configModule == null){
            throw new ServiceException(message("api.error.invalid.param"));
        }
        BeanUtil.copyProperties(configModuleForm,configModule);
        configModuleMapper.updateByPrimaryKeySelective(configModule);
    }

    /**
     * 分页查询配置模块
     * @param configModuleListForm 查询表单
     * @return
     */
    public PageResult<ConfigModuleVo> list(ConfigModuleListForm configModuleListForm){
        AppUtils.setPage(configModuleListForm);
        ConfigModule options = new ConfigModule();
        BeanUtil.copyProperties(configModuleListForm,options);
        Page<ConfigModule> page = (Page<ConfigModule>) configModuleMapper.selectAndList(options);
        PageResult<ConfigModuleVo> pageResult = new PageResult<>(page);
        List<ConfigModuleVo> list = new ArrayList<>();
        for(ConfigModule configModule:page){
            ConfigModuleVo configModuleVo = new ConfigModuleVo().asVo(configModule);
            list.add(configModuleVo);
        }
        pageResult.setList(list);
        return pageResult;
    }
}
