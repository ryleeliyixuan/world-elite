package com.worldelite.job.service;

import com.worldelite.job.anatation.SysLog;
import com.worldelite.job.context.RedisKeys;
import com.worldelite.job.entity.User;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.ResetPwdForm;
import com.worldelite.job.mapper.UserMapper;
import com.worldelite.job.vo.ApiCode;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Service
public class AuthService extends BaseService{

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserMapper userMapper;

    /**
     * 重置密码
     *
     * @param resetPwdForm
     */
    @SysLog
    public void resetPassword(ResetPwdForm resetPwdForm){
        User user = userMapper.selectByEmail(resetPwdForm.getEmail());
        if(user == null){
            throw new ServiceException(ApiCode.OBJECT_NOT_FOUND);
        }
        final String emailValidCode = redisTemplate.opsForValue().get(RedisKeys.VALIDATE_EMAIL_PREFIX + resetPwdForm.getEmail());
        if(StringUtils.isEmpty(emailValidCode) || !emailValidCode.equals(resetPwdForm.getValidCode())){
            throw new ServiceException(message("activate.email.expired"));
        }

        user.setSalt(RandomStringUtils.randomAlphanumeric(40));
        final String encodePass = DigestUtils.sha256Hex(resetPwdForm.getNewPassword() + user.getSalt());
        user.setPassword(encodePass);
        user.setUpdateTime(new Date());
        userMapper.updateByPrimaryKeySelective(user);
    }

}
