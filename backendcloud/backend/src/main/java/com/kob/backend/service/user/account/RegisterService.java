package com.kob.backend.service.user.account;

import java.util.Map;

public interface RegisterService {
    Map<String, String> register(Map<String, String> data);

    Map<String, String> sendActivationCode(Map<String, String> data);
}
