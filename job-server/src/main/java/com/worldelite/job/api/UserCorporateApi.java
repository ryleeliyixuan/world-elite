package com.worldelite.job.api;

import com.google.code.kaptcha.Producer;
import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.context.SessionKeys;
import com.worldelite.job.controller.BaseController;
import com.worldelite.job.entity.OpUser;
import com.worldelite.job.entity.UserCorporate;
import com.worldelite.job.form.*;
import com.worldelite.job.service.OpUserService;
import com.worldelite.job.service.UserApplicantService;
import com.worldelite.job.service.UserCorporateService;
import com.worldelite.job.util.ResponseUtils;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.UserApplicantVo;
import com.worldelite.job.vo.UserCorporateVo;
import com.worldelite.job.vo.UserVo;
import io.github.yedaxia.apidocs.ApiDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Email;

/**
 * 企业用户操作接口
 * @author 邓集阶
 */
@RestController
@RequestMapping("/api/usercorporate/")
@Validated
public class UserCorporateApi extends BaseController {

    @Autowired
    private UserCorporateService userCorporateService;

    @Autowired
    private Producer captchaProducer;

    @Autowired
    private OpUserService opUserService;

    /**
     * 使用邮箱注册新用户
     * @param registerForm
     * @return
     */
    @PostMapping("register")
    @ApiDoc
    public ApiResult<UserVo> register(@Valid @RequestBody RegisterForm registerForm){
        UserCorporateVo userVo = userCorporateService.register(registerForm);
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
        userCorporateService.checkRepeatEmail(email);
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
        UserCorporateVo loginUser = userCorporateService.emailLogin(loginForm);
        return ApiResult.ok(loginUser);
    }

    /**
     * 获取我的信息
     * @return
     */
    @RequireLogin
    @GetMapping("my-info")
    @ApiDoc
    public ApiResult<UserCorporateVo> myInfo(){
        UserCorporateVo loginUser = userCorporateService.getUserInfo(curUser().getId());
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
        userCorporateService.modifyUserEmail(modifyEmailForm);
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
        userCorporateService.resetPassword(resetPwdForm);
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
        userCorporateService.modifyPassword(modifyPwdForm);
        return ApiResult.ok();
    }

    /**
     * 判断当前登录用户是否为OP帐号
     * @return
     */
    @ApiDoc
    @RequireLogin(allow = UserType.COMPANY)
    @GetMapping("check-op")
    public ApiResult<Boolean> isOp(){
        Boolean isOp = opUserService.isOp();
        return ApiResult.ok(isOp);
    }

    /**
     * 退出登录
     *
     * @return
     */
    @PostMapping("logout")
    public ApiResult logout(){
        userCorporateService.logout();
        return ApiResult.ok();
    }
}
