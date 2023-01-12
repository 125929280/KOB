package com.kob.backend.utils;

public class RedisKeyUtil {
    private static final String SPLIT = ":";
    private static final String PREFIX_ACTIVATION = "activation";
    private static final String PREFIX_VERIFICATION = "verification";
    private static final String PREFIX_LOGIN = "login";

    public static String getActivationKey(String username) {
        return PREFIX_ACTIVATION + SPLIT + username;
    }

    public static String getVerificationKey(String ip) {
        return PREFIX_VERIFICATION + SPLIT + ip;
    }

    public static String getLoginKey(String userId) {
        return PREFIX_LOGIN + SPLIT + userId;
    }
}
