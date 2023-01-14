package com.kob.backend.utils;

public class RedisUtil {
    private static final String SPLIT = ":";
    private static final String PREFIX_ACTIVATION = "activation";
    public static final Long ACTIVATION_TTL  = 2L;
    private static final String PREFIX_VERIFICATION = "verification";
    public static final Long VERIFICATION_TTL  = 2L;
    private static final String PREFIX_LOGIN = "login";
    public static final Long LOGIN_TTL  = 30L;
    private static final String PREFIX_LIKE = "like";

    public static String getActivationKey(String username) {
        return PREFIX_ACTIVATION + SPLIT + username;
    }

    public static String getVerificationKey(String ip) {
        return PREFIX_VERIFICATION + SPLIT + ip;
    }

    public static String getLoginKey(String userId) {
        return PREFIX_LOGIN + SPLIT + userId;
    }

    /**
     * like:entityType:entityId -> set(userId)
     * @param entityType
     * @param entityId
     * @return
     */
    public static String getEntityLike(int entityType, int entityId) {
        return PREFIX_LIKE + SPLIT + entityType + SPLIT + entityId;
    }
}
