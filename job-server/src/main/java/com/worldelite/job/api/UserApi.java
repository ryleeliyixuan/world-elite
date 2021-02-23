package com.worldelite.job.api;

import com.worldelite.job.constants.UserStatus;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.constants.VerificationStatus;
import com.worldelite.job.context.SessionKeys;
import com.worldelite.job.controller.BaseController;
import com.worldelite.job.form.*;
import com.worldelite.job.service.AuthService;
import com.worldelite.job.service.CompanyVerificationService;
import com.worldelite.job.service.UserExpectJobService;
import com.worldelite.job.service.UserService;
import com.worldelite.job.vo.*;
import io.github.yedaxia.apidocs.ApiDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Email;

/**
 * 用户接口
 * @author yeguozhong yedaxia.github.com
 */
@RestController
@RequestMapping("/api/user/")
@Validated
public class UserApi extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserExpectJobService userExpectJobService;

    /**
     * 邮箱注册
     *
     * @return
     */
    @PostMapping("register")
    @Deprecated
    public ApiResult register(@Valid @RequestBody RegisterForm registerForm){
        UserVo userVo = authService.register(registerForm);
        return ApiResult.ok(userVo);
    }

    /**
     * 获取邮箱验证码
     * @param email
     * @return
     */
    @GetMapping("get-email-code")
    @Deprecated
    public ApiResult activateEmail(@RequestParam @Email String email){
        authService.sendEmailValidCode(email);
        return ApiResult.ok();
    }

    /**
     * 通过邮箱登录
     *
     * @param loginForm
     * @return
     */
    @PostMapping("email-login")
    @Deprecated
    public ApiResult loginWithEmail(@Valid @RequestBody LoginForm loginForm){
        UserVo loginUser = authService.emailLogin(loginForm);
        return ApiResult.ok(loginUser);
    }

    /**
     * 获取我的信息
     * @return
     */
    @RequireLogin
    @GetMapping("my-info")
    @ApiDoc
    @Deprecated
    public ApiResult<UserVo> myInfo(){
        UserVo loginUser = userService.getUserInfo(curUser().getId());
        return ApiResult.ok(loginUser);
    }

    /**
     * 保存用户求职意向
     *
     * @return
     */
    @RequireLogin(allow = UserType.GENERAL)
    @PostMapping("save-expect-job")
    @ApiDoc
    @Deprecated
    public ApiResult<UserExpectJobVo> saveUserExpectJob(@RequestBody UserExpectJobForm expectJobForm){
        UserExpectJobVo userExpectJobVo = userExpectJobService.saveUserExpectJob(expectJobForm);
        return ApiResult.ok(userExpectJobVo);
    }

    /**
     * 用户列表
     * @param listForm
     * @return
     */
    @RequireLogin(allow = UserType.ADMIN)
    @GetMapping("list")
    @ApiDoc
    public ApiResult<PageResult<UserVo>> getUserList(UserListForm listForm){
        PageResult pageResult = userService.getUserList(listForm);
        return ApiResult.ok(pageResult);
    }

    /**
     * 获取企业用户列表
     *
     * @param listForm
     * @return
     */
    @RequireLogin(allow = UserType.ADMIN)
    @GetMapping("company-user-list")
    @ApiDoc
    public ApiResult<PageResult<CompanyUserVo>> listCompanyUser(UserListForm listForm){
        PageResult pageResult = userService.getCompanyUserList(listForm);
        return ApiResult.ok(pageResult);
    }

    /**
     * 保存管理员
     * @return
     */
    @RequireLogin(allow = UserType.ADMIN)
    @PostMapping("add-admin")
    @ApiDoc
    public ApiResult addAdmin(@Valid @RequestBody AdminForm adminForm){
        userService.saveAdmin(adminForm);
        return ApiResult.ok();
    }

    /**
     * 删除管理员
     * @param userId 用户Id
     * @return
     */
    @RequireLogin(allow = UserType.ADMIN)
    @PostMapping("delete-admin")
    @ApiDoc
    public ApiResult deleteAdmin(@RequestParam Long userId){
        userService.deleteAdmin(userId);
        return ApiResult.ok();
    }

    /**
     * 修改用户状态
     *
     * @return
     */
    @RequireLogin(allow = UserType.ADMIN)
    @PostMapping("modify-status")
    @ApiDoc
    public ApiResult modifyUserStatus(@RequestBody StatusForm statusForm){
        userService.modifyUserStatus(statusForm);
        return ApiResult.ok();
    }

    /**
     * 修改 email
     *
     * @return
     */
    @RequireLogin
    @PostMapping("modify-email")
    @ApiDoc
    public ApiResult modifyEmail(HttpServletRequest request, @Valid @RequestBody ModifyEmailForm modifyEmailForm){
        HttpSession session = request.getSession();
        final String captcha = (String)session.getAttribute(SessionKeys.KAPTCHA_SESSION_KEY);
        // 立即删除
        session.removeAttribute(SessionKeys.KAPTCHA_SESSION_KEY);
        if(captcha == null || !captcha.equalsIgnoreCase(modifyEmailForm.getImgValidCode())){
            return ApiResult.fail(message("imageValidCode.invalid"));
        }
        userService.modifyUserEmail(modifyEmailForm);
        return ApiResult.ok();
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
