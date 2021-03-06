package com.worldelite.job.service;

import com.alibaba.fastjson.JSON;
import com.worldelite.job.anatation.SysLog;
import com.worldelite.job.constants.*;
import com.worldelite.job.context.RedisKeys;
import com.worldelite.job.entity.Auth;
import com.worldelite.job.entity.LoginLog;
import com.worldelite.job.entity.User;
import com.worldelite.job.entity.UserApplicant;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.*;
import com.worldelite.job.mapper.AuthMapper;
import com.worldelite.job.mapper.LoginLogMapper;
import com.worldelite.job.mapper.UserApplicantMapper;
import com.worldelite.job.mapper.UserMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.util.RequestUtils;
import com.worldelite.job.vo.ApiCode;
import com.worldelite.job.vo.UserApplicantVo;
import com.worldelite.job.vo.UserVo;
import me.zhyd.oauth.enums.AuthUserGender;
import me.zhyd.oauth.model.AuthUser;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.DomainEvents;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Service
public class AuthService extends BaseService {

    @Autowired
    private ConfigService configService;

    @Autowired
    private IEmailService emailService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private LoginLogMapper loginLogMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AuthMapper authMapper;

    @Value("${token.expired.seconds}")
    private Long TOKEN_EXPIRED_SECONDS;

    @Value("${activate.expired.seconds}")
    private Long ACTIVATE_EXPIRED_SECONDS;

    /**
     * ????????????
     *
     * @param registerForm
     * @return
     */
    @Transactional
    @Deprecated
    public UserVo register(RegisterForm registerForm) {
        checkRepeatEmail(registerForm.getEmail());

        final String validCodeKey = RedisKeys.VALIDATE_EMAIL_PREFIX + registerForm.getEmail();

        if (!registerForm.getValidCode().equals(stringRedisTemplate.opsForValue().get(validCodeKey))) {
            throw new ServiceException(message("activate.email.expired"));
        }
        try {
            User user = newUser(registerForm);
            saveUserToken(user);
            UserVo loginUser = new UserVo().asVo(user);
            loginUser.setToken(user.getToken());
            return loginUser;
        } finally {
            stringRedisTemplate.delete(validCodeKey);
        }
    }


    /**
     * ?????????????????????
     *
     * @param registerForm
     * @return
     */
    @Transactional
    @Deprecated
    public UserVo bindThirdAccount(BindAccountForm registerForm) {
        // ???????????????????????????????????????????????????
        // checkRepeatEmail(registerForm.getEmail());

        final String validCodeKey = RedisKeys.VALIDATE_EMAIL_PREFIX + registerForm.getEmail();
        if (!registerForm.getValidCode().equals(stringRedisTemplate.opsForValue().get(validCodeKey))) {
            throw new ServiceException(message("activate.email.expired"));
        }

        try {
            Auth options = new Auth();
            options.setUserId(curUser().getId());
            options.setAuthType(registerForm.getAuthType());
            List<Auth> authList = authMapper.selectAndList(options);
            if (CollectionUtils.isEmpty(authList)) {
                throw new ServiceException(ApiCode.OBJECT_NOT_FOUND);
            }

            Auth auth = authList.get(0);
            auth.setVerified(Bool.TRUE);
            auth.setUpdateTime(new Date());
            authMapper.updateByPrimaryKeySelective(auth);

            User user = userMapper.selectByEmail(registerForm.getEmail());
            user.setEmail(registerForm.getEmail());
            user.setSubscribeFlag(registerForm.getSubscribeFlag());
            setUserPassword(user, registerForm.getPassword());
            user.setUpdateTime(new Date());
            userMapper.updateByPrimaryKeySelective(user);

            return new UserVo().asVo(user);
        } finally {
            stringRedisTemplate.delete(validCodeKey);
        }
    }

    /**
     * ?????????????????????
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
     * ????????????
     *
     * @return
     */
    @Deprecated
    public UserVo emailLogin(LoginForm loginForm) {
        User user = userMapper.selectByEmail(loginForm.getEmail());
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
        UserVo loginUser =  new UserVo().asVo(user);
        loginUser.setToken(user.getToken());
        return loginUser;
    }


