package com.worldelite.job.api;

import com.worldelite.job.entity.SkillTag;
import com.worldelite.job.form.SkillTagListForm;
import com.worldelite.job.service.SkillTagService;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.PageResult;
import com.worldelite.job.vo.SkillTagGroupVo;
import com.worldelite.job.vo.SkillTagVo;
import io.github.yedaxia.apidocs.ApiDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 搜索技能标签接口
 */
@RestController
@RequestMapping("/api/skill-tag/")
public class SkillTagApi {

    @Autowired
    private SkillTagService skillTagService;

    /**
     * 通过名字搜索技能标签
     * @param type 标签类型
     * @param name 标签名
     * @return 标签列表
     */
    @ApiDoc
    @GetMapping("search")
    public ApiResult<List<SkillTagVo>> getSkillTag(@RequestParam Integer type,@RequestParam String name){
        SkillTag skillTag = new SkillTag();
        skillTag.setType(type);
        skillTag.setName(name);
        List<SkillTagVo> skillTagVoList = skillTagService.getSkillTagList(skillTag);
        return ApiResult.ok(skillTagVoList);
    }


    @GetMapping("list")
    public ApiResult<PageResult<SkillTagVo>> getSkillList(SkillTagListForm skillTagListForm) {
        PageResult<SkillTagVo> pageResult = skillTagService.getSkillTagPage(skillTagListForm);
        return ApiResult.ok(pageResult);
    }

    /**
     * 获取分组标签
     * @return 按类型分组后标签列表
     */
    @ApiDoc
    @GetMapping("list-group")
    public ApiResult<List<SkillTagGroupVo>> getSkillGroup(){
        List<SkillTagGroupVo> groupVoList = skillTagService.getSkillTagGroup();
        return ApiResult.ok(groupVoList);
    }

}
