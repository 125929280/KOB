package com.kob.backend.service.discuss;

import com.alibaba.fastjson.JSONObject;

public interface GetDiscussListService {
    JSONObject getList(Integer page);
}
