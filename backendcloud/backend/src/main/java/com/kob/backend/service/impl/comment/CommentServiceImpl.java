package com.kob.backend.service.impl.comment;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.mapper.CommentMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.Comment;
import com.kob.backend.pojo.User;
import com.kob.backend.service.comment.CommentService;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Map<String, String> add(Map<String, String> data) {
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        User user = principal.getUser();

        Integer discussId = Integer.valueOf(data.get("discuss_id"));
        String content = data.get("content");

        Map<String, String> map = new HashMap<>();
        if (content == null || content.length() == 0) {
            map.put("error_message", "内容不能为空");
            return map;
        }
        if (content.length() > 10000) {
            map.put("error_message", "内容长度不能大于10000");
            return map;
        }

        Date now = new Date();
        Comment comment = new Comment(null, user.getId(), discussId, content, now);

        commentMapper.insert(comment);
        map.put("error_message", "success");
        return map;
    }

    @Override
    public Map<String, String> remove(Map<String, String> data) {
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        User user = principal.getUser();

        Integer commentId = Integer.parseInt(data.get("comment_id"));
        Comment comment = commentMapper.selectById(commentId);

        Map<String, String> map = new HashMap<>();
        if (comment == null) {
            map.put("error_message", "Comment不存在或已被删除");
            return map;
        }
        if (!comment.getUserId().equals(user.getId())) {
            map.put("error_message", "没有权限删除该Comment");
            return map;
        }

        commentMapper.deleteById(commentId);
        map.put("error_message", "success");
        return map;
    }

    @Override
    public JSONObject getList(Integer page, Integer discussId) {
        IPage<Comment> discussIPage = new Page<>(page, 10);
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("discuss_id", discussId);
        List<Comment> comments = commentMapper.selectPage(discussIPage, queryWrapper).getRecords();

        JSONObject resp = new JSONObject();
        List<JSONObject> items = new ArrayList<>();
        for (Comment comment : comments) {
            JSONObject item = new JSONObject();
            User user = userMapper.selectById(comment.getUserId());
            item.put("photo", user.getPhoto());
            item.put("username", user.getUsername());
            item.put("comment", comment);
            items.add(item);
        }
        resp.put("comments", items);
        resp.put("comments_count", commentMapper.selectCount(queryWrapper));
        return resp;
    }
}
