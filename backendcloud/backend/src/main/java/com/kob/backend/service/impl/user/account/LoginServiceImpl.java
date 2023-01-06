package com.kob.backend.service.impl.user.account;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.account.LoginService;
import com.kob.backend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisTemplate redisTemplate;

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
        String actualVerificationCode = data.get("actualVerificationCode");
        String verificationCode = data.get("verificationCode");
        System.out.println(username + " " + password + " " + actualVerificationCode + " " + verificationCode);
        Map<String, String> map = new HashMap<>();
        if (StringUtils.isBlank(verificationCode) || !actualVerificationCode.equalsIgnoreCase(verificationCode)) {
            map.put("error_message", "验证码错误");
            return map;
        }
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        try {
            Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            UserDetailsImpl principal = (UserDetailsImpl) authenticate.getPrincipal();
            String userId = principal.getUser().getId().toString();

            String jwt = JwtUtil.createJWT(userId);
            map.put("token", jwt);

            redisTemplate.opsForValue().set("login:" + userId, principal);
        } catch (Exception e) {
            map.put("error_message", "账户名密码错误");
            return map;
        }
        map.put("error_message", "success");
        return map;
    }

    @Override
    public Map<String, String> logout() {
        Map<String, String> map = new HashMap<>();
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl principal = (UserDetailsImpl) usernamePasswordAuthenticationToken.getPrincipal();
        String userId = principal.getUser().getId().toString();

        redisTemplate.delete("login:" + userId);
        map.put("error_message", "success");
        return map;
    }
}
