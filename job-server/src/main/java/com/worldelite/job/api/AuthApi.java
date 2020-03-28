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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Email;

/**
 * @author yeguozhong yedaxia.github.com
 */
@RestController
@RequestMapping("/api/auth")
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
    public ApiResult register(@Valid @RequestBody RegisterForm registerForm){
        UserVo userVo = authService.register(registerForm);
        return ApiResult.ok(userVo);
    }

    /**
     * 检查 email 是否已经被注册
     * @param email
     * @return
     */
    @GetMapping("check-email")
    public ApiResult checkEmailExists(@Email String email){
        authService.checkRepeatEmail(email);
        return ApiResult.ok();
    }

    /**
     * 获取邮箱验证码
     * @param email
     * @return
     */
    @GetMapping("get-email-code")
    public ApiResult activateEmail(@RequestParam @Email String email){
        authService.sendEmailValidCode(email);
        return ApiResult.ok();
    }

    /**
     * 获取图片验证码
     */
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
    @PostMapping("email-login")
    public ApiResult loginWithEmail(@Valid @RequestBody LoginForm loginForm){
        UserVo loginUser = authService.emailLogin(loginForm);
        return ApiResult.ok(loginUser);
    }

    /**
     * 重设密码
     *
     * @return
     */
    @PostMapping("reset-pwd")
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
     * 绑定第三方账号
     * @return
     */
    @PostMapping("bind-account")
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
    public ApiResult logout(){
        authService.logout();
        return ApiResult.ok();
    }
}
