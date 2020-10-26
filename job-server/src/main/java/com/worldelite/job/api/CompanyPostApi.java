package com.worldelite.job.api;

import cn.hutool.core.bean.BeanUtil;
import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.constants.ObjectType;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.entity.CompanyPost;
import com.worldelite.job.form.*;
import com.worldelite.job.service.*;
import com.worldelite.job.vo.*;
import io.github.yedaxia.apidocs.ApiDoc;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * 企业帖子接口
 */
@RestController
@RequestMapping("/api/company-post/")
@Validated
public class CompanyPostApi extends BaseApi {

    @Autowired
    private CompanyPostService companyPostService;

    /**
     * 保存帖子数据
     * @param form 帖子表单
     * @return 保存后的帖子
     */
    @ApiDoc
    @PostMapping("save")
    public ApiResult<CompanyPost> save(CompanyPostForm form){
        CompanyPost companyPost = companyPostService.save(form);
        return ApiResult.ok(companyPost);
    }

    /**
     * 查询帖子
     * @param listForm 查询表单
     * @return 帖子列表
     */
    @ApiDoc
    @PostMapping("list")
    public ApiResult<PageResult<CompanyPost>> list(CompanyPostListForm listForm){
        PageResult<CompanyPost> companyPostPageResult = companyPostService.list(listForm);
        return ApiResult.ok(companyPostPageResult);
    }
}
