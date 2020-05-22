package com.worldelite.job.vo;

import com.worldelite.job.entity.CompanyVerification;
import com.worldelite.job.util.AppUtils;
import lombok.Data;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class CompanyVerificationVo implements VoConvertable<CompanyVerificationVo, CompanyVerification>{

    private String name; //名字
    private String idCard; //身份证号
    private String idCardPic; //身份证图片
    private String company;  //公司
    private String post;//职位
    private String companyLicensePic;  //营业执照
    private String remark; //备注
    private Byte status; //状态

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
