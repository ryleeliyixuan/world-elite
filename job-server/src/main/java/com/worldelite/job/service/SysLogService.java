package com.worldelite.job.service;

import com.worldelite.job.entity.SysLog;
import com.worldelite.job.form.SysLogForm;
import com.worldelite.job.mapper.SysLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 系统日志
 * @author yeguozhong yedaxia.github.com
 */
@Service
public class SysLogService extends BaseService{

    @Autowired
    private SysLogMapper sysLogMapper;

    /**
     * 保存日志
     *
     * @param sysLogForm
     */
    public void saveLog(SysLogForm sysLogForm){
        SysLog sysLog = new SysLog();
        sysLog.setUserIp(sysLogForm.getUserIp());
        if(curUser() != null){
            sysLog.setUserId(Long.valueOf(curUser().getId()));
        }
        sysLog.setReqMethod(sysLogForm.getReqMethod());
        sysLog.setReqDesc(sysLogForm.getReqDesc());
        sysLogMapper.insertSelective(sysLog);
    }

}
