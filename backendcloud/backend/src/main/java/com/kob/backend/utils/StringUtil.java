package com.kob.backend.utils;

import java.util.UUID;

public class StringUtil {
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
