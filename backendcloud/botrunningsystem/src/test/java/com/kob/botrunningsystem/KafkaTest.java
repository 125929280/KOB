package com.kob.botrunningsystem;

import com.alibaba.fastjson.JSONObject;
import com.kob.botrunningsystem.service.impl.utils.Bot;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = BotRunningSystemApplication.class)
public class KafkaTest {
    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Test
    public void testKafka() {
        Bot bot = new Bot(1,"1","1",1);
        kafkaTemplate.send("zzz", JSONObject.toJSONString(bot));

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = {"addBot"})
    public void handleMessage(ConsumerRecord record) {
//        Bot bot = JSONObject.parseObject(record.value().toString(), Bot.class);
        System.out.println(record.value());
    }
}
