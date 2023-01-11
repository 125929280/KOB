package com.kob.backend.controller.comment;

import com.kob.backend.service.comment.RemoveCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RemoveCommentController {
    @Autowired
    private RemoveCommentService removeCommentService;

    @PostMapping("/user/comment/remove/")
    public Map<String, String> remove(@RequestParam Map<String, String> data) {
        return removeCommentService.remove(data);
    }
}
