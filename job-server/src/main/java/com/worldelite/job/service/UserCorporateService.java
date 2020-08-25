package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.worldelite.job.anatation.SysLog;
import com.worldelite.job.constants.*;
import com.worldelite.job.context.RedisKeys;
import com.worldelite.job.entity.*;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.*;
import com.worldelite.job.mapper.*;
import com.worldelite.job.service.sdk.AliEmailService;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.util.RequestUtils;
import com.worldelite.job.vo.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Service
public class UserCorporateService extends BaseService {

    @Autowired
    private UserCorporateMapper userCorporateMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private LoginLogMapper loginLogMapper;

    @Autowired
    private ConfigService configService;

    @Autowired
    private AuthService authService;

    @Autowired
    private AliEmailService emailService;

    @Value("${token.expired.seconds}")
    private Long TOKEN_EXPIRED_SECONDS;

    @Value("${activate.expired.seconds}")
    private Long ACTIVATE_EXPIRED_SECONDS;


    /**
     * 注册新企业用户
     * @param registerForm
     * @return
     */
    @Transactional
    public UserCorporateVo register(RegisterForm registerForm) {
        checkRepeatEmail(registerForm.getEmail());

        final String validCodeKey = RedisKeys.VALIDATE_EMAIL_PREFIX + registerForm.getEmail();

        if (!registerForm.getValidCode().equals(stringRedisTemplate.opsForValue().get(validCodeKey))) {
            throw new ServiceException(message("activate.email.expired"));
        }
        try {
            UserCorporate user = newUser(registerForm);
            saveUserToken(user);
            UserCorporateVo loginUser = new UserCorporateVo().asVo(user);
            loginUser.setToken(user.getToken());
            return loginUser;
        } finally {
            stringRedisTemplate.delete(validCodeKey);
        }
    }

    /**
     * 发送邮箱验证码
     * @param email
     */
    public void sendEmailValidCode(String email) {
        final String activateCode = RandomStringUtils.randomNumeric(6);
        stringRedisTemplate.opsForValue().set(RedisKeys.VALIDATE_EMAIL_PREFIX + email, activateCode, ACTIVATE_EXPIRED_SECONDS, TimeUnit.SECONDS);
        EmailForm emailForm = configService.getEmailForm(ConfigType.EMAIL_ACCOUNT_VALIDATE);
        emailForm.setAddress(email);
        emailForm.setEmailBody(emailForm.getEmailBody().replace("${ACTIVATE_CODE}", activateCode));
        emailService.sendEmail(emailForm);
    }

    /**
     * 获取我的信息
     * @param id
     * @return
     */
    public UserCorporateVo getUserInfo(Long id) {
        UserCorporate userCorporate = userCorporateMapper.selectByPrimaryKey(id);
        return userCorporate == null ? null: new UserCorporateVo().asVo(userCorporate);
    }

    /**
     * 修改企业用户信息
     * @param userForm
     */
    public void modifyUser(UserForm userForm) {
        UserCorporate user = userCorporateMapper.selectByPrimaryKey(userForm.getId());
        if (user != null) {
            if(StringUtils.isNotEmpty(userForm.getAvatar())){
                user.setAvatar(AppUtils.getOssKey(userForm.getAvatar()));
            }
            user.setPhoneCode(userForm.getPhoneCode());
            user.setPhone(userForm.getPhone());
            user.setGender(userForm.getGender());
            user.setName(userForm.getName());
            user.setStatus(userForm.getStatus());
            user.setUpdateTime(new Date());
            userCorporateMapper.updateByPrimaryKeySelective(user);
            user = userCorporateMapper.selectByPrimaryKey(user.getId());
            authService.updateLoginUserInfo(user);
        }
    }

    /**
     * 修改邮箱
     * @param modifyEmailForm
     */
    public void modifyUserEmail(ModifyEmailForm modifyEmailForm){

        // 新邮箱验证码
        final String emailValidCode = stringRedisTemplate.opsForValue().get(RedisKeys.VALIDATE_EMAIL_PREFIX + modifyEmailForm.getNewEmail());
        if(org.springframework.util.StringUtils.isEmpty(emailValidCode) || !emailValidCode.equals(modifyEmailForm.getValidCode())){
            throw new ServiceException(message("activate.email.expired"));
        }


        // 密码是否正确
        UserCorporate user = userCorporateMapper.selectByPrimaryKey(curUser().getId());
        if(!user.getPassword().equals(DigestUtils.sha256Hex(modifyEmailForm.getPassword() + user.getSalt()))){
            throw new ServiceException(message("password.invalid"));
        }

        // 和原来邮箱一样
        if(user.getEmail().equals(modifyEmailForm.getNewEmail())){
            throw new ServiceException(message("modify.email.new.equals.old"));
        }

        // 新邮箱是否被注册
        UserCorporate newEmailUser = userCorporateMapper.selectByEmail(modifyEmailForm.getNewEmail());
        if(newEmailUser != null){
            throw new ServiceException(message("register.email.repeat"));
        }

        // 修改邮箱地址
        user.setEmail(modifyEmailForm.getNewEmail());
        user.setUpdateTime(new Date());
        userCorporateMapper.updateByPrimaryKeySelective(user);
    }

