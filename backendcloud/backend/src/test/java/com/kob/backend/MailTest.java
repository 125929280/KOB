package com.kob.backend;

import com.kob.backend.utils.MailUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = BackendApplication.class)
public class MailTest {
    @Autowired
    private MailUtil mailUtil;

    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void testMailUtil() {
        mailUtil.sendMail("125929280@qq.com", "test", "hello");
    }

    @Test
    public void testHtmlEmail() {
        Context context = new Context();
        context.setVariable("email", "125929280@qq.com");
        context.setVariable("url", "http://localhost:8081");
        String content = templateEngine.process("/mail/activation", context);
        System.out.println(content);
        mailUtil.sendMail("125929280@qq.com", "html", content);
    }
}
