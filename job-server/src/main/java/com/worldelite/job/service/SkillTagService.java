package com.worldelite.job.service;

import com.github.pagehelper.Page;
import com.worldelite.job.entity.Dict;
import com.worldelite.job.entity.SkillTag;
import com.worldelite.job.form.DictListForm;
import com.worldelite.job.form.SkillTagListForm;
import com.worldelite.job.mapper.SkillTagMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.DictVo;
import com.worldelite.job.vo.PageResult;
import com.worldelite.job.vo.SkillTagGroupVo;
import com.worldelite.job.vo.SkillTagVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/***
 * 技能标签服务类
 */
@Slf4j
@Service
public class SkillTagService extends BaseService{

    @Autowired
    private SkillTagMapper skillTagMapper;

    @Autowired
    private DictService dictService;

    /**
     * 通过类型和标签名模糊搜索标签
     * @param skillTag
     * @return
     */
    public List<SkillTagVo> getSkillTagList(SkillTag skillTag){
        List<SkillTag> skillTagList = skillTagMapper.selectByName(skillTag);
        List<SkillTagVo> skillTagVoList = new ArrayList<>();
        for(SkillTag skillTagTemp:skillTagList){
            SkillTagVo skillTagVo = new SkillTagVo().asVo(skillTagTemp);
            Dict type = dictService.getDict(skillTag.getType());
            skillTagVo.setType(new DictVo().asVo(type));
            skillTagVoList.add(skillTagVo);
        }
        return skillTagVoList;
    }

    /**
     * 获取所有技能标签并分组
     * @return
     */
    public List<SkillTagGroupVo> getSkillTagGroup(){
        DictListForm dictListForm = new DictListForm();
        dictListForm.setType(14);
        List<DictVo> typeList = dictService.getDictList(dictListForm);
        List<SkillTagGroupVo> groupVoList = new ArrayList<>();
        for(DictVo type:typeList){
            SkillTag skillTag = new SkillTag();
            skillTag.setType(type.getId());
            List<SkillTag> skillTagList = skillTagMapper.selectAndList(skillTag);
            List<SkillTagVo> skillTagVoList = new ArrayList<>();
            for(SkillTag skillTagTemp:skillTagList){
                SkillTagVo skillTagVo = new SkillTagVo().asVo(skillTagTemp);
                skillTagVo.setType(copyTypeVo(type));
                skillTagVoList.add(skillTagVo);
            }
            SkillTagGroupVo skillTagGroupVo = new SkillTagGroupVo();
            skillTagGroupVo.setType(copyTypeVo(type));
            skillTagGroupVo.setSkillList(skillTagVoList);
            groupVoList.add(skillTagGroupVo);
        }
        return groupVoList;
    }

    private DictVo copyTypeVo(DictVo typeVo){
        DictVo type = new DictVo();
        type.setId(typeVo.getId());
        type.setName(typeVo.getName());
        type.setValue(typeVo.getValue());
        type.setType(typeVo.getType());
        return type;
    }

    public PageResult<SkillTagVo> getSkillTagPage(SkillTagListForm skillTagListForm) {
        SkillTag options = new SkillTag();
        options.setType(skillTagListForm.getType());
        options.setDelFlag((byte) 0);
        AppUtils.setPage(skillTagListForm);
        Page<SkillTag> skillTagPage = (Page<SkillTag>) skillTagMapper.selectAndList(options);
        PageResult<SkillTagVo> pageResult = new PageResult<>(skillTagPage);
        pageResult.setList(AppUtils.asVoList(skillTagPage, SkillTagVo.class));
        return pageResult;

    }
}
