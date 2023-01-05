package com.kob.backend.service.impl.user.account;

import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.account.LoginService;
import com.kob.backend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public Map<String, String> getToken(Map<String, String> data) {
        String username = data.get("username");
        String password = data.get("password");
        String actualVerificationCode = data.get("actualVerificationCode");
        String verificationCode = data.get("verificationCode");
        System.out.println(username + " " + password + " " + actualVerificationCode + " " + verificationCode);
        Map<String, String> map = new HashMap<>();
        if(verificationCode == null || verificationCode.length() == 0) {
            map.put("error_message", "验证码不能为空");
            return map;
        }
        if(!actualVerificationCode.equals(verificationCode.toUpperCase())) {
            map.put("error_message", "验证码错误");
            return map;
        }
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        UserDetailsImpl principal = (UserDetailsImpl) authenticate.getPrincipal();
        User user = principal.getUser();

        String jwt = JwtUtil.createJWT(user.getId().toString());
        map.put("error_message", "success");
        map.put("token", jwt);
        return map;
    }
}
