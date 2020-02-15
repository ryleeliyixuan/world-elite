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

    /**
     * 验证通过发送邮箱内容
     * @return
     */
    public String getVerificationPassEmailBody(){
        Config config = configMapper.selectByType(ConfigType.VERIFICATION_PASS_EMAIL.value);
        return config == null? "" :config.getContent();
    }

    /**
     * 验证失败发送邮箱内容
     * @return
     */
    public String getVerificationRejectEmailBody(String reason){
        Config config = configMapper.selectByType(ConfigType.VERIFICATION_REJECT_EMAIL.value);
        return config == null? "" :config.getContent().replace("${REASON}", reason);
    }
}
