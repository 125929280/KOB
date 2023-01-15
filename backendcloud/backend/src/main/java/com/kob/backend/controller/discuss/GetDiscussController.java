package com.kob.backend.controller.discuss;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.service.discuss.GetDiscussListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class GetDiscussController {
    @Autowired
    private GetDiscussListService getDiscussListService;

    @GetMapping("/user/discuss/getList/")
    public JSONObject getList(@RequestParam Map<String, String> data) {
        Integer page = Integer.valueOf(data.get("page"));
        return getDiscussListService.getList(page);
    }

    @GetMapping("/user/discuss/get/")
    public JSONObject get(@RequestParam Map<String, String> data) {
        return getDiscussListService.get(data);
    }
}
