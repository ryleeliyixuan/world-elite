package com.worldelite.job.api;

import com.google.code.kaptcha.Producer;
import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.context.SessionKeys;
import com.worldelite.job.controller.BaseController;
import com.worldelite.job.entity.UserApplicant;
import com.worldelite.job.form.*;
import com.worldelite.job.service.UserApplicantService;
import com.worldelite.job.service.UserExpectJobService;
import com.worldelite.job.util.ResponseUtils;
import com.worldelite.job.vo.*;
import io.github.yedaxia.apidocs.ApiDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Email;

/**
 * 普通用户操作接口
 * @author 邓集阶
 */
@RestController
@RequestMapping("/api/userapplicant/")
@Validated
public class UserApplicantApi extends BaseController {

    @Autowired
    private UserApplicantService userApplicantService;

    @Autowired
    private Producer captchaProducer;

    @Autowired
    private UserExpectJobService userExpectJobService;

    /**
     * 使用邮箱注册新用户
     * @param registerForm
     * @return
     */
    @PostMapping("register")
    @ApiDoc
    public ApiResult<UserVo> register(@Valid @RequestBody RegisterForm registerForm){
        UserApplicantVo userVo = userApplicantService.register(registerForm);
        return ApiResult.ok(userVo);
    }

    /**
     * 检查email注册
     * @param email 邮箱
     * @return
     */
    @ApiDoc
    @GetMapping("check-email")
    public ApiResult checkEmailExists(@Email String email){
        userApplicantService.checkRepeatEmail(email);
        return ApiResult.ok();
    }

    /**
     * 通过邮箱登录
     *
     * @param loginForm
     * @return
     */
    @ApiDoc
    @PostMapping("email-login")
    public ApiResult<UserApplicantVo> loginWithEmail(@Valid @RequestBody LoginForm loginForm){
        UserApplicantVo loginUser = userApplicantService.emailLogin(loginForm);
        return ApiResult.ok(loginUser);
    }

    /**
     * 保存用户求职意向
     * @param expectJobForm
     * @return
     */
    @RequireLogin(allow = UserType.GENERAL)
    @PostMapping("save-expect-job")
    @ApiDoc
    public ApiResult<UserExpectJobVo> saveUserExpectJob(@RequestBody UserExpectJobForm expectJobForm){
        expectJobForm.setUserId(curUser().getId());
        UserExpectJobVo userExpectJobVo = userExpectJobService.saveUserExpectJob(expectJobForm);
        return ApiResult.ok(userExpectJobVo);
    }

    /**
     * 获取我的信息
     * @return
     */
    @RequireLogin
    @GetMapping("my-info")
    @ApiDoc
    public ApiResult<UserApplicantVo> myInfo(){
        UserApplicantVo loginUser = userApplicantService.getUserInfo(curUser().getId());
        return ApiResult.ok(loginUser);
    }

    /**
     * 修改邮箱
     * @param session
     * @param modifyEmailForm
     * @return
     */
    @RequireLogin
    @PostMapping("modify-email")
    @ApiDoc
    public ApiResult modifyEmail(HttpSession session, @Valid @RequestBody ModifyEmailForm modifyEmailForm){
        final String captcha = (String)session.getAttribute(SessionKeys.KAPTCHA_SESSION_KEY);
        // 立即删除
        session.removeAttribute(SessionKeys.KAPTCHA_SESSION_KEY);
        if(captcha == null || !captcha.equalsIgnoreCase(modifyEmailForm.getImgValidCode())){
            return ApiResult.fail(message("imageValidCode.invalid"));
        }
        userApplicantService.modifyUserEmail(modifyEmailForm);
        return ApiResult.ok();
    }

    /**
     * 重设密码
     *
     * @return
     */
    @ApiDoc
    @PostMapping("reset-pwd")
    public ApiResult resetPassword(HttpSession session, @Valid @RequestBody ResetPwdForm resetPwdForm){
        final String captcha = (String)session.getAttribute(SessionKeys.KAPTCHA_SESSION_KEY);
        // 立即删除
        session.removeAttribute(SessionKeys.KAPTCHA_SESSION_KEY);
        if(captcha == null || !captcha.equalsIgnoreCase(resetPwdForm.getImgValidCode())){
            return ApiResult.fail(message("imageValidCode.invalid"));
        }
        userApplicantService.resetPassword(resetPwdForm);
        return ApiResult.ok();
    }

    /**
     * 修改密码
     * @param modifyPwdForm
     * @return
     */
    @ApiDoc
    @RequireLogin
    @PostMapping("modify-pwd")
    public ApiResult modifyPassword(HttpSession session, @Valid @RequestBody ModifyPwdForm modifyPwdForm){
        final String captcha = (String)session.getAttribute(SessionKeys.KAPTCHA_SESSION_KEY);
        // 立即删除
        session.removeAttribute(SessionKeys.KAPTCHA_SESSION_KEY);
        if(captcha == null || !captcha.equalsIgnoreCase(modifyPwdForm.getImgValidCode())){
            return ApiResult.fail(message("imageValidCode.invalid"));
        }
        if(modifyPwdForm.getNewPassword().endsWith(modifyPwdForm.getOldPassword())){
            return ApiResult.fail(message("modify.same.pwd.error"));
        }
        userApplicantService.modifyPassword(modifyPwdForm);
        return ApiResult.ok();
    }

    /**
     * 绑定第三方账号
     * @return
     */
    @PostMapping("bind-account")
    @RequireLogin(allow = UserType.GENERAL)
    public ApiResult bindAccount(@Valid @RequestBody BindAccountForm bindAccountForm){
        UserApplicantVo loginUser = userApplicantService.bindThirdAccount(bindAccountForm);
        return ApiResult.ok(loginUser);
    }

    /**
     * 退出登录
     *
     * @return
     */
    @PostMapping("logout")
    public ApiResult logout(){
        userApplicantService.logout();
        return ApiResult.ok();
    }
}
