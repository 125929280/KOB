package com.kob.backend.service.impl.discuss;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.mapper.DiscussMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.Discuss;
import com.kob.backend.pojo.User;
import com.kob.backend.service.discuss.GetDiscussListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetDiscussListServiceImpl implements GetDiscussListService {
    @Autowired
    private DiscussMapper discussMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public JSONObject getList(Integer page) {
        IPage<Discuss> discussIPage = new Page<>(page, 10);
        QueryWrapper<Discuss> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        List<Discuss> discusses = discussMapper.selectPage(discussIPage, queryWrapper).getRecords();

        JSONObject resp = new JSONObject();
        List<JSONObject> items = new ArrayList<>();
        for (Discuss discuss : discusses) {
            JSONObject item = new JSONObject();
            User user = userMapper.selectById(discuss.getUserId());
            item.put("photo", user.getPhoto());
            item.put("username", user.getUsername());
            item.put("discuss", discuss);
            items.add(item);
        }
        resp.put("discusses", items);
        resp.put("discusses_count", discussMapper.selectCount(null));
        return resp;
    }
}