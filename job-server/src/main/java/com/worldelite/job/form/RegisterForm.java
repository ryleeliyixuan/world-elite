package com.worldelite.job.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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

    @Length(min = 8, message = "{password.format.error}")
    private String password;

    private Byte subscribeFlag;
}
