package com.kob.backend.service.user.account;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public interface LoginService {
    Map<String, String> getToken(Map<String, String> data, String ip);

    void getVerificationCode(HttpServletRequest request, HttpServletResponse response) throws IOException;

    Map<String, String> logout();
}
