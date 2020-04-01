package com.worldelite.job.service;

import com.alibaba.fastjson.JSON;
import com.worldelite.job.constants.ConfigType;
import com.worldelite.job.entity.Config;
import com.worldelite.job.form.EmailForm;
import com.worldelite.job.form.HomeConfigForm;
import com.worldelite.job.mapper.ConfigMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

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

    /**
     * 保存首页配置
     *
     * @param homeConfigForm
     */
    public void saveHomeConfig(HomeConfigForm homeConfigForm){
        Config config = configMapper.selectByType(homeConfigForm.getConfigType());
        if(config == null){
            config = new Config();
            config.setType(homeConfigForm.getConfigType());
        }
        config.setContent(JSON.toJSONString(homeConfigForm));
        if(config.getId() == null){
            configMapper.insertSelective(config);
        }else{
            config.setUpdateTime(new Date());
            configMapper.updateByPrimaryKeySelective(config);
        }
    }

    /**
     * 获取首页配置
     *
     * @return
     */
    public HomeConfigForm getHomeConfig(Integer configType){
        Config config = configMapper.selectByType(configType);
        return config == null ? null : JSON.parseObject(config.getContent(), HomeConfigForm.class);
    }
}
