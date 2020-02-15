package com.worldelite.job.context.aspect;

import com.alibaba.fastjson.JSON;
import com.worldelite.job.anatation.SysLog;
import com.worldelite.job.form.SysLogForm;
import com.worldelite.job.service.SysLogService;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.util.RequestUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Aspect
@Component
public class SysLogAspect {

    @Autowired
    private SysLogService sysLogService;

    @Pointcut("@annotation(com.worldelite.job.anatation.SysLog)")
    public void logPointCut(){ }

    @AfterReturning("logPointCut()")
    public void saveSysLog(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        SysLog logOperation = method.getAnnotation(SysLog.class);
        if(logOperation == null){
            return;
        }

        // 获取request
        SysLogForm sysLogForm = new SysLogForm();
        // 请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        sysLogForm.setReqMethod(className + "." + methodName + "()");
        // 请求的方法参数值
        Object[] args = joinPoint.getArgs();
        // 请求的方法参数名称
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] paramNames = u.getParameterNames(method);
        if (args != null && paramNames != null) {
            Map<String, Object> params = new LinkedHashMap<>(paramNames.length);
            for (int i = 0; i < args.length; i++) {
                params.put(paramNames[i], args[i]);
            }
            sysLogForm.setReqDesc(JSON.toJSONString(params));
        }
        sysLogForm.setUserIp(RequestUtils.getClientIP(AppUtils.request()));
        sysLogService.saveLog(sysLogForm);
    }
}
