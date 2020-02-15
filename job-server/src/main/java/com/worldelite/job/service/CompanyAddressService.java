package com.worldelite.job.service;

import com.worldelite.job.constants.DictType;
import com.worldelite.job.entity.CompanyAddress;
import com.worldelite.job.form.CompanyAddressForm;
import com.worldelite.job.form.DictListForm;
import com.worldelite.job.mapper.CompanyAddressMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.CompanyAddressVo;
import com.worldelite.job.vo.DictVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Service
public class CompanyAddressService {

    @Autowired
    private CompanyAddressMapper companyAddressMapper;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private DictService dictService;


    public CompanyAddressVo getCompanyAddress(Integer id){
        CompanyAddress companyAddress = companyAddressMapper.selectByPrimaryKey(id);
        return companyAddress == null? null: new CompanyAddressVo().asVo(companyAddress);
    }

    /**
     * 获取公司地址列表
     * @param companyId
     * @return
     */
    public List<CompanyAddressVo> getCompanyAddressList(Long companyId){
        CompanyAddress options = new CompanyAddress();
        options.setCompanyId(companyId);
        List<CompanyAddress> addressList = companyAddressMapper.selectAndList(options);
        return AppUtils.asVoList(addressList, CompanyAddressVo.class);
    }

    /**
     * 保存公司地址信息
     *
     * @param addressForm
     */
    public void saveCompanyAddress(CompanyAddressForm addressForm){
        companyService.checkCompanyUser(addressForm.getCompanyId());

        CompanyAddress companyAddress = null;
        if(addressForm.getId() != null){
            companyAddress = companyAddressMapper.selectByPrimaryKey(addressForm.getId());
        }

        if(companyAddress == null){
            companyAddress = new CompanyAddress();
            companyAddress.setCompanyId(addressForm.getCompanyId());
        }

        if(StringUtils.isNotEmpty(addressForm.getCity())){
            DictListForm dictListForm = new DictListForm();
            dictListForm.setName(addressForm.getCity());
            dictListForm.setType(DictType.CITY.value);
            List<DictVo> dictVoList = dictService.getDictList(dictListForm);
            if(CollectionUtils.isNotEmpty(dictVoList)){
                companyAddress.setCityId(dictVoList.get(0).getId());
            }
        }

        companyAddress.setAddress(addressForm.getAddress());
        companyAddress.setLatitude(addressForm.getLatitude());
        companyAddress.setLongitude(addressForm.getLongitude());

        if(companyAddress.getId() == null){
            companyAddressMapper.insertSelective(companyAddress);
        }else{
            companyAddress.setUpdateTime(new Date());
            companyAddressMapper.updateByPrimaryKeySelective(companyAddress);
        }
    }

    /**
     * 删除公司地址信息
     *
     * @param id
     */
    public void delCompanyAddress(Integer id){
        CompanyAddress companyAddress = companyAddressMapper.selectByPrimaryKey(id);
        if(companyAddress != null){
            companyService.checkCompanyUser(companyAddress.getCompanyId());
            companyAddressMapper.deleteByPrimaryKey(id);
        }
    }
}
