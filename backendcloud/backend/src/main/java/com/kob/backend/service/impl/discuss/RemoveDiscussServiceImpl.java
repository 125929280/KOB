package com.kob.backend.service.impl.discuss;

import com.kob.backend.mapper.DiscussMapper;
import com.kob.backend.pojo.Discuss;
import com.kob.backend.pojo.User;
import com.kob.backend.service.discuss.RemoveDiscussService;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RemoveDiscussServiceImpl implements RemoveDiscussService {
    @Autowired
    private DiscussMapper discussMapper;

    @Override
    public Map<String, String> remove(Map<String, String> data) {
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        User user = principal.getUser();

        Integer discussId = Integer.parseInt(data.get("discuss_id"));
        Discuss discuss = discussMapper.selectById(discussId);

        Map<String, String> map = new HashMap<>();
        if (discuss == null) {
            map.put("error_message", "Discuss不存在或已被删除");
            return map;
        }
        if (!discuss.getUserId().equals(user.getId())) {
            map.put("error_message", "没有权限删除该Discuss");
            return map;
        }

        discussMapper.deleteById(discussId);
        map.put("error_message", "success");
        return map;
    }
}
