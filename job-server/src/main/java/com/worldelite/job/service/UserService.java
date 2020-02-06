package com.worldelite.job.service;

import com.alibaba.fastjson.JSON;
import com.worldelite.job.constants.ConfigType;
import com.worldelite.job.constants.UserStatus;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.context.AttrKeys;
import com.worldelite.job.context.RedisKeys;
import com.worldelite.job.entity.LoginLog;
import com.worldelite.job.entity.User;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.EmailForm;
import com.worldelite.job.form.LoginForm;
import com.worldelite.job.form.RegisterForm;
import com.worldelite.job.form.UserForm;
import com.worldelite.job.mapper.LoginLogMapper;
import com.worldelite.job.mapper.UserMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.util.RequestUtils;
import com.worldelite.job.vo.UserVo;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Service
public class UserService extends BaseService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ConfigService configService;

    @Autowired
    private IEmailService emailService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoginLogMapper loginLogMapper;

    @Value("${token.expired.seconds}")
    private Long TOKEN_EXPIRED_SECONDS;

    @Value("${activate.expired.seconds}")
    private Long ACTIVATE_EXPIRED_SECONDS;

    /**
     * 用户注册
     *
     * @param registerForm
     * @return
     */
    @Transactional
    public UserVo register(RegisterForm registerForm) {
        User user = userMapper.selectByEmail(registerForm.getEmail());
        if (user != null) {
            throw new ServiceException(message("register.email.repeat"));
        }

        final String validCodeKey = RedisKeys.VALIDATE_EMAIL_PREFIX + registerForm.getEmail();

        if(!registerForm.getValidCode().equals(redisTemplate.opsForValue().get(validCodeKey))){
            throw new ServiceException(message("activate.email.expired"));
        }

        user = new User();
        user.setId(AppUtils.nextId());
        user.setEmail(registerForm.getEmail());
        user.setStatus(UserStatus.NOT_ACTIVATE.value);
        user.setType(UserType.GENERAL.value);
        user.setSalt(RandomStringUtils.randomAlphanumeric(40));
        user.setSubscribeFlag(registerForm.getSubscribeFlag());
        final String encodePass = DigestUtils.sha256Hex(registerForm.getPassword() + user.getSalt());
        user.setPassword(encodePass);
        userMapper.insertSelective(user);
        UserVo loginUser = new UserVo().asVo(user);
        saveUserToken(loginUser);
        redisTemplate.delete(validCodeKey);
        return loginUser;
    }

    /**
     * 发送邮件验证码
     */
    public void sendEmailValidCode(String email){
        final String activateCode = RandomStringUtils.randomNumeric(6);
        redisTemplate.opsForValue().set(RedisKeys.VALIDATE_EMAIL_PREFIX + email, activateCode, ACTIVATE_EXPIRED_SECONDS, TimeUnit.SECONDS);
        EmailForm emailForm = new EmailForm();
        emailForm.setAddress(email);
        emailForm.setSubject(message("activate.email.subject"));
        emailForm.setEmailBody(configService.getActivateEmailBody(activateCode));
        emailService.sendEmail(emailForm);
    }

    /**
     * 邮箱登录
     *
     * @return
     */
    public UserVo emailLogin(LoginForm loginForm){
        User user = userMapper.selectByEmail(loginForm.getEmail());
        if(user == null){
            throw new ServiceException(message("login.validate.fail"));
        }
        final String encodePass = DigestUtils.sha256Hex(loginForm.getPassword() + user.getSalt());
        if(!StringUtils.equals(encodePass, user.getPassword())){
            throw new ServiceException(message("login.validate.fail"));
        }
        UserVo loginUser = new UserVo().asVo(user);
        saveUserToken(loginUser);
        return loginUser;
    }

    /**
     * 获取用户信息
     *
     * @param id
     * @return
     */
    public UserVo getUserInfo(Long id){
        User user = userMapper.selectByPrimaryKey(id);
        UserVo userVo = new UserVo().asVo(user);
        userVo.setEmail(user.getEmail());
        userVo.setPhoneCode(user.getPhoneCode());
        if(user.getPhone() != null){
            userVo.setPhone(String.valueOf(user.getPhone()));
        }
        return userVo;
    }

    /**
     * 修改用户信息
     *
     * @param userForm
     */
    public void modifyUser(UserForm userForm){
        User user = userMapper.selectByPrimaryKey(userForm.getId());
        if(user != null){
            user.setAvatar(AppUtils.getOssKey(userForm.getAvatar()));
            user.setPhoneCode(userForm.getPhoneCode());
            user.setPhone(userForm.getPhone());
            user.setGender(userForm.getGender());
            user.setName(userForm.getName());
            user.setUpdateTime(new Date());
            userMapper.updateByPrimaryKeySelective(user);
        }
    }

    /**
     * 退出登录
     */
    public void logout(){
        if(curUser() != null){
            redisTemplate.delete(curUser().getToken());
        }
    }

    /**
     * 把用户登录凭证存到 cookie 中
     *
     * @param loginUser
     */
    private void saveUserToken(UserVo loginUser) {
        final String token = loginUser.getId() + RandomStringUtils.randomAlphanumeric(20);
        loginUser.setToken(token);
        redisTemplate.opsForValue().set(token, JSON.toJSONString(loginUser), TOKEN_EXPIRED_SECONDS, TimeUnit.SECONDS);

        LoginLog loginLog = new LoginLog();
        loginLog.setIp(RequestUtils.getClientIP(AppUtils.request()));
        loginLog.setUserId(loginUser.getId());
        loginLogMapper.insertSelective(loginLog);
    }
}
