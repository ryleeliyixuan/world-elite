package com.worldelite.job.form;

import lombok.Data;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class ResetPwdForm {
    private String email;
    private String validCode;
    private String newPassword;
    private String imgValidCode;
}
