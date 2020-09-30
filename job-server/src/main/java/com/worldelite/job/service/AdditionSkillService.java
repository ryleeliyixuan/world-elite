package com.worldelite.job.service;

import com.worldelite.job.entity.AdditionIndustry;
import com.worldelite.job.entity.AdditionSkill;
import com.worldelite.job.entity.JobSkill;
import com.worldelite.job.mapper.AdditionIndustryMapper;
import com.worldelite.job.mapper.AdditionSkillMapper;
import com.worldelite.job.vo.JobIndustryVo;
import com.worldelite.job.vo.JobSkillVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户自定义技能关键词
 * @author 邓集阶
 */
@Service
public class AdditionSkillService extends BaseService {

    @Autowired
    private AdditionSkillMapper additionSkillMapper;

    /**
     * 通过ID查询关键词
     * @param id 键词ID
     * @return
     */
    public AdditionSkill findById(Integer id){
        return additionSkillMapper.selectByPrimaryKey(id);
    }

    /**
     * 通过关键词名和创建者ID查询关键词列表
     * @param creatorId 创建者ID
     * @param name 关键词名称
     * @return
     */
    public String[] listByName(Long categoryId,Long creatorId,String name){
        List<AdditionSkill> additionSkillList = list(categoryId,creatorId,name);
        String[] names = new String[additionSkillList.size()];
        for(int i=0;i<additionSkillList.size();i++){
            names[i] = additionSkillList.get(i).getName();
        }
        return names;
    }

    /**
     * 根据关键词创建者ID查询关键词列表
     * @param creatorId 创建者ID
     * @return
     */
    public List<JobSkillVo> listByCreatorId(Long categoryId, Long creatorId){
        List<AdditionSkill> additionSkillList = list(categoryId,creatorId,null);
        List<JobSkillVo> jobSkillVoList = new ArrayList<>();
        for(AdditionSkill additionSkill : additionSkillList){
            jobSkillVoList.add(toJobSkillVo(additionSkill));
        }
        return jobSkillVoList;
    }

    public List<AdditionSkill> list(Long categoryId,Long creatorId,String name){
        AdditionSkill additionSkill = new AdditionSkill();
        additionSkill.setCategoryId(categoryId);
        additionSkill.setName(name);
        additionSkill.setCategoryId(categoryId);
        return additionSkillMapper.selectAndList(additionSkill);
    }

    /**
     * 保存自定义技能关键词
     * @param additionSkill 自定义技能关键词对象
     */
    public JobSkillVo save(AdditionSkill additionSkill){
        additionSkillMapper.insertSelective(additionSkill);
        return toJobSkillVo(additionSkill);
    }

    /**
     * 删除自定义技能关键词
     * @param id 自定义技能关键词ID
     */
    public void delete(Integer id){
        additionSkillMapper.deleteByPrimaryKey(id);
    }

    private JobSkillVo toJobSkillVo(AdditionSkill additionSkill){
        JobSkillVo jobSkillVo = new JobSkillVo();
        jobSkillVo.setId(additionSkill.getId().longValue());
        jobSkillVo.setName(additionSkill.getName());
        jobSkillVo.setCreateTime(additionSkill.getCreateTime());
        return jobSkillVo;
    }
}
