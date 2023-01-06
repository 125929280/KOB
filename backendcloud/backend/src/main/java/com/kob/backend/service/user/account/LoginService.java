package com.kob.backend.service.user.account;

import java.util.Map;

public interface LoginService {
    Map<String, String> getToken(Map<String, String> data);

    Map<String, String> logout();
}
