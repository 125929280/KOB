package com.kob.backend.schedule;

import com.kob.backend.mapper.DiscussMapper;
import com.kob.backend.pojo.Discuss;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

public class DiscussSchedule {
    @Autowired
    private DiscussMapper discussMapper;

    @Scheduled(cron = "0 0 0 * * ? *")
    public void refreshDiscussScore() {
        List<Discuss> discusses = discussMapper.selectList(null);
        discusses.forEach(e->{
            e.setScore(1500);
            discussMapper.updateById(e);
        });
    }
}
