package com.worldelite.job.api;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.context.SessionKeys;
import com.worldelite.job.controller.BaseController;
import com.worldelite.job.form.*;
import com.worldelite.job.service.AuthService;
import com.worldelite.job.service.UserService;
import com.worldelite.job.util.ResponseUtils;
import com.worldelite.job.vo.ApiResult;
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
 * 用户验证接口
 *
 * @author yeguozhong yedaxia.github.com
 */
@RestController
@RequestMapping("/api/auth/")
@Validated
public class AuthApi extends BaseController {

    @Autowired
    private Producer captchaProducer;

    @Autowired
    private AuthService authService;

    /**
     * 邮箱注册
     *
     * @return
     */
    @PostMapping("register")
    @ApiDoc
    @Deprecated
    public ApiResult<UserVo> register(@Valid @RequestBody RegisterForm registerForm){
        UserVo userVo = authService.register(registerForm);
        return ApiResult.ok(userVo);
    }

    /**
     * 检查email注册
     * @param email 邮箱
     * @return
     */
    @ApiDoc
    @GetMapping("check-email")
    @Deprecated
    public ApiResult checkEmailExists(@Email String email){
        authService.checkRepeatEmail(email);
        return ApiResult.ok();
    }

    /**
     * 获取邮箱验证码
     *
     * @param email 邮箱
     * @return
     */
    @ApiDoc
    @GetMapping("get-email-code")
    public ApiResult activateEmail(@RequestParam @Email String email){
        authService.sendEmailValidCode(email);
        return ApiResult.ok();
    }

    /**
     * 获取图片验证码
     */
    @ApiDoc
    @GetMapping("get-captcha")
    public void getKaptchaImage(HttpSession session, HttpServletResponse response){
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        String capText = captchaProducer.createText();
        session.setAttribute(SessionKeys.KAPTCHA_SESSION_KEY, capText);
        ResponseUtils.writeAsImage(response,captchaProducer.createImage(capText), "jpg");
    }


    /**
     * 通过邮箱登录
     *
     * @param loginForm
     * @return
     */
    @ApiDoc
    @PostMapping("email-login")
    @Deprecated
    public ApiResult<UserVo> loginWithEmail(@Valid @RequestBody LoginForm loginForm){
        UserVo loginUser = authService.emailLogin(loginForm);
        return ApiResult.ok(loginUser);
    }

    /**
     * 重设密码
     *
     * @return
     */
    @ApiDoc
    @PostMapping("reset-pwd")
    @Deprecated
    public ApiResult resetPassword(HttpSession session, @Valid @RequestBody ResetPwdForm resetPwdForm){
        final String captcha = (String)session.getAttribute(SessionKeys.KAPTCHA_SESSION_KEY);
        // 立即删除
        session.removeAttribute(SessionKeys.KAPTCHA_SESSION_KEY);
        if(captcha == null || !captcha.equalsIgnoreCase(resetPwdForm.getImgValidCode())){
            return ApiResult.fail(message("imageValidCode.invalid"));
        }
        authService.resetPassword(resetPwdForm);
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
    @Deprecated
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
        authService.modifyPassword(modifyPwdForm);
        return ApiResult.ok();
    }

    /**
     * 绑定第三方账号
     * @return
     */
    @PostMapping("bind-account")
    @Deprecated
    @RequireLogin(allow = UserType.GENERAL)
    public ApiResult bindAccount(@Valid @RequestBody BindAccountForm bindAccountForm){
        UserVo loginUser = authService.bindThirdAccount(bindAccountForm);
        return ApiResult.ok(loginUser);
    }

    /**
     * 退出登录
     *
     * @return
     */
    @PostMapping("logout")
    @Deprecated
    public ApiResult logout(){
        authService.logout();
        return ApiResult.ok();
    }
}
