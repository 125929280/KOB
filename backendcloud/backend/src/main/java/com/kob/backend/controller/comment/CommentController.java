package com.kob.backend.controller.comment;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.service.comment.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/comment/")
    public Map<String, String> add(@RequestParam Map<String, String> data) {
        return commentService.add(data);
    }

    @DeleteMapping("/comment/")
    public Map<String, String> remove(@RequestParam Map<String, String> data) {
        return commentService.remove(data);
    }

    @GetMapping("/comment/page/{page}/discussId/{discussId}")
    public JSONObject getList(@PathVariable("page") Integer page, @PathVariable("discussId") Integer discussId) {
        return commentService.getList(page, discussId);
    }
}
