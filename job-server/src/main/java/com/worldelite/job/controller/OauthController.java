package com.worldelite.job.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.worldelite.job.service.AuthService;
import com.xkcoding.justauth.AuthRequestFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.config.AuthSource;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Slf4j
@RestController
@RequestMapping("/api/oauth")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OauthController {

    private final AuthRequestFactory factory;

    private final AuthService authService;

    @GetMapping
    public List<String> list() {
        return factory.oauthList();
    }

    @GetMapping("/login/{type}")
    public void login(@PathVariable String type, HttpServletResponse response) throws IOException {
        AuthRequest authRequest = factory.get(type);
        response.sendRedirect(authRequest.authorize(AuthStateUtils.createState()));
    }

    @RequestMapping("/{type}/callback")
    public void login(@PathVariable String type, AuthCallback callback, HttpServletResponse response) throws IOException {
        AuthRequest authRequest = factory.get(type);
        AuthResponse<AuthUser> authResponse = authRequest.login(callback);
        String redirectUrl = "";
        if(authResponse.ok()){
            redirectUrl = authService.thirdPartLogin(authResponse.getData());
        }
        response.sendRedirect(redirectUrl);
    }
}