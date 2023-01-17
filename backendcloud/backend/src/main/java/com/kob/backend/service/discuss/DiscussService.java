package com.kob.backend.service.discuss;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public interface DiscussService {
    Map<String, String> add(Map<String, String> data);
    Map<String, String> remove(Map<String, String> data);
    Map<String, String> update(Map<String, String> data);
    JSONObject getList(Integer page);
    JSONObject get(Integer discussId);
}
