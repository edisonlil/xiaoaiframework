package com.xiaoaiframework.spring.redis.kit;


import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public abstract class RedisKit {

    protected RedisTemplate<String, Object> redisTemplate;

    /**
     * 一次性删除多少key
     */
    protected final Integer KEY_DEL_BATCH = 5000;

    /**
     * 一次性扫描多少行数
     */
    protected final Integer SCAN_COUNT = 1000;

    protected RedisKit(RedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    /**
     * 以秒为单位获取Key失效时间
     * @param key
     * @return
     */
    protected Long getExpire(String key){
        return this.redisTemplate.getExpire(key);
    }


    /**
     * 根据时间单位获取Key失效时间
     * @param key
     * @param timeUnit
     * @return
     */
    protected Long getExpire(String key, TimeUnit timeUnit){
        return this.redisTemplate.getExpire(key,timeUnit);
    }

    /**
     * scan 实现
     *
     * @param pattern  表达式
     * @param consumer 对迭代到的key进行操作
     */
    private void scan(String pattern, Consumer<byte[]> consumer) {

        this.redisTemplate.execute((RedisConnection connection) -> {
            try (Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions().count(SCAN_COUNT).match(pattern).build())) {
                cursor.forEachRemaining(consumer);
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * 获取符合条件的key
     *
     * @param pattern 表达式
     * @return
     */
    public List<String> keys(String pattern) {
        List<String> keys = new ArrayList<>();
        this.scan(pattern, item -> {
            //符合条件的key
            String key = new String(item, StandardCharsets.UTF_8);
            keys.add(key);

        });
        return keys;
    }


    /**
     * 指定缓存失效时间(默认秒)
     * @param key 键
     */
    public Boolean expire(String key, long time) {
        if (time > 0) {
            return redisTemplate.expire(key, time, TimeUnit.SECONDS);
        }
        return false;
    }

    /**
     * 指定缓存失效时间
     * @param key 键
     */
    public Boolean expire(String key, long time, TimeUnit timeUnit) {
        if (time > 0) {
            return redisTemplate.expire(key, time,timeUnit);
        }
        return false;
    }


    /**
     * 查看是否有存在的key
     *
     * @param key
     * @return
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 删除指定key
     *
     * @param key
     * @return
     */
    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 批量删除指定的keys
     *
     * @param keys
     * @return
     */
    public Long deleteBatch(List keys) {
        return redisTemplate.delete(keys);
    }




}
