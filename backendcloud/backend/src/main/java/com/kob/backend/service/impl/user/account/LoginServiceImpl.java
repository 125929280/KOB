package com.kob.backend.service.impl.user.account;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.account.LoginService;
import com.kob.backend.utils.JwtUtil;
import com.kob.backend.utils.RedisUtil;
import com.kob.backend.utils.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private DefaultKaptcha defaultKaptcha;

    /**
     * 使用authenticationManager.authenticate()进行用户认证
     * 如果认证失败，给出相应提示
     * 如果认证成功，使用userId生成jwt并返回
     * 将完成的用户信息存入redis，[userId, info]
     *
     * @param data
     * @return
     */
    @Override
    public Map<String, String> getToken(Map<String, String> data) {
        String username = data.get("username");
        String password = data.get("password");
        String ip = WebUtil.getIpAddress();
        String actualVerificationCode = (String) redisTemplate.opsForValue().get(RedisUtil.getVerificationKey(ip));
        System.out.println("[" + RedisUtil.getVerificationKey(ip) + ", " + actualVerificationCode + "]");
        String verificationCode = data.get("verificationCode");
        System.out.println(username + " " + password + " " + actualVerificationCode + " " + verificationCode);
        Map<String, String> map = new HashMap<>();

        if (StringUtils.isBlank(actualVerificationCode) || StringUtils.isBlank(verificationCode) || !actualVerificationCode.equalsIgnoreCase(verificationCode)) {
            map.put("error_message", "验证码错误");
            return map;
        }
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("用户名密码错误");
        }
        UserDetailsImpl principal = (UserDetailsImpl) authenticate.getPrincipal();
        String userId = principal.getUser().getId().toString();

        String jwt = JwtUtil.createJWT(userId);
        map.put("token", jwt);

        redisTemplate.opsForValue().set(RedisUtil.getLoginKey(userId), principal, RedisUtil.LOGIN_TTL, TimeUnit.MINUTES);
        map.put("error_message", "success");
        return map;
    }

    @Override
    public void getVerificationCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        byte[] captchaOutputStream = null;
        ByteArrayOutputStream imgOutputStream = new ByteArrayOutputStream();
        try {
            // 生成验证码字符串并保存到redis中
            String code = defaultKaptcha.createText();
            String ip = WebUtil.getIpAddress();
            redisTemplate.opsForValue().set(RedisUtil.getVerificationKey(ip), code, 60, TimeUnit.SECONDS);
            System.out.println("[" + RedisUtil.getVerificationKey(ip) + ", " + code + "]");
            BufferedImage challenge = defaultKaptcha.createImage(code);
            ImageIO.write(challenge, "jpg", imgOutputStream);
        } catch (IllegalArgumentException | IOException e) {
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

    @Override
    public Map<String, String> logout() {
        Map<String, String> map = new HashMap<>();
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl principal = (UserDetailsImpl) usernamePasswordAuthenticationToken.getPrincipal();
        String userId = principal.getUser().getId().toString();
        redisTemplate.delete(RedisUtil.getLoginKey(userId));
        map.put("error_message", "success");
        return map;
    }
}
