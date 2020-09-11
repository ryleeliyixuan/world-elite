package com.worldelite.job.service;

import com.worldelite.job.constants.VerificationStatus;
import com.worldelite.job.entity.CompanyVerification;
import com.worldelite.job.form.CompanyVerifyForm;
import com.worldelite.job.mapper.CompanyVerificationMapper;
import com.worldelite.job.vo.CompanyVerificationVo;
import com.worldelite.job.vo.UserCorporateVo;
import com.worldelite.job.vo.UserVo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.Assert;

import javax.annotation.PreDestroy;

import static org.mockito.Mockito.*;

/**
 * @author yeguozhong yedaxia.github.com
 */
@SpringBootTest
@ActiveProfiles("test")
public class VerificationServiceTest {

    @SpyBean
    @Autowired
    private CompanyVerificationService verificationService;

    @Mock
    private MessageService messageService;
    @Mock
    private UserCorporateService userService;

    @Autowired
    private CompanyVerificationMapper verificationMapper;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);

        UserVo userVo = new UserVo();
        userVo.setId(1227385762795638784L);

        when(verificationService.curUser()).thenReturn(userVo);
        when(userService.getUserInfo(any())).thenReturn(new UserCorporateVo());
        doNothing().when(verificationService).sendVerificationRejectEmail(any(), any());
        doNothing().when(verificationService).sendVerificationPassEmail(any());

        //不知道什么原因@InjectMocks+spy()无效, 所以改成了@SpyBean标注verificationService，然后手动注入Mock出来的两个service
        ReflectionTestUtils.setField(verificationService, "messageService", messageService);
        ReflectionTestUtils.setField(verificationService, "userService", userService);
    }

    @AfterEach
    public void destroy(){
        final CompanyVerification companyVerification = verificationMapper.selectByUserId(verificationService.curUser().getId());
        verificationMapper.deleteByPrimaryKey(companyVerification.getId());
    }

    @Test
    public void test_save() {
        CompanyVerifyForm companyVerifyForm = new CompanyVerifyForm();
        companyVerifyForm.setCompany("WE");
        companyVerifyForm.setCompanyLicensePic("1");
        companyVerifyForm.setIdCard("1");
        companyVerifyForm.setIdCardPic("1");
        companyVerifyForm.setName("WE");
        companyVerifyForm.setPost("java");

        verificationService.saveVerification(companyVerifyForm);
        CompanyVerificationVo verificationInfo = verificationService.getVerificationInfo(verificationService.curUser().getId());
        Assert.isTrue(verificationInfo.getStatus().equals(VerificationStatus.REVIEWING.value));
    }

    @Test
    public void test_reject() {
        test_save();

        verificationService.rejectVerification(verificationService.curUser().getId(), "你注册的邮箱不是公司邮箱，请重新使用公司邮箱申请");

        CompanyVerificationVo verificationInfo = verificationService.getVerificationInfo(verificationService.curUser().getId());
        Assert.isTrue(verificationInfo.getStatus().equals(VerificationStatus.REJECT.value));
    }

    @Test
    public void test_pass() {
        test_save();

        verificationService.passVerification(verificationService.curUser().getId());

        CompanyVerificationVo verificationInfo = verificationService.getVerificationInfo(verificationService.curUser().getId());
        Assert.isTrue(verificationInfo.getStatus().equals(VerificationStatus.PASS.value));
    }
}
