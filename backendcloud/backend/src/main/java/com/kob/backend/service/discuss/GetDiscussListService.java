package com.kob.backend.service.discuss;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public interface GetDiscussListService {
    JSONObject getList(Integer page);

    JSONObject get(Map<String, String> data);
}
