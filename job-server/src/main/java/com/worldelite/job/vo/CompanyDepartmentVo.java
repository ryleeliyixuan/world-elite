package com.worldelite.job.vo;

import com.worldelite.job.entity.CompanyDepartment;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Data;

import java.util.List;

@Data
public class CompanyDepartmentVo implements VoConvertable<CompanyDepartmentVo, CompanyDepartment>{

    private Integer id; //ID
    private String companyId; //公司ID
    private Integer parentId; //父ID
    private String name; //部门名

    @Ignore
    private List<CompanyDepartmentVo> children;

    @Override
    public CompanyDepartmentVo asVo(CompanyDepartment companyDepartment) {
        if(companyDepartment==null) return null;
        setId(companyDepartment.getId());
        setCompanyId(String.valueOf(companyDepartment.getCompanyId()));
        setParentId(companyDepartment.getParentId());
        setName(companyDepartment.getName());
        return this;
    }
}
