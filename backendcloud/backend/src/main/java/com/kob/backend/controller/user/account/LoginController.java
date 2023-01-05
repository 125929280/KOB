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
import java.util.Map;

@RestController
@Data
public class LoginController {
    @Autowired
    private LoginService loginService;

    @Autowired
    private DefaultKaptcha defaultKaptcha;

    private String verificationCode;

    @PostMapping("/user/account/token/")
    public Map<String, String> getToken(@RequestParam Map<String, String> map) {
        map.put("actualVerificationCode", verificationCode);
//        System.out.println((String) request.getSession().getAttribute("verificationCode"));
        return loginService.getToken(map);
    }

    @GetMapping("/user/account/getVerificationCode")
    public void getVerificationCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
        byte[] captchaOutputStream = null;
        ByteArrayOutputStream imgOutputStream = new ByteArrayOutputStream();
        try {
            //生成验证码字符串并保存到session中
            String code = defaultKaptcha.createText();
            request.getSession().setAttribute("verificationCode", code);
            System.out.println((String) request.getSession().getAttribute("verificationCode"));

            System.out.println(code);
            this.setVerificationCode(code);
            BufferedImage challenge = defaultKaptcha.createImage(verificationCode);
            ImageIO.write(challenge, "jpg", imgOutputStream);
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        captchaOutputStream = imgOutputStream.toByteArray();
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream = response.getOutputStream();
        responseOutputStream.write(captchaOutputStream);
        responseOutputStream.flush();
    }
}
