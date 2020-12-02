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
        if(!isForbidden(userId)){
            addForbidden(userId,companyForbiddenForm.getDaysId(),companyForbiddenForm.getContent());
            if(companyForbiddenForm.getNotice()!=0){
                //notice user;
            }
        }
    }

    public void unForbidden(CompanyForbiddenForm companyForbiddenForm){
        Long userId = companyForbiddenForm.getUserId();
        if(userId==null){
            throw new ServiceException(message("api.error.invalid.param"));
        }
        if(isForbidden(userId)){
            deleteForbidden(userId);
            if(companyForbiddenForm.getNotice()!=0){
                //notice user;
            }
        }
    }

    public void forbiddenAll(CompanyForbiddenForm companyForbiddenForm){
        Long[] userIds = companyForbiddenForm.getUserIds();
        if(userIds==null){
            throw new ServiceException(message("api.error.invalid.param"));
        }
        for(int i=0;i<userIds.length;i++){
            if(!isForbidden(userIds[i])){
                addForbidden(userIds[i],companyForbiddenForm.getDaysId(),companyForbiddenForm.getContent());
                if(companyForbiddenForm.getNotice()!=0){
                    //notice user;
                }
            }
        }
    }

    public void unForbiddenAll(CompanyForbiddenForm companyForbiddenForm){
        Long[] userIds = companyForbiddenForm.getUserIds();
        if(userIds==null){
            throw new ServiceException(message("api.error.invalid.param"));
        }
        for(int i=0;i<userIds.length;i++){
            if(isForbidden(userIds[i])){
                deleteForbidden(userIds[i]);
                if(companyForbiddenForm.getNotice()!=0){
                    //notice user;
                }
            }
        }
    }

    public void addForbidden(Long userId,Integer daysId,String content){
        CompanyForbidden companyForbidden = new CompanyForbidden();
        companyForbidden.setUserId(userId);
        companyForbidden.setDaysId(daysId);
        companyForbidden.setContent(content);
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
