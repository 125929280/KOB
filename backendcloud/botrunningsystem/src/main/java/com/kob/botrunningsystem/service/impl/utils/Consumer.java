package com.kob.botrunningsystem.service.impl.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kob.botrunningsystem.utils.StringUtil;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.joor.Reflect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.function.Supplier;

@Component
public class Consumer extends Thread {
    private Bot bot;
    private static RestTemplate restTemplate;
    private final static String receiveBotMoveUrl = "http://127.0.0.1:3000/pk/receive/bot/move/";

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        Consumer.restTemplate = restTemplate;
    }

    @KafkaListener(topics = {"addBot"})
    public void handleMessage(ConsumerRecord record) {
        Consumer consumer = new Consumer();
        Bot bot = JSONObject.parseObject(record.value().toString(), Bot.class);
        consumer.startTimeout(2000L, bot);
    }

    public void startTimeout(long timeout, Bot bot) {
        this.bot = bot;
        this.start();

        try {
            this.join(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            this.interrupt();
        }
    }

    private String addUid(String code, String uid) {
        int k = code.indexOf(" implements java.util.function.Supplier<Integer>");
        return code.substring(0, k) + uid + code.substring(k);
    }

    @Override
    public void run() {
        String uid = StringUtil.generateUUID().substring(0, 8);
        Supplier<Integer> botInterface = Reflect.compile("com.kob.botrunningsystem.utils.Bot" + uid, addUid(bot.getBotCode(), uid)).create().get();
        File file = new File("input.txt");
        try (PrintWriter fout = new PrintWriter(file)){
            fout.println(bot.getInput());
            fout.flush();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Integer direction = botInterface.get();
        System.out.println("direction: " + bot.getUserId() + " " + direction);
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", bot.getUserId().toString());
        data.add("direction", direction.toString());
        data.add("opponent_id", bot.getOpponentId().toString());
        restTemplate.postForObject(receiveBotMoveUrl, data, String.class);
    }
}
