package com.worldelite.job.service;

import com.github.pagehelper.Page;
import com.worldelite.job.entity.Company;
import com.worldelite.job.form.CompanyListForm;
import com.worldelite.job.mapper.CompanyMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.CompanyVo;
import com.worldelite.job.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Service
public class CompanyService {

    @Autowired
    private CompanyMapper companyMapper;

    /**
     * 搜索公司
     *
     * @return
     */
    public PageResult<CompanyVo> search(CompanyListForm companyListForm){
        AppUtils.setPage(companyListForm);
        Company options = new Company();
        options.setFullName(companyListForm.getName());
        Page<Company> companyPage = (Page<Company>) companyMapper.selectAndList(options);
        PageResult<CompanyVo> pageResult = new PageResult<>(companyPage);
        pageResult.setList(AppUtils.asVoList(companyPage, CompanyVo.class));
        return pageResult;
    }
}
