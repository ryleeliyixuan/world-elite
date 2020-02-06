package com.worldelite.job.service;

import com.worldelite.job.constants.ConfigType;
import com.worldelite.job.entity.Config;
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
     * 获取邮箱激活发送内容
     * @return
     */
    public String getActivateEmailBody(String activateCode){
        Config config = configMapper.selectByType(ConfigType.VALIDATE_EMAIL_BODY.value);
        return config == null ? null : config.getContent().replace("${ACTIVATE_CODE}", activateCode);
    }
}
