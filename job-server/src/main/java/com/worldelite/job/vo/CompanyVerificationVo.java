package com.worldelite.job.vo;

import com.worldelite.job.entity.CompanyVerification;
import com.worldelite.job.util.AppUtils;
import lombok.Data;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class CompanyVerificationVo implements VoConvertable<CompanyVerificationVo, CompanyVerification>{

    private String name;
    private String idCard;
    private String idCardPic;
    private String company;
    private String post;
    private String companyLicensePic;
    private String remark;
    private Byte status;

    @Override
    public CompanyVerificationVo asVo(CompanyVerification companyVerification) {
        setName(companyVerification.getRealName());
        setIdCard(companyVerification.getIdCard());
        setIdCardPic(AppUtils.absOssUrl(companyVerification.getIdCardPic()));
        setCompany(companyVerification.getCompany());
        setPost(companyVerification.getPost());
        setCompanyLicensePic(AppUtils.absOssUrl(companyVerification.getCompanyLicensePic()));
        setRemark(companyVerification.getRemark());
        setStatus(companyVerification.getStatus());
        return this;
    }
}
