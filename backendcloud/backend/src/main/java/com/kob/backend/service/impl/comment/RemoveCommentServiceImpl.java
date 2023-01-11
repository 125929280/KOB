package com.kob.backend.service.impl.comment;

import com.kob.backend.mapper.CommentMapper;
import com.kob.backend.pojo.Comment;
import com.kob.backend.pojo.Discuss;
import com.kob.backend.pojo.User;
import com.kob.backend.service.comment.RemoveCommentService;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RemoveCommentServiceImpl implements RemoveCommentService {
    @Autowired
    private CommentMapper commentMapper;
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
}
