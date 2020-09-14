package com.worldelite.job.api;

import com.alibaba.excel.util.StringUtils;
import com.worldelite.job.entity.ResumeRepository;
import com.worldelite.job.form.ResumeRepositoryForm;
import com.worldelite.job.form.ResumeRepositoryListForm;
import com.worldelite.job.service.ResumeRepositoryService;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.PageResult;
import com.worldelite.job.vo.ResumeRepositoryVo;
import io.github.yedaxia.apidocs.ApiDoc;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/resume-repository/")
public class ResumeRepositoryApi extends BaseApi{

    @Autowired
    private ResumeRepositoryService resumeRepositoryService;

    /**
     * 通过解析附件简历新增简历库数据
     * @param attachmentName 简历文件路径
     * @return
     */
    @ApiDoc
    @PostMapping("save-attachment")
    public ApiResult saveAttachment(@RequestParam String attachmentName){
        resumeRepositoryService.saveAttachment(attachmentName);
        return ApiResult.ok();
    }

    /**
     * 给简历库数据添加简历附件
     * @param id 简历库中简历ID
     * @param attachmentName 附件文件路径
     * @return
     */
    @ApiDoc
    @PostMapping("add-attachment")
    public ApiResult addAttachment(@RequestParam Long id,@RequestParam String attachmentName){
        resumeRepositoryService.addAttachment(id,attachmentName);
        return ApiResult.ok();
    }

    /**
     * 保存简历
     * @param resumeRepositoryForm 简历信息
     * @return
     */
    @ApiDoc
    @PostMapping("add-from-form")
    public ApiResult addResume(@RequestBody ResumeRepositoryForm resumeRepositoryForm){
        resumeRepositoryService.addResume(resumeRepositoryForm);
        return ApiResult.ok();
    }

    /**
     * 将用户简历添加到当前企业简历库
     * @param resumeId 用户简历ID
     * @return
     */
    @ApiDoc
    @PostMapping("add-from-user")
    public ApiResult addResume(Long resumeId){
        resumeRepositoryService.addResume(resumeId);
        return ApiResult.ok();
    }

    /**
     * 将简历添加到指定企业简历库
     * @param originId 源简历
     * @param companyId 目标企业
     * @param type 添加类型：1、从用户简历添加（默认）；2、从企业简历库添加
     * @return
     */
    @ApiDoc
    @PostMapping("add-resume")
    public ApiResult addResume(Long originId,Long companyId,int type){
        switch (type){
            case 2:
                resumeRepositoryService.addFromRepository(originId,companyId);
                break;
            default:
                resumeRepositoryService.addFromUserResume(originId,companyId);
                break;
        }
        return ApiResult.ok();
    }

    /**
     * 搜索简历库
     * @param resumeRepositoryListForm 过滤条件
     * @return
     */
    @ApiDoc
    @PostMapping("search")
    public ApiResult<PageResult<ResumeRepositoryVo>> search(@RequestBody ResumeRepositoryListForm resumeRepositoryListForm){
        if(StringUtils.isEmpty(resumeRepositoryListForm.getKeyword())){
            PageResult<ResumeRepositoryVo> pageResult = resumeRepositoryService.search(resumeRepositoryListForm);
            return ApiResult.ok(pageResult);
        }else{
            PageResult<ResumeRepositoryVo> pageResult = resumeRepositoryService.searchByKeyword(resumeRepositoryListForm);
            return ApiResult.ok(pageResult);
        }
    }

    public void suggest(){

    }

    public void delete(){

    }
}
