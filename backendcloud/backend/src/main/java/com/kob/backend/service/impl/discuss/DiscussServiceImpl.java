package com.kob.backend.service.impl.discuss;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.dict.DiscussType;
import com.kob.backend.mapper.DiscussMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.Discuss;
import com.kob.backend.pojo.User;
import com.kob.backend.service.action.LikeService;
import com.kob.backend.service.discuss.DiscussService;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DiscussServiceImpl implements DiscussService {
    @Autowired
    private DiscussMapper discussMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LikeService likeService;

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
        Discuss discuss = new Discuss(null, user.getId(), title, type, content, now, now, 1500, 0);

        discussMapper.insert(discuss);
        map.put("error_message", "success");
        return map;
    }

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

    @Override
    public Map<String, String> update(Map<String, String> data) {
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        User user = principal.getUser();

        Integer discussId = Integer.parseInt(data.get("discuss_id"));
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
            map.put("error_message", "代码不能为空");
            return map;
        }
        if (content.length() > 10000) {
            map.put("error_message", "代码长度不能大于10000");
            return map;
        }

        Discuss discuss = discussMapper.selectById(discussId);
        if (discuss == null) {
            map.put("error_message", "Discuss不存在或已被删除");
            return map;
        }
        if (!discuss.getUserId().equals(user.getId())) {
            map.put("error_message", "没有权限修改该Discuss");
            return map;
        }

        Discuss new_discuss = new Discuss(discuss.getId(), user.getId(), title, type, content, discuss.getCreateTime(), new Date(), 1500, 0);
        discussMapper.updateById(new_discuss);
        map.put("error_message", "success");
        return map;
    }

    @Override
    public JSONObject getList(Integer page) {
        IPage<Discuss> discussIPage = new Page<>(page, 10);
        QueryWrapper<Discuss> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("score");
        List<Discuss> discusses = discussMapper.selectPage(discussIPage, queryWrapper).getRecords();

        JSONObject resp = new JSONObject();
        List<JSONObject> items = new ArrayList<>();
        for (Discuss discuss : discusses) {
            JSONObject item = new JSONObject();
            User user = userMapper.selectById(discuss.getUserId());
            item.put("photo", user.getPhoto());
            item.put("username", user.getUsername());
            item.put("type", discuss.getType().getDesc());
            item.put("discuss", discuss);
            item.put("isLiked", likeService.getLikeStatus(discuss.getId()));
            items.add(item);
        }
        resp.put("discusses", items);
        resp.put("discusses_count", discussMapper.selectCount(null));
        return resp;
    }

    @Override
    public JSONObject get(Integer discussId) {
        QueryWrapper<Discuss> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", discussId);
        Discuss discuss = discussMapper.selectOne(queryWrapper);
        JSONObject resp = new JSONObject();
        User user = userMapper.selectById(discuss.getUserId());
        resp.put("photo", user.getPhoto());
        resp.put("username", user.getUsername());
        resp.put("type", discuss.getType().getDesc());
        resp.put("discuss", discuss);
        resp.put("isLiked", likeService.getLikeStatus(discuss.getId()));
        return resp;
    }
}
