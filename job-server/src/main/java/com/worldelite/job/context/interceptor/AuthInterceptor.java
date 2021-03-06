package com.worldelite.job.context.interceptor;

import com.alibaba.fastjson.JSON;
import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.constants.UserStatus;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.context.AttrKeys;
import com.worldelite.job.context.MessageResource;
import com.worldelite.job.context.RedisKeys;
import com.worldelite.job.util.ResponseUtils;
import com.worldelite.job.vo.ApiCode;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.UserVo;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @author yeguozhong yedaxia.github.com
 */
public class AuthInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate<String, ?> redisTemplate;

    @Autowired
    private MessageResource messageResource;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod)) {
            // 如果不是映射到方法，直接通过
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        String token = request.getHeader("X-Token");

        if (StringUtils.isEmpty(token)) {
            token = request.getParameter("_token");
        }

        UserVo loginUser = null;

        if (StringUtils.isNotEmpty(token)) {
            final String userJson = stringRedisTemplate.opsForValue().get(token);
            if (StringUtils.isNotEmpty(userJson)) {
                loginUser = JSON.parseObject(userJson, UserVo.class);
                request.setAttribute(AttrKeys.LOGIN_USER, loginUser);

                //刷新redis中的用户最后活动时间,此功能目前提供给IM界面显示
                redisTemplate.opsForHash().put(RedisKeys.ATTR_ONLINE_INFO, loginUser.getId().toString(), new Date());
            }
        }

        RequireLogin requireLogin = method.getAnnotation(RequireLogin.class);

        if (requireLogin != null) {
            if (loginUser == null) {
                ResponseUtils.writeAsJson(response, ApiResult.fail(ApiCode.NEED_LOGIN, messageResource.getMessage("need.login")));
                return false;
            }
            if (!accessAllow(requireLogin, loginUser)) {
                ResponseUtils.writeAsJson(response, ApiResult.fail(ApiCode.PERMISSION_DENIED, messageResource.getMessage("permission.denied")));
                return false;
            }
            if (ArrayUtils.contains(requireLogin.allow(), UserType.COMPANY)
                    && loginUser.getStatus() == UserStatus.NOT_ACTIVATE.value) {
                ResponseUtils.writeAsJson(response, ApiResult.fail(ApiCode.NOT_ACTIVATE, messageResource.getMessage("user.not.activate")));
                return false;
            }
        }

        return super.preHandle(request, response, handler);
    }

    private boolean accessAllow(RequireLogin requireLogin, UserVo loginUser) {
        UserType loginUserType = UserType.valueOf(loginUser.getType());
        return ArrayUtils.isEmpty(requireLogin.allow()) || ArrayUtils.contains(requireLogin.allow(), loginUserType);
    }
}