    /**
     * ????????????
     *
     * @param resetPwdForm
     */
    @SysLog
    @Deprecated
    public void resetPassword(ResetPwdForm resetPwdForm) {
        User user = userMapper.selectByEmail(resetPwdForm.getEmail());
        if (user == null) {
            throw new ServiceException(ApiCode.OBJECT_NOT_FOUND);
        }
        final String emailValidCode = stringRedisTemplate.opsForValue().get(RedisKeys.VALIDATE_EMAIL_PREFIX + resetPwdForm.getEmail());
        if (StringUtils.isEmpty(emailValidCode) || !emailValidCode.equals(resetPwdForm.getValidCode())) {
            throw new ServiceException(message("activate.email.expired"));
        }

        setUserPassword(user, resetPwdForm.getNewPassword());
        user.setUpdateTime(new Date());
        userMapper.updateByPrimaryKeySelective(user);
    }


    /**
     * ????????????
     *
     * @param modifyPwdForm
     */
    @SysLog
    @Deprecated
    public void modifyPassword(ModifyPwdForm modifyPwdForm){
        User user = userMapper.selectByPrimaryKey(curUser().getId());
        if(!StringUtils.equals(user.getPassword(), encodePassword(modifyPwdForm.getOldPassword(), user.getSalt()))){
            throw new ServiceException(message("modify.old.pwd.error"));
        }
        setUserPassword(user, modifyPwdForm.getNewPassword());
        user.setUpdateTime(new Date());
        userMapper.updateByPrimaryKeySelective(user);
    }

    /**
     * ???????????????
     *
     * @return ?????????????????????
     */
    @Transactional
    @Deprecated
    public String thirdPartLogin(AuthUser authUser) {
        final String lockKey = String.format("%s::%s", authUser.getSource(), authUser.getUuid());
        final String lock = stringRedisTemplate.opsForValue().getAndSet(lockKey, "lock");
        if (StringUtils.isNotEmpty(lock)) {
            return null;
        }

        try {
            if ("WECHAT_OPEN".equalsIgnoreCase(authUser.getSource())) {
                return handleWechatOpenLogin(authUser);
            } else if ("GOOGLE".equalsIgnoreCase(authUser.getSource())) {
                return handleGoogleLogin(authUser);
            } else if ("MYLINKEDIN".equalsIgnoreCase(authUser.getSource())) {
                return handleLinkedInLogin(authUser);
            }
        } finally {
            stringRedisTemplate.delete(lockKey);
        }

        return null;
    }

    /**
     * ????????????
     */
    @Deprecated
    public void logout() {
        if (curUser() != null) {
            User user  = userMapper.selectByPrimaryKey(curUser().getId());
            if(user != null){
                stringRedisTemplate.delete(user.getToken());
            }
        }
    }

    /**
     * ????????????
     *
     * @param registerForm
     * @return
     */
    @Deprecated
    private User newUser(RegisterForm registerForm) {
        User user = new User();
        user.setId(AppUtils.nextId());
        user.setEmail(registerForm.getEmail());
        user.setSubscribeFlag(registerForm.getSubscribeFlag());
        if (registerForm.getUserType() == null || registerForm.getUserType() == UserType.GENERAL.value) {
            user.setStatus(UserStatus.NORMAL.value);
            user.setType(UserType.GENERAL.value);
        } else if (registerForm.getUserType() == UserType.COMPANY.value) {
            user.setStatus(UserStatus.NOT_ACTIVATE.value);
            user.setType(UserType.COMPANY.value);
        }
        user.setGender(registerForm.getGender());
        user.setName(registerForm.getName());
        user.setSubscribeFlag(registerForm.getSubscribeFlag());
        setUserPassword(user, registerForm.getPassword());
        userMapper.insertSelective(user);
        return user;
    }

