package com.worldelite.job.service;

import com.alibaba.fastjson.JSON;
import com.worldelite.job.constants.ConfigType;
import com.worldelite.job.entity.Config;
import com.worldelite.job.form.EmailForm;
import com.worldelite.job.form.HomeConfigForm;
import com.worldelite.job.form.RecommendConfigForm;
import com.worldelite.job.mapper.ConfigMapper;
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

    /**
     * 保存用户端首页配置信息
     * @param recommendConfigForm 配置表单
     */
    public void saveRecommendConfig(RecommendConfigForm recommendConfigForm){
        Config config = configMapper.selectByType(recommendConfigForm.getConfigType());
        if(config == null){
            config = new Config();
            config.setType(recommendConfigForm.getConfigType());
        }
        config.setContent(JSON.toJSONString(recommendConfigForm));
        if(config.getId() == null){
            configMapper.insertSelective(config);
        }else{
            config.setUpdateTime(new Date());
            configMapper.updateByPrimaryKeySelective(config);
        }
    }

    /**
     * 获取用户端首页配置信息
     * @param configType
     * @return
     */
    public RecommendConfigForm getRecommendConfig(Integer configType){
        Config config = configMapper.selectByType(configType);
        return config == null ? null : JSON.parseObject(config.getContent(), RecommendConfigForm.class);
    }

    /**
     * 用户协议
     *
     * @return
     */
    public String getUserProtocol(){
        Config config = configMapper.selectByType(ConfigType.USER_PROTOCOL.value);
        return config == null ? null: config.getContent();
    }
}
