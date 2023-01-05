package com.kob.backend.controller.user.account;

import com.kob.backend.service.user.account.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class RegisterController {
    @Autowired
    private RegisterService registerService;

    @PostMapping("/user/account/register/")
    public Map<String, String> register(@RequestParam Map<String, String> map) {
        return registerService.register(map);
    }

    @PostMapping("/user/account/sendActivationCode/")
    public Map<String, String> sendActivationCode(@RequestParam Map<String, String> data) {
        System.out.println("zz");
        return registerService.sendActivationCode(data);
    }
}
