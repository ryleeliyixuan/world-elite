package com.worldelite.job.form;

import com.worldelite.job.constants.Regexs;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class ResetPwdForm {
    @Email(message = "{email.format.error}")
    private String email; //邮箱
    @NotBlank(message = "{validCode.not.blank}")
    private String validCode; //验证码
    @Pattern(regexp = Regexs.PASSWORD_REGEX, message = "{password.format.error}")
    private String newPassword; //新密码
    @NotBlank(message = "{imageValidCode.not.blank}")
    private String imgValidCode; //图片验证码
}
