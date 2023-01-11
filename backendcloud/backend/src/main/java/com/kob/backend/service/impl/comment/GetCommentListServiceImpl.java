package com.kob.backend.service.impl.comment;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.mapper.CommentMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.Comment;
import com.kob.backend.pojo.User;
import com.kob.backend.service.comment.GetCommentListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetCommentListServiceImpl implements GetCommentListService {
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserMapper userMapper;

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
