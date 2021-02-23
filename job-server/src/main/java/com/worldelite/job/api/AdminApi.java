package com.worldelite.job.api;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.context.SessionKeys;
import com.worldelite.job.controller.BaseController;
import com.worldelite.job.form.*;
import com.worldelite.job.service.AdminService;
import com.worldelite.job.vo.*;
import io.github.yedaxia.apidocs.ApiDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.prefs.BackingStoreException;

/**
 * 管理员接口
 * @author 邓集阶
 */
@RestController
@RequestMapping("/api/admin/")
@Validated
public class AdminApi extends BaseController {

    @Autowired
    private AdminService adminService;

    /**
     * 获取普通用户列表
     * @param listForm
     * @return
     */
    @RequireLogin(allow = UserType.ADMIN)
    @GetMapping("list")
    @ApiDoc
    public ApiResult<PageResult<UserVo>> getUserList(UserListForm listForm){
        PageResult pageResult = adminService.getUserList(listForm);
        return ApiResult.ok(pageResult);
    }

    /**
     * 获取企业用户列表
     * @param listForm
     * @return
     */
    @RequireLogin(allow = UserType.ADMIN)
    @GetMapping("company-user-list")
    @ApiDoc
    public ApiResult<PageResult<CompanyUserVo>> listCompanyUser(UserListForm listForm){
        PageResult pageResult = adminService.getCompanyUserList(listForm);
        return ApiResult.ok(pageResult);
    }

    /**
     * 获取管理员用户列表
     * @param listForm
     * @return
     */
    @RequireLogin(allow = UserType.ADMIN)
    @GetMapping("admin-list")
    @ApiDoc
    public ApiResult<PageResult<AdminVo>> listAdminUser(UserListForm listForm){
        PageResult pageResult = adminService.getAdminUserList(listForm);
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
        adminService.saveAdmin(adminForm);
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
        adminService.deleteAdmin(userId);
        return ApiResult.ok();
    }

    /**
     * 修改普通用户状态
     *
     * @return
     */
    @RequireLogin(allow = UserType.ADMIN)
    @PostMapping("modify-applicant-status")
    @ApiDoc
    public ApiResult modifyUserApplicantStatus(@RequestBody StatusForm statusForm){
        adminService.modifyUserApplicantStatus(statusForm);
        return ApiResult.ok();
    }

    /**
     * 修改企业用户状态
     * @param statusForm
     * @return
     */
    @RequireLogin(allow = UserType.ADMIN)
    @PostMapping("modify-corporate-status")
    @ApiDoc
    public ApiResult modifyUserCorporateStatus(@RequestBody StatusForm statusForm){
        adminService.modifyUserCorporateStatus(statusForm);
        return ApiResult.ok();
    }

    /**
     * 管理员邮箱登录
     * @param loginForm
     * @return
     */
    @ApiDoc
    @PostMapping("email-login")
    public ApiResult<AdminVo> loginWithEmail(@Valid @RequestBody LoginForm loginForm){
        AdminVo loginUser = adminService.emailLogin(loginForm);
        return ApiResult.ok(loginUser);
    }

    /**
     * 退出登录
     * @return
     */
    @PostMapping("logout")
    public ApiResult logout(){
        adminService.logout();
        return ApiResult.ok();
    }

    /**
     * 获取管理员信息
     * @return
     */
    @RequireLogin
    @GetMapping("my-info")
    @ApiDoc
    public ApiResult<AdminVo> myInfo(){
        AdminVo loginUser = adminService.getAdminInfo(curUser().getId());
        return ApiResult.ok(loginUser);
    }

    /**
     * 修改邮件
     * @param request
     * @param modifyEmailForm
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
        adminService.modifyUserEmail(modifyEmailForm);
        return ApiResult.ok();
    }

    /**
     * 修改密码
     * @param request
     * @param modifyPwdForm
     * @return
     */
    @ApiDoc
    @RequireLogin
    @PostMapping("modify-pwd")
    @Deprecated
    public ApiResult modifyPassword(HttpServletRequest request, @Valid @RequestBody ModifyPwdForm modifyPwdForm){
        HttpSession session = request.getSession();
        final String captcha = (String)session.getAttribute(SessionKeys.KAPTCHA_SESSION_KEY);
        // 立即删除
        session.removeAttribute(SessionKeys.KAPTCHA_SESSION_KEY);
        if(captcha == null || !captcha.equalsIgnoreCase(modifyPwdForm.getImgValidCode())){
            return ApiResult.fail(message("imageValidCode.invalid"));
        }
        if(modifyPwdForm.getNewPassword().endsWith(modifyPwdForm.getOldPassword())){
            return ApiResult.fail(message("modify.same.pwd.error"));
        }
        adminService.modifyPassword(modifyPwdForm);
        return ApiResult.ok();
    }
}
