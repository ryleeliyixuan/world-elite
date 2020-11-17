package com.worldelite.job.vo;

import cn.hutool.core.bean.BeanUtil;
import com.worldelite.job.entity.CompanyProduct;
import lombok.Data;

/**
 * 企业产品视图对象
 */
@Data
public class CompanyProductVo implements VoConvertable<CompanyProductVo, CompanyProduct>{

    private Integer id; //发展路径ID
    private String companyId; //公司ID
    private String name; //产品名
    private String description; //产品描述
    private String imageName; //产品图片名
    private String imageUrl; //图片地址
    private String url; //产品链接
    private Integer position; //排序

    @Override
    public CompanyProductVo asVo(CompanyProduct companyProduct) {
        if(companyProduct==null) return null;
        BeanUtil.copyProperties(companyProduct,this,"companyId");
        setCompanyId(String.valueOf(companyProduct.getCompanyId()));
        return this;
    }
}
