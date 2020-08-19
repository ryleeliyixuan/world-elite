package com.worldelite.job.api;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.form.JobCategorySkillForm;
import com.worldelite.job.form.JobSkillForm;
import com.worldelite.job.service.JobCategorySkillService;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.JobSkillVo;
import io.github.yedaxia.apidocs.ApiDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 职位技能操作接口
 * @author 王星瀚
 */
@RestController
@RequestMapping("/api/jobskill/")
public class JobCategorySkillApi {

    @Autowired
    private JobCategorySkillService jobCategorySkillService;

    /**
     * 保存职位技能
     * @param jobSkillForm
     * @return
     */
    @RequireLogin(allow = UserType.COMPANY)
    @PostMapping("save")
    @ApiDoc
    public ApiResult saveJobSkill(@RequestBody JobSkillForm jobSkillForm){
        jobCategorySkillService.saveJobSkill(jobSkillForm);

        return ApiResult.ok();
    }

    /**
     * 删除职位技能
     * @param jobSkillId
     * @return
     */
    @RequireLogin(allow = UserType.COMPANY)
    @PostMapping("delete")
    @ApiDoc
    public ApiResult deleteJobSkill(@RequestBody long jobSkillId){
        jobCategorySkillService.deleteJobSkillById(jobSkillId);

        return ApiResult.ok();
    }

    /**
     * 根据职位类别Id获取对应的职位技能
     * @param jobCategoryId
     * @return
     */
    @RequireLogin(allow = UserType.COMPANY)
    @GetMapping("list")
    @ApiDoc
    public ApiResult<List<JobSkillVo>> getJobSkillList(@RequestParam long jobCategoryId) {
        final List<JobSkillVo> jobSkillVos = jobCategorySkillService.getJobSkillByJobCategoryId(jobCategoryId);

        return ApiResult.ok(jobSkillVos);
    }

    /**
     * 保存职位技能-职位类别映射
     * @param jobCategorySkillForm
     * @return
     */
    @RequireLogin(allow = UserType.COMPANY)
    @PostMapping("category/save")
    @ApiDoc
    public ApiResult saveJobCategorySkill(@RequestBody JobCategorySkillForm jobCategorySkillForm){
        jobCategorySkillService.saveJobCategorySkill(jobCategorySkillForm);

        return ApiResult.ok();
    }

    /**
     * 删除职位技能-职位类别映射
     * @param jobCategorySkillId
     * @return
     */
    @RequireLogin(allow = UserType.COMPANY)
    @PostMapping("category/delete")
    @ApiDoc
    public ApiResult deleteComment(@RequestBody long jobCategorySkillId){
        jobCategorySkillService.deleteJobCategorySkillById(jobCategorySkillId);

        return ApiResult.ok();
    }
}
