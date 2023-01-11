package com.kob.backend.service.impl.discuss;

import com.kob.backend.dict.DiscussType;
import com.kob.backend.mapper.DiscussMapper;
import com.kob.backend.pojo.Discuss;
import com.kob.backend.pojo.User;
import com.kob.backend.service.discuss.AddDiscussService;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AddDiscussServiceImpl implements AddDiscussService {
    @Autowired
    private DiscussMapper discussMapper;

    @Override
    public Map<String, String> add(Map<String, String> data) {
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        User user = principal.getUser();


        String title = data.get("title");
        DiscussType type = DiscussType.valueOf(data.get("type"));
        String content = data.get("content");

        Map<String, String> map = new HashMap<>();
        if (title == null || title.length() == 0) {
            map.put("error_message", "标题不能为空");
            return map;
        }
        if (title.length() > 100) {
            map.put("error_message", "标题长度不能大于100");
            return map;
        }
        if (content == null || content.length() == 0) {
            map.put("error_message", "内容不能为空");
            return map;
        }
        if (content.length() > 10000) {
            map.put("error_message", "内容长度不能大于10000");
            return map;
        }

        Date now = new Date();
        Discuss discuss = new Discuss(null, user.getId(), title, type, content, now, now);

        discussMapper.insert(discuss);
        map.put("error_message", "success");
        return map;
    }
}
