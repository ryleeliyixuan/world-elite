package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import com.worldelite.job.entity.CompanyProduct;
import com.worldelite.job.form.CompanyProductForm;
import com.worldelite.job.mapper.CompanyProductMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.CompanyProductVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 企业产品服务类
 */
@Slf4j
@Service
public class CompanyProductService extends BaseService{

    @Autowired
    private CompanyProductMapper companyProductMapper;

    /**
     * 保存产品
     * @param companyProductForm 产品表单
     */
    public CompanyProduct save(CompanyProductForm companyProductForm){
        CompanyProduct companyProduct = new CompanyProduct();
        BeanUtil.copyProperties(companyProductForm,companyProduct);
        if(companyProductForm.getId() == null){
            companyProductMapper.insertSelective(companyProduct);
            return companyProduct;
        }
        companyProductMapper.updateByPrimaryKeySelective(companyProduct);
        return companyProduct;
    }

    /**
     * 通过ID查询产品
     * @param id 产品ID
     * @return 产品
     */
    public CompanyProduct getById(Integer id){
        return companyProductMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询企业对应的所有产品
     * @param companyId 企业ID
     * @return 产品列表
     */
    public List<CompanyProduct> getByCompanyId(Long companyId){
        CompanyProduct companyProduct = new CompanyProduct();
        companyProduct.setCompanyId(companyId);
        return companyProductMapper.selectAndList(companyProduct);
    }

    public List<CompanyProductVo> listProductVo(Long companyId){
        List<CompanyProduct> productList = getByCompanyId(companyId);
        return AppUtils.asVoList(productList,CompanyProductVo.class);
    }

    public CompanyProductVo toVo(CompanyProduct companyProduct){
        return new CompanyProductVo().asVo(companyProduct);
    }

    /**
     * 根据ID删除产品
     * @param id 产品ID
     */
    public void deleteById(Integer id){
        companyProductMapper.deleteByPrimaryKey(id);
    }
}
