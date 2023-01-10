package com.kob.backend.controller.user.account;

import com.kob.backend.service.user.account.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
public class InfoController {
    @Autowired
    private InfoService infoService;

    @GetMapping("/user/account/info/")
    public Map<String, String> getInfo() {
        return infoService.getInfo();
    }

    @PostMapping("/user/account/updatePassword/")
    public Map<String, String> updatePassword(@RequestParam Map<String, String> data) {
        return infoService.updatePassword(data);
    }

    @PostMapping("/user/account/updatePhoto/")
    public Map<String, String> updatePhoto(MultipartFile data) {
        return infoService.updatePhoto(data);
    }
}
