package com.worldelite.job.vo;

import cn.hutool.core.bean.BeanUtil;
import com.worldelite.job.entity.CompanyStructure;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Data;

import java.util.List;

/**
 * 企业组织架构对象
 */
@Data
public class CompanyStructureVo implements VoConvertable<CompanyStructureVo, CompanyStructure>{

    private Integer id; //组织架构ID
    private Integer parentId; //组织架构父级ID
    private String companyId; //公司ID
    private String name; //组织架构名

    @Ignore
    //避免使用ApiDoc生成文档时循环引用报错，需要加上Ignore注解
    private List<CompanyStructureVo> children; //子组织架构

    @Override
    public CompanyStructureVo asVo(CompanyStructure companyStructure) {
        if(companyStructure==null) return null;
        BeanUtil.copyProperties(companyStructure,this,"companyId");
        setCompanyId(String.valueOf(companyStructure.getCompanyId()));
        return this;
    }
}
