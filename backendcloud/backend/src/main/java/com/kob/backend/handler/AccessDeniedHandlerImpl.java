package com.kob.backend.handler;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.utils.WebUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        // 处理异常
        JSONObject json = new JSONObject();
        json.put("error_message", "权限不足");
        WebUtil.renderString(httpServletResponse, json.toJSONString());
    }
}
