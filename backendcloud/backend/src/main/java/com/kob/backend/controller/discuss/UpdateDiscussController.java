package com.kob.backend.controller.discuss;

import com.kob.backend.service.discuss.UpdateDiscussService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UpdateDiscussController {
    @Autowired
    private UpdateDiscussService updateDiscussService;

    @PostMapping("/user/discuss/update/")
    public Map<String, String> update(@RequestParam Map<String, String> data) {
        return updateDiscussService.update(data);
    }
}
