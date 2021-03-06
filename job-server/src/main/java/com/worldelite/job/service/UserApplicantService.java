package com.worldelite.job.service;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.worldelite.job.anatation.SysLog;
import com.worldelite.job.constants.*;
import com.worldelite.job.context.RedisKeys;
import com.worldelite.job.entity.Auth;
import com.worldelite.job.entity.Avatar;
import com.worldelite.job.entity.LoginLog;
import com.worldelite.job.entity.UserApplicant;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.*;
import com.worldelite.job.mapper.AuthMapper;
import com.worldelite.job.mapper.AvatarMapper;
import com.worldelite.job.mapper.LoginLogMapper;
import com.worldelite.job.mapper.UserApplicantMapper;
import com.worldelite.job.service.sdk.AliEmailService;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.util.RequestUtils;
import com.worldelite.job.util.TimeUtils;
import com.worldelite.job.vo.ApiCode;
import com.worldelite.job.vo.UserApplicantVo;
import me.zhyd.oauth.enums.AuthUserGender;
import me.zhyd.oauth.model.AuthUser;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Service
public class UserApplicantService extends BaseService {

    @Autowired
    private UserApplicantMapper userApplicantMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private LoginLogMapper loginLogMapper;

    @Autowired
    private AuthMapper authMapper;

    @Autowired
    private ConfigService configService;

    @Autowired
    private AliEmailService emailService;

    @Autowired
    private AuthService authService;

    @Autowired
    private AvatarMapper avatarMapper;

    @Value("${token.expired.seconds}")
    private Long TOKEN_EXPIRED_SECONDS;

    @Value("${activate.expired.seconds}")
    private Long ACTIVATE_EXPIRED_SECONDS;


    /**
     * ?????????????????????
     * @param registerForm
     * @return
     */
    @Transactional
    public UserApplicantVo register(RegisterForm registerForm) {
        checkRepeatEmail(registerForm.getEmail());

        final String validCodeKey = RedisKeys.VALIDATE_EMAIL_PREFIX + registerForm.getEmail();

        if (!registerForm.getValidCode().equals(stringRedisTemplate.opsForValue().get(validCodeKey))) {
            throw new ServiceException(message("activate.email.expired"));
        }
        try {
            UserApplicant user = newUser(registerForm);
            saveUserToken(user);
            UserApplicantVo loginUser = new UserApplicantVo().asVo(user);
            loginUser.setToken(user.getToken());
            return loginUser;
        } finally {
            stringRedisTemplate.delete(validCodeKey);
        }
    }

    /**
     * ???????????????????????????
     * @param loginForm
     * @return
     */
    public UserApplicantVo emailLogin(LoginForm loginForm) {
        UserApplicant user = userApplicantMapper.selectByEmail(loginForm.getEmail());
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
        UserApplicantVo loginUser =  new UserApplicantVo().asVo(user);
        loginUser.setToken(user.getToken());
        return loginUser;
    }

    /**
     * ??????????????????
     * @param id
     * @return
     */
    public UserApplicantVo getUserInfo(Long id) {
        UserApplicant userApplicant = userApplicantMapper.selectByPrimaryKey(id);
        return userApplicant == null ? null: new UserApplicantVo().asVo(userApplicant);
    }

    /**
     * ????????????ID??????????????????
     * @param id
     * @return
     */
    public UserApplicant selectByPrimaryKey(Long id) {
        return userApplicantMapper.selectByPrimaryKey(id);
    }

    public void updateByPrimaryKeySelective(UserApplicant userApplicant){
        userApplicantMapper.updateByPrimaryKeySelective(userApplicant);
    }
    /**
     * ??????????????????
     * @param userForm
     */
    public void modifyUser(UserForm userForm) {
        UserApplicant user = userApplicantMapper.selectByPrimaryKey(userForm.getId());
        if (user != null) {
            if (StringUtils.isNotBlank(userForm.getAvatar()))
                user.setAvatar(AppUtils.getOssKey(userForm.getAvatar()));

            if (StringUtils.isNotBlank(userForm.getPhoneCode()))
                user.setPhoneCode(userForm.getPhoneCode());

            if (userForm.getPhone() != null)
                user.setPhone(userForm.getPhone());

            if (userForm.getGender() != null)
                user.setGender(userForm.getGender());

            if (StringUtils.isNotBlank(userForm.getName()))
                user.setName(userForm.getName());

            if (userForm.getStatus() != null)
                user.setStatus(userForm.getStatus());

            userApplicantMapper.updateByPrimaryKeySelective(user);
            authService.updateLoginUserInfo(user);
        }else{
            throw new ServiceException(message("user.not.exists"));
        }
    }

