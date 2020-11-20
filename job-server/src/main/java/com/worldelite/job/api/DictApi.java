package com.worldelite.job.api;

import com.worldelite.job.form.DictForm;
import com.worldelite.job.form.DictListForm;
import com.worldelite.job.service.DictService;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.DictVo;
import com.worldelite.job.vo.PageResult;
import io.github.yedaxia.apidocs.ApiDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 字典接口
 * @author yeguozhong yedaxia.github.com
 */
@RestController
@RequestMapping("/api/dict/")
@Validated
public class DictApi extends BaseApi{

    @Autowired
    private DictService dictService;

    /**
     * 字典列表
     * @param listForm
     * @return
     */
    @GetMapping("list")
    @ApiDoc
    public ApiResult<PageResult<DictVo>> getDictList(DictListForm listForm){
        PageResult<DictVo> pageResult = dictService.getDictPage(listForm);
        return ApiResult.ok(pageResult);
    }

    /**
     * 获取字典对象
     * @param id
     * @return
     */
    @GetMapping("get-by-id")
    @ApiDoc
    public ApiResult<DictVo> getDictById(@RequestParam Integer id){
        DictVo dictVo = dictService.getById(id);
        return ApiResult.ok(dictVo);
    }

    /**
     * 保存字典
     * @param dictForm
     * @return
     */
    @PostMapping("save")
    @ApiDoc
    public ApiResult<DictVo> saveDict(@Valid @RequestBody DictForm dictForm){
        DictVo dictVo = dictService.saveDict(dictForm);
        return ApiResult.ok(dictVo);
    }

    /**
     * 删除字段
     *
     * @param id
     * @return
     */
    @PostMapping("delete")
    @ApiDoc
    public ApiResult deleteDict(@RequestParam Integer id){
        dictService.deleteDict(id);
        return ApiResult.ok();
    }
}
