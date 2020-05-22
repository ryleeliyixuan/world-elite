package com.worldelite.job.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class CompanyVerifyForm {
    @NotBlank(message = "{form.verify.name.not.blank}")
    private String name; //名字
    @NotBlank(message = "{form.verify.idCard.not.blank}")
    private String idCard; //身份证号
    @NotBlank(message = "{form.verify.idCardPic.not.blank}")
    private String idCardPic; //身份证图片
    @NotBlank(message = "{form.verify.company.not.blank}")
    private String company; //公司
    @NotBlank(message = "{form.verify.post.not.blank}")
    private String post; //职位
    @NotBlank(message = "{form.verify.companyLicensePic.not.blank}")
    private String companyLicensePic; //营业执照
}
