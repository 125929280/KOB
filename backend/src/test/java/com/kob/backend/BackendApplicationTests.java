package com.kob.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class BackendApplicationTests {

    @Test
    void contextLoads() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.encode("19990728"));
        System.out.println(passwordEncoder.matches("19990728", "$2a$10$c2RbPcw2LkHovza/lyoWBOvoIumMgXnsvTaEydRhjtCWIXHy0kds."));
    }

}
