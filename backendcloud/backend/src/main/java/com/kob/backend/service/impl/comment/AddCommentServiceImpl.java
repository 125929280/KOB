package com.kob.backend.service.impl.comment;

import com.kob.backend.mapper.CommentMapper;
import com.kob.backend.pojo.Comment;
import com.kob.backend.pojo.User;
import com.kob.backend.service.comment.AddCommentService;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AddCommentServiceImpl implements AddCommentService {
    @Autowired
    private CommentMapper commentMapper;

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
}