    /**
     * 企业用户的邮箱登录
     * @param loginForm
     * @return
     */
    public UserCorporateVo emailLogin(LoginForm loginForm) {
        UserCorporate user = userCorporateMapper.selectByEmail(loginForm.getEmail());
        if (user == null) {
            throw new ServiceException(message("login.validate.fail"));
        }
        if (user.getStatus() == UserStatus.BLACK.value) {
            throw new ServiceException(message("user.black.list"));
        }
        final String encodePass = encodePassword(loginForm.getPassword(), user.getSalt());
        if (!org.apache.commons.lang3.StringUtils.equals(encodePass, user.getPassword())) {
            throw new ServiceException(message("login.validate.fail"));
        }
        saveUserToken(user);
        UserCorporateVo loginUser =  new UserCorporateVo().asVo(user);
        loginUser.setToken(user.getToken());
        return loginUser;
    }

    /**
     * 企业用户重置密码（忘记密码）
     * @param resetPwdForm
     */
    @SysLog
    public void resetPassword(ResetPwdForm resetPwdForm) {
        UserCorporate user = userCorporateMapper.selectByEmail(resetPwdForm.getEmail());
        if (user == null) {
            throw new ServiceException(ApiCode.OBJECT_NOT_FOUND);
        }
        final String emailValidCode = stringRedisTemplate.opsForValue().get(RedisKeys.VALIDATE_EMAIL_PREFIX + resetPwdForm.getEmail());
        if (StringUtils.isEmpty(emailValidCode) || !emailValidCode.equals(resetPwdForm.getValidCode())) {
            throw new ServiceException(message("activate.email.expired"));
        }

        setUserPassword(user, resetPwdForm.getNewPassword());
        user.setUpdateTime(new Date());
        userCorporateMapper.updateByPrimaryKeySelective(user);
    }

    /**
     * 企业用户修改密码
     * @param modifyPwdForm
     */
    @SysLog
    public void modifyPassword(ModifyPwdForm modifyPwdForm){
        UserCorporate user = userCorporateMapper.selectByPrimaryKey(curUser().getId());
        if(!StringUtils.equals(user.getPassword(), encodePassword(modifyPwdForm.getOldPassword(), user.getSalt()))){
            throw new ServiceException(message("modify.old.pwd.error"));
        }
        setUserPassword(user, modifyPwdForm.getNewPassword());
        user.setUpdateTime(new Date());
        userCorporateMapper.updateByPrimaryKeySelective(user);
    }

    /**
     * 退出登录
     */
    public void logout() {
        if (curUser() != null) {
            UserCorporate user  = userCorporateMapper.selectByPrimaryKey(curUser().getId());
            if(user != null){
                stringRedisTemplate.delete(user.getToken());
            }
        }
    }

    /**
     * 构造普通用户对象
     * 并将用户信息插入数据库
     * @param registerForm
     * @return
     */
    private UserCorporate newUser(RegisterForm registerForm) {
        UserCorporate user = new UserCorporate();
        user.setId(AppUtils.nextId());
        user.setEmail(registerForm.getEmail());
        user.setSubscribeFlag(registerForm.getSubscribeFlag());
        user.setStatus(UserStatus.NOT_ACTIVATE.value);
        user.setType(UserType.COMPANY.value);
        user.setGender(registerForm.getGender());
        user.setName(registerForm.getName());
        user.setSubscribeFlag(registerForm.getSubscribeFlag());
        setUserPassword(user, registerForm.getPassword());
        userCorporateMapper.insertSelective(user);
        return user;
    }

    /**
     * 将Token保存到Redis和数据库
     * @param loginUser
     */
    private void saveUserToken(UserCorporate loginUser) {
        final String token = loginUser.getId() + RandomStringUtils.randomAlphanumeric(20);
        loginUser.setToken(token);
        stringRedisTemplate.opsForValue().set(token, JSON.toJSONString(loginUser), TOKEN_EXPIRED_SECONDS, TimeUnit.SECONDS);

        //把token写入数据库
        userCorporateMapper.updateByPrimaryKeySelective(loginUser);

        LoginLog loginLog = new LoginLog();
        loginLog.setIp(RequestUtils.getClientIP(AppUtils.request()));
        loginLog.setUserId(loginUser.getId());
        loginLogMapper.insertSelective(loginLog);
    }

    /**
     * 检查普通用户邮箱地址是否已经被注册
     * @param email
     */
    public void checkRepeatEmail(String email) {
        UserCorporate user = userCorporateMapper.selectByEmail(email);
        if (user != null) {
            throw new ServiceException(message("register.email.repeat"));
        }
    }

    /**
     * 设置用户密码
     *
     * @param user
     * @param password
     */
    private void setUserPassword(UserCorporate user, String password) {
        user.setSalt(RandomStringUtils.randomAlphanumeric(40));
        final String encodePass = encodePassword(password, user.getSalt());
        user.setPassword(encodePass);
    }

    private String encodePassword(String password, String salt) {
        return DigestUtils.sha256Hex(password + salt);
    }
}