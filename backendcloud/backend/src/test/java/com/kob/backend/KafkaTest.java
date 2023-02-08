package com.kob.backend;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = BackendApplication.class)
public class KafkaTest {
    @Autowired
    private KafkaTemplate kafkaTemplate;
    @Test
    public void testKafka() {
//        producer.sendMessage("test", "hello");
//        producer.sendMessage("test", "world");
        kafkaTemplate.send("test", "world");
        kafkaTemplate.send("test", "world");

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = {"test"})
    public void handleMessage(ConsumerRecord record) {
        System.out.println(record.value());
    }
}

@Component
class Producer {
    @Autowired
    private KafkaTemplate kafkaTemplate;

    public void sendMessage(String topic, String content) {
        kafkaTemplate.send(topic, content);
    }
}

//@Component
//class Consumer {
//    @KafkaListener(topics = {"addBot"})
//    public void handleMessage(ConsumerRecord record) {
//        System.out.println(record.value());
//    }
//}
