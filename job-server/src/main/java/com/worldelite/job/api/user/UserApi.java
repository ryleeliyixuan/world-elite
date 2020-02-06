package com.worldelite.job.api.user;

import com.worldelite.job.context.anatation.RequireLogin;
import com.worldelite.job.controller.BaseController;
import com.worldelite.job.form.LoginForm;
import com.worldelite.job.form.RegisterForm;
import com.worldelite.job.service.UserService;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;

/**
 * 用户接口
 * @author yeguozhong yedaxia.github.com
 */
@RestController
@RequestMapping("/api/user")
@Validated
public class UserApi extends BaseController {

    @Autowired
    private UserService userService;

    /**
     * 邮箱注册
     *
     * @return
     */
    @PostMapping("register")
    public ApiResult register(@Valid @RequestBody RegisterForm registerForm){
        UserVo userVo = userService.register(registerForm);
        return ApiResult.ok(userVo);
    }

    /**
     * 获取邮箱验证码
     * @param email
     * @return
     */
    @GetMapping("get-email-code")
    public ApiResult activateEmail(@RequestParam @Email String email){
        userService.sendEmailValidCode(email);
        return ApiResult.ok();
    }

    /**
     * 通过邮箱登录
     *
     * @param loginForm
     * @return
     */
    @PostMapping("email-login")
    public ApiResult loginWithEmail(@Valid @RequestBody LoginForm loginForm){
        UserVo loginUser = userService.emailLogin(loginForm);
        return ApiResult.ok(loginUser);
    }

    /**
     * 获取我的信息
     * @return
     */
    @RequireLogin
    @GetMapping("my-info")
    public ApiResult<UserVo> myInfo(){
        UserVo loginUser = userService.getUserInfo(curUser().getId());
        return ApiResult.ok(loginUser);
    }

    /**
     * 退出登录
     *
     * @return
     */
    @PostMapping("logout")
    public ApiResult logout(){
        userService.logout();
        return ApiResult.ok();
    }
}
