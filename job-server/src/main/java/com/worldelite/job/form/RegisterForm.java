package com.worldelite.job.form;

import com.worldelite.job.constants.Regexs;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class RegisterForm {

    @NotNull
    @Email(message = "{email.format.error}")
    private String email; //邮箱

    @NotBlank(message = "{vcode.not.empty}")
    private String validCode; //验证码

    @Pattern(regexp = Regexs.PASSWORD_REGEX, message = "{password.format.error}")
    private String password; //密码

    private String name; //名字

    private Byte gender; //性别

    private Byte userType; //用户类型

    private Byte subscribeFlag; //是否订阅
}
