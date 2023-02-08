package com.kob.botrunningsystem.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kob.botrunningsystem.service.BotRunningService;
import com.kob.botrunningsystem.service.impl.utils.Bot;
import com.kob.botrunningsystem.service.impl.utils.BotPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class BotRunningServiceImpl implements BotRunningService {
    public final static BotPool botpool = new BotPool();

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Override
    public String addBot(Integer userId, String botCode, String input, Integer opponentId) {
        System.out.println("add bot: " + userId + " " + opponentId);
//        botpool.addBot(userId, botCode, input, opponentId);
        Bot bot = new Bot(userId, botCode, input, opponentId);
        kafkaTemplate.send("addBot", JSONObject.toJSONString(bot));
        return "add bot success";
    }
}
