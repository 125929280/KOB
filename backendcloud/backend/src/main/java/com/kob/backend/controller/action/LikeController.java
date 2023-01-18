package com.kob.backend.controller.action;

import com.kob.backend.service.action.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class LikeController {
    @Autowired
    private LikeService likeService;

    @PutMapping("/like/")
    public Map<String, String> like(@RequestParam Map<String, String> data) {
        return likeService.like(Integer.parseInt(data.get("discuss_id")));
    }
}