    /**
     * ??????email?????????????????????
     * @param email
     * @return
     */
    @Deprecated
    public void checkRepeatEmail(String email) {
        User user = userMapper.selectByEmail(email);
        if (user != null) {
            throw new ServiceException(message("register.email.repeat"));
        }
    }

    /**
     * ???????????? token
     * @param userId
     */
    @Deprecated
    public void removeUserToken(Long userId){
        User user = userMapper.selectByPrimaryKey(userId);
        if(user != null){
            stringRedisTemplate.delete(user.getToken());
        }
    }

    /**
     * ??????????????????
     *
     * @param user
     * @param password
     */
    private void setUserPassword(User user, String password) {
        user.setSalt(RandomStringUtils.randomAlphanumeric(40));
        final String encodePass = encodePassword(password, user.getSalt());
        user.setPassword(encodePass);
    }

    private String encodePassword(String password, String salt) {
        return DigestUtils.sha256Hex(password + salt);
    }

    /**
     * ??????????????????????????? cookie ???
     *
     * @param loginUser
     */
    private void saveUserToken(User loginUser) {
        final String token = loginUser.getId() + RandomStringUtils.randomAlphanumeric(20);
        loginUser.setToken(token);
        stringRedisTemplate.opsForValue().set(token, JSON.toJSONString(loginUser), TOKEN_EXPIRED_SECONDS, TimeUnit.SECONDS);

        //???token???????????????
        userMapper.updateByPrimaryKeySelective(loginUser);

        LoginLog loginLog = new LoginLog();
        loginLog.setIp(RequestUtils.getClientIP(AppUtils.request()));
        loginLog.setUserId(loginUser.getId());
        loginLogMapper.insertSelective(loginLog);
    }


    /**
     * ??????????????????????????????
     * @param authUser
     * @return
     */
    private String handleWechatOpenLogin(AuthUser authUser) {
        String redirectUrl = "";
        Auth options = new Auth();
        options.setAuthType(AuthType.WECHAT_OPEN.value);
        options.setAuthId(authUser.getToken().getUnionId());
        options.setOpenId(authUser.getToken().getOpenId());
        List<Auth> authList = authMapper.selectAndList(options);
        User user;
        if (CollectionUtils.isEmpty(authList)) {
            RegisterForm registerForm = new RegisterForm();
            // ?????????????????????email ??? ??????????????????
            registerForm.setEmail(UUID.randomUUID().toString() + "@myworldelite.com");
            registerForm.setPassword(RandomStringUtils.random(20));
            registerForm.setUserType(UserType.GENERAL.value);
            registerForm.setName(authUser.getUsername());
            registerForm.setGender(authUser.getGender() == AuthUserGender.MALE ? Gender.MALE.value : Gender.FEMALE.value);
            user = newUser(registerForm);

            Auth auth = options;
            auth.setUserId(user.getId());
            auth.setVerified(Bool.FALSE); //?????????????????????
            authMapper.insertSelective(auth);

            // ??????????????????
            redirectUrl = "bind-account";
        } else {
            Auth auth = authList.get(0);
            user = userMapper.selectByPrimaryKey(auth.getUserId());

            if (auth.getVerified() == Bool.FALSE) {
                redirectUrl = "bind-account";
            } else {
                redirectUrl = "/";
            }
        }

        saveUserToken(user);

        return AppUtils.wholeWebUrl(String.format("%s?_token=%s&authType=%d", redirectUrl, user.getToken(), AuthType.WECHAT_OPEN.value));
    }

