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
    private String email;

    @NotBlank(message = "{vcode.not.empty}")
    private String validCode;

    @Pattern(regexp = Regexs.PASSWORD_REGEX, message = "{password.format.error}")
    private String password;
    
    private Byte userType;

    private Byte subscribeFlag;
}
