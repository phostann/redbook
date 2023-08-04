package cc.phos.service.impl;

import cc.phos.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public <T> boolean cacheValue(String key, T value) {
        try {
            ValueOperations<String, Object> ops = redisTemplate.opsForValue();
            ops.set(key, value);
            return true;
        } catch (Throwable e) {
            log.error("缓存[{}]失败, value[{}]", key, value, e);
            return false;
        }
    }

    @Override
    public <T> boolean cacheValue(String key, T value, long time, TimeUnit timeUnit) {
        try {
            ValueOperations<String, Object> ops = redisTemplate.opsForValue();
            ops.set(key, value);
            if (time > 0) {
                redisTemplate.expire(key, time, timeUnit);
            }
            return true;
        } catch (Throwable e) {
            log.error("缓存[{}]失败, value[{}]", key, value, e);
            return false;
        }
    }

    @Override
    public <T> T getValue(String key) {
        try {
            ValueOperations<String, Object> ops = redisTemplate.opsForValue();
            return (T) ops.get(key);
        } catch (Throwable e) {
            log.error("获取缓存失败key[" + key + ", error[" + e + "]");
            return null;
        }
    }

    @Override
    public boolean removeValue(String key) {
        try {
            redisTemplate.delete(key);
            return true;
        } catch (Throwable e) {
            log.error("获取缓存失败key[" + key + ", error[" + e + "]");
            return false;
        }
    }
}
