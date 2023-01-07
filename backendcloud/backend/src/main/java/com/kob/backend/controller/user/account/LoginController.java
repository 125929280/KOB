package com.kob.backend.controller.user.account;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.kob.backend.service.user.account.LoginService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

@RestController
@Data
public class LoginController {
    @Autowired
    private LoginService loginService;

    @Autowired
    private DefaultKaptcha defaultKaptcha;


    @PostMapping("/user/account/token/")
    public Map<String, String> getToken(@RequestParam Map<String, String> map) {
        return loginService.getToken(map);
    }

    @GetMapping("/user/account/getVerificationCode")
    public void getVerificationCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        loginService.getVerificationCode(request, response);
    }

    @PostMapping("/user/account/logout/")
    public Map<String, String> logout() {
        return loginService.logout();
    }
}
