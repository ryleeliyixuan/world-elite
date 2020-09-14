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
    @RequireLogin(allow = UserType.ADMIN)
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
    @RequireLogin(allow = UserType.ADMIN)
    @PostMapping("delete")
    @ApiDoc
    public ApiResult deleteJobSkill(@RequestParam long jobSkillId){
        jobCategorySkillService.deleteJobSkillById(jobSkillId);

        return ApiResult.ok();
    }

    /**
     * 获取全部职位技能
     *
     * @return
     */
    @RequireLogin(allow = {UserType.ADMIN})
    @GetMapping("list-all")
    @ApiDoc
    public ApiResult<List<JobSkillVo>> getJobSkillListAll() {
        final List<JobSkillVo> jobSkillVos = jobCategorySkillService.getJobSkillList();

        return ApiResult.ok(jobSkillVos);
    }
    
    /**
     * 根据职位类别Id获取对应的职位技能
     * @param jobCategoryId 第二级职位类别id
     * @return
     */
    @RequireLogin(allow = {UserType.COMPANY, UserType.ADMIN})
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
    @RequireLogin(allow = UserType.ADMIN)
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
    @RequireLogin(allow = UserType.ADMIN)
    @PostMapping("category/delete")
    @ApiDoc
    public ApiResult deleteComment(@RequestParam long jobCategorySkillId){
        jobCategorySkillService.deleteJobCategorySkillById(jobCategorySkillId);

        return ApiResult.ok();
    }
}
