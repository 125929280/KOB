package com.kob.backend.service.impl.user.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.dict.DiscussType;
import com.kob.backend.mapper.DiscussMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.Discuss;
import com.kob.backend.pojo.User;
import com.kob.backend.service.user.account.RegisterService;
import com.kob.backend.utils.MailUtil;
import com.kob.backend.utils.RedisUtil;
import com.kob.backend.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MailUtil mailUtil;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private DiscussMapper discussMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    @Transactional
    public Map<String, String> register(Map<String, String> data) {
        String username = data.get("username");
        String password = data.get("password");
        String confirmedPassword = data.get("confirmedPassword");
        String email = data.get("email");
        String activationCode = data.get("activationCode");
        String actualActivationCode = (String) redisTemplate.opsForValue().get(RedisUtil.getActivationKey(username));

        Map<String, String> map = new HashMap<>();
        if (username == null) {
            map.put("error_message", "用户名不能为空");
            return map;
        }
        if (password == null || confirmedPassword == null) {
            map.put("error_message", "密码不能为空");
            return map;
        }
        if (email == null) {
            map.put("error_message", "邮箱不能为空");
            return map;
        }
        if (activationCode == null) {
            map.put("error_message", "验证码不能为空");
            return map;
        }
        username = username.trim();
        if (username.length() == 0) {
            map.put("error_message", "用户名不能为空");
            return map;
        }
        if (password.length() == 0 || confirmedPassword.length() == 0) {
            map.put("error_message", "密码不能为空");
            return map;
        }
        if (email.length() == 0) {
            map.put("error_message", "邮箱不能为空");
            return map;
        }
        if (activationCode.length() == 0) {
            map.put("error_message", "验证码不能为空");
            return map;
        }
        if (username.length() > 100) {
            map.put("error_message", "用户名长度不能大于100");
            return map;
        }
        if (password.length() > 100 || confirmedPassword.length() > 100) {
            map.put("error_message", "密码长度不能大于100");
            return map;
        }
        if (!password.equals(confirmedPassword)) {
            map.put("error_message", "两次输入的密码不一致");
            return map;
        }
        if (!activationCode.equals(actualActivationCode)) {
            map.put("error_message", "验证码错误");
            return map;
        }

        List<User> users = userMapper.selectList(new QueryWrapper<User>().eq("username", username));
        if (!users.isEmpty()) {
            map.put("error_message", "用户名已存在");
            return map;
        }

        users = userMapper.selectList(new QueryWrapper<User>().eq("email", email));
        if (!users.isEmpty()) {
            map.put("error_message", "该邮箱已被注册");
            return map;
        }

        String encodedPassword = passwordEncoder.encode(password);
        String photo = "https://cdn.acwing.com/media/user/profile/photo/177468_lg_32587ffaa8.jpg";
        Date now = new Date();
        User user = new User(null, username, encodedPassword, photo, 1500, email, now, now);
        userMapper.insert(user);

        Discuss discuss = new Discuss(null, user.getId(), "新人报道", DiscussType.CHAT, "Hello", now, now, 1500, 0);
        discussMapper.insert(discuss);
        map.put("error_message", "success");
        return map;
    }

    @Override
    public Map<String, String> sendActivationCode(Map<String, String> data) {
        String username = data.get("username");
        String email = data.get("email");
        String activationCode = StringUtil.generateUUID();
        Map<String, String> map = new HashMap<>();

        if (username == null) {
            map.put("error_message", "用户名不能为空");
            return map;
        }
        if (email == null) {
            map.put("error_message", "邮箱不能为空");
            return map;
        }
        username = username.trim();
        if (username.length() == 0) {
            map.put("error_message", "用户名不能为空");
            return map;
        }
        if (email.length() == 0) {
            map.put("error_message", "邮箱不能为空");
            return map;
        }

        redisTemplate.opsForValue().set(RedisUtil.getActivationKey(username), activationCode, 60, TimeUnit.SECONDS);

        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("activationCode", activationCode);
        String content = templateEngine.process("/mail/activation", context);
        mailUtil.sendMail(email, "激活账号", content);
        map.put("error_message", "success");
        return map;
    }
}
