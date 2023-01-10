package com.kob.backend;

import com.kob.backend.utils.TrieUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TrieTest {
    @Autowired
    private TrieUtil trieUtil;
    @Test
    public void testTrie() {
        System.out.println(trieUtil.filter("这里可以⭐赌⭐️博⭐"));
    }
}
