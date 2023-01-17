package com.kob.backend.service.comment;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public interface CommentService {
    Map<String, String> add(Map<String, String> data);
    Map<String, String> remove(Map<String, String> data);
    JSONObject getList(Integer page, Integer discussId);
}
