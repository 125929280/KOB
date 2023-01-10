package com.kob.backend.service.impl.user.account;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.account.InfoService;
import com.kob.backend.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class InfoServiceImpl implements InfoService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Map<String, String> getInfo() {
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        User user = principal.getUser();

        Map<String, String> map = new HashMap<>();
        map.put("error_message", "success");
        map.put("id", user.getId().toString());
        map.put("username", user.getUsername());
        map.put("photo", user.getPhoto());
        return map;
    }

    @Override
    public Map<String, String> updatePassword(Map<String, String> data) {
        String oldPassword = data.get("oldPassword");
        String password = data.get("password");
        String confirmedPassword = data.get("confirmedPassword");
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        User user = principal.getUser();

        Map<String, String> map = new HashMap<>();
        if (oldPassword.length() == 0 || password.length() == 0 || confirmedPassword.length() == 0) {
            map.put("error_message", "密码不能为空");
            return map;
        }
        if(!passwordEncoder.matches(oldPassword, user.getPassword())) {
            map.put("error_message", "旧密码错误");
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

        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
        userMapper.updateById(user);
        map.put("error_message", "success");
        return map;
    }

    public Map<String, String> updatePhoto(MultipartFile data) {
        Map<String, String> map = new HashMap<>();
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        User user = principal.getUser();

        if(Objects.isNull(data)) {
            map.put("error_message", "请选择图片");
            return map;
        }
        String originalFilename = data.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.indexOf("."));
        if(StringUtils.isBlank(suffix)) {
            map.put("error_message", "文件格式错误");
            return map;
        }

        String filename = StringUtil.generateUUID() + suffix;
        File file = new File(System.getProperty("user.dir") + "/backend/src/main/resources/resources/" + filename);
        try {
            data.transferTo(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String url = "http://localhost:3000/" + filename;
        user.setPhoto(url);
        userMapper.updateById(user);
        map.put("error_message", "success");
        return map;
    }
}
