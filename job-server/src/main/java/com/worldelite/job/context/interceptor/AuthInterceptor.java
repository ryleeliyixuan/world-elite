package com.worldelite.job.context.interceptor;

import com.alibaba.fastjson.JSON;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.context.AttrKeys;
import com.worldelite.job.context.MessageResource;
import com.worldelite.job.context.anatation.RequireLogin;
import com.worldelite.job.service.UserService;
import com.worldelite.job.util.ResponseUtils;
import com.worldelite.job.vo.ApiCode;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.UserVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author yeguozhong yedaxia.github.com
 */
public class AuthInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private MessageResource messageResource;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(!(handler instanceof HandlerMethod)) {
            // 如果不是映射到方法，直接通过
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        final String token = request.getHeader("X-Token");

        UserVo loginUser = null;

        if(StringUtils.isNoneEmpty(token)){
            final String userJson = redisTemplate.opsForValue().get(token);
            if(StringUtils.isNoneEmpty(userJson)){
                loginUser = JSON.parseObject(userJson, UserVo.class);
                request.setAttribute(AttrKeys.LOGIN_USER, loginUser);
            }
        }

        RequireLogin requireLogin = method.getAnnotation(RequireLogin.class);

        if(requireLogin != null){
            if(loginUser == null){
                ResponseUtils.writeAsJson(response, ApiResult.fail(ApiCode.NEED_LOGIN, messageResource.getMessage("need.login")));
                return false;
            }
            if(!accessAllow(requireLogin, loginUser)){
                ResponseUtils.writeAsJson(response, ApiResult.fail(ApiCode.NEED_LOGIN, messageResource.getMessage("permission.denied")));
                return false;
            }
        }

        return super.preHandle(request, response, handler);
    }

    private boolean accessAllow(RequireLogin requireLogin, UserVo loginUser){
        UserType loginUserType = UserType.valueOf(loginUser.getType());
        return ArrayUtils.isEmpty(requireLogin.allow()) || ArrayUtils.contains(requireLogin.allow(), loginUserType);
    }
}