    /**
     * ????????????
     * @param modifyEmailForm
     */
    public void modifyUserEmail(ModifyEmailForm modifyEmailForm){

        // ??????????????????
        final String emailValidCode = stringRedisTemplate.opsForValue().get(RedisKeys.VALIDATE_EMAIL_PREFIX + modifyEmailForm.getNewEmail());
        if(org.springframework.util.StringUtils.isEmpty(emailValidCode) || !emailValidCode.equals(modifyEmailForm.getValidCode())){
            throw new ServiceException(message("activate.email.expired"));
        }


        // ??????????????????
        UserApplicant user = userApplicantMapper.selectByPrimaryKey(curUser().getId());
        if(!user.getPassword().equals(DigestUtils.sha256Hex(modifyEmailForm.getPassword() + user.getSalt()))){
            throw new ServiceException(message("password.invalid"));
        }

        // ?????????????????????
        if(user.getEmail().equals(modifyEmailForm.getNewEmail())){
            throw new ServiceException(message("modify.email.new.equals.old"));
        }

        // ????????????????????????
        UserApplicant newEmailUser = userApplicantMapper.selectByEmail(modifyEmailForm.getNewEmail());
        if(newEmailUser != null){
            throw new ServiceException(message("register.email.repeat"));
        }

        // ??????????????????
        user.setEmail(modifyEmailForm.getNewEmail());
        user.setUpdateTime(new Date());
        userApplicantMapper.updateByPrimaryKeySelective(user);
    }

    /**
     * ??????????????????????????????????????????
     * @param resetPwdForm
     */
    @SysLog
    public void resetPassword(ResetPwdForm resetPwdForm) {
        UserApplicant user = userApplicantMapper.selectByEmail(resetPwdForm.getEmail());
        if (user == null) {
            throw new ServiceException(ApiCode.OBJECT_NOT_FOUND);
        }
        final String emailValidCode = stringRedisTemplate.opsForValue().get(RedisKeys.VALIDATE_EMAIL_PREFIX + resetPwdForm.getEmail());
        if (StringUtils.isEmpty(emailValidCode) || !emailValidCode.equals(resetPwdForm.getValidCode())) {
            throw new ServiceException(message("activate.email.expired"));
        }

        setUserPassword(user, resetPwdForm.getNewPassword());
        user.setUpdateTime(new Date());
        userApplicantMapper.updateByPrimaryKeySelective(user);
    }

    /**
     * ????????????????????????
     * @param modifyPwdForm
     */
    @SysLog
    public void modifyPassword(ModifyPwdForm modifyPwdForm){
        UserApplicant user = userApplicantMapper.selectByPrimaryKey(curUser().getId());
        if(!StringUtils.equals(user.getPassword(), encodePassword(modifyPwdForm.getOldPassword(), user.getSalt()))){
            throw new ServiceException(message("modify.old.pwd.error"));
        }
        setUserPassword(user, modifyPwdForm.getNewPassword());
        user.setUpdateTime(new Date());
        userApplicantMapper.updateByPrimaryKeySelective(user);
    }

