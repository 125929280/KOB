package com.kob.backend.controller.comment;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.service.comment.GetCommentListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class GetCommentListController {
    @Autowired
    private GetCommentListService getCommentListService;

    @GetMapping("/user/comment/getList/")
    public JSONObject getList(@RequestParam Map<String, String> data) {
        Integer page = Integer.valueOf(data.get("page"));
        Integer discussId = Integer.valueOf(data.get("discuss_id"));
        return getCommentListService.getList(page, discussId);
    }
}
