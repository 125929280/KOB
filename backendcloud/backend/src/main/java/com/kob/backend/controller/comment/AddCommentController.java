package com.kob.backend.controller.comment;

import com.kob.backend.service.comment.AddCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AddCommentController {
    @Autowired
    private AddCommentService addCommentService;

    @PostMapping("/user/comment/add/")
    public Map<String, String> add(@RequestParam Map<String, String> data) {
        return addCommentService.add(data);
    }
}