    /**
     * ????????????????????????
     * @param registerForm
     * @return
     */
    @Transactional
    public UserApplicantVo bindThirdAccount(BindAccountForm registerForm) {
        // ???????????????????????????????????????????????????
        // checkRepeatEmail(registerForm.getEmail());

        final String validCodeKey = RedisKeys.VALIDATE_EMAIL_PREFIX + registerForm.getEmail();
        if (!registerForm.getValidCode().equals(stringRedisTemplate.opsForValue().get(validCodeKey))) {
            throw new ServiceException(message("activate.email.expired"));
        }

        // ?????????????????????????????????????????????????????????????????????
        UserApplicant user = userApplicantMapper.selectByEmail(registerForm.getEmail());
        if (user != null) {
            final String encodePass = encodePassword(registerForm.getPassword(), user.getSalt());
            if (!org.apache.commons.lang3.StringUtils.equals(encodePass, user.getPassword())) {
                throw new ServiceException(message("login.validate.fail"));
            }
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

            if(user == null) {
                //?????????????????????????????????????????????
                user = newUser(registerForm);
            }
            //???????????????
            auth.setUserId(user.getId());
            authMapper.updateByPrimaryKeySelective(auth);
            //??????????????????
            UserApplicant curUser = userApplicantMapper.selectByPrimaryKey(curUser().getId());
            curUser.setDelFlag((byte) 1);
            userApplicantMapper.updateByPrimaryKeySelective(curUser);
//            user = userApplicantMapper.selectByEmail(registerForm.getEmail());
//            user.setEmail(registerForm.getEmail());
//            user.setSubscribeFlag(registerForm.getSubscribeFlag());
//            setUserPassword(user, registerForm.getPassword());
//            user.setUpdateTime(new Date());
//            userApplicantMapper.updateByPrimaryKeySelective(user);
            //????????????????????????
            UserApplicantVo loginUser = new UserApplicantVo().asVo(user);
            stringRedisTemplate.opsForValue().set(curUser().getToken(), JSON.toJSONString(loginUser), TOKEN_EXPIRED_SECONDS, TimeUnit.SECONDS);
            loginUser.setToken(curUser().getToken());
            return loginUser;
        } finally {
            stringRedisTemplate.delete(validCodeKey);
        }
    }

