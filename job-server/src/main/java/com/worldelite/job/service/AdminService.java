package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.worldelite.job.anatation.SysLog;
import com.worldelite.job.constants.UserStatus;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.constants.VerificationStatus;
import com.worldelite.job.context.RedisKeys;
import com.worldelite.job.entity.*;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.*;
import com.worldelite.job.mapper.AdminMapper;
import com.worldelite.job.mapper.LoginLogMapper;
import com.worldelite.job.mapper.UserApplicantMapper;
import com.worldelite.job.mapper.UserCorporateMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.util.RequestUtils;
import com.worldelite.job.vo.*;
import org.apache.commons.codec.digest.DigestUtils;
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
 * 管理员服务类
 * @author 邓集阶
 */
@Service
public class AdminService extends BaseService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private UserApplicantMapper userApplicantMapper;

    @Autowired
    private UserCorporateMapper userCorporateMapper;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyVerificationService companyVerificationService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private MessageService messageService;

    @Autowired
    private LoginLogMapper loginLogMapper;

    @Value("${token.expired.seconds}")
    private Long TOKEN_EXPIRED_SECONDS;

    @Value("${activate.expired.seconds}")
    private Long ACTIVATE_EXPIRED_SECONDS;

    /**
     * 获取普通用户列表
     * @param listForm
     * @return
     */
    public PageResult<UserApplicantVo> getUserList(UserListForm listForm) {
        UserOptions options = new UserOptions();
        BeanUtil.copyProperties(listForm, options);
        AppUtils.setPage(listForm);
        Page<UserApplicant> userPage = (Page<UserApplicant>) userApplicantMapper.selectAndList(options);
        PageResult<UserApplicantVo> pageResult = new PageResult<>(userPage);
        pageResult.setList(AppUtils.asVoList(userPage,UserApplicantVo.class));
        return pageResult;
    }

    /**
     * 获取企业用户列表
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
        Page<UserCorporate> userPage = (Page<UserCorporate>) userCorporateMapper.selectAndList(options);
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
                companyUserVo = companyService.getCompanyUser2(user.getId());
            }

            if(companyUserVo != null){
                companyUserVoList.add(companyUserVo);
            }
        }
        pageResult.setList(companyUserVoList);
        return pageResult;
    }

    /**
     * 获取管理员用户列表
     * @param listForm
     * @return
     */
    public PageResult<AdminVo> getAdminUserList(UserListForm listForm) {
        UserOptions options = new UserOptions();
        BeanUtil.copyProperties(listForm, options);
        AppUtils.setPage(listForm);
        Page<Admin> userPage = (Page<Admin>) adminMapper.selectAndList(options);
        PageResult<AdminVo> pageResult = new PageResult<>(userPage);
        pageResult.setList(AppUtils.asVoList(userPage,AdminVo.class));
        return pageResult;
    }

    /**
     * 获取我的信息
     * @param id
     * @return
     */
    public AdminVo getAdminInfo(Long id) {
        Admin admin = adminMapper.selectByPrimaryKey(id);
        return admin == null ? null: new AdminVo().asVo(admin);
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
        Admin user = adminMapper.selectByPrimaryKey(curUser().getId());
        if(!user.getPassword().equals(DigestUtils.sha256Hex(modifyEmailForm.getPassword() + user.getSalt()))){
            throw new ServiceException(message("password.invalid"));
        }

        // 和原来邮箱一样
        if(user.getEmail().equals(modifyEmailForm.getNewEmail())){
            throw new ServiceException(message("modify.email.new.equals.old"));
        }

        // 新邮箱是否被注册
        Admin newEmailUser = adminMapper.selectByEmail(modifyEmailForm.getNewEmail());
        if(newEmailUser != null){
            throw new ServiceException(message("register.email.repeat"));
        }

        // 修改邮箱地址
        user.setEmail(modifyEmailForm.getNewEmail());
        user.setUpdateTime(new Date());
        adminMapper.updateByPrimaryKeySelective(user);
    }

    /**
     * 修改密码
     * @param modifyPwdForm
     */
    @SysLog
    public void modifyPassword(ModifyPwdForm modifyPwdForm){
        Admin user = adminMapper.selectByPrimaryKey(curUser().getId());
        if(!StringUtils.equals(user.getPassword(), encodePassword(modifyPwdForm.getOldPassword(), user.getSalt()))){
            throw new ServiceException(message("modify.old.pwd.error"));
        }
        setAdminPassword(user, modifyPwdForm.getNewPassword());
        user.setUpdateTime(new Date());
        adminMapper.updateByPrimaryKeySelective(user);
    }

    /**
     * 保存管理员
     * @param adminForm
     */
    public void saveAdmin(AdminForm adminForm){
        Admin admin = adminMapper.selectByEmail(adminForm.getEmail());
        if(admin != null){
            throw new ServiceException("该email已经注册用户，不能注册管理员");
        }
        admin = new Admin();
        admin.setName(adminForm.getName());
        admin.setEmail(adminForm.getEmail());
        admin.setStatus(UserStatus.NORMAL.value);
        admin.setType(UserType.ADMIN.value);
        setAdminPassword(admin, adminForm.getPassword());
        adminMapper.insertSelective(admin);
    }

    /**
     * 删除管理员
     * @param adminId
     */
    public void deleteAdmin(Long adminId){
        Admin admin = adminMapper.selectByPrimaryKey(adminId);
        if(admin != null && admin.getType() == UserType.ADMIN.value){
            adminMapper.deleteByPrimaryKey(adminId);
        }
    }

    /**
     * 修改普通用户状态
     *
     * @param userForm
     */
    @SysLog
    public void modifyUserApplicantStatus(StatusForm userForm) {
        UserApplicant user = userApplicantMapper.selectByPrimaryKey(userForm.getUserId());
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
            userApplicantMapper.updateByPrimaryKeySelective(user);

            // 移除token，让用户重新登录
            //authService.removeUserToken(user.getId());
        }
    }

    /**
     * 修改企业用户状态
     *
     * @param userForm
     */
    @SysLog
    public void modifyUserCorporateStatus(StatusForm userForm) {
        UserCorporate user = userCorporateMapper.selectByPrimaryKey(userForm.getUserId());
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
            userCorporateMapper.updateByPrimaryKeySelective(user);

            // 移除token，让用户重新登录
            //authService.removeUserToken(user.getId());
        }
    }

    /**
     * 管理员的邮箱登录
     * @param loginForm
     * @return
     */
    public AdminVo emailLogin(LoginForm loginForm) {
        Admin admin = adminMapper.selectByEmail(loginForm.getEmail());
        if (admin == null) {
            throw new ServiceException(message("login.validate.fail"));
        }
        //管理员不会被拉入黑名单
        //如果后续管理员也有黑名单功能，这里的注释可以去掉
        //if (admin.getStatus() == UserStatus.BLACK.value) {
        //    throw new ServiceException(message("user.black.list"));
        //}
        final String encodePass = encodePassword(loginForm.getPassword(), admin.getSalt());
        if (!org.apache.commons.lang3.StringUtils.equals(encodePass, admin.getPassword())) {
            throw new ServiceException(message("login.validate.fail"));
        }
        saveAdminToken(admin);
        AdminVo loginUser =  new AdminVo().asVo(admin);
        loginUser.setToken(admin.getToken());
        return loginUser;
    }

    /**
     * 管理员退出登录
     */
    public void logout() {
        if (curUser() != null) {
            Admin admin  = adminMapper.selectByPrimaryKey(curUser().getId());
            if(admin != null){
                stringRedisTemplate.delete(admin.getToken());
            }
        }
    }


    /**
     * 生成管理员密码
     * @param admin
     * @param password
     */
    private void setAdminPassword(Admin admin, String password) {
        admin.setSalt(RandomStringUtils.randomAlphanumeric(40));
        final String encodePass = encodePassword(password, admin.getSalt());
        admin.setPassword(encodePass);
    }

    private String encodePassword(String password, String salt) {
        return DigestUtils.sha256Hex(password + salt);
    }

    /**
     * 保存用户登录凭证到cookie
     * 以维持会话
     * @param loginUser
     */
    private void saveAdminToken(Admin loginUser) {
        final String token = loginUser.getId() + RandomStringUtils.randomAlphanumeric(20);
        loginUser.setToken(token);
        stringRedisTemplate.opsForValue().set(token, JSON.toJSONString(loginUser), TOKEN_EXPIRED_SECONDS, TimeUnit.SECONDS);

        //把token写入数据库
        adminMapper.updateByPrimaryKeySelective(loginUser);

        LoginLog loginLog = new LoginLog();
        loginLog.setIp(RequestUtils.getClientIP(AppUtils.request()));
        loginLog.setUserId(loginUser.getId());
        loginLogMapper.insertSelective(loginLog);
    }

}
