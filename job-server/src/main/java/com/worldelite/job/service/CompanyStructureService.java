package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import com.worldelite.job.entity.CompanyStructure;
import com.worldelite.job.form.CompanyStructureForm;
import com.worldelite.job.mapper.CompanyStructureMapper;
import com.worldelite.job.vo.CompanyStructureVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.css.CSSValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 企业组织架构服务类
 */
@Slf4j
@Service
public class CompanyStructureService extends BaseService{

    @Autowired
    private CompanyStructureMapper companyStructureMapper;

    /**
     * 保存组织架构
     * @param companyStructureForm 组织架构表单
     */
    public CompanyStructure save(CompanyStructureForm companyStructureForm){
        CompanyStructure companyStructure = new CompanyStructure();
        BeanUtil.copyProperties(companyStructureForm,companyStructure);
        if(companyStructureForm.getId() == null){
            companyStructureMapper.insertSelective(companyStructure);
            return companyStructure;
        }
        companyStructureMapper.updateByPrimaryKeySelective(companyStructure);
        return companyStructure;
    }

    /**
     * 通过ID查询组织架构
     * @param id 组织架构ID
     * @return 组织架构
     */
    public CompanyStructure getById(Integer id){
        return companyStructureMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询企业对应的所有组织架构
     * @param companyId 企业ID
     * @return 组织架构列表
     */
    public List<CompanyStructure> getByCompanyId(Long companyId){
        CompanyStructure companyStructure = new CompanyStructure();
        companyStructure.setCompanyId(companyId);
        return companyStructureMapper.selectAndList(companyStructure);
    }

    /**
     * 获取组织架构树
     * @param companyId
     * @return
     */
    public List<CompanyStructureVo> getStructureTree(Long companyId){
        //取出所有数据
        List<CompanyStructure> structureList = getByCompanyId(companyId);
        //数据存入Map中，之后可以直接通过ID取到节点
        Map<Integer,CompanyStructureVo> nodeMap = new HashMap<>();
        for(CompanyStructure structure:structureList){
            nodeMap.put(structure.getId(),toVo(structure));
        }
        //生成树结构数据
        List<CompanyStructureVo> roots = new ArrayList<>();
        for(Integer id:nodeMap.keySet()){
            CompanyStructureVo structureVo = nodeMap.get(id);
            if(structureVo.getParentId()==null || structureVo.getParentId()==0){
                //添加根节点
                structureVo.setParentId(0);
                roots.add(structureVo);
            }else{
                //添加子节点
                CompanyStructureVo parentStructure = nodeMap.get(structureVo.getParentId());
                if(parentStructure.getChildren()==null){
                    parentStructure.setChildren(new ArrayList<>());
                }
                parentStructure.getChildren().add(structureVo);
            }
        }
        return roots;
    }

    public CompanyStructureVo toVo(CompanyStructure companyStructure){
        return new CompanyStructureVo().asVo(companyStructure);
    }

    /**
     * 根据ID删除组织架构
     * @param id 组织架构ID
     */
    @Transactional
    public void deleteById(Integer id){
        companyStructureMapper.deleteByPrimaryKey(id);
        //删除子架构
        companyStructureMapper.deleteByParentId(id);
    }
}