    /**
     * ?????????????????????
     * @param authUser
     * @return
     */
    @Transactional
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
        UserApplicant user;
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
            user = userApplicantMapper.selectByPrimaryKey(auth.getUserId());

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
        UserApplicant user;
        if (CollectionUtils.isEmpty(authList)) {
            RegisterForm registerForm = new RegisterForm();
            registerForm.setUserType(UserType.GENERAL.value);
            registerForm.setName(authUser.getNickname());

            if(StringUtils.isNotEmpty(authUser.getEmail())){
                if(userApplicantMapper.selectByEmail(authUser.getEmail()) == null){
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
            user = userApplicantMapper.selectByPrimaryKey(auth.getUserId());

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
        UserApplicant user;
        if (CollectionUtils.isEmpty(authList)) {
            RegisterForm registerForm = new RegisterForm();
            registerForm.setUserType(UserType.GENERAL.value);
            registerForm.setName(authUser.getNickname());

            if(StringUtils.isNotEmpty(authUser.getEmail())){
                if(userApplicantMapper.selectByEmail(authUser.getEmail()) == null){
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
            user = userApplicantMapper.selectByPrimaryKey(auth.getUserId());

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
     * ????????????
     */
    public void logout() {
        if (curUser() != null) {
            UserApplicant user  = userApplicantMapper.selectByPrimaryKey(curUser().getId());
            if(user != null){
                stringRedisTemplate.delete(user.getToken());
            }
        }
    }

    /**
     * ????????????????????????
     * ?????????????????????????????????
     * @param registerForm
     * @return
     */
    private UserApplicant newUser(RegisterForm registerForm) {
        UserApplicant user = new UserApplicant();
        user.setId(AppUtils.nextId());
        user.setEmail(registerForm.getEmail());
        user.setSubscribeFlag(registerForm.getSubscribeFlag());
        user.setStatus(UserStatus.NORMAL.value);
        user.setType(UserType.GENERAL.value);
        user.setGender(registerForm.getGender());

        //?????????????????????
        if(StringUtils.isNotBlank(registerForm.getName()))
            user.setName(registerForm.getName());
        else{
            user.setName("??????" + user.getId() % 100000);
        }

        //??????????????????
        Avatar options = new Avatar();
        options.setDelFlag(Bool.FALSE);
        final List<Avatar> avatars = avatarMapper.selectAndList(options);
        if(avatars.size() > 0){
            final Avatar avatar = avatars.get(RandomUtil.randomInt(avatars.size() - 1));
            user.setAvatar(avatar.getAvatarUrl());
        }

        user.setSubscribeFlag(registerForm.getSubscribeFlag());
        setUserPassword(user, registerForm.getPassword());
        userApplicantMapper.insertSelective(user);
        return user;
    }

    /**
     * ???Token?????????Redis????????????
     * @param loginUser
     */
    private void saveUserToken(UserApplicant loginUser) {
        final String token = loginUser.getId() + RandomStringUtils.randomAlphanumeric(20);
        loginUser.setToken(token);
        stringRedisTemplate.opsForValue().set(token, JSON.toJSONString(loginUser), TOKEN_EXPIRED_SECONDS, TimeUnit.SECONDS);

        //???token???????????????
        userApplicantMapper.updateByPrimaryKeySelective(loginUser);

        LoginLog loginLog = new LoginLog();
        loginLog.setIp(RequestUtils.getClientIP(AppUtils.request()));
        loginLog.setUserId(loginUser.getId());
        loginLogMapper.insertSelective(loginLog);
    }

    /**
     * ???????????????????????????????????????????????????
     * @param email
     */
    public void checkRepeatEmail(String email) {
        UserApplicant user = userApplicantMapper.selectByEmail(email);
        if (user != null) {
            throw new ServiceException(message("register.email.repeat"));
        }
    }

    /**
     * ??????????????????
     *
     * @param user
     * @param password
     */
    private void setUserPassword(UserApplicant user, String password) {
        user.setSalt(RandomStringUtils.randomAlphanumeric(40));
        final String encodePass = encodePassword(password, user.getSalt());
        user.setPassword(encodePass);
    }

    private String encodePassword(String password, String salt) {
        return DigestUtils.sha256Hex(password + salt);
    }


    /**
     * ??????n?????????????????????????????????
     *
     * @param days ??????
     * @return
     */
    public List<Long> selectStatInfo(Integer days) {
        String format = "yyyy-MM-dd";
        String formatMysql = "%Y-%m-%d";
        String startDate = TimeUtils.getBeforeDate(days, format);
        String endDate = TimeUtils.getDateNow(format);
        List<String> dateList = TimeUtils.getDateList(startDate, endDate, format);
        dateList = dateList.subList(0, dateList.size() - 1);
        List<Long> result = new ArrayList<>();
        for (String date : dateList) {
            result.add(userApplicantMapper.selectStatInfoInDate(formatMysql, date));
        }
        return result;
        //        List<Map<String, Object>> maps = userApplicantMapper.selectStatInfo(days);
//        return formatStatInfo(maps, days);
    }



    /**
     * ???????????????????????????????????????
     *
     * @param date ?????? yyyy/MM
     * @return
     */
    public List<Long> selectSpecifyMonthStatInfo(String date) {
        String format = "yyyy-MM-dd";
        String formatMysql = "%Y-%m-%d";
        String dStart = date + "-01";
        String dEnd = date + "-" + TimeUtils.getDaysOfMonth(date);
        List<String> dateList = TimeUtils.getDateList(dStart, dEnd, format);
        dateList = dateList.subList(0, dateList.size() - 1);
        List<Long> result = new ArrayList<>();
        for (String d : dateList) {
            result.add(userApplicantMapper.selectStatInfoInDate(formatMysql, d));
        }
        return result;

//        List<Map<String, Object>> maps = userApplicantMapper.selectSpecifyMonthStatInfo(date);
//        return formatStatInfo(maps, date);
    }


    /**
     * ???????????????????????????????????????
     *
     * @param date ?????? yyyy
     * @return
     */
    public List<Long> selectSpecifyYearStatInfo(String date) {
        String format = "yyyy-MM";
        String formatMysql = "%Y-%m";
        String dStart = date + "-01";
        String dEnd = date + "-12";
        List<String> dateList = TimeUtils.getDateList(dStart, dEnd, format);
        dateList = dateList.subList(0, dateList.size() - 1);
        List<Long> result = new ArrayList<>();
        for (String d : dateList) {
            result.add(userApplicantMapper.selectStatInfoInDate(formatMysql, d));
        }
        return result;

//        List<Map<String, Object>> maps = userApplicantMapper.selectSpecifyYearStatInfo(dateStr);
//        return formatYearStatInfo(maps, dateStr);
    }



    /**
     * ????????????????????????
     *
     * @return
     */
    public Date selectFirstCreateTime() {
        return userApplicantMapper.selectFirstCreateTime();
    }



    /**
     * ???map?????????????????????list
     * ???????????????????????????????????????list????????????null
     *
     * @param maps
     * @param date
     * @return
     */
    private List<Long> formatStatInfo(List<Map<String, Object>> maps, String date) {
        Integer days = getDaysOfMonth(date);
        if (maps == null || maps.size() == 0) {
            return getEmptyList(days);
        }
        List<Long> stats = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date start = null;
        Date end = null;
        try {
            start = sdf.parse(date + "-1");
            end = sdf.parse(date + "-" + days);
        } catch (ParseException e) {
            throw new ServiceException(message("api.error.data.date"));
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);

        Date indexDate = null;
        for (int i = 0; (indexDate = calendar.getTime()).compareTo(end) <= 0 ;) {
            LocalDate l1 = new LocalDate(new DateTime(indexDate));
            LocalDate l2 = new LocalDate(new DateTime(maps.get(i).get("create_time")));
            if (l1.equals(l2)) { // ??????
                stats.add((Long) maps.get(i).get("count"));
                if (i < maps.size() - 1) i++;
            } else {
                stats.add(0L);
            }
            calendar.add(Calendar.DAY_OF_YEAR, 1); // start + 1???
        }

        return stats;
    }


    private List<Long> formatYearStatInfo(List<Map<String, Object>> maps, String date) {
        if (maps == null || maps.size() == 0) {
            return getEmptyList(12);
        }
        List<Long> stats = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date start = null;
        Date end = null;
        try {
            start = sdf.parse(date + "-1");
            end = sdf.parse(date + "-12");

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(start);

            Date indexDate = null;
            for (int i = 0; (indexDate = calendar.getTime()).before(end) ;) {
                Date d2 = sdf.parse(String.valueOf(maps.get(i).get("create_time")));
                if (indexDate.compareTo(d2) == 0) {
                    stats.add((Long) maps.get(i).get("count"));
                    if (i < maps.size() - 1) i++;
                } else {
                    stats.add(0L);
                }
                calendar.add(Calendar.MONTH, 1); // start + 1???
            }
        } catch (ParseException e) {
            throw new ServiceException(message("api.error.data.date"));
        }
        return stats;
    }


    /**
     * ???map?????????????????????list
     * ???????????????????????????????????????list????????????null
     *
     * @param maps
     * @param days
     * @return
     */
    private List<Long> formatStatInfo(List<Map<String, Object>> maps, Integer days) {
        if (maps == null || maps.size() == 0) {
            return getEmptyList(days);
        }
        List<Long> stats = new ArrayList<>();

        Date end = new Date(System.currentTimeMillis()); // ????????????
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(end);
        calendar.add(Calendar.DAY_OF_YEAR, -days); // ????????????

        Date indexDate = null;
        for (int i = 0; (indexDate = calendar.getTime()).before(end); ) {
            LocalDate l1 = new LocalDate(new DateTime(indexDate));
            LocalDate l2 = new LocalDate(new DateTime(maps.get(i).get("create_time")));

            if (l1.equals(l2)) { // ??????
                stats.add((Long) maps.get(i).get("count"));
                if (i < maps.size() - 1) i++;
            } else { // ?????????
                stats.add(0L);
            }

            calendar.add(Calendar.DAY_OF_YEAR, 1); // start + 1???
        }
        return stats;
    }


    /**
     * ???????????????????????????
     *
     * @param date
     * @return
     * @throws ParseException
     */
    private Integer getDaysOfMonth(String date) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new SimpleDateFormat("yyyy-MM").parse(date));
            return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Long> getEmptyList(Integer days) {
        List<Long> list = new ArrayList<>();
        for (int i = 0; i < days; i++) {
            list.add(0L);
        }
        return list;
    }
}
