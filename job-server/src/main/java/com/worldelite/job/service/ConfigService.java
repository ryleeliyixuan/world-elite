package com.worldelite.job.service;

import com.alibaba.fastjson.JSON;
import com.worldelite.job.constants.ConfigType;
import com.worldelite.job.entity.Config;
import com.worldelite.job.form.EmailForm;
import com.worldelite.job.mapper.ConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Service
public class ConfigService extends BaseService{

    @Autowired
    private ConfigMapper configMapper;

    /**
     * 获取邮件模板
     *
     * @param configType
     * @return
     */
    public EmailForm getEmailForm(ConfigType configType){
        Config config = configMapper.selectByType(configType.value);
        return config == null ? null : JSON.parseObject(config.getContent(), EmailForm.class);
    }
}
