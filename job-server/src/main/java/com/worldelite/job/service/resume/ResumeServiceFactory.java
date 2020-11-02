package com.worldelite.job.service.resume;

import com.worldelite.job.constants.ResumeType;
import com.worldelite.job.entity.Resume;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 简历服务类工厂类
 * 根据简历类型返回对应的简历服务类实例
 */
@Component
public class ResumeServiceFactory extends BaseService {

    private static ResumeGeneralService resumeGeneralService;

    private static ResumeCompanyService resumeCompanyService;

    @Autowired
    public void setResumeGeneralService(ResumeGeneralService resumeGeneralService){
        ResumeServiceFactory.resumeGeneralService = resumeGeneralService;
    }

    @Autowired
    public void setResumeCompanyService(ResumeCompanyService resumeCompanyService){
        ResumeServiceFactory.resumeCompanyService = resumeCompanyService;
    }

    public static ResumeService getResumeService(Byte resumeType){
        //默认为用户简历
        if(resumeType == null) resumeType = ResumeType.GENERAL.value;
        switch (ResumeType.valueOf(resumeType)){
            case GENERAL:
                return resumeGeneralService;
            case COMPANY:
                return resumeCompanyService;
            default:
                //不支持的简历类型
                return null;
        }
    }

    /**
     * 对于一些公共方法
     * 返回任意一个Service都能实现
     * 这里默认返回普通用户简历的服务类
     * @return 默认的简历服务类
     */
    public static ResumeService getDefaultService(){
        return getResumeService(ResumeType.GENERAL.value);
    }

    /**
     * 根据简历ID判定返回的简历服务类类型
     * @param resumeId
     * @return
     */
    public static ResumeService getResumeService(Long resumeId){
        //先用默认类型
        ResumeService resumeService = getDefaultService();
        //获取简历基础信息
        Resume resume = resumeService.getResumeBasic(resumeId);
        if(resume==null) return null;
        return getResumeService(resume.getType());
    }

}
