package com.kob.backend.service.comment;

import com.alibaba.fastjson.JSONObject;

public interface GetCommentListService {
    JSONObject getList(Integer page, Integer discussId);
}
