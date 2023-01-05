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
        System.out.println(map.get("actualVerificationCode"));
        return loginService.getToken(map);
    }

    @GetMapping("/user/account/getVerificationCode")
    public void getVerificationCode(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        byte[] captchaOutputStream = null;
        ByteArrayOutputStream imgOutputStream = new ByteArrayOutputStream();
        try {
            //生成验证码字符串并保存到session中
            String code = defaultKaptcha.createText();
            httpServletRequest.getSession().setAttribute("verificationCode", code);

            System.out.println(code);
            this.setVerificationCode(code);
            BufferedImage challenge = defaultKaptcha.createImage(verificationCode);
            ImageIO.write(challenge, "jpg", imgOutputStream);
        } catch (IllegalArgumentException e) {
            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        captchaOutputStream = imgOutputStream.toByteArray();
        httpServletResponse.setHeader("Cache-Control", "no-store");
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setDateHeader("Expires", 0);
        httpServletResponse.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream = httpServletResponse.getOutputStream();
        responseOutputStream.write(captchaOutputStream);
        responseOutputStream.flush();
    }
}
