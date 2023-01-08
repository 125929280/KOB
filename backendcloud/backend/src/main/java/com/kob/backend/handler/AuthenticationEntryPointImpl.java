package com.kob.backend.handler;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.utils.WebUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        // 处理异常
        JSONObject json = new JSONObject();
        json.put("error_message", "认证失败，请重新登录");
        WebUtil.renderString(httpServletResponse, json.toJSONString());
    }
}
