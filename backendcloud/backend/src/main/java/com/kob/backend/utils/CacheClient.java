package com.kob.backend.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Component
public class CacheClient {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public void set(String key, Object value, Long time, TimeUnit unit) {
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(value), time, unit);
    }

    public void setWithLogicalExpire(String key, Object value, Long time, TimeUnit unit) {
        RedisData redisData = new RedisData(LocalDateTime.now().plusSeconds(unit.toSeconds(time)), value);
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(redisData));
    }

    public <T> T get(String key, Class<T> type) {
        return JSONUtil.toBean(stringRedisTemplate.opsForValue().get(key), type);
    }

    public <T> T getWithPassThrough(String key, Class<T> type, Function<String, T> dbFallback, Long time, TimeUnit unit) {
        String json = stringRedisTemplate.opsForValue().get(key);

        if (StrUtil.isNotBlank(json)) {
            return JSONUtil.toBean(json, type);
        }
        if (ObjectUtil.isNotNull(json)) {
            return null;
        }
        T t = dbFallback.apply(key);
        if (ObjectUtil.isNull(t)) {
            stringRedisTemplate.opsForValue().set(key, "", time, unit);
            return null;
        }
        this.set(key, t, time, unit);
        return t;
    }

    public void delete(String key) {
        stringRedisTemplate.delete(key);
    }

    public void expire(String key, Long time, TimeUnit unit) {
        stringRedisTemplate.expire(key, time, unit);
    }
}
