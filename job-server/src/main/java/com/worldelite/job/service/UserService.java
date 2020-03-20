package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.worldelite.job.anatation.SysLog;
import com.worldelite.job.constants.ConfigType;
import com.worldelite.job.constants.UserStatus;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.constants.VerificationStatus;
import com.worldelite.job.context.AttrKeys;
import com.worldelite.job.context.RedisKeys;
import com.worldelite.job.entity.*;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.*;
import com.worldelite.job.mapper.LoginLogMapper;
import com.worldelite.job.mapper.UserMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.util.RequestUtils;
import com.worldelite.job.vo.*;
import org.apache.commons.beanutils.BeanUtils;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Service
public class UserService extends BaseService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MessageService messageService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyVerificationService companyVerificationService;

    @Autowired
    private AuthService authService;

    /**
     * 获取用户信息
     *
     * @param id
     * @return
     */
    public UserVo getUserInfo(Long id) {
        User user = userMapper.selectByPrimaryKey(id);
        return new UserVo().asVo(user);
    }

    /**
     * 修改用户信息
     *
     * @param userForm
     */
    public void modifyUser(UserForm userForm) {
        User user = userMapper.selectByPrimaryKey(userForm.getId());
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
            userMapper.updateByPrimaryKeySelective(user);
            authService.updateLoginUserInfo(user);
        }
    }

    /**
     * 获取用户列表
     *
     * @return
     */
    public PageResult<UserVo> getUserList(UserListForm listForm) {
        UserOptions options = new UserOptions();
        BeanUtil.copyProperties(listForm, options);
        AppUtils.setPage(listForm);
        Page<User> userPage = (Page<User>) userMapper.selectAndList(options);
        PageResult<UserVo> pageResult = new PageResult<>(userPage);
        pageResult.setList(AppUtils.asVoList(userPage, UserVo.class));
        return pageResult;
    }

    /**
     * 获取企业用户列表
     *
     * @param listForm
     * @return
     */
    public PageResult<CompanyUserVo> getCompanyUserList(UserListForm listForm){
        UserOptions options = new UserOptions();
        BeanUtil.copyProperties(listForm, options);
        // 待审核用户
        if(listForm.getStatus() == UserStatus.NOT_ACTIVATE.value){
            options.setVerifyStatus(VerificationStatus.REVIEWING.value);
        }
        AppUtils.setPage(listForm);
        Page<User> userPage = (Page<User>) userMapper.selectAndList(options);
        PageResult<CompanyUserVo> pageResult = new PageResult<>(userPage);
        List<CompanyUserVo> companyUserVoList = new ArrayList<>(userPage.size());
        for(User user : userPage){
            CompanyUserVo companyUserVo = null;
            if(user.getStatus() == UserStatus.NOT_ACTIVATE.value){
                CompanyVerificationVo verificationVo = companyVerificationService.getVerificationInfo(user.getId());
                if(verificationVo != null){
                    companyUserVo = new CompanyUserVo();
                    companyUserVo.asVo(user);
                    companyUserVo.setPost(verificationVo.getPost());
                    CompanyVo companyVo = new CompanyVo();
                    companyVo.setFullName(verificationVo.getCompany());
                    companyUserVo.setCompany(companyVo);
                }
            }else{
                companyUserVo = companyService.getCompanyUser(user.getId());
            }

            if(companyUserVo != null){
                companyUserVoList.add(companyUserVo);
            }
        }
        pageResult.setList(companyUserVoList);
        return pageResult;
    }

    /**
     * 修改用户状态
     *
     * @param userForm
     */
    @SysLog
    public void modifyUserStatus(StatusForm userForm) {
        User user = userMapper.selectByPrimaryKey(userForm.getUserId());
        if (user != null) {

            // 站内消息
            Message message = new Message();
            message.setToUser(user.getId());
            if (user.getStatus() == UserStatus.NORMAL.value && userForm.getStatus() == UserStatus.BLACK.value) {
                message.setContent(message("message.status.black", userForm.getReason()));
            } else if (user.getStatus() == UserStatus.BLACK.value && userForm.getStatus() == UserStatus.NORMAL.value) {
                message.setContent(message("message.status.recover"));
            }
            if(message.getContent() != null){
                messageService.sendMessage(message);
            }

            user.setStatus(userForm.getStatus());
            user.setUpdateTime(new Date());
            userMapper.updateByPrimaryKeySelective(user);
        }
    }

    /**
     * 修改用户 email
     *
     * @param modifyEmailForm
     */
    public void modifyUserEmail(ModifyEmailForm modifyEmailForm){

        // 新邮箱验证码
        final String emailValidCode = stringRedisTemplate.opsForValue().get(RedisKeys.VALIDATE_EMAIL_PREFIX + modifyEmailForm.getNewEmail());
        if(org.springframework.util.StringUtils.isEmpty(emailValidCode) || !emailValidCode.equals(modifyEmailForm.getValidCode())){
            throw new ServiceException(message("activate.email.expired"));
        }


        // 密码是否正确
        User user = userMapper.selectByPrimaryKey(curUser().getId());
        if(!user.getPassword().equals(DigestUtils.sha256Hex(modifyEmailForm.getPassword() + user.getSalt()))){
            throw new ServiceException(message("password.invalid"));
        }

        // 和原来邮箱一样
        if(user.getEmail().equals(modifyEmailForm.getNewEmail())){
            throw new ServiceException(message("modify.email.new.equals.old"));
        }

        // 新邮箱是否被注册
        User newEmailUser = userMapper.selectByEmail(modifyEmailForm.getNewEmail());
        if(newEmailUser != null){
            throw new ServiceException(message("register.email.repeat"));
        }

        // 修改邮箱地址
        user.setEmail(modifyEmailForm.getNewEmail());
        user.setUpdateTime(new Date());
        userMapper.updateByPrimaryKeySelective(user);
    }
}
