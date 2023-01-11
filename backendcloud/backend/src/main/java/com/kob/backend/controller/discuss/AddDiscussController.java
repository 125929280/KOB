package com.kob.backend.controller.discuss;

import com.kob.backend.service.discuss.AddDiscussService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AddDiscussController {
    @Autowired
    private AddDiscussService addDiscussService;

    @PostMapping("/user/discuss/add/")
    public Map<String, String> add(@RequestParam Map<String, String> data) {
        return addDiscussService.add(data);
    }
}
