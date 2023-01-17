package com.kob.backend.controller.discuss;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.service.discuss.DiscussService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class DiscussController {
    @Autowired
    private DiscussService discussService;

    @PostMapping("/discuss/")
    public Map<String, String> add(@RequestParam Map<String, String> data) {
        return discussService.add(data);
    }

    @DeleteMapping("/discuss/")
    public Map<String, String> remove(@RequestParam Map<String, String> data) {
        return discussService.remove(data);
    }

    @PutMapping("/discuss/")
    public Map<String, String> update(@RequestParam Map<String, String> data) {
        return discussService.update(data);
    }

    @GetMapping("/discuss/")
    public JSONObject getList(@RequestParam Map<String, String> data) {
        Integer page = Integer.valueOf(data.get("page"));
        return discussService.getList(page);
    }

    @GetMapping("/discuss/discussId/{discussId}")
    public JSONObject get(@PathVariable("discussId") Integer discussId, @RequestParam Map<String, String> data) {
        return discussService.get(discussId);
    }
}
