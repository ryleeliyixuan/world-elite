package com.worldelite.job.api;

import com.worldelite.job.form.CompanyListForm;
import com.worldelite.job.service.CompanyService;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.CompanyVo;
import com.worldelite.job.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yeguozhong yedaxia.github.com
 */
@RestController
@RequestMapping("/api/company")
public class CompanyApi {

    @Autowired
    private CompanyService companyService;

    /**
     * 搜索公司
     *
     * @param companyListForm
     * @return
     */
    @GetMapping("search")
    public ApiResult<CompanyVo> search(CompanyListForm companyListForm){
        PageResult pageResult = companyService.search(companyListForm);
        return ApiResult.ok(pageResult);
    }

}
