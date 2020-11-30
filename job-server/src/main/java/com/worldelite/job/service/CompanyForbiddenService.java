package com.worldelite.job.service;

import com.worldelite.job.entity.CompanyForbidden;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.CompanyForbiddenForm;
import com.worldelite.job.mapper.CompanyForbiddenMapper;
import com.worldelite.job.vo.CompanyForbiddenVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@Service
public class CompanyForbiddenService extends BaseService {

    @Autowired
    private CompanyForbiddenMapper companyForbiddenMapper;

    public void forbidden(CompanyForbiddenForm companyForbiddenForm){
        Long userId = companyForbiddenForm.getUserId();
        if(userId==null){
            throw new ServiceException(message("api.error.invalid.param"));
        }
        if(isForbidden(userId)){
            deleteForbidden(userId);
        }else{
            addForbidden(userId);
        }
    }

    /**
     * 判读当前登陆用户是否被禁言
     * @return
     */
    public CompanyForbiddenVo myForbidden(){
        Long userId = curUser().getId();
        CompanyForbiddenVo companyForbiddenVo = new CompanyForbiddenVo();
        companyForbiddenVo.setForbidden(isForbidden(userId));
        return companyForbiddenVo;
    }

    public CompanyForbiddenVo getForbidden(Long userId){
        CompanyForbiddenVo companyForbiddenVo = new CompanyForbiddenVo();
        companyForbiddenVo.setForbidden(isForbidden(userId));
        return companyForbiddenVo;
    }

    public void addForbidden(Long userId){
        CompanyForbidden companyForbidden = new CompanyForbidden();
        companyForbidden.setUserId(userId);
        companyForbiddenMapper.insertSelective(companyForbidden);
    }

    public void deleteForbidden(Long userId){
        companyForbiddenMapper.deleteByUserId(userId);
    }

    public boolean isForbidden(Long userId){
        CompanyForbidden options = new CompanyForbidden();
        options.setUserId(userId);
        List<CompanyForbidden> forbiddenList = companyForbiddenMapper.selectByUserId(userId);
        if(CollectionUtils.isEmpty(forbiddenList)){
            return false;
        }
        return true;
    }

}
