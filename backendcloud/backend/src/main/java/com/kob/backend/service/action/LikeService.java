package com.kob.backend.service.action;

import java.util.Map;

public interface LikeService {
    Map<String, String> like(int discussId);

    long getLikesCount(int discussId);

    int getLikeStatus(int discussId);
}
