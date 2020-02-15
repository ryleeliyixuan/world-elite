package com.worldelite.job.service;

import com.worldelite.job.anatation.SysLog;
import com.worldelite.job.constants.Bool;
import com.worldelite.job.constants.UserStatus;
import com.worldelite.job.constants.VerificationStatus;
import com.worldelite.job.context.RedisKeys;
import com.worldelite.job.entity.Company;
import com.worldelite.job.entity.CompanyUser;
import com.worldelite.job.entity.CompanyVerification;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.CompanyVerifyForm;
import com.worldelite.job.form.EmailForm;
import com.worldelite.job.form.UserForm;
import com.worldelite.job.mapper.CompanyMapper;
import com.worldelite.job.mapper.CompanyUserMapper;
import com.worldelite.job.mapper.CompanyVerificationMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.ApiCode;
import com.worldelite.job.vo.CompanyVerificationVo;
import com.worldelite.job.vo.UserVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.rowset.serial.SerialException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Service
public class CompanyVerificationService extends BaseService{

    @Autowired
    private CompanyVerificationMapper verificationMapper;

    @Autowired
    private CompanyUserMapper companyUserMapper;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private IEmailService emailService;

    @Autowired
    private ConfigService configService;

    /**
     * 获取用户的审核信息
     * @param userId
     * @return
     */
    public CompanyVerificationVo getVerificationInfo(Long userId){
        CompanyVerification companyVerification = verificationMapper.selectByUserId(userId);
        return companyVerification != null? new CompanyVerificationVo().asVo(companyVerification): null;
    }

    /**
     * 提交验证信息
     *
     * @param companyVerifyForm
     */
    @Transactional
    public void saveVerification(CompanyVerifyForm companyVerifyForm){
        CompanyVerification companyVerification = verificationMapper.selectByUserId(curUser().getId());
        if(companyVerification!= null && companyVerification.getStatus() == VerificationStatus.PASS.value){
            throw new ServiceException(ApiCode.INVALID_OPERATION);
        }
        if(companyVerification == null){
            companyVerification = new CompanyVerification();
        }
        companyVerification.setUserId(curUser().getId());
        companyVerification.setRealName(companyVerifyForm.getName());
        companyVerification.setCompany(companyVerifyForm.getCompany());
        companyVerification.setCompanyLicensePic(AppUtils.getOssKey(companyVerifyForm.getCompanyLicensePic()));
        companyVerification.setPost(companyVerifyForm.getPost());
        companyVerification.setStatus(VerificationStatus.REVIEWING.value);
        companyVerification.setIdCard(companyVerifyForm.getIdCard());
        companyVerification.setIdCardPic(AppUtils.getOssKey(companyVerifyForm.getIdCardPic()));
        if(companyVerification.getId() != null){
            companyVerification.setUpdateTime(new Date());
            verificationMapper.updateByPrimaryKeySelective(companyVerification);
        }else{
            verificationMapper.insertSelective(companyVerification);
        }

        //修改用户名
        UserForm userForm = new UserForm();
        userForm.setId(curUser().getId());
        userForm.setName(companyVerifyForm.getName());
        userService.modifyUser(userForm);
    }

    /**
     * 通过审核
     *
     **/
    @SysLog
    @Transactional
    public void passVerification(Long userId){
        CompanyVerification companyVerification = verificationMapper.selectByUserId(userId);
        if(companyVerification == null || companyVerification.getStatus() != VerificationStatus.REVIEWING.value){
            throw new ServiceException(ApiCode.INVALID_OPERATION);
        }
        companyVerification.setStatus(VerificationStatus.PASS.value);
        companyVerification.setUpdateTime(new Date());
        verificationMapper.updateByPrimaryKeySelective(companyVerification);

        Company companyOptions = new Company();
        companyOptions.setFullName(companyVerification.getCompany());
        List<Company> companyList = companyMapper.selectAndList(companyOptions);

        Company company;
        if(CollectionUtils.isEmpty(companyList)){
            Company newCompany = new Company();
            newCompany.setId(AppUtils.nextId());
            newCompany.setFullName(companyVerification.getCompany());
            companyMapper.insertSelective(newCompany);
            company = newCompany;
        }else{
            company = companyList.get(0);
        }

        CompanyUser companyUserOptions = new CompanyUser();
        companyUserOptions.setCompanyId(company.getId());
        companyUserOptions.setUserId(userId);
        List<CompanyUser> companyUserList = companyUserMapper.selectAndList(companyUserOptions);
        if(CollectionUtils.isEmpty(companyUserList)){
            CompanyUser companyUser = new CompanyUser();
            companyUser.setUserId(userId);
            companyUser.setPost(companyVerification.getPost());
            companyUser.setCompanyId(company.getId());
            companyUserMapper.insertSelective(companyUser);
        }

        // 发送审核成功邮件
        UserVo userVo = userService.getUserInfo(userId);
        sendVerificationPassEmail(userVo.getEmail());
    }

    /**
     * 拒绝通过审核
     *
     * @param userId
     * @param reason 拒绝原因
     */
    @SysLog
    public void rejectVerification(Long userId, String reason){
        CompanyVerification companyVerification = verificationMapper.selectByUserId(userId);
        if(companyVerification == null || companyVerification.getStatus() != VerificationStatus.REVIEWING.value){
            throw new ServiceException(ApiCode.INVALID_OPERATION);
        }
        companyVerification.setStatus(VerificationStatus.REJECT.value);
        companyVerification.setRemark(reason);
        companyVerification.setUpdateTime(new Date());
        verificationMapper.updateByPrimaryKeySelective(companyVerification);

        //发送审核失败邮件
        UserVo userVo = userService.getUserInfo(userId);
        sendVerificationRejectEmail(userVo.getEmail(), reason);
    }


    private void sendVerificationPassEmail(String email){
        EmailForm emailForm = new EmailForm();
        emailForm.setAddress(email);
        emailForm.setSubject(message("verification.email.subject"));
        emailForm.setEmailBody(configService.getVerificationPassEmailBody());
        emailService.sendEmail(emailForm);
    }

    private void sendVerificationRejectEmail(String email, String reason){
        EmailForm emailForm = new EmailForm();
        emailForm.setAddress(email);
        emailForm.setSubject(message("verification.email.subject"));
        emailForm.setEmailBody(configService.getVerificationRejectEmailBody(reason));
        emailService.sendEmail(emailForm);
    }
}
