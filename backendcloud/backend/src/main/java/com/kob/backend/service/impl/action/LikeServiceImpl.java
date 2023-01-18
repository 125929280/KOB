package com.kob.backend.service.impl.action;

import cn.hutool.core.util.BooleanUtil;
import com.kob.backend.mapper.DiscussMapper;
import com.kob.backend.pojo.Discuss;
import com.kob.backend.pojo.User;
import com.kob.backend.service.action.LikeService;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LikeServiceImpl implements LikeService {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private DiscussMapper discussMapper;

    public Map<String, String> like(int discussId) {
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        User user = principal.getUser();

        Map<String, String> map = new HashMap<>();
        String likeKey = RedisUtil.getLikeKey(discussId);
        String userId = user.getId().toString();
        Boolean isMember = redisTemplate.opsForSet().isMember(likeKey, userId);
        Discuss discuss = discussMapper.selectById(discussId);
        if (BooleanUtil.isFalse(isMember)) {
            redisTemplate.opsForSet().add(likeKey, userId);
            discuss.setLikeCount(discuss.getLikeCount() + 1);
            discuss.setScore(discuss.getScore() + 10);
        } else {
            redisTemplate.opsForSet().remove(likeKey, userId);
            discuss.setLikeCount(discuss.getLikeCount() - 1);
            discuss.setScore(discuss.getScore() - 10);
        }
        discussMapper.updateById(discuss);
        map.put("error_message", "success");
        return map;
    }

    public long getLikesCount(int discussId) {
        String likeKey = RedisUtil.getLikeKey(discussId);
        return redisTemplate.opsForSet().size(likeKey);
    }

    public int getLikeStatus(int discussId) {
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        User user = principal.getUser();

        String likeKey = RedisUtil.getLikeKey(discussId);
        String userId = user.getId().toString();
        return BooleanUtil.isTrue(redisTemplate.opsForSet().isMember(likeKey, userId)) ? 1 : 0;
    }
}
