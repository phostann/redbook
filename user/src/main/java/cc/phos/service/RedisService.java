package cc.phos.service;

import java.util.concurrent.TimeUnit;

public interface RedisService {
    /**
     * 缓存值
     *
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    <T> boolean cacheValue(String key, T value);

    /**
     * 缓存值，并且设置过期时间
     *
     * @param key
     * @param value
     * @param time
     * @param timeUnit
     * @param <T>
     * @return
     */
    <T> boolean cacheValue(String key, T value, long time, TimeUnit timeUnit);

    /**
     * 获取缓存的值
     *
     * @param key
     * @param <T>
     * @return
     */
    <T> T getValue(String key);

    /**
     * 删除缓存
     *
     * @param key
     * @return
     */
    boolean removeValue(String key);
}