    /**
     * ??????????????????
     *
     * @param authUser
     * @return
     */
    private String handleGoogleLogin(AuthUser authUser){
        String redirectUrl = "";
        Auth options = new Auth();
        options.setAuthType(AuthType.GOOGLE.value);
        options.setAuthId(authUser.getUuid());
        List<Auth> authList = authMapper.selectAndList(options);
        User user;
        if (CollectionUtils.isEmpty(authList)) {
            RegisterForm registerForm = new RegisterForm();
            registerForm.setUserType(UserType.GENERAL.value);
            registerForm.setName(authUser.getNickname());

            if(StringUtils.isNotEmpty(authUser.getEmail())){
                if(userMapper.selectByEmail(authUser.getEmail()) == null){
                    registerForm.setEmail(authUser.getEmail());
                }
            }

            if(StringUtils.isEmpty(registerForm.getEmail())){
                registerForm.setEmail(UUID.randomUUID().toString() + "@myworldelite.com");
            }

            registerForm.setPassword(RandomStringUtils.random(20));
            if(authUser.getGender() != null){
                registerForm.setGender(authUser.getGender() == AuthUserGender.MALE ? Gender.MALE.value : Gender.FEMALE.value);
            }
            user = newUser(registerForm);

            Auth auth = options;
            auth.setOpenId(authUser.getUuid());
            auth.setUserId(user.getId());
            auth.setVerified(Bool.FALSE); //?????????????????????
            authMapper.insertSelective(auth);

            // ??????????????????
            redirectUrl = "bind-account";
        } else {
            Auth auth = authList.get(0);
            user = userMapper.selectByPrimaryKey(auth.getUserId());

            if (auth.getVerified() == Bool.FALSE) {
                redirectUrl = "bind-account";
            } else {
                redirectUrl = "/";
            }
        }

        saveUserToken(user);

        return AppUtils.wholeWebUrl(String.format("%s?_token=%s&authType=%d", redirectUrl, user.getToken(), AuthType.GOOGLE.value));
    }

    /**
     * ??????LinkedIn?????????????????????
     * @param authUser
     * @return
     */
    private String handleLinkedInLogin(AuthUser authUser){
        String redirectUrl = "";
        Auth options = new Auth();
        options.setAuthType(AuthType.LINKEDIN.value);
        options.setAuthId(authUser.getUuid());
        List<Auth> authList = authMapper.selectAndList(options);
        User user;
        if (CollectionUtils.isEmpty(authList)) {
            RegisterForm registerForm = new RegisterForm();
            registerForm.setUserType(UserType.GENERAL.value);
            registerForm.setName(authUser.getNickname());

            if(StringUtils.isNotEmpty(authUser.getEmail())){
                if(userMapper.selectByEmail(authUser.getEmail()) == null){
                    registerForm.setEmail(authUser.getEmail());
                }
            }

            if(StringUtils.isEmpty(registerForm.getEmail())){
                registerForm.setEmail(UUID.randomUUID().toString() + "@myworldelite.com");
            }

            registerForm.setPassword(RandomStringUtils.random(20));
            if(authUser.getGender() != null){
                registerForm.setGender(authUser.getGender() == AuthUserGender.MALE ? Gender.MALE.value : Gender.FEMALE.value);
            }
            user = newUser(registerForm);

            Auth auth = options;
            auth.setOpenId(authUser.getUuid());
            auth.setUserId(user.getId());
            auth.setVerified(Bool.FALSE); //?????????????????????
            authMapper.insertSelective(auth);

            // ??????????????????
            redirectUrl = "bind-account";
        } else {
            Auth auth = authList.get(0);
            user = userMapper.selectByPrimaryKey(auth.getUserId());

            if (auth.getVerified() == Bool.FALSE) {
                redirectUrl = "bind-account";
            } else {
                redirectUrl = "/";
            }
        }

        saveUserToken(user);

        return AppUtils.wholeWebUrl(String.format("%s?_token=%s&authType=%d", redirectUrl, user.getToken(), AuthType.LINKEDIN.value));
    }

    /**
     * ?????????????????????????????????
     *
     * @param user
     */
    void updateLoginUserInfo(User user) {
        if (user != null && StringUtils.isNotEmpty(user.getToken())) {
            UserVo loginUser = new UserVo().asVo(user);
            stringRedisTemplate.opsForValue().set(user.getToken(), JSON.toJSONString(loginUser), TOKEN_EXPIRED_SECONDS, TimeUnit.SECONDS);
        }
    }
}
